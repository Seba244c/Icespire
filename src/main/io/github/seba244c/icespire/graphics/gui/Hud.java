package io.github.seba244c.icespire.graphics.gui;

import io.github.seba244c.icespire.ecs.Component;
import io.github.seba244c.icespire.ecs.Entity;

/**
 * A UI that can render text and sprites
 * @author Sebsa
 * @since 1.0.4
 */
public interface Hud {
	Entity[] getGameObjects();

    default void cleanup() {
    	Entity[] gameObjects = getGameObjects();
        for (Entity gameObject : gameObjects) {
        	for(Component component : gameObject.getAllComponents()) {
        		component.cleanup();
        	}
        }
    }
}
