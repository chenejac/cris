package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetikaRudarstvoIEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskeTehnologijeIRudarstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMasinstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMaterijaliIHemijskeTehnologije;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTMetalurskoInzenjerstvo extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTMetalurskoInzenjerstvo(){
		super();
		comissionID = 113;
		appointmentBoard = "SV-TT-MEI";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Mašinstvo"
		//"Materijali i hemijske tehnologije"
		//"Energetske tehnologije i rudarstvo"
		//"Energetika, rudarstvo i energetska efikasnost"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOMasinstvo.getMNOMasinstvo());
		mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
		mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
		mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos172");				//Metallurgy & Metallurgical Engineering
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos48");				//Metallurgy & Mining (current - relevant discipline)
		researchAreas.add(wos);
	}
}

