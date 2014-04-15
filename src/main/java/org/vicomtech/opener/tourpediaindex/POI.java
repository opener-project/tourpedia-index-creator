package org.vicomtech.opener.tourpediaindex;

/**
 * This class represents the POIs to add to the tourpedia index
 * 
 * org.vicomtech.opener.tourpediaindex is a module of Tourpedia Index Tool for OpeNER
 * @author Andoni Azpeitia (aazpeitia@vicomtech.org) - Vicomtech-IK4 (http://www.vicomtech.es/)
 *
 */
public class POI {

	public static enum Field {
		ID(0), NAME(1), DIRECTION(2), TYPE(3), COUNTRY(4), URL(5);
		private int index;
		private Field(int index) {
			this.index = index;
		}
		private int getIndex() {
			return this.index;
		}
	}
	
	private int id = -1;
	private String name = null;
	private String direction = null;
	private String type = null;
	private String country = null;
	private String url = null;
	
	private final static int SIZE = 6;
	private final static String SEPARATOR = ";";
	
	public POI(String line) {
		String[] fields = line.split(SEPARATOR);
		if (fields.length == SIZE) {
			// ID
			this.id = parseInt(fields, Field.ID.getIndex());
			// NAME
			this.name = parseString(fields, Field.NAME.getIndex());
			// DIRECTION
			this.direction = parseString(fields, Field.DIRECTION.getIndex());
			// TYPE
			this.type = parseString(fields, Field.TYPE.getIndex());
			// COUNTRY
			this.country = parseString(fields, Field.COUNTRY.getIndex());
			// URL
			this.url = parseString(fields, Field.URL.getIndex());
		}
	}
	
	private int parseInt(String[] fields, int index) {
		String text = fields[index].trim().toLowerCase();
		return Integer.parseInt(text.substring(1, text.length()-1).trim().replaceAll(" ", "_"));
	}
	
	private String parseString(String[] fields, int index) {
		String text = fields[index].trim().toLowerCase();
		return text.substring(1, text.length()-1).trim().replaceAll(" ", "_");
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public boolean isEmpty() {
		return this.id < 0;
	}
}
