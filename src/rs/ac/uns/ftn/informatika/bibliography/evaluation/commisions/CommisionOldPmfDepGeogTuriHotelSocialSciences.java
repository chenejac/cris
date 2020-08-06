package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNODrustveneNauke;

public class CommisionOldPmfDepGeogTuriHotelSocialSciences extends CommisionStrucnoVece {

	public CommisionOldPmfDepGeogTuriHotelSocialSciences() {
		comissionID = 23;
		appointmentBoard = "DGTH-SOC-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 28);
		appointmentDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Слободан Б. Марковић, Лазар Лазић, Анђелија Ивков-Џигурски";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "socialSciences";
		
		//"Društvene nauke"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos29");				//Area Studies
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos85");				//Demography
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos92");				//Education & Educational Research
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos93");				//Education, Scientific Disciplines
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos94");				//Education, Special
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos120");				//Ethnic Studies
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos121");				//Family Studies
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos138");				//History of Social Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos140");				//Hospitality, Leisure, Sport & Tourism
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos158");				//Management
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos184");				//Multidisciplinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos215");				//Planning & Development
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos194");				//Remote Sensing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos233");				//Social Sciences, Interdisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos248");				//Urban Studies
		researchAreas.add(wos);
	}
}
