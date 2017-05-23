package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.rendering.resourcemanagement.MappedValues;

import java.util.HashMap;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class Material extends MappedValues {
	private HashMap<String, Texture> textureMap;

	public Material() {
		super();
		textureMap = new HashMap<String, Texture>();
	}

	public Material addTexture(String name, Texture texture) {
		textureMap.put(name, texture);
		return this;
	}

	public Texture getTexture(String name) {
		Texture result = textureMap.get(name);
		if (result != null)
			return result;

		return new Texture("error.png");
	}
}
