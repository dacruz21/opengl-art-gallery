package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public abstract class GameComponent {
	public abstract void input(Transform transform, float delta);
	public abstract void update(Transform transform, float delta);
	public abstract void render(Transform transform, Shader shader);

	public abstract void addToRenderingEngine(RenderingEngine renderingEngine);
}
