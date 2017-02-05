package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Transform;
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
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);

		material.getTexture().bind();

		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
}
