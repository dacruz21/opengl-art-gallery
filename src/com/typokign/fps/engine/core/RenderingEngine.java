package com.typokign.fps.engine.core;

import com.typokign.fps.engine.components.DirectionalLight;
import com.typokign.fps.engine.components.PointLight;
import com.typokign.fps.engine.components.SpotLight;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by Typo Kign on 1/30/2017.
 */
public class RenderingEngine {
	private Camera mainCamera;
	private Vector3f ambientLight;
	private DirectionalLight activeDirectionalLight;
	private PointLight activePointLight;
	private SpotLight activeSpotLight;

	// Permanent structures
	private ArrayList<DirectionalLight> directionalLights;
	private ArrayList<PointLight> pointLights;
	private ArrayList<SpotLight> spotLights;

	public RenderingEngine() {
		directionalLights = new ArrayList<DirectionalLight>();
		pointLights = new ArrayList<PointLight>();
		spotLights = new ArrayList<SpotLight>();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// face culling = unrendering faces not facing towards the camera
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);

		// don't render a face if it is behind another face
		glEnable(GL_DEPTH_TEST);

		// fixes issues if a camera is right on the border of a face
		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);

		mainCamera = new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f);

		ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	}

	public Vector3f getAmbientLight() {
		return ambientLight;
	}

	public DirectionalLight getActiveDirectionalLight() {
		return activeDirectionalLight;
	}

	public PointLight getActivePointLight() {
		return activePointLight;
	}

	public SpotLight getActiveSpotLight() {
		return activeSpotLight;
	}

	public void input(float delta) {
		mainCamera.input(delta);
//		activeSpotLight.getPointLight().setPosition(getMainCamera().getPosition());
//		activeSpotLight.setDirection(getMainCamera().getForward());
	}

	public void render(GameObject object) {
		clearScreen();
		clearLightList();

		object.addToRenderingEngine(this);

		Shader forwardAmbient = ForwardAmbient.getInstance();
		Shader forwardDirectional = ForwardDirectional.getInstance();
		Shader forwardPoint = ForwardPoint.getInstance();
		Shader forwardSpot = ForwardSpot.getInstance();
		forwardAmbient.setRenderingEngine(this);
		forwardDirectional.setRenderingEngine(this);
		forwardPoint.setRenderingEngine(this);
		forwardSpot.setRenderingEngine(this);

		object.render(forwardAmbient);

		glEnable(GL_BLEND); // prepare to blend multiple colors on each pass
		glBlendFunc(GL_ONE, GL_ONE); // coefficients of a(existing color) * b(new color), we just want the colors multiplied together so a and b should be one
		glDepthMask(false); // ignore depth buffer while blending
		glDepthFunc(GL_EQUAL);

		for (DirectionalLight directionalLight : directionalLights) {
			activeDirectionalLight = directionalLight;
			object.render(forwardDirectional);
		}

		for (PointLight pointLight : pointLights) {
			activePointLight = pointLight;
			object.render(forwardPoint);
		}

		for (SpotLight spotLight : spotLights) {
			activeSpotLight = spotLight;
			object.render(forwardSpot);
		}

		glDepthFunc(GL_LESS); // revert changes above
		glDepthMask(true);
		glDisable(GL_BLEND);
	}

	public void addDirectionalLight(DirectionalLight directionalLight) {
		directionalLights.add(directionalLight);
	}

	public void addPointLight(PointLight pointLight) {
		pointLights.add(pointLight);
	}

	public void addSpotLight(SpotLight spotLight) {
		spotLights.add(spotLight);
	}

	private void clearLightList() {
		directionalLights.clear();
		pointLights.clear();
		spotLights.clear();
	}

	private static void clearScreen() {
		// TODO: stencil buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private static void setTextures(boolean enabled) {
		if (enabled)
			glEnable(GL_TEXTURE_2D);
		else
			glDisable(GL_TEXTURE_2D);
	}

	private static void unbindTextures() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private static void setClearColor(Vector3f color) {
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
}
