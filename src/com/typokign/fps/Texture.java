package com.typokign.fps;

import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;

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

	public static Texture loadTexture(String filename) {

		String[] splitArray = filename.split("\\.");
		String fileExtension = splitArray[splitArray.length - 1];

		try {
			int id = TextureLoader.getTexture(fileExtension, new FileInputStream(new File("./res/textures/" + filename))).getTextureID();

			return new Texture(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}
}
