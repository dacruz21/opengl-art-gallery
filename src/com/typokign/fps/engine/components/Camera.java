package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Window;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/25/2017.
 */
public class Camera extends GameComponent {
	public static final Vector3f yAxis = new Vector3f(0,1,0); // world-up3

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
	public void addToRenderingEngine(RenderingEngine renderingEngine) {
		renderingEngine.addCamera(this);
	}

	boolean mouseLocked = false;
	Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

	@Override
	public void input(float delta) {
		float sensitivity = 0.25f;
		float movAmt = 10f * delta;
		//float rotAmt = (float) (100 * Time.getDelta());

		if (Input.getKey(Keyboard.KEY_ESCAPE)) {
			Input.setCursor(true);
			mouseLocked = false;
		}

		if (Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}

		int factor = 1;

		if (Input.getKey(Keyboard.KEY_LSHIFT) || Input.getKey(Keyboard.KEY_RSHIFT))
			factor = 2;
		else
			factor = 1;

		if (Input.getKey(Keyboard.KEY_W)) {
			move(getTransform().getRotation().getForward(), movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_S)) {
			move(getTransform().getRotation().getForward(), -movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_A)) {
			move(getTransform().getRotation().getLeft(), movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_D)) {
			move(getTransform().getRotation().getRight(), movAmt * factor);
		}

		if (mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if(rotY)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotX)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

			if (rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}

	public void move(Vector3f direction, float magnitude) {
		getTransform().setPosition(getTransform().getPosition().add(direction.mul(magnitude)));
	}
}
