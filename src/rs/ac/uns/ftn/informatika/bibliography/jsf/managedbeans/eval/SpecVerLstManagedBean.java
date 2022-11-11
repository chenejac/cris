package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.eval;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.FileDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.SpecVerLstResultsTypeOfResPublDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ResultsTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.SpecVerLstResultsTypeOfResPublDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.ModesManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SpecVerLstManagedBean extends CRUDManagedBean{
	private List<SpecVerLstDTO> list;
	private SpecVerLstDTO selectedSpecVerLst = null;
	
	List<SpecVerLstDTO> similarSpecVerLsts = null;
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private ModesManagedBean modesManagedBean = new ModesManagedBean();
	
	private IPickSpecVerLstManagedBean iPickSpecVerLstManagedBean = null;
	private String pickMessage;

	private PublicationDTO selectedPublication = null;
	private List<ResultsTypeDTO> allResultsTypes = null;
	private String selectedPublicationControlNumber = null;
	private String selectedResultsTypeCompariableId = null;
	private String selectedPublHumanReadId = null;
	private String selectedPublDisplName = null;
	private String selectedYear = null;
	private boolean specVerLstResultsTypeOfResPublAddMode = false;
	
	/**
	 * 
	 */
	public SpecVerLstManagedBean() {
		super();
		pickSimilarMessage = "records.specVerLst.pickSimilarMessage";
		tableModalPanel = "specVerLstBrowseModalPanel";
		editModalPanel = "specVerLstEditModalPanel";
		
		selectedPublication = null;
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		selectedPublicationControlNumber = null;
		selectedResultsTypeCompariableId = null;
		selectedPublHumanReadId = null;
		selectedPublDisplName = null;
		selectedYear = null;
		specVerLstResultsTypeOfResPublAddMode = false;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedSpecVerLst = null;
		selectedPublication = null;
		allResultsTypes = new ArrayList<ResultsTypeDTO>();
		selectedPublicationControlNumber = null;
		selectedResultsTypeCompariableId = null;
		selectedPublHumanReadId = null;
		selectedPublDisplName = null;
		selectedYear = null;
		specVerLstResultsTypeOfResPublAddMode = false;
		
		if(list != null)
			populateList = false;
		tableModalPanel = "specVerLstBrowseModalPanel";
		editModalPanel = "specVerLstEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<SpecVerLstDTO> listTmp = getSpecVerLsts(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));

			list = listTmp;
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<SpecVerLstDTO>();
		}
	}
	
	/**
	 * Creates query.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createQuery() throws ParseException {
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("TYPE", Types.SPECIALLY_VERIFIED_LIST)), Occur.MUST);
		return bq;
	}
	
	/**
	 * Retrieves a list of spec Ver Lsts that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving spec Ver Lsts
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of spec Ver Lsts that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<SpecVerLstDTO> getSpecVerLsts(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getSpecVerLsts(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of spec Ver Lsts that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving spec Ver Lsts
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of spec Ver Lsts that correspond to the query
	 */
	private List<SpecVerLstDTO> getSpecVerLsts(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<SpecVerLstDTO> retVal = new ArrayList<SpecVerLstDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector); 
		
		for (Record record : list) {
			try {
				SpecVerLstDTO dto = (SpecVerLstDTO) record.getDto();
				if (dto != null){
//					dto.setFile(recordDAO.getFileFromDatabase(dto.getControlNumber()));
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
			Collections.sort(retVal, new GenericComparator<SpecVerLstDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
	/**
	 * @return the list of spec Ver Lsts (filtered and ordered by ...)
	 */
	public List<SpecVerLstDTO> getSpecVerLsts() {
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
			Collections.sort(list, new GenericComparator<SpecVerLstDTO>(
					orderByList, directionsList));
		}
		return list;
	}
	
	/**
	 * @return the spec Ver Lst (filtered and ordered by ...)
	 */
	public SpecVerLstDTO getSpecVerLstBySpecVerLstUserCode(String specVerLstUserCode) {
		SpecVerLstDTO retVal = null;
		
		if(specVerLstUserCode!= null && !"".equals(specVerLstUserCode)){
			BooleanQuery bq = new BooleanQuery();
	        bq.add(new TermQuery(new Term("LN", specVerLstUserCode)), Occur.MUST);
	        bq.add(new TermQuery(new Term("TYPE", Types.SPECIALLY_VERIFIED_LIST)), Occur.MUST);
			
			List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
			if(listRecord.size()>0){
				try {
					SpecVerLstDTO dto = (SpecVerLstDTO) listRecord.get(0).getDto();
					if (dto != null) {
//						dto.setFile(recordDAO.getFileFromDatabase(dto.getControlNumber()));
						retVal=dto;
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		return retVal;
	}
	
	public List<SpecVerLstDTO> autocompleteUserCode(String suggest) {
		List<SpecVerLstDTO> retVal = new ArrayList<SpecVerLstDTO>();
		
		String specVerLstUserCode = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(specVerLstUserCode != null && !"".equals(specVerLstUserCode)){
			bq.add(QueryUtils.makeBooleanQuery("LN", specVerLstUserCode + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("LN", specVerLstUserCode + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.SPECIALLY_VERIFIED_LIST)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				SpecVerLstDTO dto = (SpecVerLstDTO) recordDTO.getDto();
				if (dto != null) {
//					dto.setFile(recordDAO.getFileFromDatabase(dto.getControlNumber()));
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<SpecVerLstDTO> autocompleteName(String suggest) {
		List<SpecVerLstDTO> retVal = new ArrayList<SpecVerLstDTO>();
		
		String specVerLstName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(specVerLstName != null && !"".equals(specVerLstName)){
			bq.add(QueryUtils.makeBooleanQuery("NA", specVerLstName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("NA", specVerLstName + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.SPECIALLY_VERIFIED_LIST)), Occur.MUST);
		
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				SpecVerLstDTO dto = (SpecVerLstDTO) recordDTO.getDto();
				if (dto != null) {
//					dto.setFile(recordDAO.getFileFromDatabase(dto.getControlNumber()));
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	/**
	 * Creates query for finding SIMILAR ruleBooks with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarSpecVerLstsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedSpecVerLst!=null){
			if(selectedSpecVerLst.getName().getContent()!=null){
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedSpecVerLst.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("NA", selectedSpecVerLst.getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
			else if(selectedSpecVerLst.getNameTranslations().size() > 0){
				for (MultilingualContentDTO iterable_element : selectedSpecVerLst.getNameTranslations()) {
					BooleanQuery bqTr = new BooleanQuery();
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
					bqTr.add(QueryUtils.makeBooleanQuery("NA", iterable_element.getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
					bq.add(bqTr, Occur.SHOULD);
				}
			}
			bq.add(new TermQuery(new Term("TYPE", Types.SPECIALLY_VERIFIED_LIST)), Occur.MUST);
		}
		return bq;
	}
	
	/**
	 * @return the selectedSpecVerLst
	 */
	public SpecVerLstDTO getSelectedSpecVerLst() {
		return selectedSpecVerLst;
	}

	/**
	 * @param selectedSpecVerLst the selectedSpecVerLst to set
	 */
	public void setSelectedSpecVerLst(SpecVerLstDTO selectedSpecVerLst) {
		this.selectedSpecVerLst = selectedSpecVerLst;
	}
	
	/**
	 * @return the similarSpecVerLsts
	 */
	public List<SpecVerLstDTO> getSimilarSpecVerLsts() {
		return similarSpecVerLsts;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedSpecVerLst = findSpecVerLstByControlNumber(list);
		if (selectedSpecVerLst != null) {
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedSpecVerLst);
		}
	}
	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedSpecVerLst = new SpecVerLstDTO();	
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchSpecVerLstResultsTypeOfResPublToAddMode() {
		specVerLstResultsTypeOfResPublAddMode = true;
		selectedPublication = null;
		
		if(allResultsTypes.isEmpty()){
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			//citanje svih tipovan nuacnih rezultata
			ResultsTypeManagedBean mb = (ResultsTypeManagedBean) extCtx.getSessionMap().get(
					"resultsTypeManagedBean");
			if (mb == null) {
				mb = new ResultsTypeManagedBean();
				extCtx.getSessionMap().put("resultsTypeManagedBean", mb);
			}
			allResultsTypes = mb.getResultsTypes();
		}

		selectedPublicationControlNumber = null;
		selectedResultsTypeCompariableId = null;
		selectedPublHumanReadId = null;
		selectedPublDisplName = null;
		selectedYear = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedSpecVerLst = findSpecVerLstByControlNumber(list);
		if (selectedSpecVerLst != null) {
			super.switchToViewDetailsMode();
			debug("switchToViewDetailsMode: \n" + selectedSpecVerLst);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == modesManagedBean.getPick() && iPickSpecVerLstManagedBean != null) {
			iPickSpecVerLstManagedBean.cancelPickingSpecVerLst();
		}
		super.switchToTableNoneMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	public void switchSpecVerLstResultsTypeOfResPublToNoneMode() {
		specVerLstResultsTypeOfResPublAddMode = false;
		selectedPublication = null;
		selectedPublicationControlNumber = null;
		selectedResultsTypeCompariableId = null;
		selectedPublHumanReadId = null;
		selectedPublDisplName = null;
		selectedYear = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		super.switchToBrowseMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
	}
	
	@Override
	public void update() {
		if (selectedSpecVerLst == null)
			return;
		
//		System.out.println("SpecVerLst-update-1");
		//update liste fajlova
		FileDAO dao1 = new FileDAO();
		if(dao1.update(selectedSpecVerLst, selectedSpecVerLst.getAttachedFiles())==false){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLst.update.errorAttachedFiles", FacesContext
								.getCurrentInstance());
				return;
		}
		
//		System.out.println("SpecVerLst-update-2");
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.SPECIALLY_VERIFIED_LIST, 
				selectedSpecVerLst)) == false) {
//			System.out.println("SpecVerLst-update-3");
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.specVerLst.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedSpecVerLst);
			sendRecordMessage(selectedSpecVerLst, "update");
		}
	}

	@Override
	public void add() {
		similarSpecVerLsts = null;
		if(selectedSpecVerLst.getSpecVerLstUserCode()== null || "".equals(selectedSpecVerLst.getSpecVerLstUserCode().trim())){
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.add.errorSpecVerLstUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
		
		if(getSpecVerLstBySpecVerLstUserCode(selectedSpecVerLst.getSpecVerLstUserCode()) != null){
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.add.errorSameSpecVerLstUserCodeNotValid", FacesContext
							.getCurrentInstance());
			return;
		}
		
		if(editTabNumber == 0){
			try {
				debug("findSimilarSpecVerLsts");
				similarSpecVerLsts = getSpecVerLsts(createSimilarSpecVerLstsQuery(),
						null, null, new AllDocCollector(true));
			} catch (ParseException e) {
				error("findSimilarSpecVerLsts", e);
			}
		}
		if((similarSpecVerLsts == null ) || (similarSpecVerLsts.size()==0)){
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.SPECIALLY_VERIFIED_LIST, 
					selectedSpecVerLst)) == false) {
				facesMessages.addToControlFromResourceBundle(
						"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLst.add.error", FacesContext
								.getCurrentInstance());
			} else {
				
//				if(addAllSpecVerLstResultsTypeOfResPublToDatabase()==false){
//					facesMessages.addToControlFromResourceBundle(
//							"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
//							"records.specVerLst.add.errorSpecVerLstResultsTypeOfResPubls", FacesContext
//									.getCurrentInstance());
//					return;
//				}
						
				
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"ruleBookEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.specVerLst.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedSpecVerLst);
				newRecordEmailNotification(selectedSpecVerLst, facesMessages.getMessageFromResourceBundle("records.specVerLst.newSpecVerLstNotification.subject"));
			}
		} else {
			nextEditTab();
		}
	}
	
	@Override
	public void delete() {
		selectedSpecVerLst = findSpecVerLstByControlNumber(list);
		if (selectedSpecVerLst == null)
			return;
		
//		System.out.println("SpecVerLst-delete()-1");
		//PRVO TREBA DA OSE OBRISU SVI SpecVerLstResultsTypeOfResPubl PA ONDA SpecVerLst
		selectedSpecVerLst.loadResultsTypesOfResultPublicationsFromDatabase();
		SpecVerLstResultsTypeOfResPublDAO dao1 = new SpecVerLstResultsTypeOfResPublDAO();
		for(SpecVerLstResultsTypeOfResPublDTO el: selectedSpecVerLst.getSpecVerLstResultsTypeOfResPubls()){
			if(dao1.delete(el)==false){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLst.delete.errorSpecVerLstResultsTypeOfResPubls", FacesContext
								.getCurrentInstance());
				return;
			}
		}
		
		
//		System.out.println("SpecVerLst-delete()-2");
		//PRVO TREBA DA OSE OBRISU SVI attachedFiles PA ONDA SpecVerLst
		selectedSpecVerLst.loadAttachedFilesFromDatabase();
		FileDAO dao2 = new FileDAO();
		for(FileDTO el: selectedSpecVerLst.getAttachedFiles()){
			if(dao2.delete(el)==false){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLst.delete.errorAttachedFiles", FacesContext
								.getCurrentInstance());
				return;
			}
		}
		
//		System.out.println("SpecVerLst-delete()-3");
		if (recordDAO.delete(new Record(getUserManagedBean().getLoggedUser()
				.getEmail(), new GregorianCalendar(), null, null, new Integer(100), CerifEntitiesNames.SPECIALLY_VERIFIED_LIST, 
				selectedSpecVerLst)) == false) {
//			System.out.println("SpecVerLst-delete()-3-Error");
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.delete.error", FacesContext
							.getCurrentInstance());
		} else {
//			System.out.println("SpecVerLst-delete()-4");
			debug("deleted: \n" + selectedSpecVerLst);
			selectedSpecVerLst = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * Sets chosen specVerLst to the CRUDManagedBean which wanted to pick
	 * ruleBook
	 */
	public void chooseSpecVerLst() {

		try {
			selectedSpecVerLst = findSpecVerLstByControlNumber(list);
			if (selectedSpecVerLst != null && iPickSpecVerLstManagedBean != null) {
				iPickSpecVerLstManagedBean.setSpecVerLst(selectedSpecVerLst);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBook", e);
		}
	}

	/**
	 * Sets chosen ruleBook to the CRUDManagedBean which wanted to pick
	 * ruleBook
	 */
	public void chooseSimilar() {

		try {
			selectedSpecVerLst = findSpecVerLstByControlNumber(similarSpecVerLsts);
			if (selectedSpecVerLst != null && iPickSpecVerLstManagedBean!=null) {
				iPickSpecVerLstManagedBean.setSpecVerLst(selectedSpecVerLst);
			}
			tableTabNumber = 0;
			setTableMode(modesManagedBean.getNone());
			editTabNumber = 0;
			setEditMode(modesManagedBean.getNone());
		} catch (Exception e) {
			error("chooseRuleBook", e);
		}
	}
	
	/**
	 * @param iPickSpecVerLstManagedBean the iPickSpecVerLstManagedBean to set
	 */
	public void setiPickSpecVerLstManagedBean(
			IPickSpecVerLstManagedBean iPickSpecVerLstManagedBean) {
		this.iPickSpecVerLstManagedBean = iPickSpecVerLstManagedBean;
	}

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
	
	@Override
	public String finishWizard() {
		if ((editMode == modesManagedBean.getAdd()) && (tableMode == modesManagedBean.getPick())) {
			if(iPickSpecVerLstManagedBean!=null)
				iPickSpecVerLstManagedBean.setSpecVerLst(selectedSpecVerLst);
			setTableMode(modesManagedBean.getNone());
			setEditMode(modesManagedBean.getNone());
		} else
			super.switchToEditNoneMode();
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "specVerLstPage";
		return retVal;
	}

	private SpecVerLstDTO findSpecVerLstByControlNumber(List<SpecVerLstDTO> specVerLstsList) {
		SpecVerLstDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (SpecVerLstDTO dto : specVerLstsList) {
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
	
	public void nameTranslations(){
		this.openMultilingualContentForm(editMode, selectedSpecVerLst.getNameTranslations(), false, "records.specVerLst.editPanel.nameTranslations.panelHeader", "records.specVerLst.editPanel.nameTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedSpecVerLst.getKeywordsTranslations(), false, "records.specVerLst.editPanel.keywordsTranslations.panelHeader", "records.specVerLst.editPanel.keywordsTranslations.contentHeader");
	}
	
	public Date getStartDate(){
		if(selectedSpecVerLst.getStartDate()!=null)
			return selectedSpecVerLst.getStartDate().getTime();
		else 
			return null;
	}
	
	public void setStartDate(Date startDate){
		if(startDate!=null){
			if(selectedSpecVerLst.getStartDate()==null){
				Calendar stC = new GregorianCalendar();
				selectedSpecVerLst.setStartDate(stC);
			}
			selectedSpecVerLst.getStartDate().setTime(startDate);
		}
	}
	
	public Date getEndDate(){
		if(selectedSpecVerLst.getEndDate()!=null)
			return selectedSpecVerLst.getEndDate().getTime();
		else 
			return null;
	}
	
	public void setEndDate(Date endDate){
		if(endDate!=null){
			if(selectedSpecVerLst.getEndDate()==null){
				Calendar enC = new GregorianCalendar();
				selectedSpecVerLst.setEndDate(enC);
			}
			selectedSpecVerLst.getEndDate().setTime(endDate);
		}
	}
	
	/**
	 * @return the allResultsTypes
	 */
	public List<ResultsTypeDTO> getAllResultsTypes() {
		return allResultsTypes;
	}
	
	/**
	 * @return the selectedPublication
	 */
	public PublicationDTO getSelectedPublication() {
		return selectedPublication;
	}

	/**
	 * @param selectedPublication the selectedPublication to set
	 */
	public void setSelectedPublication(PublicationDTO selectedPublication) {
		this.selectedPublication = selectedPublication;
	}

	/**
	 * @return the selectedPublicationControlNumber
	 */
	public String getSelectedPublicationControlNumber() {
		return selectedPublicationControlNumber;
	}

	/**
	 * @param selectedPublicationControlNumber the selectedPublicationControlNumber to set
	 */
	public void setSelectedPublicationControlNumber(
			String selectedPublicationControlNumber) {
		this.selectedPublicationControlNumber = selectedPublicationControlNumber;
	}

	/**
	 * @return the selectedResultsTypeCompariableId
	 */
	public String getSelectedResultsTypeCompariableId() {
		return selectedResultsTypeCompariableId;
	}

	/**
	 * @param selectedResultsTypeCompariableId the selectedResultsTypeCompariableId to set
	 */
	public void setSelectedResultsTypeCompariableId(
			String selectedResultsTypeCompariableId) {
		this.selectedResultsTypeCompariableId = selectedResultsTypeCompariableId;
	}

	/**
	 * @return the selectedPublHumanReadId
	 */
	public String getSelectedPublHumanReadId() {
		return selectedPublHumanReadId;
	}

	/**
	 * @param selectedPublHumanReadId the selectedPublHumanReadId to set
	 */
	public void setSelectedPublHumanReadId(String selectedPublHumanReadId) {
		this.selectedPublHumanReadId = selectedPublHumanReadId;
	}

	/**
	 * @return the selectedPublDisplName
	 */
	public String getSelectedPublDisplName() {
		return selectedPublDisplName;
	}

	/**
	 * @param selectedPublDisplName the selectedPublDisplName to set
	 */
	public void setSelectedPublDisplName(String selectedPublDisplName) {
		this.selectedPublDisplName = selectedPublDisplName;
	}

	/**
	 * @return the selectedYear
	 */
	public String getSelectedYear() {
		return selectedYear;
	}

	/**
	 * @param selectedYear the selectedYear to set
	 */
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	/**
	 * @return the specVerLstResultsTypeOfResPublAddMode
	 */
	public boolean isSpecVerLstResultsTypeOfResPublAddMode() {
		return specVerLstResultsTypeOfResPublAddMode;
	}

	/**
	 * @param specVerLstResultsTypeOfResPublAddMode the specVerLstResultsTypeOfResPublAddMode to set
	 */
	public void setSpecVerLstResultsTypeOfResPublAddMode(
			boolean specVerLstResultsTypeOfResPublAddMode) {
		this.specVerLstResultsTypeOfResPublAddMode = specVerLstResultsTypeOfResPublAddMode;
	}

	/**
	 * @param similarSpecVerLsts the similarSpecVerLsts to set
	 */
	public void setSimilarSpecVerLsts(List<SpecVerLstDTO> similarSpecVerLsts) {
		this.similarSpecVerLsts = similarSpecVerLsts;
	}

	/**
	 * @param allResultsTypes the allResultsTypes to set
	 */
	public void setAllResultsTypes(List<ResultsTypeDTO> allResultsTypes) {
		this.allResultsTypes = allResultsTypes;
	}
	
	public List<FileDTO> getAllAttachedFiles(){
		List<FileDTO> retVal = selectedSpecVerLst.getAttachedFiles();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("fileName");
		directionsList.add("asc");
		
		Collections.sort(retVal, new GenericComparator<FileDTO>(
				orderByList, directionsList));
		
		return retVal;
	}
	
	public void uploadListener(FileUploadEvent event) {
        try{
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
	        fileDTO.setFileName((item.getFileName().lastIndexOf("\\") != -1)?item.getFileName().substring(item.getFileName().lastIndexOf("\\")+1):item.getFileName());
	        fileDTO.setControlNumber(selectedSpecVerLst.getControlNumber());
	        fileDTO.setUploader(getUserManagedBean().getLoggedUser().getEmail());
	        if(selectedSpecVerLst.addFile(fileDTO)==false){
	        	facesMessages.addToControlFromResourceBundle(
						"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLst.add.errorAttachedFile", FacesContext
								.getCurrentInstance());
	        }
        } catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.add.errorAttachedFile", FacesContext
							.getCurrentInstance());
			return;
        }
    }

	public void deleteFile(){
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String fileNameId = facesCtx.getExternalContext().getRequestParameterMap().get("fileNameId");
		if(fileNameId == null || fileNameId.equals("")){
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLst.delete.errorAttachedFiles", FacesContext
							.getCurrentInstance());
			return;
		}
		FileDTO deletedFile = selectedSpecVerLst.getFileByFileName(fileNameId);
		selectedSpecVerLst.deleteFile(deletedFile);
	}
	
	//--------------------------------------------------------------------specVerLstResultsTypeOfResPubl START
	
	public boolean isVisibleSpecVerLstResultsTypeOfResPubls(){
		boolean retVal = true;
		if(editMode != modesManagedBean.getUpdate()){
			retVal = false;
		}
		else if(selectedSpecVerLst==null){
			retVal = false;
		}
		else if(selectedSpecVerLst!=null && selectedSpecVerLst.getControlNumber()== null){
			retVal = false;
		}
		else if( "".equals(selectedSpecVerLst.getControlNumber())){
			retVal = false;
		}
		return retVal;
	}
	
	public void setPublication(){
		if(!selectedPublicationControlNumber.startsWith("(BISIS)")){
			selectedPublicationControlNumber = "";
			return;
		}
		
		selectedPublication = (PublicationDTO) recordDAO.getDTO(selectedPublicationControlNumber);
		selectedPublicationControlNumber = "";
		
		
		if(selectedPublication instanceof JournalDTO){
				selectedPublHumanReadId = ((JournalDTO)selectedPublication).getIssn();
				selectedPublDisplName = ((JournalDTO)selectedPublication).getSomeName();
		}
	}
	
	public void clearPublication(){
		selectedPublication = null;
		selectedPublicationControlNumber = "";
	}
	
	public void addSpecVerLstResultsTypeOfResPublToDatabase(){
		try {
			
			if(selectedPublication==null){
				//TO BE IMPLEMENTED
//				selectedPublicationId
//				selectedPublication
			}

			if(selectedPublHumanReadId==null || "".equals(selectedPublHumanReadId.trim())){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLstResultsTypeOfResPubl.add.errorPublHumanReadId", FacesContext
								.getCurrentInstance());
				return;	
			}
			
			if(SpecVerLstResultsTypeOfResPublDTO.isValidPublHumanReadId(selectedPublHumanReadId.trim())==false){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLstResultsTypeOfResPubl.add.errorFormatNotValidPublHumanReadId", FacesContext
								.getCurrentInstance());
				return;
			}
			
			String[] comparableIDs = SpecVerLstResultsTypeOfResPublDTO.getValidPublHumanReadId(selectedPublHumanReadId.trim());
			
			if(selectedPublDisplName==null || "".equals(selectedPublDisplName.trim())){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLstResultsTypeOfResPubl.add.errorPublDisplName", FacesContext
								.getCurrentInstance());
				return;	
			}
			
			if(isInteger(selectedYear)==false){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLstResultsTypeOfResPubl.add.errorYear", FacesContext
								.getCurrentInstance());
				return;
			}
			
			Integer year = Integer.parseInt(selectedYear);
			
			ResultsTypeDTO resultsType = null;
			for (ResultsTypeDTO rt : allResultsTypes) {
				if(rt.getClassComparableId().equalsIgnoreCase(selectedResultsTypeCompariableId)){
					resultsType = rt;
					break;
				}
			}
						
			if(resultsType == null){
				facesMessages.addToControlFromResourceBundle(
						"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.specVerLstResultsTypeOfResPubl.add.errorResultsType", FacesContext
								.getCurrentInstance());
				return;
			}

			for (String comparableID : comparableIDs) {
				SpecVerLstResultsTypeOfResPublDTO sVLRTOfRP = new SpecVerLstResultsTypeOfResPublDTO(selectedSpecVerLst, resultsType, selectedPublication, comparableID, selectedPublDisplName, year);
				
				if(selectedSpecVerLst.isExistedSpecVerLstResultsTypeOfResPubl(sVLRTOfRP) == true){
					facesMessages.addToControlFromResourceBundle(
							"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.specVerLstResultsTypeOfResPubl.add.errorSameSpecVerLstResultsTypeOfResPubl", FacesContext
									.getCurrentInstance());
					return;	
				}
			
				SpecVerLstResultsTypeOfResPublDAO dao = new SpecVerLstResultsTypeOfResPublDAO();
				if(dao.add(sVLRTOfRP)==false){
					facesMessages.addToControlFromResourceBundle(
							"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.specVerLstResultsTypeOfResPubl.add.errorSpecVerLstResultsTypeOfResPubl", FacesContext
									.getCurrentInstance());
					return;	
				}
				selectedSpecVerLst.getSpecVerLstResultsTypeOfResPubls().add(sVLRTOfRP);
			}
			
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			
			orderByList.add("year");
			orderByList.add("resultsType");
			orderByList.add("publDisplName");
			directionsList.add("asc");
			directionsList.add("asc");
			directionsList.add("asc");
			Collections.sort(selectedSpecVerLst.getSpecVerLstResultsTypeOfResPubls(), new GenericComparator<SpecVerLstResultsTypeOfResPublDTO>(
					orderByList, directionsList));
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.specVerLstResultsTypeOfResPubl.add.successSpecVerLstResultsTypeOfResPubl");
			String messageToSend = MessageFormat.format(pattern, year, selectedPublDisplName, resultsType);
			facesMessages.addToControl("specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());
			
		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"specVerLstResultsTypeOfResPublEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLstResultsTypeOfResPubl.add.error", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	public void deleteSpecVerLstResultsTypeOfResPublToDatabase(){
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			
			String selectedYear = facesCtx.getExternalContext().getRequestParameterMap().get("selectedYear");
			Integer year = Integer.parseInt(selectedYear);	
			String selectedPublHumanReadId = facesCtx.getExternalContext().getRequestParameterMap().get("selectedPublHumanReadId");
			
			
			SpecVerLstResultsTypeOfResPublDTO sVLRTOfRP = selectedSpecVerLst.getSpecVerLstResultsTypeOfResPublByPublHumanReadIdAndYear(selectedPublHumanReadId, year);
			
			SpecVerLstResultsTypeOfResPublDAO dao = new SpecVerLstResultsTypeOfResPublDAO();
			List<SpecVerLstResultsTypeOfResPublDTO> listaTemp = selectedSpecVerLst.getSpecVerLstResultsTypeOfResPubls();
			int upperBoundaru = listaTemp.size()-1;
			for (int i = upperBoundaru; i >=0; i--) {
				if(listaTemp.get(i).equals(sVLRTOfRP)==true){
					if(dao.delete(listaTemp.get(i))==false){
						facesMessages.addToControlFromResourceBundle(
								"specVerLstEditForm:generalSpecVerLstResultsTypeOfResPubls", FacesMessage.SEVERITY_ERROR, 
								"records.specVerLst.delete.errorSpecVerLstResultsTypeOfResPubl", FacesContext
										.getCurrentInstance());
						return;
					}
					listaTemp.remove(i);
					break;
				}
			}
			
			String pattern = facesMessages.getMessageFromResourceBundle("records.specVerLst.delete.successSpecVerLstResultsTypeOfResPubl");
			String messageToSend = MessageFormat.format(pattern, sVLRTOfRP.getYear(), sVLRTOfRP.getPublDisplName(), sVLRTOfRP.getResultsType());
			facesMessages.addToControl("specVerLstEditForm:generalSpecVerLstResultsTypeOfResPubls", FacesMessage.SEVERITY_INFO, messageToSend, FacesContext.getCurrentInstance());

		} catch (Exception e) {
			facesMessages.addToControlFromResourceBundle(
					"specVerLstEditForm:generalSpecVerLstResultsTypeOfResPubls", FacesMessage.SEVERITY_ERROR, 
					"records.specVerLstResultsTypeOfResPubl.delete.errorSpecVerLstResultsTypeOfResPubl", FacesContext
							.getCurrentInstance());
			return;
		}
	}
	
	public List<SpecVerLstResultsTypeOfResPublDTO> getAllSpecVerLstResultsTypeOfResPubls(){
		List<SpecVerLstResultsTypeOfResPublDTO> retVal = selectedSpecVerLst.getSpecVerLstResultsTypeOfResPubls();

		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("year");
		orderByList.add("resultsType");
		orderByList.add("publDisplName");
		directionsList.add("asc");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(retVal, new GenericComparator<SpecVerLstResultsTypeOfResPublDTO>(
				orderByList, directionsList));
		
		return retVal;
	}
	
	private boolean isInteger(String vrednost){
		try {
			Integer.parseInt(vrednost);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	//--------------------------------------------------------------------specVerLstResultsTypeOfResPubl END
}
