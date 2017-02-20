package com.typokign.fps.engine.rendering;

import com.typokign.fps.engine.components.BaseLight;
import com.typokign.fps.engine.components.Camera;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.resourcemanagement.MappedValues;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by Typo Kign on 1/30/2017.
 */
public class RenderingEngine extends MappedValues {
	private Camera mainCamera;
	private Vector3f ambientLight;

	// Permanent structures
	private ArrayList<BaseLight> lights;

	private BaseLight activeLight;

	private HashMap<String, Integer> samplerMap;

	public RenderingEngine() {
		super();
		ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);

		lights = new ArrayList<BaseLight>();
		samplerMap = new HashMap<String, Integer>();

		samplerMap.put("diffuse", 0);

		addVector3f("ambientIntensity", ambientLight);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// face culling = not rendering faces not facing towards the camera
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);

		// don't render a face if it is behind another face
		glEnable(GL_DEPTH_TEST);

		// fixes issues if a camera is right on the border of a face
		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
	}

	public Vector3f getAmbientLight() {
		return ambientLight;
	}

	public void render(GameObject object) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		lights.clear();

		object.addToRenderingEngine(this);

		Shader forwardAmbient = ForwardAmbient.getInstance();

		object.render(forwardAmbient, this);

		glEnable(GL_BLEND); // prepare to blend multiple colors on each pass
		glBlendFunc(GL_ONE, GL_ONE); // coefficients of a(existing color) * b(new color), we just want the colors multiplied together so a and b should be one
		glDepthMask(false); // ignore depth buffer while blending
		glDepthFunc(GL_EQUAL);

		for (BaseLight light : lights) {
			activeLight = light;

			object.render(light.getShader(), this);
		}

		glDepthFunc(GL_LESS); // revert changes above
		glDepthMask(true);
		glDisable(GL_BLEND);
	}

	public void addLight(BaseLight light) {
		lights.add(light);
	}

	public void addCamera(Camera camera) {
		mainCamera = camera;
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}

	public BaseLight getActiveLight() {
		return activeLight;
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
}
