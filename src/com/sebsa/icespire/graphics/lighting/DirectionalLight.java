package com.sebsa.icespire.graphics.lighting;

import org.joml.Vector3f;
/**
 * A light with no specific position. But it just shoot light from a direction like the sun, and it does not fade
 * @author Sebsa
 * @since 20-406 
 *
 */
public class DirectionalLight {
    /**
     * The Color of the light
     */
    private Vector3f color;

    /**
     * The direction wich the light shine in
     */
    private Vector3f direction;

    /**
     * The intensity the wich the lights shines at
     */
    private float intensity;

    /**
     * @param color The color of the light
     * @param direction The direction of the light
     * @param intensity The intensity of the light
     */
    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }

    /**
     * @return The current color of the light
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * @param color The new color the light is going to shine in
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * @return The current Direction that the Light is shining
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * @param direction The new direction the light will shine in
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    /**
     * @return The current intensity the light shines in
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * @param intensity The new intensity the light will shine in
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
