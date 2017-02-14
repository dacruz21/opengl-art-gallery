package com.typokign.fps.engine.rendering.mesh;

/**
 * Created by Typo Kign on 2/12/2017.
 */
public class OBJIndex {
	private int vertexIndex;
	private int texCoordIndex;
	private int normalIndex;

	public OBJIndex() {
		this(0,0, 0);
	}

	public OBJIndex(int vertexIndex, int texCoordIndex, int normalIndex) {
		this.vertexIndex = vertexIndex;
		this.texCoordIndex = texCoordIndex;
		this.normalIndex = normalIndex;
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	public void setVertexIndex(int vertexIndex) {
		this.vertexIndex = vertexIndex;
	}

	public int getTexCoordIndex() {
		return texCoordIndex;
	}

	public void setTexCoordIndex(int texCoordIndex) {
		this.texCoordIndex = texCoordIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}
}
