package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.io.Serializable;

/**
 * PrefixPair class groups two values <code>(prefName, value)</code> to pair.
 * 
 * @author mbranko@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PrefixValue implements Serializable, Comparable<PrefixValue> {

	/**
	 * Construct PrefixPair object.
	 * 
	 * @param prefName
	 *            prefix name
	 * @param value
	 *            value
	 */
	public PrefixValue(String prefName, String value) {
		this.prefName = prefName;
		this.value = value;
	}

	/**
	 * Construct PrefixPair object with empty attributes.
	 */
	public PrefixValue() {
		prefName = "";
		value = "";
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PrefixValue pv) {
		return prefName.compareTo(pv.prefName);
	}

	/* prefix name */
	private String prefName;

	/* value */
	private String value;

	/**
	 * @return prefix name
	 */
	public String getPrefName() {
		return prefName;
	}

	/**
	 * @param prefName
	 *            prefix name to set
	 */
	public void setPrefName(String prefName) {
		this.prefName = prefName;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
