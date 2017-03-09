package com.typokign.fps.engine.audio;

import com.typokign.fps.engine.audio.resourcemanagement.SoundResource;
import com.typokign.fps.engine.core.Util;
import com.typokign.fps.engine.math.Vector3f;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;

import static org.lwjgl.openal.AL10.*;

/**
 * Created by Typo Kign on 3/5/2017.
 */
public class Sound {
	private static HashMap<String, SoundResource> loadedBuffers = new HashMap<String, SoundResource>();

	private SoundResource resource;
	private String filename;

	public Sound(String filename) {
		this.filename = filename;

		SoundResource existingResource = loadedBuffers.get(filename);

		if (existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			loadSound(filename);
			loadedBuffers.put(filename, resource);
		}
	}

	@Override
	protected void finalize() {
		if (resource.removeReference()) {
			loadedBuffers.remove(filename);
		}
	}

	private void loadSound(String filename) {
		IntBuffer buffer = Util.createIntBuffer(1);

		alGenBuffers(buffer);

		if (alGetError() != AL_NO_ERROR) {
			System.err.println("Failed to load wave file: " + filename);
			System.exit(1);
		}

		FileInputStream fin = null;
		BufferedInputStream bin = null;

		try {
			fin = new FileInputStream("./res/sounds/" + filename);
			bin = new BufferedInputStream(fin);
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		}

		WaveData file = WaveData.create(bin);

		if (file != null) {
			alBufferData(buffer.get(0), file.format, file.data, file.samplerate);
			file.dispose();
		} else {
			System.err.println("File not found: /res/sounds/" + filename);
		}

		if (alGetError() != AL_NO_ERROR) {
			System.err.println("Failed to load wave file: " + filename);
			System.exit(1);
		}

		resource = new SoundResource(buffer.get(0));
	}

	public int getBuffer() {
		return resource.getBuffer();
	}
}
