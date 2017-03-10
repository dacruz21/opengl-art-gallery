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
			alDistanceModel(AL_INVERSE_DISTANCE);
		} catch (LWJGLException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	public void update(CoreEngine engine) {
		Camera camera = engine.getRenderingEngine().getMainCamera();

		Vector3f position = camera.getTransform().getPosition();
		Vector3f velocity = new Vector3f(0, 0, 0);

		Vector3f up = camera.getTransform().getRotation().getUp();
		Vector3f forward = camera.getTransform().getRotation().getForward();

		FloatBuffer listenerRotation = Util.createFloatBuffer(6);
		listenerRotation.put(forward.getX());
		listenerRotation.put(forward.getY());
		listenerRotation.put(forward.getZ());
		listenerRotation.put(up.getX());
		listenerRotation.put(up.getY());
		listenerRotation.put(up.getZ());
		listenerRotation.flip();

		alListener3f(AL_POSITION, position.getX(), position.getY(), position.getZ());
		alListener3f(AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
		alListener(AL_ORIENTATION, listenerRotation);
	}

	public void destroy() {
		AL.destroy();
	}
}
