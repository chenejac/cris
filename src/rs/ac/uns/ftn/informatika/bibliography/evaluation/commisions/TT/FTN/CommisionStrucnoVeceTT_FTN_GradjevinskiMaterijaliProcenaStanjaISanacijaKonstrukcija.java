package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMaterijaliIHemijskeTehnologije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOSaobracajUrbanizamIGradevinarstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommisionStrucnoVece;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija(){
		super();
		comissionID = 164;
		appointmentBoard = "SV-TT-FTN-GMPSiSK";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2013);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
//		Saobraćaj, urbanizam i građevinarstvo
//		Materijali i hemijske tehnologije
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOSaobracajUrbanizamIGradevinarstvo.getMNOSaobracajUrbanizamIGradevinarstvo());
		mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos =null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos81");				//Construction & Building Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos92");				//Education & Educational Research
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos40");				//Engineering (current - Engineering, …)
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
		wos.setClassId("wos113");				//Engineering, Electrical & Electronic
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos47");				//Materials Science (current - Materials Science, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos162");				//Materials Science, Ceramics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos175");				//Materials Science, Characterization & Testing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos177");				//Materials Science, Composites
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos163");				//Materials Science, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
	}
}

