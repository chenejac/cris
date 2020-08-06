package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNODrustveneNauke;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOGeonaukeIAstronomija;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVecePMGeografija extends CommisionStrucnoVece {
	public CommisionStrucnoVecePMGeografija(){
		super();
		comissionID = 56;
		appointmentBoard = "SV-PM-GEO";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		//"Geonauke i astronomija"
		//"Društvene nauke"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOGeonaukeIAstronomija.getMNOGeonaukeIAstronomija());
		mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos127");				//Geography
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos128");				//Geography, Physical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos129");				//Geology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos46");				//Geosciences (current - Geosciences, Interdisciplinary)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos130");				//Geosciences, Multidisciplinary
		researchAreas.add(wos);
	}
}
