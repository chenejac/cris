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

import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class RuleBookResultsTypeDB {

	static {
		RuleBookResultsTypeDB.log = LogFactory.getLog(RuleBookResultsTypeDB.class.getName());
	}
	
	public RuleBookResultsTypeDTO getRuleBookResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		RuleBookResultsTypeDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from RULEBOOK_RESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");

			if (rset.next()) {	
				retVal = new RuleBookResultsTypeDTO(ruleBook,resultsType);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, RuleBookResultsTypeDTO> getAllRuleBookResultsType(Connection conn, RuleBookDTO ruleBook) {
		Map<String, RuleBookResultsTypeDTO> retVal = new HashMap<String, RuleBookResultsTypeDTO>();
		try {
//			System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 1");
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE from RULEBOOK_RESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "'");

//			System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 2");
			while (rset.next()) {
				String classResultsType = rset.getString(3);
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO rt = rtDB.getResultsType(conn, classResultsType, false);
				
//				System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 3");
				
				RuleBookResultsTypeDTO rrt = new RuleBookResultsTypeDTO(ruleBook,rt);
//				System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 4");
				retVal.put(ruleBook.getControlNumber()+rt.toString(), rrt);
//				System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 5");
			}
			rset.close();
			stmt.close();
//			System.out.println("RuleBookResultsTypeDB - getAllRuleBookResultsType - 6 - size:"+retVal.size());
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean addRuleBookResultsType(Connection conn, RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into RULEBOOK_RESULTSTYPE (RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE) values (?, ?, ?)");
			
			String ruleBookId = ruleBookResultsType.getRuleBook().getControlNumber();
			if (ruleBookId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, ruleBookId);
			
			String resultTypeSchemeId = ruleBookResultsType.getResultsType().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, resultTypeSchemeId);
			
			String resultTypeClassId = ruleBookResultsType.getResultsType().getClassId();
			if (resultTypeClassId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, resultTypeClassId);
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add RuleBook Result Type entry for Rulebook: " + ruleBookResultsType.getRuleBook().getControlNumber() + " and Result Type:"+ruleBookResultsType.getResultsType().toString());
		}
		return retVal;
	}
	
	public boolean addRuleBookResultsTypeAndResultsTypeMeasures(Connection conn, RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = addRuleBookResultsType(conn, ruleBookResultsType);
		for (ResultsTypeMeasureDTO element : ruleBookResultsType.getResultsTypeMeasures()) {
			if(retVal){
				try {
					PreparedStatement stmt = conn
							.prepareStatement("insert into RESULTSTYPEMEASURE (RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE, CFCLASSSCHEMEIDSCIENCESGROUP, CFCLASSIDSCIENCESGROUP, QUANTITATIVEMEASURE) values (?, ?, ?, ?, ?, ?)");
					
					String ruleBookId = ruleBookResultsType.getRuleBook().getControlNumber();
					if (ruleBookId == null)
						stmt.setNull(1, Types.VARCHAR);
					else
						stmt.setString(1, ruleBookId);
					
					String resultTypeSchemeId = ruleBookResultsType.getResultsType().getSchemeId();
					if (resultTypeSchemeId == null)
						stmt.setNull(2, Types.VARCHAR);
					else
						stmt.setString(2, resultTypeSchemeId);
					
					String resultTypeClassId = ruleBookResultsType.getResultsType().getClassId();
					if (resultTypeClassId == null)
						stmt.setNull(3, Types.VARCHAR);
					else
						stmt.setString(3, resultTypeClassId);
					
					String sciGroupSchemeId = element.getSciencesGroup().getSchemeId();
					if (sciGroupSchemeId == null)
						stmt.setNull(4, Types.VARCHAR);
					else
						stmt.setString(4, sciGroupSchemeId);
					
					String sciGroupClassId = element.getSciencesGroup().getClassId();
					if (sciGroupClassId == null)
						stmt.setNull(5, Types.VARCHAR);
					else
						stmt.setString(5, sciGroupClassId);
					
					double quantitativeMeasure = element.getQuantitativeMeasure();
					if (quantitativeMeasure == 0)
						stmt.setNull(6, Types.DOUBLE);
					else
						stmt.setDouble(6, quantitativeMeasure);
					
					if(stmt.executeUpdate() > 0)
						retVal = retVal && true;
					
					stmt.close();
				} catch (SQLException ex) {
					retVal = false;
					log.fatal(ex);
					log.fatal("Cannot add Result Measure entry for Rulebook: " + ruleBookResultsType.getRuleBook().getControlNumber() + " and Result Type:"+ruleBookResultsType.getResultsType().toString() + " and Science Group:"+ element.getSciencesGroup().toString());
				}
			}
		}
		return retVal;
	}
	

	public boolean deleteRuleBookResultsType(Connection conn, RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = false;
		retVal = deleteRuleBookResultsType(conn, ruleBookResultsType.getRuleBook(), ruleBookResultsType.getResultsType());
		return retVal;
	}
	
	public boolean deleteRuleBookResultsType(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RULEBOOK_RESULTSTYPE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");
			stmt.close();
//			System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsType - 1");
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete RuleBook Result Type for Rulebook: " + ruleBook.getControlNumber() + " and Result Type:"+resultsType.toString());
			return false;
		}
	}
	
	public boolean deleteRuleBookResultsTypeAndDependacies(Connection conn, RuleBookResultsTypeDTO ruleBookResultsType) {
		boolean retVal = false;
		retVal = deleteRuleBookResultsTypeAndDependacies(conn, ruleBookResultsType.getRuleBook(), ruleBookResultsType.getResultsType());
		return retVal;
	}
	
	public boolean deleteRuleBookResultsTypeAndDependacies(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		
		boolean retVal = false;
//		System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsTypeAndResultsTypeMeasures - 1");
		EntityResultsTypeDB ertDB = new EntityResultsTypeDB();
		if(ertDB.deleteEntityResultsTypeByResultsType(conn, ruleBook, resultsType)==false)
			return false;
//		System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsTypeAndResultsTypeMeasures - 2");
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RESULTSTYPEMEASURE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");
			stmt.close();
			retVal = true;
//			System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsTypeAndResultsTypeMeasures - 3");
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete all Result Measure for Rulebook: " + ruleBook.getControlNumber() + " and Result Type:"+resultsType.toString());
			return false;
		}
//		System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsTypeAndResultsTypeMeasures - 4");
		if(retVal)
			retVal = deleteRuleBookResultsType(conn, ruleBook, resultsType);
//		System.out.println("RuleBookResultsTypeDB - deleteRuleBookResultsTypeAndResultsTypeMeasures - 5");
		
		return retVal;
	}
	
	private static Log log;
}
