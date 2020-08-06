/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;

/**
 * Interface which must be implemented by managed beans that picking journals
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickJournalManagedBean {

	/**
	 * @param journal
	 *            the journal to set
	 */
	public void setJournal(JournalDTO journal);

	/**
	 * Cancel picking journal
	 */
	public void cancelPickingJournal();
}
