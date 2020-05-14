package io.github.seba244c.icespire.graphics;

import io.github.seba244c.icespire.ecs.Entity;
import io.github.seba244c.icespire.ecs.components.MeshRenderer;
import io.github.seba244c.icespire.utils.ListingUtils;

import java.nio.charset.Charset;
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

    private String text;

    private final int numCols;

    private final int numRows;

    public TextObject(String text, String fontFileName, int numCols, int numRows) throws Exception {
        this.text = text;
        this.numCols = numCols;
        this.numRows = numRows;
        Texture texture = new Texture(fontFileName);
        this.addComponent(new MeshRenderer(buildMesh(texture, numCols, numRows)));
    }
    
    private Mesh buildMesh(Texture texture, int numCols, int numRows) {
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals   = new float[0];
        List<Integer> indices   = new ArrayList<>();
        
        float tileWidth = (float)texture.getWidth() / (float)numCols;
        float tileHeight = (float)texture.getHeight() / (float)numRows;

        for(int i=0; i<numChars; i++) {
            byte currChar = chars[i];
            int col = currChar % numCols;
            int row = currChar / numCols;
            
            // Build a character tile composed by two triangles
            
            // Left Top vertex
            positions.add((float)i*tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)col / (float)numCols );
            textCoords.add((float)row / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD);
                        
            // Left Bottom vertex
            positions.add((float)i*tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float)col / (float)numCols );
            textCoords.add((float)(row + 1) / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add((float)i*tileWidth + tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(col + 1)/ (float)numCols );
            textCoords.add((float)(row + 1) / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add((float)i*tileWidth + tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(col + 1)/ (float)numCols );
            textCoords.add((float)row / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 3);
            
            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);
        }
        
        float[] posArr = ListingUtils.listToArray(positions);
        float[] textCoordsArr = ListingUtils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(texture));
        return mesh;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        MeshRenderer mRenderer = this.getComponent(MeshRenderer.class);
        Texture texture = mRenderer.getMesh().getMaterial().getTexture();
        mRenderer.getMesh().deleteBuffers();
        mRenderer.setMesh(buildMesh(texture, numCols, numRows));
    }
}