/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;

/**
 * DTO class which presents email message
 * 
 * @author "chenejac@uns.ac.rs"
 *
 */
@SuppressWarnings("serial")
public class EmailMessage implements Serializable {

	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String text;
	
	/**
	 * 
	 */
	public EmailMessage() {
	}
	
	/**
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param text
	 */
	public EmailMessage(String from, String to, String cc, String bcc, String subject, String text) {
		super();
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.text = text;
	}
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the cc
	 */
	public String getCC() {
		return cc;
	}
	/**
	 * @param cc the cc to set
	 */
	public void setCC(String cc) {
		this.cc = cc;
	}
	/**
	 * @return the bcc
	 */
	public String getBCC() {
		return bcc;
	}
	/**
	 * @param bcc the bcc to set
	 */
	public void setBCC(String bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	

}
