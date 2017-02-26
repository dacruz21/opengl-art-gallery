package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.resourcemanagement.MeshResource;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Mesh {
	private static HashMap<String, MeshResource> loadedMeshes = new HashMap<String, MeshResource>();
	private MeshResource resource;
	private String filename;

	public Mesh(String filename) {
		this.filename = filename;
		MeshResource existingResource = loadedMeshes.get(filename);

		if (existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			loadMesh(filename);
			loadedMeshes.put(filename, resource);
		}
	}

	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false);
	}

	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
		filename = "";
		resource = new MeshResource(0);
		addVertices(vertices, indices, calcNormals);
	}

	@Override
	protected void finalize() {
		if (resource.removeReference() && !filename.isEmpty()) {
			loadedMeshes.remove(filename);
		}
	}

	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {

		if (calcNormals) {
			calcNormals(vertices, indices);
		}

		resource = new MeshResource(indices.length);

		// treat vbo as a resource
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());

		// resource all of our vertices, but we need to do some wacky stuff to flip the resource
		GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}

	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // 4 bytes per float * 3 floats for position = 12 bytes offset to get texture
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); // 4 bytes per float * 2 floats for texCoord = 8 more byte offset

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

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

		for (Vertex vertex : vertices) {
			vertex.setNormal(vertex.getNormal().normalized()); // normalize each vector
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

		OBJModel objModel = new OBJModel("./res/models/" + fileName);
		IndexedModel model = objModel.toIndexedModel();
		model.calcNormals();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for (int i = 0; i < model.getPositions().size(); i++) {
			vertices.add(new Vertex(model.getPositions().get(i),
					model.getTexCoords().get(i),
					model.getNormals().get(i)));
		}

		Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false);

		return null;
	}
}
