/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.AbstractRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class Record implements Serializable{

	protected String type;
	protected String creator;
	protected String modifier;
	protected Calendar creationDate;
	protected Calendar lastModificationDate;
	protected Integer archived = new Integer(0);
	protected AbstractRecordConverter converter;
	protected RecordDTO dto;
	protected String dtoType;
	
	protected MARC21Record mARC21Record;
	protected List<FileDTO> files;
	protected List<FileDTO> deletedFiles;
	protected String scopusID;
	protected String ORCID;
	
	protected List<Classification> recordClasses;
	protected List<RecordRecord> relationsThisRecordOtherRecords;
	protected List<RecordRecord> relationsOtherRecordsThisRecord;
	protected List<MultilingualContentDTO> recordKeywords;
	
	/**
	 * 
	 */
	public Record() {
		mARC21Record = new MARC21Record();
		recordClasses = new ArrayList<Classification>();
		relationsThisRecordOtherRecords = new ArrayList<RecordRecord>();
		relationsOtherRecordsThisRecord = new ArrayList<RecordRecord>();
		recordKeywords = new ArrayList<MultilingualContentDTO>();
		files = new ArrayList<FileDTO>();
		deletedFiles = new ArrayList<FileDTO>();
	}
	
	/**
	 * @param dto
	 * 
	 */
	public Record(RecordDTO dto) {
		super();
		this.mARC21Record = dto.getRecord().getMARC21Record();
		this.recordClasses = dto.getRecord().getRecordClasses();
		this.relationsOtherRecordsThisRecord = dto.getRecord().getRelationsOtherRecordsThisRecord();
		this.relationsThisRecordOtherRecords = dto.getRecord().getRelationsThisRecordOtherRecords();
		this.recordKeywords = dto.getRecord().getRecordKeywords();
		this.files = dto.getRecord().getFiles();
		this.deletedFiles = dto.getRecord().getDeletedFiles();
		this.scopusID = dto.getScopusID();
		this.ORCID = dto.getORCID();
		setDto(dto);
		loadMARC21FromDTO();
	}
	
	/**
	 * @param creator
	 * @param creationDate
	 * @param modifier
	 * @param lastModificationDate
	 * @param archived
	 * @param type
	 * @param scopusID
	 * @param ORCID
	 * @param mARC21Record
	 * @param recordClasses
	 * @param relationsOtherRecordsThisRecord
	 * @param relationsThisRecordOtherRecords
	 * @param recordKeywords
	 */
	public Record(String creator, Calendar creationDate, String modifier,
			Calendar lastModificationDate, Integer archived, String type, String scopusID, String ORCID, MARC21Record mARC21Record, 
			List<Classification> recordClasses,
			List<RecordRecord> relationsOtherRecordsThisRecord,
			List<RecordRecord> relationsThisRecordOtherRecords,
			List<MultilingualContentDTO> recordKeywords) {
		super();
		this.creator = creator;
		this.creationDate = creationDate;
		this.modifier = modifier;
		this.lastModificationDate = lastModificationDate;
		this.archived = archived;
		this.type = type;
		this.scopusID = scopusID;
		this.ORCID = ORCID;
		this.mARC21Record = mARC21Record;
		setRecordClasses(recordClasses);
		this.relationsOtherRecordsThisRecord = relationsOtherRecordsThisRecord;
		this.relationsThisRecordOtherRecords = relationsThisRecordOtherRecords;
		this.recordKeywords = recordKeywords;
		this.files = new ArrayList<FileDTO>();
		this.deletedFiles = new ArrayList<FileDTO>();
		this.dto = ConverterFactory.getDTO(dtoType);
		this.dto.setRecord(this);
		this.dto.setControlNumber(mARC21Record.getControlNumber());
		this.dto.setNotLoaded(true);
	}
	
	/**
	 * @param creator
	 * @param creationDate
	 * @param modifier
	 * @param lastModificationDate
	 * @param archived
	 * @param type
	 * @param dto
	 * 
	 */
	public Record(String creator, Calendar creationDate, String modifier,
			Calendar lastModificationDate, Integer archived, String type, RecordDTO dto) {
		super();
		this.creator = creator;
		this.creationDate = creationDate;
		this.modifier = modifier;
		this.lastModificationDate = lastModificationDate;
		this.archived = archived;
		this.type = type;
		this.mARC21Record = dto.getRecord().getMARC21Record();
		this.recordClasses = dto.getRecord().getRecordClasses();
		this.relationsOtherRecordsThisRecord = dto.getRecord().getRelationsOtherRecordsThisRecord();
		this.relationsThisRecordOtherRecords = dto.getRecord().getRelationsThisRecordOtherRecords();
		this.recordKeywords = dto.getRecord().getRecordKeywords();
		this.files = dto.getRecord().getFiles();
		this.deletedFiles = dto.getRecord().getDeletedFiles();
		this.scopusID = dto.getScopusID();
		this.ORCID = dto.getORCID();
		setDto(dto);
		loadMARC21FromDTO();
	}
	
	public boolean loadMARC21FromDTO() {
		if(converter != null){
			if(converter.getRecord(dto)){
				return true;
			} else {
				log.error("loadMARC21FromDTO nije uspeo: " + this);
				return false;
			}
		} else
			return false;
	}
	
	public boolean loadDTOFromMARC21() {
		if(dto != null){
			dto.setNotLoaded(false);
		}
		if(converter.getDTO(this)){
			dto.setNotLoaded(false);
			return true;
		} else {
			log.error("loadDTOFromMARC21 nije uspeo: " + this);
			dto.setNotLoaded(true);
			return false;
		}
		
	}
	
	public void loadFromDatabase(){
		loadMARC21FromDatabase();
		loadDTOFromMARC21();
	}
	
	public void loadMARC21FromDatabase(){
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		Record rec = recordDAO.getRecordFromDatabase(dto.getControlNumber());
		this.setMARC21Record(rec.getMARC21Record());
		this.setRecordClasses(rec.getRecordClasses());
		this.setRecordKeywords(rec.getRecordKeywords());
		this.setRelationsOtherRecordsThisRecord(rec.getRelationsOtherRecordsThisRecord());
		this.setRelationsThisRecordOtherRecords(rec.getRelationsThisRecordOtherRecords());
		this.modifier = rec.modifier;
		this.creator = rec.creator;
		this.creationDate = rec.creationDate;
		this.lastModificationDate = rec.lastModificationDate;
		this.archived = rec.archived;
		this.setType(rec.getType());
		this.setFiles(rec.getFiles());
		this.setDeletedFiles(rec.getDeletedFiles());
		this.scopusID = rec.scopusID;
		this.ORCID = rec.ORCID;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the converter
	 */
	public AbstractRecordConverter getConverter() {
		return converter;
	}

	/**
	 * @param converter
	 *            the converter to set
	 */
	public void setConverter(AbstractRecordConverter converter) {
		this.converter = converter;
	}

	/**
	 * @return the dto
	 */
	public RecordDTO getDto() {
		return dto;
	}

	/**
	 * @param dto
	 *            the dto to set
	 */
	public void setDto(RecordDTO dto) {
		this.dto = dto;
		this.dto.setRecord(this);
		this.converter = ConverterFactory.getConverter(dto);
		this.dtoType = ConverterFactory.getConverterType(this.dto);	
	}

	/**
	 * @return the dtoType
	 */
	public String getDtoType() {
		return dtoType;
	}

	/**
	 * @param dtoType the dtoType to set
	 */
	public void setDtoType(String dtoType) {
		this.dtoType = dtoType;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the creationDate
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastModificationDate
	 */
	public Calendar getLastModificationDate() {
		return lastModificationDate;
	}

	/**
	 * @param lastModifiedDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(Calendar lastModifiedDate) {
		this.lastModificationDate = lastModifiedDate;
	}

	/**
	 * @return the archived
	 */
	public Integer getArchived() {
		return archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(Integer archived) {
		this.archived = archived;
	}

	/**
	 * @return the mARC21Record
	 */
	public MARC21Record getMARC21Record() {
		return mARC21Record;
	}

	/**
	 * @param record the mARC21Record to set
	 */
	public void setMARC21Record(MARC21Record record) {
		mARC21Record = record;
	}
	
	/**
	 * @return the files
	 */
	public List<FileDTO> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<FileDTO> files) {
		this.files = files;
	}

	/**
	 * @return the deletedFiles
	 */
	public List<FileDTO> getDeletedFiles() {
		return deletedFiles;
	}

	/**
	 * @param deletedFiles the deletedFiles to set
	 */
	public void setDeletedFiles(List<FileDTO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	/**
	 * @return the recordClasses
	 */
	public List<Classification> getRecordClasses() {
		return recordClasses;
	}

	/**
	 * @param recordClasses the recordClasses to set
	 */
	public void setRecordClasses(List<Classification> recordClasses) {
		this.recordClasses = recordClasses;
		for (Classification classification : this.recordClasses) {
			if((Types.TYPE_SCHEMA.equalsIgnoreCase(classification.getCfClassSchemeId())&&(classification.getCommissionId().equals(new Integer(0))))){ 
				this.dtoType = classification.getCfClassId();
				this.converter = ConverterFactory.getConverter(classification.getCfClassId());
			}
		}
	}
	
	/**
	 * @param recordClasses the string recordClasses to set
	 */
	public void setRecordClasses(String recordClasses) {
		List<Classification> recClassesList = new ArrayList<Classification>();
		String[] recClasses = recordClasses.split("\\|;");
		for (String recClass : recClasses) {
			recClassesList.add(new Classification(recClass));
		}
		setRecordClasses(recClassesList);
	}

	/**
	 * @return the relationsThisRecordOtherRecords
	 */
	public List<RecordRecord> getRelationsThisRecordOtherRecords() {
		return relationsThisRecordOtherRecords;
	}

	/**
	 * @param relationsThisRecordOtherRecords the relationsThisRecordOtherRecords to set
	 */
	public void setRelationsThisRecordOtherRecords(
			List<RecordRecord> relationsThisRecordOtherRecords) {
		this.relationsThisRecordOtherRecords = relationsThisRecordOtherRecords;
	}
	
	/**
	 * @param relationsThisRecordOtherRecords the string representation of relationsOtherRecordsThisRecord to set
	 */
	public void setRelationsThisRecordOtherRecords(String relationsThisRecordOtherRecords) {
		List<RecordRecord> relationsThisRecordOtherRecordsList = new ArrayList<RecordRecord>();
		String[] relThisRecordOtherRecords = relationsThisRecordOtherRecords.split("\\|;");
		for (String relThisRecordOtherRecord : relThisRecordOtherRecords) {
			if(relThisRecordOtherRecord.contains("##&##"))
				relationsThisRecordOtherRecordsList.add(new RecordResultPublication(relThisRecordOtherRecord));
			else
				relationsThisRecordOtherRecordsList.add(new RecordRecord(relThisRecordOtherRecord));
		}
		setRelationsThisRecordOtherRecords(relationsThisRecordOtherRecordsList);
	}

	/**
	 * @return the relationsOtherRecordsThisRecord
	 */
	public List<RecordRecord> getRelationsOtherRecordsThisRecord() {
		return relationsOtherRecordsThisRecord;
	}

	/**
	 * @param relationsOtherRecordsThisRecord the relationsOtherRecordsThisRecord to set
	 */
	public void setRelationsOtherRecordsThisRecord(
			List<RecordRecord> relationsOtherRecordsThisRecord) {
		this.relationsOtherRecordsThisRecord = relationsOtherRecordsThisRecord;
	}
	
	/**
	 * @param relationsOtherRecordsThisRecord the string representation of relationsOtherRecordsThisRecord to set
	 */
	public void setRelationsOtherRecordsThisRecord(String relationsOtherRecordsThisRecord) {
		List<RecordRecord> relationsOtherRecordsThisRecordList = new ArrayList<RecordRecord>();
		String[] relOtherRecordsThisRecord = relationsOtherRecordsThisRecord.split("\\|;");
		for (String relOtherRecordThisRecord : relOtherRecordsThisRecord) {
			if(relOtherRecordThisRecord.contains("##&##"))
				relationsOtherRecordsThisRecordList.add(new RecordResultPublication(relOtherRecordThisRecord));
			else
				relationsOtherRecordsThisRecordList.add(new RecordRecord(relOtherRecordThisRecord));
		}
		setRelationsOtherRecordsThisRecord(relationsOtherRecordsThisRecordList);
	}

	/**
	 * @return the recordKeywords
	 */
	public List<MultilingualContentDTO> getRecordKeywords() {
		return recordKeywords;
	}

	/**
	 * @param thisRecordKeywords the recordKeywords to set
	 */
	public void setRecordKeywords(List<MultilingualContentDTO> thisRecordKeywords) {
		this.recordKeywords = thisRecordKeywords;
	}
	
	/**
	 * @param recordKeywords the string representation of recordKeywords to set
	 */
	public void setRecordKeywords(String recordKeywords) {
		List<MultilingualContentDTO> recKeywordsList = new ArrayList<MultilingualContentDTO>();
		String[] recKeywords = recordKeywords.split("\\|;");
		for (String recKeyword : recKeywords) {
			recKeywordsList.add(new MultilingualContentDTO(recKeyword));
		}
		setRecordKeywords(recKeywordsList);
	}
	
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append("MARC21 Record: \n" + mARC21Record.getPrintableString());
		retVal.append("\nRecord Keywords: \n" + Arrays.toString(recordKeywords.toArray()));
		retVal.append("\nRecord Classes: \n" + Arrays.toString(recordClasses.toArray()));
		retVal.append("\nRelations this record other records: \n" + Arrays.toString(relationsThisRecordOtherRecords.toArray()));
		retVal.append("\nRelations other records this record: \n" + Arrays.toString(relationsOtherRecordsThisRecord.toArray()));
		
		if(dto != null)
			retVal.append("\nDTO: \n" + dto);
		return retVal.toString();
	}
	
	public String getControlNumber(){
		if((dto != null) && (dto.getControlNumber()!=null) && (dto.getControlNumber().trim().length()!=0))
			return dto.getControlNumber();
		else if ((mARC21Record != null) && (mARC21Record.getControlNumber()!=null) && (mARC21Record.getControlNumber().trim().length()!=0))
				return mARC21Record.getControlNumber();
		return null;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		Record temp = (Record) arg0;
		if((temp.getControlNumber() != null) && (this.getControlNumber()!=null))
			if(temp.getControlNumber().equals(this.getControlNumber()))
				return true;
		return false;
	}


	public void clear(){
		type=null;
		creator=null;
		modifier=null;
		creationDate=null;
		lastModificationDate=null;
		archived = null;
		converter=null;
		if(dto!=null){
			dto.setNotLoaded(true);
//			dto=null;
			dtoType=null;
		}
		
		mARC21Record=null;
		files = new ArrayList<FileDTO>();
		deletedFiles = new ArrayList<FileDTO>();
		
		recordClasses=null;
		relationsThisRecordOtherRecords=null;
		relationsOtherRecordsThisRecord=null;
		recordKeywords=null;
	}
	
	
	
	/**
	 * @return the scopusID
	 */
	public String getScopusID() {
		return scopusID;
	}

	/**
	 * @param scopusID the scopusID to set
	 */
	public void setScopusID(String scopusID) {
		this.scopusID = scopusID;
	}
	
	



	/**
	 * @return the oRCID
	 */
	public String getORCID() {
		return ORCID;
	}

	/**
	 * @param oRCID the oRCID to set
	 */
	public void setORCID(String oRCID) {
		ORCID = oRCID;
	}





	protected static Log log = LogFactory.getLog(Record.class.getName());
	
}
