/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;

/**
 * Interface which must be implemented by managed beans that picking research area
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickResearchAreaManagedBean {

	/**
	 * @param researchArea
	 *            the researchArea to set
	 */
	public void setResearchArea(ResearchAreaDTO researchArea);

	/**
	 * Cancel picking research area
	 */
	public void cancelPickingResearchArea();
}
