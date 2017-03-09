package com.typokign.fps.engine.audio.resourcemanagement;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Typo Kign on 3/5/2017.
 */
public class SoundResource {
	private int buffer;
	private int refCount;

	public SoundResource(int buffer) {
		this.buffer = buffer;
		this.refCount = 1;
	}

	@Override
	protected void finalize() {
		alDeleteBuffers(buffer);
	}

	public void addReference() {
		refCount++;
	}

	public boolean removeReference() {
		refCount--;
		return refCount == 0;
	}

	public int getBuffer() {
		return buffer;
	}
}
