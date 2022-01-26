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
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
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
public class PaperProceedingsManagedBean extends CRUDManagedBean implements
		IPickProceedingsManagedBean, IPickConferenceManagedBean, IPickAuthorManagedBean {

	private List<PaperProceedingsDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private PaperProceedingsDTO selectedPaperProceedings = null;
	
	public static String M99_PMF_WHERE_CLAUSE = "ARCHIVED != 100 and RECORDID in (SELECT RECORDID from MARC21RECORD_CLASS where CFCLASSID in ('paperProceedings', 'abstractPP', 'fullPP', 'invTalkAbstractPP', 'invTalkFullPP', 'discussionPP'))  " +
	"and RECORDID in (SELECT RECORDID2 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorshipType' and RECORDID1 in (SELECT RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929' and CFSTARTDATE like '2021-01-01 00:00:00'))  " +
	"and RECORDID not in (SELECT RECORDID from MARC21RECORD_CLASS where COMMISSIONID in (711, 712, 713, 714, 715, 721, 722, 723, 724, 725))";

	public static String M99_TF_WHERE_CLAUSE = "ARCHIVED != 100 and RECORDID in (SELECT RECORDID from MARC21RECORD_CLASS where CFCLASSID in ('paperProceedings', 'abstractPP', 'fullPP', 'invTalkAbstractPP', 'invTalkFullPP', 'discussionPP')) " +
	"and RECORDID in (SELECT RECORDID2 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorshipType' and RECORDID1 in (SELECT RECORDID1 from MARC21RECORD_MARC21RECORD where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5933' and CFSTARTDATE like '2021-01-01 00:00:00')) " +
	"and RECORDID not in (SELECT RECORDID from MARC21RECORD_CLASS where COMMISSIONID in (701))";


	/**
	 * 
	 */
	public PaperProceedingsManagedBean() {
		super();
		tableModalPanel = "paperProceedingsBrowseModalPanel";
		editModalPanel = "paperProceedingsEditModalPanel";
		simpleEditModalPanel = "paperProceedingsSimpleEditModalPanel";
		pickSimilarMessage = "records.paperProceedings.pickSimilarMessage";
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPaperProceedings = null;
		proceedings = null;
		conference = null;
		conferenceProceedings = null;
		tableModalPanel = "paperProceedingsBrowseModalPanel";
		editModalPanel = "paperProceedingsEditModalPanel";
		simpleEditModalPanel = "paperProceedingsSimpleEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<PaperProceedingsDTO> listTmp = getPaperProceedings(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedPaperProceedings != null
					&& selectedPaperProceedings.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPaperProceedings.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("paperProceedingsTable");
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
			list = new ArrayList<PaperProceedingsDTO>();
		}
	}
	
	/**
	 * Retrieves a list of papers (published in proceedings) that correspond to
	 * the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in proceedings) that correspond to the
	 *         query
	 */
	@SuppressWarnings("unused")
	private List<PaperProceedingsDTO> getPaperProceedings(String query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getPaperProceedings(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of papers (published in proceedings) that correspond to
	 * the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in proceedings) that correspond to the
	 *         query
	 */
	private List<PaperProceedingsDTO> getPaperProceedings(Query query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		List<PaperProceedingsDTO> retVal = new ArrayList<PaperProceedingsDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				PaperProceedingsDTO dto = (PaperProceedingsDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<PaperProceedingsDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of papers published in proceedings (filtered and ordered
	 *         by ...)
	 */
	public List<PaperProceedingsDTO> getPaperProceedings() {
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
			Collections.sort(list, new GenericComparator<PaperProceedingsDTO>(
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
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser()
				.getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		if (proceedings != null)
			bq.add(new TermQuery(new Term("PRCN", proceedings.getControlNumber())), Occur.MUST);
		return bq;
	}
	
	public List<PaperProceedingsDTO> autocompleteTitle(String suggest) {
		List<PaperProceedingsDTO> retVal = new ArrayList<PaperProceedingsDTO>();
		
		String paperProceedingsTitle = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(paperProceedingsTitle != null){
			bq.add(QueryUtils.makeBooleanQuery("TI", paperProceedingsTitle + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", paperProceedingsTitle + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		bq.add(type, Occur.MUST);

		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				PaperProceedingsDTO dto = (PaperProceedingsDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	List<PaperProceedingsDTO> similarPaperProceedings = null;
	
	/**
	 * @return the list of SIMILAR paper proceedings with selected paper proceedings
	 */
	public List<PaperProceedingsDTO> getSimilarPaperProceedings() {
		return similarPaperProceedings;
	}

	/**
	 * Creates query for finding SIMILAR paper proceedings with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarPaperProceedingsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedPaperProceedings.getTitle().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedPaperProceedings.getTitle().getContent(), Occur.SHOULD, 0.7f, 0.7f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedPaperProceedings.getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		else if(selectedPaperProceedings.getTitleTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("TI",selectedPaperProceedings.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.7f, 0.7f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("TI",selectedPaperProceedings.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);	
		}
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected paper published in proceedings
	 */
	public PaperProceedingsDTO getSelectedPaperProceedings() {
		return selectedPaperProceedings;
	}

	/**
	 * @param paperProceedingsDTO
	 *            the paper published in proceedings to set as selected paper
	 */
	public void setSelectedPaperProceedings(
			PaperProceedingsDTO paperProceedingsDTO) {
		selectedPaperProceedings = paperProceedingsDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedPaperProceedings = findPaperProceedingsByControlNumber();
			if (selectedPaperProceedings != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedPaperProceedings);
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
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToSimpleUpdateMode();
		else {
			selectedPaperProceedings = findPaperProceedingsByControlNumber();
			if (selectedPaperProceedings != null) {
				super.switchToSimpleUpdateMode();
				debug("switchToSimpleUpdateMode: \n" + selectedPaperProceedings);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedPaperProceedings = new PaperProceedingsDTO();
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
			selectedPaperProceedings.setMainAuthor(author);
		}
		if (proceedings != null) {
			selectedPaperProceedings.setProceedings(proceedings);
			editTabNumber = 1;
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleAddMode()
	 */
	@Override
	public void switchToSimpleAddMode() {
		simpleAuthorList = true;
		super.switchToSimpleAddMode();
		setAuthorManageBeanToPick(this);
		authorName = "";
		selectedPaperProceedings = new PaperProceedingsDTO();
		if (proceedings != null) {
			selectedPaperProceedings.setProceedings(proceedings);
			editTabNumber = 0;
		}
		selectedPaperProceedings.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToViewDetailsMode();
		else {
			selectedPaperProceedings = findPaperProceedingsByControlNumber();
			if (selectedPaperProceedings != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperProceedings);
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
			selectedPaperProceedings = findPaperProceedingsByControlNumber();
			if (selectedPaperProceedings != null) {
				super.switchToSimpleViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperProceedings);
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		editModalPanel = "paperProceedingsEditModalPanel";
		try {
			debug("switchToImportMode");
			orderList = false;
			similarPaperProceedings = getPaperProceedings(createSimilarPaperProceedingsQuery(),
					null, null,  new AllDocCollector(true));
			mergePaperProceedings = null;
			editTabNumber = 3;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("switchToImportMode", e);
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
			selectedPaperProceedings.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergePaperProceedings = selectedPaperProceedings;
		selectedPaperProceedings = findPaperProceedingsByControlNumber(similarPaperProceedings);
		if(selectedPaperProceedings != null){
			if(selectedPaperProceedings.getScopusID() == null || "".equals(selectedPaperProceedings.getScopusID())){
				selectedPaperProceedings.setScopusID(mergePaperProceedings.getScopusID());
			}
			if(selectedPaperProceedings.getStartPage() == null)
				selectedPaperProceedings.setStartPage(mergePaperProceedings.getStartPage());
			if(selectedPaperProceedings.getEndPage() == null)
				selectedPaperProceedings.setEndPage(mergePaperProceedings.getEndPage());
			if(selectedPaperProceedings.getDoi() == null)
				selectedPaperProceedings.setDoi(mergePaperProceedings.getDoi());
			if(selectedPaperProceedings.getUri() == null)
				selectedPaperProceedings.setUri(mergePaperProceedings.getUri());
			if(selectedPaperProceedings.getOtherAuthors().size() == 0)
				selectedPaperProceedings.setOtherAuthors(mergePaperProceedings.getOtherAuthors());
			if(selectedPaperProceedings.getAbstracT().getContent() == null){
				selectedPaperProceedings.getAbstracT().setContent(mergePaperProceedings.getAbstracT().getContent());
				selectedPaperProceedings.getAbstracT().setLanguage(mergePaperProceedings.getAbstracT().getLanguage());
			}
			if(selectedPaperProceedings.getPublicationYear() == null)
				selectedPaperProceedings.setPublicationYear(mergePaperProceedings.getPublicationYear());
			if(selectedPaperProceedings.getPaperType() == null)
				selectedPaperProceedings.setPaperType(mergePaperProceedings.getPaperType());
		} else {
			selectedPaperProceedings = mergePaperProceedings;
			mergePaperProceedings = null;
		}
		switchToSimpleUpdateMode();
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(PaperProceedingsDTO paperProceedings){
		mergePaperProceedings = selectedPaperProceedings;

		selectedPaperProceedings = paperProceedings;
		if(selectedPaperProceedings != null){
			if(selectedPaperProceedings.getScopusID() == null || "".equals(selectedPaperProceedings.getScopusID())){
				selectedPaperProceedings.setScopusID(mergePaperProceedings.getScopusID());
			}
			if(selectedPaperProceedings.getStartPage() == null)
				selectedPaperProceedings.setStartPage(mergePaperProceedings.getStartPage());
			if(selectedPaperProceedings.getEndPage() == null)
				selectedPaperProceedings.setEndPage(mergePaperProceedings.getEndPage());
			if(selectedPaperProceedings.getDoi() == null)
				selectedPaperProceedings.setDoi(mergePaperProceedings.getDoi());
			if(selectedPaperProceedings.getUri() == null)
				selectedPaperProceedings.setUri(mergePaperProceedings.getUri());
			if(selectedPaperProceedings.getOtherAuthors().size() == 0)
				selectedPaperProceedings.setOtherAuthors(mergePaperProceedings.getOtherAuthors());
			if(selectedPaperProceedings.getAbstracT().getContent() == null){
				selectedPaperProceedings.getAbstracT().setContent(mergePaperProceedings.getAbstracT().getContent());
				selectedPaperProceedings.getAbstracT().setLanguage(mergePaperProceedings.getAbstracT().getLanguage());
			}
			if(selectedPaperProceedings.getPublicationYear() == null)
				selectedPaperProceedings.setPublicationYear(mergePaperProceedings.getPublicationYear());
			if(selectedPaperProceedings.getPaperType() == null)
				selectedPaperProceedings.setPaperType(mergePaperProceedings.getPaperType());
		} else {
			selectedPaperProceedings = mergePaperProceedings;
			mergePaperProceedings = null;
		}
//		switchToSimpleUpdateMode();
//		populateImportMessages();
	}
	
	public void mergeData(){
		mergePaperProceedings = findPaperProceedingsByControlNumber(similarPaperProceedings);
		if(mergePaperProceedings != null){
			selectedPaperProceedings.setControlNumber(mergePaperProceedings.getControlNumber());
			editTabNumber = 1;
			populateImportMessages();
			if(editMode == ModesManagedBean.MODE_ADD)
				editMode = ModesManagedBean.MODE_UPDATE;
		}
	}
	
	private PaperProceedingsDTO mergePaperProceedings;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergePaperProceedings != null){
			if(editTabNumber == 0){
				if((mergePaperProceedings.getTitle().getLanguage() != null) && (mergePaperProceedings.getTitle().getLanguage().trim().length()>0))
					if((selectedPaperProceedings.getTitle().getLanguage() != null) && (! selectedPaperProceedings.getTitle().getLanguage().equals(mergePaperProceedings.getTitle().getLanguage()))){
						facesMessages.addToControl(
								"paperProceedingsSimpleEditForm:language", FacesMessage.SEVERITY_INFO, new FacesMessages("messages.messages-records", new Locale("sr"))
								.getMessageFromResourceBundle("records.language." + mergePaperProceedings.getTitle().getLanguage())
								, FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getTitle().getContent() != null) && (mergePaperProceedings.getTitle().getContent().trim().length()>0))
					if((selectedPaperProceedings.getTitle().getContent() != null) && (! selectedPaperProceedings.getTitle().getContent().equals(mergePaperProceedings.getTitle().getContent()))){
						facesMessages.addToControl(
								"paperProceedingsSimpleEditForm:title", FacesMessage.SEVERITY_INFO, 
								mergePaperProceedings.getTitle().getContent(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getStartPage() != null) && (mergePaperProceedings.getStartPage().trim().length()>0))
					if((selectedPaperProceedings.getStartPage() != null) && (! selectedPaperProceedings.getStartPage().equals(mergePaperProceedings.getStartPage()))){
						facesMessages.addToControl(
							"paperProceedingsSimpleEditForm:startPage", FacesMessage.SEVERITY_INFO, 
							mergePaperProceedings.getStartPage(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getEndPage() != null) && (mergePaperProceedings.getEndPage().trim().length()>0))
					if((selectedPaperProceedings.getEndPage() != null) && (! selectedPaperProceedings.getEndPage().equals(mergePaperProceedings.getEndPage()))){
						facesMessages.addToControl(
							"paperProceedingsSimpleEditForm:endPage", FacesMessage.SEVERITY_INFO, 
							mergePaperProceedings.getEndPage(), FacesContext
									.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getPublicationYear() != null) && (mergePaperProceedings.getPublicationYear().trim().length()>0))
					if((selectedPaperProceedings.getPublicationYear() != null) && (! selectedPaperProceedings.getPublicationYear().equals(mergePaperProceedings.getPublicationYear()))){
						facesMessages.addToControl(
						"paperProceedingsSimpleEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getPublicationYear(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getDoi() != null) && (mergePaperProceedings.getDoi().trim().length()>0))
					if((selectedPaperProceedings.getDoi() != null) && (! selectedPaperProceedings.getDoi().equals(mergePaperProceedings.getDoi()))){
						facesMessages.addToControl(
						"paperProceedingsSimpleEditForm:doi", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getDoi(), FacesContext
								.getCurrentInstance());
						retVal = true;
					}
				if((mergePaperProceedings.getPaperType() != null) && (mergePaperProceedings.getPaperType().trim().length()>0))
					if((selectedPaperProceedings.getPaperType() != null) && (! selectedPaperProceedings.getPaperType().equals(mergePaperProceedings.getPaperType()))){
						facesMessages.addToControl(
						"paperProceedingsSimpleEditForm:paperType", FacesMessage.SEVERITY_INFO, new FacesMessages("messages.messages-records", new Locale("sr"))
						.getMessageFromResourceBundle(mergePaperProceedings.getPaperType())
						, FacesContext
								.getCurrentInstance());	
						retVal = true;
					}
				String listOfAuthors = "";
				for (AuthorDTO author : mergePaperProceedings.getAllAuthors()) {
					if(!listOfAuthors.equals(""))
						listOfAuthors += ", ";
					listOfAuthors += author.getName().toString();
				}
				if (listOfAuthors.length() > 0)
					facesMessages.addToControl(
							"paperProceedingsSimpleEditForm:authorName", FacesMessage.SEVERITY_INFO, 
							listOfAuthors, FacesContext
									.getCurrentInstance());
				if(mergePaperProceedings.getAllAuthors().size() != selectedPaperProceedings.getAllAuthors().size())
					retVal = true;
			} 
			if(editTabNumber == 1){
				retVal = true;
				if((mergePaperProceedings.getTitle().getLanguage() != null) && (mergePaperProceedings.getTitle().getLanguage().trim().length()>0))
					if((selectedPaperProceedings.getTitle().getLanguage() != null) && (! selectedPaperProceedings.getTitle().getLanguage().equals(mergePaperProceedings.getTitle().getLanguage())))
						facesMessages.addToControl(
								"paperJournalEditForm:language", FacesMessage.SEVERITY_INFO, 
								mergePaperProceedings.getTitle().getLanguage(), FacesContext
									.getCurrentInstance());
				if((mergePaperProceedings.getTitle().getContent() != null) && (mergePaperProceedings.getTitle().getContent().trim().length()>0))
					if((selectedPaperProceedings.getTitle().getContent() != null) && (! selectedPaperProceedings.getTitle().getContent().equals(mergePaperProceedings.getTitle().getContent())))
						facesMessages.addToControl(
								"paperJournalEditForm:title", FacesMessage.SEVERITY_INFO, 
								mergePaperProceedings.getTitle().getContent(), FacesContext
									.getCurrentInstance());
				if((mergePaperProceedings.getSubtitle().getContent() != null) && (mergePaperProceedings.getSubtitle().getContent().trim().length()>0))
					if((selectedPaperProceedings.getSubtitle().getContent() != null) && (! selectedPaperProceedings.getSubtitle().getContent().equals(mergePaperProceedings.getSubtitle().getContent())))
							facesMessages.addToControl(
						"paperJournalEditForm:subtitle", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getSubtitle().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getKeywords().getContent() != null) && (mergePaperProceedings.getKeywords().getContent().trim().length()>0))
					if((selectedPaperProceedings.getKeywords().getContent() != null) && (! selectedPaperProceedings.getKeywords().getContent().equals(mergePaperProceedings.getKeywords().getContent())))
					facesMessages.addToControl(
						"paperJournalEditForm:keywords", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getKeywords().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getNote().getContent() != null) && (mergePaperProceedings.getNote().getContent().trim().length()>0))
					if((selectedPaperProceedings.getNote().getContent() != null) && (! selectedPaperProceedings.getNote().getContent().equals(mergePaperProceedings.getNote().getContent())))
						facesMessages.addToControl(
						"paperJournalEditForm:note", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getNote().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getAbstracT().getContent() != null) && (mergePaperProceedings.getAbstracT().getContent().trim().length()>0))
					if((selectedPaperProceedings.getAbstracT().getContent() != null) && (! selectedPaperProceedings.getAbstracT().getContent().equals(mergePaperProceedings.getAbstracT().getContent())))
						facesMessages.addToControl(
						"paperJournalEditForm:abstract", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getAbstracT().getContent(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getStartPage() != null) && (mergePaperProceedings.getStartPage().trim().length()>0))
					if((selectedPaperProceedings.getStartPage() != null) && (! selectedPaperProceedings.getStartPage().equals(mergePaperProceedings.getStartPage())))
						facesMessages.addToControl(
						"paperJournalEditForm:startPage", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getStartPage(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getEndPage() != null) && (mergePaperProceedings.getEndPage().trim().length()>0))
					if((selectedPaperProceedings.getEndPage() != null) && (! selectedPaperProceedings.getEndPage().equals(mergePaperProceedings.getEndPage())))
						facesMessages.addToControl(
						"paperJournalEditForm:endPage", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getEndPage(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getPublicationYear() != null) && (mergePaperProceedings.getPublicationYear().trim().length()>0))
					if((selectedPaperProceedings.getPublicationYear() != null) && (! selectedPaperProceedings.getPublicationYear().equals(mergePaperProceedings.getPublicationYear())))
						facesMessages.addToControl(
						"paperJournalEditForm:publicationYear", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getPublicationYear(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getDoi() != null) && (mergePaperProceedings.getDoi().trim().length()>0))
					if((selectedPaperProceedings.getDoi() != null) && (! selectedPaperProceedings.getDoi().equals(mergePaperProceedings.getDoi())))
						facesMessages.addToControl(
						"paperJournalEditForm:doi", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getDoi(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getUri() != null) && (mergePaperProceedings.getUri().trim().length()>0))
					if((selectedPaperProceedings.getUri() != null) && (! selectedPaperProceedings.getUri().equals(mergePaperProceedings.getUri())))
						facesMessages.addToControl(
						"paperJournalEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getUri(), FacesContext
								.getCurrentInstance());
				if((mergePaperProceedings.getPaperType() != null) && (mergePaperProceedings.getPaperType().trim().length()>0))
					if((selectedPaperProceedings.getPaperType() != null) && (! selectedPaperProceedings.getPaperType().equals(mergePaperProceedings.getPaperType())))
						facesMessages.addToControl(
						"paperJournalEditForm:paperType", FacesMessage.SEVERITY_INFO, 
						mergePaperProceedings.getPaperType(), FacesContext
								.getCurrentInstance());	
			} 
			if (editTabNumber == 2){
				retVal = true;
				String listOfAuthors = "";
				for (AuthorDTO author : mergePaperProceedings.getAllAuthors()) {
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
		try {
			if (editTabNumber == 1) {
				if (selectedPaperProceedings.getStartPage() != null) {
					if (selectedPaperProceedings.getEndPage() != null)
						selectedPaperProceedings
								.setNumberOfPages(Integer.parseInt(selectedPaperProceedings
										.getEndPage())
										- Integer.parseInt(selectedPaperProceedings.getStartPage())
										+ 1);
					else if (selectedPaperProceedings.getNumberOfPages() != null)
						selectedPaperProceedings
								.setEndPage(new Integer(Integer.parseInt(selectedPaperProceedings.getStartPage())
										+ selectedPaperProceedings.getNumberOfPages() - 1).toString());
				}
			}
		} catch (Exception e) {
		}
		if ((editTabNumber == 0) && (selectedPaperProceedings.getProceedings().getControlNumber() == null)){
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperProceedings.proceedings.pleaseSelect.error",
					FacesContext.getCurrentInstance());
		}
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
		if((selectedPaperProceedings.getControlNumber() == null) || (! selectedPaperProceedings.getControlNumber().contains("BISIS")))
			add();
		else {
			if(simpleForm){
				if (validateAll() == false) {
					return;
				}
			}else if (validateAuthors() == false) {
				facesMessages.addToControlFromResourceBundle(
						"paperProceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.paperProceedings.author.name.pleaseSelect.error",
						FacesContext.getCurrentInstance());
				return;
			}
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedPaperProceedings)) == false) {
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"paperProceedingsSimpleEditForm:general":"paperProceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.paperProceedings.update.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				if((editTabNumber == 3) && (simpleForm == false))
					nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"paperProceedingsSimpleEditForm:general":"paperProceedingsEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.paperProceedings.update.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedPaperProceedings);
				changedReferenceAuthorsEmailNotification(selectedPaperProceedings, facesMessages.getMessageFromResourceBundle("records.paperProceedings.changedPaperProceedingsAuthorsNotification.subject"));
				sendRecordMessage(selectedPaperProceedings, "update");
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
					"paperProceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperProceedings.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperProceedings)) == false) {
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperProceedingsSimpleEditForm:general":"paperProceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperProceedings.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			if((editTabNumber == 3) && (simpleForm == false))
				nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperProceedingsSimpleEditForm:general":"paperProceedingsEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.paperProceedings.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("added: \n" + selectedPaperProceedings);
			newReferenceAuthorsEmailNotification(selectedPaperProceedings, facesMessages.getMessageFromResourceBundle("records.paperProceedings.newPaperProceedingsAuthorsNotification.subject"));
			newRecordEmailNotification(selectedPaperProceedings, facesMessages.getMessageFromResourceBundle("records.paperProceedings.newPaperProceedingsNotification.subject"));
			sendRecordMessage(selectedPaperProceedings, "add");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPaperProceedings = findPaperProceedingsByControlNumber();
		if (selectedPaperProceedings == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperProceedings)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.paperProceedings.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedPaperProceedings);
			selectedPaperProceedings = null;
			populateList = true;
			orderList = true;
		}
	}
	
	private List<Record> conferenceProceedings = null;
	
	/**
	 * @return the conferenceProceedings
	 */
	public List<Record> getConferenceProceedings() {
		return conferenceProceedings;
	}

	/**
	 * @param conferenceProceedings the conferenceProceedings to set
	 */
	public void setConferenceProceedings(List<Record> conferenceProceedings) {
		this.conferenceProceedings = conferenceProceedings;
	}

	private ProceedingsDTO proceedings = null;

	/**
	 * Prepares web form where user can choose Proceedings
	 */
	public void pickProceedings() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ProceedingsManagedBean mb = (ProceedingsManagedBean) extCtx
				.getSessionMap().get("proceedingsManagedBean");
		if (mb == null) {
			mb = new ProceedingsManagedBean();
			extCtx.getSessionMap().put("proceedingsManagedBean", mb);
		}

		mb.setIPickProceedingsManagedBean(this);
		mb.setSelectedProceedings(null);
		mb.setCustomPick(true);
		mb.switchToPickMode();
		if ((conference != null) && (editMode == ModesManagedBean.MODE_NONE)){
			mb.setConference(conference);
			mb.setTableTabNumber(1);
			mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarNotExistFirstSentence");
			mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarNotExistSecondSentence");
			mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistFirstSentence");
			mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistSecondSentence");
			mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistThirdSentence");
			mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistFourthSentence");
		} else {
			mb.setConference(selectedPaperProceedings.getProceedings().getConference());
			mb.setTableTabNumber(1);
			mb.setPickMessageFirstTab("records.paperProceedings.pickProceedingsMessageFirstTab");
			mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarNotExistFirstSentence");
			mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarNotExistSecondSentence");
			mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistFirstSentence");
			mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistSecondSentence");
			mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistThirdSentence");
			mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperProceedings.conferenceChoosed.pickProceedingsMessageSecondTabSimilarExistFourthSentence");
		}
//		if ((selectedPaperProceedings!=null) && (selectedPaperProceedings.getProceedings()!=null) 
//				&& (selectedPaperProceedings.getProceedings().getControlNumber() != null) && (mb.isCustomPick()) &&  (editMode == ModesManagedBean.MODE_UPDATE)) {
//			mb.setTitle(selectedPaperProceedings.getProceedings().getTitle());
//			mb.setConferenceName(selectedPaperProceedings.getProceedings()
//					.getConference().getName());
//			mb.setConferenceYear(selectedPaperProceedings.getProceedings()
//					.getConference().getYear());
//		}
	}
	
	/**
	 * Prepares web form where user can choose Proceedings
	 */
	public void addProceedings() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ProceedingsManagedBean mb = (ProceedingsManagedBean) extCtx
				.getSessionMap().get("proceedingsManagedBean");
		if (mb == null) {
			mb = new ProceedingsManagedBean();
			extCtx.getSessionMap().put("proceedingsManagedBean", mb);
		}

		mb.setIPickProceedingsManagedBean(this);
		mb.setSelectedProceedings(null);
		if ((conference != null) && (editMode == ModesManagedBean.MODE_NONE)){
			mb.setConference(conference);
		} else {
			mb.setConference(selectedPaperProceedings.getProceedings().getConference());
		}
		mb.switchToAddMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickProceedingsManagedBean#setProceedings(rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO)
	 */
	public void setProceedings(ProceedingsDTO proceedings) {
		if (proceedings != null){
			if ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD)) {
				selectedPaperProceedings.setProceedings(proceedings);
			} else {
				BooleanQuery bq = new BooleanQuery();
				bq.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
				bq.add(new TermQuery(new Term("COCN", conference.getControlNumber())), Occur.MUST);
				conferenceProceedings = recordDAO.getDTOs(bq, new AllDocCollector(false));
				this.proceedings = proceedings;
				this.conference = proceedings.getConference();
				populateList = true;
				orderList = true;
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickProceedingsManagedBean#cancelPickingProceedings()
	 */
	@Override
	public void cancelPickingProceedings() {
	}
	
	/**
	 * @return the proceedings
	 */
	public ProceedingsDTO getProceedings() {
		return proceedings;
	}
	
	private ConferenceDTO conference = null;

	
	/**
	 * Prepares web form where user can choose Conference for
	 * papers from proceedings
	 */
	public void pickConference() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ConferenceManagedBean mb = (ConferenceManagedBean) extCtx
				.getSessionMap().get("conferenceManagedBean");
		if (mb == null) {
			mb = new ConferenceManagedBean();
			extCtx.getSessionMap().put("conferenceManagedBean", mb);
		}

		mb.setIPickConferenceManagedBean(this);
		mb.setSelectedConference(null);
		mb.setPickMessageFirstTab("records.paperProceedings.pickConferenceMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperProceedings.pickConferenceMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);

		mb.switchToPickMode();
		
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#cancelPickingConference()
	 */
	@Override
	public void cancelPickingConference() {
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#setConference(rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO)
	 */
	@Override
	public void setConference(ConferenceDTO conference) {
		if ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD)) {
			ProceedingsDTO tempProceedings = new ProceedingsDTO();
			tempProceedings.setConference(conference);
			selectedPaperProceedings.setProceedings(tempProceedings);
		} else {
			this.conference = conference;
			proceedings = null;
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
			bq.add(new TermQuery(new Term("COCN", conference.getControlNumber())), Occur.MUST);
			conferenceProceedings = recordDAO.getDTOs(bq, new AllDocCollector(false));
		}
	}
	
	/**
	 * @return the conference
	 */
	public ConferenceDTO getConference() {
		return conference;
	}

	/**
	 * Prepares web form where user can choose proceedings for selected paper
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
		mb.setPickMessageFirstTab("records.paperProceedings.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperProceedings.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.paperProceedings.pickAuthor.pleaseInstitution");
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
			if(selectedPaperProceedings.getOtherAuthors().contains(selectedAuthor))
				selectedPaperProceedings.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedPaperProceedings.getMainAuthor())){
					if(selectedPaperProceedings.getOtherAuthors().size() > 0){
						selectedPaperProceedings.setMainAuthor(selectedPaperProceedings.getOtherAuthors().get(0));
						selectedPaperProceedings.getOtherAuthors().remove(0);
					} else {
						selectedPaperProceedings.setMainAuthor(new AuthorDTO());
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
			for (AuthorDTO dto : selectedPaperProceedings.getAllAuthors()) {
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
		for (int i = 0; i < selectedPaperProceedings.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperProceedings.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedPaperProceedings.getOtherAuthors()
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
		for (int i = 0; i < selectedPaperProceedings.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperProceedings.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedPaperProceedings
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedPaperProceedings
				.getMainAuthor() : selectedPaperProceedings.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedPaperProceedings
				.getMainAuthor() : selectedPaperProceedings.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedPaperProceedings.setMainAuthor(second);
		else
			selectedPaperProceedings.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedPaperProceedings.setMainAuthor(first);
		else
			selectedPaperProceedings.getOtherAuthors().set(secondIndex, first);
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
		
		if ((selectedPaperProceedings.getAllAuthors().contains(author))) {
			if (selectedPaperProceedings.getMainAuthor().getControlNumber()
					.equals(author.getControlNumber())){
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperProceedings.getMainAuthor().getName());
				} 
				selectedPaperProceedings.setMainAuthor(author);
			}else {
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperProceedings.getOtherAuthors().get(selectedPaperProceedings.getOtherAuthors().indexOf(
							author)).getName());
				} 
				selectedPaperProceedings.getOtherAuthors().set(
						selectedPaperProceedings.getOtherAuthors().indexOf(
								author), author);
			}
		} else {
			if (("".equals(selectedPaperProceedings.getMainAuthor()
					.getControlNumber()))
					|| (selectedPaperProceedings.getMainAuthor()
							.getControlNumber() == null))
				selectedPaperProceedings.setMainAuthor(author);
			else
				selectedPaperProceedings.getOtherAuthors().add(author);
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
		if (selectedPaperProceedings.getPaperType() == null) {
			retVal
					.add(new SelectItem(
							null,
							facesMessages
									.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.pleaseSelect")));
		}
		retVal
				.add(new SelectItem(
						"records.paperProceedings.editPanel.paperType.full",
						facesMessages
								.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.full")));
		retVal
				.add(new SelectItem(
						"records.paperProceedings.editPanel.paperType.abstract",
						facesMessages
								.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.abstract")));
		retVal
				.add(new SelectItem(
						"records.paperProceedings.editPanel.paperType.invitedTalkFull",
						facesMessages
								.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.invitedTalkFull")));
		retVal
				.add(new SelectItem(
						"records.paperProceedings.editPanel.paperType.invitedTalkAbstract",
						facesMessages
								.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.invitedTalkAbstract")));
		retVal
				.add(new SelectItem(
						"records.paperProceedings.editPanel.paperType.discussion",
						facesMessages
								.getMessageFromResourceBundle("records.paperProceedings.editPanel.paperType.discussion")));
		return retVal;
	}
	
	/**
	 * @return the allPaperTypesSelectItems
	 */
	public List<SelectItem> getAllProceedingsSelectItems() {
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		if (proceedings == null) {
			retVal
					.add(new SelectItem(
							null,
							facesMessages
									.getMessageFromResourceBundle("records.paperProceedings.tablePanel.proceedings.pleaseSelect")));
		}
		for (Record proc : conferenceProceedings) {
			retVal
			.add(new SelectItem(
					(ProceedingsDTO)proc.getDto(),
					((ProceedingsDTO)proc.getDto()).getStringRepresentation()));
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedPaperProceedings = findPaperProceedingsByControlNumber(similarPaperProceedings);
			if ((selectedPaperProceedings != null) && (iPickPaperProceedingsManagedBean != null)){
				iPickPaperProceedingsManagedBean.setPaperProceedings(selectedPaperProceedings);
				tableTabNumber = 0;
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarPaperJournal", e);
		}
	}
	
	public void chooseSimilar(PaperProceedingsDTO paperProceedings) {
		try {
			selectedPaperProceedings = paperProceedings;
			if ((selectedPaperProceedings != null) && (iPickPaperProceedingsManagedBean != null)){
				iPickPaperProceedingsManagedBean.setPaperProceedings(selectedPaperProceedings);
				tableTabNumber = 0;
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarPaperJournal", e);
		}
	}

	private IPickPaperProceedingsManagedBean iPickPaperProceedingsManagedBean = null;

	/**
	 * @param iPickStudyFinalDocumentManagedBean
	 *            the CRUDManagedBean which wants to pick study final document
	 */
	public void setIPickPaperProceedingsManagedBean(
			IPickPaperProceedingsManagedBean iPickPaperProceedingsManagedBean) {
		this.iPickPaperProceedingsManagedBean = iPickPaperProceedingsManagedBean;
	}

	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "paperProceedingsPage";
		return retVal;
	}

	private PaperProceedingsDTO findPaperProceedingsByControlNumber() {
		PaperProceedingsDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PaperProceedingsDTO dto : list) {
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
	
	private PaperProceedingsDTO findPaperProceedingsByControlNumber(List<PaperProceedingsDTO> paperProceedingsList) {
		PaperProceedingsDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PaperProceedingsDTO dto : paperProceedingsList) {
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
			iPickPaperProceedingsManagedBean.setPaperProceedings(selectedPaperProceedings);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}

	private boolean validateAuthors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedPaperProceedings.getAllAuthors()) {
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
			if (! selectedPaperProceedings.getAllAuthors().contains(getUserManagedBean().getLoggedUser().getAuthor())) {
					retVal = false;
			}
		} else if(selectedPaperProceedings.getAllAuthors().size() == 0)
				retVal = false;
		return retVal;
	}
	
	private boolean validateAll(){
		boolean retVal = true;
		if ((selectedPaperProceedings.getProceedings() == null) || (selectedPaperProceedings.getProceedings().getControlNumber() == null) || (selectedPaperProceedings.getProceedings().getControlNumber().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:proceedingsTitle", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperProceedings.getTitle() == null) || (selectedPaperProceedings.getTitle().getLanguage() == null) || (selectedPaperProceedings.getTitle().getLanguage().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:language", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperProceedings.getSomeTitle() == null) || (selectedPaperProceedings.getSomeTitle().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:title", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperProceedings.getPaperType() == null) || (selectedPaperProceedings.getPaperType().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:paperType", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateLoggedAuthor() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.author.name.noLoggedAuthor.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperProceedingsSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperJournal.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		return retVal;
	}
	
	public void titleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperProceedings.getTitleTranslations(), false, "records.paperProceedings.editPanel.titleTranslations.panelHeader", "records.paperProceedings.editPanel.titleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperProceedings.getSubtitleTranslations(), false, "records.paperProceedings.editPanel.subtitleTranslations.panelHeader", "records.paperProceedings.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperProceedings.getNoteTranslations(), false, "records.paperProceedings.editPanel.noteTranslations.panelHeader", "records.paperProceedings.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperProceedings.getKeywordsTranslations(), false, "records.paperProceedings.editPanel.keywordsTranslations.panelHeader", "records.paperProceedings.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperProceedings.getAbstractTranslations(), false, "records.paperProceedings.editPanel.abstractTranslations.panelHeader", "records.paperProceedings.editPanel.abstractTranslations.contentHeader");
	}
	
	public void populateM99PMF(){
		populateM99(PaperProceedingsManagedBean.M99_PMF_WHERE_CLAUSE);
	}
	
	public void populateM99TF(){
		populateM99(PaperProceedingsManagedBean.M99_TF_WHERE_CLAUSE);
	}
	
	public void populateM99(String whereClause){
		populateList = false;
		orderList = true;
		orderBy = null;
		whereStr = "M99";
		
		list = new ArrayList<PaperProceedingsDTO>();
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null) {
			for (Record record : records) {
				list.add((PaperProceedingsDTO) record.getDto());
			}
		}
	}
	
}