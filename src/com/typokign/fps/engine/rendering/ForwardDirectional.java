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
		super("forwardDirectional");
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

		setUniformDirectionalLight("directionalLight", (DirectionalLight) renderingEngine.getActiveLight()); // this uses the overloaded methods below, because structs are a huge pain
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
