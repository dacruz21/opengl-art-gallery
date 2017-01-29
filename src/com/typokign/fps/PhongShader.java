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

	private static Vector3f ambientLight;

	private PhongShader() {
		super();

		addVertexShader(ResourceLoader.loadShader("phongVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.fs"));
		compileShader();

		addUniform("transform");
		addUniform("baseColor");
		addUniform("ambientLight");
	}

	@Override
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null) {
			material.getTexture().bind();
		} else {
			RenderUtil.unbindTextures();
		}

		setUniform("transform", projectedMatrix);
		setUniform("baseColor", material.getColor());
		setUniform("ambientLight", ambientLight);
	}

	public static Vector3f getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}
}
