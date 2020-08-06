package rs.ac.uns.ftn.informatika.bibliography.evaluation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ExportJournalNameISSN {

	protected Connection conn;
	protected RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private static Log log = LogFactory.getLog(ExportJournalNameISSN.class.getName());
	
	// Well behaved document
	protected Workbook wb = new HSSFWorkbook();
	protected Sheet sheetJournalIF = wb.createSheet();
	
	public static HashMap<String, Integer> workbookHeaders = new HashMap<String, Integer>();
	public static int hederRowNumber = 0;
	
	/*****************************************************/
	public boolean setHeader()
	{
		CreationHelper createHelper = wb.getCreationHelper();
	    Row row=null;
	    
	    // Create a row and put some cells in it. Rows are 0 based.
	    row = sheetJournalIF.createRow(hederRowNumber);
	    
	    //CASOPIS
	    row.createCell(0).setCellValue(
		         createHelper.createRichTextString("ID"));
	    workbookHeaders.put("ID", new Integer(0));
	    
	    row.createCell(1).setCellValue(
		         createHelper.createRichTextString("Naziv"));
	    workbookHeaders.put("Naziv", new Integer(1));
	    
	    row.createCell(2).setCellValue(
		         createHelper.createRichTextString("Skraceni naziv"));
	    workbookHeaders.put("Skraceni naziv", new Integer(2));
	    
	    row.createCell(3).setCellValue(
		         createHelper.createRichTextString("ISSN"));
	    workbookHeaders.put("ISSN", new Integer(3));
	    
	    row.createCell(4).setCellValue(
		         createHelper.createRichTextString("URL"));
	    workbookHeaders.put("URL", new Integer(4));
	    
	    row.createCell(5).setCellValue(
		         createHelper.createRichTextString("IZMENA"));
	    workbookHeaders.put("IZMENA", new Integer(5));
	    
	    row.createCell(6).setCellValue(
		         createHelper.createRichTextString("KOMENTAR"));	
	    workbookHeaders.put("KOMENTAR", new Integer(6));
		
		return true;
	}
	
	/*****************************************************************/
	public boolean getAllJournals()
	{
		try {
			Map<String, JournalDTO> journals = collectJournalsBasic();
			printJournalsBasic(journals);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
		return true;
	}
	
	public Map<String, JournalDTO> collectJournalsBasic(){
		Map<String, JournalDTO> retVal = new HashMap<String, JournalDTO>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		for (int i=0;i<listJournals.size();i++) {
			try {
				JournalDTO jDTO = (JournalDTO) listJournals.get(i).getDto();
				if(retVal.containsKey(jDTO.getControlNumber()))
				{
					System.out.println("Kontrolni broj se ponavlja");
					System.exit(0);
				}
				retVal.put(jDTO.getControlNumber(), jDTO);
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		return retVal;
	}
	private void printJournalsBasic(Map<String, JournalDTO> journals) {
		
		int rowCounter = hederRowNumber;
		for (JournalDTO journalDTO : journals.values()){
			rowCounter++;
			journalDTO.getRecord().loadFromDatabase();
			ubaciRedJournalBasic(journalDTO,rowCounter);
		}
	}
	public boolean ubaciRedJournalBasic(JournalDTO journal, int rowNumber)
	{
		//CASOPIS
	    int idID = workbookHeaders.get("ID").intValue();
	    int idNaziv = workbookHeaders.get("Naziv").intValue();
	    int idSkraceniNaziv = workbookHeaders.get("Skraceni naziv").intValue();
	    int idISSN = workbookHeaders.get("ISSN").intValue();
	    int idURL = workbookHeaders.get("URL").intValue();
	    int idIZMENA = workbookHeaders.get("IZMENA").intValue();
	    int idKOMENTAR = workbookHeaders.get("KOMENTAR").intValue();
	    
		CreationHelper createHelper = wb.getCreationHelper();
	    Row row=null;
	    row = sheetJournalIF.createRow(rowNumber);
	    
	    //CASOPIS
	    row.createCell(idID).setCellValue(createHelper.createRichTextString(journal.getControlNumber()));
	    row.createCell(idNaziv).setCellValue(createHelper.createRichTextString(journal.getSomeName()));
	    
	    row.createCell(idSkraceniNaziv).setCellValue(createHelper.createRichTextString(((journal.getNameAbbreviation().getContent()!=null)?(Sanitizer.sanitizeCSV(journal.getNameAbbreviation().getContent())):("nema"))));
	    row.createCell(idISSN).setCellValue(createHelper.createRichTextString(((journal.getIssn()!=null)?(Sanitizer.sanitizeCSV(journal.getIssn())):("nema"))));
	    row.createCell(idURL).setCellValue(createHelper.createRichTextString(((journal.getUri()!=null)?(Sanitizer.sanitizeCSV(journal.getUri())):("nema"))));
	    row.createCell(idIZMENA).setCellValue(createHelper.createRichTextString("nema"));
	    row.createCell(idKOMENTAR).setCellValue(createHelper.createRichTextString("nema"));
		return true;
	}
	
	/************************************************************/
	public boolean snimiUFajl(String fileName)
	{
		try {
		FileOutputStream fileOut = null;
		if(fileName==null)
		{
			System.out.println("Ime fajla je null");
			return false;
		}
		else
			fileOut = new FileOutputStream(fileName);
		
		wb.write(fileOut);
		fileOut.close();
		}
		catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			System.out.println("Greska pri kreiranju excel fajla");
			e2.printStackTrace();
			return false;
		}
	    catch (IOException e1) {
	    	System.out.println("Greska pri snimanju excel fajla");
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	    return true;
	}
	
	/**********************************************************/
	public static boolean checkInteger(String intValue)
	{
		try {
				Integer.parseInt(intValue);
			}
		catch (NumberFormatException e) {
				return false;
		}
		return true;
	}
	
	public static boolean checkFloat(String floatValue)
	{
		try {
				Float.parseFloat(floatValue);
			}
		catch (NumberFormatException e) {
				return false;
		}
		return true;
	}
		
	/**
	 * @return the wb
	 */
	public Workbook getWb() {
		return wb;
	}

	/**
	 * @param wb the wb to set
	 */
	public void setWb(Workbook wb) {
		this.wb = wb;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection conn = null;
		String luceneIndexPath = "";
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		String importFolder = rb.getString("importFolder");
		String exportFolder = rb.getString("exportFolder");

		System.out.println("Parametri preuzeti");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(false);
		} catch (Exception e) 
		{		
			
		}
		luceneIndexPath = rb.getString("luceneIndex");
		Retriever.setIndexPath(luceneIndexPath);

		ExportJournalNameISSN exportJournalNameISSN = new ExportJournalNameISSN();
		exportJournalNameISSN.conn = conn;
		
		System.out.println("i podeseni");
		
		if(!exportJournalNameISSN.setHeader())
		{
			System.out.println("Greska pri kreiranju hedera tabele");
			System.exit(0);
		}
		
		if(!exportJournalNameISSN.getAllJournals())
		{
			System.out.println("Greska pri izcitavanju casopisa");
			System.exit(0);
		}
		
		if(!exportJournalNameISSN.snimiUFajl(exportFolder+"/SviCasopisiPMFNajnovijeBaza.xlsx"))
		{
			System.out.println("Greska pri snimanju casopisa");
			System.exit(0);
		}
		
		System.out.println("Zavrsio snimanje u xml fajl");
	}

}
