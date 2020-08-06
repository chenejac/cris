/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * @author bdimic@uns.ac.rs
 *
 */
public class AutocitationDTO implements Serializable {
	
	private String id;
	private PublicationDTO inPublication;
	private AuthorDTO forResearcher;
	private int numberOfAutocitationM20;
	private int numberOfAutocitationM50;
	
	
	
	/**
	 * 
	 */
	public AutocitationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	public AutocitationDTO(PublicationDTO inPublication,	AuthorDTO forResearcher) {
		super();
		this.inPublication = inPublication;
		this.forResearcher = forResearcher;
		
	}
	
	
	/**
	 * @param id
	 * @param inPublication
	 * @param forResearcher
	 */
	public AutocitationDTO(String id, PublicationDTO inPublication,
			AuthorDTO forResearcher) {
		super();
		this.id = id;
		this.inPublication = inPublication;
		this.forResearcher = forResearcher;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the inPublication
	 */
	public PublicationDTO getInPublication() {
		return inPublication;
	}
	/**
	 * @param inPublication the inPublication to set
	 */
	public void setInPublication(PublicationDTO inPublication) {
		this.inPublication = inPublication;
	}
	/**
	 * @return the forResearcher
	 */
	public AuthorDTO getForResearcher() {
		return forResearcher;
	}
	/**
	 * @param forResearcher the forResearcher to set
	 */
	public void setForResearcher(AuthorDTO forResearcher) {
		this.forResearcher = forResearcher;
	}
	/**
	 * @return the numberOfAutocitationM20
	 */
	public int getNumberOfAutocitationM20() {
		return numberOfAutocitationM20;
	}
	/**
	 * @param numberOfAutocitationM20 the numberOfAutocitationM20 to set
	 */
	public void setNumberOfAutocitationM20(int numberOfAutocitationM20) {
		this.numberOfAutocitationM20 = numberOfAutocitationM20;
	}
	/**
	 * @return the numberOfAutocitationM50
	 */
	public int getNumberOfAutocitationM50() {
		return numberOfAutocitationM50;
	}
	/**
	 * @param numberOfAutocitationM50 the numberOfAutocitationM50 to set
	 */
	public void setNumberOfAutocitationM50(int numberOfAutocitationM50) {
		this.numberOfAutocitationM50 = numberOfAutocitationM50;
	}
	
	
	

}
