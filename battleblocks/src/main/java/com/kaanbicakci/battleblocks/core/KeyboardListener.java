package com.kaanbicakci.battleblocks.core;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardListener {
    private boolean keys[] = new boolean[256];

    private static KeyboardListener instance = null;

    public static KeyboardListener get() {
        if (KeyboardListener.instance == null) {
            KeyboardListener.instance = new KeyboardListener();
        }

        return KeyboardListener.instance;
    }

    public static void onKeyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keys[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keys[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        return get().keys[key];
    }
}
