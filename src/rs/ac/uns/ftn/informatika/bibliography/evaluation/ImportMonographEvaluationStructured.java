package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDataDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographEvaluationDataDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportMonographEvaluationStructured{
	
	public static Connection conn;
	 
	public static RecordDB recordDB = new RecordDB();
	
	public static RecordDAO recordDAO = new RecordDAO(recordDB);
	
	public static EvaluationDB evaluationDB = new EvaluationDB();
	
	public static EvaluationDataDB evaluationDataDB = new EvaluationDataDB();
	
	static {
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void importFromExcel (String xlsPath) throws Throwable{
		Integer commissionId = 1;
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
			rows.next();
			
			while (rows.hasNext ()){
				String dataLabel = null;
				String cellContent = "";
				XSSFRow row = null;
				XSSFCell cell = null;
				while ((rows.hasNext()) && (dataLabel == null)) {
					row = (XSSFRow) rows.next();
					cell = row.getCell(0);
					if(cell == null)
						cellContent = "";
					else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString();
					}
					if(cellContent.equals("ID monografije")){
						dataLabel=cellContent;
					}
				};
				Record monograph = null;
				cellContent = "";
				cell = row.getCell(1);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString();
				}
				monograph = recordDB.getRecord(conn, cellContent);
				if(monograph == null){
					System.out.println(cellContent);
					continue;
				} else {
					System.out.println("Monograph: " + monograph);
				}
				
				MonographEvaluationDataDTO monographEvaluationData = new MonographEvaluationDataDTO();
				monographEvaluationData.setMonograph((MonographDTO) monograph.getDto());
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				cellContent = "";
				cell = row.getCell(1);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString();
				}
				if((cellContent != null) && (cellContent.trim().length() > 0))
					monographEvaluationData.setHasReviewInInternationalJournal(cellContent);
				row = (XSSFRow) rows.next();
				
				cellContent = "";
				cell = row.getCell(1);
				if(cell == null)
					cellContent = "";
				else {
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						cellContent = "" + (int)cell.getNumericCellValue();
					} else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString().trim();
					}
				}
				if((cellContent != null) && (cellContent.trim().length() > 0)){
					try {
						monographEvaluationData.setNumberOfReviewers(Integer.parseInt(cellContent));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if((monographEvaluationData.getHasReviewInInternationalJournal() != null) || (monographEvaluationData.getNumberOfReviewers() != 0)){
					evaluationDataDB.insertMonographEvaluationData(conn, monographEvaluationData);
					System.out.println("skladisteni podaci za monographEvaluationData");
				} else {
					System.out.println("nema podataka za monographEvaluationData");
				}
					
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				row = (XSSFRow) rows.next();
				cellContent = "";
				cell = row.getCell(1);
				if(cell == null)
					cellContent = "";
				else {
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						cellContent = "" + (int)cell.getNumericCellValue();
					} else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString().trim();
					}
				}
				System.out.println(cellContent);
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1905);
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 3005);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				String type = "type";
				String value = cellContent;
				if((value != null) && (value.trim().length() == 3)){
//					if(value.equalsIgnoreCase("M11"))
//						value = "outstandingInternationalMonograph";
//					else if (value.equalsIgnoreCase("M12"))
//						value = "internationalMonograph";
//					else if (value.equalsIgnoreCase("M15"))
//						value = "leadingInternationalPublication";
//					else if (value.equalsIgnoreCase("M16"))
//						value = "internationalPublication";
//					else if (value.equalsIgnoreCase("M41"))
//						value = "outstandingNationalMonograph";
//					else if (value.equalsIgnoreCase("M42"))
//						value = "nationalMonograph";
//					else if (value.equalsIgnoreCase("M43"))
//						value = "monograph";
//					else if (value.equalsIgnoreCase("M46"))
//						value = "leadingNationalPublication";
//					else if (value.equalsIgnoreCase("M47"))
//						value = "nationalPublication";
//					else
						type = "resultType";
				}
				System.out.println(type);
				System.out.println(value);
				if(evaluationDB.addResultClassificationByCommission(conn, monograph.getMARC21Record().getControlNumber(), type, value, startDate, endDate, commissionId))
					k++;
				else
					System.out.println(monograph);
//				Record paperMonograph = null;
//				for (int j = 8; j < row.getLastCellNum(); j++){
//					paperMonograph = null;
//					cell = row.getCell(j);
//					if(cell == null)
//						cellContent = "";
//					else {
//						XSSFRichTextString richTextString = cell.getRichStringCellValue();
//						cellContent = richTextString.getString();
//					}
//					paperMonograph = recordDB.getRecord(conn, cellContent);
//					if(paperMonograph == null){
//						System.out.println(paperMonograph);
//						throw new Throwable();
//					}
////					monograph.loadDTOFromMARC21();
////					MonographDTO monographDTO = (MonographDTO)monograph.getDto();
////					if(monographDTO.getEditors().size()==0 && monographDTO.getAllAuthors().size() > 0){
////						monographDTO.setEditors(monographDTO.getAllAuthors());
////						monographDTO.setMainAuthor(new AuthorDTO());
////						monographDTO.setOtherAuthors(new ArrayList<AuthorDTO>());
////						monograph.loadMARC21FromDTO();
////						recordDAO.update(monograph);
////						System.out.println("Updated: " + monograph);
////					}
//					if(row.getCell(j+2)!=null)
//						value = row.getCell(j+2).getStringCellValue();
//					else
//						value=null;
//					if(value != null){
//						if(evaluationDB.addResultClassificationByCommission(conn, paperMonograph.getMARC21Record().getControlNumber(), "resultType", value, startDate, endDate, commissionId))
//							i++;
//						else {
//							System.out.println(paperMonograph.getMARC21Record().getControlNumber());
//						}
//					}
//					j+=2;
//				}
			}	
//			recordDAO.optimizeIndexer();
			System.out.println("Monografija: " + k);
			System.out.println("Radova: " + i);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  xlsPath    = "D:/MonografijeDMI.xlsx";
		try {
			ImportMonographEvaluationStructured.importFromExcel (xlsPath);
		} catch (Throwable e1) {
			System.exit(0);
		}
		
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}		