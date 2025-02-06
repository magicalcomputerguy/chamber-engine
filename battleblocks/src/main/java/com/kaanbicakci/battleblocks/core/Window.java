package com.kaanbicakci.battleblocks.core;

import com.kaanbicakci.battleblocks.helper.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    public float r = 1f, g = 1f, b = 1f, a = 1f;

    private static Window instance = null;
    private static Scene currentScene = null;

    private Window() {
        this.width = 800;
        this.height = 600;
        this.title = "Chamber Engine";
    }

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }

        return Window.instance;
    }

    public void run() {
        System.out.println("LWJGL " + Version.getVersion());
        init();
        loop();

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW Window
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException();
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create Window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        // Callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::onMousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::onMouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::onMouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyboardListener::onKeyCallback);

        // Set OpenGL Context
        glfwMakeContextCurrent(glfwWindow);
        // Vsync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        // Load default scene
        Window.loadScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float deltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0) {
                currentScene.update(deltaTime);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static void loadScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new LevelEditorScene();
                // currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                // currentScene.init();
                break;
            default:
                assert false : "Unable to load scene index " + scene;
                break;
        }
    }
}
