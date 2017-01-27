package com.typokign.fps;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Typo Kign on 1/26/2017.
 */
public class Texture {

	private int id;

	public Texture(int id) {
		this.id = id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getID() {
		return id;
	}
}
