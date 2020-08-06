package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which present publisher name (default name or other format name).
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PublisherNameDTO implements Serializable {

	private String name;
	private String place;
	private String state;
	private String language;
	private String transType = MultilingualContentDTO.TRANS_HUMAN;
	
	public PublisherNameDTO() {
	}
	
	/**
	 * @param name
	 * @param place
	 * @param state
	 * @param language
	 * @param transType
	 */
	public PublisherNameDTO(String name, String place, String state,
			String language, String transType) {
		super();
		this.name = name;
		this.place = place;
		this.state = state;
		this.language = language;
		this.transType = transType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place
	 *            the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @return the localized language
	 */
	public String getLocalizedLanguage() {
		return new FacesMessages("messages.messages-records",  new Locale("sr"))
		//FacesContext.getCurrentInstance().getViewRoot().getLocale())
			.getMessageFromResourceBundle("records.language."
					+ language);
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	

	/**
	 * @return the transType
	 */
	public String getTransType() {
		return transType;
	}

	/**
	 * @param transType the transType to set
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		if((name != null) && (!"".equals(name.trim())))
			retVal.append(name);
		if((place != null) && (!"".equals(place.trim())))
			retVal.append(", " + place);
		if((state != null) && (!"".equals(state.trim())))
			retVal.append(", " + state);
		return retVal.toString();
	}

}
