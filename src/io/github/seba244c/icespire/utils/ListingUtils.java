package io.github.seba244c.icespire.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * General utility class for lists and arrays
 * @author Sebsa
 * @since 1.0.2
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
