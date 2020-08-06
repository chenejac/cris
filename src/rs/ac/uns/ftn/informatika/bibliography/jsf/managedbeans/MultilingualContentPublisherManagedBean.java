package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.List;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;

/**
 * Managed bean with CRUD operations for multilingual content of publisher
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MultilingualContentPublisherManagedBean extends AMultilingualContentManagedBean {

		
	private List<PublisherNameDTO> publisherTranslations = null;

	private String pubName = null;
	
	private String pubPlace = null;
	
	private String pubState = null;
	
		
	/**
	 * @return the translations of publisher name
	 */
	public List<PublisherNameDTO> getPublisherTranslations() {
		return publisherTranslations;
	}

	/**
	 * @param publisherTranslations the publisherTranslations to set
	 */
	public void setPublisherTranslations(List<PublisherNameDTO> publisherTranslations) {
		this.publisherTranslations = publisherTranslations;
	}

	/**
	 * @return the pubName
	 */
	public String getPubName() {
		return pubName;
	}

	/**
	 * @param pubName the pubName to set
	 */
	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	/**
	 * @return the pubPlace
	 */
	public String getPubPlace() {
		return pubPlace;
	}

	/**
	 * @param pubPlace the pubPlace to set
	 */
	public void setPubPlace(String pubPlace) {
		this.pubPlace = pubPlace;
	}

	/**
	 * @return the pubState
	 */
	public String getPubState() {
		return pubState;
	}

	/**
	 * @param pubState the pubState to set
	 */
	public void setPubState(String pubState) {
		this.pubState = pubState;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.AMultilingualContentManagedBean#add()
	 */
	@Override
	public void add() {
		PublisherNameDTO pn = new PublisherNameDTO();
		pn.setName(pubName);
		pn.setPlace(pubPlace);
		pn.setState(pubState);
		pn.setLanguage(language);
		publisherTranslations.add(pn);
		pubName = null;
		pubPlace = null;
		pubState = null;
		language = null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.AMultilingualContentManagedBean#remove()
	 */
	@Override
	public void remove() {
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String ordNum = facesCtx.getExternalContext()
					.getRequestParameterMap().get("ordNum");
			publisherTranslations.remove(Integer.parseInt(ordNum));
		} catch (Exception e) {
		}
	}
}
