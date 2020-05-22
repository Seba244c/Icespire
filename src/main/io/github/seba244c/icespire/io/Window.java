package io.github.seba244c.icespire.io;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL11.*;

import java.nio.IntBuffer;
import java.text.DecimalFormat;

import javax.swing.text.BadLocationException;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.joml.Vector3f;

import io.github.seba244c.icespire.utils.FileUtils;
import io.github.seba244c.icespire.utils.IMG;
import io.github.seba244c.icespire.utils.LoggingUtils;

/**
 * The glfw window the user sees everything in
 * @author Sebsa
 * @since 1.0.2
 *
 */
@SuppressWarnings("deprecation")
public class Window {
	/**
	 * The GLFW window id
	 */
	public long windowId;
	/**
	 * The input class. Use to get last frames input
	 */
	public Input input;
	private int width;
    private int height;
    private int tempW;
    private int tempH;
    private boolean resized;
    private boolean isFullscreen;
    private String title;
    private int[] posX = new int[1];
    private int[] posY = new int[1];
	private boolean lineView;
	private boolean vSync;
	private boolean isCursorShown = true;
	private int frames;
	private int fps;
	private static long time;
	
	
	// Vars used in Averege frame length calculation
	private static double averegeFrameTime;
	private static double aft;
	private static double afl;
	
	private boolean showFps;
	private DecimalFormat aftFormat;

	private GLFWImage.Buffer iconBuffer;
		
    /**
     * @param title The title of the window
     * @param width The window width
     * @param height The window height
     * @param vsync Wether vsync should be enabled or not
     * @param showFpsInTitleBar Wether the Frames per second should be displayed in the title of the window
     */
    public Window(String title, int width, int height, boolean vsync, boolean showFpsInTitleBar) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.resized = false;
        this.iconBuffer = null;
        this.vSync = vsync;
        this.showFps = showFpsInTitleBar;
    }
    
	/**
	 * Initialize, and creates the window
	 * @param clearColor The background color of the window
	 */
	public void init(Vector3f clearColor) {
		LoggingUtils.infoLog("Window", "init", "Initializing Window");
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() ) {
			LoggingUtils.errorLog("Window", "init", "Unable to initialize GLFW");
			throw new IllegalStateException("[Window.java/init] Unable to initialize GLFW");
		}
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		// OSX Sipport
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// Create the window
		windowId = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if ( windowId == NULL ) {
			LoggingUtils.errorLog("Window", "init", "Failed to create the GLFW window");
			throw new IllegalStateException("[Window.java/init] Failed to create the GLFW window");
		}
		// Setup resize callback
        glfwSetFramebufferSizeCallback(windowId, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(windowId, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			posX[0] = (vidmode.width() - width) / 2;
			posY[0] = (vidmode.height() - height) / 2;
			// Center the window
			glfwSetWindowPos(windowId, posX[0], posY[0]);
		}
		// Make the OpenGL context current
		glfwMakeContextCurrent(windowId);
		// Enable v-sync
		if(vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		LoggingUtils.infoLog("Window", "init", "Setting up OpenGL");
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		
		// Support for transparencies
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Culling
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// Make the window visible
		glfwShowWindow(windowId);
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 0.0f);
		
		// Input
		LoggingUtils.infoLog("Window", "init", "Creating the input class");
		input = new Input(this);
		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		// FPS counter
		frames = 0;
		fps = 0;
		time = System.currentTimeMillis();
		aft = System.currentTimeMillis();
		averegeFrameTime = 0;
		aftFormat = new DecimalFormat("#.#####");
		afl = 0;
	}
	
	/**
	 * Cleanup and destroys the window. Should happen on shutdown
	 */
	public void cleanup() {
		LoggingUtils.infoLog("Window", "cleanup", "Destorying window");
		input.cleanup(windowId);
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(windowId);
		glfwDestroyWindow(windowId);

		// Terminate GLFW and free the error callback
		glfwSetErrorCallback(null);
		glfwTerminate();
		
		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		iconBuffer.close();
	}
	
	/**
	 * Updates glfw, and the Logging.devWindow. Should happen every frame
	 */
	public void update() {
		glfwSwapBuffers(windowId); // swap the color buffers
		glfwPollEvents();
		frames++;
		
		// Time bewteen frames
		averegeFrameTime = averegeFrameTime + (System.currentTimeMillis() - aft);
		aft = System.currentTimeMillis();
		
		// Fps and report to logging class
		if (System.currentTimeMillis() > time + 1000) {
			if(showFps) {
				glfwSetWindowTitle(windowId, title + " | FPS: " + frames);
			}
			fps = frames;
			time = System.currentTimeMillis();
			frames = 0;
			
			try { LoggingUtils.report(fps, aftFormat.format(averegeFrameTime/fps/1000)); } catch (BadLocationException e) {
				e.printStackTrace();
			}
			averegeFrameTime = 0;
			afl = Double.parseDouble(aftFormat.format(averegeFrameTime/fps/1000));
		}
	}
	/**
	 * Checks wether the window and application should be closed or not
	 * @return True if should close, false if not
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowId);
	}

	/**
	 * Sets the title of the window
	 * @param title The title
	 */
	public void setTitle(String title) {
		LoggingUtils.infoLog("Window", "setTitle", "Setting title of the window to: " + title);
		glfwSetWindowTitle(windowId, title);
	}
	
	/**
	 * Sets the background color of the window
	 * @param clearColor The color
	 */
	public void setClearColor(Vector3f clearColor) {
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 0.0f);
	}
	
	/**
	 * Setups the default input callbacks to the Input class.
	 */
	public void setDefaultICallbacks() {
		LoggingUtils.debugLog("Window", "setDefaultICallbacks", "Setting up input callbacks");
		glfwSetKeyCallback(windowId, input.getKeyboardCallback());
		glfwSetCursorPosCallback(windowId, input.getMouseMoveCallback());
		glfwSetScrollCallback(windowId, input.getMouseScrollCallback());
		glfwSetMouseButtonCallback(windowId, input.getMouseButtonsCallback());
		glfwSetCursorEnterCallback(windowId, input.getCursorEnterCallback());
	}
	
	/**
	 * Gets the current width of the window
	 * @return Current width
	 */
	public int getWidth() {
        return width;
    }

    /**
     * Gets the current height of the window
     * @return Current height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Checks if the window has been resized within the last frame
     * @return Wether the window has been resized or not
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * Sets if the window has been resized. This should not be used.
     * @param resized True if resized, false if not
     */
    public void setResized(boolean resized) {
        this.resized = resized;
    }
    
    /**
     * Checks if the window is in fullscreen mode or not
     * @return True if fullscreen false if not
     */
    public boolean isFullscreen() {
		return isFullscreen;
	}
    
    /**
     * Enables or disables fullscreen mode
     * @param isFullscreen True to enable fullscren false to disable
     */
    public void setFullscreen(boolean isFullscreen) {
    	LoggingUtils.infoLog("Window", "setFullscreen", "Setting fullscreen to: " + isFullscreen);
		this.isFullscreen = isFullscreen;
		resized = true;
		if (isFullscreen) {
			tempW = width;
			tempH = height;
			GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwGetWindowPos(windowId, posX, posY);
			glfwSetWindowMonitor(windowId, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), GLFW_DONT_CARE);
			// Enable v-sync
			if(vSync) {
				glfwSwapInterval(1);
			} else {
				glfwSwapInterval(0);
			}
		} else {
			glfwSetWindowMonitor(windowId, 0, posX[0], posY[0], tempW, tempH, GLFW_DONT_CARE);
		}
	}
    
    /**
     * Sets if the models should be rendered as lineView or not
     * @param bool True if rendered as line false if not 
     */
    public void setLineView(boolean bool) {
    	LoggingUtils.infoLog("Window", "setLineView", "Setting line view to: " + bool);
    	if (bool) { glPolygonMode( GL_FRONT_AND_BACK, GL_LINE  ); } else { glPolygonMode( GL_FRONT_AND_BACK, GL_FILL ); }
    	lineView = bool;
    }
    /**
     * Checks if the models is rendered as lineView or not
     * @return True if rendered as line false if not
     */
    public boolean isLineView() {
    	return lineView;
    }
    
    /**
     * Sets the window icon to a image
     * @param path Image path
     */
    public void setIcon(String path) {
    	LoggingUtils.infoLog("Window", "setIcon", "Setting icon to file at: " + path);
    	IMG icon = FileUtils.loadImage(path);
    	GLFWImage iconImage = GLFWImage.malloc();
    	iconBuffer = GLFWImage.malloc(1); 
    	iconImage.set(icon.w, icon.h, icon.image);
    	iconBuffer.put(0, iconImage);
		glfwSetWindowIcon(windowId, iconBuffer);
		iconImage.free();
    }
    
    /**
     * Sets if the cursor should or should not be shown
     * @param show False to hide cursor or true to reset
     */
    public void showCursor(boolean show) {
    	isCursorShown = show;
    	if(!show) {
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    	} else {
    		glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    	}
    }
    
    /**
     * Checks if the cursor is shown
     * @return True if cursor is shown false if not
     */
    public boolean isCursorShown() {
		return isCursorShown;
	}
    
    /**
     * Checks if vsync is enabled or not
     * @return If vSync is enabled
     */
    public boolean isVSync() {
    	return vSync;
    }
    
    /**
     * Sets if vSync should or should not be enabled
     * @param vsync New vSync state
     */
    public void setVSync(boolean vsync) {
    	vSync = vsync;
    	LoggingUtils.infoLog("Window", "setVSync", "Setting vsync to: " + vsync);
    	if(vSync) {
    		glfwSwapInterval(1);
    	} else {
    		glfwSwapInterval(0);
    	}
    }

	/**
	 * @return The amount of frames that was displayed the last second
	 */
	public int getFps() {
		return fps;
	}

	/**
	 * @param showFps Wether the Frames per second should be displayed in the title of the window
	 */
	public void setShowFps(boolean showFps) {
		this.showFps = showFps;
	}

	/**
	 * @return The averege lenght of a frame, also shown in the devWindow
	 */
	public static double getAfl() {
		return afl;
	}
}
