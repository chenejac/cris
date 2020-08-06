/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;

/**
 * Interface which must be implemented by managed beans that picking monograph
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickMonographManagedBean {

	/**
	 * @param monograph
	 *            the monograph to set
	 */
	public void setMonograph(MonographDTO monograph);

	/**
	 * Cancel picking monograph
	 */
	public void cancelPickingMonograph();
}
