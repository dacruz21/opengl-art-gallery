package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Window;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/25/2017.
 */
public class Camera extends GameComponent {
	private Matrix4f projection;

	public Camera(float fov, float aspect, float zNear, float zFar) {
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}

	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation = getTransform().getTransformedRotation().conjugate().toRotationMatrix();
		Vector3f cameraPosition = getTransform().getTransformedPosition().mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ()); // camera never actually moves, move the world opposite the direction of camera for effect

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		getParent().getEngine().getAudioEngine().updateListener(getPosition(), new Vector3f(0, 0, 0) /*TODO: velocity*/, getRotation());
	}

	@Override
	public void addToEngine(CoreEngine engine) {
		engine.getRenderingEngine().addCamera(this);
	}
}
