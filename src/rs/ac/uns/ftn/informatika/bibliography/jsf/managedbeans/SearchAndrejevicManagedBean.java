package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import org.primefaces.component.api.UITree;
import org.primefaces.event.NodeSelectEvent;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.SearchDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.mediator.MediatorService;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchAndrejevicManagedBean extends CRUDManagedBean implements IPickAuthorManagedBean, IPickJournalManagedBean, IPickConferenceManagedBean{

	protected List<SearchDTO> searchQuerys = null;
	@SuppressWarnings("rawtypes")
	protected List<SelectItem> searchQueryOperator = new ArrayList(Arrays.asList(new SelectItem("AND"), new SelectItem("OR"), new SelectItem("AND NOT")));
	protected int counterSearchQuery = 0;
	
	private String [] searchQueryTypeString = new String [] {"default", "choice1", "choice2", "choice3", "choice4", "choice5", "choice6", "choice7", "choice8","choice9", "choice10", "choice11"};
	protected List<SelectItem> dateRange = null;
	
	protected boolean fuzzy = false; //for defining fuzzy query
	
	protected String fromDate = "";
	protected String toDate = "";
			
	protected String publicationType = ""; //je zapravo Library Type
	
	protected List<AuthorDTO> authorSearched = null;
	protected String firstLastName="";
	
	
	protected String representationStyle = "htmlRepresentation";
	
	// Search possible Types in correspondence to positions in searchQueryTypeString 
	// All fields;	Article Title, Abstract, Keywords;	Authors;   First Author;	Article Title;	Abstract;	Keywords;	Conference;		Journal
	
	//	Search possible publication Types in correspondence to positions in searchQueryTypeString
	//	All;	Conference paper;	Journal paper;	Monograph;	Thesis;
	
	//	Search possible library Types in correspondence to positions in searchQueryTypeString
	//  ACADEMIA, DISSERTATIO, POSEBNA IZDANJA, ZELENA LINIJA ŽIVOTA, 	ZBORNICI, 	EDICIJA NAUČNICI JUGOSLAVIJE, INSPIRATIO, SPECIALIS, EDUCATIO, INITIUM, OSTALO
	
	public boolean populateAll = true;
	protected List<TreeNodeDTO<InstitutionDTO>> root = null;
	//DZA
	protected List<TreeNodeDTO<ResearchAreaDTO>> rootAndrejevicResearchArea = null;
	protected List<InstitutionDTO> allInstitutions = null;
	protected List<ResearchAreaDTO> allAndrejevicResearchArea = null; 
	protected List<OrganizationUnitDTO> allOrganizations = null;
	protected List<TreeNodeDTO<InstitutionDTO>> allInstitutionsAndOrganizations = null;
	protected List<TreeNodeDTO<ResearchAreaDTO>> allAndrejevicResearchAreaTreeNode = null;
	
	
	
	
	// Advance query
	protected String advanceQuery = "";
	List<RecordDTO> records =null;
	protected String searchQueryError = null;
	
	protected boolean expandAll=true;
	private MediatorService mediatorService = null;
	
	/**
	 * Constructor
	 */
	public SearchAndrejevicManagedBean() {
		super();
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
		rootAndrejevicResearchArea = null;
		allOrganizations=null;
		allInstitutions=null;
		allAndrejevicResearchArea=null;
		allInstitutionsAndOrganizations=null;
		allAndrejevicResearchAreaTreeNode=null;
		populateAll = true;
		populateAll();
		populateAllAndrejevicResearchArea();
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
	public void setSearchQuerys(List<SearchDTO> searchQueries) {
		this.searchQuerys = searchQueries;
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
		retVal = "searchAndrejevicPage";
		return retVal;
	}
	
	
	public String searchPageEnter() {
		
		resetForm();
		
		setAuthorManageBeanToPick();
		setJournalManageBeanToPick();
		setConferenceManageBeanToPick();
		
		root= null;
		rootAndrejevicResearchArea=null;
		allOrganizations=null;
		allInstitutions=null;
		allAndrejevicResearchArea=null;
		allInstitutionsAndOrganizations=null;
		allAndrejevicResearchAreaTreeNode=null;
		populateAll = true;
		populateAll();
		populateAllAndrejevicResearchArea();
		
		returnPage = "indexPage";
		return "searchAndrejevicPage";
	}
	
	
	public void searchPageEnter(PhaseEvent event){
		if(init == false){
			resetForm();
			
			setAuthorManageBeanToPick();
			setJournalManageBeanToPick();
			setConferenceManageBeanToPick();
			
			root= null;
			rootAndrejevicResearchArea=null;
			allOrganizations=null;
			allInstitutions=null;
			allAndrejevicResearchArea=null;
			allInstitutionsAndOrganizations=null;
			allAndrejevicResearchAreaTreeNode=null;
			populateAll = true;
			populateAll();
			populateAllAndrejevicResearchArea();
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
	 * @param firstLastName the authorName to set
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
		
	}
	
	
	public void btnAuthorAndrejevicFind()
	{
		records =null;
		searchQueryError=null;
		
		int sizeAuthorSelectedHistory = authorSearched.size();
		for(int i =0; i<sizeAuthorSelectedHistory; i++)
			authorSearched.remove(0);
		
		//Ovde formiram upit da dobijem sve zapise tipa Autor koji se nalaze u bazi
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
			//System.out.println("Nema autora sa tim imenom");
			return;
		}
		
		//Deo u kome postvljam upit da dobijem sve monografije iz andrejevica  
		StringBuffer bufferAndrejevicQuery = new StringBuffer();
		bufferAndrejevicQuery.append("dc.creator= " + firstLastName+
				" AND " + "cris.type" + "=" + Types.MONOGRAPH + " AND " + "dc.publisher" + "=andrejevic"  ); 
		
		List<RecordDTO> tempMonoraphRecords =null; // Lista zapisa u koji cu smestiti recorde (buduce monografije) iz andrejevica iz Recorada
		tempMonoraphRecords = mediatorService.getRecords(bufferAndrejevicQuery.toString());
		
		if(tempMonoraphRecords==null)
		{
			//System.out.println("Navedeni autor nema monografiju");
			return;
		}
		
		
		List<MonographDTO> tempMonographAndrejevicRecords = new ArrayList<MonographDTO> (); // Privremena lista u kojoj ce se smestiti konkretni rekordi monografija iz andrejevica
		List<List<AuthorDTO>> tempListOfAuthorsMonographAndrejevicRecords = new ArrayList<List<AuthorDTO>>(); // Za svaku entiet (monografije) imam listu autora   
		List<AuthorDTO> tempAuthorMonographAndrejevicRecords = new ArrayList<AuthorDTO> (); //Lista svih autora u monografijama
		
				
		for (RecordDTO imonograph :tempMonoraphRecords){
			tempMonographAndrejevicRecords.add((MonographDTO)imonograph);
			
		}
		
		for (int i=0; i<tempMonographAndrejevicRecords.size(); i++){
				tempListOfAuthorsMonographAndrejevicRecords.add(tempMonographAndrejevicRecords.get(i).getAllAuthors());
		}
		
		//System.out.println(tempListOfAuthorsMonographAndrejevicRecords.size());
		for (List<AuthorDTO> iListAuthor :tempListOfAuthorsMonographAndrejevicRecords){
			for(AuthorDTO iAuthor:iListAuthor){
				//System.out.println(iAuthor.getHTMLRepresentation());
				tempAuthorMonographAndrejevicRecords.add(iAuthor);
			}
		}
		
		
		
		//
		List<AuthorDTO> authorsFromAuthorRecords = new ArrayList<AuthorDTO> ();
		
		for(RecordDTO iAuthorRecord : tempRecords)
		{
			authorsFromAuthorRecords.add((AuthorDTO)iAuthorRecord);	
		}
			
		//authorsFromAuthorRecords - lista svih autora 
		//tempAuthorMonographAndrejevicRecords - lista svih autora u mografijama andrejevic
		for(AuthorDTO allAuthorRecord : authorsFromAuthorRecords){
			for(AuthorDTO allAuthorInAndrejevicMonograph : tempAuthorMonographAndrejevicRecords){
				if(allAuthorRecord.getControlNumber().equals(allAuthorInAndrejevicMonograph.getControlNumber())){
					authorSearched.add(allAuthorRecord);
				}
			}
		}
		
		
		
		
		/*System.out.println("List iz Autora");
		System.out.println(authorsFromAuthorRecords.size());
		for (AuthorDTO iauthorRecord :authorsFromAuthorRecords){
			System.out.println(iauthorRecord.getHTMLRepresentation());
		}
		
		System.out.println("List iz Monografija");
		System.out.println(tempAuthorMonographAndrejevicRecords.size());
		for (AuthorDTO iAuthorMonographAndrejevicRecord :tempAuthorMonographAndrejevicRecords){
			System.out.println(iAuthorMonographAndrejevicRecord.getHTMLRepresentation());
		}
		*/
		
		
	}
	
	
	public void btnAuthorAndrejevicFind1()
	{
		records =null;
		searchQueryError=null;
		
		authorSearched.clear();
		
		
		RecordDAO personDAO = new RecordDAO(new PersonDB());
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
        
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
		
		for (RecordDTO recordDTO : tempRecords) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO;
				if (dto != null) {
					BooleanQuery bqMonograph = new BooleanQuery();
					
					BooleanQuery bqAuthorRole = new BooleanQuery();
					bqAuthorRole.add(new TermQuery(new Term("AUCN", dto.getControlNumber())), Occur.SHOULD);
					bqAuthorRole.add(new TermQuery(new Term("EDCN", dto.getControlNumber())), Occur.SHOULD);
					
					bqMonograph.add(bqAuthorRole, Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
					List<Record> listRecordMonograph = recordDAO.getDTOs(bqMonograph, new AllDocCollector(true));
					if(listRecordMonograph.size()>0){
						authorSearched.add(dto);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		
		
		
//		
//		//Deo u kome postvljam upit da dobijem sve monografije iz andrejevica  
//		StringBuffer bufferAndrejevicQuery = new StringBuffer();
//		bufferAndrejevicQuery.append("dc.creator= " + authorName+
//				" AND " + "cris.type" + "=" + Types.MONOGRAPH + " AND " + "dc.publisher" + "=andrejevic"  ); 
//		
//		List<RecordDTO> tempMonoraphRecords =null; // Lista zapisa u koji cu smestiti recorde (buduce monografije) iz andrejevica iz Recorada
//		tempMonoraphRecords = mediatorService.getRecords(bufferAndrejevicQuery.toString());
//		
//		if(tempMonoraphRecords==null)
//		{
//			//System.out.println("Navedeni autor nema monografiju");
//			return;
//		}
//
//		for (RecordDTO imonograph :tempMonoraphRecords){
//			List<AuthorDTO> tempAuthors = ((MonographDTO)imonograph).getAllAuthors();
//			for(AuthorDTO temp :tempAuthors){
//				addAuthorToAuthorSearched(temp, authorSearched);
//			}
//			
//		}

	}
//	private void addAuthorToAuthorSearched(AuthorDTO author,List<AuthorDTO> authorSearched){
//		if(authorSearched.size()==0){
//			authorSearched.add(author);
//			return;
//		}	
//		boolean found = false;
//		for(AuthorDTO dto:authorSearched){
//			if(dto.getControlNumber().equals(author.getControlNumber())){
//				found=true;
//				break;
//			}
//		}
//		if(!found){
//			authorSearched.add(author);
//		}
//	}
//	
	
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
			bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND ( " + "cris.type" + "=" + Types.PAPER_JOURNAL+
						" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL +
						" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL +" )");
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[2]))
		{	//papers from author Proceedings
//			System.out.println("papers from author Proceedings");
			bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
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
			bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
						" AND  " +  "cris.type" + "=" + Types.MONOGRAPH
						+ " AND " + "dc.publisher=andrejevic");
//			System.out.println(bufferQuery.toString());
		}
		else if(queryType.equalsIgnoreCase(searchQueryTypeString[4]))
		{	//papers from author Theses and Dissertations
//			System.out.println("papers from author Theses and Dissertations");
			bufferQuery.append("dc.creator= "+selectedAuthor.getName().getLastname()+" "+selectedAuthor.getName().getFirstname()+
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
		/*else if(type.equalsIgnoreCase(searchQueryTypeString[7]))
		{	//conference
			setConferenceManageBeanToPick();
		}
		else if(type.equalsIgnoreCase(searchQueryTypeString[8]))
		{	//jurnal
			setJournalManageBeanToPick();
			
		}*/	
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
		return records;
	}
	/**
	 * @param records the records to set
	 */
	public void setRecords(List<RecordDTO> records) {
		this.records = records;
	}
	/**
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
	 */
	public void advanceQuerySearch() {
		records =null;
		List<RecordDTO> tempRecords =null;
		searchQueryError=null;
		mediatorService.setLocaleLan(new Locale(this.getUserManagedBean().getLanguage()));
		tempRecords = mediatorService.getRecords(advanceQuery);
		
		if(mediatorService.getErrMessage()!=null){
			records=tempRecords;
			searchQueryError = mediatorService.getErrMessage();
			return;
		}	
		records = new ArrayList<RecordDTO>();
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		
		for (RecordDTO dto :tempRecords){
			if (dto instanceof AuthorDTO){
				
				
				BooleanQuery bqMonograph = new BooleanQuery();
				
				BooleanQuery bqAuthorRole = new BooleanQuery();
				bqAuthorRole.add(new TermQuery(new Term("AUCN", dto.getControlNumber())), Occur.SHOULD);
				bqAuthorRole.add(new TermQuery(new Term("EDCN", dto.getControlNumber())), Occur.SHOULD);
				
				bqMonograph.add(bqAuthorRole, Occur.MUST);
				bqMonograph.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
				bqMonograph.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
				
				
				
				List<Record> listRecordMonograph = recordDAO.getDTOs(bqMonograph, new AllDocCollector(true));
				if(listRecordMonograph.size()>0){
					records.add(dto);
				}
			}
			else if (dto instanceof MonographDTO){
				BooleanQuery bq = new BooleanQuery();
				
				bq.add(new TermQuery(new Term("CN", dto.getControlNumber())), Occur.MUST);
				bq.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
				
				List<Record> listRecordMonograph = recordDAO.getDTOs(bq, new AllDocCollector(true));
				if(listRecordMonograph.size()>0){
					records.add(dto);
				}
			}
		}
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
	
	public void populateAllAndrejevicResearchArea(){
		
		if(populateAll){
			getTreeAndrejevicResearchArea();
			/*for(TreeNodeDTO<InstitutionDTO> rootEl : root)
			{
				setOrganizationTreeForInstitutionTree(rootEl);
			}*/
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
			treeState.clearInitialState();
		
		return root;
	}
	
	/**
	 * @return the rootAndrejevicResearchArea
	 */
	public List<TreeNodeDTO<ResearchAreaDTO>> getRootAndrejevicResearchArea() {
		
		if(rootAndrejevicResearchArea==null)
		{
			populateAll=true;
			populateAllAndrejevicResearchArea();
		}
		
		return rootAndrejevicResearchArea;
	}
	
	/**
	 * @param root the root to set
	 */
	public void setRoot(List<TreeNodeDTO<InstitutionDTO>> root) {
		this.root = root;
	}
	
	/**
	 * @param rootAndrejevicResearchArea the rootAndrejevicResearchArea to set
	 */
	public void setRootAndrejevicResearchArea(List<TreeNodeDTO<ResearchAreaDTO>> rootAndrejevicResearchArea) {
		this.rootAndrejevicResearchArea = rootAndrejevicResearchArea;
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
	 * @return the allAndrejevicResearchArea
	 */
	public List<ResearchAreaDTO> getAllAndrejevicResearchArea() {
		return allAndrejevicResearchArea;
	}
	/**
	 * @param allAndrejevicResearchArea the allAndrejevicResearchArea to set
	 */
	public void setAllAndrejevicResearchArea(
			List<ResearchAreaDTO> allAndrejevicResearchArea) {
		this.allAndrejevicResearchArea = allAndrejevicResearchArea;
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
	 * @return the allAndrejevicResearchAreaTreeNode
	 */
	public List<TreeNodeDTO<ResearchAreaDTO>> getAllAndrejevicResearchAreaTreeNode() {
		return allAndrejevicResearchAreaTreeNode;
	}
	/**
	 * @param allAndrejevicResearchAreaTreeNode the allAndrejevicResearchAreaTreeNode to set
	 */
	public void setAllAndrejevicResearchAreaTreeNode(
			List<TreeNodeDTO<ResearchAreaDTO>> allAndrejevicResearchAreaTreeNode) {
		this.allAndrejevicResearchAreaTreeNode = allAndrejevicResearchAreaTreeNode;
	}

	
	
	
	/**
	 * @return the populateAll
	 */
	public boolean isPopulateAll() {
		return populateAll;
	}
	
	public void getTreeAndrejevicResearchArea() {
		debug("getTreeAndrejevicResearchArea");
		try {
				//prvi korak umesto InstitutionDTO ResearchAreaDTO
			 // System.out.println("Usao u tree");
			rootAndrejevicResearchArea = new ArrayList <TreeNodeDTO<ResearchAreaDTO>>();
		      if(allAndrejevicResearchArea == null){
		    	//  System.out.println("Nema Area vadim iz contexta");
		      	FacesContext facesCtx = FacesContext.getCurrentInstance();
				ExternalContext extCtx = facesCtx.getExternalContext();
				ResearchAreaManagedBean mb = (ResearchAreaManagedBean) extCtx.getSessionMap().get(
						"researchAreaManagedBean");
				if (mb == null) {
					mb = new ResearchAreaManagedBean();
					extCtx.getSessionMap().put("researchAreaManagedBean", mb);
				}
				//System.out.println("Ima mangead bean");
				
				//mb.setIncludeOrganizationUnits(false);
				List<ResearchAreaDTO> allResearchArea=mb.getResearchAreas();
				//System.out.println(allResearchArea.size());
				
				allAndrejevicResearchArea = new ArrayList<ResearchAreaDTO>();
				for(int i=0; i<allResearchArea.size(); i++){
					
					if((allResearchArea.get(i).getClassId()).startsWith("and")){
						allAndrejevicResearchArea.add(allResearchArea.get(i));
					}
				}
		      }
		      if (allAndrejevicResearchAreaTreeNode == null)
		      {
		 	    	allAndrejevicResearchAreaTreeNode = new ArrayList<TreeNodeDTO<ResearchAreaDTO>>();
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
//		      "(BISIS)5929"; //Prirodno-matematiÄ�ki fakultet u Novom Sadu, Univerzitet u Novom Sadu (BISIS)5929
		     /* for(InstitutionDTO ins:allInstitutions){
					if(ins.getControlNumber().equalsIgnoreCase("(BISIS)5929")){
						TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
						node.setParent(null);
						allInstitutionsAndOrganizations.add(node);
						addNodeInstitution(node, ins);
						root.add(node);
					}
		      }*/
		      
		      for(ResearchAreaDTO ra: allAndrejevicResearchArea){
		    	  
		    	  if ((ra.getClassId().equalsIgnoreCase("andOb4")) || (ra.getClassId().equalsIgnoreCase("andOb5")) || (ra.getClassId().equalsIgnoreCase("andOb6")) || 
		    			  (ra.getClassId().equalsIgnoreCase("andOb7")) || (ra.getClassId().equalsIgnoreCase("andOb8")) || (ra.getClassId().equalsIgnoreCase("andOb10")) || 
		    			  (ra.getClassId().equalsIgnoreCase("andOb11"))){
		    		  TreeNodeDTO<ResearchAreaDTO> node = new TreeNodeDTO<ResearchAreaDTO>(ra);
		    		  node.setParent(null);
		    		  addNodeResearchArea(node, ra);
		    		  rootAndrejevicResearchArea.add(node);
		    		  allAndrejevicResearchAreaTreeNode.add(node);
		    	  }
		      }
//		      for (TreeNodeDTO<ResearchAreaDTO>  ra:rootAndrejevicResearchArea){
//		    	  int i=1;
//		    	  if(ra.getData() !=null){
//		    		  printChildsArea(ra, "", i);
//		    	  }
//		    	  i++;
//		    		  
//		      }
		      
		      
		      
	    } catch (Exception e) {
	    	error("getTreeAndrejevicResearchArea", e);
	    }
	}
	
	private void addNodeResearchArea(TreeNodeDTO<ResearchAreaDTO> parentNode, ResearchAreaDTO parentResearcArea) {
		  for(ResearchAreaDTO ra:getResearchAreas(parentResearcArea)){
			  
			  	TreeNodeDTO<ResearchAreaDTO> node = new TreeNodeDTO<ResearchAreaDTO>(ra);
				node.setParent(parentNode);
				allAndrejevicResearchAreaTreeNode.add(node);
				addNodeResearchArea(node, ra);
				parentNode.addChild(node);
			  
		}
	  }
	
	private List<ResearchAreaDTO> getResearchAreas(ResearchAreaDTO parentResearcArea) {
		List<ResearchAreaDTO> retVal = new ArrayList<ResearchAreaDTO>();
		for (ResearchAreaDTO ra : allAndrejevicResearchArea) {
			if(ra.getSuperResearchArea() != null)
				if(parentResearcArea.getClassId().equals(ra.getSuperResearchArea().getClassId())){
					retVal.add(ra);
				}
		}
		return retVal;
	}
	
	public void printChildsArea(TreeNodeDTO<ResearchAreaDTO> inputTree, String s, int broj){
		//System.out.println(s + broj + " 	" +inputTree.getData().getClassId());
		if (inputTree.isLeaf()!=true)
		{
			
			int i =1;
			List<TreeNodeDTO <ResearchAreaDTO>> children = inputTree.getChildren();
			for(TreeNodeDTO <ResearchAreaDTO> child : children)
			{
				printChildsArea(child, s + broj + ".", i);
				i++;
			}
		}
	}
	
	
	public void getTree() {
		debug("getTree");
		try {
				//prvi korak umesto InstitutionDTO ResearchAreaDTO 
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
//		      "(BISIS)5929"; //Prirodno-matematiÄ�ki fakultet u Novom Sadu, Univerzitet u Novom Sadu (BISIS)5929
		      for(InstitutionDTO ins:allInstitutions){
					if(ins.getControlNumber().equalsIgnoreCase("(BISIS)5929")){
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
		  for(InstitutionDTO ins:getInstitutions(parentInstitution)){
			  if(ins.isEnabledElement())
			  {
				TreeNodeDTO<InstitutionDTO> node = new TreeNodeDTO<InstitutionDTO>(ins);
				node.setParent(parentNode);
				allInstitutionsAndOrganizations.add(node);
				addNodeInstitution(node, ins);
				parentNode.addChild(node);
			  }
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
			List <TreeNodeDTO<InstitutionDTO>> tempTreeOgranizfation = getOrganizationTree(parent.getData());
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
	            	if(ou.isEnabledElement())
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
					if(ou.isEnabledElement())
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
	
	private boolean isTroggleEvent = false;
	
	public void processTreeToggle(){
		//System.out.println("RADDDDi");
		//isTroggleEvent = true;
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		String classId = facesCtx.getExternalContext()
//				.getRequestParameterMap().get("classId");
//		changeSelectionOnTreeAndrejevic(classId);
//		
//		TreeNodeDTO<ResearchAreaDTO> selectedResearchAreaDTO = null;
// 		for (TreeNodeDTO<ResearchAreaDTO> dto : allAndrejevicResearchAreaTreeNode) {
// 			if ((dto.getData().getClassId() != null)&& (dto.getData().getClassId().equalsIgnoreCase(classId))) {
// 				selectedResearchAreaDTO = dto;
// 				break;
// 			}
// 		}
// 		if (selectedResearchAreaDTO == null){
// 				return;
// 		}
// 		System.out.println(selectedResearchAreaDTO.getData().getTerm());
// 		
// 		boolean expanded = selectedResearchAreaDTO.isExpanded();
// 		System.out.println(selectedResearchAreaDTO.isExpanded());
// 		selectedResearchAreaDTO.setExpanded(!expanded);
// 		System.out.println(selectedResearchAreaDTO.isExpanded());
 		
    }

	public void selectionChanged(NodeSelectEvent selectionChangeEvent){

		treeState = (UITree) selectionChangeEvent.getSource();

		String storedKey = treeState.getRowKey();
		treeState.setRowKey(selectionChangeEvent.getTreeNode().getRowKey());

		if(((TreeNodeDTO<InstitutionDTO>)treeState.getRowNode()).getData()!=null)
			changeSelectionOnTree(((TreeNodeDTO<InstitutionDTO>)treeState.getRowNode()).getData().getControlNumber());
		treeState.setRowKey(storedKey);
	}

	private UITree treeState;

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
	
	
	public void selectAndrejevicTreeTroughCommand() {
		if(isTroggleEvent == true)
		{
			isTroggleEvent=false;
			return;
		}
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String classId = facesCtx.getExternalContext()
				.getRequestParameterMap().get("classId");
		changeSelectionOnTreeAndrejevic(classId);
	}
	
	
	public void changeSelectionOnTreeAndrejevic(String classId) {
		expandAll=false;	
		//System.out.println(classId);
		TreeNodeDTO<ResearchAreaDTO> selectedResearchAreaDTO = null;
 		for (TreeNodeDTO<ResearchAreaDTO> dto : allAndrejevicResearchAreaTreeNode) {
 			if ((dto.getData().getClassId() != null)&& (dto.getData().getClassId().equalsIgnoreCase(classId))) {
 				selectedResearchAreaDTO = dto;
 				break;
 			}
 		}
 		if (selectedResearchAreaDTO == null){
 				return;
 		}
 		
 		//System.out.println(selectedResearchAreaDTO.getData().getTerm());
 		
 		boolean oldState = false;
 		oldState = selectedResearchAreaDTO.isCheckbox_state();
 		boolean newState = !oldState;
 				
 		if(newState==false)
 		{
 			selectedResearchAreaDTO.setTree_state("empty");
 			selectedResearchAreaDTO.setCheckbox_state(false);
 		}
 		else
 		{
 			selectedResearchAreaDTO.setTree_state("full");
 			selectedResearchAreaDTO.setCheckbox_state(true);
 		}

 		TreeNodeDTO<ResearchAreaDTO> parentResearchAreaTree = (TreeNodeDTO<ResearchAreaDTO>) selectedResearchAreaDTO.getParent();
 		
 		if(parentResearchAreaTree != null)
 		{
 			TreeNodeDTO<ResearchAreaDTO> parentResearchArea = parentResearchAreaTree;
 			
 			if(parentResearchArea != null){
	 			if(newState==false)
	 			{
	 				if (parentResearchArea.isCheckbox_state()){
	 					parentResearchArea.setTree_state("partial");
	 				}
	 			}
	 			else
	 			{
	 				boolean fullAll = true;
	 				
	 				List<TreeNodeDTO <ResearchAreaDTO>> children = parentResearchArea.getChildren();
	 				for(TreeNodeDTO <ResearchAreaDTO> child : children)
	 				{
	 					if(child.isCheckbox_state()==false)
	 		            {
	 		            	fullAll = false;
	 		            	break;
	 		            }
	 				}
	 				
	 		        if(fullAll==true && parentResearchArea.isCheckbox_state())
	 		        {	
	 		        	parentResearchArea.setTree_state("full");
	 		        }
	 		        else if (fullAll==false && parentResearchArea.isCheckbox_state()){
	 		        	parentResearchArea.setTree_state("partial");
	 		        }
	 		        else
	 		        {
	 		        	parentResearchArea.setTree_state("empty");
	 		        }
	 			}
 			}
 		}
 		if(selectedResearchAreaDTO.isLeaf() == true)
 		{
 			return;
 		}
 		else
 		{
 			int i=1;
 			List<TreeNodeDTO <ResearchAreaDTO>> children = selectedResearchAreaDTO.getChildren();
			for(TreeNodeDTO <ResearchAreaDTO> child : children)
			{
				changerememberMeChildsAndrejevic(child, newState,"",i);
		       	i++;
			}	
 		}
 		
// 		int i=1;
// 		
// 		Iterator<TreeNodeDTO<ResearchAreaDTO>> it = rootAndrejevicResearchArea.iterator();
//	    while (it != null && it.hasNext()) {
//	       	printChildsArea(it.next(),"",i);
//	       	i++;
//	    }
 }
	
	public void changerememberMeChildsAndrejevic(TreeNodeDTO<ResearchAreaDTO> inputTree, boolean state, String s, int broj){
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
			List<TreeNodeDTO <ResearchAreaDTO>> children = inputTree.getChildren();
			for(TreeNodeDTO <ResearchAreaDTO> child : children)
			{
				changerememberMeChildsAndrejevic(child, state, s + "." + broj, i);
				i++;
			}
		}
	}
	
	
	public void changeSelectionOnTree(String controlNumber) {
		expandAll=false;	
		TreeNodeDTO<InstitutionDTO> selectedInstitutionDTO = null;
 		for (TreeNodeDTO<InstitutionDTO> dto : allInstitutionsAndOrganizations) {
 			if ((dto.getData().getControlNumber() != null)&& (dto.getData().getControlNumber().equalsIgnoreCase(controlNumber))) {
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
		/*Zakomentariosao jer ovo ne postoji kod andrejevica
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice7")){
			//retVal.append("konferencije" + "=" + query.getInputQuery());
			retVal.append("cris.conference" + equationSymbol + query.getInputQuery());
		}
		
		else if (query.getInputQueryType().trim().equalsIgnoreCase("choice8")){
			//retVal.append("casopisi" + "=" + query.getInputQuery());
			retVal.append("cris.journal" + equationSymbol + query.getInputQuery());
		}*/
		/*else if (query.getInputQueryType().trim().equalsIgnoreCase("choice9")){
			//retVal.append("casopisi" + "=" + query.getInputQuery());
			retVal.append("cris.moograph" + equationSymbol + query.getInputQuery());
		}*/
		
		/*Promenjeni else je dole zbog Andrejevica
		else
		{	//default vrednost
			//retVal.append("autor" + "=" + query.getInputQuery() + " AND " + "prviautor" + "=" + query.getInputQuery() + " " + "AND " + "naslov" + "=" + query.getInputQuery() + " " + "AND " + "apstrakt" + "=" + query.getInputQuery() + " " + "AND " + "kljucnereci" + "=" + query.getInputQuery() + " " + "AND " +"konferencije" + "=" + query.getInputQuery() + " " + " AND " +"casopisi" + "=" + query.getInputQuery());
			retVal.append("(dc.creator" + equationSymbol + query.getInputQuery() + " OR " + "dc.title" + equationSymbol + query.getInputQuery() + " OR " + "cris.abstract" + equationSymbol + query.getInputQuery() + " OR " + "dc.subject" + equationSymbol + query.getInputQuery() + " OR " +"cris.conference" + equationSymbol + query.getInputQuery() + " OR " +"cris.journal" + equationSymbol + query.getInputQuery() + " OR " +"cris.monograph" + equationSymbol + query.getInputQuery()+ ") ");	
		}*/
		
		else
		{	//default vrednost
			//retVal.append("autor" + "=" + query.getInputQuery() + " AND " + "prviautor" + "=" + query.getInputQuery() + " " + "AND " + "naslov" + "=" + query.getInputQuery() + " " + "AND " + "apstrakt" + "=" + query.getInputQuery() + " " + "AND " + "kljucnereci" + "=" + query.getInputQuery() + " " + "AND " +"konferencije" + "=" + query.getInputQuery() + " " + " AND " +"casopisi" + "=" + query.getInputQuery());
			retVal.append("(dc.creator" + equationSymbol + query.getInputQuery() + " OR " + "dc.title" + equationSymbol + query.getInputQuery() + " OR " + "cris.abstract" + equationSymbol + query.getInputQuery() + " OR " + "dc.subject" + equationSymbol + query.getInputQuery() + ") ");	
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
		temVrednost = appendCqlAndrejevicTree(retVal, havePreviusQueries);
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
						
			retVal.append(	" (dc.source" + "=" + "ACADEMIA"+
			" AND " + "dc.source" + "=" + "1450-653X)");
			
			/*retVal.append(	"cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
							" OR " + "cris.type" + "=" + Types.FULL_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.ABSTRACT_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS+
							" OR " + "cris.type" + "=" + Types.DISCUSSION_PAPER_PROCEEDINGS);*/
			
		}
		else if (publicationType.trim().equalsIgnoreCase("choice2")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u casopisu" + " " );
			/*retVal.append(	"cris.type" + "=" + Types.JOURNAL+
							" OR " + "cris.type" + "=" + Types.PAPER_JOURNAL+
							" OR " + "cris.type" + "=" + Types.SCIENTIFIC_CRITICISM_JOURNAL+
							" OR " + "cris.type" + "=" + Types.OTHER_JOURNAL);*/
			
			retVal.append( " (dc.source" + "=" + "DISSERTATIO"+
			" AND " + "dc.source" + "=" + "0354-7671)");
			
		}
		else if (publicationType.trim().equalsIgnoreCase("choice3")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u monografiji" + " " );
			/*retVal.append(	"cris.type" + "=" + Types.MONOGRAPH+
							" OR " + "cris.type" + "=" + Types.PAPER_MONOGRAPH);*/
			
			retVal.append( " (dc.source" + "=" + "POSEBNA IZDANJA"+
			" AND " + "dc.source" + "=" + "1450-801X)");
						
		}
		else if (publicationType.trim().equalsIgnoreCase("choice4")){
			//retVal.append(" " + "tippublikacije" + "=" + "rad u monografiji" + " " );
			/*retVal.append(	"cris.type" + "=" + Types.PHD_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.PHD_ART_PROJECT+
							" OR " + "cris.type" + "=" + Types.MASTER_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.OLD_MASTER_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.BACHELOR_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT+
							" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);*/
			
			retVal.append( " (dc.source" + "=" + "ZELENA LINIJA ŽIVOTA"+
			" AND " + "dc.source" + "=" + "1450-084)");
						

			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice5")){

			retVal.append( " (dc.source" + "=" + "ZBORNICI"+
			" AND " + "dc.source" + "=" + "1451-2157)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice6")){

			
			retVal.append( " (dc.source" + "=" + "EDICIJA NAUČNICI JUGOSLAVIJE"+
			" AND " + "dc.source" + "=" + "1450-8028)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice7")){

			
			retVal.append( " (dc.source" + "=" + "INSPIRATIO"+
			" AND " + "dc.source" + "=" + "1820-3566)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice8")){

			
			retVal.append( " (dc.source" + "=" + "SPECIALIS"+
			" AND " + "dc.source" + "=" + "1452-1520)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice9")){

			
			retVal.append( " (dc.source" + "=" + "EDUCATIO"+
			" AND " + "dc.source" + "=" + "1452-242X)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice10")){
			//System.out.println("Usao u choice 10");
			
			retVal.append( " (dc.source" + "=" + "INITIUM"+
			" AND " + "dc.source" + "=" + "1821-2484)");
			
		}
		
		else if (publicationType.trim().equalsIgnoreCase("choice11")){
			//System.out.println("Usao u choice 10");
			
			retVal.append(	" (dc.publisher=andrejevic) NOT (dc.source" + "=" + "ACADEMIA"+
					" AND " + "dc.source" + "=" + "1450-653X)"+
					" NOT " + "(dc.source" + "=" + "DISSERTATIO"+
					" AND " + "dc.source" + "=" + "0354-7671)"+
					" NOT " + "(dc.source" + "=" + "POSEBNA IZDANJA"+
					" AND " + "dc.source" + "=" + "1450-801X)"+
					" NOT " + "(dc.source" + "=" + "ZELENA LINIJA ŽIVOTA"+
					" AND " + "dc.source" + "=" + "1450-084)"+
					" NOT " + "(dc.source" + "=" + "ZBORNICI"+
					" AND " + "dc.source" + "=" + "1451-2157)"+
					" NOT " + "(dc.source" + "=" + "EDICIJA NAUČNICI JUGOSLAVIJE"+
					" AND " + "dc.source" + "=" + "1450-8028)"+
					" NOT " + "(dc.source" + "=" + "INSPIRATIO"+
					" AND " + "dc.source" + "=" + "1820-3566)"+
					" NOT " + "(dc.source" + "=" + "SPECIALIS"+
					" AND " + "dc.source" + "=" + "1452-1520)"+
					" NOT " + "(dc.source" + "=" + "EDUCATIO"+
					" AND " + "dc.source" + "=" + "1452-242X)"+
					" NOT " + "(dc.source" + "=" + "INITIUM"+
					" AND " + "dc.source" + "=" + "1821-2484)");
			
		}
		
		
		else
		{	//default
			//retVal.append(" tippublikacije=rad u zborniku radova AND tippublikacije=rad u casopisu AND tippublikacije=rad u monografiji");
			/*retVal.append("cris.type" + "=" + Types.PAPER_PROCEEDINGS+ 
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
					" OR " + "cris.type" + "=" + Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT);*/
			
			retVal.append(	" (dc.source" + "=" + "ACADEMIA"+
					" AND " + "dc.source" + "=" + "1450-653X)"+
					" OR " + "(dc.source" + "=" + "DISSERTATIO"+
					" AND " + "dc.source" + "=" + "0354-7671)"+
					" OR " + "(dc.source" + "=" + "POSEBNA IZDANJA"+
					" AND " + "dc.source" + "=" + "1450-801X)"+
					" OR " + "(dc.source" + "=" + "ZELENA LINIJA ŽIVOTA"+
					" AND " + "dc.source" + "=" + "1450-084)"+
					" OR " + "(dc.source" + "=" + "ZBORNICI"+
					" AND " + "dc.source" + "=" + "1451-2157)"+
					" OR " + "(dc.source" + "=" + "EDICIJA NAUČNICI JUGOSLAVIJE"+
					" AND " + "dc.source" + "=" + "1450-8028)"+
					" OR " + "(dc.source" + "=" + "INSPIRATIO"+
					" AND " + "dc.source" + "=" + "1820-3566)"+
					" OR " + "(dc.source" + "=" + "SPECIALIS"+
					" AND " + "dc.source" + "=" + "1452-1520)"+
					" OR " + "(dc.source" + "=" + "EDUCATIO"+
					" AND " + "dc.source" + "=" + "1452-242X)"+
					" OR " + "(dc.source" + "=" + "INITIUM"+
					" AND " + "dc.source" + "=" + "1821-2484)");
		}
		
		retVal.append(") ");
		havePreviusQueries =true;
		
		return true;
	}
	
	
	public boolean appendCqlAndrejevicTree(StringBuffer retVal, boolean havePreviusQueries)
	{
		int numberSelected = 0;
		
		
		for (TreeNodeDTO<ResearchAreaDTO> dto : allAndrejevicResearchAreaTreeNode){
			boolean foundScientificArea = false;
			if (dto.getData().getClassId().equalsIgnoreCase("andOb4") || dto.getData().getClassId().equalsIgnoreCase("andOb5") || dto.getData().getClassId().equalsIgnoreCase("andOb6")
					|| dto.getData().getClassId().equalsIgnoreCase("andOb7") || dto.getData().getClassId().equalsIgnoreCase("andOb8") || dto.getData().getClassId().equalsIgnoreCase("andOb10")
					|| dto.getData().getClassId().equalsIgnoreCase("andOb11") || dto.getData().getClassId().equalsIgnoreCase("andOb12"))
			foundScientificArea = true;	
			if (dto.isCheckbox_state() && ( !(foundScientificArea))) {
				numberSelected = numberSelected+1;
			}
		}
		if(numberSelected>0)
		{
			//System.out.println("Ima vise od jednog nublerSelected "+ numberSelected);
		
			if(havePreviusQueries==true){
				retVal.append(" AND ");
			}
			
			retVal.append("( ");
			
		
			
			boolean foundFirst = false;
			for (int i = 0; i<allAndrejevicResearchAreaTreeNode.size(); i++)
			{
				TreeNodeDTO<ResearchAreaDTO> dto = allAndrejevicResearchAreaTreeNode.get(i);
				boolean foundScientificArea = false;
				if (dto.getData().getClassId().equalsIgnoreCase("andOb4") || dto.getData().getClassId().equalsIgnoreCase("andOb5") || dto.getData().getClassId().equalsIgnoreCase("andOb6")
						|| dto.getData().getClassId().equalsIgnoreCase("andOb7") || dto.getData().getClassId().equalsIgnoreCase("andOb8") || dto.getData().getClassId().equalsIgnoreCase("andOb10")
						|| dto.getData().getClassId().equalsIgnoreCase("andOb11") || dto.getData().getClassId().equalsIgnoreCase("andOb12"))
				foundScientificArea = true;	
				if ((dto.isCheckbox_state()) && (!(foundScientificArea))) {
					if(foundFirst == true)
						retVal.append(" OR ");
	 				retVal.append("cris.researchArea adj \"" + dto.getData().getClassId()+"\"");
	 				foundFirst = true;
	 			}
			}
			retVal.append(" )");
			
			if(numberSelected>0 )
				havePreviusQueries=true;
		}
		else
		{
			if(havePreviusQueries==true){
				retVal.append(" AND ");
			}
			
			retVal.append("( ");
			
			boolean foundFirst = false;
			for (int i = 0; i<allAndrejevicResearchAreaTreeNode.size(); i++)
			{
				TreeNodeDTO<ResearchAreaDTO> dto = allAndrejevicResearchAreaTreeNode.get(i);
				boolean foundScientificArea = false;
				if (dto.getData().getClassId().equalsIgnoreCase("andOb4") || dto.getData().getClassId().equalsIgnoreCase("andOb5") || dto.getData().getClassId().equalsIgnoreCase("andOb6")
						|| dto.getData().getClassId().equalsIgnoreCase("andOb7") || dto.getData().getClassId().equalsIgnoreCase("andOb8") || dto.getData().getClassId().equalsIgnoreCase("andOb10")
						|| dto.getData().getClassId().equalsIgnoreCase("andOb11") || dto.getData().getClassId().equalsIgnoreCase("andOb12"))
				foundScientificArea = true;	
				
				//System.out.println("Ima vise od jednog nublerSelected "+ foundScientificArea + foundFirst);
				/*if(dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)5929")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6782")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6887")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6888")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6889")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6890")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6891")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6892")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6893")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6894")
						||dto.getData().getControlNumber().equalsIgnoreCase("(BISIS)6895"))
				{*/
				
					
						if (foundScientificArea)
							continue;
						if(foundFirst == true)
						retVal.append(" OR ");
	 				retVal.append(" cris.researchArea adj \"" + dto.getData().getClassId()+"\"");
	 				foundFirst = true;
					
					
					
				//}
			}
			retVal.append(" )");
		}
		
		if(havePreviusQueries==true){
			retVal.append(" AND ");
		}
		
		retVal.append("( dc.publisher=andrejevic AND cris.type=monograph)");
		
		return havePreviusQueries;
	}
	
	
	public boolean appendCqlTree(StringBuffer retVal, boolean havePreviusQueries)
	{
		/*int numberSelected = 0;
		
		
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
				if(dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)5929")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6782")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6887")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6888")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6889")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6890")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6891")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6892")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6893")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6894")
						||dto.getElement().getControlNumber().equalsIgnoreCase("(BISIS)6895"))
				{
					if(foundFirst == true)
						retVal.append(" OR ");
	 				retVal.append("dc.contributor adj \"" + dto.getElement().getSomeName()+"\"");
	 				foundFirst = true;
				}
			}
			retVal.append(" )");
		}*/
		
		if(havePreviusQueries==true){
			retVal.append(" AND ");
		}
		
		retVal.append("( dc.publisher=andrejevic )");
		
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
} 
