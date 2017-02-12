package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardAmbient extends Shader {
	// Singleton
	private static final ForwardAmbient instance = new ForwardAmbient();

	public static ForwardAmbient getInstance() {
		return instance;
	}

	private ForwardAmbient() {
		super();

		addVertexShaderFromFile("forwardAmbient.vs");
		addFragmentShaderFromFile("forwardAmbient.fs");

		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);

		compileShader();

		addUniform("MVP");
		addUniform("ambientIntensity");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		material.getTexture("diffuse").bind();

		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", renderingEngine.getAmbientLight());
	}
}
