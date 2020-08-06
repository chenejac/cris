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
import rs.ac.uns.ftn.informatika.bibliography.db.eval.SpecVerLstResultsTypeOfResPublDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstResultsTypeOfResPublDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class SpecVerLstResultsTypeOfResPublDAO {

	static {
		SpecVerLstResultsTypeOfResPublDAO.log = LogFactory.getLog(SpecVerLstResultsTypeOfResPublDAO.class.getName());
		SpecVerLstResultsTypeOfResPublDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(String specVerLstControlNumber) {
		Connection conn = null;
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
			conn = dataSource.getConnection();
			RecordDB rDB = new RecordDB();
			SpecVerLstDTO specVerLst = (SpecVerLstDTO) rDB.getRecord(conn, specVerLstControlNumber).getDto();
			retVal.addAll(storage.getAllSpecVerLstResultsTypeOfResPubls(conn, specVerLst));
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
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(SpecVerLstDTO specVerLst) {
		Connection conn = null;
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllSpecVerLstResultsTypeOfResPubls(conn, specVerLst));
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
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(SpecVerLstDTO specVerLst, PublicationDTO publication) {
		Connection conn = null;
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllSpecVerLstResultsTypeOfResPubls(conn, specVerLst, publication));
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
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(SpecVerLstDTO specVerLst, String publHumanReadId) {
		Connection conn = null;
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllSpecVerLstResultsTypeOfResPubls(conn, specVerLst, publHumanReadId));
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

	public SpecVerLstResultsTypeOfResPublDTO getSpecVerLstResultsTypeOfResPubl(SpecVerLstDTO specVerLst, String publHumanReadId, int year) {
		Connection conn = null;
		SpecVerLstResultsTypeOfResPublDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getSpecVerLstResultsTypeOfResPubl(conn, specVerLst, publHumanReadId, year);
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
	 * Adds a new SpecVerLst ResultsType Of Result Publication to the database.
	 * 
	 * @param SpecVerLstResultsTypeOfResPublDTO
	 *            SpecVerLst ResultsType Of Result Publication to add
	 * @return true if successful else false
	 */
	public boolean add(SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.add(conn, specVerLstResultsTypeOfResPubl) == false) {
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
	 * Updates the SpecVerLst ResultsType Of Result Publication in the database
	 * files.
	 * 
	 * @param rec
	 *            SpecVerLstResultsTypeOfResPublDTO to update
	 * @return true if successful else false
	 */
	public boolean update(SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = dataSource.getConnection();
			if (storage.update(conn, specVerLstResultsTypeOfResPubl) == false) {
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
	 * Deletes a SpecVerLst ResultsType Of Result Publication from the database.
	 * 
	 * @param SpecVerLstResultsTypeOfResPublDTO
	 *            SpecVerLst ResultsType Of Result Publication which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.delete(conn, specVerLstResultsTypeOfResPubl) == false) {
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
	
	protected SpecVerLstResultsTypeOfResPublDB storage = new SpecVerLstResultsTypeOfResPublDB();
	protected static DataSource dataSource;
	protected static Log log;
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		System.out.println("Poceo");
//		SpecVerLstResultsTypeOfResPublDAO dao = new SpecVerLstResultsTypeOfResPublDAO();
//		List<SpecVerLstResultsTypeOfResPublDTO> lista = dao.getAllSpecVerLstResultsTypeOfResPubls("(BISIS)83243");
//		
//		for (SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPublDTO : lista) {
//			System.out.println(specVerLstResultsTypeOfResPublDTO);
//		}
//		
//		System.out.println("ZAVRSIO");
//	}
}
