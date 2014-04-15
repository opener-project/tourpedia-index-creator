package org.vicomtech.opener.tourpediaindex;

import ixa.kaflib.Entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.queryparser.classic.ParseException;
import org.vicomtech.opener.kafparser.KAFParser;

public class NELinking {

	private QueryManager searcher;
	private KAFParser kafparser;
	
	private static final String INDEX_CONF  = "./config/index.conf";
	private static final String DATASET_KEY = "DATASET_PATH";
	private static final String INDEX_KEY   = "INDEX_PATH";
	
	private static final int MAX_RESULTS = 100;
	
	@SuppressWarnings("serial")
	public class ConfigException extends Exception {
		private static final String MESSAGE = "Key [%s] not found at config file";
		ConfigException(String key) {
			super(String.format(MESSAGE, key));
		}
	}
	
	public NELinking(File inKaf) throws ConfigException, FileNotFoundException, IOException {
		this.createSearcher();
		if (inKaf == null)
			this.kafparser = new KAFParser(System.in);
		else
			this.kafparser = new KAFParser(inKaf);
	}
	
	private void createSearcher() throws IOException, ConfigException {
		// get configuration file
		Properties p = new Properties();
		p.load(new FileInputStream(INDEX_CONF));
		// get index directory
		String indexDir = new String();
		if ((indexDir = p.getProperty(INDEX_KEY)) == null) {
			throw new ConfigException(INDEX_KEY);
		}
		File indexFile = new File(indexDir);
		// create searcher
		this.searcher = new QueryManager(indexFile);
		try {
			this.searcher.createSearcher();
		} catch (IOException e) {
			this.createNewIndex(p, indexFile);
			this.searcher = new QueryManager(indexFile);
			this.searcher.createSearcher();
		}
	}
	
	private void createNewIndex(Properties prop, File indexFile) throws ConfigException, IOException {
		String docPath = new String();
		if ((docPath = prop.getProperty(DATASET_KEY)) == null) {
			throw new ConfigException(DATASET_KEY);
		}
		IndexManager.create(new File(docPath), indexFile);
	}
	
	private void run() throws IOException, ParseException {
		List<Entity> entities = this.kafparser.getEntities();
		for (int i=0; i<entities.size(); i++) {
			Entity entity = entities.get(i);
			String text = entity.getStr();
			text = text.replaceAll(" ", "_");
			List<String> urls = this.searcher.search(text, true, MAX_RESULTS, false);
			if (urls.size() > 0) {
				this.kafparser.addExternalReference(i, urls.get(0));
			}
		}
	}
	
	private void write(File outKaf) throws IOException {
		if (outKaf == null) {
			this.kafparser.write(System.out);
		}
		else {
			this.kafparser.write(outKaf);
		}
	}
	
	public static void link(File inKaf, File outKaf) throws IOException, ConfigException, ParseException {
		NELinking linker = new NELinking(inKaf);
		linker.run();
		linker.write(outKaf);
	}
	
	

}
