package io.github.seba244c.icespire.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

/**
 * A class used to load files
 * @author Sebsa
 * @since 1.0.2
 *
 */
public class FileUtils {
	/**
	 * Reads a text file
	 * @param fileName The location of the file starting from class directory. Starting with a slash
	 * @return The file as a single string
	 * @throws Exception When the file is not found
	 */
	public static String loadResourceAsString(String fileName) throws Exception {
		LoggingUtils.debugLog("FileUtils", "loadResourceAsString", "Loading file at path: "+fileName);
		
        String result;
        try (InputStream in = FileUtils.class.getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
	
	/**
	 * Reads a text file
	 * @param fileName The location of the file starting from class directory. Not starting with a slash
	 * @return A List String with every line in the file
	 * @throws Exception When the file is not found
	 */
	public static List<String> readAllLines(String fileName) throws Exception {
		LoggingUtils.debugLog("FileUtils", "readAllLines", "Reading all lines of file at path: "+fileName);
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(FileUtils.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
	
	/**
	 * Loads an image file
	 * @deprecated
	 * @param path The location of the file starting from class directory. Starting with a slash
	 * @return A IMG with the image Buffer, width and height
	 */
	public static IMG loadImage(String path) {
		LoggingUtils.debugLog("FileUtils", "loadImage", "Loading image at path: "+path);
        ByteBuffer image;
        int width = 0;
        int height = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = STBImage.stbi_load(path, w, h, comp, 4);
            if (image == null) {
                LoggingUtils.errorLog("FileUtils", "loadImage", "Couldn't load "+path);
            }   
            width = w.get();
            height = h.get();
        }
        return new IMG(image, width, height);
    }
}
