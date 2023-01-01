package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordResultPublication;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21RecordFactory;

/**
 * Class for persist and retrieve data about bibliographic and authority records
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class RecordDB {

	public FileDB fileDB = new FileDB();
	
	public static String ORGANIZATION = "BISIS";
	
	public static int DELETEDNUMBER = 100;

	/**
	 * Retrieves a new control number.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The new control number.
	 */
	public String getNewControlNumber(Connection conn) {
		return "(" + RecordDB.ORGANIZATION + ")"
				+ CounterDB.getNewId(conn, "recordid");
	}
	
	/**
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The array of records.
	 */
	public List<Record> getAllRecords(Connection conn) {
		List<Record> retVal = new ArrayList<Record>();
		PersonDB personDB = new PersonDB();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID, SUBSTRING_INDEX(RECORDID, ')', -1) + 0 as RECORDID_COL_NUM from MARC21RECORD where ARCHIVED<>" + RecordDB.DELETEDNUMBER + " order by RECORDID_COL_NUM");
			// where (RECORDID like '(BISIS)15%' or RECORDID like '(BISIS)16%') and DATECREATED >= '2011-06-22 00:00:00'        where RECORDID like '(BISIS)1'
			while (rset.next()) {
				String recordId = rset.getString(1);
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				
				Record record = null;
				record = new Record(creator, creationDate, modifier,
					lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), null, null, getRecordKeywords(conn, recordId));
				record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				if(type.startsWith("pers")){
					record = personDB.getRecord(conn, recordId, record);
				} 
				retVal.add(record);
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
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @return The array of records.
	 */
	public List<Record> getAllRecordsOfCertainType(Connection conn, String[] types) {
		List<Record> retVal = new ArrayList<Record>();
		if((types == null) || (types.length == 0))
			return retVal;
		PersonDB personDB = new PersonDB();
		try {
			Statement stmt = conn.createStatement();
			StringBuffer query = new StringBuffer("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID, SUBSTRING_INDEX(RECORDID, ')', -1) + 0 as RECORDID_COL_NUM from MARC21RECORD where ARCHIVED<>" + RecordDB.DELETEDNUMBER + " AND RECORDID in (select RECORDID from MARC21RECORD_CLASS where COMMISSIONID=0 and CFCLASSSCHEMEID like 'type' and (");
			for (int i=0; i < types.length; i++) {
				String type = types[i];
				query.append(((i!=0)?(" or "):("")) + "CFCLASSID like '" + type + "' ");
			}
			query.append(")) order by RECORDID_COL_NUM"); //LIMIT 4300, 3000
			ResultSet rset = stmt
					.executeQuery(query.toString());
			// where (RECORDID like '(BISIS)15%' or RECORDID like '(BISIS)16%') and DATECREATED >= '2011-06-22 00:00:00'        where RECORDID like '(BISIS)1'
			while (rset.next()) {
				String recordId = rset.getString(1);
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				Record record = null;
				record = new Record(creator, creationDate, modifier,
						lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), null, null, getRecordKeywords(conn, recordId));
				record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				if(type.startsWith("pers")){
					record = personDB.getRecord(conn, recordId, record);
				} 
				retVal.add(record);
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
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param types
	 *            record types
	 * @param startDate
	 *            date range start point
	 * @param endDate
	 *            date range end point
	 * @return The array of records.
	 */
	public List<Record> getAllRecordsOfCertainTypeWithinDateRange(Connection conn, String[] types, java.util.Date startDate, java.util.Date endDate) {
		List<Record> retVal = new ArrayList<Record>();
		if((types == null) || (types.length == 0) || (startDate == null) || (endDate == null))
			return retVal;
		PersonDB personDB = new PersonDB();
		try {
			Statement stmt = conn.createStatement();
			java.sql.Date startSQLDate = new java.sql.Date(startDate.getTime());
			java.sql.Date endSQLDate = new java.sql.Date(endDate.getTime());
			StringBuffer query = new StringBuffer("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID, SUBSTRING_INDEX(RECORDID, ')', -1) + 0 as RECORDID_COL_NUM from MARC21RECORD where ARCHIVED<>" + RecordDB.DELETEDNUMBER + " AND DATEMODIFIED >= '" + startSQLDate + "' AND DATEMODIFIED <= '" + endSQLDate + "' AND RECORDID in (select RECORDID from MARC21RECORD_CLASS where COMMISSIONID=0 and CFCLASSSCHEMEID like 'type' and (");
			for (int i=0; i < types.length; i++) {
				String type = types[i];
				query.append(((i!=0)?(" or "):("")) + "CFCLASSID like '" + type + "' ");
			}
			query.append("))  order by RECORDID_COL_NUM");
			ResultSet rset = stmt
					.executeQuery(query.toString());
			// where (RECORDID like '(BISIS)15%' or RECORDID like '(BISIS)16%') and DATECREATED >= '2011-06-22 00:00:00'        where RECORDID like '(BISIS)1'
			while (rset.next()) {
				String recordId = rset.getString(1);
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				Record record = null;
				record = new Record(creator, creationDate, modifier,
						lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), null, null, getRecordKeywords(conn, recordId));
				record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				if(type.startsWith("pers")){
					record = personDB.getRecord(conn, recordId, record);
				} 
				retVal.add(record);
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
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param types
	 *            record types
	 * @param startDate
	 *            date range start point
	 * @param endDate
	 *            date range end point
	 * @return The array of records.
	 */
	public List<Record> getAllRecordsOfCertainTypeWithinDateRangeWhoAreReletedToRecords(Connection conn, String[] types, java.util.Date startDate, java.util.Date endDate, List<String> relatedToRecordsID) {
		List<Record> retVal = new ArrayList<Record>();
		if((types == null) || (types.length == 0) || (startDate == null) || (endDate == null) || (relatedToRecordsID == null) || (relatedToRecordsID.size() == 0))
			return retVal;
		PersonDB personDB = new PersonDB();
		try {
			Statement stmt = conn.createStatement();
			java.sql.Date startSQLDate = new java.sql.Date(startDate.getTime());
			java.sql.Date endSQLDate = new java.sql.Date(endDate.getTime());
			StringBuffer query = new StringBuffer("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID, SUBSTRING_INDEX(RECORDID, ')', -1) + 0 as RECORDID_COL_NUM from MARC21RECORD where ARCHIVED<>" + RecordDB.DELETEDNUMBER + " AND DATEMODIFIED >= '" + startSQLDate + "' AND DATEMODIFIED <= '" + endSQLDate 
					+ "' AND RECORDID in (select RECORDID from MARC21RECORD_CLASS where COMMISSIONID=0 and CFCLASSSCHEMEID like 'type' and (");
			for (int i=0; i < types.length; i++) {
				String type = types[i];
				query.append(((i!=0)?(" or "):("")) + "CFCLASSID like '" + type + "' ");
			}
			query.append(")) AND RECORDID in (select RECORDID1 from MARC21RECORD_MARC21RECORD where (");
			for (int i=0; i < relatedToRecordsID.size(); i++) {
				String relatedRecord = relatedToRecordsID.get(i);
				query.append(((i!=0)?(" or "):("")) + "RECORDID2 like '" + relatedRecord + "' ");
			}
			
			query.append(")) order by RECORDID_COL_NUM");
			ResultSet rset = stmt
					.executeQuery(query.toString());
			// where (RECORDID like '(BISIS)15%' or RECORDID like '(BISIS)16%') and DATECREATED >= '2011-06-22 00:00:00'        where RECORDID like '(BISIS)1'
			while (rset.next()) {
				String recordId = rset.getString(1);
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				Record record = null;
				record = new Record(creator, creationDate, modifier,
						lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), null, null, getRecordKeywords(conn, recordId));
				record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				if(type.startsWith("pers")){
					record = personDB.getRecord(conn, recordId, record);
				} 
				retVal.add(record);
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

	private List<Classification> getRecordClasses(Connection conn, String recordId) {
		List<Classification> retVal = new ArrayList<Classification>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID, RESEARCHAREA from MARC21RECORD_CLASS where RECORDID like '" + recordId + "'");
			while (rset.next()) {
				String cfClassSchemeId = rset.getString(1);
				String cfClassId = rset.getString(2);
				Calendar cfStartDate = null;
				if (rset.getDate(3) != null) {
					cfStartDate = new GregorianCalendar();
					cfStartDate.setTimeInMillis(rset.getDate(3).getTime());
				}
				Calendar cfEndDate = null;
				if (rset.getDate(4) != null) {
					cfEndDate = new GregorianCalendar();
					cfEndDate.setTimeInMillis(rset.getDate(4).getTime());
				}
				Integer commissionId = rset.getInt(5);
				String researchArea = rset.getString(6);
				Classification classification = new Classification(cfClassSchemeId, cfClassId, cfStartDate, cfEndDate, commissionId, researchArea); 
				retVal.add(classification);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read relations between record and classes ");
			log.fatal(ex);
			return null;
		}
	}
	
	public List<RecordRecord> getRelationsThisRecordOtherRecords(Connection conn, String recordId) {
		List<RecordRecord> retVal = new ArrayList<RecordRecord>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RECORDID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE from MARC21RECORD_MARC21RECORD where RECORDID1 like '" + recordId + "' and RECORDID2 not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");
			while (rset.next()) {
				String recordId2 = rset.getString(1);
				String cfClassSchemeId = rset.getString(2);
				String cfClassId = rset.getString(3);
				Calendar cfStartDate = null;
				if (rset.getDate(4) != null) {
					cfStartDate = new GregorianCalendar();
					cfStartDate.setTimeInMillis(rset.getDate(4).getTime());
				}
				Calendar cfEndDate = null;
				if (rset.getDate(5) != null) {
					cfEndDate = new GregorianCalendar();
					cfEndDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				RecordRecord recordRecord = new RecordRecord(recordId2, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate); 
				retVal.add(recordRecord);
			}
			rset.close();
			stmt.close();
			changeRelationsThisRecordResultPublications(conn, recordId, retVal);
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read relations between records");
			log.fatal(ex);
			return null;
		}
	}
	
	private void changeRelationsThisRecordResultPublications(Connection conn,
			String recordId, List<RecordRecord> retVal) throws Exception {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RESPUBLRECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFCOPYRIGHT from MARC21RECORD_RESPUBL where RECORDID like '" + recordId + "' and RESPUBLRECORDID not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");
			while (rset.next()) {
				String recordId2 = rset.getString(1);
				String cfClassSchemeId = rset.getString(2);
				String cfClassId = rset.getString(3);
				Calendar cfStartDate = null;
				if (rset.getDate(4) != null) {
					cfStartDate = new GregorianCalendar();
					cfStartDate.setTimeInMillis(rset.getDate(4).getTime());
				}
				Calendar cfEndDate = null;
				if (rset.getDate(5) != null) {
					cfEndDate = new GregorianCalendar();
					cfEndDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				String cfCopyright = rset.getString(6);
				RecordResultPublication recordResultPublication = new RecordResultPublication(recordId2, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate, cfCopyright); 
				retVal.set(retVal.indexOf(recordResultPublication), recordResultPublication);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal("Cannot read relations between records");
			log.fatal(ex);
			throw ex;
		}
		
	}

	public List<RecordRecord> getRelationsOtherRecordsThisRecord(
			Connection conn, String recordId) {
		List<RecordRecord> retVal = new ArrayList<RecordRecord>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RECORDID1, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE from MARC21RECORD_MARC21RECORD where RECORDID2 like '" + recordId + "' and RECORDID1 not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");
			while (rset.next()) {
				String recordId1 = rset.getString(1);
				String cfClassSchemeId = rset.getString(2);
				String cfClassId = rset.getString(3);
				Calendar cfStartDate = null;
				if (rset.getDate(4) != null) {
					cfStartDate = new GregorianCalendar();
					cfStartDate.setTimeInMillis(rset.getDate(4).getTime());
				}
				Calendar cfEndDate = null;
				if (rset.getDate(5) != null) {
					cfEndDate = new GregorianCalendar();
					cfEndDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				RecordRecord recordRecord = new RecordRecord(recordId1, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate); 
				retVal.add(recordRecord);
			}
			rset.close();
			stmt.close();
			changeRelationsResultPublicationOtherRecords(conn, recordId, retVal);
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read relations between records");
			log.fatal(ex);
			return null;
		}
	}
	
	private void changeRelationsResultPublicationOtherRecords(Connection conn,
			String recordId, List<RecordRecord> retVal) throws Exception {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFCOPYRIGHT from MARC21RECORD_RESPUBL where RESPUBLRECORDID like '" + recordId + "'");
			while (rset.next()) {
				String recordId2 = rset.getString(1);
				String cfClassSchemeId = rset.getString(2);
				String cfClassId = rset.getString(3);
				Calendar cfStartDate = null;
				if (rset.getDate(4) != null) {
					cfStartDate = new GregorianCalendar();
					cfStartDate.setTimeInMillis(rset.getDate(4).getTime());
				}
				Calendar cfEndDate = null;
				if (rset.getDate(5) != null) {
					cfEndDate = new GregorianCalendar();
					cfEndDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				String cfCopyright = rset.getString(6);
				RecordResultPublication recordResultPublication = new RecordResultPublication(recordId2, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate, cfCopyright); 
				retVal.set(retVal.indexOf(recordResultPublication), recordResultPublication);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal("Cannot read relations between records " + recordId);
			log.fatal(ex);
			throw ex;
		}
		
	}
	
	private List<MultilingualContentDTO> getRecordKeywords(Connection conn,
			String recordId) {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFLANGCODE, CFTRANS, CFKEYWORDS from MARC21RECORD_KEYWORDS where RECORDID like '" + recordId + "'");
			while (rset.next()) {
				String cfLangCode = rset.getString(1);
				String cfTrans = rset.getString(2);
				String cfKeywords = rset.getString(3);
				
				MultilingualContentDTO recordKeywords = new MultilingualContentDTO(cfKeywords, cfLangCode, cfTrans); 
				retVal.add(recordKeywords);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read record keywords");
			log.fatal(ex);
			return null;
		}
	}

	/**
	 * Retrieves a mARC21Record.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordId
	 *            The mARC21Record identifier
	 * @return The retrieved mARC21Record; null if not found or an error occured.
	 */
	public Record getRecord(Connection conn, String recordId) {
		Record rec = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID from MARC21RECORD where (ARCHIVED<>" + RecordDB.DELETEDNUMBER + " OR RECORDSTRING like '%hiddenTheses543yte%') AND  RECORDID like '"
							+ recordId + "'");
			if (rset.next()) {
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				rec = new Record(creator, creationDate, modifier,
							lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), null, null, getRecordKeywords(conn, recordId));
				rec.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				if(type.startsWith("pers")){
					PersonDB personDB = new PersonDB();
					rec = personDB.getRecord(conn, recordId, rec);
				}
			}
			rset.close();
			stmt.close();
			return rec;
		} catch (Exception ex) {
			log.fatal("Cannot read mARC21Record with id: "
					+ recordId);
			log.fatal(ex);
			return null;
		}
	}


	/**
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordIds
	 *            The mARC21Record identifiers
	 * @return The array of records
	 */
	public List<Record> getRecords(Connection conn, String[] recordIds) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID from MARC21RECORD where ARCHIVED<>" + RecordDB.DELETEDNUMBER + " AND RECORDID like '?'");
			for (String recordId : recordIds) {
				stmt.setString(1, recordId);
				ResultSet rset = stmt.executeQuery();
				if (rset.next()) {
					String type = rset.getString(2);
					String creator = rset.getString(3);
					String modifier = rset.getString(4);
					Calendar creationDate = null;
					if (rset.getDate(5) != null) {
						creationDate = new GregorianCalendar();
						creationDate.setTimeInMillis(rset.getDate(5).getTime());
					}
					Calendar lastModificationDate = null;
					if (rset.getDate(6) != null) {
						lastModificationDate = new GregorianCalendar();
						lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
					}
					Integer archived = rset.getInt(7);
					String recStr = rset.getString(8);
					MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
					String scopusID = rset.getString(9);
					String ORCID = rset.getString(10);
					Record record = new Record(creator, creationDate, modifier,
							lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), 
							null, null, getRecordKeywords(conn, recordId));
					record.setMARC21Record(mARC21Record);
					record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
					retVal.add(record);
				}
				rset.close();
			}
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			String s = "";
			for (String i : recordIds)
				s += i + ",";
			log
					.fatal("Cannot read multiple records with identifiers: "
							+ s);
			log.fatal(ex);
			return null;
		}
	}
	
	/**
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param whereClause
	 *            The where clause
	 * @return The array of records
	 */
	public List<Record> getRecordsByWhereClause(Connection conn, String whereClause) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("select RECORDID, TYPE, CREATOR, MODIFIER, DATECREATED, DATEMODIFIED, ARCHIVED, RECORDSTRING, SCOPUSID, ORCID from MARC21RECORD where " + whereClause);
			ResultSet rset = stmt.executeQuery();
			while(rset.next()) {
				String recordId = rset.getString(1);
				String type = rset.getString(2);
				String creator = rset.getString(3);
				String modifier = rset.getString(4);
				Calendar creationDate = null;
				if (rset.getDate(5) != null) {
					creationDate = new GregorianCalendar();
					creationDate.setTimeInMillis(rset.getDate(5).getTime());
				}
				Calendar lastModificationDate = null;
				if (rset.getDate(6) != null) {
					lastModificationDate = new GregorianCalendar();
					lastModificationDate.setTimeInMillis(rset.getDate(6).getTime());
				}
				Integer archived = rset.getInt(7);
				String recStr = rset.getString(8);
				MARC21Record mARC21Record = MARC21RecordFactory.fromFullFormatString(recStr);
				String scopusID = rset.getString(9);
				String ORCID = rset.getString(10);
				Record record = new Record(creator, creationDate, modifier,
						lastModificationDate, archived, type, scopusID, ORCID, mARC21Record, getRecordClasses(conn, recordId), 
						null, null, getRecordKeywords(conn, recordId));
				record.setMARC21Record(mARC21Record);
				record.setFiles(FileDB.getFilesByRecordControlNumber(conn, recordId));
				retVal.add(record);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			String s = "";
			log	.fatal("Cannot read multiple records with where clause: "
							+ s);
			log.fatal(ex);
			return null;
		}
	}
	
	/**
	 * Retrieves an array of records.
	 * 
	 * @param conn
	 *            Database connection
	 * @param whereClause
	 *            The where clause
	 * @return The array of records
	 */
	public List<String> getRecordsIdsByWhereClause(Connection conn, String whereClause) {
		List<String> retVal = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("select RECORDID from MARC21RECORD where " + whereClause);
			ResultSet rset = stmt.executeQuery();
			while(rset.next()) {
				String recordId = rset.getString(1);
				retVal.add(recordId);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			String s = "";
			log	.fatal("Cannot read multiple records with where clause: "
							+ s);
			log.fatal(ex);
			return null;
		}
	}

	
	/**
	 * Retrieves a mARC21Record with an exclusive lock for editing.
	 * 
	 * @param conn
	 *            Database connection
	 * @param controlNumber
	 *            MARC21Record control number
	 * @param user
	 *            User trying to lock the mARC21Record
	 * @return The locked mARC21Record
	 * @throws LockException
	 *             if the mARC21Record is already locked
	 */
	public Record getAndLock(Connection conn, String controlNumber,
			String user) throws LockException {
		String inUseBy = lock(conn, controlNumber, user);
		if (inUseBy.equals(user))
			return getRecord(conn, controlNumber);
		else
			throw new LockException(inUseBy);
	}

	/**
	 * Locks the given mARC21Record.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordId
	 *            MARC21Record identifier
	 * @param user
	 *            User trying to lock the mARC21Record
	 * @return The user that owns the lock
	 */
	public String lock(Connection conn, String recordId, String user) {
		String inUseBy = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select INUSEBY from MARC21RECORD where RECORDID like '"
							+ recordId + "' for update");
			if (rset.next()) {
				inUseBy = rset.getString(1);
				if (inUseBy == null || rset.wasNull()) {
					PreparedStatement pstmt = conn
							.prepareStatement("update MARC21RECORD set INUSEBY=? where RECORDID=?");
					pstmt.setString(1, user);
					pstmt.setString(2, recordId);
					pstmt.executeUpdate();
					pstmt.close();
					inUseBy = user;
				}
			}
			rset.close();
			stmt.close();
			conn.commit();
			return inUseBy;
		} catch (SQLException ex) {
			log.fatal("User " + user
					+ " cannot lock mARC21Record with identifier: "
					+ recordId);
			log.fatal(ex);
			return null;
		}
	}

	/**
	 * Releases the exclusive lock from the given mARC21Record.
	 * 
	 * @param conn
	 *            Database connection
	 * @param recordId
	 *            MARC21Record identifier
	 */
	public void unlock(Connection conn, String recordId) {
		try {
			Statement stmt = conn.createStatement();
			stmt
					.executeUpdate("update MARC21RECORD set INUSEBY=NULL where RECORDID like '"
							+ recordId + "'");
			stmt.close();
			conn.commit();
		} catch (SQLException ex) {
			log.fatal("Cannot unlock mARC21Record with identifiers: "
					+ recordId);
			log.fatal(ex);
		}
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
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD (RECORDID, TYPE, CREATOR, DATECREATED, DATEMODIFIED, ARCHIVED, INUSEBY, RECORDSTRING, SCOPUSID, ORCID) values (?, ?, ?, ?, ?, ?, NULL, ?, ?, ?)");
			String controlNumber = getNewControlNumber(conn);
			rec.getDto().setControlNumber(controlNumber);
			rec.getMARC21Record().setControlNumber(controlNumber);
			stmt.setString(1, controlNumber);
			stmt.setString(2, rec.getType());
			String creator = rec.getCreator();
			if (creator == null)
				stmt.setNull(3, Types.VARCHAR);
			else
				stmt.setString(3, creator);
			Calendar creationDate = rec.getCreationDate();
			if (creationDate == null){
				stmt.setNull(4, Types.DATE);
				stmt.setNull(5, Types.DATE);
			}
			else {
				stmt.setDate(4, new java.sql.Date(creationDate.getTime()
						.getTime()));
				stmt.setDate(5, new java.sql.Date(creationDate.getTime()
						.getTime()));
			}
			stmt.setInt(6, rec.getArchived());
			stmt.setString(7, MARC21RecordFactory.toFullFormatString(rec.getMARC21Record()));
			stmt.setString(8, rec.getScopusID());
			stmt.setString(9, rec.getORCID());
			stmt.executeUpdate();
			stmt.close();
			
			if(insertClasses(conn, rec))		
				if(insertRelationsThisRecordOtherRecords(conn, rec))
					if(insertRelationsOtherRecordsThisRecord(conn, rec))
						if(insertRecordKeywords(conn, rec)){
							retVal = true;
							if((rec.getFiles()!=null)){
								for (FileDTO fileDTO : rec.getFiles()) {
									if((fileDTO!=null) && (fileDTO.getData() != null) && 
											(fileDTO.getData().length != 0)){
										fileDTO.setControlNumber(controlNumber);
										if(fileDB.add(conn, fileDTO)){
											if(!FileStorage.add(fileDTO)){
												retVal = false;
												break;
											}
										} else {
											retVal = false;
											break;
										}
									} 
								}
							} 
						}
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add mARC21Record: " + rec);
		}
		return retVal;
	}
	
	private boolean insertClasses(Connection conn, Record rec){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_CLASS (RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID, RESEARCHAREA) values (?, ?, ?, ?, ?, ?, ?)");
			
			String controlNumber = rec.getMARC21Record().getControlNumber();
			
			
			for (Classification classification : rec.getRecordClasses()) {
				stmt.setString(1, controlNumber);
				stmt.setString(2, classification.getCfClassSchemeId());
				stmt.setString(3, classification.getCfClassId());
				stmt.setDate(4, new java.sql.Date(classification.getCfStartDate().getTime()
							.getTime()));
				stmt.setDate(5, new java.sql.Date(classification.getCfEndDate().getTime()
						.getTime()));
				stmt.setInt(6, classification.getCommissionId());
				stmt.setString(7, classification.getResearchArea());
				stmt.executeUpdate();
			}
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Record: " + rec.getMARC21Record() + " and classes");
		}
		return false;
	}
	
	private boolean insertRelationsThisRecordOtherRecords(Connection conn, Record rec){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_MARC21RECORD (RECORDID1, RECORDID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?, ?, ?)");
			
			String recordId1 = rec.getMARC21Record().getControlNumber();
			
			for (RecordRecord recordRecord : rec.getRelationsThisRecordOtherRecords()) {
				stmt.setString(1, recordId1);
				stmt.setString(2, recordRecord.getRecordId());
				stmt.setString(3, recordRecord.getCfClassSchemeId());
				stmt.setString(4, recordRecord.getCfClassId());
				stmt.setDate(5, new java.sql.Date(recordRecord.getCfStartDate().getTime()
							.getTime()));
				stmt.setDate(6, new java.sql.Date(recordRecord.getCfEndDate().getTime()
						.getTime()));
				stmt.executeUpdate();
				
				if(recordRecord instanceof RecordResultPublication){
					if(! insertRelationsThisRecordResultPublication(conn, rec, (RecordResultPublication)recordRecord))
						return false;
				}
			}
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Records");
		}
		return false;
	}
	
	private boolean insertRelationsThisRecordResultPublication(Connection conn, Record rec, RecordResultPublication recordResultPublication) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_RESPUBL (RECORDID, RESPUBLRECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFCOPYRIGHT) values (?, ?, ?, ?, ?, ?, ?)");
			
			String recordId1 = rec.getMARC21Record().getControlNumber();
			
			stmt.setString(1, recordId1);
			stmt.setString(2, recordResultPublication.getRecordId());
			stmt.setString(3, recordResultPublication.getCfClassSchemeId());
			stmt.setString(4, recordResultPublication.getCfClassId());
			stmt.setDate(5, new java.sql.Date(recordResultPublication.getCfStartDate().getTime()
						.getTime()));
			stmt.setDate(6, new java.sql.Date(recordResultPublication.getCfEndDate().getTime()
					.getTime()));
			stmt.setString(7, recordResultPublication.getCfCopyright());
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Records");
		}
		return false;
	}

	private boolean insertRelationsOtherRecordsThisRecord(Connection conn, Record rec){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_MARC21RECORD (RECORDID1, RECORDID2, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?, ?, ?)");
			
			String recordId1 = rec.getMARC21Record().getControlNumber();
			for (RecordRecord recordRecord : rec.getRelationsOtherRecordsThisRecord()) {
				stmt.setString(1, recordRecord.getRecordId());
				stmt.setString(2, recordId1);
				stmt.setString(3, recordRecord.getCfClassSchemeId());
				stmt.setString(4, recordRecord.getCfClassId());
				stmt.setDate(5, new java.sql.Date(recordRecord.getCfStartDate().getTime()
							.getTime()));
				stmt.setDate(6, new java.sql.Date(recordRecord.getCfEndDate().getTime()
						.getTime()));
				stmt.executeUpdate();
				if(recordRecord instanceof RecordResultPublication){
					if(! insertRelationsResultPublicationOtherRecord(conn, rec, (RecordResultPublication)recordRecord))
						return false;
				}
			}
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Records");
		}
		return false;
	}
	
	private boolean insertRelationsResultPublicationOtherRecord(Connection conn, Record rec, RecordResultPublication recordResultPublication) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_RESPUBL (RECORDID, RESPUBLRECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, CFCOPYRIGHT) values (?, ?, ?, ?, ?, ?, ?)");
			
			String recordId1 = rec.getMARC21Record().getControlNumber();
			
			stmt.setString(1, recordResultPublication.getRecordId());
			stmt.setString(2, recordId1);
			stmt.setString(3, recordResultPublication.getCfClassSchemeId());
			stmt.setString(4, recordResultPublication.getCfClassId());
			stmt.setDate(5, new java.sql.Date(recordResultPublication.getCfStartDate().getTime()
						.getTime()));
			stmt.setDate(6, new java.sql.Date(recordResultPublication.getCfEndDate().getTime()
					.getTime()));
			stmt.setString(7, recordResultPublication.getCfCopyright());
			
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Records");
		}
		return false;
	}
	
	private boolean insertRecordKeywords(Connection conn, Record rec){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_KEYWORDS (RECORDID, CFLANGCODE, CFTRANS, CFKEYWORDS) values (?, ?, ?, ?)");
			
			String recordId = rec.getMARC21Record().getControlNumber();
			for (MultilingualContentDTO recordKeywords : rec.getRecordKeywords()) {
				stmt.setString(1, recordId);
				stmt.setString(2, recordKeywords.getLanguage());
				stmt.setString(3, recordKeywords.getTransType());
				stmt.setString(4, recordKeywords.getContent());
				stmt.executeUpdate();
			}
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add record keywords");
		}
		return false;
	}

	/**
	 * Updates the mARC21Record in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param rec
	 *            MARC21Record to update
	 * @return true if successful
	 */
	public boolean update(Connection conn, Record rec) {
		boolean retVal = false;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update MARC21RECORD set TYPE=?,  MODIFIER=?,  DATEMODIFIED=?, ARCHIVED=?, INUSEBY=NULL, RECORDSTRING=?, SCOPUSID=?, ORCID=? where RECORDID like ?");
			stmt.setString(1, rec.getType());
			String modifier = rec.getModifier();
			if (modifier == null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, modifier);
			Calendar lastModificationDate = rec.getLastModificationDate();
			if (lastModificationDate == null)
				stmt.setNull(3, Types.DATE);
			else
				stmt.setDate(3, new java.sql.Date(lastModificationDate
						.getTime().getTime()));
			stmt.setInt(4, rec.getArchived());
			stmt.setString(5, MARC21RecordFactory.toFullFormatString(rec.getMARC21Record()));
			stmt.setString(6, rec.getScopusID());
			stmt.setString(7, rec.getORCID());
			stmt.setString(8, rec.getMARC21Record().getControlNumber());
			stmt.executeUpdate();
			stmt.close();
			
			if(updateClasses(conn, rec))		
				if(updateRelationsThisRecordOtherRecords(conn, rec))
					if(updateRelationsOtherRecordsThisRecord(conn, rec))
						if(updateRecordKeywords(conn, rec)){
							retVal = true;
							if(rec.getDeletedFiles() != null){
								for (FileDTO fileDTO : rec.getDeletedFiles()) {
									if(fileDTO.getId() != 0){
										if(fileDB.delete(conn, fileDTO.getId())){
											if(!FileStorage.delete(fileDTO)){
												retVal = false;
												break;
											}
										}else {
											retVal = false;
											break;
										}
									}
								}
							}
							if ((retVal) && (rec.getFiles() != null)){
								for (FileDTO fileDTO : rec.getFiles()) {
									if((fileDTO != null) && (fileDTO.getData() != null) && 
											(fileDTO.getData().length != 0)){
										fileDTO.setControlNumber(rec.getMARC21Record().getControlNumber());
										if(fileDB.add(conn, fileDTO)){
											if(!FileStorage.add(fileDTO)){
												retVal = false;
												break;
											}
										} else {
											retVal = false;
											break;
										}
									} else if((fileDTO != null) && (fileDTO.getId() != 0)){
										if(!fileDB.update(conn, fileDTO)){
											retVal = false;
											break;
										}
									}
								}
							}
						}
		} catch (SQLException ex) {
			log.fatal("Cannot update record: " + rec);
			log.fatal(ex);
		}
		return retVal;
	}
	
	private boolean updateClasses(Connection conn, Record rec){
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from MARC21RECORD_CLASS where RECORDID like '"
							+ rec.getMARC21Record().getControlNumber() + "'");
			stmt.close();
			return insertClasses(conn, rec);
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update relations between mARC21Record: " + rec.getMARC21Record() + " and classes");
		}
		return false;
	}
	
	private boolean updateRelationsThisRecordOtherRecords(Connection conn, Record rec){
		try {
			Statement stmt = conn.createStatement();
//			stmt.executeUpdate("delete from MARC21RECORD_RESPUBL where RECORDID like '"
//						+ rec.getMARC21Record().getControlNumber() + "' and RESPUBLRECORDID not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");
			stmt.executeUpdate("delete from MARC21RECORD_MARC21RECORD where RECORDID1 like '"
								+ rec.getMARC21Record().getControlNumber() + "' and RECORDID2 not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");		
			stmt.close();
			return insertRelationsThisRecordOtherRecords(conn, rec);
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update relations between mARC21Records");
		}
		return false;
	}

	private boolean updateRelationsOtherRecordsThisRecord(Connection conn, Record rec){
		try {
			Statement stmt = conn.createStatement();
//			stmt.executeUpdate("delete from MARC21RECORD_RESPUBL where RESPUBLRECORDID like '"
//					+ rec.getMARC21Record().getControlNumber() + "' and RECORDID not in (select RECORDID from MARC21RECORD where ARCHIVED=100)");
			stmt.executeUpdate("delete from MARC21RECORD_MARC21RECORD where RECORDID2 like '"
							+ rec.getMARC21Record().getControlNumber() + "'  and RECORDID1 not in (select RECORDID from MARC21RECORD where ARCHIVED=100)"); 
			stmt.close();
			return insertRelationsOtherRecordsThisRecord(conn, rec);
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update relations between mARC21Records");
		}
		return false;
	}
	
	private boolean updateRecordKeywords(Connection conn, Record rec){
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from MARC21RECORD_KEYWORDS where RECORDID like '"
							+ rec.getMARC21Record().getControlNumber() + "'");
			stmt.close();
			return insertRecordKeywords(conn, rec);
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot update record keywords");
		}
		return false;
	}

	/**
	 * Deletes a mARC21Record from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param rec
	 *            MARC21Record to delete
	 * @return true if successful
	 */
	public boolean delete(Connection conn, Record rec) {
		boolean retVal = false;
//		if(rec.getFile() != null){
//			if(FileStorage.delete(rec.getFile()))
//				if(fileDB.delete(conn, rec.getFile()))
//					retVal = delete(conn, rec.getMARC21Record().getControlNumber());
//		} else 
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update MARC21RECORD set MODIFIER=?,  DATEMODIFIED=?, ARCHIVED=? where RECORDID like ?");
			String modifier = rec.getModifier();
			if (modifier == null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, modifier);
			Calendar lastModificationDate = rec.getLastModificationDate();
			if (lastModificationDate == null)
				stmt.setNull(2, Types.DATE);
			else
				stmt.setDate(2, new java.sql.Date(lastModificationDate
						.getTime().getTime()));
			stmt.setInt(3, RecordDB.DELETEDNUMBER);
			stmt.setString(4, rec.getControlNumber());
			stmt.executeUpdate();
			stmt.close();
			retVal = true;
		} catch (SQLException ex) {
			log.fatal("Cannot delete mARC21Record with control number: "
					+ rec.getMARC21Record().getControlNumber());
			log.fatal(ex);
			return false;
		}
		return retVal;	
	}

//	/**
//	 * Deletes a mARC21Record from the database.
//	 * 
//	 * @param conn
//	 *            Database connection
//	 * @param recordId
//	 *            MARC21Record identifier
//	 * @return true if successful
//	 */
//	protected boolean delete(Connection conn, String recordId) {
//		try {
//			Statement stmt = conn.createStatement();
//			stmt.executeUpdate("delete from MARC21RECORD where RECORDID like '"
//							+ recordId + "'");
//			stmt.close();
//			return true;
//		} catch (SQLException ex) {
//			log.fatal("Cannot delete mARC21Record with control number: "
//					+ recordId);
//			log.fatal(ex);
//			return false;
//		}
//	}

	private static Log log = LogFactory.getLog(RecordDB.class.getName());

}
