package com.typokign.fps.engine.math;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Vector2f {

	private float x;
	private float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	// Pythagorean theorem, c = sqrt(x^2 + y^2)
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	// Dot product
	public float dot(Vector2f other) {
		return x * other.getX() + y * other.getY();
	}

	public float max() {
		return Math.max(x, y);
	}

	// Normalize, or set length to 1 while maintaining proportions
	public Vector2f normalized() {
		return new Vector2f(x / length(), y / length());
	}

	public float crossProduct(Vector2f other) {
		return x * other.getY() - y * other.getX();
	}

	public Vector2f rotate(float angle) {

		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	// Linear extrapolation

	public Vector2f lerp(Vector2f destination, float lerpFactor) {
		return destination.sub(this).mul(lerpFactor).add(this);
	}

	// Add subtract multiply divide methods
	// Takes either another Vector or a float
	// If vector, operate on x's of both vectors and y's of both vectors
	// If float, perform same operation on both x and y comp of this vector

	public Vector2f add(Vector2f other) {
		return new Vector2f(x + other.getX(), y  + other.getY());
	}

	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}

	public Vector2f sub(Vector2f other) {
		return new Vector2f(x - other.getX(), y  - other.getY());
	}

	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}

	public Vector2f mul(Vector2f other) {
		return new Vector2f(x * other.getX(), y  * other.getY());
	}

	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}

	public Vector2f div(Vector2f other) {
		return new Vector2f(x / other.getX(), y  / other.getY());
	}

	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}

	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	// Getters & setters

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

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public boolean equals(Vector2f other) {
		return x == other.getX() && y == other.getY();
	}
}
