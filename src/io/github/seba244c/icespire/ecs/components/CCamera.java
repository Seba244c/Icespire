package io.github.seba244c.icespire.ecs.components;

import io.github.seba244c.icespire.ecs.Component;

/**
 * @author Sebsa
 * @since 20-504
 */
public class CCamera extends Component {
	/**
     * Changes the rotation of the Camera by an offset
     * @param offsetX The value the x rotation will change by
     * @param offsetY The value the y rotation will change by
     * @param offsetZ The value the z rotation will change by
     * @param clamp If true the x(up and down) will be clamped between -90 and 90
     */
    public void moveRotation(float offsetX, float offsetY, float offsetZ, boolean clamp) {
    	entity.getTransform().getRotation().x += offsetX;
    	entity.getTransform().getRotation().y += offsetY;
    	entity.getTransform().getRotation().z += offsetZ;
    	
        if(clamp)
        	entity.getTransform().getRotation().x =  Math.max(-90, Math.min(90, entity.getTransform().getRotation().x));
    }
    
    /**
     * Changes the position of the Camera by an offset
     * @param offsetX The value the x position will change by
     * @param offsetY The value the y position will change by
     * @param offsetZ The value the z position will change by
     */
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
        	entity.getTransform().getPosition().x += (float)Math.sin(Math.toRadians(entity.getTransform().getRotation().y)) * -1.0f * offsetZ;
        	entity.getTransform().getPosition().z += (float)Math.cos(Math.toRadians(entity.getTransform().getRotation().y)) * offsetZ;
        }
        if ( offsetX != 0) {
        	entity.getTransform().getPosition().x += (float)Math.sin(Math.toRadians(entity.getTransform().getRotation().y - 90)) * -1.0f * offsetX;
        	entity.getTransform().getPosition().z += (float)Math.cos(Math.toRadians(entity.getTransform().getRotation().y - 90)) * offsetX;
        }
        entity.getTransform().getPosition().y += offsetY;
    }
}
