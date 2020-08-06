package rs.ac.uns.ftn.informatika.bibliography.db.eval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.EntityTypesDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.EntityResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class EntityResultsTypeDB {

	static {
		EntityResultsTypeDB.log = LogFactory.getLog(EntityResultsTypeDB.class.getName());
	}

	public EntityResultsTypeDTO getEntityResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType, ClassDTO entityType) {
		EntityResultsTypeDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+"' and CFCLASSSCHEMEIDENTITYTYPE like '"    + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()
							+"' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + resultsType.getSchemeId() + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");

			if (rset.next()) {	
				retVal = new EntityResultsTypeDTO(ruleBook,resultsType, entityType);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, EntityResultsTypeDTO> getAllEntityResultsTypeByRuleBook(Connection conn, RuleBookDTO ruleBook) {
		Map<String, EntityResultsTypeDTO> retVal = new HashMap<String, EntityResultsTypeDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "'");

			while (rset.next()) {
				String classEntityType = rset.getString(3);
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO et = cerifDB.getClassDTO(conn, EntityTypesDAO.schemaId, classEntityType);
				String classResultsType = rset.getString(5);
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO rt = rtDB.getResultsType(conn, classResultsType, false);

				EntityResultsTypeDTO ert = new EntityResultsTypeDTO(ruleBook, rt,et);
				retVal.put(ert.toString(), ert);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, EntityResultsTypeDTO> getAllEntityResultsTypeByResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		Map<String, EntityResultsTypeDTO> retVal = new HashMap<String, EntityResultsTypeDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+ "' and CFCLASSSCHEMEIDRESULTSTYPE like '" + resultsType.getSchemeId() + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");

			while (rset.next()) {
				String classEntityType = rset.getString(3);
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO et = cerifDB.getClassDTO(conn, EntityTypesDAO.schemaId, classEntityType);
				
				EntityResultsTypeDTO ert = new EntityResultsTypeDTO(ruleBook, resultsType,et);
				retVal.put(ert.toString(), ert);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, EntityResultsTypeDTO> getAllEntityResultsTypeByEntityType(Connection conn, RuleBookDTO ruleBook, ClassDTO entityType) {
		Map<String, EntityResultsTypeDTO> retVal = new HashMap<String, EntityResultsTypeDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+ "' and CFCLASSSCHEMEIDENTITYTYPE like '" + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()+"'");

			while (rset.next()) {
				String classResultsType = rset.getString(5);
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO rt = rtDB.getResultsType(conn, classResultsType, false);
				
				EntityResultsTypeDTO ert = new EntityResultsTypeDTO(ruleBook, rt,entityType);
				retVal.put(ert.toString(), ert);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean addEntityResultsType(Connection conn, EntityResultsTypeDTO entityResultsType) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into ENTITYRESULTSTYPE (RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE) values (?, ?, ?, ?, ?)");
			
			String ruleBookId = entityResultsType.getRuleBook().getControlNumber();
			if (ruleBookId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, ruleBookId);
			
			String entityTypeSchemeId = entityResultsType.getEntityType().getSchemeId();
			if (entityTypeSchemeId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, entityTypeSchemeId);
			
			String entityTypeClassId = entityResultsType.getEntityType().getClassId();
			if (entityTypeClassId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, entityTypeClassId);
			
			String resultTypeSchemeId = entityResultsType.getResultsType().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(4, resultTypeSchemeId);
			
			String resultTypeClassId = entityResultsType.getResultsType().getClassId();
			if (resultTypeClassId == null)
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, resultTypeClassId);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add Entity Result Type entry for Rulebook: " + entityResultsType.getRuleBook().getControlNumber() + " and Entity Type:"+entityResultsType.getEntityType().toString() + " and Result Type:"+entityResultsType.getResultsType().toString());
		}
		return retVal;
	}

	public boolean delete(Connection conn, EntityResultsTypeDTO entityResultsType) {
		boolean retVal = false;
		retVal = deleteEntityResultsType(conn, entityResultsType.getRuleBook(), entityResultsType.getResultsType(), entityResultsType.getEntityType());
		return retVal;
	}
	
	public boolean deleteEntityResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType, ClassDTO entityType) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+"' and CFCLASSSCHEMEIDENTITYTYPE like '"    + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()
							+"' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + resultsType.getSchemeId() + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete Entity Result Type entry for Rulebook: " + ruleBook.getControlNumber() + " and Entity Type:"+entityType.toString() + " and Result Type:"+resultsType.toString());
			return false;
		}
	}
	
	public boolean deleteEntityResultsTypeByRuleBook(Connection conn, RuleBookDTO ruleBook) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete Entity Result Type entry for Rulebook: " + ruleBook.getControlNumber());
			return false;
		}
	}
	
	public boolean deleteEntityResultsTypeByResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+"' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + resultsType.getSchemeId() + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete Entity Result Type entry for Rulebook: " + ruleBook.getControlNumber() +" and Result Type:"+resultsType.toString());
			return false;
		}
	}
	
	public boolean deleteEntityResultsTypeByEntityType(Connection conn, RuleBookDTO ruleBook, ClassDTO entityType) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from ENTITYRESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber()
							+"' and CFCLASSSCHEMEIDENTITYTYPE like '"    + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete Entity Result Type entry for Rulebook: " + ruleBook.getControlNumber() + " and Entity Type:"+entityType.toString());
			return false;
		}
	}
	
	private static Log log;
}

