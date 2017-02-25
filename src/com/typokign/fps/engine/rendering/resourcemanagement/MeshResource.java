package com.typokign.fps.engine.rendering.resourcemanagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * Created by Typo Kign on 2/17/2017.
 */
public class MeshResource {
	private int vbo;
	private int ibo;
	private int size;
	private int refCount;

	public MeshResource(int size) {
		this.vbo = glGenBuffers();
		this.ibo = glGenBuffers();
		this.size = size;
		this.refCount = 1;
	}

	@Override
	protected void finalize() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
	}

	public void addReference() {
		refCount++;
	}

	public boolean removeReference() {
		refCount--;
		return refCount == 0;
	}

	public int getVbo() {
		return vbo;
	}

	public int getIbo() {
		return ibo;
	}

	public int getSize() {
		return size;
	}
}
