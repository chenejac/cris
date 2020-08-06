package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ImportJournalNameISSN {
	
	public static Connection conn;
	 
	public static RecordDB recordDB = new RecordDB();

	public static String importFolder;
	
	public static String exportFolder;
	
	public static HashMap<String, Integer> workbookHeaders = new HashMap<String, Integer>();
	
	public static int hederRowNumber = 0;
	
	
	static {
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		
		importFolder = rb.getString("importFolder");
		exportFolder = rb.getString("exportFolder");
		
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
		
		//postavlja se putanja
		xlsPath = importFolder+xlsPath;
		
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream (xlsPath);
		}catch (FileNotFoundException e){
			System.out.println ("File not found in the specified path.");
			e.printStackTrace ();
		}
		
		try {
			//reading xls document and its header
			HSSFWorkbook workBook = new HSSFWorkbook(inputStream);
			
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			if(sheet.getRow(hederRowNumber)!=null)
			{
				importHeaderFromExcel(sheet.getRow(hederRowNumber));
				for(int i=0;i<=hederRowNumber;i++)
					rows.next();
			}

			Record journal = null;
			JournalDTO journalDTO = null;
			int rowsCounter = hederRowNumber; //zbog zaglavlja
			
			//reading each row into JOURNALDTO object
			while (rows.hasNext ()){
				
				journal = null;
				journalDTO = null;
				HSSFRow row = (HSSFRow) rows.next();
				rowsCounter++;
				
				System.out.println(rowsCounter);
				
				//postavljanje svihmogucih vrednosti koje mogu da se ocitaju na null
				String ID = null;
				String naziv = null;
				String skraceni_Naziv = null;
				String ISSN = null;
				String URL = null;
				
				//citanje BYSIS() ID kolone
				HSSFCell cell = row.getCell(workbookHeaders.get("ID").intValue());
				if(cell == null)
				{
					System.out.println("Ocekuje se vrednost ID u "+ rowsCounter);
					System.exit(0);
				}
				else 
				{
					HSSFRichTextString richTextString = cell.getRichStringCellValue();			
					ID = richTextString.getString();
					
					journal = recordDB.getRecord(conn, ID);
					//ne postoji casopis za bazu
					if(journal==null)
					{
						System.out.println("Ne postoji casopis u bazi sa "+ ID);
						System.exit(0);
					}
					journal.loadFromDatabase();
					
					if(!(journal.getDto() instanceof JournalDTO))
					{
						System.out.println("ID u bazi ne valja "+ ID + " to je " + journal.getDto().getRecord().getType());
						continue;
					}
					
					
					journalDTO = (JournalDTO) journal.getDto();
//					System.out.println(journalDTO.getStringRepresentation());
				}
				
				//citanje Naziva kolone
				cell = row.getCell(workbookHeaders.get("Naziv").intValue());
				if(cell == null)
				{
					System.out.println("Ocekuje se vrednost Naziv u "+ rowsCounter);
					System.exit(0);
				}
				else {
					HSSFRichTextString richTextString = cell.getRichStringCellValue();
					naziv = richTextString.getString();
					journalDTO.getName().setContent(naziv);
				}
				
				//citanje Skraceni naziv kolone
				cell = row.getCell(workbookHeaders.get("Skraceni naziv").intValue());
				if(cell == null)
				{
//					System.out.println("Ocekuje se vrednost Skraceni naziv u "+ rowsCounter);
//					System.exit(0);
				}
				else {
					HSSFRichTextString richTextString = cell.getRichStringCellValue();
					skraceni_Naziv = richTextString.getString();
				}
				
				//citanje ISSN kolone i obrada
				cell = row.getCell(workbookHeaders.get("ISSN").intValue());
				if(cell == null)
				{
					System.out.println("Ocekuje se vrednost ISSN u "+ rowsCounter);
					System.exit(0);
				}
				else {
					HSSFRichTextString richTextString = cell.getRichStringCellValue();
					ISSN = richTextString.getString();
					
					if(ISSN.startsWith("ISBN:"))
					{
//						System.out.println("Vrednost ISBN je:" +ISSN);
					}
					else if (ISSN.startsWith("NN"))
					{
//						System.out.println("Vrednost NN je:");
					}
					else if (ISSN.startsWith("Printed:"))
					{
						String[] tokens = ISSN.split(";");
						StringBuffer retVal = new StringBuffer();
						
						for (int i = 0; i < tokens.length; i++) {
							String token = tokens[i];
							token = token.trim();
							if (token.startsWith("Printed:") && i==0)
							{
								token = token.substring(8);
								retVal.append(token);
								if(1 < tokens.length)
									retVal.append("(pISSN);");
							}
							else if (token.startsWith("Online:") && i==1)
							{
								token = token.substring(7);
								retVal.append(token);
								if(1 < tokens.length)
									retVal.append("(eISSN);");
							}
							else
							{
								System.out.println("Vrednost ISSN nije u odgovaraju'em formatu "+ ISSN);
								System.exit(0);
							}
							
						}
						ISSN = retVal.toString();
					}
					else if (ISSN.startsWith("Online:"))
					{
						String[] tokens = ISSN.split(";");
						StringBuffer retVal = new StringBuffer();
						
						for (int i = 0; i < tokens.length; i++) {
							String token = tokens[i];
							token = token.trim();
							if (token.startsWith("Online:") && i==0)
							{
								token = token.substring(7);
								retVal.append(token);
								if(1 < tokens.length)
									retVal.append("(eISSN);");
							}
							else
							{
								System.out.println("Vrednost ISSN nije u odgovaraju'em formatu "+ ISSN);
								System.exit(0);
							}
							
						}
						ISSN = retVal.toString();
					}
					else
					{
						System.out.println("Vrednost ISSN nije u odgovaraju'em formatu "+ ISSN);
						System.exit(0);
					}
					journalDTO.setIssn(ISSN);
				}
				
				//citanje URL
				cell = row.getCell(workbookHeaders.get("URL").intValue());
				if(cell == null)
				{
//					System.out.println("Ocekuje se vrednost URL u "+ rowsCounter);
//					System.exit(0);
				}
				else {
					HSSFRichTextString richTextString = cell.getRichStringCellValue();
					URL = richTextString.getString();
				}
				
				//SNIMANJE JOURNALDTO IZMENJENOG U BAZU
				
				RecordDAO recordDAO = new RecordDAO(new RecordDB());
				RecordConsumerListener recordConsumer = new RecordConsumerListener();
				if ((recordDAO.update(new Record(null, null, "sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
						journalDTO)) == false) || (recordConsumer.update(journalDTO) == false)) 
				{
					System.out.println("Neuspeli update "+ rowsCounter);
					System.exit(0);
				}
				else
				{
					System.out.println("UPDATE USPEO "+ journalDTO.getControlNumber());
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void importHeaderFromExcel (HSSFRow row){
		
		if(row==null)
			return;

		for (int i = 0; i < row.getLastCellNum(); i++){
			
			HSSFCell cell = row.getCell(i);
			String cellContent="";
			
			if(cell == null)
				continue;
			else {
				HSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString();
			}
			
			if(cellContent.equalsIgnoreCase("ID")){
				workbookHeaders.put("ID", new Integer(i));
			}
			else if (cellContent.equalsIgnoreCase("Naziv")){
				workbookHeaders.put("Naziv", new Integer(i));
			}
			else if (cellContent.equalsIgnoreCase("Skraceni naziv")){
				workbookHeaders.put("Skraceni naziv", new Integer(i));
			}
			else if (cellContent.equalsIgnoreCase("ISSN")){
				workbookHeaders.put("ISSN", new Integer(i));
			}
			else if (cellContent.equalsIgnoreCase("URL")){
				workbookHeaders.put("URL", new Integer(i));
			}
		}
//				System.out.println("ID " + workbookHeaders.get("ID") + " TITLE " + workbookHeaders.get("TITLE") + " ISSN " + workbookHeaders.get("ISSN"));
	}
	
	
	public static void main (String[] args){
		
//		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
//		System.out.println(rb.getString("hostname"));
//		System.out.println(rb.getString("port"));
//		System.out.println(rb.getString("schema"));
//		System.out.println(rb.getString("connectionParameters"));
//		System.out.println(rb.getString("username"));
//		System.out.println(rb.getString("password"));

		String  xlsDocument    = "/promeneISSN.xls";
		ImportJournalNameISSN.importFromExcel(xlsDocument);
		
		
		
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Zqavrsio sve");

	}


}
