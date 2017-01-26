package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vector3f {

	private float x;
	private float y;
	private float z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Pythagorean theorem, c = sqrt(x^2 + y^2 + z^2)
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	// Dot product
	public float dot(Vector3f other) {
		return x * other.getX() + y * other.getY() + z * other.getZ();
	}

	// Cross product
	public Vector3f crossProduct(Vector3f other) {
		float x_ = y * other.getZ() - z * other.getY();
		float y_ = z * other.getX() - x * other.getZ();
		float z_ = x * other.getY() - y * other.getX();

		return new Vector3f(x_, y_, z_);
	}

	// Normalize, or set length to 1 while maintaining proportions
	public Vector3f normalize() {
		float length = length();

		x /= length;
		y /= length;
		z /= length;

		return this;
	}

	public Vector3f rotate(float angle, Vector3f axis) {
		float sinHalfAngle = (float) Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));

		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;

		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		x = w.getX();
		y = w.getY();
		z = w.getZ();

		return this;
	}

	// Add subtract multiply divide methods
	// Takes either another Vector or a float
	// If vector, operate on x's of both vectors and y's of both vectors
	// If float, perform same operation on both x and y comp of this vector

	public Vector3f add(Vector3f other) {
		return new Vector3f(x + other.getX(), y  + other.getY(), z + other.getZ());
	}

	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}

	public Vector3f sub(Vector3f other) {
		return new Vector3f(x - other.getX(), y  - other.getY(), z - other.getZ());
	}

	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}

	public Vector3f mul(Vector3f other) {
		return new Vector3f(x * other.getX(), y  * other.getY(), z * other.getZ());
	}

	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}

	public Vector3f div(Vector3f other) {
		return new Vector3f(x / other.getX(), y  / other.getY(), z / other.getZ());
	}

	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}

	// Getters and setters

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
