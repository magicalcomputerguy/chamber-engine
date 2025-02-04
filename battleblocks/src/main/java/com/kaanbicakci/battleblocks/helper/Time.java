package com.kaanbicakci.battleblocks.helper;

public class Time {
    public static float genesis = System.nanoTime();

    public static float getTime() {
        return (float)((System.nanoTime() - genesis) * 1E-9);
    }
}
