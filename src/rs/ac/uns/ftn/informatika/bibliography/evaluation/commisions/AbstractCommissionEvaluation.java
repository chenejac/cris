package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public abstract class AbstractCommissionEvaluation {

	protected Set<ResearchAreaDTO> researchAreas = null;
	protected List<MNO> mnoList;
	
	protected int comissionID;
	protected String appointmentBoard;
	protected Calendar appointmentDate;
	protected String members;
	protected String cfClassShemeIdScienceArea;
	protected String cfClassIdScienceArea;
	protected String scientificField = null;
	
	int firstEvaluationYear = -1;
	int lastEvaluationYear = -1;
	
	public AbstractCommissionEvaluation() {
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		lastEvaluationYear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
	}
	
	
	
	
	/**
	 * @return the firstEvaluationYear
	 */
	public int getFirstEvaluationYear() {
		return firstEvaluationYear;
	}





	/**
	 * @param firstEvaluationYear the firstEvaluationYear to set
	 */
	public void setFirstEvaluationYear(int firstEvaluationYear) {
		this.firstEvaluationYear = firstEvaluationYear;
	}





	/**
	 * @return the lastEvaluationYear
	 */
	public int getLastEvaluationYear() {
		return lastEvaluationYear;
	}





	/**
	 * @param lastEvaluationYear the lastEvaluationYear to set
	 */
	public void setLastEvaluationYear(int lastEvaluationYear) {
		this.lastEvaluationYear = lastEvaluationYear;
	}





	/**
	 * @param comissionID
	 * @param appointmentBoard
	 * @param appointmentDate
	 * @param members
	 * @param cfClassShemeIdScienceArea
	 * @param cfClassIdScienceArea
	 * @param scientificField
	 * @param researchAreas
	 * @param mnoList
	 */
	public AbstractCommissionEvaluation(int comissionID,
			String appointmentBoard, Calendar appointmentDate, String members,
			String cfClassShemeIdScienceArea, String cfClassIdScienceArea,
			String scientificField, Set<ResearchAreaDTO> researchAreas,
			List<MNO> mnoList) {
		super();
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		lastEvaluationYear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
		this.comissionID = comissionID;
		this.appointmentBoard = appointmentBoard;
		this.appointmentDate = appointmentDate;
		this.members = members;
		this.cfClassShemeIdScienceArea = cfClassShemeIdScienceArea;
		this.cfClassIdScienceArea = cfClassIdScienceArea;
		this.scientificField = scientificField;
		this.researchAreas = researchAreas;
		this.mnoList = mnoList;
	}



	public int getComissionID() {
		return comissionID;
	}
	public void setComissionID(int comissionID) {
		this.comissionID = comissionID;
	}
	public String getAppointmentBoard() {
		return appointmentBoard;
	}
	public void setAppointmentBoard(String appointmentBoard) {
		this.appointmentBoard = appointmentBoard;
	}
	public Calendar getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Calendar appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getCfClassShemeIdScienceArea() {
		return cfClassShemeIdScienceArea;
	}
	public void setCfClassShemeIdScienceArea(String cfClassShemeIdScienceArea) {
		this.cfClassShemeIdScienceArea = cfClassShemeIdScienceArea;
	}
	public String getCfClassIdScienceArea() {
		return cfClassIdScienceArea;
	}
	public void setCfClassIdScienceArea(String cfClassIdScienceArea) {
		this.cfClassIdScienceArea = cfClassIdScienceArea;
	}
	public Set<ResearchAreaDTO> getResearchAreas() {
		return researchAreas;
	}
	public void setResearchAreas(Set<ResearchAreaDTO> researchAreas) {
		this.researchAreas = researchAreas;
	}
	public String getScientificField() {
		return scientificField;
	}
	public void setScientificField(String scientificField) {
		this.scientificField = scientificField;
	}
	public List<MNO> getMnoList() {
		return mnoList;
	}
	public void setMnoList(List<MNO> mnoList) {
		this.mnoList = mnoList;
	}
	
	public static String[] processISSN(String ISSN)
	{
		String[] tokens = ISSN.split(";");
		String[] ISSNs = new String [tokens.length];
		
		if(tokens.length>2){
			for (int i = 2; i < tokens.length; i++) {
				if (tokens[i].endsWith("(oldISSN)")) {
					 ISSNs[i] = tokens[i].substring(0, tokens[i].lastIndexOf("("));
				}
				else {
					System.out.println("Ne valja issn" + ISSN);
					System.exit(0);
				}
			}
		}
		if(tokens.length>1) {
			if (tokens[0].endsWith("(pISSN)")) {
				ISSNs[0] = tokens[0].substring(0, tokens[0].lastIndexOf("("));
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[0] = tokens[0];
			}
			else {
				System.out.println("Ne valja issn" + ISSN);
				System.exit(0);
			}
			if (tokens[1].endsWith("(eISSN)")) {
				ISSNs[1] = tokens[1].substring(0, tokens[1].lastIndexOf("("));
			}
			else if (tokens[1].endsWith("(oldISSN)")) {
				ISSNs[1] = tokens[1].substring(0, tokens[1].lastIndexOf("("));
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[1] = tokens[1];
			}
			else {
				System.out.println("Ne valja issn" + ISSN);
				System.exit(0);
			}
		}
		if (tokens.length==1) {
			ISSNs[0] = tokens[0];
		}
		
		return ISSNs;
	}

	public abstract HashMap<Integer, JournalEvaluationResult> getJournalEvaluations(JournalEval journal);
	protected abstract HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsSCI(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear);
	protected abstract JournalEvaluationResult getJournalEvaluationM21M22M23(Integer year, JournalEval journal);
	protected abstract HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndSpecial(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear);
	protected abstract HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndNonSpecial(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear);
}
