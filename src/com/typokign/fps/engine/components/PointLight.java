package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.ForwardPoint;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class PointLight extends BaseLight {
	private static final int COLOR_DEPTH = 256;

	private Attenuation attenuation;
	private float range;

	public PointLight(Color color, float intensity, Attenuation attenuation) {
		super(color, intensity);
		this.attenuation = attenuation;

		float a = attenuation.getQuadratic();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

		this.range = (float) ((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a)); // only do the + component of the quadratic equation, due to nature of our system, a and b are never negative

		setShader(ForwardPoint.getInstance());
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
}
