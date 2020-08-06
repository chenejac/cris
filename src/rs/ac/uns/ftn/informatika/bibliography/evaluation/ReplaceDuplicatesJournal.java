package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ReplaceDuplicatesJournal {
	public static Connection conn;
	 
	public static RecordDB recordDB = new RecordDB();
	
	private static RecordDAO recordDAO = new RecordDAO(new RecordDB());

	public static String importFolder;
	
	public static String exportFolder;
	
	public static HashMap<String, Integer> workbookHeaders = new HashMap<String, Integer>();
	
	public static int hederRowNumber = 0;
	
	public static Map<String, List<String>> duplicates = new HashMap<String, List<String>>();
	
	public static RecordConsumerListener recordConsumer = new RecordConsumerListener();
	
	
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

			int rowsCounter = hederRowNumber; //zbog zaglavlja
			
			String IDPRIME = null;
			
			//reading each row into JOURNALDTO object
			while (rows.hasNext ()){
				
				HSSFRow row = (HSSFRow) rows.next();
				rowsCounter++;
				
				//postavljanje svihmogucih vrednosti koje mogu da se ocitaju na null
				String ID = null;

				//citanje BYSIS() ID kolone
				HSSFCell cell = row.getCell(workbookHeaders.get("IDPRIME").intValue());
				if(cell == null)
				{
					System.out.println("Ocekuje se vrednost IDPRIME u "+ rowsCounter);
					System.exit(0);
				}
				else 
				{
					HSSFRichTextString richTextString = cell.getRichStringCellValue();
					if(richTextString.getString().equalsIgnoreCase("SKIP"))
					{
						continue;
					}
					else if(!(richTextString.getString().equalsIgnoreCase("DUPLIKAT")))
					{
						IDPRIME = richTextString.getString();
						continue;
					}	
				}
				
				//citanje BYSIS() ID kolone
				cell = row.getCell(workbookHeaders.get("ID").intValue());
				if(cell == null)
				{
					System.out.println("Ocekuje se vrednost ID u "+ rowsCounter);
					System.exit(0);
				}
				else 
				{
					HSSFRichTextString richTextString = cell.getRichStringCellValue();			
					ID = richTextString.getString();
					
					if(duplicates.containsKey(IDPRIME))
						duplicates.get(IDPRIME).add(ID);
					else
					{
						duplicates.put(IDPRIME, new ArrayList<String>());
						duplicates.get(IDPRIME).add(ID);
					}
				}
				
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static void loadAndDeleteDupliactes() {
		
		int brojOriginala  = duplicates.keySet().size();
		int doasoDoBroja=0;
		
		for (String IDPRIME : duplicates.keySet())
		{
			doasoDoBroja++;
//			Record journalPrime = recordDB.getRecord(conn, IDPRIME);
//			//ne postoji casopis za bazu
//			if(journalPrime==null)
//			{
//				System.out.println("Ne postoji casopis u bazi sa prime "+ IDPRIME);
//				System.exit(0);
//			}
//			journalPrime.loadFromDatabase();
//			
//			if(!(journalPrime.getDto() instanceof JournalDTO))
//			{
//				System.out.println("IDPRIME u bazi ne valja "+ IDPRIME + " to je " + journalPrime.getDto().getRecord().getType());
//				System.exit(0);
//			}
			
//			JournalDTO journalDTOPrime = (JournalDTO) journalPrime.getDto();
			
			JournalDTO journalDTOPrime = (JournalDTO) recordDAO.getDTO(IDPRIME);
			
			System.out.println(" ");
			System.out.println("Obradjuje glavni ************************************************** "+ IDPRIME + " dosao do broja "+ doasoDoBroja + " od "+ brojOriginala);
			
			for (String IDDupliacate : duplicates.get(IDPRIME))
			{

//				Record journalDuplicate = recordDB.getRecord(conn, IDDupliacate);
//
//				if(journalDuplicate==null)
//				{
//					System.out.println("Ne postoji casopis u bazi sa dupl "+ IDDupliacate);
//					System.exit(0);
//				}
//				journalDuplicate.loadFromDatabase();
//				
//				if(!(journalDuplicate.getDto() instanceof JournalDTO))
//				{
//					System.out.println("IDDupliacate u bazi ne valja "+ IDDupliacate + " to je " + journalDuplicate.getDto().getRecord().getType());
//					System.exit(0);
//				}
				
//				JournalDTO journalDTODuplicate = (JournalDTO) journalDuplicate.getDto();
				
				JournalDTO journalDTODuplicate = (JournalDTO) recordDAO.getDTO(IDDupliacate);
				replaceDuplicateJournals(journalDTOPrime, journalDTODuplicate);
			}
		}
		
	}
	
	public static void replaceDuplicateJournals(JournalDTO journalDTOPrime, JournalDTO journalDTODuplicate){
		try {
			Query query = new TermQuery(new Term("JOCN", journalDTODuplicate.getControlNumber()));
			List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
			for (Record record : list) {
				record.loadFromDatabase();
				
				System.out.print(".");
				
				if(record.getDto() instanceof PaperJournalDTO){
					PaperJournalDTO paperJournalDTO = (PaperJournalDTO)(record.getDto());
					paperJournalDTO.setJournal(journalDTOPrime);
					
					if((recordDAO.update(new Record(null, null, "sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							paperJournalDTO)) == false) || (recordConsumer.update(paperJournalDTO) == false)) {
						System.out.println("Neuspeli update rada u casopisu"+ paperJournalDTO.getControlNumber() + " sa casopisom "+journalDTOPrime.getControlNumber() + " ------------------------------------------------------------");
						System.exit(0);
						
					} else {
//						System.out.println("UPDATE USPEO RADA U CASPISU"+ paperJournalDTO.getControlNumber());
					}
				} else {
					System.out.println("Ocitan zapis u bazi nije casopis"+ record.getDto().getControlNumber() + " to je " + record.getDto().getRecord().getType());
					System.exit(0);
				}
			}
			
			if (recordDAO.delete(journalDTODuplicate.getRecord())==false)
			{
				System.out.println("Neuspelo brisanje duplikata --------------------------------------------------------"+ journalDTODuplicate.getControlNumber());
				System.exit(0);
			}
			else
			{
					System.out.println("Obrisao duplikat "+ journalDTODuplicate.getControlNumber());
			}
		} catch (Exception e) {
			System.out.println("Ima greska ");
			e.printStackTrace();
		}
	}
	
	private static void printDupliacted() {
		
		for (String IDPRIME : duplicates.keySet())
		{
			System.out.println("Original je : " + IDPRIME + "-------------------------------------------------");
			for (String ID : duplicates.get(IDPRIME))
			{
				System.out.println("Duplikat je : " + ID);
			}
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
			
			if(cellContent.equalsIgnoreCase("IDPRIME")){
				workbookHeaders.put("IDPRIME", new Integer(i));
			}
			else if(cellContent.equalsIgnoreCase("ID")){
				workbookHeaders.put("ID", new Integer(i));
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

		String  xlsDocument    = "/ISSNDuplicatesSurla.xls";
		ReplaceDuplicatesJournal.importFromExcel(xlsDocument);
		ReplaceDuplicatesJournal.loadAndDeleteDupliactes();

		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Zqavrsio sve");

	}
}
