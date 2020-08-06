package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMatematikaIMehanika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMatematikaRacunarskeNaukeIMehanika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommisionStrucnoVece;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn(){
		super();
		comissionID = 174;
		appointmentBoard = "SV-TT-FTN-GIiD";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2013);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
//		Matematika i mehanika	
//		Matematika, računarske nauke i mehanika
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOMatematikaIMehanika.getMNOMatematikaIMehanika());
		mnoList.add(MNOMatematikaRacunarskeNaukeIMehanika.getMNOMatematikaRacunarskeNaukeIMehanika());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos =null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos259");				//Art (AHCi)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos77");				//Communication
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");				//Computer Science, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos93");				//Education, Scientific Disciplines
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos107");				//Engineering, Industrial
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos108");				//Engineering, Manufacturing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos103");				//Engineering, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos263");				//Film, Radio, Television (AHCi)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos207");				//Imaging Science & Photographic Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos148");				//Information Science & Library Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos269");				//Literary Reviews (AHCi)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos176");				//Materials Science, Coatings & Films
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos163");				//Materials Science, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos161");				//Materials Science, Paper & Wood
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos178");				//Materials Science, Textiles
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos181");				//Microscopy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos184");				//Multidisciplinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos147");				//Nanoscience & Nanotechnology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos196");				//Optics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos49");				//Photographic Technology (current - Imaging Science & Photographic Technol.)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos182");				//Robotics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos238");				//Spectroscopy
		researchAreas.add(wos);
	}
}

