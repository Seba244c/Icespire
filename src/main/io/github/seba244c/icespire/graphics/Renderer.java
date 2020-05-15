package io.github.seba244c.icespire.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import io.github.seba244c.icespire.ecs.Entity;
import io.github.seba244c.icespire.ecs.Scene;
import io.github.seba244c.icespire.ecs.components.CCamera;
import io.github.seba244c.icespire.ecs.components.MeshRenderer;
import io.github.seba244c.icespire.ecs.components.SpriteRenderer;
import io.github.seba244c.icespire.graphics.lighting.DirectionalLight;
import io.github.seba244c.icespire.graphics.lighting.PointLight;
import io.github.seba244c.icespire.graphics.lighting.SpotLight;
import io.github.seba244c.icespire.io.Window;
import io.github.seba244c.icespire.utils.FileUtils;
import io.github.seba244c.icespire.utils.LoggingUtils;

/**
 * The renderer renders the meshes of gamobjects to the window
 * @author Sebsa
 * @since 1.0.2
 */
public class Renderer {
    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static final int MAX_POINT_LIGHTS = 5;

    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    private ShaderProgram hudShaderProgram;
    private ShaderProgram spriteShaderProgram;

    private final float specularPower;
    
    /**
     * The list of meshes need to be rendered the next possible frame
     */
    public static List<MeshRenderer> tbrMeshs;
    
    /**
     * The list of sprites need to be rendered the next possible frame
     */
    public static List<SpriteRenderer> tbrSprites;

    public Renderer() throws Exception {
        transformation = new Transformation();
        specularPower = 10f;
        tbrMeshs = new ArrayList<MeshRenderer>();
        tbrSprites = new ArrayList<SpriteRenderer>();	
    }

    public void init() throws Exception {
    	LoggingUtils.infoLog("Renderer", "init", "Initializing Renderer");
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(FileUtils.loadResourceAsString("/shaders/vertex.glsl"));
	    shaderProgram.createFragmentShader(FileUtils.loadResourceAsString("/shaders/fragment.glsl"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        shaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        shaderProgram.createDirectionalLightUniform("directionalLight");
        
        setupHudShader();
        setupSpriteShader();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, Entity camera, Vector3f ambientLight, Hud hud, Scene scene) throws Exception {
    	if(camera.getComponent(CCamera.class)==null)
    		throw new IllegalArgumentException("[Renderer.java/render] Given camera entity has no CCamera component");
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        
        renderMeshes(window, camera, ambientLight, scene);
        
        renderSprites(window);
        
        renderHud(window, hud);
    }
    
    public void render(Window window, Entity camera, Vector3f ambientLight, Scene scene) throws Exception {
    	if(camera.getComponent(CCamera.class)==null) 
    		throw new IllegalArgumentException("[Renderer.java/render] Given camera entity has no CCamera component");
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        
        renderMeshes(window, camera, ambientLight, scene);
        renderSprites(window);
        shaderProgram.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight,
            PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {

        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

        // Process Point Lights
        int numLights=0;
        if(pointLightList!=null) {
	        numLights = pointLightList != null ? pointLightList.length : 0;
	        for (int i = 0; i < numLights; i++) {
	            // Get a copy of the point light object and transform its position to view coordinates
	            PointLight currPointLight = new PointLight(pointLightList[i]);
	            Vector3f lightPos = currPointLight.getPosition();
	            Vector4f aux = new Vector4f(lightPos, 1);
	            aux.mul(viewMatrix);
	            lightPos.x = aux.x;
	            lightPos.y = aux.y;
	            lightPos.z = aux.z;
	            shaderProgram.setUniform("pointLights", currPointLight, i);
	        }
        }
        
        if(spotLightList!=null&&pointLightList!=null) {
	        // Process Spot Ligths
	        numLights = spotLightList != null ? spotLightList.length : 0;
	        for (int i = 0; i < numLights; i++) {
	            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
	            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
	            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
	            dir.mul(viewMatrix);
	            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
	            Vector3f lightPos = currSpotLight.getPointLight().getPosition();
	
	            Vector4f aux = new Vector4f(lightPos, 1);
	            aux.mul(viewMatrix);
	            lightPos.x = aux.x;
	            lightPos.y = aux.y;
	            lightPos.z = aux.z;
	
	            shaderProgram.setUniform("spotLights", currSpotLight, i);
	        }
        }
        
        if(directionalLight!=null) {
	        // Get a copy of the directional light object and transform its position to view coordinates
	        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
	        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
	        dir.mul(viewMatrix);
	        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
	        shaderProgram.setUniform("directionalLight", currDirLight);
        }
    }
    
    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(FileUtils.loadResourceAsString("/shaders/hud_vertex.glsl"));
        hudShaderProgram.createFragmentShader(FileUtils.loadResourceAsString("/shaders/hud_fragment.glsl"));
        hudShaderProgram.link();

        // Create uniforms for Ortographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
    }
    
    private void setupSpriteShader() throws Exception {
        spriteShaderProgram = new ShaderProgram();
        spriteShaderProgram.createVertexShader(FileUtils.loadResourceAsString("/shaders/sprite_vertex.glsl"));
        spriteShaderProgram.createFragmentShader(FileUtils.loadResourceAsString("/shaders/sprite_fragment.glsl"));
        spriteShaderProgram.link();

        // Create uniforms for Ortographic-model projection matrix and base colour
        spriteShaderProgram.createUniform("projModelMatrix");
        spriteShaderProgram.createUniform("colour");
    }
    
    private void renderHud(Window window, Hud hud) {

        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (Entity entity : hud.getGameObjects()) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(entity, ortho);
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            hudShaderProgram.setUniform("colour", entity.getComponent(MeshRenderer.class).getMesh().getMaterial().getAmbientColour());

            // Render the mesh for this HUD item
            entity.getComponent(MeshRenderer.class).renderMesh();
        }

        hudShaderProgram.unbind();
    }
    
    private void renderSprites(Window window) {
    	if(tbrSprites.isEmpty()) return;
    	spriteShaderProgram.bind();
        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (SpriteRenderer sprite : tbrSprites) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(sprite.entity, ortho);
            spriteShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            spriteShaderProgram.setUniform("colour", sprite.getMesh().getMaterial().getAmbientColour());

            // Render the mesh for this HUD item
            sprite.renderSprite();
        }
        tbrSprites.clear();
        spriteShaderProgram.unbind();
    }
    
    private void renderMeshes(Window window, Entity camera, Vector3f ambientLight, Scene scene) {
    	if(tbrMeshs.isEmpty()) return;
        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update Light Uniforms
        renderLights(viewMatrix, ambientLight, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each mesh
        for (MeshRenderer meshRenderer : tbrMeshs) {
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(meshRenderer.entity, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform("material", meshRenderer.getMesh().getMaterial());
            meshRenderer.renderMesh();
        }
        tbrMeshs.clear();
        
        shaderProgram.unbind();
    }

    public void cleanup() {
    	LoggingUtils.infoLog("Renderer", "cleanup", "Cleaning up the Renderer");
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
        	hudShaderProgram.cleanup();
        }
        if (spriteShaderProgram != null) {
        	spriteShaderProgram.cleanup();
        }
    }
}
