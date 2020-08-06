/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import java.io.Serializable;

/**
 * Represents a field in a MARC21 mARC21Record. This is abstract class.
 * 
 * @author bojana
 * 
 */
@SuppressWarnings("serial")
public abstract class Field implements Serializable {

	public static char separator = '\036';
	
	protected String name;

	public Field() {
	}

	/**
	 * @param name
	 *            field name
	 */
	public Field(String name) {
		this.name = name;
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sorts subfields of field if exist
	 */
	void sort() {
	};

	public abstract void fromString(String str);

}
