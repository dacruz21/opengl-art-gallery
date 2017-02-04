package com.typokign.fps.engine.core;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Time {
	private static final long SECOND = 1000000000L; // number of nanoseconds in 1 second

	private static double delta;

	public static double getTime() {
		return System.nanoTime() / (double) SECOND;
	}
}
