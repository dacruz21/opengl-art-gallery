#version 120
#include <lighting.glh>

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPosition0;

uniform sampler2D R_diffuse;
uniform DirectionalLight R_directionalLight;

void main() {
	gl_FragColor = texture2D(R_diffuse, texCoord0.xy) * calcDirectionalLight(R_directionalLight, normalize(normal0), worldPosition0);
}
