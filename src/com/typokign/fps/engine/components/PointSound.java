package com.typokign.fps.engine.components;

import com.typokign.fps.engine.audio.Sound;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;

import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Typo Kign on 3/8/2017.
 */
public class PointSound extends GameComponent {
	protected Sound sound;
	protected IntBuffer source;

	public PointSound(Sound sound, boolean loop, boolean startAutomatically) {
		this.sound = sound;

		IntBuffer source = Util.createIntBuffer(1);
		int buffer = sound.getBuffer();

		alGenSources(source);

		if (alGetError() != AL_NO_ERROR) {
			System.err.println("Failed to play file");
			System.exit(1);
		}

		alSourcei(source.get(0), AL_BUFFER, buffer);
		alSourcef(source.get(0), AL_PITCH, 1.0f);
		alSourcef(source.get(0), AL_GAIN, 1.0f);
		alSourcei(source.get(0), AL_LOOPING, loop ? AL_TRUE : AL_FALSE);

		this.source = source;

		if (startAutomatically)
			play();
	}

	@Override
	protected void finalize() {
		alDeleteSources(source);
	}

	public void play() {
		Vector3f position = getPosition();
		Vector3f velocity = new Vector3f(0, 0, 0);

		alSource3f(source.get(0), AL_POSITION, position.getX(), position.getY(), position.getZ());
		alSource3f(source.get(0), AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
		alSourcePlay(source.get(0));
	}
}
