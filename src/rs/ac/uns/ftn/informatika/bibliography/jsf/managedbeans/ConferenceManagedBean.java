package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.lucene.search.TopDocCollector;

import org.primefaces.component.datatable.DataTable;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
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
 * Managed bean with CRUD operations for conferences
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ConferenceManagedBean extends CRUDManagedBean {

	private List<ConferenceDTO> list;
	
	private List<ConferenceDTO> duplicateConferences;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private ConferenceDTO selectedConference = null;

	public ConferenceManagedBean(){
		super();
		pickSimilarMessage = "records.conference.pickSimilarMessage";
		tableModalPanel = "conferenceBrowseModalPanel";
		editModalPanel = "conferenceEditModalPanel";
		pickSimilarMessage = "records.conference.pickSimilarMessage";
	}



	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedConference = null;
		pickMessage = null;
		pickMessageFirstTab = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickConferenceManagedBean = null;
		tableModalPanel = "conferenceBrowseModalPanel";
		editModalPanel = "conferenceEditModalPanel";
		return super.resetForm();
	}

	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<ConferenceDTO> listTmp = getConferences(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedConference != null
					&& selectedConference.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedConference.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("conferenceTable");
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
			list = new ArrayList<ConferenceDTO>();
		}
	}
	
	/**
	 * Retrieves a list of conferences that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving conferences
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of conferences that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<ConferenceDTO> getConferences(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getConferences(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of conferences that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving conferences
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of conferences that correspond to the query
	 */
	private List<ConferenceDTO> getConferences(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<ConferenceDTO> retVal = new ArrayList<ConferenceDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				ConferenceDTO dto = (ConferenceDTO) record.getDto();
				if (dto != null)
					retVal.add(dto);
			} catch (Exception e) {
				error("getConferences", e);
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
			Collections.sort(retVal, new GenericComparator<ConferenceDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of conferences (filtered and ordered by ...)
	 */
	public List<ConferenceDTO> getConferences() {
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
			Collections.sort(list, new GenericComparator<ConferenceDTO>(
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
			if ((year != null) && (!"".equals(year))){
				bq.add(QueryUtils.makeBooleanQuery("YE", year + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("YE", year, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
		} else if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
		return bq;
	}
	
	List<ConferenceDTO> similarConferences = null;
	
	/**
	 * @return the list of SIMILAR conferences with selected conference
	 */
	public List<ConferenceDTO> getSimilarConferences() {
		return similarConferences;
	}

	/**
	 * Creates query for finding SIMILAR conferences with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarConferencesQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedConference.getName().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedConference.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedConference.getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}else if(selectedConference.getNameTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedConference.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedConference.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);	
		}
		bq.add(new TermQuery(new Term("YE", selectedConference.getYear().toString())), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
		return bq;
	}
	
	public List<ConferenceDTO> autocomplete(String suggest) {
		List<ConferenceDTO> retVal = new ArrayList<ConferenceDTO>();
		if(suggest.contains("(BISIS)")){
        	retVal.add((ConferenceDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedConference!=null && selectedConference.getControlNumber() != null){
        	retVal.add(selectedConference);
        	return retVal;
        }
		
		String conferenceName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(conferenceName != null)
			bq.add(QueryUtils.makeBooleanQuery("NA", conferenceName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		if ((year != null) && (!"".equals(year)))
			bq.add(QueryUtils.makeBooleanQuery("YE", year + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				ConferenceDTO dto = (ConferenceDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }

	public List<String> autocompleteForSearch(String suggest) {
		List<String> retVal = new ArrayList<String>();
		if(suggest.contains("(BISIS)")){
        	retVal.add(((ConferenceDTO)recordDAO.getDTO(suggest)).getSomeName());
        	return retVal;
        }
		
		String conferenceName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(conferenceName != null)
			bq.add(QueryUtils.makeBooleanQuery("NA", conferenceName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		if ((year != null) && (!"".equals(year)))
			bq.add(QueryUtils.makeBooleanQuery("YE", year + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new TopDocCollector(10));
		for (Record recordDTO : listRecord) {
			try {
				ConferenceDTO dto = (ConferenceDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto.getSomeName());
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	/**
	 * @return the selected conference
	 */
	public ConferenceDTO getSelectedConference() {
		return selectedConference;
	}

	/**
	 * @param conferenceDTO
	 *            the conference to set as selected conference
	 */
	public void setSelectedConference(ConferenceDTO conferenceDTO) {
		selectedConference = conferenceDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedConference = findConferenceByControlNumber(list);
		if (selectedConference != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedConference);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedConference = new ConferenceDTO();
		selectedConference.getName().setContent(name);
		try {
			selectedConference.setYear(Integer.parseInt(year));
		} catch (Exception e) {
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedConference = findConferenceByControlNumber(list);
		if (selectedConference != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedConference);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			orderList = false;
			similarConferences = getConferences(createSimilarConferencesQuery(),
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
		conferenceControlNumber = null;
		name = null;
		year = null;
		duplicateConferences = new ArrayList<ConferenceDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedConference.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergeConference = selectedConference;
		
		selectedConference = findConferenceByControlNumber(similarConferences);
		if(selectedConference == null){
			selectedConference = mergeConference;
			mergeConference = null;
		} else {
			if(selectedConference.getUri() == null)
				selectedConference.setUri(mergeConference.getUri());
			if(selectedConference.getScopusID() == null || "".equals(selectedConference.getScopusID()))
				selectedConference.setScopusID(mergeConference.getScopusID());
			if(selectedConference.getPlace() == null)
				selectedConference.setPlace(mergeConference.getPlace());
			if(selectedConference.getState() == null)
				selectedConference.setState(mergeConference.getState());
			if(selectedConference.getPeriod() == null)
				selectedConference.setPeriod(mergeConference.getPeriod());
			if(selectedConference.getNumber() == null)
				selectedConference.setNumber(mergeConference.getNumber());
			if(selectedConference.getYear() == null)
				selectedConference.setYear(mergeConference.getYear());
			if(selectedConference.getName().getContent() == null){
				selectedConference.getName().setContent(mergeConference.getName().getContent());
				selectedConference.getName().setLanguage(mergeConference.getName().getLanguage());
			}
		}
		editTabNumber = 0;
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(ConferenceDTO conference){
		mergeConference = selectedConference;
		
		selectedConference = conference;
		if(selectedConference == null){
			selectedConference = mergeConference;
			mergeConference = null;
		} else {
			if(selectedConference.getUri() == null)
				selectedConference.setUri(mergeConference.getUri());
			if(selectedConference.getScopusID() == null || "".equals(selectedConference.getScopusID()))
				selectedConference.setScopusID(mergeConference.getScopusID());
			if(selectedConference.getPlace() == null)
				selectedConference.setPlace(mergeConference.getPlace());
			if(selectedConference.getState() == null)
				selectedConference.setState(mergeConference.getState());
			if(selectedConference.getPeriod() == null)
				selectedConference.setPeriod(mergeConference.getPeriod());
			if(selectedConference.getNumber() == null)
				selectedConference.setNumber(mergeConference.getNumber());
			if(selectedConference.getYear() == null)
				selectedConference.setYear(mergeConference.getYear());
			if(selectedConference.getName().getContent() == null){
				selectedConference.getName().setContent(mergeConference.getName().getContent());
				selectedConference.getName().setLanguage(mergeConference.getName().getLanguage());
			}
		}
		editTabNumber = 0;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeConference = findConferenceByControlNumber(similarConferences);
		if(mergeConference != null){
			selectedConference.setControlNumber(mergeConference.getControlNumber());
			editTabNumber = 0;
			populateImportMessages();
		}
	}
	
	private ConferenceDTO mergeConference;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergeConference != null){
			if((mergeConference.getUri() != null) && (mergeConference.getUri().trim().length()>0))
				if((selectedConference.getUri() != null) && (! selectedConference.getUri().equals(mergeConference.getUri()))){
					facesMessages.addToControl(
						"conferenceEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergeConference.getUri(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeConference.getPlace() != null) && (mergeConference.getPlace().trim().length()>0))
				if((selectedConference.getPlace() != null) && (! selectedConference.getPlace().equals(mergeConference.getPlace())))	{
					facesMessages.addToControl(
						"conferenceEditForm:place", FacesMessage.SEVERITY_INFO, 
						mergeConference.getPlace(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeConference.getState() != null) && (mergeConference.getState().trim().length()>0))
				if((selectedConference.getState() != null) && (! selectedConference.getState().equals(mergeConference.getState()))){
					facesMessages.addToControl(
							"conferenceEditForm:state", FacesMessage.SEVERITY_INFO, 
							mergeConference.getState(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeConference.getPeriod() != null) && (mergeConference.getPeriod().trim().length()>0))
				if((selectedConference.getPeriod() != null) && (! selectedConference.getPeriod().equals(mergeConference.getPeriod()))){
					facesMessages.addToControl(
							"conferenceEditForm:period", FacesMessage.SEVERITY_INFO, 
							mergeConference.getPeriod(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeConference.getNumber() != null) && (mergeConference.getNumber().trim().length()>0))
				if((selectedConference.getNumber() != null) && (! selectedConference.getNumber().equals(mergeConference.getNumber()))){
					facesMessages.addToControl(
							"conferenceEditForm:number", FacesMessage.SEVERITY_INFO, 
							mergeConference.getNumber(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if(mergeConference.getYear() != null)
				if((selectedConference.getYear() != null) && (! selectedConference.getYear().equals(mergeConference.getYear()))){
					facesMessages.addToControl(
							"conferenceEditForm:year", FacesMessage.SEVERITY_INFO, 
							mergeConference.getYear().toString(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeConference.getName().getContent() != null) && (mergeConference.getName().getContent().trim().length()>0))
				if((selectedConference.getName().getContent() != null) && (! selectedConference.getName().getContent().equals(mergeConference.getName().getContent()))){
					facesMessages.addToControl(
							"conferenceEditForm:name", FacesMessage.SEVERITY_INFO, 
							mergeConference.getName().getContent(), FacesContext
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
		if((selectedConference.getControlNumber() == null) || (! selectedConference.getControlNumber().contains("BISIS")))
			add();
		else {
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.EVENT, 
				selectedConference)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"conferenceEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.conference.update.error", FacesContext
							.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"conferenceEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.conference.update.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedConference);
				if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedConference, "update");
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		similarConferences = null;
		if(editTabNumber == 0){
			try {
				debug("findSimilarConferences");
				similarConferences = getConferences(createSimilarConferencesQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarConferences", e);
			}
		}
		if((similarConferences == null ) || (similarConferences.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.EVENT, 
					selectedConference)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"conferenceEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.conference.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"conferenceEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.conference.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedConference);
				newRecordEmailNotification(selectedConference, facesMessages.getMessageFromResourceBundle("records.conference.newConferenceNotification.subject"));
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
		selectedConference = findConferenceByControlNumber(list);
		if (selectedConference == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.EVENT, 
				selectedConference)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"conferencePickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.conference.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedConference);
			selectedConference = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void chooseDuplicateConference() {

		try {
			ConferenceDTO duplicateConference = findConferenceByControlNumber(list);
			if (duplicateConference != null) {
				if(duplicateConferences.contains(duplicateConference))
					duplicateConferences.remove(duplicateConference);
				else
					duplicateConferences.add(duplicateConference);
			}
		} catch (Exception e) {
			error("chooseDuplicateConference", e);
		}
	}
	
	public void replaceDuplicateConferences(){
		try {
			selectedConference = findConferenceByControlNumber(list);
			if ((selectedConference != null) && (!(duplicateConferences.contains(selectedConference)))){
				
				for (ConferenceDTO duplicateConference : duplicateConferences) {
					Query query = new TermQuery(new Term("COCN", duplicateConference.getControlNumber()));
					List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
					for (Record record : list) {
						record.loadFromDatabase();
						if(record.getDto() instanceof ProceedingsDTO){
							ProceedingsDTO temp = (ProceedingsDTO)(record.getDto());
							temp.setConference(selectedConference);
							if(recordDAO.update(new Record(null, null, getUserManagedBean()
									.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									temp)) == false){
								facesMessages.addToControlFromResourceBundle(
										"conferencePickForm:general", FacesMessage.SEVERITY_ERROR, 
										"records.conference.replace.error", FacesContext
												.getCurrentInstance());
								return;
							} else {
								sendRecordMessage(temp, "update");
							}
						} else {
							facesMessages.addToControlFromResourceBundle(
									"conferencePickForm:general", FacesMessage.SEVERITY_ERROR, 
									"records.conference.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					debug("conference: \n" + duplicateConference.toString() +  "\n\nreplaced with: \n" + selectedConference.toString());
					recordDAO.delete(duplicateConference.getRecord());
				}
				facesMessages.addToControlFromResourceBundle(
						"conferencePickForm:general", FacesMessage.SEVERITY_INFO, 
						"records.conference.replace.success", FacesContext
								.getCurrentInstance());
				selectedConference = null;
				populateList = true;
				orderList = true;
				duplicateConferences = new ArrayList<ConferenceDTO>();
			} else {
				facesMessages.addToControlFromResourceBundle(
						"conferencePickForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.conference.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateConferences", e);
		}
	}

	/**
	 * @return the duplicateConferences
	 */
	public String getDuplicateConferencesAsString() {
		StringBuffer retVal = new StringBuffer();
		for (ConferenceDTO duplicateConference : duplicateConferences) {
			retVal.append(duplicateConference + "\n");
		}
		return retVal.toString();
	}

	/**
	 * Sets chosen conference to the CRUDManagedBean which wanted to pick
	 * conference
	 */
	public void chooseConference() {

		try {
			if(tableTabNumber != 0) 
				selectedConference = findConferenceByControlNumber(list);
			if (selectedConference != null) {
				iPickConferenceManagedBean.setConference(selectedConference);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseConference", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedConference = findConferenceByControlNumber(similarConferences);
			if (selectedConference != null) {
				iPickConferenceManagedBean.setConference(selectedConference);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarConference", e);
		}
	}
	
	public void chooseSimilar(ConferenceDTO conference) {
		try {
			selectedConference = conference;
			if (selectedConference != null) {
				iPickConferenceManagedBean.setConference(selectedConference);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarJournal", e);
		}
	}
	

	private IPickConferenceManagedBean iPickConferenceManagedBean = null;

	/**
	 * @param iPickConferenceManagedBean
	 *            the CRUDManagedBean which wants to pick conference
	 */
	public void setIPickConferenceManagedBean(
			IPickConferenceManagedBean iPickConferenceManagedBean) {
		this.iPickConferenceManagedBean = iPickConferenceManagedBean;
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
	 * @return the pick message for second tab if SIMILAR conferences do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR conferences do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR conferences do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR conferences do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR conferences  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR conferences exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR conferences exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR conferences exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR conferences exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR conferences exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR conferences exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR conferences exist to set
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

	private String conferenceControlNumber;
	
	/**
	 * @return the conferenceControlNumber
	 */
	public String getConferenceControlNumber() {
		return conferenceControlNumber;
	}

	/**
	 * @param conferenceControlNumber the conferenceControlNumber to set
	 */
	public void setConferenceControlNumber(String conferenceControlNumber) {
		this.conferenceControlNumber = conferenceControlNumber;
		selectedConference = (ConferenceDTO)recordDAO.getDTO(conferenceControlNumber);
	}

	private ConferenceDTO findConferenceByControlNumber(List<ConferenceDTO> conferencesList) {
		ConferenceDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (ConferenceDTO dto : conferencesList) {
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

	private String year;

	/**
	 * @return the conference name for filtering conferences
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
	 * @return the conference year for filtering conferences
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickConferenceManagedBean.cancelPickingConference();
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
			iPickConferenceManagedBean.setConference(selectedConference);
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
		this.openMultilingualContentForm(editMode, selectedConference.getNameTranslations(), false, "records.conference.editPanel.nameTranslations.panelHeader", "records.conference.editPanel.nameTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedConference.getDescriptionTranslations(), false, "records.conference.editPanel.descriptionTranslations.panelHeader", "records.conference.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedConference.getKeywordsTranslations(), false, "records.conference.editPanel.keywordsTranslations.panelHeader", "records.conference.editPanel.keywordsTranslations.contentHeader");
	}

}
