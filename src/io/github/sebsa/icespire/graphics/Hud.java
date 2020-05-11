package io.github.sebsa.icespire.graphics;

import io.github.sebsa.icespire.ecs.Component;
import io.github.sebsa.icespire.ecs.Entity;

/**
 * @author Sebsa
 * @since 20-427 
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
