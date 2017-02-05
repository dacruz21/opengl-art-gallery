package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.DirectionalLight;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardDirectional extends Shader {
	// Singleton
	private static final ForwardDirectional instance = new ForwardDirectional();

	public static ForwardDirectional getInstance() {
		return instance;
	}

	private ForwardDirectional() {
		super();

		addVertexShaderFromFile("forwardDirectional.vs");
		addFragmentShaderFromFile("forwardDirectional.fs");

		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);

		compileShader();

		addUniform("model");
		addUniform("MVP");

		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("cameraPosition");

		addUniform("directionalLight.base.color"); // directionalLight is a struct, and Java doesn't have structs. thus we access the components of the struct as separate uniforms
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
	}

	@Override
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);

		material.getTexture().bind();

		setUniform("model", worldMatrix);
		setUniform("MVP", projectedMatrix);

		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());

		setUniform("cameraPosition", getRenderingEngine().getMainCamera().getPosition());

		setUniformDirectionalLight("directionalLight", (DirectionalLight) getRenderingEngine().getActiveLight()); // this uses the overloaded methods below, because structs are a huge pain
	}

	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
}
