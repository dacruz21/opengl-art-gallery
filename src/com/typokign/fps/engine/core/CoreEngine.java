package com.typokign.fps.engine.core;

import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.rendering.Window;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class CoreEngine {
	private boolean isRunning;
	private Game game;
	private RenderingEngine renderingEngine;
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

	public void createWindow(String title) {
		Window.createWindow(width, height, title);
		this.renderingEngine = new RenderingEngine();
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
		double frameCounter = 0;

		game.init();

		double lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning) {
			boolean render = false;

			// Calculate the time between each frame
			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;

			// Calculate how often to tick the game engine
			unprocessedTime += passedTime;
			frameCounter+=passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;

				// Tick here
				if (Window.isCloseRequested()) {
					stop();
				}

				game.input((float) frameTime);
				// TODO: temp
				renderingEngine.input((float) frameTime);
				Input.update();

				game.update((float) frameTime);

				// FPS counter
				if (frameCounter >= 1) {
					System.out.println(frames + " FPS");
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render) {
				renderingEngine.render(game.getRootObject());
				Window.render();
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

	// Garbage collection
	private void cleanUp() {
		Window.dispose();
	}
}
