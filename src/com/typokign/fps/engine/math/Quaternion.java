package com.typokign.fps.engine.math;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	// Pythagorean theorem again
	public float length() {
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}

	// Set length equal to 1 while maintaining proportions
	public Quaternion normalize() {
		float length = length();

		x /= length;
		y /= length;
		z /= length;
		w /= length;

		return this;
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

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
}
