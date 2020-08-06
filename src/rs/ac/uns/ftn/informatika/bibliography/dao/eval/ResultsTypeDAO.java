package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.ResultsTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ResultsTypeDAO {

	static {
		ResultsTypeDAO.log = LogFactory.getLog(ResultsTypeDAO.class.getName());
		ResultsTypeDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public void loadSuperResultsTypeAndSuperResultsGroup(ResultsTypeDTO resultsType) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			storage.loadResultsType(conn, resultsType);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	public void loadAllResultsTypesOfSpecificResultsTypeGroup(ResultsTypeGroupDTO resultsTypeGroup) {
		Connection conn = null;
		try {
			resultsTypeGroup.getResultsTypes().clear();
			conn = dataSource.getConnection();
			resultsTypeGroup.getResultsTypes().addAll(storage.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, resultsTypeGroup.getClassId(), true));
//			resultsTypeGroup.arangeResultsTypes();
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	public ResultsTypeDTO getResultsType(String classId, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		Connection conn = null;
		ResultsTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsType(conn, classId, loadSuperResultsTypeAndResultsTypeGroup);
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
	
	public ResultsTypeDTO getResultsType(String classId, boolean loadSuperResultsTypeAndResultsTypeGroup, List<ResultsTypeDTO> resultsTypes) {
		Connection conn = null;
		ResultsTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsType(conn, classId, loadSuperResultsTypeAndResultsTypeGroup, resultsTypes);
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
	
	public List<ResultsTypeDTO> getAllResultsTypes(boolean loadSuperResultsTypeAndResultsTypeGroup) {
		Connection conn = null;
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsType(conn, loadSuperResultsTypeAndResultsTypeGroup));
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
	
	public List<ResultsTypeDTO> getAllResultsTypesOfSpecificResultsTypeGroup(String classResultsTypeGroup, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		Connection conn = null;
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, classResultsTypeGroup, loadSuperResultsTypeAndResultsTypeGroup));
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
	 * Adds a new Results type to the database.
	 * 
	 * @param ResultsTypeDTO
	 *            Results type to add
	 * @return true if successful else false
	 */
	public boolean add(ResultsTypeDTO resultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addClass(conn, resultsType) == false) {
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
	 * Updates the Results type in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeDTO
	 *            Results type  to update
	 * @return true if successful else false
	 */
	public boolean update(ResultsTypeDTO resultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.updateClass(conn, resultsType) == false) {
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
	 * Deletes a Results type from the database.
	 * 
	 * @param ResultsTypeDTO
	 *            Results type which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteResultsTypeDTO(ResultsTypeDTO resultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteClass(conn, resultsType) == false) {
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
	 * Deletes a Results type from the database.
	 * 
	 * @param ResultsTypeDTO
	 *            Results type which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteResultsTypeDTOHierarchy(ResultsTypeDTO resultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteClassAndRelatedClass(conn, resultsType) == false) {
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
	
	
	protected static DataSource dataSource;
	protected ResultsTypeDB storage = new ResultsTypeDB();
	protected static Log log;
}
