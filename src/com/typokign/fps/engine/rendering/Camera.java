package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.core.Time;
import com.typokign.fps.engine.core.Vector2f;
import com.typokign.fps.engine.core.Vector3f;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/25/2017.
 */
public class Camera {

	public static final Vector3f yAxis = new Vector3f(0,1,0); // world-up3

	private Vector3f position;
	private Vector3f forward; // cam-forward
	private Vector3f up; // cam-up


	public Camera() {
		this(new Vector3f(0,0,0), new Vector3f(0,0,1), new Vector3f(0,1,0));
	}

	public Camera(Vector3f position, Vector3f forward, Vector3f up) {
		this.position = position;
		this.forward = forward.normalized();
		this.up = up.normalized();
		mouseLocked = false;
	}

	boolean mouseLocked = false;
	Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

	public void input() {
		float sensitivity = 0.25f;
		float movAmt = (float) (10 * Time.getDelta());
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

		if (Input.getKey(Keyboard.KEY_W)) {
			move(getForward(), movAmt);
		}
		if (Input.getKey(Keyboard.KEY_S)) {
			move(getForward(), -movAmt);
		}
		if (Input.getKey(Keyboard.KEY_A)) {
			move(getLeft(), movAmt);
		}
		if (Input.getKey(Keyboard.KEY_D)) {
			move(getRight(), movAmt);
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

//		if (Input.getKey(Keyboard.KEY_UP)) {
//			rotateX(-rotAmt);
//		}
//		if (Input.getKey(Keyboard.KEY_DOWN)) {
//			rotateX(rotAmt);
//		}
//		if (Input.getKey(Keyboard.KEY_LEFT)) {
//			rotateY(-rotAmt);
//		}
//		if (Input.getKey(Keyboard.KEY_RIGHT)) {
//			rotateY(rotAmt);
//		}

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
