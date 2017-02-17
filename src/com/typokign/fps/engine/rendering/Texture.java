package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.rendering.resourcemanagement.TextureResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Typo Kign on 1/26/2017.
 */
public class Texture {
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	private TextureResource resource;
	private String filename;

	public Texture(String filename) {
		this.filename = filename;
		TextureResource existingResource = loadedTextures.get(filename);

		if (existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = new TextureResource(loadTexture(filename));
			loadedTextures.put(filename, resource);
		}
	}

	@Override
	protected void finalize() {
		if (resource.removeReference() && !filename.isEmpty()) {
			loadedTextures.remove(filename);
		}
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, resource.getId());
	}

	public int getID() {
		return resource.getId();
	}

	private static int loadTexture(String filename) {

		String[] splitArray = filename.split("\\.");
		String fileExtension = splitArray[splitArray.length - 1];

		try {
			BufferedImage image = ImageIO.read(new File("./res/textures/" + filename));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);
			boolean hasAlpha = image.getColorModel().hasAlpha();

			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];

					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) (pixel & 0xFF));

					if (hasAlpha)
						buffer.put((byte) ((pixel >> 24) & 0xFF));
					else
						buffer.put((byte) 0xFF);
				}
			}

			buffer.flip();

			int id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			return id;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return 0;
	}
}
