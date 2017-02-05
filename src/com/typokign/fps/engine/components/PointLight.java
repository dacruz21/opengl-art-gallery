package com.typokign.fps.engine.components;

import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.ForwardPoint;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class PointLight extends BaseLight {
	private float exponent;
	private float linear;
	private float constant;
	private Vector3f position;
	private float range;

	public PointLight(Vector3f color, float intensity, float exponent, float linear, float constant, Vector3f position, float range) {
		super(color, intensity);
		this.exponent = exponent;
		this.linear = linear;
		this.constant = constant;
		this.position = position;
		this.range = range;

		setShader(ForwardPoint.getInstance());
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
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

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
}
