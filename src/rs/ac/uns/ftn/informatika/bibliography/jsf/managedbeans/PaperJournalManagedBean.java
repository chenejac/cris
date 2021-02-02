package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
import org.richfaces.component.UIDataTable;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for paper published in proceedings
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PaperJournalManagedBean extends CRUDManagedBean implements
		 IPickJournalManagedBean, IPickAuthorManagedBean {

	private List<PaperJournalDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private PaperJournalDTO selectedPaperJournal = null;
	
	public static String M99_PMF_WHERE_CLAUSE = "ARCHIVED != 100 and RECORDID in (SELECT RECORDID from MARC21RECORD_CLASS where CFCLASSID like 'paperJournal')  " +
			"and RECORDID in (SELECT RECORDID2 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorshipType' and RECORDID1 in (SELECT RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929' and CFSTARTDATE like '2020-01-01 00:00:00'))  " +
			"and RECORDID not in (SELECT RECORDID from MARC21RECORD_CLASS where COMMISSIONID in (711, 712, 713, 714, 715, 721, 722, 723, 724, 725))";
		
	public static String M99_TF_WHERE_CLAUSE = "ARCHIVED != 100 and RECORDID in (SELECT RECORDID from MARC21RECORD_CLASS where CFCLASSID like 'paperJournal') " +
			"and RECORDID in (SELECT RECORDID2 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorshipType' and RECORDID1 in (SELECT RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5933' and CFSTARTDATE like '2020-01-01 00:00:00')) " +
			"and RECORDID not in (SELECT RECORDID from MARC21RECORD_CLASS where COMMISSIONID in (701))";

	/**
	 * 
	 */
	public PaperJournalManagedBean() {
		super();
		tableModalPanel = "paperJournalBrowseModalPanel";
		editModalPanel = "paperJournalEditModalPanel";
		simpleEditModalPanel = "paperJournalSimpleEditModalPanel";
		pickSimilarMessage = "records.paperJournal.pickSimilarMessage";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPaperJournal = null;
		journal = null;
		tableModalPanel = "paperJournalBrowseModalPanel";
		editModalPanel = "paperJournalEditModalPanel";
		simpleEditModalPanel = "paperJournalSimpleEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<PaperJournalDTO> listTmp = getPaperJournals(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false) && (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedPaperJournal != null
					&& selectedPaperJournal.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPaperJournal.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("paperJournalTable");
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
			list = new ArrayList<PaperJournalDTO>();
		}
	}
	
	/**
	 * Retrieves a list of papers (published in journals) that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in proceedings) that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<PaperJournalDTO> getPaperJournals(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getPaperJournals(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of papers (published in journals) that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in journals) that correspond to the query
	 */
	private List<PaperJournalDTO> getPaperJournals(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<PaperJournalDTO> retVal = new ArrayList<PaperJournalDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				PaperJournalDTO dto = (PaperJournalDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<PaperJournalDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of papers published in journals (filtered and ordered
	 *         by ...)
	 */
	public List<PaperJournalDTO> getPaperJournals() {
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
			Collections.sort(list, new GenericComparator<PaperJournalDTO>(
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
		if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		if (journal != null)
			bq.add(new TermQuery(new Term("JOCN", journal.getControlNumber())), Occur.MUST);
		return bq;
	}

	public List<PaperJournalDTO> autocompleteTitle(String suggest) {
		List<PaperJournalDTO> retVal = new ArrayList<PaperJournalDTO>();
		
		String paperJournalName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(paperJournalName != null){
			bq.add(QueryUtils.makeBooleanQuery("TI", paperJournalName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", paperJournalName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		bq.add(type, Occur.MUST);

		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				PaperJournalDTO dto = (PaperJournalDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	List<PaperJournalDTO> similarPaperJournals = null;
	
	/**
	 * @return the list of SIMILAR paper journals with selected paper journal
	 */
	public List<PaperJournalDTO> getSimilarPaperJournals() {
		return similarPaperJournals;
	}

	/**
	 * Creates query for finding SIMILAR paper journals with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarPaperJournalsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedPaperJournal.getTitle().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedPaperJournal.getTitle().getContent(), Occur.SHOULD, 0.7f, 0.7f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedPaperJournal.getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		else if(selectedPaperJournal.getTitleTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("TI",selectedPaperJournal.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.7f, 0.7f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("TI",selectedPaperJournal.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);	
		}
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		return bq;
	}
	
	/**
	 * @return the selected paper published in journal
	 */
	public PaperJournalDTO getSelectedPaperJournal() {
		return selectedPaperJournal;
	}

	/**
	 * @param paperJournalDTO
	 *            the paper published in journal to set as selected paper
	 */
	public void setSelectedPaperJournal(
			PaperJournalDTO paperJournalDTO) {
		selectedPaperJournal = paperJournalDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedPaperJournal = findPaperJournalByControlNumber();
			if (selectedPaperJournal != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedPaperJournal);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleUpdateMode()
	 */
	@Override
	public void switchToSimpleUpdateMode() {
		simpleAuthorList = true;
		setAuthorManageBeanToPick(this);
		authorName = "";
		if ((tableMode == ModesManagedBean.MODE_NONE))
			super.switchToSimpleUpdateMode();
		else {
			selectedPaperJournal = findPaperJournalByControlNumber();
			if (selectedPaperJournal != null) {
				super.switchToSimpleUpdateMode();
				debug("switchToSimpleUpdateMode: \n" + selectedPaperJournal);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		mergePaperJournal = null;
		super.switchToAddMode();
		selectedPaperJournal = new PaperJournalDTO();
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
			selectedPaperJournal.setMainAuthor(author);
		}
		if (journal != null) {
			selectedPaperJournal.setJournal(journal);
			editTabNumber = 1;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleAddMode()
	 */
	@Override
	public void switchToSimpleAddMode() {
		mergePaperJournal = null;
		simpleAuthorList = true;
		super.switchToSimpleAddMode();
		setAuthorManageBeanToPick(this);
		authorName = "";
		selectedPaperJournal = new PaperJournalDTO();
		if (journal != null) {
			selectedPaperJournal.setJournal(journal);
			editTabNumber = 0;
		}
		selectedPaperJournal.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		mergePaperJournal = null;
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToViewDetailsMode();
		else {
			selectedPaperJournal = findPaperJournalByControlNumber();
			if (selectedPaperJournal != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperJournal);
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
			selectedPaperJournal = findPaperJournalByControlNumber();
			if (selectedPaperJournal != null) {
				super.switchToSimpleViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperJournal);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		editModalPanel = "paperJournalEditModalPanel";
		try {
			debug("findSimilarPaperJournals");
			orderList = false;
			similarPaperJournals = getPaperJournals(createSimilarPaperJournalsQuery(),
					null, null,  new AllDocCollector(true));
			mergePaperJournal = null;
			editTabNumber = 3;
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
			selectedPaperJournal.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergePaperJournal = selectedPaperJournal;
		selectedPaperJournal = findPaperJournalByControlNumber(similarPaperJournals);
		if(selectedPaperJournal != null){
			if(selectedPaperJournal.getStartPage() == null)
				selectedPaperJournal.setStartPage(mergePaperJournal.getStartPage());
			if(selectedPaperJournal.getScopusID() == null || "".equals(selectedPaperJournal.getScopusID())){
				selectedPaperJournal.setScopusID(mergePaperJournal.getScopusID());
			}
			if(selectedPaperJournal.getEndPage() == null)
				selectedPaperJournal.setEndPage(mergePaperJournal.getEndPage());
			if(selectedPaperJournal.getVolume() == null)
				selectedPaperJournal.setVolume(mergePaperJournal.getVolume());
			if(selectedPaperJournal.getNumber() == null)
				selectedPaperJournal.setNumber(mergePaperJournal.getNumber());
			if(selectedPaperJournal.getDoi() == null)
				selectedPaperJournal.setDoi(mergePaperJournal.getDoi());
			if(selectedPaperJournal.getUri() == null)
				selectedPaperJournal.setUri(mergePaperJournal.getUri());
			if(selectedPaperJournal.getOtherAuthors().size() == 0)
				selectedPaperJournal.setOtherAuthors(mergePaperJournal.getOtherAuthors());
			if(selectedPaperJournal.getAbstracT().getContent() == null){
				selectedPaperJournal.getAbstracT().setContent(mergePaperJournal.getAbstracT().getContent());
				selectedPaperJournal.getAbstracT().setLanguage(mergePaperJournal.getAbstracT().getLanguage());
			}
			if(selectedPaperJournal.getPublicationYear() == null)
				selectedPaperJournal.setPublicationYear(mergePaperJournal.getPublicationYear());
		} else {
			selectedPaperJournal = mergePaperJournal;
			mergePaperJournal = null;
		}
		switchToSimpleUpdateMode();
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(PaperJournalDTO paperJournal){
		mergePaperJournal = selectedPaperJournal;

		selectedPaperJournal = paperJournal;
		if(selectedPaperJournal != null){
			if(selectedPaperJournal.getStartPage() == null)
				selectedPaperJournal.setStartPage(mergePaperJournal.getStartPage());
			if(selectedPaperJournal.getScopusID() == null || "".equals(selectedPaperJournal.getScopusID())){
				selectedPaperJournal.setScopusID(mergePaperJournal.getScopusID());
			}
			if(selectedPaperJournal.getEndPage() == null)
				selectedPaperJournal.setEndPage(mergePaperJournal.getEndPage());
			if(selectedPaperJournal.getVolume() == null)
				selectedPaperJournal.setVolume(mergePaperJournal.getVolume());
			if(selectedPaperJournal.getNumber() == null)
				selectedPaperJournal.setNumber(mergePaperJournal.getNumber());
			if(selectedPaperJournal.getDoi() == null)
				selectedPaperJournal.setDoi(mergePaperJournal.getDoi());
			if(selectedPaperJournal.getUri() == null)
				selectedPaperJournal.setUri(mergePaperJournal.getUri());
			if(selectedPaperJournal.getOtherAuthors().size() == 0)
				selectedPaperJournal.setOtherAuthors(mergePaperJournal.getOtherAuthors());
			if(selectedPaperJournal.getAbstracT().getContent() == null){
				selectedPaperJournal.getAbstracT().setContent(mergePaperJournal.getAbstracT().getContent());
				selectedPaperJournal.getAbstracT().setLanguage(mergePaperJournal.getAbstracT().getLanguage());
			}
			if(selectedPaperJournal.getPublicationYear() == null)
				selectedPaperJournal.setPublicationYear(mergePaperJournal.getPublicationYear());
		} else {
			selectedPaperJournal = mergePaperJournal;
			mergePaperJournal = null;
		}
//		switchToSimpleUpdateMode();
//		populateImportMessages();
	}
	
	public void mergeData(){
		mergePaperJournal = findPaperJournalByControlNumber(similarPaperJournals);
		if(mergePaperJournal != null){
			selectedPaperJournal.setControlNumber(mergePaperJournal.getControlNumber());
			editTabNumber = 1;
			populateImportMessages();
			if(editMode == ModesManagedBean.MODE_ADD)
				editMode = ModesManagedBean.MODE_UPDATE;
		}
	}
	
	private PaperJournalDTO mergePaperJournal;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergePaperJournal != null){
			if(editTabNumber == 0){
				if((mergePaperJournal.getTitle().getLanguage() != null) && (mergePaperJournal.getTitle().getLanguage().trim().length()>0))
					if((selectedPaperJournal.getTitle().getLanguage() != null) && (! selectedPaperJournal.getTitle().getLanguage().equals(mergePaperJournal.getTitle().getLanguage()))){
						facesMessages.addToControl(
								"paperJournalSimpleEditForm:language", FacesMessage.SEVERITY_INFO, new FacesMessages("messages.messages-records", new Locale("sr"))
								.getMessageFromResourceBundle("records.language." + mergePaperJournal.getTitle().getLanguage())
								, FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getTitle().getContent() != null) && (mergePaperJournal.getTitle().getContent().trim().length()>0))
					if((selectedPaperJournal.getTitle().getContent() != null) && (! selectedPaperJournal.getTitle().getContent().equals(mergePaperJournal.getTitle().getContent()))){
						facesMessages.addToControl(
								"paperJournalSimpleEditForm:title", FacesMessage.SEVERITY_INFO, 
								mergePaperJournal.getTitle().getContent(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getStartPage() != null) && (mergePaperJournal.getStartPage().trim().length()>0))
					if((selectedPaperJournal.getStartPage() != null) && (! selectedPaperJournal.getStartPage().equals(mergePaperJournal.getStartPage()))){
						facesMessages.addToControl(
							"paperJournalSimpleEditForm:startPage", FacesMessage.SEVERITY_INFO, 
							mergePaperJournal.getStartPage(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getEndPage() != null) && (mergePaperJournal.getEndPage().trim().length()>0))
					if((selectedPaperJournal.getEndPage() != null) && (! selectedPaperJournal.getEndPage().equals(mergePaperJournal.getEndPage()))){
						facesMessages.addToControl(
							"paperJournalSimpleEditForm:endPage", FacesMessage.SEVERITY_INFO, 
							mergePaperJournal.getEndPage(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getVolume() != null) && (mergePaperJournal.getVolume().trim().length()>0))
					if((selectedPaperJournal.getVolume() != null) && (! selectedPaperJournal.getVolume().equals(mergePaperJournal.getVolume()))){
						facesMessages.addToControl(
						"paperJournalSimpleEditForm:volume", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getVolume(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getNumber() != null) && (mergePaperJournal.getNumber().trim().length()>0))
					if((selectedPaperJournal.getNumber() != null) && (! selectedPaperJournal.getNumber().equals(mergePaperJournal.getNumber()))){
						facesMessages.addToControl(
						"paperJournalSimpleEditForm:number", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getNumber(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getPublicationYear() != null) && (mergePaperJournal.getPublicationYear().trim().length()>0))
					if((selectedPaperJournal.getPublicationYear() != null) && (! selectedPaperJournal.getPublicationYear().equals(mergePaperJournal.getPublicationYear()))){
						facesMessages.addToControl(
						"paperJournalSimpleEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getPublicationYear(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getDoi() != null) && (mergePaperJournal.getDoi().trim().length()>0))
					if((selectedPaperJournal.getDoi() != null) && (! selectedPaperJournal.getDoi().equals(mergePaperJournal.getDoi()))){
						facesMessages.addToControl(
						"paperJournalSimpleEditForm:doi", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getDoi(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperJournal.getPaperType() != null) && (mergePaperJournal.getPaperType().trim().length()>0))
					if((selectedPaperJournal.getPaperType() != null) && (! selectedPaperJournal.getPaperType().equals(mergePaperJournal.getPaperType()))){
						facesMessages.addToControl(
						"paperJournalSimpleEditForm:paperType", FacesMessage.SEVERITY_INFO, new FacesMessages("messages.messages-records", new Locale("sr"))
						.getMessageFromResourceBundle(mergePaperJournal.getPaperType())
						, FacesContext
								.getCurrentInstance());	
						retVal = true;
					}
				String listOfAuthors = "";
				for (AuthorDTO author : mergePaperJournal.getAllAuthors()) {
					if(!listOfAuthors.equals(""))
						listOfAuthors += ", ";
					listOfAuthors += author.getName().toString();
				}
				if (listOfAuthors.length() > 0)
					facesMessages.addToControl(
							"paperJournalSimpleEditForm:authorName", FacesMessage.SEVERITY_INFO, 
							listOfAuthors, FacesContext
									.getCurrentInstance());
				if(mergePaperJournal.getAllAuthors().size() != selectedPaperJournal.getAllAuthors().size())
					retVal = true;
			} 
			if(editTabNumber == 1){
				retVal = true;
				if((mergePaperJournal.getTitle().getLanguage() != null) && (mergePaperJournal.getTitle().getLanguage().trim().length()>0))
					if((selectedPaperJournal.getTitle().getLanguage() != null) && (! selectedPaperJournal.getTitle().getLanguage().equals(mergePaperJournal.getTitle().getLanguage())))
						facesMessages.addToControl(
								"paperJournalEditForm:language", FacesMessage.SEVERITY_INFO, 
								mergePaperJournal.getTitle().getLanguage(), FacesContext
									.getCurrentInstance());
				if((mergePaperJournal.getTitle().getContent() != null) && (mergePaperJournal.getTitle().getContent().trim().length()>0))
					if((selectedPaperJournal.getTitle().getContent() != null) && (! selectedPaperJournal.getTitle().getContent().equals(mergePaperJournal.getTitle().getContent())))
						facesMessages.addToControl(
								"paperJournalEditForm:title", FacesMessage.SEVERITY_INFO, 
								mergePaperJournal.getTitle().getContent(), FacesContext
									.getCurrentInstance());
				if((mergePaperJournal.getSubtitle().getContent() != null) && (mergePaperJournal.getSubtitle().getContent().trim().length()>0))
					if((selectedPaperJournal.getSubtitle().getContent() != null) && (! selectedPaperJournal.getSubtitle().getContent().equals(mergePaperJournal.getSubtitle().getContent())))
							facesMessages.addToControl(
						"paperJournalEditForm:subtitle", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getSubtitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getKeywords().getContent() != null) && (mergePaperJournal.getKeywords().getContent().trim().length()>0))
					if((selectedPaperJournal.getKeywords().getContent() != null) && (! selectedPaperJournal.getKeywords().getContent().equals(mergePaperJournal.getKeywords().getContent())))
					facesMessages.addToControl(
						"paperJournalEditForm:keywords", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getKeywords().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getNote().getContent() != null) && (mergePaperJournal.getNote().getContent().trim().length()>0))
					if((selectedPaperJournal.getNote().getContent() != null) && (! selectedPaperJournal.getNote().getContent().equals(mergePaperJournal.getNote().getContent())))
						facesMessages.addToControl(
						"paperJournalEditForm:note", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getNote().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getAbstracT().getContent() != null) && (mergePaperJournal.getAbstracT().getContent().trim().length()>0))
					if((selectedPaperJournal.getAbstracT().getContent() != null) && (! selectedPaperJournal.getAbstracT().getContent().equals(mergePaperJournal.getAbstracT().getContent())))
						facesMessages.addToControl(
						"paperJournalEditForm:abstract", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getAbstracT().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getStartPage() != null) && (mergePaperJournal.getStartPage().trim().length()>0))
					if((selectedPaperJournal.getStartPage() != null) && (! selectedPaperJournal.getStartPage().equals(mergePaperJournal.getStartPage())))
						facesMessages.addToControl(
						"paperJournalEditForm:startPage", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getStartPage(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getEndPage() != null) && (mergePaperJournal.getEndPage().trim().length()>0))
					if((selectedPaperJournal.getEndPage() != null) && (! selectedPaperJournal.getEndPage().equals(mergePaperJournal.getEndPage())))
						facesMessages.addToControl(
						"paperJournalEditForm:endPage", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getEndPage(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getVolume() != null) && (mergePaperJournal.getVolume().trim().length()>0))
					if((selectedPaperJournal.getVolume() != null) && (! selectedPaperJournal.getVolume().equals(mergePaperJournal.getVolume())))
						facesMessages.addToControl(
						"paperJournalEditForm:volume", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getVolume(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getNumber() != null) && (mergePaperJournal.getNumber().trim().length()>0))
					if((selectedPaperJournal.getNumber() != null) && (! selectedPaperJournal.getNumber().equals(mergePaperJournal.getNumber())))
						facesMessages.addToControl(
						"paperJournalEditForm:number", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getNumber(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getPublicationYear() != null) && (mergePaperJournal.getPublicationYear().trim().length()>0))
					if((selectedPaperJournal.getPublicationYear() != null) && (! selectedPaperJournal.getPublicationYear().equals(mergePaperJournal.getPublicationYear())))
						facesMessages.addToControl(
						"paperJournalEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getPublicationYear(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getDoi() != null) && (mergePaperJournal.getDoi().trim().length()>0))
					if((selectedPaperJournal.getDoi() != null) && (! selectedPaperJournal.getDoi().equals(mergePaperJournal.getDoi())))
						facesMessages.addToControl(
						"paperJournalEditForm:doi", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getDoi(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getUri() != null) && (mergePaperJournal.getUri().trim().length()>0))
					if((selectedPaperJournal.getUri() != null) && (! selectedPaperJournal.getUri().equals(mergePaperJournal.getUri())))
						facesMessages.addToControl(
						"paperJournalEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getUri(), FacesContext
								.getCurrentInstance());
				if((mergePaperJournal.getPaperType() != null) && (mergePaperJournal.getPaperType().trim().length()>0))
					if((selectedPaperJournal.getPaperType() != null) && (! selectedPaperJournal.getPaperType().equals(mergePaperJournal.getPaperType())))
						facesMessages.addToControl(
						"paperJournalEditForm:paperType", FacesMessage.SEVERITY_INFO, 
						mergePaperJournal.getPaperType(), FacesContext
								.getCurrentInstance());	
			} 
			if (editTabNumber == 2){
				retVal = true;
				String listOfAuthors = "";
				for (AuthorDTO author : mergePaperJournal.getAllAuthors()) {
					if(!listOfAuthors.equals(""))
						listOfAuthors += ", ";
					listOfAuthors += author.toString();
				}
				if (listOfAuthors.length() > 0)
					facesMessages.addToControl(
							"paperJournalEditForm:listOfAuthors", FacesMessage.SEVERITY_INFO, 
							listOfAuthors, FacesContext
									.getCurrentInstance());
			}
		}
		return retVal;
	}
	
	/**
	 * Increases the edit tab number for one
	 */
	public void nextEditTab() {
		editTabNumber++;
		if((editTabNumber == 0) && (simpleForm == true))
			populateImportMessages();
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
		if((editTabNumber == 0) && (simpleForm == true))
			populateImportMessages();
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if((selectedPaperJournal.getControlNumber() == null) || (! selectedPaperJournal.getControlNumber().contains("BISIS")))
			add();
		else {
			if(simpleForm){
				if (validateAll() == false) {
					return;
				}
			}else if (validateAuthors() == false) {
				facesMessages.addToControlFromResourceBundle(
						"paperJournalEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.paperJournal.author.name.pleaseSelect.error",
						FacesContext.getCurrentInstance());
				return;
			}
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedPaperJournal)) == false) {
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"paperJournalSimpleEditForm:general":"paperJournalEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.paperJournal.update.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				if((editTabNumber == 3) && (simpleForm == false))
					nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"paperJournalSimpleEditForm:general":"paperJournalEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.paperJournal.update.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedPaperJournal);
				changedReferenceAuthorsEmailNotification(selectedPaperJournal, facesMessages.getMessageFromResourceBundle("records.paperJournal.changedPaperJournalAuthorsNotification.subject"));
				//if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedPaperJournal, "update");
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if(simpleForm){
			if (validateAll() == false) {
				return;
			}
		} else 	if (validateAuthors() == false) {
				facesMessages.addToControlFromResourceBundle(
					"paperJournalEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
				return;
		}
		selectedPaperJournal.setControlNumber(null);
		if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperJournal)) == false) {
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperJournalSimpleEditForm:general":"paperJournalEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			if((editTabNumber == 3) && (simpleForm == false))
				nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperJournalSimpleEditForm:general":"paperJournalEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.paperJournal.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("added: \n" + selectedPaperJournal);
			newReferenceAuthorsEmailNotification(selectedPaperJournal, facesMessages.getMessageFromResourceBundle("records.paperJournal.newPaperJournalAuthorsNotification.subject"));
			newRecordEmailNotification(selectedPaperJournal, facesMessages.getMessageFromResourceBundle("records.paperJournal.newPaperJournalNotification.subject"));
			sendRecordMessage(selectedPaperJournal, "add");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPaperJournal = findPaperJournalByControlNumber();
		if (selectedPaperJournal == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperJournal)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperJournalTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedPaperJournal);
			selectedPaperJournal = null;
			populateList = true;
			orderList = true;
		}
	}

	/**
	 * Prepares web form where user can choose Journal
	 */
	public void pickJournal() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		JournalManagedBean mb = (JournalManagedBean) extCtx
				.getSessionMap().get("journalManagedBean");
		if (mb == null) {
			mb = new JournalManagedBean();
			extCtx.getSessionMap().put("journalManagedBean", mb);
		}

		mb.setIPickJournalManagedBean(this);
		mb.setSelectedJournal(null);
		mb.setPickMessageFirstTab("records.paperJournal.pickJournalMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperJournal.pickJournalMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperJournal.pickJournalMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperJournal.pickJournalMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperJournal.pickJournalMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperJournal.pickJournalMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperJournal.pickJournalMessageSecondTabSimilarExistFourthSentence");

		mb.setCustomPick(true);
		mb.switchToPickMode();
		if ((selectedPaperJournal!=null) && (selectedPaperJournal.getJournal()!=null) 
				&& (selectedPaperJournal.getJournal().getControlNumber() != null) && (mb.isCustomPick())) {
			mb.setName(selectedPaperJournal.getJournal().getName().getContent());
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#setJournal(rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO)
	 */
	public void setJournal(JournalDTO journal) {
		if ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD)) {
			selectedPaperJournal.setJournal(journal);
		} else {
			this.journal = journal;
			populateList = true;
			orderList = true;
		}
		//populateImportMessages();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#cancelPickingJournal()
	 */
	@Override
	public void cancelPickingJournal() {
	}

	/**
	 * Prepares web form where user can choose author for selected paper
	 */
	public void pickAuthor() {
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
		mb.setPickMessageFirstTab("records.paperJournal.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperJournal.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.paperJournal.pickAuthor.pleaseInstitution");
		mb.switchToPickMode();
		if(simpleForm){
			simpleFormPickAuthor(mb);
		}
	}

	/**
	 * Removes the selected author from the list of authors
	 */
	public void removeAuthor() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) 
			if(selectedPaperJournal.getOtherAuthors().contains(selectedAuthor))
				selectedPaperJournal.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedPaperJournal.getMainAuthor())){
					if(selectedPaperJournal.getOtherAuthors().size() > 0){
						selectedPaperJournal.setMainAuthor(selectedPaperJournal.getOtherAuthors().get(0));
						selectedPaperJournal.getOtherAuthors().remove(0);
					} else {
						selectedPaperJournal.setMainAuthor(new AuthorDTO());
					}
				}
	}
	
	
	/**
	 * Adds the other format name to selected author
	 */
	public void addAuthorOtherFormatName() {
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
			mb.pleaseInstitution = false;
			mb.setIPickAuthorManagedBean(this);
			mb.setSelectedAuthor((AuthorDTO)(personDAO.getDTO(selectedAuthor.getControlNumber())));
			mb.setFirstnameOtherFormat("");
			mb.setLastnameOtherFormat("");
		}
	}
	
	/**
	 * Adds the otherName to selected author
	 */
	public void editOtherName() {
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
			for (AuthorDTO dto : selectedPaperJournal.getAllAuthors()) {
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
	 * Switches the selected author with previous
	 */
	public void moveAuthorUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");

		int index = -1;
		AuthorDTO authorDTO = null;
		for (int i = 0; i < selectedPaperJournal.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperJournal.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedPaperJournal.getOtherAuthors()
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
		for (int i = 0; i < selectedPaperJournal.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperJournal.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedPaperJournal
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedPaperJournal
				.getMainAuthor() : selectedPaperJournal.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedPaperJournal
				.getMainAuthor() : selectedPaperJournal.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedPaperJournal.setMainAuthor(second);
		else
			selectedPaperJournal.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedPaperJournal.setMainAuthor(first);
		else
			selectedPaperJournal.getOtherAuthors().set(secondIndex, first);
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
		}
		else if(((mb.editMode == ModesManagedBean.MODE_UPDATE) || (mb.editMode == ModesManagedBean.MODE_NONE)) && (! simpleForm)){
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
		
		if ((selectedPaperJournal.getAllAuthors().contains(author))) {
			if (selectedPaperJournal.getMainAuthor().getControlNumber()
					.equals(author.getControlNumber())){
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperJournal.getMainAuthor().getName());
				} 
				selectedPaperJournal.setMainAuthor(author);
			}
			else {
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperJournal.getOtherAuthors().get(selectedPaperJournal.getOtherAuthors().indexOf(
							author)).getName());
				} 
				selectedPaperJournal.getOtherAuthors().set(
						selectedPaperJournal.getOtherAuthors().indexOf(
								author), author);
			}
		} else {
			if (("".equals(selectedPaperJournal.getMainAuthor()
					.getControlNumber()))
					|| (selectedPaperJournal.getMainAuthor()
							.getControlNumber() == null))
				selectedPaperJournal.setMainAuthor(author);
			else
				selectedPaperJournal.getOtherAuthors().add(author);
		}
		authorName = "";
		setAuthorManageBeanToPick(this);
		populateImportMessages();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}

	/**
	 * @return the allPaperTypesSelectItems
	 */
	public List<SelectItem> getAllPaperTypesSelectItems() {
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		if (selectedPaperJournal.getPaperType() == null) {
			retVal
					.add(new SelectItem(
							null,
							facesMessages
									.getMessageFromResourceBundle("records.paperJournal.editPanel.paperType.pleaseSelect")));
		}
		retVal
				.add(new SelectItem(
						"records.paperJournal.editPanel.paperType.paper",
						facesMessages
								.getMessageFromResourceBundle("records.paperJournal.editPanel.paperType.paper")));
		retVal
				.add(new SelectItem(
						"records.paperJournal.editPanel.paperType.scientificCriticism",
						facesMessages
								.getMessageFromResourceBundle("records.paperJournal.editPanel.paperType.scientificCriticism")));
		retVal
		.add(new SelectItem(
				"records.paperJournal.editPanel.paperType.other",
				facesMessages
						.getMessageFromResourceBundle("records.paperJournal.editPanel.paperType.other")));
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedPaperJournal = findPaperJournalByControlNumber(similarPaperJournals);
			if ((selectedPaperJournal != null) && (iPickPaperJournalManagedBean != null)){
				iPickPaperJournalManagedBean.setPaperJournal(selectedPaperJournal);
				tableTabNumber = 0;
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarPaperJournal", e);
		}
	}
	
	public void chooseSimilar(PaperJournalDTO paperJournal) {
		try {
			selectedPaperJournal = paperJournal;
			if ((selectedPaperJournal != null) && (iPickPaperJournalManagedBean != null)){
				iPickPaperJournalManagedBean.setPaperJournal(selectedPaperJournal);
				tableTabNumber = 0;
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarPaperJournal", e);
		}
	}

	private IPickPaperJournalManagedBean iPickPaperJournalManagedBean = null;

	/**
	 * @param iPickStudyFinalDocumentManagedBean
	 *            the CRUDManagedBean which wants to pick study final document
	 */
	public void setIPickPaperJournalManagedBean(
			IPickPaperJournalManagedBean iPickPaperJournalManagedBean) {
		this.iPickPaperJournalManagedBean = iPickPaperJournalManagedBean;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "paperJournalPage";
		return retVal;
	}

	private JournalDTO journal = null;

	/**
	 * @return the journal
	 */
	public JournalDTO getJournal() {
		return journal;
	}

	private PaperJournalDTO findPaperJournalByControlNumber() {
		PaperJournalDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PaperJournalDTO dto : list) {
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
	
	private PaperJournalDTO findPaperJournalByControlNumber(List<PaperJournalDTO> paperJournalsList) {
		PaperJournalDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PaperJournalDTO dto : paperJournalsList) {
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
			iPickPaperJournalManagedBean.setPaperJournal(selectedPaperJournal);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}

	private boolean validateAuthors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedPaperJournal.getAllAuthors()) {
			if (("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	
	private boolean validateLoggedAuthor() {
		boolean retVal = true;
		if(getUserManagedBean().getLoggedUser().getAuthor().getControlNumber()!=null){
			if (! selectedPaperJournal.getAllAuthors().contains(getUserManagedBean().getLoggedUser().getAuthor())) {
					retVal = false;
			}
		} else if(selectedPaperJournal.getAllAuthors().size() == 0)
				retVal = false;
		return retVal;
	}
	
	private boolean validateAll(){
		boolean retVal = true;
		if ((selectedPaperJournal.getJournal() == null) || (selectedPaperJournal.getJournal().getControlNumber() == null) || (selectedPaperJournal.getJournal().getControlNumber().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:journalName", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperJournal.getTitle() == null) || (selectedPaperJournal.getTitle().getLanguage() == null) || (selectedPaperJournal.getTitle().getLanguage().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:language", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperJournal.getSomeTitle() == null) || (selectedPaperJournal.getSomeTitle().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:title", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperJournal.getPaperType() == null) || (selectedPaperJournal.getPaperType().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:paperType", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateLoggedAuthor() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.author.name.noLoggedAuthor.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperJournalSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		return retVal;
	}

	public void titleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperJournal.getTitleTranslations(), false, "records.paperJournal.editPanel.titleTranslations.panelHeader", "records.paperJournal.editPanel.titleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperJournal.getSubtitleTranslations(), false, "records.paperJournal.editPanel.subtitleTranslations.panelHeader", "records.paperJournal.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperJournal.getNoteTranslations(), false, "records.paperJournal.editPanel.noteTranslations.panelHeader", "records.paperJournal.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperJournal.getKeywordsTranslations(), false, "records.paperJournal.editPanel.keywordsTranslations.panelHeader", "records.paperJournal.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperJournal.getAbstractTranslations(), false, "records.paperJournal.editPanel.abstractTranslations.panelHeader", "records.paperJournal.editPanel.abstractTranslations.contentHeader");
	}
	
	public void populateM99PMF(){
		populateM99(PaperJournalManagedBean.M99_PMF_WHERE_CLAUSE);
	}
	
	public void populateM99TF(){
		populateM99(PaperJournalManagedBean.M99_TF_WHERE_CLAUSE);
	}
	
	public void populateM99(String whereClause){
		populateList = false;
		orderList = true;
		orderBy = null;
		whereStr = "M99";
		
		list = new ArrayList<PaperJournalDTO>();
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null) {
			for (Record record : records) {
				list.add((PaperJournalDTO) record.getDto());
			}
		}
	}
	
}
