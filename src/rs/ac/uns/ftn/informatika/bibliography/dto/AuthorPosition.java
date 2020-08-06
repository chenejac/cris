package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * DTO class which presents research position.
 *  
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorPosition implements Serializable {

	private PositionDTO position;
	private Calendar startDate;
	private Calendar endDate;
	private String researchArea;
	
	public AuthorPosition() {
		position = new PositionDTO();
	}
	
	/**
	 * @param position
	 * @param startDate
	 * @param endDate
	 * @param researchArea
	 */
	public AuthorPosition(PositionDTO position, Calendar startDate, Calendar endDate, String researchArea) {
		this.position = position;
		this.startDate = startDate;
		this.endDate = endDate;
		this.researchArea = researchArea;
	}

	/**
	 * @return the position
	 */
	public PositionDTO getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(PositionDTO position) {
		if(position!=null)
			this.position = position;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	
	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return (startDate!=null)?startDate.get(Calendar.YEAR):null;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
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
	public boolean equals(Object arg0) {
		
		try {
			AuthorPosition temp = (AuthorPosition) arg0;
			if ((this.position != null)
					&& (this.position.equals(temp.position)))
				return true;
			else
				return false;
		} catch (Throwable e) {
			PositionDTO temp = (PositionDTO) arg0;
			if ((this.position != null)
					&& (this.position.equals(temp)))
				return true;
			else
				return false;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return position.toString();
	}
	
	

}
