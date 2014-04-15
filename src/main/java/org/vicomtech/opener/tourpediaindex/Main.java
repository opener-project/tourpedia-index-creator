package org.vicomtech.opener.tourpediaindex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.vicomtech.opener.tourpediaindex.NELinking.ConfigException;
import org.vicomtech.opener.tourpediaindex.Utils.ExitStatus;

/**
 * This is the main class of the Tourpedia Index Tool
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class Main {

	private final static int KAF_CMD    = 0;
	private final static int CREATE_CMD = 1;
	private final static int QUERY_CMD  = 2;
	
	private int command = -1;
	
	private File inKaf    = null;
	private File outKaf   = null;
	private File docPath  = null;
	private File indexDir = null;
	private String value  = null;
	private boolean exactMatch = false;
	private boolean showQuery  = false;
	
	/**
	 * Get arguments for the application
	 * @param args
	 * @throws IOException 
	 * @throws FieldNotFoundException 
	 */
	private void getArguments(String[] originalArgs) throws IOException {
		ArrayList<String> args = new ArrayList<String>();
		for ( String tmp : originalArgs )
			if ( tmp.length() > 0 ) args.add(tmp);
		
		if ( args.size() == 0 || args.contains("-h") || args.contains("--help") ) {
			this.showHelp();
			System.exit(ExitStatus.SUCCESS.getValue());
		}
		
		// get arguments
		for ( int i=0; i<args.size(); i++ ) {
			String arg = args.get(i);
			if ( arg.equalsIgnoreCase("-kaf") ) {
				this.command = KAF_CMD;
				if (i+2 < args.size()
						&& !args.get(i+1).startsWith("-")
						&& !args.get(i+2).startsWith("-")) {
					this.inKaf = new File(this.getArgument(args, ++i));
					this.outKaf = new File(this.getArgument(args, ++i));
				}
			}
			else if ( arg.equalsIgnoreCase("-create") )
				this.command = CREATE_CMD;
			else if ( arg.equalsIgnoreCase("-query") )
				this.command = QUERY_CMD;				
			else if ( arg.equalsIgnoreCase("-index") )
				this.indexDir = new File(this.getArgument(args, ++i));
			else if ( arg.equalsIgnoreCase("-doc") )
				this.docPath = new File(this.getArgument(args, ++i));
			else if ( arg.equalsIgnoreCase("-exactMatch") )
				this.exactMatch = true;
			else if ( arg.equalsIgnoreCase("-value") )
				this.value = this.getArgument(args, ++i);
			else if ( arg.equalsIgnoreCase("-showQuery") )
				this.showQuery = true;
			else {
				Utils.printError(String.format("option '%s' unknown", arg));
				this.showHelp();
				System.exit(ExitStatus.SUCCESS.getValue());
			}
		}
				
		// check arguments
		if (!this.checkArguments()) {
			this.showHelp();
			System.exit(ExitStatus.ERROR.getValue());
		}
	}
	
	/**
	 * Get the argument at the index position
	 * @param args
	 * @param index
	 * @return
	 */
	private String getArgument (List<String> args, int index) {
		if (index >= args.size()) {
			this.showHelp();
			Utils.printError(String.format(
				"missing parameter after '%s'", args.get(index-1)));
			System.exit(ExitStatus.ERROR.getValue());
		}
		return args.get(index);
	}
	
	/**
	 * Checks parameters
	 * @return
	 */
	private boolean checkArguments() {
		if (this.command < 0) {
			Utils.printError("command missing");
			return false;
		}
		else if (this.command == KAF_CMD) {
			return true;
		}
		else if (this.command == CREATE_CMD) {
			return checkCreateArguments();
		}
		else if (this.command == QUERY_CMD) {
			return checkQueryArguments();
		}
		return false;
	}
	
	/**
	 * Checks parameters for create command
	 * @return
	 */
	private boolean checkCreateArguments() {
		if (this.docPath == null) {
			Utils.printError("path to documents file not especified");
			return false;
		}
		else if (!this.docPath.exists()) {
			Utils.printError(String.format(
					"documents file '%s' does not exist",
					this.docPath.toString()));
			return false;
		}
		
		if (this.indexDir == null) {
			Utils.printError("path to index directory not especified");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks parameters for query command
	 * @return
	 */
	private boolean checkQueryArguments() {
		if (this.value == null) {
			Utils.printError("value string not especified");
			return false;
		}
		if (this.indexDir == null) {
			Utils.printError("path to index directory not especified");
			return false;
		}
		else if (!this.indexDir.exists()) {
			Utils.printError(String.format(
					"index directory '%s' does not exist",
					this.indexDir.toString()));
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		Main prog = new Main();
		
		try {
			prog.getArguments(args);
			
			if (prog.command == KAF_CMD) {
				NELinking.link(prog.inKaf, prog.outKaf);
			}
			else if (prog.command == CREATE_CMD) {
				IndexManager.create(prog.docPath, prog.indexDir);
			}
			else if (prog.command == QUERY_CMD) {
				QueryManager.query(prog.value, prog.exactMatch, prog.indexDir, prog.showQuery);
			}
		}
		catch (IOException ex) {
			Utils.printError(ex.getMessage());
			System.exit(ExitStatus.ERROR.getValue());
		} catch (ParseException ex) {
			Utils.printError(ex.getMessage());
			System.exit(ExitStatus.ERROR.getValue());
		} catch (ConfigException ex) {
			Utils.printError(ex.getMessage());
			System.exit(ExitStatus.ERROR.getValue());
		}
	}
	
	
	
	private void showHelp() {
		System.err.println("Shows this screen: -h or --help");
    	System.err.println("");
    	System.err.println("Link entities within KAF file to the tourpedia index:");
    	System.err.println("    -kaf [input.kaf output.kaf]");
    	System.err.println("  If input and output KAF files are not especified,");
    	System.err.println("  reads KAF from std input and writes in std output.");
    	System.err.println("");
    	System.err.println("Create a new index:");
    	System.err.println("    -create -doc docFile -index indexDir");
    	System.err.println("  ARGUMENTS:");
    	System.err.println("    -doc      docDir,       Path to the documents file.");
    	System.err.println("    -index    indexDir,     Path to the index directory.");
    	System.err.println("");
    	System.err.println("Perform a query:");
    	System.err.println("    -query -value value -index indexDir");
    	System.err.println("  ARGUMENTS:");
    	System.err.println("    -value    value,        The value to search.");
    	System.err.println("    -index    indexDir,     Path to the index directory.");
    	System.err.println("  OPTIONS:");
    	System.err.println("    -exactMatch             Searches value with no wildcard.");
    	System.err.println("    -showQuery              Prints performed query.");
	}

}
