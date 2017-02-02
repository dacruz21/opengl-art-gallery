package com.typokign.fps.engine.core;

import com.typokign.fps.Game;
import com.typokign.fps.engine.rendering.RenderUtil;
import com.typokign.fps.engine.rendering.Window;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class CoreEngine {
	private boolean isRunning;
	private Game game;
	private int width;
	private int height;
	private double frameTime; // not framerate, but the amount of time allowed to each frame

	public CoreEngine(int width, int height, double framerate, Game game) {
		this.isRunning = false;
		this.game = game;
		this.width = width;
		this.height = height;
		this.frameTime = 1 / framerate;
	}

	private void initializeRenderingSystem() {
		System.out.println("OpenGL Version: " + RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
	}

	public void createWindow(String title) {
		Window.createWindow(width, height, title);
		initializeRenderingSystem();
	}

	// Initialization code
	public void start() {
		if (isRunning) return;

		game.init();
		run();
	}

	// Controlling the isRunning boolean
	public void stop() {
		if (!isRunning) return;

		isRunning = false;
	}

	// CoreEngine loop
	private void run() {
		isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		game.init();

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning) {
			boolean render = false;

			// Calculate the time between each frame
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			// Calculate how often to tick the game engine
			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter+=passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;

				// Tick here
				if (Window.isCloseRequested()) {
					stop();
				}

				Time.setDelta(frameTime);

				game.input();
				Input.update();

				game.update();

				// FPS counter
				if (frameCounter >= Time.SECOND) {
					System.out.println(frames + " FPS");
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render) {
				render();
				game.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		cleanUp();
	}

	// Display updating
	private void render() {
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}

	// Garbage collection
	private void cleanUp() {
		Window.dispose();
	}
}
