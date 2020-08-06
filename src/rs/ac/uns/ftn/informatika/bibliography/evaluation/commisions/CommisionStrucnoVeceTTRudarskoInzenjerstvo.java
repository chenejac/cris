package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetikaRudarstvoIEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskeTehnologijeIRudarstvo;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTRudarskoInzenjerstvo extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTRudarskoInzenjerstvo(){
		super();
		comissionID = 110;
		appointmentBoard = "SV-TT-RUI";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Energetske tehnologije i rudarstvo"
		//"Energetika, rudarstvo i energetska efikasnost"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
		mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos183");				//Mineralogy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos255");				//Mining & Mineral Processing
		researchAreas.add(wos);
	}
}
