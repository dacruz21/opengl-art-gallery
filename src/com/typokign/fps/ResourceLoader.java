package com.typokign.fps;

import java.io.BufferedReader;
import java.io.FileReader;

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
}
