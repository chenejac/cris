/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportAuthorsFromKNRTask implements Task {

	private RecordDAO recordDAO = new RecordDAO(new PersonDB());
	
	public ImportAuthorsFromKNRTask(Connection source) {
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
			query.append("select AUID, FIRSTNAME, LASTNAME, YEAROFBIRTH, POSITION, TITLE, AFFILIATION, CONTROLNUMBER, DEPID, CHAIRID, JMBG, TLF, TLF2, BIOGRAPHY, KEYWORDS from AUTHOR");
			Statement stmt = source.createStatement();
			PreparedStatement pstmt = source
				.prepareStatement("update AUTHOR set CONTROLNUMBER=? where AUID=?");
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				
				AuthorDTO author = null;
				
				String controlNumber = rset.getString(8);
				if((controlNumber.startsWith("(BISIS)"))){
					author = (AuthorDTO)recordDAO.getDTO(controlNumber);
				} else {
					author = new AuthorDTO();
				// }
					List<AuthorNameDTO> allFormatNames = ImportAuthorsFromKNRTask.getAuthorNames(rset.getString(2), rset.getString(3));
					author.setName(allFormatNames.get(0));
					allFormatNames.remove(0);
					author.setOtherFormatNames(allFormatNames);
					author.setYearOfBirth(rset.getInt(4));
//					author.setPositionString(rset.getString(5));
					author.setTitle(rset.getString(6));
					author.setInstitution(loadInstitution(rset.getString(7)));
				}
				if(author==null){
					System.out.println(controlNumber);
					continue;
				}
				Integer depId = rset.getInt(9);
				Integer chairId = rset.getInt(10);
				if(chairId != null)
					author.setOrganizationUnit(loadOrganizationUnit(chairId));
				else if (depId != null)
					author.setOrganizationUnit(loadOrganizationUnit(depId));
				String jmbg = rset.getString(11);
				String directPhones = rset.getString(12);
				String localPhones = rset.getString(13);
				String biography = rset.getString(14);
				String keywords = rset.getString(15);
				if((biography!=null) && (biography.trim().length()>0)){
					author.getBiography().setContent(biography);
					author.getBiography().setLanguage("srp");
				}
				if((keywords!=null) && (keywords.trim().length()>0)){
					author.getKeywords().setContent(keywords);
					author.getKeywords().setLanguage("srp");
				}
				if((controlNumber.startsWith("(BISIS)"))){
					if (recordDAO.update(new Person(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
						author, jmbg, directPhones, localPhones, null)) == false) {
						retVal = false;
						System.out.println("Neuspesno!!!");
						break;
					} else {
						//System.out.println("NRID: " + rset.getInt(1) + "; ControlNumber" + author.getControlNumber());
//						pstmt.setInt(2, rset.getInt(1));
//						pstmt.setString(1, author.getControlNumber());
//						pstmt.executeUpdate();
//						System.out.println("Uspesno!!!");
						//System.out.println("Number of changed rows: " + pstmt.executeUpdate());
					}
				} else {
					if (recordDAO.add(new Person(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
							author, jmbg, directPhones, localPhones, null)) == false) {
							retVal = false;
							break;
						} else {
							//System.out.println("NRID: " + rset.getInt(1) + "; ControlNumber" + author.getControlNumber());
							pstmt.setInt(2, rset.getInt(1));
							pstmt.setString(1, author.getControlNumber());
							pstmt.executeUpdate();
							//System.out.println("Number of changed rows: " + pstmt.executeUpdate());
						}
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Throwable ex) {
			ex.printStackTrace();
			log.fatal("Cannot read multiple users");
			log.fatal(ex);
			return false;
		}
	}
	
	public static List<AuthorNameDTO> getAuthorNames(String firstName, String lastName){
		List<AuthorNameDTO> retVal = new ArrayList<AuthorNameDTO>();
		//System.out.println("Author: " + authorName);

		AuthorNameDTO authorName = new AuthorNameDTO(firstName, lastName, "");
		retVal.add(authorName);
		ImportAuthorsFromKNRTask.addCyrillicLatin(authorName, retVal);
		if(lastName.contains("-")){
			authorName = new AuthorNameDTO(firstName, lastName.split("\\-")[0], "");
			retVal.add(authorName);
			ImportAuthorsFromKNRTask.addCyrillicLatin(authorName, retVal);
		}
		if(lastName.contains("(")){
			String tempLastName = lastName.substring(lastName.indexOf("(")+1, lastName.indexOf(")"));
			if(firstName.contains("(")){
				String tempFirstName = firstName.substring(firstName.indexOf("(")+1, firstName.indexOf(")"));
				authorName = new AuthorNameDTO(tempFirstName, tempLastName, "");
				retVal.add(authorName);
				ImportAuthorsFromKNRTask.addCyrillicLatin(authorName, retVal);
			} else {
				String[] tempLastNames = tempLastName.split(",");
				for (String ln : tempLastNames) {
					authorName = new AuthorNameDTO(firstName, ln, "");
					retVal.add(authorName);
					ImportAuthorsFromKNRTask.addCyrillicLatin(authorName, retVal);
				}
			}
		}
		return retVal;
	}
	
	private static void addCyrillicLatin(AuthorNameDTO authorName, List<AuthorNameDTO> list){
		AuthorNameDTO cyrillic = new AuthorNameDTO(LatCyrUtils.toCyrillic(authorName.getFirstname()), LatCyrUtils.toCyrillic(authorName.getLastname()), "");
		AuthorNameDTO latin = new AuthorNameDTO(LatCyrUtils.toLatin(authorName.getFirstname()), LatCyrUtils.toLatin(authorName.getLastname()), "");
		AuthorNameDTO latinUnaccented = new AuthorNameDTO(LatCyrUtils.toLatinUnaccented(authorName.getFirstname()), LatCyrUtils.toLatinUnaccented(authorName.getLastname()), "");
		if(!authorName.equals(cyrillic))
			list.add(cyrillic);
		if(!authorName.equals(latin))
			list.add(latin);
		if(!authorName.equals(latinUnaccented))
			list.add(latinUnaccented);
	}
	
	public static boolean findAuthor(AuthorNameDTO authorNameDTO, String firstName, String lastName){
		List<AuthorNameDTO> formatNames = ImportAuthorsFromKNRTask.getAuthorNames(firstName, lastName);
		return ImportAuthorsFromKNRTask.findAuthor(authorNameDTO, formatNames);
	}
	
	public static boolean findAuthor(AuthorNameDTO authorNameDTO, List<AuthorNameDTO> formatNames){
		boolean retVal = false;
		AuthorNameDTO latinUnaccented = null;
		for (AuthorNameDTO formatName : formatNames) {
			if(authorNameDTO.getLastname().replaceAll("[\\.,:;]+", " ").trim().equalsIgnoreCase(formatName.getLastname().replaceAll("[\\.,:;]+", " ").trim())){
				String[] firstNames = authorNameDTO.getFirstname().split("\\.");
				for (String fn : firstNames) {
					if((fn == null) || (fn.trim().length()==0))
						continue;
					if(formatName.getFirstname().replaceAll("[\\.,:;]+", " ").trim().toLowerCase().startsWith(fn.replaceAll("[\\.,:;]+", "").toLowerCase().trim())){
						authorNameDTO.setFirstname(formatName.getFirstname());
						authorNameDTO.setLastname(formatName.getLastname());
						authorNameDTO.setOtherName(formatName.getOtherName());
						return true;
					}						
				}
			} 
			if(LatCyrUtils.toLatinUnaccented(authorNameDTO.getLastname()).replaceAll("[\\.,:;]+", " ").trim().equalsIgnoreCase(formatName.getLastname().replaceAll("[\\.,:;]+", " ").trim())){
				String[] firstNames = authorNameDTO.getFirstname().split("\\.");
				for (String fn : firstNames) {
					if((fn == null) || (fn.trim().length()==0))
						continue;
					if(LatCyrUtils.toLatinUnaccented(formatName.getFirstname()).replaceAll("[\\.,:;]+", " ").trim().toLowerCase().startsWith(fn.replaceAll("[\\.,:;]+", "").toLowerCase().trim())){
						latinUnaccented = formatName;
						break;
					}						
				}
			}
		}
		
		if (latinUnaccented != null){
			authorNameDTO.setFirstname(latinUnaccented.getFirstname());
			authorNameDTO.setLastname(latinUnaccented.getLastname());
			authorNameDTO.setOtherName(latinUnaccented.getOtherName());
			retVal = true;
		}
		return retVal;
	}
	
	private InstitutionDTO loadInstitution(String affilation) {
		InstitutionDTO retVal = new InstitutionDTO();
		try {
			if(affilation!=null){
				PreparedStatement pstmt = source
				.prepareStatement("select CONTROLNUMBER from INSTITUTION where NAME like ?");
				pstmt.setString(1, affilation);
				ResultSet rset = pstmt.executeQuery();
				RecordDAO recDAO = new RecordDAO(new RecordDB());
				if(rset.next())
					retVal = (InstitutionDTO) recDAO.getDTO(rset.getString(1));
			}
		} catch (Throwable e) {
			
		}
		return retVal;
	}
	
	private OrganizationUnitDTO loadOrganizationUnit(Integer id) {
		OrganizationUnitDTO retVal = new OrganizationUnitDTO();
		try {
			if(id!=null){
				PreparedStatement pstmt = source
				.prepareStatement("select CONTROLNUMBER from ORGANIZATIONUNIT where KNRID=?");
				pstmt.setInt(1, id);
				ResultSet rset = pstmt.executeQuery();
				RecordDAO recDAO = new RecordDAO(new RecordDB());
				if(rset.next())
					retVal = (OrganizationUnitDTO) recDAO.getDTO(rset.getString(1));
			}
		} catch (Throwable e) {
			
		}
		return retVal;
	}
	
	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportAuthorsFromKNRTask.class.getName());

}
