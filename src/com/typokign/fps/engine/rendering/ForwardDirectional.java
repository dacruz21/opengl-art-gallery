package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.DirectionalLight;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardDirectional extends Shader {
	// Singleton
	private static final ForwardDirectional instance = new ForwardDirectional();

	public static ForwardDirectional getInstance() {
		return instance;
	}

	private ForwardDirectional() {
		super("forwardDirectional");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		super.updateUniforms(transform, material, renderingEngine);
	}
}
