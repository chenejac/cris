package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.SciencesGroupDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.ModesManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SciencesGroupManagedBean extends CRUDManagedBean {

	private List<SciencesGroupDTO> list;
	private SciencesGroupDTO selectedSciencesGroup = null;
	
	private SciencesGroupDAO sciencesGroupDAO = new SciencesGroupDAO();
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickSciencesGroupManagedBean iPickSciencesGroupManagedBean = null;
	private String pickMessage;
	
	/**
	 * 
	 */
	public SciencesGroupManagedBean() {
		super();
		orderBy = "someTerm";
		direction = "asc";
		tableModalPanel = "sciencesGroupBrowseModalPanel";
		editModalPanel = "sciencesGroupEditModalPanel";
		
//		FacesContext.getCurrentInstance().getViewRoot().setLocale(
//				new Locale("en"));
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedSciencesGroup = null;
		if(list != null)
			populateList = false;
		tableModalPanel = "sciencesGroupBrowseModalPanel";
		editModalPanel = "sciencesGroupEditModalPanel";
		
//		FacesContext.getCurrentInstance().getViewRoot().setLocale(
//				new Locale("en"));
		
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			list = sciencesGroupDAO.getAllSciencesGroup();
			Collections.sort(list, new GenericComparator<SciencesGroupDTO>(
					orderBy, direction));
			
		} catch (Exception e) {
			error("populateList", e);
			list = new ArrayList<SciencesGroupDTO>();
		}
	}
	
	/**
	 * @return the list of sciencesGroups (filtered and ordered by ...)
	 */
	public List<SciencesGroupDTO> getSciencesGroups() {
		if(populateList){
			populateList();
			populateList = false;
		}
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		if(orderList == true){
			orderList = false;
			if ((orderBy != null) && (!"".equals(orderBy))) {
				if (direction != null) {
					orderByList.add(orderBy);
					directionsList.add(direction);
				}
			}
			Collections.sort(list, new GenericComparator<SciencesGroupDTO>(
					orderByList, directionsList));
		}
		
		return list;
	}

	/**
	 * @return the selectedSciencesGroup
	 */
	public SciencesGroupDTO getSelectedSciencesGroup() {
		return selectedSciencesGroup;
	}

	/**
	 * @param selectedSciencesGroup the selectedSciencesGroup to set
	 */
	public void setSelectedSciencesGroup(SciencesGroupDTO selectedSciencesGroup) {
		this.selectedSciencesGroup = selectedSciencesGroup;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedSciencesGroup = findSciencesGroupByClassId(list);
		if (selectedSciencesGroup != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedSciencesGroup);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedSciencesGroup = new SciencesGroupDTO();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedSciencesGroup = findSciencesGroupByClassId(list);
		if (selectedSciencesGroup != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedSciencesGroup);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickSciencesGroupManagedBean!=null) {
			iPickSciencesGroupManagedBean.cancelPickingSciencesGroup();
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
	
	@Override
	public void update() {
		if (selectedSciencesGroup == null)
			return;
		
		if (sciencesGroupDAO.update(selectedSciencesGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"sciencesGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.sciencesGroup.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"sciencesGroupEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.sciencesGroup.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedSciencesGroup);
		}
	}

	@Override
	public void add() {
		if (sciencesGroupDAO.add(selectedSciencesGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"sciencesGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.sciencesGroup.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"sciencesGroupEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.sciencesGroup.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("added: \n" + selectedSciencesGroup);
			newSciencesGroupDTOEmailNotification(selectedSciencesGroup, facesMessages.getMessageFromResourceBundle("cerif.sciencesGroup.newSciencesGroupNotification.subject"));
		}
	}

	private void newSciencesGroupDTOEmailNotification(SciencesGroupDTO newSciencesGroup, String subject) {
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newSciencesGroup.getSomeTerm() + "</pre>"));
	}
	
	@Override
	public void delete() {
		selectedSciencesGroup = findSciencesGroupByClassId(list);
		if (selectedSciencesGroup == null)
			return;
		
		if (sciencesGroupDAO.delete(selectedSciencesGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"sciencesGroupTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.sciencesGroup.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedSciencesGroup);
			selectedSciencesGroup = null;
			populateList = true;
		}
	}

	/**
	 * Sets chosen sciencesGroup to the CRUDManagedBean which wanted to pick
	 * sciencesGroup
	 */
	public void chooseSciencesGroup() {

		try {
			selectedSciencesGroup = findSciencesGroupByClassId(list);
			if (selectedSciencesGroup != null && iPickSciencesGroupManagedBean!=null) {
				iPickSciencesGroupManagedBean.setSciencesGroup(selectedSciencesGroup);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseSciencesGroup", e);
		}
	}
	
	/**
	 * @param iPickSciencesGroupManagedBean the iPickSciencesGroupManagedBean to set
	 */
	public void setiPickSciencesGroupManagedBean(
			IPickSciencesGroupManagedBean iPickSciencesGroupManagedBean) {
		this.iPickSciencesGroupManagedBean = iPickSciencesGroupManagedBean;
	}

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
	
	@Override
	public String finishWizard() {
		if ((editMode == modesManagedBean.getAdd()) && (tableMode == modesManagedBean.getPick())) {
			if(iPickSciencesGroupManagedBean!=null)
				iPickSciencesGroupManagedBean.setSciencesGroup(selectedSciencesGroup);
			setTableMode(modesManagedBean.getNone());
			setEditMode(modesManagedBean.getNone());
		} else
			super.switchToEditNoneMode();
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "sciencesGroupPage";
		return retVal;
	}

	private SciencesGroupDTO findSciencesGroupByClassId(List<SciencesGroupDTO> sciencesGroupsList) {
		SciencesGroupDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (SciencesGroupDTO item : sciencesGroupsList) {
				if ((item.getClassId() != null)
						&& (item.getClassId()
								.equalsIgnoreCase(classId))) {
					retVal = item;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}
	
	public void termTranslations(){
		this.openMultilingualContentForm(editMode, selectedSciencesGroup.getTermTranslations(), false, "cerif.sciencesGroup.editPanel.termTranslations.panelHeader", "cerif.sciencesGroup.editPanel.termTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedSciencesGroup.getDescriptionTranslations(), false, "cerif.sciencesGroup.editPanel.descriptionTranslations.panelHeader", "cerif.sciencesGroup.editPanel.descriptionTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		return selectedSciencesGroup.getStartDate().getTime();
	}
	
	public void setStartDate(Date startDate){
		selectedSciencesGroup.getStartDate().setTime(startDate);
	}
	
	public Date getEndDate(){
		return selectedSciencesGroup.getEndDate().getTime();
	}
	
	public void setEndDate(Date endDate){
		selectedSciencesGroup.getEndDate().setTime(endDate);
	}
	
	/**
	 * @return the current language
	 */
//	public String getLanguage() {
//		return "en";
//	}
}