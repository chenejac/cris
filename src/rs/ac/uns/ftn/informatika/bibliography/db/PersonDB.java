package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * Class for persist and retrieve data about persons
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class PersonDB extends RecordDB {
	
	/**
	 * Retrieves an array of persons.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The array of persons.
	 */
	@Override
	public List<Record> getAllRecords(Connection conn) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select RECORDID, JMBG, DIRECTPHONES, LOCALPHONES, APVNT from PERSON");
			while (rset.next()) {
				String recordId = rset.getString(1);
				String jmbg = rset.getString(2);
				String directPhones = rset.getString(3);
				String localPhones = rset.getString(4);
				String apvnt = rset.getString(5);
				Record rec = super.getRecord(conn, recordId);
				if(rec != null)
					retVal.add(new Person(rec, jmbg, directPhones, localPhones, apvnt));
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read records ");
			log.fatal(ex);
			return null;
		}
	}
	
	/**
	 * Retrieves an array of persons.
	 * 
	 * @param conn
	 *            Database connection
	 * @param institutionId
	 *            Institution identifier
	 * @return The array of persons.
	 */
	public List<Record> getInstitutionRecords(Connection conn, String institutionId, String startDate) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			Statement stmt = conn.createStatement();
			String query = "select RECORDID, JMBG, DIRECTPHONES, LOCALPHONES, APVNT from PERSON where RECORDID in (select RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and CFCLASSID like 'belongs to' and RECORDID2 like '" + institutionId + "' and CFSTARTDATE like '" + startDate + "')";
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				String recordId = rset.getString(1);
				String jmbg = rset.getString(2);
				String directPhones = rset.getString(3);
				String localPhones = rset.getString(4);
				String apvnt = rset.getString(5);
				Record rec = super.getRecord(conn, recordId);
				if(rec != null)
					retVal.add(new Person(rec, jmbg, directPhones, localPhones, apvnt));
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read records ");
			log.fatal(ex);
			return null;
		}
	}
	
	public List<String> getInstitutionRecordsIds(Connection conn, String institutionId, String startDate) {
		List<String> retVal = new ArrayList<String>();
		try {
			Statement stmt = conn.createStatement();
			String query = "select RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and CFCLASSID like 'belongs to' and RECORDID2 like '" + institutionId + "' and CFSTARTDATE like '" + startDate + "'";
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {
				String recordId = rset.getString(1);
				if(recordId != null)
					retVal.add(recordId);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read records ");
			log.fatal(ex);
			return null;
		}
	}
	
	public List<Record> getInstitutionRecordsNoDate(Connection conn, String institutionId) {
		
		List<Record> retVal = new ArrayList<Record>();
		try {
			Statement stmt = conn.createStatement();
			String query = "select RECORDID, JMBG, DIRECTPHONES, LOCALPHONES, APVNT from PERSON where RECORDID in (select RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and CFCLASSID like 'belongs to' and RECORDID2 like '" + institutionId + "')";
			ResultSet rset = stmt.executeQuery(query);
			
			while (rset.next()) {				
				String recordId = rset.getString(1);
				String jmbg = rset.getString(2);
				String directPhones = rset.getString(3);
				String localPhones = rset.getString(4);
				String apvnt = rset.getString(5);
				Record rec = super.getRecord(conn, recordId);
				if(rec != null)
					retVal.add(new Person(rec, jmbg, directPhones, localPhones, apvnt));
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read records ");
			log.fatal(ex);
			ex.printStackTrace();
			return null;
		}
	}



	/**
	 * Retrieves a person.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordId
	 *            The person identifier
	 * @param rec
	 *            record with basic information
	 * @return The retrieved person; null if not found or an error occurred.
	 */
	public Record getRecord(Connection conn, String recordId, Record rec) {
		Record retVal = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select JMBG, DIRECTPHONES, LOCALPHONES, APVNT from PERSON where RECORDID like '"
							+ recordId + "'");
			String jmbg = null;
			String directPhones = null;
			String localPhones = null;
			String apvnt = null;
			if (rset.next()) {
				jmbg = rset.getString(1);
				directPhones = rset.getString(2);
				localPhones = rset.getString(3);
				apvnt = rset.getString(4);
			}
			if(rec != null)
				retVal = new Person(rec, jmbg, directPhones, localPhones, apvnt);
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read mARC21Record with id: "
					+ recordId);
			log.fatal(ex);
		}
		return null;
	}


	/**
	 * Retrieves an array of persons.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordIds
	 *            The person identifiers
	 * @return The array of persons
	 */
	@Override
	public List<Record> getRecords(Connection conn, String[] recordIds) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			for (String recordId : recordIds) {
				Record rec = getRecord(conn, recordId);
				retVal.add(rec);
			}
			return retVal;
		} catch (Exception ex) {
			String s = "";
			for (String i : recordIds)
				s += i + ",";
			log
					.fatal("Cannot read multiple persons with identifiers: "
							+ s);
			log.fatal(ex);
		}
		return null;
	}
	

	/**
	 * Adds a new mARC21Record to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param rec
	 *            MARC21Record to add
	 * @return true if successful
	 * @throws SQLException
	 */
	public boolean add(Connection conn, Record rec) {
		try {
			if(super.add(conn, rec)){
				Person person = (Person) rec;
				PreparedStatement stmt = conn
						.prepareStatement("insert into PERSON (RECORDID, JMBG, DIRECTPHONES, LOCALPHONES, APVNT) values (?, ?, ?, ?, ?)");
				stmt.setString(1, person.getMARC21Record().getControlNumber());
				stmt.setString(2, person.getJmbg());
				stmt.setString(3, person.getDirectPhones());
				stmt.setString(4, person.getLocalPhones());
				stmt.setString(5, person.getApvnt());
				stmt.executeUpdate();
				stmt.close();
				return true;
			}
		
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add Person: " + rec);
		}
		return false;
	}

	/**
	 * Updates the Person in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param rec
	 *            Person to update
	 * @return true if successful
	 */
	@Override
	public boolean update(Connection conn, Record rec) {
		try {
			Person person = (Person) rec;
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select JMBG, DIRECTPHONES, LOCALPHONES, APVNT from PERSON where RECORDID like '"
							+ person.getMARC21Record().getControlNumber() + "'");
			PreparedStatement pstmt = null;
			if (rset.next()) 
				pstmt = conn
					.prepareStatement("update PERSON set JMBG=?,  DIRECTPHONES=?,  LOCALPHONES=?, APVNT=? where RECORDID like ?");
			else
				pstmt = conn
					.prepareStatement("insert into PERSON (JMBG, DIRECTPHONES, LOCALPHONES, APVNT, RECORDID) values (?, ?, ?, ?, ?)");
			rset.close();
			stmt.close();
			pstmt.setString(1, person.getJmbg());
			pstmt.setString(2, person.getDirectPhones());
			pstmt.setString(3, person.getLocalPhones());
			pstmt.setString(4, person.getApvnt());
			pstmt.setString(5, person.getMARC21Record().getControlNumber());
			pstmt.executeUpdate();
			pstmt.close();
			return super.update(conn, rec);
		} catch (SQLException ex) {
			log.fatal("Cannot update record: " + rec);
			log.fatal(ex);
		}
		return false;
	}


//	/**
//	 * Deletes a Person from the database.
//	 * 
//	 * @param conn
//	 *            Database connection
//	 * @param rec
//	 *            Person to delete
//	 * @return true if successful
//	 */
//	@Override
//	public boolean delete(Connection conn, Record rec) {
//		return delete(conn, rec.getMARC21Record().getControlNumber());
//	}
//
//	/**
//	 * Deletes a mARC21Record from the database.
//	 * 
//	 * @param conn
//	 *            Database connection
//	 * @param recordId
//	 *            MARC21Record identifier
//	 * @return true if successful
//	 */
//	@Override
//	public boolean delete(Connection conn, String recordId) {
//		try {
//			Statement stmt = conn.createStatement();
//			stmt
//					.executeUpdate("delete from PERSON where RECORDID like '"
//							+ recordId + "'");
//			stmt.close();
//			return super.delete(conn, recordId);
//		} catch (SQLException ex) {
//			log.fatal("Cannot delete Person with control number: "
//					+ recordId);
//			log.fatal(ex);
//			return false;
//		}
//	}

	private static Log log = LogFactory.getLog(PersonDB.class.getName());
}
