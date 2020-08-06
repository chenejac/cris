package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO class which presents data about publisher.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PublisherDTO implements Serializable{

	private PublisherNameDTO originalPublisher;
	private List<PublisherNameDTO> publisherTranslations;

	public PublisherDTO() {
		publisherTranslations = new ArrayList<PublisherNameDTO>();
		originalPublisher = new PublisherNameDTO();
		originalPublisher.setTransType(MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param originalPublisher
	 *            original publisher name
	 * @param publisherTranslations
	 *            translations of publisher name
	 */
	public PublisherDTO(PublisherNameDTO originalPublisher,
			List<PublisherNameDTO> publisherTranslations) {
		this.originalPublisher = originalPublisher;
		this.publisherTranslations = publisherTranslations;
	}

	/**
	 * @return the originalPublisher
	 */
	public PublisherNameDTO getOriginalPublisher() {
		return originalPublisher;
	}

	/**
	 * @param originalPublisher the originalPublisher to set
	 */
	public void setOriginalPublisher(PublisherNameDTO originalPublisher) {
		this.originalPublisher = originalPublisher;
	}

	/**
	 * @return the publisherTranslations
	 */
	public List<PublisherNameDTO> getPublisherTranslations() {
		return publisherTranslations;
	}

	/**
	 * @param publisherTranslations the publisherTranslations to set
	 */
	public void setPublisherTranslations(
			List<PublisherNameDTO> publisherTranslations) {
		this.publisherTranslations = publisherTranslations;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if ((originalPublisher.getName()!=null) && (!("".equals(originalPublisher.getName())))){
			return originalPublisher.toString();
		} else if (publisherTranslations.size()>0)
			return publisherTranslations.get(0).toString(); 		
		else 
			return "";
	}


}
