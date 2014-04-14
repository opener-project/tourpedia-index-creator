package org.vicomtech.opener.tourpediaindex;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * This class manages queries to the Tourpedia index
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class QueryManager {

	private static final int MAX_RESULTS = 100;
	
	private String value;
	private File indexDir;
	private LuceneIndexQuery searcher;
	
	public QueryManager(String value, File indexDir) {
		this.value    = value.toLowerCase();
		this.indexDir = indexDir;
	}
	
	private void createSearcher() throws IOException {
		this.searcher = new LuceneIndexQuery(this.indexDir);
		this.searcher.createIndexSearcher();
	}
	
	private List<String> search(boolean exactMatch, int maxResults, boolean showQuery) throws IOException, ParseException {
		return this.searcher.getURLsByField(this.value, exactMatch, maxResults, showQuery);
	}
	
	public static void query(String value, boolean exactMatch, File indexDir, boolean showQuery) throws IOException, ParseException {
		QueryManager manager = new QueryManager(value, indexDir);
		
		Utils.displayHeader("EXECUTING QUERY");
		
		Utils.displayTextln("Creating searcher.....");
		
		manager.createSearcher();
		
		Utils.displayTextln("Performing query.....");
		
		List<String> urls = manager.search(exactMatch, MAX_RESULTS, showQuery);
		
		Utils.displayHeader("QUERY RESULTS");
		for (String url : urls) {
			Utils.displayTextln(url);
		}
	}
	
}
