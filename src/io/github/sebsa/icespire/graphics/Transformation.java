package io.github.sebsa.icespire.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import io.github.sebsa.icespire.ecs.Entity;

/**
 * @author Sebsa
 * @since 20-406 
 *
 */
public class Transformation {
	private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f orthoMatrix;
    
    public Transformation() {
    	modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        orthoMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }
    
    public Matrix4f getModelViewMatrix(Entity gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getTransform().getRotation();
        modelViewMatrix.identity().translate(gameObject.getTransform().getPosition()).
            rotateX((float)Math.toRadians(-rotation.x)).
            rotateY((float)Math.toRadians(-rotation.y)).
            rotateZ((float)Math.toRadians(-rotation.z)).
            scale(gameObject.getTransform().getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }
    
    public Matrix4f getViewMatrix(Entity camera) {
        Vector3f cameraPos = camera.getTransform().getPosition();
        Vector3f rotation = camera.getTransform().getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }
    
    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }
    
    public Matrix4f getOrtoProjModelMatrix(Entity entity, Matrix4f orthoMatrix) {
        Vector3f rotation = entity.getTransform().getRotation();
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(entity.getTransform().getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(entity.getTransform().getScale());
        Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
        orthoMatrixCurr.mul(modelMatrix);
        return orthoMatrixCurr;
    }
}