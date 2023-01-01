/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ConverterFactory;


/**
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class Person extends Record {

	private String jmbg;
	private String directPhones;
	private String localPhones;
	private String apvnt; //APVNT
	
	public Person(){
		super();
	}
	
	/**
	 * @param RecordDTO
	 * 
	 */
	public Person(RecordDTO dto) {
		this.mARC21Record = dto.getRecord().getMARC21Record();
		this.recordClasses = dto.getRecord().getRecordClasses();
		this.relationsOtherRecordsThisRecord = dto.getRecord().relationsOtherRecordsThisRecord;
		this.relationsThisRecordOtherRecords = dto.getRecord().relationsThisRecordOtherRecords;
		this.recordKeywords = dto.getRecord().getRecordKeywords();
		this.files = dto.getRecord().getFiles();
		this.deletedFiles = dto.getRecord().getDeletedFiles();
		this.scopusID = dto.getScopusID();
		this.ORCID = dto.getORCID();
		if(dto instanceof AuthorDTO){
			this.jmbg = ((AuthorDTO)dto).getJmbg();
			this.directPhones = ((AuthorDTO)dto).getDirectPhones();
			this.localPhones = ((AuthorDTO)dto).getLocalPhones();
			this.apvnt = ((AuthorDTO)dto).getApvnt();
		}
		setDto(dto);
		loadMARC21FromDTO();
	}
	
//	public Person(Record record){
//		this.creator = record.creator;
//		this.creationDate = record.creationDate;
//		this.modifier = record.modifier;
//		this.lastModificationDate = record.lastModificationDate;
//		this.type = record.type;
//		this.mARC21Record = record.mARC21Record;
//		this.dto = record.dto;
//		this.recordClasses = record.recordClasses;
//		this.converter = record.converter;
//		this.relationsOtherRecordsThisRecord = record.relationsOtherRecordsThisRecord;
//		this.relationsThisRecordOtherRecords = record.relationsThisRecordOtherRecords;
//		this.recordKeywords = record.recordKeywords;
//		this.dto.setRecord(this);
//	}
	
	public Person(Record record, String jmbg, String directPhones, String localPhones, String apvnt){
		this.creator = record.creator;
		this.creationDate = record.creationDate;
		this.modifier = record.modifier;
		this.lastModificationDate = record.lastModificationDate;
		this.type = record.type;
		this.mARC21Record = record.mARC21Record;
		this.dto = record.dto;
		this.recordClasses = record.recordClasses;
		this.converter = record.converter;
		this.relationsOtherRecordsThisRecord = record.relationsOtherRecordsThisRecord;
		this.relationsThisRecordOtherRecords = record.relationsThisRecordOtherRecords;
		this.recordKeywords = record.recordKeywords;
		this.files = record.files;
		this.deletedFiles = record.deletedFiles;
		this.scopusID = record.scopusID;
		this.ORCID = record.ORCID;
		this.jmbg = jmbg;
		this.directPhones = directPhones;
		this.localPhones = localPhones;
		this.apvnt = apvnt;
		this.dto.setRecord(this);
	}
	
	/**
	 * @param creator
	 * @param creationDate
	 * @param modifier
	 * @param lastModificationDate
	 * @param archived
	 * @param type
	 * @param AbstractDTO
	 * 
	 */
	public Person(String creator, Calendar creationDate, String modifier,
			Calendar lastModificationDate, Integer archived, String type, RecordDTO dto, String jmbg, String directPhones, String localPhones, String apvnt){
		this.creator = creator;
		this.creationDate = creationDate;
		this.modifier = modifier;
		this.lastModificationDate = lastModificationDate;
		this.archived = archived;
		this.type = type;
		this.mARC21Record = dto.getRecord().getMARC21Record();
		this.recordClasses = dto.getRecord().getRecordClasses();
		this.relationsOtherRecordsThisRecord = dto.getRecord().relationsOtherRecordsThisRecord;
		this.relationsThisRecordOtherRecords = dto.getRecord().relationsThisRecordOtherRecords;
		this.recordKeywords = dto.getRecord().getRecordKeywords();
		this.files = dto.getRecord().getFiles();
		this.deletedFiles = dto.getRecord().getDeletedFiles();
		this.jmbg = jmbg;
		this.directPhones = directPhones;
		this.localPhones = localPhones;
		this.apvnt = apvnt;
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
	 * @param AbstractDTO
	 * 
	 */
	public Person(String creator, Calendar creationDate, String modifier,
			Calendar lastModificationDate, Integer archived, String type, RecordDTO dto) {
		this.creator = creator;
		this.creationDate = creationDate;
		this.modifier = modifier;
		this.lastModificationDate = lastModificationDate;
		this.archived = archived;
		this.type = type;
		this.mARC21Record = dto.getRecord().getMARC21Record();
		this.recordClasses = dto.getRecord().getRecordClasses();
		this.relationsOtherRecordsThisRecord = dto.getRecord().relationsOtherRecordsThisRecord;
		this.relationsThisRecordOtherRecords = dto.getRecord().relationsThisRecordOtherRecords;
		this.recordKeywords = dto.getRecord().getRecordKeywords();
		this.files = dto.getRecord().getFiles();
		this.deletedFiles = dto.getRecord().getDeletedFiles();
		this.scopusID = dto.getScopusID();
		this.ORCID = dto.getORCID();
		setDto(dto);
		loadMARC21FromDTO();
	}
	
	
	/**
	 * @return the jmbg
	 */
	public String getJmbg() {
		return jmbg;
	}
	/**
	 * @param jmbg the jmbg to set
	 */
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	/**
	 * @return the directPhones
	 */
	public String getDirectPhones() {
		return directPhones;
	}
	/**
	 * @param directPhones the directPhones to set
	 */
	public void setDirectPhones(String directPhones) {
		this.directPhones = directPhones;
	}
	/**
	 * @return the localPhones
	 */
	public String getLocalPhones() {
		return localPhones;
	}
	/**
	 * @param localPhones the localPhones to set
	 */
	public void setLocalPhones(String localPhones) {
		this.localPhones = localPhones;
	}
	
	/**
	 * @return the apvnt
	 */
	public String getApvnt() {
		return apvnt;
	}
	/**
	 * @param apvnt the apvnt to set
	 */
	public void setApvnt(String apvnt) {
		this.apvnt = apvnt;
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
		if(dto != null)
			dto.setNotLoaded(false);
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
		Person rec = (Person)recordDAO.getRecordFromDatabase(dto.getControlNumber());
		this.setMARC21Record(rec.getMARC21Record());
		this.setRecordClasses(rec.getRecordClasses());
		this.setRecordKeywords(rec.getRecordKeywords());
		this.setRelationsOtherRecordsThisRecord(rec.relationsOtherRecordsThisRecord);
		this.setRelationsThisRecordOtherRecords(rec.relationsThisRecordOtherRecords);
		this.setType(rec.getType());
		this.setFiles(rec.getFiles());
		this.setDeletedFiles(rec.getDeletedFiles());
		this.setJmbg(rec.getJmbg());
		this.setDirectPhones(rec.getDirectPhones());
		this.setLocalPhones(rec.getLocalPhones());
		this.setApvnt(rec.getApvnt());
		this.modifier = rec.modifier;
		this.creator = rec.creator;
		this.creationDate = rec.creationDate;
		this.lastModificationDate = rec.lastModificationDate;
		this.archived = rec.archived;
		this.scopusID = rec.scopusID;
		this.ORCID = rec.ORCID;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}
	
	protected static Log log = LogFactory.getLog(Person.class.getName());
	
}
