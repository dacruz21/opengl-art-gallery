package com.typokign.fps.engine.core;

import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;

/**
 * Created by Typo Kign on 1/23/2017.
 */
public class Transform {
	private Transform parent;
	private Matrix4f parentMatrix;

	// dx, dy, dz of the translation
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;

	private Vector3f oldPosition;
	private Quaternion oldRotation;
	private Vector3f oldScale;

	public Transform() {
		this(new Vector3f(0,0,0), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));
	}

	public Transform(Vector3f position, Quaternion rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;

		parentMatrix = new Matrix4f().initIdentity();
	}

	public void update() {
		if (oldPosition != null || oldRotation != null || oldScale != null) {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		} else {
			oldPosition = new Vector3f().set(position).add(1.0f);
			oldRotation = new Quaternion().set(rotation).mul(0.5f);
			oldScale = new Vector3f().set(scale).add(1.0f);
		}
	}

	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).mul(rotation).normalized();

	}

	public boolean hasChanged() {
		if (parent != null && parent.hasChanged())
			return true;

		return !position.equals(oldPosition) || !rotation.equals(oldRotation) || !scale.equals(oldScale);

	}

	// combine all 3 transformations into a single matrix4f
	public Matrix4f getTransformation() {
		Matrix4f transMatrix = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
		Matrix4f rotMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());



		// order matters :
		// first scale (does not affect center)
		// then rotate (in order to rotate on the pre-existing axis)
		// then translate
		// then set relative to parent
		return getParentMatrix().mul(transMatrix.mul(rotMatrix.mul(scaleMatrix)));
	}

	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();

		return parentMatrix;
	}

	public Vector3f getTransformedPosition() {
		return getParentMatrix().transform(position);
	}

	public Quaternion getTransformedRotation() {
		Quaternion parentRotation = new Quaternion();

		if (parent != null)
			parentRotation = parent.getTransformedRotation();

		return parentRotation.mul(rotation);
	}

	public Transform getParent() {
		return parent;
	}

	public void setParent(Transform parent) {
		this.parent = parent;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		return "Transform{" +
				"position=" + position +
				", rotation=" + rotation +
				", scale=" + scale +
				'}';
	}
}
