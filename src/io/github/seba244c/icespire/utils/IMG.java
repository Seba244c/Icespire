package io.github.seba244c.icespire.utils;

import java.nio.ByteBuffer;

/**
 * A class purely for storing data
 * @author Sebsa
 * @since 20-406 
 * @deprecated
 */
public class IMG {
	/**
	 * Image width
	 */
	public int w;
	/**
	 * Image height
	 */
	public int h;
	/**
	 * Image pixels
	 */
	public ByteBuffer image;
	
	/**
	 * @param image Image pixels
	 * @param width Image width
	 * @param height Image height
	 */
	public IMG(ByteBuffer image, int width, int height) {
		this.image = image;
		this.w = width;
		this.h = height;
	}
}
