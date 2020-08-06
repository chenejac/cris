/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;

/**
 * Interface which must be implemented by managed beans that picking position
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickPositionManagedBean {

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(PositionDTO position);

	/**
	 * Cancel picking position
	 */
	public void cancelPickingPosition();
}
