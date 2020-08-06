package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.List;



/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ResultTypeDTO extends ClassDTO{

	private ResultTypeDTO superResultType;
	
	private RuleBookDTO ruleBook;
	
	
	public ResultTypeDTO() {
		super();
	}

	
	
	/**
	 * @param schemeId
	 * @param classId
	 * @param name
	 * @param description
	 * @param superResultType
	 * @param ruleBook
	 */
	public ResultTypeDTO(String schemeId, String classId, MultilingualContentDTO term, List<MultilingualContentDTO> termTranslations,
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations, 
			ResultTypeDTO superResultType, RuleBookDTO ruleBook) {
		super(schemeId, classId, term, termTranslations, description, descriptionTranslations);
		this.superResultType = superResultType;
		this.ruleBook = ruleBook;
	}

	/**
	 * @return the superResultType
	 */
	public ResultTypeDTO getSuperResultType() {
		return superResultType;
	}

	/**
	 * @param superResultType the superResultType to set
	 */
	public void setSuperResultType(ResultTypeDTO superResultType) {
		this.superResultType = superResultType;
	}

	/**
	 * @return the ruleBook
	 */
	public RuleBookDTO getRuleBook() {
		if (superResultType != null)
			return superResultType.getRuleBook();
		else 
			return ruleBook;
	}

	/**
	 * @param ruleBook the ruleBook to set
	 */
	public void setRuleBook(RuleBookDTO ruleBook) {
		this.ruleBook = ruleBook;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO#equals(java.lang.Object)
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return classId + " - " + term.getContent();
	}
	
	
	
}
