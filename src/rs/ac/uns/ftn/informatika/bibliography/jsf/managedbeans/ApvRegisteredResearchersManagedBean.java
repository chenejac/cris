package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.sql.DataSource;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger.OutputField;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.primefaces.component.datatable.DataTable;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.UserDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationSelectionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.AbstractCommissionEvaluation;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommissionFactory;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.ReportsMSWord;
import rs.ac.uns.ftn.informatika.bibliography.reports.ReportsServlet;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.KnrDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultsGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;


@SuppressWarnings("serial")
public class ApvRegisteredResearchersManagedBean extends CRUDManagedBean {

	/*ApvRegisteredResearchersManagedBean(){
		//iscitivanje korisnika i njihovih BISISS u posebnu listu
	}*/
	
	protected List<AuthorDTO> list;
	protected UserDAO userDAO = new UserDAO();
	protected AuthorDTO selectedAuthor = null;
	protected OrganizationUnitDTO selectedAuthorOrgUnit = null;
	
	protected String OUCN = null;
	protected String INCN = null;
	
	protected Query OUCNQuery = null;
	protected Query INCNQuery = null;
	
	protected KnrDTO selectedAuthorKNR;
	
	protected String filterAPVNT = "";
	protected String filterFirstname = "";
	protected String filterLastname = "";
	
	private String pdfUrl = null;
	
	protected String fromDate = "";
	protected String toDate = "";
	
	private DataSource dataSource = null;
	
	private String authorControlNumber;
	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	
	public ApvRegisteredResearchersManagedBean(){
		//pickSimilarMessage = "records.author.pickSimilarMessage";
		tableModalPanel = "apvRegisteredResearchersTableFormPanel";
		editModalPanel = "apvRegisteredResearcherViewDetailsModalPanel";
		dataSource = DataSourceFactory.getDataSource();
	}
	@Override
	public String resetForm() {
		selectedAuthor = null;
		fromDate = "";
		toDate = "";
		selectedAuthorKNR = null;
		authorControlNumber = null;
		tableModalPanel = "apvRegisteredResearchersTableFormPanel";
		editModalPanel = "apvRegisteredResearcherViewDetailsModalPanel";
		return super.resetForm();
	}
	
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "apvRegisteredResearchersPage";
		return retVal;
	}
	
	
	public String apvRegisteredResearchersPageEnter() {
		super.enterCRUDPage();
		returnPage = "indexPage";
		return "apvRegisteredResearchersPage";
	}
	
	public void enterPage(PhaseEvent event){
		String departmentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("department");
		String institutionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("institution");
		if((init == false) || (departmentId != null) || (institutionId != null)){
			OUCN = departmentId;
			INCN = institutionId;
			if(OUCN != null){
				OUCNQuery = personDAO.getInstitutionRecordsQuery(OUCN, "2021-01-01 00:00:00");
			} else 
				OUCNQuery = null;
			if(INCN != null){
				INCNQuery = personDAO.getInstitutionRecordsQuery(INCN, "2021-01-01 00:00:00");
			} else 
				INCNQuery = null;
			enterCRUDPage();
			init = true;
		}
		
		String apvn = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("apvn");
		if(apvn != null){
			selectedAuthor = findAuthorByApvn(apvn);
			if (selectedAuthor != null) {			
				retrieveSelectedAuthorKNR();			
				super.switchToTableNoneMode();
				super.switchToViewDetailsMode();		
				showList = new HashMap<String, Boolean>();
				for(ResultsGroupDTO res : selectedAuthorKNR.getResultsGroups()){			
					showList.put(res.getResultType().getClassId(), false); 
				}
				debug("switchToViewDetailsModeFromKnr: \n" + selectedAuthor);
			}
		}	 
	}
	
	@Override
	public void switchToBrowseMode() {
		selectedAuthor = null;
		fromDate = "";
		toDate = "";
		super.switchToBrowseMode();
	}
	private AuthorDTO findAuthorByApvn(String apvn) {
		AuthorDTO retVal = null;
		Query query = new TermQuery(new Term("APVNT", apvn));
		List<Record> results = this.personDAO.getDTOs(query, new TopDocCollector(1));
		if((results != null) && (results.size() > 0))
			retVal = (AuthorDTO) results.get(0).getDto();
		return retVal;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String finishWizard() {
		// TODO Auto-generated method stub
		return null;
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
		/*	
			orderByList.add("controlNumber");
			directionsList.add("asc");
			*/
			Collections.sort(retVal, new GenericComparator<AuthorDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	

	/**
	 * Creates query.
	 * 
	 * @return the created query
	 * @throws ParseException 
	 */
	protected Query createQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if(OUCNQuery != null){
			bq.add(OUCNQuery, Occur.MUST);
		}
		else if(INCNQuery != null){
			bq.add(INCNQuery, Occur.MUST);
		} else {
			RangeQuery apvnt = new RangeQuery(new Term("APVNT", "1"), new Term("APVNT", "9999"), true);
			bq.add(apvnt, Occur.MUST);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		if(filterFirstname!=null && !filterFirstname.equals("")){			
			BooleanQuery firstNameTerm = QueryUtils.makeBooleanQuery("FN", filterFirstname.toLowerCase()+"*", Occur.MUST, (float)1.0, (float)0.0, false);
			bq.add(firstNameTerm, Occur.MUST);
		
		}
		if(filterLastname!=null && !filterLastname.equals("")){			
			BooleanQuery lastNameTerm = QueryUtils.makeBooleanQuery("LN", filterLastname.toLowerCase()+"*", Occur.MUST, (float)1.0, (float)0.0, false);
			bq.add(lastNameTerm, Occur.MUST);
			
		}
		if(filterAPVNT!=null && !filterAPVNT.equals("")){		
			BooleanQuery apvntNameTerm = QueryUtils.makeBooleanQuery("APVNT", filterAPVNT.toLowerCase()+"*", Occur.MUST, (float)1.0, (float)0.0, false);
			bq.add(apvntNameTerm, Occur.MUST);
		}
			
		
		return bq;
	}
	
//	private Query createOrgUnitQuery() {
//		BooleanQuery bq = new BooleanQuery();
//		bq.add(new TermQuery(new Term("TYPE", Types.ORGANIZATION_UNIT)), Occur.MUST);
//		bq.add(new TermQuery(new Term("OUCN", OUCN)), Occur.MUST);
//		RecordDAO recordDAO = new RecordDAO(new RecordDB());
//		List<Record> chairs = recordDAO.getDTOs(bq, new AllDocCollector(false));
//		BooleanQuery retVal = new BooleanQuery();
//		retVal.add(new TermQuery(new Term("OUCN", OUCN)), Occur.SHOULD);
//		for (Record record : chairs) {
//			retVal.add(new TermQuery(new Term("OUCN", record.getControlNumber())), Occur.SHOULD);
//		}
//		return retVal;
//	}
//	
//	private Query createInsQuery() {
//		BooleanQuery bq = new BooleanQuery();
//		bq.add(new TermQuery(new Term("TYPE", Types.INSTITUTION)), Occur.MUST);
//		bq.add(new TermQuery(new Term("INCN", INCN)), Occur.MUST);
//		RecordDAO recordDAO = new RecordDAO(new RecordDB());
//		List<Record> subinstitutions = recordDAO.getDTOs(bq, new AllDocCollector(false));
//		BooleanQuery retVal = new BooleanQuery();
//		retVal.add(new TermQuery(new Term("INCN", INCN)), Occur.SHOULD);
//		for (Record record : subinstitutions) {
//			retVal.add(new TermQuery(new Term("INCN", record.getControlNumber())), Occur.SHOULD);
//		}
//		return retVal;
//	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList(){
		try {
			debug("populateList");			
			setOrderBy("names");
			setDirection("asc");
			List<AuthorDTO> listTmp = getAuthors(createQuery(),
						orderBy, direction, new AllDocCollector((orderList == false)&&(orderBy==null)));
			
			
					
			if (init == true && listTmp.size() != 0 && selectedAuthor != null
					&& selectedAuthor.getControlNumber() != null) {
	
				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedAuthor.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					DataTable table = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("apvRegistredResearchersTable");
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
			list = new ArrayList<AuthorDTO>();
		}
	}
	
	public List<AuthorDTO> getAuthors() {
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		if(populateList){
			populateList();
			populateList = false;
		}
		if(orderList == true){
			orderList = false;			
			if ((orderBy != null) && (!"".equals(orderBy))) {
				if (direction != null) {
					orderByList.add(orderBy);
					directionsList.add(direction);
				}
			}
		}else{/*
			orderByList.add("name.lastname");
			directionsList.add("asc");
			Collections.sort(list, new GenericComparator<AuthorDTO>(
					orderByList, directionsList));
					*/
		}
		return list;
	}
	
	public void btnFind(){	
		populateList();
	}
	
	public void btnShowAll(){
		filterAPVNT="";
		filterLastname="";
		filterFirstname="";
		orderList = true;
		populateList();
	}
	
	
	public void switchToViewDetailsMode() {
		selectedAuthor = findAuthorByControlNumber(list);	
		if (selectedAuthor != null) {			
			retrieveSelectedAuthorKNR();			
			super.switchToTableNoneMode();
			super.switchToViewDetailsMode();		
			showList = new HashMap<String, Boolean>();
			for(ResultsGroupDTO res : selectedAuthorKNR.getResultsGroups()){			
				showList.put(res.getResultType().getClassId(), false); 
			}
			debug("switchToViewDetailsMode: \n" + selectedAuthor);
		}
	}
	
	/**
	 * bojana
	 */
	
	public void retrieveSelectedAuthorKNR(){
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();	
		EvaluationManagedBean emb = (EvaluationManagedBean) extCtx.getSessionMap().get(
			"evaluationManagedBean");
		if (emb == null) {
			emb = new EvaluationManagedBean();
			extCtx.getSessionMap().put("evaluationManagedBean", emb);
		}
		if(selectedAuthor!=null){
			emb.setCurrentAuthor(selectedAuthor);
			emb.loadRuleBook();
			emb.loadCommission();
			emb.populateMap();	
		}	
		int fromDateInt = 1950;
		try{
			fromDateInt =  Integer.parseInt(fromDate);
		} catch (Exception ex){
		}
		int toDateInt = Calendar.getInstance().get(Calendar.YEAR);
		try{
			toDateInt =  Integer.parseInt(toDate);
		} catch (Exception ex){
		}
		HashMap<ResultMeasureDTO, List<PublicationDTO>> allResults = emb.getEvaluatedResults();		
		List<ResultsGroupDTO> selectedAuthorResultsGroups = new ArrayList<ResultsGroupDTO>();
		for (ResultMeasureDTO resultMeasureDTO : allResults.keySet()) {
			if(resultMeasureDTO.getResultType().getClassId().equals("M99"))
				continue;	
			List<PublicationDTO> publications = allResults.get(resultMeasureDTO);
			List<ResultDTO> results = new ArrayList<ResultDTO>();
			for (PublicationDTO publicationDTO : publications) {
				int publicationYear=0;
				try{
					publicationYear = Integer.parseInt(publicationDTO.getPublicationYear());
				} catch (Exception ex){
					continue;
				}
				if((toDateInt >= publicationYear) && (fromDateInt <= publicationYear))
					results.add(new ResultDTO(resultMeasureDTO.getResultType(), publicationDTO));
			}
			ResultsGroupDTO rtsDTO = new ResultsGroupDTO(resultMeasureDTO, results);
			if(results.size() > 0){
				selectedAuthorResultsGroups.add(rtsDTO);
			}
		}	
		Collections.sort(selectedAuthorResultsGroups, new GenericComparator<ResultsGroupDTO>(
				"resultType.classId", "asc"));
		selectedAuthorKNR = new KnrDTO();
		selectedAuthorKNR.setResultsGroups(selectedAuthorResultsGroups);
		selectedAuthorKNR.setResearcher(selectedAuthor);
		// orgUtnit
		selectedAuthorOrgUnit = selectedAuthor.getOrganizationUnit();
		selectedAuthorOrgUnit.getRecord().loadFromDatabase();
		  while (selectedAuthorOrgUnit.getSuperOrganizationUnit() != null){
		  	selectedAuthorOrgUnit = selectedAuthorOrgUnit.getSuperOrganizationUnit();
		  	selectedAuthorOrgUnit.getRecord().loadFromDatabase();
		  } 
		 
		  
	    if(emb.getCommission().getCommissionId()!=null){
			pdfUrl = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.rulebook.id"+emb.getCommission().getCommissionId());
	    }
		else
			pdfUrl = null;
					
	}
	
	public String calculateAgain(){
		retrieveSelectedAuthorKNR();
		return null;
	}
	
	

	public void setAuthorControlNumber(String controlNumber){
		authorControlNumber=controlNumber;
		selectedAuthor = (AuthorDTO)personDAO.getDTO(controlNumber);
	}
	
	public String getAuthorControlNumber(){
		return authorControlNumber;
	}
	
	
	protected AuthorDTO findAuthorByControlNumber(List<AuthorDTO> authorsList) {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = (authorControlNumber!=null)?authorControlNumber:facesCtx.getExternalContext().getRequestParameterMap().get("controlNumber");
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
	}

	public void setList(AuthorDTO authorDTO){
		selectedAuthor = authorDTO;
		list = new ArrayList<AuthorDTO>();
		list.add(selectedAuthor);
		authorControlNumber = selectedAuthor.getControlNumber();
	}

	public void selectResearcherProfile(AuthorDTO authorDTO) {
		setList(authorDTO);
		retrieveSelectedAuthorKNR();
		switchToTableNoneMode();
		switchToViewDetailsMode();
		showList = new HashMap<String, Boolean>();
		for(ResultsGroupDTO res : selectedAuthorKNR.getResultsGroups()){
			showList.put(res.getResultType().getClassId(), false);
		}
		debug("switchToViewDetailsModeFromKnr: \n" + selectedAuthor);
	}
	
	/**
	 * @return the selectedAuthorKNR
	 */
	public KnrDTO getSelectedAuthorKNR() {
		return selectedAuthorKNR;
	}
	/**
	 * @param selectedAuthorKNR the selectedAuthorKNR to set
	 */
	public void setSelectedAuthorKNR(KnrDTO selectedAuthorKNR) {
		this.selectedAuthorKNR = selectedAuthorKNR;		
	}
	
	protected Boolean show = new Boolean(true);
	
	protected Map<String,Boolean> showList = new HashMap<String, Boolean>();

	/**
	 * @return the show
	 */
	public Boolean getShow() {
		return show;
	}
	/**
	 * @param show the show to set
	 */
	public void setShow(Boolean show) {
		this.show = show;
	}
	/**
	 * @return the showList
	 */
	public Map<String, Boolean> getShowList() {
		return showList;
	}
	/**
	 * @param showList the showList to set
	 */
	public void setShowList(Map<String, Boolean> showList) {
		this.showList = showList;
	}
	/**
	 * @return the filterAPVNT
	 */
	public String getFilterAPVNT() {
		return filterAPVNT;
	}
	/**
	 * @param filterAPVNT the filterAPVNT to set
	 */
	public void setFilterAPVNT(String filterAPVNT) {
		this.filterAPVNT = filterAPVNT;
	}
	/**
	 * @return the filterFirstname
	 */
	public String getFilterFirstname() {
		return filterFirstname;
	}
	/**
	 * @param filterFirstname the filterFirstname to set
	 */
	public void setFilterFirstname(String filterFirstname) {
		this.filterFirstname = filterFirstname;
	}
	/**
	 * @return the filterLastname
	 */
	public String getFilterLastname() {
		return filterLastname;
	}
	/**
	 * @param filterLastname the filterLastname to set
	 */
	public void setFilterLastname(String filterLastname) {
		this.filterLastname = filterLastname;
	}
	/**
	 * @return the selectedAuthorOrgUnit
	 */
	public OrganizationUnitDTO getSelectedAuthorOrgUnit() {
		return selectedAuthorOrgUnit;
	}
	/**
	 * @param selectedAuthorOrgUnit the selectedAuthorOrgUnit to set
	 */
	public void setSelectedAuthorOrgUnit(OrganizationUnitDTO selectedAuthorOrgUnit) {
		this.selectedAuthorOrgUnit = selectedAuthorOrgUnit;
	}
	/**
	 * @return the pdfUrl
	 */
	public String getPdfUrl() {
		return pdfUrl;
	}
	/**
	 * @param pdfUrl the pdfUrl to set
	 */
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
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
	
	public void prepareReportGeneration(){
		wordMLPackage = null;
		publications = new ArrayList<PublicationSelectionDTO>();
		for (ResultsGroupDTO resultGroupDTO : selectedAuthorKNR.getResultsGroups()) {
			for (ResultDTO result : resultGroupDTO.getResults()) {
				publications.add(new PublicationSelectionDTO(result.getPublication(), true, resultGroupDTO));
			}
		}
	}
	
	public void exitPublications(){
		publications = null;
	}
	
	/**
	 * @return the publications
	 */
	public List<PublicationSelectionDTO> getPublications() {
		return publications;
	}
	/**
	 * @param publications the publications to set
	 */
	public void setPublications(List<PublicationSelectionDTO> publications) {
		this.publications = publications;
	}

	protected List<PublicationSelectionDTO> publications = null;
	
	public void selectAllPublications() {
		for (PublicationSelectionDTO publicationSelection : publications) {
			publicationSelection.setSelected(true);
		}
	}
	
	public void deselectAllPublications() {
		for (PublicationSelectionDTO publicationSelection : publications) {
			publicationSelection.setSelected(false);
		}
	}
	
	WordprocessingMLPackage wordMLPackage = null;
	
	public void generateReport() {
		Connection conn = null;
		try {
			String docxFile = ReportsServlet.subreportDir + "izborUZvanje.docx";
	
		    wordMLPackage = WordprocessingMLPackage.load(new java.io.File(docxFile));
	
	//	    List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();
	
		    Map<DataFieldName, String> item = new HashMap<DataFieldName, String>();
		    Map<String, List<Map<String, String>>> ref = new HashMap<String, List<Map<String, String>>>();
		
		    reportInitialization(item, ref);
		    String name = selectedAuthorKNR.getResearcher().getName().getFirstname() + " " + selectedAuthorKNR.getResearcher().getName().getLastname();
		    item.put(new DataFieldName("name"), name);	    
		    
		    DecimalFormat df = new DecimalFormat("0.##");
		    double totalPoints = 0.0;
		    
		    conn = dataSource.getConnection();
		    
		    for (PublicationSelectionDTO publication : publications) {
		    	if(publication.isSelected()){
			    	int number = 0;
			    	double points = 0.0;
					String category = publication.getResultEvaluation().getResultType().getClassId();
					if(item.get(new DataFieldName(category + "number")) != null){
						number = Integer.parseInt(item.get(new DataFieldName(category + "number")));
					}
					if(item.get(new DataFieldName(category + "totalPoints")) != null){
						points = Double.parseDouble(item.get(new DataFieldName(category + "totalPoints")));
					}
					number ++;
					points += publication.getResultEvaluation().getQuantitativeMeasure().doubleValue();
					totalPoints += publication.getResultEvaluation().getQuantitativeMeasure();
					item.put(new DataFieldName(category + "number"), "" + number);
					item.put(new DataFieldName(category + "totalPoints"), df.format(points));
					String tempCategory = category;
					if((category.equals("M12")) || (category.equals("M13")) || (category.equals("M14"))){
						tempCategory = "M11";
					}
					if((category.equals("M16")) || (category.equals("M17")) || (category.equals("M18"))){
						tempCategory = "M15";
					}
					if((category.equals("M26")) || (category.equals("M27")) || (category.equals("M28"))){
						tempCategory = "M25";
					}
					if(category.startsWith("M3")){
						tempCategory = "M31";
					}
					if(category.startsWith("M4")){
						tempCategory = "M41";
					}
					if(category.equals("M56")){
						tempCategory = "M55";
					}
					if(category.startsWith("M6")){
						tempCategory = "M61";
					}
					if(category.startsWith("M7")){
						tempCategory = "M71";
					}
					if(category.startsWith("M8")){
						tempCategory = "M81";
					}
					if(category.startsWith("M9")){
						tempCategory = "M91";
					}
					if(ref.get(tempCategory) == null){
						ref.put(tempCategory, new ArrayList<Map<String, String>>());
					}
					ArrayList<Map<String, String>> tempRef = (ArrayList<Map<String, String>>) ref.get(tempCategory);
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("SJ_" + tempCategory + "RB", "" + (tempRef.size() + 1));
					String reference = publication.getPublication().getHarvardRepresentation();
					JournalEvaluationResult evaluatedResult = null;
					if(category.startsWith("M2")){
						try {
							MetricsDB metricsDB = new MetricsDB();
							JournalDTO journal = ((PaperJournalDTO)publication.getPublication()).getJournal();
							List<ImpactFactor> allImpactFactors = metricsDB.getJournalImpactFactors(conn, journal.getControlNumber(), Arrays.asList(new String[]{"twoYearsIF", "fiveYearsIF"}));
							Integer publicationYear = Integer.parseInt(publication.getPublication().getPublicationYear());
							JournalEval journalEval= new JournalEval(journal.getControlNumber(), journal.getSomeName(), journal.getIssn(), allImpactFactors, publicationYear);
							AbstractCommissionEvaluation absCommission = CommissionFactory.getInstance().getCommissionEvaluation(publication.getResultEvaluation().getCommissionDTO().getCommissionId());
							HashMap<Integer, JournalEvaluationResult> results = absCommission.getJournalEvaluations(journalEval);
							
							if(results.get(publicationYear) != null)
								evaluatedResult = results.get(publicationYear);
							
							if(evaluatedResult != null){
								String evRes = getEvaluatedResultAsString(evaluatedResult);
								if(evRes != null)
									reference += "<br/><br/> (" + evRes + ")";
							}
						} catch (Throwable e){
							log.warn("Can't calculate explanation for M20 category: " + e.getMessage());
						} 
	
					}
					tempMap.put("SJ_" + tempCategory + "Ref", "" + reference.replace("&nbsp;", " ").replace("&", "&amp;"));
					tempMap.put("SJ_" + tempCategory + "C", "" + publication.getResultEvaluation().getResultType().getClassId());
					tempRef.add(tempMap);
		    	}
		    	publication.getPublication().setNotLoaded(true);
			}
		    item.put(new DataFieldName("totalPoints"), df.format(totalPoints));
	//	    data.add(item);
	
		    org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);
	
		    org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, item, true);
		    
		    for (String tempCategory : ref.keySet()) {
		    	ReportsMSWord.replaceTable(new String[]{"SJ_" + tempCategory + "RB","SJ_" + tempCategory + "Ref","SJ_" + tempCategory + "C"}, ref.get(tempCategory), wordMLPackage, "font-size:11pt;");
		    }

	//	    wordMLPackage.save(new java.io.File("E:/OUT.docx"));
	//	    getReport();
		} catch (Throwable e){
			log.error("Can't generate report for promotion: " + e.getMessage());
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				
			}
		}
	}
	
	public String getEvaluatedResultAsString(JournalEvaluationResult evaluatedResult) {
		String retVal = null;
		ResearchAreaRanking ra = evaluatedResult.getImpactFactor().getMaxPositionReseachArea(true, true);
		long round  = Math.round(ra.getPosition()/ra.getDividend());
		String vrednostKategorije = ra.getPosition().intValue() + "/" + round;
		retVal = "<b>" + facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.year") + ":</b> " + evaluatedResult.getImpactFactor().getYear()+ "; " + 
			"<b>" +facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.category") + ":</b> " +evaluatedResult.getCategory() + "; " + 
			"<b>" +facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.researchArea") + ":</b> " +evaluatedResult.getImpactFactor().getMaxPositionReseachArea(true, true).getResearchAreaDTO().getSomeTerm()+ "; " +
			"<b>" +facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.position") + ":</b> " +vrednostKategorije + "; " +
			"<b>" +facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.impactFactor") + ":</b> " +evaluatedResult.getImpactFactor().getValueOfImpactFactorForMaxResearchArea(true, true);
		return retVal;
	}
	
	
	private void reportInitialization(Map<DataFieldName, String> item,
			Map<String, List<Map<String, String>>> ref) {
		ref.put("M11", new ArrayList<Map<String, String>>());
	    ref.put("M15", new ArrayList<Map<String, String>>());
	    ref.put("M21a", new ArrayList<Map<String, String>>());
	    ref.put("M21", new ArrayList<Map<String, String>>());
	    ref.put("M22", new ArrayList<Map<String, String>>());
	    ref.put("M23", new ArrayList<Map<String, String>>());
	    ref.put("M24", new ArrayList<Map<String, String>>());
	    ref.put("M25", new ArrayList<Map<String, String>>());
	    ref.put("M31", new ArrayList<Map<String, String>>());
	    ref.put("M41", new ArrayList<Map<String, String>>());
	    ref.put("M51", new ArrayList<Map<String, String>>());
	    ref.put("M52", new ArrayList<Map<String, String>>());
	    ref.put("M53", new ArrayList<Map<String, String>>());
	    ref.put("M55", new ArrayList<Map<String, String>>());
	    ref.put("M61", new ArrayList<Map<String, String>>());
	    ref.put("M71", new ArrayList<Map<String, String>>());
	    ref.put("M81", new ArrayList<Map<String, String>>());
	    ref.put("M91", new ArrayList<Map<String, String>>());
	    
	    item.put(new DataFieldName("M11number"), "0");
	    item.put(new DataFieldName("M11totalPoints"), "0");
	    item.put(new DataFieldName("M12number"), "0");
	    item.put(new DataFieldName("M12totalPoints"), "0");
	    item.put(new DataFieldName("M13number"), "0");
	    item.put(new DataFieldName("M13totalPoints"), "0");
	    item.put(new DataFieldName("M14number"), "0");
	    item.put(new DataFieldName("M14totalPoints"), "0");
	    item.put(new DataFieldName("M15number"), "0");
	    item.put(new DataFieldName("M15totalPoints"), "0");
	    item.put(new DataFieldName("M16number"), "0");
	    item.put(new DataFieldName("M16totalPoints"), "0");
	    item.put(new DataFieldName("M17number"), "0");
	    item.put(new DataFieldName("M17totalPoints"), "0");
	    item.put(new DataFieldName("M18number"), "0");
	    item.put(new DataFieldName("M18totalPoints"), "0");
	    item.put(new DataFieldName("M21anumber"), "0");
	    item.put(new DataFieldName("M21atotalPoints"), "0");
	    item.put(new DataFieldName("M21number"), "0");
	    item.put(new DataFieldName("M21totalPoints"), "0");
	    item.put(new DataFieldName("M22number"), "0");
	    item.put(new DataFieldName("M22totalPoints"), "0");
	    item.put(new DataFieldName("M23number"), "0");
	    item.put(new DataFieldName("M23totalPoints"), "0");
	    item.put(new DataFieldName("M24number"), "0");
	    item.put(new DataFieldName("M24totalPoints"), "0");
	    item.put(new DataFieldName("M25number"), "0");
	    item.put(new DataFieldName("M25totalPoints"), "0");
	    item.put(new DataFieldName("M26number"), "0");
	    item.put(new DataFieldName("M26totalPoints"), "0");
	    item.put(new DataFieldName("M27number"), "0");
	    item.put(new DataFieldName("M27totalPoints"), "0");
	    item.put(new DataFieldName("M28number"), "0");
	    item.put(new DataFieldName("M28totalPoints"), "0");
	    item.put(new DataFieldName("M31number"), "0");
	    item.put(new DataFieldName("M31totalPoints"), "0");
	    item.put(new DataFieldName("M32number"), "0");
	    item.put(new DataFieldName("M32totalPoints"), "0");
	    item.put(new DataFieldName("M33number"), "0");
	    item.put(new DataFieldName("M33totalPoints"), "0");
	    item.put(new DataFieldName("M34number"), "0");
	    item.put(new DataFieldName("M34totalPoints"), "0");
	    item.put(new DataFieldName("M35number"), "0");
	    item.put(new DataFieldName("M35totalPoints"), "0");
	    item.put(new DataFieldName("M36number"), "0");
	    item.put(new DataFieldName("M36totalPoints"), "0");
	    item.put(new DataFieldName("M41number"), "0");
	    item.put(new DataFieldName("M41totalPoints"), "0");
	    item.put(new DataFieldName("M42number"), "0");
	    item.put(new DataFieldName("M42totalPoints"), "0");
	    item.put(new DataFieldName("M43number"), "0");
	    item.put(new DataFieldName("M43totalPoints"), "0");
	    item.put(new DataFieldName("M44number"), "0");
	    item.put(new DataFieldName("M44totalPoints"), "0");
	    item.put(new DataFieldName("M45number"), "0");
	    item.put(new DataFieldName("M45totalPoints"), "0");
	    item.put(new DataFieldName("M46number"), "0");
	    item.put(new DataFieldName("M46totalPoints"), "0");
	    item.put(new DataFieldName("M47number"), "0");
	    item.put(new DataFieldName("M47totalPoints"), "0");
	    item.put(new DataFieldName("M48number"), "0");
	    item.put(new DataFieldName("M48totalPoints"), "0");
	    item.put(new DataFieldName("M49number"), "0");
	    item.put(new DataFieldName("M49totalPoints"), "0");
	    item.put(new DataFieldName("M51number"), "0");
	    item.put(new DataFieldName("M51totalPoints"), "0");
	    item.put(new DataFieldName("M52number"), "0");
	    item.put(new DataFieldName("M52totalPoints"), "0");
	    item.put(new DataFieldName("M53number"), "0");
	    item.put(new DataFieldName("M53totalPoints"), "0");
	    item.put(new DataFieldName("M55number"), "0");
	    item.put(new DataFieldName("M55totalPoints"), "0");
	    item.put(new DataFieldName("M56number"), "0");
	    item.put(new DataFieldName("M56totalPoints"), "0");
	    item.put(new DataFieldName("M61number"), "0");
	    item.put(new DataFieldName("M61totalPoints"), "0");
	    item.put(new DataFieldName("M62number"), "0");
	    item.put(new DataFieldName("M62totalPoints"), "0");
	    item.put(new DataFieldName("M63number"), "0");
	    item.put(new DataFieldName("M63totalPoints"), "0");
	    item.put(new DataFieldName("M64number"), "0");
	    item.put(new DataFieldName("M64totalPoints"), "0");
	    item.put(new DataFieldName("M65number"), "0");
	    item.put(new DataFieldName("M65totalPoints"), "0");
	    item.put(new DataFieldName("M66number"), "0");
	    item.put(new DataFieldName("M66totalPoints"), "0");
	    item.put(new DataFieldName("M71number"), "0");
	    item.put(new DataFieldName("M71totalPoints"), "0");
	    item.put(new DataFieldName("M72number"), "0");
	    item.put(new DataFieldName("M72totalPoints"), "0");
	    item.put(new DataFieldName("M81number"), "0");
	    item.put(new DataFieldName("M81totalPoints"), "0");
	    item.put(new DataFieldName("M82number"), "0");
	    item.put(new DataFieldName("M82totalPoints"), "0");
	    item.put(new DataFieldName("M83number"), "0");
	    item.put(new DataFieldName("M83totalPoints"), "0");
	    item.put(new DataFieldName("M84number"), "0");
	    item.put(new DataFieldName("M84totalPoints"), "0");
	    item.put(new DataFieldName("M85number"), "0");
	    item.put(new DataFieldName("M85totalPoints"), "0");
	    item.put(new DataFieldName("M86number"), "0");
	    item.put(new DataFieldName("M86totalPoints"), "0");
	    item.put(new DataFieldName("M91number"), "0");
	    item.put(new DataFieldName("M91totalPoints"), "0");
	    item.put(new DataFieldName("M92number"), "0");
	    item.put(new DataFieldName("M92totalPoints"), "0");
	    item.put(new DataFieldName("M93number"), "0");
	    item.put(new DataFieldName("M93totalPoints"), "0");
		
	}
	public void getReport()  {
	   try{
			if(wordMLPackage != null) {
			   	FacesContext fc = FacesContext.getCurrentInstance();
			    ExternalContext ec = fc.getExternalContext();
		
			    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
			    ec.setResponseContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
		//	    ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
			    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"obrazac1.docx\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
		
			    OutputStream output = ec.getResponseOutputStream();
			    // Now you can write the InputStream of the file to the above OutputStream the usual way.
			    // ...
			    wordMLPackage.save(output);
			    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
		   }
	   } catch (Throwable e){
		  log.error("Can't download report for promotion: " + e.getMessage()); 
	   }
	}
	
	/**
	 * @return the wordMLPackage
	 */
	public WordprocessingMLPackage getWordMLPackage() {
		return wordMLPackage;
	}
	/**
	 * @param wordMLPackage the wordMLPackage to set
	 */
	public void setWordMLPackage(WordprocessingMLPackage wordMLPackage) {
		this.wordMLPackage = wordMLPackage;
	}
	
	
}
