package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.PointLight;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardPoint extends Shader {
	// Singleton
	private static final ForwardPoint instance = new ForwardPoint();

	public static ForwardPoint getInstance() {
		return instance;
	}

	private ForwardPoint() {
		super("forwardPoint");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		super.updateUniforms(transform, material, renderingEngine);
	}
}
