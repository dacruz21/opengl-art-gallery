package com.typokign.fps.engine.rendering.resourcemanagement;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Typo Kign on 2/24/2017.
 */
public class ShaderResource {
	private int program;
	private int refCount;
	private HashMap<String, Integer> uniforms;

	private ArrayList<String> uniformNames;
	private ArrayList<String> uniformTypes;

	public ShaderResource() {
		this.refCount = 1;
		this.program = glCreateProgram();
		this.uniforms = new HashMap<String, Integer>();
		this.uniformNames = new ArrayList<String>();
		this.uniformTypes = new ArrayList<String>();

		if (program == 0) {
			System.err.println("Shader creation failed. Could not find valid memory location.");
			System.exit(1);
		}
	}

	@Override
	protected void finalize() {
		glDeleteBuffers(program);
	}

	public void addReference() {
		refCount++;
	}

	public boolean removeReference() {
		refCount--;
		return refCount == 0;
	}

	public int getProgram() {
		return program;
	}

	public HashMap<String, Integer> getUniforms() {
		return uniforms;
	}

	public ArrayList<String> getUniformNames() {
		return uniformNames;
	}

	public ArrayList<String> getUniformTypes() {
		return uniformTypes;
	}
}
