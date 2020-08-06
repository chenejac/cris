package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;

/**
* This java program is used to read the data from a Excel file and display them
* on the console output.
*/

public class ImportAuthorsAPVN{
	
	public static Connection conn;
	 
	public static PersonDB recordDB = new PersonDB();
	
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
		Integer commissionId = 5;
		Integer collumnEvaluationId = 6;
		int k = 0;
		InputStream inputStream = null;
		
		RecordDAO recordDAO = new RecordDAO(new PersonDB());
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
			while (rows.hasNext ()){
				person = null;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cell = row.getCell(0);
				if(cell == null)
					cellContent = "";
				else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString();
				}
				person = (Person)recordDB.getRecord(conn, cellContent);
				if(person == null){
					System.out.println("Cell content: " + cellContent);
					continue;
				}
				
				if(row.getCell(1) != null){
					String value = row.getCell(1).getRawValue();
					person.loadDTOFromMARC21();
					person.setApvnt(value);
					if(recordDAO.update(person))
						k++;
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


	public static void main (String[] args){
		
		String  xlsPath    = "D:/apvn.xlsx";
		ImportAuthorsAPVN.importFromExcel (xlsPath);
		
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}		