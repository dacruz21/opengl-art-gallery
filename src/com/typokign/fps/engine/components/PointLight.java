package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.RenderingEngine;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.BaseLight;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class PointLight extends GameComponent {
	private BaseLight base;
	private Attenuation atten;
	private Vector3f position;
	private float range;

	public PointLight(BaseLight base, Attenuation atten, Vector3f position, float range) {
		this.base = base;
		this.atten = atten;
		this.position = position;
		this.range = range;
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addPointLight(this);
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

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
}
