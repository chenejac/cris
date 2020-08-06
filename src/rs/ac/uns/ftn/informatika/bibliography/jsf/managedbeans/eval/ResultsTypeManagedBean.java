package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.ModesManagedBean;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class ResultsTypeManagedBean extends CRUDManagedBean {
	private List<ResultsTypeDTO> list;
	private ResultsTypeDTO selectedResultsType = null;
	private ResultsTypeGroupDTO superResultsTypeGroup = null;
	
	private List<TreeNodeDTO<ResultsTypeDTO>> rootNodes = null;
	
	private ResultsTypeDAO resultsTypeDAO = new ResultsTypeDAO();
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickResultsTypeManagedBean iPickResultsTypeManagedBean = null;
	private String pickMessage;
	
	/**
	 * 
	 */
	public ResultsTypeManagedBean() {
		super();
		tableModalPanel = "resultsTypeBrowseModalPanel";
		editModalPanel = "resultsTypeEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedResultsType = null;
		superResultsTypeGroup = null;
		if(list != null)
			populateList = false;
		tableModalPanel = "resultsTypeBrowseModalPanel";
		editModalPanel = "resultsTypeEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			if(superResultsTypeGroup!=null){
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				ExternalContext extCtx = facesCtx.getExternalContext();
				ResultsTypeGroupManagedBean mb = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
						"resultsTypeGroupManagedBean");
				if (mb == null) {
					mb = new ResultsTypeGroupManagedBean();
					extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mb);
				}
				mb.setSelectedResultsTypeGroup(superResultsTypeGroup);
				mb.loadSelectedResultsTypeGroup(true);
				if(superResultsTypeGroup.arangeResultsTypes(1,3,"NUMBER", "ASC") == false)
					superResultsTypeGroup.arangeResultsTypes();
				this.list = superResultsTypeGroup.getResultsTypes();
			}else {
				list = resultsTypeDAO.getAllResultsTypes(true);
			}
		} catch (Exception e) {
			error("populateList", e);
			list = new ArrayList<ResultsTypeDTO>();
		}
	}
	
	/**
	 * @return the list of resultsTypes (filtered and ordered by ...)
	 */
	public List<ResultsTypeDTO> getResultsTypes() {
		if(populateList){
			populateList();
			populateList = false;
		}
		return list;
	}
	
	/**
	 * @return the selectedResultsType
	 */
	public ResultsTypeDTO getSelectedResultsType() {
		return selectedResultsType;
	}

	/**
	 * @param selectedResultsType the selectedResultsType to set
	 */
	public void setSelectedResultsType(ResultsTypeDTO selectedResultsType) {
		this.selectedResultsType = selectedResultsType;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		//za update mode nisu potrebni podaci o super results type group entitetu
		selectedResultsType = findResultsTypeByClassId(list);
		if (selectedResultsType != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedResultsType);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		//za add mode mora postojati super results type group entitet
		if(superResultsTypeGroup== null)
			return;
		
		super.switchToAddMode();
		selectedResultsType = new ResultsTypeDTO();
		selectedResultsType.setClassId("XXX_"+superResultsTypeGroup.getClassId());
		ResultsTypeDTO superResultType = findResultsTypeByClassId(list);
		selectedResultsType.setSuperResultsType(superResultType);
		selectedResultsType.setSuperResultsTypeGroup(superResultsTypeGroup);	
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		//za view details mode nisu potrebni podaci o super results type group entitetu
		selectedResultsType = findResultsTypeByClassId(list);
		if (selectedResultsType != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedResultsType);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickResultsTypeManagedBean!=null) {
			iPickResultsTypeManagedBean.cancelPickingResultsType();
		}
		super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		//za browse mode nisu potrebni podaci o super results type group entitetu
		super.switchToBrowseMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		//za browse mode nisu potrebni podaci o super results type group entitetu
		super.switchToPickMode();
	}
	
	@Override
	public void update() {
		if (selectedResultsType == null)
			return;
		
		if (resultsTypeDAO.update(selectedResultsType) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsType.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.resultsTypeGroup.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedResultsType);
		}
	}

	@Override
	public void add() {
		//za add mode mora postojati super results type group entitet
		if(superResultsTypeGroup== null){
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsType.add.errorResutsTypeGroupNotFound", FacesContext
							.getCurrentInstance());
			return;
		}
		
		if(!selectedResultsType.getClassId().endsWith(superResultsTypeGroup.getClassId())){
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsType.add.errorResutsTypeClassID", FacesContext
							.getCurrentInstance());
			return;
		}
		
		for (ResultsTypeDTO rt : list) {
			if(rt.getClassId().equalsIgnoreCase(selectedResultsType.getClassId())){
				facesMessages.addToControlFromResourceBundle(
						"resultsTypeGroupEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"cerif.resultsType.add.errorSameCode", FacesContext
								.getCurrentInstance());
				return;
			}
		}
		
		if (resultsTypeDAO.add(selectedResultsType) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsType.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.resultsType.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("added: \n" + selectedResultsType);
			newResultsTypeDTOEmailNotification(selectedResultsType, facesMessages.getMessageFromResourceBundle("cerif.resultsType.newResultsTypeNotification.subject"));
		}
	}

	private void newResultsTypeDTOEmailNotification(ResultsTypeDTO newResultsType, String subject) {
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newResultsType.getSomeTerm() + "</pre>"));
	}
	
	@Override
	public void delete() {
		selectedResultsType = findResultsTypeByClassId(list);
		if (selectedResultsType == null)
			return;
		
		if (resultsTypeDAO.deleteResultsTypeDTOHierarchy(selectedResultsType) == false) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.resultsType.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedResultsType);
			selectedResultsType = null;
			populateList = true;
		}
	}

	/**
	 * Sets chosen resultsType to the CRUDManagedBean which wanted to pick
	 * resultsType
	 */
	public void chooseResultsType() {

		try {
			selectedResultsType = findResultsTypeByClassId(list);
			if (selectedResultsType != null && iPickResultsTypeManagedBean!=null) {
				iPickResultsTypeManagedBean.setResultsType(selectedResultsType);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseResultsType", e);
		}
	}

	/**
	 * @param iPickResultsTypeManagedBean the iPickResultsTypeManagedBean to set
	 */
	public void setiPickResultsTypeManagedBean(
			IPickResultsTypeManagedBean iPickResultsTypeManagedBean) {
		this.iPickResultsTypeManagedBean = iPickResultsTypeManagedBean;
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
			if(iPickResultsTypeManagedBean!=null)
				iPickResultsTypeManagedBean.setResultsType(selectedResultsType);
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
		retVal = "resultsTypePage";
		return retVal;
	}

	private ResultsTypeDTO findResultsTypeByClassId(List<ResultsTypeDTO> resultsTypesList) {
		ResultsTypeDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (ResultsTypeDTO item : resultsTypesList) {
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
		this.openMultilingualContentForm(editMode, selectedResultsType.getTermTranslations(), false, "cerif.resultsType.editPanel.termTranslations.panelHeader", "cerif.resultsType.editPanel.termTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedResultsType.getDescriptionTranslations(), false, "cerif.resultsType.editPanel.descriptionTranslations.panelHeader", "cerif.resultsType.editPanel.descriptionTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		return selectedResultsType.getStartDate().getTime();
	}
	
	public void setStartDate(Date startDate){
		selectedResultsType.getStartDate().setTime(startDate);
	}
	
	public Date getEndDate(){
		return selectedResultsType.getEndDate().getTime();
	}
	
	public void setEndDate(Date endDate){
		selectedResultsType.getEndDate().setTime(endDate);
	}

	/**
	 * @return the superResultsTypeGroup
	 */
	public ResultsTypeGroupDTO getSuperResultsTypeGroup() {
		return superResultsTypeGroup;
	}

	/**
	 * @param superResultsTypeGroup the superResultsTypeGroup to set
	 */
	public void setSuperResultsTypeGroup(ResultsTypeGroupDTO superResultsTypeGroup) {
		populateList = true;
		this.superResultsTypeGroup = superResultsTypeGroup;
	}
	
	public Boolean isOpened(ResultsTypeDTO parent){
		ResultsTypeDTO child = selectedResultsType;
		return isChild(child, parent);
	}
	
	private boolean isChild(ResultsTypeDTO child, ResultsTypeDTO parent){
		try{
			if(child.getSuperResultsType().getClassId().equals(parent.getClassId())){
				return true;
			}
			return isChild(child.getSuperResultsType(), parent);
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	public void getTree() {
		debug("getTree");
		try {
		    rootNodes = new ArrayList<TreeNodeDTO<ResultsTypeDTO>>();
		    List<ResultsTypeDTO> allResultsTypes = getResultsTypes(); //ponistava populate list ako treba
		    
		    for(ResultsTypeDTO rt:allResultsTypes){
		    	if(rt.getSuperResultsType() == null) {
					TreeNodeDTO<ResultsTypeDTO> node = new TreeNodeDTO<ResultsTypeDTO>(rt);
					addSubnodesToNode(node, rt);
					node.setExpanded(isOpened(rt));
					rootNodes.add(node);
				}
		    }		 
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	private void addSubnodesToNode(TreeNodeDTO<ResultsTypeDTO> parentNode, ResultsTypeDTO parentResultsType) {
		for(ResultsTypeDTO el:getAllSubElements(parentResultsType)){		
				TreeNodeDTO<ResultsTypeDTO> node = new TreeNodeDTO<ResultsTypeDTO>(el);
				addSubnodesToNode(node, el);
				node.setExpanded(isOpened(el));
				parentNode.addChild(node);
		}
	 }
	
	private List<ResultsTypeDTO> getAllSubElements(ResultsTypeDTO parentResultsType) {
		List<ResultsTypeDTO> retVal = new ArrayList<ResultsTypeDTO>();
		for (ResultsTypeDTO el : list) {
			if(el.getSuperResultsType() != null)
				if(parentResultsType.getClassId().equals(el.getSuperResultsType().getClassId())){
					retVal.add(el);
				}
		}
		return retVal;
	}
	
	/**
	 * @return the rootNodes
	 */
	public List<TreeNodeDTO<ResultsTypeDTO>> getRootNodes() {
		if(populateList){
			getTree();
		}
		return rootNodes;
	}
	
	/**
	 * @param rootNodes the rootNodes to set
	 */
	public void setRootNodes(List<TreeNodeDTO<ResultsTypeDTO>> rootNodes) {
		this.rootNodes = rootNodes;
	}
}