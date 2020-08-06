package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.richfaces.component.UIDataGrid;
import org.richfaces.component.UIDataTable;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JobAdDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationSelectionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
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
 * Managed bean with CRUD operations for job competitions
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JobAdManagedBean extends CRUDManagedBean {

	private List<JobAdDTO> list;
	
	private List<JobAdDTO> duplicateJobAds;
	
	protected List<AuthorDTO> listAuthors = null;
	protected AuthorDTO selectedAuthor = null;
	protected PublicationDTO selectedPublication = null;
	protected List<PublicationSelectionDTO> listPublications = null;
	protected List<PublicationDTO> listSelectedPublications = null;
	
	protected boolean populateAuthorsList = true;
	protected boolean orderAuthorsList = true;
	
	protected boolean populatePublicationsList = true;
	protected boolean orderPublicationsList = true;
	
	protected boolean populateSelectedPublicationsList = true;
	protected boolean orderSelectedPublicationsList = true;
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private JobAdDTO selectedJobAd = null;

	public JobAdManagedBean(){
		super();
		pickSimilarMessage = "records.jobAd.pickSimilarMessage";
		tableModalPanel = "jobAdBrowseModalPanel";
		editModalPanel = "jobAdEditModalPanel";
		simpleEditModalPanel = "jobAdSimpleEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedJobAd = null;
		showApplications = false;
		showPublications = false;
		populateAuthorsList = true;
		listAuthors = null;
		selectedAuthor = null;
		populatePublicationsList = true;
		listPublications = null;
		listSelectedPublications = null;
		selectedPublication = null;
		pickMessage = null;
		pickMessageFirstTab = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickJobAdManagedBean = null;
		tableModalPanel = "jobAdBrowseModalPanel";
		editModalPanel = "jobAdEditModalPanel";
		simpleEditModalPanel = "jobAdSimpleEditModalPanel";
		return super.resetForm();
	}

	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<JobAdDTO> listTmp = getJobAds(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedJobAd != null
					&& selectedJobAd.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedJobAd.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataGrid table = (UIDataGrid)FacesContext.getCurrentInstance().getViewRoot().findComponent("jobAdTable");
					if(table!=null){
						int page = index / table.getElements();
						table.setFirst(table.getElements()*page);
					}
				}
				init = false;
			}

			list = listTmp;
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<JobAdDTO>();
		}
	}
	
	public void populateAuthorsList(){
		try {
			debug("populateAuthorsList");			
			List<AuthorDTO> listTmp = getAuthors(createAuthorsQuery(), null, null, new AllDocCollector(true));
			
			
					
			if (init == true && listTmp.size() != 0 && selectedJobAd != null
					&& selectedJobAd.getControlNumber() != null) {
	
				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedJobAd.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("authorsTable");
					if(table!=null){
						int page = index / table.getRows();
						table.setFirst(table.getRows()*page);
					}
				}
				init = false;
			}

			listAuthors = listTmp;
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("names");
			directionsList.add("asc");
			
			orderByList.add("controlNumber");
			directionsList.add("asc");
			Collections.sort(listAuthors, new GenericComparator<AuthorDTO>(
					orderByList, directionsList));
		} catch (ParseException e) {
			error("populateList", e);
			listAuthors = new ArrayList<AuthorDTO>();
		}
	}
	
	public void populatePublicationsList(){
		try {
			debug("populatePublicationsList");	
			JobAdDTO temp = selectedJobAd;
			selectedJobAd = null;
			List<PublicationDTO> listTmp = getPublications(createPublicationsQuery(), null, null, new AllDocCollector(true));
			selectedJobAd = temp;	
			
			
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
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("publicationsTable");
					if(table!=null){
						int page = index / table.getRows();
						table.setFirst(table.getRows()*page);
					}
				}
				init = false;
			}
			
			List<PublicationDTO> listTmpSelected = getPublications(createPublicationsQuery(), null, null, new AllDocCollector(true));

			listPublications = new ArrayList<PublicationSelectionDTO>();
			
			for (PublicationDTO publicationDTO : listTmp) {
				if(listTmpSelected.contains(publicationDTO))
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
	
	/**
	 * Retrieves a list of jobAds that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving jobAds
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of jobAds that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<JobAdDTO> getJobAds(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getJobAds(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of jobAds that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving jobAds
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of jobAds that correspond to the query
	 */
	private List<JobAdDTO> getJobAds(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<JobAdDTO> retVal = new ArrayList<JobAdDTO>();
		 
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				JobAdDTO dto = (JobAdDTO) record.getDto();
				if (dto != null)
					retVal.add(dto);
			} catch (Exception e) {
				error("getJobAds", e);
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
			Collections.sort(retVal, new GenericComparator<JobAdDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	protected List<AuthorDTO> getAuthors(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		
		List<Record> list = personDAO.getDTOs(query, hitCollector);
		for (Record record : list) {			
			try {
				AuthorDTO dto = (AuthorDTO) record.getDto();				
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
			Collections.sort(retVal, new GenericComparator<AuthorDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
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
	 * @return the list of jobAds (filtered and ordered by ...)
	 */
	public List<JobAdDTO> getJobAds() {
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
			Collections.sort(list, new GenericComparator<JobAdDTO>(
					orderByList, directionsList));
		}
		return list;
	}
	
	public List<AuthorDTO> getAuthors() {
		if(populateAuthorsList){
			populateAuthorsList();
			populateAuthorsList = false;
		}
		return listAuthors;
	}
	
	public List<PublicationSelectionDTO> getPublications() {
		if(populatePublicationsList){
			populatePublicationsList();
			populatePublicationsList = false;
		}
		return listPublications;
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
		} else if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if ((loggedAuthor.getControlNumber() != null)){
			bq.add(new TermQuery(new Term("APCN", getUserManagedBean().getLoggedUser().getAuthor().getControlNumber())), Occur.MUST);
			selectedAuthor = loggedAuthor;
		}
		bq.add(new TermQuery(new Term("TYPE", Types.JOB_AD)), Occur.MUST);
		
//		dodati i da li je komisija zakacena
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String currentDate = formatter.format(new Date());
		RangeQuery rq = new RangeQuery(new Term("ED", currentDate), null, true);
		bq.add(rq, Occur.MUST);
		return bq;
	}

	/**
	 * Creates query for retrieving authors list.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createAuthorsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		if(selectedJobAd != null){			
			bq.add(new TermQuery(new Term("JACN", selectedJobAd.getControlNumber())), Occur.MUST);
		}
		return bq;
	}
	
	/**
	 * Creates query for retrieving publications list.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createPublicationsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		BooleanQuery type = new BooleanQuery();
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_JOURNAL).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PATENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
		}
		if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PRODUCT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
			type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
		}
		bq.add(type, Occur.MUST);
		
		
		AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if ((loggedAuthor.getControlNumber() != null) && ((selectedAuthor == null) || (selectedAuthor.getControlNumber() == null))){
			selectedAuthor = loggedAuthor;
		}
		if(selectedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			authorsPapers.add(new TermQuery(new Term("EDCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		} 
		
		if(selectedJobAd != null){			
			bq.add(new TermQuery(new Term("JACN", selectedJobAd.getControlNumber())), Occur.MUST);
		}
		
		return bq;
	}
	
	List<JobAdDTO> similarJobAds = null;
	
	/**
	 * @return the list of SIMILAR jobAds with selected jobAd
	 */
	public List<JobAdDTO> getSimilarJobAds() {
		return similarJobAds;
	}

	/**
	 * Creates query for finding SIMILAR jobAds with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	protected Query createSimilarJobAdsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(selectedJobAd.getName().getContent()!=null)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedJobAd.getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		else if(selectedJobAd.getNameTranslations().size() > 0)
			bq.add(QueryUtils.makeBooleanQuery("NA", selectedJobAd.getNameTranslations().get(0).getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);	
		if (startDate != null) {
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String startDateString = formatter.format(startDate);
			bq.add(new TermQuery(new Term("SD", startDateString)), Occur.MUST);
		}
		if (selectedJobAd.getInstitution().getControlNumber() != null) {
			bq.add(new TermQuery(new Term("INCN", selectedJobAd.getInstitution().getControlNumber())), Occur.MUST);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.JOB_AD)), Occur.MUST);
		return bq;
	}
	
	public List<JobAdDTO> autocomplete(String suggest) {
		List<JobAdDTO> retVal = new ArrayList<JobAdDTO>();
		if(suggest.contains("(BISIS)")){
        	retVal.add((JobAdDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedJobAd!=null && selectedJobAd.getControlNumber() != null){
        	retVal.add(selectedJobAd);
        	return retVal;
        }
		
		String jobAdName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(jobAdName != null)
			bq.add(QueryUtils.makeBooleanQuery("NA", jobAdName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.JOB_AD)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				JobAdDTO dto = (JobAdDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }

	public List<JobAdDTO> autocompleteForSearch(String suggest) {
		List<JobAdDTO> retVal = new ArrayList<JobAdDTO>();
		if(suggest.contains("(BISIS)")){
        	retVal.add((JobAdDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
		
		String jobAdName = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(jobAdName != null)
			bq.add(QueryUtils.makeBooleanQuery("NA", jobAdName + ".", Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.JOB_AD)), Occur.MUST);
		
		List<Record> listRecord = recordDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				JobAdDTO dto = (JobAdDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	/**
	 * @return the selected jobAd
	 */
	public JobAdDTO getSelectedJobAd() {
		return selectedJobAd;
	}

	/**
	 * @param jobAdDTO
	 *            the jobAd to set as selected jobAd
	 */
	public void setSelectedJobAd(JobAdDTO jobAdDTO) {
		selectedJobAd = jobAdDTO;
		populateAuthorsList = true;
	}
	
	private boolean showApplications = false;
	
	public void exitApplications(){
		showApplications = false;
	}
	
	/**
	 * @return the selected jobAd
	 */
	public JobAdDTO getSelectedJobAdApplications() {
		if(showApplications)
			return selectedJobAd;
		else
			return null;
	}

	/**
	 * @param jobAdDTO
	 *            the jobAd to set as selected jobAd
	 */
	public void setSelectedJobAdApplications(JobAdDTO jobAdDTO) {
		if(jobAdDTO !=null){
			showApplications = true;
			setSelectedJobAd(jobAdDTO);
		}
	}
	
	private boolean showPublications = false;
	
	public void exitPublications(){
		showPublications = false;
	}
	
	/**
	 * @return the selected jobAd
	 */
	public JobAdDTO getSelectedJobAdPublications() {
		if(showPublications)
			return selectedJobAd;
		else
			return null;
	}

	/**
	 * @param jobAdDTO
	 *            the jobAd to set as selected jobAd
	 */
	public void setSelectedJobAdPublications(JobAdDTO jobAdDTO) {
		if(jobAdDTO !=null){
			showPublications = true;
			setSelectedJobAd(jobAdDTO);
		}
	}	
	
	/**
	 * @return the selected author
	 */
	public AuthorDTO getSelectedAuthor() {
		return selectedAuthor;
	}

	/**
	 * @param authorDTO
	 *            the author to set as selected author
	 */
	public void setSelectedAuthor(AuthorDTO authorDTO) {
		selectedAuthor = authorDTO;
		populatePublicationsList = true;
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedJobAd = findJobAdByControlNumber(list);
		if (selectedJobAd != null) {
			startDate = selectedJobAd.getStartDate().getTime();
			endDate = selectedJobAd.getEndDate().getTime();
			super.switchToUpdateMode();
			debug("switchToUpdateMode: \n" + selectedJobAd);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleUpdateMode()
	 */
	@Override
	public void switchToSimpleUpdateMode() {
		if (tableMode == ModesManagedBean.MODE_NONE)
			super.switchToSimpleUpdateMode();
		else {
			selectedJobAd = findJobAdByControlNumber(list);
			if (selectedJobAd != null) {
				super.switchToSimpleUpdateMode();
				startDate = selectedJobAd.getStartDate().getTime();
				endDate = selectedJobAd.getEndDate().getTime();
				debug("switchToSimpleUpdateMode: \n" + selectedJobAd);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedJobAd = new JobAdDTO();
		selectedJobAd.getName().setContent(name);
		startDate = null;
		endDate = null;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToSimpleAddMode()
	 */
	@Override
	public void switchToSimpleAddMode() {
		super.switchToSimpleAddMode();
		selectedJobAd = new JobAdDTO();
		selectedJobAd.getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
		selectedJobAd.setInstitution((InstitutionDTO)recordDAO.getDTO("(BISIS)5928"));
		startDate = null;
		endDate = null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedJobAd = findJobAdByControlNumber(list);
		if (selectedJobAd != null) {
			super.switchToViewDetailsMode();
			startDate = selectedJobAd.getStartDate().getTime();
			endDate = selectedJobAd.getEndDate().getTime();
			debug("switchToViewDetailsMode: \n" + selectedJobAd);
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
			selectedJobAd = findJobAdByControlNumber(list);
			if (selectedJobAd != null) {
				super.switchToSimpleViewDetailsMode();
				startDate = selectedJobAd.getStartDate().getTime();
				endDate = selectedJobAd.getEndDate().getTime();
				debug("switchToViewDetailsMode: \n" + selectedJobAd);
			}
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		jobAdControlNumber = null;
		name = null;
		duplicateJobAds = new ArrayList<JobAdDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		super.switchToBrowseMode();
		jobAdControlNumber = null;
		name = null;
		duplicateJobAds = new ArrayList<JobAdDTO>();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		selectedJobAd.setNotLoaded(true);
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
		}
		Calendar sd = new GregorianCalendar();
		sd.setTime(startDate);
		selectedJobAd.setStartDate(sd);
		Calendar ed = new GregorianCalendar();
		if(simpleForm){
			ed.setTime(startDate);
			ed.add(Calendar.DATE, 15);
		} else {
			ed.setTime(endDate);
		}
		selectedJobAd.setEndDate(ed);
		if (recordDAO.update(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.EVENT, 
				selectedJobAd)) == false) {
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"jobAdSimpleEditForm:general":"jobAdEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.jobAd.update.error", FacesContext
							.getCurrentInstance());
		} else {
			init = true;
			nextEditTab();
			nextEditTab();
			facesMessages.addToControlFromResourceBundle(
					(simpleForm)?"jobAdSimpleEditForm:general":"jobAdEditForm:general", FacesMessage.SEVERITY_INFO, 
					"records.jobAd.update.success", FacesContext
							.getCurrentInstance());
			populateList = true;
			orderList = true;
			debug("updated: \n" + selectedJobAd);
			sendRecordMessage(selectedJobAd, "update");
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
		}
		similarJobAds = null;
		Calendar sd = new GregorianCalendar();
		sd.setTime(startDate);
		selectedJobAd.setStartDate(sd);
		Calendar ed = new GregorianCalendar();
		if(simpleForm){
			ed.setTime(startDate);
			ed.add(Calendar.DATE, 15);
		} else {
			ed.setTime(endDate);
		}
		selectedJobAd.setEndDate(ed);
		if((getUserManagedBean().isLoggedIn()) && (getUserManagedBean().getLoggedUser().getAuthor().getControlNumber() != null)){
			selectedJobAd.getApplications().add(getUserManagedBean().getLoggedUser().getAuthor());
		}
		if((editTabNumber == 0) && (selectedJobAd.getCommission() != null)){
			try {
				debug("findSimilarJobAds");
				similarJobAds = getJobAds(createSimilarJobAdsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarJobAds", e);
			}
		}
		if((similarJobAds == null ) || (similarJobAds.size()==0)){		
			if (recordDAO.add(new Record(getUserManagedBean().getLoggedUser()
					.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.EVENT, 
					selectedJobAd)) == false) {
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"jobAdSimpleEditForm:general":"jobAdEditForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.jobAd.add.error", FacesContext
								.getCurrentInstance());
			} else {
				init = true;
				if(editTabNumber == 0)
					nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						(simpleForm)?"jobAdSimpleEditForm:general":"jobAdEditForm:general", FacesMessage.SEVERITY_INFO, 
						"records.jobAd.add.success", FacesContext
								.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("added: \n" + selectedJobAd);
				newRecordEmailNotification(selectedJobAd, facesMessages.getMessageFromResourceBundle("records.jobAd.newJobAdNotification.subject"));
			}
		} else {
			nextEditTab();
		}
	}
	
	public void updateSelectedPublications() {
		boolean allRight = true;
		for (PublicationSelectionDTO publicationSelection : listPublications) {
			PublicationDTO publication = publicationSelection.getPublication();
			boolean update = false;
			if(publicationSelection.isSelected()){
				if(! publication.getJobAds().contains(selectedJobAd)){
					publication.getJobAds().add(selectedJobAd);
					update = true;
				} 
			} else {
				if(publication.getJobAds().contains(selectedJobAd)){
					publication.getJobAds().remove(selectedJobAd);
					update = true;
				} 
			}
			if(update){
				if (recordDAO.update(new Record(null, null, getUserManagedBean()
						.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), publication.getRecord().getType(), 
						publication)) == false) {
					allRight = false;
					break;
				} 
			}
		}
		if(allRight){
			facesMessages.addToControlFromResourceBundle(
					"publicationsForm:general", FacesMessage.SEVERITY_INFO, 
					"records.jobAd.publications.update.success", FacesContext
							.getCurrentInstance());
			debug("updated: \n" + selectedJobAd);
		} else {
			facesMessages.addToControlFromResourceBundle(
					"publicationsForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.jobAd.publications.update.error", FacesContext
							.getCurrentInstance());
		}
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

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedJobAd = findJobAdByControlNumber(list);
		if (selectedJobAd == null)
			return;
		if (recordDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.EVENT, 
				selectedJobAd)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"jobAdPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.jobAd.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedJobAd);
			selectedJobAd = null;
			populateList = true;
			orderList = true;
		}
	}
	
	public void chooseDuplicateJobAd() {
		try {
			JobAdDTO duplicateJobAd = findJobAdByControlNumber(list);
			if (duplicateJobAd != null) {
				if(duplicateJobAds.contains(duplicateJobAd))
					duplicateJobAds.remove(duplicateJobAd);
				else
					duplicateJobAds.add(duplicateJobAd);
			}
		} catch (Exception e) {
			error("chooseDuplicateJobAd", e);
		}
	}
	
	public void replaceDuplicateJobAds(){
		try {
			selectedJobAd = findJobAdByControlNumber(list);
			if ((selectedJobAd != null) && (!(duplicateJobAds.contains(selectedJobAd)))){
				
				for (JobAdDTO duplicateJobAd : duplicateJobAds) {
					Query query = new TermQuery(new Term("JACN", duplicateJobAd.getControlNumber()));
					List<Record> list = recordDAO.getDTOs(query, new AllDocCollector(false));
					for (Record record : list) {
						record.loadFromDatabase();
						RecordDTO dto = record.getDto();
						if (dto instanceof PublicationDTO){
							PublicationDTO temp = (PublicationDTO)(dto);
							temp.getJobAds().remove(selectedJobAd);
							for (JobAdDTO jobAd : temp.getJobAds()) {
								if (jobAd.equals(duplicateJobAd)){
									jobAd.setControlNumber(selectedJobAd.getControlNumber());
									jobAd.setName(selectedJobAd.getName());
								}
							}
						} else if (dto instanceof AuthorDTO){
							AuthorDTO temp = (AuthorDTO)(dto);
							temp.getJobApplications().remove(selectedJobAd);
							for (JobAdDTO jobAd : temp.getJobApplications()) {
								if (jobAd.equals(duplicateJobAd)){
									jobAd.setControlNumber(selectedJobAd.getControlNumber());
									jobAd.setName(selectedJobAd.getName());
								}
							}
						} else {
							facesMessages.addToControlFromResourceBundle(
									(tableMode == ModesManagedBean.MODE_PICK)?("jobAdPickForm:general"):("jobAdBrowseForm:general"), FacesMessage.SEVERITY_ERROR, 
									"records.jobAd.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					debug("jobAd: \n" + duplicateJobAd.toString() +  "\n\nreplaced with: \n" + selectedJobAd.toString());
					recordDAO.delete(duplicateJobAd.getRecord());
				}
				facesMessages.addToControlFromResourceBundle(
						(tableMode == ModesManagedBean.MODE_PICK)?("jobAdPickForm:general"):("jobAdBrowseForm:general"), FacesMessage.SEVERITY_INFO, 
						"records.jobAd.replace.success", FacesContext
								.getCurrentInstance());
				selectedJobAd = null;
				populateList = true;
				orderList = true;
				duplicateJobAds = new ArrayList<JobAdDTO>();
			} else {
				facesMessages.addToControlFromResourceBundle(
						(tableMode == ModesManagedBean.MODE_PICK)?("jobAdPickForm:general"):("jobAdBrowseForm:general"), FacesMessage.SEVERITY_ERROR, 
						"records.jobAd.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateJobAds", e);
		}
	}

	/**
	 * @return the duplicateJobAds
	 */
	public String getDuplicateJobAdsAsString() {
		StringBuffer retVal = new StringBuffer();
		if(duplicateJobAds!=null)
			for (JobAdDTO duplicateJobAd : duplicateJobAds) {
				retVal.append(duplicateJobAd + "\n");
			}
		return retVal.toString();
	}

	/**
	 * Sets chosen jobAd to the CRUDManagedBean which wanted to pick
	 * jobAd
	 */
	public void chooseJobAd() {

		try {
			if(tableTabNumber != 0) 
				selectedJobAd = findJobAdByControlNumber(list);
			if (selectedJobAd != null) {
				iPickJobAdManagedBean.setJobAd(selectedJobAd);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseJobAd", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedJobAd = findJobAdByControlNumber(similarJobAds);
			if (selectedJobAd != null) {
				iPickJobAdManagedBean.setJobAd(selectedJobAd);
			}
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilarJobAd", e);
		}
	}
	

	private IPickJobAdManagedBean iPickJobAdManagedBean = null;

	/**
	 * @param iPickJobAdManagedBean
	 *            the CRUDManagedBean which wants to pick jobAd
	 */
	public void setIPickJobAdManagedBean(
			IPickJobAdManagedBean iPickJobAdManagedBean) {
		this.iPickJobAdManagedBean = iPickJobAdManagedBean;
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
	 * @return the pick message for second tab if SIMILAR jobAds do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR jobAds do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR jobAds do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR jobAds do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR jobAds  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR jobAds exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR jobAds exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR jobAds exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR jobAds exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR jobAds exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR jobAds exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR jobAds exist to set
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
		retVal = "jobAdPage";
		return retVal;
	}

	private String jobAdControlNumber;
	
	/**
	 * @return the jobAdControlNumber
	 */
	public String getJobAdControlNumber() {
		return jobAdControlNumber;
	}

	/**
	 * @param jobAdControlNumber the jobAdControlNumber to set
	 */
	public void setJobAdControlNumber(String jobAdControlNumber) {
		this.jobAdControlNumber = jobAdControlNumber;
		selectedJobAd = (JobAdDTO)recordDAO.getDTO(jobAdControlNumber);
	}

	private JobAdDTO findJobAdByControlNumber(List<JobAdDTO> jobAdsList) {
		JobAdDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (JobAdDTO dto : jobAdsList) {
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
	
	protected AuthorDTO findAuthorByControlNumber(List<AuthorDTO> authorsList) {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext().getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : authorsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
								.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	protected PublicationDTO findPublicationByControlNumber(List<PublicationDTO> publicationsList) {
		PublicationDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext().getRequestParameterMap().get("controlNumber");
			for (PublicationDTO dto : publicationsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
								.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}	

	private String name;

	/**
	 * @return the jobAd name for filtering jobAds
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
			iPickJobAdManagedBean.cancelPickingJobAd();
		}
		super.switchToTableNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_ADD) && (tableMode == ModesManagedBean.MODE_PICK)
				&& (customPick)) {
			iPickJobAdManagedBean.setJobAd(selectedJobAd);
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
		this.openMultilingualContentForm(editMode, selectedJobAd.getNameTranslations(), false, "records.jobAd.editPanel.nameTranslations.panelHeader", "records.jobAd.editPanel.nameTranslations.contentHeader");
	}
	
	public void descriptionTranslations(){
		this.openMultilingualContentForm(editMode, selectedJobAd.getDescriptionTranslations(), true, "records.jobAd.editPanel.descriptionTranslations.panelHeader", "records.jobAd.editPanel.descriptionTranslations.contentHeader");
	}
	
	public void keywordsTranslations(){
		this.openMultilingualContentForm(editMode, selectedJobAd.getKeywordsTranslations(), false, "records.jobAd.editPanel.keywordsTranslations.panelHeader", "records.jobAd.editPanel.keywordsTranslations.contentHeader");
	}
	
	public void conditionsTranslations(){
		this.openMultilingualContentForm(editMode, selectedJobAd.getConditionsTranslations(), true, "records.jobAd.editPanel.conditionsTranslations.panelHeader", "records.jobAd.editPanel.conditionsTranslations.contentHeader");
	}
	
	private Date startDate = null;
	
	private Date endDate = null;
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void enterPage(PhaseEvent event){
		if(init == false){
			if (populateList == false)
				populateList();
			enterCRUDPage();
			init = true;
		}
		selectedJobAd = findJobAdByControlNumber(list);	
		if (selectedJobAd != null) {			
			populateAuthorsList();
		}
	}
	
	public void jobAdEnter(PhaseEvent event) {
		getUserManagedBean().setJobAd(true);
	}
	
	public void jobAdExit(PhaseEvent event) {
		getUserManagedBean().setJobAd(false);
	}
	
	private boolean validateAll(){
		boolean retVal = true;
//		if ((selectedJobAd.getInstitution() == null) || (selectedJobAd.getInstitution().getControlNumber() == null) || (selectedPaperJournal.getJournal().getControlNumber().trim().equals(""))){
//			facesMessages.addToControlFromResourceBundle(
//					"jobAdSimpleEditForm:institution", FacesMessage.SEVERITY_ERROR, 
//					"javax.faces.component.UIInput.REQUIRED",
//					FacesContext.getCurrentInstance());
//			retVal = false;
//		}
//		if ((selectedJobAd.getName() == null) || (selectedJobAd.getName().getLanguage() == null) || (selectedJobAd.getName().getLanguage().trim().equals(""))){
//			facesMessages.addToControlFromResourceBundle(
//					"jobAdSimpleEditForm:language", FacesMessage.SEVERITY_ERROR, 
//					"javax.faces.component.UIInput.REQUIRED",
//					FacesContext.getCurrentInstance());
//			retVal = false;
//		}
		if (selectedJobAd.getPlace() == null){
			facesMessages.addToControlFromResourceBundle(
					"jobAdSimpleEditForm:place", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if ((selectedJobAd.getSomeName() == null) || (selectedJobAd.getSomeName().trim().equals(""))){
			facesMessages.addToControlFromResourceBundle(
					"jobAdSimpleEditForm:name", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
		if (startDate == null){
			facesMessages.addToControlFromResourceBundle(
					"jobAdSimpleEditForm:startDate", FacesMessage.SEVERITY_ERROR, 
					"javax.faces.component.UIInput.REQUIRED",
					FacesContext.getCurrentInstance());
			retVal = false;
		}
//		if (endDate == null){
//			facesMessages.addToControlFromResourceBundle(
//					"jobAdSimpleEditForm:endDate", FacesMessage.SEVERITY_ERROR, 
//					"javax.faces.component.UIInput.REQUIRED",
//					FacesContext.getCurrentInstance());
//			retVal = false;
//		}
		return retVal;
	}

}
