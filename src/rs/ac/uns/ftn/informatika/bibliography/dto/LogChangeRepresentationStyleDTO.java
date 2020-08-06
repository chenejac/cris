/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Dragan Ivanovic
 *
 */
public class LogChangeRepresentationStyleDTO extends LogDTO {

	private long miliseconds = 0l;
	private String userId;
	private String sessionId;
	private String locationCity;
	private String locationPostalCode;
	private String locationRegionName;
	private String locationCountryName;
	private double locationLatitude;
	private double locationLongitude;
	private String userAgent;
	private String newRepresentationStyle;
	
	/**
	 * 
	 */
	public LogChangeRepresentationStyleDTO() {
		super();
	}

	/**
	 * @return the miliseconds
	 */
	public long getMiliseconds() {
		return miliseconds;
	}

	/**
	 * @param miliseconds the miliseconds to set
	 */
	public void setMiliseconds(long miliseconds) {
		this.miliseconds = miliseconds;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the locationCity
	 */
	public String getLocationCity() {
		return locationCity;
	}

	/**
	 * @param locationCity the locationCity to set
	 */
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	/**
	 * @return the locationPostalCode
	 */
	public String getLocationPostalCode() {
		return locationPostalCode;
	}

	/**
	 * @param locationPostalCode the locationPostalCode to set
	 */
	public void setLocationPostalCode(String locationPostalCode) {
		this.locationPostalCode = locationPostalCode;
	}

	/**
	 * @return the locationRegionName
	 */
	public String getLocationRegionName() {
		return locationRegionName;
	}

	/**
	 * @param locationRegionName the locationRegionName to set
	 */
	public void setLocationRegionName(String locationRegionName) {
		this.locationRegionName = locationRegionName;
	}

	/**
	 * @return the locationCountryName
	 */
	public String getLocationCountryName() {
		return locationCountryName;
	}

	/**
	 * @param locationCountryName the locationCountryName to set
	 */
	public void setLocationCountryName(String locationCountryName) {
		this.locationCountryName = locationCountryName;
	}

	/**
	 * @return the locationLatitude
	 */
	public double getLocationLatitude() {
		return locationLatitude;
	}

	/**
	 * @param locationLatitude the locationLatitude to set
	 */
	public void setLocationLatitude(double locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	/**
	 * @return the locationLongitude
	 */
	public double getLocationLongitude() {
		return locationLongitude;
	}

	/**
	 * @param locationLongitude the locationLongitude to set
	 */
	public void setLocationLongitude(double locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the newRepresentationStyle
	 */
	public String getNewRepresentationStyle() {
		return newRepresentationStyle;
	}

	/**
	 * @param newRepresentationStyle the newRepresentationStyle to set
	 */
	public void setNewRepresentationStyle(String newRepresentationStyle) {
		this.newRepresentationStyle = newRepresentationStyle;
	}


	
	
	
}
