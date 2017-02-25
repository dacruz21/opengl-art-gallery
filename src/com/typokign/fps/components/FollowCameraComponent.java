package com.typokign.fps.components;

import com.typokign.fps.engine.components.GameComponent;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 2/25/2017.
 */
public class FollowCameraComponent extends GameComponent {
	RenderingEngine renderingEngine;

	@Override
	public void update(float delta) {
		if (renderingEngine != null) {
			Vector3f position = getTransform().getPosition().lerp(renderingEngine.getMainCamera().getTransform().getTransformedPosition(), delta);
			getTransform().setPosition(position);

			Quaternion rotation = getTransform().getPointTowardsDirection(renderingEngine.getMainCamera().getTransform().getTransformedPosition(), new Vector3f(0, 1, 0));
			getTransform().setRotation(getTransform().getRotation().lerpSpherical(rotation, delta * 5.0f, true));
		}
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
}
