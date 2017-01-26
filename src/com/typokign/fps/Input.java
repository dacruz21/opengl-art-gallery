package com.typokign.fps;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Input {

	public static final int NUM_KEYCODES = 256;
	public static final int NUM_MOUSEBUTTONS = 5;

	// Current keys that have been pressed
	private static ArrayList<Integer> currentKeys = new ArrayList<Integer>();
	// Current keys that have been pressed in the last tick
	private static ArrayList<Integer> downKeys = new ArrayList<Integer>();
	// Current keys that have been released in the last tick
	private static ArrayList<Integer> upKeys = new ArrayList<Integer>();

	// Current mouse buttons that have been pressed
	private static ArrayList<Integer> currentMouse = new ArrayList<Integer>();
	// Current mouse buttons that have been pressed in the last tick
	private static ArrayList<Integer> downMouse = new ArrayList<Integer>();
	// Current mouse buttons that have been released in the last tick
	private static ArrayList<Integer> upMouse = new ArrayList<Integer>();

	public static void update() {

		upMouse.clear();
		for (int button = 0; button < NUM_MOUSEBUTTONS; button++)
			if (!getMouse(button) && currentMouse.contains(button))
				upMouse.add(button);

		downMouse.clear();
		for (int button = 0; button < NUM_MOUSEBUTTONS; button++)
			if (getMouse(button) && !currentMouse.contains(button))
				downMouse.add(button);

		upKeys.clear();
		for (int key = 0; key < NUM_KEYCODES; key++)
			if (!getKey(key) && currentKeys.contains(key))
				upKeys.add(key);

		downKeys.clear();

		for (int key = 0; key < NUM_KEYCODES; key++)
			if (getKey(key) && !currentKeys.contains(key))
				downKeys.add(key);

		currentKeys.clear();

		for (int key = 0; key < NUM_KEYCODES; key++)
			if (getKey(key))
				currentKeys.add(key);

		currentMouse.clear();
		for (int button = 0; button < NUM_MOUSEBUTTONS; button++)
			if (getMouse(button))
				currentMouse.add(button);
	}

	// Returns true while the key is held
	public static boolean getKey(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}

	// Returns true once, when the key is first pressed
	public static boolean getKeyDown(int keyCode) {
		return downKeys.contains(keyCode);
	}

	// Returns true when the key is released
	public static boolean getKeyUp(int keyCode) {
		return upKeys.contains(keyCode);
	}

	public static boolean getMouse(int mouseButton) {
		return Mouse.isButtonDown(mouseButton);
	}

	public static boolean getMouseDown(int mouseButton) {
		return downMouse.contains(mouseButton);
	}

	public static boolean getMouseUp(int mouseButton) {
		return upMouse.contains(mouseButton);
	}

	public static Vector2f getMousePosition() {
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}
}
