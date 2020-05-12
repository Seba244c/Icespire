package io.github.seba244c.icespire.utils;

public class TimeUtils {
	private static double time1;
	private static double dt;
	private static double adt;
	/**
	 * Starts timer 1
	 */
	public static void startTimer1() {
		time1 = System.currentTimeMillis();
	}
	
	/**
	 * Ends timer 1
	 * @return The amount of seconds between the startTimer1 method was called and this method
	 */
	public static double endTimer1() {
		time1 = (System.currentTimeMillis() - time1)/1000;
		return time1;
	}
	
	/**
	 * @return The time between this frame and the last frame
	 */
	public static double getDeltaTime() {
		return adt/1000;
	}
	
	/**
	 * Calculates the delta time. This method should be called once a frame
	 */
	public static void updateDeltaTime() {
		adt = System.currentTimeMillis() - dt;
		dt = System.currentTimeMillis();
	}

	/**
	 * @return The amount of seconds between the startTimer1 method was and the endTimer1 function
	 */
	public static double getTime1() {
		return time1;
	}
}
