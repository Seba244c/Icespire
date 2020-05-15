package io.github.seba244c.icespire.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import io.github.seba244c.icespire.io.TextAreaOutputStream;

/**
 * Function that prints stuff in diffrent formats to the console
 * and has a custom develoopment console window. With timers, console and fps counter
 * @author Sebsa
 * @since 20-406 
 */
public class LoggingUtils {
	private static boolean customConsole = false;
	private static boolean disableLogging = false;
	private static JFrame frame;
	private static PrintStream ps;
	private static JTextPane console;
	private static JTextPane performance;
	private static JTextPane timer;
	private static JToolBar toolBar;
	private static JScrollPane consoleComp;
	private static Style performanceStyle;
	private static Style timerStyle;
	private static DefaultStyledDocument performanceDocument;
	private static DefaultStyledDocument timerDocument;
	/**
	 * Prints a line to console with the format "[file.java/method] Message", will be colored yellow if using Logging dev window
	 * @param file The file calling this
	 * @param function The method calling this
	 * @param message The message that should be printed
	 */
	public static void debugLog(String file, String function, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.out.println("^ ["+file+".java/"+function+"] "+message);
			} else {
				System.out.println(" ["+file+".java/"+function+"] "+message);
			}
		}
	}
	
	/**
	 * Prints a line to console with the format "[file.java] Message", will be colored yellow if using Logging dev window
	 * @param file The file calling this
	 * @param message The message that should be printed
	 */
	public static void debugLog(String file, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.out.println("^ ["+file+".java] "+message);
			} else {
				System.out.println(" ["+file+".java] "+message);
			}
		}
	}
	
	/**
	 * Err prints a line to console with the format "[file.java/method] Message", will be red if using Logging dev window
	 * @param file The file calling this
	 * @param function The method calling this
	 * @param message The message that should be printed
	 */
	public static void errorLog(String file, String function, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.err.println("~ ["+file+".java/"+function+"] "+message);
			} else {
				System.err.println(" ["+file+".java/"+function+"] "+message);
			}
		}
	}
	
	/**
	 * Err prints a line to console with the format "[file.java] Message", will be red if using Logging dev window
	 * @param file The file calling this
	 * @param message The message that should be printed
	 */
	public static void errorLog(String file, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.err.println("~ ["+file+".java] "+message);
			} else {
				System.err.println(" ["+file+".java] "+message);
			}
		}
	}
	
	/**
	 * Prints a line to console with the format "[file.java] Message", will be colored white
	 * @param file The file calling this
	 * @param message The message that should be printed
	 * @param function The method calling this
	 */
	public static void infoLog(String file, String function, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.out.println("* ["+file+".java/"+function+"] "+message);
			} else {
				System.out.println(" ["+file+".java/"+function+"] "+message);
			}
		}
	}
	
	/**
	 * Prints a line to console with the format "[file.java] Message", will be colored white
	 * @param file The file calling this
	 * @param message The message that should be printed
	 */
	public static void infoLog(String file, String message) {
		if(!disableLogging) {
			if(customConsole) {
				System.out.println("* ["+file+".java] "+message);
			} else {
				System.out.println(" ["+file+".java] "+message);
			}
		}
	}
	
	/**
	 * This is a develooper feature only and shouldnt be used for realese, and it may not work on MacOS systems
	 * Redirects println to a new custom JFrame console window, that also shows FPS and other development stuff
	 * @param title The console windows title
	 */
	public static void useDevWindow(String title) {
		customConsole = true;
		// Create jFrame, textpanes and style document
		frame = new JFrame(title);
		DefaultStyledDocument document = new DefaultStyledDocument();
		performanceDocument = new DefaultStyledDocument();
		timerDocument = new DefaultStyledDocument();
		console = new JTextPane(document);    
		performance = new JTextPane(performanceDocument);
		timer = new JTextPane(timerDocument);
		
		// Configure console window
		console.setForeground(Color.WHITE);
		console.setBackground(Color.DARK_GRAY);
		console.setEditable(false);
		
		// Configure performance window
		performance.setForeground(Color.WHITE);
		performance.setBackground(Color.DARK_GRAY);
		performance.setEditable(false);
		
		// Configure performance window
		timer.setForeground(Color.WHITE);
		timer.setBackground(Color.DARK_GRAY);
		timer.setEditable(false);
		
		// performance document
		StyleContext context = new StyleContext();
        performanceStyle = context.addStyle("statStyle", null);
		StyleConstants.setForeground(performanceStyle, Color.white);
		
		// performance document
		StyleContext timerContext = new StyleContext();
		timerStyle = timerContext.addStyle("timerStyle", null);
		StyleConstants.setForeground(timerStyle, Color.white);
        
        // Create and configure TextOutputStream
        TextAreaOutputStream taos = new TextAreaOutputStream( console, document );
        ps = new PrintStream( taos );
        System.setOut( ps );
        System.setErr( ps );
        
        // Create window selection panel
        toolBar = new JToolBar("Tool Bar");
        toolBar.setFloatable(false);
        
        // Create action listener
        ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("s1")) {
					frame.remove(performance);
					frame.remove(timer);
					frame.add(consoleComp);
					frame.pack();
					frame.setSize(600, 155);
				} else if(e.getActionCommand().equals("s2")) {
					frame.remove(consoleComp);
					frame.remove(timer);
					frame.add(performance);
					frame.pack();
					frame.setSize(600, 155);
				} else if(e.getActionCommand().equals("s3")) {
					frame.remove(consoleComp);
					frame.remove(performance);
					frame.add(timer);
					frame.pack();
					frame.setSize(600, 155);
				}
			}
		};
        
        // Buttons
        JButton consoleButton = new JButton("Console");
        toolBar.add(consoleButton);
        consoleButton.setActionCommand("s1");
        consoleButton.addActionListener(al);
        
        JButton performanceButton = new JButton("Performance");
        toolBar.add(performanceButton);
        performanceButton.setActionCommand("s2");
        performanceButton.addActionListener(al);
        
        JButton timerButton = new JButton("Timers");
        toolBar.add(timerButton);
        timerButton.setActionCommand("s3");
        timerButton.addActionListener(al);
        
        // Configure jFrame
        consoleComp = new JScrollPane( console );
        frame.add( consoleComp );
        frame.add(toolBar, BorderLayout.PAGE_START);
        frame.pack();
        frame.setVisible( true );
        frame.setSize(600, 155);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Destroys the devWindow. Must be done on shutdown if using devWindow
	 */
	public static void destroyDevWindow() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		ps.close();
	}
	
	/**
	 * Disable all logging and println from the logging class
	 * and all Icespire println and loggings
	 */
	public static void disableLogging() {
		disableLogging = true;
	}
	
	@Deprecated
	public static void report(int FPS, String afl) throws BadLocationException {
		if(!customConsole) return;
		performanceDocument.replace(0, performanceDocument.getLength(), "Frames Per Second: "+FPS+"\nAverege Frame Length: "+afl+"s", performanceStyle);
		timerDocument.replace(0, timerDocument.getLength(), "Timer1: "+TimeUtils.getTime1()+"s", timerStyle);
	}
	
}
