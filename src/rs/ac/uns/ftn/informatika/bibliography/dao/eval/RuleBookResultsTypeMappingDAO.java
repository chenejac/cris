package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.eval.RuleBookResultsTypeMappingDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeMappingDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class RuleBookResultsTypeMappingDAO {
	
	static {
		RuleBookResultsTypeMappingDAO.log = LogFactory.getLog(RuleBookResultsTypeMappingDAO.class.getName());
		RuleBookResultsTypeMappingDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMapping(Integer mappingId) {
		Connection conn = null;
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getRuleBookResultsTypeMapping(conn, mappingId);
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
	
	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMapping(RuleBookDTO ruleBook, ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeForResearcherRole, ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		Connection conn = null;
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getRuleBookResultsTypeMapping(conn, ruleBook, researcherRole, entityType, entitySourceType, 
					resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
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
	
	public RuleBookResultsTypeMappingDTO getResultsTypeMappingForResearcherRoleOfObsEntity(RuleBookDTO ruleBook, ClassDTO researcherRole, 
			ClassDTO entityType, ResultsTypeDTO resultsTypeOfObsEntity) {
		Connection conn = null;
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsTypeMappingForResearcherRoleOfObsEntity(conn, ruleBook, researcherRole, entityType, resultsTypeOfObsEntity);
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
	
	public RuleBookResultsTypeMappingDTO getResultsTypeMappingForResearcherRoleOfObsEntitySource(RuleBookDTO ruleBook, ClassDTO researcherRole, 
			ClassDTO entityType, ClassDTO entitySourceType, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		Connection conn = null;
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getResultsTypeMappingForResearcherRoleOfObsEntitySource(conn, ruleBook, researcherRole, entityType, entitySourceType, resultsTypeOfObsEntitySource);
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
	
	public List<RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByRuleBook(RuleBookDTO ruleBook) {
		Connection conn = null;
		List<RuleBookResultsTypeMappingDTO> retVal = new ArrayList<RuleBookResultsTypeMappingDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsTypeMappingsByRuleBook(conn, ruleBook).values());
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
	
	public List<RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByResearcherRole(RuleBookDTO ruleBook, ClassDTO researcherRole) {
		Connection conn = null;
		List<RuleBookResultsTypeMappingDTO> retVal = new ArrayList<RuleBookResultsTypeMappingDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsTypeMappingsByResearcherRole(conn, ruleBook, researcherRole).values());
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

	public List<RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByObsEntity(RuleBookDTO ruleBook, ClassDTO entityType) {
		Connection conn = null;
		List<RuleBookResultsTypeMappingDTO> retVal = new ArrayList<RuleBookResultsTypeMappingDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getAllResultsTypeMappingsByObsEntity(conn, ruleBook, entityType).values());
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
	 * Adds a new RuleBook Results Type Mapping to the database.
	 * 
	 * @param RuleBookResultsTypeMappingDTO
	 *            RuleBook Results Type Mapping to add
	 * @return true if successful else false
	 */
	public boolean add(RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.add(conn, ruleBookResultsTypeMapping) == false) {
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
	 * Deletes a RuleBook Results Type Mapping from the database.
	 * 
	 * @param RuleBookResultsTypeMappingDTO
	 *            RuleBook Results Type Mapping which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (storage.delete(conn, ruleBookResultsTypeMapping) == false) {
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
	
	protected RuleBookResultsTypeMappingDB storage = new RuleBookResultsTypeMappingDB();
	protected static DataSource dataSource;
	protected static Log log;
}
