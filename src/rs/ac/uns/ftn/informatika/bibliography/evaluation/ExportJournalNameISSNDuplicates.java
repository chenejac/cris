package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ExportJournalNameISSNDuplicates {
	
	protected Connection conn;
	protected RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private static Log log = LogFactory.getLog(ExportJournalNameISSNDuplicates.class.getName());
	
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
		         createHelper.createRichTextString("IDPRIME"));
	    workbookHeaders.put("IDPRIME", new Integer(0));
	    
	    row.createCell(1).setCellValue(
		         createHelper.createRichTextString("ID"));
	    workbookHeaders.put("ID", new Integer(1));
	    
	    row.createCell(2).setCellValue(
		         createHelper.createRichTextString("Naziv"));
	    workbookHeaders.put("Naziv", new Integer(2));
	    
	    row.createCell(3).setCellValue(
		         createHelper.createRichTextString("Skraceni naziv"));
	    workbookHeaders.put("Skraceni naziv", new Integer(3));
	    
	    row.createCell(4).setCellValue(
		         createHelper.createRichTextString("ISSN"));
	    workbookHeaders.put("ISSN", new Integer(4));
	    
	    row.createCell(5).setCellValue(
		         createHelper.createRichTextString("KOMENTAR"));	
	    workbookHeaders.put("KOMENTAR", new Integer(5));
	    
		return true;
	}
	
	/*****************************************************************/
	public boolean getAllJournals(int version)
	{
		try {
			if(version==1)
			{
				Map<String, List<JournalDTO>> journals = collectJournalsISSNDuppliacates1();
				storeJournalsISSNDuppliacates1(journals);
				
			}
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
	
	/******************************************************************/
	public Map<String, List<JournalDTO>> collectJournalsISSNDuppliacates1(){
		
		Map<String, List<JournalDTO>> retVal = new HashMap<String, List<JournalDTO>>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));

		System.out.println(listJournals.size());
		
		int brojac = 0;
		while (listJournals.size()>0) {
			try {
				Record jRecord = listJournals.get(0);
				JournalDTO jDTO = (JournalDTO) jRecord.getDto();
				
				retVal.put(jDTO.getControlNumber(), new ArrayList<JournalDTO>());
				retVal.get(jDTO.getControlNumber()).add(jDTO);
				brojac++;
				
				List<Record> toBeRemoved = new ArrayList<Record>();
				toBeRemoved.add(jRecord);
				
				for(Record record : listJournals)
				{
					JournalDTO journalDTO = (JournalDTO) record.getDto();
					if(!jDTO.getControlNumber().equalsIgnoreCase(journalDTO.getControlNumber()))
					{
						if(jDTO.getIssn().equalsIgnoreCase(journalDTO.getIssn()))
						{
							retVal.get(jDTO.getControlNumber()).add(journalDTO);
							toBeRemoved.add(record);
						}
					}
				}
				
				for(Record record : toBeRemoved)
				{
					listJournals.remove(record);
					brojac++;
					System.out.println(brojac);
				}
				
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		return retVal;
	}
	private void storeJournalsISSNDuppliacates1(Map<String, List<JournalDTO>> journals) {
		int rowCounter = hederRowNumber;
		int idIDPrime = workbookHeaders.get("IDPRIME").intValue();
		int idID = workbookHeaders.get("ID").intValue();
	    int idNaziv = workbookHeaders.get("Naziv").intValue();
	    int idSkraceniNaziv = workbookHeaders.get("Skraceni naziv").intValue();
	    int idISSN = workbookHeaders.get("ISSN").intValue();
	    int idKOMENTAR = workbookHeaders.get("KOMENTAR").intValue();
		for (List<JournalDTO> journalDTODupliactes : journals.values()){
			int brojacElemenata = 0;
			for (JournalDTO journalDTO : journalDTODupliactes){
				rowCounter++;
				CreationHelper createHelper = wb.getCreationHelper();
				Row row=null;
			    row = sheetJournalIF.createRow(rowCounter);
			    
			    //row set casopis
			    if(brojacElemenata==0)
			    	row.createCell(idIDPrime).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));
			    else
			    	row.createCell(idIDPrime).setCellValue(createHelper.createRichTextString(""));
			    
			    row.createCell(idID).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));
			    row.createCell(idNaziv).setCellValue(createHelper.createRichTextString(journalDTO.getSomeName()));
			    
			    row.createCell(idSkraceniNaziv).setCellValue(createHelper.createRichTextString(((journalDTO.getNameAbbreviation().getContent()!=null)?(Sanitizer.sanitizeCSV(journalDTO.getNameAbbreviation().getContent())):("nema"))));
			    row.createCell(idISSN).setCellValue(createHelper.createRichTextString(((journalDTO.getIssn()!=null)?(Sanitizer.sanitizeCSV(journalDTO.getIssn())):("nema"))));
			    row.createCell(idKOMENTAR).setCellValue(createHelper.createRichTextString("nema"));
			    brojacElemenata++;
			} 
		}
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

		ExportJournalNameISSNDuplicates exportJournalNameISSNDupplicates = new ExportJournalNameISSNDuplicates();
		exportJournalNameISSNDupplicates.conn = conn;
		
		System.out.println("Poceo");
		
		if(!exportJournalNameISSNDupplicates.setHeader())
		{
			System.out.println("Greska pri kreiranju hedera tabele");
			System.exit(0);
		}
		
		if(!exportJournalNameISSNDupplicates.getAllJournals(1))
		{
			System.out.println("Greska pri izcitavanju casopisa");
			System.exit(0);
		}
		
		if(!exportJournalNameISSNDupplicates.snimiUFajl(exportFolder+"/ISSNDuplicates.xlsx"))
		{
			System.out.println("Greska pri snimanju casopisa");
			System.exit(0);
		}
		
		System.out.println("Zavrsio snimanje u xml fajl");
	}

}
