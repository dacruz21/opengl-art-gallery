package com.typokign.fps.engine.math;

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

	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3]  = x;
		m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3]  = y;
		m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3]  = z;
		m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3]  = 1;

		return this;
	}

	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rotX = new Matrix4f();
		Matrix4f rotY = new Matrix4f();
		Matrix4f rotZ = new Matrix4f();

		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);

		// Perform rotations in each 2D plane
		rotX.m[0][0] = 1;                       rotX.m[0][1] = 0;                       rotX.m[0][2] = 0;                       rotX.m[0][3]  = 0;
		rotX.m[1][0] = 0;                       rotX.m[1][1] = (float) Math.cos(x);     rotX.m[1][2] = (float) -Math.sin(x);    rotX.m[1][3]  = 0;
		rotX.m[2][0] = 0;                       rotX.m[2][1] = (float) Math.sin(x);     rotX.m[2][2] = (float) Math.cos(x);     rotX.m[2][3]  = 0;
		rotX.m[3][0] = 0;                       rotX.m[3][1] = 0;                       rotX.m[3][2] = 0;                       rotX.m[3][3]  = 1;

		rotY.m[0][0] = (float) Math.cos(y);     rotY.m[0][1] = 0;                       rotY.m[0][2] = (float) -Math.sin(y);    rotY.m[0][3]  = 0;
		rotY.m[1][0] = 0;                       rotY.m[1][1] = 1;                       rotY.m[1][2] = 0;                       rotY.m[1][3]  = 0;
		rotY.m[2][0] = (float) Math.sin(y);     rotY.m[2][1] = 0;                       rotY.m[2][2] = (float) Math.cos(y);     rotY.m[2][3]  = 0;
		rotY.m[3][0] = 0;                       rotY.m[3][1] = 0;                       rotY.m[3][2] = 0;                       rotY.m[3][3]  = 1;


		rotZ.m[0][0] = (float) Math.cos(z);     rotZ.m[0][1] = (float) -Math.sin(z);    rotZ.m[0][2] = 0;                       rotZ.m[0][3]  = 0;
		rotZ.m[1][0] = (float) Math.sin(z);     rotZ.m[1][1] = (float) Math.cos(z);     rotZ.m[1][2] = 0;                       rotZ.m[1][3]  = 0;
		rotZ.m[2][0] = 0;                       rotZ.m[2][1] = 0;                       rotZ.m[2][2] = 1;                       rotZ.m[2][3]  = 0;
		rotZ.m[3][0] = 0;                       rotZ.m[3][1] = 0;                       rotZ.m[3][2] = 0;                       rotZ.m[3][3]  = 1;

		// Multiply them together
		m = rotZ.mul(rotY.mul(rotX)).getM();

		return this;
	}

	public Matrix4f initScale(float x, float y, float z) {
		m[0][0] = x;    m[0][1] = 0;    m[0][2] = 0;    m[0][3]  = 0;
		m[1][0] = 0;    m[1][1] = y;    m[1][2] = 0;    m[1][3]  = 0;
		m[2][0] = 0;    m[2][1] = 0;    m[2][2] = z;    m[2][3]  = 0;
		m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3]  = 1;

		return this;
	}

	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float) Math.tan(fov / 2);
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);    m[0][1] = 0;                    m[0][2] = 0;    						m[0][3]  = 0;
		m[1][0] = 0;                            		m[1][1] = 1.0f / tanHalfFOV;    m[1][2] = 0;    						m[1][3]  = 0;
		m[2][0] = 0;                            		m[2][1] = 0;                    m[2][2] = (-zNear - zFar)  / zRange;    m[2][3]  = 2 * zFar * zNear / zRange;
		m[3][0] = 0;                            		m[3][1] = 0;                    m[3][2] = 1;    						m[3][3]  = 0;

		return this;
	}

	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far) { // rectangular view

		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		// scale the scene into the constraints of the rectangular prism, then translate to the prism location
		m[0][0] = 2 / width;	m[0][1] = 0;    		m[0][2] = 0;    		m[0][3]  = -(right + left) / width;
		m[1][0] = 0;    		m[1][1] = 2 / height;	m[1][2] = 0;    		m[1][3]  = -(top + bottom) / height;
		m[2][0] = 0;    		m[2][1] = 0;    		m[2][2] = -2 / depth;	m[2][3]  = -(far + near) / depth; // that -2 is supposed to be there, shhh
		m[3][0] = 0;    		m[3][1] = 0;    		m[3][2] = 0;    		m[3][3]  = 1;

		return this;
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();

		Vector3f r = up.normalized().crossProduct(f); // right vector

		Vector3f u = f.crossProduct(r); // re-calculate the up vector

		return initRotation(f, u, r);
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f f = forward;
		Vector3f r = right; // right vector
		Vector3f u = up; // re-calculate the up vector

		m[0][0] = r.getX();		m[0][1] = r.getY();		m[0][2] = r.getZ();		m[0][3]  = 0;
		m[1][0] = u.getX();     m[1][1] = u.getY();  	m[1][2] = u.getZ();    	m[1][3]  = 0;
		m[2][0] = f.getX();    	m[2][1] = f.getY();    	m[2][2] = f.getZ();    	m[2][3]  = 0;
		m[3][0] = 0;    		m[3][1] = 0;    		m[3][2] = 0;    		m[3][3]  = 1;

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
		float[][] result = new float[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				result[i][j] = m[i][j];

		return result;
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
