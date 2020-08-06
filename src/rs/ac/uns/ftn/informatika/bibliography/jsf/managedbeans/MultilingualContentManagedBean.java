package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.List;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;

/**
 * Managed bean with CRUD operations for multilingual content
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MultilingualContentManagedBean extends AMultilingualContentManagedBean {

	private List<MultilingualContentDTO> contents = null;

	private String content = null;
	
	private String contentHeader = null;
	
	private boolean htmlContent = false;
	
		
	/**
	 * @return the contents
	 */
	public List<MultilingualContentDTO> getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(List<MultilingualContentDTO> contents) {
		this.contents = contents;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the contentHeader
	 */
	public String getContentHeader() {
		return facesMessages.getMessageFromResourceBundle(contentHeader);
	}

	/**
	 * @param contentHeader the contentHeader to set
	 */
	public void setContentHeader(String contentHeader) {
		this.contentHeader = contentHeader;
	}
	
	/**
	 * @return the htmlContent
	 */
	public boolean isHtmlContent() {
		return htmlContent;
	}

	/**
	 * @param htmlContent the htmlContent to set
	 */
	public void setHtmlContent(boolean htmlContent) {
		this.htmlContent = htmlContent;
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

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.AMultilingualContentManagedBean#add()
	 */
	@Override
	public void add() {
		MultilingualContentDTO mc = new MultilingualContentDTO();
		mc.setContent(content);
		mc.setLanguage(language);
		contents.add(mc);
		content = null;
		language = null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.AMultilingualContentManagedBean#remove()
	 */
	@Override
	public void remove() {
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String ordNum = facesCtx.getExternalContext()
					.getRequestParameterMap().get("ordNum");
			contents.remove(Integer.parseInt(ordNum));
		} catch (Exception e) {
		}
	}

}
