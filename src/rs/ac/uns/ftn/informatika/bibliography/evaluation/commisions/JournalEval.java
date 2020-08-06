package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class JournalEval {

	private int startingYear;
	private String controlNumber;
	private String someName;
	private String ISSN;
	private List<ImpactFactor> impactFactors;
	
	/**
	 * 
	 */
	public JournalEval() {
		super();
	}

	/**
	 * @param journal
	 * @param papers
	 */
	public JournalEval(String controlNumber, String someName, String ISSN, List<ImpactFactor> impactFactors, int startingYear) {
		super();
		this.controlNumber = controlNumber;
		this.someName = someName;
		this.ISSN = ISSN;
		this.impactFactors = impactFactors;
		this.startingYear = startingYear;
	}
//	/**
//	 * @return the journal
//	 */
//	public JournalDTO getJournal() {
//		return journal;
//	}
//
//	/**
//	 * @param journal the journal to set
//	 */
//	public void setJournal(JournalDTO journal) {
//		//this.journal = journal;
//	}
	
	/**
	 * @return the controlNumber
	 */
	public String getControlNumber() {
		return controlNumber;
	}

	/**
	 * @param controlNumber the controlNumber to set
	 */
	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	/**
	 * @return the someName
	 */
	public String getSomeName() {
		return someName;
	}

	/**
	 * @param someName the someName to set
	 */
	public void setSomeName(String someName) {
		this.someName = someName;
	}

	/**
	 * @return the iSSN
	 */
	public String getISSN() {
		return ISSN;
	}

	/**
	 * @param iSSN the iSSN to set
	 */
	public void setISSN(String iSSN) {
		ISSN = iSSN;
	}

	/**
	 * @return the impactFactors
	 */
	public List<ImpactFactor> getImpactFactors() {
		return impactFactors;
	}
	
	/**
	 * @param impactFactors the impactFactors to set
	 */
	public void setImpactFactors(List<ImpactFactor> impactFactors) {
		this.impactFactors = impactFactors;
	}

	public int getStartingYear() {
		return startingYear;
	}

	public void setStartingYear(int startingYear) {
		this.startingYear = startingYear;
	}

	public boolean hasIfInYear(int godina)
	{
		boolean retVal = false;
		if(impactFactors!=null && (!impactFactors.isEmpty())){
			for (ImpactFactor imf : impactFactors) {
				if(imf.getYear()==godina){
					retVal = true;
					break;
				}
			}
		}
		
		return retVal;
	}
}
