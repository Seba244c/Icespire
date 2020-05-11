package com.sebsa.icespire.ecs;

import com.sebsa.icespire.io.Window;

/**
 * @author Sebsa
 * @since 20-504
 */
public abstract class Component<T> {
	public Entity entity;
	
	public void update() {
		return;
	}
	
	public void cleanup() {
		return;
	}
	
	public void setup(Window window) {
		
	}
}
