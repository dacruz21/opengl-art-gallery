package com.typokign.fps;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class RenderUtil {
	public static void clearScreen() {
		// TODO: stencil buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setTextures(boolean enabled) {
		if (enabled)
			glEnable(GL_TEXTURE_2D);
		else
			glDisable(GL_TEXTURE_2D);
	}

	public static void initGraphics() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// face culling = unrendering faces not facing towards the camera
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);

		// don't render a face if it is behind another face
		glEnable(GL_DEPTH_TEST);

		//TODO: depth clamping

		// auto-gamma correction to the SRGB space
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_FRAMEBUFFER_SRGB);
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
}
