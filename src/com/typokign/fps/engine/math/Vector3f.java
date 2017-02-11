package com.typokign.fps.engine.math;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vector3f {

	private float x;
	private float y;
	private float z;

	public Vector3f() {
		this(0, 0, 0);
	}

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

	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	// Cross product
	public Vector3f crossProduct(Vector3f other) {
		float x_ = y * other.getZ() - z * other.getY();
		float y_ = z * other.getX() - x * other.getZ();
		float z_ = x * other.getY() - y * other.getX();

		return new Vector3f(x_, y_, z_);
	}

	// Normalize, or set length to 1 while maintaining proportions
	public Vector3f normalized() {
		return new Vector3f(x / length(), y / length(), z / length());
	}

	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	public Vector3f rotate(Vector3f axis, float angle) {
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);

		return this.crossProduct(axis.mul(sinAngle)).add(           //Rotation on local X
				(this.mul(cosAngle)).add(                     //Rotation on local Z
						axis.mul(this.dot(axis.mul(1 - cosAngle))))); //Rotation on local Y
	}

	// Linear interpolation
	public Vector3f lerp(Vector3f destination, float lerpFactor) {
		return destination.sub(this).mul(lerpFactor).add(this);
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

	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	// Swizzling

	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	// Getters and setters

	public float getX() {
		return x;
	}

	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}

	public float getZ() {
		return z;
	}

	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f set(Vector3f other) {
		set(other.getX(), other.getY(), other.getZ());
		return this;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public boolean equals(Vector3f other) {
		return x == other.getX() && y == other.getY() && z == other.getZ();
	}
}
