package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOElektronikaITelekomunikacije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOElektronikaTelekomunikacijeIInformacioneTehnologije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetikaRudarstvoIEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskeTehnologijeIRudarstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOIndustrijskiSoftverIInformatika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMasinstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommisionStrucnoVece;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima(){
		super();
		comissionID = 150;
		appointmentBoard = "SV-TT-FTN-AiUS";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2013);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
//		Elektronika i telekomunikacije
//		Industrijski softver i informatika
//		Elektronika, telekomunikacije i informacione tehnologije
//		Mašinstvo
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
		mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
		mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
		mnoList.add(MNOMasinstvo.getMNOMasinstvo());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos11");				//Automation & Control Systems
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos38");				//Computer Applications & Cybernetics (current - Computer Science, ...)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos72");				//Computer Science, Artificial Intelligence
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");				//Computer Science, Interdisciplinary Applications
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
		wos.setClassId("wos113");				//Engineering, Electrical & Electronic
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos103");				//Engineering, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos149");				//Instruments & Instrumentation
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos174");				//Materials Science, Biomaterials
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos164");				//Mathematics, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos165");				//Mathematics, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos169");				//Mechanics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos168");				//Medical Informatics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos173");				//Medicine, Research & Experimental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos184");				//Multidisciplinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos187");				//Neurosciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos159");				//Operations Research & Management Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos227");				//Rehabilitation
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos194");				//Remote Sensing
		researchAreas.add(wos);
	}
}
