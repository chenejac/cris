/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bojana
 * 
 */
@SuppressWarnings("serial")
public class DataField extends Field {

	private MARC21Record owner;
	
	/**
	 * the value of the first indicator
	 */
	private char ind1;

	/**
	 * the value of the second indicator
	 */
	private char ind2;

	/**
	 * the list of subfields
	 */
	private List<Subfield> subfields;

	public DataField() {
		this("", ' ', ' ');
	}

	/**
	 * @param name
	 *            The name of the field
	 */
	public DataField(String name) {
		this(name, ' ', ' ');
	}

	/**
	 * @param name
	 *            The name of the field
	 * @param ind1
	 *            The value of the first indicator
	 * @param ind2
	 *            The value of the second indicator
	 */
	public DataField(String name, char ind1, char ind2) {
		super(name);
		this.ind1 = ind1;
		this.ind2 = ind2;
		subfields = new ArrayList<Subfield>();
		owner = null;
	}

	/**
	 * @return the first indicator.
	 */
	public char getInd1() {
		return ind1;
	}

	/**
	 * @param ind1
	 *            The first indicator to set.
	 */
	public void setInd1(char ind1) {
		this.ind1 = ind1;
	}

	/**
	 * @return the second indicator.
	 */
	public char getInd2() {
		return ind2;
	}

	/**
	 * @param ind2
	 *            The second indicator to set.
	 */
	public void setInd2(char ind2) {
		this.ind2 = ind2;
	}

	/**
	 * @return the indicator with a given index (1 or 2)
	 */
	public char getInd(int index) {
		if (index == 1)
			return this.getInd1();
		else
			return this.getInd2();
	}

	/**
	 * @return the subfields.
	 */
	public List<Subfield> getSubfields() {
		return subfields;
	}

	/**
	 * @param subfields
	 *            The subfields to set.
	 */
	public void setSubfields(List<Subfield> subfields) {
		this.subfields = new ArrayList<Subfield>();
		for (Subfield subfield : subfields) {
			addSubfield(subfield);
		}
	}

	/**
	 * Adds a subfield to this field.
	 * 
	 * @param sf
	 *            The subfield to be added
	 */
	public boolean addSubfield(Subfield sf) {
		sf.setOwner(this);
		return subfields.add(sf);
	}

	/**
	 * Retrieves number of subfields in list
	 * 
	 * @return number of subfields
	 */
	public int getSubfieldCount() {
		return subfields.size();
	}

	/**
	 * Retrieves subfield from the list of subfields
	 * 
	 * @param index
	 *            index of subfield which should be retrieved.
	 * @return subfield
	 */
	public Subfield getSubfield(int index) {
		return (Subfield) subfields.get(index);
	}

	/**
	 * Retrieves subfield from the list of subfields
	 * 
	 * @param name
	 *            name of subfield which should be retrieved.
	 * @return subfield
	 */
	public Subfield getSubfield(char name) {
		Subfield retVal = null;
		for (Subfield sf : subfields) {
			if (sf.getName() == name)
				retVal = sf;
		}
		return retVal;
	}

	/**
	 * Retrieves the list of subfields with given name
	 * 
	 * @param name
	 *            name of subfields which should be retrieved.
	 * @return list of subfields
	 */
	public List<Subfield> getSubfields(char name) {
		List<Subfield> retVal = new ArrayList<Subfield>();
		for (Subfield sf : subfields)
			if (sf.getName() == name)
				retVal.add(sf);
		return retVal;
	}

	/**
	 * Sorts subfields by their names
	 * 
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.Field#sort()
	 */
	@Override
	void sort() {
		for (int i = 1; i < subfields.size(); i++) {
			for (int j = 0; j < subfields.size() - i; j++) {
				Subfield sf1 = (Subfield) subfields.get(j);
				Subfield sf2 = (Subfield) subfields.get(j + 1);
				if (sf1.getName() > sf2.getName()) {
					subfields.set(j, sf2);
					subfields.set(j + 1, sf1);
				}
			}
		}
	}

	/**
	 * @return a printable string representation of this data field
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(name);
		retVal.append(' ');
		retVal.append(getInd1() == ' ' ? '#' : getInd1());
		retVal.append(getInd2() == ' ' ? '#' : getInd2());
		retVal.append(' ');
		for (int i = 0; i < subfields.size(); i++) {
			retVal.append(subfields.get(i).toString());
		}
		return retVal.toString();

	}

	/**
	 * loads data from MARC 21 string.
	 * 
	 * @param str
	 *            MARC 21 String
	 */
	@Override
	public void fromString(String str) {
		String[] df = str.split("\\s+", 3);
		name = df[0];
		ind1 = (df[1].charAt(0) == '#' ? ' ' : df[1].charAt(0));
		ind2 = (df[1].charAt(1) == '#' ? ' ' : df[1].charAt(1));
		
		String[] sfs = null;
		if(this.name.equals("678"))
			sfs = df[2].split(""+Subfield.separator, 2);
		else
			sfs = df[2].split(""+Subfield.separator);
//		if(df[2].contains(""+Subfield.separator)){
//		} else {
//			if((this.name.equals("245")) || (this.name.equals("500")) || (this.name.equals("520")) || (this.name.equals("653")) || (this.name.equals("856")))
//				sfs = df[2].split("\\$", 2);
//			else if (this.name.equals("880"))
//					sfs = df[2].split("\\$", 3);
//				else 
//					sfs = df[2].split("\\$");
//		}
		
		for (String sf : sfs) {
			if ((sf != null) && (sf.length() > 0)) {
				Subfield subfield = new Subfield();
				if(this.name.equals("678"))
					subfield.fromString(sf.replace(""+Subfield.separator, ""));
				else
					subfield.fromString(sf);
				addSubfield(subfield);
			}
		}
	}

	/**
	 * @return the owner
	 */
	public MARC21Record getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(MARC21Record owner) {
		this.owner = owner;
	}
	
}
