package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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
import org.apache.lucene.search.TopDocCollector;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import rs.ac.uns.ftn.informatika.bibliography.dao.EvaluationDataDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDataDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AutocitationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographEvaluationDataDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for monograph
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MonographManagedBean extends CRUDManagedBean implements
		IPickAuthorManagedBean, IPickResearchAreaManagedBean {

	private List<MonographDTO> list;
	
	private List<MonographDTO> duplicateMonographs;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private MonographDTO selectedMonograph = null;
	
	private MonographEvaluationDataDTO selectedMonographEvaluationData = null;
	
	


	public MonographManagedBean(){
		super();
		pickSimilarMessage = "records.monograph.pickSimilarMessage";
		tableModalPanel = "monographBrowseModalPanel";
		editModalPanel = "monographEditModalPanel";
		simpleEditModalPanel = "monographSimpleEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedMonograph = null;
		pickMessage = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickMonographManagedBean = null;
		tableModalPanel = "monographBrowseModalPanel";
		editModalPanel = "monographEditModalPanel";
		simpleEditModalPanel = "monographSimpleEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<MonographDTO> listTmp = getMonographs(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false) && (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedMonograph != null
					&& selectedMonograph.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedMonograph.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("monographTable");
					if(table!=null){
						int page = index / table.getRows();
						table.setFirst(table.getRows()*page);
					}
				}
				init = false;
			}

			list = listTmp;
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<MonographDTO>();
		}
	}
	
	/**
	 * Retrieves a list of monographs that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving monographs
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of monograph that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<MonographDTO> getMonographs(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getMonographs(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of monographs that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving monographs
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of monographs that correspond to the query
	 */
	private List<MonographDTO> getMonographs(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<MonographDTO> retVal = new ArrayList<MonographDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				MonographDTO dto = (MonographDTO) record.getDto();
				if (dto != null) {
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
			Collections.sort(retVal, new GenericComparator<MonographDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of monographs (filtered and ordered by ...)
	 */
	public List<MonographDTO> getMonographs() {
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
			Collections.sort(list, new GenericComparator<MonographDTO>(
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
		if((tableMode == ModesManagedBean.MODE_PICK) && (customPick)){
			bq.add(QueryUtils.makeBooleanQuery("TI", title, Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", title + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		} else if ((whereStr != null) && (!"".equals(whereStr))) {
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		}
		if (tableMode == ModesManagedBean.MODE_BROWSE){
			AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
			if (loggedAuthor.getControlNumber() != null){
				BooleanQuery authorsPapers = new BooleanQuery();
				authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
				authorsPapers.add(new TermQuery(new Term("EDCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
				bq.add(authorsPapers, Occur.MUST);
			}
		}
		bq.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
		return bq;
	}
	
	List<MonographDTO> similarMonographs = null;
	
	/**
	 * @return the list of SIMILAR monograph with selected monograph
	 */
	public List<MonographDTO> getSimilarMonographs() {
		return similarMonographs;
	}

	/**
	 * Creates query for finding SIMILAR monograph with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarMonographsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedMonograph.getTitle().getContent()!=null)
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedMonograph.getTitle().getContent(), Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		else if(selectedMonograph.getTitleTranslations().size() > 0)
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedMonograph.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);	
		bq.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
		return bq;
	}
	
	public List<MonographDTO> autocomplete(String suggest) {
		List<MonographDTO> retVal = new ArrayList<MonographDTO>();
		if(suggest.contains("(BISIS)")){
        	retVal.add((MonographDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedMonograph!=null && selectedMonograph.getControlNumber() != null){
        	retVal.add(selectedMonograph);
        	return retVal;
        }
		
		String monographTitle = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(monographTitle != null)
			bq.add(QueryUtils.makeBooleanQuery("TI", monographTitle + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
		

		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				MonographDTO dto = (MonographDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }

	
	public List<String> autocompleteAndrejevicMonograph(String suggest) {
		List<String> retVal = new ArrayList<String>();

		
		String monographTitle = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(monographTitle != null)
			bq.add(QueryUtils.makeBooleanQuery("TI", monographTitle + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
		bq.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
		

		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				MonographDTO dto = (MonographDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto.getSomeTitle());
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }

	
	
	/**
	 * @return the selected monograph
	 */
	public MonographDTO getSelectedMonograph() {
		return selectedMonograph;
	}

	/**
	 * @param monographDTO
	 *            the monograph to set as selected monograph
	 */
	public void setSelectedMonograph(MonographDTO monographDTO) {
		selectedMonograph = monographDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedMonograph = findMonographByControlNumber(list);
			if (selectedMonograph != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedMonograph);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleUpdateMode()
	 */
	@Override
	public void switchToSimpleUpdateMode() {
		simpleAuthorList = true;
		pickingAuthor = true;
		setAuthorManageBeanToPick(this);
		authorName = "";
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToSimpleUpdateMode();
		else {
			selectedMonograph = findMonographByControlNumber(list);
			if (selectedMonograph != null) {
				super.switchToSimpleUpdateMode();
				debug("switchToSimpleUpdateMode: \n" + selectedMonograph);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedMonograph = new MonographDTO();
		if(tableMode == ModesManagedBean.MODE_BROWSE){
			if ((getUserManagedBean().getLoggedUser() != null)
				&& (getUserManagedBean().getLoggedUser().getAuthor() != null)
				&& (getUserManagedBean().getLoggedUser().getAuthor()
						.getControlNumber() != null)
				&& (!("".equals(getUserManagedBean().getLoggedUser()
						.getAuthor().getControlNumber())))) {
				AuthorDTO author = getUserManagedBean().getLoggedUser().getAuthor();
				List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
				otherFormatNames.add(author.getName());
				otherFormatNames.addAll(author.getOtherFormatNames());
				author.setOtherFormatNames(otherFormatNames);
				author.setName(new AuthorNameDTO());
				selectedMonograph.setMainAuthor(author);
			}
		}
		selectedMonograph.getTitle().setContent(title);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleAddMode()
	 */
	@Override
	public void switchToSimpleAddMode() {
		simpleAuthorList = true;
		super.switchToSimpleAddMode();
		pickingAuthor = true;
		setAuthorManageBeanToPick(this);
		authorName = "";
		selectedMonograph = new MonographDTO();
		editTabNumber = 0;
		selectedMonograph.getTitle().setContent(title);
		selectedMonograph.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToViewDetailsMode();
		else {
			selectedMonograph = findMonographByControlNumber(list);
			if (selectedMonograph != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedMonograph);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleViewDetailsMode()
	 */
	@Override
	public void switchToSimpleViewDetailsMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToSimpleViewDetailsMode();
		else {
			selectedMonograph = findMonographByControlNumber(list);
			if (selectedMonograph != null) {
				super.switchToSimpleViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedMonograph);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			debug("findSimilarMonographs");
			similarMonographs = getMonographs(createSimilarMonographsQuery(),
					null, null,  new AllDocCollector(true));
			editTabNumber = 3;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("findSimilarMonographs", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			if(iPickMonographManagedBean != null)
				iPickMonographManagedBean.cancelPickingMonograph();
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		monographControlNumber = null;
		title = null;
		duplicateMonographs = new ArrayList<MonographDTO>();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		super.switchToBrowseMode();
		orderBy = "publicationYear";
		direction = "asc";
		duplicateMonographs = new ArrayList<MonographDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedMonograph.setNotLoaded(true);
		super.switchToEditNoneMode();
	}
	
	
	public void swichToAddEvaluationData(){		
		simpleForm = false;		
		editTabNumber = 0;
		editModalPanel = "monographEvaluationDataModalPanel";
		setEditMode(ModesManagedBean.MODE_ADD_MONOGRAPH_EVALUATION_DATA);
		addEvaluationData();
		debug("switchToAddEvaluationDataMode: \n" + selectedMonograph);					
	}
	
	private void addEvaluationData(){
		selectedMonographEvaluationData = new EvaluationDataDAO(new EvaluationDataDB()).getEvaluationDataFromDatabase(selectedMonograph);
		if(selectedMonographEvaluationData==null){
			selectedMonographEvaluationData = new MonographEvaluationDataDTO(selectedMonograph);			
			for(AuthorDTO author:selectedMonograph.getAllAuthors()){
				AutocitationDTO autocitation = new AutocitationDTO(selectedMonograph, author);				
				selectedMonographEvaluationData.getAutocitations().add(autocitation);
			}
		}else{
			// ako se se promenili autori			
				for(AuthorDTO author:selectedMonograph.getAllAuthors()){
					if(selectedMonographEvaluationData.getAutocitationForAuthor(author)==null){
						// postoji u listi autora i treba ga dodati u autocitate
						selectedMonographEvaluationData.getAutocitations()
							.add(new AutocitationDTO(selectedMonograph, author));						
					}
				}
				List<AutocitationDTO> forDelete = new ArrayList<AutocitationDTO>();
				for(AutocitationDTO autocitation:selectedMonographEvaluationData.getAutocitations()){
					if(!selectedMonograph.getAllAuthors().contains(autocitation.getForResearcher())){
						// postoji u autocitatima, a nema ga u lisiti autora treba ga obristi iz liste autocitata
						forDelete.add(autocitation);
					}
				}
				selectedMonographEvaluationData.getAutocitations().removeAll(forDelete);	
		}
		selectedMonograph.setEvaluationData(selectedMonographEvaluationData);		
		selectedMonographEvaluationData.setMonograph(selectedMonograph);
	}
	
	public void saveEvaluationData(){
		EvaluationDataDAO evaldataDAO = new EvaluationDataDAO(new EvaluationDataDB());
		evaldataDAO.updateMonographEvaluationData(selectedMonographEvaluationData);
		finishWizard();
	}
	
	public void examineData(){
		mergeMonograph = selectedMonograph;
		
		selectedMonograph = findMonographByControlNumber(similarMonographs);
		if(selectedMonograph == null){
			selectedMonograph = mergeMonograph;
			mergeMonograph = null;
		} else {
			if(selectedMonograph.getLanguages().size() == 0)
				selectedMonograph.setLanguages(mergeMonograph.getLanguages());
			if(selectedMonograph.getOriginalLanguages().size() == 0)
				selectedMonograph.setOriginalLanguages(mergeMonograph.getOriginalLanguages());
			if(selectedMonograph.getPublisher().getOriginalPublisher().getName() == null)
				selectedMonograph.getPublisher().getOriginalPublisher().setName(mergeMonograph.getPublisher().getOriginalPublisher().getName());
			if(selectedMonograph.getPublisher().getOriginalPublisher().getPlace() == null)
				selectedMonograph.getPublisher().getOriginalPublisher().setPlace(mergeMonograph.getPublisher().getOriginalPublisher().getPlace());
			if(selectedMonograph.getPublisher().getOriginalPublisher().getState() == null)
				selectedMonograph.getPublisher().getOriginalPublisher().setState(mergeMonograph.getPublisher().getOriginalPublisher().getState());
			if(selectedMonograph.getNumberOfPages() == null)
				selectedMonograph.setNumberOfPages(mergeMonograph.getNumberOfPages());
			if(selectedMonograph.getDimension() == null)
				selectedMonograph.setDimension(mergeMonograph.getDimension());
			if(selectedMonograph.getEditionTitle() == null)
				selectedMonograph.setEditionTitle(mergeMonograph.getEditionTitle());
			if(selectedMonograph.getEditionNumber() == null)
				selectedMonograph.setEditionNumber(mergeMonograph.getEditionNumber());
			if(selectedMonograph.getEditionISSN() == null)
				selectedMonograph.setEditionISSN(mergeMonograph.getEditionISSN());
			if(selectedMonograph.getKeywords().getContent() == null)
				selectedMonograph.setKeywords(mergeMonograph.getKeywords());
			if(selectedMonograph.getResearchArea().getClassId() == null)
				selectedMonograph.setResearchArea(mergeMonograph.getResearchArea());
			
			
		}
		editTabNumber = 0;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeMonograph = findMonographByControlNumber(similarMonographs);
		if(mergeMonograph != null){
			selectedMonograph.setControlNumber(mergeMonograph.getControlNumber()); 
			editTabNumber = 0;
			populateImportMessages();
		}
	}
	
	private MonographDTO mergeMonograph;
	
	public void populateImportMessages(){
		if(mergeMonograph != null){
			if(simpleForm){
				if((mergeMonograph.getTitle().getLanguage() != null) && (mergeMonograph.getTitle().getLanguage().trim().length()>0))
					facesMessages.addToControl(
						"monographSimpleEditForm:language", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getTitle().getLanguage(), FacesContext
								.getCurrentInstance());
				if((mergeMonograph.getTitle().getContent() != null) && (mergeMonograph.getTitle().getContent().trim().length()>0))
					facesMessages.addToControl(
						"monographSimpleEditForm:title", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getTitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeMonograph.getIsbn() != null) && (mergeMonograph.getIsbn().trim().length()>0))
					facesMessages.addToControl(
						"monographSimpleEditForm:isbn", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getIsbn(), FacesContext
								.getCurrentInstance());
				if(mergeMonograph.getNumberOfPages() != null)
					facesMessages.addToControl(
						"monographSimpleEditForm:numberOfPages", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getNumberOfPages().toString(), FacesContext
								.getCurrentInstance());
				String listOfAuthors = "";
				for (AuthorDTO author : mergeMonograph.getAllAuthors()) {
					listOfAuthors += author.toString();
				}
				if (listOfAuthors.length() > 0)
					facesMessages.addToControl(
							"monographSimpleEditForm:listOfAuthors", FacesMessage.SEVERITY_INFO, 
							listOfAuthors, FacesContext
									.getCurrentInstance());
				String publisher = "";
				if((mergeMonograph.getPublisher().getOriginalPublisher().getName() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getName().trim().length()>0))
					publisher += mergeMonograph.getPublisher().getOriginalPublisher().getName();
				if((mergeMonograph.getPublisher().getOriginalPublisher().getPlace() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getPlace().trim().length()>0))
					publisher += ", " + mergeMonograph.getPublisher().getOriginalPublisher().getPlace();
				if((mergeMonograph.getPublisher().getOriginalPublisher().getState() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getState().trim().length()>0))
					publisher += ", " + mergeMonograph.getPublisher().getOriginalPublisher().getState();
				if(publisher.trim().length() > 0)
					facesMessages.addToControl(
						"monographSimpleEditForm:publisher", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getPublisher().getOriginalPublisher().getName(), FacesContext
								.getCurrentInstance());
				if((mergeMonograph.getPublicationYear() != null) && (mergeMonograph.getPublicationYear().trim().length()>0))
					facesMessages.addToControl(
						"monographSimpleEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
						mergeMonograph.getPublicationYear(), FacesContext
								.getCurrentInstance());
			} else {
				if(editTabNumber == 0){
					if((mergeMonograph.getTitle().getLanguage() != null) && (mergeMonograph.getTitle().getLanguage().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:language", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getTitle().getLanguage(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getTitle().getContent() != null) && (mergeMonograph.getTitle().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:title", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getTitle().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getSubtitle().getContent() != null) && (mergeMonograph.getSubtitle().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:subtitle", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getSubtitle().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getIsbn() != null) && (mergeMonograph.getIsbn().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:isbn", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getIsbn(), FacesContext
									.getCurrentInstance());
					if(mergeMonograph.getNumberOfPages() != null)
						facesMessages.addToControl(
							"monographEditForm:numberOfPages", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getNumberOfPages().toString(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getDimension() != null) && (mergeMonograph.getDimension().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:dimension", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getDimension(), FacesContext
									.getCurrentInstance());
					if(mergeMonograph.getCount() != null)
						facesMessages.addToControl(
							"monographEditForm:count", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getCount().toString(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getVolumeCode() != null) && (mergeMonograph.getVolumeCode().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:volumeCode", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getVolumeCode(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getVolumeTitle() != null) && (mergeMonograph.getVolumeTitle().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:volumeTitle", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getVolumeTitle(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getVolumeCode() != null) && (mergeMonograph.getVolumeCode().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:volumeCode", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getVolumeCode(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getEditionTitle() != null) && (mergeMonograph.getEditionTitle().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:editionTitle", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getEditionTitle(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getEditionISSN() != null) && (mergeMonograph.getEditionISSN().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:editionISSN", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getEditionISSN(), FacesContext
									.getCurrentInstance());
					if(mergeMonograph.getEditionNumber() != null)
						facesMessages.addToControl(
							"monographEditForm:editionNumber", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getEditionNumber().toString(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getNote().getContent() != null) && (mergeMonograph.getNote().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:note", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getNote().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getKeywords().getContent() != null) && (mergeMonograph.getKeywords().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:keywords", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getKeywords().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getAbstracT().getContent() != null) && (mergeMonograph.getAbstracT().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:abstract", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getAbstracT().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getUri() != null) && (mergeMonograph.getUri().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:uri", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getUri(), FacesContext
									.getCurrentInstance());
				} 
				if (editTabNumber == 1){
					String listOfAuthors = "";
					for (AuthorDTO author : mergeMonograph.getAllAuthors()) {
						listOfAuthors += author.toString();
					}
					if (listOfAuthors.length() > 0)
						facesMessages.addToControl(
								"monographEditForm:listOfAuthors", FacesMessage.SEVERITY_INFO, 
								listOfAuthors, FacesContext
										.getCurrentInstance());
					String listOfEditors = "";
					for (AuthorDTO editor : mergeMonograph.getEditors()) {
						listOfEditors += editor.toString();
					}
					if (listOfEditors.length() > 0)
						facesMessages.addToControl(
								"monographEditForm:listOfEditors", FacesMessage.SEVERITY_INFO, 
								listOfEditors, FacesContext
										.getCurrentInstance());
					
				} 
				if(editTabNumber == 2){
					if((mergeMonograph.getResearchArea().getTerm().getContent() != null) && (mergeMonograph.getResearchArea().getTerm().getContent().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:researchArea", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getResearchArea().getTerm().getContent(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getPublisher().getOriginalPublisher().getName() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getName().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:publisherName", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getPublisher().getOriginalPublisher().getName(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getPublisher().getOriginalPublisher().getPlace() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getPlace().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:publisherPlace", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getPublisher().getOriginalPublisher().getPlace(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getPublisher().getOriginalPublisher().getState() != null) && (mergeMonograph.getPublisher().getOriginalPublisher().getState().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:publisherState", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getPublisher().getOriginalPublisher().getState(), FacesContext
									.getCurrentInstance());
					if((mergeMonograph.getPublicationYear() != null) && (mergeMonograph.getPublicationYear().trim().length()>0))
						facesMessages.addToControl(
							"monographEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
							mergeMonograph.getPublicationYear(), FacesContext
									.getCurrentInstance());
					String listOfLanguages = "";
					for (String language : mergeMonograph.getLanguages()) {
						listOfLanguages += ", " + language;
					}
					if (listOfLanguages.length() > 0)
						facesMessages.addToControl(
								"monographEditForm:listOfLanguages", FacesMessage.SEVERITY_INFO, 
								listOfLanguages, FacesContext
										.getCurrentInstance());
					String listOfOriginalLanguages = "";
					for (String language : mergeMonograph.getOriginalLanguages()) {
						listOfOriginalLanguages += ", " + language;
					}
					if (listOfOriginalLanguages.length() > 0)
						facesMessages.addToControl(
								"monographEditForm:listOfOriginalLanguages", FacesMessage.SEVERITY_INFO, 
								listOfOriginalLanguages, FacesContext
										.getCurrentInstance());
				}
			} 
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if(simpleForm){
			if (validateAll() == false) {
				return;
			}
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedMonograph)) == false) {
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"monographSimpleEditForm:general":"monographEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.monograph.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"monographSimpleEditForm:general":"monographEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.monograph.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedMonograph);
			sendRecordMessage(selectedMonograph, "update");
			changedReferenceAuthorsEmailNotification(selectedMonograph, facesMessages.getMessageFromResourceBundle("records.monograph.changedMonographAuthorsNotification.subject"));
		}
	}

	@Override
	public void add() {
		if(simpleForm){
			if (validateAll() == false) {
				return;
			}
		} 
		similarMonographs = null;
		if(editTabNumber < 3){
			try {
				debug("findSimilarMonograph");
				similarMonographs = getMonographs(createSimilarMonographsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarMonograph", e);
			}
		}
		if((similarMonographs == null ) || (similarMonographs.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedMonograph)) == false) {
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"monographSimpleEditForm:general":"monographEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.monograph.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber < 3)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"monographSimpleEditForm:general":"monographEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.monograph.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedMonograph);
				newReferenceAuthorsEmailNotification(selectedMonograph, facesMessages.getMessageFromResourceBundle("records.paperMonograph.newMonographAuthorsNotification.subject"));
				newRecordEmailNotification(selectedMonograph, facesMessages.getMessageFromResourceBundle("records.monograph.newMonographNotification.subject"));
			}
		} else {
			nextEditTab();
		}
	}

	@Override
	public void delete() {
		selectedMonograph = findMonographByControlNumber(list);
		if (selectedMonograph == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedMonograph)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"monographPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.monograph.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedMonograph);
			selectedMonograph = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void chooseDuplicateMonograph() {

		try {
			MonographDTO duplicateMonograph = findMonographByControlNumber(list);
			if (duplicateMonograph != null) {
				if(duplicateMonographs.contains(duplicateMonograph))
					duplicateMonographs.remove(duplicateMonograph);
				else
					duplicateMonographs.add(duplicateMonograph);
			}
		} catch (Exception e) {
			error("chooseDuplicateMonograph", e);
		}
	}
	
	public void replaceDuplicateMonographs(){
		try {
			selectedMonograph = findMonographByControlNumber(list);
			if ((selectedMonograph != null) && (!(duplicateMonographs.contains(selectedMonograph)))){
				
				for (MonographDTO duplicateMonograph : duplicateMonographs) {
					Query query = new TermQuery(new Term("MOCN", duplicateMonograph.getControlNumber()));
					List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
					for (Record record : list) {
						record.loadFromDatabase();
						if(record.getDto() instanceof PaperMonographDTO){
							PaperMonographDTO temp = (PaperMonographDTO)(record.getDto());
							temp.setMonograph(selectedMonograph);
							if(recordDAO.update(new Record(null, null, getUserManagedBean()
									.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									temp)) == false){
								facesMessages.addToControlFromResourceBundle(
										"monographPickForm:general", FacesMessage.SEVERITY_ERROR, 
										"records.monograph.replace.error", FacesContext
												.getCurrentInstance());
								return;
							} else {
								sendRecordMessage(temp, "update");
							}
						} else {
							facesMessages.addToControlFromResourceBundle(
									"monographPickForm:general", FacesMessage.SEVERITY_ERROR, 
									"records.monograph.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					debug("monograph: \n" + duplicateMonograph +  "\n\nreplaced with: \n" + selectedMonograph);
					recordDAO.delete(duplicateMonograph.getRecord());
				}
				facesMessages.addToControlFromResourceBundle(
						"monographPickForm:general", FacesMessage.SEVERITY_INFO, 
						"records.monograph.replace.success", FacesContext
								.getCurrentInstance());
				selectedMonograph = null;
				populateList = true;
				orderList = true;
				duplicateMonographs = new ArrayList<MonographDTO>();
			} else {
				facesMessages.addToControlFromResourceBundle(
						"monographPickForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.monograph.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateMonographs", e);
		}
	}

	/**
	 * @return the duplicateMonographs
	 */
	public String getDuplicateMonographsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (MonographDTO duplicateMonograph : duplicateMonographs) {
			retVal.append(duplicateMonograph + "\n");
		}
		return retVal.toString();
	}

	/**
	 * Prepares web form where user can choose Research area for selected study final document
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
		mb.setPickMessage("records.monograph.pickResearchAreaMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#setResearchArea(rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO)
	 */
	@Override
	public void setResearchArea(ResearchAreaDTO researchArea) {
		selectedMonograph.setResearchArea(researchArea);
	}
	

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#cancelPickingResearchArea()
	 */
	@Override
	public void cancelPickingResearchArea() {
	}

	
	private boolean pickingAuthor;
	
	/**
	 * Prepares web form where user can choose Author for selected authorsMonograph
	 */
	public void pickAuthor() {
		
		pickingAuthor = true;
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}

		mb.setIPickAuthorManagedBean(this);
		mb.setSelectedAuthor(new AuthorDTO());
		mb.setPickMessageFirstTab("records.monograph.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.monograph.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.monograph.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.monograph.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.monograph.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.monograph.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.monograph.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.monograph.pickAuthor.pleaseInstitution");
		mb.switchToPickMode();
		if(simpleForm){
			simpleFormPickAuthor(mb);
		}
	}
	
	/**
	 * Adds the other format name to selected author
	 */
	public void addAuthorOtherFormatName() {
		
		pickingAuthor = true;
		
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
					"authorManagedBean");
			if (mb == null) {
				mb = new AuthorManagedBean();
				extCtx.getSessionMap().put("authorManagedBean", mb);
			}
			mb.setEditMode(ModesManagedBean.MODE_ADD_FORMAT_NAME);
			mb.editTabNumber = 0;
			mb.pleaseInstitution = true;
			mb.setIPickAuthorManagedBean(this);
			mb.setSelectedAuthor((AuthorDTO)(recordDAO.getDTO(selectedAuthor.getControlNumber())));
			mb.setFirstnameOtherFormat("");
			mb.setLastnameOtherFormat("");
		}
	}
	
	/**
	 * Adds the otherName to selected author
	 */
	public void editAuthorOtherName() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
					"authorManagedBean");
			if (mb == null) {
				mb = new AuthorManagedBean();
				extCtx.getSessionMap().put("authorManagedBean", mb);
			}
			mb.setEditMode(ModesManagedBean.MODE_UPDATE);
			mb.editTabNumber = 0;
			mb.pleaseInstitution = false;
			mb.setSelectedAuthor((AuthorDTO)(personDAO.getDTO(selectedAuthor.getControlNumber())));
			mb.setFirstnameOtherFormat("");
			mb.setLastnameOtherFormat("");
		}
	}
	
	private AuthorDTO findAuthorByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedMonograph.getAllAuthors()) {
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

	/**
	 * Removes the selected author from the list of authors
	 */
	public void removeAuthor() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) 
			if(selectedMonograph.getOtherAuthors().contains(selectedAuthor))
				selectedMonograph.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedMonograph.getMainAuthor())){
					if(selectedMonograph.getOtherAuthors().size() > 0){
						selectedMonograph.setMainAuthor(selectedMonograph.getOtherAuthors().get(0));
						selectedMonograph.getOtherAuthors().remove(0);
					} else {
						selectedMonograph.setMainAuthor(new AuthorDTO());
					}
				}
	}

	/**
	 * Switches the selected author with previous
	 */
	public void moveAuthorUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedMonograph.getOtherAuthors().size(); i++) {
			authorDTO = selectedMonograph.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedMonograph.getOtherAuthors()
						.size() - 1);
				break;
			default:
				switchAuthors(index, (index - 1));
				break;
		}
	}

	/**
	 * Switches the selected author with next
	 */
	public void moveAuthorDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedMonograph.getOtherAuthors().size(); i++) {
			authorDTO = selectedMonograph.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedMonograph
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedMonograph
				.getMainAuthor() : selectedMonograph.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedMonograph
				.getMainAuthor() : selectedMonograph.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedMonograph.setMainAuthor(second);
		else
			selectedMonograph.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedMonograph.setMainAuthor(first);
		else
			selectedMonograph.getOtherAuthors().set(secondIndex, first);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#setAuthor(rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO)
	 */
	@Override
	public void setAuthor(AuthorDTO author) {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}
		
		if((mb.editMode == ModesManagedBean.MODE_ADD_FORMAT_NAME) && (author.getOtherFormatNames().size() > 0)){
			author.setName(author.getOtherFormatNames().get(author.getOtherFormatNames().size()-1));
		} else if(((mb.editMode == ModesManagedBean.MODE_UPDATE) || (mb.editMode == ModesManagedBean.MODE_NONE)) && (! simpleForm)){
			List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
			otherFormatNames.add(author.getName());
			otherFormatNames.addAll(author.getOtherFormatNames());
			author.setOtherFormatNames(otherFormatNames);
			author.setName(new AuthorNameDTO());
		} else if((simpleForm) && (mb.editMode != ModesManagedBean.MODE_UPDATE)){
			try{
				String[] names = StringUtils.clearDelimiters(authorName, ",;:\"()[]{}+/.!-").split(" ");
				String lastname = names[0].toLowerCase();
				String firstname = (names.length > 1)?names[1].toLowerCase():"skljwm";
				boolean nameFound = false;
				for (AuthorNameDTO authorNameDTO : author.getAllNames()) {
					if(authorNameDTO.getLastname().equalsIgnoreCase(lastname)){
						author.setName(authorNameDTO);
						nameFound = true;
						break;
					} else if (authorNameDTO.getLastname().equalsIgnoreCase(firstname)){
						author.setName(authorNameDTO);
						nameFound = true;
						break;
					}	
				}
				
				if(! nameFound){
					for (AuthorNameDTO authorNameDTO : author.getAllNames()) {
						if(authorNameDTO.getLastname().toLowerCase().startsWith(lastname)){
							author.setName(authorNameDTO);
							nameFound = true;
							break;
						} 
					}
				}
				
				if(! nameFound){
					for (AuthorNameDTO authorNameDTO : author.getAllNames()) {
						if(authorNameDTO.getLastname().toLowerCase().startsWith(firstname)){
							author.setName(authorNameDTO);
							nameFound = true;
							break;
						} 
					}
				}
			} catch (Exception e){
				
			}
		}
		
		if (pickingAuthor) {
			if ((selectedMonograph.getAllAuthors().contains(author))) {
				if (selectedMonograph.getMainAuthor().getControlNumber()
						.equals(author.getControlNumber())){
					if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
						author.setName(selectedMonograph.getMainAuthor().getName());
					} 
					selectedMonograph.setMainAuthor(author);
				}else {
					if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
						author.setName(selectedMonograph.getOtherAuthors().get(selectedMonograph.getOtherAuthors().indexOf(
								author)).getName());
					} 
					selectedMonograph.getOtherAuthors().set(
							selectedMonograph.getOtherAuthors().indexOf(
									author), author);
				}
			} else {
				if (("".equals(selectedMonograph.getMainAuthor()
						.getControlNumber()))
						|| (selectedMonograph.getMainAuthor()
								.getControlNumber() == null))
					selectedMonograph.setMainAuthor(author);
				else
					selectedMonograph.getOtherAuthors().add(author);
			}
		} else {
			if ((selectedMonograph.getEditors().contains(author))) {
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedMonograph.getEditors().get(selectedMonograph.getEditors().indexOf(
							author)).getName());
				} 
				selectedMonograph.getEditors().set(
						selectedMonograph.getEditors().indexOf(author), author);
			}else {
				selectedMonograph.getEditors().add(author);
			}
		}
		populateImportMessages();
		authorName = "";
		pickingAuthor = true;
		setAuthorManageBeanToPick(this);
	}
	
	/**
	 * Prepares web form where user can choose Editor for selected monograph
	 */
	public void pickEditor() {
		
		pickingAuthor = false;
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}

		mb.setIPickAuthorManagedBean(this);
		mb.setSelectedAuthor(new AuthorDTO());
		mb.setPickMessageFirstTab("records.monograph.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.monograph.pickEditorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.monograph.pickEditorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.monograph.pickEditorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.monograph.pickEditorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.monograph.pickEditorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.monograph.pickEditorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(false);

		mb.switchToPickMode();

	}
	
	/**
	 * Adds the other format name to selected editor
	 */
	public void addEditorOtherFormatName() {
		
		pickingAuthor = false;
		
		AuthorDTO selectedAuthor = findEditorByControlNumber();
		if (selectedAuthor != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
					"authorManagedBean");
			if (mb == null) {
				mb = new AuthorManagedBean();
				extCtx.getSessionMap().put("authorManagedBean", mb);
			}
			mb.setEditMode(ModesManagedBean.MODE_ADD_FORMAT_NAME);
			mb.editTabNumber = 0;
			mb.pleaseInstitution = false;
			mb.setIPickAuthorManagedBean(this);
			mb.setSelectedAuthor((AuthorDTO)(recordDAO.getDTO(selectedAuthor.getControlNumber())));
			mb.setFirstnameOtherFormat("");
			mb.setLastnameOtherFormat("");
		}
	}
	
	/**
	 * Adds the otherName to selected author
	 */
	public void editEditorOtherName() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
					"authorManagedBean");
			if (mb == null) {
				mb = new AuthorManagedBean();
				extCtx.getSessionMap().put("authorManagedBean", mb);
			}
			mb.setEditMode(ModesManagedBean.MODE_UPDATE);
			mb.editTabNumber = 0;
			mb.pleaseInstitution = false;
			mb.setSelectedAuthor((AuthorDTO)(personDAO.getDTO(selectedAuthor.getControlNumber())));
			mb.setFirstnameOtherFormat("");
			mb.setLastnameOtherFormat("");
		}
	}
	
	private AuthorDTO findEditorByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedMonograph.getEditors()) {
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

	/**
	 * Removes the selected editor from the list of editors
	 */
	public void removeEditor() {
		AuthorDTO selectedAuthor = findEditorByControlNumber();
		if (selectedAuthor != null) 
			selectedMonograph.getEditors().remove(selectedAuthor);
	}

	/**
	 * Switches the selected editor with previous
	 */
	public void moveEditorUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedMonograph.getEditors().size(); i++) {
			authorDTO = selectedMonograph.getEditors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchEditors(index, (index - 1)
						% selectedMonograph.getEditors().size());
				break;
		}
	}

	/**
	 * Switches the selected editor with next
	 */
	public void moveEditorDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedMonograph.getEditors().size(); i++) {
			authorDTO = selectedMonograph.getEditors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchEditors(index, (index + 1)
						% selectedMonograph.getEditors().size());
				break;
		}
	}

	private void switchEditors(int firstIndex, int secondIndex) {
		AuthorDTO first = selectedMonograph.getEditors().get(firstIndex);
		AuthorDTO second = selectedMonograph.getEditors().get(secondIndex);
		selectedMonograph.getEditors().set(firstIndex, second);
		selectedMonograph.getEditors().set(secondIndex, first);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}

	private String language;

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Adds language to the list of monograph languages
	 */
	public void addLanguage() {
		if ((!("".equals(language))) && (!(selectedMonograph.getLanguages().contains(language))))
			selectedMonograph.getLanguages().add(language);
	}

	/**
	 * Removes language from the list of monograph languages
	 */
	public void removeLanguage() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String paramLanguage = facesCtx.getExternalContext()
				.getRequestParameterMap().get("language");

		int index = -1;
		String tempLanguage = null;
		for (int i = 0; i < selectedMonograph.getLocalizedLanguages().size(); i++) {
			tempLanguage = selectedMonograph.getLocalizedLanguages().get(i);
			if (tempLanguage.equals(paramLanguage)) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedMonograph.getLanguages().remove(index);
	}
	
	private String originalLanguage;

	/**
	 * @return the originalLanguage
	 */
	public String getOriginalLanguage() {
		return originalLanguage;
	}

	/**
	 * @param originalLanguage
	 *            the originalLanguage to set
	 */
	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	/**
	 * Adds language to the list of monograph original languages
	 */
	public void addOriginalLanguage() {
		if ((!("".equals(originalLanguage))) && (!(selectedMonograph.getOriginalLanguages().contains(originalLanguage))))
			selectedMonograph.getOriginalLanguages().add(originalLanguage);
	}

	/**
	 * Removes language from the list of monograph original languages
	 */
	public void removeOriginalLanguage() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String paramLanguage = facesCtx.getExternalContext()
				.getRequestParameterMap().get("language");

		int index = -1;
		String tempLanguage = null;
		for (int i = 0; i < selectedMonograph.getLocalizedLanguages().size(); i++) {
			tempLanguage = selectedMonograph.getLocalizedLanguages().get(i);
			if (tempLanguage.equals(paramLanguage)) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedMonograph.getOriginalLanguages().remove(index);
	}


	/**
	 * Sets chosen monograph to the CRUDManagedBean which wanted to pick
	 * monograph
	 */
	public void chooseMonograph() {
		try {
			if(tableTabNumber != 0) 
				selectedMonograph = findMonographByControlNumber(list);
			if (selectedMonograph != null) {
				iPickMonographManagedBean.setMonograph(selectedMonograph);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseMonograph", e);
		}
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedMonograph = findMonographByControlNumber(similarMonographs);
			if ((selectedMonograph != null) && (iPickMonographManagedBean != null)){
				iPickMonographManagedBean.setMonograph(selectedMonograph);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarMonograph", e);
		}
	}

	private IPickMonographManagedBean iPickMonographManagedBean = null;

	/**
	 * @param iPickMonographManagedBean
	 *            the CRUDManagedBean which wants to pick monograph
	 */
	public void setIPickMonographManagedBean(
			IPickMonographManagedBean iPickMonographManagedBean) {
		this.iPickMonographManagedBean = iPickMonographManagedBean;
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

	
	private String pickMessageFirstTab;

	/**
	 * @return the pick message for first tab
	 */
	public String getPickMessageFirstTab() {
		return facesMessages.getMessageFromResourceBundle(pickMessageFirstTab);
	}

	/**
	 * @param pickMessageFirstTab
	 *            the pick message for first tab to set
	 */
	public void setPickMessageFirstTab(String pickMessageFirstTab) {
		this.pickMessageFirstTab = pickMessageFirstTab;
	}
	
	private String pickMessageSecondTabSimilarNotExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR monographs do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR monographs do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR monographs exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR monographs exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR monographs exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR monographs exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR monographs exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFourthSentence(String pickMessageSecondTabSimilarExistFourthSentence) {
		this.pickMessageSecondTabSimilarExistFourthSentence = pickMessageSecondTabSimilarExistFourthSentence;
	}


	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "monographPage";
		return retVal;
	}

	private String monographControlNumber;
	
	/**
	 * @return the monographControlNumber
	 */
	public String getMonographControlNumber() {
		return monographControlNumber;
	}

	/**
	 * @param monographControlNumber the monographControlNumber to set
	 */
	public void setMonographControlNumber(String monographControlNumber) {
		this.monographControlNumber = monographControlNumber;
		selectedMonograph = (MonographDTO)recordDAO.getDTO(monographControlNumber);
	}
	
	private MonographDTO findMonographByControlNumber(List<MonographDTO> monographList) {
		MonographDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (MonographDTO dto : monographList) {
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

	private String title;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#nextTableTab()
	 */
	@Override
	public void nextTableTab() {
		if (tableTabNumber == 0)
			whereStr = ((title != null) ? (title) : (""));
		super.nextTableTab();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#nextEditTab()
	 */
	@Override
	public void nextEditTab() {
		if (editTabNumber == 1) {
			if (validateEditors() == false) {
				facesMessages.addToControlFromResourceBundle(
						"monographEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.monograph.editor.name.pleaseSelect.error",
						FacesContext.getCurrentInstance());
				return;
			}
			if (validateAuthors() == false) {
				facesMessages.addToControlFromResourceBundle(
						"authorsMonographEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.monograph.author.name.pleaseSelect.error",
						FacesContext.getCurrentInstance());
				return;
			}
		}
		editTabNumber++;
		if((editTabNumber >= 0) && (editTabNumber <= 3) && editMode!=ModesManagedBean.MODE_ADD_MONOGRAPH_EVALUATION_DATA)
			populateImportMessages();
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
		if((editTabNumber >= 0) && (editTabNumber <= 3))
			populateImportMessages();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)
				&& (customPick) && (editTabNumber == 4))) {
			iPickMonographManagedBean.setMonograph(selectedMonograph);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}

	private boolean validateAuthors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedMonograph.getAllAuthors()) {
			if (("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	
	private boolean validateEditors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedMonograph.getEditors()) {
			if ((authorDTO.getName() == null)
					|| ("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	
	private boolean validateLoggedAuthor() {
		boolean retVal = true;
		if(getUserManagedBean().getLoggedUser().getAuthor().getControlNumber()!=null){
			if ((! selectedMonograph.getAllAuthors().contains(getUserManagedBean().getLoggedUser().getAuthor())) && (! selectedMonograph.getEditors().contains(getUserManagedBean().getLoggedUser().getAuthor()))) {
					retVal = false;
			}
		} else if((selectedMonograph.getAllAuthors().size() == 0) && (selectedMonograph.getEditors().size() == 0))
				retVal = false;
		return retVal;
	}
	
	private boolean validateAll(){
		boolean retVal = true;
		if ((selectedMonograph.getTitle() == null) || (selectedMonograph.getTitle().getLanguage() == null) || (selectedMonograph.getTitle().getLanguage().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"monographEditForm:language", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedMonograph.getSomeTitle() == null) || (selectedMonograph.getSomeTitle().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"monographSimpleEditForm:title", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedMonograph.getPublicationYear() == null) || (selectedMonograph.getPublicationYear().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"monographSimpleEditForm:publicationYear", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if(tableMode != ModesManagedBean.MODE_PICK){
			if (validateLoggedAuthor() == false) {
				facesMessages.addToControlFromResourceBundle(
						"monographSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.monograph.author.name.noLoggedAuthor.error",
						FacesContext.getCurrentInstance());
				retVal = false;
			}
		}
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"monographSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.monograph.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#setCustomPick(boolean)
	 */
	@Override
	public void setCustomPick(boolean customPick) {
		super.setCustomPick(customPick);
		if (!customPick) {
			orderBy = "someTitle";
			direction = "asc";
		}
	}
	
	public void titleTranslations(){
		this.openMultilingualContentForm(editMode, selectedMonograph.getTitleTranslations(), false, "records.monograph.editPanel.titleTranslations.panelHeader", "records.monograph.editPanel.titleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedMonograph.getSubtitleTranslations(), false, "records.monograph.editPanel.subtitleTranslations.panelHeader", "records.monograph.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedMonograph.getNoteTranslations(), false, "records.monograph.editPanel.noteTranslations.panelHeader", "records.monograph.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedMonograph.getKeywordsTranslations(), false, "records.monograph.editPanel.keywordsTranslations.panelHeader", "records.monograph.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedMonograph.getAbstractTranslations(), false, "records.monograph.editPanel.abstractTranslations.panelHeader", "records.monograph.editPanel.abstractTranslations.contentHeader");
	}
	
	public void publisherTranslations(){
		this.openMultilingualContentPublisherForm(editMode, selectedMonograph.getPublisher().getPublisherTranslations(), "records.monograph.editPanel.publisherTranslations.panelHeader");
	}

	/**
	 * @return the selectedMonographEvaluationData
	 */
	public MonographEvaluationDataDTO getSelectedMonographEvaluationData() {
		return selectedMonographEvaluationData;
	}

	/**
	 * @param selectedMonographEvaluationData the selectedMonographEvaluationData to set
	 */
	public void setSelectedMonographEvaluationData(
			MonographEvaluationDataDTO selectedMonographEvaluationData) {
		this.selectedMonographEvaluationData = selectedMonographEvaluationData;
	}
	
	public void uploadListener(FileUploadEvent event) {
  try{
   FileDTO fileDTO = new FileDTO();
   UploadedFile item = event.getFile();
 	 fileDTO.setData(item.getContent());
 	 fileDTO.setLength(item.getContent().length);
   fileDTO.setFileName((item.getFileName().lastIndexOf("\\") != -1)?item.getFileName().substring(item.getFileName().lastIndexOf("\\")+1):item.getFileName());
   fileDTO.setControlNumber(selectedMonograph.getControlNumber());
   fileDTO.setUploader(getUserManagedBean().getLoggedUser().getEmail());
   selectedMonographEvaluationData.getAttachedFiles().add(fileDTO);
  } catch (Exception e) {
  	log.error("Unable to upload file");
  	log.error(e.getStackTrace());
  	e.printStackTrace();
  }
	}
	
	public void deleteAttachedFile(FileDTO file){		
		boolean deleted = new EvaluationDataDAO(new EvaluationDataDB()).deleteAttachedFile(file);
		if(deleted)
			selectedMonographEvaluationData.getAttachedFiles().remove(file);	
	}
  
  public String getFileURL(FileDTO file){
  	String filePath = "";
			  if(FacesContext.getCurrentInstance()!=null){
				  try {
					  filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				  } catch (Throwable e){
				  }
			  }
			return filePath + "/DownloadFileServlet/" + file.getFileName() + "?controlNumber=" 
							+ file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId();
  }


  /**
	 *  Sends email notification to authors for requesting evaluation data
	 */
	protected void requestEvaluationDataViaEmail(RecordDTO record, String from){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		MonographDTO monograph = (MonographDTO)record;
		StringBuffer emails = new StringBuffer("");
		for (AuthorDTO author : monograph.getAllAuthors()) {
			if((author.getEmail() != null) && (! author.getEmail().equals("proba1"))){
				if(! emails.toString().contains(author.getEmail()))
					emails.append(author.getEmail()+" ");
			}
		}
		if(emails.toString().trim().length() != 0)
			sendMessage(new EmailMessage(from, emails.toString(), null, "surla@uns.ac.rs chenejac@uns.ac.rs", rbAdministration.getString("administration.notification.evaluation.monograph.requestinformation.subject"),  rbAdministration.getString("administration.notification.evaluation.monograph.requestinformation.header") + monograph.getControlNumber() + "| " + monograph.toString() + rbAdministration.getString("administration.notification.evaluation.monograph.requestinformation.body") + rbAdministration.getString("administration.notification.evaluation.monograph.requestinformation.footer")));
	}
	
	public void sendMonographEvaluationDMIEmails(){
		BooleanQuery allMonographsQuery = new BooleanQuery();
		Query type = new TermQuery(new Term("TYPE", Types.MONOGRAPH));
		allMonographsQuery.add(type, Occur.MUST);
		List<Record> listMonographs = recordDAO.getDTOs(allMonographsQuery, new AllDocCollector(false));
		List<RecordDTO> listMonographDTOs = new ArrayList<RecordDTO>();
		for (Record recordDTO : listMonographs) {
			try {
				String monographClassification = null;
				recordDTO.loadFromDatabase();
				MonographDTO monographDTO = (MonographDTO) recordDTO.getDto();
				System.out.println(monographDTO);
				for (Classification classification : recordDTO.getRecordClasses()) {
					if(
							((!("0".equals(classification.getCommissionId().toString()))))
					&& ((classification.getCfClassSchemeId().equals("type")) || (classification.getCfClassSchemeId().equals("resultType")))) 
					 {
						monographClassification = classification.getCfClassId();
					}
				}
				if ((monographClassification == null) && (monographDTO != null)) {
					for (AuthorDTO authorDTO : monographDTO.getAllAuthors()) {
						if((authorDTO.getOrganizationUnit().getControlNumber() != null) && ((authorDTO.getOrganizationUnit().getControlNumber().equals("(BISIS)6782")) || ((authorDTO.getOrganizationUnit().getSuperOrganizationUnit() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber().equals("(BISIS)6782"))))){
							listMonographDTOs.add(monographDTO);
							break;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (RecordDTO recordDTO : listMonographDTOs) {
			requestEvaluationDataViaEmail(recordDTO, "komisija.cris@dmi.uns.ac.rs");
		}
					
	}
	
	
}
