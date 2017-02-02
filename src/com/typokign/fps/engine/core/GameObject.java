package com.typokign.fps.engine.core;

import java.util.ArrayList;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class GameObject {

	// a node in the game's scene graph. each node calls its children's methods after performing its own

	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;
	private Transform transform; // all objects should have a position in the world

	public GameObject() {
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		transform = new Transform();
	}

	public void addChild(GameObject child) {
		children.add(child);
	}

	public void addComponent(GameComponent component) {
		components.add(component);
	}

	public void input() {
		for (GameComponent component : components)
			component.input(transform);

		for (GameObject child : children)
			child.input();
	}

	public void update() {
		for (GameComponent component : components)
			component.update(transform);

		for (GameObject child : children)
			child.update();
	}

	public void render() {
		for (GameComponent component : components) {
			component.render(transform);
		}

		for (GameObject child : children) {
			child.render();
		}
	}

	public Transform getTransform() {
		return transform;
	}
}
