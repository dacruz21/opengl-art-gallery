package com.typokign.fps.engine.audio;

import com.typokign.fps.engine.components.Camera;
import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Quaternion;
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

	public void updateListener(Vector3f position, Vector3f velocity, Quaternion rotation) {
		FloatBuffer orientationBuffer = Util.createFloatBuffer(6);
		orientationBuffer.put(rotation.getForward().getX());
		orientationBuffer.put(rotation.getForward().getY());
		orientationBuffer.put(rotation.getForward().getZ());
		orientationBuffer.put(rotation.getUp().getX());
		orientationBuffer.put(rotation.getUp().getY());
		orientationBuffer.put(rotation.getUp().getZ());
		orientationBuffer.flip();

		alListener3f(AL_POSITION, position.getX(), position.getY(), position.getZ());
		alListener3f(AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
		alListener(AL_ORIENTATION, orientationBuffer);
	}

	public void destroy() {
		AL.destroy();
	}
}
