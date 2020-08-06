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
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookEntityTypeDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class RuleBookEntityTypeDB {

	static {
		RuleBookEntityTypeDB.log = LogFactory.getLog(RuleBookEntityTypeDB.class.getName());
	}
	
	public RuleBookEntityTypeDTO getRuleBookEntityType(Connection conn, RuleBookDTO ruleBook, ClassDTO entityType) {
		RuleBookEntityTypeDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE from RULEBOOK_ENTITYTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDENTITYTYPE like '"    + EntityTypesDAO.schemaId + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()+"'");

			if (rset.next()) {	
				retVal = new RuleBookEntityTypeDTO(ruleBook, entityType);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, RuleBookEntityTypeDTO> getAllRuleBookEntityTypes(Connection conn, RuleBookDTO ruleBook) {
		Map<String, RuleBookEntityTypeDTO> retVal = new HashMap<String, RuleBookEntityTypeDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE from RULEBOOK_ENTITYTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDENTITYTYPE like '"    + EntityTypesDAO.schemaId + "'");
			while (rset.next()) {
				String classEntityType = rset.getString(3);
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO et = cerifDB.getClassDTO(conn, EntityTypesDAO.schemaId, classEntityType);
				RuleBookEntityTypeDTO ret = new RuleBookEntityTypeDTO(ruleBook,et);
				retVal.put(ruleBook.getControlNumber()+et.toString(), ret);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean addRuleBookEntityType(Connection conn, RuleBookEntityTypeDTO ruleBookEntityType) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into RULEBOOK_ENTITYTYPE (RULEBOOKID, CFCLASSSCHEMEIDENTITYTYPE, CFCLASSIDENTITYTYPE) values (?, ?, ?)");
			
			String ruleBookId = ruleBookEntityType.getRuleBook().getControlNumber();
			if (ruleBookId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, ruleBookId);
			
			String entityTypeSchemeId = ruleBookEntityType.getEntityType().getSchemeId();
			if (entityTypeSchemeId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, entityTypeSchemeId);
			
			String entityTypeClassId = ruleBookEntityType.getEntityType().getClassId();
			if (entityTypeClassId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, entityTypeClassId);
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add RuleBook Entity Type entry for Rulebook: " + ruleBookEntityType.getRuleBook().getControlNumber() + " and Entity Type:"+ruleBookEntityType.getEntityType().toString());
		}
		return retVal;
	}
	

	public boolean deleteRuleBookEntityType(Connection conn, RuleBookEntityTypeDTO ruleBookEntityType) {
		boolean retVal = false;
		retVal = deleteRuleBookEntityType(conn, ruleBookEntityType.getRuleBook(), ruleBookEntityType.getEntityType());
		return retVal;
	}
	
	public boolean deleteRuleBookEntityType(Connection conn, RuleBookDTO ruleBook, ClassDTO entityType) {
		
		EntityResultsTypeDB ertDB = new EntityResultsTypeDB();
		if(ertDB.deleteEntityResultsTypeByEntityType(conn, ruleBook, entityType)==false)
			return false;
		
		//obrisi sve elemente iz RULEBOOK_ENTITYTYPE sa istim entitetima
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RULEBOOK_ENTITYTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDENTITYTYPE like '"    + EntityTypesDAO.schemaId + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete RuleBook Entity Type for Rulebook: " + ruleBook.getControlNumber() + " and Entity Type:"+entityType.toString());
			return false;
		}
	}
	
	private static Log log;
}
