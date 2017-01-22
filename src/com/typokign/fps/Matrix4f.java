package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Matrix4f {
    private float[][] m;

    public Matrix4f() {
        m = new float[4][4];
    }

    // Set the matrix equal to the identity matrix
    // Identity matrix:
    // [1, 0, 0, 0]
    // [0, 1, 0, 0]
    // [0, 0, 1, 0]
    // [0, 0, 0, 1]
    public Matrix4f initIdentity() {
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3]  = 0;
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3]  = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3]  = 0;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3]  = 1;

        return this;
    }

    public Matrix4f mul(Matrix4f other) {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.set(i, j, (m[i][0] * other.get(0, j) +
                                  m[i][1] * other.get(1, j) +
                                  m[i][2] * other.get(2, j) +
                                  m[i][3] * other.get(3, j)));
            }
        }

        return result;
    }

    // Getters and setters

    public float[][] getM() {
        return m;
    }

    public float get(int x, int y) {
        return m[x][y];
    }

    public void setM(float[][] m) {
        this.m = m;
    }

    public void set(int x, int y, float value) {
        m[x][y] = value;
    }
}