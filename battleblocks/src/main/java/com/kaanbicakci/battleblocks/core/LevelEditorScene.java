package com.kaanbicakci.battleblocks.core;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean isChangingScene = false;
    private float chagingTime = 2.0f;

    public LevelEditorScene() {
        System.out.println("Editor Scene");
    }

    @Override
    public void update(float deltaTime) {
        System.out.println("" + (1f / deltaTime) + " FPS");

        if (!isChangingScene && KeyboardListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            isChangingScene = true;
        }

        if (isChangingScene && chagingTime > 0) {
            chagingTime -= deltaTime;
            Window.get().r -= deltaTime * 5.0f;
            Window.get().g -= deltaTime * 5.0f;
            Window.get().b -= deltaTime * 5.0f;
        } else if (isChangingScene) {
            Window.loadScene(1);
        }
    }
}
