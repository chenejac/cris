package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.Serializable;

/**
 * AttributePair class groups two values <code>(attributeName, value)</code> to pair.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AttributeValue implements Serializable{

	/**
	 * Construct AttributePair object.
	 * 
	 * @param attrName
	 *            attrix name
	 * @param value
	 *            value
	 */
	public AttributeValue(String attrName, String value) {
		this.attrName = attrName;
		this.value = value;
	}

	/**
	 * Construct AttributePair object with empty attributes.
	 */
	public AttributeValue() {
		attrName = "";
		value = "";
	}


	/* attrix name */
	private String attrName;

	/* value */
	private String value;

	/**
	 * @return attrix name
	 */
	public String getPrefName() {
		return attrName;
	}

	/**
	 * @param attrName
	 *            attrix name to set
	 */
	public void setPrefName(String attrName) {
		this.attrName = attrName;
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
