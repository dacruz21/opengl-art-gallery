package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.PointLight;
import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardSpot extends Shader {
	// Singleton
	private static final ForwardSpot instance = new ForwardSpot();

	public static ForwardSpot getInstance() {
		return instance;
	}

	private ForwardSpot() {
		super("forwardSpot");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		super.updateUniforms(transform, material, renderingEngine);
	}
}
