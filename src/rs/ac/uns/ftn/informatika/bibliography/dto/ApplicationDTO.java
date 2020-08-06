/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ApplicationDTO {
	
	private JobAdDTO jobAd;
	private AuthorDTO person;
	private List<PublicationSelectionDTO> publications;
	
	/**
	 * @param jobAd
	 * @param person
	 * @param publications
	 */
	public ApplicationDTO(JobAdDTO jobAd, AuthorDTO person,
			List<PublicationSelectionDTO> publications) {
		super();
		this.jobAd = jobAd;
		this.person = person;
		this.publications = publications;
	}
	/**
	 * @return the jobAd
	 */
	public JobAdDTO getJobAd() {
		return jobAd;
	}
	/**
	 * @param jobAd the jobAd to set
	 */
	public void setJobAd(JobAdDTO jobAd) {
		this.jobAd = jobAd;
	}
	/**
	 * @return the person
	 */
	public AuthorDTO getPerson() {
		return person;
	}
	/**
	 * @param person the person to set
	 */
	public void setPerson(AuthorDTO person) {
		this.person = person;
	}
	/**
	 * @return the publications
	 */
	public List<PublicationSelectionDTO> getPublications() {
		return publications;
	}
	/**
	 * @param publications the publications to set
	 */
	public void setPublications(List<PublicationSelectionDTO> publications) {
		this.publications = publications;
	}

}
