package com.typokign.fps.engine.rendering;

/**
 * Created by Typo Kign on 2/5/2017.
 */
public class Color {
	private float r;
	private float g;
	private float b;
	private float a;

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	public Color(int r, int g, int b, int a) {
		this( (float) r / 255.0f,
			  (float) g / 255.0f,
			  (float) b / 255.0f,
			  (float) a / 255.0f);
	}

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public float max() {
		return Math.max(r, Math.max(g, b)); //TODO: do we need to add alpha here? probably not
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getOpacity() {
		return a;
	}

	public void setOpacity(float opacity) {
		this.a = opacity;
	}
}
