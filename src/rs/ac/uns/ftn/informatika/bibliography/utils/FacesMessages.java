package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Represents a connection between ResourceBundle and FacesContext
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
@SuppressWarnings("serial")
public class FacesMessages implements Serializable {

	/**
	 * @param bundleName
	 *            bundle name
	 */
	public FacesMessages(String bundleName) {
		bundle = PropertyResourceBundle.getBundle(bundleName);
	}

	/**
	 * @param bundleName
	 *            bundle name
	 * @param locale
	 *            locale
	 */
	public FacesMessages(String bundleName, Locale locale) {
		bundle = PropertyResourceBundle.getBundle(bundleName, locale);
	}

	/**
	 * @param bundle
	 *            resource bundle
	 */
	public FacesMessages(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	private ResourceBundle bundle;

	/**
	 * Adds message to JSF control
	 * 
	 * @param controlId
	 *            control id
	 * @param severity
	 *            severity
	 * @param key
	 *            message key
	 * @param facesCtx
	 *            faces context
	 */
	public void addToControlFromResourceBundle(String controlId, Severity severity, String key,
			FacesContext facesCtx, Object... params) {
		String value;
		if (bundle.containsKey(key))
			if((params!= null) && (params.length != 0))
				value = MessageFormat.format(bundle.getString(key), params);
			else 
				value = bundle.getString(key);
		else
			value = key;
		FacesMessage facesMessage = new FacesMessage(severity, value, value);
		facesCtx.addMessage(controlId, facesMessage);
	}
	
	/**
	 * Adds message to JSF control
	 * 
	 * @param controlId
	 *            control id
	 * @param severity
	 *            severity
	 * @param value
	 *            message value
	 * @param facesCtx
	 *            faces context
	 */
	public void addToControl(String controlId, Severity severity, String value,
			FacesContext facesCtx) {
		FacesMessage facesMessage = new FacesMessage(severity, value, value);
		facesCtx.addMessage(controlId, facesMessage);
	}

	/**
	 * Retrieves a message from resource bundle
	 * 
	 * @param key
	 *            message key
	 * @return the message
	 */
	public String getMessageFromResourceBundle(String key) {
		String value = key;
		if ((key != null) && (bundle.containsKey(key)))
			value = bundle.getString(key);
		return value;
	}

}
