package org.vicomtech.opener.tourpediaindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides some utils for the Tourpedia Index Tool
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class Utils {

	protected static enum ExitStatus {
		SUCCESS(1), ERROR(-1);
		private int value;
		private ExitStatus(int value) {
			this.value = value;
		}
		protected int getValue() {
			return this.value;
		}
	}
	
	private static final String DIVIDER  = "-------------------------------------------------------------------------------";
	
	public static List<String> readStream(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		return read(br);
	}
	
	/**
	 * Reads a file and returns a list with the lines within the file.
	 * @param file : input file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		return read(br);
	}
	
	/**
	 * 
	 * Reads an input file and returns an array.
	 * The array contains each line of the file.
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static List<String> readFile(String file) throws IOException {
		FileReader fileread = new FileReader(file);
		BufferedReader br = new BufferedReader(fileread);
		return read(br);
	}
	
	public static List<String> read(BufferedReader br) throws IOException {
		
		List<String> list = new ArrayList<String>();
				
		// read lines and insert at list
		String line;
		while ((line = br.readLine()) != null) {
			if (line.length() > 0)
				list.add(line);
		}
		br.close();
		return list;
	}
	
	public static File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException(String.format("File '%s' does not exist", filePath));
		}
		return file;
	}
	
	public static void printError(String message) {
		System.err.println("ERROR: "+message);
	}
	
	/**
	 * Prints a message inside dividers
	 * @param msg
	 */
	public static void displayHeader(String msg) {
		displayDivider();
		displayTextln(msg);
		displayDivider();
	}
	
	/**
	 * Prints a message
	 * @param msg
	 */
	public static void displayTextln(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Prints a message
	 * @param msg
	 */
	public static void displayText(String msg) {
		System.out.print(msg);
	}
	
	/**
	 * Prints divider
	 */
	private static void displayDivider() {
		System.out.println(DIVIDER);
	}

}
