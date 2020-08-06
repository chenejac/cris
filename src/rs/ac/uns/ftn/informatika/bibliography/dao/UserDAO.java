package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.UserDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Class for persisting and retrieving data about users of systems from database
 * and lucene index.
 * 
 * @author chenejac@uns.ac.rs
 */
public class UserDAO {

	static {
		dataSource = DataSourceFactory.getDataSource();
	}
	
	public UserDAO() {
//		try {
//			Context ctx = new InitialContext();
//			Context envCtx = (Context) ctx.lookup("java:comp/env");
//			ConnectionFactory connectionFactory = (ConnectionFactory)
//			envCtx.lookup(InitializerServlet.getFactoryName());
//			javax.jms.Connection connection =
//			connectionFactory.createConnection();
//			session = connection.createSession(false,
//			Session.AUTO_ACKNOWLEDGE);
//			producer = session.createProducer(
//					(Destination)envCtx.lookup(InitializerServlet.getQueueUpdaterName()));
//		} catch (NamingException e) {
//			log.error("Constructor UserDAO: " + e);
//		} catch (JMSException e) {
//			log.error("Constructor UserDAO: " + e);
//		} 
	}
	
	/**
	 * @param email
	 *            The user email - username
	 * @param password
	 *            The user password
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByUsernameAndPassword(String email, String password) {
		Connection conn = null;
		UserDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = userDB.getUserByUsernameAndPassword(conn, email, password);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * @param email
	 *            The user email - username
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByUsername(String email) {
		Connection conn = null;
		UserDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = userDB.getUserByUsername(conn, email);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * @param controlNumber
	 *            The author control number
	 * @return The retrieved user; null if not found or an error occurred.
	 */
	public UserDTO getUserByAuthorControlNumber(String controlNumber) {
		Connection conn = null;
		UserDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = userDB.getUserByAuthorControlNumber(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * @param controlNumber
	 *            The author control number
	 * @return The retrieved fake user; null if an author not found or an error occurred.
	 */
	public UserDTO getFakeUserByAuthorControlNumber(String controlNumber) {
		Connection conn = null;
		UserDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = userDB.getFakeUserByAuthorControlNumber(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	/**
	 * @param conn
	 *            Database connection
	 * @param filter
	 *            The filter for retrieving
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @return The array of users;
	 */
	public List<UserDTO> getUsers(String filter, String orderBy,
			String direction) {
		Connection conn = null;
		List<UserDTO> retVal = new ArrayList<UserDTO>();
		try {
			conn = dataSource.getConnection();
			List<UserDTO> allUsers = userDB.getAll(conn);
			retVal = filterUsers(allUsers, filter);
			if ((orderBy != null) && (!"".equals(orderBy))) {
				if (direction != null)
					Collections.sort(retVal, new GenericComparator<UserDTO>(
							orderBy, direction));
			}
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	private List<UserDTO> filterUsers(List<UserDTO> allUsers, String like) {
		List<UserDTO> retVal = new ArrayList<UserDTO>();
		if ((like != null) && (!"".equals(like))) {
			for (UserDTO userDTO : allUsers) {
				boolean forAdding = false;
				if ((userDTO.getEmail() != null)
						&& (userDTO.getEmail().toLowerCase().contains(like
								.toLowerCase()))) {
					forAdding = true;
				} else if ((userDTO.getNote() != null)
						&& (userDTO.getNote().toLowerCase().contains(like
								.toLowerCase()))) {
					forAdding = true;
				} else if ((userDTO.getType() != null)
						&& (userDTO.getType().toLowerCase().contains(like
								.toLowerCase()))) {
					forAdding = true;
				} else if (userDTO.getAuthor() != null) {
					forAdding = filterUsers(userDTO.getAuthor(), like);
				}
				if (forAdding)
					retVal.add(userDTO);
			}
			return retVal;
		} else
			return allUsers;

	}

	private boolean filterUsers(AuthorDTO authorDTO, String like) {
		boolean retVal = false;
		if ((authorDTO.getControlNumber() != null)
				&& (authorDTO.getControlNumber().toLowerCase().contains(like
						.toLowerCase()))) {
			retVal = true;
		} else if (authorDTO.getName() != null) {
			retVal = filterUsers(authorDTO.getName(), like);
		} else if ((authorDTO.getOtherFormatNames() != null)
				&& (authorDTO.getOtherFormatNames().size() > 0)) {
			for (AuthorNameDTO authorNameDTO : authorDTO.getOtherFormatNames()) {
				retVal = filterUsers(authorNameDTO, like);
				if (retVal)
					break;
			}
		} else if ((authorDTO.getYearOfBirth() != null)
				&& (authorDTO.getYearOfBirth().toString().toLowerCase()
						.contains(like.toLowerCase()))) {
			retVal = true;
		} else if ((authorDTO.getInstitution() != null) && (authorDTO.getInstitution().getName().getContent() != null)
				&& (authorDTO.getInstitution().getName().getContent().toLowerCase().contains(like
						.toLowerCase()))) {
			retVal = true;
		}

		return retVal;
	}

	private boolean filterUsers(AuthorNameDTO authorNameDTO, String like) {
		boolean retVal = false;
		if ((authorNameDTO.getFirstname() != null)
				&& (authorNameDTO.getFirstname().toLowerCase().contains(like
						.toLowerCase()))) {
			retVal = true;
		} else if ((authorNameDTO.getLastname() != null)
				&& (authorNameDTO.getLastname().toLowerCase().contains(like
						.toLowerCase()))) {
			retVal = true;
		}
		return retVal;
	}

	/**
	 * @param activationCode
	 *            The activation code
	 * @return The activated user; null if not found or an error occurred.
	 */
	public UserDTO activateAccount(String activationCode) {
		Connection conn = null;
		UserDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = userDB.activateAccount(conn, activationCode);
			if(retVal!=null){
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = null;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Adds a new user to the database and updates lucene index of corresponding
	 * author.
	 * 
	 * @param userDTO
	 *            User to add
	 * @return true if successful else false
	 */
	public boolean add(UserDTO userDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if ((userDTO.getAuthor()!=null) && (userDTO.getAuthor().getControlNumber()!=null)) {
				if(userDTO.getAuthor().getControlNumber().equals("(TEMP)0")){
					if (recordDAO.add(conn, new Person(userDTO.getEmail(), 
							new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.PERSON,
							userDTO.getAuthor(), userDTO.getAuthor().getJmbg(), userDTO.getAuthor().getDirectPhones(), userDTO.getAuthor().getLocalPhones(),userDTO.getAuthor().getApvnt() )) == false) {
						retVal = false;
						conn.rollback();
					} else {
						Record rec = new Person();
						rec.setType("pers");
						rec.setDto(userDTO.getAuthor());
						rec.loadMARC21FromDTO();
						if (! recordDAO.addIndex(rec)){
							retVal = false;
							conn.rollback();
						}
					}
				} else {
					Record rec = new Person(null, null, userDTO.getEmail(), 
							new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON,
							userDTO.getAuthor());
					if (! recordDAO.update(rec)){
						retVal = false;
						conn.rollback();
					}
				}
			} 
			if(retVal == true){
				if (userDB.add(conn, userDTO) == false) {
						retVal = false;
						conn.rollback();
					} else {
						conn.commit();
					}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

//	/**
//	 * Registers a new user to the database and updates lucene index of
//	 * corresponding author.
//	 * 
//	 * @param userDTO
//	 *            User to register
//	 * @return true if successful else false
//	 */
//	public boolean register(UserDTO userDTO) {
//		boolean retVal = true;
//		Connection conn = null;
//		try {
//			conn = dataSource.getConnection();
//			if (userDB.add(conn, userDTO) == false) {
//				retVal = false;
//				conn.rollback();
//			} else {
//				if ((userDTO.getAuthor() != null)
//						&& (userDTO.getAuthor().getControlNumber() != null)) {
//					if (recordDAO.update(conn, new RecordDTO(null, null,
//							userDTO.getEmail(), new GregorianCalendar(),
//							userDTO.getAuthor())) == false) {
//						retVal = false;
//						conn.rollback();
//					} else {
//						RecordDTO rec = new RecordDTO(userDTO.getAuthor());
//						if (recordDAO.registerAuthorUpdateIndex(rec))
//							conn.commit();
//						else
//							conn.rollback();
//					}
//				} else {
//					conn.commit();
//				}
//			}
//		} catch (SQLException ex) {
//			log.fatal(ex);
//			retVal = false;
//			if (conn != null) {
//				try {
//					conn.rollback();
//				} catch (SQLException e) {
//					log.fatal(e);
//				}
//			}
//		} catch (Exception e) {
//			log.fatal(e);
//			retVal = false;
//			if (conn != null) {
//				try {
//					conn.rollback();
//				} catch (SQLException ex) {
//					log.fatal(ex);
//				}
//			}
//		} finally {
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					log.fatal(e);
//				}
//			}
//		}
//		return retVal;
//	}

	/**
	 * Change password in the database.
	 * 
	 * @param userDTO
	 *            User which password should be changed
	 * @return true if successful else false
	 */
	public boolean changePassword(UserDTO userDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (userDB.changePassword(conn, userDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
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
	public boolean update(UserDTO userDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (userDB.update(conn, userDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				if ((userDTO.getAuthor() != null)
						&& (userDTO.getAuthor().getControlNumber() != null)) {
					Record rec = new Person(null, null,
							userDTO.getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
							userDTO.getAuthor(), userDTO.getAuthor().getJmbg(), userDTO.getAuthor().getDirectPhones(), userDTO.getAuthor().getLocalPhones(), userDTO.getAuthor().getApvnt());
					if (recordDAO.update(conn, rec) == false) {	
						retVal = false;
						conn.rollback();
					} else {
						if (recordDAO.updateIndex(rec)){
							conn.commit();
						}
						else {
							retVal = false;
							conn.rollback();
						}
					}
				} else {
					conn.commit();
				}

			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

	/**
	 * Deletes a user from the database.
	 * 
	 * @param userDTO
	 *            User which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(UserDTO userDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (userDB.delete(conn, userDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

//	protected Session session = null;
	
//	protected MessageProducer producer = null;
	
	protected static DataSource dataSource;
	
	private UserDB userDB = new UserDB();
	private RecordDAO recordDAO = new RecordDAO(new PersonDB());

	private static Log log = LogFactory.getLog(UserDAO.class.getName());

}
