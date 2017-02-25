package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vertex {
	// PLEASE: if you add fields to this class, updateAll SIZE and updateAll createFlippedBuffer in Util.java
	public static final int SIZE = 8;

	private Vector3f position;
	private Vector2f texCoord;
	private Vector3f normal;

	public Vertex(Vector3f position) {
		this(position, new Vector2f(0, 0));
	}

	public Vertex(Vector3f position, Vector2f texCoord) {
		this(position, texCoord, new Vector3f(0,0,0));
	}

	public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
		this.position = position;
		this.texCoord = texCoord;
		this.normal = normal;
	}

	public Vector3f getPos() {
		return position;
	}

	public void setPos(Vector3f position) {
		this.position = position;
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
