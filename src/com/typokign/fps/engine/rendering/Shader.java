package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.DirectionalLight;
import com.typokign.fps.engine.components.PointLight;
import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.resourcemanagement.ShaderResource;
import com.typokign.fps.engine.rendering.resourcemanagement.TextureResource;

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

	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();

	private ShaderResource resource;
	private String filename;

	public Shader(String filename) {
		this.filename = filename;

		ShaderResource existingResource = loadedShaders.get(filename);

		if (existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = new ShaderResource();
			String vertexShaderSource = loadShader(filename + ".vs");
			String fragmentShaderSource = loadShader(filename + ".fs");

			addVertexShader(vertexShaderSource);
			addFragmentShader(fragmentShaderSource);

			addAllAttributes(vertexShaderSource);

			compileShader();

			addAllUniforms(vertexShaderSource);
			addAllUniforms(fragmentShaderSource);
		}


	}

	public void bind() {
		glUseProgram(resource.getProgram());
	}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f MVPMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		for (int i = 0; i < resource.getUniformNames().size(); i++) {
			String uniformType = resource.getUniformTypes().get(i);
			String uniformName = resource.getUniformNames().get(i);
			String unprefixedUniformName = uniformName.substring(2);

			if (uniformType.equals("sampler2D")) {
				int samplerSlot = renderingEngine.getSamplerSlot(unprefixedUniformName);
				material.getTexture(unprefixedUniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			} else if (uniformName.startsWith("T_")) {
				switch (uniformName) {
					case "T_MVP":
						setUniform(uniformName, MVPMatrix);
						break;
					case "T_model":
						setUniform(uniformName, worldMatrix);
						break;
					default:
						throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			} else if (uniformName.startsWith("R_")) {
				switch(uniformType) {
					case "vec3":
						setUniform(uniformName, renderingEngine.getVector3f(unprefixedUniformName));
						break;
					case "float":
						setUniformf(uniformName, renderingEngine.getFloat(unprefixedUniformName));
						break;
					case "DirectionalLight":
						setUniformDirectionalLight(uniformName, (DirectionalLight) renderingEngine.getActiveLight());
						break;
					case "PointLight":
						setUniformPointLight(uniformName, (PointLight) renderingEngine.getActiveLight());
						break;
					case "SpotLight":
						setUniformSpotLight(uniformName, (SpotLight) renderingEngine.getActiveLight());
						break;
					default:
						renderingEngine.updateUniformStruct(transform, material, this, uniformName, uniformType);
				}
			} else if (uniformName.startsWith("M_")) {
				switch (uniformType) {
					case "vec3":
						setUniform(uniformName, material.getVector3f(unprefixedUniformName));
						break;
					case "float":
						setUniformf(uniformName, material.getFloat(unprefixedUniformName));
						break;
					default:
						throw new IllegalArgumentException("Unhandled Material type \"" + uniformType + "\" of uniform " + uniformName);
				}
			} else if (uniformName.startsWith("C_")) {
				switch (uniformName) {
					case "C_cameraPosition":
						setUniform(uniformName, renderingEngine.getMainCamera().getTransform().getTransformedPosition());
						break;
					default:
						throw new IllegalArgumentException(uniformName + " is not a valid component of the Camera");
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

			resource.getUniformNames().add(uniformName);
			resource.getUniformTypes().add(uniformType);
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

		int uniformLocation = glGetUniformLocation(resource.getProgram(), uniformName);

		// validity test
		if (uniformLocation == 0xffffffff) {
			System.err.println("Error: Could not find uniform: " + uniformName);
			new Exception().printStackTrace();
			System.exit(1);
		}

		resource.getUniforms().put(uniformName, uniformLocation);
	}

	// We refer to the uniform by its string name in the hashmap
	public void setUniformi(String uniformName, int value) {
		glUniform1i(resource.getUniforms().get(uniformName), value);
	}

	public void setUniformf(String uniformName, float value) {
		glUniform1f(resource.getUniforms().get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(resource.getUniforms().get(uniformName), value.getX(), value.getY(), value.getZ());
	}

	public void setUniform(String uniformName, Color value) {
		glUniform3f(resource.getUniforms().get(uniformName), value.getR(), value.getG(), value.getB());
	}

	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(resource.getUniforms().get(uniformName), true, Util.createFlippedBuffer(value));
	}

	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getQuadratic());
		setUniform(uniformName + ".position", pointLight.getTransform().getTransformedPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}

	public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
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
		glBindAttribLocation(resource.getProgram(), location, attributeName);
	}

	public void compileShader() {
		glLinkProgram(resource.getProgram());

		if (glGetProgrami(resource.getProgram(), GL_LINK_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}

		glValidateProgram(resource.getProgram());
		if (glGetProgrami(resource.getProgram(), GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(resource.getProgram(), 1024));
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

		glAttachShader(resource.getProgram(), shader);
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
