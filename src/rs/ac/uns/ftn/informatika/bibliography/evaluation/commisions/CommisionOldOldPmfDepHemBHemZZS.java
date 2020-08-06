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
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOHemija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOZastitaIKoriscenjeVodaUSrbiji;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionOldOldPmfDepHemBHemZZS extends AbstractCommissionEvaluation{


	public CommisionOldOldPmfDepHemBHemZZS(){
		super();
		comissionID = 2;
		appointmentBoard = "DH-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 13);
		appointmentDate.set(Calendar.MONTH, Calendar.DECEMBER);
		appointmentDate.set(Calendar.YEAR, 2011);
		members = "Т. Ђаковић-Секулић, М. Сакач, Д. Шојић, С. Јовановић-Шанта и А. Тубић";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		
		//"Hemija"
		//"Zastita i koriscenje voda u Srbiji"
		//"Uređenje, zaštitu i korišćenje voda, zemljišta i vazduha"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOHemija.getMNOHemija());
		mnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());
		mnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos33");				//Agriculture (current - Agronomy)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos15");				//Agriculture, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos17");				//Agronomy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos18");				//Allergy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos29");				//Area studies
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos42");				//Biochemical Research Methods
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos43");				//Biochemistry & Molecular Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos44");				//Biology
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos45");				//Biology, Miscellaneous (current - relevant discipline)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos34");				//Biomethods (current - Biochemical Research Methods)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos54");				//Biophysics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos55");				//Biotechnology & Applied Microbiology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos3");					//Cancer (current - Oncology)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos61");				//Cell Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos36");				//Chemistry (current - Chemistry, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos68");				//Chemistry, Analytical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos64");				//Chemistry, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos37");				//Chemistry, Clinical & Medicinal (current - Chemistry, Medicinal)
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos69");				//Chemistry, Inorganic & Nuclear
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos65");				//Chemistry, Medicinal
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos66");				//Chemistry, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos70");				//Chemistry, Organic
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos71");				//Chemistry, Physical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos186");				//Clinical Neurology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");				//Computer Science, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos84");				//Crystallography
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos4");					//Cytology & Histology
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos87");				//Dermatology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos90");				//Ecology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos92");				//Education & Educational Research
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos93");				//Education, Scientific disciplines
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos94");				//Education, Special
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos98");				//Electrochemistry
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos101");				//Endocrinology & Metabolism
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos102");				//Energy & Fuels
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos40");				//Engineering (current - Engineering, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos104");				//Engineering, Biomedical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos106");				//Engineering, Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos110");				//Engineering, Civil
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos103");				//Engineering, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos112");				//Engineering, Petroleum
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos117");				//Environmental Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos118");				//Environmental Studies
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos123");				//Food Science & Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos125");				//Gastroenterology & Hepatology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos88");				//Geochemistry & Geophysics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos130");				//Geosciences, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos97");				//Health Care Sciences & Services
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos134");				//Hematology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos137");				//History & Philosophy of Science
		researchAreas.add(wos);					//History & Philosophy of Science (AHCi)
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos139");				//Horticulture
		researchAreas.add(wos);					

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos7");					//Hygiene & Public Health (current - Public, Environm. & Occupat. Health)
		researchAreas.add(wos);	
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos143");				//Immunology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos145");				//Infectious Diseases
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos149");				//Instruments & Instrumentation
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos151");				//Integrative & Complementary Medicine
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos152");				//Law
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos158");				//Management
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos175");				//Materials Science, Characterization & Testing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos163");				//Materials Science, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos47");				//Materials Science (current - Materials Science, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos174");				//Materials Science, Biomaterials
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos161");				//Materials Science, Paper & Wood
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos165");				//Mathematics, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos170");				//Medical Laboratory Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos171");				//Medicine, General & Internal
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos154");				//Medicine, Legal
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos8");					//Medicine, Miscellaneous (current - relevant discipline)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos173");				//Medicine, Research & Experimental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos180");				//Microbiology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos181");				//Microscopy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos183");				//Mineralogy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos184");				//Multidisciplinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos185");				//Mycology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos147");				//Nanoscience & Nanotechnology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos187");				//Neurosciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos191");				//Nutrition & Dietetics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos192");				//Obstetrics & Gynecology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos59");				//Oncology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos202");				//Pathology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos204");				//Pharmacology & Pharmacy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos23");				//Physics (current - Physics, ...)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos206");				//Physics, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos209");				//Physics, Atomic, Molecular & Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos211");				//Physics, Condensed Matter
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos216");				//Physics, Mathematical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos210");				//Physics, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos212");				//Physiology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos56");				//Plant Sciences
		researchAreas.add(wos);					//Botany (current - Plant Sciences) 
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos52");				//Radiology & Nuclear Medicine (current - Radiol., Nucl. Med. & Med. Imag.)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos67");				//Reproductive Systems (current - Reproductive Biology)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos231");				//Social Issues
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos233");				//Social Sciences, Interdisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos237");				//Soil Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos238");				//Spectroscopy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos63");				//Thermodynamics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos243");				//Toxicology
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos249");				//Urology & Nephrology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos250");				//Veterinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos252");				//Virology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos256");				//Water Resources
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
					
				} else if (year < 1981 && journal.hasIfInYear(1981) ){
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
					retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100));
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
					retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100));
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
							retVal.put(year, new JournalEvaluationResult("M52", journal, null, 100));
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
		JournalEvaluationResult withinResearchAreas = null;
		JournalEvaluationResult outsideResearchAreas = null;
		ImpactFactor imf = new ImpactFactor();
		imf.setYear(year);
		imf = (journal.getImpactFactors().indexOf(imf)!=-1)?journal.getImpactFactors().get(journal.getImpactFactors().indexOf(imf)):null;
		if (imf != null){
			for (ResearchAreaRanking rar : imf.getResearchAreas()) {
				boolean m21 = false;
				boolean m22 = false;
				boolean m23 = false;
				if(rar.getDividend() <= 0.3)
					m21 = true;
				else if (rar.getDividend() > 0.5)
					m23 = true;
				else
					m22 = true;
				
				if(researchAreas.contains(rar.getResearchAreaDTO())){
					if(((withinResearchAreas==null) || (withinResearchAreas.getEvaluation() > 1)) && (m21))
						withinResearchAreas = new JournalEvaluationResult("M21", journal, imf, 1);
					else if(((withinResearchAreas==null) || (withinResearchAreas.getEvaluation() > 2)) && (m22))
						withinResearchAreas = new JournalEvaluationResult("M22", journal, imf, 2);
					else if ((withinResearchAreas==null)  && (m23))
						withinResearchAreas = new JournalEvaluationResult("M23", journal, imf, 3);
				} else {
					if(((outsideResearchAreas==null) || (outsideResearchAreas.getEvaluation() <3 )) && (m23))
						outsideResearchAreas = new JournalEvaluationResult("M23", journal, imf, 3);
					else if(((outsideResearchAreas==null) || (outsideResearchAreas.getEvaluation() < 2)) && (m22))
						outsideResearchAreas = new JournalEvaluationResult("M22", journal, imf, 2);
					else if ((outsideResearchAreas==null)  && (m21))
						outsideResearchAreas = new JournalEvaluationResult("M21", journal, imf, 1);
				}
			}		
		}
		if(withinResearchAreas != null){
			retVal = withinResearchAreas;
		} else if (outsideResearchAreas != null){
			retVal = outsideResearchAreas;
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
			retVal.put(i, new JournalEvaluationResult("M53", journal, null, 53));
		}
		return retVal;
	}
}