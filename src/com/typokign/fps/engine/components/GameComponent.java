package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public abstract class GameComponent {
	public void input(Transform transform, float delta) {}
	public void update(Transform transform, float delta) {}
	public void render(Transform transform, Shader shader) {}

	public void addToRenderingEngine(RenderingEngine renderingEngine) {}
}
