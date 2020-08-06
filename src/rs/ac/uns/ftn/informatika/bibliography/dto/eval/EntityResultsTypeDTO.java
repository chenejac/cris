package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class EntityResultsTypeDTO implements Serializable{
	
	private RuleBookDTO ruleBook;
	private ResultsTypeDTO resultsType;
	private ClassDTO entityType;
	
	public EntityResultsTypeDTO() {
		super();
	}

	/**
	 * @param ruleBook
	 * @param resultsType
	 * @param entityType
	 */
	public EntityResultsTypeDTO(RuleBookDTO ruleBook,
			ResultsTypeDTO resultsType, ClassDTO entityType) {
		super();
		this.ruleBook = ruleBook;
		this.resultsType = resultsType;
		this.entityType = entityType;
	}
	
	/**
	 * @return the ruleBook
	 */
	public RuleBookDTO getRuleBook() {
		return ruleBook;
	}

	/**
	 * @param ruleBook the ruleBook to set
	 */
	public void setRuleBook(RuleBookDTO ruleBook) {
		this.ruleBook = ruleBook;
	}

	/**
	 * @return the resultsType
	 */
	public ResultsTypeDTO getResultsType() {
		return resultsType;
	}

	/**
	 * @param resultsType the resultsType to set
	 */
	public void setResultsType(ResultsTypeDTO resultsType) {
		this.resultsType = resultsType;
	}

	/**
	 * @return the entityType
	 */
	public ClassDTO getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(ClassDTO entityType) {
		this.entityType = entityType;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		try {
			EntityResultsTypeDTO temp = (EntityResultsTypeDTO) arg0;
			if ((this.ruleBook != null) && (this.ruleBook.getControlNumber().equals(temp.ruleBook.getControlNumber())) && 
					(this.resultsType != null) && (this.resultsType.equals(temp.resultsType)) &&
					(this.entityType != null) && (this.entityType.equals(temp.entityType)))
				return true;
			else
				return false;
		} catch (Throwable e) {

			return false;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return entityType + ":" + resultsType+ " "+ ruleBook;
	}
}
