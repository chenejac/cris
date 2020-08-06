package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.ResultsTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.RuleBookResultsTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class RuleBookResultsTypeDAO {

	static {
		RuleBookResultsTypeDAO.log = LogFactory.getLog(RuleBookResultsTypeDAO.class.getName());
		RuleBookResultsTypeDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public List<RuleBookResultsTypeDTO> getAllRuleBookResultsTypes(String ruleBookControlNumber) {
		Connection conn = null;
		List<RuleBookResultsTypeDTO> retVal = new ArrayList<RuleBookResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			retVal.addAll(storage.getAllRuleBookResultsType(conn, ruleBook).values());
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
	
	public List<RuleBookResultsTypeDTO> getAllRuleBookResultsTypes(RuleBookDTO ruleBook) {
		Connection conn = null;
		List<RuleBookResultsTypeDTO> retVal = new ArrayList<RuleBookResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllRuleBookResultsType(conn, ruleBook).values());
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
	
	public RuleBookResultsTypeDTO getRuleBookResultsType(String ruleBookControlNumber, String classResultsType) {
		Connection conn = null;
		RuleBookResultsTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			ResultsTypeDB rtDB = new ResultsTypeDB();
			ResultsTypeDTO rt = rtDB.getResultsType(conn, classResultsType, false);
			retVal = storage.getRuleBookResultsType(conn, ruleBook, rt);
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
	
	public RuleBookResultsTypeDTO getRuleBookResultsType(RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		Connection conn = null;
		RuleBookResultsTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getRuleBookResultsType(conn, ruleBook, resultsType);
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
	 * Adds a new Rule Book Results type to the database.
	 * 
	 * @param RuleBookResultsTypeDTO
	 *            Rule Book Results type to add
	 * @return true if successful else false
	 */
	public boolean addRuleBookResultsType(RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addRuleBookResultsType(conn, ruleBookResultsType) == false) {
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
	 * Adds a new Rule Book Results type and it's and Results measures to the database.
	 * 
	 * @param RuleBookResultsTypeDTO
	 *            Rule Book Results type to add
	 * @return true if successful else false
	 */
	public boolean addRuleBookResultsTypeAndResultsTypeMeasures(RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addRuleBookResultsTypeAndResultsTypeMeasures(conn, ruleBookResultsType) == false) {
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
	 * Deletes a Rule Book Results type from the database.
	 * 
	 * @param RuleBookResultsTypeDTO
	 *            Rule Book Results type which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteRuleBookResultsType(RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteRuleBookResultsType(conn, ruleBookResultsType) == false) {
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
	 * Deletes a Rule Book Results type and it's Results measures from the database.
	 * 
	 * @param RuleBookResultsTypeDTO
	 *            Rule Book Results type which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteRuleBookResultsTypeAndDependacies(RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteRuleBookResultsTypeAndDependacies(conn, ruleBookResultsType) == false) {
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
	
	protected RuleBookResultsTypeDB storage = new RuleBookResultsTypeDB();
	protected static DataSource dataSource;
	protected static Log log;
}
