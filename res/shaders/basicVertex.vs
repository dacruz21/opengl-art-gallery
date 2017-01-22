#version 330

out vec4 color;

layout (location = 0) in vec3 position;

void main() {
	color = vec4(clamp(position, 0.0, 1.0), 1.0);
	gl_Position = vec4(0.25 * position, 1.0);
}