package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.math.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

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

	public static void unbindTextures() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static void setClearColor(Vector3f color) {
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}

	public static void initGraphics() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// face culling = unrendering faces not facing towards the camera
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);

		// don't render a face if it is behind another face
		glEnable(GL_DEPTH_TEST);

		// fixes issues if a camera is right on the border of a face
		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
		//glEnable(GL_FRAMEBUFFER_SRGB);
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
}
