package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;

import rs.ac.uns.ftn.informatika.bibliography.db.FileDB;
import rs.ac.uns.ftn.informatika.bibliography.db.LockException;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixValue;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author chenejac@uns.ac.rs
 */
public class RecordDAO {
	
	static {
		RecordDAO.log = LogFactory.getLog(RecordDAO.class.getName());
		try{
			RecordDAO.indexer = new Indexer(Retriever.getIndexPath(), new CrisAnalyzer());
		} catch (Throwable ex){
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
			String luceneIndex = rb.getString("luceneIndex");
			RecordDAO.indexer = new Indexer(luceneIndex, new CrisAnalyzer());
		}
		dataSource = DataSourceFactory.getDataSource();
	}
	
	public RecordDAO(RecordDB storage) {
		this.storage = storage;
	}
			
	/**
	 * Retrieves a list of records that correspond to the query.
	 * 
	 * @param @param query
	 *            query for retrieving records
	 * @return list of records that correspond to the query
	 */
	public List<Record> getDTOs(Query query, HitCollector hitCollector){
		return Retriever.select(query, hitCollector);
	}

	/**
	 * Retrieves a DTO which correspond to mARC21Record with controlNumber passed as parameter.
	 * 
	 * @param controlNumber
	 *            controlNumber of mARC21Record
	 * @return mARC21Record
	 */
	public RecordDTO getDTO(String controlNumber) {
		Record record = getRecord(controlNumber);
		if(record != null){
			if(record.getDto().getControlNumber() == null){
				if(!record.loadDTOFromMARC21())
					return null;
			}
			
			return record.getDto();
		}else {
			return null;
		}
	}
	
	/**
	 * Retrieves a mARC21Record with controlNumber passed as parameter.
	 * 
	 * @param controlNumber
	 *            controlNumber of mARC21Record
	 * @return mARC21Record
	 */
	public Record getRecord(String controlNumber) {
		Record retVal = null;
		try {
			TermQuery query = new TermQuery(new Term("CN", controlNumber));
			List<Record> list = getDTOs(query, new TopDocCollector(1));
			if ((list != null) && (list.size() > 0)) {
				retVal = list.get(0);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return retVal;
	}
	
	/**
	 * Retrieves a mARC21Records with controlNumbers passed as parameter.
	 * 
	 * @param controlNumbers
	 *            controlNumbers of mARC21Records
	 * @return List of mARC21Record
	 */
	public List<Record> getRecords(List<String> controlNumbers) {
		List<Record> retVal = new ArrayList<Record>();
		try {
			BooleanQuery query = new BooleanQuery();
			for (String controlNumber : controlNumbers) {
				TermQuery termQuery = new TermQuery(new Term("CN", controlNumber));
				query.add(termQuery, Occur.SHOULD);
			}
			if(controlNumbers.size() != 0)
				retVal = getDTOs(query, new AllDocCollector(false));
		} catch (Exception e) {
			log.error(e);
		}
		return retVal;
	}

	public List<FileDTO> getFilesFromDatabase(String controlNumber) {
		Connection conn = null;
		List<FileDTO> retVal = new ArrayList<FileDTO>();
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = FileDB.getFilesByRecordControlNumber(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	public Record getRecordFromDatabase(String controlNumber) {
		Connection conn = null;
		Record retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getRecord(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	public List<RecordRecord> getRelationsThisRecordOtherRecords(String controlNumber) {
		Connection conn = null;
		List<RecordRecord> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getRelationsThisRecordOtherRecords(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	public List<RecordRecord> getRelationsOtherRecordsThisRecord(String controlNumber) {
		Connection conn = null;
		List<RecordRecord> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getRelationsOtherRecordsThisRecord(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public Query getInstitutionRecordsQuery(String institutionId, String startDate){
		Connection conn = null;
		BooleanQuery query = new BooleanQuery();
		if(storage instanceof PersonDB){
			
			try {
				conn = RecordDAO.dataSource.getConnection();
				List<String> listAuthors = ((PersonDB)storage).getInstitutionRecordsIds(conn, institutionId, startDate);	
				for (String controlNumber : listAuthors) {
					TermQuery termQuery = new TermQuery(new Term("CN", controlNumber));
					query.add(termQuery, Occur.SHOULD);
				}
			} catch (SQLException ex) {
				log.fatal(ex);
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return query;
	}
	
	public List<Record> getRecordsIdsFromDatabaseByWhereClause(String query){
		Connection conn = null;
		List<Record> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			List<String> controlNumbers = storage.getRecordsIdsByWhereClause(conn, query);
			if(controlNumbers != null){
				retVal = getRecords(controlNumbers);
			}
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public List<Record> getRecordsFromDatabaseByWhereClause(String query){
		Connection conn = null;
		List<Record> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getRecordsByWhereClause(conn, query);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	

	public List<Record> getRecordsFromDatabaseOfCertainTypeWithinDateRange(String[] types, java.util.Date startDate, java.util.Date endDate) {
		Connection conn = null;
		List<Record> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getAllRecordsOfCertainTypeWithinDateRange(conn, types, startDate, endDate);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public List<Record> getRecordsFromDatabaseOfCertainTypeWithinDateRangeWhoAreReletedToRecords(String[] types, java.util.Date startDate, java.util.Date endDate, List<String> relatedToRecordsID) {
		Connection conn = null;
		List<Record> retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getAllRecordsOfCertainTypeWithinDateRangeWhoAreReletedToRecords(conn, types, startDate, endDate, relatedToRecordsID);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * Retrieves a mARC21Record with an exclusive lock for editing.
	 * 
	 * @param controlNumber
	 *            MARC21Record control number
	 * @param user
	 *            User trying to lock the mARC21Record
	 * @return The locked mARC21Record if the mARC21Record is not already locked else null
	 */
	public Record getAndLock(String controlNumber, String user) {
		Connection conn = null;
		Record retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.getAndLock(conn, controlNumber, user);
		} catch (SQLException ex) {
			log.fatal(ex);
		} catch (LockException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	/**
	 * Locks the given mARC21Record.
	 * 
	 * @param controlNumber
	 *            MARC21Record control number
	 * @param user
	 *            User trying to lock the mARC21Record
	 * @return The user that owns the lock
	 */
	public String lock(String controlNumber, String user) {
		Connection conn = null;
		String retVal = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			retVal = storage.lock(conn, controlNumber, user);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	/**
	 * Releases the exclusive lock from the given mARC21Record.
	 * 
	 * @param controlNumber
	 *            MARC21Record control number
	 */
	public void unlock(String controlNumber) {
		Connection conn = null;
		try {
			conn = RecordDAO.dataSource.getConnection();
			storage.unlock(conn, controlNumber);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * Adds a new mARC21Record to the database and lucene Document to lucene index
	 * files.
	 * 
	 * @param rec
	 *            MARC21Record to add
	 * @return true if successful else false
	 */
	public boolean add(Record rec) {
		Connection conn = null;
		boolean retVal = true;
		try {
//			System.out.println("RecorDAO - method add - 1");
			conn = RecordDAO.dataSource.getConnection();
			if (storage.add(conn, rec) == false) {
//				System.out.println("RecorDAO - method add - 1-GRESKA");
				retVal = false;
				conn.rollback();
			} else {
//				System.out.println("RecorDAO - method add - 2");
				if (addIndex(rec)){
					conn.commit();
				}else{
//					System.out.println("RecorDAO - method add - 2-GRESKA");
					retVal = false;
					conn.rollback();
				}
//				System.out.println("RecorDAO - method add - 2-END");
			}
		} catch (SQLException ex) {
//			System.out.println("RecorDAO - method add - 3-GRESKA");
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
			// } catch (JMSException e) {
			// log.fatal(e);
			// retVal = false;
			// if(conn != null){
			// try {
			// conn.rollback();
			// } catch (SQLException ex) {
			// log.fatal(e);
			// }
			// }
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * Adds the mARC21Record in the database without commit.
	 * 
	 * @param rec
	 *            MARC21Record to update
	 * 
	 * @return true if successful else false
	 */
	boolean add(Connection conn, Record rec) {
		return storage.add(conn, rec);
	}


	/**
	 * Updates the mARC21Record in the database and lucene Document in lucene index
	 * files.
	 * 
	 * @param rec
	 *            MARC21Record to update
	 * @return true if successful else false
	 */
	public boolean update(Record rec) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RecordDAO.dataSource.getConnection();
			rec.getModifier();
			if (storage.update(conn, rec) == false) {
				retVal = false;
				conn.rollback();
			} else {
				if (updateIndex(rec)){
					conn.commit();
				}
				else{
					retVal = false;
					conn.rollback();
				}
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
			// } catch (JMSException e) {
			// log.fatal(e);
			// retVal = false;
			// if(conn != null){
			// try {
			// conn.rollback();
			// } catch (SQLException ex) {
			// log.fatal(e);
			// }
			// }
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}

	/**
	 * Updates the mARC21Record in the database without commit.
	 * 
	 * @param rec
	 *            MARC21Record to update
	 * 
	 * @return true if successful else false
	 */
	boolean update(Connection conn, Record rec) {
		return storage.update(conn, rec);
	}

	/**
	 * Deletes a mARC21Record from the database.
	 * 
	 * @param rec
	 *            MARC21Record to delete
	 * @return true if successful else false
	 */
	public boolean delete(Record rec) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = RecordDAO.dataSource.getConnection();
			if(rec.getDto().getRelatedRecords().size() != 0)
				return false;
			if (storage.delete(conn, rec) == false) {
				retVal = false;
				conn.rollback();
			} else {
				if (deleteIndex(rec))
				{
					conn.commit();
				}
				else
				{
					conn.rollback();
				}
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
			// }catch (JMSException e) {
			// log.fatal(e);
			// retVal = false;
			// if(conn != null){
			// try {
			// conn.rollback();
			// } catch (SQLException ex) {
			// log.fatal(e);
			// }
			// }
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}


	/**
	 * Reindex lucene Document in lucene index files.
	 * 
	 * @param controlNumber
	 *            Control Number of mARC21Record that should be reindexed
	 * @return true if successful else false
	 */
	public boolean reindex(String controlNumber) {
		Record rec = getRecordFromDatabase(controlNumber);
		if (rec == null) {
			log.warn("MARC21Record with control number " + controlNumber
					+ " does not exist. Not reindexed.");
			return false;
		} else {
			if(rec.getDto().getControlNumber() == null){
				if(!rec.loadDTOFromMARC21()){
					log.warn("MARC21Record with control number " + controlNumber
							+ " cannot be converted to DTO.");
					return false;
				}
			}
		}
		// try {
		return updateIndex(rec);
		// } catch (JMSException e) {
		// log.fatal(e);
		// log.fatal("Reindexing failed for mARC21Record with control number: " +
		// controlNumber);
		// }
		// return false;
	}

	// public void sendMessage(String command, Serializable obj) throws
	// JMSException {
	// ObjectMessage addMessage = session.createObjectMessage();
	// addMessage.setStringProperty("operation", command);
	// addMessage.setObject(obj);
	// producer.send(addMessage);
	// }

	/**
	 * Add lucene Document to lucene index files.
	 * 
	 * @param rec
	 *            MARC21Record that should be indexed
	 * @return true if successful else false
	 */
	boolean addIndex(Record rec) {
//		System.out.println("RecorDAO - metoda addIndex - 1");
		boolean retVal = false;
		if(rec.getArchived() != 100){
			retVal = RecordDAO.indexer.add(rec);
	//		System.out.println("RecorDAO - metoda addIndex - 2");
			if (retVal) {
				log.info("MARC21Record added, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			} else {
				log.error("MARC21Record not added, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			}
		} else 
			retVal = true;
		// sendMessage("add", rec);
		return retVal;
	}

	/**
	 * Reindex lucene Document in lucene index files.
	 * 
	 * @param rec
	 *            MARC21Record that should be reindexed
	 * @return true if successful else false
	 */
	boolean updateIndex(Record rec) {
		boolean retVal = false;
		if(rec.getArchived() != 100){
			if(rec.getDto() instanceof AuthorDTO){
				BooleanQuery query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("UNSDISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("AUCN", rec.getControlNumber())), Occur.MUST);
				
				List<Record> recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0)){
					((AuthorDTO) rec.getDto()).setAuthorUnsDissertations(true);
				}
				else {
					((AuthorDTO) rec.getDto()).setAuthorUnsDissertations(false);
				}
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("UNSDISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("ADCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setAdvisorUnsDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setAdvisorUnsDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("UNSDISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("CCCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setCommissionChairUnsDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setCommissionChairUnsDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("UNSDISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("CMCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setCommissionMemberUnsDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setCommissionMemberUnsDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("PADISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("AUCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setAuthorPaDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setAuthorPaDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("PADISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("ADCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setAdvisorPaDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setAdvisorPaDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("PADISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("CCCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setCommissionChairPaDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setCommissionChairPaDissertations(false);
				
				query = new BooleanQuery();
				query.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.MUST);
				query.add(new TermQuery(new Term("PADISSERTATION", "true")), Occur.MUST);
				query.add(new TermQuery(new Term("CMCN", rec.getControlNumber())), Occur.MUST);
				
				recordsTemp = getDTOs(query, new AllDocCollector(false));
				if((recordsTemp != null) && (recordsTemp.size() > 0))
					((AuthorDTO) rec.getDto()).setCommissionMemberPaDissertations(true);
				else
					((AuthorDTO) rec.getDto()).setCommissionMemberPaDissertations(false);
			}
			retVal = RecordDAO.indexer.update(rec);
			if (retVal) {
				log.info("MARC21Record updated, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			} else {
				log.error("MARC21Record not updated, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			}
		} else {
			deleteIndex(rec);
			retVal = true;
		}
		// sendMessage("update", rec);
		return retVal;
	}


	/**
	 * Delete lucene Document from lucene index files.
	 * 
	 * @param rec
	 *            MARC21Record that should be deleted
	 * @return true if successful else false
	 */
	boolean deleteIndex(Record rec) {
		boolean retVal = false;
		retVal = RecordDAO.indexer.delete(rec);
		if(rec.getArchived() != 100){
			if (retVal) {
				log.info("MARC21Record deleted, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			} else {
				log.error("MARC21Record not deleted, Control number: "
						+ rec.getMARC21Record().getControlNumber());
			}
		} else 
			retVal = true;
		// sendMessage("delete", rec.getRecord().getControlNumber());
		return retVal;
	}

	public void optimizeIndexer(){
		indexer.optimize();
	}
	
	public boolean update(String controlNumber, List<PrefixValue> fields){
		boolean retVal = false;
		retVal = RecordDAO.indexer.update(controlNumber, fields);
		if (retVal) {
			log.info("MARC21Record updated, Control number : "
					+ controlNumber);
		} else {
			log.error("MARC21Record not updated, Control number : "
					+ controlNumber);
		}
		return retVal;
	}
	
	public static DataSource dataSource;
	
	protected static Indexer indexer;
	
	
	protected RecordDB storage;

	protected static Log log;

}
