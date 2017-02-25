package com.typokign.fps.engine.components;

import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class SpotLight extends PointLight {
	private float cutoff;

	public SpotLight(Color color, float intensity, Attenuation attenuation, float cutoff) {
		super(color, intensity, attenuation);
		this.cutoff = cutoff;
		setShader(new Shader("forwardSpot"));
	}

	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().getForward();
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
