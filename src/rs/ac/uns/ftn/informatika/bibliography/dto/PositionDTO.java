package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;


/**
 * DTO class which represents researcher position.
 *  
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PositionDTO extends ClassDTO {

	public static String POSITION_SCHEMA = "position";
	
	public PositionDTO() {
		schemeId = PositionDTO.POSITION_SCHEMA;
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param term
	 * @param termTranslations
	 * @param description
	 * @param descriptionTranslations
	 */
	public PositionDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getClassId()
	 */
	@Override
	public String getClassId() {
		return super.getClassId();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getSomeTerm()
	 */
	@Override
	public String getSomeTerm() {
		return super.getSomeTerm();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getSomeTerm();
	}

}
