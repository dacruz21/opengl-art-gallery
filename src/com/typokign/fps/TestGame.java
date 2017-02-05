package com.typokign.fps;

import com.typokign.fps.components.Flashlight;
import com.typokign.fps.engine.components.*;
import com.typokign.fps.engine.core.*;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.*;

import java.util.Random;

/**
 * Created by Typo Kign on 1/29/2017.
 */
public class TestGame extends Game {
	Random random = new Random(); // omg RNG game is unbalanced plz fix

	public void init() {

		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;

		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
				new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
				new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
				new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

		int indices[] = { 0, 1, 2,
				2, 1, 3};

		Material material = new Material(new Texture("test.png"), new Vector3f(1,1,1), 1, 8);
		Mesh mesh = new Mesh(vertices, indices, true);  //new Mesh("cube.obj");

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().getPosition().set(0, -1, 5);

		GameObject sun = new GameObject();
		DirectionalLight sunnyDLight = new DirectionalLight(new Color(0.988f,0.953f,0.851f), 0.2f, new Vector3f(1,1,1)); // http://promo.sunnyd.com/slider_images/bottles/smooth.png
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
				lightObject.getTransform().getPosition().set(danceFloorStartX + danceFloorStepX * i, 0, danceFloorStartZ + danceFloorStepZ * j);
				danceFloor.addChild(lightObject);
			}
		}

		GameComponent flashlight = new Flashlight(new Color(1f, 1f, 0.902f), 1.0f, 100, 0.7f);
		GameObject flashlightObj = new GameObject();
		flashlightObj.addComponent(flashlight);
		flashlightObj.getTransform().getPosition().set(0, 0, 0);

		getRootObject().addChild(planeObject);
		getRootObject().addChild(sun);
		getRootObject().addChild(danceFloor);
		getRootObject().addChild(flashlightObj);
	}

}
