package com.typokign.fps.engine.core;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public abstract class Game {

	private GameObject root;

	public void init() {

	}

	public void input() {
		getRootObject().input();
	}

	public void update() {
		getRootObject().update();
	}

	public GameObject getRootObject() {
		if (root == null)
			root = new GameObject();

		return root;
	}
}
