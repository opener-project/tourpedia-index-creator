package org.vicomtech.opener.tourpediaindex;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.vicomtech.opener.tourpediaindex.POI.Field;

/**
 * This class creates the tourpedia index using the Apache Lucene library:
 * http://lucene.apache.org/
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class LuceneIndexWriter {
	
	private File indexDir;
	private IndexWriter writer;
	
	public LuceneIndexWriter(File indexDir) {
		this.indexDir = indexDir;
	}
	
	/**
	 * Creates the index in create mode, creates a new index
	 * or overwrites an existing one.
	 * @throws IOException 
	 */
	public void createIndex() throws IOException {
	    boolean create = true;
	    if (this.indexDir.exists() && this.indexDir.isDirectory()) {
	    	create = false;
	    }
	 
	    Directory dir = FSDirectory.open(this.indexDir);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
	 
	    if (create) {
	    	// Create a new index in the directory, removing any
	    	// previously indexed documents:
	    	iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	    }
	 
	    this.writer = new IndexWriter(dir, iwc);
	    this.commit();
	}
	
	/**
	 * Creates the index in create_or_append mode, creates a new index
	 * or appends if exists.
	 * @throws IOException 
	 */
	public void createOrAppendIndex() throws IOException {
		Directory dir = FSDirectory.open(this.indexDir);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		this.writer = new IndexWriter(dir, iwc);
		this.commit();
	}
	
	/**
	 * Adds a new document to the index
	 * @param bookVO
	 * @throws IOException 
	 */
	public void addDocumentToIndex(POI poi) throws IOException {
		Document document = new Document();
		document.add(new StringField(Field.NAME.toString(), poi.getName(), Store.YES));
		document.add(new StringField(Field.DIRECTION.toString(), poi.getDirection(), Store.YES));
		document.add(new StringField(Field.COUNTRY.toString(), poi.getCountry(), Store.YES));
		document.add(new StringField(Field.URL.toString(), poi.getURL(), Store.YES));
		this.writer.addDocument(document);
		this.commit();
	}
	
	private void commit() throws IOException {
		try {
			this.writer.commit();
		}
		catch (OutOfMemoryError ex) {
			this.writer.close(false);
			throw new OutOfMemoryError(ex.getMessage());
		}
	}
	
	/**
	 * Closes index writer
	 * @throws IOException
	 */
	public void closeIndex() throws IOException {
		this.writer.close();
	}
}
