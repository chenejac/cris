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
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class ResultsTypeMeasureDB {

	static {
		ResultsTypeMeasureDB.log = LogFactory.getLog(ResultsTypeMeasureDB.class.getName());
	}
	
	public ResultsTypeMeasureDTO getResultsTypeMeasure(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType, SciencesGroupDTO sciencesGroup) {
		ResultsTypeMeasureDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE, CFCLASSSCHEMEIDSCIENCESGROUP, CFCLASSIDSCIENCESGROUP, QUANTITATIVEMEASURE from RESULTSTYPEMEASURE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"' and CFCLASSIDSCIENCESGROUP like '"    + SciencesGroupDTO.SCIENCES_GROUP_SCHEMA + "' and CFCLASSIDSCIENCESGROUP like '" + sciencesGroup.getClassId()+"'");

			if (rset.next()) {	
				Double quantitativeMeasure = rset.getDouble(6);
				retVal = new ResultsTypeMeasureDTO(ruleBook,resultsType, sciencesGroup, quantitativeMeasure);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public Map<String, ResultsTypeMeasureDTO> getAllResultsTypeMeasures(Connection conn, RuleBookDTO ruleBook, ResultsTypeDTO resultsType) {
		Map<String, ResultsTypeMeasureDTO> retVal = new HashMap<String, ResultsTypeMeasureDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE, CFCLASSSCHEMEIDSCIENCESGROUP, CFCLASSIDSCIENCESGROUP, QUANTITATIVEMEASURE from RESULTSTYPEMEASURE where RULEBOOKID like '" + ruleBook.getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsType.getClassId()+"'");
			while (rset.next()) {
				String classSciencesGroup = rset.getString(5);
				Double quantitativeMeasure = rset.getDouble(6);

				SciencesGroupDB sciGroupDB = new SciencesGroupDB();
				SciencesGroupDTO sciencesGroup = sciGroupDB.getSciencesGroup(conn, classSciencesGroup);
				
				ResultsTypeMeasureDTO resultsTypeMeasure = new ResultsTypeMeasureDTO(ruleBook,resultsType, sciencesGroup, quantitativeMeasure);
				retVal.put(ruleBook.getControlNumber()+resultsType.toString()+sciencesGroup.toString(), resultsTypeMeasure);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean add(Connection conn, ResultsTypeMeasureDTO resultsTypeMeasure) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into RESULTSTYPEMEASURE (RULEBOOKID, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE, CFCLASSSCHEMEIDSCIENCESGROUP, CFCLASSIDSCIENCESGROUP, QUANTITATIVEMEASURE) values (?, ?, ?, ?, ?, ?)");
			
			String ruleBookId = resultsTypeMeasure.getRuleBook().getControlNumber();
			if (ruleBookId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, ruleBookId);
			
			String resultTypeSchemeId = resultsTypeMeasure.getResultsType().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, resultTypeSchemeId);
			
			String resultTypeClassId = resultsTypeMeasure.getResultsType().getClassId();
			if (resultTypeClassId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, resultTypeClassId);
			
			String sciGroupSchemeId = resultsTypeMeasure.getSciencesGroup().getSchemeId();
			if (sciGroupSchemeId == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(4, sciGroupSchemeId);
			
			String sciGroupClassId = resultsTypeMeasure.getSciencesGroup().getClassId();
			if (sciGroupClassId == null)
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, sciGroupClassId);
			
			double quantitativeMeasure = resultsTypeMeasure.getQuantitativeMeasure();
			if (quantitativeMeasure == 0)
				stmt.setNull(6, Types.DOUBLE);
			else
				stmt.setDouble(6, quantitativeMeasure);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add Result Measure entry for Rulebook: " + resultsTypeMeasure.getRuleBook().getControlNumber() + " and Results Type:"+resultsTypeMeasure.getResultsType().toString() + " and Sciences Group:"+ resultsTypeMeasure.getSciencesGroup().toString());
		}
		return retVal;
	}
	
	public boolean update(Connection conn, ResultsTypeMeasureDTO resultsTypeMeasure) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update RESULTSTYPEMEASURE set QUANTITATIVEMEASURE=? where RULEBOOKID like '" + resultsTypeMeasure.getRuleBook().getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '"    + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsTypeMeasure.getResultsType().getClassId()+"' and CFCLASSSCHEMEIDSCIENCESGROUP like '"    + SciencesGroupDTO.SCIENCES_GROUP_SCHEMA + "' and CFCLASSIDSCIENCESGROUP like '" + resultsTypeMeasure.getSciencesGroup().getClassId()+"'");
			
			double quantitativeMeasure = resultsTypeMeasure.getQuantitativeMeasure();
			if (quantitativeMeasure == 0)
				stmt.setNull(1, Types.DOUBLE);
			else
				stmt.setDouble(1, quantitativeMeasure);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update Result Measure for for Rulebook: " + resultsTypeMeasure.getRuleBook().getControlNumber() + " and Results Type:"+resultsTypeMeasure.getResultsType().toString() + " and Sciences Group:"+ resultsTypeMeasure.getSciencesGroup().toString());
		}
		return retVal;
	}
	
	public boolean delete(Connection conn, ResultsTypeMeasureDTO resultsTypeMeasure) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from RESULTSTYPEMEASURE where RULEBOOKID like '" + resultsTypeMeasure.getRuleBook().getControlNumber() + "' and CFCLASSSCHEMEIDRESULTSTYPE like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSIDRESULTSTYPE like '" + resultsTypeMeasure.getResultsType().getClassId()+"' and CFCLASSSCHEMEIDSCIENCESGROUP like '" + SciencesGroupDTO.SCIENCES_GROUP_SCHEMA + "' and CFCLASSIDSCIENCESGROUP like '" + resultsTypeMeasure.getSciencesGroup().getClassId()+"'");
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete Result Measure for for Rulebook: " + resultsTypeMeasure.getRuleBook().getControlNumber() + " and Results Type:"+resultsTypeMeasure.getResultsType().toString() + " and Sciences Group:"+ resultsTypeMeasure.getSciencesGroup().toString());
			return false;
		}
	}
	
	private static Log log;
}
