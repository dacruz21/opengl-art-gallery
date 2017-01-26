package com.typokign.fps;

import org.lwjgl.input.Keyboard;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Game {

    private Mesh mesh;
    private Shader shader;
    private Transform transform;

    public Game() {
        mesh = ResourceLoader.loadMesh("cube.obj");
        shader = new Shader();

//        Vertex[] vertices = new Vertex[] {new Vertex(new Vector3f(-0.9f, -0.9f, 0)),
//                                      new Vertex(new Vector3f(0, 0.9f, 0)),
//                                      new Vertex(new Vector3f(0.9f, -0.9f, 0)),
//                                      new Vertex((new Vector3f(0f, -0.9f, 0.9f)))};
//
//        int[] indices = new int[] {0,1,3,
//                                   3,1,2,
//                                   2,1,0,
//                                   0,2,3};

//        mesh.addVertices(vertices, indices);

        transform = new Transform();
        transform.setProjection(70, Main.WIDTH, Main.HEIGHT, 0.1f, 1000);

        shader.addVertexShader(ResourceLoader.loadShader("basicVertex.vs"));
        shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.fs"));
        shader.compileShader();

        shader.addUniform("transform");
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

        float sinTemp = (float) Math.sin(temp);

        transform.setTranslation((float) sinTemp, 0, 5);
        transform.setRotation(0, sinTemp * 180, 0);
        transform.setScale(sinTemp, sinTemp, sinTemp);
    }

    public void render() {
        shader.bind();
        shader.setUniform("transform", transform.getProjectedTransformation());
        mesh.draw();
    }
}
