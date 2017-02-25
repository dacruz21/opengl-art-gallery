package com.typokign.fps.engine.math;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Quaternion {
	private static final float EPSILON = 1e-3f;

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

	public Quaternion(Matrix4f rotation) {
		float trace = rotation.get(0,0) + rotation.get(1,1) + rotation.get(2, 2);

		if (trace > 0) {
			float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
			w = 0.25f / s;
			x = (rotation.get(1, 2) - rotation.get(2, 1)) * s;
			y = (rotation.get(2, 0) - rotation.get(0, 2)) * s;
			z = (rotation.get(0, 1) - rotation.get(1, 0)) * s;
		} else {
			if (rotation.get(0, 0) > rotation.get(1, 1) && rotation.get(0, 0) > rotation.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rotation.get(0, 0) - rotation.get( 1, 1) - rotation.get(2,2));
				w = (rotation.get(1, 2) - rotation.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rotation.get(1, 0) + rotation.get(0, 1)) / s;
				z = (rotation.get(2, 0) + rotation.get(0, 2)) / s;
			} else if (rotation.get(1, 1) > rotation.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rotation.get(1, 1) - rotation.get(0, 0) - rotation.get(2, 2));
				w = (rotation.get(2, 0) - rotation.get(0, 2)) / s;
				x = (rotation.get(1, 0) + rotation.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rotation.get(2, 1) + rotation.get(1, 2)) / s;
			} else {
				float s = 2.0f * (float) Math.sqrt(1.0f + rotation.get(2, 2) - rotation.get(0, 0) - rotation.get(1, 1));
				w = (rotation.get(0, 1) - rotation.get(1, 0)) / s;
				x = (rotation.get(2, 0) + rotation.get(0, 2)) / s;
				y = (rotation.get(1, 2) + rotation.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}

		x /= length();
		y /= length();
		z /= length();
		w /= length();
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

	// Math functions

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

	public Quaternion mul(float r) {
		return new Quaternion(x * r, y * r, z * r, w * r);
	}

	public Quaternion add(Quaternion other) {
		return new Quaternion(x + other.getX(), y + other.getY(), z + other.getZ(), w + other.getW());
	}

	public Quaternion sub(Quaternion other) {
		return new Quaternion(x - other.getX(), y - other.getY(), z - other.getZ(), w - other.getW());
	}

	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
		Vector3f up = new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
		Vector3f right = new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));

		return new Matrix4f().initRotation(forward, up, right);
	}

	public float dot(Quaternion other) {
		return x * other.getX() + y * other.getY() + z * other.getZ() + w * other.getW();
	}

	// Linear interpolation

	public Quaternion lerpNormalized(Quaternion destination, float lerpFactor, boolean shortest) {
		Quaternion correctedDestination = destination;

		if (shortest && this.dot(destination) < 0) {
			correctedDestination = new Quaternion(-destination.getX(), -destination.getY(), -destination.getZ(), -destination.getW());
		}

		return correctedDestination.sub(this).mul(lerpFactor).add(this).normalized();
	}

	public Quaternion lerpSpherical(Quaternion destination, float lerpFactor, boolean shortest) {
		float cos = this.dot(destination);

		Quaternion correctedDestination = destination;

		if (shortest && cos < 0) {
			cos = -cos;
			correctedDestination = new Quaternion(-destination.getX(), -destination.getY(), -destination.getZ(), -destination.getW());
		}

		if (Math.abs(cos) == 1 - EPSILON) {
			return lerpNormalized(correctedDestination, lerpFactor, false);
		}

		float sin = (float) Math.sqrt(1.0f - cos * cos); // the only way to get the cosine of a Quaternion is the dot product, so other trig functions need to be derived with identities
		float angle = (float) Math.atan2(sin, cos);
		float csc = 1.0f/sin;

		float sourceFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * csc;
		float destinationFactor = (float) Math.sin((lerpFactor) * angle) * csc;

		return this.mul(sourceFactor).add(correctedDestination.mul(destinationFactor));
	}

	// Direction getters

	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}

	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}

	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}

	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}

	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}

	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
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
