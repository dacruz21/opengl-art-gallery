package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public abstract class GameComponent {
	private GameObject parent;

	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader, RenderingEngine renderingEngine) {}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public GameObject getParent() {
		return parent;
	}

	public Transform getTransform() {
		if (parent != null)
			return parent.getTransform();

		return new Transform();
	}

	public Vector3f getPosition() {
		return getTransform().getPosition();
	}

	public Quaternion getRotation() {
		return getTransform().getRotation();
	}

	public Vector3f getScale() {
		return getTransform().getScale();
	}

	public void addToEngine(CoreEngine engine) {}
}
