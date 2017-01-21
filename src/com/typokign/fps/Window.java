package com.typokign.fps;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Window {
    public static void createWindow(int width, int height, String title) {
        Display.setTitle(title);

        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

    }

    public static void render() {
        Display.update();
    }

    public static void dispose() {
        Display.destroy();
    }

    public static boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public static int getWidth() {
        return Display.getDisplayMode().getWidth();
    }

    public static int getHeight() {
        return Display.getDisplayMode().getHeight();
    }

    public static String getTitle() {
        return Display.getTitle();
    }
}