package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vertex {
	// PLEASE: if you add fields to this class, update SIZE and update createFlippedBuffer in Util.java
	public static final int SIZE = 5;

	private Vector3f pos;
	private Vector2f texCoord;

	public Vertex(Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord) {
		this.pos = pos;
		this.texCoord = texCoord;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector2f getTexCoord() {
		return texCoord;
	}

	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}
}
