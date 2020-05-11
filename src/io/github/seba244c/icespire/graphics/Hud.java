package io.github.seba244c.icespire.graphics;

import io.github.seba244c.icespire.ecs.Component;
import io.github.seba244c.icespire.ecs.Entity;

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
