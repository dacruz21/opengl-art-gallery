package com.typokign.fps.engine.math;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion() {
		this(0, 0, 0, 1);
	}

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float) Math.sin(angle / 2);
		float cosHalfAngle = (float) Math.cos(angle / 2);

		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}

	// Pythagorean theorem again
	public float length() {
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}

	// Set length equal to 1 while maintaining proportions
	public Quaternion normalized() {
		float length = length();

		return new Quaternion(x / length, y / length, z / length, w / length);
	}

	// Conjugate: all imaginary parts are *-1, real part is left same
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion mul(Quaternion other) {
		// just basic math, no need for comments here
		float w_ = w * other.getW() - x * other.getX() - y * other.getY() - z * other.getZ();
		float x_ = x * other.getW() + w * other.getX() + y * other.getZ() - z * other.getY();
		float y_ = y * other.getW() + w * other.getY() + z * other.getX() - x * other.getZ();
		float z_ = z * other.getW() + w * other.getZ() + x * other.getY() - y * other.getX();

		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion mul(Vector3f r) {
		// just google quaternion mathematics
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

		return new Quaternion(x_, y_, z_, w_);
	}

	public Matrix4f toRotationMatrix() {
		return new Matrix4f().initRotation(getForward(), getUp(), getRight());
	}

	public Vector3f getForward() {
		return new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
	}

	public Vector3f getBack() {
		return new Vector3f(-2.0f * (x*z - w*y), -2.0f * (y*z + w*x), -(1.0f - 2.0f * (x*x + y*y)));
	}

	public Vector3f getUp() {
		return new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
	}

	public Vector3f getDown() {
		return new Vector3f(-2.0f * (x*y + w*z), -(1.0f - 2.0f * (x*x + z*z)), -2.0f * (y*z - w*x));
	}

	public Vector3f getRight() {
		return new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));
	}

	public Vector3f getLeft() {
		return new Vector3f(-(1.0f - 2.0f * (y*y + z*z)), -2.0f * (x*y - w*z), -2.0f * (x*z + w*y));
	}


	// Getters and setters

	public float getX() {
		return x;
	}

	public Quaternion setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Quaternion setY(float y) {
		this.y = y;
		return this;
	}

	public float getZ() {
		return z;
	}

	public Quaternion setZ(float z) {
		this.z = z;
		return this;
	}

	public float getW() {
		return w;
	}

	public Quaternion setW(float w) {
		this.w = w;
		return this;
	}

	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Quaternion set(Quaternion other) {
		set(other.getX(), other.getY(), other.getZ(), other.getW());
		return this;
	}

	@Override
	public String toString() {
		return "Quaternion{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", w=" + w +
				'}';
	}

	public boolean equals(Quaternion other) {
		return x == other.getX() && y == other.getY() && z == other.getZ() && w == other.getW();
	}
}
