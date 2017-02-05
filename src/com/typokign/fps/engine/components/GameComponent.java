package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public abstract class GameComponent {
	private GameObject parent;

	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader) {}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public GameObject getParent() {
		return parent;
	}

	public Transform getTransform() {
		return parent.getTransform();
	}

	public void addToRenderingEngine(RenderingEngine renderingEngine) {}
}