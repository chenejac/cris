package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;

/**
 * Class for persist and retrieve data about CERIF semantic layer
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class CERIFSemanticLayerDB {
	
	public List<ClassDTO> getAllClassDTO(Connection conn, String schemeId){
		
		List<ClassDTO> retVal = new ArrayList<ClassDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID, CFSTARTDATE, CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + schemeId + "'");

			while (rset.next()) {
				String classId = rset.getString(1);
//				Calendar startDate = new GregorianCalendar();
//				if (rset.getDate(2) != null) {
//					startDate.setTimeInMillis(rset.getDate(2).getTime());
//				}
//				
//				Calendar endDate = new GregorianCalendar();
//				if (rset.getDate(3) != null) {
//					endDate.setTimeInMillis(rset.getDate(3).getTime());
//				}
				ClassDTO classEl = new ClassDTO(schemeId, classId, getClassTerm(conn, schemeId, classId), getClassTermTranslations(conn, schemeId, classId), getClassDescription(conn, schemeId, classId), getClassDescriptionTranslations(conn, schemeId, classId));
				retVal.add(classEl);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public ClassDTO getClassDTO(Connection conn, String schemeId, String classId){
		ClassDTO retVal = null;
		try {
			retVal = new ClassDTO(schemeId, classId, getClassTerm(conn, schemeId, classId), getClassTermTranslations(conn, schemeId, classId), getClassDescription(conn, schemeId, classId), getClassDescriptionTranslations(conn, schemeId, classId));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public MultilingualContentDTO getClassTerm(Connection conn, String schemeId, String classId) {
		MultilingualContentDTO retVal = new MultilingualContentDTO(null, "srp", MultilingualContentDTO.TRANS_ORIGINAL);
		try {
			String query = "select CFTERM, CFLANGCODE from CFCLASSTERM where CFCLASSSCHEMEID like '"
			+ schemeId + "' and CFCLASSID like '"
			+ classId + "' and CFTRANS like 'o'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);
			if (rset.next()) 
				retVal = new MultilingualContentDTO(rset.getString(1), rset.getString(2), MultilingualContentDTO.TRANS_ORIGINAL);
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<MultilingualContentDTO> getClassTermTranslations(Connection conn, String schemeId, String classId) {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		try {
			String query = "select CFTERM, CFLANGCODE, CFTRANS from CFCLASSTERM where CFCLASSSCHEMEID like '"
			+ schemeId + "' and CFCLASSID like '"
			+ classId + "' and CFTRANS not like 'o'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			while (rset.next()) 
				retVal.add(new MultilingualContentDTO(rset.getString(1), rset.getString(2), rset.getString(3)));
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public MultilingualContentDTO getClassDescription(Connection conn, String schemeId, String classId) {
		MultilingualContentDTO retVal = new MultilingualContentDTO(null, "srp", MultilingualContentDTO.TRANS_ORIGINAL);
		try {
			String query = "select CFDESCRIPTION, CFLANGCODE from CFCLASSDESCRIPTION where CFCLASSSCHEMEID like '"
				+ schemeId + "' and CFCLASSID like '"
				+ classId + "' and CFTRANS like 'o'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) 
				retVal = new MultilingualContentDTO(rset.getString(1), rset.getString(2), MultilingualContentDTO.TRANS_ORIGINAL);
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<MultilingualContentDTO> getClassDescriptionTranslations(Connection conn, String schemeId, String classId) {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		try {
			String query = "select CFDESCRIPTION, CFLANGCODE, CFTRANS from CFCLASSDESCRIPTION where CFCLASSSCHEMEID like '"
				+ schemeId + "' and CFCLASSID like '"
				+ classId + "' and CFTRANS not like 'o'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			while (rset.next()) 
				retVal.add(new MultilingualContentDTO(rset.getString(1), rset.getString(2), rset.getString(3)));
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean addClassTerms(Connection conn, String schemeId, String classId, List<MultilingualContentDTO> classTerms) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFCLASSTERM (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFTERM) values (?, ?, ?, ?, ?)");
			stmt.setString(1, schemeId);
			stmt.setString(2, classId);
			
			for (MultilingualContentDTO classTerm : classTerms) {
				stmt.setString(3, classTerm.getLanguage());
				stmt.setString(4, classTerm.getTransType());
				stmt.setString(5, classTerm.getContent());
			
				stmt.executeUpdate();
			}
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add classTerms: " + schemeId + " | " + classId);
		}
		return false;
	}
	
	public boolean addClassDescriptions(Connection conn, String schemeId, String classId, List<MultilingualContentDTO> classDescriptions) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFCLASSDESCRIPTION (CFCLASSSCHEMEID, CFCLASSID, CFLANGCODE, CFTRANS, CFDESCRIPTION) values (?, ?, ?, ?, ?)");
			stmt.setString(1, schemeId);
			stmt.setString(2, classId);
			
			for (MultilingualContentDTO classDescription : classDescriptions) {
				stmt.setString(3, classDescription.getLanguage());
				stmt.setString(4, classDescription.getTransType());
				stmt.setString(5, classDescription.getContent());
			
				stmt.executeUpdate();
			}
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add classDescriptions: " + schemeId + " | " + classId);
		}
		return false;
	}
	
	public boolean updateClassTerms(Connection conn, String schemeId, String classId, List<MultilingualContentDTO> classTerms) {
		if(deleteClassTerms(conn, schemeId, classId))
			return addClassTerms(conn, schemeId, classId, classTerms);
		else return false;
	}
	
	public boolean updateClassDescriptions(Connection conn, String schemeId, String classId, List<MultilingualContentDTO> classDescriptions) {
		if(deleteClassDescriptions(conn, schemeId, classId))
			return addClassDescriptions(conn, schemeId, classId, classDescriptions);
		else return false;
	}
	
	public boolean deleteClassTerms(Connection conn, String schemeId, String classId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFCLASSTERM where CFCLASSSCHEMEID like '" + schemeId + "' and CFCLASSID like '" + classId + "'");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete classTerms: " + schemeId + " | " + classId);
		}
		return false;
	}
	
	public boolean deleteClassDescriptions(Connection conn, String schemeId, String classId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFCLASSDESCRIPTION where CFCLASSSCHEMEID like '" + schemeId + "' and CFCLASSID like '" + classId + "'");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete classDescriptions: " + schemeId + " | " + classId);
		}
		return false;
	}
	
	public boolean addClassClass(Connection conn, String schemeId1, String classId1, String schemeId2, String classId2, String schemeId, String classId, Calendar startDate, Calendar endDate) {
		try {
			if((schemeId2 == null) || (classId2 == null))
				return true;
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFCLASS_CLASS (CFCLASSSCHEMEID1, CFCLASSID1, CFCLASSSCHEMEID2, CFCLASSID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, schemeId1);
			stmt.setString(2, classId1);
			stmt.setString(3, schemeId2);
			stmt.setString(4, classId2);
			stmt.setString(5, schemeId);
			stmt.setString(6, classId);
			stmt.setDate(7, new java.sql.Date(startDate.getTime()
					.getTime()));
			stmt.setDate(8, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.executeUpdate();
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add classClass: " + schemeId1 + " | " + classId1 + " and " + schemeId2 + " | " + classId2);
		}
		return false;
	}
	
	public boolean updateClassClass(Connection conn, String schemeId1, String classId1, String schemeId2, String classId2, String schemeId, String classId, Calendar startDate, Calendar endDate) {
		if(deleteClassClass(conn, schemeId1, classId1, schemeId2, classId2, schemeId, classId)){
			return addClassClass(conn, schemeId1, classId1, schemeId2, classId2, schemeId, classId, startDate, endDate);
		}
		else {
//			System.out.println("Puce delete");
			return false;
		}
	}
	
	public boolean deleteClassClass(Connection conn, String schemeId1, String classId1, String schemeId2, String classId2, String schemeId, String classId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '" + schemeId1 + "' and CFCLASSID1 like '" + classId1 + "' " +
															"and CFCLASSSCHEMEID2 like '" + schemeId2 + "' and CFCLASSID2 like '" + classId2 + "' " +
															"and CFCLASSSCHEMEID like '" + schemeId + "' and CFCLASSID like '" + classId + "' ");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete classClass: " + schemeId1 + " | " + classId1);
		}
		return false;
	}
	
	/**
	 * Adds a new class to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param classDTO
	 *            class to add
	 * @return true if successful else false
	 */
	public boolean addClass(Connection conn, ClassDTO classDTO) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFCLASS (CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?)");
			stmt.setString(1, classDTO.getSchemeId());
			stmt.setString(2, classDTO.getClassId());
			
			Calendar startDate = new GregorianCalendar();
			startDate.set(Calendar.YEAR, 1900);
			startDate.set(Calendar.MONTH, Calendar.JANUARY);
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			stmt.setDate(3, new java.sql.Date(startDate.getTime()
					.getTime()));
			
			Calendar endDate = new GregorianCalendar();
			endDate.set(Calendar.YEAR, 2099);
			endDate.set(Calendar.MONTH, Calendar.DECEMBER);
			endDate.set(Calendar.DAY_OF_MONTH, 31);
			stmt.setDate(4, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.executeUpdate();
			stmt.close();
			
			if(addClassTerms(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllTerms()))
				if(addClassDescriptions(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add classDTO: " + classDTO);
		}
		return false;
	}

	/**
	 * Adds a new class to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param classDTO
	 *            class to add
	 * @param startDate
	 *            date to add
	 * @param endDate
	 *            date to add
	 * @return true if successful else false
	 */
	public boolean addClass(Connection conn, ClassDTO classDTO, Calendar startDate, Calendar endDate) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFCLASS (CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?)");
			stmt.setString(1, classDTO.getSchemeId());
			stmt.setString(2, classDTO.getClassId());
			
			if(startDate==null){
				startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1900);
				startDate.set(Calendar.MONTH, Calendar.JANUARY);
				startDate.set(Calendar.DAY_OF_MONTH, 1);
			}
			stmt.setDate(3, new java.sql.Date(startDate.getTime()
					.getTime()));
			
			if(endDate==null){
				endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 2099);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
			}
			stmt.setDate(4, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.executeUpdate();
			stmt.close();
			
			if(addClassTerms(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllTerms()))
				if(addClassDescriptions(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add classDTO: " + classDTO);
		}
		return false;
	}
	
	/**
	 * Updates the class in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param classDTO
	 *            class to update
	 * @return true if successful else false
	 */
	public boolean updateClass(Connection conn, ClassDTO classDTO) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update CFCLASS set CFSTARTDATE=?, CFENDDATE=? where CFCLASSSCHEMEID=? and CFCLASSID=?");
			
			Calendar startDate = new GregorianCalendar();
			startDate.set(Calendar.YEAR, 1900);
			startDate.set(Calendar.MONTH, Calendar.JANUARY);
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			stmt.setDate(1, new java.sql.Date(startDate.getTime()
					.getTime()));
			
			Calendar endDate = new GregorianCalendar();
			endDate.set(Calendar.YEAR, 2099);
			endDate.set(Calendar.MONTH, Calendar.DECEMBER);
			endDate.set(Calendar.DAY_OF_MONTH, 31);
			stmt.setDate(2, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.setString(3, classDTO.getSchemeId());
			stmt.setString(4, classDTO.getClassId());
			
			stmt.executeUpdate();
			stmt.close();
			
			if(updateClassTerms(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllTerms()))
				if(updateClassDescriptions(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot update class: " + classDTO);
		}
		return false;
	}
	
	/**
	 * Updates the class in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param classDTO
	 *            class to update
	 * @param startDate
	 *            date to update
	 * @param endDate
	 *            date to update
	 * @return true if successful else false
	 */
	public boolean updateClass(Connection conn, ClassDTO classDTO, Calendar startDate, Calendar endDate) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update CFCLASS set CFSTARTDATE=?, CFENDDATE=? where CFCLASSSCHEMEID=? and CFCLASSID=?");
			
			if(startDate==null){
				startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1900);
				startDate.set(Calendar.MONTH, Calendar.JANUARY);
				startDate.set(Calendar.DAY_OF_MONTH, 1);
			}
			stmt.setDate(1, new java.sql.Date(startDate.getTime()
					.getTime()));
			
			if(endDate==null){
				endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 2099);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
			}
			stmt.setDate(2, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.setString(3, classDTO.getSchemeId());
			stmt.setString(4, classDTO.getClassId());
			
			stmt.executeUpdate();
			stmt.close();
			
			if(updateClassTerms(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllTerms()))
				if(updateClassDescriptions(conn, classDTO.getSchemeId(), classDTO.getClassId(), classDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot update class: " + classDTO);
		}
		return false;
	}


	
	/**
	 * Deletes the class from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param classDTO
	 *            class
	 * @return true if successful
	 */
	public boolean deleteClass(Connection conn, ClassDTO classDTO) {
		try {
			Statement stmt = conn.createStatement();

			int brojObrisanih = stmt.executeUpdate("delete from CFCLASS where CFCLASSSCHEMEID like '" + classDTO.getSchemeId() + "'" +
															" and CFCLASSID like '" + classDTO.getClassId() + "'");
			stmt.close();
			if(brojObrisanih == 0)
				return false;
			else 
				return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete class: " + classDTO);
		}
		return false;
	}


	public Calendar getClassStartDate(Connection conn, String schemeId, String classId) {
		Calendar retVal = new GregorianCalendar();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select CFSTARTDATE from CFCLASS where CFCLASSSCHEMEID like '" + schemeId + "'" + " and CFCLASSID like '" + classId + "'");
			
			if (rset.next()) 
				retVal.setTimeInMillis(rset.getDate(1).getTime());

			rset.close();
			stmt.close();	
		} catch (SQLException e) {
			log.fatal(e);
		}
		return retVal;
	}
	
	public Calendar getClassEndDate(Connection conn, String schemeId, String classId) {
		Calendar retVal = new GregorianCalendar();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select CFENDDATE from CFCLASS where CFCLASSSCHEMEID like '" + schemeId + "'" + " and CFCLASSID like '" + classId + "'");
			
			if (rset.next()) 
				retVal.setTimeInMillis(rset.getDate(1).getTime());

			rset.close();
			stmt.close();	
		} catch (SQLException e) {
			log.fatal(e);
		}
		return retVal;
	}
	
	private static Log log = LogFactory.getLog(CERIFSemanticLayerDB.class.getName());

}
