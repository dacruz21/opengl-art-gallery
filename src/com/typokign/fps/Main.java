package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Main {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Spring Final";
	public static final double FRAME_CAP = 5000.0;

	private boolean isRunning;
	private Game game;

	public Main() {
		System.out.println("OpenGL Version: " + RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
	}

	// Initialization code
	public void start() {
		if (isRunning) return;

		run();
	}

	// Controlling the isRunning boolean
	public void stop() {
		if (!isRunning) return;

		isRunning = false;
	}

	// Main loop
	private void run() {
		isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		// The length of one frame, frequency (frame cap)^-1
		final double frameTime = 1.0 / FRAME_CAP;

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
				Input.update();

				game.input();
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

	public static void main(String[] args) {
		Window.createWindow(WIDTH, HEIGHT, TITLE);

		Main main = new Main();
		main.start();
	}
}
