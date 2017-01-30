package com.typokign.fps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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

	public Mesh(String filename) {
		initMeshData();
		loadMesh(filename);
	}

	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false);
	}

	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
		initMeshData();
		addVertices(vertices, indices, calcNormals);
	}

	private void initMeshData() {
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		size = 0;
	}

	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {

		if (calcNormals) {
			calcNormals(vertices, indices);
		}

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
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // 4 bytes per float * 3 floats for position = 12 bytes offset to get texture
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); // 4 bytes per float * 2 floats for texCoord = 8 more byte offset

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}

	private void calcNormals(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i+= 3) { // count by triangles
			int i0 = indices[i];
			int i1 = indices[i+1]; // ^ v vertices for triangles
			int i2 = indices[i+2];

			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos()); // first face
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos()); // second face

			Vector3f normal = v1.crossProduct(v2).normalized();

			vertices[i0].setNormal(vertices[i0].getNormal().add(normal)); // add the normal to every vertex
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal)); // add the normal to every vertex
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal)); // add the normal to every vertex
		}

		for (int i = 0; i < vertices.length; i++) {
			vertices[i].setNormal(vertices[i].getNormal().normalized()); // normalize each vector
		}
	}

	private Mesh loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\."); // get the file extension via regex splitting
		String fileExtension = splitArray[splitArray.length - 1]; // last element will be extension

		if (!fileExtension.equals("obj")) {
			System.err.println("Error: file format not supported for mesh data: " + fileExtension);
			new Exception().printStackTrace();
			System.exit(1);
		}

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		try {
			BufferedReader meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));

			String line;
			while ((line  = meshReader.readLine()) != null) {
				String[] tokens = line.split(" "); // see a sample obj from the models directory, each line in obj is a v(ertex) or f(ace), followed by x,y,z or triangular indexes
				tokens = Util.removeEmptyStrings(tokens);

				if (tokens.length == 0 || tokens[0].equals("#")) { // blank line or comment
					continue;
				} else if (tokens[0].equals("v")) { // vertex
					vertices.add(new Vertex(new Vector3f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])))); // quite the complicated nest, but really just create a vertex with the x,y,z separated by spaces
				} else if (tokens[0].equals("f")) { // face
					indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1
					indices.add(Integer.parseInt(tokens[2].split("/")[0]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1
					indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1

					if (tokens.length > 4) {
						indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1); // triangulate quadrilaterals
						indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						indices.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
					}
				}
			}

			meshReader.close();

			Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
			vertices.toArray(vertexData);

			Integer[] indexData = indices.toArray(new Integer[indices.size()]);
			indices.toArray(indexData);

			addVertices(vertexData, Util.toIntArray(indexData), true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}
}
