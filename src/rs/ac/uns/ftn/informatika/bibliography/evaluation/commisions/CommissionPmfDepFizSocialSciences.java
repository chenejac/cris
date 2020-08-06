package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

public class CommissionPmfDepFizSocialSciences extends CommissionWoSSimple {

	public CommissionPmfDepFizSocialSciences(){
		super();
		comissionID = 724;
		appointmentBoard = "DF-SOC-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 28);
		appointmentDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		appointmentDate.set(Calendar.YEAR, 2012);
		members = "Željka Cvejić, Milan Pantić, Dušan Mrđa";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "socialSciences";
		
		mnoList = new ArrayList<MNO>();
		researchAreas = new HashSet<ResearchAreaDTO>();
		

		
	}
}
