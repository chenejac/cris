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

import com.gint.util.string.StringUtils;

import org.primefaces.component.datatable.DataTable;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for patent
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PatentManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean{

	private List<PatentDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private PatentDTO selectedPatent = null;
	
	/**
	 * 
	 */
	public PatentManagedBean() {
		super();
		tableModalPanel = "patentBrowseModalPanel";
		editModalPanel = "patentEditModalPanel";
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPatent = null;
		tableModalPanel = "patentBrowseModalPanel";
		editModalPanel = "patentEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<PatentDTO> listTmp = getPatents(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedPatent != null
					&& selectedPatent.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPatent.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("patentTable");
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
			list = new ArrayList<PatentDTO>();
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
	private List<PatentDTO> getPatents(String query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getPatents(q, orderBy, direction, hitCollector);
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
	private List<PatentDTO> getPatents(Query query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		List<PatentDTO> retVal = new ArrayList<PatentDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				PatentDTO dto = (PatentDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<PatentDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of study final documents (filtered and ordered
	 *         by ...)
	 */
	public List<PatentDTO> getPatents() {
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
			Collections.sort(list, new GenericComparator<PatentDTO>(
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
		bq.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.MUST);
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser()
				.getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		return bq;
	}

	/**
	 * @return the selected study final document
	 */
	public PatentDTO getSelectedPatent() {
		return selectedPatent;
	}

	/**
	 * @param patent
	 *            the study final document to set as selected study final document
	 */
	public void setSelectedPatent(
			PatentDTO patent) {
		selectedPatent = patent;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedPatent = findPatentByControlNumber();
			if (selectedPatent != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedPatent);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedPatent = new PatentDTO();
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
			selectedPatent.setMainAuthor(author);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToViewDetailsMode();
		else {
			selectedPatent = findPatentByControlNumber();
			if (selectedPatent != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPatent);
			}
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
		selectedPatent.setNotLoaded(true);
		super.switchToEditNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.patent.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PATENT, 
				selectedPatent)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.patent.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.patent.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedPatent);
			changedReferenceAuthorsEmailNotification(selectedPatent, facesMessages.getMessageFromResourceBundle("records.patent.changedPatentAuthorsNotification.subject"));
			sendRecordMessage(selectedPatent, "update");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.patent.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PATENT, 
				selectedPatent)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.patent.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"patentEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.patent.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("added: \n" + selectedPatent);
			newReferenceAuthorsEmailNotification(selectedPatent, facesMessages.getMessageFromResourceBundle("records.patent.newPatentNotification.subject"));
			newRecordEmailNotification(selectedPatent, facesMessages.getMessageFromResourceBundle("records.patent.newPatentNotification.subject"));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPatent = findPatentByControlNumber();
		if (selectedPatent == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPatent)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"patentTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.patent.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedPatent);
			selectedPatent = null;
			populateList = true;
			orderList = true;
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "patentPage";
		return retVal;
	}

	private PatentDTO findPatentByControlNumber() {
		PatentDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PatentDTO dto : list) {
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
		super.switchToEditNoneMode();
		return null;
	}

	/**
	 * Prepares web form where user can choose Author for selected study final document
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
		mb.setPickMessageFirstTab("records.patent.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.patent.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.patent.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.patent.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.patent.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.patent.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.patent.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.patent.pickAuthor.pleaseInstitution");
		mb.switchToPickMode();

	}
	
	/**
	 * Removes the selected author from the list of authors
	 */
	public void removeAuthor() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) 
			if(selectedPatent.getOtherAuthors().contains(selectedAuthor))
				selectedPatent.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedPatent.getMainAuthor())){
					selectedPatent.setMainAuthor(selectedPatent.getOtherAuthors().get(0));
					selectedPatent.getOtherAuthors().remove(0);
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

	private AuthorDTO findAuthorByControlNumber() {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : selectedPatent.getAllAuthors()) {
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
		for (int i = 0; i < selectedPatent.getOtherAuthors().size(); i++) {
			authorDTO = selectedPatent.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedPatent.getOtherAuthors()
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
		for (int i = 0; i < selectedPatent.getOtherAuthors().size(); i++) {
			authorDTO = selectedPatent.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedPatent
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedPatent
				.getMainAuthor() : selectedPatent.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedPatent
				.getMainAuthor() : selectedPatent.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedPatent.setMainAuthor(second);
		else
			selectedPatent.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedPatent.setMainAuthor(first);
		else
			selectedPatent.getOtherAuthors().set(secondIndex, first);
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
		} else if((mb.editMode == ModesManagedBean.MODE_UPDATE) || (mb.editMode == ModesManagedBean.MODE_NONE)){
			List<AuthorNameDTO> otherFormatNames = new ArrayList<AuthorNameDTO>();
			otherFormatNames.add(author.getName());
			otherFormatNames.addAll(author.getOtherFormatNames());
			author.setOtherFormatNames(otherFormatNames);
			author.setName(new AuthorNameDTO());
		}
		
		if ((selectedPatent.getAllAuthors().contains(author))) {
			if (selectedPatent.getMainAuthor().getControlNumber()
					.equals(author.getControlNumber()))
				selectedPatent.setMainAuthor(author);
			else {
				selectedPatent.getOtherAuthors().set(
						selectedPatent.getOtherAuthors().indexOf(
								author), author);
			}
		} else {
			if (("".equals(selectedPatent.getMainAuthor()
					.getControlNumber()))
					|| (selectedPatent.getMainAuthor()
							.getControlNumber() == null))
				selectedPatent.setMainAuthor(author);
			else
				selectedPatent.getOtherAuthors().add(author);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}

	private boolean validateAuthors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedPatent.getAllAuthors()) {
			if (("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	
	public void titleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPatent.getTitleTranslations(), false, "records.patent.editPanel.titleTranslations.panelHeader", "records.patent.editPanel.titleTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedPatent.getKeywordsTranslations(), false, "records.patent.editPanel.keywordsTranslations.panelHeader", "records.patent.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedPatent.getAbstractTranslations(), false, "records.patent.editPanel.abstractTranslations.panelHeader", "records.patent.editPanel.abstractTranslations.contentHeader");
	}
	
	public void publisherTranslations(){
		this.openMultilingualContentPublisherForm(editMode, selectedPatent.getPublisher().getPublisherTranslations(), "records.patent.editPanel.publisherTranslations.panelHeader");
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
	
}
