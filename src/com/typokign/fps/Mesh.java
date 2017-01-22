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
    private int size;

    public Mesh() {
        this.vbo = glGenBuffers();
        this.size = 0;
    }

    public void addVertices(Vertex[] vertices) {
        size = vertices.length; // the size constant in Vertex is the number of bytes each object takes in memory, all Vertices have the same SIZE regardless of pos value

        // treat vbo as a buffer
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // buffer all of our vertices, but we need to do some wacky stuff to flip the buffer
        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
    }

    public void draw() {
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);

        glDrawArrays(GL_TRIANGLES, 0, size);

        glDisableVertexAttribArray(0);
    }
}
