/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ListRanking {
	private String value;
	private String listName;

	/**
	 *
	 */
	public ListRanking() {
		super();
	}

	public ListRanking(String value, String listName) {
		this.value = value;
		this.listName = listName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
}
