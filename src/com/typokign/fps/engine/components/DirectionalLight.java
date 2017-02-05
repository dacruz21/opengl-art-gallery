package com.typokign.fps.engine.components;

import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.ForwardDirectional;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class DirectionalLight extends BaseLight {
	private Vector3f direction;

	public DirectionalLight(Color color, float intensity, Vector3f direction) {
		super(color, intensity);
		this.direction = direction.normalized();

		setShader(ForwardDirectional.getInstance());
	}

	public Vector3f getDirection() {
		return direction.normalized();
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
}
