package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 1/28/2017.
 */

public class BasicShader extends Shader {

	// Singleton
	private static final BasicShader instance = new BasicShader();

	public static BasicShader getInstance() {
		return instance;
	}

	private BasicShader() {
		super();

		addVertexShaderFromFile("basicVertex.vs");
		addFragmentShaderFromFile("basicFragment.fs");
		compileShader();

		addUniform("transform");
		addUniform("color");
	}

	@Override
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		if (material.getTexture() != null) {
			material.getTexture().bind();
		} else {
			RenderUtil.unbindTextures();
		}

		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
}
