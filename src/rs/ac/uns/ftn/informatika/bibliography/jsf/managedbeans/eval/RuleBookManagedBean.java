package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.EntityResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.EntityTypesDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResearcherRoleDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.ResultsTypeMeasureDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookEntityTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeMappingDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.EntityResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookEntityTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookImplementationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.RuleBookResultsTypeMappingDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SciencesGroupDTO;
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
public class RuleBookManagedBean extends CRUDManagedBean implements IPickResultsTypeGroupManagedBean{
	private List<RuleBookDTO> list;
	private RuleBookDTO selectedRuleBook = null;
	private boolean includeRuleBookImplementations = false;
	
	List<RuleBookDTO> similarRuleBooks = null;
	
	private List<TreeNodeDTO<RuleBookDTO>> rootNodes = null;
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickRuleBookManagedBean iPickRuleBookManagedBean = null;
	private String pickMessage;
	
	private RuleBookImplementationManagedBean ruleBookImplementationManagedBean = null;
	
	private List<ResultsTypeDTO> allResultsTypes = null;
	private List<SciencesGroupDTO> allSciencesGroups = null;
	private String selectedResultsTypeClassId = null;
	private String selectedSciencesGroupClassId = null;
	private String selectedQuantitativeMeasure = null;
	private boolean resultsTypeMeasureAddMode = false;
	
	
	private List<ClassDTO> allEntityTypes = null;
	private String selectedEntityTypeClassId = null;
	private boolean ruleBookEntityTypeAddMode = false;
	
	private String selectedRuleBookEntityTypeClassId = null;
	private String selectedRuleBookResultsTypeClassId = null;
	private boolean entityResultsTypeAddMode = false;
	
	
	
	private List<ClassDTO> allResearcherRoles = null;
	private String selectedResearcherRoleCompariableId = null;
	private String selectedEntityTypeCompariableId = null;
	private String selectedEntitySourceTypeCompariableId = null;
	private String selectedResultsTypeForResearcherRoleCompariableId = null;
	private String selectedResultsTypeOfObsEntityCompariableId = null;
	private String selectedResultsTypeOfObsEntitySourceCompariableId = null;
	private boolean ruleBookResultsTypeMappingAddMode = false;
	
	/**
	 * 
	 */
	public RuleBookManagedBean() {
		super();
		pickSimilarMessage = "records.ruleBook.pickSimilarMessage";
		tableModalPanel = "ruleBookBrowseModalPanel";
		editModalPanel = "ruleBookEditModalPanel";
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		allSciencesGroups = new ArrayList<SciencesGroupDTO>();
		selectedResultsTypeClassId = null;
		selectedSciencesGroupClassId = null;
		selectedQuantitativeMeasure = null;
		resultsTypeMeasureAddMode = false;
		allEntityTypes = new ArrayList<ClassDTO>();
		selectedEntityTypeClassId = null;
		ruleBookEntityTypeAddMode = false;
		selectedRuleBookEntityTypeClassId = null;
		selectedRuleBookResultsTypeClassId = null;
		entityResultsTypeAddMode = false;
		allResearcherRoles = new ArrayList<ClassDTO>();
		selectedResearcherRoleCompariableId = null;
		selectedEntityTypeCompariableId = null;
		selectedEntitySourceTypeCompariableId = null;
		selectedResultsTypeForResearcherRoleCompariableId = null;
		selectedResultsTypeOfObsEntityCompariableId = null;
		selectedResultsTypeOfObsEntitySourceCompariableId = null;
		ruleBookResultsTypeMappingAddMode = false;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedRuleBook = null;
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		allSciencesGroups = new ArrayList<SciencesGroupDTO>();
		selectedResultsTypeClassId = null;
		selectedSciencesGroupClassId = null;
		selectedQuantitativeMeasure = null;
		resultsTypeMeasureAddMode = false;
		allEntityTypes = new ArrayList<ClassDTO>();
		selectedEntityTypeClassId = null;
		ruleBookEntityTypeAddMode = false;
		selectedRuleBookEntityTypeClassId = null;
		selectedRuleBookResultsTypeClassId = null;
		entityResultsTypeAddMode = false;
		allResearcherRoles = new ArrayList<ClassDTO>();
		selectedResearcherRoleCompariableId = null;
		selectedEntityTypeCompariableId = null;
		selectedEntitySourceTypeCompariableId = null;
		selectedResultsTypeForResearcherRoleCompariableId = null;
		selectedResultsTypeOfObsEntityCompariableId = null;
		selectedResultsTypeOfObsEntitySourceCompariableId = null;
		ruleBookResultsTypeMappingAddMode = false;
		
		includeRuleBookImplementations = true;
		if(list != null)
			populateList = false;
		tableModalPanel = "ruleBookBrowseModalPanel";
		editModalPanel = "ruleBookEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<RuleBookDTO> listTmp = getRuleBooks(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));

			list = listTmp;
			if(includeRuleBookImplementations)
				list.addAll(getRuleBookImplementationManagedBean().getRuleBookImplementations());
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<RuleBookDTO>();
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
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK)), Occur.MUST);
		return bq;
	}
	
	/**
	 * Retrieves a list of ruleBooks that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving ruleBooks
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of ruleBooks that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<RuleBookDTO> getRuleBooks(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getRuleBooks(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of ruleBooks that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving ruleBooks
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of ruleBooks that correspond to the query
	 */
	private List<RuleBookDTO> getRuleBooks(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<RuleBookDTO> retVal = new ArrayList<RuleBookDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector); 
		
		for (Record record : list) {
			try {
				RuleBookDTO dto = (RuleBookDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<RuleBookDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
	/**
	 * @return the list of ruleBooks (filtered and ordered by ...)
	 */
	public List<RuleBookDTO> getRuleBooks() {
		if((populateList) || ((includeRuleBookImplementations)&&(getRuleBookImplementationManagedBean().isPopulateList()))){
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
			Collections.sort(list, new GenericComparator<RuleBookDTO>(
					orderByList, directionsList));
		}
		return list;
	}
	
	/**
	 * @return the ruleBook (filtered and ordered by ...)
	 */
	public RuleBookDTO getRuleBookByRuleBookUserCode(String ruleBookUserCode) {
		RuleBookDTO retVal = null;
		
		if(ruleBookUserCode!= null && !"".equals(ruleBookUserCode)){
			BooleanQuery bq = new BooleanQuery();
	        bq.add(new TermQuery(new Term("LN", ruleBookUserCode)), Occur.MUST);
	        bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK)), Occur.MUST);
			
			List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
			if(listRecord.size()>0){
				try {
					RuleBookDTO dto = (RuleBookDTO) listRecord.get(0).getDto();
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
	
	public List<RuleBookDTO> autocompleteUserCode(String suggest) {
		List<RuleBookDTO> retVal = new ArrayList<RuleBookDTO>();
		
		String ruleBookUserCode = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(ruleBookUserCode != null && !"".equals(ruleBookUserCode)){
			bq.add(QueryUtils.makeBooleanQuery("LN", ruleBookUserCode + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("LN", ruleBookUserCode + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				RuleBookDTO dto = (RuleBookDTO) recordDTO.getDto();
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
	
	public List<RuleBookDTO> autocompleteName(String suggest) {
		List<RuleBookDTO> retVal = new ArrayList<RuleBookDTO>();
		
		String ruleBookName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(ruleBookName != null && !"".equals(ruleBookName)){
			bq.add(QueryUtils.makeBooleanQuery("NA", ruleBookName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", ruleBookName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				RuleBookDTO dto = (RuleBookDTO) recordDTO.getDto();
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
	 * Creates query for finding SIMILAR ruleBooks with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarRuleBooksQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedRuleBook!=null){
			if(selectedRuleBook.getName().getContent()!=null){
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedRuleBook.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedRuleBook.getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
			else if(selectedRuleBook.getNameTranslations().size() > 0){
				for (MultilingualContentDTO iterable_element : selectedRuleBook.getNameTranslations()) {
					BooleanQuery bqTr = new BooleanQuery();
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
					bq.add(bqTr, Occur.SHOULD);
				}
			}
			bq.add(new TermQuery(new Term("TYPE", Types.RULEBOOK)), Occur.MUST);
		}
		return bq;
	}
	
	/**
	 * @return the selectedRuleBook
	 */
	public RuleBookDTO getSelectedRuleBook() {
		return selectedRuleBook;
	}

	/**
	 * @param selectedRuleBook the selectedRuleBook to set
	 */
	public void setSelectedRuleBook(RuleBookDTO selectedRuleBook) {
		this.selectedRuleBook = selectedRuleBook;
	}

	/**
	 * @return the includeRuleBookImplementations
	 */
	public boolean isIncludeRuleBookImplementations() {
		return includeRuleBookImplementations;
	}

	/**
	 * @param includeRuleBookImplementations the includeRuleBookImplementations to set
	 */
	public void setIncludeRuleBookImplementations(
			boolean includeRuleBookImplementations) {
		this.includeRuleBookImplementations = includeRuleBookImplementations;
	}

	/**
	 * @return the list of SIMILAR ruleBooks with selected ruleBook
	 */
	public List<RuleBookDTO> getSimilarRuleBooks() {
		return similarRuleBooks;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedRuleBook = findRuleBookTypeByControlNumber(list);
		if (selectedRuleBook != null) {
			super.switchToUpdateMode();
			
			selectedResultsTypeClassId = null;
			selectedSciencesGroupClassId = null;
			selectedQuantitativeMeasure = null;
			debug("switchToUpdateMode: \n" + selectedRuleBook);
		}
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedRuleBook = new RuleBookDTO();	
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchRuleBookImplementationToAddMode() {
		selectedRuleBook = findRuleBookTypeByControlNumber(list);
		if(selectedRuleBook != null)
			getRuleBookImplementationManagedBean().switchToAddModefFromRuleBook(selectedRuleBook);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchResultsTypeMeasureToAddMode() {
		resultsTypeMeasureAddMode = true;
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		allSciencesGroups = new ArrayList<SciencesGroupDTO>();
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		
		if(selectedRuleBook != null && selectedRuleBook.getResultsTypeGroup().getClassId()!=null){
			//citanje svih tipovan nuacnih rezultata iz grupe
			ResultsTypeGroupManagedBean mbRt = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
					"resultsTypeGroupManagedBean");
			if (mbRt == null) {
				mbRt = new ResultsTypeGroupManagedBean();
				extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mbRt);
			}
			ResultsTypeGroupDTO temp = selectedRuleBook.getResultsTypeGroup();
			mbRt.setSelectedResultsTypeGroup(temp);
			mbRt.loadSelectedResultsTypeGroup(true);
			allResultsTypes = selectedRuleBook.getResultsTypeGroup().getResultsTypes();
		}
		
		//citanje svih naucnih grupa
		SciencesGroupManagedBean mbSG = (SciencesGroupManagedBean) extCtx.getSessionMap().get(
				"sciencesGroupManagedBean");
		if (mbSG == null) {
			mbSG = new SciencesGroupManagedBean();
			extCtx.getSessionMap().put("sciencesGroupManagedBean", mbSG);
		}
		allSciencesGroups = mbSG.getSciencesGroups();
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchRuleBookEntityTypeToAddMode() {
		ruleBookEntityTypeAddMode = true;
		selectedEntityTypeClassId = null;
		if(allEntityTypes.isEmpty()){
			EntityTypesDAO etDAO = new EntityTypesDAO();
			List<ClassDTO> tempListaSaDjubretom = etDAO.getAllEntityTypes();
			for (ClassDTO classDTO : tempListaSaDjubretom) {
				if(!classDTO.getAllTerms().isEmpty())
					allEntityTypes.add(classDTO);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchEntityResultsTypeToAddMode() {
		entityResultsTypeAddMode = true;
		selectedRuleBookEntityTypeClassId = null;
		selectedRuleBookResultsTypeClassId = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchRuleBookResultsTypeMappingToAddMode() {
		ruleBookResultsTypeMappingAddMode = true;
		selectedResearcherRoleCompariableId = null;
		selectedEntityTypeCompariableId = null;
		selectedEntitySourceTypeCompariableId = null;
		selectedResultsTypeForResearcherRoleCompariableId = null;
		selectedResultsTypeOfObsEntityCompariableId = null;
		selectedResultsTypeOfObsEntitySourceCompariableId = null;
		if(allResearcherRoles.isEmpty()){
			ResearcherRoleDAO rrDAO = new ResearcherRoleDAO();
			List<ClassDTO> tempListaSaDjubretom = rrDAO.getAllResearcherRole();
			for (ClassDTO classDTO : tempListaSaDjubretom) {
				if(!classDTO.getAllTerms().isEmpty())
					allResearcherRoles.add(classDTO);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedRuleBook = findRuleBookTypeByControlNumber(list);
		if (selectedRuleBook != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedRuleBook);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickRuleBookManagedBean != null) {
			iPickRuleBookManagedBean.cancelPickingRuleBook();
		}
		super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchResultsTypeMeasureToNoneMode() {
		resultsTypeMeasureAddMode = false;
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		allSciencesGroups = new ArrayList<SciencesGroupDTO>();
		selectedResultsTypeClassId = null;
		selectedSciencesGroupClassId = null;
		selectedQuantitativeMeasure = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchRuleBookEntityTypeToNoneMode() {
		ruleBookEntityTypeAddMode = false;
		selectedEntityTypeClassId = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchEntityResultsTypeToNoneMode() {
		entityResultsTypeAddMode = false;
		selectedRuleBookEntityTypeClassId = null;
		selectedRuleBookResultsTypeClassId = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchRuleBookResultsTypeMappingToNoneMode() {
		ruleBookResultsTypeMappingAddMode = false;
		selectedResearcherRoleCompariableId = null;
		selectedEntityTypeCompariableId = null;
		selectedEntitySourceTypeCompariableId = null;
		selectedResultsTypeForResearcherRoleCompariableId = null;
		selectedResultsTypeOfObsEntityCompariableId = null;
		selectedResultsTypeOfObsEntitySourceCompariableId = null;
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
		if (selectedRuleBook == null)
			return;
//		System.out.println("Rulebook-update-1");
		
		RuleBookDTO oldRuleBook = (RuleBookDTO) recordDAO.getDTO(selectedRuleBook.getControlNumber());
		oldRuleBook.getName();
		
		if(!oldRuleBook.getResultsTypeGroup().equals(selectedRuleBook.getResultsTypeGroup())){
//			System.out.println("Rulebook-update-2");
			if(removeAllRuleBookResultsTypesFromDatabase()==false){
//				System.out.println("Rulebook-update-2-Error");
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorRuleBookResultsTypes", FacesContext
								.getCurrentInstance());
				return;
			}
			selectedRuleBook.getRuleBookResultsTypes().clear();
			
			//ubaci nove rulebook result types od grupe
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ResultsTypeGroupManagedBean mb = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
					"resultsTypeGroupManagedBean");
			if (mb == null) {
				mb = new ResultsTypeGroupManagedBean();
				extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mb);
			}
			if(selectedRuleBook.getResultsTypeGroup()!=null){
				mb.setSelectedResultsTypeGroup(selectedRuleBook.getResultsTypeGroup());
				mb.loadSelectedResultsTypeGroup(true);
			}
			
			ResultsTypeGroupDTO rtg = selectedRuleBook.getResultsTypeGroup();
			for (ResultsTypeDTO resultsType : rtg.getResultsTypes()) {
				RuleBookResultsTypeDTO ruleBookResultsTypeDTO = new RuleBookResultsTypeDTO(selectedRuleBook, resultsType);
				ruleBookResultsTypeDTO.setNotLoadedResultsTypeMeasures(false); 
				selectedRuleBook.getRuleBookResultsTypes().add(ruleBookResultsTypeDTO);
				
				List<String> orderByList = new ArrayList<String>();
				List<String> directionsList = new ArrayList<String>();
				
				orderByList.add("resultsType");
				directionsList.add("asc");
				Collections.sort(selectedRuleBook.getRuleBookResultsTypes(), new GenericComparator<RuleBookResultsTypeDTO>(
						orderByList, directionsList));
			}
			
			if(addAllRuleBookResultsTypesToDatabase()==false){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorRuleBookResultsTypes", FacesContext
								.getCurrentInstance());
				return;
			}
			
			selectedRuleBook.setNotLoadedRuleBookResultsTypesDependentTables();
			
//			System.out.println("Rulebook-update-3");
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
//					System.out.println("Rulebook-update-3.1");
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.changeResultsTypeGroup(rbi, selectedRuleBook)==false){
//						System.out.println("Rulebook-update-3.1-Error");
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorRuleBookResultsTypesForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
		}
//		System.out.println("Rulebook-update-4");
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RULEBOOK, 
				selectedRuleBook)) == false) {
//			System.out.println("Rulebook-update-4-Error");
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.ruleBook.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedRuleBook);
			sendRecordMessage(selectedRuleBook, "update");
		}
	}

	@Override
	public void add() {
//		System.out.println("RuleBookMB method add 1");
//		System.out.println("RuleBookMB method add 1 - DTO.getLocale " + selectedRuleBook.getLocale());
//		System.out.println("RuleBookMB method add 1 - DTO.toString " + selectedRuleBook.toString());
//		System.out.println("RuleBookMB method add 1 - DTO.toString " + selectedRuleBook.getHTMLRepresentation());
		
		selectedRuleBook.getLocale();
		selectedRuleBook.toString();
		selectedRuleBook.getHTMLRepresentation();
		
		similarRuleBooks = null;
		if(selectedRuleBook.getRuleBookUserCode()== null || "".equals(selectedRuleBook.getRuleBookUserCode().trim())){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.errorRuleBookUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookMB method add 2");
		if(getRuleBookByRuleBookUserCode(selectedRuleBook.getRuleBookUserCode()) != null){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.errorSameRuleBookUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookMB method add 3");
		if(selectedRuleBook.getResultsTypeGroup() == null || selectedRuleBook.getResultsTypeGroup().getClassId()==null){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.errorResultsTypeGroup", FacesContext
							.getCurrentInstance());
			return;
		}
//		System.out.println("RuleBookMB method add 4");
		if(editTabNumber == 0){
			try {
				debug("findSimilarRuleBooks");
				similarRuleBooks = getRuleBooks(createSimilarRuleBooksQuery(),
						null, null, new AllDocCollector(true));
			} catch (ParseException e) {
				error("findSimilarRuleBooks", e);
			}
		}
//		System.out.println("RuleBookMB method add 5");
		if((similarRuleBooks == null ) || (similarRuleBooks.size()==0)){
//			System.out.println("RuleBookMB method add 6");
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RULEBOOK, 
					selectedRuleBook)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.error", FacesContext
								.getCurrentInstance());
			} else {
//				System.out.println("RuleBookMB method add 7");
				//ubaci nove rulebook result types od grupe
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				ExternalContext extCtx = facesCtx.getExternalContext();
				ResultsTypeGroupManagedBean mb = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
						"resultsTypeGroupManagedBean");
				if (mb == null) {
					mb = new ResultsTypeGroupManagedBean();
					extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mb);
				}
				if(selectedRuleBook.getResultsTypeGroup()!=null){
					mb.setSelectedResultsTypeGroup(selectedRuleBook.getResultsTypeGroup());
					mb.loadSelectedResultsTypeGroup(true);
				}
//				System.out.println("RuleBookMB method add 8");
				ResultsTypeGroupDTO rtg = selectedRuleBook.getResultsTypeGroup();
				for (ResultsTypeDTO resultsType : rtg.getResultsTypes()) {
					RuleBookResultsTypeDTO ruleBookResultsTypeDTO = new RuleBookResultsTypeDTO(selectedRuleBook, resultsType);
					ruleBookResultsTypeDTO.setNotLoadedResultsTypeMeasures(false); 
					selectedRuleBook.getRuleBookResultsTypes().add(ruleBookResultsTypeDTO);
					
					List<String> orderByList = new ArrayList<String>();
					List<String> directionsList = new ArrayList<String>();
					
					orderByList.add("resultsType");
					directionsList.add("asc");
					Collections.sort(selectedRuleBook.getRuleBookResultsTypes(), new GenericComparator<RuleBookResultsTypeDTO>(
							orderByList, directionsList));
				}
				
				if(addAllRuleBookResultsTypesToDatabase()==false){
					facesMessages.addToControlFromResourceBundle(
							"ruleBookEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.ruleBook.add.errorRuleBookResultsTypes", FacesContext
									.getCurrentInstance());
					return;
				}
						
//				System.out.println("RuleBookMB method add 9");
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.ruleBook.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedRuleBook);
				newRecordEmailNotification(selectedRuleBook, facesMessages.getMessageFromResourceBundle("records.ruleBook.newRuleBookNotification.subject"));
//				System.out.println("RuleBookMB method add 10");
			}
		} else {
//			System.out.println("RuleBookMB method add 11");
			nextEditTab();
		}
//		System.out.println("RuleBookMB method add 12");
	}
	
	@Override
	public void delete() {
		selectedRuleBook = findRuleBookTypeByControlNumber(list);
		if (selectedRuleBook == null)
			return;
		
//		System.out.println("Rulebook-delete()-1");
		//PRVO TREBA DA OSE OBRISU SVI RULEBOOKIMPLEMENTATION PA ONDA RULEBOOK
		for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
			delete(el);
		}
		
//		System.out.println("Rulebook-delete()-2");
		if (recordDAO.delete(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(100), CerifEntitiesNames.RULEBOOK, 
				selectedRuleBook)) == false) {
//			System.out.println("Rulebook-delete()-2-Error");
			facesMessages.addToControlFromResourceBundle(
					"ruleBookTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.delete.error", FacesContext
							.getCurrentInstance());
		} else {
//			System.out.println("Rulebook-delete()-3");
			debug("deleted: \n" + selectedRuleBook);
			selectedRuleBook = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void delete(RuleBookDTO book) {
//		System.out.println("Rulebook-delete(book)-1");
		if(book instanceof RuleBookImplementationDTO){
//			System.out.println("Rulebook-delete(book)-2");
			RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
			rbiMB.delete((RuleBookImplementationDTO)book);
//			System.out.println("Rulebook-delete(book)-3");
		}
		else if(book instanceof RuleBookDTO){
//			System.out.println("Rulebook-delete(book)-4");
			for(RuleBookDTO el:getAllSubElements(book)){
//				System.out.println("Rulebook-delete(book)-4");
				delete(el);
//				System.out.println("Rulebook-delete(book)-4.1");
			}
//			System.out.println("Rulebook-delete(book)-5");
			if (recordDAO.delete(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RULEBOOK, 
					book)) == false) {
//				System.out.println("Rulebook-delete(book)-5-Error");
				facesMessages.addToControlFromResourceBundle(
						"ruleBookTableForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.error", FacesContext
								.getCurrentInstance());
			} else {
//				System.out.println("Rulebook-delete(book)-6");
				debug("deleted: \n" + book);
				if(selectedRuleBook == book)
					selectedRuleBook = null;
				populateList = true;
				orderList = true;
			}
		}
	}
	
	/**
	 * Sets chosen ruleBook to the CRUDManagedBean which wanted to pick
	 * ruleBook
	 */
	public void chooseRuleBook() {

		try {
			selectedRuleBook = findRuleBookTypeByControlNumber(list);
			if (selectedRuleBook != null && iPickRuleBookManagedBean != null) {
				iPickRuleBookManagedBean.setRuleBook(selectedRuleBook);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBook", e);
		}
	}

	/**
	 * Sets chosen ruleBook to the CRUDManagedBean which wanted to pick
	 * ruleBook
	 */
	public void chooseSimilar() {

		try {
			selectedRuleBook = findRuleBookTypeByControlNumber(similarRuleBooks);
			if (selectedRuleBook != null && iPickRuleBookManagedBean!=null) {
				iPickRuleBookManagedBean.setRuleBook(selectedRuleBook);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
			editTabNumber = 0;
			setEditMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBook", e);
		}
	}
	
	/**
	 * @param iPickRuleBookManagedBean the iPickRuleBookManagedBean to set
	 */
	public void setiPickRuleBookManagedBean(
			IPickRuleBookManagedBean iPickRuleBookManagedBean) {
		this.iPickRuleBookManagedBean = iPickRuleBookManagedBean;
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
			if(iPickRuleBookManagedBean!=null)
				iPickRuleBookManagedBean.setRuleBook(selectedRuleBook);
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
		retVal = "ruleBookPage";
		return retVal;
	}

	private RuleBookDTO findRuleBookTypeByControlNumber(List<RuleBookDTO> ruleBooksList) {
		RuleBookDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (RuleBookDTO dto : ruleBooksList) {
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
		this.openMultilingualContentForm(editMode, selectedRuleBook.getNameTranslations(), false, "records.ruleBook.editPanel.nameTranslations.panelHeader", "records.ruleBook.editPanel.nameTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedRuleBook.getDescriptionTranslations(), false, "records.ruleBook.editPanel.descriptionTranslations.panelHeader", "records.ruleBook.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedRuleBook.getKeywordsTranslations(), false, "records.ruleBook.editPanel.keywordsTranslations.panelHeader", "records.ruleBook.editPanel.keywordsTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		if(selectedRuleBook.getStartDate()!=null)
			return selectedRuleBook.getStartDate().getTime();
		else 
			return null;
	}
	
	public void setStartDate(Date startDate){
		if(startDate!=null){
			if(selectedRuleBook.getStartDate()==null){
				Calendar stC = new GregorianCalendar();
				selectedRuleBook.setStartDate(stC);
			}
			selectedRuleBook.getStartDate().setTime(startDate);
		}
	}
	
	public Date getEndDate(){
		if(selectedRuleBook.getEndDate()!=null)
			return selectedRuleBook.getEndDate().getTime();
		else 
			return null;
	}
	
	public void setEndDate(Date endDate){
		if(endDate!=null){
			if(selectedRuleBook.getEndDate()==null){
				Calendar enC = new GregorianCalendar();
				selectedRuleBook.setEndDate(enC);
			}
			selectedRuleBook.getEndDate().setTime(endDate);
		}
	}
	
	/**
	 * @return the allResultsTypes
	 */
	public List<ResultsTypeDTO> getAllResultsTypes() {
		return allResultsTypes;
	}

	/**
	 * @return the allSciencesGroups
	 */
	public List<SciencesGroupDTO> getAllSciencesGroups() {
		return allSciencesGroups;
	}
	
	/**
	 * @return the selectedResultsTypeClassId
	 */
	public String getSelectedResultsTypeClassId() {
		return selectedResultsTypeClassId;
	}

	/**
	 * @param selectedResultsTypeClassId the selectedResultsTypeClassId to set
	 */
	public void setSelectedResultsTypeClassId(String selectedResultsTypeClassId) {
		this.selectedResultsTypeClassId = selectedResultsTypeClassId;
	}

	/**
	 * @return the selectedSciencesGroupClassId
	 */
	public String getSelectedSciencesGroupClassId() {
		return selectedSciencesGroupClassId;
	}

	/**
	 * @param selectedSciencesGroupClassId the selectedSciencesGroupClassId to set
	 */
	public void setSelectedSciencesGroupClassId(String selectedSciencesGroupClassId) {
		this.selectedSciencesGroupClassId = selectedSciencesGroupClassId;
	}

	/**
	 * @return the selectedQuantitativeMeasure
	 */
	public String getSelectedQuantitativeMeasure() {
		return selectedQuantitativeMeasure;
	}

	/**
	 * @param selectedQuantitativeMeasure the selectedQuantitativeMeasure to set
	 */
	public void setSelectedQuantitativeMeasure(String selectedQuantitativeMeasure) {
		this.selectedQuantitativeMeasure = selectedQuantitativeMeasure;
	}

	/**
	 * @return the resultsTypeMeasureAddMode
	 */
	public boolean isResultsTypeMeasureAddMode() {
		return resultsTypeMeasureAddMode;
	}

	/**
	 * @param resultsTypeMeasureAddMode the resultsTypeMeasureAddMode to set
	 */
	public void setResultsTypeMeasureAddMode(boolean resultsTypeMeasureAddMode) {
		this.resultsTypeMeasureAddMode = resultsTypeMeasureAddMode;
	}
	
	/**
	 * @return the selectedEntityTypeClassId
	 */
	public String getSelectedEntityTypeClassId() {
		return selectedEntityTypeClassId;
	}

	/**
	 * @param selectedEntityTypeClassId the selectedEntityTypeClassId to set
	 */
	public void setSelectedEntityTypeClassId(String selectedEntityTypeClassId) {
		this.selectedEntityTypeClassId = selectedEntityTypeClassId;
	}

	/**
	 * @return the ruleBookEntityTypeAddMode
	 */
	public boolean isRuleBookEntityTypeAddMode() {
		return ruleBookEntityTypeAddMode;
	}
	
	/**
	 * @return the selectedRuleBookEntityTypeClassId
	 */
	public String getSelectedRuleBookEntityTypeClassId() {
		return selectedRuleBookEntityTypeClassId;
	}

	/**
	 * @param selectedRuleBookEntityTypeClassId the selectedRuleBookEntityTypeClassId to set
	 */
	public void setSelectedRuleBookEntityTypeClassId(
			String selectedRuleBookEntityTypeClassId) {
		this.selectedRuleBookEntityTypeClassId = selectedRuleBookEntityTypeClassId;
	}

	/**
	 * @return the selectedRuleBookResultsTypeClassId
	 */
	public String getSelectedRuleBookResultsTypeClassId() {
		return selectedRuleBookResultsTypeClassId;
	}

	/**
	 * @param selectedRuleBookResultsTypeClassId the selectedRuleBookResultsTypeClassId to set
	 */
	public void setSelectedRuleBookResultsTypeClassId(
			String selectedRuleBookResultsTypeClassId) {
		this.selectedRuleBookResultsTypeClassId = selectedRuleBookResultsTypeClassId;
	}

	/**
	 * @return the entityResultsTypeAddMode
	 */
	public boolean isEntityResultsTypeAddMode() {
		return entityResultsTypeAddMode;
	}

	/**
	 * @return the allEntityTypes
	 */
	public List<ClassDTO> getAllEntityTypes() {
		return allEntityTypes;
	}

	/**
	 * @return the selectedResearcherRoleCompariableId
	 */
	public String getSelectedResearcherRoleCompariableId() {
		return selectedResearcherRoleCompariableId;
	}

	/**
	 * @param selectedResearcherRoleCompariableId the selectedResearcherRoleCompariableId to set
	 */
	public void setSelectedResearcherRoleCompariableId(
			String selectedResearcherRoleCompariableId) {
		this.selectedResearcherRoleCompariableId = selectedResearcherRoleCompariableId;
	}

	/**
	 * @return the selectedEntityTypeCompariableId
	 */
	public String getSelectedEntityTypeCompariableId() {
		return selectedEntityTypeCompariableId;
	}

	/**
	 * @param selectedEntityTypeCompariableId the selectedEntityTypeCompariableId to set
	 */
	public void setSelectedEntityTypeCompariableId(
			String selectedEntityTypeCompariableId) {
		this.selectedEntityTypeCompariableId = selectedEntityTypeCompariableId;
	}

	/**
	 * @return the selectedEntitySourceTypeCompariableId
	 */
	public String getSelectedEntitySourceTypeCompariableId() {
		return selectedEntitySourceTypeCompariableId;
	}

	/**
	 * @param selectedEntitySourceTypeCompariableId the selectedEntitySourceTypeCompariableId to set
	 */
	public void setSelectedEntitySourceTypeCompariableId(
			String selectedEntitySourceTypeCompariableId) {
		this.selectedEntitySourceTypeCompariableId = selectedEntitySourceTypeCompariableId;
	}

	/**
	 * @return the selectedResultsTypeForResearcherRoleCompariableId
	 */
	public String getSelectedResultsTypeForResearcherRoleCompariableId() {
		return selectedResultsTypeForResearcherRoleCompariableId;
	}

	/**
	 * @param selectedResultsTypeForResearcherRoleCompariableId the selectedResultsTypeForResearcherRoleCompariableId to set
	 */
	public void setSelectedResultsTypeForResearcherRoleCompariableId(
			String selectedResultsTypeForResearcherRoleCompariableId) {
		this.selectedResultsTypeForResearcherRoleCompariableId = selectedResultsTypeForResearcherRoleCompariableId;
	}

	/**
	 * @return the selectedResultsTypeOfObsEntityCompariableId
	 */
	public String getSelectedResultsTypeOfObsEntityCompariableId() {
		return selectedResultsTypeOfObsEntityCompariableId;
	}

	/**
	 * @param selectedResultsTypeOfObsEntityCompariableId the selectedResultsTypeOfObsEntityCompariableId to set
	 */
	public void setSelectedResultsTypeOfObsEntityCompariableId(
			String selectedResultsTypeOfObsEntityCompariableId) {
		this.selectedResultsTypeOfObsEntityCompariableId = selectedResultsTypeOfObsEntityCompariableId;
	}

	/**
	 * @return the selectedResultsTypeOfObsEntitySourceCompariableId
	 */
	public String getSelectedResultsTypeOfObsEntitySourceCompariableId() {
		return selectedResultsTypeOfObsEntitySourceCompariableId;
	}

	/**
	 * @param selectedResultsTypeOfObsEntitySourceCompariableId the selectedResultsTypeOfObsEntitySourceCompariableId to set
	 */
	public void setSelectedResultsTypeOfObsEntitySourceCompariableId(
			String selectedResultsTypeOfObsEntitySourceCompariableId) {
		this.selectedResultsTypeOfObsEntitySourceCompariableId = selectedResultsTypeOfObsEntitySourceCompariableId;
	}

	/**
	 * @return the allResearcherRoles
	 */
	public List<ClassDTO> getAllResearcherRoles() {
		return allResearcherRoles;
	}

	/**
	 * @return the ruleBookResultsTypeMappingAddMode
	 */
	public boolean isRuleBookResultsTypeMappingAddMode() {
		return ruleBookResultsTypeMappingAddMode;
	}

	public Boolean isOpened(RuleBookDTO parent){
		RuleBookDTO childRuleBook = selectedRuleBook;
		boolean result = isChild(childRuleBook, parent);
    	if (result)
    		return true;
    	else if (includeRuleBookImplementations){
			RuleBookImplementationDTO childRuleBookImplementation = getRuleBookImplementationManagedBean().getSelectedRuleBookImplementation();
    		return isChild(childRuleBookImplementation, parent);
    	}
    	
    	return false;
	}
	
	private boolean isChild(RuleBookDTO child, RuleBookDTO parent){
		try{
			if(child instanceof RuleBookImplementationDTO){
				RuleBookImplementationDTO childRuleBookImplementation = (RuleBookImplementationDTO) child;
				if(childRuleBookImplementation.getSuperRuleBook() != null )
					if(childRuleBookImplementation.getSuperRuleBook().getControlNumber().equals(parent.getControlNumber())){
						return true;
					}
				if(childRuleBookImplementation.getSuperRuleBookImplementation() != null )
					if(childRuleBookImplementation.getSuperRuleBookImplementation().getControlNumber().equals(parent.getControlNumber())){
						return true;
					}
				
				if((childRuleBookImplementation.getSuperRuleBook()!=null) && (childRuleBookImplementation.getSuperRuleBook().getControlNumber()!=null))
					return isChild(childRuleBookImplementation.getSuperRuleBook(), parent); //nedostupan kod jer RuleBook za sada nema decu tipa RuleBook
				else if((childRuleBookImplementation.getSuperRuleBookImplementation()!=null) && (childRuleBookImplementation.getSuperRuleBookImplementation().getControlNumber()!=null))
					return isChild(childRuleBookImplementation.getSuperRuleBookImplementation(), parent);
			}
			else if (child instanceof RuleBookDTO){
				// RuleBook za sada nema decu tipa RuleBook tj. RuleBookDTO nema roditelja tipa RuleBookDTO
			}
		
		}catch(Exception ex){
    	
		}
		return false;
	}
	
	public void getTree() {
		debug("getTree");
		try {
		    rootNodes = new ArrayList<TreeNodeDTO<RuleBookDTO>>();
		    List<RuleBookDTO> allRuleBooks = getRuleBooks(); //ponistava populate list ako treba
		      Collections.sort(allRuleBooks, new GenericComparator<RuleBookDTO>(
						"controlNumber", "asc"));
		      
		    for(RuleBookDTO rb:allRuleBooks){
		    	if(! (rb instanceof RuleBookImplementationDTO)){
		    		TreeNodeDTO<RuleBookDTO> node = new TreeNodeDTO<RuleBookDTO>(rb);
		    		addSubnodesToNode(node, rb);
					node.setExpanded(isOpened(rb));
					rootNodes.add(node);
		    	}
		    }		 
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	private void addSubnodesToNode(TreeNodeDTO<RuleBookDTO> parentNode, RuleBookDTO parentRuleBook) {
		for(RuleBookDTO el:getAllSubElements(parentRuleBook)){		
				TreeNodeDTO<RuleBookDTO> node = new TreeNodeDTO<RuleBookDTO>(el);
				addSubnodesToNode(node, el);
				node.setExpanded(isOpened(el));
				parentNode.addChild(node);
		}
	 }
	
	private List<RuleBookDTO> getAllSubElements(RuleBookDTO parentRuleBook) {
		List<RuleBookDTO> retVal = new ArrayList<RuleBookDTO>();
		if(parentRuleBook instanceof RuleBookImplementationDTO){
			for (RuleBookDTO rb : list) {
				if(rb instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO)rb;
					if(rbi.getSuperRuleBookImplementation() != null)
						if(parentRuleBook.getControlNumber().equals(rbi.getSuperRuleBookImplementation().getControlNumber())){
							retVal.add(rb);
						}
				}
				else if(rb instanceof RuleBookDTO){
					// RuleBookImplementation za sada nema decu tipa RuleBook tj. RuleBookImplementationDTO nema roditelja tipa RuleBookDTO
				}
			}
		} else if(parentRuleBook instanceof RuleBookDTO){
			for (RuleBookDTO rb : list) {
				if(rb instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO)rb;
					if(rbi.getSuperRuleBook() != null)
						if(parentRuleBook.getControlNumber().equals(rbi.getSuperRuleBook().getControlNumber())){
							retVal.add(rb);
						}
				}
				else if(rb instanceof RuleBookDTO){
					// RuleBook za sada nema decu tipa RuleBook tj. RuleBookDTO nema roditelja tipa RuleBookDTO
				}
			}
		}

		return retVal;
	}
	
	/**
	 * @return the rootNodes
	 */
	public TreeNode getRootNodes() {
		if((populateList) || ((includeRuleBookImplementations)&&(getRuleBookImplementationManagedBean().isPopulateList()))){
			getTree();
		}
		TreeNode retVal = new TreeNodeDTO<RuleBookDTO>(new RuleBookDTO());
		retVal.setExpanded(true);
		for (Object node:rootNodes
		) {
			TreeNodeDTO<OrganizationUnitDTO> nodeOrgUnit = (TreeNodeDTO<OrganizationUnitDTO>) node;
			nodeOrgUnit.setParent(retVal);
			retVal.getChildren().add(nodeOrgUnit);
		}
		return retVal;
	}
	
	/**
	 * @param rootNodes the rootNodes to set
	 */
	public void setRootNodes(List<TreeNodeDTO<RuleBookDTO>> rootNodes) {
		this.rootNodes = rootNodes;
	}
	
	/**
	 * @return the RuleBookImplementationManagedBean from current session
	 */
	protected RuleBookImplementationManagedBean getRuleBookImplementationManagedBean() {
		if (ruleBookImplementationManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			RuleBookImplementationManagedBean mb = (RuleBookImplementationManagedBean) extCtx.getSessionMap().get(
					"ruleBookImplementationManagedBean");
			if (mb == null) {
				mb = new RuleBookImplementationManagedBean();
				extCtx.getSessionMap().put("ruleBookImplementationManagedBean", mb);
			}
			ruleBookImplementationManagedBean = mb;
		}
		return ruleBookImplementationManagedBean;
	}

	/**
	 * @return the populateList
	 */
	public boolean isPopulateList(){
		return populateList;
	}
	
	@Override
	public void setResultsTypeGroup(ResultsTypeGroupDTO resultsTypeGroup) {
		if(!selectedRuleBook.getResultsTypeGroup().equals(resultsTypeGroup)){
			selectedRuleBook.setResultsTypeGroup(resultsTypeGroup);
		}
		
	}

	@Override
	public void cancelPickingResultsTypeGroup() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Prepares web form where user can choose Result Type Group for selected ruleBook
	 */
	public void pickResultsTypeGroup() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ResultsTypeGroupManagedBean mb = (ResultsTypeGroupManagedBean) extCtx.getSessionMap().get(
				"resultsTypeGroupManagedBean");
		if (mb == null) {
			mb = new ResultsTypeGroupManagedBean();
			extCtx.getSessionMap().put("resultsTypeGroupManagedBean", mb);
		}
		if(selectedRuleBook.getResultsTypeGroup()!=null){
			mb.setSelectedResultsTypeGroup(selectedRuleBook.getResultsTypeGroup());
			mb.loadSelectedResultsTypeGroup(true);
		}
		
		mb.setiPickResultsTypeGroupManagedBean(this);
		mb.setPickMessage("records.ruleBook.pickResultsTypeGroupMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();
	}
	
	public void uploadListener(FileUploadEvent event) {
        try{
	        FileDTO fileDTO = new FileDTO();
	        UploadedFile item = event.getFile();
//	        if (item. isTempFile()) {
//	        	 byte[] fileInBytes = new byte[(int)item.getFile().length()];
//	        	 java.io.File tempFile = item.getFile();
//	        	 FileInputStream fileInputStream = new FileInputStream(tempFile);
//	        	 fileInputStream.read(fileInBytes);
//	        	 fileInputStream.close();
//	        	 fileDTO.setData(fileInBytes);
//	        	 fileDTO.setLength(item.getFile().length());
//	        } else {
	        	 fileDTO.setData(item.getContent());
	        	 fileDTO.setLength(item.getContent().length);
//	        }
	        fileDTO.setFileName((item.getFileName().lastIndexOf("\\") != -1)?item.getFileName().substring(item.getFileName().lastIndexOf("\\")+1):item.getFileName());
	        fileDTO.setControlNumber(selectedRuleBook.getControlNumber());
	        fileDTO.setUploader(getUserManagedBean().getLoggedUser().getEmail());
	        selectedRuleBook.setFile(fileDTO);
        } catch (Exception e) {
        	selectedRuleBook.setFile(null);
        }
    }

	public void deleteFile(){
		if(selectedRuleBook.getFile().getControlNumber() != null)
			selectedRuleBook.setDeletedFile(selectedRuleBook.getFile());
		selectedRuleBook.setFile(null);
	}
	
	//--------------------------------------------------------------------RULE BOOK RESULT MEASURES START
	public boolean isVisibleRuleBookResultsTypeMeasures(){
		boolean retVal = true;
//		System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-1");
		if(editMode != modesManagedBean.getUpdate()){
			retVal = false;
//			System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-2");
		}
		else if(selectedRuleBook==null){
			retVal = false;
//			System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-3");
		}
		else if(selectedRuleBook!=null && selectedRuleBook.getControlNumber()== null){
			retVal = false;
//			System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-4");
		}
		else if( "".equals(selectedRuleBook.getControlNumber())){
			retVal = false;
//			System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-5");
		}
		else {
			RuleBookDTO oldRuleBook = (RuleBookDTO) recordDAO.getDTO(selectedRuleBook.getControlNumber());
			oldRuleBook.getName();
			if(!oldRuleBook.getResultsTypeGroup().equals(selectedRuleBook.getResultsTypeGroup())){
				retVal = false;
//				System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-6");
			}
		}
//		System.out.println("RuleBookManagedBean-isVisibleRuleBookResultsTypeMeasures-7:"+retVal);
		return retVal;
	}
	
	public boolean updateAllRuleBookResultsTypeMeasures() {
		
		if (selectedRuleBook == null){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.update.error", FacesContext
							.getCurrentInstance());
			return false;
		}
			
		
		if(removeAllRuleBookResultsTypeMeasuresFromDatabase()==false){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.delete.errorRuleBookResultsTypeMeasures", FacesContext
							.getCurrentInstance());
			return false;
		}
		
		if(addAllRuleBookResultsTypeMeasuresToDatabase()==false){
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.errorRuleBookResultsTypeMeasures", FacesContext
							.getCurrentInstance());
			return false;
		}
		
		for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
			if(el instanceof RuleBookImplementationDTO){
				RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
				RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
				if(rbiMB.updateAllRuleBookResultsTypeMeasures(rbi, selectedRuleBook.getRuleBookResultsTypes())==false){
					facesMessages.addToControlFromResourceBundle(
							"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
							"records.ruleBook.update.errorRuleBookResultsTypesForRuleBookSubelements", FacesContext
									.getCurrentInstance());
					return false;
				}
			}	
		}
		populateList = true;
		return true;
	}

	private boolean removeAllRuleBookResultsTypesFromDatabase() {

		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
//		System.out.println("RuleBook-removeAllRuleBookResultsTypesFromDatabase-1");
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = dao.getAllRuleBookResultsTypes(selectedRuleBook);
		for (RuleBookResultsTypeDTO rt : ruleBookResultsTypes) {
			rt.loadResultsTypeMeasuresFromDatabase();
		}
//		System.out.println("RuleBook-removeAllRuleBookResultsTypesFromDatabase-2");
		int upperBoundary = ruleBookResultsTypes.size()-1;
		for (int i = upperBoundary; i >= 0; i--) {
//			System.out.println("RuleBook-removeAllRuleBookResultsTypesFromDatabase-3");
			if(dao.deleteRuleBookResultsTypeAndDependacies(ruleBookResultsTypes.get(i))==false){
//				System.out.println("RuleBook-removeAllRuleBookResultsTypesFromDatabase-3-GRESKA");
				return false;
			}
			ruleBookResultsTypes.remove(i);
		}
		return true;
	}
	
	private boolean removeAllRuleBookResultsTypeMeasuresFromDatabase() {

		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
//		System.out.println("RuleBook-removeAllRuleBookResultsTypeMeasuresFromDatabase-1");
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = dao.getAllRuleBookResultsTypes(selectedRuleBook);
		for (RuleBookResultsTypeDTO rt : ruleBookResultsTypes) {
			rt.loadResultsTypeMeasuresFromDatabase();
		}
//		System.out.println("RuleBook-removeAllRuleBookResultsTypeMeasuresFromDatabase-2");
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
		}
		return true;
	}
	
	private boolean addAllRuleBookResultsTypesToDatabase() {
		
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = selectedRuleBook.getRuleBookResultsTypes();
		
		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
		for (int i = 0; i < ruleBookResultsTypes.size(); i++) {
			if(dao.addRuleBookResultsType(ruleBookResultsTypes.get(i))==false){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean addAllRuleBookResultsTypeMeasuresToDatabase() {
		ResultsTypeMeasureDAO daoMeasure = new ResultsTypeMeasureDAO();
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = selectedRuleBook.getRuleBookResultsTypes();
//		System.out.println("RuleBook-addAllRuleBookResultsTypeMeasuresToDatabase-1");
		for (int i = 0; i < ruleBookResultsTypes.size(); i++) {
			List<ResultsTypeMeasureDTO> rtmeasures = ruleBookResultsTypes.get(i).getResultsTypeMeasures();
//			System.out.println("RuleBook-addAllRuleBookResultsTypeMeasuresToDatabase-2");
			for (ResultsTypeMeasureDTO resultsTypeMeasureDTO : rtmeasures) {
//				System.out.println("RuleBook-addAllRuleBookResultsTypeMeasuresToDatabase-3");
				if(daoMeasure.add(resultsTypeMeasureDTO)==false){
//					System.out.println("RuleBook-addAllRuleBookResultsTypeMeasuresToDatabase-3-GRESKA");
					return false;
				}
			}
		}
		return true;
	}
	
	public void addRuleBookResultsTypeMeasure(){
		try {
//			System.out.println("poceo metodu");
//			FacesContext facesCtx = FacesContext.getCurrentInstance();
			
			//citanje kvantitativne vrednosti
			if(isDouble(selectedQuantitativeMeasure)==false){
				facesMessages.addToControlFromResourceBundle(
						"resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorQuantitativeMeasure", FacesContext
								.getCurrentInstance());
				return;
			}
			
			Double quantitativeMeasure = Double.parseDouble(selectedQuantitativeMeasure);
			//citanje naucne grupe
			SciencesGroupDTO sciencesGroup = null;
			for (SciencesGroupDTO sg : allSciencesGroups) {
				if(sg.getClassId().equalsIgnoreCase(selectedSciencesGroupClassId)){
					sciencesGroup = sg;
					break;
				}
			}
			
			if(sciencesGroup == null){
				facesMessages.addToControlFromResourceBundle(
						"resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorSciencesGroup", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//citanje tipa naucnog rezulta	
			ResultsTypeDTO resultsType = null;
			for (ResultsTypeDTO rt : allResultsTypes) {
				if(rt.getClassId().equalsIgnoreCase(selectedResultsTypeClassId)){
					resultsType = rt;
					break;
				}
			}
			
			if(resultsType == null){
				facesMessages.addToControlFromResourceBundle(
						"resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorResultsType", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//pronalazenje ruleBookResultsType
			RuleBookResultsTypeDTO ruleBookResultsTypeDTO = selectedRuleBook.getRuleBookResultsTypeByResultsType(resultsType);
			if(ruleBookResultsTypeDTO == null){
				ruleBookResultsTypeDTO = new RuleBookResultsTypeDTO(selectedRuleBook, resultsType);
				ruleBookResultsTypeDTO.setNotLoadedResultsTypeMeasures(false); 
				selectedRuleBook.getRuleBookResultsTypes().add(ruleBookResultsTypeDTO);
			}
			
			List<ResultsTypeMeasureDTO> resultsTypeMeasures = ruleBookResultsTypeDTO.getResultsTypeMeasures();
			for (ResultsTypeMeasureDTO resultsTypeMeasure : resultsTypeMeasures) {
				if(resultsTypeMeasure.getSciencesGroup().equals(sciencesGroup) && resultsTypeMeasure.getResultsType().equals(resultsType)){
					facesMessages.addToControlFromResourceBundle(
							"resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.ruleBook.add.errorSameResultsTypeMeasure", FacesContext
									.getCurrentInstance());
					return;
				}
			}
			
			resultsTypeMeasures.add(new ResultsTypeMeasureDTO(selectedRuleBook, resultsType, sciencesGroup, quantitativeMeasure));
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.add.successResultsTypeMeasure");
			String messageToSend = MessageFormat.format(pattern, quantitativeMeasure, resultsType, sciencesGroup);
			facesMessages.addToControl("resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());
			
//			System.out.println("uspesno zavrsio metodu");
			
		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"resultsTypeMeasureEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.error", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	private boolean isDouble(String vrednost){
		try {
			Double.parseDouble(vrednost);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	public void deleteResultsTypeMeasure(){
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-1");	
			//citanje naucne grupe
			String sciencesGroupClassId = facesCtx.getExternalContext().getRequestParameterMap().get("sciencesGroupClassId");		
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-2");	
			if(sciencesGroupClassId == null || sciencesGroupClassId.equals("")){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorSciencesGroup", FacesContext
								.getCurrentInstance());
				return;
			}
			SciencesGroupDTO sciencesGroup = new SciencesGroupDTO();
			sciencesGroup.setClassId(sciencesGroupClassId);
			
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-3");	
			//citanje tipa naucnog rezulta	
			String resultsTypeClassId = facesCtx.getExternalContext().getRequestParameterMap().get("resultsTypeClassId");
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-4");	
			if(resultsTypeClassId == null || resultsTypeClassId.equals("")){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorResultsType", FacesContext
								.getCurrentInstance());
				return;
			}
			ResultsTypeDTO resultsType = new ResultsTypeDTO();
			resultsType.setClassId(resultsTypeClassId);
			
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-5");	
			//pronalazenje ruleBookResultsType
			RuleBookResultsTypeDTO ruleBookResultsTypeDTO = selectedRuleBook.getRuleBookResultsTypeByResultsType(resultsType);
			if(ruleBookResultsTypeDTO == null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalRuleBookResultsTypeMeasures", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.update.errorRuleBookResultsTypes", FacesContext
								.getCurrentInstance());
				return;
			}
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-6");	
			List<ResultsTypeMeasureDTO> resultsTypeMeasures = ruleBookResultsTypeDTO.getResultsTypeMeasures();
			for (int i = 0; i < resultsTypeMeasures.size(); i++) {
				ResultsTypeMeasureDTO resultsTypeMeasure = resultsTypeMeasures.get(i);
				if(resultsTypeMeasure.getSciencesGroup().equals(sciencesGroup)){
					resultsTypeMeasures.remove(i);
//					System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-6.1");	
					break;
				}
			}
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-7");	
			
			if(resultsTypeMeasures.size()==0){
				List<RuleBookResultsTypeDTO> ruleBookResultsTypes = selectedRuleBook.getRuleBookResultsTypes();
				int upperBoundary = ruleBookResultsTypes.size()-1;
				
				for (int i = upperBoundary; i >= 0; i--) {
					if((ruleBookResultsTypes.get(i).getResultsType().getClassId() != null)
							&& (ruleBookResultsTypes.get(i).getResultsType().equals(ruleBookResultsTypeDTO.getResultsType()))){
						ruleBookResultsTypes.remove(i);
//						System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-7.1");	
						break;
					}
				}
			}
//			System.out.println("RuleBookManagedBean-deleteRuleBookResultsTypeAndResultsTypeMeasure-8");	
		} catch (Exception e) {
		}
	}
	
	public List<ResultsTypeMeasureDTO> getAllResultsTypeMeasures(){
		List<ResultsTypeMeasureDTO> retVal = new ArrayList<ResultsTypeMeasureDTO>();
//		System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-1");
		List<RuleBookResultsTypeDTO> ruleBookResultsTypes = selectedRuleBook.getRuleBookResultsTypes();
		for (RuleBookResultsTypeDTO ruleBookResultsType : ruleBookResultsTypes) {
//			System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-2");
//			ruleBookResultsType.loadResultsTypeMeasuresFromDatabase();
//			System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-3");
			List<ResultsTypeMeasureDTO> resultsTypeMeasures = ruleBookResultsType.getResultsTypeMeasures();
			for (ResultsTypeMeasureDTO resultsTypeMeasure : resultsTypeMeasures) {
				retVal.add(resultsTypeMeasure);
			}
//			System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-4");
		}
		
//		System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-5 - size:"+retVal.size());
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("resultsType");
		orderByList.add("sciencesGroup");
		directionsList.add("asc");
		directionsList.add("asc");
		
		Collections.sort(retVal, new GenericComparator<ResultsTypeMeasureDTO>(
					orderByList, directionsList));
		
//		System.out.println("RuleBookManagedBean-getAllResultsTypeMeasures-6");

		return retVal;
	}
	//--------------------------------------------------------------------RULE BOOK RESULT MEASURES END
	
	//--------------------------------------------------------------------RULE BOOK ENTITY TYPES START
	
	public void addRuleBookEntityTypeToDatabase(){
		try {
			//citanje entity tipa
			ClassDTO entityType = null;
			for (ClassDTO et : allEntityTypes) {
				if(et.getClassId().equalsIgnoreCase(selectedEntityTypeClassId)){
					entityType = et;
					break;
				}
			}
			
			if(entityType == null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorEntityType", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//pronalazenje Rule Book Entity Type
			RuleBookEntityTypeDTO ruleBookEntityTypeDTO = selectedRuleBook.getRuleBookEntityTypeByEntityType(entityType);
			if(ruleBookEntityTypeDTO != null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorSameRuleBookEntityType", FacesContext
								.getCurrentInstance());
				return;	
			}
			
			ruleBookEntityTypeDTO = new RuleBookEntityTypeDTO(selectedRuleBook, entityType);
		
			RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
			if(dao.addRuleBookEntityType(ruleBookEntityTypeDTO)==false){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorRuleBookEntityType", FacesContext
								.getCurrentInstance());
				return;	
			}
			
			selectedRuleBook.getRuleBookEntityTypes().add(ruleBookEntityTypeDTO);
			
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			
			orderByList.add("entityType");
			directionsList.add("asc");
			Collections.sort(selectedRuleBook.getRuleBookEntityTypes(), new GenericComparator<RuleBookEntityTypeDTO>(
					orderByList, directionsList));
			
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.addRuleBookEntityTypeToDatabase(rbi, entityType)==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorRuleBookEntityTypeForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.add.successRuleBookEntityType");
			String messageToSend = MessageFormat.format(pattern, ruleBookEntityTypeDTO);
			facesMessages.addToControl("ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());
//			System.out.println("uspesno zavrsio metodu");
			
		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookEntityTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.error", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	public void deleteRuleBookEntityTypeToDatabase(){
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			
			String entityTypeClassId = facesCtx.getExternalContext().getRequestParameterMap().get("entityTypeClassId");			
			if(entityTypeClassId == null || entityTypeClassId.equals("")){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalRuleBookEntityTypes", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorEntityType", FacesContext
								.getCurrentInstance());
				return;
			}
			
			ClassDTO entityType = new ClassDTO();
			entityType.setSchemeId(EntityTypesDAO.schemaId);
			entityType.setClassId(entityTypeClassId);
			
			RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
			List<RuleBookEntityTypeDTO> listaTemp = selectedRuleBook.getRuleBookEntityTypes();
			int upperBoundaru = listaTemp.size()-1;
			for (int i = upperBoundaru; i >=0; i--) {
				if(listaTemp.get(i).getEntityType().equals(entityType)){
					if(dao.deleteRuleBookEntityType(listaTemp.get(i))==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalRuleBookEntityTypes", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorRuleBookEntityType", FacesContext
										.getCurrentInstance());
						return;
					}
					listaTemp.remove(i);
					break;
				}
			}
			//ocitaj ponovo listu Entity Results Type iy baze jer je doslo do brijanja RuleBookEntityType
			selectedRuleBook.setNotLoadedRuleBookEntityTypesDependentTables();
					
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.deleteRuleBookEntityTypeToDatabase(rbi, entityType)==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalRuleBookEntityTypes", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorRuleBookEntityTypeForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.delete.successRuleBookEntityType");
			String messageToSend = MessageFormat.format(pattern, selectedRuleBook.getRuleBookEntityTypeByEntityType(entityType).toString());
			facesMessages.addToControl("ruleBookEditForm:generalRuleBookEntityTypes", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());

		} catch (Exception e) {
		}
	}
	
	public List<RuleBookEntityTypeDTO> getAllRuleBookEntityTypes(){
		List<RuleBookEntityTypeDTO> retVal = selectedRuleBook.getRuleBookEntityTypes();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("entityType");
		directionsList.add("asc");
		
		Collections.sort(retVal, new GenericComparator<RuleBookEntityTypeDTO>(
					orderByList, directionsList));
		return retVal;
	}
	
	//--------------------------------------------------------------------RULE BOOK ENTITY TYPES END
	
	//--------------------------------------------------------------------ENTITY RESULTS TYPE START
	
	public boolean isVisibleEntityResultsTypes(){
		boolean retVal = true;
		if(editMode != modesManagedBean.getUpdate()){
			retVal = false;
		}
		else if(selectedRuleBook==null){
			retVal = false;
		}
		else if(selectedRuleBook!=null && selectedRuleBook.getControlNumber()== null){
			retVal = false;
		}
		else if( "".equals(selectedRuleBook.getControlNumber())){
			retVal = false;
		}
		else {
			RuleBookDTO oldRuleBook = (RuleBookDTO) recordDAO.getDTO(selectedRuleBook.getControlNumber());
			oldRuleBook.getName();
			if(!oldRuleBook.getResultsTypeGroup().equals(selectedRuleBook.getResultsTypeGroup())){
				retVal = false;
			}
		}
		return retVal;
	}
	
	public void addEntityResultsTypeToDatabase(){
		try {
			//citanje entity tipa
			ClassDTO entityType = null;
			//provera da li postoji u listi entiteta pravilnika
			for (RuleBookEntityTypeDTO ret : selectedRuleBook.getRuleBookEntityTypes()) {
				if(ret.getEntityType().getClassId().equalsIgnoreCase(selectedRuleBookEntityTypeClassId)){
					entityType = ret.getEntityType();
					break;
				}
			}
			
			if(entityType == null){
				facesMessages.addToControlFromResourceBundle(
						"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorEntityType", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//citanje entity tipa
			ResultsTypeDTO resultsType = null;
			//provera da li postoji u listi rezultata pravilnika
			for (RuleBookResultsTypeDTO rrt : selectedRuleBook.getRuleBookResultsTypes()) {
				if(rrt.getResultsType().getClassId().equalsIgnoreCase(selectedRuleBookResultsTypeClassId)){
					resultsType = rrt.getResultsType();
					break;
				}
			}
						
			if(resultsType == null){
				facesMessages.addToControlFromResourceBundle(
						"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorResultsType", FacesContext
								.getCurrentInstance());
				return;
			}

			//pronalazenje Entity Results Type
			EntityResultsTypeDTO entityResultsTypeDTO = selectedRuleBook.getEntityResultsTypeByResultsTypeAndEntityType(resultsType, entityType);
			if(entityResultsTypeDTO != null){
				facesMessages.addToControlFromResourceBundle(
						"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorSameEntityResultsType", FacesContext
								.getCurrentInstance());
				return;	
			}
			entityResultsTypeDTO = new EntityResultsTypeDTO(selectedRuleBook, resultsType, entityType);
		
			EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
			if(dao.add(entityResultsTypeDTO)==false){
				facesMessages.addToControlFromResourceBundle(
						"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorEntityResultsType", FacesContext
								.getCurrentInstance());
				return;	
			}
			selectedRuleBook.getEntityResultsTypes().add(entityResultsTypeDTO);
			
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			
			orderByList.add("entityType");
			orderByList.add("resultsType");
			directionsList.add("asc");
			directionsList.add("asc");
			Collections.sort(selectedRuleBook.getEntityResultsTypes(), new GenericComparator<EntityResultsTypeDTO>(
					orderByList, directionsList));
			
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.addEntityResultsTypeToDatabase(rbi, resultsType, entityType)==false){
						facesMessages.addToControlFromResourceBundle(
								"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorEntityResultsTypeForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.add.successEntityResultsType");
			String messageToSend = MessageFormat.format(pattern, entityType, resultsType);
			facesMessages.addToControl("entityResultsTypeEditForm:general", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());
			
		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"entityResultsTypeEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.error", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	public void deleteEntityResultsTypeToDatabase(){
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			
			String entityTypeClassId = facesCtx.getExternalContext().getRequestParameterMap().get("ruleBookEntityTypeClassId");			
			if(entityTypeClassId == null || entityTypeClassId.equals("")){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalEntityResultsTypes", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorEntityType", FacesContext
								.getCurrentInstance());
				return;
			}

			String resultsTypeClassId = facesCtx.getExternalContext().getRequestParameterMap().get("ruleBookResultsTypeClassId");			
			if(resultsTypeClassId == null || resultsTypeClassId.equals("")){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:generalEntityResultsTypes", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.delete.errorResultsType", FacesContext
								.getCurrentInstance());
				return;
			}

			ClassDTO entityType = new ClassDTO();
			entityType.setSchemeId(EntityTypesDAO.schemaId);
			entityType.setClassId(entityTypeClassId);
			
			ResultsTypeDTO resultsType = new ResultsTypeDTO();
			resultsType.setClassId(resultsTypeClassId);
			
			EntityResultsTypeDTO ert = selectedRuleBook.getEntityResultsTypeByResultsTypeAndEntityType(resultsType, entityType);
			if(ert!=null){
				entityType = ert.getEntityType();
				resultsType = ert.getResultsType();
			}
			
			EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
			List<EntityResultsTypeDTO> listaTemp = selectedRuleBook.getEntityResultsTypes();
			int upperBoundaru = listaTemp.size()-1;
			for (int i = upperBoundaru; i >=0; i--) {
				if(listaTemp.get(i).getResultsType().equals(resultsType) && listaTemp.get(i).getEntityType().equals(entityType)){
					if(dao.delete(listaTemp.get(i))==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalEntityResultsTypes", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorEntityResultsType", FacesContext
										.getCurrentInstance());
						return;
					}
					listaTemp.remove(i);
					break;
				}
			}
			//ocitaj ponovo listu Entity Results Type iy baze jer je doslo do brijanja RuleBookEntityType			
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.deleteEntityResultsTypeToDatabase(rbi, resultsType, entityType)==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalRuleBookEntityTypes", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorEntityResultsTypeForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.delete.successEntityResultsType");
			String messageToSend = MessageFormat.format(pattern, entityType, resultsType);
			facesMessages.addToControl("ruleBookEditForm:generalEntityResultsTypes", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());

		} catch (Exception e) {
		}
	}
	
	public List<EntityResultsTypeDTO> getAllEntityResultsTypes(){
		List<EntityResultsTypeDTO> retVal = selectedRuleBook.getEntityResultsTypes();

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
	
	//--------------------------------------------------------------------ENTITY RESULTS TYPE END
	
	
	//--------------------------------------------------------------------RULEBOOK RESULTS TYPE MAPPING START
	
	public boolean isVisibleRuleBookResultsTypeMappings(){
		boolean retVal = true;
		if(editMode != modesManagedBean.getUpdate()){
			retVal = false;
		}
		else if(selectedRuleBook==null){
			retVal = false;
		}
		else if(selectedRuleBook!=null && selectedRuleBook.getControlNumber()== null){
			retVal = false;
		}
		else if( "".equals(selectedRuleBook.getControlNumber())){
			retVal = false;
		}
		else {
			RuleBookDTO oldRuleBook = (RuleBookDTO) recordDAO.getDTO(selectedRuleBook.getControlNumber());
			oldRuleBook.getName();
			if(!oldRuleBook.getResultsTypeGroup().equals(selectedRuleBook.getResultsTypeGroup())){
				retVal = false;
			}
		}
		return retVal;
	}
	
	public void addRuleBookResultsTypeMappingToDatabase(){
		
		try {
			//citanje research rola
			ClassDTO researcherRole=null;
			//provera da li postoji u listi svih research rola
			for (ClassDTO rr : allResearcherRoles) {
				if(rr.getClassComparableId().equalsIgnoreCase(selectedResearcherRoleCompariableId)){
					researcherRole = rr;
					break;
				}
			}
			
			if(researcherRole == null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorResearcherRole", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//citanje entity tipa
			ClassDTO entityType = null;
			//provera da li postoji u listi entiteta pravilnika
			for (RuleBookEntityTypeDTO ret : selectedRuleBook.getRuleBookEntityTypes()) {
				if(ret.getEntityType().getClassComparableId().equalsIgnoreCase(selectedEntityTypeCompariableId)){
					entityType = ret.getEntityType();
					break;
				}
			}
			
			if(entityType == null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorEntityType", FacesContext
								.getCurrentInstance());
				return;
			}
			
			//citanje entity source typa
			ClassDTO entitySourceType = null;
			//provera da li postoji u listi entiteta pravilnika
			for (RuleBookEntityTypeDTO ret : selectedRuleBook.getRuleBookEntityTypes()) {
				if(ret.getEntityType().getClassComparableId().equalsIgnoreCase(selectedEntitySourceTypeCompariableId)){
					entitySourceType = ret.getEntityType();
					break;
				}
			}
			
			//citanje researcher results tipa
			ResultsTypeDTO resultsTypeForResearcherRole = null;
			//provera da li postoji u listi rezultata pravilnika
			for (RuleBookResultsTypeDTO rrt : selectedRuleBook.getRuleBookResultsTypes()) {
				if(rrt.getResultsType().getClassComparableId().equalsIgnoreCase(selectedResultsTypeForResearcherRoleCompariableId)){
					resultsTypeForResearcherRole = rrt.getResultsType();
					break;
				}
			}
						
			if(resultsTypeForResearcherRole == null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorResultsTypeForResearcherRole", FacesContext
								.getCurrentInstance());
				return;
			}

			//citanje results tipa posmatranog entiteta
			ResultsTypeDTO resultsTypeOfObsEntity = null;
			//provera da li postoji u listi rezultata pravilnika
			for (RuleBookResultsTypeDTO rrt : selectedRuleBook.getRuleBookResultsTypes()) {
				if(rrt.getResultsType().getClassComparableId().equalsIgnoreCase(selectedResultsTypeOfObsEntityCompariableId)){
					resultsTypeOfObsEntity = rrt.getResultsType();
					break;
				}
			}
			
			//citanje results tipa posmatranog izvora za entitet
			ResultsTypeDTO resultsTypeOfObsEntitySource = null;
			//provera da li postoji u listi rezultata pravilnika
			for (RuleBookResultsTypeDTO rrt : selectedRuleBook.getRuleBookResultsTypes()) {
				if(rrt.getResultsType().getClassComparableId().equalsIgnoreCase(selectedResultsTypeOfObsEntitySourceCompariableId)){
					resultsTypeOfObsEntitySource = rrt.getResultsType();
					break;
				}
			}
					
			//pronalazenje RuleBook Results Type Mapping
			RuleBookResultsTypeMappingDTO ruleBookResultsTypeMapping = selectedRuleBook.getRuleBookResultsTypeMappingByAllParms
					(researcherRole, entityType, entitySourceType, resultsTypeForResearcherRole, resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
			if(ruleBookResultsTypeMapping != null){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorSameRuleBookResultsTypeMapping", FacesContext
								.getCurrentInstance());
				return;	
			}
			
			ruleBookResultsTypeMapping = new RuleBookResultsTypeMappingDTO(null, selectedRuleBook, researcherRole, entityType, entitySourceType, resultsTypeForResearcherRole,
					resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
		
			RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
			if(dao.add(ruleBookResultsTypeMapping)==false){
				facesMessages.addToControlFromResourceBundle(
						"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.ruleBook.add.errorRuleBookResultsTypeMapping", FacesContext
								.getCurrentInstance());
				return;	
			}
			selectedRuleBook.getRuleBookResultsTypeMappings().add(ruleBookResultsTypeMapping);
			
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			
			orderByList.add("researcherRole");
			orderByList.add("entityType");
			orderByList.add("entitySourceType");
			directionsList.add("asc");
			directionsList.add("asc");
			directionsList.add("asc");
			Collections.sort(selectedRuleBook.getRuleBookResultsTypeMappings(), new GenericComparator<RuleBookResultsTypeMappingDTO>(
					orderByList, directionsList));
			
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.addRuleBookResultsTypeMappingToDatabase(rbi, researcherRole, entityType, entitySourceType, resultsTypeForResearcherRole,
							resultsTypeOfObsEntity, resultsTypeOfObsEntitySource)==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.add.errorRuleBookResultsTypeMappingForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			selectedRuleBook.setNotLoadedRuleBookResultsTypeMappings(true);			
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.add.successRuleBookResultsTypeMapping");
			String messageToSend = MessageFormat.format(pattern, researcherRole, entityType, entitySourceType, resultsTypeForResearcherRole,
					resultsTypeOfObsEntity, resultsTypeOfObsEntitySource);
			facesMessages.addToControl("ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());
			
		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"ruleBookResultsTypeMappingEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.ruleBook.add.error", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	public void deleteRuleBookResultsTypeMappingToDatabase(){
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			
			String ruleBookResultsTypeMappingMappingId = facesCtx.getExternalContext().getRequestParameterMap().get("ruleBookResultsTypeMappingMappingId");			
			RuleBookResultsTypeMappingDTO rrtm = selectedRuleBook.getRuleBookResultsTypeMappingByMappingId(new Integer(ruleBookResultsTypeMappingMappingId));
			
			RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
			List<RuleBookResultsTypeMappingDTO> listaTemp = selectedRuleBook.getRuleBookResultsTypeMappings();
			int upperBoundaru = listaTemp.size()-1;
			for (int i = upperBoundaru; i >=0; i--) {
				if(listaTemp.get(i).equals(rrtm)){
					if(dao.delete(listaTemp.get(i))==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalRuleBookResultsTypeMappings", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorRuleBookResultsTypeMapping", FacesContext
										.getCurrentInstance());
						return;
					}
					listaTemp.remove(i);
					break;
				}
			}
			
			//ocitaj ponovo listu Entity Results Type iy baze jer je doslo do brijanja RuleBookEntityType			
			for(RuleBookDTO el:getAllSubElements(selectedRuleBook)){
				if(el instanceof RuleBookImplementationDTO){
					RuleBookImplementationDTO rbi = (RuleBookImplementationDTO) el;
					RuleBookImplementationManagedBean rbiMB = getRuleBookImplementationManagedBean();
					if(rbiMB.deleteRuleBookResultsTypeMappingToDatabase(rbi, rrtm.getResearcherRole(), rrtm.getEntityType(), rrtm.getEntitySourceType(), rrtm.getResultsTypeForResearcherRole(),
							rrtm.getResultsTypeOfObsEntity(), rrtm.getResultsTypeOfObsEntitySource())==false){
						facesMessages.addToControlFromResourceBundle(
								"ruleBookEditForm:generalRuleBookResultsTypeMappings", FacesMessage.SEVERITY_ERROR, 
								"records.ruleBook.delete.errorRuleBookResultsTypeMappingForRuleBookSubelements", FacesContext
										.getCurrentInstance());
						return;
					}
				}	
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.ruleBook.delete.successRuleBookResultsTypeMapping");
			String messageToSend = MessageFormat.format(pattern, rrtm.getResearcherRole(), rrtm.getEntityType(), rrtm.getEntitySourceType(), rrtm.getResultsTypeForResearcherRole(),
					rrtm.getResultsTypeOfObsEntity(), rrtm.getResultsTypeOfObsEntitySource());
			facesMessages.addToControl("ruleBookEditForm:generalRuleBookResultsTypeMappings", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());

		} catch (Exception e) {
		}
	}
	
	public List<RuleBookResultsTypeMappingDTO> getAllRuleBookResultsTypeMappings(){
		List<RuleBookResultsTypeMappingDTO> retVal = selectedRuleBook.getRuleBookResultsTypeMappings();

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
	//--------------------------------------------------------------------RULEBOOK RESULTS TYPE MAPPING END
}
