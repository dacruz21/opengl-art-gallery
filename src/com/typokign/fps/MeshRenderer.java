package com.typokign.fps;

import com.typokign.fps.engine.core.GameComponent;
import com.typokign.fps.engine.core.Transform;
import com.typokign.fps.engine.rendering.Material;
import com.typokign.fps.engine.rendering.Mesh;
import com.typokign.fps.engine.rendering.Shader;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class MeshRenderer implements GameComponent {

	private Mesh mesh;
	private Material material;

	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public void input(Transform transform) {

	}

	@Override
	public void update(Transform transform) {

	}

	@Override
	public void render(Transform transform, Shader shader) {
		shader.bind();
		shader.updateUniforms(transform, material);
		mesh.draw();
	}
}
