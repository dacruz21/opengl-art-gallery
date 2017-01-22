package com.typokign.fps;

import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Game {

    private Mesh mesh;

    public Game() {
        mesh = new Mesh();

        Vertex[] data = new Vertex[] {new Vertex(new Vector3f(-0.9f, -0.9f, 0)),
                                      new Vertex(new Vector3f(0, 0.9f, 0)),
                                      new Vertex(new Vector3f(0.9f, -0.9f, 0))};

        mesh.addVertices(data);
    }

    public void input() {
        if (Input.getKeyDown(Keyboard.KEY_UP)) {
            System.out.println("Up pressed!");
        }

        if (Input.getKeyUp(Keyboard.KEY_UP)) {
            System.out.println("Up released!");
        }

        if (Input.getMouseDown(1)) {
            System.out.println("Right clicked at " + Input.getMousePosition() + "!");
        }

        if (Input.getMouseUp(1)) {
            System.out.println("Right released!");
        }
    }

    public void update() {

    }

    public void render() {
        mesh.draw();
    }
}
