package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vertex {
	// PLEASE: if you add fields to this class, update SIZE and update createFlippedBuffer in Util.java
	public static final int SIZE = 8;

	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;

	public Vertex(Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord) {
		this(pos, texCoord, new Vector3f(0,0,0));
	}

	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal) {
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
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

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
}
