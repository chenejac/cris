package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.SearchDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.mediator.MediatorService;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean, IPickJournalManagedBean, IPickConferenceManagedBean{

	protected List<SearchDTO> searchQuerys = null;
	@SuppressWarnings("rawtypes")
	protected List<SelectItem> searchQueryOperator = new ArrayList(Arrays.asList(new SelectItem("AND"), new SelectItem("OR"), new SelectItem("AND NOT")));
	protected int counterSearchQuery = 0;
	
	private String [] searchQueryTypeString = new String [] {"default", "choice1", "choice2", "choice3", "choice4", "choice5", "choice6", "choice7", "choice8","choice9"};
	protected List<SelectItem> dateRange = null;
	
	protected boolean fuzzy = false; //for defining fuzzy query
	
	protected String fromDate = "";
	protected String toDate = "";
			
	protected String publicationType = "";
	
	protected List<AuthorDTO> authorSearched = null;
	protected String firstLastName="";
	
	
	protected String representationStyle = "htmlRepresentation";
	protected String sortMethod = "relevance";
	
	// Search possible Types in correspondence to positions in searchQueryTypeString 
	// All fields;	Article Title, Abstract, Keywords;	Authors;   First Author;	Article Title;	Abstract;	Keywords;	Conference;		Journal
	
	//	Search possible publication Types in correspondence to positions in searchQueryTypeString
	//	All;	Conference paper;	Journal paper;	Monograph;	Thesis;
	
	public boolean populateAll = true;
	protected List<TreeNodeDTO<InstitutionDTO>> root = null;
	protected List<InstitutionDTO> allInstitutions = null;
	protected List<OrganizationUnitDTO> allOrganizations = null;
	protected List<TreeNodeDTO<InstitutionDTO>> allInstitutionsAndOrganizations = null;
	
	// Advance query
	protected String advanceQuery = "";
	List<RecordDTO> records =null;
	protected String searchQueryError = null;
	
	protected boolean expandAll=true;
	private MediatorService mediatorService = null;
	
	/**
	 * Constructor
	 */
	public SearchManagedBean() {
		super();
		Cookie[] cookies = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("languageSearch")) {
					setLocale(cookie.getValue());
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
		if(searchQuerys==null)
			searchQuerys = new ArrayList<SearchDTO>();
		searchQuerys.add(new SearchDTO(counterSearchQuery,searchQueryOperator.get(0).toString(), searchQueryTypeString[0]));
		counterSearchQuery = counterSearchQuery+1;
		
		if (dateRange==null)
		{	
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			dateRange = new ArrayList() ;
			/*for (int i = 1960,j=0; i<=currentYear;i++,j++)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}*/
			
			// idemo od tekuce godine do 1960
			for (int i = currentYear; i>=1960;i--)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}
		}
		fuzzy = false;
		fromDate = "";
		toDate = "";
		firstLastName="";
		authorSearched = new ArrayList<AuthorDTO>();
		populateAll = true;
		init=true;
		publicationType = "";
		advanceQuery = "";
		searchQueryError = null;
		records =null;
		root= null;
		allOrganizations=null;
		allInstitutions=null;
		allInstitutionsAndOrganizations=null;
		populateAll = true;
		populateAll();
		mediatorService = new MediatorService();
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		
		//mozda treba obrisati
		setAuthorManageBeanToPick();
		setJournalManageBeanToPick();
		setConferenceManageBeanToPick();
	}
	
	/**
	 * @return the searchQueries
	 */
	public List<SearchDTO> getSearchQuerys() {

		return searchQuerys;
	}
	/**
	 * @param searchQueries the searchQueries to set
	 */
	public void setSearchQuerys(List<SearchDTO> searchQuerys) {
		this.searchQuerys = searchQuerys;
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
	 * @return the searchQueryTypeString
	 */
	public String[] getSearchQueryTypeString() {
		return searchQueryTypeString;
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
	 * Removes query form list
	 */
	public void removeQuery(){
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		int removedQuery = Integer.parseInt((facesCtx.getExternalContext().getRequestParameterMap().get("removedQuery")).trim());
		
		boolean found = false;
		for (int i = 0; i < searchQuerys.size(); i++)
		{
			if(removedQuery==searchQuerys.get(i).getId() && found==false)
			{
				searchQuerys.remove(i);
				counterSearchQuery--;
				found=true;
			}
			if(found==true && i!=searchQuerys.size())
			{
				searchQuerys.get(i).setId(i);
			}
		}
	}
	
	/**
	 * Adds query form list
	 */
	public void addQuery(){
		searchQuerys.add(new SearchDTO(counterSearchQuery,searchQueryOperator.get(0).toString(), searchQueryTypeString[0]));
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
		{	//if something is already selected in the UIdata
			
			init= false;
		}
	}
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "searchPage";
		return retVal;
	}
	
	
	public String searchPageEnter() {
		resetForm();
		
		setAuthorManageBeanToPick();
		setJournalManageBeanToPick();
		setConferenceManageBeanToPick();
		
		root= null;
		allOrganizations=null;
		allInstitutions=null;
		allInstitutionsAndOrganizations=null;
		populateAll = true;
		populateAll();
		
		getUserManagedBean().setJobAd(false);
		
		returnPage = "indexPage";
		return "searchPage";
	}
	
	public void searchPageEnter(PhaseEvent event){
		String departmentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("department");
		String institutionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("institution");
		if((init == false) || (departmentId != null) || (institutionId != null)){
			resetForm();
			
			setAuthorManageBeanToPick();
			
			root = null;
			allInstitutions=null;
			allOrganizations = null;
			allInstitutionsAndOrganizations=null;
			
			populateAll();
			if (departmentId != null){
				changeSelectionOnTree(departmentId);
			} else if (institutionId != null){
				changeSelectionOnTree(institutionId);
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
		searchQuerys = new ArrayList<SearchDTO>();
		searchQuerys.add(new SearchDTO(counterSearchQuery++,searchQueryOperator.get(0).toString(), searchQueryTypeString[0]));
		fuzzy = false;
		fromDate = "";
		toDate = "";
		firstLastName="";
		representationStyle = "htmlRepresentation";
		sortMethod = "relevance";
		populateAll = true;
		authorSearched = new ArrayList<AuthorDTO>();
		publicationType = "";
		advanceQuery = "";
		searchQueryError = null;
		records =null;
//		populateAll(); Ovo nekako staviti u enterCRUDPage
		mediatorService = new MediatorService();
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		cancelAllPicking();
		//cancel all tree selection
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickAuthorManagedBean#setAuthor(rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO)
	 */
	@Override
	public void setAuthor(AuthorDTO author) {
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
	public String getFirstLastName() {
		return firstLastName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
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
		this.representationStyle = representationStyle;
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
		mb.switchToPickMode();
	}
	
	//////OVO CE SE BRISATI
	/**
	 * Returns result from AutorManageBean for AutorPanel search
	 */
	public void setChooseAuthorFirstLastName()
	{
		records = null;
		searchQueryError=null;
		
		int sizeAuthorSelectedHistory = authorSearched.size();
		for(int i =0; i<sizeAuthorSelectedHistory; i++)
			authorSearched.remove(0);
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
				"authorManagedBean");
		if (mb == null) {
			mb = new AuthorManagedBean();
			extCtx.getSessionMap().put("authorManagedBean", mb);
		}
		AuthorDTO authorSelectedTemp = mb.getSelectedAuthor();
		mb.switchToTableNoneMode();
		mb.switchToEditNoneMode();
		
		firstLastName = authorSelectedTemp.getName().getLastname() +" "+ authorSelectedTemp.getName().getFirstname();
		authorSearched.add(authorSelectedTemp);
	}
	
	/*
	public void selectAuthorFromAuthorSearched()
	{
		records = null;
		searchQueryError=null;
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext().getRequestParameterMap().get("selectedQueryAuthorSearched").trim();

		AuthorDTO authorSelectedTemp = null;
		int i = 0;
		
		for(AuthorDTO iAuthor : authorSearched)
		{
			if(iAuthor.getControlNumber().equalsIgnoreCase(controlNumber))
			{
				authorSelectedTemp = authorSearched.remove(i);
				break;
			}
			i++;
		}
		authorSearched.add(0,authorSelectedTemp);
		//set display information
	}
	public void removeAuthorFromAuthorSearched()
	{
		records = null;
		searchQueryError=null;
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext().getRequestParameterMap().get("removedQueryAuthorSearched").trim();

		int i = 0;
		
		for(AuthorDTO iAuthor : authorSearched)
		{
			if(iAuthor.getControlNumber().equalsIgnoreCase(controlNumber))
			{
				authorSearched.remove(i);
				break;
			}
			i++;
		}
	}
	*/
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
		
		StringBuffer bufferQuery = new StringBuffer();
		if(firstLastName!=null && !firstLastName.trim().equalsIgnoreCase(""))
		{	//author All
//			System.out.println("author Search");
			bufferQuery.append("dc.creator= " + firstLastName+
					" AND " + "cris.type" + "=" + Types.AUTHOR); 		
		}
		else
		{
			return;
		}

		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		
		
		List<RecordDTO> tempRecords =null;
		tempRecords = mediatorService.getRecords(bufferQuery.toString());
		if(tempRecords==null)
		{
			return;
		}
		
		for(RecordDTO iAuthorRecord : tempRecords)
		{
			authorSearched.add((AuthorDTO)iAuthorRecord);
		}
		
		System.out.println(authorSearched.size());
		
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
		StringBuffer bufferQuery = new StringBuffer();
		records =null;
		searchQueryError = null;
		
		if(queryType.equalsIgnoreCase(searchQueryTypeString[0]))
		{	//author All
//			System.out.println("author All");
			bufferQuery.append("( dc.creator= \""+ selectedAuthor.getControlNumber()+ "\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+ " ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.JOURNAL+
					" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
			bufferQuery.append(" )");			 
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[1]))
		{	//papers from author Journal
//			System.out.println("papers from author Journal");
			/*bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND ( " + "cris.type" + "=" + Types.PAPER_JOURNAL+
						" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL +
						" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL +" )");*/
			
			bufferQuery.append(" dc.creator= \""+ selectedAuthor.getControlNumber()+ "\"" +
					" AND ( " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL +
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL +" )");
						
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[2]))
		{	//papers from author Proceedings
//			System.out.println("papers from author Proceedings");
			/*bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND ( " + "cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
						" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
						" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
						" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
						" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
						" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+" )");*/
			bufferQuery.append(" dc.creator= \""+ selectedAuthor.getControlNumber()+ "\"" +
					" AND ( " + "cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+" )");
			
//			System.out.println(bufferQuery.toString());
		}	
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[3]))
		{	//papers from author Monograph
//			System.out.println("papers from author Monograph");
			/*bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND ( " +  "cris.type" + "=" + Types.MONOGRAPH+
						" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" )");*/
			
			bufferQuery.append(" dc.creator= \""+ selectedAuthor.getControlNumber()+ "\"" +
					" AND ( " +  "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" )");
			
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[4]))
		{	//papers from author Theses and Dissertations
//			System.out.println("papers from author Theses and Dissertations");
			/*bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND ( " +  "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
						" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
						" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
						" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
						" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
						" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
						" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT+" )");*/
			
			bufferQuery.append(" dc.creator= \""+ selectedAuthor.getControlNumber()+ "\"" +
					" AND ( " +  "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT+" )");
			
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[5]))
		{	//papers from author Institution
//			System.out.println("papers from author Institution");
			bufferQuery.append("( dc.contributor adj \""+selectedAuthor.getInstitution().getSomeName() + "\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+ " ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.JOURNAL+
					" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
			bufferQuery.append(" )");
			
			
			
			
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[6]))
		{	//papers from author Organisation
//			System.out.println("papers from author Organisation");
			//trebalo bi oavako kako je zakomentarisano ali podacii u bazi podataka nisu adekvatni
//			bufferQuery.append("( dc.contributor adj \""+authorSelectedHistory.get(0).getInstitution().getSomeName() + "\""+
//						" AND " + "dc.contributor adj \""+authorSelectedHistory.get(0).getOrganizationUnit().getSomeName()+ "\"");
			/*bufferQuery.append("( dc.contributor adj \""+selectedAuthor.getOrganizationUnit().getSomeName()+ "\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+ " ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.JOURNAL+
					" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
			bufferQuery.append(" )");*/
			
			bufferQuery.append("( dc.contributor adj \""+selectedAuthor.getOrganizationUnit().getSomeName()+ "\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+ " ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.JOURNAL+
					" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" ");
			bufferQuery.append(" OR " + "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
			bufferQuery.append(" )");			
			
			
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[7]))
		{	//author Coworkers Institucija
//			System.out.println("author Coworkers Institucija");
			bufferQuery.append("( cris.ins= \""+selectedAuthor.getInstitution().getControlNumber()+"\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.AUTHOR);
			bufferQuery.append(" )");
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[8]))
		{	//author Coworkers Departman
//			System.out.println("author Coworkers Departman");
			bufferQuery.append("( cris.ins= \""+selectedAuthor.getOrganizationUnit().getControlNumber()+"\"");
			bufferQuery.append(" ) AND ( ");
			bufferQuery.append("cris.type" + "=" + Types.AUTHOR);
			bufferQuery.append(" )");
//			System.out.println(bufferQuery.toString());
		}
		else
		{
			
		}
		
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		records = mediatorService.getRecords(bufferQuery.toString());
		if(mediatorService.getErrMessage()!=null)
			searchQueryError = mediatorService.getErrMessage();
	}
	
	/**
	 * Returns result from AutorManageBean for PUblicationPanel search
	 */
	public void switchToPick(ValueChangeEvent event)
	{
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		ExternalContext extCtx = facesCtx.getExternalContext();	
//		
//		String inputQueryRow = extCtx.getRequestParameterMap().get("inputQueryRow");
//		System.out.println("nova vrednost inputQueryRow:"+ inputQueryRow);
		
		String inputQueryType = (String)event.getNewValue().toString();
		
		if(inputQueryType.equalsIgnoreCase(searchQueryTypeString[2]) || inputQueryType.equalsIgnoreCase(searchQueryTypeString[3]))
		{	//author firstAuthor
			setAuthorManageBeanToPick();
		}
		else if(inputQueryType.equalsIgnoreCase(searchQueryTypeString[7]))
		{	//conference
			setConferenceManageBeanToPick();
		}
		else if(inputQueryType.equalsIgnoreCase(searchQueryTypeString[8]))
		{	//jurnal
			setJournalManageBeanToPick();
			
		}	
		else
		{
			
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#setJournal(rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO)
	 */
	@Override
	public void setJournal(JournalDTO journal) {
		// TODO Auto-generated method stub
	}
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#cancelPickingJournal()
	 */
	@Override
	public void cancelPickingJournal() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Sets all necessary things for AuthorManageBean in pick mode
	 */
	public void setJournalManageBeanToPick() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		JournalManagedBean mb = (JournalManagedBean) extCtx.getSessionMap().get(
				"journalManagedBean");
		if (mb == null) {
			mb = new JournalManagedBean();
			extCtx.getSessionMap().put("journalManagedBean", mb);
		}

		mb.setIPickJournalManagedBean(this);
		mb.setSelectedJournal(null);
		mb.setCustomPick(true);
		mb.switchToPickMode();
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#setConference(rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO)
	 */
	@Override
	public void setConference(ConferenceDTO conference) {
		// TODO Auto-generated method stub
	}
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickConferenceManagedBean#cancelPickingConference()
	 */
	@Override
	public void cancelPickingConference() {
		// TODO Auto-generated method stub
	}
	/**
	 * Sets all necessary things for AuthorManageBean in pick mode
	 */
	public void setConferenceManageBeanToPick() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		ConferenceManagedBean mb = (ConferenceManagedBean) extCtx.getSessionMap().get(
				"conferenceManagedBean");
		if (mb == null) {
			mb = new ConferenceManagedBean();
			extCtx.getSessionMap().put("conferenceManagedBean", mb);
		}

		mb.setIPickConferenceManagedBean(this);
		mb.setSelectedConference(null);
		mb.setCustomPick(true);
		mb.switchToPickMode();
	}
	public void cancelAllPicking()
	{

	}
	/**
	 * @return the advanceQuery
	 */
	public String getAdvanceQuery() {
		return advanceQuery;
	}
	/**
	 * @param advanceQuery the advanceQuery to set
	 */
	public void setAdvanceQuery(String advanceQuery) {
		this.advanceQuery = advanceQuery;
	}
	/**
	 * @return the records
	 */
	public List<RecordDTO> getRecords() {
		if(sortMethod.equals("date")){
			List<String> orderByList = new ArrayList<String>();
			List<String> directionsList = new ArrayList<String>();
			orderByList.add("publicationYear");
			directionsList.add("desc");
			orderByList.add("harvardRepresentation");
			directionsList.add("asc");
			List<RecordDTO> temp = new ArrayList<RecordDTO>(records);
			Collections.sort(temp, new GenericComparator<RecordDTO>(
					orderByList, directionsList));
			return temp;
		} else 
			return records;
	}
	/**
	 * @param records the records to set
	 */
	public void setRecords(List<RecordDTO> records) {
		this.records = records;
	}
	/**
	 * @param records the records to set
	 */
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
	/**
	 * @param advanceQuerySearch the advanceQuerySearch to set
	 */
	public void advanceQuerySearch() {
		records =null;
		searchQueryError=null;
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		records = mediatorService.getRecords(advanceQuery);
		if(mediatorService.getErrMessage()!=null)
			searchQueryError = mediatorService.getErrMessage();
	}

	//	
	//	SINISA END
	//	
	
	/*	
	*	VALENTIN POCETAK
	*/		

	public void populateAll(){
		
		if(populateAll){
			getTree();
			for(TreeNodeDTO<InstitutionDTO> rootEl : root)
			{
				setOrganizationTreeForInstitutionTree(rootEl);
			}
		}
	}
	
	/**
	 * @return the root
	 */
	public List<TreeNodeDTO<InstitutionDTO>> getRoot() {
		
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
	public void setRoot(List<TreeNodeDTO<InstitutionDTO>> root) {
		this.root = root;
	}
	
	/**
	 * @return the allInstitutions
	 */
	public List<InstitutionDTO> getAllInstitutions() {
		return allInstitutions;
	}
	/**
	 * @param allInstitutions the allInstitutions to set
	 */
	public void setAllInstitutions(List<InstitutionDTO> allInstitutions) {
		this.allInstitutions = allInstitutions;
	}
	/**
	 * @return the allOrganizations
	 */
	public List<OrganizationUnitDTO> getAllOrganizations() {
		return allOrganizations;
	}
	/**
	 * @param allOrganizations the allOrganizations to set
	 */
	public void setAllOrganizations(List<OrganizationUnitDTO> allOrganizations) {
		this.allOrganizations = allOrganizations;
	}
	/**
	 * @return the allInstitutionsAndOrganizations
	 */
	public List<TreeNodeDTO<InstitutionDTO>> getAllInstitutionsAndOrganizations() {
		return allInstitutionsAndOrganizations;
	}
	/**
	 * @param allInstitutionsAndOrganizations the allInstitutionsAndOrganizations to set
	 */
	public void setAllInstitutionsAndOrganizations(
			List<TreeNodeDTO<InstitutionDTO>> allInstitutionsAndOrganizations) {
		this.allInstitutionsAndOrganizations = allInstitutionsAndOrganizations;
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
			  root = new ArrayList <TreeNodeDTO<InstitutionDTO>>();
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
		      }
		      
		      if (allInstitutionsAndOrganizations == null)
		      {
		 	    	allInstitutionsAndOrganizations = new ArrayList<TreeNodeDTO<InstitutionDTO>>();
		 	  }
		      
//			  for(InstitutionDTO ins:allInstitutions){
//					if(ins.getSuperInstitution() == null && ins.isEnabledElement()){
//						TreeNode<TreeNodeDTO<InstitutionDTO>> node = new TreeNodeImpl<TreeNodeDTO<InstitutionDTO>>();
//						node.setData(new TreeNodeDTO <InstitutionDTO> (ins));
//						allInstitutionsAndOrganizations.add(node.getData());
//						addNodeInstitution(node, ins);
//						root.addChild(ins.getControlNumber(), node);
//					}
//		      }
//		      naslilno dodeljujemo da je vrh stabla PMF
//		      "(BISIS)5929"; //Prirodno-matematički fakultet u Novom Sadu, Univerzitet u Novom Sadu (BISIS)5929
		      for(InstitutionDTO ins:allInstitutions){
					if(ins.getControlNumber().equalsIgnoreCase("(BISIS)5929")
							){
							//|| ins.getControlNumber().equalsIgnoreCase("(BISIS)5933")){
						TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
						node.setParent(null);
						allInstitutionsAndOrganizations.add(node);
						addNodeInstitution(node, ins);
						root.add(node);
					}
		      }
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	/*
	 public Boolean adviseNodeOpened(UITree tree) {
		 if (expandAll) {
		 return Boolean.TRUE;
		 }
		 return null;
	    }

	    public Boolean adviseNodeSelected(UITree tree) {
	        return null;
	    }
	 */
	private void addNodeInstitution(TreeNodeDTO<InstitutionDTO> parentNode, InstitutionDTO parentInstitution) {
//		parentInstitution.getAcro();  
		for(InstitutionDTO ins:getInstitutions(parentInstitution)){
//			  if(ins.isEnabledElement())
//			  {
				TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
				node.setParent(parentNode);
				allInstitutionsAndOrganizations.add(node);
				addNodeInstitution(node, ins);
				parentNode.addChild(node);
//			  }
		}
	  }
	
	private List<InstitutionDTO> getInstitutions(InstitutionDTO parentInstitution) {
		List<InstitutionDTO> retVal = new ArrayList<InstitutionDTO>();
		for (InstitutionDTO ins : allInstitutions) {
			if(ins.getSuperInstitution() != null)
				if(parentInstitution.getControlNumber().equals(ins.getSuperInstitution().getControlNumber())){
					retVal.add(ins);
				}
		}
		return retVal;
	}
	
	public void setOrganizationTreeForInstitutionTree(TreeNodeDTO<InstitutionDTO> parent)
	{
		if (!parent.isLeaf()){
			List<TreeNodeDTO <InstitutionDTO>> children = parent.getChildren();
			for(TreeNodeDTO <InstitutionDTO> child : children)
			{
				setOrganizationTreeForInstitutionTree(child);
			}
		}
		
		if(parent != null){
			List <TreeNodeDTO<InstitutionDTO>> tempTreeOgranizfation = getOrganizationTree(parent.getElement());
			if (tempTreeOgranizfation.isEmpty()){
				return;
			}
			
			for(TreeNodeDTO <InstitutionDTO> childOrganizations : tempTreeOgranizfation)
			{
				childOrganizations.setParent(parent);
				parent.addChild(childOrganizations);
			}
		}	
		
	}
	
	public List<TreeNodeDTO<InstitutionDTO>> getOrganizationTree(InstitutionDTO selectedInstitutionDTO)
	{
		if (allOrganizations == null){
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			OrganizationUnitManagedBean mb = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
					"organizationUnitManagedBean");
			if (mb == null) {
				mb = new OrganizationUnitManagedBean();
				extCtx.getSessionMap().put("organizationUnitManagedBean", mb);
			}
			allOrganizations = mb.getOrganizationUnits();
		}
		List <TreeNodeDTO<InstitutionDTO>> listTree = new ArrayList<TreeNodeDTO<InstitutionDTO>>();
		
		for (OrganizationUnitDTO ou : allOrganizations){
	            if((ou.getInstitution() != null) && (selectedInstitutionDTO.getControlNumber().equals(ou.getInstitution().getControlNumber()))){
//	            	if(ou.isEnabledElement())
	            	{
	            		TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ou);
						allInstitutionsAndOrganizations.add(node);
						addNodeOrganization(node, ou);
						listTree.add(node);
	            	}
	            }
	        }
		return listTree;
      }
		
	
	private void addNodeOrganization(TreeNodeDTO<InstitutionDTO> parentNode, OrganizationUnitDTO parentOrganizationUnit) {
		
		List<OrganizationUnitDTO> organizationSubList = new ArrayList<OrganizationUnitDTO>();
		for (OrganizationUnitDTO ou : allOrganizations) {
			if(ou.getSuperOrganizationUnit() != null)
				if(parentOrganizationUnit.getControlNumber().equals(ou.getSuperOrganizationUnit().getControlNumber())){
//					if(ou.isEnabledElement())
					organizationSubList.add(ou);
				}
		}
		
		
		for(OrganizationUnitDTO ou:organizationSubList){
				TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ou);
				node.setParent(parentNode);
				allInstitutionsAndOrganizations.add(node);
				addNodeOrganization(node, ou);
				parentNode.addChild(node);
		}
	  }
		
	public void changerememberMeChilds(TreeNodeDTO<InstitutionDTO> inputTree, boolean state, String s, int broj){
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
			List<TreeNodeDTO <InstitutionDTO>> children = inputTree.getChildren();
			for(TreeNodeDTO <InstitutionDTO> child : children)
			{
				changerememberMeChilds(child, state, s + "." + broj, i);
				i++;
			}
		}
	}
	
	public void printChilds(TreeNodeDTO<InstitutionDTO> inputTree, String s, int broj){
		if (inputTree.isLeaf()!=true)
		{
			int i =1;
			List<TreeNodeDTO <InstitutionDTO>> children = inputTree.getChildren();
			for(TreeNodeDTO <InstitutionDTO> child : children)
			{
				printChilds(child, s + "." + broj, i);
				i++;
			}
		}
	}
	
	public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent){
		
        List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
        Object currentSelectionKey = selection.get(0);
        treeState = (UITree) selectionChangeEvent.getSource();
 
        Object storedKey = treeState.getRowKey();
        treeState.setRowKey(currentSelectionKey);
        
        if(((TreeNodeDTO<InstitutionDTO>)treeState.getRowData()).getElement()!=null)
			changeSelectionOnTree(((TreeNodeDTO<InstitutionDTO>)treeState.getRowData()).getElement().getControlNumber());
        treeState.setRowKey(storedKey);	
	}

	public String kontrolniBroj ="";
	
	public String getKontrolniBroj() {
		return kontrolniBroj;
	}
	public void setKontrolniBroj(String kontrolniBroj) {
		this.kontrolniBroj = kontrolniBroj;
	}
	
	public void selectTreeTroughCommand() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String controlNumber = facesCtx.getExternalContext()
				.getRequestParameterMap().get("controlNumber");
		changeSelectionOnTree(controlNumber);
	}
	
	
	public void changeSelectionOnTree(String controlNumber) {
		expandAll=false;	
		TreeNodeDTO<InstitutionDTO> selectedInstitutionDTO = null;
 		for (TreeNodeDTO<InstitutionDTO> dto : allInstitutionsAndOrganizations) {
 			if ((dto.getElement().getControlNumber() != null)&& (dto.getElement().getControlNumber().equalsIgnoreCase(controlNumber))) {
 				selectedInstitutionDTO = dto;
 				break;
 			}
 		}
 		if (selectedInstitutionDTO == null){
 				return;
 		}
 		
 		boolean oldState = false;
 		oldState = selectedInstitutionDTO.isCheckbox_state();
 		boolean newState = !oldState;
 				
 		if(newState==false)
 		{
 			selectedInstitutionDTO.setTree_state("empty");
 			selectedInstitutionDTO.setCheckbox_state(false);
 		}
 		else
 		{
 			selectedInstitutionDTO.setTree_state("full");
 			selectedInstitutionDTO.setCheckbox_state(true);
 			selectedInstitutionDTO.setExpanded(true);
 		}
 		TreeNodeDTO<InstitutionDTO> parentInstitutionTree = (TreeNodeDTO<InstitutionDTO>) selectedInstitutionDTO.getParent();
 		
 		if(parentInstitutionTree != null)
 		{
 			TreeNodeDTO<InstitutionDTO> parentInstitution = parentInstitutionTree;
 			
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
	 				
	 				List<TreeNodeDTO <InstitutionDTO>> children = parentInstitution.getChildren();
	 				for(TreeNodeDTO <InstitutionDTO> child : children)
	 				{
	 					if(child.isCheckbox_state()==false)
	 		            {
	 		            	fullAll = false;
	 		            	break;
	 		            }
	 				}
	 				if(fullAll==true && parentInstitution.isCheckbox_state())
	 		        {	
	 		        	parentInstitution.setTree_state("full");
	 		        }
	 		        else if (fullAll==false && parentInstitution.isCheckbox_state()){
	 		        	parentInstitution.setTree_state("partial");
	 		        }
	 		        else
	 		        {
	 		        	parentInstitution.setTree_state("empty");
	 		        	parentInstitution.setExpanded(true);
	 		        }
	 			}
 			}
 		}
 		if(selectedInstitutionDTO.isLeaf() == true)
 		{
 			return;
 		}
 		else
 		{
 			int i=1;
 			List<TreeNodeDTO <InstitutionDTO>> children = selectedInstitutionDTO.getChildren();
			for(TreeNodeDTO <InstitutionDTO> child : children)
			{
				changerememberMeChilds(child, newState,"",i);
		       	i++;
			}	
 		}
 		
 		int i=1;
 		
 		Iterator<TreeNodeDTO<InstitutionDTO>> it = root.iterator();
	    while (it != null && it.hasNext()) {
	       	printChilds(it.next(),"",i);
	       	i++;
	    }
 }

	public void sendPublicationForCQL(){

		boolean havePreviusQueries= false;
		StringBuffer resultBufer = new StringBuffer();
		for (int i = 0; i < searchQuerys.size(); i++)
		{
//			System.out.println("havePreviusQueries: "+havePreviusQueries);
			boolean temVrednost = resolveTableColumToCQL(resultBufer, searchQuerys.get(i),i, havePreviusQueries);
			havePreviusQueries = (havePreviusQueries || temVrednost);
			
		}
//		System.out.println("Upit je tab" + resultBufer.toString());
		
		boolean temVrednost = resolveOthers(resultBufer,havePreviusQueries);
		havePreviusQueries = (havePreviusQueries || temVrednost);
		
		records =null;
		searchQueryError=null;
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		
		records = mediatorService.getRecords(resultBufer.toString());

		if(mediatorService.getErrMessage()!=null)
			searchQueryError = mediatorService.getErrMessage();
	}
		
	public boolean resolveTableColumToCQL(StringBuffer retVal, SearchDTO query, int queryOperatorIndex, boolean havePreviusQueries){
		
//		System.out.println("Pre "+queryOperatorIndex + " reda je:"+retVal.toString());
		if(query.getInputQuery()==null)
			return false;
		else if(query.getInputQuery().trim().equalsIgnoreCase(""))
			return false;
		
		String equationSymbol ="";
		if(query.getInputQuery().startsWith("\"") && query.getInputQuery().endsWith("\""))
		{
			equationSymbol=" adj ";
		}
		else if (this.fuzzy==true)
		{
			equationSymbol="=/fuzzy ";
		}
		else
		{
			equationSymbol="=";
		}
		
		if(havePreviusQueries==true)
		{
				String operator = returnLogicalOperator(query.getOperator());
				retVal.append(" "+operator+" ");
		}
//		System.out.println(" 1 ");
		if (query.getInputQueryType().trim().equalsIgnoreCase("choice1")){
			//retVal.append("naslov" + "=" + query.getInputQuery() + " " + " AND " + "apstrakt" + "=" + query.getInputQuery() + " " + "AND " + "kljucnereci" + "=" + query.getInputQuery());
			retVal.append("(dc.title" + equationSymbol + query.getInputQuery() + " OR " + "cris.abstract" + equationSymbol + query.getInputQuery() + " OR " + "dc.subject" + equationSymbol + query.getInputQuery()+ ") ");
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice2")){
			//ili izmedju imena autora jer ko zna kako je autor uneo publikacije
			if(query.getInputQuery().contains(";"))
			{
				String temp = query.getInputQuery();
				temp = temp.replaceAll("\"", "");		
				StringTokenizer tokenizer = new StringTokenizer(temp, ";");			
				retVal.append("(");	
				while(tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					retVal.append("dc.creator" + equationSymbol + "\"" +token +"\"");
					if(tokenizer.hasMoreTokens())
						retVal.append(" OR ");
				}	
				retVal.append(") ");
			}
			else
			{
				retVal.append("dc.creator" + equationSymbol + query.getInputQuery());
			}
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice3")){
			//ili izmedju imena autora jer ko zna kako je autor uneo publikacije
			if(query.getInputQuery().contains(";"))
			{
				String temp = query.getInputQuery();
				temp = temp.replaceAll("\"", "");		
				StringTokenizer tokenizer = new StringTokenizer(temp, ";");			
				retVal.append("(");	
				while(tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					retVal.append("cris.firstAuthor" + equationSymbol + "\"" +token +"\"");
					if(tokenizer.hasMoreTokens())
						retVal.append(" OR ");
				}	
				retVal.append(") ");
			}
			else
			{
				retVal.append("cris.firstAuthor" + equationSymbol + query.getInputQuery());
			}
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice4")){
			//retVal.append("naslov" + "=" + query.getInputQuery());
			retVal.append("dc.title" + equationSymbol + query.getInputQuery());
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice5")){
			//retVal.append("apstrakt" + "=" + query.getInputQuery());
			retVal.append("cris.abstract" + equationSymbol + query.getInputQuery());
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice6")){
			//retVal.append("kljucnereci" + "=" + query.getInputQuery());
			retVal.append("dc.subject" + equationSymbol + query.getInputQuery());
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice7")){
			//retVal.append("konferencije" + "=" + query.getInputQuery());
			retVal.append("cris.conference" + equationSymbol + query.getInputQuery());
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice8")){
			//retVal.append("casopisi" + "=" + query.getInputQuery());
			retVal.append("cris.journal" + equationSymbol + query.getInputQuery());
		}
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice9")){
			//retVal.append("casopisi" + "=" + query.getInputQuery());
			retVal.append("cris.moograph" + equationSymbol + query.getInputQuery());
		}
		else
		{	//default vrednost
			//retVal.append("autor" + "=" + query.getInputQuery() + " AND " + "prviautor" + "=" + query.getInputQuery() + " " + "AND " + "naslov" + "=" + query.getInputQuery() + " " + "AND " + "apstrakt" + "=" + query.getInputQuery() + " " + "AND " + "kljucnereci" + "=" + query.getInputQuery() + " " + "AND " +"konferencije" + "=" + query.getInputQuery() + " " + " AND " +"casopisi" + "=" + query.getInputQuery());
			retVal.append("(dc.creator" + equationSymbol + query.getInputQuery() + " OR " + "dc.title" + equationSymbol + query.getInputQuery() + " OR " + "cris.abstract" + equationSymbol + query.getInputQuery() + " OR " + "dc.subject" + equationSymbol + query.getInputQuery() + " OR " +"cris.conference" + equationSymbol + query.getInputQuery() + " OR " +"cris.journal" + equationSymbol + query.getInputQuery() + " OR " +"cris.monograph" + equationSymbol + query.getInputQuery()+ ") ");	
		}
//		System.out.println("Posle "+queryOperatorIndex + " reda je:"+retVal.toString());
		
		return true;
	}
	
	
	public boolean resolveOthers(StringBuffer retVal, boolean havePreviusQueries){

		boolean temVrednost = false;
		temVrednost = appendCqlPublicationDate(retVal,havePreviusQueries);
		havePreviusQueries = (havePreviusQueries || temVrednost);
//		System.out.println("Posle appendCqlPublicationDate je:"+retVal.toString());
		temVrednost = appendCqlPublicationType(retVal, havePreviusQueries);
		havePreviusQueries = (havePreviusQueries || temVrednost);
//		System.out.println("Posle appendCqlPublicationType je:"+retVal.toString());
		temVrednost = appendCqlTree(retVal, havePreviusQueries);
		havePreviusQueries = (havePreviusQueries || temVrednost);
//		System.out.println("Posle appendCqlTree je:"+retVal.toString());
		return havePreviusQueries;	
	}
	
	public boolean appendCqlPublicationDate(StringBuffer retVal, boolean havePreviusQueries)
	{
		if (havePreviusQueries==true && ((!fromDate.trim().equalsIgnoreCase("default")) || (!toDate.trim().equalsIgnoreCase("default"))))
			retVal.append(" AND ");
		
		if ((!fromDate.trim().equalsIgnoreCase("default")) || (!toDate.trim().equalsIgnoreCase("default")))
			retVal.append("(");
		
		boolean hasFfirst = false;
		if (!fromDate.trim().equalsIgnoreCase("default"))
		{
			retVal.append("dc.date>=" + fromDate);
			havePreviusQueries =true;
			hasFfirst = true;
		}
		if (!toDate.trim().equalsIgnoreCase("default"))
		{
			if(hasFfirst)
				retVal.append(" AND ");
			retVal.append("dc.date<=" + toDate);
			havePreviusQueries =true;
		}
		if ((!fromDate.trim().equalsIgnoreCase("default")) || (!toDate.trim().equalsIgnoreCase("default")))
			retVal.append(") ");
		
		return havePreviusQueries;
	}
	
	public boolean appendCqlPublicationType(StringBuffer retVal, boolean havePreviusQueries)
	{
		if(havePreviusQueries==true){
			retVal.append(" AND ");
		}
		
		retVal.append("(");
		
		if (publicationType.trim().equalsIgnoreCase("choice1")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u zborniku radova" + " " );
			retVal.append(	"cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
							" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS);
			
		}
		else if (publicationType.trim().equalsIgnoreCase("choice2")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u casopisu" + " " );
			retVal.append(	"cris.type" + "=" + Types.JOURNAL+
							" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
							" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
							" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL);
		}
		else if (publicationType.trim().equalsIgnoreCase("choice3")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u monografiji" + " " );
			retVal.append(	"cris.type" + "=" + Types.MONOGRAPH+
							" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH);
		}
		else if (publicationType.trim().equalsIgnoreCase("choice4")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u monografiji" + " " );
			retVal.append(	"cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+		
							" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
		}
		else
		{	//default
			//retVal.append(" tippublikacije=rad u zborniku radova AND tippublikacije=rad u casopisu AND tippublikacije=rad u monografiji");
			retVal.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
					" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
					" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS+ " ");
			retVal.append(" OR " + "cris.type" + "=" + Types.JOURNAL+
					" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
					" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
					" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL+" ");
			retVal.append(" OR " + "cris.type" + "=" + Types.MONOGRAPH+
					" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH+" ");
			retVal.append(" OR " + "cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
					" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);
		}
		
		retVal.append(") ");
		havePreviusQueries =true;
		
		return true;
	}
	
	public boolean appendCqlTree(StringBuffer retVal, boolean havePreviusQueries)
	{
		int numberSelected = 0;
		
		
		for (TreeNodeDTO<InstitutionDTO> dto : allInstitutionsAndOrganizations){
			if (dto.isCheckbox_state()) {
				numberSelected = numberSelected+1;
			}
		}
		if(numberSelected>0)
		{
		
			if(havePreviusQueries==true){
				retVal.append(" AND ");
			}
			
			retVal.append("( ");
			
			boolean foundFirst = false;
			for (int i = 0; i<allInstitutionsAndOrganizations.size(); i++)
			{
				TreeNodeDTO<InstitutionDTO> dto = allInstitutionsAndOrganizations.get(i);
				if ((dto.isCheckbox_state())) {
					if(foundFirst == true)
						retVal.append(" OR ");
	 				retVal.append("dc.contributor adj \"" + dto.getElement().getSomeName()+"\"");
	 				foundFirst = true;
	 			}
			}
			retVal.append(" )");
			
			if(numberSelected>0)
				havePreviusQueries=true;
		}
		else
		{
			if(havePreviusQueries==true){
				retVal.append(" AND ");
			}
			
			retVal.append("( ");
			
			boolean foundFirst = false;
			for (int i = 0; i<allInstitutionsAndOrganizations.size(); i++)
			{
				TreeNodeDTO<InstitutionDTO> dto = allInstitutionsAndOrganizations.get(i);
				
				//Ako ne odaberemo nista iz drveta za koje ce se podatke izvrsiti pretraga
				
				//Pretrazuje se PMF i sve njegove katedre
				
				/*if(dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)5929")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6782")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6887")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6888")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6889")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6890")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6891")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6892")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6893")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6894")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6895"))*/
				
				if(dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)5929") // Prirodno Matematicki fakultet u Novom Sadu
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6782") // Departman za matematiku i informatiku 
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6887") // Departman za matematiku i informatiku  Katedra za opštu algebru i teorijsko računarstvo
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6888") // Departman za matematiku i informatiku  Katedra za analizu, verovatnoću i diferencijalne jednačine
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6889") // Departman za matematiku i informatiku  Katedra za numeričku matematiku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6890") // Departman za matematiku i informatiku  Katedra za primenjenu algebru
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6891") // Departman za matematiku i informatiku  Katedra za funkcionalnu analizu, geometriju i topologiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6892") // Departman za matematiku i informatiku  Katedra za računarske nauke
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6893") // Departman za matematiku i informatiku  Katedra za matematičku logiku i diskretnu matematiku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6894") // Departman za matematiku i informatiku  Katedra za primenjenu analizu
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6895") // Departman za matematiku i informatiku  Katedra za informacione tehnologije i sisteme
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)27362") // Departman za matematiku i informatiku  Katedra za teorijske osnove informatike
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6778") // Departman za biologiju i ekologiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6864") // Departman za biologiju i ekologiju Katedra za botaniku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6865") // Departman za biologiju i ekologiju Katedra za fiziologiju, genetiku i histologiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6866") // Departman za biologiju i ekologiju Katedra za zoologiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6867") // Departman za biologiju i ekologiju Katedra za mikrobiologiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6868") // Departman za biologiju i ekologiju Katedra za humanu biologiju i metodiku nastave biologije
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)7098") // Departman za biologiju i ekologiju Katedra za ekologiju i zaštitu životne sredine
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)106923") // Departman za biologiju i ekologiju Katedra za biohemiju, molekularnu biologiju i genetiku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6779") // Departman za fiziku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6869") // Departman za fiziku Katedra za eksperimentalnu fiziku kondenzovane materije
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6870") // Departman za fiziku Katedra za fizičku elektroniku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6871") // Departman za fiziku Katedra za nuklearnu fiziku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6872") // Departman za fiziku Katedra za teorijsku fiziku
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6873") // Departman za fiziku Katedra za opštu fiziku i metodiku nastave fizike
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6780") // Departman za geografiju, turizam i hotelijerstvo
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6874") // Departman za geografiju, turizam i hotelijerstvo Katedra za fizičku geografiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6875") // Departman za geografiju, turizam i hotelijerstvo Katedra za društvenu geografiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6876") // Departman za geografiju, turizam i hotelijerstvo Katedra za regionalnu geografiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6877") // Departman za geografiju, turizam i hotelijerstvo Katedra za turizam
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6878") // Departman za geografiju, turizam i hotelijerstvo Katedra za hotelijerstvo
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6879") // Departman za geografiju, turizam i hotelijerstvo Katedra za lovni turizam
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)106924") // Departman za geografiju, turizam i hotelijerstvo Katedra za gastronomiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6781") // Departman za hemiju, biohemiju i zaštitu životne sredine
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6880") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za analitičku hemiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6881") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za biohemiju i hemiju prirodnih proizvoda
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6882") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za fizičku hemiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6883") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za hemijsko obrazovanje i metodiku nastave hemije
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6884") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za hemijsku tehnologiju i zaštitu životne sredine
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6885") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za opštu i neorgansku hemiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6886") // Departman za hemiju, biohemiju i zaštitu životne sredine Katedra za organsku hemiju
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)5933") // Tehnicko tehnoloski fakultet
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85798") //katedre
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85799")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85800")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85801")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85802")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85803")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85804")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)85805")
						) 
				
				
				{
					if(foundFirst == true)
						retVal.append(" OR ");
	 				retVal.append("dc.contributor adj \"" + dto.getElement().getSomeName()+"\"");
	 				foundFirst = true;
				}
			}
			retVal.append(" )");
		}
		
		return havePreviusQueries;
	}
	
	public String returnLogicalOperator(String operator)
	{
		return (operator.equalsIgnoreCase("AND NOT")?"NOT": operator);
	}

	/*	
	*	VALRNTIN END
	*/
	
	public boolean isSearchResultShow()
	{
		return (searchQueryError!=null)||(records!=null);
	}
	
	public boolean getSearchResultShow()
	{
		return (searchQueryError!=null)||(records!=null);
	}
	
	
	private UITree treeState;
	
	/**
	 * Switches interface to English language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String englishLanguage() {
		setLocale("en");
		init = false;
//		allInstitutions = null;
//		allOrganizations = null;
//		root = null;
//		allInstitutionsAndOrganizations = null;
		
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		ExternalContext extCtx = facesCtx.getExternalContext();
//		OrganizationUnitManagedBean mbO = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
//				"organizationUnitManagedBean");
//		if (mbO == null) {
//			mbO = new OrganizationUnitManagedBean();
//			extCtx.getSessionMap().put("organizationUnitManagedBean", mbO);
//		}
//		mbO.populateList = true;
//		
//		InstitutionManagedBean mbI = (InstitutionManagedBean) extCtx.getSessionMap().get(
//				"institutionManagedBean");
//		if (mbI == null) {
//			mbI = new InstitutionManagedBean();
//			extCtx.getSessionMap().put("institutionManagedBean", mbI);
//		}
//		mbI.populateList = true;
		
		return "searchPage";
	}

	/**
	 * Switches interface to Serbian language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String serbianLanguage() {
		setLocale("sr");
		init = false;
//		allInstitutions = null;
//		allOrganizations = null;
//		root = null;
//		allInstitutionsAndOrganizations = null;
		
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		ExternalContext extCtx = facesCtx.getExternalContext();
//		OrganizationUnitManagedBean mbO = (OrganizationUnitManagedBean) extCtx.getSessionMap().get(
//				"organizationUnitManagedBean");
//		if (mbO == null) {
//			mbO = new OrganizationUnitManagedBean();
//			extCtx.getSessionMap().put("organizationUnitManagedBean", mbO);
//		}
//		mbO.populateList = true;
//		
//		InstitutionManagedBean mbI = (InstitutionManagedBean) extCtx.getSessionMap().get(
//				"institutionManagedBean");
//		if (mbI == null) {
//			mbI = new InstitutionManagedBean();
//			extCtx.getSessionMap().put("institutionManagedBean", mbI);
//		}
//		mbI.populateList = true;
		return "searchPage";
	}

	private String language = "sr";

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
				"languageSearch=" + language + ";HttpOnly;expires=" + date.getTime());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(language));
		this.language = language;
	}

} 
