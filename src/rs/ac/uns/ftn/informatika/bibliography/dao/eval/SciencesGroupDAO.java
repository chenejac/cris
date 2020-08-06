package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.SciencesGroupDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class SciencesGroupDAO {

	static {
		SciencesGroupDAO.log = LogFactory.getLog(SciencesGroupDAO.class.getName());
		SciencesGroupDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public SciencesGroupDTO getSciencesGroup(String classSciencesGroup) {
		Connection conn = null;
		SciencesGroupDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getSciencesGroup(conn, classSciencesGroup);
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
	
	public List<SciencesGroupDTO> getAllSciencesGroup() {
		Connection conn = null;
		List<SciencesGroupDTO> retVal = new ArrayList<SciencesGroupDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllSciencesGroup(conn).values());
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
	 * Adds a new Sciences Group to the database.
	 * 
	 * @param SciencesGroupDTO
	 *            Sciences Group to add
	 * @return true if successful else false
	 */
	public boolean add(SciencesGroupDTO sciencesGroupDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addClass(conn, sciencesGroupDTO) == false) {
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
	 * Updates the Sciences Group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param SciencesGroupDTO
	 *            Sciences Group  to update
	 * @return true if successful else false
	 */
	public boolean update(SciencesGroupDTO sciencesGroupDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.updateClass(conn, sciencesGroupDTO) == false) {
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
	 * Deletes a Sciences Group from the database.
	 * 
	 * @param SciencesGroupDTO
	 *            Sciences Group which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(SciencesGroupDTO sciencesGroupDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteClass(conn, sciencesGroupDTO) == false) {
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
	
	protected SciencesGroupDB storage = new SciencesGroupDB();
	protected static DataSource dataSource;
	protected static Log log;
}
