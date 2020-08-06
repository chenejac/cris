package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

public class CommissionPmfDepHemZZS extends CommissionWoSSimple {

	public CommissionPmfDepHemZZS(){
		super();
		comissionID = 712;
		appointmentBoard = "DH-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 13);
		appointmentDate.set(Calendar.MONTH, Calendar.DECEMBER);
		appointmentDate.set(Calendar.YEAR, 2011);
		members = "Т. Ђаковић-Секулић, М. Сакач, Д. Шојић, С. Јовановић-Шанта и А. Тубић";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		mnoList = new ArrayList<MNO>();
		researchAreas = new HashSet<ResearchAreaDTO>();
		
	}
}
