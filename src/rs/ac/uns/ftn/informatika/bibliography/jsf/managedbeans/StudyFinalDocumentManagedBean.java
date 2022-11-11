package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.FileDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationSelectionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for study final document
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class StudyFinalDocumentManagedBean extends CRUDManagedBean 
		implements IPickAuthorManagedBean, IPickInstitutionManagedBean, IPickResearchAreaManagedBean{

	List<StudyFinalDocumentDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
//	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private RegisterEntryDAO registerEntryDAO = new RegisterEntryDAO(new RegisterEntryDB());

	private StudyFinalDocumentDTO selectedStudyFinalDocument = null;
	
	private AuthorDTO selectedAuthor = new AuthorDTO();
	
	private List<InstitutionDTO> institutions;
	
	private boolean hiddenDissertations = false;
	
	/**
	 * 
	 */
	public StudyFinalDocumentManagedBean() {
		super();
		pickSimilarMessage = "records.studyFinalDocument.pickSimilarMessage";
		tableModalPanel = "studyFinalDocumentBrowseModalPanel";
		editModalPanel = "studyFinalDocumentEditModalPanel";
		
		allTitlesSelectItems = new ArrayList<SelectItem>();
		
		allTitlesSelectItems.add(new SelectItem("др", "др"));
		allTitlesSelectItems.add(new SelectItem("академик", "академик"));
		allTitlesSelectItems.add(new SelectItem("др ум.", "др ум."));
		allTitlesSelectItems.add(new SelectItem("мр", "мр"));
		allTitlesSelectItems.add(new SelectItem("без титуле", ""));        
		
		allPositionsSelectItems = new ArrayList<SelectItem>();
		
		allPositionsSelectItems.add(new SelectItem("Redovni profesor", "ред. проф."));
		allPositionsSelectItems.add(new SelectItem("Vаnredni profesor", "ванр. проф."));
		allPositionsSelectItems.add(new SelectItem("Docent", "доцент"));
		allPositionsSelectItems.add(new SelectItem("Profesor emeritus", "проф. емеритус"));
		allPositionsSelectItems.add(new SelectItem("Naučni - saradnik", "науч. сар."));
		allPositionsSelectItems.add(new SelectItem("Viši naučni - saradnik", "виши науч. сар."));
		allPositionsSelectItems.add(new SelectItem("Naučni savetnik", "науч. сав."));
		allPositionsSelectItems.add(new SelectItem("Profesor u penziji", "проф. у пензији"));
		allPositionsSelectItems.add(new SelectItem("prof. inž. habil", "проф. инж. хабил"));
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		populatePublicationsList = true;
		listPublications = null;
		selectedPublication = null;
		selectedStudyFinalDocument = null;
		selectedAuthor = null;
		tableModalPanel = "studyFinalDocumentBrowseModalPanel";
		editModalPanel = "studyFinalDocumentEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<StudyFinalDocumentDTO> listTmp = (hiddenDissertations)?getHiddenDissertationsList():getStudyFinalDocuments(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedStudyFinalDocument != null
					&& selectedStudyFinalDocument.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedStudyFinalDocument.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("studyFinalDocumentTable");
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
			list = new ArrayList<StudyFinalDocumentDTO>();
		}
	}
	
	/**
	 * Retrieves a list of study final document that correspond to
	 * the query.
	 * 
	 * @param query
	 *            query for retrieving study final documents
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of study final documents that correspond to the
	 *         query
	 */
	@SuppressWarnings("unused")
	private List<StudyFinalDocumentDTO> getStudyFinalDocuments(String query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getStudyFinalDocuments(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of study final documents that correspond to
	 * the query.
	 * 
	 * @param query
	 *            query for retrieving study final documents
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of study final documents that correspond to the
	 *         query
	 */
	private List<StudyFinalDocumentDTO> getStudyFinalDocuments(Query query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				StudyFinalDocumentDTO dto = (StudyFinalDocumentDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<StudyFinalDocumentDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of study final documents (filtered and ordered
	 *         by ...)
	 */
	public List<StudyFinalDocumentDTO> getStudyFinalDocuments() {
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
			Collections.sort(list, new GenericComparator<StudyFinalDocumentDTO>(
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
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
		if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() == null) 
//				|| 
//				(! ("(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())))
				){
			type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		}
		bq.add(type, Occur.MUST);
		if(institutions == null){
			fillInstitutins();
		}
		BooleanQuery institutionQuery = new BooleanQuery();
		for (InstitutionDTO institution : institutions) {
			institutionQuery.add(new TermQuery(new Term("INCN", institution.getControlNumber())), Occur.SHOULD);
		}
		if(institutions.size() != 0)
			bq.add(institutionQuery, Occur.MUST);
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser()
				.getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			bq.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.MUST);
		}
		if (selectedAuthor != null)
			bq.add(new TermQuery(new Term("AUCN", selectedAuthor.getControlNumber())), Occur.MUST);
		else if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
			
		return bq;
	}
	
	private void fillInstitutins() {
		institutions = new ArrayList<InstitutionDTO>();
		if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() != null)){
			InstitutionDTO userInstitution = getUserManagedBean().getLoggedUser().getInstitution();
			institutions.add(userInstitution);
			for (RecordDTO record : userInstitution.getRelatedRecords()) {
				if((!(record instanceof OrganizationUnitDTO)) && (record instanceof InstitutionDTO)){
					institutions.add((InstitutionDTO) record);
				}
			}
		}
	}

	List<StudyFinalDocumentDTO> similarStudyFinalDocuments = null;
	
	/**
	 * @return the list of SIMILAR study final documents with selected study final document
	 */
	public List<StudyFinalDocumentDTO> getSimilarStudyFinalDocuments() {
		return similarStudyFinalDocuments;
	}

	/**
	 * Creates query for finding SIMILAR study final documents with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarStudyFinalDocumentsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedStudyFinalDocument.getTitle().getContent()!=null)
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedStudyFinalDocument.getTitle().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		else if(selectedStudyFinalDocument.getTitleTranslations().size() > 0)
			bq.add(QueryUtils.makeBooleanQuery("TI",selectedStudyFinalDocument.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected study final document
	 */
	public StudyFinalDocumentDTO getSelectedStudyFinalDocument() {
		return selectedStudyFinalDocument;
	}

	/**
	 * @param studyFinalDocument
	 *            the study final document to set as selected study final document
	 */
	public void setSelectedStudyFinalDocument(
			StudyFinalDocumentDTO studyFinalDocument) {
		selectedStudyFinalDocument = studyFinalDocument;
	}

	/**
	 * @return the selectedAuthor
	 */
	public AuthorDTO getSelectedAuthor() {
		return selectedAuthor;
	}

	/**
	 * @param selectedAuthor the selectedAuthor to set
	 */
	public void setSelectedAuthor(AuthorDTO selectedAuthor) {
		this.selectedAuthor = selectedAuthor;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		editModalPanel = "studyFinalDocumentEditModalPanel";
		mergeStudyFinalDocument = null;
		if (tableMode == ModesManagedBean.MODE_NONE){
			super.switchToUpdateMode();
			if(selectedStudyFinalDocument != null){
				advisorsList = new HashMap<String, Boolean>();
				for (AuthorDTO advisor : selectedStudyFinalDocument.getAdvisors()) {
					if(selectedStudyFinalDocument.getCommitteeMembers().contains(advisor)){
						advisorsList.put(advisor.getControlNumber(), new Boolean(true));
						selectedStudyFinalDocument.getCommitteeMembers().set(selectedStudyFinalDocument.getCommitteeMembers().indexOf(advisor), advisor);
					} else
						advisorsList.put(advisor.getControlNumber(), new Boolean(false));
				}
				removedAuthors = new ArrayList<AuthorDTO>();
				publicationDate = null;
				acceptedOn = null;
				defendedOn = null;
				if (selectedStudyFinalDocument != null) {
					if(selectedStudyFinalDocument.getPublicationDate() != null)
						publicationDate = selectedStudyFinalDocument.getPublicationDate().getTime();
					if(selectedStudyFinalDocument.getAcceptedOn() != null)
						acceptedOn = selectedStudyFinalDocument.getAcceptedOn().getTime();
					if(selectedStudyFinalDocument.getDefendedOn() != null)
						defendedOn = selectedStudyFinalDocument.getDefendedOn().getTime();
					super.switchToUpdateMode();
					debug("switchToUpdateMode: \n" + selectedStudyFinalDocument);
				}
			}
		} else {
			selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
			advisorsList = new HashMap<String, Boolean>();
			for (AuthorDTO advisor : selectedStudyFinalDocument.getAdvisors()) {
				if(selectedStudyFinalDocument.getCommitteeMembers().contains(advisor)){
					advisorsList.put(advisor.getControlNumber(), new Boolean(true));
					selectedStudyFinalDocument.getCommitteeMembers().set(selectedStudyFinalDocument.getCommitteeMembers().indexOf(advisor), advisor);
				} else
					advisorsList.put(advisor.getControlNumber(), new Boolean(false));
			}
			removedAuthors = new ArrayList<AuthorDTO>();
			publicationDate = null;
			acceptedOn = null;
			defendedOn = null;
			if (selectedStudyFinalDocument != null) {
				if(selectedStudyFinalDocument.getPublicationDate() != null)
					publicationDate = selectedStudyFinalDocument.getPublicationDate().getTime();
				if(selectedStudyFinalDocument.getAcceptedOn() != null)
					acceptedOn = selectedStudyFinalDocument.getAcceptedOn().getTime();
				if(selectedStudyFinalDocument.getDefendedOn() != null)
					defendedOn = selectedStudyFinalDocument.getDefendedOn().getTime();
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedStudyFinalDocument);
			}
		}
		if(! getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA)){
			editTabNumber = 1;
		}
				
		setInstitutionManageBeanToPick();

	}
	
	/**
	 * 
	 */
	public void switchToUpdateRegisterEntryMode() {
		editModalPanel = "registerEntryEditModalPanel";
		setEditMode(ModesManagedBean.MODE_UPDATE_REGISTER_ENTRY);
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		selectedStudyFinalDocument.loadRegisterEntry(true);
		birthDate = null;
		defendedOn = null;
		previouslyDefendedOn = null;
		promotionDate = null;
		diplomaPublicationDate = null;
		supplementPublicationDate = null;
		if(selectedStudyFinalDocument.getRegisterEntry().getBirthDate() != null)
			birthDate = selectedStudyFinalDocument.getRegisterEntry().getBirthDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDefendedOn() != null)
			defendedOn = selectedStudyFinalDocument.getRegisterEntry().getDefendedOn().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld() != null)
			previouslyDefendedOn = selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPromotionDate() != null)
			promotionDate = selectedStudyFinalDocument.getRegisterEntry().getPromotionDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate() != null)
			diplomaPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate() != null)
			supplementPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate().getTime();
		editTabNumber = 0;
	}
	
	/**
	 * 
	 */
	public void switchToUpdateRegisterEntryMode(StudyFinalDocumentDTO dto) {
		editModalPanel = "registerEntryEditModalPanel";
		setEditMode(ModesManagedBean.MODE_UPDATE_REGISTER_ENTRY);
		selectedStudyFinalDocument = dto;
		selectedStudyFinalDocument.loadRegisterEntry(true);
		birthDate = null;
		defendedOn = null;
		previouslyDefendedOn = null;
		promotionDate = null;
		diplomaPublicationDate = null;
		supplementPublicationDate = null;
		if(selectedStudyFinalDocument.getRegisterEntry().getBirthDate() != null)
			birthDate = selectedStudyFinalDocument.getRegisterEntry().getBirthDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDefendedOn() != null)
			defendedOn = selectedStudyFinalDocument.getRegisterEntry().getDefendedOn().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld() != null)
			previouslyDefendedOn = selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPromotionDate() != null)
			promotionDate = selectedStudyFinalDocument.getRegisterEntry().getPromotionDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate() != null)
			diplomaPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate() != null)
			supplementPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate().getTime();
		editTabNumber = 0;
	}
	
	/**
	 * 
	 */
	public void switchToAddRegisterEntryMode() {
		editModalPanel = "registerEntryEditModalPanel";
		setEditMode(ModesManagedBean.MODE_ADD_REGISTER_ENTRY);
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		selectedStudyFinalDocument.loadRegisterEntry(true);
		birthDate = null;
		defendedOn = null;
		previouslyDefendedOn = null;
		promotionDate = null;
		diplomaPublicationDate = null;
		supplementPublicationDate = null;
		if(selectedStudyFinalDocument.getRegisterEntry().getBirthDate() != null)
			birthDate = selectedStudyFinalDocument.getRegisterEntry().getBirthDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDefendedOn() != null)
			defendedOn = selectedStudyFinalDocument.getRegisterEntry().getDefendedOn().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld() != null)
			previouslyDefendedOn = selectedStudyFinalDocument.getRegisterEntry().getPreviouslyNameOfAuthorDegreeDateOld().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getPromotionDate() != null)
			promotionDate = selectedStudyFinalDocument.getRegisterEntry().getPromotionDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate() != null)
			diplomaPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getDiplomaPublicationDate().getTime();
		if(selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate() != null)
			supplementPublicationDate = selectedStudyFinalDocument.getRegisterEntry().getSupplementPublicationDate().getTime();
		editTabNumber = 0;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		editModalPanel = "studyFinalDocumentEditModalPanel";
		mergeStudyFinalDocument = null;
		super.switchToAddMode();
		selectedStudyFinalDocument = new StudyFinalDocumentDTO();
		removedAuthors = new ArrayList<AuthorDTO>();
		advisorsList = new HashMap<String, Boolean>();
		publicationDate = new Date();
		acceptedOn = null;
		defendedOn = null;
		AuthorDTO author = null;
		if ((getUserManagedBean().getLoggedUser() != null)
				&& (getUserManagedBean().getLoggedUser().getAuthor() != null)
				&& (getUserManagedBean().getLoggedUser().getAuthor()
						.getControlNumber() != null)
				&& (!("".equals(getUserManagedBean().getLoggedUser()
						.getAuthor().getControlNumber())))) {
			author = getUserManagedBean().getLoggedUser().getAuthor();
		} 
		if(selectedAuthor != null)
			author = selectedAuthor;
		if(author!=null){
			List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
			otherFormatNames.add(author.getName());
			otherFormatNames.addAll(author.getOtherFormatNames());
			author.setOtherFormatNames(otherFormatNames);
			author.setName(new AuthorNameDTO());
			selectedStudyFinalDocument.setAuthor(author);
		}
		if(institutions == null){
			fillInstitutins();
		}
		if(institutions.size() != 0){
			selectedStudyFinalDocument.setInstitution(institutions.get(0));
		}
		
		if(! getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA)){
			editTabNumber = 1;
		}
		
		setInstitutionManageBeanToPick();
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		editModalPanel = "studyFinalDocumentEditModalPanel";
		mergeStudyFinalDocument = null;
		if (tableMode == ModesManagedBean.MODE_NONE){
			super.switchToUpdateMode();
		} else {
			selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		}
		publicationDate = null;
		acceptedOn = null;
		defendedOn = null;
		if (selectedStudyFinalDocument != null) {
			if(selectedStudyFinalDocument.getPublicationDate() != null)
				publicationDate = selectedStudyFinalDocument.getPublicationDate().getTime();
			if(selectedStudyFinalDocument.getAcceptedOn() != null)
				acceptedOn = selectedStudyFinalDocument.getAcceptedOn().getTime();
			if(selectedStudyFinalDocument.getDefendedOn() != null)
				defendedOn = selectedStudyFinalDocument.getDefendedOn().getTime();
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedStudyFinalDocument);
		}
		
		if(! getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA)){
			editTabNumber = 1;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		editModalPanel = "studyFinalDocumentEditModalPanel";
		try {
			debug("findSimilarStudyFinalDocuments");
			similarStudyFinalDocuments = getStudyFinalDocuments(createSimilarStudyFinalDocumentsQuery(),
					null, null,  new AllDocCollector(true));
			mergeStudyFinalDocument = null;
			editTabNumber = 4;
			if (selectedStudyFinalDocument != null) {
				if(selectedStudyFinalDocument.getPublicationDate() != null)
					publicationDate = selectedStudyFinalDocument.getPublicationDate().getTime();
				if(selectedStudyFinalDocument.getAcceptedOn() != null)
					acceptedOn = selectedStudyFinalDocument.getAcceptedOn().getTime();
				if(selectedStudyFinalDocument.getDefendedOn() != null)
					defendedOn = selectedStudyFinalDocument.getDefendedOn().getTime();
			}
			super.switchToImportMode();
		} catch (ParseException e) {
			error("findSimilarStudyFinalDocuments", e);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		super.switchToBrowseMode();
		orderBy = "publicationYear";
		direction = "asc";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedStudyFinalDocument.setNotLoaded(true);
		if((selectedAuthor != null) && (selectedAuthor.getControlNumber()!=null))
			selectedAuthor.setNotLoaded(true);
		super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergeStudyFinalDocument = selectedStudyFinalDocument;
		
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(similarStudyFinalDocuments);
		if(selectedStudyFinalDocument != null){
			if(selectedStudyFinalDocument.getFile() == null){
				selectedStudyFinalDocument.setFile(mergeStudyFinalDocument.getFile());
			}
			if("".equals(selectedStudyFinalDocument.getResearchArea().getSomeTerm()))
				selectedStudyFinalDocument.setResearchArea(mergeStudyFinalDocument.getResearchArea());
			if(selectedStudyFinalDocument.getAdvisors().size() == 0)
				selectedStudyFinalDocument.setAdvisors(mergeStudyFinalDocument.getAdvisors());
			if(selectedStudyFinalDocument.getCommitteeMembers().size() == 0)
				selectedStudyFinalDocument.setCommitteeMembers(mergeStudyFinalDocument.getCommitteeMembers());
			if(selectedStudyFinalDocument.getHoldingData() == null)
				selectedStudyFinalDocument.setHoldingData(mergeStudyFinalDocument.getHoldingData());
			if(("-1, -1, -1, -1, -1, -1, -1".equals(selectedStudyFinalDocument.getPhysicalDescription().toString())) || ("null, null, null, null, null, null, null".equals(selectedStudyFinalDocument.getPhysicalDescription().toString()))){
				selectedStudyFinalDocument.setPhysicalDescription(mergeStudyFinalDocument.getPhysicalDescription());
			}
			if(selectedStudyFinalDocument.getTitleTranslations().size() == 0)
				selectedStudyFinalDocument.setTitleTranslations(mergeStudyFinalDocument.getTitleTranslations());
			if(selectedStudyFinalDocument.getPublicationDate() == null)
				publicationDate = mergeStudyFinalDocument.getPublicationDate().getTime();
			if(selectedStudyFinalDocument.getAcceptedOn() == null)
				acceptedOn = mergeStudyFinalDocument.getAcceptedOn().getTime();
			if(selectedStudyFinalDocument.getDefendedOn() == null)
				defendedOn = mergeStudyFinalDocument.getDefendedOn().getTime();
		} else {
			selectedStudyFinalDocument = mergeStudyFinalDocument;
			mergeStudyFinalDocument = null;
		}
		editTabNumber = 1;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeStudyFinalDocument = findStudyFinalDocumentByControlNumber(similarStudyFinalDocuments);
		if(mergeStudyFinalDocument != null){
			selectedStudyFinalDocument.setControlNumber(mergeStudyFinalDocument.getControlNumber());
			if(selectedStudyFinalDocument.getFile() == null){
				selectedStudyFinalDocument.setFile(mergeStudyFinalDocument.getFile());
			} 
			editTabNumber = 1;
			populateImportMessages();
			if(editMode == ModesManagedBean.MODE_ADD)
				editMode = ModesManagedBean.MODE_UPDATE;
		}
	}
	
	private StudyFinalDocumentDTO mergeStudyFinalDocument;
	
	public void populateImportMessages(){
		if(mergeStudyFinalDocument != null){
			if(editTabNumber == 1){
				if((mergeStudyFinalDocument.getAuthor().getName() != null) && (mergeStudyFinalDocument.getAuthor().getName().toString().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:author", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getAuthor().getName().toString(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getStudyType() != null) && (mergeStudyFinalDocument.getStudyType().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:studyType", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getLocalizedStudyType(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getLanguage() != null) && (mergeStudyFinalDocument.getLanguage().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:language", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getLanguage(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getTitle().getContent() != null) && (mergeStudyFinalDocument.getTitle().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:title", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getTitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getSubtitle().getContent() != null) && (mergeStudyFinalDocument.getSubtitle().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:subtitle", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getSubtitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getAlternativeTitle().getContent() != null) && (mergeStudyFinalDocument.getAlternativeTitle().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:alternativeTitle", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getAlternativeTitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getKeywords().getContent() != null) && (mergeStudyFinalDocument.getKeywords().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:keywords", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getKeywords().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getNote().getContent() != null) && (mergeStudyFinalDocument.getNote().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:note", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getNote().getContent(), FacesContext
								.getCurrentInstance());
				if(mergeStudyFinalDocument.getPhysicalDescription() != null) 
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:physicalDescription", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getPhysicalDescription().toString(), FacesContext
								.getCurrentInstance());
			} 
			if (editTabNumber == 2){
				if((mergeStudyFinalDocument.getInstitution().getControlNumber() != null) && (mergeStudyFinalDocument.getInstitution().getControlNumber().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:institution", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getInstitution().toString(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getResearchArea().getTerm().getContent() != null) && (mergeStudyFinalDocument.getResearchArea().getTerm().getContent().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:researchArea", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getResearchArea().getTerm().getContent(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getRegisterEntry().getNameOfAuthorDegree() != null) && (mergeStudyFinalDocument.getRegisterEntry().getNameOfAuthorDegree().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:nameOfAuthorDegree", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getRegisterEntry().getNameOfAuthorDegree(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getLevelOfEducation() != null) && (mergeStudyFinalDocument.getLevelOfEducation().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:levelOfEducation", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getLevelOfEducation(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getHoldingData() != null) && (mergeStudyFinalDocument.getHoldingData().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:holdingData", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getHoldingData(), FacesContext
								.getCurrentInstance());
				if(mergeStudyFinalDocument.getAcceptedOn() != null){
					String dateString = null;
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = mergeStudyFinalDocument.getAcceptedOn().getTime();
					if(date != null){
						dateString = formatter.format(date);
						facesMessages.addToControl(
								"studyFinalDocumentEditForm:acceptedOn", FacesMessage.SEVERITY_INFO, 
								dateString, FacesContext
									.getCurrentInstance());
					}
				}
				if(mergeStudyFinalDocument.getDefendedOn() != null){
					String dateString = null;
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = mergeStudyFinalDocument.getDefendedOn().getTime();
					if(date != null){
						dateString = formatter.format(date);
						facesMessages.addToControl(
								"studyFinalDocumentEditForm:defendedOn", FacesMessage.SEVERITY_INFO, 
								dateString, FacesContext
									.getCurrentInstance());
					}
				}
				if((mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getName() != null) && (mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getName().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:publisherName", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getName(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getPlace() != null) && (mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getPlace().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:publisherPlace", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getPlace(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getState() != null) && (mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getState().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:publisherState", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getPublisher().getOriginalPublisher().getState(), FacesContext
								.getCurrentInstance());
				if(mergeStudyFinalDocument.getPublicationDate() != null){
					String dateString = null;
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date date = mergeStudyFinalDocument.getPublicationDate().getTime();
					if(date != null){
						dateString = formatter.format(date);
						facesMessages.addToControl(
								"studyFinalDocumentEditForm:publicationDate", FacesMessage.SEVERITY_INFO, 
								dateString, FacesContext
									.getCurrentInstance());
					}
				}
				if((mergeStudyFinalDocument.getIsbn() != null) && (mergeStudyFinalDocument.getIsbn().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:isbn", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getIsbn(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getUdc() != null) && (mergeStudyFinalDocument.getUdc().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:udc", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getUdc(), FacesContext
								.getCurrentInstance());
				if((mergeStudyFinalDocument.getUri() != null) && (mergeStudyFinalDocument.getUri().trim().length()>0))
					facesMessages.addToControl(
						"studyFinalDocumentEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergeStudyFinalDocument.getUri(), FacesContext
								.getCurrentInstance());
			}
			if(editTabNumber == 3){
				String listOfAdvisors = "";
				for (AuthorDTO advisor : mergeStudyFinalDocument.getAdvisors()) {
					listOfAdvisors += advisor.toString();
				}
				if (listOfAdvisors.length() > 0)
					facesMessages.addToControl(
							"studyFinalDocumentEditForm:listOfAdvisors", FacesMessage.SEVERITY_INFO, 
							listOfAdvisors, FacesContext
									.getCurrentInstance());
				String listOfCommitteeMembers = "";
				for (AuthorDTO committeeMember : mergeStudyFinalDocument.getCommitteeMembers()) {
					listOfCommitteeMembers += committeeMember.toString();
				}
				if (listOfCommitteeMembers.length() > 0)
					facesMessages.addToControl(
							"studyFinalDocumentEditForm:listOfCommitteeMembers", FacesMessage.SEVERITY_INFO, 
							listOfCommitteeMembers, FacesContext
									.getCurrentInstance());
			}
		}
	}
	
	/**
	 * Increases the edit tab number for one
	 */
	public void nextEditTab() {
		if((editTabNumber == 1) && ("studyFinalDocumentEditModalPanel".equals(editModalPanel))){
			if(validateAuthor() == false){
				facesMessages.addToControlFromResourceBundle(
						"studyFinalDocumentEditForm:author", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.author.name.pleaseSelect.error",
						FacesContext.getCurrentInstance());
				return;
			}
		}
		editTabNumber++;
		if((editMode != ModesManagedBean.MODE_ADD_REGISTER_ENTRY) && (editMode != ModesManagedBean.MODE_UPDATE_REGISTER_ENTRY) && (editTabNumber >= 1) && (editTabNumber <= 3))
			populateImportMessages();
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
		if((editMode != ModesManagedBean.MODE_ADD_REGISTER_ENTRY) && (editMode != ModesManagedBean.MODE_UPDATE_REGISTER_ENTRY) && (editTabNumber >= 1) && (editTabNumber <= 3))
			populateImportMessages();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (validateAdvisorsAndCommitteeMembers() == false) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.advisorsCommitteeMembers.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), selectedStudyFinalDocument.getRecord().getArchived(), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedStudyFinalDocument)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.studyFinalDocument.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedStudyFinalDocument);
			sendRecordMessage(selectedStudyFinalDocument, "update");
		}
	}
	
	public void updateRegisterEntry() {
		selectedStudyFinalDocument.getRegisterEntry().setUser(getUserManagedBean().getLoggedUser().getEmail());
		if((selectedStudyFinalDocument.getInstitution().getSuperInstitution() != null) && (selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber()!=null))
			if(selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)94894"))
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UPA");
			else
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UNS");
		if (registerEntryDAO.update(selectedStudyFinalDocument.getRegisterEntry()) == false) {
				facesMessages.addToControlFromResourceBundle(
						"registerEntryEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.registerEntry.update.error", FacesContext
								.getCurrentInstance());
			
		} else {
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"registerEntryEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.studyFinalDocument.registerEntry.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true; 
			debug("updated: \n" + selectedStudyFinalDocument.getRegisterEntry());
			sendRecordMessage(selectedStudyFinalDocument, "update");
		}
	}
	
	public void sendToPromotion() {
		nextEditTab();
	}

	public void confirmSendingToPromotion(){
		selectedStudyFinalDocument.getRegisterEntry().setId("-2");
		if((selectedStudyFinalDocument.getInstitution().getSuperInstitution() != null) && (selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber()!=null))
			if(selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)94894"))
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UPA");
			else
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UNS");
		
		if (registerEntryDAO.update(selectedStudyFinalDocument.getRegisterEntry()) == false) {
				facesMessages.addToControlFromResourceBundle(
						"registerEntryEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.registerEntry.sendToPromotion.error", FacesContext
								.getCurrentInstance());
			
		} else {
			nextEditTab();
			
			populateList = true;
			orderList = true; 
			debug("updated (send to promotion): \n" + selectedStudyFinalDocument.getRegisterEntry());
	}
	}
	
	public void quitSendingToPromotion(){
		previousEditTab();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (validateAdvisorsAndCommitteeMembers() == false) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.advisorsCommitteeMembers.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		similarStudyFinalDocuments = null;
		if ((editTabNumber == 3) || ((editTabNumber == 2) && (! getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA)))){
			try {
				debug("findSimilarStudyFinalDocuments");
				similarStudyFinalDocuments = getStudyFinalDocuments(createSimilarStudyFinalDocumentsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarStudyFinalDocuments", e);
			}
		}
		if((similarStudyFinalDocuments == null ) || (similarStudyFinalDocuments.size()==0)){
			if(hiddenDissertations){
				selectedStudyFinalDocument.getAbstracT().setContent("hiddenTheses543yte");
				if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
						.getEmail(), new GregorianCalendar(), null, null, new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
						selectedStudyFinalDocument)) == false) {
					facesMessages.addToControlFromResourceBundle(
							"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.studyFinalDocument.add.error", FacesContext
									.getCurrentInstance());
				} else {
					init = true;
					if(editTabNumber == 2)
						nextEditTab();
					if(editTabNumber == 3)
						nextEditTab();
					nextEditTab();
					facesMessages.addToControlFromResourceBundle(
							"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_INFO, 
							"records.studyFinalDocument.add.success", FacesContext
									.getCurrentInstance());
					populateList = true;
					orderList = true;
					debug("added: \n" + selectedStudyFinalDocument);
				}
			} else {
				if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
						.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
						selectedStudyFinalDocument)) == false) {
					facesMessages.addToControlFromResourceBundle(
							"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.studyFinalDocument.add.error", FacesContext
									.getCurrentInstance());
				} else {
					init = true;
					if(editTabNumber == 2)
						nextEditTab();
					if(editTabNumber == 3)
						nextEditTab();
					nextEditTab();
					facesMessages.addToControlFromResourceBundle(
							"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_INFO, 
							"records.studyFinalDocument.add.success", FacesContext
									.getCurrentInstance());
					populateList = true;
					orderList = true;
					debug("added: \n" + selectedStudyFinalDocument);
					newRecordEmailNotification(selectedStudyFinalDocument, facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.newStudyFinalDocumentNotification.subject"));
					sendRecordMessage(selectedStudyFinalDocument, "add");
					for (AuthorDTO author : removedAuthors) {
						sendRecordMessage(author, "update");
					}
				}
			}
		} else {
			if ((editTabNumber == 2) && (! getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA))){
				nextEditTab();
			}
			nextEditTab();
		}
	}
	
	/**
	 *  Sends email notification to administrator with details about new record
	 */
	protected void newRegisterEntryEmailNotification(RegisterEntryDTO newRegisterEntry, String subject){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		sendMessage(new EmailMessage(rbAdministration.getString("administration.email.cris"), rbAdministration.getString("administration.email.administrator"), null, null, subject, "<pre>" + newRegisterEntry.toString() + "</pre>"));
	}
	
	public void addRegisterEntry() {
		selectedStudyFinalDocument.getRegisterEntry().setUser(getUserManagedBean().getLoggedUser().getEmail());
		if((selectedStudyFinalDocument.getInstitution().getSuperInstitution() != null) && (selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber()!=null))
			if(selectedStudyFinalDocument.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)94894"))
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UPA");
			else
				selectedStudyFinalDocument.getRegisterEntry().setUniversityId("UNS");
		if (registerEntryDAO.add(selectedStudyFinalDocument.getRegisterEntry()) == false) {
				facesMessages.addToControlFromResourceBundle(
						"registerEntryEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.registerEntry.add.error", FacesContext
								.getCurrentInstance());
			
		} else {
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"registerEntryEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.studyFinalDocument.registerEntry.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true; 
			debug("added: \n" + selectedStudyFinalDocument.getRegisterEntry());
			newRegisterEntryEmailNotification(selectedStudyFinalDocument.getRegisterEntry(), facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.newStudyFinalDocumentNotification.subject"));
			sendRecordMessage(selectedStudyFinalDocument, "update");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		if (selectedStudyFinalDocument == null)
			return;
		if(hiddenDissertations){
			selectedStudyFinalDocument.getAbstracT().setContent("");
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedStudyFinalDocument)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.delete.error", FacesContext
								.getCurrentInstance());
			} else {
				facesMessages.addToControlFromResourceBundle(
						"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_INFO, 
						"records.studyFinalDocument.delete.success", FacesContext
								.getCurrentInstance());
				debug("deleted: \n" + selectedStudyFinalDocument);
				selectedStudyFinalDocument = null;
				populateList = true;
				orderList = true;
			}
		} else {
			if (recordDAO.delete(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedStudyFinalDocument)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.studyFinalDocument.delete.error", FacesContext
								.getCurrentInstance());
			} else {
				debug("deleted: \n" + selectedStudyFinalDocument);
				selectedStudyFinalDocument = null;
				populateList = true;
				orderList = true;
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#archive()
	 */
	@Override
	public void archive() {
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		if (selectedStudyFinalDocument == null)
			return;
		Integer archived = 0;
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2))
			archived = 2;
		else if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1))
			archived = 2; //archived = 1
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), archived, CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedStudyFinalDocument)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.archive.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("archived: \n" + selectedStudyFinalDocument);
			selectedStudyFinalDocument = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#extractArchive()
	 */
	@Override
	public void extractArchive() {
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		if (selectedStudyFinalDocument == null)
			return;
		Integer archived = 0;
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), archived, CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedStudyFinalDocument)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.unarchive.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("archived: \n" + selectedStudyFinalDocument);
			selectedStudyFinalDocument = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void setAvailableToThePublic() {
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		if (selectedStudyFinalDocument == null)
			return;
		Calendar today = new GregorianCalendar();
		if(!(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA))){
			selectedStudyFinalDocument.setPublicationDate(today);
		}
		if(selectedStudyFinalDocument.getPreliminaryTheses() != null){
			if(today.getTimeInMillis() - selectedStudyFinalDocument.getPublicationDate().getTimeInMillis() <= (30l*24*60*60*1000))
				selectedStudyFinalDocument.getPreliminaryTheses().setLicense("Temporary available");
			else if (selectedStudyFinalDocument.getDefendedOn() == null){
				selectedStudyFinalDocument.getPreliminaryTheses().setLicense("Temporary available - not defended");
//				selectedStudyFinalDocument.getPreliminaryTheses().setNote("Public period finished!");
			} else {
				selectedStudyFinalDocument.getPreliminaryTheses().setLicense(null);
				selectedStudyFinalDocument.getPreliminaryTheses().setNote("Public period finished!");
			}
		}
		if(selectedStudyFinalDocument.getPreliminarySupplement() != null){
			if(today.getTimeInMillis() - selectedStudyFinalDocument.getPublicationDate().getTimeInMillis() <= (30l*24*60*60*1000))
				selectedStudyFinalDocument.getPreliminarySupplement().setLicense("Temporary available");
			else if (selectedStudyFinalDocument.getDefendedOn() == null){
				selectedStudyFinalDocument.getPreliminarySupplement().setLicense("Temporary available - not defended");
//				selectedStudyFinalDocument.getPreliminaryTheses().setNote("Public period finished!");
			} else {
				selectedStudyFinalDocument.getPreliminarySupplement().setLicense(null);
				selectedStudyFinalDocument.getPreliminarySupplement().setNote("Public period finished!");
			}
		}
		if(selectedStudyFinalDocument.getReport() != null){
			selectedStudyFinalDocument.getReport().setDateModified(selectedStudyFinalDocument.getPublicationDate());
			if(today.getTimeInMillis() - selectedStudyFinalDocument.getPublicationDate().getTimeInMillis() <= (30l*24*60*60*1000))
				selectedStudyFinalDocument.getReport().setLicense("Temporary available");
			else if (selectedStudyFinalDocument.getDefendedOn() == null){
				selectedStudyFinalDocument.getReport().setLicense("Temporary available - not defended");
//				selectedStudyFinalDocument.getReport().setNote("Public period finished!");
			} else {
				selectedStudyFinalDocument.getReport().setLicense(null);
				selectedStudyFinalDocument.getReport().setNote("Public period finished!");
			}
				
		}
		if ((selectedStudyFinalDocument.getReport()==null) || (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), today, ((selectedStudyFinalDocument.getReport().getLicense() != null) && (selectedStudyFinalDocument.getReport().getLicense().equals("Temporary available")))?1:0, CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedStudyFinalDocument)) == false)) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.setPublic.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("setAvalilableToThePublic: \n" + selectedStudyFinalDocument);
			PublicThesesManagedBean.PUBLIC_THESES_LAST_MODIFICATION_DATE = new Date();
			selectedStudyFinalDocument = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void setUnavailableToThePublic() {
		selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(list);
		if (selectedStudyFinalDocument == null)
			return;
		Calendar today = new GregorianCalendar();
		if(selectedStudyFinalDocument.getPreliminaryTheses() != null){
			selectedStudyFinalDocument.getPreliminaryTheses().setLicense(null);
		}
		if(selectedStudyFinalDocument.getPreliminarySupplement() != null){
			selectedStudyFinalDocument.getPreliminarySupplement().setLicense(null);
		}
		if(selectedStudyFinalDocument.getReport() != null){
			selectedStudyFinalDocument.getReport().setLicense(null);
		}
		if ((selectedStudyFinalDocument.getReport()==null) || (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), today, 0, CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedStudyFinalDocument)) == false)) {
			facesMessages.addToControlFromResourceBundle(
					"studyFinalDocumentTableForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.setNotPublic.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("setUnavalilableToThePublic: \n" + selectedStudyFinalDocument);
			PublicThesesManagedBean.PUBLIC_THESES_LAST_MODIFICATION_DATE = new Date();
			selectedStudyFinalDocument = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedStudyFinalDocument = findStudyFinalDocumentByControlNumber(similarStudyFinalDocuments);
			if ((selectedStudyFinalDocument != null) && (iPickStudyFinalDocumentManagedBean != null)){
				iPickStudyFinalDocumentManagedBean.setStudyFinalDocument(selectedStudyFinalDocument);
				tableTabNumber = 0;
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarStudyFinalDocument", e);
		}
	}

	private IPickStudyFinalDocumentManagedBean iPickStudyFinalDocumentManagedBean = null;

	/**
	 * @param iPickStudyFinalDocumentManagedBean
	 *            the CRUDManagedBean which wants to pick study final document
	 */
	public void setIPickStudyFinalDocumentManagedBean(
			IPickStudyFinalDocumentManagedBean iPickStudyFinalDocumentManagedBean) {
		this.iPickStudyFinalDocumentManagedBean = iPickStudyFinalDocumentManagedBean;
	}
	
	/**
	 * Prepares web form where user can choose Institution
	 */
	public void pickInstitution() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx
				.getSessionMap().get("institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}

		mb.setIPickInstitutionManagedBean(this);
		if(selectedStudyFinalDocument.getInstitution().getControlNumber() != null)
			mb.setSelectedInstitution(selectedStudyFinalDocument.getInstitution());
		else 
			mb.setSelectedInstitution(null);
		mb.setIncludeOrganizationUnits(false);
		mb.setPickMessage("records.studyFinalDocument.pickInstitutionMessage");

		mb.setCustomPick(false);
		mb.switchToPickMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickInstitutionManagedBean#setInstitution(rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO)
	 */
	@Override
	public void setInstitution(InstitutionDTO institution) {
		selectedStudyFinalDocument.setInstitution(institution);
//		selectedStudyFinalDocument.setResearchArea(new ResearchAreaDTO());
		populateImportMessages();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickInstitutionManagedBean#cancelPickingInstitution()
	 */
	@Override
	public void cancelPickingInstitution() {
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

		mb.setInstitution(selectedStudyFinalDocument.getInstitution());
		mb.setIPickResearchAreaManagedBean(this);
		mb.setSelectedResearchArea(new ResearchAreaDTO());
		mb.setPickMessage("records.studyFinalDocument.pickResearchAreaMessage");
		mb.setCustomPick(false);
		mb.switchToPickMode();

	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#setResearchArea(rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO)
	 */
	@Override
	public void setResearchArea(ResearchAreaDTO researchArea) {
		selectedStudyFinalDocument.setResearchArea(researchArea);
		populateImportMessages();
	}
	

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickResearchAreaManagedBean#cancelPickingResearchArea()
	 */
	@Override
	public void cancelPickingResearchArea() {
	}
	
	private boolean pickingAuthor;
	
	/**
	 * Prepares web form where user can choose Author for selected study final document
	 */
	public void pickAuthor() {
		pickingAdvisor = false;
		pickingAuthor = true;
		whereStr = null;
		
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
		mb.setPickMessageFirstTab("records.studyFinalDocument.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.studyFinalDocument.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(false);
		mb.setPleaseInstitutionMessage("records.studyFinalDocument.pickAuthor.pleaseInstitution");
		mb.switchToPickMode();

	}
	
	/**
	 * Adds the other format name to selected author
	 */
	public void addAuthorOtherFormatName() {
		
		pickingAdvisor = false;
		pickingAuthor = true;
		
		AuthorDTO selectedAuthor = (selectedStudyFinalDocument.getAuthor().getControlNumber()!=null)?(selectedStudyFinalDocument.getAuthor()):(null);
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
	
	private boolean pickingAdvisor;
	
	/**
	 * Prepares web form where user can choose Advisor for selected study final document
	 */
	public void pickAdvisor() {
		pickingAdvisor = true;
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
		mb.setPickMessageFirstTab("records.studyFinalDocument.pickAdvisorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.studyFinalDocument.pickAdvisorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(false);
		mb.setPleaseInstitutionMessage("records.studyFinalDocument.pickAdvisor.pleaseInstitution");
		mb.switchToPickMode();

	}
	
	/**
	 * Adds the other format name to selected advisor
	 */
	public void addAdvisorOtherFormatName() {
		
		pickingAdvisor = true;
		pickingAuthor = false;
		
		AuthorDTO selectedAuthor = findAdvisorByControlNumber();
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
	
	private AuthorDTO findAdvisorByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedStudyFinalDocument.getAdvisors()) {
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
	 * Removes the selected advisor from the list of advisors
	 */
	public void removeAdvisor() {
		AuthorDTO selectedAuthor = findAdvisorByControlNumber();
		if (selectedAuthor != null) {
			selectedStudyFinalDocument.getAdvisors().remove(selectedAuthor);
			advisorsList.remove(selectedAuthor.getControlNumber());
			if((selectedStudyFinalDocument.getControlNumber()!=null) && (selectedAuthor.getControlNumber()!=null))
				if(! removedAuthors.contains(selectedAuthor))
					removedAuthors.add(selectedAuthor);
		}
	}

	/**
	 * Switches the selected advisor with previous
	 */
	public void moveAdvisorUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedStudyFinalDocument.getAdvisors().size(); i++) {
			authorDTO = selectedStudyFinalDocument.getAdvisors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchAdvisors(index, (index - 1)
						% selectedStudyFinalDocument.getAdvisors().size());
				break;
		}
	}

	/**
	 * Switches the selected advisor with next
	 */
	public void moveAdvisorDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedStudyFinalDocument.getAdvisors().size(); i++) {
			authorDTO = selectedStudyFinalDocument.getAdvisors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchAdvisors(index, (index + 1)
						% selectedStudyFinalDocument.getAdvisors().size());
				break;
		}
	}

	private void switchAdvisors(int firstIndex, int secondIndex) {
		AuthorDTO first = selectedStudyFinalDocument.getAdvisors().get(firstIndex);
		AuthorDTO second = selectedStudyFinalDocument.getAdvisors().get(secondIndex);
		selectedStudyFinalDocument.getAdvisors().set(firstIndex, second);
		selectedStudyFinalDocument.getAdvisors().set(secondIndex, first);
	}

	private List<AuthorDTO> removedAuthors;
	private Map<String,Boolean> advisorsList;
	
	
	/**
	 * @return the advisorsList
	 */
	public Map<String, Boolean> getAdvisorsList() {
		return advisorsList;
	}

	/**
	 * @param advisorsList the advisorsList to set
	 */
	public void setAdvisorsList(Map<String, Boolean> advisorsList) {
		this.advisorsList = advisorsList;
	}
	
	public void changeCommitteeMembersList(){
		for (AuthorDTO advisor : selectedStudyFinalDocument.getAdvisors()) {
			if(advisorsList.get(advisor.getControlNumber())){
				if(! selectedStudyFinalDocument.getCommitteeMembers().contains(advisor)){
					selectedStudyFinalDocument.getCommitteeMembers().add(advisor);
				}
			} else {
				if(selectedStudyFinalDocument.getCommitteeMembers().contains(advisor)){
					selectedStudyFinalDocument.getCommitteeMembers().remove(advisor);
				}
			}
		}
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
		}else if(((mb.editMode == ModesManagedBean.MODE_UPDATE) || (mb.editMode == ModesManagedBean.MODE_NONE)) && ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD))){
			List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
			otherFormatNames.add(author.getName());
			otherFormatNames.addAll(author.getOtherFormatNames());
			author.setOtherFormatNames(otherFormatNames);
			author.setName(new AuthorNameDTO());
		}
		author.getInstitution().setNotLoaded(true);
		if (pickingAdvisor) {
			if ((selectedStudyFinalDocument.getAdvisors().contains(author))) {
				selectedStudyFinalDocument.getAdvisors().set(
						selectedStudyFinalDocument.getAdvisors().indexOf(author), author);
				if ((selectedStudyFinalDocument.getCommitteeMembers().contains(author))) {
					selectedStudyFinalDocument.getCommitteeMembers().set(
							selectedStudyFinalDocument.getCommitteeMembers().indexOf(author), author);
				}
			} else {
				selectedStudyFinalDocument.getAdvisors().add(author);
				advisorsList.put(author.getControlNumber(), new Boolean(false));
			}
		} else if (pickingAuthor) {
			if ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD)) {
//				List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
//				otherFormatNames.add(author.getName());
//				otherFormatNames.addAll(author.getOtherFormatNames());
//				author.setOtherFormatNames(otherFormatNames);
//				author.setName(new AuthorNameDTO());
				if((selectedStudyFinalDocument.getControlNumber()!=null) && (selectedStudyFinalDocument.getAuthor().getControlNumber()!=null))
					if(! removedAuthors.contains(selectedStudyFinalDocument.getAuthor()))
						removedAuthors.add(selectedStudyFinalDocument.getAuthor());
				selectedStudyFinalDocument.setAuthor(author);
			}  else {
				this.selectedAuthor = author;
				populateList = true;
				orderList = true;
			}
		} else {
			if ((selectedStudyFinalDocument.getCommitteeMembers().contains(author))) {
				selectedStudyFinalDocument.getCommitteeMembers().set(
						selectedStudyFinalDocument.getCommitteeMembers().indexOf(author), author);
			}else {
				selectedStudyFinalDocument.getCommitteeMembers().add(author);
			}
		}
		populateImportMessages();
	}
	
	/**
	 * Prepares web form where user can choose Committee member for selected study final document
	 */
	public void pickCommitteeMember() {
		
		pickingAuthor = false;
		pickingAdvisor = false;
		
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
		mb.setPickMessageFirstTab("records.studyFinalDocument.pickCommitteeMemberMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.studyFinalDocument.pickCommitteeMemberMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(false);
		mb.setPleaseInstitutionMessage("records.studyFinalDocument.pickCommitteeMember.pleaseInstitution");
		mb.switchToPickMode();

	}
	
	/**
	 * Adds the other format name to selected committee member
	 */
	public void addCommitteeMemberOtherFormatName() {
		
		pickingAdvisor = false;
		pickingAuthor = false;
		
		AuthorDTO selectedAuthor = findCommitteeMemberByControlNumber();
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
	
	private AuthorDTO findCommitteeMemberByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedStudyFinalDocument.getCommitteeMembers()) {
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

//	/**
//	 * Removes the selected editor from the list of editors
//	 */
//	public void updateCommitteeMember() {
//		AuthorDTO selectedAuthor = findCommitteeMemberByControlNumber();
//		if (personDAO.update(new Person(null, null, getUserManagedBean()
//				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
//				selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false) {
//			facesMessages.addToControlFromResourceBundle(
//					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_ERROR, "records.author.update.error",
//					FacesContext.getCurrentInstance());
//		} else {
//			facesMessages.addToControlFromResourceBundle(
//					"studyFinalDocumentEditForm:general", FacesMessage.SEVERITY_INFO,  "records.author.update.success",
//					FacesContext.getCurrentInstance());
//			debug("updated: \n" + selectedAuthor);
//		}
//		
//	}
	
	/**
	 * Removes the selected editor from the list of editors
	 */
	public void removeCommitteeMember() {
		AuthorDTO selectedAuthor = findCommitteeMemberByControlNumber();
		if (selectedAuthor != null){ 
			selectedStudyFinalDocument.getCommitteeMembers().remove(selectedAuthor);
			if((selectedStudyFinalDocument.getControlNumber()!=null) && (selectedAuthor.getControlNumber()!=null))
				if(! removedAuthors.contains(selectedAuthor))
					removedAuthors.add(selectedAuthor);
		}
	}

	/**
	 * Switches the selected editor with previous
	 */
	public void moveCommitteeMemberUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedStudyFinalDocument.getCommitteeMembers().size(); i++) {
			authorDTO = selectedStudyFinalDocument.getCommitteeMembers().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchCommitteeMembers(index, (index - 1)
						% selectedStudyFinalDocument.getCommitteeMembers().size());
				break;
		}
	}

	/**
	 * Switches the selected editor with next
	 */
	public void moveCommitteeMemberDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedStudyFinalDocument.getCommitteeMembers().size(); i++) {
			authorDTO = selectedStudyFinalDocument.getCommitteeMembers().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				break;
			default:
				switchCommitteeMembers(index, (index + 1)
						% selectedStudyFinalDocument.getCommitteeMembers().size());
				break;
		}
	}

	private void switchCommitteeMembers(int firstIndex, int secondIndex) {
		AuthorDTO first = selectedStudyFinalDocument.getCommitteeMembers().get(firstIndex);
		AuthorDTO second = selectedStudyFinalDocument.getCommitteeMembers().get(secondIndex);
		selectedStudyFinalDocument.getCommitteeMembers().set(firstIndex, second);
		selectedStudyFinalDocument.getCommitteeMembers().set(secondIndex, first);
	}

	private List<SelectItem> allTitlesSelectItems;
	
	private List<SelectItem> allPositionsSelectItems;
	
	/**
	 * @return the allTitlesSelectItems
	 */
	public List<SelectItem> getAllTitlesSelectItems() {
		return allTitlesSelectItems;
	}

	/**
	 * @param allTitlesSelectItems the allTitlesSelectItems to set
	 */
	public void setAllTitlesSelectItems(List<SelectItem> allTitlesSelectItems) {
		this.allTitlesSelectItems = allTitlesSelectItems;
	}

	/**
	 * @return the allPositionsSelectItems
	 */
	public List<SelectItem> getAllPositionsSelectItems() {
		return allPositionsSelectItems;
	}

	/**
	 * @param allPositionsSelectItems the allPositionsSelectItems to set
	 */
	public void setAllPositionsSelectItems(List<SelectItem> allPositionsSelectItems) {
		this.allPositionsSelectItems = allPositionsSelectItems;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}
	
	
	private boolean validateAuthor() {
		boolean retVal = true;
		if ((selectedStudyFinalDocument.getAuthor().getName() == null)
					|| ("".equals(selectedStudyFinalDocument.getAuthor().getName().getLastname()))) {
			retVal = false;
		}
		return retVal;
	}
	
	private boolean validateAdvisorsAndCommitteeMembers() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedStudyFinalDocument.getAdvisors()) {
			if ((authorDTO.getName() == null)
					|| ("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		for (AuthorDTO authorDTO : selectedStudyFinalDocument.getCommitteeMembers()) {
			if ((authorDTO.getName() == null)
					|| ("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	/**
	 * @return the allPaperTypesSelectItems
	 */
	public List<SelectItem> getAllStudyTypesSelectItems() {
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		if (selectedStudyFinalDocument.getStudyType() == null) {
			if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() == null) 
//					|| 
//				(! ("(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())))
				){
					retVal.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.pleaseSelect")));
			}
		}
		retVal.add(new SelectItem(
						"records.studyFinalDocument.editPanel.studyType.phd",
						facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.phd")));
		retVal.add(new SelectItem(
				"records.studyFinalDocument.editPanel.studyType.phdArt",
				facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.phdArt")));
		if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() == null) 
//				|| 
//				(! ("(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())))
				){
			retVal.add(new SelectItem(
					"records.studyFinalDocument.editPanel.studyType.oldMaster",
					facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.oldMaster")));
			retVal.add(new SelectItem(
					"records.studyFinalDocument.editPanel.studyType.master",
					facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.master")));
			retVal.add(new SelectItem(
					"records.studyFinalDocument.editPanel.studyType.oldBachelor",
					facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.oldBachelor")));
			retVal.add(new SelectItem(
					"records.studyFinalDocument.editPanel.studyType.bachelor",
					facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.bachelor")));
			retVal.add(new SelectItem(
					"records.studyFinalDocument.editPanel.studyType.specialistic",
					facesMessages.getMessageFromResourceBundle("records.studyFinalDocument.editPanel.studyType.specialistic")));
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "studyFinalDocumentPage";
		return retVal;
	}

	private StudyFinalDocumentDTO findStudyFinalDocumentByControlNumber(List<StudyFinalDocumentDTO> studyFinalDocumentsList) {
		StudyFinalDocumentDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (StudyFinalDocumentDTO dto : studyFinalDocumentsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber().equalsIgnoreCase(controlNumber))) {
					retVal = dto;
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
		if((editMode == ModesManagedBean.MODE_IMPORT)){
			iPickStudyFinalDocumentManagedBean.setStudyFinalDocument(selectedStudyFinalDocument);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	
	public void titleTranslations(){
		int tempEditMode = editMode;
		if ((selectedStudyFinalDocument.getRecord().getArchived() == 1) && (!getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA))){
			tempEditMode = ModesManagedBean.MODE_VIEW_DETAILS;
		}
		this.openMultilingualContentForm(tempEditMode, selectedStudyFinalDocument.getTitleTranslations(), false, "records.studyFinalDocument.editPanel.titleTranslations.panelHeader", "records.studyFinalDocument.editPanel.titleTranslations.contentHeader");
	}
	
	public void alternativeTitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getAlternativeTitleTranslations(), false, "records.studyFinalDocument.editPanel.alternativeTitleTranslations.panelHeader", "records.studyFinalDocument.editPanel.alternativeTitleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getSubtitleTranslations(), false, "records.studyFinalDocument.editPanel.subtitleTranslations.panelHeader", "records.studyFinalDocument.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getNoteTranslations(), false, "records.studyFinalDocument.editPanel.noteTranslations.panelHeader", "records.studyFinalDocument.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getKeywordsTranslations(), false, "records.studyFinalDocument.editPanel.keywordsTranslations.panelHeader", "records.studyFinalDocument.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getAbstractTranslations(), true, "records.studyFinalDocument.editPanel.abstractTranslations.panelHeader", "records.studyFinalDocument.editPanel.abstractTranslations.contentHeader");
	}
	
	public void extendedAbstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getExtendedAbstractTranslations(), true, "records.studyFinalDocument.editPanel.extendedAbstractTranslations.panelHeader", "records.studyFinalDocument.editPanel.extendedAbstractTranslations.contentHeader");
	}
	
	public void rightsTranslations(){
		this.openMultilingualContentForm(editMode, selectedStudyFinalDocument.getRightsTranslations(), true, "records.studyFinalDocument.editPanel.rightsTranslations.panelHeader", "records.studyFinalDocument.editPanel.rightsTranslations.contentHeader");
	}
	
	public void publisherTranslations(){
		this.openMultilingualContentPublisherForm(editMode, selectedStudyFinalDocument.getPublisher().getPublisherTranslations(), "records.studyFinalDocument.editPanel.publisherTranslations.panelHeader");
	}
	
//	private boolean institutionsPopulated = false;
//	
//	private HtmlSelectOneMenu institutionSelectOneMenu = new HtmlSelectOneMenu(); 
//	
//	/**
//	 * @return the institutionSelectOneMenu
//	 */
//	public HtmlSelectOneMenu getInstitutionSelectOneMenu() {
//		if (institutionsPopulated == false) {
//			final UISelectItems items = new UISelectItems();
//			FacesContext facesCtx = FacesContext.getCurrentInstance();
//			ExternalContext extCtx = facesCtx.getExternalContext();
//			
//			InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
//			"institutionManagedBean");
//			if (mb == null) {
//				mb = new InstitutionManagedBean();
//				extCtx.getSessionMap().put("institutionManagedBean", mb);
//			}
//			items.setValue(mb.getAllList());
//			institutionSelectOneMenu.getChildren().add(items);
//			institutionsPopulated = true;
//		}
//		return institutionSelectOneMenu;
//	}
//
//	/**
//	 * @param institutionSelectOneMenu the institutionSelectOneMenu to set
//	 */
//	public void setInstitutionSelectOneMenu(
//			HtmlSelectOneMenu institutionSelectOneMenu) {
//		this.institutionSelectOneMenu = institutionSelectOneMenu;
//	}
	
	private Date publicationDate;
	private Date acceptedOn;
	private Date defendedOn;
	private Date previouslyDefendedOn;
	private Date birthDate;
	private Date promotionDate;
	private Date diplomaPublicationDate;
	private Date supplementPublicationDate;

	/**
	 * @return the publicationDate
	 */
	public Date getPublicationDate() {
		return publicationDate;
	}

	/**
	 * @param publicationDate the publicationDate to set
	 */
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
		if(this.publicationDate!=null) {
			Calendar pd = new GregorianCalendar();
			pd.setTime(publicationDate);
			selectedStudyFinalDocument.setPublicationDate(pd);
		}
	}

	/**
	 * @return the acceptedOn
	 */
	public Date getAcceptedOn() {
		return acceptedOn;
	}

	/**
	 * @param acceptedOn the acceptedOn to set
	 */
	public void setAcceptedOn(Date acceptedOn) {
		this.acceptedOn = acceptedOn;
		if(this.acceptedOn!=null) {
			Calendar ao = new GregorianCalendar();
			ao.setTime(acceptedOn);
			selectedStudyFinalDocument.setAcceptedOn(ao);
		}
	}

	/**
	 * @return the defendedOn
	 */
	public Date getDefendedOn() {
		return defendedOn;
	}

	/**
	 * @param defendedOn the defendedOn to set
	 */
	public void setDefendedOn(Date defendedOn) {
		this.defendedOn = defendedOn;
		if(this.defendedOn!=null) {
			Calendar defo = new GregorianCalendar();
			defo.setTime(defendedOn);
			selectedStudyFinalDocument.getRegisterEntry().setDefendedOn(defo);
		}
	}
	
	/**
	 * @return the previouslyDefendedOn
	 */
	public Date getPreviouslyDefendedOn() {
		return previouslyDefendedOn;
	}

	/**
	 * @param previouslyDefendedOn the previouslyDefendedOn to set
	 */
	public void setPreviouslyDefendedOn(Date previouslyDefendedOn) {
		this.previouslyDefendedOn = previouslyDefendedOn;
		if(this.previouslyDefendedOn!=null) {
			Calendar pdefo = new GregorianCalendar();
			pdefo.setTime(previouslyDefendedOn);
			selectedStudyFinalDocument.getRegisterEntry().setPreviouslyNameOfAuthorDegreeDateOld(pdefo);
		}
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		if(this.birthDate!=null) {
			Calendar date = new GregorianCalendar();
			date.setTime(birthDate);
			selectedStudyFinalDocument.getRegisterEntry().setBirthDate(date);
		}
	}

	/**
	 * @return the promotionDate
	 */
	public Date getPromotionDate() {
		return promotionDate;
	}

	/**
	 * @param promotionDate the promotionDate to set
	 */
	public void setPromotionDate(Date promotionDate) {
		this.promotionDate = promotionDate;
		if(this.promotionDate!=null) {
			Calendar date = new GregorianCalendar();
			date.setTime(promotionDate);
			selectedStudyFinalDocument.getRegisterEntry().setPromotionDate(date);
		}
	}

	/**
	 * @return the diplomaPublicationDate
	 */
	public Date getDiplomaPublicationDate() {
		return diplomaPublicationDate;
	}

	/**
	 * @param diplomaPublicationDate the diplomaPublicationDate to set
	 */
	public void setDiplomaPublicationDate(Date diplomaPublicationDate) {
		this.diplomaPublicationDate = diplomaPublicationDate;
		if(this.diplomaPublicationDate!=null) {
			Calendar date = new GregorianCalendar();
			date.setTime(diplomaPublicationDate);
			selectedStudyFinalDocument.getRegisterEntry().setDiplomaPublicationDate(date);
		}
	}

	/**
	 * @return the supplementPublicationDate
	 */
	public Date getSupplementPublicationDate() {
		return supplementPublicationDate;
	}

	/**
	 * @param supplementPublicationDate the supplementPublicationDate to set
	 */
	public void setSupplementPublicationDate(Date supplementPublicationDate) {
		this.supplementPublicationDate = supplementPublicationDate;
		if(this.supplementPublicationDate!=null) {
			Calendar date = new GregorianCalendar();
			date.setTime(supplementPublicationDate);
			selectedStudyFinalDocument.getRegisterEntry().setSupplementPublicationDate(date);
		}
	}

	public void uploadFileListener(FileUploadEvent event) {
		try{
			uploadDocumentListener(event, "document");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setFile(null);
        }
    }

	public void deleteFile(){
		if(selectedStudyFinalDocument.getFile().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getFile());
		}
		if(selectedStudyFinalDocument.getWordCloudImage() != null){
			if(selectedStudyFinalDocument.getWordCloudImage().getId() != 0){
				selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getWordCloudImage());
			}
			selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getWordCloudImage());
			selectedStudyFinalDocument.setWordCloudImage(null);
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getFile());
		selectedStudyFinalDocument.setFile(null);
	}
	
	public void uploadFileCopyrightListener(FileUploadEvent event) {
        try{
	        uploadDocumentListener(event, "copyright");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setFileCopyright(null);
        }
    }

	public void deleteFileCopyright(){
		if(selectedStudyFinalDocument.getFileCopyright().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getFileCopyright());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getFileCopyright());
		selectedStudyFinalDocument.setFileCopyright(null);
		if(selectedStudyFinalDocument.getFile()!=null)
			selectedStudyFinalDocument.getFile().setLicense(null);
		if(selectedStudyFinalDocument.getSupplement()!=null)
			selectedStudyFinalDocument.getSupplement().setLicense(null);
	}
	
	public void uploadMetadataCopyrightListener(FileUploadEvent event) {
        try{
	        uploadDocumentListener(event, "metadataCopyright");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setMetadataCopyright(null);
        }
    }

	public void deleteMetadataCopyright(){
		if(selectedStudyFinalDocument.getMetadataCopyright().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getMetadataCopyright());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getMetadataCopyright());
		selectedStudyFinalDocument.setMetadataCopyright(null);
	}
	
	public void uploadPreliminaryThesesListener(FileUploadEvent event) {
        try{
        	uploadDocumentListener(event, "preliminaryTheses");
	        if(selectedStudyFinalDocument.getFile() == null)
	        	uploadDocumentListener(event, "document");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setPreliminaryTheses(null);
        }
    }

	public void deletePreliminaryTheses(){
		if(selectedStudyFinalDocument.getPreliminaryTheses().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getPreliminaryTheses());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getPreliminaryTheses());
		selectedStudyFinalDocument.setPreliminaryTheses(null);
	}
	
	public void uploadPreliminarySupplementListener(FileUploadEvent event) {
        try{
        	uploadDocumentListener(event, "preliminarySupplement");
	        if(selectedStudyFinalDocument.getSupplement() == null)
	        	uploadDocumentListener(event, "supplement");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setPreliminarySupplement(null);
        }
    }

	public void deletePreliminarySupplement(){
		if(selectedStudyFinalDocument.getPreliminarySupplement().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getPreliminarySupplement());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getPreliminarySupplement());
		selectedStudyFinalDocument.setPreliminarySupplement(null);
	}
	
	public void uploadSupplementListener(FileUploadEvent event) {
        try{
        	uploadDocumentListener(event, "supplement");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setSupplement(null);
        }
    }

	public void deleteSupplement(){
		if(selectedStudyFinalDocument.getSupplement().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getSupplement());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getSupplement());
		selectedStudyFinalDocument.setSupplement(null);
	}
	
	public void uploadReportListener(FileUploadEvent event) {
        try{
	        uploadDocumentListener(event, "report");
        } catch (Exception e) {
        	selectedStudyFinalDocument.setReport(null);
        }
    }

	public void deleteReport(){
		if(selectedStudyFinalDocument.getReport().getId() != 0){
			selectedStudyFinalDocument.getDeletedFiles().add(selectedStudyFinalDocument.getReport());
		}
		selectedStudyFinalDocument.getFiles().remove(selectedStudyFinalDocument.getReport());
		selectedStudyFinalDocument.setReport(null);
	}
	
	public void uploadDocumentListener(FileUploadEvent event, String type) throws Exception {
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
        fileDTO.setFileNameClient((item.getFileName().lastIndexOf("\\") != -1)?LatCyrUtils.toLatinUnaccented(item.getFileName().substring(item.getFileName().lastIndexOf("\\")+1)):LatCyrUtils.toLatinUnaccented(item.getFileName()));
        fileDTO.setControlNumber(selectedStudyFinalDocument.getControlNumber());
        fileDTO.setUploader(getUserManagedBean().getLoggedUser().getEmail());
        fileDTO.setType(type);
        fileDTO.setDateModified(new GregorianCalendar());
        List<FileDTO> files = selectedStudyFinalDocument.getFiles();
        files.add(fileDTO);
        selectedStudyFinalDocument.setFiles(files);
    }

	
	/**
	 * Sets all necessary things for InstitutionManageBean in pick mode
	 */
	public void setInstitutionManageBeanToPick() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
				"institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}

		mb.setIPickInstitutionManagedBean(this);
		mb.setSelectedInstitution(null);
		mb.setCustomPick(true);
		mb.switchToPickMode();
	}
	
	public boolean canDownload(int id){
		boolean retVal = false;
		UserManagedBean ub = getUserManagedBean();
		retVal = ub.getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD);
		Connection conn = null;
		try {
			conn = DataSourceFactory.getDataSource().getConnection();
			FileDTO file = FileDB.getFileByID(conn, id);
			if(file != null){
				selectedStudyFinalDocument = (StudyFinalDocumentDTO) recordDAO.getDTO(file.getControlNumber());
				if(retVal == false)
					if((selectedStudyFinalDocument != null) && (selectedStudyFinalDocument.getFileId() == id) && (selectedStudyFinalDocument.getAuthor().equals(getUserManagedBean().getLoggedUser().getAuthor())))
						retVal = true;	
			}
		} catch (SQLException e) {
			error("canDownload", e);
		} catch (Exception e) {
			error("canDownload", e);
		} finally {
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		if((selectedStudyFinalDocument != null) && (editMode != ModesManagedBean.MODE_NONE))
			selectedStudyFinalDocument.setNotLoaded(true);
		return retVal;
	}
	
	public boolean isOpenAccess(int id){
		boolean retVal = false;
		
		Connection conn = null;
		try {
			conn = DataSourceFactory.getDataSource().getConnection();
			FileDTO file = FileDB.getFileByID(conn, id);
		
			selectedStudyFinalDocument = (StudyFinalDocumentDTO) recordDAO.getDTO(file.getControlNumber());
			if((file.getLicense() != null) && (!(file.getLicense().equals("Usage forbidden"))))//((file.getLicense().equals("Temporary available")) || (file.getLicense().equals("Creative Commons")))) 
				retVal = true;
			if(selectedStudyFinalDocument == null)
				retVal = false;
		} catch (SQLException e) {
			error("canDownload", e);
		} catch (Exception e) {
			error("canDownload", e);
		} finally {
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		if(selectedStudyFinalDocument != null)
			selectedStudyFinalDocument.setNotLoaded(true);
		return retVal;
	}
	
	public void checkMaticnaKnjigaForSingleElement(){
	 FacesContext context = FacesContext.getCurrentInstance();    
  try {    
        
       HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();  
       HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();       
       RequestDispatcher dispatcher = request.getRequestDispatcher("/MaticnaKnjigaServlet?type=generateReport&singleElement=yes&controlNumber="
       		+selectedStudyFinalDocument.getControlNumber());  
       dispatcher.forward(request, response);         
  }catch (Exception e) {    
     e.printStackTrace();    
  }    
  finally{    
     context.responseComplete();    
  }    

	}
	
	
	private boolean showPublications = false;
	
	public void exitPublications(){
		showPublications = false;
	}
	
	/**
	 * @return the selected StudyFinalDocumentDTO
	 */
	public StudyFinalDocumentDTO getStudyFinalDocumentPublications() {
		if(showPublications)
			return selectedStudyFinalDocument;
		else
			return null;
	}

	/**
	 * @param studyFinalDocumentDTO
	 *            the studyFinalDocumentDTO to set as selected studyFinalDocumentDTO
	 */
	public void setStudyFinalDocumentPublications(StudyFinalDocumentDTO studyFinalDocumentDTO) {
		if(studyFinalDocumentDTO !=null){
			showPublications = true;
			setSelectedStudyFinalDocument(studyFinalDocumentDTO);
			populatePublicationsList = true;
		}
	}	
	
	protected PublicationDTO selectedPublication = null;
	protected List<PublicationSelectionDTO> listPublications = null;
	
	protected boolean populatePublicationsList = true;
	protected boolean orderPublicationsList = true;
	
	
	protected List<PublicationDTO> getPublications(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<PublicationDTO> retVal = new ArrayList<PublicationDTO>();
		
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {			
			try {
				PublicationDTO dto = (PublicationDTO) record.getDto();				
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
			Collections.sort(retVal, new GenericComparator<PublicationDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
	/**
	 * Creates query for retrieving publications list.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createPublicationsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
//		if ((whereStr != null) && (!"".equals(whereStr))) 
//			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		BooleanQuery type = new BooleanQuery();
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_JOURNAL).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PATENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
//		}
//		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PRODUCT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
//			type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
//		}
		bq.add(type, Occur.MUST);
		
		
//		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
//		if ((loggedAuthor.getControlNumber() != null) && ((selectedAuthor == null) || (selectedAuthor.getControlNumber() == null))){
//			selectedAuthor = loggedAuthor;
//		}
		
		
		if(selectedStudyFinalDocument.getAuthor().getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", selectedStudyFinalDocument.getAuthor().getControlNumber())), Occur.SHOULD);
//			authorsPapers.add(new TermQuery(new Term("EDCN", selectedStudyFinalDocument.getAuthor().getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		} 
		
//		if(selectedJobAd != null){			
//			bq.add(new TermQuery(new Term("JACN", selectedJobAd.getControlNumber())), Occur.MUST);
//		}
		return bq;
	}
	
	public void populatePublicationsList(){
		try {
			debug("populatePublicationsList");	
			List<PublicationDTO> listTmp = getPublications(createPublicationsQuery(), null, null, new AllDocCollector(true));
			
			
			if (init == true && listTmp.size() != 0 && selectedPublication != null
					&& selectedPublication.getControlNumber() != null) {
	
				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPublication.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("publicationsTable");
					if(table!=null){
						int page = index / table.getRows();
						table.setFirst(table.getRows()*page);
					}
				}
				init = false;
			}
			

			listPublications = new ArrayList<PublicationSelectionDTO>();
			
			for (PublicationDTO publicationDTO : listTmp) {
				if(selectedStudyFinalDocument.getRelatedPublications().contains(publicationDTO))
					listPublications.add(new PublicationSelectionDTO(publicationDTO, true, null));
				else
					listPublications.add(new PublicationSelectionDTO(publicationDTO, false, null));
			}
			
			
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("publication.publicationYear");
			directionsList.add("desc");
			
			orderByList.add("publication.controlNumber");
			directionsList.add("asc");
			Collections.sort(listPublications, new GenericComparator<PublicationSelectionDTO>(
					orderByList, directionsList));
		} catch (ParseException e) {
			error("populatePublicationsList", e);
			listPublications = new ArrayList<PublicationSelectionDTO>();
		}
	}
	
	public void updateSelectedPublications() {
		boolean allRight = true;
		selectedStudyFinalDocument.getRelatedPublications().clear();
		for (PublicationSelectionDTO publicationSelection : listPublications) {
			PublicationDTO publication = publicationSelection.getPublication();
			if(publicationSelection.isSelected()){
				selectedStudyFinalDocument.getRelatedPublications().add(publication);
			} 
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), selectedStudyFinalDocument.getRecord().getArchived(), selectedStudyFinalDocument.getRecord().getType(), 
				selectedStudyFinalDocument)) == false) {
			allRight = false;
		} 
		if(allRight){
			facesMessages.addToControlFromResourceBundle(
					"publicationsForm:general", FacesMessage.SEVERITY_INFO, 
					"records.studyFinalDocument.publications.update.success", FacesContext
							.getCurrentInstance());
			debug("updated: \n" + selectedStudyFinalDocument);
		} else {
			facesMessages.addToControlFromResourceBundle(
					"publicationsForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.studyFinalDocument.publications.update.error", FacesContext
							.getCurrentInstance());
		}
	}
	
	public List<PublicationSelectionDTO> getPublications() {
		if(populatePublicationsList){
			populatePublicationsList();
			populatePublicationsList = false;
		}
		return listPublications;
	}
	
	public void selectAllPublications() {
		for (PublicationSelectionDTO publicationSelection : listPublications) {
			publicationSelection.setSelected(true);
		}
	}
	
	public void deselectAllPublications() {
		for (PublicationSelectionDTO publicationSelection : listPublications) {
			publicationSelection.setSelected(false);
		}
	}
	
	public void showHiddenDissertations(){
		hiddenDissertations = !hiddenDissertations;
		populateList = true;
	}

	/**
	 * @return the hiddenDissertations
	 */
	public boolean isHiddenDissertations() {
		return hiddenDissertations;
	}

	/**
	 * @param hiddenDissertations the hiddenDissertations to set
	 */
	public void setHiddenDissertations(boolean hiddenDissertations) {
		this.hiddenDissertations = hiddenDissertations;
	}
	
	public List<StudyFinalDocumentDTO> getHiddenDissertationsList(){
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		String query = "" + StudyFinalDocumentManagedBean.HIDDEN_THESES_WHERE_CLAUSE;
		UserDTO userDTO = getUserManagedBean().getLoggedUser();
		if(userDTO != null)
			if(((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)94894")) || 
					((userDTO.getInstitution().getSuperInstitution() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)94894")))))
				query += StudyFinalDocumentManagedBean.HIDDEN_THESES_WHERE_CLAUSE_PA;
			else if(((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)5920")) || 
					((userDTO.getInstitution().getSuperInstitution() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber() != null) && (userDTO.getInstitution().getSuperInstitution().getControlNumber().equals("(BISIS)5920")))))
				query += StudyFinalDocumentManagedBean.HIDDEN_THESES_WHERE_CLAUSE_UNS;
		System.out.println(query);
		List<Record> records = recordDAO.getRecordsFromDatabaseByWhereClause(query);
		if(records != null) {
			for (Record record : records) {
				record.loadDTOFromMARC21();
				retVal.add((StudyFinalDocumentDTO) record.getDto());
			}
		}
		return retVal;
	}
	
	private String fileLicense;
	
	/**
	 * @return the fileLicense
	 */
	public String getFileLicense() {
		if (selectedStudyFinalDocument.getFile() != null)
			return selectedStudyFinalDocument.getFile().getLicense();
		else
			return null;
	}

	/**
	 * @param fileLicense the fileLicense to set
	 */
	public void setFileLicense(String fileLicense) {
		if (selectedStudyFinalDocument.getFile() != null)
			selectedStudyFinalDocument.getFile().setLicense(fileLicense);
		if (selectedStudyFinalDocument.getSupplement() != null)
			selectedStudyFinalDocument.getSupplement().setLicense(fileLicense);
	}

	public static String HIDDEN_THESES_WHERE_CLAUSE = "ARCHIVED = 100 AND RECORDSTRING like '%hiddenTheses543yte%'";
	
	public static String HIDDEN_THESES_WHERE_CLAUSE_UNS = " AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
	
	public static String HIDDEN_THESES_WHERE_CLAUSE_PA = " AND (RECORDSTRING like '%710_2#__0(BISIS)9489%' OR RECORDSTRING like '%710_2#__0(BISIS)107024%')";
	
}
