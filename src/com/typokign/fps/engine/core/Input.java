package com.typokign.fps.engine.core;

import com.typokign.fps.engine.math.Vector2f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Input {

	public static final int NUM_KEYCODES = 256;
	public static final int NUM_MOUSEBUTTONS = 5;

	private static boolean[] lastKeys = new boolean[NUM_KEYCODES];
	private static boolean[] lastMouse = new boolean[NUM_MOUSEBUTTONS];

	public static void update() {

		for (int i = 0; i < NUM_KEYCODES; i++) {
			lastKeys[i] = getKey(i);
		}
		for (int i = 0; i < NUM_MOUSEBUTTONS; i++) {
			lastMouse[i] = getMouse(i);
		}
	}

	// Returns true while the key is held
	public static boolean getKey(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}

	// Returns true once, when the key is first pressed
	public static boolean getKeyDown(int keyCode) {
		return getKey(keyCode) && !lastKeys[keyCode];
	}

	// Returns true when the key is released
	public static boolean getKeyUp(int keyCode) {
		return !getKey(keyCode) && lastKeys[keyCode];
	}

	public static boolean getMouse(int mouseButton) {
		return Mouse.isButtonDown(mouseButton);
	}

	public static boolean getMouseDown(int mouseButton) {
		return getMouse(mouseButton) && !lastMouse[mouseButton];
	}

	public static boolean getMouseUp(int mouseButton) {
		return !getMouse(mouseButton) && lastMouse[mouseButton];
	}

	public static Vector2f getMousePosition() {
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}

	public static void setMousePosition(Vector2f pos) {
		Mouse.setCursorPosition( (int) pos.getX(), (int) pos.getY());
	}

	public static void setCursor(boolean enabled) {
		Mouse.setGrabbed(!enabled);
	}
}
