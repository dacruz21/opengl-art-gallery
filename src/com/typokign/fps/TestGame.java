package com.typokign.fps;

import com.typokign.fps.components.Flashlight;
import com.typokign.fps.components.FollowCameraComponent;
import com.typokign.fps.engine.components.*;
import com.typokign.fps.engine.core.Game;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.*;
import com.typokign.fps.engine.rendering.mesh.Mesh;
import com.typokign.fps.engine.rendering.mesh.Primitives;
import com.typokign.fps.engine.rendering.mesh.Vertex;
import org.lwjgl.input.Keyboard;

import java.util.Random;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class TestGame extends Game {
	Random random;
	String[] args;

	public TestGame(String[] args) {
		super();
		this.random = new Random();
		this.args = args;
	}

	public void init() {
		Mesh mesh = Primitives.createCuboid(new Vector3f(-10, -1, -10),new Vector3f(10, 0, 10));

		Material material = new Material();
		material.addTexture("diffuse", new Texture("text.png"));
		material.addFloat("specularIntensity", 0.5f);
		material.addFloat("specularExponent", 8);

		Material grayMat = new Material();
		grayMat.addTexture("diffuse", new Texture("gray.png"));
		grayMat.addFloat("specularIntensity", 0.5f);
		grayMat.addFloat("specularExponent", 8);

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.setPosition(0, -1, 5);

//		Mesh kachigga = new Mesh("kachigga.obj");
//		Material kerchoo = new Material();
//		kerchoo.addTexture("diffuse", new Texture("gray.png"));
//		kerchoo.addFloat("specularIntensity", 1);
//		kerchoo.addFloat("specularExponent", 8);
//
//		MeshRenderer kachow = new MeshRenderer(kachigga, kerchoo);
//		GameObject kachowObj = new GameObject();
//		kachowObj.addComponent(kachow);
//		kachowObj.getTransform().getPosition().set(0, 2, 0);
//		kachowObj.getTransform().getScale().set(0.1f, 0.1f, 0.1f);
//		planeObject.addChild(kachowObj);

		Mesh monkey = new Mesh("monkey.obj");
		GameObject monkeyObj = new GameObject().addComponent(new MeshRenderer(monkey, grayMat));

		GameObject sun = new GameObject();
		DirectionalLight sunnyDLight = new DirectionalLight(new Color(0.988f,0.953f,0.851f), 0.2f); // http://promo.sunnyd.com/slider_images/bottles/smooth.png
		sun.addComponent(sunnyDLight);

		GameObject danceFloor = new GameObject();

		int danceFloorWidth = 6;
		int danceFloorDepth = 6;

		float danceFloorStartX = 0;
		float danceFloorStartZ = 0;
		float danceFloorStepX = 7;
		float danceFloorStepZ = 7;

		for (int i = 0; i < danceFloorWidth; i++) {
			for (int j = 0; j < danceFloorDepth; j++) {
				GameObject lightObject = new GameObject();
				lightObject.addComponent(new PointLight(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()), 2.0f, Attenuation.ACCURATE));
				lightObject.setPosition(danceFloorStartX + danceFloorStepX * i, 2, danceFloorStartZ + danceFloorStepZ * j);
				danceFloor.addChild(lightObject);
				GameObject monkeyObject = new GameObject();
				monkeyObject.addComponent(new MeshRenderer(new Mesh("bee.obj"), grayMat));
				monkeyObject.addComponent(new FollowCameraComponent());
				monkeyObject.setPosition(danceFloorStartX + danceFloorStepX * i, 0, danceFloorStartZ + danceFloorStepZ * j);
				danceFloor.addChild(monkeyObject);
			}
		}

		Camera camera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f);
		FreeLook freeLook = new FreeLook();
		GameObject cameraObj = new GameObject();
		cameraObj.addComponent(camera);
		cameraObj.addComponent(freeLook);

		addObject(planeObject);
		addObject(sun);
		addObject(danceFloor);
		addObject(cameraObj);
		addObject(monkeyObj);

		sun.setRotation(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
	}
}
