/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;

/**
 * Interface which must be implemented by managed beans that picking authors
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickAuthorManagedBean {

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(AuthorDTO author);

	/**
	 * Cancel picking author
	 */
	public void cancelPickingAuthor();
}
