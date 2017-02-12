package com.typokign.fps.components;

import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.rendering.Attenuation;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.ForwardSpot;
import com.typokign.fps.engine.rendering.RenderingEngine;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class Flashlight extends SpotLight {
	private float intensity;
	private int toggleKeyCode;
	private boolean on;

	public Flashlight(Color color, float intensity, Attenuation attenuation, float cutoff, int toggleKeyCode, boolean startOn) {
		super(color, startOn ? intensity : 0, attenuation, cutoff);
		this.intensity = intensity;
		this.toggleKeyCode = toggleKeyCode;
		this.on = startOn;
		setShader(ForwardSpot.getInstance());
	}

	@Override
	public void input(float delta) {
		super.input(delta);

		if (Input.getKeyDown(toggleKeyCode)) {
			on = !on;
		}
	}

	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
	}


	@Override
	public void update(float delta) {
		super.update(delta);
		setIntensity(on ? intensity : 0);
	}
}
