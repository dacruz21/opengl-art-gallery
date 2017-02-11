package com.typokign.fps.engine.core;

import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.components.Camera;

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

	public boolean hasChanged() {
		if (oldPosition == null || oldRotation == null || oldScale == null) {
			oldPosition = new Vector3f().set(position);
			oldRotation = new Quaternion().set(rotation);
			oldScale = new Vector3f().set(scale);
			return true;
		}

		if (parent != null && parent.hasChanged())
			return true;

		if (!position.equals(oldPosition) || !rotation.equals(oldRotation) || !scale.equals(oldScale))
			return true;

		return false;
	}

	// combine all 3 transformations into a single matrix4f
	public Matrix4f getTransformation() {
		Matrix4f transMatrix = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
		Matrix4f rotMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());

		if (parent != null && parent.hasChanged()) {
			parentMatrix = parent.getTransformation();
		}

		if (oldPosition != null || oldRotation != null || oldScale != null) {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		}

		// order matters :
		// first scale (does not affect center)
		// then rotate (in order to rotate on the pre-existing axis)
		// then translate
		// then set relative to parent
		return parentMatrix.mul(transMatrix.mul(rotMatrix.mul(scaleMatrix)));
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
