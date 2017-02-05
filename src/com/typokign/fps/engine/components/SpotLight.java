package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.ForwardSpot;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class SpotLight extends PointLight {
	private Vector3f direction;
	private float cutoff;

	public SpotLight(Vector3f color, float intensity, float exponent, float linear, float constant, Vector3f position, float range, Vector3f direction, float cutoff) {
		super(color, intensity, exponent, linear, constant, position, range);
		this.direction = direction.normalized();
		this.cutoff = cutoff;
		setShader(ForwardSpot.getInstance());
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction.normalized();
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
