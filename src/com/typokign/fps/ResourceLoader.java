package com.typokign.fps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class ResourceLoader {

    public static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));

            String line;
            while ((line  = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }

    public static Mesh loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\."); // get the file extension via regex splitting
        String fileExtension = splitArray[splitArray.length - 1]; // last element will be extension

        if (!fileExtension.equals("obj")) {
            System.err.println("Error: file format not supported for mesh data: " + fileExtension);
            new Exception().printStackTrace();
            System.exit(1);
        }

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        try {
            BufferedReader meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));

            String line;
            while ((line  = meshReader.readLine()) != null) {
                String[] tokens = line.split(" "); // see a sample obj from the models directory, each line in obj is a v(ertex) or f(ace), followed by x,y,z or triangular indexes
                tokens = Util.removeEmptyStrings(tokens);

                if (tokens.length == 0 || tokens[0].equals("#")) { // blank line or comment
                    continue;
                } else if (tokens[0].equals("v")) { // vertex
                    vertices.add(new Vertex(new Vector3f(Float.valueOf(tokens[1]),
                                                         Float.valueOf(tokens[2]),
                                                         Float.valueOf(tokens[3])))); // quite the complicated nest, but really just create a vertex with the x,y,z separated by spaces
                } else if (tokens[0].equals("f")) { // face
                    indices.add(Integer.parseInt(tokens[1]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1
                    indices.add(Integer.parseInt(tokens[2]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1
                    indices.add(Integer.parseInt(tokens[3]) - 1); // obj indices are 1-indexed, our system is 0-indexed, so subtract 1
                }
            }

            meshReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Mesh result = new Mesh();
        Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
        Integer[] indexData = indices.toArray(new Integer[indices.size()]);
        result.addVertices(vertexData, Util.toIntArray(indexData));

        return result;
    }
}
