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

import org.primefaces.event.DragDropEvent;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for institutions
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class InstitutionManagedBean extends CRUDManagedBean implements IPickResearchAreaManagedBean{

	private List<InstitutionDTO> list;
	
	private List<SelectItem> allList;
	
	private List<InstitutionDTO> unsInstitutionsList;
	
	private List<SelectItem> applicationList;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private InstitutionDTO selectedInstitution = null;
	
	private boolean includeOrganizationUnits = false;
	
	private List<InstitutionDTO> duplicateInstitutions = new ArrayList<InstitutionDTO>();

	
	public InstitutionManagedBean() {
		super();
		pickSimilarMessage = "records.institution.pickSimilarMessage";
		tableModalPanel = "institutionBrowseModalPanel";
		editModalPanel = "institutionEditModalPanel";
		try {
			allList = new ArrayList<SelectItem>();
			unsInstitutionsList = new ArrayList<InstitutionDTO>();
			applicationList = new ArrayList<SelectItem>();
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("TYPE", Types.INSTITUTION)), Occur.MUST);
//			bq.add(new TermQuery(new Term("INCN", "(BISIS)5920")), Occur.MUST);
			List<InstitutionDTO> institutionList = getInstitutions(bq, null, null, new AllDocCollector(false));
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("controlNumber");
			directionsList.add("asc");
			Collections.sort(institutionList, new GenericComparator<InstitutionDTO>(
					orderByList, directionsList));
			allList.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.institution.pleaseSelect")));
			applicationList.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.institution.pleaseSelect")));
			for (InstitutionDTO ins : institutionList){
	            if((ins.getSuperInstitution() != null) && (("(BISIS)5920".equals(ins.getSuperInstitution().getControlNumber())) || (("(BISIS)94894".equals(ins.getSuperInstitution().getControlNumber()))))){
	            	unsInstitutionsList.add(ins);
	            	if(("(BISIS)5920".equals(ins.getSuperInstitution().getControlNumber())))
	            		allList.add(new SelectItem(ins, ins.toString()));
	            	if("(BISIS)5928".equals(ins.getControlNumber()))
	            		applicationList.add(new SelectItem(ins, ins.toString()));
	            }
	        }
			InstitutionDTO otherIns = new InstitutionDTO();
			otherIns.setControlNumber("otherInstitution");
			InstitutionDTO noIns = new InstitutionDTO();
			noIns.setControlNumber("noInstitution");
			noIns.getName().setContent("Nema institucije");
			applicationList.add(new SelectItem(otherIns, facesMessages.getMessageFromResourceBundle("records.institution.otherInstitution")));
			applicationList.add(new SelectItem(noIns, facesMessages.getMessageFromResourceBundle("records.institution.noInstitution")));
		} catch (ParseException e) {
		}
	}



	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		includeOrganizationUnits = true;
		selectedInstitution = null;
		populateList = false;
		tableModalPanel = "institutionBrowseModalPanel";
		editModalPanel = "institutionEditModalPanel";
		duplicateInstitutions = new ArrayList<InstitutionDTO>();
		return super.resetForm();
	}
	
	/**
	 * @return the allList
	 */
	public List<SelectItem> getAllList() {
		if((getUserManagedBean().getJobAd()) && (getUserManagedBean().editMode == ModesManagedBean.MODE_REGISTER))
			return applicationList;
		else 
			return allList;
	}

	/**
	 * @param allList the allList to set
	 */
	public void setAllList(List<SelectItem> allList) {
		this.allList = allList;
	}
	
	/**
	 * @return the unsInstitutionsList
	 */
	public List<InstitutionDTO> getUnsInstitutionsList() {
		return unsInstitutionsList;
	}



	/**
	 * @param unsInstitutionsList the unsInstitutionsList to set
	 */
	public void setUnsInstitutionsList(List<InstitutionDTO> unsInstitutionsList) {
		this.unsInstitutionsList = unsInstitutionsList;
	}



	public List<InstitutionDTO> autocompleteUNS(String suggest) {
		List<InstitutionDTO> retVal = new ArrayList<InstitutionDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((InstitutionDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedInstitution!=null && selectedInstitution.getControlNumber() != null){
        	retVal.add(selectedInstitution);
        	return retVal;
        }
        String institutionName = suggest;
    
        BooleanQuery bq = new BooleanQuery();
		if(institutionName != null)
			bq.add(QueryUtils.makeBooleanQuery("NA", institutionName + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.INSTITUTION)), Occur.MUST);
		BooleanQuery ins = new BooleanQuery();
		
		ins.add(new TermQuery(new Term("INCN", "(BISIS)5920")), Occur.SHOULD);
		ins.add(new TermQuery(new Term("INCN", "(BISIS)94894")), Occur.SHOULD);
		
		bq.add(ins, Occur.MUST);

		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				InstitutionDTO dto = (InstitutionDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<InstitutionDTO> listTmp = getInstitutions(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
//			if (init == true && listTmp.size() != 0
//					&& selectedInstitution != null
//					&& selectedInstitution.getControlNumber() != null) {
//
//				int index = -1;
//				for (int i = 0; i < listTmp.size(); i++) {
//					if (listTmp.get(i).getControlNumber().equals(
//							selectedInstitution.getControlNumber())) {
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
			if(includeOrganizationUnits)
				list.addAll(getOrganizationUnitManagedBean().getOrganizationUnits());
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<InstitutionDTO>();
		}
	}
	
	/**
	 * Retrieves a list of institutions that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving institutions
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of institutions that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<InstitutionDTO> getInstitutions(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getInstitutions(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of institutions that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving institutions
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of institutions that correspond to the query
	 */
	private List<InstitutionDTO> getInstitutions(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<InstitutionDTO> retVal = new ArrayList<InstitutionDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				InstitutionDTO dto = (InstitutionDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<InstitutionDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of institutions (filtered and ordered by ...)
	 */
	public List<InstitutionDTO> getInstitutions() {
		if((populateList) || ((includeOrganizationUnits)&&(organizationUnitManagedBean.populateList))){
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
			Collections.sort(list, new GenericComparator<InstitutionDTO>(
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
		bq.add(new TermQuery(new Term("TYPE", Types.INSTITUTION)), Occur.MUST);
		return bq;
	}
	
	/**
	 * @return the includeOrganizationUnits
	 */
	public boolean isIncludeOrganizationUnits() {
		return includeOrganizationUnits;
	}



	/**
	 * @param includeOrganizationUnits the includeOrganizationUnits to set
	 */
	public void setIncludeOrganizationUnits(boolean includeOrganizationUnits) {
		this.includeOrganizationUnits = includeOrganizationUnits;
	}



	List<InstitutionDTO> similarInstitutions = null;
	
	/**
	 * @return the list of SIMILAR institutions with selected institution
	 */
	public List<InstitutionDTO> getSimilarInstitutions() {
		return similarInstitutions;
	}

	/**
	 * Creates query for finding SIMILAR institutions with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarInstitutionsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedInstitution.getName().getContent()!=null)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedInstitution.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		else if(selectedInstitution.getNameTranslations().size() > 0)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedInstitution.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
		bq.add(new TermQuery(new Term("TYPE", Types.INSTITUTION)), Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected institution
	 */
	public InstitutionDTO getSelectedInstitution() {
		return selectedInstitution;
	}

	/**
	 * @param institutionDTO
	 *            the institution to set as selected institution
	 */
	public void setSelectedInstitution(InstitutionDTO institutionDTO) {
		selectedInstitution = institutionDTO;
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		institutionControlNumber = null;
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedInstitution = findInstitutionByControlNumber(list);
		if (selectedInstitution != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedInstitution);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedInstitution = new InstitutionDTO();
		InstitutionDTO superInstitution = findInstitutionByControlNumber(list);
		if(superInstitution == null)
			superInstitution = new InstitutionDTO();
		selectedInstitution.setSuperInstitution(superInstitution);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchOrganizationUnitToAddMode() {
		getOrganizationUnitManagedBean().switchToAddMode();
		selectedInstitution = findInstitutionByControlNumber(list);
		if(selectedInstitution != null){
			getOrganizationUnitManagedBean().setInstitution(selectedInstitution);
			getOrganizationUnitManagedBean().getSelectedOrganizationUnit().setInstitution(selectedInstitution);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedInstitution = findInstitutionByControlNumber(list);
		if (selectedInstitution != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedInstitution);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			debug("findSimilarInstitutions");
			similarInstitutions = getInstitutions(createSimilarInstitutionsQuery(),
					null, null,  new AllDocCollector(true));
			editTabNumber = 1;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("findSimilarInstitutions", e);
		}
	}



	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
				selectedInstitution.setNotLoaded(true);
		super.switchToEditNoneMode();
	}
	
	public void examineData(){
		InstitutionDTO tempInstitution = findInstitutionByControlNumber(similarInstitutions);
		if(tempInstitution != null){
			if((selectedInstitution.getName().getLanguage() != null) && (selectedInstitution.getName().getLanguage().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:language", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getName().getLanguage(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getName().getContent() != null) && (selectedInstitution.getName().getContent().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:name", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getName().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getPlace() != null) && (selectedInstitution.getPlace().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:place", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getPlace(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getAcro() != null) && (selectedInstitution.getAcro().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:acro", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getAcro(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getSuperInstitution().toString() != null) && (selectedInstitution.getSuperInstitution().toString().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:superInstitution", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getSuperInstitution().toString(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getKeywords().getContent() != null) && (selectedInstitution.getKeywords().getContent().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:keywords", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getKeywords().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getUri() != null) && (selectedInstitution.getUri().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:uri", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getUri(), FacesContext
							.getCurrentInstance());
			selectedInstitution = tempInstitution;
		}
		editTabNumber = 0;
	}
	
	public void mergeData(){
		InstitutionDTO mergeInstitution = findInstitutionByControlNumber(similarInstitutions);
		if(mergeInstitution != null){
			selectedInstitution.setControlNumber(mergeInstitution.getControlNumber());
			InstitutionDTO tempInstitution = selectedInstitution;
			selectedInstitution = mergeInstitution;
			if((selectedInstitution.getName().getLanguage() != null) && (selectedInstitution.getName().getLanguage().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:language", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getName().getLanguage(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getName().getContent() != null) && (selectedInstitution.getName().getContent().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:name", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getName().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getPlace() != null) && (selectedInstitution.getPlace().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:place", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getPlace(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getAcro() != null) && (selectedInstitution.getAcro().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:acro", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getAcro(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getSuperInstitution().toString() != null) && (selectedInstitution.getSuperInstitution().toString().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:superInstitution", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getSuperInstitution().toString(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getKeywords().getContent() != null) && (selectedInstitution.getKeywords().getContent().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:keywords", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getKeywords().getContent(), FacesContext
							.getCurrentInstance());
			if((selectedInstitution.getUri() != null) && (selectedInstitution.getUri().trim().length()>0))
				facesMessages.addToControl(
					"institutionEditForm:uri", FacesMessage.SEVERITY_INFO, 
					selectedInstitution.getUri(), FacesContext
							.getCurrentInstance());
			selectedInstitution = tempInstitution;
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
				selectedInstitution)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"institutionEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.institution.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"institutionEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.institution.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedInstitution);
			sendRecordMessage(selectedInstitution, "update");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		similarInstitutions = null;
		if(editTabNumber == 0){
			try {
				debug("findSimilarInstitutions");
				similarInstitutions = getInstitutions(createSimilarInstitutionsQuery(),
						null, null, new AllDocCollector(true));
			} catch (ParseException e) {
				error("findSimilarInstitutions", e);
			}
		}
		if((similarInstitutions == null ) || (similarInstitutions.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
					selectedInstitution)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"institutionEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.institution.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"institutionEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.institution.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedInstitution);
				newRecordEmailNotification(selectedInstitution, facesMessages.getMessageFromResourceBundle("records.institution.newInstitutionNotification.subject"));
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
		selectedInstitution = findInstitutionByControlNumber(list);
		if (selectedInstitution == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.ORGANIZATION_UNIT, 
				selectedInstitution)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.institution.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedInstitution);
			selectedInstitution = null;
			populateList = true;
			orderList = true;
		}
	}
	  
	/**
	 * Prepares web form where user can choose Research Area for selected institution
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

		mb.setInstitution(null);
		mb.setIPickResearchAreaManagedBean(this);
		mb.setSelectedResearchArea(new ResearchAreaDTO());
		mb.setPickMessage("records.institution.pickResearchAreaMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();

	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#setResearchArea(rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO)
	 */
	@Override
	public void setResearchArea(ResearchAreaDTO researchArea) {
		if ((selectedInstitution.getResearchAreas().contains(researchArea))) {
			selectedInstitution.getResearchAreas().set(
					selectedInstitution.getResearchAreas().indexOf(researchArea), researchArea);
		} else {
			selectedInstitution.getResearchAreas().add(researchArea);
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
			selectedInstitution.getResearchAreas().remove(selectedResearchArea);
	}
	
	private ResearchAreaDTO findResearchAreaByControlNumber() {
		ResearchAreaDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String classId = facesCtx.getExternalContext()
					.getRequestParameterMap().get("classId");
			for (ResearchAreaDTO dto : selectedInstitution.getResearchAreas()) {
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
	 * Sets chosen institution to the CRUDManagedBean which wanted to pick
	 * institution
	 */
	public void chooseInstitution() {
		try {
			
			if(institutionControlNumber != null)
				selectedInstitution = findInstitutionByControlNumber(unsInstitutionsList);
			else
				selectedInstitution = findInstitutionByControlNumber(list);
			if (selectedInstitution != null) {
				iPickInstitutionManagedBean.setInstitution(selectedInstitution);
				if(institutionControlNumber != null){
					institutionControlNumber = null;
					selectedInstitution = null;
				}
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseInstitution", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedInstitution = findInstitutionByControlNumber(similarInstitutions);
			if (selectedInstitution != null) {
				iPickInstitutionManagedBean.setInstitution(selectedInstitution);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarInstitution", e);
		}
	}
	
	public void chooseSimilar(InstitutionDTO institution) {
		try {
			selectedInstitution = institution;
			if (selectedInstitution != null) {
				iPickInstitutionManagedBean.setInstitution(selectedInstitution);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarInstitution", e);
		}
	}

	private IPickInstitutionManagedBean iPickInstitutionManagedBean = null;

	/**
	 * @param iPickInstitutionManagedBean
	 *            the CRUDManagedBean which wants to pick institution
	 */
	public void setIPickInstitutionManagedBean(
			IPickInstitutionManagedBean iPickInstitutionManagedBean) {
		this.iPickInstitutionManagedBean = iPickInstitutionManagedBean;
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
		retVal = "institutionPage";
		return retVal;
	}

	private InstitutionDTO findInstitutionByControlNumber(List<InstitutionDTO> institutionsList) {
		InstitutionDTO retVal = null;
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = (institutionControlNumber!=null)?institutionControlNumber:facesCtx.getExternalContext().getRequestParameterMap().get("controlNumber");
		retVal = findInstitutionByControlNumber(institutionsList, controlNumber);
		return retVal;
	}

	private InstitutionDTO findInstitutionByControlNumber(List<InstitutionDTO> institutionsList, String controlNumber) {
		InstitutionDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			for (InstitutionDTO dto : institutionsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
						.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					if(retVal.getSuperInstitution() == null)
						retVal.setSuperInstitution(new InstitutionDTO());
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
			iPickInstitutionManagedBean.cancelPickingInstitution();
		}
		super.switchToTableNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK))) {
			iPickInstitutionManagedBean.setInstitution(selectedInstitution);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedInstitution.getNameTranslations(), false, "records.institution.editPanel.nameTranslations.panelHeader", "records.institution.editPanel.nameTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedInstitution.getKeywordsTranslations(), false, "records.institution.editPanel.keywordsTranslations.panelHeader", "records.institution.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void processDropInstitution(DragDropEvent event){
		InstitutionDTO drop = (InstitutionDTO)findInstitutionByControlNumber(list, event.getDropId());
		OrganizationUnitDTO drag = (OrganizationUnitDTO)findInstitutionByControlNumber(list, event.getDragId());
		if(!(drag.equals(drop)) && (!(isChildInstitution(drop, drag)))) {
			drag.setSuperInstitution(drop);
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
					drag)))
				sendRecordMessage(drag, "update");
			populateList = true;
		}
	}
	
	public void processDropOrganizationUnit(DragDropEvent event){
		InstitutionDTO drop = (InstitutionDTO)findInstitutionByControlNumber(list, event.getDropId());
		OrganizationUnitDTO drag = (OrganizationUnitDTO)findInstitutionByControlNumber(list, event.getDragId());
		if(!(drag.equals(drop))) {
			drag.setInstitution(drop);
			drag.setSuperOrganizationUnit(new OrganizationUnitDTO());
			if(recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
					drag)))
				sendRecordMessage(drag, "update");
			populateList = true;
		}
	}


	public Boolean isOpened(InstitutionDTO parent){
		InstitutionDTO childInstitution = selectedInstitution;
		boolean result = isChildInstitution(childInstitution, parent);
    	if (result)
    		return true;
    	else if (includeOrganizationUnits){
    		OrganizationUnitDTO childOrganizationUnit = getOrganizationUnitManagedBean().getSelectedOrganizationUnit();
    		return isChildOrganizationUnit(childOrganizationUnit, parent);
    	} else
    		return false;
	}
	
	private boolean isChildInstitution(InstitutionDTO child, InstitutionDTO parent){
		try{
			if(child.getSuperInstitution().getControlNumber().equals(parent.getControlNumber())){
				return true;
			}
			return isChildInstitution(child.getSuperInstitution(), parent);
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	private boolean isChildOrganizationUnit(OrganizationUnitDTO child, InstitutionDTO parent){
		try{
			if(child.getInstitution().getControlNumber().equals(parent.getControlNumber())){
				return true;
			}
			if(child.getSuperOrganizationUnit().getControlNumber().equals(parent.getControlNumber())){
				return true;
			}
			if((child.getSuperOrganizationUnit()!=null) && (child.getSuperOrganizationUnit().getControlNumber()!=null))
				return isChildOrganizationUnit(child.getSuperOrganizationUnit(), parent);
			else
				return isChildInstitution(child.getSuperInstitution(), parent);
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	private List<TreeNodeDTO<InstitutionDTO>> rootNodes = null;
	
	public void getTree() {
		debug("getTree");
		try {
		      rootNodes = new ArrayList<TreeNodeDTO<InstitutionDTO>>();
		      List<InstitutionDTO> allInstitutions = getInstitutions(); 
		      Collections.sort(allInstitutions, new GenericComparator<InstitutionDTO>(
						"controlNumber", "asc"));
			  for(InstitutionDTO ins:allInstitutions){
					if(! (ins instanceof OrganizationUnitDTO))
					  	if(ins.getSuperInstitution() == null){
							TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
							addNodeInstitution(node, ins);
							node.setExpanded(isOpened(ins));
							rootNodes.add(node);
						}
		      }
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	private void addNodeInstitution(TreeNodeDTO<InstitutionDTO> parentNode, InstitutionDTO parentInstitution) {
		  for(InstitutionDTO ins:getInstitutions(parentInstitution)){
			  	TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
				addNodeInstitution(node, ins);
				node.setExpanded(isOpened(ins));
				parentNode.addChild(node);
		}
	 }
	
	private List<InstitutionDTO> getInstitutions(InstitutionDTO parentInstitution) {
		List<InstitutionDTO> retVal = new ArrayList<InstitutionDTO>();
		if(parentInstitution instanceof OrganizationUnitDTO){
			for (InstitutionDTO ins : list) {
				if(ins instanceof OrganizationUnitDTO){
				OrganizationUnitDTO ou = (OrganizationUnitDTO)ins;
				if(ou.getSuperOrganizationUnit() != null)
					if(parentInstitution.getControlNumber().equals(ou.getSuperOrganizationUnit().getControlNumber())){
						retVal.add(ins);
					}
				}
			}
		} else {
			for (InstitutionDTO ins : list) {
				if(ins.getSuperInstitution() != null)
					if(parentInstitution.getControlNumber().equals(ins.getSuperInstitution().getControlNumber())){
						retVal.add(ins);
					}
			}
		}
		return retVal;
	}

	/**
	 * @return the rootNodes
	 */
	public List<TreeNodeDTO<InstitutionDTO>> getRootNodes() {
		if((populateList) || ((includeOrganizationUnits)&&(organizationUnitManagedBean.populateList))){
			getTree();
		}
		return rootNodes;
	}



	/**
	 * @param rootNodes the rootNodes to set
	 */
	public void setRootNodes(List<TreeNodeDTO<InstitutionDTO>> rootNodes) {
		this.rootNodes = rootNodes;
	}
	
	private OrganizationUnitManagedBean organizationUnitManagedBean = null;

	/**
	 * @return the OrganizationUnitManagedBean from current session
	 */
	protected OrganizationUnitManagedBean getOrganizationUnitManagedBean() {
		if (organizationUnitManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			OrganizationUnitManagedBean mb = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
					"organizationUnitManagedBean");
			if (mb == null) {
				mb = new OrganizationUnitManagedBean();
				extCtx.getSessionMap().put("organizationUnitManagedBean", mb);
			}
			organizationUnitManagedBean = mb;
		}
		return organizationUnitManagedBean;
	}
	
	public void chooseDuplicateInstitution() {
		
		try {
			InstitutionDTO duplicateInstitution = findInstitutionByControlNumber(list);
			
			
			if (duplicateInstitution != null) {
				if(duplicateInstitutions.contains(duplicateInstitution))
					duplicateInstitutions.remove(duplicateInstitution);
				else
					duplicateInstitutions.add(duplicateInstitution);
			}
		} catch (Exception e) {
			error("chooseDuplicateInstitution", e);
		}
	}
	
	/**
	 * @return the duplicateInstitutions
	 */
	public String getDuplicateInstitutionsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (InstitutionDTO duplicateInstitution : duplicateInstitutions) {
			retVal.append(duplicateInstitution.getStringRepresentation() + "\n");
		}
		return retVal.toString();
	}
	
	public void replaceDuplicateInstitutions(){
		try {
			selectedInstitution = findInstitutionByControlNumber(list);
			if(selectedInstitution instanceof InstitutionDTO)
			{	
				if(duplicateInstitutions.size()>0)
				{	
					if ((selectedInstitution != null) && (!(duplicateInstitutions.contains(selectedInstitution)))){
						for (InstitutionDTO duplicateInstitution : duplicateInstitutions) {
							Query query = new TermQuery(new Term("INCN", duplicateInstitution.getControlNumber()));
							List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
							
							for (Record record : list) 
							{
								record.loadFromDatabase();
								if(record.getDto() instanceof OrganizationUnitDTO){
									OrganizationUnitDTO temp = (OrganizationUnitDTO)(record.getDto());
									temp.setSuperInstitution(selectedInstitution);
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
								else if(record.getDto() instanceof InstitutionDTO){
									InstitutionDTO temp = (InstitutionDTO)(record.getDto());
									temp.setSuperInstitution(selectedInstitution);
									if(recordDAO.update(new Record(null, null, getUserManagedBean()
											.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
											temp)) == false){
										facesMessages.addToControlFromResourceBundle(
												"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
												"records.institution.replace.error", FacesContext
														.getCurrentInstance());
										return;
									} else {
										sendRecordMessage(temp, "update");
									}
								}
								else if(record.getDto() instanceof AuthorDTO){
									AuthorDTO temp = (AuthorDTO)(record.getDto());
									temp.setInstitution(selectedInstitution);
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
								else if(record.getDto() instanceof StudyFinalDocumentDTO){
									StudyFinalDocumentDTO temp = (StudyFinalDocumentDTO)(record.getDto());
									if(temp.getRecord().getArchived().intValue() == 0){
										temp.setInstitution(selectedInstitution);
										if(recordDAO.update(new Record(null, null, getUserManagedBean()
												.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
												temp)) == false){
											facesMessages.addToControlFromResourceBundle(
													"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
													"records.studyFinalDocument.update.error", FacesContext
															.getCurrentInstance());
											return;
										} else {
											sendRecordMessage(temp, "update");
										}
									}
								}
								else {
									facesMessages.addToControlFromResourceBundle(
											"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
											"records.institution.replace.error", FacesContext
													.getCurrentInstance());
									return;
								}
								
							}
							
							
							debug("institution: \n" + duplicateInstitution +  "\n\nreplaced with: \n" + selectedInstitution);
							recordDAO.delete(duplicateInstitution.getRecord());
						}
						facesMessages.addToControlFromResourceBundle(
								"institutionPickForm:general", FacesMessage.SEVERITY_INFO, 
								"records.institution.replace.success", FacesContext
										.getCurrentInstance());
						selectedInstitution = null;
						populateList = true;
						orderList = true;
						duplicateInstitutions = new ArrayList<InstitutionDTO>();
					} else {
						facesMessages.addToControlFromResourceBundle(
								"institutionPickForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.institution.replace.error", FacesContext
										.getCurrentInstance());
					}
				}
			}
		} catch (Exception e) {
			error("replaceDuplicateInstitutions", e);
		}
	}
	
	private String institutionControlNumber;
	
	public void setInstitutionControlNumber(String controlNumber){
		institutionControlNumber=controlNumber;
		selectedInstitution = (InstitutionDTO)recordDAO.getDTO(controlNumber);
	}
	
	public String getInstitutionControlNumber(){
		return institutionControlNumber;
	}
	
	

}
