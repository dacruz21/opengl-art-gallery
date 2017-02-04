#version 330

const int MAX_POINT_LIGHTS = 4; // max point lights PER PIXEL, not per level. this is REALLY expensive to increase
const int MAX_SPOT_LIGHTS = 4; // same as above

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPosition0;

out vec4 fragColor;

uniform vec3 baseColor;
uniform vec3 cameraPosition;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform float specularIntensity;
uniform float specularExponent;

struct BaseLight {
	vec3 color;
	float intensity;
};

struct DirectionalLight {
	BaseLight base;
	vec3 direction;
};

struct Attenuation { // attenuation formula is a simple quadratic in the form of (exponent * distance^2 + linear * distance + constant) (ex^2 + lx + c)
	float constant;
	float linear;
	float exponent;
};

struct PointLight {
	BaseLight base;
	Attenuation atten;
	vec3 position;
	float range;
};

struct SpotLight {
	PointLight pointLight;
	vec3 direction;
	float cutoff;
};

uniform DirectionalLight directionalLight; // max 1
uniform PointLight pointLights[MAX_POINT_LIGHTS]; // array of max point lights
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal) {
	float diffuseFactor = dot(normal, -direction);

	vec4 diffuseColor = vec4(0,0,0,0);
	vec4 specularColor = vec4(0,0,0,0);

	if (diffuseFactor > 0) {
		diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;

		vec3 directionToCamera = normalize(cameraPosition - worldPosition0);
		vec3 reflectDirection = normalize(reflect(direction, normal));

		float specularFactor = dot(directionToCamera, reflectDirection);
		specularFactor = pow(specularFactor, specularExponent);

		if (specularFactor > 0) {
			specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
		}
	}

	return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal) {
	return calcLight(directionalLight.base, -directionalLight.direction, normal);
}

vec4 calcPointLight(PointLight pointLight, vec3 normal) {
	vec3 lightDirection = worldPosition0 - pointLight.position;
	float distanceToPoint = length(lightDirection); // put this in a var so we can normalize direction

	if (distanceToPoint > pointLight.range) {
		return vec4(0,0,0,0);
	}

	lightDirection = normalize(lightDirection);

	// standard light calculation for a light, direction, and normal
	vec4 color = calcLight(pointLight.base, lightDirection, normal);

	// attenuate the color
	float attenuation = pointLight.atten.constant +
						pointLight.atten.linear * distanceToPoint +
						pointLight.atten.exponent * distanceToPoint * distanceToPoint +
						0.0001; // attenuation could actually equal zero, causing a div by 0 error, so add a really small float to the atten

	return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal) {
	vec3 lightDirection = normalize(worldPosition0 - spotLight.pointLight.position);
	float spotFactor = dot(lightDirection, spotLight.direction);

	vec4 color = vec4(0,0,0,0);

	if (spotFactor > spotLight.cutoff) {
		color = calcPointLight(spotLight.pointLight, normal) *
				(1.0 - (1.0 - spotFactor) / (1.0 - spotLight.cutoff));
	}

	return color;
}

void main() {
	vec4 totalLight = vec4(ambientLight, 1);
	vec4 color = vec4(baseColor, 1);
	vec4 textureColor = texture(sampler, texCoord0.xy);

	// multiply all sources of color together

	if (textureColor != vec4(0,0,0,0))
		color *= textureColor;

	vec3 normal = normalize(normal0);

	totalLight += calcDirectionalLight(directionalLight, normal);

	for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
		if (pointLights[i].base.intensity > 0) {
			totalLight += calcPointLight(pointLights[i], normal); // remember pointLights is an array, so we need to loop thru it
		}
	}

	for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
    	if (spotLights[i].pointLight.base.intensity > 0) {
    		totalLight += calcSpotLight(spotLights[i], normal); // remember pointLights is an array, so we need to loop thru it
    	}
    }

	fragColor = color * totalLight;
}
