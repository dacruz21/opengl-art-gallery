package com.typokign.fps;

/**
 * Created by Typo Kign on 1/23/2017.
 */
public class Transform {
    // dx, dy, dz of the translation
    private Vector3f translation;

    public Transform() {
        translation = new Vector3f(0,0,0);
    }

    // combine all 3 transformations into a single matrix4f
    public Matrix4f getTransformation() {
        Matrix4f transMatrix = new Matrix4f();
        transMatrix.initTranslation(translation.getX(), translation.getY(), translation.getZ());

        return transMatrix;
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
}
