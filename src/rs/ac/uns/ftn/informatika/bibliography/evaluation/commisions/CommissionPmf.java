package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

public class CommissionPmf extends CommissionWoSSimple {

	public CommissionPmf(){
		super();
		comissionID = 710;
		appointmentBoard = "PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 13);
		appointmentDate.set(Calendar.MONTH, Calendar.DECEMBER);
		appointmentDate.set(Calendar.YEAR, 2011);
		members = " ";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		mnoList = new ArrayList<MNO>();
		researchAreas = new HashSet<ResearchAreaDTO>();
		
	}
}
