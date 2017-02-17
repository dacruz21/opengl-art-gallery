package com.typokign.fps.engine.rendering.resourcemanagement;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Typo Kign on 2/17/2017.
 */
public class TextureResource {
	private int id;
	private int refCount;

	public TextureResource(int id) {
		this.id = id;
		this.refCount = 1;
	}

	@Override
	protected void finalize() {
		glDeleteBuffers(id);
	}

	public void addReference() {
		refCount++;
	}

	public boolean removeReference() {
		refCount--;
		return refCount == 0;
	}

	public int getId() {
		return id;
	}
}
