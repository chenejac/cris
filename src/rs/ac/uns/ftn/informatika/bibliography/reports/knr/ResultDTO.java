/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.reports.knr;

import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultTypeDTO;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ResultDTO {

	private PublicationDTO publication;
	private ResultTypeDTO resultType;
	
	/**
	 * 
	 */
	public ResultDTO() {
		super();
	}
	/**
	 * @param resultType
	 * @param publication
	 */
	public ResultDTO(ResultTypeDTO resultType, PublicationDTO publication) {
		super();
		this.resultType = resultType;
		this.publication = publication;
	}
	/**
	 * @return the publication
	 */
	public PublicationDTO getPublication() {
		return publication;
	}
	/**
	 * @param publication the publication to set
	 */
	public void setPublication(PublicationDTO publication) {
		this.publication = publication;
	}
	/**
	 * @return the resultType
	 */
	public ResultTypeDTO getResultType() {
		return resultType;
	}
	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(ResultTypeDTO resultType) {
		this.resultType = resultType;
	}
	
	
	
}
