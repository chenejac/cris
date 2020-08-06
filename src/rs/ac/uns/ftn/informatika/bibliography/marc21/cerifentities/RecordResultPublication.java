/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class RecordResultPublication extends RecordRecord implements Serializable{
	
	private String cfCopyright;

	/**
	 * @param allInformation
	 */
	public RecordResultPublication(String allInformation) {
		super(allInformation);
		parseString(recordId);
	}

	/**
	 * @param record
	 * @param cfClassSchemeId
	 * @param cfClassId
	 * @param cfEndDate
	 * @param cfStartDate
	 * @param cfCopyright
	 */
	public RecordResultPublication(Record record, String cfClassSchemeId,
			String cfClassId, Calendar cfStartDate, Calendar cfEndDate,
			String cfCopyright) {
		super(record, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate);
		this.cfCopyright = cfCopyright;
	}
	
	/**
	 * @param recordId
	 * @param cfClassSchemeId
	 * @param cfClassId
	 * @param cfEndDate
	 * @param cfStartDate
	 * @param cfCopyright
	 */
	public RecordResultPublication(String recordId, String cfClassSchemeId,
			String cfClassId, Calendar cfStartDate, Calendar cfEndDate,
			String cfCopyright) {
		super(recordId, cfClassSchemeId, cfClassId, cfStartDate, cfEndDate);
		this.cfCopyright = cfCopyright;
	}

	/**
	 * @return the cfCopyright
	 */
	public String getCfCopyright() {
		return cfCopyright;
	}

	/**
	 * @param cfCopyright the cfCopyright to set
	 */
	public void setCfCopyright(String cfCopyright) {
		this.cfCopyright = cfCopyright;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return cfCopyright + "##&##" + super.toString();
	}
	
	private void parseString(String copyrightRecordId){
		String[] params = copyrightRecordId.split("##&##");
		cfCopyright = params[0];
		if(("null".equals(cfCopyright)) || ("".equals(cfCopyright)))
			cfCopyright = null;
		recordId = params[1];
	}
	
}
