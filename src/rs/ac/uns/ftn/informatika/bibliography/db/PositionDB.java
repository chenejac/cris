package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;

/**
 * Class for persist and retrieve data about position
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class PositionDB extends CERIFSemanticLayerDB {
 
	public Map<String, PositionDTO> getAll(Connection conn) {
		Map<String, PositionDTO> retVal = new HashMap<String, PositionDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID from CFCLASS where CFCLASSSCHEMEID like '" + PositionDTO.POSITION_SCHEMA +"'");

			while (rset.next()) {
				String classPosition = rset.getString(1);
				PositionDTO ra = new PositionDTO(PositionDTO.POSITION_SCHEMA, classPosition, getClassTerm(conn, PositionDTO.POSITION_SCHEMA, classPosition), getClassTermTranslations(conn, PositionDTO.POSITION_SCHEMA, classPosition), getClassDescription(conn, PositionDTO.POSITION_SCHEMA, classPosition), getClassDescriptionTranslations(conn, PositionDTO.POSITION_SCHEMA, classPosition));
				retVal.put(classPosition, ra);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public PositionDTO getPosition(Connection conn, String schemePosition, String classPosition){
		PositionDTO po = null;
		try {
			po = new PositionDTO(schemePosition, classPosition, getClassTerm(conn, schemePosition, classPosition), getClassTermTranslations(conn, schemePosition, classPosition), getClassDescription(conn, schemePosition, classPosition), getClassDescriptionTranslations(conn, schemePosition, classPosition));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return po;
	}
	
	private static Log log = LogFactory.getLog(ResearchAreaDB.class.getName());
}
