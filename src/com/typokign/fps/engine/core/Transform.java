package com.typokign.fps.engine.core;

import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.Camera;

/**
 * Created by Typo Kign on 1/23/2017.
 */
public class Transform {

	// dx, dy, dz of the translation
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;

	public Transform() {
		position = new Vector3f(0,0,0);
		rotation = new Quaternion(0, 0, 0, 1);

		// note to self; don't set this default to 0,0,0. 48 hours of debugging for this
		scale = new Vector3f(1, 1, 1);
	}

	public Transform(Vector3f position, Quaternion rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
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
		return transMatrix.mul(rotMatrix.mul(scaleMatrix));
	}

	public Matrix4f getProjectedTransformation(Camera camera) {
		return camera.getViewProjection().mul(getTransformation());
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
}
