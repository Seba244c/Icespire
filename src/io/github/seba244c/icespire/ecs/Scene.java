package io.github.seba244c.icespire.ecs;

import io.github.seba244c.icespire.graphics.lighting.DirectionalLight;
import io.github.seba244c.icespire.graphics.lighting.PointLight;
import io.github.seba244c.icespire.graphics.lighting.SpotLight;

/**
 * A scene is a container for lists of things like lights and GameObjects, and is used to render
 * @author Sebsa
 * @since 20-427
 */
public class Scene {
	private Entity[] entities;
	private DirectionalLight directionalLight;
	private SpotLight[] spotLights;
	private PointLight[] pointLights;
	
	public Scene() {
	}
	
	public Scene(Entity[] e) {
		entities = e;
	}
	
	public Scene(Entity[] e, DirectionalLight dLight, SpotLight[] sLights, PointLight[] pLights) {
		entities = e;
		directionalLight = dLight;
		spotLights = sLights;
		pointLights = pLights;
	}

	public Entity[] getEntities() {
		return entities;
	}

	public DirectionalLight getDirectionalLight() {
		return directionalLight;
	}

	public SpotLight[] getSpotLights() {
		return spotLights;
	}

	public PointLight[] getPointLights() {
		return pointLights;
	}

	public void setDirectionalLight(DirectionalLight directionalLight) {
		this.directionalLight = directionalLight;
	}
	
	public void clearEntities() {
		entities = null;
	}
	
	public void destroy() {
		for (Entity entity : entities) {
			for(Component component : entity.getAllComponents()) {
				component.cleanup();
			}
		}
		entities = null;
	}
}
