package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SciencesGroupDTO extends ClassDTO {

	public static final String SCIENCES_GROUP_SCHEMA = "sciencesGroup";
	
	private Calendar startDate;
	private Calendar endDate;
	
	public SciencesGroupDTO(){
		super();
		schemeId = SciencesGroupDTO.SCIENCES_GROUP_SCHEMA;
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2008);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param name
	 * @param description
	 */
	public SciencesGroupDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2008);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param term
	 * @param termTranslations
	 * @param description
	 * @param descriptionTranslations
	 * @param startDate
	 * @param endDate
	 */
	public SciencesGroupDTO(String schemeId, String classId,
			MultilingualContentDTO term,
			List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			Calendar startDate, Calendar endDate) {
		super(schemeId, classId, term, termTranslations, description,
				descriptionTranslations);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(term.getContent());
		return retVal.toString();
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getSomeTerm()
	 */
	@Override
	public String getSomeTerm() {
		// TODO Auto-generated method stub
//		locale = new Locale("en");
		return super.getSomeTerm();
	}
}
