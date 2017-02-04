package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.core.Time;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/25/2017.
 */
public class Camera {

	public static final Vector3f yAxis = new Vector3f(0,1,0); // world-up3

	private Vector3f position;
	private Vector3f forward; // cam-forward
	private Vector3f up; // cam-up
	private Matrix4f projection;
	boolean mouseLocked;

	public Camera(float fov, float aspect, float zNear, float zFar) {
		this.position = new Vector3f(0, 0, 0);
		this.forward = new Vector3f(0, 0, 1).normalized();
		this.up = new Vector3f(0, 1, 0).normalized();
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
		mouseLocked = false;
	}

	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation = new Matrix4f().initRotation(forward, up);
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(-position.getX(), -position.getY(), - position.getZ()); // camera never actually moves, move the world opposite the direction of camera for effect

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

	public void input(float delta) {
		float sensitivity = 0.25f;
		float movAmt = (float) (10 * delta);
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
			move(getForward(), movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_S)) {
			move(getForward(), -movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_A)) {
			move(getLeft(), movAmt * factor);
		}
		if (Input.getKey(Keyboard.KEY_D)) {
			move(getRight(), movAmt * factor);
		}

		if (mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if (rotY)
				rotateY(deltaPos.getX() * sensitivity);
			if (rotX)
				rotateX(-deltaPos.getY() * sensitivity);

			if (rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}

	public void move(Vector3f direction, float magnitude) {
		position = position.add(direction.mul(magnitude));
	}

	public void rotateX(float angle) {
		Vector3f hAxis = yAxis.crossProduct(forward).normalized();

		forward = forward.rotate(angle, hAxis).normalized();

		up = forward.crossProduct(hAxis).normalized();
	}

	public void rotateY(float angle) {
		Vector3f hAxis = yAxis.crossProduct(forward).normalized();

		forward = forward.rotate(angle, yAxis).normalized();

		up = forward.crossProduct(hAxis).normalized();
	}

	public Vector3f getLeft() {
		Vector3f left = forward.crossProduct(up).normalized(); // remember right-hand rule

		return left;
	}

	public Vector3f getRight() {
		Vector3f right = up.crossProduct(forward).normalized();

		return right;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getForward() {
		return forward;
	}

	public void setForward(Vector3f forward) {
		this.forward = forward;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}
}
