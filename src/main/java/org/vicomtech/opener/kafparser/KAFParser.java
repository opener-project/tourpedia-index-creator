package org.vicomtech.opener.kafparser;

import ixa.kaflib.Entity;
import ixa.kaflib.ExternalRef;
import ixa.kaflib.KAFDocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;

import org.vicomtech.opener.tourpediaindex.Main;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * This class uses IXA kaflib to parse KAF files
 * 
 * org.vicomtech.opener.kafparser is a module of Domain Adaptation Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class KAFParser {
	
	private KAFDocument inKaf;
	
	private static final String LINGUISTIC_PROCESSOR = "vicom-tourpedia-nel";
	private static final String RESOURCE             = "tourpedia_v1";
	
	public KAFParser(File inputFile) throws IOException {
		this.inKaf = KAFDocument.createFromFile(inputFile);
		this.addLinguisticProcessor();
	}
	
	public KAFParser(InputStream inStream) throws IOException {
		this.inKaf = KAFDocument.createFromStream(new InputStreamReader(inStream));
		this.addLinguisticProcessor();
	}
	
	private void addLinguisticProcessor() throws IOException {
		Properties p = new Properties();
		p.load(Main.class.getClass().getResourceAsStream("/version.prop"));
		String version = p.getProperty("version");
		//String v = KAFParser.class.getClass().getPackage().getImplementationVersion();
		this.inKaf.addLinguisticProcessor("entities", LINGUISTIC_PROCESSOR, this.getTimestamp(), version);
	}
	
	public String getTimestamp() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		String data = fmt.format(ts);
		fmt = new SimpleDateFormat("HH:mm:ss");
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		String time = fmt.format(ts);
		String timestamp = data + "T" + time + "Z";
		return timestamp;
	}
	
	public List<Entity> getEntities() {
		return this.inKaf.getEntities();
	}
	
	public void addExternalReference(int index, String reference) {
		ExternalRef ref = this.inKaf.createExternalRef(RESOURCE, reference);
		this.inKaf.getEntities().get(index).addExternalRef(ref);
	}
	
	public void write(OutputStream outStream) throws IOException {
		String kaf = this.inKaf.toString();
		OutputStreamWriter oow = null;
		try {
			oow = new OutputStreamWriter(outStream);
			oow.write(kaf);
		}
		finally {
			if (oow != null) oow.close();
		}
	}
	
	public void write(File outputFile) {
		this.inKaf.save(outputFile.getAbsolutePath());
	}
	
}
