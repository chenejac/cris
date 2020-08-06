/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;


/**
 * @author chenejac@uns.ac.rs
 *
 */
public class PublicationRecommendationDTO {
	
	private PublicationDTO publication;
	private boolean recommended = false;
	private boolean notRecommended = false;
	
	/**
	 * @param publication
	 * @param recommended
	 * @param notRecommended
	 */
	public PublicationRecommendationDTO(PublicationDTO publication, boolean recommended, boolean notRecommended) {
		super();
		this.publication = publication;
		this.recommended = recommended;
		this.notRecommended = notRecommended;
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
	 * @return the recommended
	 */
	public boolean isRecommended() {
		return recommended;
	}
	/**
	 * @param recommended the recommended to set
	 */
	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}
	/**
	 * @return the notRecommended
	 */
	public boolean isNotRecommended() {
		return notRecommended;
	}
	/**
	 * @param notRecommended the notRecommended to set
	 */
	public void setNotRecommended(boolean notRecommended) {
		this.notRecommended = notRecommended;
	}

}
