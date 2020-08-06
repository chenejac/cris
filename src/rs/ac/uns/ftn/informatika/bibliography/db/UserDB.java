package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * Class for persist and retrieve data about users of systems from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class UserDB {


	/**
	 * Retrieves a user.
	 * 
	 * @param conn
	 *            Database connection
	 * @param email
	 *            The user email - username
	 * @param password
	 *            The user password
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByUsernameAndPassword(Connection conn, String email,
			String password) {
		UserDTO user = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select TYPE, NOTE, LANGUAGE, RECORDID, INSTITUTIONID, ORGANIZATIONUNITID from USER where EMAIL like '"
							+ email + "' and PASSWORD like binary '" + password + "' and ACTIVATIONCODE like 'ACTIVATED'");

			if (rset.next()) {
				String type = rset.getString(1);
				String note = rset.getString(2);
				String language = rset.getString(3);
				String recordControlNumber = rset.getString(4);
				AuthorDTO author = new AuthorDTO();
				if ((recordControlNumber != null)
						&& (recordControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					Record record = recordDAO.getRecord(recordControlNumber);
					if(record != null){
						author = (AuthorDTO) record.getDto();
					} else
						return null;
				}
				String institutionControlNumber = rset.getString(5);
				InstitutionDTO institution = new InstitutionDTO();
				if ((institutionControlNumber != null)
						&& (institutionControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(institutionControlNumber);
					if(record != null){
						institution = (InstitutionDTO) record.getDto();
					} else
						return null;
				}
				String organizationUnitControlNumber = rset.getString(6);
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				if ((organizationUnitControlNumber != null)
						&& (organizationUnitControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(organizationUnitControlNumber);
					if(record != null){
						organizationUnit = (OrganizationUnitDTO) record.getDto();
					} else
						return null;
				}
				user = new UserDTO(email, password, note, language, author, institution, organizationUnit,
						type, "ACTIVATED");
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read mARC21Record with email: " + email
					+ " and password: " + password);
		}
		return user;
	}
	
	/**
	 * Retrieves a user.
	 * 
	 * @param conn
	 *            Database connection
	 * @param email
	 *            The user email - username
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByUsername(Connection conn, String email) {
		UserDTO user = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select PASSWORD, TYPE, NOTE, LANGUAGE, RECORDID, INSTITUTIONID, ORGANIZATIONUNITID, ACTIVATIONCODE from USER where EMAIL like '"
							+ email + "'");

			if (rset.next()) {
				String password = rset.getString(1);
				String type = rset.getString(2);
				String note = rset.getString(3);
				String language = rset.getString(4);
				String recordControlNumber = rset.getString(5);
				AuthorDTO author = new AuthorDTO();
				if ((recordControlNumber != null)
						&& (recordControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					Record record = recordDAO.getRecord(recordControlNumber);
					if(record != null){
						author = (AuthorDTO) record.getDto();
					} else
						return null;
				}
				String institutionControlNumber = rset.getString(6);
				InstitutionDTO institution = new InstitutionDTO();
				if ((institutionControlNumber != null)
						&& (institutionControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(institutionControlNumber);
					if(record != null){
						institution = (InstitutionDTO) record.getDto();
					} else
						return null;
				}
				String organizationUnitControlNumber = rset.getString(7);
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				if ((organizationUnitControlNumber != null)
						&& (organizationUnitControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(organizationUnitControlNumber);
					if(record != null){
						organizationUnit = (OrganizationUnitDTO) record.getDto();
					} else
						return null;
				}
				String activationCode = rset.getString(8);
				user = new UserDTO(email, password, note, language, author, institution, organizationUnit,
						type, activationCode);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read mARC21Record with email: " + email);
		}
		return user;
	}
	
	/**
	 * Retrieves a user.
	 * 
	 * @param conn
	 *            Database connection
	 * @param controlNumber
	 *            The author control number
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByAuthorControlNumber(Connection conn, String controlNumber) {
		UserDTO user = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select EMAIL, PASSWORD, TYPE, NOTE, LANGUAGE, INSTITUTIONID, ORGANIZATIONUNITID, ACTIVATIONCODE from USER where RECORDID like '"
							+ controlNumber + "'");

			if (rset.next()) {
				String email = rset.getString(1);
				String password = rset.getString(2);
				String type = rset.getString(3);
				String note = rset.getString(4);
				String language = rset.getString(5);
				AuthorDTO author = new AuthorDTO();
				if ((controlNumber != null)
						&& (controlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					Record record = recordDAO.getRecord(controlNumber);
					if(record != null){
						author = (AuthorDTO) record.getDto();
					} else
						return null;
				}
				String institutionControlNumber = rset.getString(6);
				InstitutionDTO institution = new InstitutionDTO();
				if ((institutionControlNumber != null)
						&& (institutionControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(institutionControlNumber);
					if(record != null){
						institution = (InstitutionDTO) record.getDto();
					} else
						return null;
				}
				String organizationUnitControlNumber = rset.getString(7);
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				if ((organizationUnitControlNumber != null)
						&& (organizationUnitControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(organizationUnitControlNumber);
					if(record != null){
						organizationUnit = (OrganizationUnitDTO) record.getDto();
					} else
						return null;
				}
				String activationCode = rset.getString(8);
				user = new UserDTO(email, password, note, language, author, institution, organizationUnit,
						type, activationCode);
			} 
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read mARC21Record with control number: " + controlNumber);
		}
		return user;
	}
	
	/**
	 * Retrieves a user.
	 * 
	 * @param conn
	 *            Database connection
	 * @param controlNumber
	 *            The author control number
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getFakeUserByAuthorControlNumber(Connection conn, String controlNumber) {
		UserDTO user = null;
		try {
			
				String email = "email";
				String password = "password";
				String type = "author";
				String note = "note";
				String language = "sr";
				AuthorDTO author = new AuthorDTO();
				if ((controlNumber != null)
						&& (controlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					Record record = recordDAO.getRecord(controlNumber);
					if(record != null){
						author = (AuthorDTO) record.getDto();
					} else
						return null;
				}
				InstitutionDTO institution = new InstitutionDTO();
				
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				
				String activationCode = "not exist";
				user = new UserDTO(email, password, note, language, author, institution, organizationUnit,
						type, activationCode);

		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read mARC21Record with control number: " + controlNumber);
		}
		return user;
	}

	/**
	 * Retrieves an array of users.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The array of users;
	 */
	public List<UserDTO> getAll(Connection conn) {
		List<UserDTO> retVal = new ArrayList<UserDTO>();
		try {
			StringBuffer query = new StringBuffer();
			query
					.append("select EMAIL, PASSWORD, TYPE, NOTE, LANGUAGE, RECORDID, INSTITUTIONID, ORGANIZATIONUNITID, ACTIVATIONCODE from USER ");
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				String email = rset.getString(1);
				String password = rset.getString(2);
				String type = rset.getString(3);
				String note = rset.getString(4);
				String language = rset.getString(5);
				String recordControlNumber = rset.getString(6);
				AuthorDTO author = new AuthorDTO();
				if ((recordControlNumber != null)
						&& (recordControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					author = (AuthorDTO) recordDAO.getDTO(recordControlNumber);
					if(author == null){
						RecordDB recDB = new RecordDB();
						Record authorRec = recDB.getRecord(conn, recordControlNumber);
						if(authorRec != null){
							authorRec.loadDTOFromMARC21();
							author = (AuthorDTO)(authorRec.getDto());
						} else 
							continue;
					}
				}
				String institutionControlNumber = rset.getString(7);
				InstitutionDTO institution = new InstitutionDTO();
				if ((institutionControlNumber != null)
						&& (institutionControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(institutionControlNumber);
					if(record != null){
						institution = (InstitutionDTO) record.getDto();
					} else
						continue;
				}
				String organizationUnitControlNumber = rset.getString(8);
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				if ((organizationUnitControlNumber != null)
						&& (organizationUnitControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(organizationUnitControlNumber);
					if(record != null){
						organizationUnit = (OrganizationUnitDTO) record.getDto();
					} else
						continue;
				}
				String activationCode = rset.getString(9);
				UserDTO user = new UserDTO(email, password, note, language,
						author, institution, organizationUnit, type, activationCode);
				retVal.add(user);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read multiple users");
		}
		return null;
	}

	/**
	 * Adds a new user to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param user
	 *            User to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	public boolean add(Connection conn, UserDTO user){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into USER (EMAIL, PASSWORD, TYPE, NOTE, LANGUAGE, RECORDID, INSTITUTIONID, ORGANIZATIONUNITID, ACTIVATIONCODE) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getType());
			String note = user.getNote();
			if (note == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(4, note);
			stmt.setString(5, user.getLanguage());
			if ((user.getAuthor() == null)
					|| (user.getAuthor().getControlNumber() == null))
				stmt.setNull(6, Types.VARCHAR);
			else
				stmt.setString(6, user.getAuthor().getControlNumber());
			if ((user.getInstitution() == null)
					|| (user.getInstitution().getControlNumber() == null))
				stmt.setNull(7, Types.VARCHAR);
			else
				stmt.setString(7, user.getInstitution().getControlNumber());
			if ((user.getOrganizationUnit() == null)
					|| (user.getOrganizationUnit().getControlNumber() == null))
				stmt.setNull(8, Types.VARCHAR);
			else
				stmt.setString(8, user.getOrganizationUnit().getControlNumber());
			stmt.setString(9, user.getActivationCode());
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add user: " + user);
		}
		return false;
	}

	/**
	 * Change password in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param user
	 *            User to change password
	 * @return true if successful else false
	 */
	public boolean changePassword(Connection conn, UserDTO user) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update USER set PASSWORD=? where EMAIL=?");
			stmt.setString(1, user.getPassword());
			stmt.setString(2, user.getEmail());
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update user: " + user);
		}
		return false;
	}

	/**
	 * Updates the user in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param user
	 *            User to update
	 * @return true if successful else false
	 */
	public boolean update(Connection conn, UserDTO user) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update USER set PASSWORD=?, TYPE=?, NOTE=?, LANGUAGE=?, RECORDID=?, INSTITUTIONID=?, ORGANIZATIONUNITID=?, ACTIVATIONCODE=? where EMAIL=?");
			stmt.setString(1, user.getPassword());
			stmt.setString(2, user.getType());
			String note = user.getNote();
			if (note == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, note);
			stmt.setString(4, user.getLanguage());
			if ((user.getAuthor() == null)
					|| (user.getAuthor().getControlNumber() == null))
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, user.getAuthor().getControlNumber());
			if ((user.getInstitution() == null)
					|| (user.getInstitution().getControlNumber() == null))
				stmt.setNull(6, Types.VARCHAR);
			else
				stmt.setString(6, user.getInstitution().getControlNumber());
			if ((user.getOrganizationUnit() == null)
					|| (user.getOrganizationUnit().getControlNumber() == null))
				stmt.setNull(7, Types.VARCHAR);
			else
				stmt.setString(7, user.getOrganizationUnit().getControlNumber());
			stmt.setString(8, user.getActivationCode());
			stmt.setString(9, user.getEmail());
			if(stmt.executeUpdate()!=0){
				stmt.close();
				return true;
			} else {
				stmt.close();
				return false;
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update user: " + user);
		}
		return false;
	}
	
	/**
	 * Activates the user account.
	 * 
	 * @param conn
	 *            Database connection
	 * @param activationCode
	 *            activation Code
	 * @return activated UserDTO if successful else null
	 */
	public UserDTO activateAccount(Connection conn, String activationCode) {
		UserDTO user = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select TYPE, NOTE, LANGUAGE, RECORDID, INSTITUTIONID, ORGANIZATIONUNITID, EMAIL, PASSWORD from USER where ACTIVATIONCODE like '" + activationCode + "'");

			if (rset.next()) {
				String type = rset.getString(1);
				String note = rset.getString(2);
				String language = rset.getString(3);
				String recordControlNumber = rset.getString(4);
				AuthorDTO author = new AuthorDTO();
				if ((recordControlNumber != null)
						&& (recordControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new PersonDB());
					Record record = recordDAO.getRecord(recordControlNumber);
					if(record != null){
						author = (AuthorDTO) record.getDto();
					} else
						return null;
				}
				String institutionControlNumber = rset.getString(5);
				InstitutionDTO institution = new InstitutionDTO();
				if ((institutionControlNumber != null)
						&& (institutionControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(institutionControlNumber);
					if(record != null){
						institution = (InstitutionDTO) record.getDto();
					} else
						return null;
				}
				String organizationUnitControlNumber = rset.getString(6);
				OrganizationUnitDTO organizationUnit = new OrganizationUnitDTO();
				if ((organizationUnitControlNumber != null)
						&& (organizationUnitControlNumber.toLowerCase().startsWith("("
								+ RecordDB.ORGANIZATION.toLowerCase() + ")"))) {
					RecordDAO recordDAO = new RecordDAO(new RecordDB());
					Record record = recordDAO.getRecord(organizationUnitControlNumber);
					if(record != null){
						organizationUnit = (OrganizationUnitDTO) record.getDto();
					} else
						return null;
				}
				String email = rset.getString(7);
				String password = rset.getString(8);
				user = new UserDTO(email, password, note, language, author, institution, organizationUnit,
						type, "ACTIVATED");
			}
			rset.close();
			stmt.close();
			if(user != null)
				if(! update(conn, user))
					user = null;
		} catch (Exception ex) {
			log.fatal(ex);
			log.fatal("Cannot read mARC21Record with activation code: " + activationCode);
		}
		return user;
	}

	/**
	 * Deletes a user from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param user
	 *            User to delete
	 * @return true if successful
	 */
	public boolean delete(Connection conn, UserDTO user) {
		return delete(conn, user.getEmail());
	}

	/**
	 * Deletes a user from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param email
	 *            User email
	 * @return true if successful
	 */
	public boolean delete(Connection conn, String email) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from USER where EMAIL like '" + email + "'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete user with email: " + email);
		}
		return false;
	}

	private static Log log = LogFactory.getLog(UserDB.class.getName());
}
