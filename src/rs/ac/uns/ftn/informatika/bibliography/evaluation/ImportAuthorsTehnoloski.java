package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.PositionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportAuthorsTehnoloski{
	
	 
	public static PersonDB recordDB = new PersonDB();
	
	public static RecordDAO recordDAO = new RecordDAO(recordDB);
	
	public static EvaluationDB evaluationDB = new EvaluationDB();
	
	public static PositionDAO positionDAO = new PositionDAO();
	
//	static {
//		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
//		String hostname = rb.getString("hostname");
//		String port = rb.getString("port");
//		String schema = rb.getString("schema");
//		String connectionParameters = rb.getString("connectionParameters");
//		String username = rb.getString("username");
//		String password = rb.getString("password");
//
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
//				+ "/" + schema + connectionParameters, username, password);
//			conn.setAutoCommit(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void importFromExcel (String xlsPath){
		int k = 0;
		int i = 0;
		InputStream inputStream = null;
		
		try{
			inputStream = new FileInputStream (xlsPath);
		}catch (FileNotFoundException e){
			System.out.println ("File not found in the specified path.");
			e.printStackTrace ();
		}

		try{
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
//			rows.next();
			Person person = null;
			String cellContent = "";
//			rows.next();
			while (rows.hasNext ()){
				person = null;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(6);
				if(cell == null)
					cellContent = "";
				else {
					cellContent = cell.getRichStringCellValue().getString(); //"(BISIS)" + (int)cell.getNumericCellValue();
				}
				person = (Person)recordDAO.getRecord(cellContent);
				if(person == null){
					System.out.println("Cell content: " + cellContent);
					continue;
				}
				
				person.loadFromDatabase();
//				person.loadDTOFromMARC21();
				AuthorDTO dto = ((AuthorDTO)person.getDto());
//				if(row.getCell(1) != null){
//					String value = row.getCell(1).getRawValue();
//					person.setApvnt(value);
//				}
//				if(row.getCell(2) != null){
//					XSSFRichTextString richTextString = row.getCell(2).getRichStringCellValue();
//					cellContent = richTextString.getString();
//					AuthorNameDTO firstName = new AuthorNameDTO();
//					if(cellContent!= null)
//						firstName.setLastname(cellContent.toString());
//					if(row.getCell(3) != null){
//						richTextString = row.getCell(3).getRichStringCellValue();
//						cellContent = richTextString.getString();
//						firstName.setOtherName(cellContent.trim());
//					}
//					else
//						firstName.setOtherName(null);
//					richTextString = row.getCell(4).getRichStringCellValue();
//					cellContent = richTextString.getString();
//					if(cellContent!= null)
//						firstName.setFirstname(cellContent.trim());
//					dto.setName(firstName);
//				}
				
				//(i++ > 348) && 
				if(row.getCell(13) != null){
					cellContent = "" + (int)row.getCell(13).getNumericCellValue();
					PositionDTO position = positionDAO.getPosition(cellContent);
//					if(cellContent.trim().equals("Redovni profesor"))
//						position = positionDAO.getPosition("50");
//					else if(cellContent.trim().equals("Vanredni profesor"))
//						position = positionDAO.getPosition("40");
//					else if(cellContent.trim().equals("Docent"))
//						position = positionDAO.getPosition("30");
//					else if(cellContent.trim().equals("Viši predavač"))
//						position = positionDAO.getPosition("25");
//					else if(cellContent.trim().equals("istraživač pripravnik"))
//						position = positionDAO.getPosition("66");
//					else if(cellContent.trim().equals("istraživač saradnik"))
//						position = positionDAO.getPosition("70");
//					else if(cellContent.trim().equals("naučni saradnik"))
//						position = positionDAO.getPosition("80");
//					else if(cellContent.trim().equals("viši naučni saradnik"))
//						position = positionDAO.getPosition("90");
//					else if(cellContent.trim().equals("asistent"))
//						position = positionDAO.getPosition("12");
//					else if(cellContent.trim().equals("stručni saradnik"))
//						position = positionDAO.getPosition("110");
					if(position == null)
						System.out.println("Position null, " + dto.getControlNumber());
					else {
//						AuthorPosition authorPosition = dto.getCurrentPosition();
//						if(authorPosition!=null){
//							dto.getFormerPositions().add(authorPosition);
//						}
						dto.getCurrentPosition().setPosition(position);
						XSSFRichTextString richTextString = row.getCell(10).getRichStringCellValue();
						cellContent = richTextString.getString().trim();
						if(! ("-".equals(cellContent))){
							try {
								DateFormat formatter ; 
								Date date ; 
								formatter = new SimpleDateFormat("dd.MM.yyyy.");
								date = (Date)formatter.parse(cellContent);
								Calendar cal=GregorianCalendar.getInstance();
								cal.setTime(date);
								dto.getCurrentPosition().setStartDate(cal);
								//authorPosition.setEndDate(cal);
								cal = new GregorianCalendar();
								cal.set(Calendar.YEAR, 2009);
								cal.set(Calendar.MONTH, 11);
								cal.set(Calendar.DAY_OF_MONTH, 31);
								dto.getCurrentPosition().setEndDate(cal);
							} catch (ParseException e) {
								e.printStackTrace();
							} 
						}
					}
					
				}
				
				
				
				
//				if(row.getCell(7) != null){
//					cellContent = "" + (int)row.getCell(7).getNumericCellValue();
////					cellContent = richTextString.getString();
//					OrganizationUnitDTO organization = null;
//					if(cellContent.trim().equals("2158"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85798");
//					else if(cellContent.trim().equals("2159"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85799");
//					else if(cellContent.trim().equals("2160"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85800");
//					else if(cellContent.trim().equals("2161"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85801");
//					else if(cellContent.trim().equals("2162"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85802");
//					else if(cellContent.trim().equals("2163"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85803");
//					else if(cellContent.trim().equals("2164"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85804");
//					else if(cellContent.trim().equals("2165"))
//						organization = (OrganizationUnitDTO)recordDAO.getDTO("(BISIS)85805");
//					if(organization == null)
//						System.out.println("Organization null, " + dto.getControlNumber());
//					else {
//						dto.setOrganizationUnit(organization);
//					}
				
					dto.setInstitution((InstitutionDTO)(recordDAO.getDTO("(BISIS)5929")));
//				}
					if(row.getCell(2) != null){
						XSSFRichTextString richTextString = row.getCell(2).getRichStringCellValue();
						cellContent = richTextString.getString();
						if((cellContent!=null) && (!"".equals(cellContent.trim()))){
							dto.setOrganizationUnit((OrganizationUnitDTO)(recordDAO.getDTO(cellContent)));
						}
					}
				
//				System.out.println(person);
				
				person.loadMARC21FromDTO();
				
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 2021);
				startDate.set(Calendar.MONTH, Calendar.JANUARY);
				startDate.set(Calendar.DAY_OF_MONTH, 1);
				
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 2021);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				if(dto.getInstitution().getControlNumber()!= null)
					person.getRelationsThisRecordOtherRecords().add(new RecordRecord(dto.getInstitution().getControlNumber(), "authorInstitutionSelfevaluation", "belongs to", startDate, endDate));
				if(row.getCell(2) != null){
					XSSFRichTextString richTextString = row.getCell(2).getRichStringCellValue();
					cellContent = richTextString.getString();
					if((cellContent!=null) && (!"".equals(cellContent.trim()))){
						person.getRelationsThisRecordOtherRecords().add(new RecordRecord(dto.getOrganizationUnit().getControlNumber(), "authorInstitutionSelfevaluation", "belongs to", startDate, endDate));
						if(dto.getOrganizationUnit().getSuperOrganizationUnit() != null)
							person.getRelationsThisRecordOtherRecords().add(new RecordRecord(dto.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber(), "authorInstitutionSelfevaluation", "belongs to", startDate, endDate));
					}
				}
//				System.out.println(person);
				if(recordDAO.update(person)){
					k++;
					System.out.println(k);
				}
				else {
					System.out.println(person);
				}
			}	
			System.out.println(k);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  xlsPath    = "E:/zaposleniPMF2021.xlsx";
		ImportAuthorsTehnoloski.importFromExcel (xlsPath);
//		String  xlsPath    = "E:/autoriPMF.xlsx";
//		ImportAuthorsTehnoloski.importFromExcelORCID (xlsPath);
	}

	public static void importFromExcelORCID (String xlsPath){
		int k = 0;
		int i = 0;
		InputStream inputStream = null;

		try{
			inputStream = new FileInputStream (xlsPath);
		}catch (FileNotFoundException e){
			System.out.println ("File not found in the specified path.");
			e.printStackTrace ();
		}

		try{
			XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			Person person = null;
			String cellContent = "";
			while (rows.hasNext ()){
				person = null;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(10);
				if(cell == null)
					cellContent = "";
				else {
					cellContent = cell.getRichStringCellValue().getString(); //"(BISIS)" + (int)cell.getNumericCellValue();
				}
				System.out.println(cellContent);
				person = (Person)recordDAO.getRecord(cellContent);
				if(person == null){
					System.out.println("Cell content: " + cellContent);
					continue;
				}

				person.loadFromDatabase();

				AuthorDTO dto = ((AuthorDTO)person.getDto());

				boolean personChanged = false;

				if(row.getCell(4) != null){
					XSSFRichTextString richTextString = row.getCell(4).getRichStringCellValue();
					cellContent = richTextString.getString().trim();
					String orcid = cellContent;

					if(! ("".equals(orcid)))
						if((dto.getORCID()== null) || (!orcid.equals(dto.getORCID()))) {
							personChanged = true;
							System.out.println(orcid);
							System.out.println(dto.getORCID());
							person.setORCID(orcid);
							dto.setORCID(orcid);
						}
				}

				if(row.getCell(5) != null){
					cellContent = ("" + (long)row.getCell(5).getNumericCellValue()).trim();
					String scopus = cellContent;

					if(! ("".equals(scopus)))
						if((dto.getScopusID() == null) || (!scopus.equals(dto.getScopusID()))) {
							personChanged = true;
							System.out.println(scopus);
							System.out.println(dto.getScopusID());
							dto.setScopusID(scopus);
						}

				}

				if(personChanged) {
					person.loadMARC21FromDTO();
//					System.out.println(person);
//					k++;
					if(recordDAO.update(person)){
						k++;
						System.out.println(k);
					}
					else {
						System.out.println(person);
					}
				}
			}
			System.out.println(k);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}		