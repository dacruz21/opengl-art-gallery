package com.typokign.fps.engine.rendering.mesh;

import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;

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
				new Vertex(rdf, new Vector2f(1, 0)), // rdf
				new Vertex(rdb, new Vector2f(1, 1)), // rdb
				new Vertex(ldb, new Vector2f(0, 1)), // ldb
				new Vertex(ldf, new Vector2f(0, 0)), // ldf
				new Vertex(ruf, new Vector2f(1, 1)), // ruf
				new Vertex(rub, new Vector2f(1, 0)), // rub
				new Vertex(lub, new Vector2f(0, 0)), // lub
				new Vertex(luf, new Vector2f(0, 1)) // luf
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
	 * @return
	 */
	public static Mesh createSphere(Vector3f center, float radius, int lod) {
		return null;
	}
}
