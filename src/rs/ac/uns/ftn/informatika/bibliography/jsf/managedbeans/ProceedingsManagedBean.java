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
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
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
 * Managed bean with CRUD operations for proceedings
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ProceedingsManagedBean extends CRUDManagedBean implements
		IPickConferenceManagedBean, IPickAuthorManagedBean {

	private List<ProceedingsDTO> list;
	
	private List<ProceedingsDTO> duplicateProceedings;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private ProceedingsDTO selectedProceedings = null;

	public ProceedingsManagedBean(){
		super();
		pickSimilarMessage = "records.proceedings.pickSimilarMessage";
		tableModalPanel = "proceedingsBrowseModalPanel";
		editModalPanel = "proceedingsEditModalPanel";
		pickSimilarMessage = "records.proceedings.pickSimilarMessage";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedProceedings = null;
		pickMessage = null;
		conference = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickProceedingsManagedBean = null;
		tableModalPanel = "proceedingsBrowseModalPanel";
		editModalPanel = "proceedingsEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");

			List<ProceedingsDTO> listTmp = getProceedings(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false) && (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedProceedings != null
					&& selectedProceedings.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedProceedings.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("proceedingsTable");
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
			list = new ArrayList<ProceedingsDTO>();
		}
	}
	
	/**
	 * Retrieves a list of proceedings that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving proceedings
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of proceedings that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<ProceedingsDTO> getProceedings(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getProceedings(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of proceedings that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving proceedings
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of proceedings that correspond to the query
	 */
	private List<ProceedingsDTO> getProceedings(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<ProceedingsDTO> retVal = new ArrayList<ProceedingsDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				ProceedingsDTO dto = (ProceedingsDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<ProceedingsDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of proceedings (filtered and ordered by ...)
	 */
	public List<ProceedingsDTO> getProceedings() {
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
			Collections.sort(list, new GenericComparator<ProceedingsDTO>(
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
		if(conference !=null){
			bq.add(new TermQuery(new Term("COCN", conference.getControlNumber())), Occur.MUST);
		}
		if((conference == null)){
			if((tableMode == ModesManagedBean.MODE_PICK) && (customPick)){
				if((title!=null) && (!("".equals(title)))){
					bq.add(QueryUtils.makeBooleanQuery("TI", title, Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
					bq.add(QueryUtils.makeBooleanQuery("TI", title, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
				}
				bq.add(QueryUtils.makeBooleanQuery("NA", conferenceName, Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("NA", conferenceName, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
				bq.add(new TermQuery(new Term("YE", conferenceYear.toString())), Occur.MUST);
				orderList = false;
			} else if ((whereStr != null) && (!"".equals(whereStr))) 
				bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
		return bq;
	}
	
	List<ProceedingsDTO> similarProceedings = null;
	
	/**
	 * @return the list of SIMILAR proceedings with selected proceedings
	 */
	public List<ProceedingsDTO> getSimilarProceedings() {
		return similarProceedings;
	}

	/**
	 * Creates query for finding SIMILAR proceedings with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarProceedingsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedProceedings.getTitle().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedProceedings.getTitle().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedProceedings.getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		else if(selectedProceedings.getTitleTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedProceedings.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("TI", selectedProceedings.getTitleTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);	
		}if(selectedProceedings.getConference().getName().getContent()!=null){
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedProceedings.getConference().getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedProceedings.getConference().getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		else if(selectedProceedings.getConference().getNameTranslations().size() > 0){
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedProceedings.getConference().getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedProceedings.getConference().getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("YE", selectedProceedings.getConference().getYear().toString())), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected proceedings
	 */
	public ProceedingsDTO getSelectedProceedings() {
		return selectedProceedings;
	}

	/**
	 * @param proceedingsDTO
	 *            the proceedings to set as selected proceedings
	 */
	public void setSelectedProceedings(ProceedingsDTO proceedingsDTO) {
		selectedProceedings = proceedingsDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedProceedings = findProceedingsByControlNumber(list);
		if (selectedProceedings != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedProceedings);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedProceedings = new ProceedingsDTO();
		selectedProceedings.getTitle().setContent(title);
		if(conference!=null){
			selectedProceedings.setConference(conference);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedProceedings = findProceedingsByControlNumber(list);
		if (selectedProceedings != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedProceedings);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		conference = null;
		super.switchToBrowseMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		title = null;
		conference = null;
		duplicateProceedings = new ArrayList<ProceedingsDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			orderList = false;
			similarProceedings = getProceedings(createSimilarProceedingsQuery(),
					null, null,  new AllDocCollector(true));
			editTabNumber = 3;
			super.switchToImportMode();
		} catch (ParseException e) {
			error("switchToImportMode", e);
		}
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		conference = null;
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickProceedingsManagedBean.cancelPickingProceedings();
			setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToTableNoneMode();
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedProceedings.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}
	
	public void examineData(){
		mergeProceedings = selectedProceedings;
		
		selectedProceedings = findProceedingsByControlNumber(similarProceedings);
		if(selectedProceedings == null){
			selectedProceedings = mergeProceedings;
			mergeProceedings = null;
		} else {
			if(selectedProceedings.getUri() == null)
				selectedProceedings.setUri(mergeProceedings.getUri());
			if(selectedProceedings.getScopusID() == null || "".equals(selectedProceedings.getScopusID()))
				selectedProceedings.setScopusID(mergeProceedings.getScopusID());
			if(selectedProceedings.getIsbn() == null)
				selectedProceedings.setIsbn(mergeProceedings.getIsbn());
			if(selectedProceedings.getDoi() == null)
				selectedProceedings.setDoi(mergeProceedings.getDoi());
			if(selectedProceedings.getTitle().getContent() == null){
				selectedProceedings.getTitle().setContent(mergeProceedings.getTitle().getContent());
				selectedProceedings.getTitle().setLanguage(mergeProceedings.getTitle().getLanguage());
			}
		}
		editTabNumber = 0;
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(ProceedingsDTO proceedings){
		mergeProceedings = selectedProceedings;
		
		selectedProceedings = proceedings;
		if(selectedProceedings == null){
			selectedProceedings = mergeProceedings;
			mergeProceedings = null;
		} else {
			if(selectedProceedings.getUri() == null)
				selectedProceedings.setUri(mergeProceedings.getUri());
			if(selectedProceedings.getScopusID() == null || "".equals(selectedProceedings.getScopusID()))
				selectedProceedings.setScopusID(mergeProceedings.getScopusID());
			if(selectedProceedings.getIsbn() == null)
				selectedProceedings.setIsbn(mergeProceedings.getIsbn());
			if(selectedProceedings.getDoi() == null)
				selectedProceedings.setDoi(mergeProceedings.getDoi());
			if(selectedProceedings.getTitle().getContent() == null){
				selectedProceedings.getTitle().setContent(mergeProceedings.getTitle().getContent());
				selectedProceedings.getTitle().setLanguage(mergeProceedings.getTitle().getLanguage());
			}
		}
		editTabNumber = 0;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeProceedings = findProceedingsByControlNumber(similarProceedings);
		if(mergeProceedings != null){
			selectedProceedings.setControlNumber(mergeProceedings.getControlNumber());
			editTabNumber = 0;
			populateImportMessages();
		}
	}
	
	private ProceedingsDTO mergeProceedings;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergeProceedings != null){
			if((mergeProceedings.getIsbn() != null) && (mergeProceedings.getIsbn().trim().length()>0))
				if((selectedProceedings.getIsbn() != null) && (! selectedProceedings.getIsbn().equals(mergeProceedings.getIsbn())))	{
					facesMessages.addToControl(
						"proceedingsEditForm:isbn", FacesMessage.SEVERITY_INFO, 
						mergeProceedings.getIsbn(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeProceedings.getUri() != null) && (mergeProceedings.getUri().trim().length()>0))
				if((selectedProceedings.getUri() != null) && (! selectedProceedings.getUri().equals(mergeProceedings.getUri()))){
					facesMessages.addToControl(
						"proceedingsEditForm:uri", FacesMessage.SEVERITY_INFO, 
						mergeProceedings.getUri(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeProceedings.getDoi() != null) && (mergeProceedings.getDoi().trim().length()>0))
				if((selectedProceedings.getDoi() != null) && (! selectedProceedings.getDoi().equals(mergeProceedings.getDoi()))){
					facesMessages.addToControl(
							"proceedingsEditForm:doi", FacesMessage.SEVERITY_INFO, 
							mergeProceedings.getDoi(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeProceedings.getTitle().getContent() != null) && (mergeProceedings.getTitle().getContent().trim().length()>0))
				if((selectedProceedings.getTitle().getContent() != null) && (! selectedProceedings.getTitle().getContent().equals(mergeProceedings.getTitle().getContent()))){
					facesMessages.addToControl(
							"proceedingsEditForm:title", FacesMessage.SEVERITY_INFO, 
							mergeProceedings.getTitle().getContent(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
		}
		return retVal;
	}
	
	

	/**
	 * Decreases edit tab number for one
	 */
	public void previousEditTab() {
		editTabNumber--;
		if((editTabNumber >= 1) && (editTabNumber <= 2))
			populateImportMessages();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if((selectedProceedings.getControlNumber() == null) || (! selectedProceedings.getControlNumber().contains("BISIS")))
			add();
		else {
			if (recordDAO.update(new Record(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedProceedings)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"proceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.proceedings.update.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"proceedingsEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.proceedings.update.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedProceedings);
				if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedProceedings, "update");
			}
		}
	}

	@Override
	public void add() {
		similarProceedings = null;
		if(editTabNumber == 2){
			try {
				debug("findSimilarProceedings");
				similarProceedings = getProceedings(createSimilarProceedingsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarProceedings", e);
			}
		}
		if((similarProceedings == null ) || (similarProceedings.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					selectedProceedings)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"proceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.proceedings.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 2)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"proceedingsEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.proceedings.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedProceedings);
				newRecordEmailNotification(selectedProceedings, facesMessages.getMessageFromResourceBundle("records.proceedings.newProceedingsNotification.subject"));
			}
		} else {
			nextEditTab();
		}
	}

	@Override
	public void delete() {
		selectedProceedings = findProceedingsByControlNumber(list);
		if (selectedProceedings == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedProceedings)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"proceedingsPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.proceedings.general.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedProceedings);
			selectedProceedings = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void chooseDuplicateProceedings() {

		try {
			ProceedingsDTO duplicProceedings = findProceedingsByControlNumber(list);
			if (duplicProceedings != null)  {
				if((duplicateProceedings.contains(duplicProceedings)))
					duplicateProceedings.remove(duplicProceedings);
				else
					duplicateProceedings.add(duplicProceedings);
			}
		} catch (Exception e) {
			error("chooseDuplicateProceedings", e);
		}
	}
	
	public void replaceDuplicateProceedings(){
		try {
			selectedProceedings = findProceedingsByControlNumber(list);
			if ((selectedProceedings != null) && (!(duplicateProceedings.contains(selectedProceedings)))){
				
				for (ProceedingsDTO duplicProceedings : duplicateProceedings) {
					Query query = new TermQuery(new Term("PRCN", duplicProceedings.getControlNumber()));
					List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
					for (Record record : list) {
						record.loadFromDatabase();
						if(record.getDto() instanceof PaperProceedingsDTO){
							PaperProceedingsDTO temp = (PaperProceedingsDTO)(record.getDto());
							temp.setProceedings(selectedProceedings);
							if(recordDAO.update(new Record(null, null, getUserManagedBean()
									.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
									temp)) == false){
								facesMessages.addToControlFromResourceBundle(
										"proceedingsPickForm:general", FacesMessage.SEVERITY_ERROR, 
										"records.proceedings.replace.error", FacesContext
												.getCurrentInstance());
								return;
							} else {
								sendRecordMessage(temp, "update");
							}
						} else {
							facesMessages.addToControlFromResourceBundle(
									"proceedingsPickForm:general", FacesMessage.SEVERITY_ERROR, 
									"records.proceedings.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					debug("proceedings: \n" + duplicProceedings +  "\n\nreplaced with: \n" + selectedProceedings);
					recordDAO.delete(duplicProceedings.getRecord());
				}
				facesMessages.addToControlFromResourceBundle(
						"proceedingsPickForm:general", FacesMessage.SEVERITY_INFO, 
						"records.proceedings.replace.success", FacesContext
								.getCurrentInstance());
				selectedProceedings = null;
				populateList = true;
				orderList = true;
				duplicateProceedings = new ArrayList<ProceedingsDTO>();
			} else {
				facesMessages.addToControlFromResourceBundle(
						"proceedingsPickForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.proceedings.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateProceedings", e);
		}
	}

	/**
	 * @return the duplicateProceedings
	 */
	public String getDuplicateProceedingsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (ProceedingsDTO duplicProceedings : duplicateProceedings) {
			retVal.append(duplicProceedings + "\n");
		}
		return retVal.toString();
	}


	private ConferenceDTO conference = null;
	
	/**
	 * Prepares web form where user can choose Conference for selected
	 * proceedings
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
		mb.setPickMessageFirstTab("records.proceedings.pickConferenceMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.proceedings.pickConferenceMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.proceedings.pickConferenceMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.proceedings.pickConferenceMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.proceedings.pickConferenceMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.proceedings.pickConferenceMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.proceedings.pickConferenceMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);

		mb.switchToPickMode();

		if ((editMode == ModesManagedBean.MODE_ADD)
				&& (tableMode == ModesManagedBean.MODE_PICK)
				&& (customPick)
				&& (selectedProceedings.getConference().getControlNumber() == null)) {
			mb.setName(conferenceName);
			mb.setYear(conferenceYear);
		} else if ((selectedProceedings.getConference().getControlNumber() != null)
				&& (mb.isCustomPick())) {
			mb.setName(selectedProceedings.getConference().getName().getContent());
			mb.setYear(selectedProceedings.getConference().getYear().toString());
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#setConference(rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO)
	 */
	@Override
	public void setConference(ConferenceDTO conference) {
		if(editMode == ModesManagedBean.MODE_NONE){
			this.conference = conference;
			populateList = true;
			orderList = true;
		} else {
			selectedProceedings.setConference(conference);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#cancelPickingConference()
	 */
	@Override
	public void cancelPickingConference() {
//		if (selectedProceedings.getConference().getControlNumber() == null)
//			editMode = ModesManagedBean.MODE_NONE;
	}

	/**
	 * @return the conference
	 */
	public ConferenceDTO getConference() {
		return conference;
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
		mb.setPickMessageFirstTab("records.proceedings.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.proceedings.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.proceedings.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.proceedings.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.proceedings.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.proceedings.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.proceedings.pickAuthorMessageSecondTabSimilarExistFourthSentence");
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
			for (AuthorDTO dto : selectedProceedings.getEditors()) {
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
			selectedProceedings.getEditors().remove(selectedAuthor);
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
		for (int i = 0; i < selectedProceedings.getEditors().size(); i++) {
			authorDTO = selectedProceedings.getEditors().get(i);
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
						% selectedProceedings.getEditors().size());
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
		for (int i = 0; i < selectedProceedings.getEditors().size(); i++) {
			authorDTO = selectedProceedings.getEditors().get(i);
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
						% selectedProceedings.getEditors().size());
				break;
		}
	}

	private void switchEditors(int firstIndex, int secondIndex) {
		AuthorDTO first = selectedProceedings.getEditors().get(firstIndex);
		AuthorDTO second = selectedProceedings.getEditors().get(secondIndex);
		selectedProceedings.getEditors().set(firstIndex, second);
		selectedProceedings.getEditors().set(secondIndex, first);
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
		
		if ((selectedProceedings.getEditors().contains(author))) {
			selectedProceedings.getEditors().set(
					selectedProceedings.getEditors().indexOf(author), author);
		} else {
			selectedProceedings.getEditors().add(author);
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
		if ((!("".equals(language))) && (!(selectedProceedings.getLanguages().contains(language))))
			selectedProceedings.getLanguages().add(language);
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
		for (int i = 0; i < selectedProceedings.getLocalizedLanguages().size(); i++) {
			tempLanguage = selectedProceedings.getLocalizedLanguages().get(i);
			if (tempLanguage.equals(paramLanguage)) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedProceedings.getLanguages().remove(index);
	}

	/**
	 * Sets chosen proceedings to the CRUDManagedBean which wanted to pick
	 * proceedings
	 */
	public void chooseProceedings() {
		try {
			if(tableTabNumber != 0)
				selectedProceedings = findProceedingsByControlNumber(list);
			if (selectedProceedings != null) {
				iPickProceedingsManagedBean.setProceedings(selectedProceedings);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseProceedings", e);
		}
	}

	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedProceedings = findProceedingsByControlNumber(similarProceedings);
			if (selectedProceedings != null) {
				iPickProceedingsManagedBean.setProceedings(selectedProceedings);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0; 
			setEditMode(ModesManagedBean.MODE_NONE); 
		} catch (Exception e) {
			error("chooseSimilarProceedings", e);
		}
	}
	

	public void chooseSimilar(ProceedingsDTO proceedings) {
		try {
			selectedProceedings = proceedings;
			if (selectedProceedings != null) {
				iPickProceedingsManagedBean.setProceedings(selectedProceedings);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0; 
			setEditMode(ModesManagedBean.MODE_NONE); 
		} catch (Exception e) {
			error("chooseSimilarProceedings", e);
		}
	}


	private IPickProceedingsManagedBean iPickProceedingsManagedBean = null;

	/**
	 * @param iPickProceedingsManagedBean
	 *            the CRUDManagedBean which wants to pick proceedings
	 */
	public void setIPickProceedingsManagedBean(
			IPickProceedingsManagedBean iPickProceedingsManagedBean) {
		this.iPickProceedingsManagedBean = iPickProceedingsManagedBean;
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
	 * @return the pick message for second tab if SIMILAR proceedings do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR proceedings do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR proceedings do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR proceedings do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR proceedings  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR proceedings exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR proceedings exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR proceedings exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR proceedings exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR proceedings exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR proceedings exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR proceedings exist to set
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

	private ProceedingsDTO findProceedingsByControlNumber(List<ProceedingsDTO> proceedingsList) {
		ProceedingsDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (ProceedingsDTO dto : proceedingsList) {
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

	private String conferenceName;

	private String conferenceYear;

	private String title;

	/**
	 * @return the conference name
	 */
	public String getConferenceName() {
		return conferenceName;
	}

	/**
	 * @param conferenceName
	 *            the conference name to set
	 */
	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	/**
	 * @return the conference year
	 */
	public String getConferenceYear() {
		return conferenceYear;
	}

	/**
	 * @param conferenceYear
	 *            the conference year to set
	 */
	public void setConferenceYear(String conferenceYear) {
		this.conferenceYear = conferenceYear;
	}

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
			whereStr = conferenceName + " " + conferenceYear + " "
					+ ((title != null) ? (title) : (""));
		super.nextTableTab();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#nextEditTab()
	 */
	@Override
	public void nextEditTab() {
		if (editMode == ModesManagedBean.MODE_IMPORT){
			editTabNumber++;
//			if((editTabNumber == 1) && (editTabNumber <= 2))
			if(editTabNumber == 1)
				populateImportMessages();
		} else {
			if (editTabNumber == 0) {
				if (selectedProceedings.getConference().getControlNumber() == null){
					facesMessages.addToControlFromResourceBundle(
							"proceedingsEditForm:conferenceName", FacesMessage.SEVERITY_ERROR, 
							"javax.faces.component.UIInput.REQUIRED",
							FacesContext.getCurrentInstance());
					return;
				}
				if (validateEditors() == false) {
					facesMessages.addToControlFromResourceBundle(
							"proceedingsEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.proceedings.editor.name.pleaseSelect.error",
							FacesContext.getCurrentInstance());
					return;
				}
			}
			super.nextEditTab();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_NONE)) || ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)
				&& (customPick) && (editTabNumber == 4))) {
			iPickProceedingsManagedBean.setProceedings(selectedProceedings);
			if (tableMode != ModesManagedBean.MODE_NONE)
				setTableMode(ModesManagedBean.MODE_NONE);
			setEditMode(ModesManagedBean.MODE_NONE);
		} else
			super.switchToEditNoneMode();
		return null;
	}
	

	private boolean validateEditors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedProceedings.getEditors()) {
			if ((authorDTO.getName() == null)
					|| ("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
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
		this.openMultilingualContentForm(editMode, selectedProceedings.getTitleTranslations(), false, "records.proceedings.editPanel.titleTranslations.panelHeader", "records.proceedings.editPanel.titleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedProceedings.getSubtitleTranslations(), false, "records.proceedings.editPanel.subtitleTranslations.panelHeader", "records.proceedings.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void nameAbbreviationTranslations(){
		this.openMultilingualContentForm(editMode, selectedProceedings.getNameAbbreviationTranslations(), false, "records.proceedings.editPanel.nameAbbreviationTranslations.panelHeader", "records.proceedings.editPanel.nameAbbreviationTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedProceedings.getNoteTranslations(), false, "records.proceedings.editPanel.noteTranslations.panelHeader", "records.proceedings.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedProceedings.getKeywordsTranslations(), false, "records.proceedings.editPanel.keywordsTranslations.panelHeader", "records.proceedings.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void publisherTranslations(){
		this.openMultilingualContentPublisherForm(editMode, selectedProceedings.getPublisher().getPublisherTranslations(), "records.proceedings.editPanel.publisherTranslations.panelHeader");
	}

}
