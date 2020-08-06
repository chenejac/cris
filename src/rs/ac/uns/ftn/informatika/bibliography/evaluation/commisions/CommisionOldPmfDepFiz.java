package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOFizika;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionOldPmfDepFiz extends CommisionStrucnoVece {

		public CommisionOldPmfDepFiz(){
			super();
			comissionID = 14;
			appointmentBoard = "DF-PMF";
			appointmentDate = new GregorianCalendar();
			appointmentDate.set(Calendar.DAY_OF_YEAR, 28);
			appointmentDate.set(Calendar.MONTH, Calendar.FEBRUARY);
			appointmentDate.set(Calendar.YEAR, 2012);
			members = "Željka Cvejić, Milan Pantić, Dušan Mrđa";
			cfClassShemeIdScienceArea = "sciencesGroup";
			cfClassIdScienceArea = "naturalSciences";
			
			//"Fizika"
			mnoList = new ArrayList<MNO>();
			mnoList.add(MNOFizika.getMNOFizika());
			
			//setovanje naucnih oblasti
			researchAreas = new HashSet<ResearchAreaDTO>();
			
			ResearchAreaDTO wos = null;
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos147");				//Nanoscience & Nanotechnology
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos196");				//Optics
			researchAreas.add(wos);
			
			wos.setSchemeId("researchArea");
			wos.setClassId("wos23");				//Physics (current - Physics, ...)
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos206");				//Physics, Applied
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos209");				//Physics, Atomic, Molecular & Chemical
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos211");				//Physics, Condensed Matter
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos208");				//Physics, Fluids & Plasmas
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos21");				//Physics, General (current - Physics, ...)
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos216");				//Physics, Mathematical
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos50");				//Physics, Miscellaneous (current - Physics, ...)
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos210");				//Physics, Multidisciplinary
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos213");				//Physics, Nuclear
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos214");				//Physics, Particles & Fields
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos63");				//Thermodynamics
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos92");				//Education & Educational Research
			researchAreas.add(wos);

			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos94");				//Education, Special
			researchAreas.add(wos);

			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos84");				//Crystallography
			researchAreas.add(wos);
			
			wos = new ResearchAreaDTO();
			wos.setSchemeId("researchArea");
			wos.setClassId("wos179");				//Meteorology & Atmospheric Sciences
			researchAreas.add(wos);
		}
}
