package com.typokign.fps.components;

import com.typokign.fps.engine.components.GameComponent;
import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.math.Vector3f;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class Flashlight extends GameComponent {
	private SpotLight spotLight;
	private RenderingEngine renderingEngine;

	private boolean on;
	private float onIntensity;

	public Flashlight(Color color, float onIntensity, float range, float cutoff) {
		this.spotLight = new SpotLight(color, onIntensity, Attenuation.ACCURATE, new Vector3f(0, 0, 0), cutoff);
		this.onIntensity = onIntensity;
		on = false;
	}

	@Override
	public void input(float delta) {
		if (Input.getKeyDown(Keyboard.KEY_F)) {
			on = !on;
		}
	}

	@Override
	public void update(float delta){
		if (spotLight.getParent() == null)
			spotLight.setParent(getParent());
		if (renderingEngine != null) {
			spotLight.getTransform().setPosition(renderingEngine.getMainCamera().getPosition());
			spotLight.setDirection(renderingEngine.getMainCamera().getForward());
		}

		spotLight.setIntensity(on ? onIntensity : 0.0f);
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
		renderingEngine.addLight(spotLight);
	}
}
