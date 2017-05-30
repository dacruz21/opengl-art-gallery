package com.typokign.fps;

import com.typokign.fps.engine.components.*;
import com.typokign.fps.engine.core.Game;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Quaternion;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.*;
import com.typokign.fps.engine.rendering.mesh.Mesh;
import com.typokign.fps.engine.rendering.mesh.Primitives;
import org.lwjgl.input.Keyboard;

/**
 * Created by Typo on 5/30/2017.
 */
public class ArtGallery extends Game {

	@Override
	public void init() {
		// Materials
		Material carpetMat = new Material();
		carpetMat.addTexture("diffuse", new Texture("carpet.png"));
		carpetMat.addFloat("specularIntensity", 1);
		carpetMat.addFloat("specularExponent", 1);

		Material ceilingMat = new Material();
		ceilingMat.addTexture("diffuse", new Texture("ceiling.png"));
		ceilingMat.addFloat("specularIntensity", 1);
		ceilingMat.addFloat("specularExponent", 1);

		Material metalMat = new Material();
		metalMat.addTexture("diffuse", new Texture("gray.png"));
		metalMat.addFloat("specularIntensity", 2);
		metalMat.addFloat("specularExponent", 8);

		// Artwork
		Material screamMat = new Material();
		screamMat.addTexture("diffuse", new Texture("art/scream.png"));
		screamMat.addFloat("specularIntensity", 1);
		screamMat.addFloat("specularExponent", 1);

		Material monaLisaMat = new Material();
		monaLisaMat.addTexture("diffuse", new Texture("art/monalisa.png"));
		monaLisaMat.addFloat("specularIntensity", 1);
		monaLisaMat.addFloat("specularExponent", 1);

		Material memoryMat = new Material();
		memoryMat.addTexture("diffuse", new Texture("art/memory.png"));
		memoryMat.addFloat("specularIntensity", 1);
		memoryMat.addFloat("specularExponent", 1);

		// Requirements

		Camera mainCamera = new Camera((float) Math.toRadians(70f), Window.getWidth() / Window.getHeight(), 0.01f, 1000f);
		FreeLook freeLook = new FreeLook();

		GameObject cameraObj = new GameObject();
		cameraObj.addComponent(mainCamera);
		cameraObj.addComponent(freeLook);
		cameraObj.setPosition(0f, 1f, 0f);
		addObject(cameraObj);

		DirectionalLight dLight = new DirectionalLight(new Color(0.988f,0.953f,0.851f), 0f);

		GameObject dLightObj = new GameObject();
		dLightObj.addComponent(dLight);
		addObject(dLightObj);

		dLightObj.setRotation(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));

		// Room

		MeshRenderer floor = new MeshRenderer(Primitives.createCuboid(new Vector3f(-10,0,-10), new Vector3f(10, 0.5f, 10), 16), carpetMat);
		MeshRenderer ceiling = new MeshRenderer(Primitives.createCuboid(new Vector3f(-10, 10, -10), new Vector3f(10, 10.5f, 10), 2), ceilingMat);
		GameObject room = new GameObject();
		room.addComponent(floor);

		MeshRenderer wall1 = new MeshRenderer(Primitives.createCuboid(new Vector3f(-10, 0.5f, 10), new Vector3f(-1, 10, 10.5f), 2), metalMat);
		MeshRenderer wall2 = new MeshRenderer(Primitives.createCuboid(new Vector3f(1, 0.5f, 10), new Vector3f(10, 10, 10.5f), 2), metalMat);
		MeshRenderer wall3 = new MeshRenderer(Primitives.createCuboid(new Vector3f(-1, 8.5f, 10), new Vector3f(1, 10f, 10.5f), 2), metalMat);
		MeshRenderer wall4 = new MeshRenderer(Primitives.createCuboid(new Vector3f(-1, 0.5f, 10), new Vector3f(1, 5, 10.5f), 2), metalMat);

		MeshRenderer wall5 = new MeshRenderer(Primitives.createCuboid(new Vector3f(-10, 0.5f, -10), new Vector3f(10, 10, -10.5f), 2), metalMat);
		MeshRenderer wall6 = new MeshRenderer(Primitives.createCuboid(new Vector3f(-10, 0.5f, -10), new Vector3f(-10.5f, 10, 10), 2), metalMat);
		MeshRenderer wall7 = new MeshRenderer(Primitives.createCuboid(new Vector3f(10, 0.5f, -10), new Vector3f(10.5f, 10, 10), 2), metalMat);


		MeshRenderer bar = new MeshRenderer(Primitives.createCuboid(new Vector3f(-0.125f, 5, 10), new Vector3f(0.125f, 8.5f, 10.5f), 2), metalMat);

		room.addComponent(wall1);
		room.addComponent(wall2);
		room.addComponent(wall3);
		room.addComponent(wall4);
		room.addComponent(wall5);
		room.addComponent(wall6);
		room.addComponent(wall7);

		room.addComponent(ceiling);

		room.addComponent(bar);

		// Artwork
		MeshRenderer monaLisa = new MeshRenderer(Primitives.createCuboid(new Vector3f(1.5f, 2, -9.75f), new Vector3f(-1.5f, 7, -10), 1), monaLisaMat);
		room.addComponent(monaLisa);

		MeshRenderer scream = new MeshRenderer(Primitives.createCuboid(new Vector3f(7.5f, 2, -9.75f), new Vector3f(5f, 7, -10), 1), screamMat);
		room.addComponent(scream);

		MeshRenderer memory = new MeshRenderer(Primitives.createCuboid(new Vector3f(-3.5f, 2, -9.75f), new Vector3f(-8f, 7, -10), 1), memoryMat);
		room.addComponent(memory);

		// Statue
		MeshRenderer pedestal = new MeshRenderer(Primitives.createCuboid(new Vector3f(-8f, 0.5f, -0.5f), new Vector3f(-9, 4, 0.5f), 1), metalMat);
		room.addComponent(pedestal);

		MeshRenderer monkey = new MeshRenderer(new Mesh("monkey.obj"), metalMat);
		GameObject statue = new GameObject();
		statue.setPosition(-8.5f, 5, 0);
		statue.setRotation(new Quaternion(0, 0.707f, 0, 0.707f));
		statue.addComponent(monkey);

		addObject(statue);


		addObject(room);

		// Light
		PointLight light = new PointLight(new Color(0.988f,0.953f,0.851f), 100, Attenuation.ACCURATE);
		GameObject lightBulb = new GameObject();
		lightBulb.addComponent(light);
		lightBulb.setPosition(0, 5f, 0);
		lightBulb.setRotation(new Quaternion(new Vector3f(0, -1, 0), 0));

		addObject(lightBulb);
	}
}
