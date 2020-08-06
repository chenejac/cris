package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import rs.ac.uns.ftn.informatika.bibliography.dao.PositionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for positions
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PositionManagedBean extends CRUDManagedBean {

	private List<PositionDTO> list;
	
	private List<SelectItem> allList;

	private PositionDAO positionDAO = new PositionDAO();

	private PositionDTO selectedPosition = null;
	
	public PositionManagedBean() {
		super();
		allList = new ArrayList<SelectItem>();
		orderBy = "someTerm";
		direction = "asc";
		list = new ArrayList<PositionDTO>();
		for (PositionDTO pos : positionDAO.getPositions()) {			
			list.add(new PositionDTO(pos.getSchemeId(), pos.getClassId(), pos.getTerm(), pos.getTermTranslations(), pos.getDescription(), pos.getDescriptionTranslations()));
		}
		Collections.sort(list, new GenericComparator<PositionDTO>(orderBy, direction));
        allList.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("cerif.position.pleaseSelect")));
        for (PositionDTO pos : list){
           allList.add(new SelectItem(pos, pos.toString()));
        }
        tableModalPanel = "positionBrowseModalPanel";
		editModalPanel = "positionEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPosition = null;
		populateList = true;
		tableModalPanel = "positionBrowseModalPanel";
		editModalPanel = "positionEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @return the allList
	 */
	public List<SelectItem> getAllList() {
		return allList;
	}

	/**
	 * @param allList the allList to set
	 */
	public void setAllList(List<SelectItem> allList) {
		this.allList = allList;
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			list = positionDAO.getPositions();
			orderBy = "someTerm";
			direction = "asc";
			Collections.sort(list, new GenericComparator<PositionDTO>(
					orderBy, direction));
			
		} catch (Exception e) {
			error("populateList", e);
			list = new ArrayList<PositionDTO>();
		}
	}

	/**
	 * @return the list of positions (filtered and ordered by ...)
	 */
	public List<PositionDTO> getPositions() {
		if(populateList){
			populateList();
			populateList = false;
		}
		return list;
	}

	/**
	 * @return the selected position
	 */
	public PositionDTO getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * @param positionDTO
	 *            the position to set as selected position
	 */
	public void setSelectedPosition(PositionDTO positionDTO) {
		selectedPosition = positionDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedPosition = findPositionByClassId(list);
		if (selectedPosition != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedPosition);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedPosition = new PositionDTO();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedPosition = findPositionByClassId(list);
		if (selectedPosition != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedPosition);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (positionDAO.update(selectedPosition) == false) {
			facesMessages.addToControlFromResourceBundle(
					"positionEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.position.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"positionEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.position.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedPosition);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (positionDAO.add(selectedPosition) == false) {
			facesMessages.addToControlFromResourceBundle(
					"positionEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.position.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"positionEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.position.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("added: \n" + selectedPosition);
			newPositionEmailNotification(selectedPosition, facesMessages.getMessageFromResourceBundle("cerif.position.newPositionNotification.subject"));
		}
	}

	private void newPositionEmailNotification(PositionDTO newPosition, String subject) {
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newPosition.toString() + "</pre>"));
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPosition = findPositionByClassId(list);
		if (selectedPosition == null)
			return;
		if (positionDAO.delete(selectedPosition) == false) {
			facesMessages.addToControlFromResourceBundle(
					"positionTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"cerif.position.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedPosition);
			selectedPosition = null;
			populateList = true;
		}
	}

	/**
	 * Sets chosen position to the CRUDManagedBean which wanted to pick
	 * position
	 */
	public void choosePosition() {

		try {
			selectedPosition = findPositionByClassId(list);
			if (selectedPosition != null) {
				iPickPositionManagedBean.setPosition(selectedPosition);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("choosePosition", e);
		}
	}

	private IPickPositionManagedBean iPickPositionManagedBean = null;

	/**
	 * @param iPickPositionManagedBean
	 *            the CRUDManagedBean which wants to pick position
	 */
	public void setIPickPositionManagedBean(
			IPickPositionManagedBean iPickPositionManagedBean) {
		this.iPickPositionManagedBean = iPickPositionManagedBean;
	}
	
	private String pickMessage;

	/**
	 * @return the pick message 
	 */
	public String getPickMessage() {
		return facesMessages.getMessageFromResourceBundle(pickMessage);
	}

	/**
	 * @param pickMessage
	 *            the pick message to set
	 */
	public void setPickMessage(String pickMessage) {
		this.pickMessage = pickMessage;
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "positionPage";
		return retVal;
	}

	private PositionDTO findPositionByClassId(List<PositionDTO> positionsList) {
		PositionDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (PositionDTO ra : positionsList) {
				if ((ra.getClassId() != null)
						&& (ra.getClassId()
								.equalsIgnoreCase(classId))) {
					retVal = ra;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickPositionManagedBean.cancelPickingPosition();
		}
		super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		orderBy = "someTerm";
		direction = "asc";
		super.switchToBrowseMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		orderBy = "someTerm";
		direction = "asc";
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)) {
			iPickPositionManagedBean.setPosition(selectedPosition);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	
	public void termTranslations(){
		this.openMultilingualContentForm(editMode, selectedPosition.getTermTranslations(), false, "cerif.position.editPanel.termTranslations.panelHeader", "cerif.position.editPanel.termTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedPosition.getDescriptionTranslations(), false, "cerif.position.editPanel.descriptionTranslations.panelHeader", "cerif.position.editPanel.descriptionTranslations.contentHeader");
	}

}
