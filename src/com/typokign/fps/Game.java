package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Spring Final";

    private boolean isRunning;

    public Game() {
        isRunning = false;
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

    // Game loop
    private void run() {
        isRunning = true;

        while (isRunning) {
            if (Window.isCloseRequested()) {
                stop();
            }

            render();
        }

        cleanUp();
    }

    // Display updating
    private void render() {
        Window.render();
    }

    // Garbage collection
    private void cleanUp() {
        Window.dispose();
    }

    public static void main(String[] args) {
        Window.createWindow(WIDTH, HEIGHT, TITLE);

        Game game = new Game();
        game.start();
    }
}
