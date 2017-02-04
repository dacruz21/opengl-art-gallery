package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.math.Matrix4f;

/**
 * Created by Typo Kign on 2/4/2017.
 */
public class ForwardPoint extends Shader {
	// Singleton
	private static final ForwardPoint instance = new ForwardPoint();

	public static ForwardPoint getInstance() {
		return instance;
	}

	private ForwardPoint() {
		super();

		addVertexShaderFromFile("forwardPoint.vs");
		addFragmentShaderFromFile("forwardPoint.fs");

		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);

		compileShader();

		addUniform("model");
		addUniform("MVP");

		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("cameraPosition");

		addUniform("pointLight.base.color");
		addUniform("pointLight.base.intensity");
		addUniform("pointLight.atten.constant");
		addUniform("pointLight.atten.linear");
		addUniform("pointLight.atten.exponent");
		addUniform("pointLight.position");
		addUniform("pointLight.range");
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

		setUniform("pointLight", getRenderingEngine().getPointLight());
	}

	public void setUniform(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniform(String uniformName, PointLight pointLight) {
		setUniform(uniformName + ".base", pointLight.getBase());
		setUniformf(uniformName + ".atten.constant", pointLight.getAtten().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAtten().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAtten().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
}
