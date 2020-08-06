package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;
/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsTypeMeasureDTO implements Serializable {

	private Double quantitativeMeasure;
	private RuleBookDTO ruleBook;
	private ResultsTypeDTO resultsType;
	private SciencesGroupDTO sciencesGroup;
	
	public ResultsTypeMeasureDTO() {
		super();
	}

	/**
	 * @param ruleBook
	 * @param resultsType
	 * @param sciencesGroup
	 * @param quantitativeMeasure
	 */
	public ResultsTypeMeasureDTO(RuleBookDTO ruleBook,
			ResultsTypeDTO resultsType, SciencesGroupDTO sciencesGroup, Double quantitativeMeasure) {
		super();
		this.quantitativeMeasure = quantitativeMeasure;
		this.ruleBook = ruleBook;
		this.resultsType = resultsType;
		this.sciencesGroup = sciencesGroup;
	}



	/**
	 * @return the quantitativeMeasure
	 */
	public Double getQuantitativeMeasure() {
		return quantitativeMeasure;
	}

	/**
	 * @param quantitativeMeasure the quantitativeMeasure to set
	 */
	public void setQuantitativeMeasure(Double quantitativeMeasure) {
		this.quantitativeMeasure = quantitativeMeasure;
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
	 * @return the sciencesGroup
	 */
	public SciencesGroupDTO getSciencesGroup() {
		return sciencesGroup;
	}

	/**
	 * @param sciencesGroup the sciencesGroup to set
	 */
	public void setSciencesGroup(SciencesGroupDTO sciencesGroup) {
		this.sciencesGroup = sciencesGroup;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		try {
			ResultsTypeMeasureDTO temp = (ResultsTypeMeasureDTO) arg0;
			if ((this.resultsType != null) && (this.resultsType.equals(temp.resultsType)) &&
					(this.ruleBook != null) && (this.ruleBook.equals(temp.ruleBook)) &&
					(this.sciencesGroup != null) && (this.sciencesGroup.equals(temp.sciencesGroup)))
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
		return ruleBook.hashCode() + resultsType.hashCode() + sciencesGroup.hashCode();
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return resultsType.toString() + " ( " + quantitativeMeasure + "p. ) in " + sciencesGroup;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toStringWithoutSciencesGroup() {
		return resultsType.toString() + " ( " + quantitativeMeasure + "p. )";
	}

}
