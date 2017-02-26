package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

import java.util.ArrayList;

/**
 * Provides static methods to generate Meshes based on common primitive shapes, including cuboids (cubes and rectangular prisms), spheres,
 * cylinders, etc.
 */
public class Primitives {
	/**
	 * Creates a rectangular prism (cuboid) in the area between two corners.
	 * @param corner A Vector3f position in the world
	 * @param corner2 Another position
	 * @return A mesh occupying the region between both points
	 */
	public static Mesh createCuboid(Vector3f corner, Vector3f corner2) {
		if (corner.getX() == corner2.getX() || corner.getY() == corner2.getY() || corner.getZ() == corner2.getZ()) {
			throw new IllegalArgumentException("Cannot create cuboid for two vectors on the same plane. Aborting.");
		}

		float left = Math.min(corner.getX(), corner2.getX());
		float right = Math.max(corner.getX(), corner2.getX());
		float down = Math.min(corner.getY(), corner2.getY());
		float up = Math.max(corner.getY(), corner2.getY());
		float front = Math.min(corner.getZ(), corner2.getZ());
		float back = Math.max(corner.getZ(), corner2.getZ());

		Vector3f ldf = new Vector3f(left, down, front);
		Vector3f ldb = new Vector3f(left, down, back);
		Vector3f luf = new Vector3f(left, up, front);
		Vector3f lub = new Vector3f(left, up, back);
		Vector3f rdf = new Vector3f(right, down, front);
		Vector3f rdb = new Vector3f(right, down, back);
		Vector3f ruf = new Vector3f(right, up, front);
		Vector3f rub = new Vector3f(right, up, back);

		Vertex[] vertices = new Vertex[] {
				new Vertex(rdf, new Vector2f(1, 0)),
				new Vertex(rdb, new Vector2f(1, 1)),
				new Vertex(ldb, new Vector2f(0, 1)),
				new Vertex(ldf, new Vector2f(0, 0)),
				new Vertex(ruf, new Vector2f(1, 1)),
				new Vertex(rub, new Vector2f(1, 0)),
				new Vertex(lub, new Vector2f(0, 0)),
				new Vertex(luf, new Vector2f(0, 1))
		};

		int[] indices = new int[] {
				1, 3, 0,
				7, 5, 4,
				4, 1, 0,
				5, 2, 1,
				2, 7, 3,
				0, 7, 4,
				1, 2, 3,
				7, 6, 5,
				4, 5, 1,
				5, 6, 2,
				2, 6, 7,
				0, 3, 7
		};

		return new Mesh(vertices, indices, true);
	}

	/**
	 * Creates a sphere with a given radius that surrounds a point.
	 * Runs in O(n^2) time, where n is the level of detail
	 * @param center The center point of the sphere in the world
	 * @param radius The radius of the sphere
	 * @param lod The level of detail of the sphere. Higher values will produce more vertices and a smoother surface but at the cost of performance
	 * @return A spherical mesh centered around the center point with provided radius and lod
	 */
	public static Mesh createSphere(Vector3f center, float radius, int lod) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		// Start and end polar coordinates, with the number of latitude/longitude lines determined by the LoD
		float startLongitude = 0;
		float startLatitude = 0;
		float endLongitude = (float) Math.PI * 2;
		float endLatitude = (float) Math.PI;
		float stepLongitude = (endLongitude - startLongitude) / lod;
		float stepLatitude = (endLatitude - startLatitude) / lod;

		// Iterate through all intersections of latitude/longitude lines
		for (int i = 0; i < lod; i++) {
			for (int j = 0; j < lod; j++) {
				float longitude = i * stepLongitude + startLongitude;
				float latitude = j * stepLatitude + startLatitude;

				// Get the next lat/long lines
				float nextLongitude = i+1 == lod ? endLongitude : (i+1) * stepLongitude + startLongitude;
				float nextLatitude = j+1 == lod ? endLatitude : (j+1) * stepLatitude + startLatitude;

				// Get points on the sphere for all combinations of current and next lat/long lines
				Vector3f point0 = getSphereCoords(longitude, latitude, radius).add(center);
				Vector3f point1 = getSphereCoords(longitude, nextLatitude, radius).add(center);
				Vector3f point2 = getSphereCoords(nextLongitude, latitude, radius).add(center);
				Vector3f point3 = getSphereCoords(nextLongitude, nextLatitude, radius).add(center);

				// Add vertices for triangles between the lat/long lines
				vertices.add(new Vertex(point0));
				vertices.add(new Vertex(point2));
				vertices.add(new Vertex(point1));
				indices.add(vertices.size() - 3);
				indices.add(vertices.size() - 2);
				indices.add(vertices.size() - 1);

				vertices.add(new Vertex(point3));
				vertices.add(new Vertex(point1));
				vertices.add(new Vertex(point2));
				indices.add(vertices.size() - 3);
				indices.add(vertices.size() - 2);
				indices.add(vertices.size() - 1);
			}
		}

		Vertex[] vertArray = new Vertex[vertices.size()];
		vertArray = vertices.toArray(vertArray);

		int[] indexArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			indexArray[i] = indices.get(i);

		// Calculate normals of the unbaked mesh
		calcNormals(vertArray, indexArray);

		// Use the normals to generate texCoords
		for (Vertex vertex : vertArray) {
			float texCoordU = vertex.getNormal().getX() / 2 + 0.5f;
			float texCoordV = vertex.getNormal().getY() / 2 + 0.5f;
			vertex.setTexCoord(new Vector2f(texCoordU, texCoordV));
		}

		return new Mesh(vertArray, indexArray, false);
	}

	/**
	 * The parametric equation for a sphere. Used for generating triangles between each line of latitude/longitude on a sphere
	 * @param longitude The current longitude position
	 * @param latitude The current latitude position
	 * @param radius The radius of the sphere
	 * @return The position of the vertex
	 */
	private static Vector3f getSphereCoords(float longitude, float latitude, float radius) {
		return new Vector3f((float) (Math.cos(longitude) * Math.sin(latitude) * radius), (float) (Math.cos(latitude) * radius), (float) (Math.sin(longitude) * Math.sin(latitude) * radius));
	}

	private static void calcNormals(Vertex[] vertices, int[] indices) {
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
}
