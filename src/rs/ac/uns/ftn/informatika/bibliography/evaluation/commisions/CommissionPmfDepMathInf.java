package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;

public class CommissionPmfDepMathInf extends CommissionWoSSimple {

	public CommissionPmfDepMathInf(){
		super();
		comissionID = 711;
		appointmentBoard = "DMI-PMF";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 21);
		appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
		appointmentDate.set(Calendar.YEAR, 2011);
		members = "Душан Сурла, Милош Рацковић, Марко Недељков";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "naturalSciences";
		
		
		
	}
}
