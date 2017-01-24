package com.typokign.fps;

import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Game {

    private Mesh mesh;
    private Shader shader;

    public Game() {
        mesh = new Mesh();
        shader = new Shader();

        Vertex[] data = new Vertex[] {new Vertex(new Vector3f(-0.9f, -0.9f, 0)),
                                      new Vertex(new Vector3f(0, 0.9f, 0)),
                                      new Vertex(new Vector3f(0.9f, -0.9f, 0))};

        mesh.addVertices(data);

        shader.addVertexShader(ResourceLoader.loadShader("basicVertex.vs"));
        shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.fs"));
        shader.compileShader();

        shader.addUniform("maximum");

        shader.bind();
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

    float temp = 0.0f;

    public void update() {
        temp += Time.getDelta();

        shader.setUniformf("maximum", (float) Math.abs(Math.sin(temp)));
    }

    public void render() {
        shader.bind();
        mesh.draw();
    }
}
