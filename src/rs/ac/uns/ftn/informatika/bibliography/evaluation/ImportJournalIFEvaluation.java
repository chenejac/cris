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

import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportJournalIFEvaluation{
	
	public static Connection conn;
	 
	public static RecordDB recordDB = new RecordDB();
	
	public static EvaluationDB evaluationDB = new EvaluationDB();
	
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
	
	public static void importFromExcel (String xlsPath){
		Integer commissionId = 1;
		int i = 0;
		int k = 0;
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
			
			Record journal = null;
			String cellContent = "";
			while (rows.hasNext ()){
				journal = null;
				XSSFRow row = (XSSFRow) rows.next();

				XSSFCell cell = row.getCell(0);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString();
				}
				if(cellContent.equals("id")){
					XSSFCell idCell = row.getCell(1);
					XSSFRichTextString richTextString = idCell.getRichStringCellValue();
					cellContent = richTextString.getString();
					journal = recordDB.getRecord(conn, cellContent);
				}
				if(journal == null)
					continue;
				else 
					k++;
				while (rows.hasNext()){
					row = (XSSFRow) rows.next();
					cell = row.getCell(0);
					if(cell == null)
						cellContent = "";
					else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString();
					}
					
					if(cellContent.equalsIgnoreCase("Vrednovanje")){
						for (int j = 1; j < row.getLastCellNum(); j++){
							if((sheet.getRow(row.getRowNum()+1).getCell(j) != null) && (0 != (int)sheet.getRow(row.getRowNum()+1).getCell(j).getNumericCellValue())){
								int year = (int)sheet.getRow(row.getRowNum()+1).getCell(j).getNumericCellValue();
								Calendar startDate = new GregorianCalendar();
								startDate.set(Calendar.YEAR, year);
								startDate.set(Calendar.DAY_OF_YEAR, 1);
								Calendar endDate = new GregorianCalendar();
								endDate.set(Calendar.YEAR, year);
								endDate.set(Calendar.MONTH, Calendar.DECEMBER);
								endDate.set(Calendar.DAY_OF_MONTH, 31);
								String value = row.getCell(j).getStringCellValue();
								if(value.equalsIgnoreCase("M21a"))
									value = "topLeadingInternationalJournal";
								else if(value.equalsIgnoreCase("M21"))
									value = "leadingInternationalJournal";
								else if (value.equalsIgnoreCase("M22"))
									value = "outstandingInternationalJournal";
								else if (value.equalsIgnoreCase("M23"))
									value = "internationalJournal";
								else if (value.equalsIgnoreCase("M24"))
									value = "speciallyVerifiedInternationalJournal";
								else if (value.equalsIgnoreCase("M51"))
									value = "leadingNationalJournal";
								else if (value.equalsIgnoreCase("M52"))
									value = "nationalJournal";
								else if (value.equalsIgnoreCase("M53"))
									value = "scienceJournal";
								if(evaluationDB.addResultClassificationByCommission(conn, journal.getMARC21Record().getControlNumber(), "type", value, startDate, endDate, commissionId))
									i++;
								else {
									System.out.println("Greska: " + journal.getMARC21Record().getControlNumber());
								}
							}
						}
						break;
					} 
					
				}
			}	
			System.out.println(i + " " + k);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  xlsPath    = "D:/casopisiVrednovanjeSaImpaktFaktorima.xlsx";
		ImportJournalIFEvaluation.importFromExcel (xlsPath);
		
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}		