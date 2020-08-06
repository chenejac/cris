package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

/**
 * Represents a subfield in a MARC21 mARC21Record.
 * 
 * @author bojana
 */

@SuppressWarnings("serial")
public class Subfield implements java.io.Serializable {

	public static char separator = '\037';
	private DataField owner;
	
	/**
	 * the name of this subfield
	 */
	private char name;
	/**
	 * subfield content; an empty string if the subfield is empty
	 */
	private String content;

	/**
	 * @param owner
	 */
	public Subfield() {
		name = ' ';
		content = "";
		owner = null;
	}

	/**
	 * Constructs a subfield with the given name.
	 * 
	 * @param name
	 *            The name of the subfield
	 */
	public Subfield(char name) {
		this.name = name;
		content = "";
		owner = null;
	}

	/**
	 * @return a printable string representation of this subfield.
	 */
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(""+Subfield.separator);
		retVal.append(name);
		retVal.append(content);
		return retVal.toString();
	}

	/**
	 * loads data from MARC 21 string.
	 * 
	 * @param str
	 *            MARC 21 String
	 */
	public void fromString(String str) {
		name = str.charAt(0);
		setContent(str.substring(1));
	}

	/**
	 * @return the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		if(content != null)
			content = content.replaceAll("\\s+", " ").replaceAll("\\p{C}", "").trim();
		this.content = content;
	}

	/**
	 * @return the name.
	 */
	public char getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(char name) {
		this.name = name;
	}

	/**
	 * @return the owner
	 */
	public DataField getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(DataField owner) {
		this.owner = owner;
	}
	
	

}