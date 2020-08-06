package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.UserDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportUsersTask implements Task {

	public ExportUsersTask(Connection conn, PrintWriter out) {
		this.out = out;
		this.conn = conn;
	}

	@Override
	public boolean execute() {
		try {
			List<UserDTO> users = collectUsers();
			printUsers(users);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	public List<UserDTO> collectUsers(){
		List<UserDTO> retVal = new ArrayList<UserDTO>();
		UserDAO userDAO = new UserDAO();
		retVal = userDAO.getUsers("author", "author.controlNumber", "asc");
		return retVal;
	}
	
	private void printUsers(List<UserDTO> users) {
		int i = 1;
		for (UserDTO user : users) {
			System.out.println(user.getAuthor().getControlNumber());
			AuthorDTO author = user.getAuthor();
//			author.getRecord().loadFromDatabase();
			String datePositionString = null;
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			if(author.getCurrentPosition().getStartDate() != null){
				Date date = author.getCurrentPosition().getStartDate().getTime();
				if(date != null)
					datePositionString = formatter.format(date);
			}
			String dateBirthString = null;
			if(author.getDateOfBirth() != null){
				Date date = author.getDateOfBirth().getTime();
				if(date != null)
					dateBirthString = formatter.format(date);
			}
			author.getOrganizationUnit().setNotLoaded(true);
			author.getOrganizationUnit().setLocale(new Locale("en"));
			author.getInstitution().setNotLoaded(true);
			author.getInstitution().setLocale(new Locale("en"));
			StringBuilder publications = new StringBuilder();
			for (RecordDTO recordDTO : author.getRelatedRecords()) {
				if(recordDTO instanceof PublicationDTO)
					publications.append("," + Sanitizer.sanitizeCSV(recordDTO.getStringRepresentation()));
			}
////			out.println(i++ + "," + Sanitizer.sanitizeCSV(author.getName().getLastname()) + "," + Sanitizer.sanitizeCSV(author.getName().getFirstname()) + "," + Sanitizer.sanitizeCSV(author.getControlNumber()) + "," + Sanitizer.sanitizeCSV(author.getCurrentPositionName()) + "," + dateString + "," +Sanitizer.sanitizeCSV(author.getOrganizationUnit().toString()));
			out.println(i++ + "," + author.getControlNumber() + "," + author.getSex() +","+Sanitizer.sanitizeCSV(author.getName().toString()) + "," + dateBirthString + "," + Sanitizer.sanitizeCSV(author.getCurrentPositionName()) + "," + datePositionString + "," + Sanitizer.sanitizeCSV(author.getTitle()) +"," + ((author.getOrganizationUnit().getControlNumber()!=null)?Sanitizer.sanitizeCSV(author.getOrganizationUnit().getControlNumber()):"") + "," + Sanitizer.sanitizeCSV(author.getOrganizationUnit().getSomeName())+"," + ((author.getInstitution().getControlNumber()!=null)?Sanitizer.sanitizeCSV(author.getInstitution().getControlNumber()):"") + "," + Sanitizer.sanitizeCSV(author.getInstitution().getSomeName())+"," + Sanitizer.sanitizeCSV(author.getSomeBiography())+"," + Sanitizer.sanitizeCSV(author.getSomeKeywords()) + publications);
			author.clear();
		}
		out.flush();
	}

	private PrintWriter out;
	
	private Connection conn;
	
	private static Log log = LogFactory.getLog(ExportAuthorsTask.class.getName());

}
