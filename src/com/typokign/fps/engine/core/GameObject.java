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
	private CoreEngine engine;

	public GameObject() {
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		transform = new Transform();
		engine = null;
	}

	public GameObject addChild(GameObject child) {
		children.add(child);
		child.getTransform().setParent(transform);
		child.setEngine(engine);

		return this;
	}

	public GameObject addComponent(GameComponent component) {
		components.add(component);
		component.setParent(this);

		return this;
	}

	public void inputAll(float delta) {
		input(delta);

		for (GameObject child : children)
			child.inputAll(delta);
	}

	public void updateAll(float delta) {
		update(delta);

		for (GameObject child : children)
			child.updateAll(delta);
	}

	public void renderAll(Shader shader, RenderingEngine renderingEngine) {
		render(shader, renderingEngine);

		for (GameObject child : children) {
			child.renderAll(shader, renderingEngine);
		}
	}

	public void input(float delta) {
		transform.update();
		for (GameComponent component : components)
			component.input(delta);
	}

	public void update(float delta) {
		for (GameComponent component : components)
			component.update(delta);
	}

	public void render(Shader shader, RenderingEngine renderingEngine) {
		for (GameComponent component : components)
			component.render(shader, renderingEngine);
	}

	public ArrayList<GameObject> getAllAttached() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();

		for (GameObject child : children) {
			result.addAll(child.getAllAttached());
		}

		result.add(this);
		return result;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setEngine(CoreEngine engine) {
		if (this.engine != engine) {
			this.engine = engine;

			for (GameComponent component : components)
				component.addToEngine(engine);

			for (GameObject child : children)
				child.setEngine(engine);
		}
	}

	public CoreEngine getEngine() {
		return engine;
	}
}
