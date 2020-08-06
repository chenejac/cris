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
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
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
public class ImportConferenceProceedingsFromKNRTask implements Task {

	public ImportConferenceProceedingsFromKNRTask(Connection source) {
		this.source = source;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.backup.Task#execute()
	 */
	@Override
	public boolean execute() {
		boolean retVal = true;
		try {
			PreparedStatement pstmt1 = source
				.prepareStatement("update CONFERENCEPROCEEDINGS set CONTROLNUMBER=? where DIFFORDNUM=? or ID=?");
			PreparedStatement pstmt2 = source
				.prepareStatement("update CONFERENCEPROCEEDINGS set STATE=? where ID=?");
			StringBuffer query = new StringBuffer();
			query
					.append("select ID, DIFFORDNUM, CONFERENCENAME, CONFERENCEYEAR, CONFERENCENUMBER, CONFERENCEPLACE, PROCEEDINGSTITLE, PROCEEDINGSPUBLICATIONYEAR, ISBN, PUBLISHERNAME, PUBLISHERPLACE, PUBLISHERSTATE, CONTROLNUMBER, CONFERENCEABBREVNAME, PROCEEDINGSABBREVTITLE, CONFERENCEKEYWORDS, PROCEEDINGSKEYWORDS, CONFERENCEURL, PROCEEDINGSURL, LANGUAGE from CONFERENCEPROCEEDINGS where STATE in (1) and CONTROLNUMBER not like '(BISIS)%'");
			Statement stmt = source.createStatement();
			ResultSet rset = stmt.executeQuery(query.toString());
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			while (rset.next()) {
				
				String conferenceName = rset.getString(3);
				System.out.println("Conference name: " + conferenceName);
				ConferenceDTO conference = null; 
				ProceedingsDTO proceedings = null;
				String proceedingsTitle = rset.getString(7);
				String controlNumber = rset.getString(13);
				String lang = getLanguage(rset.getString(20));
				if((controlNumber.startsWith("(BISIS)"))){
					System.out.println(controlNumber);
					proceedings = (ProceedingsDTO)recordDAO.getDTO(controlNumber);
					conference = proceedings.getConference();
				} else {
					BooleanQuery bq1 = new BooleanQuery();
					bq1.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
					bq1.add(QueryUtils.makeBooleanQuery("NA", LatCyrUtils.toLatinUnaccented(conferenceName.toLowerCase()), Occur.SHOULD, 0.9f, 0.9f, false), Occur.MUST);
					List<Record> list = recordDAO.getDTOs(bq1, new AllDocCollector(true));
					boolean similarConferenceExists = false;
					for (Record record : list) {
						ConferenceDTO temp = (ConferenceDTO)record.getDto();
						if((temp.getYear().equals(rset.getInt(4))) && (temp.getPlace().equals(rset.getString(6)))){
							similarConferenceExists = true;
							break;
						}
					}
//					if(similarConferenceExists){
//						System.out.println(conferenceName);
//						continue;						
//					}	
					proceedings = new ProceedingsDTO();
					conference = new ConferenceDTO();
					proceedings.setConference(conference);
				}
//				proceedings.setControlNumber(controlNumber);
//				conference.setControlNumber("(BISIS)4561");
				Integer conferenceYear = rset.getInt(4);
				String conferenceNumber = rset.getString(5);
				String conferencePlace = rset.getString(6);
//				String conferenceAbbrevName = rset.getString(14);
				String conferenceKeywords = rset.getString(16);
				String conferenceURL = rset.getString(18);
				conference.getName().setContent(conferenceName);
				conference.getName().setLanguage(lang);
				conference.setYear(conferenceYear);
				conference.setNumber(conferenceNumber);
				conference.setPlace(conferencePlace);
				if((conferenceKeywords != null) && (conferenceKeywords.trim().length() > 0))
					conference.getKeywords().setContent(conferenceKeywords);
				conference.setUri(conferenceURL);
//				conference.getAbbrev().setContent(conferenceName);
				Integer proceedingsPublicationYear = rset.getInt(8);
				String isbn = rset.getString(9);
				String publisherName = rset.getString(10);
				String publisherPlace = rset.getString(11);
				String publisherState = rset.getString(12);
				String abbrevName = rset.getString(15);
				String keywords = rset.getString(17);
				String URL = rset.getString(19);
				proceedings.setConference(conference);
				if((proceedingsTitle == null) || (proceedingsTitle.trim().length() == 0))
					continue;
				proceedings.getTitle().setContent(proceedingsTitle);
				proceedings.getTitle().setLanguage(lang);
				proceedings.setPublicationYear(proceedingsPublicationYear.toString());
				proceedings.setIsbn(isbn);
				if((abbrevName != null) && (abbrevName.trim().length() > 0))
					proceedings.getNameAbbreviation().setContent(abbrevName);
				if((keywords != null) && (keywords.trim().length() > 0))
					proceedings.getKeywords().setContent(keywords);
				proceedings.setUri(URL);
				PublisherNameDTO publisher = new PublisherNameDTO(publisherName, publisherPlace, publisherState, "srp", "o");
				proceedings.getPublisher().setOriginalPublisher(publisher);
				if((controlNumber.startsWith("(BISIS)"))){
					if ((recordDAO.update(new Record(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.EVENT, 
							conference)) == false) || 
							(recordDAO.update(new Record(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									proceedings)) == false)){
						retVal = false;
						break;
					} else {
						System.out.println("ID: " + rset.getInt(1));
						pstmt2.setInt(2, rset.getInt(1));
						pstmt2.setInt(1, new Integer(4));
						System.out.println("Number of changed rows: " + pstmt2.executeUpdate());
					}
				} else {
					if ((recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.EVENT, 
							conference)) == false) || 
							(recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									proceedings)) == false)){
						retVal = false;
						break;
					} else {
						System.out.println("DIFFORDNUM: " + rset.getInt(2) + "; ControlNumber" + proceedings.getControlNumber());
						pstmt1.setInt(2, rset.getInt(1));
						pstmt1.setInt(3, rset.getInt(1));
						pstmt1.setString(1, proceedings.getControlNumber());
						System.out.println("Number of changed rows: " + pstmt1.executeUpdate());
						System.out.println("ID: " + rset.getInt(1));
						pstmt2.setInt(2, rset.getInt(1));
						pstmt2.setInt(1, new Integer(5));
						System.out.println("Number of changed rows: " + pstmt2.executeUpdate());
					}
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read multiple users");
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
	
	private static Log log = LogFactory.getLog(ImportConferenceProceedingsFromKNRTask.class.getName());

}
