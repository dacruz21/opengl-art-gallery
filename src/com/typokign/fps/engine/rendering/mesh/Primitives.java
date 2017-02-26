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
	 * Runs in O(n^2) time, where n is vertLOD
	 * @param radius The radius of the sphere
	 * @param vertLOD The level of detail of the sphere. Higher values will produce more vertices and a smoother surface but at the cost of performance
	 * @return A spherical mesh centered around the center point with provided radius and lod
	 */
	public static Mesh createSphere(float radius, int vertLOD, float texLOD) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		// Start and end polar coordinates, with the number of latitude/longitude lines determined by the LoD
		float startLongitude = 0;
		float startLatitude = 0;
		float endLongitude = (float) Math.PI * 2;
		float endLatitude = (float) Math.PI;
		float stepLongitude = (endLongitude - startLongitude) / vertLOD;
		float stepLatitude = (endLatitude - startLatitude) / vertLOD;

		// Iterate through all intersections of latitude/longitude lines
		for (int i = 0; i < vertLOD; i++) {
			for (int j = 0; j < vertLOD; j++) {
				float longitude = i * stepLongitude + startLongitude;
				float latitude = j * stepLatitude + startLatitude;

				// Get the next lat/long lines
				float nextLongitude = i+1 == vertLOD ? endLongitude : (i+1) * stepLongitude + startLongitude;
				float nextLatitude = j+1 == vertLOD ? endLatitude : (j+1) * stepLatitude + startLatitude;

				// Get points on the sphere for all combinations of current and next lat/long lines
				// Also, generate texture coordinates as ratios of the lat/long lines to the end lat/long lines
				Vertex point0 = new Vertex(
						getSphereCoords(longitude, latitude, radius),
						new Vector2f(longitude * texLOD / endLongitude, latitude * texLOD / endLatitude)
				);
				Vertex point1 = new Vertex(
						getSphereCoords(longitude, nextLatitude, radius),
						new Vector2f(longitude * texLOD / endLongitude, nextLatitude * texLOD / endLatitude)
				);
				Vertex point2 = new Vertex(
						getSphereCoords(nextLongitude, latitude, radius),
						new Vector2f(nextLongitude * texLOD / endLongitude, latitude * texLOD / endLatitude)
				);
				Vertex point3 = new Vertex(
						getSphereCoords(nextLongitude, nextLatitude, radius),
						new Vector2f(nextLongitude * texLOD / endLongitude, nextLatitude * texLOD / endLatitude)
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

	/**
	 * Creates a cylinder with a given radius and height
	 * Runs in O(n) time where n is vertLOD
	 * @param radius The radius of the cylinder
	 * @param height The height of the cylinder
	 * @param vertLOD The amount of vertices used when constructing the base circle
	 * @return A cylindrical mesh with the given parameters
	 */
	public static Mesh createCylinder(float radius, float height, int vertLOD) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		float startTheta = 0;
		float endTheta = (float) Math.PI * 2;
		float stepTheta = (endTheta - startTheta) / vertLOD;

		// Centers of the base circles, 0 = bottom, 1 = top
		vertices.add(0, new Vertex(new Vector3f(0, 0, 0), new Vector2f(0.5f, 0.5f)));
		vertices.add(1, new Vertex(new Vector3f(0, height, 0), new Vector2f(0.5f, 0.5f)));

		for (int i = 0; i < vertLOD; i++) {
			float theta = i * stepTheta + startTheta;

			float nextTheta = i+1 == vertLOD ? endTheta : (i+1) * stepTheta + startTheta;

			Vertex point0 = new Vertex(getCylinderCoords(theta, radius, 0));
			Vertex point1 = new Vertex(getCylinderCoords(nextTheta, radius, 0));
			Vertex point2 = new Vertex(getCylinderCoords(theta, radius, height));
			Vertex point3 = new Vertex(getCylinderCoords(nextTheta, radius, height));

			vertices.add(point0);
			vertices.add(point1);
			vertices.add(point2);
			indices.add(vertices.size() - 1);
			indices.add(vertices.size() - 2);
			indices.add(vertices.size() - 3);

			// Fill in the bottom circle by drawing triangles between the current theta, next theta, and bottom center
			indices.add(0);
			indices.add(vertices.size() - 3);
			indices.add(vertices.size() - 2);

			vertices.add(point3);
			vertices.add(point2);
			vertices.add(point1);
			indices.add(vertices.size() - 1);
			indices.add(vertices.size() - 2);
			indices.add(vertices.size() - 3);

			// Fill in the top circle between current theta, next theta, top center
			indices.add(1);
			indices.add(vertices.size() - 3);
			indices.add(vertices.size() - 4);
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
	private static Vector3f getSphereCoords(float longitude, float latitude, float radius) {
		return new Vector3f((float) (Math.cos(longitude) * Math.sin(latitude) * radius), (float) (Math.cos(latitude) * radius), (float) (Math.sin(longitude) * Math.sin(latitude) * radius));
	}

	/**
	 * The parametric equation for a cylinder. Used for generating triangles between each angle on the cylinder
	 * @param theta The current angle
	 * @param radius The radius of the base circle
	 * @param height The height of the cylinder
	 * @return The position of the vertex
	 */
	private static Vector3f getCylinderCoords(float theta, float radius, float height) {
		return new Vector3f(radius * (float) Math.cos(theta), height, radius * (float) Math.sin(theta));
	}
}
