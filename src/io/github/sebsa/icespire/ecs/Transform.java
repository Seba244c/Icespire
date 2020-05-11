package io.github.sebsa.icespire.ecs;

import org.joml.Vector3f;

/**
 * @author Sebsa
 * @since 20-504
 */
public class Transform {
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;
    
    public Transform() {
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
        
    }
    
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void movePosition(float x, float y, float z) {
    	this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }
    
    public Vector3f getPosition() {
    	return position;
    }
    
    public float getScale() {
        return scale;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public void moveRotation(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }
    
    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ", "+position.z+")";
    }
}
