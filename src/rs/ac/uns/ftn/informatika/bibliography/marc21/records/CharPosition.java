/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import java.io.Serializable;

/**
 * @author bojana
 * 
 */
@SuppressWarnings("serial")
public class CharPosition implements Serializable {

	/**
	 * starting position
	 */
	private int start;
	/**
	 * length
	 */
	private int length;
	/**
	 * the content of this character position
	 */
	private String content;

	/**
	 * @param length
	 * @param start
	 */
	public CharPosition(int length, int start) {
		this.length = length;
		this.start = start;
	}

	/**
	 * @return Returns the content.
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

	/**
	 * @return Returns the length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            The length to set.
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(int start) {
		this.start = start;
	}

}
