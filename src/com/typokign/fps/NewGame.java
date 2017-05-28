package com.typokign.fps;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.typokign.fps.engine.audio.Sound;
import com.typokign.fps.engine.components.*;
import com.typokign.fps.engine.core.Game;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.physics.PhysicsObject;
import com.typokign.fps.engine.rendering.Color;
import com.typokign.fps.engine.rendering.Material;
import com.typokign.fps.engine.rendering.Texture;
import com.typokign.fps.engine.rendering.Window;
import com.typokign.fps.engine.rendering.mesh.Mesh;
import com.typokign.fps.engine.rendering.mesh.Primitives;

/**
 * Created by Typo on 5/5/2017.
 */
public class NewGame extends Game {
	@Override
	public void init() {
		// Gray material
		Material grayMaterial = new Material();
		grayMaterial.addTexture("diffuse", new Texture("gray.png"));
		grayMaterial.addFloat("specularIntensity", 0.7f);
		grayMaterial.addFloat("specularExponent", 2);

		// Camera
		Camera mainCamera = new Camera((float) Math.toRadians(70f), Window.getWidth() / Window.getHeight(), 0.01f, 1000f);
		FreeLook freeLook = new FreeLook();

		GameObject cameraObj = new GameObject();
		cameraObj.addComponent(mainCamera);
		cameraObj.addComponent(freeLook);
		cameraObj.setPosition(0f, 1f, 0f);
		addObject(cameraObj);

		// Directional light
		DirectionalLight dLight = new DirectionalLight(new Color(0.988f,0.953f,0.851f), 0.2f);

		GameObject dLightObj = new GameObject();
		dLightObj.addComponent(dLight);
		addObject(dLightObj);

		dLightObj.setRotation(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));

		// Plane
		Mesh cuboid = Primitives.createCuboid(new Vector3f(-10, -1, -10), new Vector3f(10, 0, 10), 8);

		Material material = new Material();
		material.addTexture("diffuse", new Texture("grass.png"));
		material.addFloat("specularIntensity", 0.2f);
		material.addFloat("specularExponent", 2);

		MeshRenderer plane = new MeshRenderer(cuboid, material);

		PhysicsObject planeObj = new PhysicsObject(new BoxShape(new Vector3f(5f, 0.5f, 5f).asJavaX()), 0f);
		planeObj.addComponent(plane);
		planeObj.setPosition(0, 0, 0);
		addObject(planeObj);

		// Bee statue + sound
		MeshRenderer beeStatue = new MeshRenderer(new Mesh("bee.obj"), grayMaterial);
		PointSound beeSound = new PointSound(new Sound("bee.wav"), true, true);

		GameObject beeObj = new GameObject();
		beeObj.addComponent(beeStatue);
		beeObj.addComponent(beeSound);
		beeObj.setPosition(5, 2, 0);
		addObject(beeObj);

		// Physical monkey
		MeshRenderer monkey = new MeshRenderer(new Mesh("monkey.obj"), grayMaterial);

		PhysicsObject monkeyObj = new PhysicsObject(new SphereShape(2.0f), 10f);
		monkeyObj.addComponent(monkey);
		monkeyObj.setPosition(0, 50, 0);
		addObject(monkeyObj);
	}
}
