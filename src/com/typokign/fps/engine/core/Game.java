package com.typokign.fps.engine.core;

import com.typokign.fps.engine.rendering.RenderingEngine;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public abstract class Game {
	private GameObject root = new GameObject();

	public void init() {

	}

	public void input(float delta) {
		root.input(delta);
	}

	public void update(float delta) {
		root.update(delta);
	}

	public void render(RenderingEngine renderingEngine) {
		renderingEngine.render(root);
	}

	public void addObject(GameObject object) {
		root.addChild(object);
	}
}
