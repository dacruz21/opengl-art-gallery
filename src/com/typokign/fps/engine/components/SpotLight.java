package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.ForwardSpot;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class SpotLight extends PointLight {
	private Vector3f direction;
	private float cutoff;

	public SpotLight(Color color, float intensity, Attenuation attenuation, Vector3f direction, float cutoff) {
		super(color, intensity, attenuation);
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
		this.direction = direction;
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
