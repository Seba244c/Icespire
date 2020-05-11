package com.sebsa.icespire.graphics.lighting;

import org.joml.Vector3f;

/**
 * A light wich shoots light rays all around it self, but it slowley fades
 * @author Sebsa
 * @since 20-406 
 *
 */
public class PointLight {
	private Vector3f color;
    private Vector3f position;
    protected float intensity;
    private Attenuation attenuation;
    
    /**
     * @param color The color of the light
     * @param position The position of the light
     * @param intensity The intensity of the light
     */
    public PointLight(Vector3f color, Vector3f position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    /**
     * @param color The color of the light
     * @param position The position of the light
     * @param intensity The intensity of the light
     * @param attenuation A attenuation which decides the way the light fades
     */
    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
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
     * @return The position of the light
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @param position The new position of the light
     */
    public void setPosition(Vector3f position) {
        this.position = position;
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
    
    /**
     * @return The current Attenuation of the light
     */
    public Attenuation getAttenuation() {
        return attenuation;
    }

    /**
     * @param attenuation The new Attenuation that the light will have
     */
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {

        private float constant;

        private float linear;

        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }
}
