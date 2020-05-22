package io.github.seba244c.icespire.graphics.gui;

import io.github.seba244c.icespire.ecs.Entity;
import io.github.seba244c.icespire.ecs.components.MeshRenderer;
import io.github.seba244c.icespire.graphics.Material;
import io.github.seba244c.icespire.graphics.Mesh;
import io.github.seba244c.icespire.utils.ListingUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * A object with a text as a mesh W.I.P
 * @author Sebsa
 * @since 1.0.4
 */
public class TextObject extends Entity {
	private static final float ZPOS = 0.0f;

    private static final int VERTICES_PER_QUAD = 4;

    private final FontTexture fontTexture;
    
    private String text;

    public TextObject(String text, FontTexture fontTexture) throws Exception {
    	super();
        this.text = text;
        this.fontTexture = fontTexture;
        this.addComponent(new MeshRenderer(buildMesh()));
    }
    
    private Mesh buildMesh() {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals   = new float[0];
        List<Integer> indices   = new ArrayList<>();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i=0; i<numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);
            
            // Build a character tile composed by two triangles
            
            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add( (float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);
                        
            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float)fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);
            
            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);
            
            startx += charInfo.getWidth();
        }

        float[] posArr = ListingUtils.listToArray(positions);
        float[] textCoordsArr = ListingUtils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        this.getComponent(MeshRenderer.class).getMesh().deleteBuffers();
        this.getComponent(MeshRenderer.class).setMesh(buildMesh());
    }
}