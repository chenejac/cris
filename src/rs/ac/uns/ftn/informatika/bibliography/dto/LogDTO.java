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
public class LogDTO implements Serializable {


	private static final long serialVersionUID = 1900323572538954780L;
	
	private String method;
	private Calendar date;
	private String controlNumber;
	private String fileId;
	private String fileName;
	private String source;
	private String referrer;
	private String ipaddress;
	private String proxy;
	private String recordString;
	
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int seconds;
	private int dayOfWeek;
	
	/**
	 * 
	 */
	public LogDTO() {
		super();
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
		if(date != null){
			day = date.get(Calendar.DAY_OF_MONTH);
			month = date.get(Calendar.MONTH) + 1;
			year = date.get(Calendar.YEAR);
			hour = date.get(Calendar.HOUR_OF_DAY);
			minute = date.get(Calendar.MINUTE);
			seconds = date.get(Calendar.SECOND);
			dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		}
	}
	/**
	 * @return the controlNumber
	 */
	public String getControlNumber() {
		return controlNumber;
	}
	/**
	 * @param controlNumber the controlNumber to set
	 */
	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}
	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the referrer
	 */
	public String getReferrer() {
		return referrer;
	}
	/**
	 * @param referrer the referrer to set
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}
	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/**
	 * @return the proxy
	 */
	public String getProxy() {
		return proxy;
	}
	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	/**
	 * @return the recordString
	 */
	public String getRecordString() {
		return recordString;
	}
	/**
	 * @param recordString the recordString to set
	 */
	public void setRecordString(String recordString) {
		this.recordString = recordString;
	}
	
	
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}
	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}
	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}
	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}
	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}
	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	/**
	 * @return the dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "method: " + method + 
				" controlNumber: " + controlNumber + 
				" day: " + day +
				" month: " + month +
				" year: " + year +
				" hour: " + hour +
				" minute: " + minute +
				" seconds: " + seconds +
				" dayOfWeek: " + dayOfWeek +
				" fileId: " + fileId + 
				" fileName: " + fileName + 
				" source: " + source + 
				" referrer: " + referrer +
				" ip address: " + ipaddress + 
				" proxy: " + proxy + 
				" record: " + recordString; 
				
				
	}
	
	
	
}
