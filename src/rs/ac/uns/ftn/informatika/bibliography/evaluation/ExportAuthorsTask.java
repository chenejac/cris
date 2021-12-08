package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.UserDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportAuthorsTask implements Task {

	public ExportAuthorsTask(Connection conn, PrintWriter out) {
		this.out = out;
		this.conn = conn;
	}

	@Override
	public boolean execute() {
		try {
			List<AuthorDTO> authors = collectAuthors();
			printAuthors(authors);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	public List<AuthorDTO> collectAuthors(){
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		PersonDB personDB = new PersonDB();
		UserDB userDB = new UserDB();
		List<Record> listAuthors = personDB.getInstitutionRecords(conn, "(BISIS)5929", "2021-01-01 00:00:00");
		// (BISIS)5929 - PMF
		// (BISIS)6782 - DMI
		// (BISIS)6780 - DGTH
		// (BISIS)6779 - DF
		// (BISIS)6781 - DHZZS
		// (BISIS)6778 - DBE
		
//		RecordDAO recordDAO = new RecordDAO(new PersonDB());
//		Query allAuthorsQuery = new TermQuery(new Term("TYPE", Types.AUTHOR));
//		List<Record> listAuthors = recordDAO.getDTOs(allAuthorsQuery, new AllDocCollector(false));
		for (Record recordDTO : listAuthors) {
			try {
//				TermQuery query = new TermQuery(new Term("CN", recordDTO.getControlNumber()));
//				List<Record> tempRecord = Retriever.select(query, new TopDocCollector(5));
//				AuthorDTO temp = (AuthorDTO)(tempRecord.get(0)).getDto();
//				if(userDB.getUserByAuthorControlNumber(conn, recordDTO.getControlNumber())!=null)
//				if(! temp.isAlreadyRegistered())
					retVal.add((AuthorDTO)recordDTO.getDto());
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
//		List<String> orderBy = new ArrayList<String>();
//		orderBy.add("name.lastname");
//		List<String> directions = new ArrayList<String>();
//		directions.add("asc");
//		Collections.sort(retVal, new GenericComparator<AuthorDTO>(
//				orderBy, directions));
		return retVal;
	}
	
	private void printAuthors(List<AuthorDTO> authors) {
		int i = 1;
		for (AuthorDTO author : authors) {
			System.out.println(author.getControlNumber());
			if(author.getControlNumber().equals("(BISIS)67252")) continue;
			author.getRecord().loadFromDatabase();
			String dateString = null;
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			if(author.getCurrentPosition().getStartDate() != null){
				Date date = author.getCurrentPosition().getStartDate().getTime();
				if(date != null)
					dateString = formatter.format(date);
			}
			author.getOrganizationUnit().setNotLoaded(true);
//			out.println(i++ + "," + Sanitizer.sanitizeCSV(author.getName().getLastname()) + "," + Sanitizer.sanitizeCSV(author.getName().getFirstname()) + "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.getCurrentPositionName()) + "," + dateString + "," +Sanitizer.sanitizeCSV(author.getOrganizationUnit().toString()));
//			out.println(i++ + "," + author.getControlNumber() + "," + author.getApvnt() +","+Sanitizer.sanitizeCSV(author.getName().toString()) + "," + Sanitizer.sanitizeCSV(author.getCurrentPositionName()) + "," + dateString + "," + Sanitizer.sanitizeCSV(author.getOrganizationUnit().getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.getOrganizationUnit().getSomeName()) + ",http://cris.uns.ac.rs/ReportsServlet/knr?reportType=knrXML&researcherId="+author.getControlNumber().substring(author.getControlNumber().indexOf(')')+1));
			out.println(
					Sanitizer.sanitizeCSV(author.getName().getLastname()) 
					+","+((author.getName().getOtherName().equals(author.getCurrentPositionName()))?"":Sanitizer.sanitizeCSV(author.getName().getOtherName())) 
					+","+Sanitizer.sanitizeCSV(author.getName().getFirstname()) 
					+ "," + author.getEmail() 
					+ "," 
					+","+((author.getKeywords().getContent()!=null)?Sanitizer.sanitizeCSV(author.getKeywords().getContent()).replaceAll("\\<.*?>","").replace("&nbsp;"," ").replace("&scaron;","š"):(""))
					+ "," + ((author.getResearchAreas().size() > 0)?(Sanitizer.sanitizeCSV(author.getResearchAreas().get(0).getTerm().getContent())):(""))
					+ "," + Sanitizer.sanitizeCSV(author.getCurrentPositionName()) 
					+ "," + author.getORCID()
					+ "," + author.getScopusID()
					+ "," + author.getControlNumber() 
					+ "," + ((author.getOrganizationUnit()!=null)?(Sanitizer.sanitizeCSV(author.getOrganizationUnit().toString())):"") 
					);			
			author.clear();
		}
		out.flush();
	}

	private PrintWriter out;
	
	private Connection conn;
	
	private static Log log = LogFactory.getLog(ExportAuthorsTask.class.getName());

}
