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
import org.richfaces.component.UIDataTable;

import com.gint.util.string.StringUtils;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
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
public class ProductManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean{

	private List<ProductDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	private ProductDTO selectedProduct = null;
	
	/**
	 * 
	 */
	public ProductManagedBean() {
		super();
		tableModalPanel = "productBrowseModalPanel";
		editModalPanel = "productEditModalPanel";
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedProduct = null;
		tableModalPanel = "productBrowseModalPanel";
		editModalPanel = "productEditModalPanel";
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<ProductDTO> listTmp = getProducts(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList == false)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedProduct != null
					&& selectedProduct.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedProduct.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("productTable");
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
			list = new ArrayList<ProductDTO>();
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
	private List<ProductDTO> getProducts(String query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getProducts(q, orderBy, direction, hitCollector);
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
	private List<ProductDTO> getProducts(Query query,
			String orderBy, String direction, HitCollector hitCollector)
			throws ParseException {
		List<ProductDTO> retVal = new ArrayList<ProductDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				ProductDTO dto = (ProductDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<ProductDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of study final documents (filtered and ordered
	 *         by ...)
	 */
	public List<ProductDTO> getProducts() {
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
			Collections.sort(list, new GenericComparator<ProductDTO>(
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
		bq.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.MUST);
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
	public ProductDTO getSelectedProduct() {
		return selectedProduct;
	}

	/**
	 * @param productDTO
	 *            the study final document to set as selected study final document
	 */
	public void setSelectedProduct(
			ProductDTO product) {
		selectedProduct = product;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToUpdateMode();
		else {
			selectedProduct = findProductByControlNumber();
			if (selectedProduct != null) {
				super.switchToUpdateMode();
				debug("switchToUpdateMode: \n" + selectedProduct);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedProduct = new ProductDTO();
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
			selectedProduct.setMainAuthor(author);
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
			selectedProduct = findProductByControlNumber();
			if (selectedProduct != null) {
				super.switchToViewDetailsMode();
				debug("switchToViewDetailsMode: \n" + selectedProduct);
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
		selectedProduct.setNotLoaded(true);
		super.switchToEditNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.product.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PRODUCT, 
				selectedProduct)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.product.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.product.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedProduct);
			changedReferenceAuthorsEmailNotification(selectedProduct, facesMessages.getMessageFromResourceBundle("records.product.changedProductAuthorsNotification.subject"));
			sendRecordMessage(selectedProduct, "update");
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		if (validateAuthors() == false) {
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.product.author.name.pleaseSelect.error",
					FacesContext.getCurrentInstance());
			return;
		}
		if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PRODUCT, 
				selectedProduct)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.product.add.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"productEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.product.add.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("added: \n" + selectedProduct);
			newReferenceAuthorsEmailNotification(selectedProduct, facesMessages.getMessageFromResourceBundle("records.product.newProductAuthorsNotification.subject"));
			newRecordEmailNotification(selectedProduct, facesMessages.getMessageFromResourceBundle("records.product.newProductNotification.subject"));
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedProduct = findProductByControlNumber();
		if (selectedProduct == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.RESULT_PUBLICATION, 
				selectedProduct)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"productTableForm:deleteError", FacesMessage.SEVERITY_ERROR, 
					"records.product.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedProduct);
			selectedProduct = null;
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
		retVal = "productPage";
		return retVal;
	}

	private ProductDTO findProductByControlNumber() {
		ProductDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (ProductDTO dto : list) {
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
		mb.setPickMessageFirstTab("records.product.pickAuthorMessageFirstTab");
		mb.setPickMessageSecondTabSimilarNotExistFirstSentence("records.product.pickAuthorMessageSecondTabSimilarNotExistFirstSentence");
		mb.setPickMessageSecondTabSimilarNotExistSecondSentence("records.product.pickAuthorMessageSecondTabSimilarNotExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistFirstSentence("records.product.pickAuthorMessageSecondTabSimilarExistFirstSentence");
		mb.setPickMessageSecondTabSimilarExistSecondSentence("records.product.pickAuthorMessageSecondTabSimilarExistSecondSentence");
		mb.setPickMessageSecondTabSimilarExistThirdSentence("records.product.pickAuthorMessageSecondTabSimilarExistThirdSentence");
		mb.setPickMessageSecondTabSimilarExistFourthSentence("records.product.pickAuthorMessageSecondTabSimilarExistFourthSentence");
		mb.setCustomPick(true);
		mb.setPleaseInstitution(true);
		mb.setPleaseInstitutionMessage("records.product.pickAuthor.pleaseInstitution");
		mb.switchToPickMode();

	}
	
	/**
	 * Removes the selected author from the list of authors
	 */
	public void removeAuthor() {
		AuthorDTO selectedAuthor = findAuthorByControlNumber();
		if (selectedAuthor != null) 
			if(selectedProduct.getOtherAuthors().contains(selectedAuthor))
				selectedProduct.getOtherAuthors().remove(selectedAuthor);
			else if(selectedAuthor.equals(selectedProduct.getMainAuthor())){
				selectedProduct.setMainAuthor(selectedProduct.getOtherAuthors().get(0));
				selectedProduct.getOtherAuthors().remove(0);
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
			for (AuthorDTO dto : selectedProduct.getAllAuthors()) {
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
		for (int i = 0; i < selectedProduct.getOtherAuthors().size(); i++) {
			authorDTO = selectedProduct.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchAuthors(index, selectedProduct.getOtherAuthors()
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
		for (int i = 0; i < selectedProduct.getOtherAuthors().size(); i++) {
			authorDTO = selectedProduct.getOtherAuthors().get(i);
			if (authorDTO.getControlNumber().equals(controlNumber)) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchAuthors(index, ((index + 1) == selectedProduct
						.getOtherAuthors().size()) ? (-1) : (index + 1));
				break;
		}
	}

	private void switchAuthors(int firstIndex, int secondIndex) {
		AuthorDTO first = (firstIndex == -1) ? selectedProduct
				.getMainAuthor() : selectedProduct.getOtherAuthors()
				.get(firstIndex);
		AuthorDTO second = (secondIndex == -1) ? selectedProduct
				.getMainAuthor() : selectedProduct.getOtherAuthors()
				.get(secondIndex);
		if (firstIndex == -1)
			selectedProduct.setMainAuthor(second);
		else
			selectedProduct.getOtherAuthors().set(firstIndex, second);
		if (secondIndex == -1)
			selectedProduct.setMainAuthor(first);
		else
			selectedProduct.getOtherAuthors().set(secondIndex, first);
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
		
		if ((selectedProduct.getAllAuthors().contains(author))) {
			if (selectedProduct.getMainAuthor().getControlNumber()
					.equals(author.getControlNumber()))
				selectedProduct.setMainAuthor(author);
			else {
				selectedProduct.getOtherAuthors().set(
						selectedProduct.getOtherAuthors().indexOf(
								author), author);
			}
		} else {
			if (("".equals(selectedProduct.getMainAuthor()
					.getControlNumber()))
					|| (selectedProduct.getMainAuthor()
							.getControlNumber() == null))
				selectedProduct.setMainAuthor(author);
			else
				selectedProduct.getOtherAuthors().add(author);
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
		for (AuthorDTO authorDTO : selectedProduct.getAllAuthors()) {
			if (("".equals(authorDTO.getName().getLastname()))) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedProduct.getNameTranslations(), false, "records.product.editPanel.nameTranslations.panelHeader", "records.product.editPanel.nameTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedProduct.getKeywordsTranslations(), false, "records.product.editPanel.keywordsTranslations.panelHeader", "records.product.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedProduct.getDescriptionTranslations(), false, "records.product.editPanel.descriptionTranslations.panelHeader", "records.product.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void publisherTranslations(){
		this.openMultilingualContentPublisherForm(editMode, selectedProduct.getPublisher().getPublisherTranslations(), "records.product.editPanel.publisherTranslations.panelHeader");
	}
	
	
}
