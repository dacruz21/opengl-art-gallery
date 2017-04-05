package com.typokign.fps.engine.components;

import com.typokign.fps.engine.audio.Sound;
import static org.lwjgl.openal.AL10.*;

/**
 * Represents a sound that is not affected by the listener position. Most commonly used for ambience sounds
 */
public class EnvironmentSound extends PointSound {

	public EnvironmentSound(Sound sound, boolean loop, boolean startAutomatically) {
		super(sound, loop, startAutomatically);
	}

	@Override
	public void play() {
		alSourcei(source.get(0), AL_SOURCE_RELATIVE, AL_TRUE);
		alSource3f(source.get(0), AL_POSITION, 0, 0, 0);
		alSource3f(source.get(0), AL_VELOCITY, 0, 0, 0);
		alSourcePlay(source.get(0));
	}
}
