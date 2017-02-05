package com.typokign.fps.components;

import com.typokign.fps.engine.components.GameComponent;
import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Shader;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class Flashlight extends GameComponent {
	private SpotLight spotLight;
	private RenderingEngine renderingEngine;

	private boolean on;
	private float onIntensity;

	public Flashlight(Vector3f color, float onIntensity, float range, float cutoff) {
		this.spotLight = new SpotLight(color, onIntensity, 1, 0, 0, new Vector3f(0, 0, 0), range, new Vector3f(0, 0, 0), cutoff);
		this.onIntensity = onIntensity;
		on = false;
	}

	@Override
	public void input(Transform transform, float delta) {
		if (Input.getKeyDown(Keyboard.KEY_F)) {
			on = !on;
		}
	}

	@Override
	public void update(Transform transform, float delta){
		if (renderingEngine != null) {
			spotLight.setPosition(renderingEngine.getMainCamera().getPosition());
			spotLight.setDirection(renderingEngine.getMainCamera().getForward());
		}

		spotLight.setIntensity(on ? onIntensity : 0.0f);
	}

	@Override
	public void render(Transform transform, Shader shader) {}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
		renderingEngine.addLight(spotLight);
	}
}
