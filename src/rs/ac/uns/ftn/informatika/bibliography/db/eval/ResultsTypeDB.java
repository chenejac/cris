package rs.ac.uns.ftn.informatika.bibliography.db.eval;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class ResultsTypeDB extends CERIFSemanticLayerDB {

	static {
		ResultsTypeDB.log = LogFactory.getLog(ResultsTypeDB.class.getName());
	}
	
	public void loadResultsType(Connection conn,  ResultsTypeDTO resultsType){
		resultsType.setSuperResultsTypeGroup(getSuperResultsTypeGroup(conn, resultsType.getClassId()));	
		resultsType.setSuperResultsType(getSuperResultsType(conn, resultsType.getClassId(), true));
	}
	
	public void loadResultsType(Connection conn,  ResultsTypeDTO resultsType, List<ResultsTypeDTO> resultsTypes){
		resultsType.setSuperResultsTypeGroup(getSuperResultsTypeGroup(conn, resultsType.getClassId()));	
		resultsType.setSuperResultsType(getSuperResultsType(conn, resultsType.getClassId(), true, resultsTypes));
	}

	//ResultsTypeDTO
	public ResultsTypeDTO getResultsType(Connection conn, String classResultsType, boolean loadSuperResultsTypeAndResultsTypeGroup){
		ResultsTypeDTO rt = null;
		try {
			ResultsTypeGroupDTO superResultsTypeGroup = null;
			ResultsTypeDTO superResultsType = null;
			if(loadSuperResultsTypeAndResultsTypeGroup){
				superResultsTypeGroup = getSuperResultsTypeGroup(conn, classResultsType);
				superResultsType = getSuperResultsType(conn, classResultsType, loadSuperResultsTypeAndResultsTypeGroup);
			}	
			rt = new ResultsTypeDTO(ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType, getClassTerm(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassTermTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescription(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescriptionTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), superResultsTypeGroup, superResultsType, getClassStartDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassEndDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rt;
	}

	//ResultsTypeDTO i punjenje mape
	public ResultsTypeDTO getResultsType(Connection conn, String classResultsType, boolean loadSuperResultsTypeAndResultsTypeGroup, List<ResultsTypeDTO> resultsTypes){
		ResultsTypeDTO rt = null;
		try {
			for (ResultsTypeDTO resultsTypeDTO : resultsTypes) {
				if(resultsTypeDTO.getClass().equals(classResultsType)){
					rt = resultsTypeDTO;
					if(loadSuperResultsTypeAndResultsTypeGroup)
						if(rt.getSuperResultsTypeGroup()==null)
							loadResultsType(conn,  rt, resultsTypes);
					break;
				}			
			}
			
			if(rt==null){
				ResultsTypeGroupDTO superResultsTypeGroup = null;
				ResultsTypeDTO superResultsType = null;
				if(loadSuperResultsTypeAndResultsTypeGroup){
					superResultsTypeGroup = getSuperResultsTypeGroup(conn, classResultsType);
					superResultsType = getSuperResultsType(conn, classResultsType, loadSuperResultsTypeAndResultsTypeGroup,resultsTypes);
					
					if(superResultsType != null){
						for (int i = 0; i < resultsTypes.size(); i++) {
							ResultsTypeDTO resultsTypeDTO = resultsTypes.get(i);
							if(i == resultsTypes.size()-1 && !resultsTypeDTO.getClass().equals(superResultsType.getClass())){
								resultsTypes.add(superResultsType);
								break;
							}
						}
					}
				}	
				
				rt = new ResultsTypeDTO(ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType, getClassTerm(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassTermTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescription(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescriptionTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), superResultsTypeGroup, superResultsType, getClassStartDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassEndDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType));
			}
			
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rt;
	}

	
	/**
	 * Retrieves all register entries.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The retrieved register entries ; null if an error occured.
	 */
	//svi ResultsTypeDTO bez veze ka superResultsTypeDTO i superResultsTypeGroupDTO
	public List<ResultsTypeDTO> getAllResultsType(Connection conn, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "'");

			while (rset.next()) {
				String classResultsType = rset.getString(1);
				
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				
				ResultsTypeDTO rt = null;
				for (ResultsTypeDTO resultsTypeDTO : retVal) {
					if(resultsTypeDTO.getClass().equals(classResultsType)){
						rt = resultsTypeDTO;
						rt.setStartDate(startDate);
						rt.setEndDate(endDate);
						break;
					}			
				}
				
				if(rt==null){
					rt = getResultsType(conn, classResultsType, loadSuperResultsTypeAndResultsTypeGroup, retVal);
					rt.setLocale(new Locale("sr"));
					retVal.add(rt);
				}
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	/**
	 * Retrieves all register entries.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The retrieved register entries ; null if an error occured.
	 */
	//svi ResultsTypeDTO sa vezom ka superResultsTypeDTO i superResultsTypeGroupDTO
	public List<ResultsTypeDTO> getAllResultsTypesOfSpecificResultsTypeGroupID(Connection conn, String classResultsTypeGroup, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		try {
			Statement stmt = conn.createStatement();
			String query = "";
			boolean superResultsTypeGroupExists = (classResultsTypeGroup != null ? true:false);
			if(superResultsTypeGroupExists)
				query = "select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
						"CFCLASSID in (select CFCLASSID1 from CFCLASS_CLASS " +
						"where CFCLASSSCHEMEID1 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
								"CFCLASSID2 like '" + classResultsTypeGroup + "' and " +
								"CFCLASSSCHEMEID2 like '" + ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA + "' and " +
								"CFCLASSSCHEMEID like 'hierarchy' and " +
								"CFCLASSID like 'belongsTo')";
			else
				query= "select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "'";
			
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				String classResultsType = rset.getString(1);
				
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				
				ResultsTypeDTO rt = null;
				for (ResultsTypeDTO resultsTypeDTO : retVal) {
					if(resultsTypeDTO.getClass().equals(classResultsType)){
						rt = resultsTypeDTO;
						rt.setStartDate(startDate);
						rt.setEndDate(endDate);
						break;
					}			
				}
				if(rt==null){
					rt = getResultsType(conn, classResultsType, loadSuperResultsTypeAndResultsTypeGroup, retVal);
					rt.setLocale(new Locale("sr"));
					retVal.add(rt);
				}
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public ResultsTypeGroupDTO getSuperResultsTypeGroup(Connection conn, String classResultsType){
		ResultsTypeGroupDTO retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA + "' and " +
							"CFCLASSID in (select CFCLASSID2 from CFCLASS_CLASS " +
												"where CFCLASSSCHEMEID1 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
														"CFCLASSID1 like '" + classResultsType + "' and " +
														"CFCLASSSCHEMEID2 like '" + ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA + "' and " +
														"CFCLASSSCHEMEID like 'hierarchy' and " +
														"CFCLASSID like 'belongsTo')");

			if (rset.next()) {
				String classResultsTypeGroup = rset.getString(1);
				
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				
				retVal = new ResultsTypeGroupDTO(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup, getClassTerm(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassTermTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescription(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescriptionTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassStartDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassEndDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup));
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	private ResultsTypeDTO getSuperResultsType(Connection conn, String classResultsType, boolean loadSuperResultsTypeAndResultsTypeGroup){
		ResultsTypeDTO rt = null;
		try {
			String query = "select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
					"CFCLASSID in (select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
					+ ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSID1 like '"
					+ classResultsType + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "')";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			if (rset.next()) {
				String classSuperResultsType = rset.getString(1);
				
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				rt = getResultsType(conn, classSuperResultsType, loadSuperResultsTypeAndResultsTypeGroup);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rt;
	}
	
	private ResultsTypeDTO getSuperResultsType(Connection conn, String classResultsType, boolean loadSuperResultsTypeAndResultsTypeGroup, List<ResultsTypeDTO> resultsTypes){
		ResultsTypeDTO rt = null;
		try {
			String query = "select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
							"CFCLASSID in (select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
				+ ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSID1 like '"
				+ classResultsType + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "')";
			
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) {
				
				String classSuperResultsType = rset.getString(1);
				
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				
				rt = getResultsType(conn, classSuperResultsType, loadSuperResultsTypeAndResultsTypeGroup, resultsTypes);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rt;
	}
	
	public List<ResultsTypeDTO> getAllSubResultsType(Connection conn, String classSuperResultsType, boolean loadSuperResultsTypeAndResultsTypeGroup) {
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
							"CFCLASSID in (select CFCLASSID1 from CFCLASS_CLASS " +
												"where CFCLASSSCHEMEID1 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
														"CFCLASSID2 like '" + classSuperResultsType + "' and " +
														"CFCLASSSCHEMEID2 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
														"CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo')");

			while (rset.next()) {
				String classResultsType = rset.getString(1);
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				
				ResultsTypeDTO rt = getResultsType(conn, classResultsType, true);
				retVal.add(rt);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	
	/**
	 * Adds a new Results Type to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeDTO
	 *            Results Type to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	@Override
	public boolean addClass(Connection conn, ClassDTO resultsType) {
		ResultsTypeDTO temp = (ResultsTypeDTO)resultsType;
		if(super.addClass(conn, temp, temp.getStartDate(),temp.getEndDate())){
			if(temp.getSuperResultsTypeGroup()!=null)
				if(addClassClass(conn, temp.getSchemeId(), temp.getClassId(), temp.getSuperResultsTypeGroup().getSchemeId(), temp.getSuperResultsTypeGroup().getClassId(), "hierarchy", "belongsTo", temp.getStartDate(), temp.getEndDate())==false)
					return false;
			if(temp.getSuperResultsType()!=null)
				if(addClassClass(conn, temp.getSchemeId(), temp.getClassId(), temp.getSuperResultsType().getSchemeId(), temp.getSuperResultsType().getClassId(), "hierarchy", "belongsTo", temp.getStartDate(), temp.getEndDate())==false)
					return false;
			
			return true;
		}
		return false;
	}

	/**
	 * Updates the Results Type in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeDTO
	 *            Results Type to update
	 * @return true if successful else false
	 */
	@Override
	public boolean updateClass(Connection conn, ClassDTO resultsType) {
		ResultsTypeDTO temp = (ResultsTypeDTO)resultsType;	
		if (super.updateClass(conn, resultsType, temp.getStartDate(), temp.getEndDate())){
			//veze ka ostalim entitetima se ne diraju jer se relugise start date i end date preko updateClass ResultsTypeGroupDB
			return true;
		}
		return false;
	}

	/**
	 * Deletes a Results Type from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeDTO
	 *            Results Type to delete
	 * @return true if successful
	 */
	@Override
	public boolean deleteClass(Connection conn, ClassDTO resultsType) {
		return super.deleteClass(conn, resultsType);
	}
	
	public boolean deleteClassAndRelatedClass(Connection conn, ClassDTO resultsType) {
		ResultsTypeDTO temp = (ResultsTypeDTO)resultsType;	
		List<ResultsTypeDTO> subResultsTypeDTO = getAllSubResultsType(conn, resultsType.getClassId(), true);
		for (int i = 0; i < subResultsTypeDTO.size(); i++) {
			deleteClassAndRelatedClass(conn, subResultsTypeDTO.get(i));
		}
		
		if(temp.getSuperResultsTypeGroup()!=null)
			if(deleteClassClass(conn, temp.getSchemeId(), temp.getClassId(), temp.getSuperResultsTypeGroup().getSchemeId(), temp.getSuperResultsTypeGroup().getClassId(), "hierarchy", "belongsTo") == false)
				return false;
		if(temp.getSuperResultsType()!=null)
			if(deleteClassClass(conn, temp.getSchemeId(), temp.getClassId(), temp.getSuperResultsType().getSchemeId(), temp.getSuperResultsType().getClassId(), "hierarchy", "belongsTo")==false)
				return false;
		return super.deleteClass(conn, resultsType);
	}
	
	private static Log log;
}
