package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

import java.util.ArrayList;

/**
 * Created by Typo Kign on 2/11/2017.
 */
public class IndexedModel {
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<Integer> indices;

	public IndexedModel() {
		this.positions = new ArrayList<Vector3f>();
		this.texCoords = new ArrayList<Vector2f>();
		this.normals = new ArrayList<Vector3f>();
		this.indices = new ArrayList<Integer>();
	}

	public void calcNormals() {
		for (int i = 0; i < indices.size(); i+= 3) { // count by triangles
			int i0 = indices.get(i);
			int i1 = indices.get(i+1); // ^ v vertices for triangles
			int i2 = indices.get(i+2);

			Vector3f v1 = positions.get(i1).sub(positions.get(i0)); // first face
			Vector3f v2 = positions.get(i2).sub(positions.get(i0)); // second face

			Vector3f normal = v1.crossProduct(v2).normalized();

			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}

		for (int i = 0; i < positions.size(); i++) {
			normals.get(i).set(normals.get(i).normalized());
		}
	}

	public ArrayList<Vector3f> getPositions() {
		return positions;
	}

	public ArrayList<Vector2f> getTexCoords() {
		return texCoords;
	}

	public ArrayList<Vector3f> getNormals() {
		return normals;
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}
}
