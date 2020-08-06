/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportJournalsFromKNRTask implements Task {

	public ImportJournalsFromKNRTask(Connection source) {
		this.source = source;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.backup.Task#execute()
	 */
	@Override
	public boolean execute() {
		boolean retVal = true;
		try {
			StringBuffer query = new StringBuffer();
			query
					.append("select ADDOPTEDNAME, ISSN, EISSN, DIFFORDNUM, ID, CONTROLNUMBER, ABBREVNAME, KEYWORDS, LANG, URL from JOURNAL where STATE=1");
			Statement stmt = source.createStatement();
			PreparedStatement pstmt1 = source
				.prepareStatement("update JOURNAL set CONTROLNUMBER=? where DIFFORDNUM=? or ID=?" );
			PreparedStatement pstmt2 = source
				.prepareStatement("update JOURNAL set STATE=? where ID=?");
			ResultSet rset = stmt.executeQuery(query.toString());
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			while (rset.next()) {
				
				String name = rset.getString(1);
				String issn = rset.getString(2);
				String abbrevName = rset.getString(7);
				String keywords = rset.getString(8);
				String lang = getLanguage(rset.getString(9));
				System.out.println("Journal name: " + name);
//				JournalNormativeDTO journalNormative = null;
				String controlNumber = rset.getString(6);
				JournalDTO journal = null;
				if((controlNumber.startsWith("(BISIS)"))){
					journal = (JournalDTO)recordDAO.getDTO(controlNumber);
//					journalNormative = journal.getJournalNormative();
				} else {
					BooleanQuery bq1 = new BooleanQuery();
					bq1.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
					bq1.add(QueryUtils.makeBooleanQuery("NA", LatCyrUtils.toLatinUnaccented(name.toLowerCase()), Occur.SHOULD, 0.8f, 0.8f, false), Occur.MUST);
//					List<Record> list = recordDAO.getDTOs(bq1, new AllDocCollector(true));
//					if(list.size()>0){
//						System.out.println(name);
//						continue;
//					}
					journal = new JournalDTO();
//					journalNormative = new JournalNormativeDTO();
//					journal.setJournalNormative(journalNormative);
				}
				
				journal.getName().setContent(name);
				journal.getName().setLanguage(lang);
				journal.setIssn(issn);
				if((abbrevName != null) && (abbrevName.trim().length() > 0)){
					journal.getNameAbbreviation().setContent(abbrevName);
					journal.getNameAbbreviation().setLanguage(lang);
				}
				if((keywords != null) && (keywords.trim().length() > 0)){
					journal.getKeywords().setContent(keywords);
					journal.getKeywords().setLanguage(lang);
				}
				
				
				
				if(controlNumber.startsWith("(BISIS)")){
					if (recordDAO.update(new Record( null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									journal)) == false){
						retVal = false;
						break;
					} else {
						System.out.println("ID: " + rset.getInt(4));
						pstmt2.setInt(2, rset.getInt(5));
						pstmt2.setInt(1, new Integer(4));
						System.out.println("Number of changed rows: " + pstmt2.executeUpdate());
					}
				} else {
					
					if (recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									journal)) == false){
					
						retVal = false;
						break;
					} else {
						System.out.println("ORDNUM: " + rset.getInt(5) + "; ControlNumber" + journal.getControlNumber());
						pstmt1.setInt(2, rset.getInt(5));
						pstmt1.setInt(3, rset.getInt(5));
						pstmt1.setString(1, journal.getControlNumber());
						System.out.println("Number of changed rows: " + pstmt1.executeUpdate());
						System.out.println("ID: " + rset.getInt(5));
						pstmt2.setInt(2, rset.getInt(5));
						pstmt2.setInt(1, new Integer(4));
						System.out.println("Number of changed rows: " + pstmt2.executeUpdate());
					}
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read multiple journals");
			log.fatal(ex);
			return false;
		}
	}
	
	private String getLanguage(String lang) {
		String retVal = null;
		if(lang == null)
			return retVal;
		if(lang.equals("Engleski"))
			retVal = "eng";
		else if (lang.equals("Srpski"))
				retVal = "srp";
			else if (lang.equals("Francuski"))
					retVal = "fre";
				else if (lang.equals("Nemacki"))
						retVal = "ger";
					else if (lang.equals("Ruski"))
							retVal = "rus";
		if(lang.equals("Italijanski"))
			retVal = "ita";
		else if (lang.equals("Spanski"))
				retVal = "spa";
		else if (lang.equals("Madjarski"))
				retVal = "hun";
		else if (lang.equals("Slovacki"))
				retVal = "slo";
		if(retVal == null)
			System.out.println(lang);
		return retVal;
	}

	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportJournalsFromKNRTask.class.getName());

}
