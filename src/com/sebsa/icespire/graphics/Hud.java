package com.sebsa.icespire.graphics;

import com.sebsa.icespire.ecs.Component;
import com.sebsa.icespire.ecs.Entity;

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
