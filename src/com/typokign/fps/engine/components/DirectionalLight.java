package com.typokign.fps.engine.components;

import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class DirectionalLight extends BaseLight {

	public DirectionalLight(Color color, float intensity) {
		super(color, intensity);

		setShader(new Shader("forwardDirectional"));
	}

	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().getForward();
	}
}
