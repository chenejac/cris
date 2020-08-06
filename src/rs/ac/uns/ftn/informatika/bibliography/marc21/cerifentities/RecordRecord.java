/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class RecordRecord implements Serializable{

	protected Record record;
	protected String recordId;
	protected String cfClassSchemeId;
	protected String cfClassId;
	protected Calendar cfStartDate;
	protected Calendar cfEndDate;
	
	/**
	 * @param allInformation
	 */
	public RecordRecord(String allInformation) {
		parseString(allInformation);
	}

	/**
	 * @param record
	 * @param cfClassSchemeId
	 * @param cfClassId
	 * @param cfEndDate
	 * @param cfStartDate
	 */
	public RecordRecord(Record record, String cfClassSchemeId,
			String cfClassId, Calendar cfStartDate, Calendar cfEndDate) {
		this.record = record;
		this.recordId = record.getMARC21Record().getControlNumber();
		this.cfClassSchemeId = cfClassSchemeId;
		this.cfClassId = cfClassId;
		this.cfEndDate = cfEndDate;
		this.cfStartDate = cfStartDate;
	}
	
	/**
	 * @param recordId
	 * @param cfClassSchemeId
	 * @param cfClassId
	 * @param cfEndDate
	 * @param cfStartDate
	 */
	public RecordRecord(String recordId, String cfClassSchemeId,
			String cfClassId, Calendar cfStartDate, Calendar cfEndDate) {
		super();
		this.recordId = recordId;
		this.cfClassSchemeId = cfClassSchemeId;
		this.cfClassId = cfClassId;
		this.cfEndDate = cfEndDate;
		this.cfStartDate = cfStartDate;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		if(record == null) {
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			record = recordDAO.getRecord(recordId);
		}
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the recordId
	 */
	public String getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/**
	 * @return the cfClassSchemeId
	 */
	public String getCfClassSchemeId() {
		return cfClassSchemeId;
	}

	/**
	 * @param cfClassSchemeId the cfClassSchemeId to set
	 */
	public void setCfClassSchemeId(String cfClassSchemeId) {
		this.cfClassSchemeId = cfClassSchemeId;
	}

	/**
	 * @return the cfClassId
	 */
	public String getCfClassId() {
		return cfClassId;
	}

	/**
	 * @param cfClassId the cfClassId to set
	 */
	public void setCfClassId(String cfClassId) {
		this.cfClassId = cfClassId;
	}

	/**
	 * @return the cfStartDate
	 */
	public Calendar getCfStartDate() {
		return cfStartDate;
	}

	/**
	 * @param cfStartDate the cfStartDate to set
	 */
	public void setCfStartDate(Calendar cfStartDate) {
		this.cfStartDate = cfStartDate;
	}

	/**
	 * @return the cfEndDate
	 */
	public Calendar getCfEndDate() {
		return cfEndDate;
	}

	/**
	 * @param cfEndDate the cfEndDate to set
	 */
	public void setCfEndDate(Calendar cfEndDate) {
		this.cfEndDate = cfEndDate;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		RecordRecord rc = (RecordRecord) obj;
		try {
			if ((!(rc.recordId.equals(this.recordId)))
					|| (!(rc.cfClassId.equals(this.cfClassId)))
					|| (!(rc.cfClassSchemeId.equals(this.cfClassSchemeId)))
					|| (!(rc.cfStartDate.equals(this.cfStartDate)))
					|| (!(rc.cfEndDate.equals(this.cfEndDate)))) {
				retVal = false;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return recordId + "|" + cfClassSchemeId + "|" + cfClassId + "|" + cfStartDate.getTimeInMillis() + "|" + cfEndDate.getTimeInMillis();
	}
	
	private void parseString(String allInformation){
		String[] params = allInformation.split("\\|");
		recordId = params[0];
		cfClassSchemeId = params[1];
		cfClassId = params[2];
		cfStartDate = new GregorianCalendar();
		cfStartDate.setTimeInMillis(Long.parseLong(params[3]));
		cfEndDate = new GregorianCalendar();
		cfEndDate.setTimeInMillis(Long.parseLong(params[4]));
	}
	
}
