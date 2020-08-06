package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gint.util.string.LatCyrUtils;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

public class UNSArchiveSerializer implements GroupSerializer {
	
	protected String year = null;
	private List<UNSArchiveRecord> data = null;

	@Override
	public OutputStream exportRecords(ImportRecordsDTO records) {
		return null;
	}

	@Override
	public ImportRecordsDTO importRecords(InputStream is) {
		ImportRecordsDTO retVal = new ImportRecordsDTO();
		if(importInstitutions(retVal))
			if(importAuthors(retVal))
				if(importTheses(retVal))
					return retVal;
		return new ImportRecordsDTO();
	}
	
	protected List<UNSArchiveRecord> collectData(String year){
		String archiveUrl = "http://www.uns.ac.rs/sr/doktorske/javniUvid" + year + ".htm";
		try{
			Document page = Jsoup.connect(archiveUrl).get();
			ArrayList<UNSArchiveRecord> records = new ArrayList<UNSArchiveSerializer.UNSArchiveRecord>();
			page.outputSettings().charset("UTF-8");
			Elements tableRows = page.select("table[border=1] tr");
			tableRows.remove(0); //izbaciti prvi red jer je to red sa naslovima kolona
			for(Element tr : tableRows){
				Elements td = tr.select("td");
				records.add(new UNSArchiveRecord(year, td.get(1).text(), td.get(2).text(), td.get(3).text(), td.get(4).text(), td.get(5).text()));
			}
			return records;
		}catch(Exception ex){
			return null;
		}
		
	}
	
	private boolean importInstitutions(ImportRecordsDTO ir){
		//TODO institucije
		if(this.year == null) return false;
		try{
			this.data = collectData(this.year);
			if(data == null){
				return false;
			}
			List<String> institutionNames = new ArrayList<String>();
			List<RecordDTO> institutions = new ArrayList<RecordDTO>();
			for(UNSArchiveRecord uar : this.data){
				String institutionName = uar.getInstitution().trim(); //da li treba propustiti kroz latinicno-cirilicni konverter ???
				if(!institutionNames.contains(institutionName)){
					institutionNames.add(institutionName);
				}
			}
			for(String name : institutionNames){
				InstitutionDTO institutionDTO = new InstitutionDTO();
				institutionDTO.getName().setContent(name);
				institutionDTO.getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
				institutionDTO.setControlNumber(name);
				institutionDTO.setSuperInstitution(new InstitutionDTO());
				institutions.add(institutionDTO);
			}
			System.out.println(institutions.size()); //ima li potrebe za ovim ?
			ir.getAllRecords().addAll(institutions);
			ir.setAllRecords(ir.getAllRecords());
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	private boolean importAuthors(ImportRecordsDTO ir){
		//TODO autori
		if(this.year == null) return false;
		try{
			List<RecordDTO> authors = new ArrayList<RecordDTO>();
			for(UNSArchiveRecord uar : this.data){
				AuthorDTO authorDTO = new AuthorDTO();
				String name = uar.getName().trim();
				/*
				 * ime je pisano na jedan od sledeca tri nacina
				 * Prezime, Ime (gde imena i prezimena moze biti vise ali su razdvojeni zarezom)
				 * mr Ime Prezime (gde imena i prezimena moze biti vise)
				 * Ime Prezime (gde imena i prezimena moze biti vise)
				 * 
				 */
				if(name.contains(",")){
					String[] nameParts = name.split(",",2);
					authorDTO.getName().setFirstname(nameParts[1]);
					authorDTO.getName().setLastname(nameParts[0]);
				}else{
					if(LatCyrUtils.toLatin(name).toLowerCase().startsWith("mr ")){
						authorDTO.setTitle(name.substring(0, 2));
						name = name.substring(3);
					}
					String[] nameParts = name.split(" ",2);
					authorDTO.getName().setFirstname(nameParts[0]);
					authorDTO.getName().setLastname(nameParts[1]);
				}
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				if(!authors.contains(authorDTO)){
					authors.add(authorDTO);
				}
			}
			System.out.println(authors.size());
			ir.getAllRecords().addAll(authors);
			ir.setAllRecords(ir.getAllRecords());
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	private boolean importTheses(ImportRecordsDTO ir){
		//TODO teze
		
		//String pathToDissertation = ""; // year/name/izvestaj.pdf
		
		/*
		 * disertacije se nalaze na ekstrenom disku, a putanja do disertacije je u sledecem obliku
		 * J:/godina/ime_prezime/disertacija.pdf
		 * J:/godina/ime_prezime/izvestaj.pdf - ne mora da postoji
		 * 
		 * takodje, moze umesto .pdf da se javi .zip
		 * u tom slucaju, prepustiti korisniku da rucno popravi unos
		 */
		
		return false;
	}
	
	/**
	 * Inner bean class
	 * @author Molnar
	 *
	 */
	protected class UNSArchiveRecord {
		protected String year;
		protected String name; //full name
		protected String institution;
		protected String researchArea;
		protected String title;
		protected String publicPeriod;
		
		/*
		protected String dissertationLink; //url ?
		protected String reportLink;
		
		*/
		
		public UNSArchiveRecord() {
		}

		public UNSArchiveRecord(String year, String name, String institution,
				String researchArea, String title, String publicPeriod) {
			this.year = year;
			this.name = name;
			this.institution = institution;
			this.researchArea = researchArea;
			this.title = title;
			this.publicPeriod = publicPeriod;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getInstitution() {
			return institution;
		}

		public void setInstitution(String institution) {
			this.institution = institution;
		}

		public String getResearchArea() {
			return researchArea;
		}

		public void setResearchArea(String researchArea) {
			this.researchArea = researchArea;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPublicPeriod() {
			return publicPeriod;
		}

		public void setPublicPeriod(String publicPeriod) {
			this.publicPeriod = publicPeriod;
		}
		
		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public Calendar getPublicPeriodStartDate(){
			
			String start = getPublicPeriod().split("-")[0];
			String[] parts = start.trim().split(" ");
			
			Calendar cal = Calendar.getInstance();
			
			String day = parts[0].substring(0, parts[0].length()-1);
			String month = getMonthNumber(parts[1]);
			String year;
			if(parts.length > 2){
				year = parts[2].substring(0, parts[2].length()-1);
			}else{
				year = getYear();
			}
			
			cal.set(new Integer(year), new Integer(month)-1, new Integer(day), 0, 0, 0);
			return cal;
		}
		
		public Calendar getPublicPeriodEndDate(){
			
			String start = getPublicPeriod().split("-")[1];
			String[] parts = start.trim().split(" ");
			
			Calendar cal = Calendar.getInstance();
			
			String day = parts[0].substring(0, parts[0].length()-1);
			String month = getMonthNumber(parts[1]);
			String year = parts[2].substring(0, parts[2].length()-1);
			
			cal.set(new Integer(year), new Integer(month)-1, new Integer(day), 0, 0, 0);
			return cal;
		}
		
		private String getMonthNumber(String month){
			month = month.toLowerCase();
			if(month.startsWith("januar")){
				return "01";
			}else if(month.startsWith("februar")){
				return "02";
			}else if(month.startsWith("mart")){
				return "03";
			}else if(month.startsWith("april")){
				return "04";
			}else if(month.startsWith("maj")){
				return "05";
			}else if(month.startsWith("jun")){
				return "06";
			}else if(month.startsWith("jul")){
				return "07";
			}else if(month.startsWith("avgust")){
				return "08";
			}else if(month.startsWith("septembar")){
				return "09";
			}else if(month.startsWith("oktobar")){
				return "10";
			}else if(month.startsWith("novembar")){
				return "11";
			}else if(month.startsWith("decembar")){
				return "12";
			}else{
				return null;
			}
		}
		
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public String getYear(){
		return this.year;
	}
	
	public static void main(String args[]){
		String name = "mr Petar Petrovic";
		System.out.println("["+name.substring(0,2)+"]");
		System.out.println("["+name.substring(3)+"]");
	}

}
