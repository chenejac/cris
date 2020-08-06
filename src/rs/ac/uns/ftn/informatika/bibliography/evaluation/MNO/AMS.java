package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * @author Dragan Ivanovic, dragan.ivanovic@uns.ac.rs
 *
 */
public class AMS extends MNO{

	private AMS() {
		super();
		this.nameMNO = "AMS";
		this.yearsSpecial = new int [33];
		for(int i=0;i<33;i++)
			yearsSpecial[i] = 1981+i;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2013;
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/AMS.xlsx", special2013);
		for(int i=0;i<33;i++)
			this.specialJournalsAllYears.put(1981+i, special2013);
	}
	
	protected void importFromXLS(String path, HashMap<String, String> map){
		RecordDB recordDB = new RecordDB();
		
//		private static EvaluationDB evaluationDB = new EvaluationDB();
		
		RecordDAO recordDAO = new RecordDAO(recordDB);
		InputStream inputStream = null;		
		try{
			inputStream = new FileInputStream (path);
			XSSFWorkbook      workBook = new XSSFWorkbook(inputStream);
			XSSFSheet         sheet    = workBook.getSheetAt (0);
			Iterator<Row> rows     = sheet.rowIterator();
			rows.next();
			int ukupno = 0;
			while(rows.hasNext()){
				ukupno++;
				XSSFRow row = (XSSFRow) rows.next();
				XSSFCell cellISSN = row.getCell(0);
				String issn = cellISSN.getStringCellValue().trim();
				XSSFCell cellJournalAbb = row.getCell(1);
				String journalAbb = cellJournalAbb.getStringCellValue().trim();				
				
				//System.out.println("issn: "+issn+", journalAbb: "+journalAbb+", publisher: "+publisher+", start year: "+startYear);
				
				String recordId = checkISSNExists(issn);
				if(issn != null && issn.trim().length()!=0  && recordId == null){
					JournalDTO journal = new JournalDTO();
					 journal.setIssn(issn);
					 journal.setNameAbbreviation(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
					 journal.setName(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
	     /*				 
					 journal.setPublicationYear(startYear);
					 journal.setNote(new MultilingualContentDTO("Publisher:"+publisher, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
					 */
						
						if (recordDAO.add(new Record("importAMSExcel", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
								journal)) == false){
							System.out.println("Nije uspelo dodavanje casopisa "+journalAbb+", issn="+journal.getIssn());
							continue;
						}
						else {
							System.out.println("Uspelo dodavanje casopisa "+journalAbb+", issn="+journal.getIssn());
						}
				}
				map.put(issn, "M24");
				
			}
			
		
			
			
			
		}catch (Exception e){			
			e.printStackTrace ();
		}
	}
	
	private String checkISSNExists(String issn){
		if(issn == null || issn.trim().length()==0)
			return null;
		TermQuery termQuery = new TermQuery(new Term("ISSN", issn));
		List<Record> records = Retriever.select(termQuery, new AllDocCollector(false));
		if(records==null || records.size()==0) return null;
		else return records.get(0).getControlNumber();
		
	}
	
	public static AMS ams= null;
	
	public static AMS getAMS() {
		if(ams==null)
			ams = new AMS();
		return ams;
	}
	
//	public HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsWithBestValue (JournalEval journal, int startingYear) {
//		HashMap<Integer, JournalEvaluationResult> retVal = new HashMap<Integer, JournalEvaluationResult>();
//		
//		List<Integer> years = new ArrayList<Integer>();
//		for(int i = startingYear; i <= lastEvaluationYear; i++){
//			years.add(i);
//		}
//		Integer best = null;
//		
//		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
//			String m = null;
//			if(year >= yearsSpecial[0]) {
//				if(isSpecial(journal, year)){
//					m = getSpecial(journal, year);
//					int retrieved = Integer.parseInt(m.substring(1));										
//					if(best == null || best >= retrieved)
//						best = retrieved;
//				}
//			}
//			else {
//				if(best!=null)
//					m = "M"+best;
//			}
//			
//			if (m == null)
//				m = "M53";
//			if(m.startsWith("M24") && year < yearsSpecial[0])
//				m = "M24";
//			
//			retVal.put(year, new JournalEvaluationResult(m, journal, null, 100));
//		}
//		return retVal;
//	}
	
}
