package rs.ac.uns.ftn.informatika.bibliography.utils;

/** Classes that implements Sortable interface for
 *  report Bibliografija
 *
 *  @author Tanja Tosic, ttosic@uns.ns.ac.yu
 *  @version 1.0
 *  @see Sortable
 */

import java.text.RuleBasedCollator;
import java.util.Vector;

import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;

import com.gint.util.sort.Sortable;

public class SortableBiblio implements Sortable {
	private MARC21Record mARC21Record;
	private Vector<String> sortItems = new Vector<String>();
	private RuleBasedCollator collator;

	/**
	 * Constructor.
	 * 
	 * @param mARC21Record
	 *            mARC21Record
	 * @param sortItems
	 *            Vector of objects for sorting.
	 */
	public SortableBiblio(MARC21Record mARC21Record, Vector<String> sortItems) {
		this.mARC21Record = mARC21Record;
		this.sortItems = sortItems;
		this.collator = (RuleBasedCollator) SortUtils.getLatCyrCollator();
	}

	/**
	 * Compares two objects.
	 * 
	 * @param param
	 *            The other object in comparison.
	 * @return -1 if <code>this</code> is a predecessor to <code>obj</code><br>
	 *         0 if <code>this</code> and <code>obj</code> are equal (as
	 *         indicated by the <code>equals</code> method<br>
	 *         1 if <code>this</code> is a successor to <code>obj</code>
	 */
	public int compareTo(Object param) {
		String first = "", second = "";

		SortableBiblio obj = (SortableBiblio) param;
		for (int i = 0; i < sortItems.size(); i++) {
			first = sortItems.elementAt(i);
			second = obj.sortItems.elementAt(i);
			if (collator.compare(first, second) < 0) {
				return -1;
			}
			if (collator.compare(first, second) > 0) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * @return mARC21Record
	 */
	public MARC21Record getRecord() {
		return mARC21Record;
	}
}