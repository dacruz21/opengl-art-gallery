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

struct DirectionalLight {
	BaseLight base;
	vec3 direction;
};

uniform DirectionalLight directionalLight; // max 1

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

void main() {
	gl_FragColor = texture2D(diffuse, texCoord0.xy) * calcDirectionalLight(directionalLight, normalize(normal0));
}
