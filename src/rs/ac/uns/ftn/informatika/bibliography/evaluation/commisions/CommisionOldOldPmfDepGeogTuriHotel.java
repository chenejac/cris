package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNODrustveneNauke;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOGeonaukeIAstronomija;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionOldOldPmfDepGeogTuriHotel extends AbstractCommissionEvaluation{


	public CommisionOldOldPmfDepGeogTuriHotel(){
		super();
		comissionID = 3;
		appointmentBoard = "DGTH-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 28);
		appointmentDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Слободан Б. Марковић, Лазар Лазић, Анђелија Ивков-Џигурски";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		//"Geonauke i astronomija"
		//"Društvene nauke"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOGeonaukeIAstronomija.getMNOGeonaukeIAstronomija());
		mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos");				//Sve discipline
		researchAreas.add(wos);
		
	}
	
	public HashMap<Integer, JournalEvaluationResult> getJournalEvaluations(JournalEval journal) 
	{
		
		HashMap<Integer, JournalEvaluationResult> retVal = new HashMap<Integer, JournalEvaluationResult>();
		
		int startingYear = journal.getStartingYear();
		if (startingYear == -1) {
			return retVal;
		}
		
		boolean hasIF = (journal.getImpactFactors()!=null && journal.getImpactFactors().size()>0);
		boolean isSpecial = false;
		for (MNO mno : mnoList) {
			isSpecial = isSpecial || mno.isSpecial(journal, 0);
		}
		
		if(hasIF && isSpecial) {
			HashMap<Integer, JournalEvaluationResult> sciRetVal = getJournalEvaluationsSCI(new HashMap<Integer, JournalEvaluationResult>(), journal, startingYear);
			HashMap<Integer, JournalEvaluationResult> specialRetVal = getJournalEvaluationsNonSCIAndSpecial(new HashMap<Integer, JournalEvaluationResult>(), journal, startingYear);
			for(int i = startingYear; i <= lastEvaluationYear; i++) {
				int sci = sciRetVal.get(i).getEvaluation();
				int special = specialRetVal.get(i).getEvaluation();
				if(sci<=special)
					retVal.put(i, sciRetVal.get(i));
				else
					retVal.put(i, specialRetVal.get(i));
			}
		}
		else if(hasIF) {
			getJournalEvaluationsSCI(retVal, journal, startingYear);
		}
		else if(isSpecial) {
			getJournalEvaluationsNonSCIAndSpecial(retVal, journal, startingYear);
		}
		else {
			getJournalEvaluationsNonSCIAndNonSpecial(retVal, journal, startingYear);
		}
		return retVal;
	}
	
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsSCI(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear) 
	{

		List<Integer> years = new ArrayList<Integer>();
		for(int i = startingYear; i <= lastEvaluationYear; i++)
			years.add(i);
		
		JournalEvaluationResult best19891997 = null;
		JournalEvaluationResult best19811985 = null;
		
		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
			JournalEvaluationResult evaluation = getJournalEvaluationM21M22M23(year, journal);
			if(evaluation != null && evaluation.getEvaluation() != 4){
					//ima if za godinu
//					String m = null;
//					if(evaluation.getEvaluation() == 4)
//						m = "M23";
//					else if (evaluation.getEvaluation() == 3)
//						m = "M22";
//					else if (evaluation.getEvaluation() == 2)
//						m = "M21";
					retVal.put(year, evaluation);
					
				} else if (year < 1981 && journal.hasIfInYear(1981, true, true) ){
//				String m = "M52";
//				if (best19811985 != null){
//					if(best19811985 == 3)
//						m = "M23";
//					else if (best19811985 == 2)
//						m = "M22";
//					else if (best19811985 == 1)
//						m = "M21";
//					else{
//						System.out.println("Greska u kodu za 1981 mora imati IF");
//					}
//				}
				if(best19811985 != null)
					retVal.put(year, best19811985);
				else 
					retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100, true, true));
			}
			else {
				
				//nema IF za godinu
				//odrediti preko kategorije -4 godine i +2
				
				JournalEvaluationResult evaluation1 = getJournalEvaluationM21M22M23(year-1, journal);	
				JournalEvaluationResult evaluation2 = getJournalEvaluationM21M22M23(year-2, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluationM21M22M23(year-3, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluationM21M22M23(year-4, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluationM21M22M23(year+1, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluationM21M22M23(year+2, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
					evaluation1 = evaluation2;
				evaluation = evaluation1;
				
//				String m = null;
//				if ((year < 1989) || (year >1997))
//					m = "M52";
//				
//				if(evaluation == 3)
//					m = "M23";
//				else if (evaluation == 2)
//					m = "M22";
//				else if (evaluation == 1)
//					m = "M21";
				if (((year < 1989) || (year >1997)) && (evaluation == null))
					retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100, true, true));
				else
					retVal.put(year, evaluation);
			}
			
			if(evaluation!= null){
				if ((year >= 1981) && (year <=1985)){
					if ((best19811985 == null) || (evaluation.getEvaluation() < best19811985.getEvaluation()))
						best19811985 = evaluation;		
				}
				if ((year >= 1989) && (year <=1997)){
					if ((best19891997 == null) || (evaluation.getEvaluation() < best19891997.getEvaluation()))
						best19891997 = evaluation;
				}
			}
			
			//drugi prolaz za sve godine od 1989 do 1997
			if(year == 1988){
				for(int j = 1989; j <=1997; j++){
					JournalEvaluationResult evaluated = retVal.get(j);
					if(evaluated == null){
//						String m = "M52";
//						if(best19891997!=null && best19891997!=4){
//							if(best19891997 == 3)
//								m = "M23";
//							else if (best19891997 == 2)
//								m = "M22";
//							else if (best19891997 == 1)
//								m = "M21";
//							else
//								System.out.println("Greska u kodu za 1989");
//						}
						if(best19891997 == null)
							retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100, true, true));
						else 
							retVal.put(year, best19891997);
					}
					}
			}
			
		}
		return retVal;
	}
	
	protected JournalEvaluationResult getJournalEvaluationM21M22M23(Integer year, JournalEval journal) 
	{
		//M21 vrhunski medjunarodni casopis u prvih 30%							//vrednost 1 
		//M22 istaknuti medjunarodni casopis od 30% < pozicija <= 50%			//vrednost 2 
		//M23 medjunarodni casopis koji nije vrstan u prvih 50%					//vrednost 3 
		
		
		JournalEvaluationResult retVal = null;
		JournalEvaluationResult best = null;
//		Integer outsideResearchAreas = null;
		ImpactFactor imf = new ImpactFactor();
		imf.setYear(year);
		imf = (journal.getImpactFactors().indexOf(imf)!=-1)?journal.getImpactFactors().get(journal.getImpactFactors().indexOf(imf)):null;
		if (imf != null){
				ResearchAreaRanking rar = imf.getMaxPositionReseachArea(true, true);
				boolean m21 = false;
				boolean m22 = false;
				boolean m23 = false;
				if(rar.getDividend() <= 0.3)
					m21 = true;
				else if (rar.getDividend() > 0.5)
					m23 = true;
				else
					m22 = true;
				
				if(((best==null) || (best.getEvaluation() > 1)) && (m21))
					best = new JournalEvaluationResult("M21", journal, imf, 1, true, true);
				else if(((best==null) || (best.getEvaluation() > 2)) && (m22))
					best = new JournalEvaluationResult("M22", journal, imf, 2, true, true);
				else if ((best==null)  && (m23))
					best = new JournalEvaluationResult("M23", journal, imf, 3, true, true);
		}
		if(best != null){
			retVal = best;
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
		for(int i = startingYear; i <= lastEvaluationYear; i++) {
			retVal.put(i, new JournalEvaluationResult("M53", journal, null, 53, true, true));
		}
		return retVal;
	}
}