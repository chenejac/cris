package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;

/**
 * Class for persist and retrieve data about research area
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class ResearchAreaDB extends CERIFSemanticLayerDB {
 
	public Map<String, ResearchAreaDTO> getAll(Connection conn) {
		Map<String, ResearchAreaDTO> retVal = new HashMap<String, ResearchAreaDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID from CFCLASS where CFCLASSSCHEMEID like '" + ResearchAreaDTO.RESEARCH_AREA_SCHEMA + "'");

			while (rset.next()) {
				String classResearchArea = rset.getString(1);
				ResearchAreaDTO ra = new ResearchAreaDTO(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea, getClassTerm(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassTermTranslations(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassDescription(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassDescriptionTranslations(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getSuperResearchArea(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea, retVal));
				ra.setLocale(new Locale("sr"));
				retVal.put(classResearchArea, ra);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<ResearchAreaDTO> getAllSubresearchAreas(Connection conn, ResearchAreaDTO superResearchArea) {
		List<ResearchAreaDTO> retVal = new ArrayList<ResearchAreaDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID from CFCLASS where CFCLASSSCHEMEID like '" + ResearchAreaDTO.RESEARCH_AREA_SCHEMA + "' and " +
							"CFCLASSID in (select CFCLASSID1 from CFCLASS_CLASS " +
												"where CFCLASSSCHEMEID1 like '" + ResearchAreaDTO.RESEARCH_AREA_SCHEMA + "' and " +
														"CFCLASSID2 like '" + superResearchArea.getClassId() + "' and " +
														"CFCLASSSCHEMEID2 like '" + ResearchAreaDTO.RESEARCH_AREA_SCHEMA + "' and " +
														"CFCLASSSCHEMEID like 'hierarchy' and " +
														"CFCLASSID like 'belongsTo')");

			while (rset.next()) {
				String classResearchArea = rset.getString(1);
				ResearchAreaDTO ra = new ResearchAreaDTO(ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea, getClassTerm(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassTermTranslations(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassDescription(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), getClassDescriptionTranslations(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classResearchArea), superResearchArea);
				retVal.add(ra);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public ResearchAreaDTO getResearchArea(Connection conn, String schemeResearchArea, String classResearchArea, Map<String, ResearchAreaDTO> researchAreas){
		ResearchAreaDTO ra = null;
		try {
			if(researchAreas.containsKey(classResearchArea))
				ra = researchAreas.get(classResearchArea);
			else
			{
				ra = new ResearchAreaDTO(schemeResearchArea, classResearchArea, getClassTerm(conn, schemeResearchArea, classResearchArea), getClassTermTranslations(conn, schemeResearchArea, classResearchArea), getClassDescription(conn, schemeResearchArea, classResearchArea), getClassDescriptionTranslations(conn, schemeResearchArea, classResearchArea), getSuperResearchArea(conn, schemeResearchArea, classResearchArea, researchAreas));
				researchAreas.put(classResearchArea, ra);
			}
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return ra;
	}

	private ResearchAreaDTO getSuperResearchArea(Connection conn, String schemeResearchArea, String classResearchArea, Map<String, ResearchAreaDTO> researchAreas){
		ResearchAreaDTO ra = new ResearchAreaDTO();
		try {
			String query = "select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
				+ schemeResearchArea + "' and CFCLASSID1 like '"
				+ classResearchArea + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like '" + ResearchAreaDTO.RESEARCH_AREA_SCHEMA + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) {
				String classSuperResearchArea = rset.getString(1);
				ra = getResearchArea(conn, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, classSuperResearchArea, researchAreas);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return ra;
	}
	
	/**
	 * Adds a new researchArea to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param researchArea
	 *            Research area to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	@Override
	public boolean addClass(Connection conn, ClassDTO researchArea) {
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		if(super.addClass(conn, researchArea))
			if(addClassClass(conn, researchArea.getSchemeId(), researchArea.getClassId(), ((ResearchAreaDTO)researchArea).getSuperResearchArea().getSchemeId(), ((ResearchAreaDTO)researchArea).getSuperResearchArea().getClassId(), "hierarchy", "belongsTo", startDate, endDate))
					return true;
		return false;
	}

	/**
	 * Updates the researchArea in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param researchArea
	 *            Research area to update
	 * @return true if successful else false
	 */
	@Override
	public boolean updateClass(Connection conn, ClassDTO researchArea) {
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		if (super.updateClass(conn, researchArea))
			if(updateClassClass(conn, researchArea.getSchemeId(), researchArea.getClassId(), ((ResearchAreaDTO)researchArea).getSuperResearchArea().getSchemeId(), ((ResearchAreaDTO)researchArea).getSuperResearchArea().getClassId(), "hierarchy", "belongsTo", startDate, endDate))
				return true;
		return false;
	}

	/**
	 * Deletes a researchArea from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param researchArea
	 *            Research area
	 * @return true if successful
	 */
	@Override
	public boolean deleteClass(Connection conn, ClassDTO researchArea) {
		for (ResearchAreaDTO ra : getAllSubresearchAreas(conn, (ResearchAreaDTO)researchArea)) {
			if(deleteClass(conn, ra) == false)
				return false;
		}
		return super.deleteClass(conn, researchArea);
	}
	
	private static Log log = LogFactory.getLog(ResearchAreaDB.class.getName());
}
