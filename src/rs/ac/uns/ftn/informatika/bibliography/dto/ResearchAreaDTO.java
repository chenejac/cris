package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;



/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ResearchAreaDTO extends ClassDTO{

	public static String RESEARCH_AREA_SCHEMA = "researchArea";
	
	private ResearchAreaDTO superResearchArea;

	public ResearchAreaDTO(){
		super();
		schemeId = ResearchAreaDTO.RESEARCH_AREA_SCHEMA;
	}
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param name
	 * @param description
	 * @param superResultType
	 * @param ruleBook
	 */
	public ResearchAreaDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations, 
			ResearchAreaDTO superResearchArea) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
		this.superResearchArea = superResearchArea;
	}

	/**
	 * @return the superResearchArea
	 */
	public ResearchAreaDTO getSuperResearchArea() {
		return superResearchArea;
	}

	/**
	 * @param superResearchArea the superResearchArea to set
	 */
	public void setSuperResearchArea(ResearchAreaDTO superResearchArea) {
		this.superResearchArea = superResearchArea;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#getClassId()
	 */
	@Override
	public String getClassId() {
		return super.getClassId();
	}



	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getSomeTerm();
	}
	
	public String getFullHierarchy(){
		StringBuffer retVal = new StringBuffer();
		if ((superResearchArea != null) && (! ("".equals(superResearchArea.getSomeTerm())))){
			retVal.append(superResearchArea.getFullHierarchy() + " / ");
		}
		retVal.append(getSomeTerm());
		return retVal.toString();
	}
	
}
