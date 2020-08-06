/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

/**
 * Represents a control field in MARC21 mARC21Record.
 * 
 * @author bojana
 * 
 */
@SuppressWarnings("serial")
public class ControlField extends Field {

	private MARC21Record owner;
	
	/**
	 * field content
	 */
	private String content;

	public ControlField() {
		this("");
	}

	/**
	 * @param name
	 *            field name
	 */
	public ControlField( String name) {
		this(name, "");
	}

	/**
	 * @param name
	 *            field name
	 * @param content
	 *            content
	 */
	public ControlField(String name,
			String content) {
		super(name);
		this.content = content;
		owner = null;
	}

	/**
	 * @return a printable string representation of this control field
	 */
	@Override
	public String toString() {
		return name + "    " + content;
	}

	/**
	 * loads data from MARC 21 string.
	 * 
	 * @param str
	 *            MARC 21 String
	 */
	@Override
	public void fromString(String str) {
		String[] pair = str.split("\\s+", 2);
		name = pair[0];
		content = pair[1];
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
		this.content = content;
	}

	/* char positions methods */

	/**
	 * @param start
	 *            start index
	 * @param length
	 *            length
	 * @return Substring of content
	 */
	public String getCharPositions(int start, int length) {
		return content.substring(start, start + length);
	}

	/**
	 * Adds substring to the content
	 * 
	 * @param start
	 *            start index
	 * @param value
	 *            value for adding into the content
	 */
	public void addCharPositions(int start, String value) {
		content = content.substring(0, start) + value
				+ content.substring(start + value.length());
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
