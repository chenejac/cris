package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

public class ImportAMS {
	
	private static Connection conn = null;
	
	private static RecordDB recordDB = new RecordDB();
	
	private static EvaluationDB evaluationDB = new EvaluationDB();
	
	
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
	
 
	private static void importFromXLS(String path){
		
		RecordDAO recordDAO = new RecordDAO(recordDB);
		InputStream inputStream = null;		
		try{
			inputStream = new FileInputStream (path);
			XSSFWorkbook      workBook = new XSSFWorkbook(inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			rows.next();
			int ukupno = 0;
			int postoji = 0;
			while(rows.hasNext()){
				ukupno++;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cellISSN = row.getCell(0);
				String issn = cellISSN.getStringCellValue().trim();
				XSSFCell cellJournalAbb = row.getCell(1);
				String journalAbb = cellJournalAbb.getStringCellValue().trim();				
				XSSFCell cellPublisher = row.getCell(2);
				String publisher = cellPublisher.getStringCellValue().trim();				
				XSSFCell cellStartYear = row.getCell(3);
				String startYear = String.valueOf(Double.valueOf(cellStartYear.getNumericCellValue()).intValue());
				
				//System.out.println("issn: "+issn+", journalAbb: "+journalAbb+", publisher: "+publisher+", start year: "+startYear);
				
				String recordId = checkISSNExists(issn);
				if(recordId!=null){
					System.out.println("Dodavanje vrednovanja za casopis id="+recordId);
					Calendar startDate = new GregorianCalendar();
					startDate.set(Calendar.YEAR, 2009);
					startDate.set(Calendar.DAY_OF_YEAR, 1);
					Calendar endDate = new GregorianCalendar();
					endDate.set(Calendar.YEAR, 3009);
					endDate.set(Calendar.MONTH, Calendar.DECEMBER);
					endDate.set(Calendar.DAY_OF_MONTH, 31);				
					evaluationDB.addResultClassificationByCommission(conn, recordId, 
								"type", "speciallyVerifiedInternationalJournal", startDate, endDate, 631);
					conn.commit();
				}else{
				 JournalDTO journal = new JournalDTO();
				 journal.setIssn(issn);
				 journal.setNameAbbreviation(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
				 journal.setName(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
     /*				 
				 journal.setPublicationYear(startYear);
				 journal.setNote(new MultilingualContentDTO("Publisher:"+publisher, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
				 */
					
					if (recordDAO.add(new Record("importAMSExcel", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							journal))){
						System.out.println("Uspelo dodavanje casopisa "+journalAbb+", id="+journal.getControlNumber());
						System.out.println("Dodavanje vrednovanja");
						Calendar startDate = new GregorianCalendar();
						startDate.set(Calendar.YEAR, 2009);
						startDate.set(Calendar.DAY_OF_YEAR, 1);
						Calendar endDate = new GregorianCalendar();
						endDate.set(Calendar.YEAR, 3009);
						endDate.set(Calendar.MONTH, Calendar.DECEMBER);
						endDate.set(Calendar.DAY_OF_MONTH, 31);				
						evaluationDB.addResultClassificationByCommission(conn, journal.getControlNumber(), 
									"type", "speciallyVerifiedInternationalJournal", startDate, endDate, 631);
						conn.commit();
					}
				 
					
				}
				
			}
			
		
			
			
			
		}catch (Exception e){			
			e.printStackTrace ();
		}
	}
	
	private static String checkISSNExists(String issn){
		TermQuery termQuery = new TermQuery(new Term("ISSN", issn));
		List<Record> records = Retriever.select(termQuery, new AllDocCollector(false));
		if(records==null || records.size()==0) return null;
		else return records.get(0).getControlNumber();
		
	}
	
	
	public static void main(String[] args){
		importFromXLS("e:/CRIS/imports/ams/AMS.xlsx");
	}

}
