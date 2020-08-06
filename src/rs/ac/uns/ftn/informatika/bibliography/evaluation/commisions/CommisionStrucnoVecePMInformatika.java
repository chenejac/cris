package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOElektronikaTelekomunikacijeIInformacioneTehnologije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOIndustrijskiSoftverIInformatika;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVecePMInformatika extends CommisionStrucnoVece {

	public CommisionStrucnoVecePMInformatika(){
		super();
		comissionID = 51;
		appointmentBoard = "SV-PM-INF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		//"Industrijski softver i informatika"
		//"Elektrotehnika, telekomunikacije i informacione tehnologije"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
		mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos72");				//Computer Science, Artificial Intelligence
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos74");				//Computer Science, Cybernetics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos75");				//Computer Science, Hardware & Architecture
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos76");				//Computer Science, Information Systems
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");				//Computer Science, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos79");				//Computer Science, Software Engineering
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos80");				//Computer Science, Theory & Methods
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos148");				//Information Science & Library Science
		researchAreas.add(wos);
	}
}
