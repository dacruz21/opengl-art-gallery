package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Shader {
	private final static String INCLUDE_DIRECTIVE = "#include";
	private final static String UNIFORM_KEYWORD = "uniform";
	private final static String ATTRIBUTE_KEYWORD = "attribute";
	private final static String STRUCT_KEYWORD = "struct";

	private class GLSLStruct {
		public String type;
		public String name;

		public GLSLStruct(String type, String name) {
			this.type = type;
			this.name = name;
		}
	}

	// pointer
	private int program;

	// uniforms are basically variables that can be sent to the shader program from within Java
	// collection of String name to Integer pointer location
	private HashMap<String, Integer> uniforms;

	private ArrayList<String> uniformNames;
	private ArrayList<String> uniformTypes;

	public Shader(String filename) {
		uniforms = new HashMap<String, Integer>();
		uniformNames = new ArrayList<String>();
		uniformTypes = new ArrayList<String>();

		program = glCreateProgram();

		if (program == 0) {
			System.err.println("Shader creation failed. Could not find valid memory location in constructor.");
			System.exit(1);
		}

		String vertexShaderSource = loadShader(filename + ".vs");
		String fragmentShaderSource = loadShader(filename + ".fs");

		addVertexShader(vertexShaderSource);
		addFragmentShader(fragmentShaderSource);

		addAllAttributes(vertexShaderSource);

		compileShader();

		addAllUniforms(vertexShaderSource);
		addAllUniforms(fragmentShaderSource);
	}

	public void bind() {
		glUseProgram(program);
	}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f MVPMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		for (int i = 0; i < uniformNames.size(); i++) {
			String uniformType = uniformTypes.get(i);
			String uniformName = uniformNames.get(i);
			String unprefixedUniformName = uniformName.substring(2);

			if (uniformName.startsWith("T_")) {
				switch (uniformName) {
					case "T_MVP":
						setUniform(uniformName, MVPMatrix);
						break;
					case "T_world":
						setUniform(uniformName, worldMatrix);
						break;
					default:
						throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			} else if (uniformName.startsWith("R_")) {
				switch(uniformType) {
					case "sampler2D":
						int samplerSlot = renderingEngine.getSamplerSlot(unprefixedUniformName);
						material.getTexture(unprefixedUniformName).bind(samplerSlot);
						setUniformi(uniformName, samplerSlot);
						break;
					case "vec3":
						setUniform(uniformName, renderingEngine.getVector3f(unprefixedUniformName));
						break;
					case "float":
						setUniformf(uniformName, renderingEngine.getFloat(unprefixedUniformName));
						break;
					default:
						throw new IllegalArgumentException("Unhandled RenderingEngine type \"" + uniformType +"\" of uniform " + uniformName);
				}
			} else if (uniformName.startsWith("M_")) {
				switch(uniformType) {
					case "vec3":
						setUniform(uniformName, material.getVector3f(unprefixedUniformName));
						break;
					case "float":
						setUniformf(uniformName, material.getFloat(unprefixedUniformName));
					default:
						throw new IllegalArgumentException("Unhandled Material type \"" + uniformType +"\" of uniform " + uniformName);
				}
			} else {
				throw new IllegalArgumentException("Unhandled or non-existent uniform prefix in uniform " + uniformName);
			}
		}
	}

	public void addAllAttributes(String shaderSource) {
		int attributeStartLocation = shaderSource.indexOf(ATTRIBUTE_KEYWORD);
		int attributeNumber = 0;
		while (attributeStartLocation != -1) {
			int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end = shaderSource.indexOf(";", begin);

			String attributeParams = shaderSource.substring(begin, end);
			int separatorPosition = attributeParams.indexOf(" ");

//			String attributeType = attributeParams.substring(0, separatorPosition);
			String attributeName = attributeParams.substring(separatorPosition + 1, attributeParams.length());

			setAttribLocation(attributeName, attributeNumber);
			attributeNumber++;

			attributeStartLocation = shaderSource.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
		}
	}

	private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderSource) {
		String structPattern = "\\s*" + STRUCT_KEYWORD + "\\s+([a-zA-Z0-9]+)\\s*\\{([^\\}]*)\\s*\\}\\s*";
		String structDataPattern = "([a-zA-Z0-9-_]+)\\s*([a-zA-Z0-9-_]+)\\s*;";

		HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();

		Pattern structMatcher = Pattern.compile(structPattern);
		Matcher matchMatcher = structMatcher.matcher(shaderSource);
		while (matchMatcher.find()) {
			String structName = matchMatcher.group(1);
			String structData = matchMatcher.group(2);

			ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();

			Pattern dataMatcher = Pattern.compile(structDataPattern);
			Matcher matcher = dataMatcher.matcher(structData);

			while (matcher.find()) {
				String structComponentType = matcher.group(1);
				String structComponent = matcher.group(2);

				glslStructs.add(new GLSLStruct(structComponentType, structComponent));
			}

			result.put(structName, glslStructs);
		}

		return result;
	}

	public void addAllUniforms(String shaderSource) {
		HashMap<String, ArrayList<GLSLStruct>> structs = findUniformStructs(shaderSource);

		int uniformStartLocation = shaderSource.indexOf(UNIFORM_KEYWORD);
		while (uniformStartLocation != -1) {
			int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
			int end = shaderSource.indexOf(";", begin);

			String uniformParams = shaderSource.substring(begin, end);
			int separatorPosition = uniformParams.indexOf(" ");

			String uniformType = uniformParams.substring(0, separatorPosition);
			String uniformName = uniformParams.substring(separatorPosition + 1, uniformParams.length());

			addUniform(uniformName, uniformType, structs);

			uniformStartLocation = shaderSource.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
		}
	}

	private void addUniform(String uniformName, String uniformType, HashMap<String, ArrayList<GLSLStruct>> structs) {
		boolean addThis = true;
		ArrayList<GLSLStruct> structComponents = structs.get(uniformType);

		if (structComponents != null) {
			addThis = false;

			for (GLSLStruct struct : structComponents) {
				addUniform(uniformName + "." + struct.name, struct.type, structs);
			}
		}

		if (!addThis)
			return;

		int uniformLocation = glGetUniformLocation(program, uniformName);

		// validity test
		if (uniformLocation == 0xffffffff) {
			System.err.println("Error: Could not find uniform: " + uniformName);
			new Exception().printStackTrace();
			System.exit(1);
		}

		uniforms.put(uniformName, uniformLocation);
		uniformNames.add(uniformName);
		uniformTypes.add(uniformType);
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
