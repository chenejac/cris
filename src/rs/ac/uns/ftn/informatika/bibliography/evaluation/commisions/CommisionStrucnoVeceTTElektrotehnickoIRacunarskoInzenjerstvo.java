package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

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

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo(){
		super();
		comissionID = 105;
		appointmentBoard = "SV-TT-ELRACI";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Elektronika i telekomunikacije"
		//"Industrijski softver i informatika"
		//"Elektrotehnika, telekomunikacije i informacione tehnologije"
		//"Energetske tehnologije i rudarstvo"
		//"Energetska efikasnost"
		//"Energetika, rudarstvo i energetska efikasnost"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
		mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
		mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
		mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
		mnoList.add(MNOEnergetskaEfikasnost.getMNOEnergetskaEfikasnost());
		mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
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
		wos.setClassId("wos113");				//Engineering, Electrical & Electronic
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos148");				//Information Science & Library Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos149");				//Instruments & Instrumentation
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos135");				//Mathematical & Computational Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos168");				//Medical Informatics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos194");				//Remote Sensing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos182");				//Robotics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos242");				//Telecommunications
		researchAreas.add(wos);
	}
}