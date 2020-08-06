package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.openide.util.Enumerations;

import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.UserManagedBean;

/**
 * Represents a resource bundles which has messages from
 * <code>n<code> property files
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
public class ResourceBoundles extends ResourceBundle {

	private ResourceBundle rbAdministration = null;
	private ResourceBundle rbRecords = null;
	private ResourceBundle rbGeneral = null;
	private ResourceBundle rbCerif = null;
	private ResourceBundle rbSearch = null;
	private ResourceBundle rbEvaluation = null;


	/**
	 * Loads messages from properties files
	 */
	private void loadPropertyResourceBundles() {
		rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", getLocale());
		rbRecords = PropertyResourceBundle.getBundle(
				"messages.messages-records", getLocale());
		rbCerif = PropertyResourceBundle.getBundle(
				"messages.messages-cerif", getLocale());
		rbSearch = PropertyResourceBundle.getBundle(
				"messages.messages-search", getLocale());
		rbEvaluation = PropertyResourceBundle.getBundle(
				"messages.messages-evaluation", getLocale());
		rbGeneral = PropertyResourceBundle.getBundle(
				"messages.messages", getLocale());
	}

	private boolean notLoaded = true;
	
	/**
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	public Object handleGetObject(String key) {
		if(notLoaded){
			notLoaded = true;
			loadPropertyResourceBundles();
		}
		Locale currentLocale = getLocale();
		if ((currentLocale != null)
				&& (!(currentLocale.toString().equals(rbAdministration
						.getLocale().toString()))))
			loadPropertyResourceBundles();
		if (key.startsWith("administration."))
			return rbAdministration.getObject(key);
		else if (key.startsWith("records."))
			return rbRecords.getObject(key);
		else if (key.startsWith("cerif."))
			return rbCerif.getObject(key);
		else if (key.startsWith("search."))
			return rbSearch.getObject(key); 
		else if (key.startsWith("evaluation."))
			return rbEvaluation.getObject(key); 
		else if (rbGeneral.containsKey(key))
			return rbGeneral.getObject(key);
		return "" + key;
	}

	/**
	 * @see java.util.ResourceBundle#getKeys()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getKeys() {
		if(notLoaded){
			notLoaded = true;
			loadPropertyResourceBundles();
		}
		return Enumerations.concat(rbGeneral.getKeys(), Enumerations.concat(rbEvaluation.getKeys(), Enumerations.concat(rbSearch.getKeys(), Enumerations.concat(rbAdministration.getKeys(), Enumerations.concat(rbRecords
				.getKeys(), rbCerif.getKeys())))));
	}

	/**
	 * @see java.util.ResourceBundle#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String arg0) {
		if(notLoaded){
			notLoaded = true;
			loadPropertyResourceBundles();
		}
		return (rbAdministration.containsKey(arg0) || rbRecords
				.containsKey(arg0) || rbCerif.containsKey(arg0) || rbSearch.containsKey(arg0) || rbEvaluation.containsKey(arg0) || 
				rbGeneral.containsKey(arg0));
	}

	/**
	 * @see java.util.ResourceBundle#getLocale()
	 */
	@Override
	public Locale getLocale() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale == null)
			locale = new Locale(getUserManagedBean().getLanguage());
		return locale;
	}
	
	private UserManagedBean userManagedBean = null;

	/**
	 * @return the UserManagedBean from current session
	 */
	private UserManagedBean getUserManagedBean() {
		if (userManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			UserManagedBean mb = (UserManagedBean) extCtx.getSessionMap().get(
					"userManagedBean");
			if (mb == null) {
				mb = new UserManagedBean();
				extCtx.getSessionMap().put("userManagedBean", mb);
			}
			userManagedBean = mb;
		}
		return userManagedBean;
	}

}
