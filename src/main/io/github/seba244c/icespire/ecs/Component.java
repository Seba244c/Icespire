package io.github.seba244c.icespire.ecs;

/**
 * @author Sebsa
 * @since 1.0.1
 */
public abstract class Component<T> {
	public Entity entity;
	
	public void update() {
		// all components needs to have this function
	}
	
	public void cleanup() {
		// all components needs to have this function
	}
}
