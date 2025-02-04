package com.kaanbicakci.battleblocks.core;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtons[] = new boolean[3];
    private boolean isDragging;

    private static MouseListener instance = null;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void onMousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtons[0] || get().mouseButtons[1] || get().mouseButtons[2];
    }

    public static void onMouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtons.length) {
                get().mouseButtons[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtons.length) {
                get().mouseButtons[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void onMouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDeltaX() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDeltaY() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean getIsDragging() {
        return get().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtons.length) {
            return get().mouseButtons[button];
        } else {
            return false;
        }
    }
}
