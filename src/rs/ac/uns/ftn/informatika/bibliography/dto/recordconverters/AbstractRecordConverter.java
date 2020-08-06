package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;

/**
 * Abstract class which must be extended by Converter classes which present some type
 * of converter between MARC21 format and RecordDTO classes.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class AbstractRecordConverter implements Serializable {

	public static String DATE_TIME_005_PATTERN = "yyyyMMddhhmmss";
	public static String DATE_008_PATTERN = "yyMMdd";
	public static char NO_ATTEMPT_TO_CODE = '|';
	public static char BLANK = ' ';
	
	/**
	 * Convert the mARC21Record from MARC21 format to DTO format
	 * 
	 * @param rec
	 *            MARC21Record which should be converted
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	public abstract boolean getDTO(Record rec);

	/**
	 * Convert mARC21Record from DTO format to MARC21 format
	 * 
	 * @param dto
	 *            MARC21Record which should be converted
	 * 
	 * @return true if everything is ok, otherwise false
	 */
	public boolean getRecord(RecordDTO dto){
		try {
//			System.out.println("AbstractRecordConverter - method getRecord - 1");
			Record record = dto.getRecord();
//			System.out.println("AbstractRecordConverter - method getRecord - 2");
			
			MARC21Record mARC21Record = new MARC21Record();
			record.setMARC21Record(mARC21Record);
			setDataFields(mARC21Record, dto);
			String controlNumber = dto.getControlNumber();
			setLeader(mARC21Record, dto);
			setControlFields(mARC21Record, controlNumber, dto);
			setRecordClasses(record, dto);
			setRelationsThisRecordOtherRecords(record, dto);
			setRelationsOtherRecordsThisRecord(record, dto);
			record.setFiles(dto.getFiles());
			record.setDeletedFiles(dto.getDeletedFiles());
			/*
			if(dto instanceof RuleBookDTO){
//				System.out.println("AbstractRecord -3");
				record.setFile(((RuleBookDTO)dto).getFile());
				record.setDeletedFile(((RuleBookDTO)dto).getDeletedFile());
//				System.out.println("AbstractRecord -4");
			}
			else if(dto instanceof RuleBookImplementationDTO){
//				System.out.println("AbstractRecord -3");
				record.setFile(((RuleBookDTO)dto).getFile());
				record.setDeletedFile(((RuleBookDTO)dto).getDeletedFile());
//				System.out.println("AbstractRecord -4");
			}
			*/
			record.getMARC21Record().sortFields();
//			System.out.println("AbstractRecordConverter - method getRecord - 3");
			return true;
			
		} catch (Throwable e) {
			log.error("getRecord", e);
			return false;
		}
	}
	
	protected void setRelationsOtherRecordsThisRecord(Record retVal,
			RecordDTO dto) {
	}

	protected void setRelationsThisRecordOtherRecords(Record retVal,
			RecordDTO dto) {
	}

	protected void setRecordClasses(Record retVal, RecordDTO dto) {
		
		Classification type = null;
		for (Classification classification : retVal.getRecordClasses()) {
			if(classification.getCfClassSchemeId().equalsIgnoreCase(Types.TYPE_SCHEMA) && (classification.getCommissionId().equals(new Integer(0)))){
				type = classification;
				break;
			}	
		}	
		
		if(type!=null){
			retVal.getRecordClasses().remove(type);
		}
		Calendar startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		Classification recClass = new Classification(Types.TYPE_SCHEMA, ConverterFactory.getConverterType(dto), startDate, endDate, new Integer(0), null);
		retVal.getRecordClasses().add(recClass);
	}
	
	protected abstract void setDataFields(MARC21Record rec, RecordDTO dto);

	protected abstract void setLeader(MARC21Record rec, RecordDTO dto);
	
	protected abstract void setControlFields(MARC21Record rec, String controlNumber, RecordDTO dto);
	
	protected void deleteRelationsThisRecordOtherRecords(Record retVal, String schemeId, String classId){
		
		List<RecordRecord> forDeleting = new ArrayList<RecordRecord>();
		for (RecordRecord existingRecordRecord : retVal.getRelationsThisRecordOtherRecords()) {
				if(existingRecordRecord.getCfClassSchemeId().equals(schemeId)){
					if(existingRecordRecord.getCfClassId().equals(classId)){
						forDeleting.add(existingRecordRecord);
					}
				}
		}
		for (RecordRecord  existingRecordRecord: forDeleting) {
			retVal.getRelationsThisRecordOtherRecords().remove(existingRecordRecord);
		}
		
	}
	
	
	protected void deleteRelationsOtherRecordsThisRecord(Record retVal, String schemeId, String classId){
		
		List<RecordRecord> forDeleting = new ArrayList<RecordRecord>();
		for (RecordRecord existingRecordRecord : retVal.getRelationsOtherRecordsThisRecord()) {
				if(existingRecordRecord.getCfClassSchemeId().equals(schemeId)){
					if(existingRecordRecord.getCfClassId().equals(classId)){
						forDeleting.add(existingRecordRecord);
					}
				}
		}
		for (RecordRecord  existingRecordRecord: forDeleting) {
			retVal.getRelationsOtherRecordsThisRecord().remove(existingRecordRecord);
		}
		
	}	
		
	private static Log log = LogFactory.getLog(AbstractRecordConverter.class.getName());
}
