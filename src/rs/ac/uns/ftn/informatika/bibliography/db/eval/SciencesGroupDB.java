package rs.ac.uns.ftn.informatika.bibliography.db.eval;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class SciencesGroupDB extends CERIFSemanticLayerDB{

	static {
		SciencesGroupDB.log = LogFactory.getLog(SciencesGroupDB.class.getName());
	}
	
	public SciencesGroupDTO getSciencesGroup(Connection conn, String classSciencesGroup){
		SciencesGroupDTO sciGroup = null;
		try {
			sciGroup = new SciencesGroupDTO(SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup, getClassTerm(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassTermTranslations(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassDescription(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassDescriptionTranslations(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassStartDate(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassEndDate(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup));
			} catch (Exception ex) {
			log.fatal(ex);
		}
		return sciGroup;
	}
	
	public Map<String, SciencesGroupDTO> getAllSciencesGroup(Connection conn) {
		Map<String, SciencesGroupDTO> retVal = new HashMap<String, SciencesGroupDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + SciencesGroupDTO.SCIENCES_GROUP_SCHEMA + "'");

			while (rset.next()) {
				String classSciencesGroup = rset.getString(1);
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				SciencesGroupDTO sciGroup = new SciencesGroupDTO(SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup, getClassTerm(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassTermTranslations(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassDescription(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), getClassDescriptionTranslations(conn, SciencesGroupDTO.SCIENCES_GROUP_SCHEMA, classSciencesGroup), startDate, endDate);
				sciGroup.setLocale(new Locale("sr"));
				retVal.put(classSciencesGroup, sciGroup);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	/**
	 * Adds a new Sciences Group to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param SciencesGroupDTO
	 *            Sciences Group to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	@Override
	public boolean addClass(Connection conn, ClassDTO sciencesGroup) {
		SciencesGroupDTO temp = (SciencesGroupDTO)sciencesGroup;
		if(super.addClass(conn, temp, temp.getStartDate(),temp.getEndDate())){
			return true;
		}
		return false;
	}

	/**
	 * Updates the Sciences Group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param SciencesGroupDTO
	 *            Sciences Group to update
	 * @return true if successful else false
	 */
	@Override
	public boolean updateClass(Connection conn, ClassDTO sciencesGroup) {
		SciencesGroupDTO temp = (SciencesGroupDTO)sciencesGroup;
		if(super.updateClass(conn, temp, temp.getStartDate(),temp.getEndDate())){
			return true;
		}
		return false;
	}

	/**
	 * Deletes a Sciences Group from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param SciencesGroupDTO
	 *            Sciences Group to delete
	 * @return true if successful
	 */
	@Override
	public boolean deleteClass(Connection conn, ClassDTO sciencesGroup) {
		return super.deleteClass(conn, sciencesGroup);
	}
	
	private static Log log;
}
