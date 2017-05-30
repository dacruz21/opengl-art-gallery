package com.typokign.fps.engine.ui;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Typo on 5/28/2017.
 */
public class BoxElement extends UIElement {
	private float x1, y1, x2, y2;

	public BoxElement(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	@Override
	public void draw() {
		super.draw();
		glRectf(x1, x2, y1, y2);
	}

	@Override
	public void onClick() {
		super.onClick();
		System.out.println("clicked");
	}
}
