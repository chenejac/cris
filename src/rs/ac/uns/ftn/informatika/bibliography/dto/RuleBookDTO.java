package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;



/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class RuleBookDTO extends ClassDTO {	
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param name
	 * @param description
	 */
	public RuleBookDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
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
	
	
}
