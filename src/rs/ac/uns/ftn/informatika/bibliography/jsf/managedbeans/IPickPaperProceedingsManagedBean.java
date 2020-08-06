/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;

/**
 * Interface which must be implemented by managed beans that picking paper proceedings
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickPaperProceedingsManagedBean {

	/**
	 * @param paper proceedings
	 *            the paper proceedings to set
	 */
	public void setPaperProceedings(PaperProceedingsDTO paperProceedings);

	/**
	 * Cancel picking paper proceedings
	 */
	public void cancelPickingPaperProceedings();
}
