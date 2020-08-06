package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Calendar;


/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class CommissionDTO implements Serializable {

	private Integer commissionId;
	
	private String appointmentBoard;
	
	private Calendar appointmentDate;
	
	private String members;
	
	private ScienceAreaDTO scienceArea;
	
	private String scientificField;

	private String name;
	
	public CommissionDTO() {
		super();
	}

	/**
	 * @param commissionId
	 * @param appointmentBoard
	 * @param appointmentDate
	 * @param members
	 * @param scienceArea
	 * @param scientificField
	 */
	public CommissionDTO(Integer commissionId, String appointmentBoard, Calendar appointmentDate,
			String members, ScienceAreaDTO scienceArea, String scientificField) {
		super();
		this.commissionId = commissionId;
		this.appointmentBoard = appointmentBoard;
		this.appointmentDate = appointmentDate;
		this.members = members;
		this.scienceArea = scienceArea;
		this.scientificField = scientificField;
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
	 * @return the appointmentBoard
	 */
	public String getAppointmentBoard() {
		return appointmentBoard;
	}

	/**
	 * @param appointmentBoard the appointmentBoard to set
	 */
	public void setAppointmentBoard(String appointmentBoard) {
		this.appointmentBoard = appointmentBoard;
	}

	/**
	 * @return the appointmentDate
	 */
	public Calendar getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @param appointmentDate the appointmentDate to set
	 */
	public void setAppointmentDate(Calendar appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @return the members
	 */
	public String getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(String members) {
		this.members = members;
	}

	/**
	 * @return the scienceArea
	 */
	public ScienceAreaDTO getScienceArea() {
		return scienceArea;
	}

	/**
	 * @param scienceArea the scienceArea to set
	 */
	public void setScienceArea(ScienceAreaDTO scienceArea) {
		this.scienceArea = scienceArea;
	}
	
	/**
	 * @return the scientificField
	 */
	public String getScientificField() {
		return scientificField;
	}

	/**
	 * @param scientificField the scientificField to set
	 */
	public void setScientificField(String scientificField) {
		this.scientificField = scientificField;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(name != null)
			return name;
		
		StringBuffer retVal = new StringBuffer();
		retVal.append(((appointmentBoard != null)?(appointmentBoard + ", "):("")) + ((members != null)?(members + ", "):("")) + ((scienceArea != null)?(scienceArea):("")));
		return retVal.toString();
	}
	
	
	
}
