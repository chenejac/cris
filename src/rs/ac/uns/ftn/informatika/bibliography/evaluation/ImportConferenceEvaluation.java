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

public class ImportConferenceEvaluation{
	
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
		Integer commissionId = 701;
		Integer collumnEvaluationId = 5;
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
//			for(int i=0;i<125;i++)
				rows.next();
			Record conference = null;
			String cellContent = "";
			while (rows.hasNext ()){
				conference = null;
				XSSFRow row = (XSSFRow) rows.next();
				if(row.getCell(collumnEvaluationId) != null){
					XSSFCell cell = row.getCell(1);
					if(cell == null)
						cellContent = "";
					else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString();
					}
					conference = recordDB.getRecord(conn, cellContent);
					if(conference == null){
						System.out.println("Cell content: " + cellContent);
						continue;
					}
					Calendar startDate = new GregorianCalendar();
					startDate.set(Calendar.YEAR, 1908);
					startDate.set(Calendar.DAY_OF_YEAR, 1);
					Calendar endDate = new GregorianCalendar();
					endDate.set(Calendar.YEAR, 3008);
					endDate.set(Calendar.MONTH, Calendar.DECEMBER);
					endDate.set(Calendar.DAY_OF_MONTH, 31);
				
					String value = row.getCell(collumnEvaluationId).getStringCellValue();
					if(value.equalsIgnoreCase("Medjunarodni"))
						value = "internationalConference";
					else if (value.equalsIgnoreCase("Nacionalni"))
						value = "nationalConference";
					if(("internationalConference".equals(value)) || ("nationalConference".equals(value))){
						if(evaluationDB.addResultClassificationByCommission(conn, conference.getMARC21Record().getControlNumber(), "type", value, startDate, endDate, commissionId))
							k++;
						else {
							conference.loadFromDatabase();
							System.out.println(conference);
						}
					}
				}
			}	
			System.out.println(k);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static void main (String[] args){
		
		String  xlsPath    = "E:/skupoviTF.xlsx";
		ImportConferenceEvaluation.importFromExcel (xlsPath);
		
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}		