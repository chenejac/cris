//package rs.ac.uns.ftn.informatika.bibliography.evaluation;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.PropertyResourceBundle;
//import java.util.ResourceBundle;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.TermQuery;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//
//import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
//import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
//import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
//import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
//import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonJournal;
//import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
//import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
//import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
//import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;
//
///**
// * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
// *
// */
//public class CompareJournalsFromCrisAndKobson {
//
//	protected Connection connCris;
//	protected Connection connKobson;
//	
//	protected RecordDAO recordDAO = new RecordDAO(new RecordDB());
//	private static Log log = LogFactory.getLog(ExportJournalNameISSNDuplicates.class.getName());
//	
//	// Well behaved document
//	protected Workbook wb = new HSSFWorkbook();
//	protected Sheet sheetCompared = wb.createSheet("sheetCompared");
//	protected Sheet sheetKobsonFound = wb.createSheet("sheetKobsonFound");
//	protected Sheet sheetCrisFound = wb.createSheet("sheetCrisFound");
//	protected Sheet sheetCrisUnmapped = wb.createSheet("sheetCrisUnmapped");
//	
//	public static HashMap<String, Integer> workbookHeaders = new HashMap<String, Integer>();
//	public static int hederRowNumber = 0;
//	
//	//Map of Cris loaded key BysisID
//	public static Map<String, JournalDTO> crisJournalsID  = new HashMap<String, JournalDTO>();
//	//list of Cris loaded key ISSN
//	public static Map<String, List<JournalDTO>> crisJournalsISSN  = new HashMap<String, List<JournalDTO>>();
//	//list of kobson ISSN
//	public static List<String> kobsonISSN = null;
//	//list of unmapred cris
//	public static List<JournalDTO> crisJournalsUnmapped  = new ArrayList<JournalDTO>();
//	
//	/*****************************************************/
//	public boolean setHeader()
//	{
//		CreationHelper createHelper = wb.getCreationHelper();
//	    Row row=null;
//	    
//	    // sheetKobsonFound
//	    row = sheetKobsonFound.createRow(hederRowNumber);
//	    row.createCell(0).setCellValue(createHelper.createRichTextString("ID"));
//	    workbookHeaders.put("ID", new Integer(0));
//	    row.createCell(1).setCellValue(createHelper.createRichTextString("ISSN"));
//	    workbookHeaders.put("ISSN", new Integer(1));
//	    row.createCell(2).setCellValue(createHelper.createRichTextString("Naziv"));
//	    workbookHeaders.put("Naziv", new Integer(2));
//
//	    
//	    // sheetCrisFound
//	    row = sheetCrisFound.createRow(hederRowNumber);
//	    row.createCell(0).setCellValue(createHelper.createRichTextString("ID"));
//	    row.createCell(1).setCellValue(createHelper.createRichTextString("ISSN"));
//	    row.createCell(2).setCellValue(createHelper.createRichTextString("Naziv"));
//	    
//	    
//	    // sheetCrisUnmapped
//	    row = sheetCrisUnmapped.createRow(hederRowNumber);
//	    row.createCell(0).setCellValue(createHelper.createRichTextString("ID"));
//	    row.createCell(1).setCellValue(createHelper.createRichTextString("ISSN"));
//	    row.createCell(2).setCellValue(createHelper.createRichTextString("Naziv"));
//	    
//		return true;
//	}
//	
//	/******************************************************************/
//	public void collectCrisJournals(){
//		
//		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
//		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
//
//		System.out.println(listJournals.size());
//		
//		int brojac = 0;
//		while (listJournals.size()>0) {
//			try {
//				brojac++;
//
//				Record jRecord = listJournals.get(0);
//				JournalDTO jDTO = (JournalDTO) jRecord.getDto();
//				
//				String ISSN = jDTO.getIssn();
//				if(ISSN.startsWith("NN") || ISSN.startsWith("ISBN"))
//				{
//					crisJournalsUnmapped.add(jDTO);
//					listJournals.remove(jRecord);
//					continue;
//				}
//				
//				crisJournalsID.put(jDTO.getControlNumber(), jDTO);
//				String[] tokens = ISSN.split(";");
//				
//				if(tokens.length==2)
//				{
//					if (tokens[0].endsWith("(pISSN)"))
//					{
//						ISSN = tokens[0];
//						ISSN = ISSN.substring(0, ISSN.lastIndexOf("("));
//					}
//					else
//					{
//						System.out.println("Ne valja issn za casopis id "+ jDTO.getControlNumber() + " issn "+ ISSN);
//						System.exit(0);
//					}
//				}
//				else if (tokens.length>2)
//				{
//					System.out.println("Ne valja issn za casopis id "+ jDTO.getControlNumber()+ " issn "+ ISSN);
//					System.exit(0);
//				}
//
//				if(!crisJournalsISSN.containsKey(ISSN))
//					crisJournalsISSN.put(ISSN, new ArrayList<JournalDTO>());
//				
//				crisJournalsISSN.get(ISSN).add(jDTO);
//				
//				listJournals.remove(jRecord);
//				
//			} catch (Exception e) {
//				log.error(e);
//				System.out.println("Greska pri izcitavanju casopisa iz CRIS baze");
//				e.printStackTrace();
//				System.exit(0);
//			}
//		}
//	}
//	private void storeCrisJournalsUnmapped() {
//		int rowCounter = hederRowNumber;
//		
//		int idID = workbookHeaders.get("ID").intValue();
//	    int idNaziv = workbookHeaders.get("Naziv").intValue();
//	    int idISSN = workbookHeaders.get("ISSN").intValue();
//	    
//		for (JournalDTO journalDTO : crisJournalsUnmapped){
//				rowCounter++;
//				CreationHelper createHelper = wb.getCreationHelper();
//				Row row=null;
//			    row = sheetCrisUnmapped.createRow(rowCounter);
//			    
//			    row.createCell(idID).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));
//			    row.createCell(idISSN).setCellValue(createHelper.createRichTextString(((journalDTO.getIssn()!=null)?(Sanitizer.sanitizeCSV(journalDTO.getIssn())):("nema"))));
//			    row.createCell(idNaziv).setCellValue(createHelper.createRichTextString(journalDTO.getSomeName()));
//
//		}
//	}
//	
//	private void compareCrisWithKobson() {
//		int rowCounter = -1;
//		int elementCounter = 0;
//		KobsonJournal jouranlKobson = new KobsonJournal();
//		CreationHelper createHelper = wb.getCreationHelper();
//		Row row=null;
//		int idID = workbookHeaders.get("ID").intValue();
//	    int idNaziv = workbookHeaders.get("Naziv").intValue();
//	    int idISSN = workbookHeaders.get("ISSN").intValue();
//	    
//		for (JournalDTO journalDTO : crisJournalsID.values()){
//				
//				String crisISSN = journalDTO.getIssn();
//				String[] tokens = crisISSN.split(";");
//				if(tokens.length==2)
//				{
//					crisISSN = tokens[0];
//					crisISSN = crisISSN.substring(0, crisISSN.lastIndexOf("("));
//				}
//				if(!jouranlKobson.loadFromDatabase(connKobson, -1, crisISSN))
//				{
//					crisJournalsUnmapped.add(journalDTO);
//					continue;
//				}
//				elementCounter++;
//				System.out.println(elementCounter);
//				
//				///PRVA STRANICA
//				
//				//prvi red
//				rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue(createHelper.createRichTextString("RED.BR."));
//			    row.createCell(1).setCellValue(elementCounter);
//			    row.createCell(2).setCellValue(createHelper.createRichTextString(crisISSN));
//			    
//			    //prvi red
//				rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue(createHelper.createRichTextString("KOBSON"));
//			    row.createCell(1).setCellValue(jouranlKobson.getId());
//			    row.createCell(2).setCellValue(createHelper.createRichTextString(jouranlKobson.getNaslov()));
//			    row.createCell(3).setCellValue(createHelper.createRichTextString(jouranlKobson.getSkraceniNaslov()));
//			    row.createCell(4).setCellValue(createHelper.createRichTextString(jouranlKobson.getJezik()));
//			    row.createCell(5).setCellValue(createHelper.createRichTextString(jouranlKobson.getURL()));
//			    row.createCell(6).setCellValue(createHelper.createRichTextString(jouranlKobson.getISSN()));
//			    
//			    //drugi red
//				rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue(createHelper.createRichTextString("CRIS"));
//			    row.createCell(1).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));
//			    row.createCell(2).setCellValue(createHelper.createRichTextString(journalDTO.getSomeName()));
//			    row.createCell(3).setCellValue(createHelper.createRichTextString(journalDTO.getNameAbbreviation().getContent()));
//			    row.createCell(4).setCellValue(createHelper.createRichTextString(journalDTO.getNameAbbreviation().getLanguage()));
//			    row.createCell(5).setCellValue(createHelper.createRichTextString(journalDTO.getUri()));
//			    row.createCell(6).setCellValue(createHelper.createRichTextString(journalDTO.getIssn()));
//			    
//			    //treci red
//				rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue(createHelper.createRichTextString("ODLUKA K za K, C za Cobbson"));
//
//			    rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue("");
//			    rowCounter++;
//			    row = sheetCompared.createRow(rowCounter);
//			    row.createCell(0).setCellValue("");
//			    
//			    ///DRUGA STRANICA
//			    
//			    row = sheetKobsonFound.createRow(elementCounter+hederRowNumber);
//			    row.createCell(idID).setCellValue(jouranlKobson.getId());
//			    row.createCell(idISSN).setCellValue(createHelper.createRichTextString(jouranlKobson.getISSN()));
//			    row.createCell(idNaziv).setCellValue(createHelper.createRichTextString(jouranlKobson.getNaslov()));
//			    			    
//			    
//			    ///TRECA STRANICA
//			    
//			    row = sheetCrisFound.createRow(elementCounter+hederRowNumber);
//			    row.createCell(idID).setCellValue(createHelper.createRichTextString(journalDTO.getControlNumber()));
//			    row.createCell(idISSN).setCellValue(createHelper.createRichTextString(journalDTO.getIssn()));
//			    row.createCell(idNaziv).setCellValue(createHelper.createRichTextString(journalDTO.getSomeName()));
//		}
//	}
//	
//	
//	/************************************************************/
//	public boolean snimiUFajl(String fileName)
//	{
//		try {
//		FileOutputStream fileOut = null;
//		if(fileName==null)
//		{
//			System.out.println("Ime fajla je null");
//			return false;
//		}
//		else
//			fileOut = new FileOutputStream(fileName);
//		
//		wb.write(fileOut);
//		fileOut.close();
//		}
//		catch (FileNotFoundException e2) {
//			// TODO Auto-generated catch block
//			System.out.println("Greska pri kreiranju excel fajla");
//			e2.printStackTrace();
//			return false;
//		}
//	    catch (IOException e1) {
//	    	System.out.println("Greska pri snimanju excel fajla");
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return false;
//		}
//	    return true;
//	}
//
//	/**
//	 * @return the wb
//	 */
//	public Workbook getWb() {
//		return wb;
//	}
//
//	/**
//	 * @param wb the wb to set
//	 */
//	public void setWb(Workbook wb) {
//		this.wb = wb;
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		Connection connCris = null;
//		Connection connKobson = null;
//		
//		String luceneIndexPath = "";
//		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
//		String hostname = rb.getString("hostname");
//		String port = rb.getString("port");
//		String schema = rb.getString("schema");
//		String connectionParameters = rb.getString("connectionParameters");
//		String username = rb.getString("username");
//		String password = rb.getString("password");
//		
//		String exportFolder = rb.getString("exportFolder");
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connCris = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
//				+ "/" + schema + connectionParameters, username, password);
//			connCris.setAutoCommit(false);
//		} catch (Exception e) 
//		{		
//			e.printStackTrace();
//		}
//		
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connKobson = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
//				+ "/" + "kobson" + connectionParameters, username, password);
//			connKobson.setAutoCommit(false);
//		} catch (Exception e) 
//		{		
//			e.printStackTrace();
//		}
//		
//		luceneIndexPath = rb.getString("luceneIndex");
//		Retriever.setIndexPath(luceneIndexPath);
//
//		CompareJournalsFromCrisAndKobson compareJournalsFromCrisAndKobson = new CompareJournalsFromCrisAndKobson();
//		compareJournalsFromCrisAndKobson.connCris = connCris;
//		compareJournalsFromCrisAndKobson.connKobson = connKobson;
//		
//		System.out.println("Poceo");
//		
//		if(!compareJournalsFromCrisAndKobson.setHeader())
//		{
//			System.out.println("Greska pri kreiranju hedera tabele");
//			System.exit(0);
//		}
//		compareJournalsFromCrisAndKobson.collectCrisJournals();
//		
//		
//		compareJournalsFromCrisAndKobson.kobsonISSN = KobsonJournal.loadJournalsISSNFromDatabase(connKobson);
//		compareJournalsFromCrisAndKobson.compareCrisWithKobson();
//		compareJournalsFromCrisAndKobson.storeCrisJournalsUnmapped();
//		
//		if(!compareJournalsFromCrisAndKobson.snimiUFajl(exportFolder+"/ISSNCompareJournalsFromCrisAndKobson.xls"))
//		{
//			System.out.println("Greska pri snimanju casopisa");
//			System.exit(0);
//		}
//		
//		try {
//			if (connCris != null) {
//				connCris.close();
//			}
//			
//			if (connKobson != null) {
//				connKobson.close();
//			}
//		} catch (Exception e) {
//
//		}
//		
//		
//		System.out.println("Zavrsio snimanje u xml fajl");
//	}
//
//
//}
