package org.vicomtech.opener.tourpediaindex;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class manages the tourpedia index creation
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class IndexManager {

	private File docPath;
	private File indexDir;
	private LuceneIndexWriter writer;
	private int added;
	
	public IndexManager(File docPath, File indexDir) {
		this.docPath = docPath;
		this.indexDir = indexDir;
		this.added = 0;
	}
	
	private void createIndex() throws IOException {
		this.writer = new LuceneIndexWriter(this.indexDir);
		//this.writer.createIndex();
		this.writer.createOrAppendIndex();
	}
	
	private void addDocuments() throws IOException {
		List<String> documents = Utils.readFile(docPath);
		int lastAdded = 0;
		for (String document : documents) {
			POI poi = new POI(document);
			if (!poi.isEmpty()) {
				this.writer.addDocumentToIndex(poi);
				this.added++;
				if (this.added % 10 == 0) {
					Utils.displayText(".");
					if (this.added % 1000 == 0) {
						Utils.displayTextln("");
						Utils.displayText(
								String.format("Added indexes [%s...%s]", lastAdded, this.added));
						lastAdded = this.added;
					}
				}
			}
		}
		this.writer.closeIndex();
	}
	
//	private List<File> getDocuments() {
//		List<File> documents = Utils.readFile(this.docPath);
//		if (this.docDir.isDirectory()) {
//			for (File file : this.docDir.listFiles()) {
//				documents.add(file);
//			}
//		}
//		else {
//			documents.add(this.docDir);
//		}
//		return documents;
//	}
	
	public static void create(File docPath, File indexDir) throws IOException {
		IndexManager manager = new IndexManager(docPath, indexDir);
		
		Utils.displayHeader("NEW INDEX CREATION");
		
		Utils.displayTextln(
				String.format("Creating new index at '%s' indexDir.....",
						indexDir.toString()));
		
		manager.createIndex();
		
		Utils.displayText(
				String.format("Adding documents from '%s' directory",
						docPath.toString()));
		
		manager.addDocuments();
		
		Utils.displayTextln("");
		
		Utils.displayTextln(
				String.format("'%s' docs added!",
				manager.added));
	}
}
