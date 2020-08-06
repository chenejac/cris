package rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

public class MaticnaKnjigaUtils {
	
public static String calculateSkolskaGodina(Calendar datum){
		
		int month = datum.get(Calendar.MONTH)+1;		
		int year = datum.get(Calendar.YEAR);		
		if(month>9){		
			return year+"/"+(year+1);
		}
		else{			
			return (year-1)+"/"+year;
		}
		
		
	}

public static List<RegisterEntryDTO> generateData (List<RegisterEntryDTO> entries, boolean regenarate, int registerEntryId, int academicYearNumber){
	List<RegisterEntryDTO> forGeneratingId = new ArrayList<RegisterEntryDTO>();
	List<RegisterEntryDTO> forGeneratingData = new ArrayList<RegisterEntryDTO>();
//	System.out.println("Generating data for register entry...");
//	System.out.println("entries="+entries);
//	int brojac = 0;
	for(RegisterEntryDTO entry:entries){
//		System.out.println(brojac+++". Processing register entry, id="+entry.getId());
			if(entry!=null){				
					String skolskaGodina = MaticnaKnjigaUtils.calculateSkolskaGodina(entry.getPromotionDate());
					entry.setAcademicYear(skolskaGodina);
					if(regenarate){
//						forGeneratingData.add(entry);
						forGeneratingId.add(entry);
					}
					else if(entry.getId()!=null && !entry.getId().equals("") && !entry.getId().equals("-1") && !entry.getId().equals("-2")){	
						forGeneratingData.add(entry);
					}						
					else if(entry.getPromotionDate()!=null){							
						forGeneratingId.add(entry);
					}
		}
		
	}	
	
	List<String> sortAttributesForGen = new ArrayList<String>();
	sortAttributesForGen.add("promotionDateDate");
	sortAttributesForGen.add("institution");
	
	List<String> sortDirections = new ArrayList<String>();
	sortDirections.add("asc");
	sortDirections.add("asc");
	
	Collections.sort(forGeneratingId, new GenericComparator<RegisterEntryDTO>(
			sortAttributesForGen, sortDirections));
	
	
	for(RegisterEntryDTO entry:forGeneratingId){				
			entry.setId("" + registerEntryId++);
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(entry.getPromotionDate().getTime()));
			forGeneratingData.add(entry);
	}
	
	
	List<String> sortAttributes = new ArrayList<String>();
	
	sortAttributes.add("academicYear");
	sortAttributes.add("id");
	
	Collections.sort(forGeneratingData, new GenericComparator<RegisterEntryDTO>(
			sortAttributes, sortDirections));
	
	
	for(int i=0;i<forGeneratingData.size();i++){
		RegisterEntryDTO entry = forGeneratingData.get(i);
		if(i==0 || i==forGeneratingData.size()-1){
			entry.setAcademicYearNumber(academicYearNumber++);				
		} else 
		if(!entry.getAcademicYear().equals(forGeneratingData.get(i+1).getAcademicYear())){
			entry.setAcademicYearNumber(academicYearNumber++);
			academicYearNumber=1;					
		} else {
				entry.setAcademicYearNumber(academicYearNumber++);
		}
	}	
	return forGeneratingData;
}

}
