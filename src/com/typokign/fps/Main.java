package com.typokign.fps;

import com.typokign.fps.engine.core.CoreEngine;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class Main {
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(800, 600, 300, new NewGame());
		engine.createWindow("Spring Final");
		engine.start();
	}
}
