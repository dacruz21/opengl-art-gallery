package com.typokign.fps;

/**
 * Created by Typo Kign on 1/21/2017.
 */
public class Time {

    private static double delta;

    public static long getTime() {
        return System.nanoTime();
    }

    // Time since last frame
    public static double getDelta() {
        return delta;
    }

    public static void setDelta(double delta) {
        Time.delta = delta;
    }
}
