package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;

/**
 * Managed bean for preview of related records
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class RelatedRecordsManagedBean implements Serializable{

	private RecordDTO selectedRecord = null;
	private String source = null;
	
	/**
	 * @return the selected record
	 */
	public RecordDTO getSelectedRecord() {
		return selectedRecord;
	}

	/**
	 * @param recordDTO
	 *            the record to set as selected record
	 */
	public void setSelectedRecord(RecordDTO recordDTO) {
		selectedRecord = recordDTO;
		setLocale("sr");
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
	 * Performs the account activation
	 */
	public void loadRecord(PhaseEvent event){
		selectedRecord = null;
		String recordId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("recordId");
		source = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("source");
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		if(source == null){
			source = "CRIS UNS";
		} 
		String languageCode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("language");
		if(languageCode == null){
			setLocale("sr");
		} else {
			setLocale(languageCode);
		}
		selectedRecord = recordDAO.getDTO("(BISIS)"+recordId);
		String ipAddress = null;
		try{
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			ipAddress  = request.getHeader("X-FORWARDED-FOR");  
			if(ipAddress == null)  
	        {  
				ipAddress = request.getRemoteAddr();  
	        }  else {
	        	ipAddress += "(proxy = " + request.getRemoteAddr() + ")";
	        }
			String referrer = request.getHeader("referer");
			if(referrer != null)
				source += " (" + referrer +")";
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		String stringRepresentation = " | record: notLoaded";
		if(selectedRecord != null)
			stringRepresentation = " | record: " + selectedRecord.getStringRepresentation();
		log.info("View record page: (BISIS)" + recordId + " | " + source + " | " + ipAddress + stringRepresentation);
	}
	
	public void exit(){
		selectedRecord = null;
	}
	
	private String language = "sr";

	/**
	 * @return the current language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLocale(String language) {
		Calendar date = new GregorianCalendar();
		date.set(GregorianCalendar.YEAR, new GregorianCalendar().get(GregorianCalendar.YEAR)+1);
		((HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse()).addHeader("Set-Cookie",
				"languageSearchDissertations=" + language + ";HttpOnly;expires=" + date.getTime());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(language));
		this.language = language;
	}

	
	protected static Log log = LogFactory.getLog(RelatedRecordsManagedBean.class);
}
