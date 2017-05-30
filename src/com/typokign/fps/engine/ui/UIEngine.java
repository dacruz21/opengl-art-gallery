package com.typokign.fps.engine.ui;

import com.typokign.fps.engine.core.Input;

import java.util.ArrayList;

/**
 * Created by Typo on 5/28/2017.
 */
public class UIEngine {
	private ArrayList<UIElement> elements;

	public UIEngine() {
		elements = new ArrayList<UIElement>();
	}

	public void render() {
		for (UIElement element: elements) {
			element.draw();
		}
	}

	public void update() {
		if (Input.getMouseDown(0)) {
			for (UIElement element : elements) {
				if (element.mouseIntersects(Input.getMousePosition().getX(), Input.getMousePosition().getY())) {
					element.onClick();
				}
			}
		}
	}

	public void addElement(UIElement element) {
		elements.add(element);
	}
}
