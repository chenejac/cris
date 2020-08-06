package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.SwingTreeNodeImpl;
import org.richfaces.model.UploadedFile;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.EntityResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeMeasureDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookEntityTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeMappingDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.EntityResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookEntityTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeMappingDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.ModesManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;


/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class RuleBookImplementationManagedBean extends CRUDManagedBean {
	
	private List<RuleBookImplementationDTO> list;
	private RuleBookImplementationDTO selectedRuleBookImplementation = null;
	private RuleBookDTO superRuleBook = null;
	
	List<RuleBookImplementationDTO> similarRuleBookImplementations = null;
	
	private SwingTreeNodeImpl<RuleBookImplementationDTO> root = null;
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickRuleBookImplementationManagedBean iPickRuleBookImplementationManagedBean = null;
	private String pickMessage;
	
	/**
	 * 
	 */
	public RuleBookImplementationManagedBean() {
		super();
		pickSimilarMessage = "records.ruleBookImplementation.pickSimilarMessage";
		tableModalPanel = "ruleBookImplementationBrowseModalPanel";
		editModalPanel = "ruleBookImplementationEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedRuleBookImplementation = null;
		superRuleBook = null;
		if(list != null)
			populateList = false;
		tableModalPanel = "ruleBookImplementationBrowseModalPanel";
		editModalPanel = "ruleBookImplementationEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @return the superRuleBook
	 */
	public RuleBookDTO getSuperRuleBook() {
		return superRuleBook;
	}

	/**
	 * @param superRuleBook the superRuleBook to set
	 */
	public void setSuperRuleBook(RuleBookDTO superRuleBook) {
		this.superRuleBook = superRuleBook;
		populateList = true;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<RuleBookImplementationDTO> allRuleBookImplementations = getRuleBookImplementations(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
			
			if(superRuleBook!=null && superRuleBook.getControlNumber()!=null){   
				list = new ArrayList<RuleBookImplementationDTO>();
				for (RuleBookImplementationDTO el : allRuleBookImplementations){
		            if((el.getSuperRuleBook() != null) && (superRuleBook.getControlNumber().equals(el.getSuperRuleBook().getControlNumber()))){
		            	list.add(el);
		            	addAllSubElements(list, el, allRuleBookImplementations);
		            }
		        }
			}
			else {
				list = allRuleBookImplementations;
			}
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<RuleBookImplementationDTO>();
		}
	}
	
	private void addAllSubElements(List<RuleBookImplementationDTO> listResult, RuleBookImplementationDTO parentRuleBookImplementation, List<RuleBookImplementationDTO> allRuleBookImplementations){
		for (RuleBookImplementationDTO el : allRuleBookImplementations){
            if((el.getSuperRuleBookImplementation() != null) && (parentRuleBookImplementation.getControlNumber().equals(el.getSuperRuleBookImplementation().getControlNumber()))){
            	listResult.add(el);
            	addAllSubElements(listResult, el, allRuleBookImplementations);
            }
        }	
	}
	
	/**
	 * Creates query.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createQuery() throws ParseException {
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK_IMPLEMENTATION)), Occur.MUST);
		return bq;
	}
	
	/**
	 * Retrieves a list of ruleBookImplementations that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving ruleBookImplementations
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of ruleBookImplementations that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<RuleBookImplementationDTO> getRuleBookImplementations(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getRuleBookImplementations(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of ruleBookImplementations that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving ruleBookImplementations
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of ruleBookImplementations that correspond to the query
	 */
	private List<RuleBookImplementationDTO> getRuleBookImplementations(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<RuleBookImplementationDTO> retVal = new ArrayList<RuleBookImplementationDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				RuleBookImplementationDTO dto = (RuleBookImplementationDTO) record.getDto();
				if (dto != null){
					dto.setFiles(recordDAO.getFilesFromDatabase(dto.getControlNumber()));
					retVal.add(dto);
				}
					
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
			Collections.sort(retVal, new GenericComparator<RuleBookImplementationDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
	/**
	 * @return the list of ruleBookImplementations (filtered and ordered by ...)
	 */
	public List<RuleBookImplementationDTO> getRuleBookImplementations() {
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
			Collections.sort(list, new GenericComparator<RuleBookImplementationDTO>(
					orderByList, directionsList));
		}
		return list;
	}
	
	/**
	 * @return the ruleBookImplementation (filtered and ordered by ...)
	 */
	public RuleBookImplementationDTO getRuleBookImplementationByRuleBookUserCode(String ruleBookUserCode) {
		RuleBookImplementationDTO retVal = null;
		
		if(ruleBookUserCode!= null && !"".equals(ruleBookUserCode)){
			BooleanQuery bq = new BooleanQuery();
	        bq.add(new TermQuery(new Term("LN", ruleBookUserCode)), Occur.MUST);
	        bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK_IMPLEMENTATION)), Occur.MUST);
			
			List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
			if(listRecord.size()>0){
				try {
					RuleBookImplementationDTO dto = (RuleBookImplementationDTO) listRecord.get(0).getDto();
					if (dto != null) {
						dto.setFiles(recordDAO.getFilesFromDatabase(dto.getControlNumber()));
						retVal=dto;
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		return retVal;
	}
	
	public List<RuleBookImplementationDTO> autocompleteUserCode(String suggest) {
		List<RuleBookImplementationDTO> retVal = new ArrayList<RuleBookImplementationDTO>();
		
		String ruleBookUserCode = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(ruleBookUserCode != null && !"".equals(ruleBookUserCode)){
			bq.add(QueryUtils.makeBooleanQuery("LN", ruleBookUserCode + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("LN", ruleBookUserCode + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK_IMPLEMENTATION)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				RuleBookImplementationDTO dto = (RuleBookImplementationDTO) recordDTO.getDto();
				if (dto != null) {
					dto.setFiles(recordDAO.getFilesFromDatabase(dto.getControlNumber()));
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<RuleBookImplementationDTO> autocompleteName(String suggest) {
		List<RuleBookImplementationDTO> retVal = new ArrayList<RuleBookImplementationDTO>();
		
		String ruleBookImplementationName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(ruleBookImplementationName != null && !"".equals(ruleBookImplementationName)){
			bq.add(QueryUtils.makeBooleanQuery("NA", ruleBookImplementationName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", ruleBookImplementationName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK_IMPLEMENTATION)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				RuleBookImplementationDTO dto = (RuleBookImplementationDTO) recordDTO.getDto();
				if (dto != null) {
					dto.setFiles(recordDAO.getFilesFromDatabase(dto.getControlNumber()));
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	/**
	 * Creates query for finding SIMILAR ruleBookImplementations with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarRuleBookImplementationsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedRuleBookImplementation!=null){
			if(selectedRuleBookImplementation.getName().getContent()!=null){
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedRuleBookImplementation.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedRuleBookImplementation.getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
			else if(selectedRuleBookImplementation.getNameTranslations().size() > 0){
				for (MultilingualContentDTO iterable_element : selectedRuleBookImplementation.getNameTranslations()) {
					BooleanQuery bqTr = new BooleanQuery();
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
					bq.add(bqTr, Occur.SHOULD);
				}
			}
			bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK_IMPLEMENTATION)), Occur.MUST);
		}
		return bq;
	}

	/**
	 * @return the selectedRuleBookImplementation
	 */
	public RuleBookImplementationDTO getSelectedRuleBookImplementation() {
		return selectedRuleBookImplementation;
	}

	/**
	 * @param selectedRuleBookImplementation the selectedRuleBookImplementation to set
	 */
	public void setSelectedRuleBookImplementation(
			RuleBookImplementationDTO selectedRuleBookImplementation) {
		this.selectedRuleBookImplementation = selectedRuleBookImplementation;
	}

	/**
	 * @return the list of SIMILAR ruleBookImplementations with selected ruleBookImplementation
	 */
	public List<RuleBookImplementationDTO> getSimilarRuleBookImplementations() {
		return similarRuleBookImplementations;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(list);
		if (selectedRuleBookImplementation != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedRuleBookImplementation);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */

	public void switchToAddModefFromRuleBook(RuleBookDTO superRuleBookParent) {
		super.switchToAddMode();
		selectedRuleBookImplementation = new RuleBookImplementationDTO();
		if (superRuleBookParent != null) {
			selectedRuleBookImplementation.setSuperRuleBook(superRuleBookParent);
			selectedRuleBookImplementation.setResultsTypeGroup(superRuleBookParent.getResultsTypeGroup());
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedRuleBookImplementation = new RuleBookImplementationDTO();
		RuleBookImplementationDTO superRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(list);
		if(superRuleBookImplementation != null){
			selectedRuleBookImplementation.setSuperRuleBookImplementation(superRuleBookImplementation);
			selectedRuleBookImplementation.setResultsTypeGroup(superRuleBookImplementation.getResultsTypeGroup());
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(list);
		if (selectedRuleBookImplementation != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedRuleBookImplementation);
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickRuleBookImplementationManagedBean!=null) {
			iPickRuleBookImplementationManagedBean.cancelPickingRuleBookImplementation();
		}
		super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		super.switchToBrowseMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
	}
	
	@Override
	public void update() {
		if (selectedRuleBookImplementation == null)
			return;
		
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RULEBOOK, 
				selectedRuleBookImplementation)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.ruleBookImplementation.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedRuleBookImplementation);
			sendRecordMessage(selectedRuleBookImplementation, "update");
		}
	}

	@Override
	public void add() {
//		System.out.println("RuleBookImplementationManagedBean-add-1");
		similarRuleBookImplementations = null;
		selectedRuleBookImplementation.getLocale();
		selectedRuleBookImplementation.toString();
		selectedRuleBookImplementation.getHTMLRepresentation();
		
		if(selectedRuleBookImplementation.getRuleBookUserCode()== null || "".equals(selectedRuleBookImplementation.getRuleBookUserCode().trim())){
//			System.out.println("RuleBookImplementationManagedBean-add-1-Error");
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.add.errorRuleBookUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookImplementationManagedBean-add-2");
		if(getRuleBookImplementationByRuleBookUserCode(selectedRuleBookImplementation.getRuleBookUserCode()) != null){
//			System.out.println("RuleBookImplementationManagedBean-add-2-Error");
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.add.errorRuleBookUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookImplementationManagedBean-add-3");
		if(selectedRuleBookImplementation.getResultsTypeGroup() == null || selectedRuleBookImplementation.getResultsTypeGroup().getClassId()==null){
//			System.out.println("RuleBookImplementationManagedBean-add-3-Error");
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.add.errorResultsTypeGroup", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookImplementationManagedBean-add-4");
		
		if(editTabNumber == 0){
			try {
				debug("findSimilarRuleBookImplementations");
				similarRuleBookImplementations = getRuleBookImplementations(createSimilarRuleBookImplementationsQuery(),
						null, null, new AllDocCollector(true));
			} catch (ParseException e) {
				error("findSimilarRuleBookImplementations", e);
			}
		}
		if((similarRuleBookImplementations == null ) || (similarRuleBookImplementations.size()==0)){
//			System.out.println("RuleBookImplementationManagedBean-add-5");
			
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RULEBOOK, 
					selectedRuleBookImplementation)) == false) {
//				System.out.println("RuleBookImplementationManagedBean-add-5-Error");
				facesMessages.addToControlFromResourceBundle(
						"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBookImplementation.add.error", FacesContext
								.getCurrentInstance());
			} else {
//				System.out.println("RuleBookImplementationManagedBean-add-6");
				List<RuleBookResultsTypeDTO> originalRuleBookResultsTypes = null;
				List<RuleBookEntityTypeDTO> originalRuleBookEntityTypes = null;
				List<EntityResultsTypeDTO> originalEntityResultsTypes = null;
				List<RuleBookResultsTypeMappingDTO> originalRuleBookResultsTypeMappings = null;
				
				
				if(selectedRuleBookImplementation.getSuperRuleBookImplementation()!=null){
					originalRuleBookResultsTypes = selectedRuleBookImplementation.getSuperRuleBookImplementation().getRuleBookResultsTypes();
					originalRuleBookEntityTypes = selectedRuleBookImplementation.getSuperRuleBookImplementation().getRuleBookEntityTypes();
					originalEntityResultsTypes = selectedRuleBookImplementation.getSuperRuleBookImplementation().getEntityResultsTypes();
					originalRuleBookResultsTypeMappings = selectedRuleBookImplementation.getSuperRuleBookImplementation().getRuleBookResultsTypeMappings();
				}
				else {
					originalRuleBookResultsTypes = selectedRuleBookImplementation.getSuperRuleBook().getRuleBookResultsTypes();
					originalRuleBookEntityTypes = selectedRuleBookImplementation.getSuperRuleBook().getRuleBookEntityTypes();
					originalEntityResultsTypes = selectedRuleBookImplementation.getSuperRuleBook().getEntityResultsTypes();
					originalRuleBookResultsTypeMappings = selectedRuleBookImplementation.getSuperRuleBook().getRuleBookResultsTypeMappings();
				}
				
				if(originalRuleBookResultsTypes!=null && !originalRuleBookResultsTypes.isEmpty()){
//					System.out.println("RuleBookImplementationManagedBean-add-7");
					List<RuleBookResultsTypeDTO> copyRuleBookResultsTypes = deepCopyRuleBookResultsTypes(selectedRuleBookImplementation, originalRuleBookResultsTypes);
					selectedRuleBookImplementation.getRuleBookResultsTypes().addAll(copyRuleBookResultsTypes);
					
					List<String> orderByList = new ArrayList<String>();
					List<String> directionsList = new ArrayList<String>();
					
					orderByList.add("resultsType");
					directionsList.add("asc");
					Collections.sort(selectedRuleBookImplementation.getRuleBookResultsTypes(), new GenericComparator<RuleBookResultsTypeDTO>(
							orderByList, directionsList));
					
//					System.out.println("RuleBookImplementationManagedBean-add-7.1");
					if(addAllRuleBookResultsTypesToDatabase(copyRuleBookResultsTypes)==false){
//						System.out.println("RuleBookImplementationManagedBean-add-7-Error");
						facesMessages.addToControlFromResourceBundle(
								"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorRuleBookResultsTypes", FacesContext
										.getCurrentInstance());
						return;
						
					}
				}
				
				if(originalRuleBookEntityTypes!=null && !originalRuleBookEntityTypes.isEmpty()){
//					System.out.println("RuleBookImplementationManagedBean-add-8");
					List<RuleBookEntityTypeDTO> copyRuleBookEntityTypes = deepCopyRuleBookEntityTypes(selectedRuleBookImplementation, originalRuleBookEntityTypes);
					selectedRuleBookImplementation.getRuleBookEntityTypes().addAll(copyRuleBookEntityTypes);
					
					List<String> orderByList = new ArrayList<String>();
					List<String> directionsList = new ArrayList<String>();
					
					orderByList.add("entityType");
					directionsList.add("asc");
					Collections.sort(selectedRuleBookImplementation.getRuleBookEntityTypes(), new GenericComparator<RuleBookEntityTypeDTO>(
							orderByList, directionsList));
					
//					System.out.println("RuleBookImplementationManagedBean-add-8.1");
					if(addAllRuleBookEntityTypesToDatabase(copyRuleBookEntityTypes)==false){
//						System.out.println("RuleBookImplementationManagedBean-add-8-Error");
						facesMessages.addToControlFromResourceBundle(
								"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorRuleBookEntityTypes", FacesContext
										.getCurrentInstance());
						return;
						
					}
				}
				
				if(originalEntityResultsTypes!=null && !originalEntityResultsTypes.isEmpty()){
//					System.out.println("RuleBookImplementationManagedBean-add-9");
					List<EntityResultsTypeDTO> copyEntityResultsTypes = deepCopyEntityResultsTypes(selectedRuleBookImplementation, originalEntityResultsTypes);
					selectedRuleBookImplementation.getEntityResultsTypes().addAll(copyEntityResultsTypes);
					
					List<String> orderByList = new ArrayList<String>();
					List<String> directionsList = new ArrayList<String>();
					
					orderByList.add("entityType");
					orderByList.add("resultsType");
					directionsList.add("asc");
					directionsList.add("asc");
					Collections.sort(selectedRuleBookImplementation.getEntityResultsTypes(), new GenericComparator<EntityResultsTypeDTO>(
							orderByList, directionsList));
					
//					System.out.println("RuleBookImplementationManagedBean-add-9.1");
					if(addAllEntityResultsTypesToDatabase(copyEntityResultsTypes)==false){
//						System.out.println("RuleBookImplementationManagedBean-add-9-Error");
						facesMessages.addToControlFromResourceBundle(
								"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorEntityResultsTypes", FacesContext
										.getCurrentInstance());
						return;
						
					}
				}
				
				if(originalRuleBookResultsTypeMappings!=null && !originalRuleBookResultsTypeMappings.isEmpty()){
//					System.out.println("RuleBookImplementationManagedBean-add-10");
					List<RuleBookResultsTypeMappingDTO> copyRuleBookResultsTypeMappings = deepCopyRuleBookResultsTypeMappings(selectedRuleBookImplementation, originalRuleBookResultsTypeMappings);
					selectedRuleBookImplementation.getRuleBookResultsTypeMappings().addAll(copyRuleBookResultsTypeMappings);
					
					List<String> orderByList = new ArrayList<String>();
					List<String> directionsList = new ArrayList<String>();
					orderByList.add("researcherRole");
					orderByList.add("entityType");
					orderByList.add("entitySourceType");
					directionsList.add("asc");
					directionsList.add("asc");
					directionsList.add("asc");
					Collections.sort(selectedRuleBookImplementation.getRuleBookResultsTypeMappings(), new GenericComparator<RuleBookResultsTypeMappingDTO>(
							orderByList, directionsList));
					
//					System.out.println("RuleBookImplementationManagedBean-add-10.1");
					if(addAllRuleBookResultsTypeMappings(copyRuleBookResultsTypeMappings)==false){
//						System.out.println("RuleBookImplementationManagedBean-add-10-Error");
						facesMessages.addToControlFromResourceBundle(
								"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorRuleBookResultsTypeMappings", FacesContext
										.getCurrentInstance());
						return;
					}
					selectedRuleBookImplementation.setNotLoadedRuleBookResultsTypeMappings(true);
				}
				
				
//				System.out.println("RuleBookImplementationManagedBean-add-11");
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.ruleBookImplementation.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedRuleBookImplementation);
				newRecordEmailNotification(selectedRuleBookImplementation, facesMessages.getMessageFromResourceBundle("records.ruleBookImplementation.newRuleBookImplementationNotification.subject"));
//				System.out.println("RuleBookImplementationManagedBean-add-11");
			}
		} else {
			nextEditTab();
		}
	}
	
	@Override
	public void delete() {
		selectedRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(list);
		if (selectedRuleBookImplementation == null)
			return;
		
		//PRVO TREBA DA OSE OBRISU SVI RULEBOOKIMPLEMENTATION KOJI SU PODREDJENI
		for(RuleBookImplementationDTO el:getAllSubElements(selectedRuleBookImplementation)){
			delete(el);
		}
		
		if (recordDAO.delete(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(100), CerifEntitiesNames.RULEBOOK, 
				selectedRuleBookImplementation)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedRuleBookImplementation);
			selectedRuleBookImplementation = null;
			populateList = true;
			orderList = true;
		}
	}

	public void delete(RuleBookImplementationDTO book) {
		
		//PRVO TREBA DA OSE OBRISU SVI RULEBOOKIMPLEMENTATION KOJI SU PODREDJENI
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			delete(el);
		}
		
		if (recordDAO.delete(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RULEBOOK, 
				book)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedRuleBookImplementation);
			if(selectedRuleBookImplementation == book)
				selectedRuleBookImplementation = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * Sets chosen RuleBookImplementation to the CRUDManagedBean which wanted to pick
	 * RuleBookImplementation
	 */
	public void chooseRuleBookImplementation() {

		try {
			selectedRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(list);
			if (selectedRuleBookImplementation != null && iPickRuleBookImplementationManagedBean!=null) {
				iPickRuleBookImplementationManagedBean.setRuleBookImplementation(selectedRuleBookImplementation);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBookImplementation", e);
		}
	}

	/**
	 * Sets chosen ruleBookImplementation to the CRUDManagedBean which wanted to pick
	 * ruleBookImplementation
	 */
	public void chooseSimilar() {

		try {
			selectedRuleBookImplementation = findRuleBookImplementationTypeByControlNumber(similarRuleBookImplementations);
			if (selectedRuleBookImplementation != null) {
				iPickRuleBookImplementationManagedBean.setRuleBookImplementation(selectedRuleBookImplementation);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
			editTabNumber = 0;
			setEditMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBookImplementation", e);
		}
	}
	
	/**
	 * @param iPickRuleBookImplementationManagedBean the iPickRuleBookImplementationManagedBean to set
	 */
	public void setiPickRuleBookImplementationManagedBean(
			IPickRuleBookImplementationManagedBean iPickRuleBookImplementationManagedBean) {
		this.iPickRuleBookImplementationManagedBean = iPickRuleBookImplementationManagedBean;
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
			if(iPickRuleBookImplementationManagedBean!=null)
				iPickRuleBookImplementationManagedBean.setRuleBookImplementation(selectedRuleBookImplementation);
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
		retVal = "ruleBookImplementationPage";
		return retVal;
	}

	private RuleBookImplementationDTO findRuleBookImplementationTypeByControlNumber(List<RuleBookImplementationDTO> ruleBookImplementationsList) {
		RuleBookImplementationDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (RuleBookImplementationDTO dto : ruleBookImplementationsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
								.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}
	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedRuleBookImplementation.getNameTranslations(), false, "records.ruleBookImplementation.editPanel.nameTranslations.panelHeader", "records.ruleBookImplementation.editPanel.nameTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedRuleBookImplementation.getDescriptionTranslations(), false, "records.ruleBookImplementation.editPanel.descriptionTranslations.panelHeader", "records.ruleBookImplementation.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedRuleBookImplementation.getKeywordsTranslations(), false, "records.ruleBookImplementation.editPanel.keywordsTranslations.panelHeader", "records.ruleBookImplementation.editPanel.keywordsTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		if(selectedRuleBookImplementation.getStartDate()!=null)
			return selectedRuleBookImplementation.getStartDate().getTime();
		else 
			return null;
	}
	
	public void setStartDate(Date startDate){
		if(startDate!=null){
			if(selectedRuleBookImplementation.getStartDate()==null){
				Calendar stC = new GregorianCalendar();
				selectedRuleBookImplementation.setStartDate(stC);
			}
			selectedRuleBookImplementation.getStartDate().setTime(startDate);
		}
	}
	
	public Date getEndDate(){
		if(selectedRuleBookImplementation.getEndDate()!=null)
			return selectedRuleBookImplementation.getEndDate().getTime();
		else 
			return null;
	}
	
	public void setEndDate(Date endDate){
		if(endDate!=null){
			if(selectedRuleBookImplementation.getEndDate()==null){
				Calendar enC = new GregorianCalendar();
				selectedRuleBookImplementation.setEndDate(enC);
			}
			selectedRuleBookImplementation.getEndDate().setTime(endDate);
		}
	}
	
	private boolean isChild(RuleBookImplementationDTO child, RuleBookImplementationDTO parent){
		try{

			RuleBookImplementationDTO childRuleBookImplementation = (RuleBookImplementationDTO) child;
			if(childRuleBookImplementation.getSuperRuleBook() != null )
				if(childRuleBookImplementation.getSuperRuleBook().getControlNumber().equals(parent.getControlNumber())){
					return true;
				}
			if(childRuleBookImplementation.getSuperRuleBookImplementation() != null )
				if(childRuleBookImplementation.getSuperRuleBookImplementation().getControlNumber().equals(parent.getControlNumber())){
					return true;
				}
			else if((childRuleBookImplementation.getSuperRuleBookImplementation()!=null) && (childRuleBookImplementation.getSuperRuleBookImplementation().getControlNumber()!=null))
				return isChild(childRuleBookImplementation.getSuperRuleBookImplementation(), parent);
		
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	public void getTree() {
		debug("getTree");
		try {
			root = new SwingTreeNodeImpl<RuleBookImplementationDTO>(); 
			List<RuleBookImplementationDTO> allRuleBookImplementations = getRuleBookImplementations();
			for(RuleBookImplementationDTO el:allRuleBookImplementations){
				if(((superRuleBook == null) && (el.getSuperRuleBook() == null)) || ((superRuleBook != null) && (superRuleBook.equals(el.getSuperRuleBook())))){
					SwingTreeNodeImpl<RuleBookImplementationDTO> node = new SwingTreeNodeImpl<RuleBookImplementationDTO>();
					node.setData(el);
					addSubnodesToNode(node, ((RuleBookImplementationDTO)node.getData()));
					root.addChild(node);
				}
			}	 
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	private void addSubnodesToNode(SwingTreeNodeImpl<RuleBookImplementationDTO> parentNode, RuleBookImplementationDTO parentRuleBook) {
		for(RuleBookImplementationDTO el:getAllSubElements(parentRuleBook)){		
			SwingTreeNodeImpl<RuleBookImplementationDTO> node = new SwingTreeNodeImpl<RuleBookImplementationDTO>();
			node.setData(el);
			addSubnodesToNode(node, el);
			parentNode.addChild(node);
		}
	 }
	
	private List<RuleBookImplementationDTO> getAllSubElements(RuleBookImplementationDTO parentRuleBook) {
		List<RuleBookImplementationDTO> retVal = new ArrayList<RuleBookImplementationDTO>();
		if(parentRuleBook instanceof RuleBookImplementationDTO){
			for (RuleBookImplementationDTO rbi : list) {
				if(rbi.getSuperRuleBookImplementation() != null)
					if(parentRuleBook.getControlNumber().equals(rbi.getSuperRuleBookImplementation().getControlNumber())){
						retVal.add(rbi);
					}
			}
		}

		return retVal;
	}
	
	/**
	 * @return the root
	 */
	public SwingTreeNodeImpl<RuleBookImplementationDTO> getRoot() {
		if(populateList){
			getTree();
		}
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(SwingTreeNodeImpl<RuleBookImplementationDTO> root) {
		this.root = root;
	}

	/**
	 * @return the populateList
	 */
	public boolean isPopulateList(){
		return populateList;
	}
	
	public void uploadListener(FileUploadEvent event) {
        try{
	        FileDTO fileDTO = new FileDTO();
	        UploadedFile item = event.getUploadedFile();
//	        if (item. isTempFile()) {
//	        	 byte[] fileInBytes = new byte[(int)item.getFile().length()];
//	        	 java.io.File tempFile = item.getFile();
//	        	 FileInputStream fileInputStream = new FileInputStream(tempFile);
//	        	 fileInputStream.read(fileInBytes);
//	        	 fileInputStream.close();
//	        	 fileDTO.setData(fileInBytes);
//	        	 fileDTO.setLength(item.getFile().length());
//	        } else {
	        	 fileDTO.setData(item.getData());
	        	 fileDTO.setLength(item.getData().length);
//	        }
	        fileDTO.setFileName((item.getName().lastIndexOf("\\") != -1)?item.getName().substring(item.getName().lastIndexOf("\\")+1):item.getName());
	        fileDTO.setControlNumber(selectedRuleBookImplementation.getControlNumber());
	        fileDTO.setUploader(getUserManagedBean().getLoggedUser().getEmail());
	        selectedRuleBookImplementation.setFile(fileDTO);
        } catch (Exception e) {
        	selectedRuleBookImplementation.setFile(null);
        }
    }

	public void deleteFile(){
		if(selectedRuleBookImplementation.getFile().getControlNumber() != null)
			selectedRuleBookImplementation.setDeletedFile(selectedRuleBookImplementation.getFile());
		selectedRuleBookImplementation.setFile(null);
	}
	
	public boolean changeResultsTypeGroup(RuleBookImplementationDTO  book, RuleBookDTO  bookParent){
		
		loadAllRuleBookImplementationResultTypesFromDatabase(book);
		if(removeAllRuleBookResultsTypesFromDatabase(book)==false){
			return false;
		}
		
		book.getName();
		book.setResultsTypeGroup(bookParent.getResultsTypeGroup());
		
		//ubaci nove rulebook result types od grupe
		//ne treba jer je vec ocitano kod updejta rule book
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		ExternalContext extCtx = facesCtx.getExternalContext();
//		ResultsTypeGroupManagedBean mb = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
//				"resultsTypeGroupManagedBean");
//		if (mb == null) {
//			mb = new ResultsTypeGroupManagedBean();
//			extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mb);
//		}
//		if(book.getResultsTypeGroup()!=null){
//			mb.setSelectedResultsTypeGroup(book.getResultsTypeGroup());
//			mb.loadSelectedResultsTypeGroup(true);
//		}
		
		ResultsTypeGroupDTO rtg = book.getResultsTypeGroup();
		for (ResultsTypeDTO resultsType : rtg.getResultsTypes()) {
			RuleBookResultsTypeDTO ruleBookResultsTypeDTO = new RuleBookResultsTypeDTO(book, resultsType);
			ruleBookResultsTypeDTO.setNotLoadedResultsTypeMeasures(false); 
			book.getRuleBookResultsTypes().add(ruleBookResultsTypeDTO);
		}
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("resultsType");
		directionsList.add("asc");
		Collections.sort(book.getRuleBookResultsTypes(), new GenericComparator<RuleBookResultsTypeDTO>(
				orderByList, directionsList));
		
		if(addAllRuleBookResultsTypesToDatabase(book.getRuleBookResultsTypes())==false){
			return false;
		}
		
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RULEBOOK, 
				book)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookImplementationEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBookImplementation.update.error", FacesContext
							.getCurrentInstance());
			return false;
		}
		
		book.setNotLoadedRuleBookResultsTypesDependentTables();
		
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(changeResultsTypeGroup(el, bookParent)==false){
				return false;
			}
		}	
		
		populateList = true;
		return true;
	}
	
	//--------------------------------------------------------------------RULE BOOK RESULT MEASURES START
	
	private void loadAllRuleBookImplementationResultTypesFromDatabase(RuleBookImplementationDTO  book){
		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
		book.getRuleBookResultsTypes().clear();
		book.getRuleBookResultsTypes().addAll(dao.getAllRuleBookResultsTypes(book));
		for (RuleBookResultsTypeDTO rt : book.getRuleBookResultsTypes()) {
			rt.loadResultsTypeMeasuresFromDatabase();
		}
	}
	
	public boolean removeAllRuleBookResultsTypesFromDatabase(RuleBookImplementationDTO  book) {
		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = book.getRuleBookResultsTypes();
		int upperBoundary = ruleBookResultsTypes.size()-1;
		
		for (int i = upperBoundary; i >= 0; i--) {
			if(dao.deleteRuleBookResultsTypeAndDependacies(ruleBookResultsTypes.get(i))==false){
				return false;
			}
			ruleBookResultsTypes.remove(i);
		}
		ruleBookResultsTypes.clear();
		return true;
	}
	
	public boolean removeAllRuleBookResultsTypeMeasuresFromDatabase(RuleBookImplementationDTO  book) {
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = book.getRuleBookResultsTypes();
		ResultsTypeMeasureDAO daoMeasure = new ResultsTypeMeasureDAO();
		for (RuleBookResultsTypeDTO ruleBookResultsTypeDTO : ruleBookResultsTypes) {
			List<ResultsTypeMeasureDTO> rtmeasures = ruleBookResultsTypeDTO.getResultsTypeMeasures();		
			int upperBoundary = rtmeasures.size()-1;
			
			for (int i = upperBoundary; i >= 0; i--) {
				if(daoMeasure.delete(rtmeasures.get(i))==false){
					return false;
				}
				rtmeasures.remove(i);
			}
			rtmeasures.clear();
		}
		return true;
	}
	
	public boolean updateAllRuleBookResultsTypes(RuleBookImplementationDTO  book,  List<RuleBookResultsTypeDTO> ruleBookResultsTypes){
		if (book == null)
			return false;
		
		loadAllRuleBookImplementationResultTypesFromDatabase(book);
		if(removeAllRuleBookResultsTypesFromDatabase(book)==false){
			return false;
		}
		
		List<RuleBookResultsTypeDTO> copyRuleBookResultsTypes = deepCopyRuleBookResultsTypes(book, ruleBookResultsTypes);
		if(addAllRuleBookResultsTypesToDatabase(copyRuleBookResultsTypes)==false){
			return false;
		}	
		book.getRuleBookResultsTypes().addAll(copyRuleBookResultsTypes);
		
		
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(updateAllRuleBookResultsTypes(el, ruleBookResultsTypes)==false){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean updateAllRuleBookResultsTypeMeasures(RuleBookImplementationDTO  book,  List<RuleBookResultsTypeDTO> ruleBookResultsTypes){
		if (book == null)
			return false;
		
		loadAllRuleBookImplementationResultTypesFromDatabase(book);
		if(removeAllRuleBookResultsTypeMeasuresFromDatabase(book)==false){
			return false;
		}
		
		for (RuleBookResultsTypeDTO ruleBookResultsTypeDTO : ruleBookResultsTypes) {
			RuleBookResultsTypeDTO rrt = book.getRuleBookResultsTypeByResultsType(ruleBookResultsTypeDTO.getResultsType());
			List<ResultsTypeMeasureDTO> copyResultsTypeMeasures = deepCopyRuleBookResultsTypeMeasures(book, rrt, ruleBookResultsTypeDTO.getResultsTypeMeasures());

			if(addAllRuleBookResultsTypeMeasuresToDatabase(copyResultsTypeMeasures)==false){
				return false;
			}
			
			rrt.getResultsTypeMeasures().addAll(copyResultsTypeMeasures);
		}

		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(updateAllRuleBookResultsTypeMeasures(el, ruleBookResultsTypes)==false){
				return false;
			}
		}
		
		return true;
	}
	
	private List<RuleBookResultsTypeDTO> deepCopyRuleBookResultsTypes(RuleBookImplementationDTO  book, List<RuleBookResultsTypeDTO> ruleBookResultsTypes){
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-1");
		List<RuleBookResultsTypeDTO> retVal = new ArrayList<RuleBookResultsTypeDTO>();
		for (int i = 0; i < ruleBookResultsTypes.size(); i++) {
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-2");
			RuleBookResultsTypeDTO ruleBookResultsType = ruleBookResultsTypes.get(i);
			RuleBookResultsTypeDTO newRuleBookResultsType = new RuleBookResultsTypeDTO(book, ruleBookResultsType.getResultsType());
			newRuleBookResultsType.setNotLoadedResultsTypeMeasures(false);
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-3");
			List<ResultsTypeMeasureDTO> resultsTypeMeasures = ruleBookResultsType.getResultsTypeMeasures();
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-4");
			for (int j = 0; j < resultsTypeMeasures.size(); j++) {
//				System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-5");
				ResultsTypeMeasureDTO resultTypeMeasure = resultsTypeMeasures.get(j);
				ResultsTypeMeasureDTO newResultTypeMeasure = new ResultsTypeMeasureDTO(book, resultTypeMeasure.getResultsType(), resultTypeMeasure.getSciencesGroup(), resultTypeMeasure.getQuantitativeMeasure());
				newRuleBookResultsType.getResultsTypeMeasures().add(newResultTypeMeasure);
//				System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-6");
			}
			retVal.add(newRuleBookResultsType);
		}
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypes-7");
		
//		for (RuleBookResultsTypeDTO ruleBookResultsType : retVal) {
//			System.out.println("RB:"+ruleBookResultsType.getRuleBook().getControlNumber()+" results type:"+ruleBookResultsType.getResultsType()+"---------------------------");
//			
//			for (ResultsTypeMeasureDTO rtM : ruleBookResultsType.getResultsTypeMeasures()) {
//				System.out.println("\t\t RB:"+rtM.getRuleBook()+" results type:"+rtM.getResultsType()+" sciences group:"+rtM.getSciencesGroup()+" measure:"+rtM.getQuantitativeMeasure());
//			}
//		}
		
		return retVal;
	}
	
	private List<ResultsTypeMeasureDTO> deepCopyRuleBookResultsTypeMeasures(RuleBookImplementationDTO  book, RuleBookResultsTypeDTO ruleBookResultsType, List<ResultsTypeMeasureDTO> resultsTypeMeasures){
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMeasures-1");
		List<ResultsTypeMeasureDTO> retVal = new ArrayList<ResultsTypeMeasureDTO>();
		for (int i = 0; i < resultsTypeMeasures.size(); i++) {
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMeasures-2");
			ResultsTypeMeasureDTO resultTypeMeasure = resultsTypeMeasures.get(i);
			ResultsTypeMeasureDTO newResultTypeMeasure = new ResultsTypeMeasureDTO(book, ruleBookResultsType.getResultsType(), resultTypeMeasure.getSciencesGroup(), resultTypeMeasure.getQuantitativeMeasure());
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMeasures-3");
			retVal.add(newResultTypeMeasure);
		}
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMeasures-4");
		return retVal;
	}
	
	private boolean addAllRuleBookResultsTypesToDatabase(List<RuleBookResultsTypeDTO> ruleBookResultsTypes) {
		
		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
		for (int i = 0; i < ruleBookResultsTypes.size(); i++) {
			if(dao.addRuleBookResultsTypeAndResultsTypeMeasures(ruleBookResultsTypes.get(i))==false){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean addAllRuleBookResultsTypeMeasuresToDatabase(List<ResultsTypeMeasureDTO> resultsTypeMeasures) {
		
		ResultsTypeMeasureDAO daoMeasure = new ResultsTypeMeasureDAO();
		for (ResultsTypeMeasureDTO resultsTypeMeasureDTO : resultsTypeMeasures) {
			if(daoMeasure.add(resultsTypeMeasureDTO)==false){
				return false;
			}
		}
		
		return true;
	}
	
	public List<ResultsTypeMeasureDTO> getAllResultsTypeMeasures(){
		List<ResultsTypeMeasureDTO> retVal = new ArrayList<ResultsTypeMeasureDTO>();
		
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = selectedRuleBookImplementation.getRuleBookResultsTypes();
		for (RuleBookResultsTypeDTO ruleBookResultsType : ruleBookResultsTypes) {
			List<ResultsTypeMeasureDTO> resultsTypeMeasures = ruleBookResultsType.getResultsTypeMeasures();
			for (ResultsTypeMeasureDTO resultsTypeMeasure : resultsTypeMeasures) {
				retVal.add(resultsTypeMeasure);
			}	
		}
		return retVal;
	}
	
	//--------------------------------------------------------------------RULE BOOK RESULT MEASURES END
	
	
	//--------------------------------------------------------------------RULE BOOK ENTITY TYPES START
	
	
	public boolean addRuleBookEntityTypeToDatabase(RuleBookImplementationDTO  book,  ClassDTO entityType) {
		
		RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
		RuleBookEntityTypeDTO rbet = new RuleBookEntityTypeDTO(book, entityType);
		if(dao.addRuleBookEntityType(rbet)==false){
			return false;	
		}
		
		book.getRuleBookEntityTypes().add(rbet);
				
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(addRuleBookEntityTypeToDatabase(el, entityType)==false){
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteRuleBookEntityTypeToDatabase(RuleBookImplementationDTO  book,  ClassDTO entityType) {
		
		RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
		List<RuleBookEntityTypeDTO> listaTemp = book.getRuleBookEntityTypes();
		int upperBoundaru = listaTemp.size()-1;
		for (int i = upperBoundaru; i >=0; i--) {
			if(listaTemp.get(i).getEntityType().equals(entityType)){
				if(dao.deleteRuleBookEntityType(listaTemp.get(i))==false)
					return false;
				listaTemp.remove(i);
				break;
			}
		}
		//ocitaj ponovo listu Entity Results Type iz baze jer je doslo do brijanja RuleBookEntityType
		book.setNotLoadedRuleBookEntityTypesDependentTables();
		
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(deleteRuleBookEntityTypeToDatabase(el, entityType)==false){
				return false;
			}
		}
		return true;
	}
	
	public List<RuleBookEntityTypeDTO> getAllRuleBookEntityTypes(){
		List<RuleBookEntityTypeDTO> retVal = selectedRuleBookImplementation.getRuleBookEntityTypes();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("entityType");
		directionsList.add("asc");
		
		Collections.sort(retVal, new GenericComparator<RuleBookEntityTypeDTO>(
					orderByList, directionsList));
		return retVal;
	}
	
	private List<RuleBookEntityTypeDTO> deepCopyRuleBookEntityTypes(RuleBookImplementationDTO  book, List<RuleBookEntityTypeDTO> ruleBookEntityTypes){
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookEntityTypes-1");
		List<RuleBookEntityTypeDTO> retVal = new ArrayList<RuleBookEntityTypeDTO>();
		for (int i = 0; i < ruleBookEntityTypes.size(); i++) {
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookEntityTypes-2");
			RuleBookEntityTypeDTO ruleBookEntityType = ruleBookEntityTypes.get(i);
			RuleBookEntityTypeDTO newRuleBookResultsType = new RuleBookEntityTypeDTO(book, ruleBookEntityType.getEntityType());
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookEntityTypes-3");
			retVal.add(newRuleBookResultsType);
		}
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookEntityTypes-4");
		
//		for (RuleBookEntityTypeDTO ruleBookEntityType : retVal) {
//			System.out.println("RB:"+ruleBookEntityType.getRuleBook().getControlNumber()+" entity type:"+ruleBookEntityType.getEntityType()+"---------------------------");
//		}
		return retVal;
	}
	
	private boolean addAllRuleBookEntityTypesToDatabase(List<RuleBookEntityTypeDTO> ruleBookEntityTypes) {
		
		RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
		for (int i = 0; i < ruleBookEntityTypes.size(); i++) {
			if(dao.addRuleBookEntityType(ruleBookEntityTypes.get(i))==false){
				return false;
			}
		}
		return true;
	}
	//--------------------------------------------------------------------RULE BOOK ENTITY TYPES END
	
	//--------------------------------------------------------------------ENTITY RESULTS TYPE START
	
	public boolean addEntityResultsTypeToDatabase(RuleBookImplementationDTO  book, ResultsTypeDTO resultsType, ClassDTO entityType) {
		
		EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
		EntityResultsTypeDTO entityResultsTypeDTO = new EntityResultsTypeDTO(book, resultsType, entityType);
		if(dao.add(entityResultsTypeDTO)==false){
			return false;
		}

		book.getEntityResultsTypes().add(entityResultsTypeDTO);
				
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(addEntityResultsTypeToDatabase(el, resultsType, entityType)==false){
				return false;
			}
		}
		return true;
	}
	
	public boolean deleteEntityResultsTypeToDatabase(RuleBookImplementationDTO  book, ResultsTypeDTO resultsType,  ClassDTO entityType) {
		
		EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
		List<EntityResultsTypeDTO> listaTemp = book.getEntityResultsTypes();
		int upperBoundaru = listaTemp.size()-1;
		for (int i = upperBoundaru; i >=0; i--) {
			if(listaTemp.get(i).getResultsType().equals(resultsType) && listaTemp.get(i).getEntityType().equals(entityType)){
				if(dao.delete(listaTemp.get(i))==false)
					return false;
				listaTemp.remove(i);
				break;
			}
		}
		
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(deleteEntityResultsTypeToDatabase(el, resultsType, entityType)==false){
				return false;
			}
		}
		return true;
	}
	
	public List<EntityResultsTypeDTO> getAllEntityResultsTypes(){
		List<EntityResultsTypeDTO> retVal = selectedRuleBookImplementation.getEntityResultsTypes();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("entityType");
		orderByList.add("resultsType");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(retVal, new GenericComparator<EntityResultsTypeDTO>(
				orderByList, directionsList));
		return retVal;
	}
	
	private List<EntityResultsTypeDTO> deepCopyEntityResultsTypes(RuleBookImplementationDTO  book, List<EntityResultsTypeDTO> entityResultsTypes){
//		System.out.println("RuleBookImplementationManagedBean-deepCopyEntityResultsTypes-1");
		List<EntityResultsTypeDTO> retVal = new ArrayList<EntityResultsTypeDTO>();
		for (int i = 0; i < entityResultsTypes.size(); i++) {
//			System.out.println("RuleBookImplementationManagedBean-deepCopyEntityResultsTypes-2");
			EntityResultsTypeDTO entityResultsType = entityResultsTypes.get(i);
			EntityResultsTypeDTO newEntityResultsType = new EntityResultsTypeDTO(book, entityResultsType.getResultsType(), entityResultsType.getEntityType());
//			System.out.println("RuleBookImplementationManagedBean-deepCopyEntityResultsTypes-3");
			retVal.add(newEntityResultsType);
		}
//		System.out.println("RuleBookImplementationManagedBean-deepCopyEntityResultsTypes-4");
		
//		for (EntityResultsTypeDTO entityResultsType : retVal) {
//			System.out.println("RB:"+entityResultsType.getRuleBook().getControlNumber()+" entity type:"+entityResultsType.getEntityType()+" results type:"+entityResultsType.getResultsType()+"---------------------------");
//		}
		return retVal;
	}
	
	private boolean addAllEntityResultsTypesToDatabase(List<EntityResultsTypeDTO> entityResultsTypes) {
		
		EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
		for (int i = 0; i < entityResultsTypes.size(); i++) {
			if(dao.add(entityResultsTypes.get(i))==false){
				return false;
			}
		}
		return true;
	}
	//--------------------------------------------------------------------ENTITY RESULTS TYPE END
	
	//--------------------------------------------------------------------RULEBOOK RESULTS TYPE MAPPING START
	
	public boolean addRuleBookResultsTypeMappingToDatabase(RuleBookImplementationDTO  book, ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeForResearcherRole, ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		
		RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
		
		RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping = new RuleBookResultsTypeMappingDTO(null, book, researcherRole, entityType, entitySourceType, resultsTypeForResearcherRole,
				resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
		
		if(dao.add(ruleBookResultsTypeMapping)==false){
			return false;
		}

		book.getRuleBookResultsTypeMappings().add(ruleBookResultsTypeMapping);
				
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(addRuleBookResultsTypeMappingToDatabase(el, researcherRole, entityType, entitySourceType, 
					resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource)==false){
				return false;
			}
		}
		book.setNotLoadedRuleBookResultsTypeMappings(true);
		
		return true;
	}
	
	public boolean deleteRuleBookResultsTypeMappingToDatabase(RuleBookImplementationDTO  book, ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeForResearcherRole, ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource) {
		
		RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
		List<RuleBookResultsTypeMappingDTO> listaTemp = book.getRuleBookResultsTypeMappings();
		
		RuleBookResultsTypeMappingDTO rrtm = book.getRuleBookResultsTypeMappingByAllParms(researcherRole, entityType, entitySourceType, 
				resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
		
		int upperBoundaru = listaTemp.size()-1;
		for (int i = upperBoundaru; i >=0; i--) {
			if(listaTemp.get(i).equals(rrtm)){
				if(dao.delete(listaTemp.get(i))==false)
					return false;
				listaTemp.remove(i);
				break;
			}
		}
		
		for(RuleBookImplementationDTO el:getAllSubElements(book)){
			if(deleteRuleBookResultsTypeMappingToDatabase(el, rrtm.getResearcherRole(), rrtm.getEntityType(), rrtm.getEntitySourceType(), rrtm.getResultsTypeForResearcherRole(),
					rrtm.getResultsTypeOfObsEntity(), rrtm.getResultsTypeOfObsEntitySource())==false){
				return false;
			}
		}
		return true;
	}
	
	public List<RuleBookResultsTypeMappingDTO> getAllRuleBookResultsTypeMappings(){
		List<RuleBookResultsTypeMappingDTO> retVal = selectedRuleBookImplementation.getRuleBookResultsTypeMappings();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("researcherRole");
		orderByList.add("entityType");
		orderByList.add("entitySourceType");
		directionsList.add("asc");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(retVal, new GenericComparator<RuleBookResultsTypeMappingDTO>(
				orderByList, directionsList));
		return retVal;
	}
	
	private List<RuleBookResultsTypeMappingDTO> deepCopyRuleBookResultsTypeMappings(RuleBookImplementationDTO  book, List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings){
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMappings-1");
		List<RuleBookResultsTypeMappingDTO> retVal = new ArrayList<RuleBookResultsTypeMappingDTO>();
		for (int i = 0; i < ruleBookResultsTypeMappings.size(); i++) {
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMappings-2");
			RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping = ruleBookResultsTypeMappings.get(i);
			RuleBookResultsTypeMappingDTO newRuleBookResultsTypeMapping = new RuleBookResultsTypeMappingDTO(null, book, ruleBookResultsTypeMapping.getResearcherRole(), ruleBookResultsTypeMapping.getEntityType(), ruleBookResultsTypeMapping.getEntitySourceType(), 
					ruleBookResultsTypeMapping.getResultsTypeForResearcherRole(), ruleBookResultsTypeMapping.getResultsTypeOfObsEntity(), ruleBookResultsTypeMapping.getResultsTypeOfObsEntitySource());
//			System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMappings-3");
			retVal.add(newRuleBookResultsTypeMapping);
		}
//		System.out.println("RuleBookImplementationManagedBean-deepCopyRuleBookResultsTypeMappings-4");
		
//		for (RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping : retVal) {
//			System.out.println(ruleBookResultsTypeMapping.toString());
//		}
		return retVal;
	}
	
	private boolean addAllRuleBookResultsTypeMappings(List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings) {
		
		RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
		for (int i = 0; i < ruleBookResultsTypeMappings.size(); i++) {
			if(dao.add(ruleBookResultsTypeMappings.get(i))==false){
				return false;
			}
		}
		return true;
	}
	//--------------------------------------------------------------------RULEBOOK RESULTS TYPE MAPPING END
}
