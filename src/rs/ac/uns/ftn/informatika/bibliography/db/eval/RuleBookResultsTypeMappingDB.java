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

import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeMappingDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class RuleBookResultsTypeMappingDB {

	static {
		RuleBookResultsTypeMappingDB.log = LogFactory.getLog(RuleBookResultsTypeMappingDB.class.getName());
	}

	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMapping(Connection conn, Integer mappingId) {
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where MAPPINGID = '"+mappingId+"'");

			if (rset.next()) {
				String ruleBookID = rset.getString(2);
				String schemeResearcherRole = rset.getString(3);
				String classResearcherRole = rset.getString(4);
				String schemeEntityType = rset.getString(5);
				String classEntityType = rset.getString(6);
				String classEntitySourceType = rset.getString(7);
//				String schemeResultsType = rset.getString(8);
				String classResultsTypeForResearchRole = rset.getString(9);
				String classResultsTypeOfObservedEntity = rset.getString(10);
				String classResultsTypeOfObservedEntitySource = rset.getString(11);
				
				RecordDB rDB = new RecordDB();
				RuleBookDTO ruleBook = (RuleBookDTO) rDB.getRecord(conn, ruleBookID).getDto();
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO researcherRole = cerifDB.getClassDTO(conn, schemeResearcherRole, classResearcherRole);
				ClassDTO entityType = cerifDB.getClassDTO(conn, schemeEntityType, classEntityType);
				//moze biti null
				ClassDTO entitySourceType = null;
				if(classEntitySourceType!=null && !"".equals(classEntitySourceType))
					entitySourceType = cerifDB.getClassDTO(conn, schemeEntityType, classEntitySourceType);
				
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntity = null;
				if(classResultsTypeOfObservedEntity!=null && !"".equals(classResultsTypeOfObservedEntity))
					resultsTypeOfObsEntity = rtDB.getResultsType(conn, classResultsTypeOfObservedEntity, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntitySource = null;
				if(classResultsTypeOfObservedEntitySource!=null && !"".equals(classResultsTypeOfObservedEntitySource))
					resultsTypeOfObsEntitySource = rtDB.getResultsType(conn, classResultsTypeOfObservedEntitySource, false);
				
				retVal = new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMapping(Connection conn, RuleBookDTO ruleBook, ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeForResearcherRole, ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and " 
							+ "CFCLASSSCHEMEIDRESROLE like '" + researcherRole.getSchemeId() + "' and CFCLASSIDRESROLE like '" + researcherRole.getClassId()+ "' and "
							+ "CFCLASSSCHEMEIDENTITYTYPE like '"  + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId() + "' and "
							+ "CFCLASSIDENTITYSOURCETYPE "+ (entitySourceType==null?"IS NULL": ("like '" + entitySourceType.getClassId()+"'")) + " and "
							+ "CFCLASSSCHEMEIDRESULTSTYPE like '" + resultsTypeForResearcherRole.getSchemeId() + "' and CFCLASSIDRESULTSTYPEFORRESROLE like '" + resultsTypeForResearcherRole.getClassId() + "' and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITY "+ (resultsTypeOfObsEntity==null?"IS NULL": ("like '" + resultsTypeOfObsEntity.getClassId()+"'")) + " and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "+ (resultsTypeOfObsEntitySource==null?"IS NULL": ("like '" + resultsTypeOfObsEntitySource.getClassId()+"'"))
							);

			if (rset.next()) {
				Integer mappingId = rset.getInt(1);
				
				retVal = new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public RuleBookResultsTypeMappingDTO getResultsTypeMappingForResearcherRoleOfObsEntity(Connection conn, RuleBookDTO ruleBook, ClassDTO researcherRole, ClassDTO entityType,  
			ResultsTypeDTO resultsTypeOfObsEntity) {
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and " 
							+ "CFCLASSSCHEMEIDRESROLE like '" + researcherRole.getSchemeId() + "' and CFCLASSIDRESROLE like '" + researcherRole.getClassId()+ "' and "
							+ "CFCLASSSCHEMEIDENTITYTYPE like '"  + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId() + "' and "
							+ "CFCLASSIDENTITYSOURCETYPE IS NULL and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITY like '" + resultsTypeOfObsEntity.getClassId()+"' and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE IS NULL"
							);

			if (rset.next()) {
				Integer mappingId = rset.getInt(1);
				String classResultsTypeForResearchRole = rset.getString(9);
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				
				retVal = new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, null, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, null);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public RuleBookResultsTypeMappingDTO getResultsTypeMappingForResearcherRoleOfObsEntitySource(Connection conn, RuleBookDTO ruleBook, ClassDTO researcherRole, 
			ClassDTO entityType, ClassDTO entitySourceType, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		RuleBookResultsTypeMappingDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and " 
							+ "CFCLASSSCHEMEIDRESROLE like '" + researcherRole.getSchemeId() + "' and CFCLASSIDRESROLE like '" + researcherRole.getClassId()+ "' and "
							+ "CFCLASSSCHEMEIDENTITYTYPE like '"  + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId() + "' and "
							+ "CFCLASSIDENTITYSOURCETYPE like '" + entitySourceType.getClassId()+"' and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITY IS NULL and "
							+ "CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE like '" + entitySourceType.getClassId()+"'"
							);

			if (rset.next()) {
				Integer mappingId = rset.getInt(1);
				String classResultsTypeForResearchRole = rset.getString(9);
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				
				retVal = new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, null, resultsTypeOfObsEntitySource);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByRuleBook(Connection conn, RuleBookDTO ruleBook) {
		Map<String, RuleBookResultsTypeMappingDTO> retVal = new HashMap<String, RuleBookResultsTypeMappingDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() +"'");

			while (rset.next()) {

				Integer mappingId = rset.getInt(1);
				String schemeResearcherRole = rset.getString(3);
				String classResearcherRole = rset.getString(4);
				String schemeEntityType = rset.getString(5);
				String classEntityType = rset.getString(6);
				String classEntitySourceType = rset.getString(7);
//				String schemeResultsType = rset.getString(8);
				String classResultsTypeForResearchRole = rset.getString(9);
				String classResultsTypeOfObservedEntity = rset.getString(10);
				String classResultsTypeOfObservedEntitySource = rset.getString(11);
				
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO researcherRole = cerifDB.getClassDTO(conn, schemeResearcherRole, classResearcherRole);
				ClassDTO entityType = cerifDB.getClassDTO(conn, schemeEntityType, classEntityType);
				//moze biti null
				ClassDTO entitySourceType = null;
				if(classEntitySourceType!=null && !"".equals(classEntitySourceType))
					entitySourceType = cerifDB.getClassDTO(conn, schemeEntityType, classEntitySourceType);
				
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntity = null;
				if(classResultsTypeOfObservedEntity!=null && !"".equals(classResultsTypeOfObservedEntity))
					resultsTypeOfObsEntity = rtDB.getResultsType(conn, classResultsTypeOfObservedEntity, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntitySource = null;
				if(classResultsTypeOfObservedEntitySource!=null && !"".equals(classResultsTypeOfObservedEntitySource))
					resultsTypeOfObsEntitySource = rtDB.getResultsType(conn, classResultsTypeOfObservedEntitySource, false);
				
				retVal.put(mappingId.toString() ,new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource));
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByResearcherRole(Connection conn, RuleBookDTO ruleBook, ClassDTO researcherRole) {
		Map<String, RuleBookResultsTypeMappingDTO> retVal = new HashMap<String, RuleBookResultsTypeMappingDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and " 
							+ "CFCLASSSCHEMEIDRESROLE like '" + researcherRole.getSchemeId() + "' and CFCLASSIDRESROLE like '" + researcherRole.getClassId()+"'");

			while (rset.next()) {

				Integer mappingId = rset.getInt(1);
				String schemeEntityType = rset.getString(5);
				String classEntityType = rset.getString(6);
				String classEntitySourceType = rset.getString(7);
//				String schemeResultsType = rset.getString(8);
				String classResultsTypeForResearchRole = rset.getString(9);
				String classResultsTypeOfObservedEntity = rset.getString(10);
				String classResultsTypeOfObservedEntitySource = rset.getString(11);
				
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO entityType = cerifDB.getClassDTO(conn, schemeEntityType, classEntityType);
				//moze biti null
				ClassDTO entitySourceType = null;
				if(classEntitySourceType!=null && !"".equals(classEntitySourceType))
					entitySourceType = cerifDB.getClassDTO(conn, schemeEntityType, classEntitySourceType);
				
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntity = null;
				if(classResultsTypeOfObservedEntity!=null && !"".equals(classResultsTypeOfObservedEntity))
					resultsTypeOfObsEntity = rtDB.getResultsType(conn, classResultsTypeOfObservedEntity, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntitySource = null;
				if(classResultsTypeOfObservedEntitySource!=null && !"".equals(classResultsTypeOfObservedEntitySource))
					resultsTypeOfObsEntitySource = rtDB.getResultsType(conn, classResultsTypeOfObservedEntitySource, false);
				
				retVal.put(mappingId.toString() ,new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource));
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, RuleBookResultsTypeMappingDTO> getAllResultsTypeMappingsByObsEntity(Connection conn, RuleBookDTO ruleBook, ClassDTO entityType) {
		Map<String, RuleBookResultsTypeMappingDTO> retVal = new HashMap<String, RuleBookResultsTypeMappingDTO>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select MAPPINGID,RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE "
							+ "FROM RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and " 
							+ "CFCLASSSCHEMEIDENTITYTYPE like '"  + entityType.getSchemeId() + "' and CFCLASSIDENTITYTYPE like '" + entityType.getClassId() +"'");

			while (rset.next()) {

				Integer mappingId = rset.getInt(1);
				String schemeResearcherRole = rset.getString(3);
				String classResearcherRole = rset.getString(4);
				String schemeEntityType = rset.getString(5);
				String classEntitySourceType = rset.getString(7);
//				String schemeResultsType = rset.getString(8);
				String classResultsTypeForResearchRole = rset.getString(9);
				String classResultsTypeOfObservedEntity = rset.getString(10);
				String classResultsTypeOfObservedEntitySource = rset.getString(11);
				
				CERIFSemanticLayerDB cerifDB = new CERIFSemanticLayerDB();
				ClassDTO researcherRole = cerifDB.getClassDTO(conn, schemeResearcherRole, classResearcherRole);
				//moze biti null
				ClassDTO entitySourceType = null;
				if(classEntitySourceType!=null && !"".equals(classEntitySourceType))
					entitySourceType = cerifDB.getClassDTO(conn, schemeEntityType, classEntitySourceType);
				
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsTypeForResearcherRole = rtDB.getResultsType(conn, classResultsTypeForResearchRole, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntity = null;
				if(classResultsTypeOfObservedEntity!=null && !"".equals(classResultsTypeOfObservedEntity))
					resultsTypeOfObsEntity = rtDB.getResultsType(conn, classResultsTypeOfObservedEntity, false);
				//moze biti null
				ResultsTypeDTO resultsTypeOfObsEntitySource = null;
				if(classResultsTypeOfObservedEntitySource!=null && !"".equals(classResultsTypeOfObservedEntitySource))
					resultsTypeOfObsEntitySource = rtDB.getResultsType(conn, classResultsTypeOfObservedEntitySource, false);
				
				retVal.put(mappingId.toString() ,new RuleBookResultsTypeMappingDTO(mappingId, ruleBook, researcherRole, entityType, entitySourceType, 
						resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource));
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean add(Connection conn, RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into RULEBOOKRESULTSTYPEMAPPING (RULEBOOKID,CFCLASSSCHEMEIDRESROLE,CFCLASSIDRESROLE,"
							+ "CFCLASSSCHEMEIDENTITYTYPE,CFCLASSIDENTITYTYPE,CFCLASSIDENTITYSOURCETYPE,"
							+ "CFCLASSSCHEMEIDRESULTSTYPE,CFCLASSIDRESULTSTYPEFORRESROLE,CFCLASSIDRESULTSTYPEOFOBSENTITY,CFCLASSIDRESULTSTYPEOFOBSENTITYSOURCE) "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			String ruleBookId = ruleBookResultsTypeMapping.getRuleBook().getControlNumber();
			if (ruleBookId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, ruleBookId);
			
			String researcherRoleSchemeId = ruleBookResultsTypeMapping.getResearcherRole().getSchemeId();
			if (researcherRoleSchemeId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, researcherRoleSchemeId);
			
			String researcherRoleClassId = ruleBookResultsTypeMapping.getResearcherRole().getClassId();
			if (researcherRoleClassId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, researcherRoleClassId);
			
			String entityTypeSchemeId = ruleBookResultsTypeMapping.getEntityType().getSchemeId();
			if (entityTypeSchemeId == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(4, entityTypeSchemeId);
			
			String entityTypeClassId = ruleBookResultsTypeMapping.getEntityType().getClassId();
			if (entityTypeClassId == null)
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, entityTypeClassId);
			
			String entitySourceTypeClassId = (ruleBookResultsTypeMapping.getEntitySourceType()==null?null:ruleBookResultsTypeMapping.getEntitySourceType().getClassId());
			if (entitySourceTypeClassId == null)
				stmt.setNull(6, Types.VARCHAR);
			else
				stmt.setString(6, entitySourceTypeClassId);
			
			String resultTypeSchemeId = ruleBookResultsTypeMapping.getResultsTypeForResearcherRole().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(7, Types.VARCHAR);
			else
				stmt.setString(7, resultTypeSchemeId);
			
			String resultTypeForResearchRoleClassId = ruleBookResultsTypeMapping.getResultsTypeForResearcherRole().getClassId();
			if (resultTypeForResearchRoleClassId == null)
				stmt.setNull(8, Types.VARCHAR);
			else
				stmt.setString(8, resultTypeForResearchRoleClassId);
			
			String resultTypeOfObservedEntityClassId = (ruleBookResultsTypeMapping.getResultsTypeOfObsEntity()==null?null:ruleBookResultsTypeMapping.getResultsTypeOfObsEntity().getClassId());
			if (resultTypeOfObservedEntityClassId == null)
				stmt.setNull(9, Types.VARCHAR);
			else
				stmt.setString(9, resultTypeOfObservedEntityClassId);
			
			String resultTypeOfObservedEntitySourceClassId = (ruleBookResultsTypeMapping.getResultsTypeOfObsEntitySource()==null?null:ruleBookResultsTypeMapping.getResultsTypeOfObsEntitySource().getClassId());
			if (resultTypeOfObservedEntitySourceClassId == null)
				stmt.setNull(10, Types.VARCHAR);
			else
				stmt.setString(10, resultTypeOfObservedEntitySourceClassId);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add RuleBook Results Type Mapping entry for Rulebook: " + ruleBookResultsTypeMapping.getRuleBook().getControlNumber() + " and Research Role:"+ruleBookResultsTypeMapping.getResearcherRole()
					+ " and Entity Type:"+ruleBookResultsTypeMapping.getEntityType() + " and Entity Source Type:"+ruleBookResultsTypeMapping.getEntitySourceType()
					+ " and Results Type For Researcher Role:"+ruleBookResultsTypeMapping.getResultsTypeForResearcherRole() 
					+ " and Results Type Of Observed Entity:"+ruleBookResultsTypeMapping.getResultsTypeOfObsEntity()
					+ " and Results Type Of Observed Entity Source:"+ruleBookResultsTypeMapping.getResultsTypeOfObsEntitySource()
					);
		}
		return retVal;
	}

	public boolean delete(Connection conn, RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RULEBOOKRESULTSTYPEMAPPING where MAPPINGID = '"+ruleBookResultsTypeMapping.getMappingId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete RuleBook Results Type Mapping entry for mappindID: " + ruleBookResultsTypeMapping.getMappingId());
			return false;
		}
	}
	
	public boolean deleteRuleBookResultsTypeMappingByRuleBook(Connection conn, RuleBookDTO ruleBook) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RULEBOOKRESULTSTYPEMAPPING where RULEBOOKID like '" + ruleBook.getControlNumber()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete RuleBook Results Type Mapping entry for Rulebook: " + ruleBook.getControlNumber());
			return false;
		}
	}
	
	private static Log log;
}