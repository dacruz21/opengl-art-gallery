package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.PointLight;
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
		super("forwardPoint");
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		material.getTexture("diffuse").bind();

		setUniform("model", worldMatrix);
		setUniform("MVP", projectedMatrix);

		setUniformf("specularIntensity", material.getFloat("specularIntensity"));
		setUniformf("specularExponent", material.getFloat("specularExponent"));

		setUniform("cameraPosition", renderingEngine.getMainCamera().getTransform().getTransformedPosition());

		setUniformPointLight("pointLight", (PointLight) renderingEngine.getActiveLight());
	}

	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getQuadratic());
		setUniform(uniformName + ".position", pointLight.getTransform().getTransformedPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
}
