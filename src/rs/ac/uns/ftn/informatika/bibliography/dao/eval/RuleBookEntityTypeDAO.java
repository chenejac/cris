package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.RuleBookEntityTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookEntityTypeDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class RuleBookEntityTypeDAO {
	
	static {
		RuleBookEntityTypeDAO.log = LogFactory.getLog(RuleBookEntityTypeDAO.class.getName());
		RuleBookEntityTypeDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public List<RuleBookEntityTypeDTO> getAllRuleBookEntityTypes(String ruleBookControlNumber) {
		Connection conn = null;
		List<RuleBookEntityTypeDTO> retVal = new ArrayList<RuleBookEntityTypeDTO>();
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			retVal.addAll(storage.getAllRuleBookEntityTypes(conn, ruleBook).values());
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
	
	public List<RuleBookEntityTypeDTO> getAllRuleBookEntityTypes(RuleBookDTO ruleBook) {
		Connection conn = null;
		List<RuleBookEntityTypeDTO> retVal = new ArrayList<RuleBookEntityTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllRuleBookEntityTypes(conn, ruleBook).values());
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
	
	public RuleBookEntityTypeDTO getRuleBookEntityType(String ruleBookControlNumber, String classEntityType) {
		Connection conn = null;
		RuleBookEntityTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
			ClassDTO et = cerifDB.getClassDTO(conn, EntityTypesDAO.schemaId, classEntityType);
			retVal = storage.getRuleBookEntityType(conn, ruleBook, et);
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
	
	public RuleBookEntityTypeDTO getRuleBookEntityType(RuleBookDTO ruleBook, ClassDTO classEntityType) {
		Connection conn = null;
		RuleBookEntityTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getRuleBookEntityType(conn, ruleBook, classEntityType);
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
	 * Adds a new Rule Book Entity Type to the database.
	 * 
	 * @param RuleBookEntityTypeDTO
	 *            Rule Book Results type to add
	 * @return true if successful else false
	 */
	public boolean addRuleBookEntityType(RuleBookEntityTypeDTO ruleBookEntityType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addRuleBookEntityType(conn, ruleBookEntityType) == false) {
				retVal = false;
				conn.rollback();
			} else {
				conn.commit();
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
	 * Deletes a Rule Book Entity Type from the database.
	 * 
	 * @param RuleBookEntityTypeDTO
	 *            Rule Book Entity Type which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteRuleBookEntityType(RuleBookEntityTypeDTO ruleBookEntityType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteRuleBookEntityType(conn, ruleBookEntityType) == false) {
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
	
	protected RuleBookEntityTypeDB storage = new RuleBookEntityTypeDB();
	protected static DataSource dataSource;
	protected static Log log;
}
