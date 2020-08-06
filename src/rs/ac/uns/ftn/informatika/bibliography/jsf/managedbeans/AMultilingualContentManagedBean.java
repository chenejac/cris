package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * Managed bean with CRUD operations for multilingual content
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class AMultilingualContentManagedBean implements Serializable {

	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());

	protected Log log = LogFactory
		.getLog(AMultilingualContentManagedBean.class.getName());
		
	protected String language = null;
	
	protected String panelHeader = null;
	
	protected int editMode = ModesManagedBean.MODE_NONE;
		
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the panelHeader
	 */
	public String getPanelHeader() {
		return facesMessages.getMessageFromResourceBundle(panelHeader);
	}

	/**
	 * @param panelHeader the panelHeader to set
	 */
	public void setPanelHeader(String panelHeader) {
		this.panelHeader = panelHeader;
	}

	/**
	 * @return the editMode
	 */
	public int getEditMode() {
		return editMode;
	}

	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
	}

	public abstract void remove();
	
	public abstract void add();

	public void exit(){
		editMode = ModesManagedBean.MODE_NONE;
	}

}
