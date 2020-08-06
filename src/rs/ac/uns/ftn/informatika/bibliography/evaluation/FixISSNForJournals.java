package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class FixISSNForJournals {

	public static Connection connCris;
	
	public static RecordDB recordDB = new RecordDB();
	public static RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	public static Log log = LogFactory.getLog(ExportJournalNameISSNDuplicates.class.getName());
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String luceneIndexPath = "";
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connCris = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			connCris.setAutoCommit(false);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		
		luceneIndexPath = rb.getString("luceneIndex");
		Retriever.setIndexPath(luceneIndexPath);
		
		System.out.println("Poceo");

		List<JournalDTO> retVal = new ArrayList<JournalDTO>();
		
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		
		for (Record recordDTO : listJournals) {
			try {
				recordDTO.loadFromDatabase();
				JournalDTO journalDTO = (JournalDTO) recordDTO.getDto();
				if (journalDTO != null /* && journalDTO.getControlNumber().equals("(BISIS)24457") */) {
					retVal.add(journalDTO);
				}
			} catch (Exception e) {
				log.error(e);
				System.exit(0);
			}
		}
		
//		Collections.sort(retVal, new GenericComparator<JournalDTO>(
//				"controlNumber", "asc"));
		
		int brojacCasEl = 0;
		
		System.out.println("Broj casopisa " + retVal.size());
		
		for (JournalDTO journalDTO : retVal) {
			brojacCasEl++;
			System.out.println(brojacCasEl + " id="+ journalDTO.getControlNumber() + " Casopis="+ journalDTO.getSomeName());
			String[] tokeni = FixISSNForJournals.fixISSN(journalDTO.getIssn());
			if(tokeni.length > 2){
				String ISSN = "";
				for (String stringTmp : tokeni) {
					ISSN+=stringTmp;
				}
				journalDTO.setIssn(ISSN);
			}
			
			//SNIMANJE JOURNALDTO IZMENJENOG U BAZU
			
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			RecordConsumerListener recordConsumer = new RecordConsumerListener();
			if ((recordDAO.update(new Record(null, null, "sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					journalDTO)) == false) || (recordConsumer.update(journalDTO) == false)) 
			{
				System.out.println("Neuspeli update "+ journalDTO.getControlNumber());
				System.exit(0);
			}
			else
			{
				System.out.println("UPDATE USPEO "+ journalDTO.getControlNumber());
			}
		}
		System.out.println("Zavrsio");
	}
	
	public static String[] fixISSN(String ISSN)
	{
		String[] tokens = ISSN.split(";");
		String[] ISSNs = new String [tokens.length];
		
		if(tokens.length>2){
			for (int i = 2; i < tokens.length; i++) {
				if (tokens[i].endsWith("(oldISSN)")) {
					 ISSNs[i] = " " + tokens[i].trim()+";";
				}
				else {
					System.out.println("Ne valja issn" + ISSN);
					System.exit(0);
				}
			}
		}
		if(tokens.length>1) {
			if (tokens[0].endsWith("(pISSN)")) {
				ISSNs[0] = " " + tokens[0].trim()+";";
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[0] = " " + tokens[0].trim()+";";
			}
			else {
				System.out.println("Ne valja issn" + ISSN);
				System.exit(0);
			}
			if (tokens[1].endsWith("(eISSN)")) {
				ISSNs[1] = " " + tokens[1].trim()+";";
			}
			else if (tokens[1].endsWith("(oldISSN)")) {
				ISSNs[1] = " " + tokens[1].trim()+";";
			}
			else if (ISSN.contains("ISBN")) {
				ISSNs[1] = " " + tokens[1].trim()+";";
			}
			else {
				System.out.println("Ne valja issn" + ISSN);
				System.exit(0);
			}
		}
		if (tokens.length==1) {
			ISSNs[0] = tokens[0].trim();
		}
		
		return ISSNs;
	}
}
