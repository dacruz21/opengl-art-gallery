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
		super("forwardAmbient");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		super.updateUniforms(transform, material, renderingEngine);
	}
}
