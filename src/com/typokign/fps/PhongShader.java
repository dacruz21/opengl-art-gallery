package com.typokign.fps;

/**
 * Created by Typo Kign on 1/28/2017.
 */

public class PhongShader extends Shader {

	private static final int MAX_POINT_LIGHTS = 4;

	// Singleton so we don't send the shader to the GPU multiple times
	private static final PhongShader instance = new PhongShader();

	public static PhongShader getInstance() {
		return instance;
	}

	private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 0), new Vector3f(0,0,0));

	private static PointLight[] pointLights = new PointLight[] {};

	private PhongShader() {
		super();

		addVertexShader(ResourceLoader.loadShader("phongVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.fs"));
		compileShader();

		addUniform("transform");
		addUniform("transformProjected");
		addUniform("baseColor");
		addUniform("ambientLight");

		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("cameraPos");

		addUniform("directionalLight.base.color"); // directionalLight is a struct, and Java doesn't have structs. thus we access the components of the struct as separate uniforms
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");

		for (int i = 0; i < MAX_POINT_LIGHTS; i++) { // can't directly pass arrays to GLSL, so we need to iterate through each point light
			addUniform("pointLights[" + i + "].base.color");
			addUniform("pointLights[" + i + "].base.intensity");
			addUniform("pointLights[" + i + "].atten.constant");
			addUniform("pointLights[" + i + "].atten.linear");
			addUniform("pointLights[" + i + "].atten.exponent");
			addUniform("pointLights[" + i + "].position");
		}
	}

	@Override
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null) {
			material.getTexture().bind();
		} else {
			RenderUtil.unbindTextures();
		}

		setUniform("transformProjected", projectedMatrix);
		setUniform("transform", worldMatrix);
		setUniform("baseColor", material.getColor());

		setUniform("ambientLight", ambientLight);
		setUniform("directionalLight", directionalLight); // this uses the overloaded methods below, because structs are a huge pain

		for (int i = 0; i < pointLights.length; i++)
			setUniform("pointLights[" + i + "]", pointLights[i]);

		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());

		setUniform("cameraPos", Transform.getCamera().getPosition());
	}

	public static Vector3f getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}

	public static DirectionalLight getDirectionalLight() {
		return directionalLight;
	}

	public static void setDirectionalLight(DirectionalLight directionalLight) {
		PhongShader.directionalLight = directionalLight;
	}

	public static void setPointLights(PointLight[] pointLights) {
		if (pointLights.length > MAX_POINT_LIGHTS) {
			System.err.println("Error: Too many point lights, maximum " + MAX_POINT_LIGHTS + ", entered " + pointLights.length);
			new Exception().printStackTrace();
			System.exit(1);
		}

		PhongShader.pointLights = pointLights;
	}

	// setting the struct uniform data
	public void setUniform(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniform(String uniformName, DirectionalLight directionalLight) {
		setUniform(uniformName + ".base", directionalLight.getBase());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniform(String uniformName, PointLight pointLight) {
		setUniform(uniformName + ".base", pointLight.getBase());
		setUniformf(uniformName + ".atten.constant", pointLight.getAtten().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAtten().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAtten().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
	}
}
