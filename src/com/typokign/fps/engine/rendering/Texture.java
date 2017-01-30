package com.typokign.fps.engine.rendering;

import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Typo Kign on 1/26/2017.
 */
public class Texture {
	private int id;

	public Texture(String filename) {
		this(loadTexture(filename));
	}

	public Texture(int id) {
		this.id = id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getID() {
		return id;
	}

	private static int loadTexture(String filename) {

		String[] splitArray = filename.split("\\.");
		String fileExtension = splitArray[splitArray.length - 1];

		try {
			int id = TextureLoader.getTexture(fileExtension, new FileInputStream(new File("./res/textures/" + filename))).getTextureID();

			return id;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return 0;
	}
}
