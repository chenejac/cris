/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which represents multilingual content  
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class MultilingualContentDTO implements Serializable {

	public static String TRANS_ORIGINAL = "o";
	public static String TRANS_HUMAN = "h";
	public static String TRANS_MACHINE = "m";
	
	public static String LANGUAGE_SERBIAN = "srp";
	public static String LANGUAGE_ENGLISH = "eng";
	
	protected String content;
	protected String language;
	protected String transType = MultilingualContentDTO.TRANS_HUMAN;
	
	public MultilingualContentDTO() {
	}
	
	/**
	 * @param allInformation
	 */
	public MultilingualContentDTO(String allInformation) {
		parseString(allInformation);
	}

	/**
	 * @param content
	 * @param language
	 * @param transType
	 */
	public MultilingualContentDTO(String content, String language,
			String transType) {
		super();
		setContent(content);
		this.language = language;
		this.transType = transType;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		if(content!=null)
			content = content.replaceAll("\\p{C}", "");
		this.content = content;
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		MultilingualContentDTO mc = (MultilingualContentDTO) obj;
		try {
			if ((!(mc.language.equals(this.language)))
					|| (!(mc.content.equals(this.content)))) {
				retVal = false;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return language + "|" + transType + "|" + content ;
	}
	
	private void parseString(String allInformation){
		String[] params = allInformation.split("\\|");
		language = params[0];
		transType = params[1];
		setContent(params[2]);
	}
	

}
