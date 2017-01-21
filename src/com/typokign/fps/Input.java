package com.typokign.fps;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Input {

    public static final int NUM_KEYCODES = 256;

    // Current keys that have been pressed
    private static ArrayList<Integer> currentKeys = new ArrayList<Integer>();
    // Current keys that have been pressed in the last tick
    private static ArrayList<Integer> downKeys = new ArrayList<Integer>();
    // Current keys that have been released in the last tick
    private static ArrayList<Integer> upKeys = new ArrayList<Integer>();

    public static void update() {

        upKeys.clear();
        for (int key = 0; key < NUM_KEYCODES; key++) {
            if (!getKey(key) && currentKeys.contains(key)) {
                upKeys.add(key);
            }
        }

        downKeys.clear();

        for (int key = 0; key < NUM_KEYCODES; key++) {
            if (getKey(key) && !currentKeys.contains(key)) {
                downKeys.add(key);
            }
        }

        currentKeys.clear();

        for (int key = 0; key < NUM_KEYCODES; key++) {
            if (getKey(key)) {
                currentKeys.add(key);
            }
        }
    }

    // Returns true while the key is held
    public static boolean getKey(int keyCode) {
        return Keyboard.isKeyDown(keyCode);
    }

    // Returns true once, when the key is first pressed
    public static boolean getKeyDown(int keyCode) {
        if (downKeys.contains(keyCode)) {
            return true;
        }

        return false;
    }

    // Returns true when the key is released
    public static boolean getKeyUp(int keyCode) {

    }
}
