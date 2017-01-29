package com.typokign.fps;

/**
 * Created by Typo Kign on 1/28/2017.
 */

public class PhongShader extends Shader {

	// Singleton
	private static final PhongShader instance = new PhongShader();

	public static PhongShader getInstance() {
		return instance;
	}

	private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 0), new Vector3f(0,0,0));

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

	// setting the struct uniform data
	public void setUniform(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniform(String uniformName, DirectionalLight directionalLight) {
		setUniform(uniformName + ".base", directionalLight.getBase());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
}
