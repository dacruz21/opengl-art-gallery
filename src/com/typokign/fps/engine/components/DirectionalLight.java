package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.RenderingEngine;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.BaseLight;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class DirectionalLight extends GameComponent {
	private BaseLight base;
	private Vector3f direction;

	public DirectionalLight(BaseLight base, Vector3f direction) {
		this.base = base;
		this.direction = direction.normalized();
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addDirectionalLight(this);
	}

	public BaseLight getBase() {
		return base;
	}

	public void setBase(BaseLight base) {
		this.base = base;
	}

	public Vector3f getDirection() {
		return direction.normalized();
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
}
