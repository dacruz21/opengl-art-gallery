package com.typokign.fps.engine.rendering;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class Attenuation {
	private float quadratic;
	private float linear;
	private float constant;

	public static final Attenuation ACCURATE = new Attenuation(1, 0, 0);

	public Attenuation(float quadratic, float linear, float constant) {
		this.quadratic = quadratic;
		this.linear = linear;
		this.constant = constant;
	}

	public float getQuadratic() {
		return quadratic;
	}

	public void setQuadratic(float quadratic) {
		this.quadratic = quadratic;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}
}
