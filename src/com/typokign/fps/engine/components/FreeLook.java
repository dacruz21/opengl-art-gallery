package com.typokign.fps.engine.components;

import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Window;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 2/25/2017.
 */
public class FreeLook extends GameComponent {
	boolean mouseLocked = false;
	final Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

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

		int factor;

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
				getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(deltaPos.getX() * sensitivity));
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
