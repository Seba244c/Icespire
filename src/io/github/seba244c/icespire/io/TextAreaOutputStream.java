package io.github.seba244c.icespire.io;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

/**
 * @author Sebsa
 * @since 20-408 
 */
public class TextAreaOutputStream extends OutputStream {
	// *************************************************************************************************
	// INSTANCE MEMBERS
	// *************************************************************************************************

	private byte[] oneByte;                                                   
	private Appender appender;

	public TextAreaOutputStream(JTextPane txtara, DefaultStyledDocument styleDoc) {
	    oneByte=new byte[1];
	    appender=new Appender(txtara, styleDoc);
	}
	
	/** Clear the current console text area. */
	public synchronized void clear() {
	    if(appender!=null) { appender.clear(); }
	}

	public synchronized void close() {
	    appender=null;
	}

	public synchronized void flush() {
		// Just as a requiement
	    }

	public synchronized void write(int val) {
	    oneByte[0]=(byte)val;
	    write(oneByte,0,1);
	    }

	public synchronized void write(byte[] ba) {
	    write(ba,0,ba.length);
	    }

	public synchronized void write(byte[] ba,int str,int len) {
	    if(appender!=null) { appender.append(bytesToString(ba,str,len)); }
	 }

	static private String bytesToString(byte[] ba, int str, int len) {
	    try { return new String(ba,str,len,"UTF-8"); } catch(UnsupportedEncodingException thr) { return new String(ba,str,len); } // all JVMs are required to support UTF-8
	}
	
	static class Appender implements Runnable{
	    private final JTextPane             textArea;
	    private final LinkedList<Integer>   lengths;                                                    // length of lines within text area
	    private final List<String>          values;                                                     // values waiting to be appended

	    private boolean                     clear;
	    private boolean                     queue;
	    private Style errorStyle;
	    private Style debugStyle;
	    private Style infoStyle;
	    private DefaultStyledDocument document;
	    
	    Appender(JTextPane txtara, DefaultStyledDocument document) {
	        textArea =txtara;
	        lengths  =new LinkedList<Integer>();
	        values   =new ArrayList<String>();

	        clear    =false;
	        queue    =true;
	        StyleContext context = new StyleContext();
	        errorStyle = context.addStyle("errLog", null);
	        debugStyle = context.addStyle("debugLog", null);
	        infoStyle = context.addStyle("infoLog", null);
	        StyleConstants.setForeground(errorStyle, Color.RED);
	        StyleConstants.setForeground(debugStyle, Color.ORANGE);
	        StyleConstants.setForeground(infoStyle, Color.white);
	        this.document = document;
	    }

	    private synchronized void append(String val) {
	        values.add(val);
	        if(queue) { queue=false; EventQueue.invokeLater(this); }
	    }

	    private synchronized void clear() {
	        clear=true;
	        lengths.clear();
	        values.clear();
	        if(queue) { queue=false; EventQueue.invokeLater(this); }
	     }

	    // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
	    public synchronized void run() {
	        if(clear) { textArea.setText(""); }
	        
	        char type = 'I';
	        
	        for(String val: values) {	            
	            if(val.charAt(0) == '~') {
	            	type = 'E';
	            }
	            if(val.charAt(0) == '^') {
	            	type = 'D';
	            }
	            if(val.charAt(0) == '*') {
	            	type = 'I';
	            }
	            if(type=='E') {
	            	try { document.insertString(document.getLength(),val.substring(1), errorStyle); }
	                catch (BadLocationException e){}
	            } else if(type=='D') {
	            	try { document.insertString(document.getLength(),val.substring(1), debugStyle); }
	                catch (BadLocationException e){}
	            } else {
	            	try { document.insertString(document.getLength(),val.substring(1), infoStyle); }
	                catch (BadLocationException e){}
	            }
	        }
	        values.clear();
	        clear =false;
	        queue =true;
	    }
	}
}
