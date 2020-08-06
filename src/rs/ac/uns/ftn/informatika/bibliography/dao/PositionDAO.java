package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.PositionDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;

/**
 * Class for persisting and retrieving data about positions from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class PositionDAO {

	public PositionDAO() {
		
		dataSource = DataSourceFactory.getDataSource();
		
		if (Positions == null)
			fillPositions();
	}

	
	public PositionDTO getPosition(String classId) {
//		if(Positions != null)
			return Positions.get(classId);
//		Connection conn = null;
//		PositionDTO retVal = null;
//		try {
//			conn = dataSource.getConnection();
//			retVal = positionDB.getPosition(conn, PositionDTO.POSITION_SCHEMA, classId, new HashMap<String, PositionDTO>());
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
	public List<PositionDTO> getPositions(){
		return new ArrayList<PositionDTO>(Positions.values());
	}
	
	/**
	 * 
	 */
	private void fillPositions() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			Positions = positionDB.getAll(conn);
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
	 * Adds a new position to the database.
	 * 
	 * @param positionDTO
	 *            position to add
	 * @return true if successful else false
	 */
	public boolean add(PositionDTO positionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (positionDB.addClass(conn, positionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				Positions.put(positionDTO.getClassId(), positionDTO);
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
	 * Updates the position in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param positionDTO
	 *            position to update
	 * @return true if successful else false
	 */
	public boolean update(PositionDTO positionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (positionDB.updateClass(conn, positionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				Positions.put(positionDTO.getClassId(), positionDTO);
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
	 * Deletes the position from the database.
	 * 
	 * @param positionDTO
	 *            Position which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(PositionDTO positionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (positionDB.deleteClass(conn, positionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				Positions.remove(positionDTO.getClassId());
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
	private PositionDB positionDB = new PositionDB();
	
	private static Map<String, PositionDTO> Positions; 
	
	private static Log log = LogFactory.getLog(PositionDAO.class.getName());

}
