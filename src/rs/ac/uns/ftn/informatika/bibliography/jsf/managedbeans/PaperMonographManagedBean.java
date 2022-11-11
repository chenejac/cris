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

import com.gint.util.string.StringUtils;

import org.primefaces.component.datatable.DataTable;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.EmailMessage;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * Managed bean with CRUD operations for paper published in monograph
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PaperMonographManagedBean extends CRUDManagedBean implements
		IPickMonographManagedBean, IPickAuthorManagedBean {

	private List<PaperMonographDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private PaperMonographDTO selectedPaperMonograph = null;

	/**
	 * 
	 */
	public PaperMonographManagedBean() {
		super();
		tableModalPanel = "paperMonographBrowseModalPanel";
		editModalPanel = "paperMonographEditModalPanel";
		simpleEditModalPanel = "paperMonographSimpleEditModalPanel";
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPaperMonograph = null;
		monograph = null;
		tableModalPanel = "paperMonographBrowseModalPanel";
		editModalPanel = "paperMonographEditModalPanel";
		simpleEditModalPanel = "paperMonographSimpleEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<PaperMonographDTO> listTmp = getPaperMonographs(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false) && (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedPaperMonograph != null
					&& selectedPaperMonograph.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPaperMonograph.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("paperMonographTable");
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
			list = new ArrayList<PaperMonographDTO>();
		}
	}
	
	/**
	 * Retrieves a list of papers (published in monographs) that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in monographs) that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<PaperMonographDTO> getPaperMonographs(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getPaperMonographs(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of papers (published in monographs) that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of papers (published in monographs) that correspond to the query
	 */
	private List<PaperMonographDTO> getPaperMonographs(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<PaperMonographDTO> retVal = new ArrayList<PaperMonographDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				PaperMonographDTO dto = (PaperMonographDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<PaperMonographDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of papers published in proceedings (filtered and ordered
	 *         by ...)
	 */
	public List<PaperMonographDTO> getPaperMonographs() {
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
			Collections.sort(list, new GenericComparator<PaperMonographDTO>(
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
		bq.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.MUST);
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		if (monograph != null)
			bq.add(new TermQuery(new Term("MOCN", monograph.getControlNumber())), Occur.MUST);
		return bq;
	}

	/**
	 * @return the selected paper published in monograph
	 */
	public PaperMonographDTO getSelectedPaperMonograph() {
		return selectedPaperMonograph;
	}

	/**
	 * @param paperMonographDTO
	 *            the paper published in monograph to set as selected paper
	 */
	public void setSelectedPaperMonograph(
			PaperMonographDTO paperMonographDTO) {
		selectedPaperMonograph = paperMonographDTO;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedPaperMonograph = findPaperMonographByControlNumber();
			if (selectedPaperMonograph != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedPaperMonograph);
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
			selectedPaperMonograph = findPaperMonographByControlNumber();
			if (selectedPaperMonograph != null) {
				super.switchToSimpleUpdateMode();
				debug("switchToSimpleUpdateMode: \n" + selectedPaperMonograph);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedPaperMonograph = new PaperMonographDTO();
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
			selectedPaperMonograph.setMainAuthor(author);
		}
		if (monograph != null) {
			selectedPaperMonograph.setMonograph(monograph);
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
		selectedPaperMonograph = new PaperMonographDTO();
		if (monograph != null) {
			selectedPaperMonograph.setMonograph(monograph);
			editTabNumber = 0;
		}
		selectedPaperMonograph.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_ENGLISH);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToViewDetailsMode();
		else {
			selectedPaperMonograph = findPaperMonographByControlNumber();
			if (selectedPaperMonograph != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperMonograph);
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
			selectedPaperMonograph = findPaperMonographByControlNumber();
			if (selectedPaperMonograph != null) {
				super.switchToSimpleViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedPaperMonograph);
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
		selectedPaperMonograph.setNotLoaded(true);
		super.switchToEditNoneMode();
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
		}else if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperMonograph)) == false) {
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperMonographSimpleEditForm:general":"paperMonographEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"paperMonographSimpleEditForm:general":"paperMonographEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.paperMonograph.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedPaperMonograph);
			changedReferenceAuthorsEmailNotification(selectedPaperMonograph, facesMessages.getMessageFromResourceBundle("records.paperMonograph.changedPaperMonographAuthorsNotification.subject"));
			sendRecordMessage(selectedPaperMonograph, "update");
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
		} else if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperMonograph)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"paperMonographEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.paperMonograph.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("added: \n" + selectedPaperMonograph);
			newReferenceAuthorsEmailNotification(selectedPaperMonograph, facesMessages.getMessageFromResourceBundle("records.paperMonograph.newPaperMonographAuthorsNotification.subject"));
			newRecordEmailNotification(selectedPaperMonograph, facesMessages.getMessageFromResourceBundle("records.paperMonograph.newPaperMonographNotification.subject"));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPaperMonograph = findPaperMonographByControlNumber();
		if (selectedPaperMonograph == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedPaperMonograph)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedPaperMonograph);
			selectedPaperMonograph = null;
			populateList = true;
			orderList = true;
		}
	}

	/**
	 * Prepares web form where user can choose Monograph
	 */
	public void pickMonograph() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		MonographManagedBean mb = (MonographManagedBean) extCtx
				.getSessionMap().get("monographManagedBean");
		if (mb == null) {
			mb = new MonographManagedBean();
			extCtx.getSessionMap().put("monographManagedBean", mb);
		}

		mb.setIPickMonographManagedBean(this);
		mb.setSelectedMonograph(null);
		mb.setPickMessageFirstTab("records.paperMonograph.pickMonographMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperMonograph.pickMonographMessageSecondTabSimilarExistFourthSentence");

		mb.setCustomPick(true);
		mb.switchToPickMode();
		if ((selectedPaperMonograph!=null) && (selectedPaperMonograph.getMonograph()!=null) 
				&& (selectedPaperMonograph.getMonograph().getControlNumber() != null) && (mb.isCustomPick())) {
			mb.setTitle(selectedPaperMonograph.getMonograph().getTitle().getContent());
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickMonographManagedBean#setMonograph(rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO)
	 */
	public void setMonograph(MonographDTO monograph) {
		if ((editMode == ModesManagedBean.MODE_UPDATE) || (editMode == ModesManagedBean.MODE_ADD)) {
			selectedPaperMonograph.setMonograph(monograph);
		} else {
			this.monograph = monograph;
			populateList = true;
			orderList = true;
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickMonographManagedBean#cancelPickingMonograph()
	 */
	@Override
	public void cancelPickingMonograph() {
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
		mb.setPickMessageFirstTab("records.paperMonograph.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.paperMonograph.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.paperMonograph.pickAuthor.pleaseInstitution");
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
			if(selectedPaperMonograph.getOtherAuthors().contains(selectedAuthor))
				selectedPaperMonograph.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedPaperMonograph.getMainAuthor())){
					if(selectedPaperMonograph.getOtherAuthors().size() > 0){
						selectedPaperMonograph.setMainAuthor(selectedPaperMonograph.getOtherAuthors().get(0));
						selectedPaperMonograph.getOtherAuthors().remove(0);
					} else {
						selectedPaperMonograph.setMainAuthor(new AuthorDTO());
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
			for (AuthorDTO dto : selectedPaperMonograph.getAllAuthors()) {
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
		for (int i = 0; i < selectedPaperMonograph.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperMonograph.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedPaperMonograph.getOtherAuthors()
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
		for (int i = 0; i < selectedPaperMonograph.getOtherAuthors().size(); i++) {
			authorDTO = selectedPaperMonograph.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedPaperMonograph
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedPaperMonograph
				.getMainAuthor() : selectedPaperMonograph.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedPaperMonograph
				.getMainAuthor() : selectedPaperMonograph.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedPaperMonograph.setMainAuthor(second);
		else
			selectedPaperMonograph.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedPaperMonograph.setMainAuthor(first);
		else
			selectedPaperMonograph.getOtherAuthors().set(secondIndex, first);
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
		
		if ((selectedPaperMonograph.getAllAuthors().contains(author))) {
			if (selectedPaperMonograph.getMainAuthor().getControlNumber()
					.equals(author.getControlNumber())){
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperMonograph.getMainAuthor().getName());
				} 
				selectedPaperMonograph.setMainAuthor(author);
			}else {
				if((simpleForm) && (mb.editMode == ModesManagedBean.MODE_UPDATE)){
					author.setName(selectedPaperMonograph.getOtherAuthors().get(selectedPaperMonograph.getOtherAuthors().indexOf(
							author)).getName());
				} 
				selectedPaperMonograph.getOtherAuthors().set(
						selectedPaperMonograph.getOtherAuthors().indexOf(
								author), author);
			}
		} else {
			if (("".equals(selectedPaperMonograph.getMainAuthor()
					.getControlNumber()))
					|| (selectedPaperMonograph.getMainAuthor()
							.getControlNumber() == null))
				selectedPaperMonograph.setMainAuthor(author);
			else
				selectedPaperMonograph.getOtherAuthors().add(author);
		}
		authorName = "";
		setAuthorManageBeanToPick(this);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "paperMonographPage";
		return retVal;
	}

	private MonographDTO monograph = null;

	/**
	 * @return the proceedings
	 */
	public MonographDTO getMonograph() {
		return monograph;
	}

	private PaperMonographDTO findPaperMonographByControlNumber() {
		PaperMonographDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PaperMonographDTO dto : list) {
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		super.switchToEditNoneMode();
		return null;
	}

	private boolean validateAuthors() {
		boolean retVal = true;
		for (AuthorDTO authorDTO : selectedPaperMonograph.getAllAuthors()) {
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
			if (! selectedPaperMonograph.getAllAuthors().contains(getUserManagedBean().getLoggedUser().getAuthor())) {
					retVal = false;
			}
		} else if(selectedPaperMonograph.getAllAuthors().size() == 0)
				retVal = false;
		return retVal;
	}
	
	private boolean validateAll(){
		boolean retVal = true;
		if ((selectedPaperMonograph.getMonograph() == null) || (selectedPaperMonograph.getMonograph().getControlNumber() == null) || (selectedPaperMonograph.getMonograph().getControlNumber().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperMonographSimpleEditForm:monographTitle", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperMonograph.getTitle() == null) || (selectedPaperMonograph.getTitle().getLanguage() == null) || (selectedPaperMonograph.getTitle().getLanguage().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperMonographSimpleEditForm:language", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedPaperMonograph.getSomeTitle() == null) || (selectedPaperMonograph.getSomeTitle().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"paperMonographSimpleEditForm:title", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateLoggedAuthor() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.author.name.noLoggedAuthor.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"paperMonographSimpleEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.paperMonograph.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		return retVal;
	}
	
	public void titleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperMonograph.getTitleTranslations(), false, "records.paperMonograph.editPanel.titleTranslations.panelHeader", "records.paperMonograph.editPanel.titleTranslations.contentHeader");
	}
	
	public void subtitleTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperMonograph.getSubtitleTranslations(), false, "records.paperMonograph.editPanel.subtitleTranslations.panelHeader", "records.paperMonograph.editPanel.subtitleTranslations.contentHeader");
	}
	
	public void noteTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperMonograph.getNoteTranslations(), false, "records.paperMonograph.editPanel.noteTranslations.panelHeader", "records.paperMonograph.editPanel.noteTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperMonograph.getKeywordsTranslations(), false, "records.paperMonograph.editPanel.keywordsTranslations.panelHeader", "records.paperMonograph.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void abstractTranslations(){
		this.openMultilingualContentForm(editMode, selectedPaperMonograph.getAbstractTranslations(), false, "records.paperMonograph.editPanel.abstractTranslations.panelHeader", "records.paperMonograph.editPanel.abstractTranslations.contentHeader");
	}
	
	/**
	 *  Sends email notification to authors for requesting evaluation data
	 */
	protected void requestEvaluationDataViaEmail(RecordDTO record, String from){
		ResourceBundle rbAdministration = PropertyResourceBundle.getBundle(
				"messages.messages-administration", new Locale(this.getUserManagedBean().getLanguage()));
		PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
		StringBuffer emails = new StringBuffer("");
		for (AuthorDTO author : paperMonograph.getAllAuthors()) {
			if((author.getEmail() != null) && (! author.getEmail().equals("proba1"))){
				if(! emails.toString().contains(author.getEmail()))
					emails.append(author.getEmail()+" ");
			}
		}
		if(emails.toString().trim().length() != 0)
			sendMessage(new EmailMessage(from, emails.toString(), null, "surla@uns.ac.rs chenejac@uns.ac.rs", rbAdministration.getString("administration.notification.evaluation.paperMonograph.requestinformation.subject"),  rbAdministration.getString("administration.notification.evaluation.paperMonograph.requestinformation.header") + paperMonograph.getControlNumber() + "| " + paperMonograph.toString() + rbAdministration.getString("administration.notification.evaluation.paperMonograph.requestinformation.body") + rbAdministration.getString("administration.notification.evaluation.paperMonograph.requestinformation.footer")));
	}
	
	public void sendPaperMonographEvaluationDMIEmails(){
		BooleanQuery allMonographsQuery = new BooleanQuery();
		Query type = new TermQuery(new Term("TYPE", Types.MONOGRAPH));
		allMonographsQuery.add(type, Occur.MUST);
		List<Record> listMonographs = recordDAO.getDTOs(allMonographsQuery, new AllDocCollector(false));
//		List<RecordDTO> listMonographDTOs = new ArrayList<RecordDTO>();
		List<PaperMonographDTO> listPaperMonographDTOs = new ArrayList<PaperMonographDTO>();
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
					BooleanQuery allMonographPapersQuery = new BooleanQuery();
					type = new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH));
					allMonographPapersQuery.add(type, Occur.MUST);
					allMonographPapersQuery.add(new TermQuery(new Term("MOCN", monographDTO.getControlNumber())), Occur.MUST);
					List<Record> listMonographPapers = recordDAO.getDTOs(allMonographPapersQuery, new AllDocCollector(false));
					for (Record rDTO : listMonographPapers) {
						try {
							String paperMonographClassification = null;
							rDTO.loadFromDatabase();
							PaperMonographDTO paperMonographDTO = (PaperMonographDTO) rDTO.getDto();
							for (Classification classification : rDTO.getRecordClasses()) {
								if(
										((!("0".equals(classification.getCommissionId().toString()))))
								&& ((classification.getCfClassSchemeId().equals("type")) || (classification.getCfClassSchemeId().equals("resultType")))) 
								 {
									paperMonographClassification = classification.getCfClassId();
								}
							}
							if ((paperMonographClassification == null) && (paperMonographDTO != null)) {
								for (AuthorDTO authorDTO : paperMonographDTO.getAllAuthors()) {
									if((authorDTO.getOrganizationUnit().getControlNumber() != null) && ((authorDTO.getOrganizationUnit().getControlNumber().equals("(BISIS)6782")) || ((authorDTO.getOrganizationUnit().getSuperOrganizationUnit() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber() != null) && (authorDTO.getOrganizationUnit().getSuperOrganizationUnit().getControlNumber().equals("(BISIS)6782"))))){
										listPaperMonographDTOs.add(paperMonographDTO);
										break;
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (RecordDTO recordDTO : listPaperMonographDTOs) {
			requestEvaluationDataViaEmail(recordDTO, "komisija.cris@dmi.uns.ac.rs");
		}
					
	}

}
