/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class Classification implements Serializable{
	
	private String cfClassSchemeId;
	private String cfClassId;
	private Calendar cfStartDate;
	private Calendar cfEndDate;
	private Integer commissionId;
	private String researchArea;

	/**
	 * @param cfClassSchemeId
	 * @param cfClassId
	 * @param cfStartDate
	 * @param cfEndDate
	 * @param commissionId
	 * @param researchArea
	 */
	public Classification(String cfClassSchemeId, String cfClassId,
			Calendar cfStartDate, Calendar cfEndDate, Integer commissionId, String researchArea) {
		super();
		this.cfClassSchemeId = cfClassSchemeId;
		this.cfClassId = cfClassId;
		this.cfStartDate = cfStartDate;
		this.cfEndDate = cfEndDate;
		this.commissionId = commissionId;
		this.researchArea = researchArea;
	}
	
	/**
	 * @param allInformation
	 */
	public Classification(String allInformation) {
		super();
		parseString(allInformation);
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
	 * @return the commissionId
	 */
	public Integer getCommissionId() {
		return commissionId;
	}

	/**
	 * @param commissionId the commissionId to set
	 */
	public void setCommissionId(Integer commissionId) {
		this.commissionId = commissionId;
	}
	
	/**
	 * @return the researchArea
	 */
	public String getResearchArea() {
		return researchArea;
	}

	/**
	 * @param researchArea the researchArea to set
	 */
	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
	//TODO
		Classification rc = (Classification) obj;
		try {
			if ((!(rc.cfClassId.equals(this.cfClassId)))
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
		return cfClassSchemeId + "|" + cfClassId + "|" + cfStartDate.getTimeInMillis() + "|" + cfEndDate.getTimeInMillis() + "|" + commissionId + ((researchArea!=null)?("|" + researchArea):(""));
	}
	
	private void parseString(String allInformation){
		String[] params = allInformation.split("\\|");
		cfClassSchemeId = params[0];
		cfClassId = params[1];
		cfStartDate = new GregorianCalendar();
		cfStartDate.setTimeInMillis(Long.parseLong(params[2]));
		cfEndDate = new GregorianCalendar();
		cfEndDate.setTimeInMillis(Long.parseLong(params[3]));
		commissionId = Integer.parseInt(params[4]);
		if(params.length == 6)
			researchArea = params[5];
	}
	
}
