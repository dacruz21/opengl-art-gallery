package com.typokign.fps.engine.core;

import com.typokign.fps.engine.components.GameComponent;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Shader;

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

	public GameObject addChild(GameObject child) {
		children.add(child);
		child.getTransform().setParent(transform);

		return this;
	}

	public GameObject addComponent(GameComponent component) {
		components.add(component);
		component.setParent(this);

		return this;
	}

	public void input(float delta) {
		for (GameComponent component : components)
			component.input(delta);

		for (GameObject child : children)
			child.input(delta);
	}

	public void update(float delta) {
		for (GameComponent component : components)
			component.update(delta);

		for (GameObject child : children)
			child.update(delta);
	}

	public void render(Shader shader) {
		for (GameComponent component : components) {
			component.render(shader);
		}

		for (GameObject child : children) {
			child.render(shader);
		}
	}

	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		for (GameComponent component : components)
			component.addToRenderingEngine(renderingEngine);

		for (GameObject child : children)
			child.addToRenderingEngine(renderingEngine);
	}

	public Transform getTransform() {
		return transform;
	}
}
