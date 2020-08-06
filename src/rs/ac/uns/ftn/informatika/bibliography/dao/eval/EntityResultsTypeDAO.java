package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.EntityResultsTypeDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.EntityResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class EntityResultsTypeDAO {
	static {
		EntityResultsTypeDAO.log = LogFactory.getLog(EntityResultsTypeDAO.class.getName());
		EntityResultsTypeDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public EntityResultsTypeDTO getRuleBookEntityType(RuleBookDTO ruleBook, ResultsTypeDTO resultsType, ClassDTO entityType) {
		Connection conn = null;
		EntityResultsTypeDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getEntityResultsType(conn, ruleBook, resultsType, entityType);
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
	
	public List<EntityResultsTypeDTO> getAllEntityResultsType(RuleBookDTO ruleBook) {
		Connection conn = null;
		List<EntityResultsTypeDTO> retVal = new ArrayList<EntityResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllEntityResultsTypeByRuleBook(conn, ruleBook).values());
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
	
	public List<EntityResultsTypeDTO> getAllEntityResultsTypeByResultsType(RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		Connection conn = null;
		List<EntityResultsTypeDTO> retVal = new ArrayList<EntityResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllEntityResultsTypeByResultsType(conn, ruleBook, resultsType).values());
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
	
	public List<EntityResultsTypeDTO> getAllEntityResultsTypeByResultsType(RuleBookDTO ruleBook, ClassDTO entityType) {
		Connection conn = null;
		List<EntityResultsTypeDTO> retVal = new ArrayList<EntityResultsTypeDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllEntityResultsTypeByEntityType(conn, ruleBook, entityType).values());
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
	 * Adds a new Entity Results Type to the database.
	 * 
	 * @param EntityResultsTypeDTO
	 *            Entity Results Type to add
	 * @return true if successful else false
	 */
	public boolean add(EntityResultsTypeDTO entityResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.addEntityResultsType(conn, entityResultsType) == false) {
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
	 * Deletes a Entity Results Type from the database.
	 * 
	 * @param EntityResultsTypeDTO
	 *            Entity Results Type which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(EntityResultsTypeDTO entityResultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.delete(conn, entityResultsType) == false) {
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
	 * Deletes all Entity Results Type from the database.
	 * 
	 * @param RuleBookDTO
	 *            Entity Results Types which should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteEntityResultsTypeByRuleBook(RuleBookDTO ruleBook) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteEntityResultsTypeByRuleBook(conn, ruleBook) == false) {
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
	 * Deletes all Entity Results Type from the database.
	 * 
	 * @param RuleBookDTO
	 * @param ResultsTypeDTO
	 * 
	 * @return true if successful else false
	 */
	public boolean deleteEntityResultsTypeByResultsType(RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteEntityResultsTypeByResultsType(conn, ruleBook,resultsType) == false) {
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
	 * Deletes all Entity Results Type from the database.
	 * 
	 * @param RuleBookDTO
	 * @param ClassDTO
	 * 
	 * @return true if successful else false
	 */
	public boolean deleteEntityResultsTypeByEntityType(RuleBookDTO ruleBook, ClassDTO entityType) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.deleteEntityResultsTypeByEntityType(conn, ruleBook, entityType) == false) {
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
	protected EntityResultsTypeDB storage = new EntityResultsTypeDB();
	protected static Log log;
}
