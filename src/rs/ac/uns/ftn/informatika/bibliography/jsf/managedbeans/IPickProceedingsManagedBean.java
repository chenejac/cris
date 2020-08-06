/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;

/**
 * Interface which must be implemented by managed beans that picking proceedings
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickProceedingsManagedBean {

	/**
	 * @param proceedings
	 *            the proceedings to set
	 */
	public void setProceedings(ProceedingsDTO proceedings);

	/**
	 * Cancel picking proceedings
	 */
	public void cancelPickingProceedings();
}
