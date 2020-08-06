/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;

/**
 * Interface which must be implemented by managed beans that picking Organization unit
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickOrganizationUnitManagedBean {

	/**
	 * @param organizationUnit
	 *            the organization unit to set
	 */
	public void setOrganizationUnit(OrganizationUnitDTO organizationUnit);

	/**
	 * Cancel picking organizationUnit
	 */
	public void cancelPickingOrganizationUnit();
}
