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
import rs.ac.uns.ftn.informatika.bibliography.db.eval.ResultsTypeMeasureDB;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.SciencesGroupDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ResultsTypeMeasureDAO {

	static {
		ResultsTypeMeasureDAO.log = LogFactory.getLog(ResultsTypeMeasureDAO.class.getName());
		ResultsTypeMeasureDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public List<ResultsTypeMeasureDTO> getAllResultsTypeMeasures(String ruleBookControlNumber, String classResultsType) {
		Connection conn = null;
		List<ResultsTypeMeasureDTO> retVal = new ArrayList<ResultsTypeMeasureDTO>();
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			ResultsTypeDB rtDB = new ResultsTypeDB();
			ResultsTypeDTO rt = rtDB.getResultsType(conn, classResultsType, false);
			retVal.addAll(storage.getAllResultsTypeMeasures(conn, ruleBook, rt).values());
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
	
	public List<ResultsTypeMeasureDTO> getAllResultsTypeMeasures(RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		Connection conn = null;
		List<ResultsTypeMeasureDTO> retVal = new ArrayList<ResultsTypeMeasureDTO>();
		try {
//			System.out.println("ResultsTypeMeasureDAO - getAllResultsTypeMeasures -1");
			conn = dataSource.getConnection();
//			System.out.println("ResultsTypeMeasureDAO - getAllResultsTypeMeasures -2");
			retVal.addAll(storage.getAllResultsTypeMeasures(conn, ruleBook, resultsType).values());
//			System.out.println("ResultsTypeMeasureDAO - getAllResultsTypeMeasures -3");
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
	
	public ResultsTypeMeasureDTO getResultsTypeMeasure(String ruleBookControlNumber, String classResultsType, String classSciencesGroup) {
		Connection conn = null;
		ResultsTypeMeasureDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			
			RecordDB rDB = new RecordDB();
			RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookControlNumber).getDto();
			
			ResultsTypeDB rtDB = new ResultsTypeDB();
			ResultsTypeDTO resultsType = rtDB.getResultsType(conn, classResultsType, false);
			
			SciencesGroupDB sciGroupDB = new SciencesGroupDB();
			SciencesGroupDTO sciencesGroup = sciGroupDB.getSciencesGroup(conn, classSciencesGroup);
			
			retVal = storage.getResultsTypeMeasure(conn, ruleBook, resultsType, sciencesGroup);
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
	
	public ResultsTypeMeasureDTO getResultsTypeMeasure(RuleBookDTO ruleBook, ResultsTypeDTO resultsType, SciencesGroupDTO sciencesGroup) {
		Connection conn = null;
		ResultsTypeMeasureDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsTypeMeasure(conn, ruleBook, resultsType, sciencesGroup);
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
	 * Adds a new Results measure to the database.
	 * 
	 * @param ResultsTypeMeasureDTO
	 *            Results measure type to add
	 * @return true if successful else false
	 */
	public boolean add(ResultsTypeMeasureDTO resultsTypeMeasure) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.add(conn, resultsTypeMeasure) == false) {
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
	 * Updates the Results measure in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeMeasureDTO
	 *            Results measure  to update
	 * @return true if successful else false
	 */
	public boolean update(ResultsTypeMeasureDTO resultsTypeMeasure) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.update(conn, resultsTypeMeasure) == false) {
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
	 * Deletes a Results measure from the database.
	 * 
	 * @param ResultsTypeMeasureDTO
	 *            Results measure which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(ResultsTypeMeasureDTO resultsTypeMeasure) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.delete(conn, resultsTypeMeasure) == false) {
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
	
	protected ResultsTypeMeasureDB storage = new ResultsTypeMeasureDB();
	protected static DataSource dataSource;
	protected static Log log;
}
