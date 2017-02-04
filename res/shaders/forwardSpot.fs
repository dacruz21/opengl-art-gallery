#version 120

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPosition0;

uniform vec3 cameraPosition;
uniform sampler2D diffuse;

uniform float specularIntensity;
uniform float specularExponent;

struct BaseLight {
	vec3 color;
	float intensity;
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

uniform SpotLight spotLight;

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
	gl_FragColor = texture2D(diffuse, texCoord0.xy) * calcSpotLight(spotLight, normalize(normal0));
}
