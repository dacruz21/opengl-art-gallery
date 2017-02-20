package com.typokign.fps.engine.rendering.resourcemanagement;

import com.typokign.fps.engine.math.Vector3f;

import java.util.HashMap;

/**
 * Created by Typo Kign on 2/19/2017.
 */
public abstract class MappedValues {
	private HashMap<String, Vector3f> vector3fMap;
	private HashMap<String, Float> floatMap;

	public MappedValues() {
		vector3fMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
	}

	public void addVector3f(String name, Vector3f vector) {
		vector3fMap.put(name, vector);
	}

	public void addFloat(String name, float value) {
		floatMap.put(name, value);
	}

	public Vector3f getVector3f(String name) {
		return vector3fMap.get(name);
	}

	public float getFloat(String name) {
		return floatMap.get(name);
	}
}
