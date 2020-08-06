package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOHemija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOZastitaIKoriscenjeVodaUSrbiji;

public class CommisionOldPmfDepHemBHemZZS extends CommisionStrucnoVece {
	
	public CommisionOldPmfDepHemBHemZZS(){
		super();
		comissionID = 12;
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
		ResearchAreaDTO wos = null;

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
		wos.setClassId("wos34");				//Biomethods (current - Biochemical Research Methods)
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
		wos.setClassId("wos22");				//Chemistry, Miscellaneous (current - Chemistry, …)
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
		wos.setClassId("wos92");				//Education & Educational Research
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos93");				//Education, Scientific disciplines
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos98");				//Electrochemistry
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos106");				//Engineering, Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
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
		wos.setClassId("wos137");				//History & Philosophy of Science
		researchAreas.add(wos);					//History & Philosophy of Science (AHCi)
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos47");				//Materials Science (current - Materials Science, …)
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
		wos.setClassId("wos147");				//Nanoscience & Nanotechnology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos63");				//Thermodynamics
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos256");				//Water Resources
		researchAreas.add(wos);
	}
}
