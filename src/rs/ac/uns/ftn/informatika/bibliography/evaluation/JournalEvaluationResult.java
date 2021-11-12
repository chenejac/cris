/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class JournalEvaluationResult {

	private String category;
	private JournalEval journalEval;
	private ImpactFactor impactFactor;
	private int evaluation;
	private int commId;
	private int ruleNumber = 0;
	private String ruleDescr = "";
	private boolean twoYears = true;
	private boolean fiveYears = true;
	
	/**
	 * @param category
	 * @param journalEval
	 * @param impactFactor
	 * @param evaluation
	 */
	public JournalEvaluationResult(String category, JournalEval journalEval,
			ImpactFactor impactFactor, int evaluation, boolean twoYears, boolean fiveYears) {
		super();
		this.category = category;
		this.journalEval = journalEval;
		this.impactFactor = impactFactor;
		this.evaluation = evaluation;
		this.twoYears = twoYears;
		this.fiveYears = fiveYears;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the journalEval
	 */
	public JournalEval getJournalEval() {
		return journalEval;
	}

	/**
	 * @param journalEval the journalEval to set
	 */
	public void setJournalEval(JournalEval journalEval) {
		this.journalEval = journalEval;
	}

	/**
	 * @return the impactFactor
	 */
	public ImpactFactor getImpactFactor() {
		return impactFactor;
	}

	/**
	 * @param impactFactor the impactFactor to set
	 */
	public void setImpactFactor(ImpactFactor impactFactor) {
		this.impactFactor = impactFactor;
	}

	/**
	 * @return the evaluation
	 */
	public int getEvaluation() {
		return evaluation;
	}

	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}
	
	/**
	 * @return the commId
	 */
	public int getCommId() {
		return commId;
	}

	/**
	 * @param commId the commId to set
	 */
	public void setCommId(int commId) {
		this.commId = commId;
	}

	/**
	 * @return the ruleNumber
	 */
	public int getRuleNumber() {
		return ruleNumber;
	}

	/**
	 * @param ruleNumber the ruleNumber to set
	 */
	public void setRuleNumber(int ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	/**
	 * @return the ruleDescr
	 */
	public String getRuleDescr() {
		return ruleDescr;
	}

	/**
	 * @param ruleDescr the ruleDescr to set
	 */
	public void setRuleDescr(String ruleDescr) {
		this.ruleDescr = ruleDescr;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return category + ((impactFactor == null)?("; "):("; "  + impactFactor.getYear() + "; " + impactFactor.getMaxPositionReseachArea(twoYears, fiveYears).getResearchAreaDTO().getSomeTerm() + "; " + impactFactor.getMaxPositionReseachArea(true, true).getDividend()));
	}
	
	
	
	
		
	
}
