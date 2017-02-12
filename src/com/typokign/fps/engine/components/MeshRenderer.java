package com.typokign.fps.engine.components;

import com.typokign.fps.engine.rendering.RenderingEngine;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.rendering.Material;
import com.typokign.fps.engine.rendering.Mesh;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class MeshRenderer extends GameComponent {
	private Mesh mesh;
	private Material material;

	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
	}
}
