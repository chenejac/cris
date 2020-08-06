package rs.ac.uns.ftn.informatika.bibliography.db.eval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstResultsTypeOfResPublDTO;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class SpecVerLstResultsTypeOfResPublDB {

	static {
		SpecVerLstResultsTypeOfResPublDB.log = LogFactory.getLog(SpecVerLstResultsTypeOfResPublDB.class.getName());
	}
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(Connection conn, SpecVerLstDTO specVerLst) {
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 1");
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select SPECVERLSTID, RESPUBLID, YEAR, RESPUBHUMANREADID, RESPUBDISPNAME, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE  from SPECVERLST_RESULTSTYPEOFRESPUBL where SPECVERLSTID like '" + specVerLst.getControlNumber() +  "'");
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 2");
			while (rset.next()) {
				String publicationID = rset.getString(2);
				int year = rset.getInt(3);
				String publHumanReadId = rset.getString(4);
				String publDisplName = rset.getString(5);
//				String schemeResultsType = rset.getString(6);
				String classResultsType = rset.getString(7);
				
				PublicationDTO publication = null;
				if(publicationID!=null){
					RecordDB rDB = new RecordDB();
					publication = (PublicationDTO) rDB.getRecord(conn, publicationID).getDto();
				}
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsType = rtDB.getResultsType(conn, classResultsType, false);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 3");
				SpecVerLstResultsTypeOfResPublDTO temp = new SpecVerLstResultsTypeOfResPublDTO(specVerLst, resultsType, publication, publHumanReadId, publDisplName, year);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 4");
				retVal.add(temp);
			}
			rset.close();
			stmt.close();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 6 - size:"+retVal.size());
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(Connection conn, SpecVerLstDTO specVerLst, PublicationDTO publication) {
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 1");
			String selectSQL = "select SPECVERLSTID, RESPUBLID, YEAR, RESPUBHUMANREADID, RESPUBDISPNAME, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE  from SPECVERLST_RESULTSTYPEOFRESPUBL where SPECVERLSTID like ? and RESPUBLID like ?";
			PreparedStatement pstmt = conn.prepareStatement(selectSQL);	
			if (specVerLst == null || specVerLst.getControlNumber()==null)
				pstmt.setNull(1, Types.VARCHAR);
			else
				pstmt.setString(1, specVerLst.getControlNumber());
			
			if (publication == null || publication.getControlNumber()==null)
				pstmt.setNull(2, Types.VARCHAR);
			else
				pstmt.setString(2, publication.getControlNumber());
			
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 2");
			ResultSet rset = pstmt.executeQuery();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 3");
			while (rset.next()) {
				int year = rset.getInt(3);
				String publHumanReadId = rset.getString(4);
				String publDisplName = rset.getString(5);
//				String schemeResultsType = rset.getString(6);
				String classResultsType = rset.getString(7);
				
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsType = rtDB.getResultsType(conn, classResultsType, false);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 4");
				SpecVerLstResultsTypeOfResPublDTO temp = new SpecVerLstResultsTypeOfResPublDTO(specVerLst, resultsType, publication, publHumanReadId, publDisplName, year);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 5");
				retVal.add(temp);
			}
			rset.close();
			pstmt.close();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 6 - size:"+retVal.size());
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(Connection conn, SpecVerLstDTO specVerLst, String publHumanReadId) {
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
		try {
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 1");
			String selectSQL = "select SPECVERLSTID, RESPUBLID, YEAR, RESPUBHUMANREADID, RESPUBDISPNAME, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE  from SPECVERLST_RESULTSTYPEOFRESPUBL where SPECVERLSTID like ? and RESPUBHUMANREADID like ?";
			PreparedStatement pstmt = conn.prepareStatement(selectSQL);	
			if (specVerLst == null || specVerLst.getControlNumber()==null)
				pstmt.setNull(1, Types.VARCHAR);
			else
				pstmt.setString(1, specVerLst.getControlNumber());
			
			if (publHumanReadId == null)
				pstmt.setNull(2, Types.VARCHAR);
			else
				pstmt.setString(2, publHumanReadId);
			
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 2");
			ResultSet rset = pstmt.executeQuery();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 3");
			while (rset.next()) {
				String publicationID = rset.getString(2);
				int year = rset.getInt(3);
				String publDisplName = rset.getString(5);
//				String schemeResultsType = rset.getString(6);
				String classResultsType = rset.getString(7);
				
				
				PublicationDTO publication = null;
				if(publicationID!=null){
					RecordDB rDB = new RecordDB();
					publication = (PublicationDTO) rDB.getRecord(conn, publicationID).getDto();
				}
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsType = rtDB.getResultsType(conn, classResultsType, false);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 4");
				SpecVerLstResultsTypeOfResPublDTO temp = new SpecVerLstResultsTypeOfResPublDTO(specVerLst, resultsType, publication, publHumanReadId, publDisplName, year);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 5");
				retVal.add(temp);
			}
			rset.close();
			pstmt.close();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 6 - size:"+retVal.size());
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public SpecVerLstResultsTypeOfResPublDTO getSpecVerLstResultsTypeOfResPubl(Connection conn, SpecVerLstDTO specVerLst, String publHumanReadId, int year) {
		SpecVerLstResultsTypeOfResPublDTO retVal = null;
		try {
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 1");
			String selectSQL = "select SPECVERLSTID, RESPUBLID, YEAR, RESPUBHUMANREADID, RESPUBDISPNAME, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE  from SPECVERLST_RESULTSTYPEOFRESPUBL where SPECVERLSTID like ? and RESPUBHUMANREADID like ? and YEAR=?";
			PreparedStatement pstmt = conn.prepareStatement(selectSQL);	
			if (specVerLst == null || specVerLst.getControlNumber()==null)
				pstmt.setNull(1, Types.VARCHAR);
			else
				pstmt.setString(1, specVerLst.getControlNumber());
			
			if (publHumanReadId == null)
				pstmt.setNull(2, Types.VARCHAR);
			else
				pstmt.setString(2, publHumanReadId);
			
			pstmt.setInt(3, year);
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 2");
			ResultSet rset = pstmt.executeQuery();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 3");
			if (rset.next()) {
				String publicationID = rset.getString(2);
				String publDisplName = rset.getString(5);
//				String schemeResultsType = rset.getString(6);
				String classResultsType = rset.getString(7);
				
				
				PublicationDTO publication = null;
				if(publicationID!=null){
					RecordDB rDB = new RecordDB();
					publication = (PublicationDTO) rDB.getRecord(conn, publicationID).getDto();
				}
				ResultsTypeDB rtDB = new ResultsTypeDB();
				ResultsTypeDTO resultsType = rtDB.getResultsType(conn, classResultsType, false);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 4");
				retVal = new SpecVerLstResultsTypeOfResPublDTO(specVerLst, resultsType, publication, publHumanReadId, publDisplName, year);
//				System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 5");
			}
			rset.close();
			pstmt.close();
//			System.out.println("SpecVerLstResultsTypeOfResPublDB - getAllSpecVerLstResultsTypeOfResPubls - 6 - size:"+retVal.size());
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean add(Connection conn, SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into SPECVERLST_RESULTSTYPEOFRESPUBL (SPECVERLSTID, RESPUBLID, YEAR, RESPUBHUMANREADID, "
							+ "RESPUBDISPNAME, CFCLASSSCHEMEIDRESULTSTYPE, CFCLASSIDRESULTSTYPE) "
							+ "values (?, ?, ?, ?, ?, ?, ?)");
			
			String specVerLstId = specVerLstResultsTypeOfResPubl.getSpecVerLst().getControlNumber();
			if (specVerLstId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, specVerLstId);
			
			String publicationId = (specVerLstResultsTypeOfResPubl.getPublication()==null?null:specVerLstResultsTypeOfResPubl.getPublication().getControlNumber());
			if (publicationId == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, publicationId);
			
			stmt.setInt(3, specVerLstResultsTypeOfResPubl.getYear());

			String publHumanReadId = specVerLstResultsTypeOfResPubl.getPublHumanReadId();
			if (publHumanReadId == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(4, publHumanReadId);
			
			String publDisplName = specVerLstResultsTypeOfResPubl.getPublDisplName();
			if (publDisplName == null)
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, publDisplName);
			
			String resultTypeSchemeId = specVerLstResultsTypeOfResPubl.getResultsType().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(6, Types.VARCHAR);
			else
				stmt.setString(6, resultTypeSchemeId);
			
			String resultTypeClassId = specVerLstResultsTypeOfResPubl.getResultsType().getClassId();
			if (resultTypeClassId == null)
				stmt.setNull(7, Types.VARCHAR);
			else
				stmt.setString(7, resultTypeClassId);
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add SpecVerLst ResultsType Of ResPubl entry for SpecVerLst: " + specVerLstResultsTypeOfResPubl.getSpecVerLst().getControlNumber()
					+ " and Publication:"+ (specVerLstResultsTypeOfResPubl.getPublication()==null?null:specVerLstResultsTypeOfResPubl.getPublication().getControlNumber())
					+ " and Year:"+specVerLstResultsTypeOfResPubl.getYear() 
					+ " and Publication Human Readable Id:"+specVerLstResultsTypeOfResPubl.getPublHumanReadId()
					+ " and Publication Display Name:"+specVerLstResultsTypeOfResPubl.getPublDisplName()
					+ " and Results Type:"+specVerLstResultsTypeOfResPubl.getResultsType()
					);
		}
		return retVal;
	}
	
	public boolean update(Connection conn, SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update SPECVERLST_RESULTSTYPEOFRESPUBL set RESPUBLID = ?, RESPUBDISPNAME = ?, CFCLASSSCHEMEIDRESULTSTYPE = ?, CFCLASSIDRESULTSTYPE = ? "
							+ "where SPECVERLSTID like ? and RESPUBHUMANREADID like ? and YEAR=?");
			
			String publicationId = (specVerLstResultsTypeOfResPubl.getPublication()==null?null:specVerLstResultsTypeOfResPubl.getPublication().getControlNumber());
			if (publicationId == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, publicationId);
			
			String publDisplName = specVerLstResultsTypeOfResPubl.getPublDisplName();
			if (publDisplName == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, publDisplName);
			
			String resultTypeSchemeId = specVerLstResultsTypeOfResPubl.getResultsType().getSchemeId();
			if (resultTypeSchemeId == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, resultTypeSchemeId);
			
			String resultTypeClassId = specVerLstResultsTypeOfResPubl.getResultsType().getClassId();
			if (resultTypeClassId == null)
				stmt.setNull(4, Types.VARCHAR);
			else
				stmt.setString(5, resultTypeClassId);
			
			String specVerLstId = specVerLstResultsTypeOfResPubl.getSpecVerLst().getControlNumber();
			if (specVerLstId == null)
				stmt.setNull(5, Types.VARCHAR);
			else
				stmt.setString(5, specVerLstId);
			
			String publHumanReadId = specVerLstResultsTypeOfResPubl.getPublHumanReadId();
			if (publHumanReadId == null)
				stmt.setNull(6, Types.VARCHAR);
			else
				stmt.setString(6, publHumanReadId);
			
			stmt.setInt(7, specVerLstResultsTypeOfResPubl.getYear());
			
			if(stmt.executeUpdate() > 0)
				retVal = true;
			stmt.close();
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update SpecVerLst ResultsType Of ResPubl entry for SpecVerLst: " + specVerLstResultsTypeOfResPubl.getSpecVerLst().getControlNumber()
					+ " and Publication:"+ (specVerLstResultsTypeOfResPubl.getPublication()==null?null:specVerLstResultsTypeOfResPubl.getPublication().getControlNumber())
					+ " and Year:"+specVerLstResultsTypeOfResPubl.getYear() 
					+ " and Publication Human Readable Id:"+specVerLstResultsTypeOfResPubl.getPublHumanReadId()
					+ " and Publication Display Name:"+specVerLstResultsTypeOfResPubl.getPublDisplName()
					+ " and Results Type:"+specVerLstResultsTypeOfResPubl.getResultsType()
					);
		}
		return retVal;
	}
	
	public boolean delete(Connection conn, SpecVerLstResultsTypeOfResPublDTO specVerLstResultsTypeOfResPubl) {				
		return delete(conn, specVerLstResultsTypeOfResPubl.getSpecVerLst(), specVerLstResultsTypeOfResPubl.getPublHumanReadId(), specVerLstResultsTypeOfResPubl.getYear());
	}
	
	public boolean delete(Connection conn, SpecVerLstDTO specVerLst, String publHumanReadId, int year) {				
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from SPECVERLST_RESULTSTYPEOFRESPUBL where SPECVERLSTID like '" + specVerLst.getControlNumber() +  "' and RESPUBHUMANREADID like '" + publHumanReadId +  "' and YEAR="+year);
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete SpecVerLst ResultsType Of ResPubl entry for SpecVerLst: " + specVerLst.getControlNumber()
					+ " and Year:"+year
					+ " and Publication Human Readable Id:"+publHumanReadId);
			return false;
		}
	}
	
	private static Log log;
}
