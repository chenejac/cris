package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;

import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for organization unit
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class OrganizationUnitManagedBean extends CRUDManagedBean implements IPickResearchAreaManagedBean{

	private List<OrganizationUnitDTO> list;
	
	private List<SelectItem> allList;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private OrganizationUnitDTO selectedOrganizationUnit = null;

	private InstitutionDTO institution = null;
	
	private List<OrganizationUnitDTO> duplicateOrganizationUnits = new ArrayList<OrganizationUnitDTO>(); 
	
	/**
	 * 
	 */
	public OrganizationUnitManagedBean() {
		super();
		tableModalPanel = "organizationUnitBrowseModalPanel";
		editModalPanel = "organizationUnitEditModalPanel";
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
		try {
			allList = new ArrayList<SelectItem>();
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("TYPE", Types.ORGANIZATION_UNIT)), Occur.MUST);
//			bq.add(new TermQuery(new Term("INCN", "(BISIS)5920")), Occur.MUST);
			List<OrganizationUnitDTO> organizationUnitList = getOrganizationUnits(bq, null, null, new AllDocCollector(false));
	        allList.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.organizationUnit.pleaseSelect")));
	        if((institution!=null) && (institution.getControlNumber()!=null)){
		        for (OrganizationUnitDTO ou : organizationUnitList){
		            if((ou.getInstitution() != null) && (institution.getControlNumber().equals(ou.getInstitution().getControlNumber()))){
		            	allList.add(new SelectItem(ou, ou.toString()));
		            	addSubunits(organizationUnitList, ou, allList);
		            }
		        }
	        }
		} catch (ParseException e) {
		}
	}



	private void addSubunits(List<OrganizationUnitDTO> organizationUnitList,
			OrganizationUnitDTO superOu, List<SelectItem> allList) {
		for (OrganizationUnitDTO ou : organizationUnitList){
            if((ou.getSuperOrganizationUnit() != null) && (superOu.getControlNumber().equals(ou.getSuperOrganizationUnit().getControlNumber()))){
            	allList.add(new SelectItem(ou, ou.toString()));
            	addSubunits(organizationUnitList, ou, allList);
            }
        }	
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedOrganizationUnit = null;
		populateList = false;
		institution = null;
		allList = null;
		tableModalPanel = "organizationUnitBrowseModalPanel";
		editModalPanel = "organizationUnitEditModalPanel";
		duplicateOrganizationUnits = new ArrayList<OrganizationUnitDTO>();
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
			List<OrganizationUnitDTO> listTmp = getOrganizationUnits(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
//			if (init == true && listTmp.size() != 0
//					&& selectedOrganizationUnit != null
//					&& selectedOrganizationUnit.getControlNumber() != null) {
//
//				int index = -1;
//				for (int i = 0; i < listTmp.size(); i++) {
//					if (listTmp.get(i).getControlNumber().equals(
//							selectedOrganizationUnit.getControlNumber())) {
//						index = i;
//					}
//				}
//				if (index != -1) {
//					int page = index / 10;
//					tableComponent.setFirst(10 * page);
//				}
//				init = false;
//			}

			list = listTmp;
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<OrganizationUnitDTO>();
		}
	}
	
	/**
	 * Retrieves a list of organizationUnits that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving organizationUnits
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of organizationUnits that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<OrganizationUnitDTO> getOrganizationUnits(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getOrganizationUnits(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of organizationUnits that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving organizationUnits
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of organizationUnits that correspond to the query
	 */
	private List<OrganizationUnitDTO> getOrganizationUnits(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<OrganizationUnitDTO> retVal = new ArrayList<OrganizationUnitDTO>();
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				OrganizationUnitDTO dto = (OrganizationUnitDTO) record.getDto();
				if (dto != null)
					retVal.add(dto);
			} catch (Exception e) {
				log.error(e);
			}
		}
		if(orderList == true){
			orderList = false;
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			if ((orderBy != null) && (!"".equals(orderBy))) {
				if (direction != null) {
					orderByList.add(orderBy);
					directionsList.add(direction);
				}
			}
			orderByList.add("controlNumber");
			directionsList.add("asc");
			Collections.sort(retVal, new GenericComparator<OrganizationUnitDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of organizationUnits (filtered and ordered by ...)
	 */
	public List<OrganizationUnitDTO> getOrganizationUnits() {
		if(populateList){
			populateList();
			populateList = false;
		}
		if(orderList == true){
			orderList = false;
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			if ((orderBy != null) && (!"".equals(orderBy))) {
				if (direction != null) {
					orderByList.add(orderBy);
					directionsList.add(direction);
				}
			}
			orderByList.add("controlNumber");
			directionsList.add("asc");
			Collections.sort(list, new GenericComparator<OrganizationUnitDTO>(
					orderByList, directionsList));
		}
		return list;
	}

	/**
	 * Creates query.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createQuery() throws ParseException {
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("TYPE", Types.ORGANIZATION_UNIT)), Occur.MUST);
		return bq;
	}
	
	List<OrganizationUnitDTO> similarOrganizationUnits = null;
	
	/**
	 * @return the list of SIMILAR organizationUnits with selected organizationUnit
	 */
	public List<OrganizationUnitDTO> getSimilarOrganizationUnits() {
		return similarOrganizationUnits;
	}

	/**
	 * Creates query for finding SIMILAR organizationUnits with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarOrganizationUnitsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedOrganizationUnit.getName().getContent()!=null)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedOrganizationUnit.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		else if(selectedOrganizationUnit.getNameTranslations().size() > 0)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedOrganizationUnit.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
		bq.add(new TermQuery(new Term("TYPE", Types.ORGANIZATION_UNIT)), Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected organizationUnit
	 */
	public OrganizationUnitDTO getSelectedOrganizationUnit() {
		return selectedOrganizationUnit;
	}

	/**
	 * @param organizationUnitDTO
	 *            the organizationUnit to set as selected organizationUnit
	 */
	public void setSelectedOrganizationUnit(OrganizationUnitDTO organizationUnitDTO) {
		selectedOrganizationUnit = organizationUnitDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedOrganizationUnit = findOrganizationUnitByControlNumber(list);
		if (selectedOrganizationUnit != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedOrganizationUnit);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedOrganizationUnit = new OrganizationUnitDTO();
		OrganizationUnitDTO superOrganizationUnit = findOrganizationUnitByControlNumber(list);
		if(superOrganizationUnit == null)
			superOrganizationUnit = new OrganizationUnitDTO();
		selectedOrganizationUnit.setSuperOrganizationUnit(superOrganizationUnit);
		institution = new InstitutionDTO();
		selectedOrganizationUnit.setInstitution(institution);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedOrganizationUnit = findOrganizationUnitByControlNumber(list);
		if (selectedOrganizationUnit != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedOrganizationUnit);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			debug("findSimilarOrganizationUnits");
			similarOrganizationUnits = getOrganizationUnits(createSimilarOrganizationUnitsQuery(),
					null, null, new TopDocCollector(5));
			editTabNumber = 1;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("findSimilarOrganizationUnits", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedOrganizationUnit.setNotLoaded(true);
		super.switchToEditNoneMode();
	}
	
	public void examineData(){
		OrganizationUnitDTO tempOrganizationUnit = findOrganizationUnitByControlNumber(similarOrganizationUnits);
		if(tempOrganizationUnit != null){
			if((selectedOrganizationUnit.getName().getLanguage() != null) && (selectedOrganizationUnit.getName().getLanguage().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:language", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getName().getLanguage(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getName().getContent() != null) && (selectedOrganizationUnit.getName().getContent().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:name", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getName().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getPlace() != null) && (selectedOrganizationUnit.getPlace().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:place", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getPlace(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getAcro() != null) && (selectedOrganizationUnit.getAcro().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:acro", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getAcro(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getInstitution().toString() != null) && (selectedOrganizationUnit.getInstitution().toString().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:institution", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getInstitution().toString(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getSuperOrganizationUnit().toString() != null) && (selectedOrganizationUnit.getSuperOrganizationUnit().toString().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:superOrganizationUnit", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getSuperOrganizationUnit().toString(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getKeywords().getContent() != null) && (selectedOrganizationUnit.getKeywords().getContent().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:keywords", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getKeywords().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getUri() != null) && (selectedOrganizationUnit.getUri().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:uri", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getUri(), FacesContext
							.getCurrentInstance());
			selectedOrganizationUnit = tempOrganizationUnit;
		}
		editTabNumber = 0;
	}
	
	public void mergeData(){
		OrganizationUnitDTO mergeOrganizationUnit = findOrganizationUnitByControlNumber(similarOrganizationUnits);
		if(mergeOrganizationUnit != null){
			selectedOrganizationUnit.setControlNumber(mergeOrganizationUnit.getControlNumber());
			OrganizationUnitDTO tempOrganizationUnit = selectedOrganizationUnit;
			selectedOrganizationUnit = mergeOrganizationUnit;
			if((selectedOrganizationUnit.getName().getLanguage() != null) && (selectedOrganizationUnit.getName().getLanguage().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:language", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getName().getLanguage(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getName().getContent() != null) && (selectedOrganizationUnit.getName().getContent().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:name", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getName().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getPlace() != null) && (selectedOrganizationUnit.getPlace().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:place", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getPlace(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getAcro() != null) && (selectedOrganizationUnit.getAcro().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:acro", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getAcro(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getSuperOrganizationUnit().toString() != null) && (selectedOrganizationUnit.getSuperOrganizationUnit().toString().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:superOrganizationUnit", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getSuperOrganizationUnit().toString(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getKeywords().getContent() != null) && (selectedOrganizationUnit.getKeywords().getContent().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:keywords", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getKeywords().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedOrganizationUnit.getUri() != null) && (selectedOrganizationUnit.getUri().trim().length()>0))
				facesMessages.addToControl(
					"organizationUnitEditForm:uri", FacesMessage.SEVERITY_INFO, 
					selectedOrganizationUnit.getUri(), FacesContext
							.getCurrentInstance());
			selectedOrganizationUnit = tempOrganizationUnit;
			editTabNumber = 0;
		}
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
				selectedOrganizationUnit)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"organizationUnitEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.organizationUnit.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"organizationUnitEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.organizationUnit.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedOrganizationUnit);
			sendRecordMessage(selectedOrganizationUnit, "update");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		similarOrganizationUnits = null;
		if(editTabNumber == 0){
			try {
				debug("findSsimilarOrganizationUnits");
				similarOrganizationUnits = getOrganizationUnits(createSimilarOrganizationUnitsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarConferences", e);
			}
		}
		if((similarOrganizationUnits == null ) || (similarOrganizationUnits.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
					selectedOrganizationUnit)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"organizationUnitEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.organizationUnit.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"organizationUnitEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.organizationUnit.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedOrganizationUnit);
				newRecordEmailNotification(selectedOrganizationUnit, facesMessages.getMessageFromResourceBundle("records.organizationUnit.newOrganizationUnitNotification.subject"));
			}
		} else {
			nextEditTab();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedOrganizationUnit = findOrganizationUnitByControlNumber(list);
		if (selectedOrganizationUnit == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.ORGANIZATION_UNIT, 
				selectedOrganizationUnit)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"organizationUnitTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.organizationUnit.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedOrganizationUnit);
			selectedOrganizationUnit = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * Prepares web form where user can choose Research Area for selected organizationUnit
	 */
	public void pickResearchArea() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ResearchAreaManagedBean mb = (ResearchAreaManagedBean) extCtx.getSessionMap().get(
				"researchAreaManagedBean");
		if (mb == null) {
			mb = new ResearchAreaManagedBean();
			extCtx.getSessionMap().put("researchAreaManagedBean", mb);
		}

		mb.setOrganizationUnit(null);
		mb.setIPickResearchAreaManagedBean(this);
		mb.setSelectedResearchArea(new ResearchAreaDTO());
		mb.setPickMessage("records.organizationUnit.pickResearchAreaMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();

	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#setResearchArea(rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO)
	 */
	@Override
	public void setResearchArea(ResearchAreaDTO researchArea) {
		if ((selectedOrganizationUnit.getResearchAreas().contains(researchArea))) {
			selectedOrganizationUnit.getResearchAreas().set(
					selectedOrganizationUnit.getResearchAreas().indexOf(researchArea), researchArea);
		} else {
			selectedOrganizationUnit.getResearchAreas().add(researchArea);
		}
	}
	

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#cancelPickingResearchArea()
	 */
	@Override
	public void cancelPickingResearchArea() {
	}

	/**
	 * Removes the selected research area from the list of research Areas
	 */
	public void removeResearchArea() {
		ResearchAreaDTO selectedResearchArea = findResearchAreaByControlNumber();
		if (selectedResearchArea != null) 
			selectedOrganizationUnit.getResearchAreas().remove(selectedResearchArea);
	}
	
	private ResearchAreaDTO findResearchAreaByControlNumber() {
		ResearchAreaDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (ResearchAreaDTO dto : selectedOrganizationUnit.getResearchAreas()) {
				if ((dto.getClassId() != null)
						&& (dto.getClassId()
								.equalsIgnoreCase(classId))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}
	
	/**
	 * Sets chosen organizationUnit to the CRUDManagedBean which wanted to pick
	 * organizationUnit
	 */
	public void chooseOrganizationUnit() {

		try {
			selectedOrganizationUnit = findOrganizationUnitByControlNumber(list);
			if (selectedOrganizationUnit != null) {
				iPickOrganizationUnitManagedBean.setOrganizationUnit(selectedOrganizationUnit);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseOrganizationUnit", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedOrganizationUnit = findOrganizationUnitByControlNumber(similarOrganizationUnits);
			if (selectedOrganizationUnit != null) {
				iPickOrganizationUnitManagedBean.setOrganizationUnit(selectedOrganizationUnit);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarConference", e);
		}
	}

	private IPickOrganizationUnitManagedBean iPickOrganizationUnitManagedBean = null;

	/**
	 * @param iPickOrganizationUnitManagedBean
	 *            the CRUDManagedBean which wants to pick organizationUnit
	 */
	public void setIPickOrganizationUnitManagedBean(
			IPickOrganizationUnitManagedBean iPickOrganizationUnitManagedBean) {
		this.iPickOrganizationUnitManagedBean = iPickOrganizationUnitManagedBean;
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

	private OrganizationUnitDTO findOrganizationUnitByControlNumber(List<OrganizationUnitDTO> organizationUnitsList) {
		OrganizationUnitDTO retVal = null;
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");
		retVal = findOrganizationUnitByControlNumber(organizationUnitsList, controlNumber);
		return retVal;
	}

	private OrganizationUnitDTO findOrganizationUnitByControlNumber(List<OrganizationUnitDTO> organizationUnitsList, String controlNumber) {
		OrganizationUnitDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			for (OrganizationUnitDTO dto : organizationUnitsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
						.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					if(retVal.getSuperOrganizationUnit() == null)
						retVal.setSuperOrganizationUnit(new OrganizationUnitDTO());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickOrganizationUnitManagedBean.cancelPickingOrganizationUnit();
		}
		super.switchToTableNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK))) {
			iPickOrganizationUnitManagedBean.setOrganizationUnit(selectedOrganizationUnit);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedOrganizationUnit.getNameTranslations(), false, "records.organizationUnit.editPanel.nameTranslations.panelHeader", "records.organizationUnit.editPanel.nameTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedOrganizationUnit.getKeywordsTranslations(), false, "records.organizationUnit.editPanel.keywordsTranslations.panelHeader", "records.organizationUnit.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void processDrop(DragDropEvent event){
		OrganizationUnitDTO drop = (OrganizationUnitDTO)findOrganizationUnitByControlNumber(list, event.getDropId());
		OrganizationUnitDTO drag = (OrganizationUnitDTO)findOrganizationUnitByControlNumber(list, event.getDragId());
		if(!(drag.equals(drop)) && (!(isChild(drop, drag)))) {
			drag.setSuperOrganizationUnit(drop);
			if(recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
					drag)))
				sendRecordMessage(drag, "update");
			populateList = true;
		}
	}


//	public Boolean isOpened(UITree uit){
//		OrganizationUnitDTO child = selectedOrganizationUnit;
//		OrganizationUnitDTO parent = (OrganizationUnitDTO)uit.getRowData();
//		boolean result = isChild(child, parent);
//    	if (result)
//    		return new Boolean(true);
//    	else
//    		return null;
//	}
	
	private boolean isChild(OrganizationUnitDTO child, OrganizationUnitDTO parent){
		try{
			if(child.getSuperOrganizationUnit().getControlNumber().equals(parent.getControlNumber())){
				return true;
			}
			return isChild(child.getSuperOrganizationUnit(), parent);
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	private DefaultTreeNode<OrganizationUnitDTO> root = null;
	
	private void addNode(DefaultTreeNode<OrganizationUnitDTO> parentNode, OrganizationUnitDTO parentOrganizationUnit) {
		  for(OrganizationUnitDTO ins:getOrganizationUnits(parentOrganizationUnit)){
			    DefaultTreeNode<OrganizationUnitDTO> node = new DefaultTreeNode<OrganizationUnitDTO>();
				node.setData(ins);
				addNode(node, ins);
				if (parentNode.getChildren() == null)
				  parentNode.setChildren(new ArrayList<>());
				parentNode.getChildren().add(node);
		}
	  }
	  

	private List<OrganizationUnitDTO> getOrganizationUnits(OrganizationUnitDTO parentOrganizationUnit) {
		List<OrganizationUnitDTO> retVal = new ArrayList<OrganizationUnitDTO>();
		for (OrganizationUnitDTO ins : getOrganizationUnits()) {
			if(ins.getSuperOrganizationUnit() != null)
				if(parentOrganizationUnit.getControlNumber().equals(ins.getSuperOrganizationUnit().getControlNumber())){
					retVal.add(ins);
				}
		}
		return retVal;
	}



	public DefaultTreeNode<OrganizationUnitDTO> getRoot() {
		if(populateList){
			getTree();
		}
		return root;
	}

	public void setRoot(DefaultTreeNode<OrganizationUnitDTO> root) {
		this.root = root;
	}
	
	public void getTree() {
		debug("getTree");
		try {
		      root = new DefaultTreeNode<OrganizationUnitDTO>();
		      List<OrganizationUnitDTO> allOrganizationUnits = getOrganizationUnits();
			  for(OrganizationUnitDTO ou:allOrganizationUnits){
					if(((institution == null) && (ou.getSuperOrganizationUnit() == null)) || ((institution != null) && (institution.equals(ou.getInstitution())))){
						DefaultTreeNode<OrganizationUnitDTO> node = new DefaultTreeNode<OrganizationUnitDTO>();
						node.setData(ou);
						addNode(node, ((OrganizationUnitDTO)node.getData()));
						if (root.getChildren() == null)
							root.setChildren(new ArrayList<>());
						root.getChildren().add(node);
					}
		      }
			  
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	
	public void chooseDuplicateOrganizationUnit() {

		try {
			OrganizationUnitDTO duplicateOrganizationUnit = (OrganizationUnitDTO) findOrganizationUnitByControlNumber(list);
			if (duplicateOrganizationUnit != null) {
				if(duplicateOrganizationUnits.contains(duplicateOrganizationUnit))
					duplicateOrganizationUnits.remove(duplicateOrganizationUnit);
				else
					duplicateOrganizationUnits.add(duplicateOrganizationUnit);
			}
		} catch (Exception e) {
			error("chooseDuplicateOrganizationUnit", e);
		}
	}
	
	/**
	 * @return the duplicateOrganizationUnit
	 */
	public String getDuplicateOrganizationUnitsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (OrganizationUnitDTO duplicateOrganizationUnit : duplicateOrganizationUnits) {
			retVal.append(duplicateOrganizationUnit.getStringRepresentation() + "\n");
		}
		return retVal.toString();
	}
	
	public void replaceDuplicateOrganizationUnits(){
		try {
			selectedOrganizationUnit = findOrganizationUnitByControlNumber(list);
			if(selectedOrganizationUnit instanceof OrganizationUnitDTO)
			{
				if(duplicateOrganizationUnits.size()>0)
				{
					if ((selectedOrganizationUnit != null) && (!(duplicateOrganizationUnits.contains(selectedOrganizationUnit)))){
						
						for (OrganizationUnitDTO duplicateOrganizationUnit : duplicateOrganizationUnits) {
							Query query = new TermQuery(new Term("OUCN", duplicateOrganizationUnit.getControlNumber()));
							List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
							for (Record record : list) {
								record.loadFromDatabase();
								if(record.getDto() instanceof OrganizationUnitDTO){
									OrganizationUnitDTO temp = (OrganizationUnitDTO)(record.getDto());
									temp.setSuperOrganizationUnit(selectedOrganizationUnit);
									if(recordDAO.update(new Record(null, null, getUserManagedBean()
											.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
											temp)) == false){
										facesMessages.addToControlFromResourceBundle(
												"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
												"records.organizationUnit.replace.error", FacesContext
														.getCurrentInstance());
										return;
									} else {
										sendRecordMessage(temp, "update");
									} 
								}
								else if(record.getDto() instanceof AuthorDTO){
									AuthorDTO temp = (AuthorDTO)(record.getDto());
									temp.setOrganizationUnit(selectedOrganizationUnit);
									if(recordDAO.update(new Person(null, null, getUserManagedBean()
											.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
											temp, temp.getJmbg(), temp.getDirectPhones(), temp.getLocalPhones(), temp.getApvnt())) == false){
										facesMessages.addToControlFromResourceBundle(
												"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
												"records.author.update.error", FacesContext
														.getCurrentInstance());
										return;
									} else {
										sendRecordMessage(temp, "update");
									}
								}
								else {
									facesMessages.addToControlFromResourceBundle(
											"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
											"records.organizationUnit.replace.error", FacesContext
													.getCurrentInstance());
									return;
								}
								
							} 
							debug("organizationUnit: \n" + duplicateOrganizationUnit +  "\n\nreplaced with: \n" + selectedOrganizationUnit);
							recordDAO.delete(duplicateOrganizationUnit.getRecord());
						}
						facesMessages.addToControlFromResourceBundle(
								"institutionPickForm:general", FacesMessage.SEVERITY_INFO, 
								"records.organizationUnit.replace.success", FacesContext
										.getCurrentInstance());
						selectedOrganizationUnit = null;
						populateList = true;
						orderList = true;
						duplicateOrganizationUnits = new ArrayList<OrganizationUnitDTO>();
					} else {
						facesMessages.addToControlFromResourceBundle(
								"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.organizationUnit.replace.error", FacesContext
										.getCurrentInstance());
					}
				}
			}
		} catch (Exception e) {
			error("replaceDuplicateOrganizationUnits", e);
		}
	}

}
