package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

//special2009.put("XXXXXXXXX", "XXX"); 			//	XXXXXXXXX	XXXX

public abstract class MNO {

	
	protected String nameMNO = null;
	
	//prva je godina ya koju postoji specijalna lista
	//drugo je ISSN
	//trece je kategorizacija casopisa
	// 2009, "0567-8315", "M23"
	HashMap <Integer, HashMap <String, String>> specialJournalsAllYears = null;
	int [] yearsSpecial = null;
	int firstEvaluationYear = -1;
	int lastEvaluationYear = -1;
	
	public MNO() {
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		lastEvaluationYear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
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
				XSSFCell cellISSN = row.getCell(2);
				if((cellISSN == null) || (cellISSN.getStringCellValue() == null) || (cellISSN.getStringCellValue().trim().length() == 0))
					continue;
				String issn = cellISSN.getStringCellValue().trim();
				XSSFCell cellJournalAbb = row.getCell(1);
				String journalAbb = cellJournalAbb.getStringCellValue().trim();	
				XSSFCell cellEvaluation = row.getCell(3);
				String evaluation = cellEvaluation.getStringCellValue().trim();	
				if(evaluation != null){
					evaluation = LatCyrUtils.toLatin(evaluation);
					if(!evaluation.startsWith("M"))
						evaluation = "M" + evaluation;
				}
				//System.out.println("issn: "+issn+", journalAbb: "+journalAbb+", publisher: "+publisher+", start year: "+startYear);
				String recordId = checkISSNExists(issn);
				if(issn != null && issn.trim().length()!=0  && recordId == null){
					JournalDTO journal = new JournalDTO();
					 journal.setIssn(issn);
//					 journal.setNameAbbreviation(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
					 journal.setName(new MultilingualContentDTO(journalAbb, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
	     /*				 
					 journal.setPublicationYear(startYear);
					 journal.setNote(new MultilingualContentDTO("Publisher:"+publisher, MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
					 */
						
						if (recordDAO.add(new Record("importMNOExcel", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
								journal)) == false){
							System.out.println("Nije uspelo dodavanje casopisa "+journalAbb+", issn="+journal.getIssn());
							continue;
						}
						else {
							System.out.println("Uspelo dodavanje casopisa "+journalAbb+", issn="+journal.getIssn());
						}
				}
				map.put(issn, evaluation);
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

	public static String[] processISSN(String ISSN)
	{
		String[] tokens = ISSN.split(";");
		String[] ISSNs = new String [tokens.length];
		
		if(tokens.length>2){
			for (int i = 2; i < tokens.length; i++) {
				if (tokens[i].endsWith("(oldISSN)")) {
					 ISSNs[i] = tokens[i].substring(0, tokens[i].lastIndexOf("("));
				}
				else if (ISSN.contains("ISBN")) {
					ISSNs[i] = tokens[i];
				}
				else {
//					System.out.println("Ne valja issn" + ISSN);
//					System.exit(0);
					ISSNs = new String [0];
					ISSNs[0]="";
				}
			}
		}
		if(tokens.length>1) {
			if (tokens[0].endsWith("(pISSN)")) {
				ISSNs[0] = tokens[0].substring(0, tokens[0].lastIndexOf("("));
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[0] = tokens[0];
			}
			else {
				System.out.println("Ne valja issn" + ISSN);
				System.exit(0);
			}
			if (tokens[1].endsWith("(eISSN)")) {
				ISSNs[1] = tokens[1].substring(0, tokens[1].lastIndexOf("("));
			}
			else if (tokens[1].endsWith("(oldISSN)")) {
				ISSNs[1] = tokens[1].substring(0, tokens[1].lastIndexOf("("));
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[1] = tokens[1];
			}
			else {
//				System.out.println("Ne valja issn" + ISSN);
//				System.exit(0);
				ISSNs = new String [0];
				ISSNs[0]="";
			}
		}
		else if (tokens.length==1) {
			if (tokens[0].endsWith("(pISSN)")) {
				ISSNs[0] = tokens[0].substring(0, tokens[0].lastIndexOf("("));
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[0] = tokens[0];
			}
			else if (tokens[0].endsWith("(eISSN)")) {
				ISSNs[0] = tokens[0].substring(0, tokens[0].lastIndexOf("("));
			}
			else if (tokens[0].endsWith("(oldISSN)")) {
				ISSNs[0] = tokens[0].substring(0, tokens[0].lastIndexOf("("));
			}
			else{
				ISSNs[0] = tokens[0];
			}
		}
		
		return ISSNs;
	}
	
	public boolean isSpecial(JournalEval journal, Integer godina)
	{
		if(nameMNO==null || specialJournalsAllYears==null || yearsSpecial == null)
			return false;
		
		String journalISSN = journal.getISSN();
		String ISSNs[] = processISSN(journalISSN);
		
		if(godina == 0){
			for (int tempGodinaKey : specialJournalsAllYears.keySet()) {
				HashMap <String, String> tempJournals = specialJournalsAllYears.get(tempGodinaKey);
				if(tempJournals!=null && !tempJournals.isEmpty())
					for (String tempISSN : tempJournals.keySet()) {
						int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
						for (int i = 0; i < ISSNs.length; i++) {
							if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
								return true;
							}
						}
					}
			}
		}
		else {
			boolean found = false;
			for(int i = 0; i < yearsSpecial.length; i++) {
				if (godina == yearsSpecial[i]) {
					found  = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
			HashMap <String, String> tempJournals = specialJournalsAllYears.get(godina);
			if(tempJournals!=null && !tempJournals.isEmpty())
				for (String tempISSN : tempJournals.keySet()) {
					int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
					for (int i = 0; i < ISSNs.length; i++) {
						if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
							return true;
						}
					}
				}
			
		}
		return false;
	}
	
	public boolean isSpecial(String journalISSN, Integer godina)
	{
		if(nameMNO==null || specialJournalsAllYears==null || yearsSpecial == null || journalISSN == null)
			return false;
		
		String ISSNs[] = processISSN(journalISSN);
		
		if(godina == 0){
			for (int tempGodinaKey : specialJournalsAllYears.keySet()) {
				HashMap <String, String> tempJournals = specialJournalsAllYears.get(tempGodinaKey);
				if(tempJournals!=null && !tempJournals.isEmpty())
					for (String tempISSN : tempJournals.keySet()) {
						int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
						for (int i = 0; i < ISSNs.length; i++) {
							if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
								return true;
							}
						}
					}
			}
		}
		else {
			boolean found = false;
			for(int i = 0; i < yearsSpecial.length; i++) {
				if (godina == yearsSpecial[i]) {
					found  = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
			HashMap <String, String> tempJournals = specialJournalsAllYears.get(godina);
			if(tempJournals!=null && !tempJournals.isEmpty())
				for (String tempISSN : tempJournals.keySet()) {
					int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
					for (int i = 0; i < ISSNs.length; i++) {
						if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
							return true;
						}
					}
				}
			
		}
		return false;
	}
	
	public String getSpecial(String journalISSN, Integer godina)
	{
		String retVal = null;
		String ISSNs[] = processISSN(journalISSN);
		
		if (godina != 0){
			HashMap <String, String> tempJournals = specialJournalsAllYears.get(godina);
			if(tempJournals!=null && !tempJournals.isEmpty())
				for (String tempISSN : tempJournals.keySet()) {
					int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
					for (int i = 0; i < ISSNs.length; i++) {
						if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
							retVal = tempJournals.get(tempISSN);
							break;
						}
					}
				}
		}
		
		return retVal;
	}
	
	public String getSpecial(JournalEval journal, Integer godina)
	{
		String retVal = null;
		
		String journalISSN = journal.getISSN();
		String ISSNs[] = processISSN(journalISSN);
		
		if (godina != 0){
			HashMap <String, String> tempJournals = specialJournalsAllYears.get(godina);
			if(tempJournals!=null && !tempJournals.isEmpty())
				for (String tempISSN : tempJournals.keySet()) {
					int eval = Integer.parseInt(tempJournals.get(tempISSN).substring(1));
					for (int i = 0; i < ISSNs.length; i++) {
						if (ISSNs[i].equalsIgnoreCase(tempISSN) && eval>=24) {
							retVal = tempJournals.get(tempISSN);
							break;
						}
					}
				}
		}
		
		return retVal;
	}
	
	public HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsWithFirstSpecialYear(JournalEval journal, int startingYear) {
		
		HashMap<Integer, JournalEvaluationResult> retVal = new HashMap<Integer, JournalEvaluationResult>();
		
		List<Integer> years = new ArrayList<Integer>();
		for(int i = startingYear; i <= lastEvaluationYear; i++){
			years.add(i);
		}
		Integer bestThreeYears = null;
		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
			Integer best = null;
			String m = null;
			if(year >= yearsSpecial[0]) {
				if(isSpecial(journal, year)){
					m = getSpecial(journal, year);
					if((yearsSpecial[0] < 2011) && (year <= (yearsSpecial[0] + 2)))
						if(m != null){
							int retrieved = Integer.parseInt(m.substring(1));										
							if(bestThreeYears == null || bestThreeYears >= retrieved)
								bestThreeYears = retrieved;
						}	
				}
				if((m == null) && (year.intValue() == 2007)){
					m = getSpecial(journal, 2009);
					if(m != null){
						int retrieved = Integer.parseInt(m.substring(1));										
						if(best == null || best >= retrieved)
							best = retrieved;
					}
					m = getSpecial(journal, 2008);
					if(m != null){
						int retrieved = Integer.parseInt(m.substring(1));										
						if(best == null || best >= retrieved)
							best = retrieved;
					}
					m = getSpecial(journal, 2006);
					if(m != null){
						int retrieved = Integer.parseInt(m.substring(1));										
						if(best == null || best >= retrieved)
							best = retrieved;
					}
					if(best != null)
					m="M" + best;
				}
				
			}
			else {
				if(bestThreeYears != null){
					if(bestThreeYears.intValue() == 24)
						m = "M51";
					else
						m = "M" + bestThreeYears;
				}
			}
			if (m == null)
				m = "M53";

			int evaluation = 100;
			if("M21a".equals(m))
				continue;
//				evaluation = 1;
			else if("M21".equals(m))
				continue;
//				evaluation = 2;
			else if("M22".equals(m))
				continue;
//				evaluation = 3;
			else if("M23".equals(m))
				continue;
//				evaluation = 4;
			else if("M24".equals(m))
				evaluation = 5;
			else if("M51".equals(m))
				evaluation = 6;
			else if("M52".equals(m))
				evaluation = 7;
			else if("M53".equals(m))
				evaluation = 8;
			else if("M54".equals(m))
				evaluation = 9;
				
			retVal.put(year, new JournalEvaluationResult(m, journal, null, evaluation, true, true));
		}
		return retVal;
	}
	/*public HashMap<Integer, JournalEvaluationResult> getJournalEvaluationsWithBestValue (JournalEval journal, int startingYear) {
		HashMap<Integer, JournalEvaluationResult> retVal = new HashMap<Integer, JournalEvaluationResult>();
		
		List<Integer> years = new ArrayList<Integer>();
		for(int i = startingYear; i <= lastEvaluationYear; i++){
			years.add(i);
		}
		Integer best = null;
		
		for (Integer year=years.get(years.size()-1);year >= years.get(0);year--) {
			String m = null;
			if(year >= yearsSpecial[0]) {
				if(isSpecial(journal, year)){
					m = getSpecial(journal, year);
					int retrieved = Integer.parseInt(m.substring(1));										
					if(best == null || best >= retrieved)
						best = retrieved;
				}
			}
			else {
				if(best!=null)
					m = "M"+best;
			}
			
			if (m == null)
				m = "M53";
			if(m.startsWith("M24") && year < yearsSpecial[0])
				m = "M51";
			
			retVal.put(year, new JournalEvaluationResult(m, journal, null, 100));
		}
		return retVal;
	}*/

	public String getNameMNO() {
		return nameMNO;
	}

	public int[] getYearsSpecial() {
		return yearsSpecial;
	}
	
	
}
