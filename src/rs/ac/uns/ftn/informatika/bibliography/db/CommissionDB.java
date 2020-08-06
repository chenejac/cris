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

import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ScienceAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class CommissionDB extends CERIFSemanticLayerDB {

	private static Log log = LogFactory.getLog(EvaluationDB.class.getName());
	
	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());
	
	/**
	 * Adds a new commission to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param commissionDTO
	 *            commission to add
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	public boolean addCommission(Connection conn, CommissionDTO commissionDTO) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("insert into COMMISSION (COMMISSIONID, APPOINTMENTBOARD, APPOINTMENTDATE, MEMBERS, CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA, SCIENTIFICFIELD) values (?, ?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, commissionDTO.getCommissionId());
			prepStmt.setString(2, commissionDTO.getAppointmentBoard());
			prepStmt.setDate(3, new java.sql.Date(commissionDTO.getAppointmentDate().getTime()
						.getTime()));
			prepStmt.setString(4, commissionDTO.getMembers());
			prepStmt.setString(5, ((commissionDTO.getScienceArea()==null)?"":commissionDTO.getScienceArea().getSchemeId()));
			prepStmt.setString(6, ((commissionDTO.getScienceArea()==null)?"":commissionDTO.getScienceArea().getClassId()));
			prepStmt.setString(7, commissionDTO.getScientificField());
			
			prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add commissionDTO: " + commissionDTO);
		}
		return false;
	}
	
	/**
	 * Update a commission to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param commissionDTO
	 *            commission to update
	 * @return true if successful else SQLException
	 * @throws SQLException
	 */
	public boolean updateCommission(Connection conn, CommissionDTO commissionDTO) {
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("update COMMISSION set APPOINTMENTBOARD=?, APPOINTMENTDATE=?, MEMBERS=? , SCIENTIFICFIELD=? where COMMISSIONID=?");
			prepStmt.setString(1, commissionDTO.getAppointmentBoard());
			prepStmt.setDate(2, new java.sql.Date(commissionDTO.getAppointmentDate().getTime()
						.getTime()));
			prepStmt.setString(3, commissionDTO.getMembers());
			prepStmt.setString(4, commissionDTO.getScientificField());
			prepStmt.setInt(5, commissionDTO.getCommissionId());

			prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot update commissionDTO: " + commissionDTO);
		}
		return false;
	}
	
	/**
	 * Deletes the commission from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param commissionDTO
	 *            commission to delete
	 * @return true if successful
	 */
	public boolean deleteCommission(Connection conn, CommissionDTO commissionDTO) {
		try {
			Statement stmt = conn.createStatement();

			int brojObrisanih = stmt.executeUpdate("delete from COMMISSION where COMMISSIONID = " + commissionDTO.getCommissionId());
			stmt.close();
			if(brojObrisanih == 0)
				return false;
			else 
				return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete commissionDTO: " + commissionDTO);
		}
		return false;
	}
	
	public List<CommissionDTO> getCommissions(Connection conn) {
		List<CommissionDTO> retVal = new ArrayList<CommissionDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select COMMISSIONID, APPOINTMENTBOARD, APPOINTMENTDATE, MEMBERS, CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA, SCIENTIFICFIELD from COMMISSION");

			while (rset.next()) {
				Integer commissionId = rset.getInt(1);
				
				if(commissionId == 0)
					continue;
				
				String appointmentBoard = rset.getString(2);
				Calendar appointmentDate = null;
				if (rset.getDate(3) != null) {
					appointmentDate = new GregorianCalendar();
					appointmentDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				String members = rset.getString(4);
				String schemeScienceArea = rset.getString(5);
				String classScienceArea = rset.getString(6);
				String scientificField = rset.getString(7);
				ScienceAreaDTO sa = getScienceArea(conn, schemeScienceArea, classScienceArea);
				CommissionDTO comm = new CommissionDTO(commissionId, appointmentBoard, appointmentDate, members, sa, scientificField);
				String name;
				try{
					name = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.name.id"+commissionId);
					if(name.startsWith("evaluation.mainPanel.yearCommissionSelection.commission.name.id"))
						name = null;
				}
				catch (Exception e) {
					name  = null;
				}
				comm.setName(name);
				retVal.add(comm);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<CommissionDTO> getConnectedCommissions(Connection conn, Integer commId) {
		List<CommissionDTO> retVal = new ArrayList<CommissionDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select COMMISSIONID, APPOINTMENTBOARD, APPOINTMENTDATE, MEMBERS, CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA, SCIENTIFICFIELD from COMMISSION where COMMISSIONID in (select COMMISSIONID2 from COMMISSION_COMMISSION where COMMISSIONID1 = " + commId + ")");

			while (rset.next()) {
				Integer commissionId = rset.getInt(1);
				
				if(commissionId == 0)
					continue;
				
				String appointmentBoard = rset.getString(2);
				Calendar appointmentDate = null;
				if (rset.getDate(3) != null) {
					appointmentDate = new GregorianCalendar();
					appointmentDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				String members = rset.getString(4);
				String schemeScienceArea = rset.getString(5);
				String classScienceArea = rset.getString(6);
				String scientificField = rset.getString(7);
				ScienceAreaDTO sa = getScienceArea(conn, schemeScienceArea, classScienceArea);
				CommissionDTO comm = new CommissionDTO(commissionId, appointmentBoard, appointmentDate, members, sa, scientificField);
				String name;
				try{
					name = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.name.id"+commissionId);
					if(name.startsWith("evaluation.mainPanel.yearCommissionSelection.commission.name.id"))
						name = null;
				}
				catch (Exception e) {
					name  = null;
				}
				comm.setName(name);
				retVal.add(comm);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public CommissionDTO getCommission(Connection conn, Integer commissionId) {
		CommissionDTO comm = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select COMMISSIONID, APPOINTMENTBOARD, APPOINTMENTDATE, MEMBERS, CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA, SCIENTIFICFIELD from COMMISSION where COMMISSIONID="
							+ commissionId);

			if (rset.next()) {
				String appointmentBoard = rset.getString(2);
				Calendar appointmentDate = null;
				if (rset.getDate(3) != null) {
					appointmentDate = new GregorianCalendar();
					appointmentDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				String members = rset.getString(4);
				String schemeScienceArea = rset.getString(5);
				String classScienceArea = rset.getString(6);
				String scientificField = rset.getString(7);
				ScienceAreaDTO sa = getScienceArea(conn, schemeScienceArea, classScienceArea);
				comm = new CommissionDTO(commissionId, appointmentBoard, appointmentDate, members, sa, scientificField);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return comm;
	}
	
	public ScienceAreaDTO getScienceArea(Connection conn, String schemeScienceArea, String classScienceArea) {
		ScienceAreaDTO sa = null;
		try {
			sa = new ScienceAreaDTO(schemeScienceArea, classScienceArea, getClassTerm(conn, schemeScienceArea, classScienceArea), getClassTermTranslations(conn, schemeScienceArea, classScienceArea), getClassDescription(conn, schemeScienceArea, classScienceArea), getClassDescriptionTranslations(conn, schemeScienceArea, classScienceArea));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return sa;
	}
}
