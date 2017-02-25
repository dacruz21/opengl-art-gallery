package com.typokign.fps.engine.core;

import com.typokign.fps.engine.rendering.RenderingEngine;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public abstract class Game {
	private GameObject root;

	public void init() {

	}

	public void input(float delta) {
		getRootObject().inputAll(delta);
	}

	public void update(float delta) {
		getRootObject().updateAll(delta);
	}

	public void render(RenderingEngine renderingEngine) {
		renderingEngine.render(getRootObject());
	}

	public void addObject(GameObject object) {
		getRootObject().addChild(object);
	}

	public void setEngine(CoreEngine engine) {
		getRootObject().setEngine(engine);
	}

	public CoreEngine getEngine() {
		return getRootObject().getEngine();
	}

	public GameObject getRootObject() {
		if (root == null) {
			root = new GameObject();
		}

		return root;
	}
}
