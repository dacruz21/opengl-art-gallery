package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Typo Kign on 2/12/2017.
 */
public class OBJModel {
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<OBJIndex> indices;
	private boolean hasTexCoords;
	private boolean hasNormals;

	public OBJModel(String filename) {
		this.positions = new ArrayList<Vector3f>();
		this.texCoords = new ArrayList<Vector2f>();
		this.normals = new ArrayList<Vector3f>();
		this.indices = new ArrayList<OBJIndex>();
		this.hasTexCoords = false;
		this.hasNormals = false;

		BufferedReader meshReader = null;
		try {
			meshReader = new BufferedReader(new FileReader(filename));

			String line;
			while ((line  = meshReader.readLine()) != null) {
				String[] tokens = line.split(" "); // see a sample obj from the models directory, each line in obj is a vertex, texCoord, normal, or face with params split by spaces
				tokens = Util.removeEmptyStrings(tokens);

				if (tokens.length == 0 || tokens[0].equals("#")) { // blank line or comment
					continue;
				} else if (tokens[0].equals("v")) { // vertex
					positions.add(new Vector3f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3]))); // quite the complicated nest, but really just create a vertex with the x,y,z separated by spaces
				} else if (tokens[0].equals("vt")) { // vertex texture coordinate
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2])));
				} else if (tokens[0].equals("vn")) { // vertex normal
					normals.add(new Vector3f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])));
				} else if (tokens[0].equals("f")) { // face
					for (int i = 0; i < tokens.length - 3; i++) { // automatically triangulates convex polygons
						indices.add(parseOBJIndex(tokens[1]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
			}

			meshReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// O(n) where n = number of indices
	public IndexedModel toIndexedModel() {
		IndexedModel result = new IndexedModel();
		IndexedModel normalModel = new IndexedModel();
		HashMap<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		for (int i = 0; i < indices.size(); i++) {
			OBJIndex index = indices.get(i);
			Vector3f position = positions.get(index.getVertexIndex());
			Vector2f texCoord;
			Vector3f normal;

			if (hasTexCoords)
				texCoord = texCoords.get(index.getTexCoordIndex());
			else
				texCoord = new Vector2f(0, 0);

			if (hasNormals)
				normal = normals.get(index.getNormalIndex());
			else
				normal = new Vector3f(0 , 0, 0);

			Integer modelVertexIndex = resultIndexMap.get(index);

			if (modelVertexIndex == null) {
				modelVertexIndex = result.getPositions().size();
				resultIndexMap.put(index, result.getPositions().size());

				result.getPositions().add(position);
				result.getTexCoords().add(texCoord);
				if (hasNormals)
					result.getNormals().add(normal);
			}

			Integer normalModelIndex = normalIndexMap.get(index.getVertexIndex());

			if (normalModelIndex == null) {
				normalIndexMap.put(index.getVertexIndex(), normalModel.getPositions().size());
				normalModelIndex = normalModel.getPositions().size();

				normalModel.getPositions().add(position);
				normalModel.getTexCoords().add(texCoord);
				normalModel.getNormals().add(normal);
			}

			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
		}

		if (!hasNormals) {
			normalModel.calcNormals();

			for (int i = 0; i < result.getPositions().size(); i++) {
				result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
			}
		}

		return result;
	}

	private OBJIndex parseOBJIndex(String token) {
		OBJIndex result = new OBJIndex();
		String[] values = token.split("/");

		result.setVertexIndex(Integer.parseInt(values[0]) - 1);

		if (values.length > 1) {
			hasTexCoords = true;
			result.setTexCoordIndex(Integer.parseInt(values[1]) -1);

			if (values.length > 2) {
				hasNormals = true;
				result.setNormalIndex(Integer.parseInt(values[2]) - 1);
			}
		}

		return result;
	}

	public ArrayList<OBJIndex> getIndices() {
		return indices;
	}
}
