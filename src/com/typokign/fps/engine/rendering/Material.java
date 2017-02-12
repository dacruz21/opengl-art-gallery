package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.math.Vector3f;

import java.util.HashMap;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class Material {
	private HashMap<String, Texture> textureMap;
	private HashMap<String, Vector3f> vectorMap;
	private HashMap<String, Float> floatMap;

	public Material() {
		textureMap = new HashMap<String, Texture>();
		vectorMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
	}

	public void addTexture(String name, Texture texture) {
		textureMap.put(name, texture);
	}

	public Texture getTexture(String name) {
		Texture result = textureMap.get(name);
		if (result != null)
			return result;

//		System.out.println("No texture provided, using error.png");
		return new Texture("error.png");
	}

	public void addVector(String name, Vector3f vector) {
		vectorMap.put(name, vector);
	}

	public Vector3f getVector(String name) {
		Vector3f result = vectorMap.get(name);
		if (result != null)
			return  result;

		return new Vector3f(0, 0, 0);
	}

	public void addFloat(String name, float floatValue) {
		floatMap.put(name, floatValue);
	}

	public float getFloat(String name) {
		Float result = floatMap.get(name);
		if (result != null)
			return result;

		return 0;
	}
}
