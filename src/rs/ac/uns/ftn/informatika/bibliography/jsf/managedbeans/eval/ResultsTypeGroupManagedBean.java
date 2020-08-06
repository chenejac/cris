package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeGroupDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.ModesManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsTypeGroupManagedBean extends CRUDManagedBean {

	private List<ResultsTypeGroupDTO> list;
	private ResultsTypeGroupDTO selectedResultsTypeGroup = null;
	
	private ResultsTypeGroupDAO resultsTypeGroupDAO = new ResultsTypeGroupDAO();
	
	private boolean loadResultTypes = false;
	private boolean loadSuperResultsTypeAndResultsTypeGroup = false;
	
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickResultsTypeGroupManagedBean iPickResultsTypeGroupManagedBean = null;
	private String pickMessage;
	
	/**
	 * 
	 */
	public ResultsTypeGroupManagedBean() {
		super();
		orderBy = "someTerm";
		direction = "asc";
		tableModalPanel = "resultsTypeGroupBrowseModalPanel";
		editModalPanel = "resultsTypeGroupEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedResultsTypeGroup = null;
		loadResultTypes = false;
		loadSuperResultsTypeAndResultsTypeGroup = false;
		if(list != null)
			populateList = false;
		tableModalPanel = "resultsTypeGroupBrowseModalPanel";
		editModalPanel = "resultsTypeGroupEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			list = resultsTypeGroupDAO.getAllResultsTypeGroups(loadResultTypes, loadSuperResultsTypeAndResultsTypeGroup);
//			for (ResultsTypeGroupDTO it : list) {
//				System.out.println(it + " startDate:"+it.getStartDate().getTime().toGMTString()+" endDate:"+it.getEndDate().getTime().toGMTString());
//			}
			Collections.sort(list, new GenericComparator<ResultsTypeGroupDTO>(
					orderBy, direction));
		} catch (Exception e) {
			error("populateList", e);
			list = new ArrayList<ResultsTypeGroupDTO>();
		}
	}
	
	public void populateListOnlyResultsTypeGroup() {
		loadResultTypes = false;
		loadSuperResultsTypeAndResultsTypeGroup = false;
		populateList();
	}
	
	public void populateListWithResultsTypes() {
		loadResultTypes = true;
		loadSuperResultsTypeAndResultsTypeGroup = false;
		populateList();
	}
	
	public void populateListWithResultsTypesAndTheirHierarhy() {
		loadResultTypes = true;
		loadSuperResultsTypeAndResultsTypeGroup = true;
		populateList();
	}

	public void loadSelectedResultsTypeGroup(boolean loadTreeHierarhyForResultsTypes) {
		if(selectedResultsTypeGroup!=null){
			resultsTypeGroupDAO.loadAllResultsTypesOfSpecificResultsTypeGroup(selectedResultsTypeGroup, loadTreeHierarhyForResultsTypes);
			if(selectedResultsTypeGroup.arangeResultsTypes(1,3,"NUMBER", "ASC") == false)
				selectedResultsTypeGroup.arangeResultsTypes();
		}
	}
	
	/**
	 * @return the list of resultsTypeGroups (filtered and ordered by ...)
	 */
	public List<ResultsTypeGroupDTO> getResultsTypeGroups() {
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
			Collections.sort(list, new GenericComparator<ResultsTypeGroupDTO>(
					orderByList, directionsList));
		}
		return list;
	}
	
	/**
	 * @return the selectedResultsTypeGroup
	 */
	public ResultsTypeGroupDTO getSelectedResultsTypeGroup() {
		return selectedResultsTypeGroup;
	}

	/**
	 * @param selectedResultsTypeGroup the selectedResultsTypeGroup to set
	 */
	public void setSelectedResultsTypeGroup(
			ResultsTypeGroupDTO selectedResultsTypeGroup) {
		this.selectedResultsTypeGroup = selectedResultsTypeGroup;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedResultsTypeGroup = findResultsTypeGroupByClassId(list);
		if (selectedResultsTypeGroup != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ResultsTypeManagedBean mb = (ResultsTypeManagedBean) extCtx.getSessionMap().get(
					"resultsTypeManagedBean");
			if (mb == null) {
				mb = new ResultsTypeManagedBean();
				extCtx.getSessionMap().put("resultsTypeManagedBean", mb);
			}
			
			mb.setSuperResultsTypeGroup(selectedResultsTypeGroup);
			mb.switchToBrowseMode();
			
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedResultsTypeGroup);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedResultsTypeGroup = new ResultsTypeGroupDTO();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedResultsTypeGroup = findResultsTypeGroupByClassId(list);
		if (selectedResultsTypeGroup != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ResultsTypeManagedBean mb = (ResultsTypeManagedBean) extCtx.getSessionMap().get(
					"resultsTypeManagedBean");
			if (mb == null) {
				mb = new ResultsTypeManagedBean();
				extCtx.getSessionMap().put("resultsTypeManagedBean", mb);
			}
			
			mb.setSuperResultsTypeGroup(selectedResultsTypeGroup);
			mb.switchToBrowseMode();
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedResultsTypeGroup);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickResultsTypeGroupManagedBean!=null) {
			iPickResultsTypeGroupManagedBean.cancelPickingResultsTypeGroup();
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
		if (selectedResultsTypeGroup == null)
			return;

		loadSelectedResultsTypeGroup(true);
		if (resultsTypeGroupDAO.updateResultsTypeGroupDTOAndResultsTypes(selectedResultsTypeGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsTypeGroup.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.resultsTypeGroup.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedResultsTypeGroup);
		}
	}

	@Override
	public void add() {
		for (ResultsTypeGroupDTO rtg : list) {
			if(rtg.getClassId().equalsIgnoreCase(selectedResultsTypeGroup.getClassId())){
				facesMessages.addToControlFromResourceBundle(
						"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"cerif.resultsTypeGroup.add.errorSameCode", FacesContext
								.getCurrentInstance());
				return;
			}
		}
		
		if (resultsTypeGroupDAO.addResultsTypeGroupDTO(selectedResultsTypeGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsTypeGroup.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.resultsTypeGroup.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("added: \n" + selectedResultsTypeGroup);
			newResultsTypeGroupEmailNotification(selectedResultsTypeGroup, facesMessages.getMessageFromResourceBundle("cerif.resultsTypeGroup.newResultsTypeGroupNotification.subject"));
		}
	}

	private void newResultsTypeGroupEmailNotification(ResultsTypeGroupDTO newResultsTypeGroup, String subject) {
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newResultsTypeGroup.getSomeTerm() + "</pre>"));
	}
	
	@Override
	public void delete() {
		selectedResultsTypeGroup = findResultsTypeGroupByClassId(list);
		if (selectedResultsTypeGroup == null)
			return;
		
		loadSelectedResultsTypeGroup(true);
		if (resultsTypeGroupDAO.deleteResultsTypeGroupDTOAndResultsTypes(selectedResultsTypeGroup) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeGroupTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsTypeGroup.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedResultsTypeGroup);
			selectedResultsTypeGroup = null;
			populateList = true;
		}
	}

	/**
	 * Sets chosen resultsTypeGroup to the CRUDManagedBean which wanted to pick
	 * resultsTypeGroup
	 */
	public void chooseResultsTypeGroup() {

		try {
			selectedResultsTypeGroup = findResultsTypeGroupByClassId(list);
			if (selectedResultsTypeGroup != null && iPickResultsTypeGroupManagedBean!=null) {
				iPickResultsTypeGroupManagedBean.setResultsTypeGroup(selectedResultsTypeGroup);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseResultsTypeGroup", e);
		}
	}
	
	/**
	 * @param iPickResultsTypeGroupManagedBean the iPickResultsTypeGroupManagedBean to set
	 */
	public void setiPickResultsTypeGroupManagedBean(
			IPickResultsTypeGroupManagedBean iPickResultsTypeGroupManagedBean) {
		this.iPickResultsTypeGroupManagedBean = iPickResultsTypeGroupManagedBean;
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
	
	/**
	 * Increases the edit tab number for one
	 */
	public void nextEditTab() {
		editTabNumber++;
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
	}
	
	@Override
	public String finishWizard() {
		if ((editMode == modesManagedBean.getAdd()) && (tableMode == modesManagedBean.getPick())) {
			if(iPickResultsTypeGroupManagedBean!=null)
				iPickResultsTypeGroupManagedBean.setResultsTypeGroup(selectedResultsTypeGroup);
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
		retVal = "resultsTypeGroupPage";
		return retVal;
	}

	private ResultsTypeGroupDTO findResultsTypeGroupByClassId(List<ResultsTypeGroupDTO> resultsTypeGroupsList) {
		ResultsTypeGroupDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (ResultsTypeGroupDTO rtg : resultsTypeGroupsList) {
				if ((rtg.getClassId() != null)
						&& (rtg.getClassId()
								.equalsIgnoreCase(classId))) {
					retVal = rtg;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}
	
	public void termTranslations(){
		this.openMultilingualContentForm(editMode, selectedResultsTypeGroup.getTermTranslations(), false, "cerif.resultsTypeGroup.editPanel.termTranslations.panelHeader", "cerif.resultsTypeGroup.editPanel.termTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedResultsTypeGroup.getDescriptionTranslations(), false, "cerif.resultsTypeGroup.editPanel.descriptionTranslations.panelHeader", "cerif.resultsTypeGroup.editPanel.descriptionTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		return selectedResultsTypeGroup.getStartDate().getTime();
	}
	
	public void setStartDate(Date startDate){
		selectedResultsTypeGroup.getStartDate().setTime(startDate);
	}
	
	public Date getEndDate(){
		return selectedResultsTypeGroup.getEndDate().getTime();
	}
	
	public void setEndDate(Date endDate){
		selectedResultsTypeGroup.getEndDate().setTime(endDate);
	}
}
