package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.ResultsTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.ResultsTypeGroupDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ResultsTypeGroupDAO {

	static {
		ResultsTypeGroupDAO.log = LogFactory.getLog(ResultsTypeGroupDAO.class.getName());
		ResultsTypeGroupDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public void loadAllResultsTypesOfSpecificResultsTypeGroup(ResultsTypeGroupDTO resultsTypeGroup, boolean loadSuperResultsTypeAndResultsTypeGroup){
		Connection conn = null;
		try {
			resultsTypeGroup.getResultsTypes().clear();
			conn = dataSource.getConnection();
			ResultsTypeDB storageResultsTypeDB = new ResultsTypeDB();
			resultsTypeGroup.getResultsTypes().addAll(storageResultsTypeDB.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, resultsTypeGroup.getClassId(), loadSuperResultsTypeAndResultsTypeGroup));
			if(loadSuperResultsTypeAndResultsTypeGroup)
				resultsTypeGroup.arangeResultsTypes();
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
	
	public ResultsTypeGroupDTO getResultsTypeGroup(String classResultsTypeGroup, boolean loadResultTypes, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		Connection conn = null;
		ResultsTypeGroupDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsTypeGroup(conn, classResultsTypeGroup);
			if(loadResultTypes){
				ResultsTypeDB storageResultsTypeDB = new ResultsTypeDB();
				retVal.getResultsTypes().addAll(storageResultsTypeDB.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, classResultsTypeGroup, loadSuperResultsTypeAndResultsTypeGroup));
				if(loadSuperResultsTypeAndResultsTypeGroup)
					retVal.arangeResultsTypes();
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
	
	public ResultsTypeGroupDTO getResultsTypeGroup(String classResultsTypeGroup, boolean loadResultTypes, boolean loadSuperResultsTypeAndResultsTypeGroup, Map<String, ResultsTypeGroupDTO> resultsTypeGroups) {
		Connection conn = null;
		ResultsTypeGroupDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsTypeGroup(conn, classResultsTypeGroup, resultsTypeGroups);
			if(loadResultTypes){
				ResultsTypeDB storageResultsTypeDB = new ResultsTypeDB();
				retVal.getResultsTypes().addAll(storageResultsTypeDB.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, classResultsTypeGroup,loadSuperResultsTypeAndResultsTypeGroup));
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
	
	
	
	public List<ResultsTypeGroupDTO> getAllResultsTypeGroups(boolean loadResultTypes, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		Connection conn = null;
		List<ResultsTypeGroupDTO> retVal = new ArrayList<ResultsTypeGroupDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsTypeGroup(conn).values());
			if(loadResultTypes){
				ResultsTypeDB storageResultsTypeDB = new ResultsTypeDB();
				for (ResultsTypeGroupDTO resultsTypeGroupDTO : retVal) {
					resultsTypeGroupDTO.getResultsTypes().addAll(storageResultsTypeDB.getAllResultsTypesOfSpecificResultsTypeGroupID(conn, resultsTypeGroupDTO.getClassId(), loadSuperResultsTypeAndResultsTypeGroup));
					if(loadSuperResultsTypeAndResultsTypeGroup)
						resultsTypeGroupDTO.arangeResultsTypes();
				}
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
	
	/**
	 * Adds a new Results type group to the database.
	 * 
	 * @param ResultsTypeGroupDTO
	 *            Results type group to add
	 * @return true if successful else false
	 */
	public boolean addResultsTypeGroupDTO(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addClass(conn, resultsTypeGroup) == false) {
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
	 * Adds a new Results type group and it's Results types to the database.
	 * 
	 * @param ResultsTypeGroupDTO
	 *            Results type group to add
	 * @return true if successful else false
	 */
	public boolean addResultsTypeGroupDTOAndResultsTypes(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addClassAndRelatedClass(conn, resultsTypeGroup) == false) {
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
	 * Updates the Results type group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results type group to update
	 * @return true if successful else false
	 */
	public boolean updateResultsTypeGroupDTO(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.updateClass(conn, resultsTypeGroup) == false) {
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
	 * Updates the Results type group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results type group to update
	 * @return true if successful else false
	 */
	public boolean updateResultsTypeGroupDTOAndResultsTypes(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.updateClassAndRelatedClass(conn, resultsTypeGroup) == false) {
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
	 * Deletes a Results type group from the database.
	 * 
	 * @param ResultsTypeGroupDTO
	 *            Results type group which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteResultsTypeGroupDTO(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteClass(conn, resultsTypeGroup) == false) {
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
	 * Deletes a Results type group and it's Results types from the database.
	 * 
	 * @param ResultsTypeGroupDTO
	 *            Results type group which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteResultsTypeGroupDTOAndResultsTypes(ResultsTypeGroupDTO resultsTypeGroup) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteClassAndRelatedClass(conn, resultsTypeGroup) == false) {
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
	
	protected ResultsTypeGroupDB storage = new ResultsTypeGroupDB();
	protected static DataSource dataSource;
	protected static Log log;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ResultsTypeGroupDAO dao = new ResultsTypeGroupDAO();
//		List<ResultsTypeGroupDTO> lista = dao.getAllResultsTypeGroups(true, true);
//		
//		for (ResultsTypeGroupDTO rtg : lista) {
//
//			List<ResultsTypeDTO> listart = rtg.getResultsTypes();
//			
//			for (ResultsTypeDTO resultsTypeDTO : listart) {
//				System.out.println(resultsTypeDTO.toStringWithSupersResultsTypeGroupAndSuperResultsType());
//			}
//			
//			System.out.println("****************************************************");
//			rtg.arangeRandomResultsTypes();
//			for (ResultsTypeDTO resultsTypeDTO : rtg.getResultsTypes()) {
//				System.out.println(resultsTypeDTO.toStringWithSupersResultsTypeGroupAndSuperResultsType());
//			}
//			rtg.arangeResultsTypes(1,3,"NUMBER", "ASC");
//			System.out.println("--------------------------------------------------------------");
//			for (ResultsTypeDTO resultsTypeDTO : rtg.getResultsTypes()) {
//				System.out.println(resultsTypeDTO.toStringWithSupersResultsTypeGroupAndSuperResultsType());
//			}
//		}		
	}
}
