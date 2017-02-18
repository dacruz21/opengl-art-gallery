package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Shader {
	private final static String INCLUDE_DIRECTIVE = "#include";
	private final static String UNIFORM_KEYWORD = "uniform";
	private final static String ATTRIBUTE_KEYWORD = "attribute";

	// pointer
	private int program;

	// uniforms are basically variables that can be sent to the shader program from within Java
	// collection of String name to Integer pointer location
	private HashMap<String, Integer> uniforms;

	public Shader() {
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();

		if (program == 0) {
			System.err.println("Shader creation failed. Could not find valid memory location in constructor.");
			System.exit(1);
		}
	}

	public void bind() {
		glUseProgram(program);
	}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {}

	public void addAllAttributes(String attributeSource) {
		int attributeStartLocation = attributeSource.indexOf(ATTRIBUTE_KEYWORD);
		int attributeNumber = 0;
		while (attributeStartLocation != -1) {
			int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end = attributeSource.indexOf(";", begin);

			String attributeParams = attributeSource.substring(begin, end);
			int separatorPosition = attributeParams.indexOf(" ");

			String attributeType = attributeParams.substring(0, separatorPosition);
			String attributeName = attributeParams.substring(separatorPosition + 1, attributeParams.length());

			setAttribLocation(attributeName, attributeNumber);
			attributeNumber++;

			attributeStartLocation = attributeSource.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}

	public void addAllUniforms(String shaderSource) {
		int uniformStartLocation = shaderSource.indexOf(UNIFORM_KEYWORD);
		while (uniformStartLocation != -1) {
			int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			int end = shaderSource.indexOf(";", begin);

			String uniformParams = shaderSource.substring(begin, end);
			int separatorPosition = uniformParams.indexOf(" ");

			String uniformType = uniformParams.substring(0, separatorPosition);
			String uniformName = uniformParams.substring(separatorPosition + 1, uniformParams.length());

			addUniform(uniformName);

			uniformStartLocation = shaderSource.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}

	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(program, uniform);

		// validity test
		if (uniformLocation == 0xffffffff) {
			System.err.println("Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}

		uniforms.put(uniform, uniformLocation);
	}

	// We refer to the uniform by its string name in the hashmap
	public void setUniformi(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniformf(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
	}

	public void setUniform(String uniformName, Color value) {
		glUniform3f(uniforms.get(uniformName), value.getR(), value.getG(), value.getB());
	}

	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
	}

	public void addVertexShaderFromFile(String text) {
		addProgram(loadShader(text), GL_VERTEX_SHADER);
	}

	public void addFragmentShaderFromFile(String text) {
		addProgram(loadShader(text), GL_FRAGMENT_SHADER);
	}

	public void addGeometryShaderFromFile(String text) {
		addProgram(loadShader(text), GL_GEOMETRY_SHADER);
	}

	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}

	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}

	public void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}

	public void setAttribLocation(String attributeName, int location) {
		glBindAttribLocation(program, location, attributeName);
	}

	public void compileShader() {
		glLinkProgram(program);

		if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(program, 1024));
			System.exit(1);
		}

		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(program, 1024));
			System.exit(1);
		}
	}

	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);

		if (shader == 0) {
			System.err.println("Shader creation failed. Could not find valid memory location when adding shader.");
			System.exit(1);
		}

		glShaderSource(shader, text);
		glCompileShader(shader);

		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}

		glAttachShader(program, shader);
	}

	public static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader;

		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;

			while ((line  = shaderReader.readLine()) != null) {
				if (line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
				} else {
					shaderSource.append(line).append("\n");
				}
			}

			shaderReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}
}
