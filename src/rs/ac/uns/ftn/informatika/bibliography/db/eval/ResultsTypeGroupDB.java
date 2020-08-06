package rs.ac.uns.ftn.informatika.bibliography.db.eval;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
public class ResultsTypeGroupDB extends CERIFSemanticLayerDB {

	static {
		ResultsTypeGroupDB.log = LogFactory.getLog(ResultsTypeGroupDB.class.getName());
	}
	
	public ResultsTypeGroupDTO getResultsTypeGroup(Connection conn, String classResultsTypeGroup){
		ResultsTypeGroupDTO rtg = null;
		try {
			rtg = new ResultsTypeGroupDTO(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup, getClassTerm(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassTermTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescription(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescriptionTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassStartDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassEndDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rtg;
	}
	
	public ResultsTypeGroupDTO getResultsTypeGroup(Connection conn, String classResultsTypeGroup, Map<String, ResultsTypeGroupDTO> resultsTypeGroups){
		ResultsTypeGroupDTO rtg = null;
		try {
			if(resultsTypeGroups.containsKey(classResultsTypeGroup))
				rtg = resultsTypeGroups.get(classResultsTypeGroup);
			else
			{
				rtg = new ResultsTypeGroupDTO(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup, getClassTerm(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassTermTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescription(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescriptionTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassStartDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassEndDate(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup));
				resultsTypeGroups.put(classResultsTypeGroup, rtg);
			}
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rtg;
	}
	
	public Map<String, ResultsTypeGroupDTO> getAllResultsTypeGroup(Connection conn) {
		Map<String, ResultsTypeGroupDTO> retVal = new HashMap<String, ResultsTypeGroupDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA + "'");

			while (rset.next()) {
				String classResultsTypeGroup = rset.getString(1);
				Calendar startDate = new GregorianCalendar();
				if (rset.getDate(2) != null) {
					startDate.setTimeInMillis(rset.getDate(2).getTime());
				}
				
				Calendar endDate = new GregorianCalendar();
				if (rset.getDate(3) != null) {
					endDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				ResultsTypeGroupDTO rtg = new ResultsTypeGroupDTO(ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup, getClassTerm(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassTermTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescription(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), getClassDescriptionTranslations(conn, ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA, classResultsTypeGroup), startDate, endDate);
				rtg.setLocale(new Locale("sr"));
				retVal.put(classResultsTypeGroup, rtg);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	/**
	 * Adds a new Results Type Group to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results Type Group to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	@Override
	public boolean addClass(Connection conn, ClassDTO resultsTypeGroup) {
		ResultsTypeGroupDTO tempRTG = (ResultsTypeGroupDTO)resultsTypeGroup;
		if(super.addClass(conn, tempRTG, tempRTG.getStartDate(),tempRTG.getEndDate())==false)
			return false;
		return true;
	}

	public boolean addClassAndRelatedClass(Connection conn, ClassDTO resultsTypeGroup) {
		addClass(conn, resultsTypeGroup);
		ResultsTypeGroupDTO tempRTG = (ResultsTypeGroupDTO)resultsTypeGroup;
		
		tempRTG.arangeResultsTypes();
		for(ResultsTypeDTO tempRT: tempRTG.getResultsTypes()) {
			if(super.addClass(conn, tempRT, tempRT.getStartDate(),tempRT.getEndDate()) == false){
				return false;
			}
			if(addClassClass(conn, tempRT.getSchemeId(), tempRT.getClassId(), tempRTG.getSchemeId(), tempRTG.getClassId(), "hierarchy", "belongsTo", tempRTG.getStartDate(), tempRTG.getEndDate())==false){
				return false;
			}
			
			if(tempRT.getSuperResultsType()!=null)
				if(addClassClass(conn, tempRT.getSchemeId(), tempRT.getClassId(), tempRT.getSuperResultsType().getSchemeId(), tempRT.getSuperResultsType().getClassId(), "hierarchy", "belongsTo", tempRTG.getStartDate(), tempRTG.getEndDate())==false)
					return false;
		}
		
		return true;
	}
	
	/**
	 * Updates the Results Type Group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results Type Group to update
	 * @return true if successful else false
	 */
	@Override
	public boolean updateClass(Connection conn, ClassDTO resultsTypeGroup) {
		ResultsTypeGroupDTO temp = (ResultsTypeGroupDTO)resultsTypeGroup;
		if (super.updateClass(conn, resultsTypeGroup, temp.getStartDate(), temp.getEndDate())){
			return true;
		}
		return false;
	}

	/**
	 * Updates the Results Type Group in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results Type Group to update
	 * @return true if successful else false
	 */
	public boolean updateClassAndRelatedClass(Connection conn, ClassDTO resultsTypeGroup) {
		ResultsTypeGroupDTO temp = (ResultsTypeGroupDTO)resultsTypeGroup;
		if (super.updateClass(conn, resultsTypeGroup, temp.getStartDate(), temp.getEndDate()) == true){
			for(ResultsTypeDTO rt: temp.getResultsTypes()) {
				rt.getStartDate().setTimeInMillis(temp.getStartDate().getTimeInMillis());
				rt.getEndDate().setTimeInMillis(temp.getEndDate().getTimeInMillis());
				if(super.updateClassClass(conn, rt.getSchemeId(), rt.getClassId(), temp.getSchemeId(), temp.getClassId(), "hierarchy", "belongsTo", temp.getStartDate(), temp.getEndDate())==false){
//					System.out.println("*************************************Puce update veze sa grupom " + rt);
					return false;
				}
				if(rt.getSuperResultsType()!=null){
					if(super.updateClassClass(conn, rt.getSchemeId(), rt.getClassId(), rt.getSuperResultsType().getSchemeId(), rt.getSuperResultsType().getClassId(), "hierarchy", "belongsTo", temp.getStartDate(), temp.getEndDate())==false){
//						System.out.println("*********************************Puce update veze sa super " + rt);
						return false;
					}
				}else{
//					System.out.println("Nema roditelja " + rt);
				}
//				System.out.println("Update " + rt);
				if(super.updateClass(conn, rt, rt.getStartDate(), rt.getEndDate())==false){
//					System.out.println("*******************************Puce update " + rt);
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Deletes a Results Type Group from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param ResultsTypeGroupDTO
	 *            Results Type Group
	 * @return true if successful
	 */
	@Override
	public boolean deleteClass(Connection conn, ClassDTO resultsTypeGroup) {
		return super.deleteClass(conn, resultsTypeGroup);
	}
	
	public boolean deleteClassAndRelatedClass(Connection conn, ClassDTO resultsTypeGroup) {
		ResultsTypeGroupDTO tempRTG = (ResultsTypeGroupDTO)resultsTypeGroup;
		tempRTG.arangeResultsTypes();
		List<ResultsTypeDTO> list = tempRTG.getResultsTypes();
		for (int i = list.size()-1; i >= 0 ; i--) {
			ResultsTypeDTO tempRT = list.get(i);
			if(deleteClassClass(conn, tempRT.getSchemeId(), tempRT.getClassId(), tempRTG.getSchemeId(), tempRTG.getClassId(), "hierarchy", "belongsTo") == false)
				return false;
			if(tempRT.getSuperResultsType()!=null)
				if(deleteClassClass(conn, tempRT.getSchemeId(), tempRT.getClassId(), tempRT.getSuperResultsType().getSchemeId(), tempRT.getSuperResultsType().getClassId(), "hierarchy", "belongsTo")==false)
					return false;
			if(super.deleteClass(conn, tempRT)==false)
				return false;
		}
		return super.deleteClass(conn, resultsTypeGroup);
	}
	
//	public Map<String, ResultsTypeDTO> getAllResultsTypeInGroup(Connection conn, ResultsTypeGroupDTO resultsTypeGroup) {
//		Map<String, ResultsTypeDTO> retVal = new HashMap<String, ResultsTypeDTO>();
//		try {
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt
//					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
//							"CFCLASSID in (select CFCLASSID1 from CFCLASS_CLASS " +
//												"where CFCLASSSCHEMEID1 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
//														"CFCLASSID2 like '" + resultsTypeGroup.getClassId() + "' and " +
//														"CFCLASSSCHEMEID2 like '" + ResultsTypeGroupDTO.RESULTS_TYPE_GROUP_SCHEMA + "' and " +
//														"CFCLASSSCHEMEID like 'hierarchy' and " +
//														"CFCLASSID like 'belongsTo')");
//
//			while (rset.next()) {
//				String classResultsType = rset.getString(1);
//				
//				Calendar startDate = new GregorianCalendar();
//				if (rset.getDate(2) != null) {
//					startDate.setTimeInMillis(rset.getDate(2).getTime());
//				}
//				
//				Calendar endDate = new GregorianCalendar();
//				if (rset.getDate(3) != null) {
//					endDate.setTimeInMillis(rset.getDate(3).getTime());
//				}
//				
//				ResultsTypeDTO srt = null;
//				srt = getSuperResultsType(conn, classResultsType, resultsTypeGroup, retVal);
//				
//				ResultsTypeDTO rt = new ResultsTypeDTO(ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType, getClassTerm(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassTermTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescription(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescriptionTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), resultsTypeGroup, srt, startDate, endDate);
//				retVal.put(classResultsType, rt);
//			}
//			rset.close();
//			stmt.close();
//		} catch (Exception ex) {
//			log.fatal(ex);
//		}
//		return retVal;
//	}
//	
//	private ResultsTypeDTO getSuperResultsType(Connection conn, String classResultsType, ResultsTypeGroupDTO srtg, Map<String, ResultsTypeDTO> resultsTypes){
//		ResultsTypeDTO rt = null;
//		try {
//			String query = "select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and " +
//							"CFCLASSID in (select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
//				+ ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "' and CFCLASSID1 like '"
//				+ classResultsType + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like '" + ResultsTypeDTO.RESULTS_TYPE_SCHEMA + "'";
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt
//					.executeQuery(query);
//
//			if (rset.next()) {
//				
//				String classSuperResultsType = rset.getString(1);
//				
//				Calendar startDate = new GregorianCalendar();
//				if (rset.getDate(2) != null) {
//					startDate.setTimeInMillis(rset.getDate(2).getTime());
//				}
//				
//				Calendar endDate = new GregorianCalendar();
//				if (rset.getDate(3) != null) {
//					endDate.setTimeInMillis(rset.getDate(3).getTime());
//				}
//				
//				rt = getResultsType(conn, classSuperResultsType, srtg, resultsTypes);
//			}
//			rset.close();
//			stmt.close();
//		} catch (Exception ex) {
//			log.fatal(ex);
//		}
//		return rt;
//	}
//	
//	public ResultsTypeDTO getResultsType(Connection conn, String classResultsType, ResultsTypeGroupDTO srtg, Map<String, ResultsTypeDTO> resultsTypes){
//		ResultsTypeDTO rt = null;
//		try {
//			ResultsTypeDTO srt = null;
//			srt = getSuperResultsType(conn, classResultsType, srtg, resultsTypes);
//			
//			if(resultsTypes.containsKey(classResultsType))
//				rt = resultsTypes.get(classResultsType);
//			else
//			{
//				rt = new ResultsTypeDTO(ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType, getClassTerm(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassTermTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescription(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassDescriptionTranslations(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), srtg, srt, getClassStartDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType), getClassEndDate(conn, ResultsTypeDTO.RESULTS_TYPE_SCHEMA, classResultsType));
//				resultsTypes.put(classResultsType, rt);
//			}
//		} catch (Exception ex) {
//			log.fatal(ex);
//		}
//		return rt;
//	}
	
	private static Log log;
}
