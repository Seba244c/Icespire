package io.github.sebsa.icespire.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebsa
 * @since 20-426 
 */
public class ListingUtils {
	public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
	
	public static List<String> arrayToList(String[] array) {
		List<String> list = new ArrayList<String>();
        for(String aString : array) {
        	list.add(aString);
        }
		return list;
    }
}
