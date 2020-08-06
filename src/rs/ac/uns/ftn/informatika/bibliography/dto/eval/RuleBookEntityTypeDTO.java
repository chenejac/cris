package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class RuleBookEntityTypeDTO implements Serializable{
	
	private RuleBookDTO ruleBook;
	private ClassDTO entityType;

	public RuleBookEntityTypeDTO() {
		super();
	}
	
	/**
	 * @param ruleBook
	 * @param entityType
	 */
	public RuleBookEntityTypeDTO(RuleBookDTO ruleBook, ClassDTO entityType) {
		super();
		this.ruleBook = ruleBook;
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
			RuleBookEntityTypeDTO temp = (RuleBookEntityTypeDTO) arg0;
			if ((this.entityType != null) && (this.entityType.equals(temp.entityType)) && 
					(this.ruleBook != null) && (this.ruleBook.equals(temp.ruleBook)))
				return true;
			else
				return false;
		} catch (Throwable e) {

			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return entityType.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ruleBook + " " +entityType.toString();
	}
}
