/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

/**
 * Interface which must be implemented by managed beans that picking study final document
 * 
 * @author chenejac@uns.ac.rs
 */
public interface IPickStudyFinalDocumentManagedBean {

	/**
	 * @param study final document
	 *            the study final document to set
	 */
	public void setStudyFinalDocument(StudyFinalDocumentDTO studyFinalDocument);

	/**
	 * Cancel picking study final document
	 */
	public void cancelPickingStudyFinalDocument();
}
