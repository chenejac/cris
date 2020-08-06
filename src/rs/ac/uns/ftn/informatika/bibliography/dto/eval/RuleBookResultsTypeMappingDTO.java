package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class RuleBookResultsTypeMappingDTO implements Serializable{
	
	private Integer mappingId;
	
	private RuleBookDTO ruleBook;
	private ClassDTO researcherRole;
	private ClassDTO entityType;
	private ClassDTO entitySourceType;
	private ResultsTypeDTO resultsTypeForResearcherRole;
	private ResultsTypeDTO resultsTypeOfObsEntity;
	private ResultsTypeDTO resultsTypeOfObsEntitySource;
	
	/**
	 * 
	 */
	public RuleBookResultsTypeMappingDTO() {
		super();
	}
	
	/**
	 * @param mappingId
	 * @param ruleBook
	 * @param researcherRole
	 * @param entityType
	 * @param entitySourceType
	 * @param resultsTypeForResearcherRole
	 * @param resultsTypeOfObsEntity
	 * @param resultsTypeOfObsEntitySource
	 */
	public RuleBookResultsTypeMappingDTO(Integer mappingId, RuleBookDTO ruleBook,
			ClassDTO researcherRole, ClassDTO entityType,
			ClassDTO entitySourceType,
			ResultsTypeDTO resultsTypeForResearcherRole,
			ResultsTypeDTO resultsTypeOfObsEntity,
			ResultsTypeDTO resultsTypeOfObsEntitySource) {
		super();
		this.mappingId = mappingId;
		this.ruleBook = ruleBook;
		this.researcherRole = researcherRole;
		this.entityType = entityType;
		this.entitySourceType = entitySourceType;
		this.resultsTypeForResearcherRole = resultsTypeForResearcherRole;
		this.resultsTypeOfObsEntity = resultsTypeOfObsEntity;
		this.resultsTypeOfObsEntitySource = resultsTypeOfObsEntitySource;
	}

	/**
	 * @return the mappingId
	 */
	public Integer getMappingId() {
		return mappingId;
	}

	/**
	 * @param mappingId the mappingId to set
	 */
	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
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
	 * @return the researcherRole
	 */
	public ClassDTO getResearcherRole() {
		return researcherRole;
	}

	/**
	 * @param researcherRole the researcherRole to set
	 */
	public void setResearcherRole(ClassDTO researcherRole) {
		this.researcherRole = researcherRole;
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
	 * @return the entitySourceType
	 */
	public ClassDTO getEntitySourceType() {
		return entitySourceType;
	}

	/**
	 * @param entitySourceType the entitySourceType to set
	 */
	public void setEntitySourceType(ClassDTO entitySourceType) {
		this.entitySourceType = entitySourceType;
	}

	/**
	 * @return the resultsTypeForResearcherRole
	 */
	public ResultsTypeDTO getResultsTypeForResearcherRole() {
		return resultsTypeForResearcherRole;
	}

	/**
	 * @param resultsTypeForResearcherRole the resultsTypeForResearcherRole to set
	 */
	public void setResultsTypeForResearcherRole(
			ResultsTypeDTO resultsTypeForResearcherRole) {
		this.resultsTypeForResearcherRole = resultsTypeForResearcherRole;
	}

	/**
	 * @return the resultsTypeOfObsEntity
	 */
	public ResultsTypeDTO getResultsTypeOfObsEntity() {
		return resultsTypeOfObsEntity;
	}

	/**
	 * @param resultsTypeOfObsEntity the resultsTypeOfObsEntity to set
	 */
	public void setResultsTypeOfObsEntity(ResultsTypeDTO resultsTypeOfObsEntity) {
		this.resultsTypeOfObsEntity = resultsTypeOfObsEntity;
	}

	/**
	 * @return the resultsTypeOfObsEntitySource
	 */
	public ResultsTypeDTO getResultsTypeOfObsEntitySource() {
		return resultsTypeOfObsEntitySource;
	}

	/**
	 * @param resultsTypeOfObsEntitySource the resultsTypeOfObsEntitySource to set
	 */
	public void setResultsTypeOfObsEntitySource(
			ResultsTypeDTO resultsTypeOfObsEntitySource) {
		this.resultsTypeOfObsEntitySource = resultsTypeOfObsEntitySource;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		try {
			RuleBookResultsTypeMappingDTO temp = (RuleBookResultsTypeMappingDTO) arg0;
			if ((this.mappingId != null) && (temp.mappingId != null) && (this.mappingId.intValue() == mappingId.intValue()))
				return true;
			else if ((this.mappingId == null || temp.mappingId == null) && (
					(this.ruleBook != null) && (this.ruleBook.equals(temp.ruleBook)) &&
					(this.researcherRole != null) && (this.researcherRole.equals(temp.researcherRole)) &&
					(this.entityType != null) && (this.entityType.equals(temp.entityType)) &&
					((this.entitySourceType == temp.entitySourceType) || ((this.entitySourceType != null) && (this.entitySourceType.equals(temp.entitySourceType)))) &&
					(this.resultsTypeForResearcherRole != null) && (this.resultsTypeForResearcherRole.equals(temp.resultsTypeForResearcherRole)) &&
					((this.resultsTypeOfObsEntity == temp.resultsTypeOfObsEntity) || ((this.resultsTypeOfObsEntity != null) && (this.resultsTypeOfObsEntity.equals(temp.resultsTypeOfObsEntity)))) &&
					((this.resultsTypeOfObsEntitySource == temp.resultsTypeOfObsEntitySource) || ((this.resultsTypeOfObsEntitySource != null) && (this.resultsTypeOfObsEntitySource.equals(temp.resultsTypeOfObsEntitySource)))) 
					))
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
		return mappingId + ":" + ruleBook+ ";"+ researcherRole + ";" + entityType + ";"+ (entitySourceType==null?"/":entitySourceType) + ";" + resultsTypeForResearcherRole 
				+ ";" + (resultsTypeOfObsEntity==null?"/":resultsTypeOfObsEntity)+ ";" + (resultsTypeOfObsEntitySource==null?"/":resultsTypeOfObsEntitySource);
	}
}
