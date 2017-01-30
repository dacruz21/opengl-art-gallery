package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Vector3f;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class DirectionalLight {
	private BaseLight base;
	private Vector3f direction;

	public DirectionalLight(BaseLight base, Vector3f direction) {
		this.base = base;
		this.direction = direction.normalized();
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
