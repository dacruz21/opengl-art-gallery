package com.typokign.fps;

import com.typokign.fps.engine.core.CoreEngine;

import java.io.File;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class Main {
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("native/windows").getAbsolutePath());

		CoreEngine engine = new CoreEngine(1024, 768, 300, new ArtGallery());
		engine.createWindow("Art Gallery");
		engine.start();
	}
}
