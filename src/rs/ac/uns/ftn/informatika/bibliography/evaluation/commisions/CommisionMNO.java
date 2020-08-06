package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

/**
 * @author Dragan Ivanovic, chenejac@uns.ac.rs
 *
 */
public class CommisionMNO extends AbstractCommissionEvaluation {

	/**
	 * 
	 */
	public CommisionMNO() {
		super();
		// TODO Auto-generated constructor stub
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
	public CommisionMNO(int comissionID, String appointmentBoard,
			Calendar appointmentDate, String members,
			String cfClassShemeIdScienceArea, String cfClassIdScienceArea,
			String scientificField, Set<ResearchAreaDTO> researchAreas,
			List<MNO> mnoList) {
		super(comissionID, appointmentBoard, appointmentDate, members,
				cfClassShemeIdScienceArea, cfClassIdScienceArea, scientificField,
				researchAreas, mnoList);
		// TODO Auto-generated constructor stub
	}



	public HashMap<Integer, JournalEvaluationResult> getJournalEvaluations(JournalEval journal) 
	{
		
		HashMap<Integer, JournalEvaluationResult> retVal = new HashMap<Integer, JournalEvaluationResult>();
		
		int startingYear = journal.getStartingYear();
		if (startingYear == -1) {
			return retVal;
		}
		
//		boolean hasIF = (journal.getImpactFactors()!=null && journal.getImpactFactors().size()>0);
		boolean isSpecial = false;
		for (MNO mno : mnoList) {
			isSpecial = isSpecial || mno.isSpecial(journal, 0);
		}
		if(isSpecial) {
			getJournalEvaluationsNonSCIAndSpecial(retVal, journal, startingYear);
		}
		else {
			getJournalEvaluationsNonSCIAndNonSpecial(retVal, journal, startingYear);
		}
		return retVal;
	}
	
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndSpecial(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear) 
	{
		for (MNO mno : mnoList) {
			HashMap<Integer, JournalEvaluationResult> temp = mno.getJournalEvaluationsWithFirstSpecialYear(journal, startingYear);
			for (Integer key : temp.keySet()) {
				if(!retVal.containsKey(key))
					retVal.put(key, temp.get(key));
				else if (temp.get(key).getEvaluation()<retVal.get(key).getEvaluation()){
					retVal.put(key, temp.get(key));
				}
			}
		}
		
		return retVal;		
	}
	
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndNonSpecial(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear) 
	{
		return new HashMap<Integer, JournalEvaluationResult>();
//		for(int i = startingYear; i <= lastEvaluationYear; i++) {
//			retVal.put(i, "M53");
//		}
//		return retVal;
	}



	@Override
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsSCI(
			HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal,
			int startingYear) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected JournalEvaluationResult getJournalEvaluationM21M22M23(Integer year,
			JournalEval journal) {
		// TODO Auto-generated method stub
		return null;
	}
}
