package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga.NumberPhDPerInstitution;

/**
 * Class for persisting and retrieving data about register.
 * 
 * @author chenejac@uns.ac.rs
 */
public class RegisterEntryDAO {
	
	static {
		RegisterEntryDAO.log = LogFactory.getLog(RegisterEntryDAO.class.getName());
		RegisterEntryDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public RegisterEntryDAO(RegisterEntryDB storage) {
		this.storage = storage;
	}

	public RegisterEntryDTO getRegisterEntryFromDatabase(StudyFinalDocumentDTO dissertation, boolean translateToCyrillic) {
		Connection conn = null;
		RegisterEntryDTO retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getRegisterEntry(conn, dissertation, translateToCyrillic);
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
	
	public List<RegisterEntryDTO> getAllFromDatabase() {
		Connection conn = null;
		List<RegisterEntryDTO> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getAllRegisterEntries(conn);
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
	 * Adds a new register entry to the database.
	 * 
	 * @param registerEntry
	 *            register entry to add
	 * @return true if successful else false
	 */
	public boolean add(RegisterEntryDTO registerEntry) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			if (storage.add(conn, registerEntry) == false) {
				retVal = false;
				conn.rollback();
			} else {
				if(registerEntry.isAuthorChanged()){
					RecordDAO personDAO = new RecordDAO(new PersonDB());
					registerEntry.getAuthor().getRecord().loadMARC21FromDTO();
					if (personDAO.update(conn, registerEntry.getAuthor().getRecord()) == false) {	
						retVal = false;
						conn.rollback();
					} else {
						if (personDAO.updateIndex(registerEntry.getAuthor().getRecord())){
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
	 * Updates the register entry in the database
	 * 
	 * @param register entry
	 *            register entry to update
	 * @return true if successful else false
	 */
	public boolean update(RegisterEntryDTO registerEntry) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			if (storage.addOrUpdate(conn, registerEntry) == false) {
				retVal = false;
				conn.rollback();
			} else {
				if(registerEntry.isAuthorChanged()){
					RecordDAO personDAO = new RecordDAO(new PersonDB());
					registerEntry.getAuthor().getRecord().loadMARC21FromDTO();
					if (personDAO.update(conn, registerEntry.getAuthor().getRecord()) == false) {	
						retVal = false;
						conn.rollback();
					} else {
						if (personDAO.updateIndex(registerEntry.getAuthor().getRecord())){
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
	 * Deletes a register entry from the database.
	 * 
	 * @param dissertation
	 *            dissertation register entry to delete
	 * @return true if successful else false
	 */
	public boolean delete(Record rec) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			if (storage.delete(conn, ((StudyFinalDocumentDTO) rec.getDto())) == false) {
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
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public List<RegisterEntryDTO> getRegisterEntriesWithoutId(String universityID, String institution){
		Connection conn = null;
		List<RegisterEntryDTO> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getRegisterEntriesWithoutId(conn, universityID, institution);
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
	
	public boolean saveRegisterEntryForPromotion(RegisterEntryDTO regEntry){
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			if (storage.saveRegisterEntryForPromotion(conn, regEntry) == false) {
				retVal = false;
				conn.rollback();
			}else {
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
		}finally {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
		}
		return retVal;
	}
	
	public boolean savePromotionDateForRegisterEntry(RegisterEntryDTO regEntry, Date promotionDate){		
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			if (storage.savePromotionDateForRegisterEntry(conn, regEntry, promotionDate) == false) {
				retVal = false;
				conn.rollback();
			}else {
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
		}finally {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
		}
		return retVal;
	}
	
	
	public List<RegisterEntryDTO> getPromotedInPeriod(Date dateFrom, Date dateTo, String universityId, String institution){
		Connection conn = null;
		List<RegisterEntryDTO> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getPromotedInPeriod(conn, dateFrom, dateTo, universityId, institution);
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
	
	public List<RegisterEntryDTO> getPromotedForInstitution(String institution){
		Connection conn = null;
		List<RegisterEntryDTO> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getPromotedForInstitution(conn, institution);
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
	
	
	public List<NumberPhDPerInstitution> getNumberPhDPerIntitution(Date dateFrom, Date dateTo, String universityId){
		Connection conn = null;
		List<NumberPhDPerInstitution> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getNumberPhDPerIntitution(conn, dateFrom, dateTo, universityId);
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
	
	public List<String> getDistinctInstitutions(String universityId){
		Connection conn = null;
		List<String> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getDistinctInstitutions(conn, universityId);
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
	
	public List<RegisterEntryDTO> getRegisterEntriesForIdGeneration(String universityId){
		Connection conn = null;
		List<RegisterEntryDTO> retVal = null;
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getRegisterEntriesForIdGeneration(conn, universityId);
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
	
	
	public int getNextAvailableId(String universityId){
		Connection conn = null;
		int retVal = 0;	
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getNextAvailableId(conn, universityId);
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
	
	public int getNextAvailableAcademicYearNumber(String universityId, String academicYear){
		Connection conn = null;
		int retVal = 0;	
		try {
			conn = RegisterEntryDAO.dataSource.getConnection();
			retVal = storage.getNextAvailableAcademicYearNumber(conn, universityId, academicYear);
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
	
	
	protected static DataSource dataSource;
	
	protected RegisterEntryDB storage;

	protected static Log log;

}
