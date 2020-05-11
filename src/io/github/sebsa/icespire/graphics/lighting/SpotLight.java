package io.github.sebsa.icespire.graphics.lighting;

import org.joml.Vector3f;

/**
 * A pointlight that is cut of at an angle
 * @author Sebsa
 * @since 20-406 
 *
 */
public class SpotLight {

    private PointLight pointLight;

    private Vector3f coneDirection;

    private float cutOff;

    /**
     * @param pointLight The point light which the spotlight is based of
     * @param coneDirection The direction of the cone
     * @param cutOffAngle The angle wich the light is cut of at
     */
    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.getPointLight()),
                new Vector3f(spotLight.getConeDirection()),
                0);
        setCutOff(spotLight.getCutOff());
    }

    /**
     * @return Get the pointlight
     */
    public PointLight getPointLight() {
        return pointLight;
    }

    /**
     * @param pointLight Set the pointlight
     */
    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    /**
     * @return The direction of the cone
     */
    public Vector3f getConeDirection() {
        return coneDirection;
    }

    /**
     * @param coneDirection The new direction of the cone
     */
    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    /**
     * @return The angle wich the point light is cutoff at
     */
    public float getCutOff() {
        return cutOff;
    }

    /**
     * @param cutOff Set the coutoff
     */
    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }
    
    /**
     * @param cutOffAngle Set the angle wich the point light is cutoff at
     */
    public final void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float)Math.cos(Math.toRadians(cutOffAngle)));
    }

}