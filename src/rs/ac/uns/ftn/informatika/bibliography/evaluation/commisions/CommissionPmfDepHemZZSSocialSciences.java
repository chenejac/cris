package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

public class CommissionPmfDepHemZZSSocialSciences extends CommissionWoSSimple {

	public CommissionPmfDepHemZZSSocialSciences(){
		super();
		comissionID = 722;
		appointmentBoard = "DH-SOC-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 13);
		appointmentDate.set(Calendar.MONTH, Calendar.DECEMBER);
		appointmentDate.set(Calendar.YEAR, 2011);
		members = "Т. Ђаковић-Секулић, М. Сакач, Д. Шојић, С. Јовановић-Шанта и А. Тубић";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "socialSciences";
		
		mnoList = new ArrayList<MNO>();
		researchAreas = new HashSet<ResearchAreaDTO>();
		
	}
}
