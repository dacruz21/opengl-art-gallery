package com.typokign.fps;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class PointLight {
	private BaseLight base;
	private Attenuation atten;
	private Vector3f position;

	public PointLight(BaseLight base, Attenuation atten, Vector3f position) {
		this.base = base;
		this.atten = atten;
		this.position = position;
	}

	public BaseLight getBase() {
		return base;
	}

	public void setBase(BaseLight base) {
		this.base = base;
	}

	public Attenuation getAtten() {
		return atten;
	}

	public void setAtten(Attenuation atten) {
		this.atten = atten;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
}
