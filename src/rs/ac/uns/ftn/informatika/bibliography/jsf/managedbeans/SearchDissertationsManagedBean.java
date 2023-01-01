package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationRecommendationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.QueryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.personalization.FeedbackLogger;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.ClientLocation;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.GeolocationManager;

@SuppressWarnings({"serial", "unchecked"})
public class SearchDissertationsManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean {
	
	private static Log logRepresentationStyle = LogFactory.getLog("rs.representationStyle");
	
	private static int repStyle = 1;
	
	protected List<QueryDTO> searchQueries = null;
	protected List<SelectItem> searchQueryOperator = null;
	protected int counterSearchQuery = 0;
	
	protected String [] searchQueryFieldNameString = new String [] {"FT_SRP", "TAK_SRP", "TI_SRP", "AB_SRP", "KW_SRP", "AU", "AD", "CC", "CM"};
	protected String [] searchQueryTypeString = new String [] {QueryDTO.ALL, QueryDTO.EXACT_PHRASE, QueryDTO.AT_LEAST_ONE, QueryDTO.NONE, QueryDTO.SIMILAR, QueryDTO.MORE_EXACT_PHRASES};
	
	protected List<SelectItem> dateRange = null;
	
	protected boolean fuzzy = false; //for defining fuzzy query
	protected boolean advanced = false;
	
	protected String defended = null;
	
	protected String fromDate = "";
	protected String toDate = "";
			
	protected String publicationType = "";
	
	protected List<AuthorDTO> authorSearched = null;
	protected String authorName="";
	
	protected Connection conn = null;
	
	protected CERIFSemanticLayerDB cslDB = new CERIFSemanticLayerDB();
	
	protected String representationStyle = "wordCloud";
	protected String sortMethod = "relevance";
	
	
	// Search possible Types in correspondence to positions in searchQueryFieldNameString 
	// All fields;	Article Title, Abstract, Keywords;	Authors;   First Author;	Article Title;	Abstract;	Keywords;	Conference;		Journal
	
	//	Search possible publication Types in correspondence to positions in searchQueryFieldNameString
	//	All;	Conference paper;	Journal paper;	Monograph;	Thesis;
	
	public boolean populateAll = true;
	protected List<TreeNodeDTO<Object>> root = null;
	protected static List<InstitutionDTO> allInstitutions = null;
	protected List<TreeNodeDTO<Object>> allInstitutionsAndDegrees = null;
	
	// Advance query
	protected String luceneQuery = "";
	protected String basicQuery = "";
	protected List<StudyFinalDocumentDTO> records =null;
	protected List<PublicationRecommendationDTO> recommendedRecords =null;
	protected String userId = null;
	protected String searchQueryError = null;
	
	protected List<SelectItem> sortMethodItems;
	protected List<SelectItem> representationStyleItems;
	
	/**
	 * Constructor
	 */
	public SearchDissertationsManagedBean() {
		super();
		try {
			conn = DataSourceFactory.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		Cookie[] cookies = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("languageSearchDissertations")) {
					setLocale(cookie.getValue());
				} else if (cookie.getName().equals("userId")){
					userId = cookie.getValue();
//					if(userId != null){
//						if(userId.contains("["))
//							userId = userId.substring(0, userId.indexOf("["));
//						userId.trim();
//					}
				} else if (cookie.getName().equals("representationStyleSearchDissertations")) {
//					representationStyle = cookie.getValue();
				}
			}
		}
		init();
	}
	/**
	 * Initialized 
	 */
	@SuppressWarnings("rawtypes")
	public void init ()
	{
		counterSearchQuery = 0;
		searchQueryOperator = new ArrayList<SelectItem>(Arrays.asList(new SelectItem("AND", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.and")), new SelectItem("OR", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.or")), new SelectItem("AND NOT", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.andNot"))));
		searchQueries = new ArrayList<QueryDTO>();
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[2], searchQueryTypeString[0]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[5], searchQueryTypeString[5]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[0], searchQueryTypeString[0]));
		if (dateRange==null)
		{	
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			dateRange = new ArrayList() ;
			for (int i = currentYear; i>=1980;i--)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}
			for (int i = 1975; i>=1955;i=i-5)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}
		}
		fuzzy = false;
		advanced = false;
		fromDate = "";
		toDate = "";
		authorName="";
		authorSearched = new ArrayList<AuthorDTO>();
		populateAll = true;
		init=true;
		publicationType = "";
		luceneQuery = "";
		basicQuery = "";
		searchQueryError = null;
		records =null;
		recommendedRecords = null;
		root= null;
		allInstitutionsAndDegrees=null;
		populateAll = true;
//		populateAll();
		
		defended = getSearchDefendedNameString()[0];
		//mozda treba obrisati
		setAuthorManageBeanToPick();
		loadRecommendedRecords();
		sortMethodItems = new ArrayList<SelectItem>();
		sortMethodItems.add(new SelectItem("relevance", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.relevance")));
		sortMethodItems.add(new SelectItem("date", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.date")));
		if((recommendedRecords != null) && (recommendedRecords.size() > 0)){
			sortMethod = "mixed";
			sortMethodItems.add(new SelectItem("recommendation", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.recommendation")));
			sortMethodItems.add(new SelectItem("mixed", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.mixed")));
		} else 
			sortMethod = "relevance";
		representationStyleItems = new ArrayList<SelectItem>();
		representationStyleItems.add(new SelectItem("htmlRepresentation", facesMessages.getMessageFromResourceBundle("search.panelResult.representationStyle.htmlRepresentation")));
		representationStyleItems.add(new SelectItem("wordCloud", facesMessages.getMessageFromResourceBundle("search.panelResult.representationStyle.wordCloud")));
		if(repStyle == 0){
			representationStyle = "wordCloud";
			repStyle = 1;
		} else {
			representationStyle = "htmlRepresentation";
			repStyle = 1;//0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException throwables) {
			}
		}
		super.finalize();
	}

	/**
	 * @return the searchQueries
	 */
	public List<QueryDTO> getSearchQueries() {

		return searchQueries;
	}
	/**
	 * @param searchQueries the searchQueries to set
	 */
	public void setSearchQueries(List<QueryDTO> searchQueries) {
		this.searchQueries = searchQueries;
	}
	/**
	 * @return the searchQueryOperator
	 */
	public List<SelectItem> getSearchQueryOperator() {
		return searchQueryOperator;
	}

	/**
	 * @return the counterSearchQuery
	 */
	public int getCounterSearchQuery() {
		return counterSearchQuery;
	}
	
	/**
	 * @return the searchQueryFieldNameString
	 */
	public String[] getSearchQueryFieldNameString() {
		return searchQueryFieldNameString;
	}
	
	/**
	 * @return the searchQueryTypeString
	 */
	public String[] getSearchQueryTypeString() {
		return searchQueryTypeString;
	}
	
	/**
	 * @return the searchDefendedNameString
	 */
	public String[] getSearchDefendedNameString() {
		return StudyFinalDocumentDTO.defendedNameString;
	}
	/**
	 * @return the fuzzy
	 */
	public boolean isFuzzy() {
		return fuzzy;
	}
	/**
	 * @param fuzzy the fuzzy to set
	 */
	public void setFuzzy(boolean fuzzy) {
		this.fuzzy = fuzzy;
	}
	
	/**
	 * @return the advanced
	 */
	public boolean isAdvanced() {
		return advanced;
	}
	/**
	 * @param advanced the advanced to set
	 */
	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}
	
	public String getAdvancedString(){
		if(advanced)
			return "advanced";
		else
			return "basic";
	}
	
	public void switchBasicAdvanced(){
		advanced = ! advanced;
	}
	
	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}	
	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	/**
	 * @return the publicationType
	 */
	public String getPublicationType() {
		return publicationType;
	}
	/**
	 * @param publicationType the publicationType to set
	 */
	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}
	/**
	 * @return the dateRange
	 */
	public List<SelectItem> getDateRange() {
		return dateRange;
	}
	
	/**
	 * @return the defended
	 */
	public String getDefended() {
		return defended;
	}
	/**
	 * @param defended the defended to set
	 */
	public void setDefended(String defended) {
		this.defended = defended;
	}
	/**
	 * Removes query form list
	 */
	public void removeQuery(){
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		int removedQuery = Integer.parseInt((facesCtx.getExternalContext().getRequestParameterMap().get("removedQuery")).trim());
		
		boolean found = false;
		for (int i = 0; i < searchQueries.size(); i++)
		{
			if(removedQuery==searchQueries.get(i).getId() && found==false)
			{
				searchQueries.remove(i);
				counterSearchQuery--;
				found=true;
			}
			if(found==true && i!=searchQueries.size())
			{
				searchQueries.get(i).setId(i);
			}
		}
	}
	
	/**
	 * Adds query form list
	 */
	public void addQuery(){
		searchQueries.add(new QueryDTO(counterSearchQuery,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[0], searchQueryTypeString[0]));
		counterSearchQuery = counterSearchQuery+1;
	}
	
	//	
	//	SINISA POCETAK
	//	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		if (init == true)
		{	
			init= false;
		}
	}
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "searchDissertationsPage";
		return retVal;
	}
	
	
	public String searchPageEnter() {
		resetForm();
		
		setAuthorManageBeanToPick();
		root= null;
		allInstitutionsAndDegrees=null;
		populateAll = true;
		populateAll();
		
		
		
		returnPage = "indexPage";
		return "searchDissertationsPage";
	}
	
	public void searchPageEnter(PhaseEvent event){
		String institutionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("institution");
		if((init == false) || (institutionId != null)){
			resetForm();
			
			setAuthorManageBeanToPick();
			
			root = null;
			allInstitutionsAndDegrees=null;
			
			populateAll();
			
			if (institutionId != null){
				changeSelectionOnTree(institutionId);
				advanced = true;
			}
			init = true;
		}	
	}
	
	/**
	 * Resets form to the initial state
	 * 
	 * @return the outcome string for JSF navigation
	 */
	@Override
	public String resetForm() {
		
		counterSearchQuery = 0;
		searchQueryOperator = new ArrayList<SelectItem>(Arrays.asList(new SelectItem("AND", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.and")), new SelectItem("OR", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.or")), new SelectItem("AND NOT", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.andNot"))));
		searchQueries = new ArrayList<QueryDTO>();
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[2], searchQueryTypeString[0]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[5], searchQueryTypeString[5]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[0], searchQueryTypeString[0]));
		fuzzy = false;
		advanced = false; //false
		fromDate = "";
		toDate = "";
		authorName="";
		sortMethodItems = new ArrayList<SelectItem>();
		sortMethodItems.add(new SelectItem("relevance", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.relevance")));
		sortMethodItems.add(new SelectItem("date", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.date")));
		if((recommendedRecords != null) && (recommendedRecords.size() > 0)){
			sortMethod = "mixed";
			sortMethodItems.add(new SelectItem("recommendation", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.recommendation")));
			sortMethodItems.add(new SelectItem("mixed", facesMessages.getMessageFromResourceBundle("search.panelResult.sortMethod.mixed")));
		} else 
			sortMethod = "relevance";
		representationStyleItems = new ArrayList<SelectItem>();
		representationStyleItems.add(new SelectItem("htmlRepresentation", facesMessages.getMessageFromResourceBundle("search.panelResult.representationStyle.htmlRepresentation")));
		representationStyleItems.add(new SelectItem("wordCloud", facesMessages.getMessageFromResourceBundle("search.panelResult.representationStyle.wordCloud")));
		if(repStyle == 0){
			representationStyle = "wordCloud";
			repStyle = 1;
		} else {
			representationStyle = "htmlRepresentation";
			repStyle = 1;//0;
		}
		populateAll = true;
		authorSearched = new ArrayList<AuthorDTO>();
		publicationType = "";
		luceneQuery = "";
		basicQuery = "";
		searchQueryError = null;
		records =null;
		defended = getSearchDefendedNameString()[0];
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#setAuthor(rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO)
	 */
	@Override
	public void setAuthor(AuthorDTO author) {
		authorSearched = new ArrayList<AuthorDTO>();
		authorSearched.add(author);
		authorName = author.getNames();
		setAuthorManageBeanToPick();
	}
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#cancelPickingAuthor()
	 */
	@Override
	public void cancelPickingAuthor() {
		// TODO Auto-generated method stub
		
	}	
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	/**
	 * @return the representationStyle
	 */
	public String getRepresentationStyle() {
		return representationStyle;
	}
	/**
	 * @param representationStyle the representationStyle to set
	 */
	public void setRepresentationStyle(String representationStyle) {
		Calendar date = new GregorianCalendar();
		date.set(GregorianCalendar.YEAR, new GregorianCalendar().get(GregorianCalendar.YEAR)+1);
		((HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse()).addHeader("Set-Cookie",
				"representationStyleSearchDissertations=" + representationStyle + ";HttpOnly;expires=" + date.getTime());
		this.representationStyle = representationStyle;
		HttpServletRequest req = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
		HttpServletResponse res = ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse());
		
		String ipAddress = null;
		try{
			ipAddress  = req.getHeader("X-FORWARDED-FOR");  
			if(ipAddress == null)  
	        {  
				ipAddress = req.getRemoteAddr();  
	        }  else {
	        	ipAddress += "(proxy = " + req.getRemoteAddr() + ")";
	        }
		} catch (Exception ex){
		}
		
		ClientLocation location = GeolocationManager.getLocation(req.getRemoteAddr());
	
		String sessionId = "not defined";
		String userId = null;
		for (Cookie cookie:req.getCookies())
			if(cookie.getName().equals("JSESSIONID"))
				sessionId = cookie.getValue();
			else if (cookie.getName().equals("userId"))
				userId = cookie.getValue();
		
		if(userId == null){
			userId = "" + System.currentTimeMillis() + "" + (int)(Math.random() * 100);
			Cookie userCookie = new Cookie("userId", userId);
			userCookie.setMaxAge(60*60*24*365*10); //Store cookie for 10 years
			userCookie.setPath("/");
			res.addCookie(userCookie);
		}
		
		String userAgent = req.getHeader("User-Agent");
		
		Date dateCurrent = new Date();
		logRepresentationStyle.info("Date and time: " + dateCurrent + "| miliseconds: " + dateCurrent.getTime() + "| + session id: " +
				"" + sessionId + "| userId: " + userId + "| ip address: " + ipAddress + "| location: " + location.toString() + "" +
				"| user agent (device): " + userAgent + "| new representation style: " + representationStyle );	
	}
	
	/**
	 * @return the sortMethod
	 */
	public String getSortMethod() {
		return sortMethod;
	}
	/**
	 * @param sortMethod the sortMethod to set
	 */
	public void setSortMethod(String sortMethod) {
		this.sortMethod = sortMethod;
	}
	
	/**
	 * @return the sortMethodItems
	 */
	public List<SelectItem> getSortMethodItems() {
		return sortMethodItems;
	}
	/**
	 * @param sortMethodItems the sortMethodItems to set
	 */
	public void setSortMethodItems(List<SelectItem> sortMethodItems) {
		this.sortMethodItems = sortMethodItems;
	}
	
	
	/**
	 * @return the representationStyleItems
	 */
	public List<SelectItem> getRepresentationStyleItems() {
		return representationStyleItems;
	}
	/**
	 * @param representationStyleItems the representationStyleItems to set
	 */
	public void setRepresentationStyleItems(
			List<SelectItem> representationStyleItems) {
		this.representationStyleItems = representationStyleItems;
	}
	/**
	 * @return the authorSearched
	 */
	public List<AuthorDTO> getAuthorSearched() {
		return authorSearched;
	}
	/**
	 * @param authorSearched the authorSearched to set
	 */
	public void setAuthorSearched(List<AuthorDTO> authorSearched) {
		this.authorSearched = authorSearched;
	}
	/**
	 * Sets all necessary things for AuthorManageBean in pick mode
	 */
	public void setAuthorManageBeanToPick() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}

		mb.setIPickAuthorManagedBean(this);
		mb.setSelectedAuthor(null);
		mb.setCustomPick(true);
		mb.setQueryFieldName(null);
		mb.switchToPickMode();
	}
	
	
	/**
	 * Searches for author in database
	 */
	public void btnAuthorFind()
	{
		records =null;
		searchQueryError=null;
		
		int sizeAuthorSelectedHistory = authorSearched.size();
		for(int i =0; i<sizeAuthorSelectedHistory; i++)
			authorSearched.remove(0);
		
		
		if(authorName!=null && !authorName.trim().equalsIgnoreCase(""))
		{	
			BooleanQuery bq = new BooleanQuery();
			bq.add(QueryUtils.makeBooleanQuery("AU", authorName, Occur.SHOULD, 0.8f, 0.7f, false), Occur.MUST);
			bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
			BooleanQuery unsDissertation = new BooleanQuery();
			unsDissertation.add(new TermQuery(new Term("AUTHORUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("ADVISORUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONCHAIRUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONMEMBERUNSDISSERTATIONS", "true")), Occur.SHOULD);
			bq.add(unsDissertation, Occur.MUST);
			RecordDAO recordDAO = new RecordDAO(new PersonDB());
			List<Record> recordsTemp = recordDAO.getDTOs(bq, new TopDocCollector(10));
			for (Record record : recordsTemp) {
				authorSearched.add((AuthorDTO)record.getDto());
			}	
		}
		
	}
	
	public void linkAuthorDetailsFind() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		
		String queryType = extCtx.getRequestParameterMap().get("queryType");
		String authorID = extCtx.getRequestParameterMap().get("queryAuthorID");
		AuthorDTO selectedAuthor = null;
		for(AuthorDTO tempAuthor  : authorSearched)
		{
			if(tempAuthor.getControlNumber().equalsIgnoreCase(authorID))
			{
				selectedAuthor = tempAuthor;
				break;
			}
		}
		if(selectedAuthor != null){
			BooleanQuery query = new BooleanQuery();
			BooleanQuery type = new BooleanQuery();
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
			query.add(type, Occur.MUST);
			query.add(new TermQuery(new Term("UNSDISSERTATION", "true")), Occur.MUST);
			
			
			records =null;
			searchQueryError = null;
			
			if(queryType.equalsIgnoreCase(searchQueryFieldNameString[0])){
				query.add(new TermQuery(new Term("AUCN", selectedAuthor.getControlNumber())), Occur.MUST);
			} else if(queryType.equalsIgnoreCase(searchQueryFieldNameString[1])){
				query.add(new TermQuery(new Term("ADCN", selectedAuthor.getControlNumber())), Occur.MUST);
			} else if(queryType.equalsIgnoreCase(searchQueryFieldNameString[2])){
				query.add(new TermQuery(new Term("CCCN", selectedAuthor.getControlNumber())), Occur.MUST);
			} else if(queryType.equalsIgnoreCase(searchQueryFieldNameString[3])){
				query.add(new TermQuery(new Term("CMCN", selectedAuthor.getControlNumber())), Occur.MUST);
			}

			
			logQuery(query, "second");
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getDTOs(query, new AllDocCollector(true));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
			addRecommendationScores(userId, records);
			logResults(records, query, "second");
			Collections.sort(records, new GenericComparator<StudyFinalDocumentDTO>(
					"publicationYear", "desc"));
			
		}
		
	}
	
	/**
	 * Returns result from AutorManageBean for PUblicationPanel search
	 */
	public void switchToPick(ValueChangeEvent event)
	{
		String inputQueryFieldName = (String)event.getNewValue().toString();
		if(inputQueryFieldName.equalsIgnoreCase(searchQueryFieldNameString[5]) || inputQueryFieldName.equalsIgnoreCase(searchQueryFieldNameString[6]) || inputQueryFieldName.equalsIgnoreCase(searchQueryFieldNameString[7]))
		{	//author firstAuthor
			setAuthorManageBeanToPick();
		}
	}

	/**
	 * @return the luceneQuery
	 */
	public String getLuceneQuery() {
		return luceneQuery;
	}
	/**
	 * @param luceneQuery the luceneQuery to set
	 */
	public void setLuceneQuery(String luceneQuery) {
		this.luceneQuery = luceneQuery;
	}
	
	/**
	 * @return the basicQuery
	 */
	public String getBasicQuery() {
		return basicQuery;
	}
	/**
	 * @param basicQuery the basicQuery to set
	 */
	public void setBasicQuery(String basicQuery) {
		this.basicQuery = basicQuery;
	}
	
	
	
	/**
	 * @return the records
	 */
	public List<StudyFinalDocumentDTO> getRecords() {
		if(sortMethod.equals("date")){
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("publicationYear");
			directionsList.add("desc");
			orderByList.add("someHarvardRepresentation");
			directionsList.add("asc");
			List<StudyFinalDocumentDTO> temp = new ArrayList<StudyFinalDocumentDTO>(records);
			Collections.sort(temp, new GenericComparator<StudyFinalDocumentDTO>(
					orderByList, directionsList));
			return temp;
		} else if(sortMethod.equals("recommendation")){
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("recommendationScore");
			directionsList.add("desc");
			orderByList.add("relevanceScore");
			directionsList.add("desc");
			orderByList.add("someHarvardRepresentation");
			directionsList.add("asc");
			List<StudyFinalDocumentDTO> temp = new ArrayList<StudyFinalDocumentDTO>(records);
			Collections.sort(temp, new GenericComparator<StudyFinalDocumentDTO>(
					orderByList, directionsList));
			return temp;
		} else if(sortMethod.equals("mixed")){
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("mixedScore");
			directionsList.add("desc");
			orderByList.add("relevanceScore");
			directionsList.add("desc");
			orderByList.add("someHarvardRepresentation");
			directionsList.add("asc");
			List<StudyFinalDocumentDTO> temp = new ArrayList<StudyFinalDocumentDTO>(records);
			Collections.sort(temp, new GenericComparator<StudyFinalDocumentDTO>(
					orderByList, directionsList));
			return temp;
		} else
			return records;
	}
	
	
	private void addRecommendationScores(String user, 
			List<StudyFinalDocumentDTO> records) {
//		Cookie[] cookies = ((HttpServletRequest) FacesContext
//				.getCurrentInstance().getExternalContext().getRequest())
//				.getCookies();
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				 if (cookie.getName().equals("userId"))
//					userId = cookie.getValue();
//			}
//		}
			List<String> recordsIds = new ArrayList<String>();
			Float maxRelevance = new Float(1);
			if((records!=null) && (records.size() > 0)){
				maxRelevance = records.get(0).getRelevanceScore();
			}
			for (StudyFinalDocumentDTO record : records) {
				if(maxRelevance != 1){
					record.setRelevanceScore(record.getRelevanceScore()/maxRelevance);
				}
				recordsIds.add(record.getControlNumber());
			} 
			
		if((recommendedRecords != null) && (recommendedRecords.size() > 0)){
			
			List<String> recordsScores = new ArrayList<String>();
			
			String inputPath = FileStorage.storageRoot + File.separator + "import" + File.separator + user + "input.txt";
			File inputf = new File(inputPath);
			
			String outputPath = FileStorage.storageRoot + File.separator + "import" + File.separator + user + "output.txt";
			
	
			try {
				inputf.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(inputf);
				 
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			 
				int max = 20;
//				if(recordsIds.size() < 20)
					max = recordsIds.size();
				
				for (int i = 0; i < max; i++) {
					bw.write(recordsIds.get(i));
					bw.newLine();
				}
				bw.close();
	
				
				List<String>  command = new ArrayList<String>(Arrays.asList(new String[]{"/opt/cris/recommendation/SCRIPTS_operational/recommendation-collaborative/rerankDocsForUser-BestConfig.sh",  
					user, inputPath, outputPath, "&"}));
				executeCommand(command);
				BufferedReader br = new BufferedReader(new FileReader(outputPath));
	
				recordsIds = new ArrayList<String>();
			    String line = "";
			    while ((line = br.readLine())!= null) {
			    	String[] split = line.split(";");
			    	recordsIds.add(split[0].trim());
			    	recordsScores.add(split[1].trim());
			    }
			    
			    br.close();
			    
			    for (StudyFinalDocumentDTO studyFinalDocumentDTO : records) {
			    	for(int i =0; i<recordsIds.size(); i++){
			    		if(studyFinalDocumentDTO.getControlNumber().equals(recordsIds.get(i))){
			    			Float score = Float.parseFloat(recordsScores.get(i));
			    			studyFinalDocumentDTO.setRecommendationScore(score);
			    			break;
			    		} 
			    	}
			    	
			    }
			    
			} catch (IOException e) {
			} finally {
				try{
					File outputf = new File(outputPath);
					inputf.delete();
					outputf.delete();
				} catch (Throwable e){
					
				}
			}	
		}
		
	}
	
	
	
	public List<PublicationRecommendationDTO> getRecommendedRecords(){
		
		return recommendedRecords;
	}
	
	private void loadRecommendedRecords(){
		if (recommendedRecords == null){
			Cookie[] cookies = ((HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest())
					.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					 if (cookie.getName().equals("userId"))
						userId = cookie.getValue();
				}
			}
			if(userId != null){
				loadRecommendation(userId);
			}
		}
	}
	
	private void loadRecommendation(String user){
		List<String> recordsIds = new ArrayList<String>();
		List<String> recordsScores = new ArrayList<String>();
		recommendedRecords = new ArrayList<PublicationRecommendationDTO>();
		
		String path = FileStorage.storageRoot + File.separator + "import" + File.separator + user + "output.txt";
		// Use relative path for Unix systems
		try {

			List<String> command = new ArrayList<String>(Arrays.asList(new String[]{"/opt/cris/recommendation/SCRIPTS_operational/recommendation-collaborative/getRecommendationsForUser-BestConfig.sh",    
					user, ("" + 5), path, "&"}));
			executeCommand(command);
			BufferedReader br = new BufferedReader(new FileReader(path));
		    String line = "";
		    while ((line = br.readLine())!= null) {
		    	String[] split = line.split(";");
		    	recordsIds.add(split[0].trim());
		    	recordsScores.add(split[1].trim());
		    }
		    br.close();
		    
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getRecords(recordsIds);
			Float maxRelevance = new Float(1);
			if((recordsTemp!=null) && (recordsTemp.size() > 0)){
				maxRelevance = ((StudyFinalDocumentDTO)(recordsTemp.get(0).getDto())).getRelevanceScore();
			}
			for (Record record : recordsTemp) {
				if(! maxRelevance.equals(new Float(1))){
					((StudyFinalDocumentDTO)(record.getDto())).setRelevanceScore(new Float(((StudyFinalDocumentDTO)(record.getDto())).getRelevanceScore()/maxRelevance));
				}
				recommendedRecords.add(new PublicationRecommendationDTO((StudyFinalDocumentDTO)record.getDto(), false, false));
			}
			for (PublicationRecommendationDTO studyFinalDocumentDTO : recommendedRecords) {
				for(int i =0; i<recordsIds.size(); i++)
					if(studyFinalDocumentDTO.getPublication().getControlNumber().equals(recordsIds.get(i))){
						Float score = Float.parseFloat(recordsScores.get(i));
						studyFinalDocumentDTO.getPublication().setRecommendationScore(score);
						break;
					}
			}
			Collections.sort(recommendedRecords, new GenericComparator<PublicationRecommendationDTO>(
					"publication.recommendationScore", "desc"));
		} catch (Exception e) {
		} finally {
			try{
				File f = new File(path);
				f.delete();
			} catch (Throwable e){
				
			}
		}
	}
	
	public void explicitFeedbackYes(){
		PublicationRecommendationDTO publicationRecommendation = findPublicationRecommendationByControlNumber();
		publicationRecommendation.setRecommended(true);
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String positionString = facesCtx.getExternalContext()
				.getRequestParameterMap().get("position");
		int position = 0;
		try{
			if(positionString!=null)
				position = Integer.parseInt(positionString);
		} catch (Exception e){
			
		}
		
		FeedbackLogger.logRecommendationEvaluation(userId, publicationRecommendation.getPublication().getControlNumber(), "basic(recommendation list)", position, publicationRecommendation.getPublication().getRelevanceScore(), publicationRecommendation.getPublication().getRecommendationScore(), publicationRecommendation.getPublication().getMixedScore(), "explicit", "true positive");
	}
	
	private void executeCommand(List<String> commandAndArgs){
		Process p;
		try {
			ProcessBuilder pb = new ProcessBuilder(commandAndArgs).inheritIO();
			p = pb.start();
	        if (p != null) {
	            p.waitFor();
	            p.destroy();
	        }
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}

	
	public void explicitFeedbackNo(){
		PublicationRecommendationDTO publicationRecommendation = findPublicationRecommendationByControlNumber();
		publicationRecommendation.setNotRecommended(true);
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String positionString = facesCtx.getExternalContext()
				.getRequestParameterMap().get("position");
		int position = 0;
		try{
			if(positionString!=null)
				position = Integer.parseInt(positionString);
		} catch (Exception e){
			
		}
		
		FeedbackLogger.logRecommendationEvaluation(userId, publicationRecommendation.getPublication().getControlNumber(), "basic(recommendation list)", position, publicationRecommendation.getPublication().getRelevanceScore(), publicationRecommendation.getPublication().getRecommendationScore(), publicationRecommendation.getPublication().getMixedScore(), "explicit", "false positive");
	}
	
	private PublicationRecommendationDTO findPublicationRecommendationByControlNumber() {
		PublicationRecommendationDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PublicationRecommendationDTO dto : recommendedRecords) {
				if ((dto.getPublication().getControlNumber() != null)
						&& (dto.getPublication().getControlNumber().equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	
	
	/**
	 * @param records the records to set
	 */
	public void setRecords(List<StudyFinalDocumentDTO> records) {
		this.records = records;
	}

	public int getRecordSize() {
		if(records!=null)
			return records.size();
		else
			return 0;
	}
	
	/**
	 * @return the searchQueryError
	 */
	public String getSearchQueryError() {
		return searchQueryError;
	}
	/**
	 * @param searchQueryError the searchQueryError to set
	 */
	public void setSearchQueryError(String searchQueryError) {
		this.searchQueryError = searchQueryError;
	}
	
//	private static SpellChecker spellChecker = null;
	
	/**
	  */
	public void luceneQuerySearch() {
		records =null;
		searchQueryError=null;
		
		QueryParser queryParser = new QueryParser("FT_SRP", new CrisAnalyzer());
		Query query = null;
		try {
			query = queryParser.parse(luceneQuery);
		} catch (Exception e) {
			error("Lucene search query parsing error", e);
			searchQueryError = "Lucene search query parsing error";
		}
		if(query!=null){
			BooleanQuery type = new BooleanQuery();
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
			BooleanQuery bq = QueryUtils.makeBooleanQuery(query, type, QueryDTO.AND);
			bq = QueryUtils.makeBooleanQuery(bq, new TermQuery(new Term("UNSDISSERTATION", "true")), QueryDTO.AND);
			logQuery(query, "third");
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getDTOs(bq, new AllDocCollector(true));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
			addRecommendationScores(userId, records);
			logResults(records, query, "third");
		}	
	}	
	
	/**
	 */
	public void basicQuerySearch() {
		
		int counterSearchQuery = 0;
		List<QueryDTO> searchQueries = new ArrayList<QueryDTO>();
		String[] basicQueriesParts = (" " + basicQuery + " ").replace("\"\"", "\" \"").split("\"");
		int i = 1;
		for (String basicQueryPart : basicQueriesParts) {
			basicQueryPart = basicQueryPart.trim();
			if(! "".equals(basicQueryPart)){
				QueryDTO temp1 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), searchQueryFieldNameString[1], searchQueryTypeString[(i%2) + 1]);
				temp1.setValue(basicQueryPart);
				temp1.setBoost(1.5f);
				searchQueries.add(temp1);
				QueryDTO temp2 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), searchQueryFieldNameString[0], searchQueryTypeString[(i%2) + 1]);
				temp2.setValue(basicQueryPart);
				searchQueries.add(temp2);
				QueryDTO temp3 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), searchQueryFieldNameString[5], searchQueryTypeString[(i%2) + 1]);
				temp3.setValue(basicQueryPart);
				temp3.setBoost(2f);
				searchQueries.add(temp3);
				QueryDTO temp4 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), "PY", searchQueryTypeString[(i%2) + 1]);
				temp4.setValue(basicQueryPart);
				temp4.setBoost(2.5f);
				searchQueries.add(temp4);
				QueryDTO temp5 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), "INS", searchQueryTypeString[(i%2) + 1]);
				temp5.setValue(basicQueryPart);
				temp5.setBoost(2.5f);
				searchQueries.add(temp5);
				QueryDTO temp6 = new QueryDTO(counterSearchQuery++,searchQueryOperator.get(1).getValue().toString(), "DT", searchQueryTypeString[(i%2) + 1]);
				temp6.setValue(basicQueryPart);
				temp6.setBoost(2.5f);
				searchQueries.add(temp6);
			}
			i++;
		}
		searchDissertations(searchQueries);
	}	
	
	/**
	 */
	public void advancedQuerySearch() {
		searchDissertations(searchQueries);
	}	

	public void populateAll(){
		
		if(populateAll){
			
			getTree();
		}
	}
	
	/**
	 * @return the root
	 */
	public List<TreeNodeDTO<Object>> getRoot() {
		
		if(root==null)
		{
			populateAll=true;
			populateAll();
		}
		if(treeState!=null)
			treeState.getSelection().clear();
		
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(List<TreeNodeDTO<Object>> root) {
		this.root = root;
	}
	
	/**
	 * @return the allInstitutionsAndDegrees
	 */
	public List<TreeNodeDTO<Object>> getAllInstitutionsAndDegrees() {
		return allInstitutionsAndDegrees;
	}
	/**
	 * @param allInstitutionsAndDegrees the allInstitutionsAndDegrees to set
	 */
	public void setAllInstitutionsAndDegrees(
			List<TreeNodeDTO<Object>> allInstitutionsAndDegrees) {
		this.allInstitutionsAndDegrees = allInstitutionsAndDegrees;
	}
	/**
	 * @return the populateAll
	 */
	public boolean isPopulateAll() {
		return populateAll;
	}
	
	public void getTree() {
		debug("getTree");
		try {
			  root = new ArrayList <TreeNodeDTO<Object>>();
		      if(allInstitutions == null){
		      	FacesContext facesCtx = FacesContext.getCurrentInstance();
				ExternalContext extCtx = facesCtx.getExternalContext();
				InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
						"institutionManagedBean");
				if (mb == null) {
					mb = new InstitutionManagedBean();
					extCtx.getSessionMap().put("institutionManagedBean", mb);
				}
				mb.setIncludeOrganizationUnits(false);
				mb.populateList();
				allInstitutions = mb.getInstitutions();
				Collections.sort(allInstitutions, new GenericComparator<InstitutionDTO>("controlNumber", "asc"));
		      }
		      
		      if (allInstitutionsAndDegrees == null)
		      {
		    	  allInstitutionsAndDegrees = new ArrayList<TreeNodeDTO<Object>>();
		 	  }
		      
			  for(InstitutionDTO ins:allInstitutions){
					if((ins.getSuperInstitution() != null) && (ins.getSuperInstitution().getControlNumber()!=null) && (ins.getSuperInstitution().getControlNumber().equalsIgnoreCase("(BISIS)5920"))){
						if((!(ins.getControlNumber().equals("(BISIS)5963"))) && (!(ins.getControlNumber().equals("(BISIS)69119"))) && (!(ins.getControlNumber().equals("(BISIS)69121")))){
							TreeNodeDTO<Object> node = new TreeNodeDTO<Object>(ins);
							node.setParent(null);
							allInstitutionsAndDegrees.add(node);
							addInstitutionDegrees(node, ins);
							root.add(node);
						}
					}
		      }
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	
	protected void addInstitutionDegrees(TreeNodeDTO<Object> parentNode, InstitutionDTO parentInstitution) {
		parentInstitution.getAcro(); //object loading 
		for (Classification ic : parentInstitution.getRecord().getRecordClasses()) {
			if(ic.getCfClassSchemeId().equals("nameOfDegree")){
				ClassDTO classNode = new ClassDTO();
				classNode.setSchemeId(ic.getCfClassSchemeId());
				classNode.setClassId(ic.getCfClassId());
				MultilingualContentDTO term = cslDB.getClassTerm(conn, ic.getCfClassSchemeId(), ic.getCfClassId());
				if(term != null) {
					classNode.setTerm(term);	
					List<MultilingualContentDTO> terms = cslDB.getClassTermTranslations(conn, ic.getCfClassSchemeId(), ic.getCfClassId());
					classNode.setTermTranslations(terms);
					TreeNodeDTO<Object> node = new TreeNodeDTO<Object>(classNode);
					node.setParent(parentNode);
					allInstitutionsAndDegrees.add(node);
					parentNode.addChild(node);
				} 
			}
		}
	}
	
		
	public void changeSelectionChilds(TreeNodeDTO<Object> inputTree, boolean state, String s, int broj){
		if(state==true)
		{
			inputTree.setCheckbox_state(true);
			inputTree.setTree_state("full");
		}
		else
		{
			inputTree.setCheckbox_state(false);
			inputTree.setTree_state("empty");
		}
		
		if (inputTree.isLeaf()!=true)
		{
			int i =1;
			List<TreeNodeDTO <Object>> children = inputTree.getChildren();
			for(TreeNodeDTO <Object> child : children)
			{
				changeSelectionChilds(child, state, s + "." + broj, i);
				i++;
			}
		}
	}
	
//	public void printChilds(TreeNodeDTO<Object> inputTree, String s, int broj){
//		if (inputTree.isLeaf()!=true)
//		{
//			int i =1;
//			List<TreeNodeDTO <Object>> children = inputTree.getChildren();
//			for(TreeNodeDTO <Object> child : children)
//			{
//				printChilds(child, s + "." + broj, i);
//				i++;
//			}
//		}
//	}
	
	public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent){
		
        List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
        Object currentSelectionKey = selection.get(0);
        treeState = (UITree) selectionChangeEvent.getSource();
 
        Object storedKey = treeState.getRowKey();
        treeState.setRowKey(currentSelectionKey);
        
        if(((TreeNodeDTO<Object>)treeState.getRowData()).getElement()!=null){
			if(((TreeNodeDTO<Object>)treeState.getRowData()).getElement() instanceof InstitutionDTO)
				changeSelectionOnTree(((InstitutionDTO)((TreeNodeDTO<Object>)treeState.getRowData()).getElement()).getControlNumber());
			else if (((TreeNodeDTO<Object>)treeState.getRowData()).getElement() instanceof ClassDTO)
				changeSelectionOnTree(((ClassDTO)((TreeNodeDTO<Object>)treeState.getRowData()).getElement()).getClassId());
        }
        treeState.setRowKey(storedKey);	
	}
	
	
	public void changeSelectionOnTree(String controlNumber) {
		TreeNodeDTO<Object> selectedNode = null;
 		for (TreeNodeDTO<Object> obj : allInstitutionsAndDegrees) {
 			if(obj.getElement() instanceof InstitutionDTO){
	 			InstitutionDTO dto = (InstitutionDTO)obj.getElement();
 				if ((dto.getControlNumber() != null)&& (dto.getControlNumber().equalsIgnoreCase(controlNumber))) {
	 				selectedNode = obj;
	 				break;
	 			}
 			}
 			if(obj.getElement() instanceof ClassDTO){
	 			ClassDTO dto = (ClassDTO)obj.getElement();
 				if ((dto.getClassId() != null)&& (dto.getClassId().equalsIgnoreCase(controlNumber))) {
	 				selectedNode = obj;
	 				break;
	 			}
 			}
 		}
 		if (selectedNode == null){
 				return;
 		}
 		
 		boolean oldState = false;
 		oldState = selectedNode.isCheckbox_state();
 		boolean newState = !oldState;
 				
 		if(newState==false)
 		{
 			selectedNode.setTree_state("empty");
 			selectedNode.setCheckbox_state(false);
 			if(selectedNode.getType().equals("InstitutionDTO"))
 				selectedNode.setExpanded(false);
 		}
 		else
 		{
 			selectedNode.setTree_state("full");
 			selectedNode.setCheckbox_state(true);
 			if(selectedNode.getType().equals("InstitutionDTO"))
 				selectedNode.setExpanded(true);
 		}

 		TreeNodeDTO<Object> parentInstitutionTree = (TreeNodeDTO<Object>) selectedNode.getParent();
 		
 		if(parentInstitutionTree != null)
 		{
 			TreeNodeDTO<Object> parentInstitution = parentInstitutionTree;
 			
 			if(parentInstitution != null){
	 			if(newState==false)
	 			{
	 				if (parentInstitution.isCheckbox_state()){
	 					parentInstitution.setTree_state("partial");
	 				}
	 			}
	 			else
	 			{
	 				boolean fullAll = true;
	 				
	 				List<TreeNodeDTO <Object>> children = parentInstitution.getChildren();
	 				for(TreeNodeDTO <Object> child : children)
	 				{
	 					if(child.isCheckbox_state()==false)
	 		            {
	 		            	fullAll = false;
	 		            	break;
	 		            }
	 				}
	 				
	 		        if(fullAll==true)
	 		        {	
	 		        	parentInstitution.setTree_state("full");
	 		        	parentInstitution.setCheckbox_state(true);
	 		        }
	 		        else {
	 		        	parentInstitution.setTree_state("partial");
	 		        	parentInstitution.setCheckbox_state(true);
	 		        }
	 			}
 			}
 		}
 		if(selectedNode.getTree_state().equals("empty"))
 		{
 			int i=1;
 			List<TreeNodeDTO <Object>> children = selectedNode.getChildren();
			for(TreeNodeDTO <Object> child : children)
			{
				changeSelectionChilds(child, newState,"",i);
		       	i++;
			}	
 		}
 		
 }

	public void searchDissertations(List<QueryDTO> searchQueries){

		Query query = null;
		boolean sortByRelevance = true;
		for (int i = 0; i < searchQueries.size(); i++)
		{
			Query newQuery = null;
			Query newQueryEng = null;
			QueryDTO queryDTO = searchQueries.get(i);
			if((queryDTO.getValue() != null) && (! ("".equals(queryDTO.getValue().trim())))){
				newQuery = QueryUtils.makeQuery(queryDTO);
				if((queryDTO.getFieldName().endsWith("_SRP")) && (! QueryDTO.SIMILAR.equals(queryDTO.getType()))){
					queryDTO.setFieldName(queryDTO.getFieldName().replace("_SRP", "_ENG"));
					String operator = queryDTO.getOperator();
					float boost = queryDTO.getBoost();
					if("sr".equals(language))
						queryDTO.setBoost(boost/1.5f);
					else 
						queryDTO.setBoost(boost*1.5f);
					queryDTO.setOperator(QueryDTO.OR);
					if((queryDTO.getValue() != null) && (! ("".equals(queryDTO.getValue().trim())))){
						newQueryEng = QueryUtils.makeQuery(queryDTO);
						if(newQuery != null){
							newQuery = QueryUtils.makeBooleanQuery(newQuery, newQueryEng, queryDTO.getOperator());
						} else {
							newQuery = newQueryEng;
						}
					}
					queryDTO.setFieldName(queryDTO.getFieldName().replace("_ENG", "_SRP"));
					queryDTO.setOperator(operator);
					queryDTO.setBoost(boost);
				}
				if(query != null){
					query = QueryUtils.makeBooleanQuery(query, newQuery, queryDTO.getOperator());
				} else {
					query = newQuery;
				}
			}
		}
		
		if(query == null)
			sortByRelevance = false;
		
		if(advanced){
			if((defended != null) && (! defended.equals(getSearchDefendedNameString()[0]))){
				TermQuery def = new TermQuery(new Term("DEFENDED", defended));
				if(query!=null){
					query = QueryUtils.makeBooleanQuery(query, def, QueryDTO.AND);
				} else {
					query = def;
				} 
			}
		}
		
		if(advanced){
			String startYear = (fromDate.equals(searchQueryFieldNameString[0]))?"1950":fromDate;
			String endYear = (toDate.equals(searchQueryFieldNameString[0]))?(""+Calendar.getInstance().get(Calendar.YEAR)):toDate;
			if((!("1950".equals(startYear))) || (!((""+Calendar.getInstance().get(Calendar.YEAR)).equals(endYear)))){
				RangeQuery years = new RangeQuery(new Term("PY", startYear), new Term("PY", endYear), true);
				if(query!=null){
					query = QueryUtils.makeBooleanQuery(query, years, QueryDTO.AND);
				} else 
					query = years;
			}
		}
		
		if(advanced){
			Query treeQuery = treeQuery();
			if(treeQuery != null){
				if(query!=null){
					query = QueryUtils.makeBooleanQuery(query, treeQuery, QueryDTO.AND);
				} else 
					query = treeQuery;
			}
		}
		
		if(query!=null){
			BooleanQuery type = new BooleanQuery();
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
			query = QueryUtils.makeBooleanQuery(query, type, QueryDTO.AND);
			query = QueryUtils.makeBooleanQuery(query, new TermQuery(new Term("UNSDISSERTATION", "true")), QueryDTO.AND);
			logQuery(query, (advanced)?"first":"basic");
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getDTOs(query, new AllDocCollector(true));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
			addRecommendationScores(userId, records);
			logResults(records, query, (advanced)?"first":"basic");
			
//			if(sortByRelevance == false){
//				List<String> orderByList = new ArrayList<String>();
//				orderByList.add("publicationYear");
//				orderByList.add("harvardRepresentation");
//				List<String> directionsList = new ArrayList<String>();
//				directionsList.add("desc");
//				directionsList.add("asc");
//				Collections.sort(records, new GenericComparator<StudyFinalDocumentDTO>(orderByList, directionsList));
//			}
		}
	}
		
	
	protected void logResults(List<StudyFinalDocumentDTO> records, Query query, String searchingMode) {
		HttpServletRequest req = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
		HttpServletResponse res = ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse());
		Log logResults = LogFactory.getLog("rs.results");
		
		String ipAddress = null;
		try{
			ipAddress  = req.getHeader("X-FORWARDED-FOR");  
			if(ipAddress == null)  
	        {  
				ipAddress = req.getRemoteAddr();  
	        }  else {
	        	ipAddress += "(proxy = " + req.getRemoteAddr() + ")";
	        }
		} catch (Exception ex){
		}
		
		ClientLocation location = GeolocationManager.getLocation(req.getRemoteAddr());
	
		String sessionId = "not defined";
		String userId = null;
		for (Cookie cookie:req.getCookies())
			if(cookie.getName().equals("JSESSIONID"))
				sessionId = cookie.getValue();
			else if (cookie.getName().equals("userId"))
				userId = cookie.getValue();
		
		if(userId == null){
			userId = "" + System.currentTimeMillis() + "" + (int)(Math.random() * 100);
			Cookie userCookie = new Cookie("userId", userId);
			userCookie.setMaxAge(60*60*24*365*10); //Store cookie for 10 years
			userCookie.setPath("/");
			res.addCookie(userCookie);
		}
		
		String userAgent = req.getHeader("User-Agent");
		
		Date date = new Date();
		
		for(int i = 0; i < records.size(); i++){
			StudyFinalDocumentDTO sfd = records.get(i);
			String stringRepresentation = " | record: notLoaded";
			if(sfd != null)
				stringRepresentation = " | record: " + sfd.getSomeHarvardRepresentation();
			
			String url = " notLoaded";
			if(sfd != null)
				url = sfd.getFileURL();
			if((url == null) || ("".equals(url.trim())))
				url = "no file";
			
			String license = "not defined";
			if(sfd != null)
				if(sfd.getFileLicense() != null)
					license = sfd.getFileLicense();
			logResults.info("Date and time: " + date + "| miliseconds: " + date.getTime() + "| query: " + query.toString() + "" + "| searching mode: " + searchingMode + 
				"| language: " + language + "| session id: " + sessionId + "| userId: " + userId + "| ip address: " + ipAddress + "| location: " + location.toString() + "" +
				"| user agent (device): " + userAgent + "| order number: " + (i+1) + "| record id: " + sfd.getControlNumber() + "| file url: " + url + "| license: " + license + stringRepresentation + 
				"| sort method: " + sortMethod + "| relevance score: " + sfd.getRelevanceScore() + "| recommendation score: " + sfd.getRecommendationScore() + "| mixed score: " + sfd.getMixedScore());
		}		
	}
	protected void logQuery(Query query, String searchingMode) {
		HttpServletRequest req = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
		HttpServletResponse res = ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse());
		Log logQuery = LogFactory.getLog("rs.query");
		
		String ipAddress = null;
		try{
			ipAddress  = req.getHeader("X-FORWARDED-FOR");  
			if(ipAddress == null)  
	        {  
				ipAddress = req.getRemoteAddr();  
	        }  else {
	        	ipAddress += "(proxy = " + req.getRemoteAddr() + ")";
	        }
		} catch (Exception ex){
		}
		
		ClientLocation location = GeolocationManager.getLocation(req.getRemoteAddr());
	
		
		String sessionId = "not defined";
		String userId = null;
		for (Cookie cookie:req.getCookies())
			if(cookie.getName().equals("JSESSIONID"))
				sessionId = cookie.getValue();
			else if (cookie.getName().equals("userId"))
				userId = cookie.getValue();
		
		if(userId == null){
			userId = "" + System.currentTimeMillis() + "" + (int)(Math.random() * 100);
			Cookie userCookie = new Cookie("userId", userId);
			userCookie.setMaxAge(60*60*24*365*10); //Store cookie for 10 years
			userCookie.setPath("/");
			res.addCookie(userCookie);
		}
		
		String userAgent = req.getHeader("User-Agent");
		
		Date date = new Date();
		
		logQuery.info("Date and time: " + date + "| miliseconds: " + date.getTime() + "| query: " + query.toString() + "" + "| searching mode: " + searchingMode + 
				"| language: " + language + "| session id: " + sessionId + "| userId: " + userId + "| ip address: " + ipAddress + "| location: " + location.toString() + "" +
				"| user agent (device): " + userAgent);
		
	}
	public Query treeQuery()
	{
		Query retVal = null;
		for (TreeNodeDTO<Object> dto : root){
			if (dto.isCheckbox_state()) {
				InstitutionDTO institution = (InstitutionDTO) dto.getElement();
				Query tempInsQuery = new TermQuery(new Term("INCN", institution.getControlNumber()));
				Query tempDegreesQuery = null;
				for (TreeNodeDTO<Object> degreeObject : dto.getChildren()) {
					if (degreeObject.isCheckbox_state()) {
						String degree = ((ClassDTO)degreeObject.getElement()).getTerm().getContent();
						QueryDTO queryDTO = new QueryDTO(0, QueryDTO.OR, "DT", QueryDTO.EXACT_PHRASE);
		
						queryDTO.setValue(degree);
						if(tempDegreesQuery == null){
							tempDegreesQuery = QueryUtils.makeQuery(queryDTO);
						} else {
							tempDegreesQuery = QueryUtils.makeBooleanQuery(tempDegreesQuery, QueryUtils.makeQuery(queryDTO), QueryDTO.OR);
						}
					}
				}
				if(tempDegreesQuery != null)
					tempInsQuery = QueryUtils.makeBooleanQuery(tempInsQuery, tempDegreesQuery, QueryDTO.AND);
				if(retVal == null){
					retVal = tempInsQuery;
				} else {
					retVal = QueryUtils.makeBooleanQuery(retVal, tempInsQuery, QueryDTO.OR);
				}
			}
		}
		return retVal;
	}
	
	public boolean isSearchResultShow()
	{
		return (searchQueryError!=null)||(records!=null);
	}
	
	public boolean getSearchResultShow()
	{
		return (searchQueryError!=null)||(records!=null);
	}
	
	public void changeTab(javax.faces.event.FacesEvent event){
		records = null;
		searchQueryError = null;
	}
	
	
	protected UITree treeState;
	
	/**
	 * Switches interface to English language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String englishLanguage() {
		setLocale("en");
		init = false;
		recommendedRecords = null;
		loadRecommendedRecords();
//		allInstitutions = null;
		return "searchDissertationsPage";
	}

	/**
	 * Switches interface to Serbian language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String serbianLanguage() {
		setLocale("sr");
		init = false;
		recommendedRecords = null;
		loadRecommendedRecords();
//		allInstitutions = null;
		return "searchDissertationsPage";
	}

	protected String language = "sr";

	/**
	 * @return the current language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLocale(String language) {
		Calendar date = new GregorianCalendar();
		date.set(GregorianCalendar.YEAR, new GregorianCalendar().get(GregorianCalendar.YEAR)+1);
		((HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse()).addHeader("Set-Cookie",
				"languageSearchDissertations=" + language + ";HttpOnly;expires=" + date.getTime());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(language));
		this.language = language;
	}

} 
