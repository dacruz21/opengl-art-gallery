package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class BaseLight extends GameComponent {
	private Color color;
	private float intensity;
	private Shader shader;

	public BaseLight(Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}

	protected void setShader(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
	}
}
