package com.typokign.fps;

/**
 * Created by Typo Kign on 1/23/2017.
 */
public class Transform {
    // dx, dy, dz of the translation
    private Vector3f translation;
    private Vector3f rotation;

    public Transform() {
        translation = new Vector3f(0,0,0);
        rotation = new Vector3f(0, 0, 0);
    }

    // combine all 3 transformations into a single matrix4f
    public Matrix4f getTransformation() {
        Matrix4f transMatrix = new Matrix4f().initTranslation(translation.getX(), translation.getY(), translation.getZ());
        Matrix4f rotMatrix = new Matrix4f().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());

        // order matters, rotation matrix needs to be in params to rotate the object on its own axis, rather than translating and then rotating around a center point
        return transMatrix.mul(rotMatrix);
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public void setTranslation(float x, float y, float z) {
        this.translation = new Vector3f(x, y, z);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x, y, z);
    }
}
