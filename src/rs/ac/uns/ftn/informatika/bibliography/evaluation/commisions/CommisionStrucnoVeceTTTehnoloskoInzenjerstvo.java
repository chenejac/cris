package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOHemija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMaterijaliIHemijskeTehnologije;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTTehnoloskoInzenjerstvo  extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTTehnoloskoInzenjerstvo(){
		super();
		comissionID = 112;
		appointmentBoard = "SV-TT-THI";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Materijali i hemijske tehnologije"
		//"Hemija"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
		mnoList.add(MNOHemija.getMNOHemija());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos106");				//Engineering, Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos112");				//Engineering, Petroleum
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos102");				//Energy & Fuels
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
	}
}

