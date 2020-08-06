package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

/**
 * @author Dragan Ivanovic, chenejac@uns.ac.rs
 *
 */
public class CommisionOldWoSSimple extends AbstractCommissionEvaluation {

	public CommisionOldWoSSimple() {
		super();
		comissionID = 600;
		appointmentBoard = "OldWoSSimple";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_MONTH, 29);
		appointmentDate.set(Calendar.MONTH, Calendar.AUGUST);
		appointmentDate.set(Calendar.YEAR, 2014);
		members = "WoS journals' list selectors";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
//		allMnoList.add(MNOBiologija.getMNOBiologija());
//		allMnoList.add(MNOBiotehnologijaIAgroindustrija.getMNOBiotehnologijaIAgroindustrija());
//		allMnoList.add(MNOBiotehnologijaIPoljoprivreda.getMNOBiotehnologijaIPoljoprivreda());
//		allMnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
//		allMnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
//		allMnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
//		allMnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
//		allMnoList.add(MNOEnergetskaEfikasnost.getMNOEnergetskaEfikasnost());
//		allMnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
//		allMnoList.add(MNOFizika.getMNOFizika());
//		allMnoList.add(MNOGeonaukeIAstronomija.getMNOGeonaukeIAstronomija());
//		allMnoList.add(MNOHemija.getMNOHemija());
//		allMnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
//		allMnoList.add(MNOMasinstvo.getMNOMasinstvo());
//		allMnoList.add(MNOMatematikaIMehanika.getMNOMatematikaIMehanika());
//		allMnoList.add(MNOMatematikaRacunarskeNaukeIMehanika.getMNOMatematikaRacunarskeNaukeIMehanika());
//		allMnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
//		allMnoList.add(MNOMedicina.getMNOMedicina());
//		allMnoList.add(MNOSaobracajUrbanizamIGradevinarstvo.getMNOSaobracajUrbanizamIGradevinarstvo());
//		allMnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha());
//		allMnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());
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
	public CommisionOldWoSSimple(int comissionID, String appointmentBoard,
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
		
		boolean hasIF = (journal.getImpactFactors()!=null && journal.getImpactFactors().size()>0);
		
		
		if(hasIF) {
			getJournalEvaluationsSCI(retVal, journal, startingYear);
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
		
		JournalEvaluationResult best19811983 = null;
		
		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
			JournalEvaluationResult evaluation = getJournalEvaluationM21M22M23(year, journal);
			if(evaluation != null){
				//ima if za godinu
//				String m = null;
//				if(evaluation == 3)
//					m = "M23";
//				else if (evaluation == 2)
//					m = "M22";
//				else if (evaluation == 1)
//					m = "M21";
				retVal.put(year, evaluation);
				
				if ((year >= 1981) && (year <=1983)){
					if ((best19811983 == null) || (evaluation.getEvaluation() < best19811983.getEvaluation()))
						best19811983 = evaluation;		
				}
			}
			else if (year < 1981 && journal.hasIfInYear(1981) ){
//				String m = null;
//				if (best19811983 != null){
//					if(best19811983 == 3)
//						m = "M23";
//					else if (best19811983 == 2)
//						m = "M22";
//					else if (best19811983 == 1)
//						m = "M21";
//					else{
//						System.out.println("Greska u kodu za 1981 mora imati IF");
//					}
//				}
				retVal.put(year, best19811983);
			}
			else {
				
				//nema IF za godinu
				//odrediti preko kategorije -2 godine i +1
				
				JournalEvaluationResult evaluation1 = getJournalEvaluationM21M22M23(year-1, journal);	
				JournalEvaluationResult evaluation2 = getJournalEvaluationM21M22M23(year-2, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation()) ))
					evaluation1 = evaluation2;
				evaluation2 = getJournalEvaluationM21M22M23(year+1, journal);
				if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation()) ))
					evaluation1 = evaluation2;
				evaluation = evaluation1;
				
//				String m = null;
//				if(evaluation == 3)
//					m = "M23";
//				else if (evaluation == 2)
//					m = "M22";
//				else if (evaluation == 1)
//					m = "M21";
				
				retVal.put(year, evaluation);
			}
			
			//drugi prolaz za sve godine od 1989 do 1997
			if(year == 1987){
				//prvi prolaz za sve koji nisu popunjeni -2 +1 ali sa vec odredjenim kategorijama
				for(int j = 1989; j <=1997; j++){
					JournalEvaluationResult evaluated = retVal.get(j);
					if(evaluated == null){
//						Integer evaluation1 = 52;
//						Integer evaluation2 = 52;		
						JournalEvaluationResult evaluation1 = retVal.get(j-1);
//						if (evaluation1Str!=null) {
//							evaluation1 = Integer.parseInt(evaluation1Str.substring(1));
//						}
						JournalEvaluationResult evaluation2 = retVal.get(j-2);
//						if (evaluation2Str!=null) {
//							evaluation2 = Integer.parseInt(evaluation2Str.substring(1));
//						}
						if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
							evaluation1 = evaluation2;
						evaluation2 = retVal.get(j + 1);
						
						if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
							evaluation1 = evaluation2;
						
						evaluation = evaluation1;
						
//						String m = null;
//						if(evaluation == 23)
//							m = "M23";
//						else if (evaluation == 22)
//							m = "M22";
//						else if (evaluation == 21)
//							m = "M21";
						retVal.put(j, evaluation);
					}
				}
				//drugi prolaz za sve koji nisu popunjeni best of prvi najblizi -i +i, koji nije manji od 1989 ni veci od 1997
				for(int j = 1988; j <=1997; j++){
					JournalEvaluationResult evaluated = retVal.get(j);
					if(evaluated == null){
						JournalEvaluationResult best = null;
						int     i = 0;
						while(best==null) {
							i++;
							int dojaGr = j-i;
							if(dojaGr < 1989)
								dojaGr = 1989;
							int gornjaGr = j+i;
							if(gornjaGr > 1997)
								gornjaGr = 1997;
							
							JournalEvaluationResult evaluation1 = getJournalEvaluationM21M22M23(dojaGr, journal);	
							JournalEvaluationResult evaluation2 = getJournalEvaluationM21M22M23(gornjaGr, journal);
							if(evaluation1 == null || ((evaluation2 != null) && (evaluation1.getEvaluation() > evaluation2.getEvaluation())))
								evaluation = evaluation2;
							else
								evaluation = evaluation1;
							if(evaluation!=null)
								best = evaluation;
							
//							if(evaluation != 4){
//								//ima if za godinu
//								if(evaluation == 3)
//									best = "M23";
//								else if (evaluation == 2)
//									best = "M22";
//								else if (evaluation == 1)
//									best = "M21";
//							}
							
							if (dojaGr == 1989 && gornjaGr == 1997) {
								break;
							}
						}
						retVal.put(j, best);
					}
				}
			}
			
		}
		boolean found = false;
        for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
        	JournalEvaluationResult evaluated = retVal.get(year);

            if(evaluated == null){
            	if(found == true)
                    retVal.put(year, new JournalEvaluationResult("M52", journal, null, 52));
                else
                    retVal.put(year, new JournalEvaluationResult("M100", journal, null, 100));
            }

            if(evaluated!=null)
                found = true;
        } 
		return retVal;
	}
	
	protected JournalEvaluationResult getJournalEvaluationM21M22M23(Integer year, JournalEval journal) 
	{
		//M21 vrhunski medjunarodni casopis u prvih 30%							//vrednost 1 
		//M22 istaknuti medjunarodni casopis od 30% < pozicija <= 50%			//vrednost 2 
		//M23 medjunarodni casopis koji nije vrstan u prvih 50%					//vrednost 3 
		
		
		JournalEvaluationResult retVal = null;
		JournalEvaluationResult withinResearchAreas = null;
		ImpactFactor imf = new ImpactFactor();
		imf.setYear(year);
		imf = (journal.getImpactFactors().indexOf(imf)!=-1)?journal.getImpactFactors().get(journal.getImpactFactors().indexOf(imf)):null;
		if (imf != null){
				ResearchAreaRanking rar = imf.getMaxPositionReseachArea();
				boolean m21a = false;
				boolean m21 = false;
				boolean m22 = false;
				boolean m23 = false;
				if(rar.getDividend() <= 0.1)
					m21a = true;
				else if (rar.getDividend() <= 0.3)
					m21 = true;
				else if (rar.getDividend() > 0.6)
					m23 = true;
				else
					m22 = true;
				
				if(((withinResearchAreas==null) || (withinResearchAreas.getEvaluation() > 1)) && (m21a))
					withinResearchAreas = new JournalEvaluationResult("M21a", journal, imf, 1);
				else if(((withinResearchAreas==null) || (withinResearchAreas.getEvaluation() > 2)) && (m21))
					withinResearchAreas = new JournalEvaluationResult("M21", journal, imf, 2);
				else if(((withinResearchAreas==null) || (withinResearchAreas.getEvaluation() > 3)) && (m22))
					withinResearchAreas = new JournalEvaluationResult("M22", journal, imf, 3);
				else if ((withinResearchAreas==null)  && (m23))
					withinResearchAreas = new JournalEvaluationResult("M23", journal, imf, 4);				
		}
		if(withinResearchAreas != null){
			retVal = withinResearchAreas;
		}
		return retVal;
	}
	
	
	
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndNonSpecial(HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal, int startingYear) 
	{
		for(int i = startingYear; i <= lastEvaluationYear; i++) {
			retVal.put(i, new JournalEvaluationResult("M53", journal, null, 5));
		}
		return retVal;
	}


	@Override
	protected HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsNonSCIAndSpecial(
			HashMap<Integer, JournalEvaluationResult> retVal, JournalEval journal,
			int startingYear) {
		// TODO Auto-generated method stub
		return null;
	}
}
