/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;

/**
 * Interface which must be implemented by managed beans that picking conferences
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickConferenceManagedBean {

	/**
	 * @param conference
	 *            the conference to set
	 */
	public void setConference(ConferenceDTO conference);

	/**
	 * Cancel picking conference
	 */
	public void cancelPickingConference();
}
