package org.vicomtech.opener.tourpediaindex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.vicomtech.opener.tourpediaindex.POI.Field;

/**
 * This class creates queries for the tourpedia index using the Apache Lucene library:
 * http://lucene.apache.org/
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class LuceneIndexQuery {

	private File indexDir;
	private IndexSearcher indexSearcher;
	
	public LuceneIndexQuery(File indexDir) {
		this.indexDir = indexDir;
	}
	
	public void createIndexSearcher() throws IOException{
		IndexReader indexReader = null;
	    Directory dir = FSDirectory.open(this.indexDir);
	    indexReader  = DirectoryReader.open(dir);
	    this.indexSearcher = new IndexSearcher(indexReader);
	}
	
	public List<String> getURLsByField(String value, boolean exactMatch, int maxResults, boolean showQuery) throws IOException, ParseException{
		List<String> urls = new ArrayList<String>();
		KeywordAnalyzer analyzer = new KeywordAnalyzer();
		
		QueryParser parser = new QueryParser(Version.LUCENE_47, Field.NAME.toString(), analyzer);
		parser.setAllowLeadingWildcard(true);
		parser.setLowercaseExpandedTerms(false);
		
		String query = new String();
		if (!exactMatch)
			value = WildcardQuery.WILDCARD_STRING+value+WildcardQuery.WILDCARD_STRING;
		
		query += Field.NAME.toString()+":"+     value+" "+QueryParser.OR_OPERATOR+" ";
		query += Field.DIRECTION.toString()+":"+value+" "+QueryParser.OR_OPERATOR+" ";
		query += Field.COUNTRY.toString()+":"+  value;
		Query q = parser.parse(query);
		
		if (showQuery)
			System.out.println(String.format("Query: %s", q.toString()));
		
		ScoreDoc[] hits =   this.indexSearcher.search(q,maxResults).scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			Document doc = this.indexSearcher.doc(hits[i].doc);
			urls.add(doc.get(Field.URL.toString()));
		}
		return urls;
	}
}
