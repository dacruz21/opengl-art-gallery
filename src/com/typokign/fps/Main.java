package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Main {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Spring Final";

    public Main() {

    }

    public void start() {
        run();
    }

    public void stop() {

    }

    public void run() {
        while (!Window.isCloseRequested()) {
            render();
        }
    }

    public void render() {
        Window.render();
    }

    public void cleanUp() {

    }

    public static void main(String[] args) {
        Window.createWindow(WIDTH, HEIGHT, TITLE);

        Main game = new Main();
        game.start();
    }
}
