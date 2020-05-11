package io.github.sebsa;

/**
 * A general Icespire utility class
 * @author Sebsa
 * @since 20-505
 */
public class Icespire {
	/**
	 * @return The current version and build
	 */
	public static String getVersionFull() {
		return "Icespire "+getVersion()+" Build "+getBuild();
	}
	
	/**
	 * @return The current version
	 */
	public static String getVersion() {
		return "v0.1.1";
	}
	
	/**
	 * @return The current build
	 */
	public static String getBuild() {
		return "20-511";
	}
}
