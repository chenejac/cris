package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;

/**
 * Class for persisting and retrieving data about research areas from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class ResearchAreaDAO{

	public ResearchAreaDAO() {
		
		dataSource = DataSourceFactory.getDataSource();
		
		if (researchAreas == null)
			fillResearchAreas();
	}

	
	public ResearchAreaDTO getResearchArea(String classId) {
//		if(ResearchAreas != null)
			return researchAreas.get(classId);
//		Connection conn = null;
//		ResearchAreaDTO retVal = null;
//		try {
//			conn = dataSource.getConnection();
//			retVal = researchAreaDB.getResearchArea(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classId, new HashMap<String, ResearchAreaDTO>());
//		} catch (SQLException ex) {
//			log.fatal(ex);
//		} finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				log.error(e);
//			}
//		}
//		return retVal;
	}
	
	/**
	 * @return The array of research areas;
	 */
	public List<ResearchAreaDTO> getResearchAreas(){
		return new ArrayList<ResearchAreaDTO>(researchAreas.values());
	}
	
	/**
	 * 
	 */
	private void fillResearchAreas() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			researchAreas = researchAreaDB.getAll(conn);
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

	/**
	 * Adds a new research area to the database.
	 * 
	 * @param researchAreaDTO
	 *            Research area to add
	 * @return true if successful else false
	 */
	public boolean add(ResearchAreaDTO researchAreaDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (researchAreaDB.addClass(conn, researchAreaDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				researchAreas.put(researchAreaDTO.getClassId(), researchAreaDTO);
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
	 * Updates the research area in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param research area
	 *            Research area to update
	 * @return true if successful else false
	 */
	public boolean update(ResearchAreaDTO researchAreaDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (researchAreaDB.updateClass(conn, researchAreaDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				researchAreas.put(researchAreaDTO.getClassId(), researchAreaDTO);
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
	 * Deletes a research area from the database.
	 * 
	 * @param researchAreaDTO
	 *            Research area which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(ResearchAreaDTO researchAreaDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (researchAreaDB.deleteClass(conn, researchAreaDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				researchAreas.remove(researchAreaDTO.getClassId());
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
	
	private DataSource dataSource;
	private ResearchAreaDB researchAreaDB = new ResearchAreaDB();
	
	private static Map<String, ResearchAreaDTO> researchAreas; 
	
	private static Log log = LogFactory.getLog(ResearchAreaDAO.class.getName());

}
