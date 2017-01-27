package com.typokign.fps;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Mesh {

	// vbo = pointer, size = size in bytes
	// pointers in java :D
	private int vbo;
	private int ibo;
	private int size;

	public Mesh() {
		this.vbo = glGenBuffers();
		this.ibo = glGenBuffers();
		this.size = 0;
	}

	public void addVertices(Vertex[] vertices, int[] indices) {
		size = indices.length; // the size constant in Vertex is the number of bytes each object takes in memory, all Vertices have the same SIZE regardless of pos value

		// treat vbo as a buffer
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		// buffer all of our vertices, but we need to do some wacky stuff to flip the buffer
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}

	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // 4 bytes per float * 3 floats for position = 12 bytes offset to get texture

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
}
