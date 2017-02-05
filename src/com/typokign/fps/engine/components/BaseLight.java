package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/28/2017.
 */
public class BaseLight extends GameComponent {
	private Vector3f color;
	private float intensity;
	private Shader shader;

	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}

	protected void setShader(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	@Override
	public void input(Transform transform, float delta) {}

	@Override
	public void update(Transform transform, float delta) {}

	@Override
	public void render(Transform transform, Shader shader) {}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
	}
}
