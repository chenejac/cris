/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ProceedingsAndPapers;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ConferenceAndProceedings{
	
	List<ProceedingsAndPapers> proceedings;
	ConferenceDTO conference;
	String classification = "";
	String previousClassification = "";
	
	/**
	 * 
	 */
	public ConferenceAndProceedings() {
		super();
	}

	/**
	 * @param conference
	 * @param proceedings
	 */
	public ConferenceAndProceedings(ConferenceDTO conference, List<ProceedingsAndPapers> proceedings, String classification) {
		super();
		this.conference = conference;
		this.proceedings = proceedings;
		this.classification = classification;
		this.previousClassification = this.classification;
	}

	/**
	 * @return the proceedings
	 */
	public List<ProceedingsAndPapers> getProceedings() {
		return proceedings;
	}

	/**
	 * @param proceedings the proceedings to set
	 */
	public void setProceedings(List<ProceedingsAndPapers> proceedings) {
		this.proceedings = proceedings;
	}

	/**
	 * @return the conference
	 */
	public ConferenceDTO getConference() {
		return conference;
	}

	/**
	 * @param conference the conference to set
	 */
	public void setConference(ConferenceDTO conference) {
		this.conference = conference;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the previousClassification
	 */
	public String getPreviousClassification() {
		return previousClassification;
	}

	/**
	 * @param previousClassification the previousClassification to set
	 */
	public void setPreviousClassification(String previousClassification) {
		this.previousClassification = previousClassification;
	}
	
	public boolean changedClassification(){
		boolean retVal = true;
		if((classification != null) && (previousClassification != null) && (classification.equals(previousClassification)))
			retVal = false;
		return retVal;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer(conference.getControlNumber() + "," + Sanitizer.sanitizeCSV(conference.getSomeName()) + "," + ((conference.getYear()!=null)?Sanitizer.sanitizeCSV(conference.getYear().toString()):"") + "," + ((conference.getPlace()!=null)?Sanitizer.sanitizeCSV(conference.getPlace()):"") + ",");
		if(classification.equals("internationalConference"))
			retVal.append("Medjunarodni,,");
		else if(classification.equals("nationalConference")){
			retVal.append("Nacionalni,,");
		} else {
			Calendar publicationDate = new GregorianCalendar();
			Date publicationTime = new Date();
			publicationDate.setTime(publicationTime); 
			publicationDate.set(GregorianCalendar.YEAR, conference.getYear());
			boolean classified = false;
			for (Classification classification : conference.getRecord().getRecordClasses()) {
				if(
						((!"0".equals(classification.getCommissionId().toString())))
				&& (classification.getCfClassSchemeId().equals("type")) 
				&&  (! (publicationDate.before(classification.getCfStartDate()))) 
				&& (!(publicationDate.after(classification.getCfEndDate())))){
					if(classification.getCfClassId().equals("internationalConference")){
						retVal.append(",Medjunarodni,");
						classified = true;
						break;
					}
					else if(classification.getCfClassId().equals("nationalConference")){
						retVal.append(",Nacionalni,");
						classified = true;
						break;
					}
				}
			}
			if(classified == false)
				retVal.append(",,");
		}
		for (int i=0; i < proceedings.size(); i++){
			ProceedingsAndPapers proceedingsAndPapers = proceedings.get(i);
			for (PaperProceedingsDTO paper : proceedingsAndPapers.getPapers()) {
				retVal.append(Sanitizer.sanitizeCSV(paper.toString()));
				retVal.append(",");
			}
		}
		return retVal.toString();
	}

}
