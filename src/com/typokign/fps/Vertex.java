package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vertex {
	// PLEASE: if you add fields to this class, update SIZE and update createFlippedBuffer in Util.java
	public static final int SIZE = 3;

	private Vector3f pos;

	public Vertex(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
}
