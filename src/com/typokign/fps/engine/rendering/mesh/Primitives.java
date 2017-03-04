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
	public static Mesh createCuboid(Vector3f corner, Vector3f corner2, float texLOD) {
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
				new Vertex(rdf, new Vector2f(texLOD, 0)),
				new Vertex(rdb, new Vector2f(texLOD, texLOD)),
				new Vertex(ldb, new Vector2f(0, texLOD)),
				new Vertex(ldf, new Vector2f(0, 0)),
				new Vertex(ruf, new Vector2f(texLOD, texLOD)),
				new Vertex(rub, new Vector2f(texLOD, 0)),
				new Vertex(lub, new Vector2f(0, 0)),
				new Vertex(luf, new Vector2f(0, texLOD))
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
	 * Runs in O(n^2) time, where n is numVertices
	 * @param radius The radius of the sphere
	 * @param numVertices The level of detail of the sphere. Higher values will produce more vertices and a smoother surface but at the cost of performance
	 * @param textureDetail The level of detail in the texture map
	 * @return A spherical mesh centered around the center point with provided radius and lod
	 */
	public static Mesh createSphere(float radius, int numVertices, float textureDetail) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		// Start and end polar coordinates, with the number of latitude/longitude lines determined by the LoD
		float startLongitude = 0;
		float startLatitude = 0;
		float endLongitude = (float) Math.PI * 2;
		float endLatitude = (float) Math.PI;
		float stepLongitude = (endLongitude - startLongitude) / numVertices;
		float stepLatitude = (endLatitude - startLatitude) / numVertices;

		// Iterate through all intersections of latitude/longitude lines
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				float longitude = i * stepLongitude + startLongitude;
				float latitude = j * stepLatitude + startLatitude;

				// Get the next lat/long lines
				float nextLongitude = i+1 == numVertices ? endLongitude : (i+1) * stepLongitude + startLongitude;
				float nextLatitude = j+1 == numVertices ? endLatitude : (j+1) * stepLatitude + startLatitude;

				// Get points on the sphere for all combinations of current and next lat/long lines
				// Also, generate texture coordinates as ratios of the lat/long lines to the end lat/long lines
				Vertex point0 = new Vertex(
						sphereParam(longitude, latitude, radius),
						new Vector2f(longitude * textureDetail / endLongitude, latitude * textureDetail / endLatitude)
				);
				Vertex point1 = new Vertex(
						sphereParam(longitude, nextLatitude, radius),
						new Vector2f(longitude * textureDetail / endLongitude, nextLatitude * textureDetail / endLatitude)
				);
				Vertex point2 = new Vertex(
						sphereParam(nextLongitude, latitude, radius),
						new Vector2f(nextLongitude * textureDetail / endLongitude, latitude * textureDetail / endLatitude)
				);
				Vertex point3 = new Vertex(
						sphereParam(nextLongitude, nextLatitude, radius),
						new Vector2f(nextLongitude * textureDetail / endLongitude, nextLatitude * textureDetail / endLatitude)
				);

				// Add vertices for triangles between the lat/long lines
				vertices.add(point0);
				vertices.add(point2);
				vertices.add(point1);
				indices.add(vertices.size() - 3);
				indices.add(vertices.size() - 2);
				indices.add(vertices.size() - 1);

				vertices.add(point3);
				vertices.add(point1);
				vertices.add(point2);
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

	public static Mesh createCylinder(float radius, float height, int numVertices) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		float startAngle = 0;
		float endAngle = (float) Math.PI * 2;
		float stepAngle = (endAngle - startAngle) / numVertices;

		Vertex topCenter = new Vertex(new Vector3f(0, height, 0), new Vector2f(0.5f, 0.5f));
		vertices.add(0, topCenter);
		Vertex bottomCenter = new Vertex(new Vector3f(0, 0, 0), new Vector2f(0.5f, 0.5f));
		vertices.add(1, bottomCenter);

		for (int i = 0; i < numVertices; i++) {
			float angle = i * stepAngle + startAngle;
			float nextAngle = (i+1 == numVertices) ? endAngle : (i+1) * stepAngle + startAngle;

			// Top circle
			Vertex point0 = new Vertex(cylinderParam(angle, radius, height), new Vector2f(((float) (0.5f * Math.cos(angle) + 0.5f)), ((float) (0.5f * Math.sin(angle) + 0.5f))));
			Vertex point1 = new Vertex(cylinderParam(nextAngle, radius, height), new Vector2f(((float) (0.5f * Math.cos(nextAngle) + 0.5f)), ((float) (0.5f * Math.sin(nextAngle) + 0.5f))));

			vertices.add(point0);
			vertices.add(point1);

			indices.add(0);
			indices.add(vertices.size() - 1);
			indices.add(vertices.size() - 2);

			// Bottom circle
			Vertex point2 = new Vertex(cylinderParam(angle, radius, 0), new Vector2f(((float) (0.5f * Math.cos(angle) + 0.5f)), ((float) (0.5f * Math.sin(angle) + 0.5f))));
			Vertex point3 = new Vertex(cylinderParam(nextAngle, radius, 0), new Vector2f(((float) (0.5f * Math.cos(nextAngle) + 0.5f)), ((float) (0.5f * Math.sin(nextAngle) + 0.5f))));

			vertices.add(point2);
			vertices.add(point3);

			indices.add(1);
			indices.add(vertices.size() - 2);
			indices.add(vertices.size() - 1);

			// Center rectangle
			indices.add(vertices.size() - 4);
			indices.add(vertices.size() - 3);
			indices.add(vertices.size() - 1);

			indices.add(vertices.size() - 4);
			indices.add(vertices.size() - 1);
			indices.add(vertices.size() - 2);

		}

		Vertex[] vertArray = new Vertex[vertices.size()];
		vertArray = vertices.toArray(vertArray);

		int[] indexArray = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			indexArray[i] = indices.get(i);
		}

		return new Mesh(vertArray, indexArray, true);
	}

	/**
	 * The parametric equation for a sphere. Used for generating triangles between each line of latitude/longitude on a sphere
	 * @param longitude The current longitude position
	 * @param latitude The current latitude position
	 * @param radius The radius of the sphere
	 * @return The position of the vertex
	 */
	private static Vector3f sphereParam(float longitude, float latitude, float radius) {
		return new Vector3f((float) (Math.cos(longitude) * Math.sin(latitude) * radius), (float) (Math.cos(latitude) * radius), (float) (Math.sin(longitude) * Math.sin(latitude) * radius));
	}

	/**
	 * The parametric equation for a cylinder. Used for generating triangles between each angle on the cylinder
	 * @param angle The current angle
	 * @param radius The radius of the base circle
	 * @param height The height of the cylinder
	 * @return The position of the vertex
	 */
	private static Vector3f cylinderParam(float angle, float radius, float height) {
		return new Vector3f(radius * (float) Math.cos(angle), height, radius * (float) Math.sin(angle));
	}
}
