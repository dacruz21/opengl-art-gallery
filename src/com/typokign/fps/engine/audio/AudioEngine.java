package com.typokign.fps.engine.audio;

import com.typokign.fps.engine.components.Camera;
import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.util.WaveData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Typo Kign on 3/5/2017.
 */
public class AudioEngine {
	public AudioEngine() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	public void update(CoreEngine engine) {
		Camera camera = engine.getRenderingEngine().getMainCamera();

		FloatBuffer listenerPosition = Util.createFloatBuffer(3);
		listenerPosition.put(camera.getTransform().getPosition().getX());
		listenerPosition.put(camera.getTransform().getPosition().getY());
		listenerPosition.put(camera.getTransform().getPosition().getZ());
		listenerPosition.flip();

		FloatBuffer listenerVelocity = Util.createFloatBuffer(3);
		listenerVelocity.put(0);
		listenerVelocity.put(0);
		listenerVelocity.put(0);
		listenerVelocity.flip();

		FloatBuffer listenerRotation = Util.createFloatBuffer(6);

		Vector3f up = camera.getTransform().getRotation().getUp();

		listenerRotation.put(camera.getTransform().getPosition().getX());
		listenerRotation.put(camera.getTransform().getPosition().getY());
		listenerRotation.put(camera.getTransform().getPosition().getZ());
		listenerRotation.put(up.getX());
		listenerRotation.put(up.getY());
		listenerRotation.put(up.getZ());
		listenerRotation.flip();

		alListener(AL_POSITION, listenerPosition);
		alListener(AL_VELOCITY, listenerVelocity);
		alListener(AL_ORIENTATION, listenerRotation);
	}

	public void destroy() {
		AL.destroy();
	}
}
