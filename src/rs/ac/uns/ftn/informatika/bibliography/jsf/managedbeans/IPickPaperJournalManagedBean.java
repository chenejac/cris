/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;

/**
 * Interface which must be implemented by managed beans that picking paper journal
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickPaperJournalManagedBean {

	/**
	 * @param paper journal
	 *            the paper journal to set
	 */
	public void setPaperJournal(PaperJournalDTO paperJournal);

	/**
	 * Cancel picking paper journal
	 */
	public void cancelPickingPaperJournal();
}
