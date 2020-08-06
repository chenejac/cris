package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TitleInstitution implements Serializable{
	
	private String title = "";
	private Integer year;
	private InstitutionDTO institution;
	
	/**
	 * 
	 */
	public TitleInstitution() {
		super();
		institution = new InstitutionDTO();
	}
	
	/**
	 * @param title
	 * @param year
	 * @param institution
	 */
	public TitleInstitution(String title, Integer year,
			InstitutionDTO institution) {
		super();
		this.title = title;
		this.year = year;
		this.institution = institution;
	}



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		return institution;
	}
	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		if (! "".equals(institution.toString()))
			retVal.append("Titula " + title + ": " + institution.getName().getContent() + ", " + year + " godine");
		return retVal.toString();
	}
	
	

}
