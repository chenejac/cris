package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeMeasureDAO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class RuleBookResultsTypeDTO implements Serializable{
	

	private RuleBookDTO ruleBook;
	private ResultsTypeDTO resultsType;
	private List<ResultsTypeMeasureDTO> resultsTypeMeasures;
	
	private boolean notLoadedResultsTypeMeasures;
	
	public RuleBookResultsTypeDTO() {
		super();
		notLoadedResultsTypeMeasures = true;
		resultsTypeMeasures = new ArrayList<ResultsTypeMeasureDTO>();
	}
	
	/**
	 * @param ruleBook
	 * @param resultsType
	 */
	public RuleBookResultsTypeDTO(RuleBookDTO ruleBook,
			ResultsTypeDTO resultsType) {
		super();
		this.ruleBook = ruleBook;
		this.resultsType = resultsType;
		notLoadedResultsTypeMeasures = true;
		resultsTypeMeasures = new ArrayList<ResultsTypeMeasureDTO>();
	}



	/**
	 * @param ruleBook
	 * @param resultsType
	 * @param resultsTypeMeasures
	 */
	public RuleBookResultsTypeDTO(RuleBookDTO ruleBook,
			ResultsTypeDTO resultsType,
			List<ResultsTypeMeasureDTO> resultsTypeMeasures) {
		super();
		this.ruleBook = ruleBook;
		this.resultsType = resultsType;
		this.resultsTypeMeasures = resultsTypeMeasures;
		notLoadedResultsTypeMeasures = (resultsTypeMeasures!=null && resultsTypeMeasures.size()>0)?false:true;
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
	 * @return the resultsTypeMeasures
	 */
	public List<ResultsTypeMeasureDTO> getResultsTypeMeasures() {
		if(notLoadedResultsTypeMeasures)
			loadResultsTypeMeasuresFromDatabase();
		return resultsTypeMeasures;
	}

	/**
	 * @param resultsTypeMeasures the resultsTypeMeasures to set
	 */
	public void setresultsTypeMeasures(List<ResultsTypeMeasureDTO> resultsTypeMeasures) {
		this.resultsTypeMeasures = resultsTypeMeasures;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		try {
			RuleBookResultsTypeDTO temp = (RuleBookResultsTypeDTO) arg0;
			if ((this.resultsType != null) && (this.resultsType.equals(temp.resultsType)) && 
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
		return resultsType.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ruleBook + " " +resultsType.toString();
	}

	/**
	 * @return the notLoadedResultsTypeMeasures
	 */
	public boolean isNotLoadedResultsTypeMeasures() {
		return notLoadedResultsTypeMeasures;
	}

	/**
	 * @param notLoadedResultsTypeMeasures the notLoadedResultsTypeMeasures to set
	 */
	public void setNotLoadedResultsTypeMeasures(boolean notLoadedResultsTypeMeasures) {
		this.notLoadedResultsTypeMeasures = notLoadedResultsTypeMeasures;
	}

	public void loadResultsTypeMeasuresFromDatabase(){
//		System.out.println("RuleBookResultsTypeDTO - loadResultsTypeMeasuresFromDatabase -1");
		if(notLoadedResultsTypeMeasures){
//			System.out.println("RuleBookResultsTypeDTO - loadResultsTypeMeasuresFromDatabase -2");
			resultsTypeMeasures.clear();
			ResultsTypeMeasureDAO dao = new ResultsTypeMeasureDAO();
			resultsTypeMeasures.addAll(dao.getAllResultsTypeMeasures(ruleBook, resultsType));
//			System.out.println("RuleBookResultsTypeDTO - loadResultsTypeMeasuresFromDatabase -3");
		}
		notLoadedResultsTypeMeasures = false;
//		System.out.println("RuleBookResultsTypeDTO - loadResultsTypeMeasuresFromDatabase -4");
	}
}
