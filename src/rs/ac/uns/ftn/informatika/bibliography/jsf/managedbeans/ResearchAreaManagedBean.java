package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.DragDropEvent;
import rs.ac.uns.ftn.informatika.bibliography.dao.ResearchAreaDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for research areas
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ResearchAreaManagedBean extends CRUDManagedBean {

	private List<ResearchAreaDTO> list;

	private ResearchAreaDAO researchAreaDAO = new ResearchAreaDAO();

	private ResearchAreaDTO selectedResearchArea = null;
	
	private InstitutionDTO institution = null;
	
	private OrganizationUnitDTO organizationUnit = null;
	
	
	/**
	 * 
	 */
	public ResearchAreaManagedBean() {
		super();
		tableModalPanel = "researchAreaBrowseModalPanel";
		editModalPanel = "researchAreaEditModalPanel";
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedResearchArea = null;
		institution = null;
		organizationUnit=null;
		if(list != null)
			populateList = false;
		tableModalPanel = "researchAreaBrowseModalPanel";
		editModalPanel = "researchAreaEditModalPanel";
		return super.resetForm();
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			list = researchAreaDAO.getResearchAreas();
			
		} catch (Exception e) {
			error("populateList", e);
			list = new ArrayList<ResearchAreaDTO>();
		}
	}

	/**
	 * @return the list of researchAreas (filtered and ordered by ...)
	 */
	public List<ResearchAreaDTO> getResearchAreas() {
		if(populateList){
			populateList();
			populateList = false;
		}
		return list;
	}

	/**
	 * @return the selected researchArea
	 */
	public ResearchAreaDTO getSelectedResearchArea() {
		return selectedResearchArea;
	}

	/**
	 * @param researchAreaDTO
	 *            the researchArea to set as selected researchArea
	 */
	public void setSelectedResearchArea(ResearchAreaDTO researchAreaDTO) {
		selectedResearchArea = researchAreaDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedResearchArea = findResearchAreaByClassId(list);
		if (selectedResearchArea != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedResearchArea);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedResearchArea = new ResearchAreaDTO();
		ResearchAreaDTO superResearchArea = findResearchAreaByClassId(list);
		if(superResearchArea == null)
			superResearchArea = new ResearchAreaDTO();
		selectedResearchArea.setSuperResearchArea(superResearchArea);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedResearchArea = findResearchAreaByClassId(list);
		if (selectedResearchArea != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedResearchArea);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickResearchAreaManagedBean.cancelPickingResearchArea();
		}
		super.switchToTableNoneMode();
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (researchAreaDAO.update(selectedResearchArea) == false) {
			facesMessages.addToControlFromResourceBundle(
					"researchAreaEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.researchArea.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"researchAreaEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.researchArea.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("updated: \n" + selectedResearchArea);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (researchAreaDAO.add(selectedResearchArea) == false) {
			facesMessages.addToControlFromResourceBundle(
					"researchAreaEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"cerif.researchArea.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"researchAreaEditForm:general", FacesMessage.SEVERITY_INFO, 
					"cerif.researchArea.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			debug("added: \n" + selectedResearchArea);
			newResearchAreaEmailNotification(selectedResearchArea, facesMessages.getMessageFromResourceBundle("cerif.researchArea.newResearchAreaNotification.subject"));
		}
	}

	private void newResearchAreaEmailNotification(ResearchAreaDTO newResearchArea, String subject) {
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newResearchArea.getFullHierarchy() + "</pre>"));
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedResearchArea = findResearchAreaByClassId(list);
		if (selectedResearchArea == null)
			return;
		if (researchAreaDAO.delete(selectedResearchArea) == false) {
			facesMessages.addToControlFromResourceBundle(
					"researchAreaTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"cerif.researchArea.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedResearchArea);
			selectedResearchArea = null;
			populateList = true;
		}
	}

	/**
	 * Sets chosen researchArea to the CRUDManagedBean which wanted to pick
	 * researchArea
	 */
	public void chooseResearchArea() {

		try {
			selectedResearchArea = findResearchAreaByClassId(list);
			if (selectedResearchArea != null) {
				iPickResearchAreaManagedBean.setResearchArea(selectedResearchArea);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseResearchArea", e);
		}
	}

	private IPickResearchAreaManagedBean iPickResearchAreaManagedBean = null;

	/**
	 * @param iPickResearchAreaManagedBean
	 *            the CRUDManagedBean which wants to pick researchArea
	 */
	public void setIPickResearchAreaManagedBean(
			IPickResearchAreaManagedBean iPickResearchAreaManagedBean) {
		this.iPickResearchAreaManagedBean = iPickResearchAreaManagedBean;
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
		retVal = "indexPage";
		return retVal;
	}

	private ResearchAreaDTO findResearchAreaByClassId(List<ResearchAreaDTO> researchAreasList) {
		ResearchAreaDTO retVal = null;
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String classId = facesCtx.getExternalContext()
				.getRequestParameterMap().get("classId");
		retVal = findResearchAreaByClassId(researchAreasList, classId);
		return retVal;
	}

	private ResearchAreaDTO findResearchAreaByClassId(List<ResearchAreaDTO> researchAreasList, String classId) {
		ResearchAreaDTO retVal = null;
		try {
			for (ResearchAreaDTO ra : researchAreasList) {
				if ((ra.getClassId() != null)
						&& (ra.getClassId()
						.equalsIgnoreCase(classId))) {
					retVal = ra;
					if(retVal.getSuperResearchArea() == null)
						retVal.setSuperResearchArea(new ResearchAreaDTO());
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)) {
			iPickResearchAreaManagedBean.setResearchArea(selectedResearchArea);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	
	public void termTranslations(){
		this.openMultilingualContentForm(editMode, selectedResearchArea.getTermTranslations(), false, "cerif.researchArea.editPanel.termTranslations.panelHeader", "cerif.researchArea.editPanel.termTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedResearchArea.getDescriptionTranslations(), false, "cerif.researchArea.editPanel.descriptionTranslations.panelHeader", "cerif.researchArea.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void processDrop(DragDropEvent event){
		ResearchAreaDTO drop = (ResearchAreaDTO)findResearchAreaByClassId(list, event.getDropId());
		ResearchAreaDTO drag = (ResearchAreaDTO)findResearchAreaByClassId(list, event.getDragId());
		if(!(drag.equals(drop)) && (!(isChild(drop, drag)))) {
			drag.setSuperResearchArea(drop);
			researchAreaDAO.update(drag);
			populateList = true;
		}
	}


	public Boolean isOpened(ResearchAreaDTO parent){
		ResearchAreaDTO child = selectedResearchArea;
		return isChild(child, parent);
	}
	
	private boolean isChild(ResearchAreaDTO child, ResearchAreaDTO parent){
		try{
			if(child.getSuperResearchArea().getClassId().equals(parent.getClassId())){
				return true;
			}
			return isChild(child.getSuperResearchArea(), parent);
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	
	private List<TreeNodeDTO<ResearchAreaDTO>> rootNodes = null;
	
	public void getTree() {
		debug("getTree");
		try {
		    rootNodes = new ArrayList<TreeNodeDTO<ResearchAreaDTO>>();
		    List<ResearchAreaDTO> allResearchAreas = getResearchAreas(); 
		    Collections.sort(allResearchAreas, new GenericComparator<ResearchAreaDTO>(
					"classId", "asc"));
		    for(ResearchAreaDTO ra:allResearchAreas){
				  if(ra.getSuperResearchArea().getClassId() == null) {
						if (((institution == null) && (organizationUnit == null)) || ((institution != null) && (institution.getResearchAreas().contains(ra))) || ((organizationUnit != null) && (organizationUnit.getResearchAreas().contains(ra)))){
							TreeNodeDTO<ResearchAreaDTO> node = new TreeNodeDTO<ResearchAreaDTO>(ra);
							addNodeResearchArea(node, ra);
							node.setExpanded(isOpened(ra));
							rootNodes.add(node);
						}
					}
		      }		 
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	private void addNodeResearchArea(TreeNodeDTO<ResearchAreaDTO> parentNode, ResearchAreaDTO parentResearchArea) {
		for(ResearchAreaDTO ra:getResearchAreas(parentResearchArea)){
			if (((institution == null) && (organizationUnit == null)) || ((institution != null) && (institution.getResearchAreas().contains(ra))) || ((organizationUnit != null) && (organizationUnit.getResearchAreas().contains(ra)))){
				TreeNodeDTO<ResearchAreaDTO> node = new TreeNodeDTO<ResearchAreaDTO>(ra);
				addNodeResearchArea(node, ra);
				node.setExpanded(isOpened(ra));
				parentNode.addChild(node);
			}
		}
	 }
	
	private List<ResearchAreaDTO> getResearchAreas(ResearchAreaDTO parentResearchArea) {
		List<ResearchAreaDTO> retVal = new ArrayList<ResearchAreaDTO>();
		for (ResearchAreaDTO ra : list) {
			if(ra.getSuperResearchArea() != null)
				if(parentResearchArea.getClassId().equals(ra.getSuperResearchArea().getClassId())){
					retVal.add(ra);
				}
		}
		return retVal;
	}



	/**
	 * @return the rootNodes
	 */
	public List<TreeNodeDTO<ResearchAreaDTO>> getRootNodes() {
		if(populateList){
			getTree();
		}
		return rootNodes;
	}

	/**
	 * @param rootNodes the rootNodes to set
	 */
	public void setRootNodes(List<TreeNodeDTO<ResearchAreaDTO>> rootNodes) {
		this.rootNodes = rootNodes;
	}
	
	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		return institution;
	}


	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		populateList = true; 
		this.institution = institution;
	}


	/**
	 * @return the organizationUnit
	 */
	public OrganizationUnitDTO getOrganizationUnit() {
		return organizationUnit;
	}


	/**
	 * @param organizationUnit the organizationUnit to set
	 */
	public void setOrganizationUnit(OrganizationUnitDTO organizationUnit) {
		populateList = true;
		this.organizationUnit = organizationUnit;
	}
	
	

}
