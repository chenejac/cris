package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOZastitaIKoriscenjeVodaUSrbiji;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine extends CommisionStrucnoVece {

	public CommisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine(){
		super();
		comissionID = 107;
		appointmentBoard = "SV-TT-IZZS";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 01);
		appointmentDate.set(Calendar.MONTH, Calendar.JANUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Nedefinisano";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Zastita i koriscenje voda u Srbiji"
		//"Uređenje, zaštitu i korišćenje voda, zemljišta i vazduha"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());
		mnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha());
		
		
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos90");				//Ecology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos117");				//Environmental Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos118");				//Environmental Studies
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos256");				//Water Resources
		researchAreas.add(wos);
	}
}
