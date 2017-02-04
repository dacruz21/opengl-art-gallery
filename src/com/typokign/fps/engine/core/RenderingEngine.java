package com.typokign.fps.engine.core;

import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.rendering.*;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by Typo Kign on 1/30/2017.
 */
public class RenderingEngine {
	private Camera mainCamera;
	private Vector3f ambientLight;
	private DirectionalLight directionalLight;
	private PointLight pointLight;
	private SpotLight spotLight;

	private PointLight[] pointLightList;

	public RenderingEngine() {
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

		ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
		directionalLight = new DirectionalLight(new BaseLight(new Vector3f(0.988f,0.953f,0.851f), 0.4f), new Vector3f(1,1,1));
		spotLight = new SpotLight(new PointLight(new BaseLight(new Vector3f(0, 1, 1), 1.2f), new Attenuation(0, 0, 0.5f), new Vector3f(-2, 0, 5), 50), new Vector3f(1, 1, 1), 0.7f);


		int lightFieldWidth = 6;
		int lightFieldDepth = 6;

		float lightFieldStartX = 0;
		float lightFieldStartY = 0;
		float lightFieldStepX = 7;
		float lightFieldStepY = 7;

		pointLightList = new PointLight[lightFieldWidth * lightFieldDepth];

		Random random = new Random();

		for (int i = 0; i < lightFieldWidth; i++) {
			for (int j = 0; j < lightFieldDepth; j++) {
				pointLightList[i * lightFieldWidth + j] = new PointLight(new BaseLight(new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()), 2.0f),
						Attenuation.ACCURATE,
						new Vector3f(lightFieldStartX + lightFieldStepX * i, 0, lightFieldStartY + lightFieldStepY * j),
						100);
			}
		}

		pointLight = pointLightList[0];
	}

	public Vector3f getAmbientLight() {
		return ambientLight;
	}

	public DirectionalLight getDirectionalLight() {
		return directionalLight;
	}

	public PointLight getPointLight() {
		return pointLight;
	}

	public SpotLight getSpotLight() {
		return spotLight;
	}

	public void input(float delta) {
		mainCamera.input(delta);
		spotLight.getPointLight().setPosition(getMainCamera().getPosition());
		spotLight.setDirection(getMainCamera().getForward());
	}

	public void render(GameObject object) {
		clearScreen();

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

		object.render(forwardDirectional);

		for (int i = 0; i < pointLightList.length; i++) {
			pointLight = pointLightList[i];
			object.render(forwardPoint);
		}

		object.render(forwardSpot);

		glDepthFunc(GL_LESS); // revert changes above
		glDepthMask(true);
		glDisable(GL_BLEND);
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
