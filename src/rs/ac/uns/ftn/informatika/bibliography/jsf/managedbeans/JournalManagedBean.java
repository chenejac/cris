package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.lucene.search.TopDocCollector;

import org.primefaces.component.datatable.DataTable;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for journals
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JournalManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean {

	private List<JournalDTO> list;
	
	private List<JournalDTO> duplicateJournals;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private JournalDTO selectedJournal = null;

	public JournalManagedBean(){
		super();
		pickSimilarMessage = "records.journal.pickSimilarMessage";
		tableModalPanel = "journalBrowseModalPanel";
		editModalPanel = "journalEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedJournal = null;
		pickMessage = null;
		pickMessageFirstTab = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickJournalManagedBean = null;
		tableModalPanel = "journalBrowseModalPanel";
		editModalPanel = "journalEditModalPanel";
		return super.resetForm();
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<JournalDTO> listTmp = getJournals(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false) && (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedJournal != null
					&& selectedJournal.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedJournal.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("journalTable");
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
			list = new ArrayList<JournalDTO>();
		}
	}
	
	/**
	 * Retrieves a list of journals that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving journals
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of journals that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<JournalDTO> getJournals(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getJournals(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of journals that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving journals
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of journals that correspond to the query
	 */
	private List<JournalDTO> getJournals(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<JournalDTO> retVal = new ArrayList<JournalDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record recordDTO : list) {
			try {
				JournalDTO dto = (JournalDTO) recordDTO.getDto();
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
			Collections.sort(retVal, new GenericComparator<JournalDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of journals (filtered and ordered by ...)
	 */
	public List<JournalDTO> getJournals() {
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
			Collections.sort(list, new GenericComparator<JournalDTO>(
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
			bq.add(QueryUtils.makeBooleanQuery("NA", name, Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", name, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			orderList = false;
		} else if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
		return bq;
	}
	
	List<JournalDTO> similarJournals = null;
	
	/**
	 * @return the list of SIMILAR journals with selected journal
	 */
	public List<JournalDTO> getSimilarJournals() {
		return similarJournals;
	}

	/**
	 * Creates query for finding SIMILAR journals with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarJournalsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedJournal.getName().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedJournal.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedJournal.getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		else if(selectedJournal.getNameTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("NA",selectedJournal.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedJournal.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		
		if((selectedJournal.getIssn() != null) && (! "".equals(selectedJournal.getIssn()))){
			bq.add(QueryUtils.makeBooleanQuery("ISSN", selectedJournal.getIssn(), Occur.MUST, 0.99f, 0.99f, true), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
		return bq;
	}
	
	public List<JournalDTO> autocomplete(String suggest) {
		List<JournalDTO> retVal = new ArrayList<JournalDTO>();
		if(suggest.contains("(BISIS)")){
        	retVal.add((JournalDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedJournal!=null && selectedJournal.getControlNumber() != null){
        	retVal.add(selectedJournal);
        	return retVal;
        }
		
		String journalName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(journalName != null){
			bq.add(QueryUtils.makeBooleanQuery("NA", journalName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				JournalDTO dto = (JournalDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<JournalDTO> autocompleteName(String suggest) {
		List<JournalDTO> retVal = new ArrayList<JournalDTO>();
		
		String journalName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(journalName != null){
			bq.add(QueryUtils.makeBooleanQuery("NA", journalName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", journalName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new TopDocCollector(10));
		for (Record recordDTO : listRecord) {
			try {
				JournalDTO dto = (JournalDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<JournalDTO> autocompleteISSNOrName(String suggest) {
		List<JournalDTO> retVal = new ArrayList<JournalDTO>();
		String journalISSNorName = suggest;//.replace('-', '.');
        try {
	        BooleanQuery bq = new BooleanQuery();
			if(journalISSNorName != null){
				BooleanQuery bqissn = new BooleanQuery();
				bqissn.add(QueryUtils.makeBooleanQuery("ISSN", journalISSNorName + "*", Occur.MUST, 0.8f, 07f, true), Occur.MUST);
				bqissn.add(QueryUtils.makeBooleanQuery("ISSN", journalISSNorName + "*", Occur.SHOULD, 0.99f, 0.99f, true), Occur.SHOULD);
				bq.add(bqissn, Occur.SHOULD);
				BooleanQuery bqname = new BooleanQuery();
				bqname.add(QueryUtils.makeBooleanQuery("NA", journalISSNorName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bqname.add(QueryUtils.makeBooleanQuery("NA", journalISSNorName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
				bq.add(bqname, Occur.SHOULD);
			}
			bq.setMinimumNumberShouldMatch(1);
			bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
			List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
			for (Record recordDTO : listRecord) {
				try {
					JournalDTO dto = (JournalDTO) recordDTO.getDto();
					if (dto != null) {
						retVal.add(dto);
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
        }
		catch (Exception e) {
		}
		
		return retVal;
    }

	/**
	 * @return the selected journal
	 */
	public JournalDTO getSelectedJournal() {
		return selectedJournal;
	}

	/**
	 * @param journalDTO
	 *            the journal to set as selected journal
	 */
	public void setSelectedJournal(JournalDTO journalDTO) {
		selectedJournal = journalDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedJournal = findJournalByControlNumber(list);
		if (selectedJournal != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedJournal);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedJournal = new JournalDTO();
		selectedJournal.getName().setContent(name);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedJournal = findJournalByControlNumber(list);
		if (selectedJournal != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedJournal);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			orderList = false;
			similarJournals = getJournals(createSimilarJournalsQuery(),
					null, null,  new AllDocCollector(true));
			editTabNumber = 1;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("switchToImportMode", e);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		journalControlNumber = null;
		name = null;
		duplicateJournals = new ArrayList<JournalDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedJournal.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergeJournal = selectedJournal;
		
		selectedJournal = findJournalByControlNumber(similarJournals);
		if(selectedJournal == null){
			selectedJournal = mergeJournal;
			mergeJournal = null;
		} else {
			if(selectedJournal.getUri() == null)
				selectedJournal.setUri(mergeJournal.getUri());
			if(selectedJournal.getScopusID() == null || "".equals(selectedJournal.getScopusID()))
				selectedJournal.setScopusID(mergeJournal.getScopusID());
			if(selectedJournal.getIssn() == null)
				selectedJournal.setIssn(mergeJournal.getIssn());
			if(selectedJournal.getDoi() == null)
				selectedJournal.setDoi(mergeJournal.getDoi());
			if(selectedJournal.getName().getContent() == null){
				selectedJournal.getName().setContent(mergeJournal.getName().getContent());
				selectedJournal.getName().setLanguage(mergeJournal.getName().getLanguage());
			}
		}
		editTabNumber = 0;
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(JournalDTO journal){
		mergeJournal = selectedJournal;
		
		selectedJournal = journal;
		if(selectedJournal == null){
			selectedJournal = mergeJournal;
			mergeJournal = null;
		} else {
			if(selectedJournal.getUri() == null)
				selectedJournal.setUri(mergeJournal.getUri());
			if(selectedJournal.getScopusID() == null || "".equals(selectedJournal.getScopusID()))
				selectedJournal.setScopusID(mergeJournal.getScopusID());
			if(selectedJournal.getIssn() == null)
				selectedJournal.setIssn(mergeJournal.getIssn());
			if(selectedJournal.getDoi() == null)
				selectedJournal.setDoi(mergeJournal.getDoi());
			if(selectedJournal.getName().getContent() == null){
				selectedJournal.getName().setContent(mergeJournal.getName().getContent());
				selectedJournal.getName().setLanguage(mergeJournal.getName().getLanguage());
			}
		}
		editTabNumber = 0;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeJournal = findJournalByControlNumber(similarJournals);
		if(mergeJournal != null){
			selectedJournal.setControlNumber(mergeJournal.getControlNumber());
			editTabNumber = 0;
			populateImportMessages();
		}
	}
	
	private JournalDTO mergeJournal;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergeJournal != null){
			if((mergeJournal.getIssn() != null) && (mergeJournal.getIssn().trim().length()>0))
				if((selectedJournal.getIssn() != null) && (! selectedJournal.getIssn().equals(mergeJournal.getIssn())))	{
					facesMessages.addToControl(
						"journalEditForm:issn", FacesMessage.SEVERITY_INFO, 
						mergeJournal.getIssn(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeJournal.getUri() != null) && (mergeJournal.getUri().trim().length()>0))
				if((selectedJournal.getUri() != null) && (! selectedJournal.getUri().equals(mergeJournal.getUri()))){
					facesMessages.addToControl(
						"journalEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergeJournal.getUri(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeJournal.getDoi() != null) && (mergeJournal.getDoi().trim().length()>0))
				if((selectedJournal.getDoi() != null) && (! selectedJournal.getDoi().equals(mergeJournal.getDoi()))){
					facesMessages.addToControl(
							"journalEditForm:doi", FacesMessage.SEVERITY_INFO, 
							mergeJournal.getDoi(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeJournal.getName().getContent() != null) && (mergeJournal.getName().getContent().trim().length()>0))
				if((selectedJournal.getName().getContent() != null) && (! selectedJournal.getName().getContent().equals(mergeJournal.getName().getContent()))){
					facesMessages.addToControl(
							"journalEditForm:name", FacesMessage.SEVERITY_INFO, 
							mergeJournal.getName().getContent(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
		}
		return retVal;
	}
	
	/**
	 * Increases the edit tab number for one
	 */
	public void nextEditTab() {
		editTabNumber++;
		if((editTabNumber >= 0) && (editTabNumber <= 1))
			populateImportMessages();
	}

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
		if((editTabNumber >= 0) && (editTabNumber <= 1))
			populateImportMessages();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if((selectedJournal.getControlNumber() == null) || (! selectedJournal.getControlNumber().contains("BISIS")))
			add();
		else {
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
							.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							selectedJournal)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"journalEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.journal.update.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"journalEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.journal.update.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedJournal);
				if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedJournal, "update");					
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		similarJournals = null;
		if(editTabNumber == 0){
			try {
				debug("findSimilarJournals");
				similarJournals = getJournals(createSimilarJournalsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarJournals", e);
			}
		}
		if((similarJournals == null ) || (similarJournals.size()==0)){		
			selectedJournal.setControlNumber(null);
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
							.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							selectedJournal)) == false){
				facesMessages.addToControlFromResourceBundle(
						"journalEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.journal.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"journalEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.journal.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedJournal);
				newRecordEmailNotification(selectedJournal, facesMessages.getMessageFromResourceBundle("records.journal.newJournalNotification.subject"));
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
		selectedJournal = findJournalByControlNumber(list);
		if (selectedJournal == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
						selectedJournal)) == false){
			facesMessages.addToControlFromResourceBundle(
					"journalPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.journal.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedJournal);
			selectedJournal = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void chooseDuplicateJournal() {

		try {
			JournalDTO duplicateJournal = findJournalByControlNumber(list);
			if (duplicateJournal != null) {
				if(duplicateJournals.contains(duplicateJournal))
					duplicateJournals.remove(duplicateJournal);
				else
					duplicateJournals.add(duplicateJournal);
			}
		} catch (Exception e) {
			error("chooseDuplicateJournal", e);
		}
	}
	
	public void replaceDuplicateJournals(){
		try {
			selectedJournal = findJournalByControlNumber(list);
			if ((selectedJournal != null) && (!(duplicateJournals.contains(selectedJournal)))){
				
				for (JournalDTO duplicateJournal : duplicateJournals) {
					Query query = new TermQuery(new Term("JOCN", duplicateJournal.getControlNumber()));
					List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
					for (Record record : list) {
						record.loadFromDatabase();
						if(record.getDto() instanceof PaperJournalDTO){
							PaperJournalDTO temp = (PaperJournalDTO)(record.getDto());
							temp.setJournal(selectedJournal);
							if(recordDAO.update(new Record(null, null, getUserManagedBean()
									.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									temp)) == false){
								facesMessages.addToControlFromResourceBundle(
										"journalPickForm:general", FacesMessage.SEVERITY_ERROR, 
										"records.journal.replace.error", FacesContext
												.getCurrentInstance());
								return;
							} else {
								sendRecordMessage(temp, "update");
							}
						} else {
							facesMessages.addToControlFromResourceBundle(
									"journalPickForm:general", FacesMessage.SEVERITY_ERROR, 
									"records.journal.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					debug("journal: \n" + duplicateJournal +  "\n\nreplaced with: \n" + selectedJournal);
					recordDAO.delete(duplicateJournal.getRecord());
				}
				facesMessages.addToControlFromResourceBundle(
						"journalPickForm:general", FacesMessage.SEVERITY_INFO, 
						"records.journal.replace.success", FacesContext
								.getCurrentInstance());
//				sendRecordMessage(selectedJournal);
				selectedJournal = null;
				populateList = true;
				orderList = true;
				duplicateJournals = new ArrayList<JournalDTO>();
			} else {
				facesMessages.addToControlFromResourceBundle(
						"journalPickForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.journal.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateJournals", e);
		}
	}

	/**
	 * @return the duplicateJournals
	 */
	public String getDuplicateJournalsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (JournalDTO duplicateJournal : duplicateJournals) {
			retVal.append(duplicateJournal + "\n");
		}
		return retVal.toString();
	}
	
	/**
	 * Prepares web form where user can choose Editor for selected proceedings
	 */
	public void pickEditor() {
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
		mb.setPickMessageFirstTab("records.journal.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.journal.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.journal.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.journal.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.journal.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.journal.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.journal.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(false);

		mb.switchToPickMode();

	}
	
	/**
	 * Adds the other format name to selected editor
	 */
	public void addEditorOtherFormatName() {
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
	
	private AuthorDTO findAuthorByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedJournal.getEditors()) {
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
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) 
			selectedJournal.getEditors().remove(selectedAuthor);
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
		for (int i = 0; i < selectedJournal.getEditors().size(); i++) {
			authorDTO = selectedJournal.getEditors().get(i);
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
						% selectedJournal.getEditors().size());
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
		for (int i = 0; i < selectedJournal.getEditors().size(); i++) {
			authorDTO = selectedJournal.getEditors().get(i);
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
						% selectedJournal.getEditors().size());
				break;
		}
	}

	private void switchEditors(int firstIndex, int secondIndex) {
		AuthorDTO first = selectedJournal.getEditors().get(firstIndex);
		AuthorDTO second = selectedJournal.getEditors().get(secondIndex);
		selectedJournal.getEditors().set(firstIndex, second);
		selectedJournal.getEditors().set(secondIndex, first);
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
		else if((mb.editMode == ModesManagedBean.MODE_UPDATE) || (mb.editMode == ModesManagedBean.MODE_NONE)){
			List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
			otherFormatNames.add(author.getName());
			otherFormatNames.addAll(author.getOtherFormatNames());
			author.setOtherFormatNames(otherFormatNames);
			author.setName(new AuthorNameDTO());
		}
		
		if ((selectedJournal.getEditors().contains(author))) {
			selectedJournal.getEditors().set(
					selectedJournal.getEditors().indexOf(author), author);
		} else {
			selectedJournal.getEditors().add(author);
		}
		populateImportMessages();
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
	 * Adds language to the list of proceedings languages
	 */
	public void addLanguage() {
		if ((!("".equals(language))) && (!(selectedJournal.getLanguages().contains(language))))
			selectedJournal.getLanguages().add(language);
	}

	/**
	 * Removes language from the list of proceedings languages
	 */
	public void removeLanguage() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String paramLanguage = facesCtx.getExternalContext()
				.getRequestParameterMap().get("language");

		int index = -1;
		String tempLanguage = null;
		for (int i = 0; i < selectedJournal.getLocalizedLanguages().size(); i++) {
			tempLanguage = selectedJournal.getLocalizedLanguages().get(i);
			if (tempLanguage.equals(paramLanguage)) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedJournal.getLanguages().remove(index);
	}


	/**
	 * Sets chosen journal to the CRUDManagedBean which wanted to pick
	 * journal
	 */
	public void chooseJournal() {
		try {
			if(tableTabNumber != 0) 
				selectedJournal = findJournalByControlNumber(list);
			if (selectedJournal != null) {
				iPickJournalManagedBean.setJournal(selectedJournal);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseJournal", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedJournal = findJournalByControlNumber(similarJournals);
			if (selectedJournal != null) {
				iPickJournalManagedBean.setJournal(selectedJournal);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarJournal", e);
		}
	}
	
	public void chooseSimilar(JournalDTO journal) {
		try {
			selectedJournal = journal;
			if (selectedJournal != null) {
				iPickJournalManagedBean.setJournal(selectedJournal);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarJournal", e);
		}
	}
	
	private IPickJournalManagedBean iPickJournalManagedBean = null;

	/**
	 * @param iPickJournalManagedBean
	 *            the CRUDManagedBean which wants to pick journal
	 */
	public void setIPickJournalManagedBean(
			IPickJournalManagedBean iPickJournalManagedBean) {
		this.iPickJournalManagedBean = iPickJournalManagedBean;
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
	 * @return the pick message for second tab if SIMILAR journals do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR journals do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR journals do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR journals do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR journals  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR journals exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR journals exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR journals exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR journals exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR journals exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR journals exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR journals exist to set
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
		retVal = "indexPage";
		return retVal;
	}

	private String journalControlNumber;
	
	/**
	 * @return the journalControlNumber
	 */
	public String getJournalControlNumber() {
		return journalControlNumber;
	}

	/**
	 * @param journalControlNumber the journalControlNumber to set
	 */
	public void setJournalControlNumber(String journalControlNumber) {
		this.journalControlNumber = journalControlNumber;
		selectedJournal = (JournalDTO)recordDAO.getDTO(journalControlNumber);
	}
	
	private JournalDTO findJournalByControlNumber(List<JournalDTO> journalsList) {
		JournalDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (JournalDTO dto : journalsList) {
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

	private String name;

	/**
	 * @return the journal name for filtering journals
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickJournalManagedBean.cancelPickingJournal();
		}
		super.switchToTableNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)
				&& (customPick))) {
			iPickJournalManagedBean.setJournal(selectedJournal);
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#setCustomPick(boolean)
	 */
	@Override
	public void setCustomPick(boolean customPick) {
		super.setCustomPick(customPick);
		if (!customPick) {
			orderBy = "someName";
			direction = "asc";
		}
	}
	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedJournal.getNameTranslations(), false, "records.journal.editPanel.nameTranslations.panelHeader", "records.journal.editPanel.nameTranslations.contentHeader");
	}
	
	public void nameAbbreviationTranslations(){
		this.openMultilingualContentForm(editMode, selectedJournal.getNameAbbreviationTranslations(), false, "records.journal.editPanel.nameAbbreviationTranslations.panelHeader", "records.journal.editPanel.nameAbbreviationTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedJournal.getNoteTranslations(), false, "records.journal.editPanel.noteTranslations.panelHeader", "records.journal.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedJournal.getKeywordsTranslations(), false, "records.journal.editPanel.keywordsTranslations.panelHeader", "records.journal.editPanel.keywordsTranslations.contentHeader");
	}

}
