package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import rs.ac.uns.ftn.informatika.bibliography.dao.PositionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;

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

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportAuthorsUPAFarmaceutski {
	
	 
	public static PersonDB recordDB = new PersonDB();
	
	public static RecordDAO recordDAO = new RecordDAO(recordDB);
	
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
			rows.next();
			while (rows.hasNext ()){
				person = null;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(1);
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
				if(row.getCell(4) != null){
					String value = row.getCell(4).getRawValue();
					dto.setApvnt(value);
				}

				if(row.getCell(5) != null){
					String value = row.getCell(5).getRawValue();
					dto.setORCID(value);
				}

				if(row.getCell(3) != null){
					String value = row.getCell(3).getRawValue();
					dto.setEmail(value);
				}

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
				if(row.getCell(7) != null){
					cellContent = "" + (int)row.getCell(7).getNumericCellValue();
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
				
//					dto.setInstitution((InstitutionDTO)(recordDAO.getDTO("(BISIS)5929")));
//				}
					if(row.getCell(0) != null){
						XSSFRichTextString richTextString = row.getCell(0).getRichStringCellValue();
						cellContent = richTextString.getString();
						if((cellContent!=null) && (!"".equals(cellContent.trim()))){
							dto.setInstitution((InstitutionDTO)(recordDAO.getDTO(cellContent)));
						}
					}
				
//				System.out.println(person);
				
				person.loadMARC21FromDTO();
				
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 2023);
				startDate.set(Calendar.MONTH, Calendar.JANUARY);
				startDate.set(Calendar.DAY_OF_MONTH, 1);
				
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 2023);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				if(dto.getInstitution().getControlNumber()!= null)
					person.getRelationsThisRecordOtherRecords().add(new RecordRecord(dto.getInstitution().getControlNumber(), "authorInstitutionSelfevaluation", "belongs to", startDate, endDate));

//				System.out.println(person);
				if(recordDAO.update(person)){
					k++;
					System.out.println(k);
				}
				else {
					System.out.println("Neuspelo dodavanje");
				}
				System.out.println(person);
			}	
			System.out.println(k);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  xlsPath    = "E:/zaposleniUPAFarmaceutski2023.xlsx";
		ImportAuthorsUPAFarmaceutski.importFromExcel (xlsPath);
	}


}		