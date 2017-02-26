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
	 * Creates a sphere with a given radius that surrounds a point
	 * @param center The center point of the sphere in the world
	 * @param radius The radius of the sphere
	 * @param lod The level of detail of the sphere. Higher values will produce more vertices and a smoother surface but at the cost of performance
	 * @return A spherical mesh centered around the center point with provided radius and lod
	 */
	public static Mesh createSphere(Vector3f center, float radius, int lod) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		float startLongitude = 0;
		float startLatitude = 0;
		float endLongitude = (float) Math.PI * 2;
		float endLatitude = (float) Math.PI;
		float stepLongitude = (endLongitude - startLongitude) / lod;
		float stepLatitude = (endLatitude - startLatitude) / lod;

		for (int i = 0; i < lod; i++) {
			for (int j = 0; j < lod; j++) {
				float longitude = i * stepLongitude + startLongitude;
				float latitude = j * stepLatitude + startLatitude;

				float nextLongitude = i+1 == lod ? endLongitude : (i+1) * stepLongitude + startLongitude;
				float nextLatitude = j+1 == lod ? endLatitude : (j+1) * stepLatitude + startLatitude;

				Vector3f point0 = getSphereCoords(longitude, latitude, radius).add(center);
				Vector3f point1 = getSphereCoords(longitude, nextLatitude, radius).add(center);
				Vector3f point2 = getSphereCoords(nextLongitude, latitude, radius).add(center);
				Vector3f point3 = getSphereCoords(nextLongitude, nextLatitude, radius).add(center);

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

		return new Mesh(vertArray, indexArray, true);
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
}
