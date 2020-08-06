/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;

/**
 * Interface which must be implemented by managed beans that picking institution
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickInstitutionManagedBean {

	/**
	 * @param institution
	 *            the institution to set
	 */
	public void setInstitution(InstitutionDTO institution);

	/**
	 * Cancel picking institution
	 */
	public void cancelPickingInstitution();
}
