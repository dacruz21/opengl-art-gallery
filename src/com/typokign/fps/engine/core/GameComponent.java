package com.typokign.fps.engine.core;

import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public interface GameComponent {
	public void input(Transform transform);
	public void update(Transform transform);
	public void render(Transform transform, Shader shader);
}
