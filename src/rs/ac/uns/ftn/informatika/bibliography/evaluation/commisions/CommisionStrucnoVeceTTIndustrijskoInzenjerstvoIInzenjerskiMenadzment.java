package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNODrustveneNauke;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMasinstvo;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment(){
		super();
		comissionID = 106;
		appointmentBoard = "SV-TT-IIIM";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Mašinstvo"
		//"Društvene nauke"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOMasinstvo.getMNOMasinstvo());
		mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos107");				//Engineering, Industrial
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos144");				//Industrial Relations & Labor
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos158");				//Management
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos159");				//Operations Research & Management Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos215");				//Planning & Development
		researchAreas.add(wos);
	}
}