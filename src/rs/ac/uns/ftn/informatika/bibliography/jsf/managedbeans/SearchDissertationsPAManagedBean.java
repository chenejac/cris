package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.QueryDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.TreeNodeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;


@SuppressWarnings({"serial", "unchecked"})
public class SearchDissertationsPAManagedBean extends SearchDissertationsManagedBean implements IPickAuthorManagedBean {
	
	/**
	 * Constructor
	 */
	public SearchDissertationsPAManagedBean() {
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
				if (cookie.getName().equals("languageSearchDissertationsPA")) {
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
		searchQueryOperator = new ArrayList<SelectItem>(Arrays.asList(new SelectItem("AND", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.and")), new SelectItem("OR", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.or")), new SelectItem("AND NOT", facesMessages.getMessageFromResourceBundle("search.searchDissertationsPanel.publicationTab.andNot"))));
		searchQueries = new ArrayList<QueryDTO>();
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[2], searchQueryTypeString[0]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[5], searchQueryTypeString[5]));
		searchQueries.add(new QueryDTO(counterSearchQuery++,searchQueryOperator.get(0).getValue().toString(), searchQueryFieldNameString[0], searchQueryTypeString[0]));
		if (dateRange==null)
		{	
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			dateRange = new ArrayList() ;
			for (int i = currentYear; i>=2014;i--)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}
		}
		fuzzy = false;
		advanced = true;
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
		root= null;
		allInstitutionsAndDegrees=null;
		populateAll = true;
		sortMethod = "relevance";
//		populateAll();
		
		//mozda treba obrisati
		setAuthorManageBeanToPick();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "searchDissertationsPAPage";
		return retVal;
	}
	
	
	public String searchPageEnter() {
		resetForm();
		sortMethod = "relevance";
		setAuthorManageBeanToPick();
		root= null;
		allInstitutionsAndDegrees=null;
		populateAll = true;
		populateAll();
		
		
		
		returnPage = "indexPage";
		return "searchDissertationsPAPage";
	}
	
	public void searchPageEnter(PhaseEvent event){
		String institutionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("institution");
		if((init == false) || (institutionId != null)){
			resetForm();
			sortMethod = "relevance";
			setAuthorManageBeanToPick();
			
			root = null;
			allInstitutionsAndDegrees=null;
			
			populateAll();
			
			if (institutionId != null){
				changeSelectionOnTree(institutionId);
			}
			init = true;
		}	
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
			BooleanQuery paDissertation = new BooleanQuery();
			paDissertation.add(new TermQuery(new Term("AUTHORPADISSERTATIONS", "true")), Occur.SHOULD);
			paDissertation.add(new TermQuery(new Term("ADVISORPADISSERTATIONS", "true")), Occur.SHOULD);
			paDissertation.add(new TermQuery(new Term("COMMISSIONCHAIRPADISSERTATIONS", "true")), Occur.SHOULD);
			paDissertation.add(new TermQuery(new Term("COMMISSIONMEMBERPADISSERTATIONS", "true")), Occur.SHOULD);
			bq.add(paDissertation, Occur.MUST);
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
			query.add(new TermQuery(new Term("PADISSERTATION", "true")), Occur.MUST);
			
			
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
			List<Record> recordsTemp = recordDAO.getDTOs(query, new AllDocCollector(false));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
			logResults(records, query, "second");
			Collections.sort(records, new GenericComparator<StudyFinalDocumentDTO>(
					"publicationYear", "desc"));
			
		}
		
	}
	
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
			bq = QueryUtils.makeBooleanQuery(bq, new TermQuery(new Term("PADISSERTATION", "true")), QueryDTO.AND);
			logQuery(query, "third");
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getDTOs(bq, new AllDocCollector(true));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
			logResults(records, query, "third");
		}	
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
					if((ins.getSuperInstitution() != null) && (ins.getSuperInstitution().getControlNumber()!=null) && (ins.getSuperInstitution().getControlNumber().equalsIgnoreCase("(BISIS)94894"))){
							TreeNodeDTO<Object> node = new TreeNodeDTO<Object>(ins);
							node.setParent(null);
							allInstitutionsAndDegrees.add(node);
							addInstitutionDegrees(node, ins);
							root.add(node);
					}
		      }
	    } catch (Exception e) {
	    	error("getTree", e);
	    }
	}
	

	public void searchDissertations(List<QueryDTO> searchQueries){

		Query query = null;
		boolean sortByRelevance = true;
		for (int i = 0; i < searchQueries.size(); i++)
		{
			Query newQuery = null;
			QueryDTO queryDTO = searchQueries.get(i);
			if((queryDTO.getValue() != null) && (! ("".equals(queryDTO.getValue().trim())))){
				newQuery = QueryUtils.makeQuery(queryDTO);
				if(query != null){
					query = QueryUtils.makeBooleanQuery(query, newQuery, queryDTO.getOperator());
				} else {
					query = newQuery;
				}
			}
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
					newQuery = QueryUtils.makeQuery(queryDTO);
					if(query != null){
						query = QueryUtils.makeBooleanQuery(query, newQuery, queryDTO.getOperator());
					} else {
						query = newQuery;
					}
				}
				queryDTO.setFieldName(queryDTO.getFieldName().replace("_ENG", "_SRP"));
				queryDTO.setOperator(operator);
				queryDTO.setBoost(boost);
			}
		}
		
		if(query == null)
			sortByRelevance = false;
		
		if(advanced){
			String startYear = (fromDate.equals(searchQueryFieldNameString[0]))?"2013":fromDate;
			String endYear = (toDate.equals(searchQueryFieldNameString[0]))?(""+Calendar.getInstance().get(Calendar.YEAR)):toDate;
			if((!("2013".equals(startYear))) || (!((""+Calendar.getInstance().get(Calendar.YEAR)).equals(endYear)))){
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
			query = QueryUtils.makeBooleanQuery(query, new TermQuery(new Term("PADISSERTATION", "true")), QueryDTO.AND);
			logQuery(query, "first");
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			List<Record> recordsTemp = recordDAO.getDTOs(query, new AllDocCollector(sortByRelevance));
			records = new ArrayList<StudyFinalDocumentDTO>();
			for (Record record : recordsTemp) {
				records.add((StudyFinalDocumentDTO)record.getDto());
			}
		
			logResults(records, query, "first");
			if(sortByRelevance == false){
				List<String> orderByList = new ArrayList<String>();
				orderByList.add("publicationYear");
				orderByList.add("harvardRepresentation");
				List<String> directionsList = new ArrayList<String>();
				directionsList.add("desc");
				directionsList.add("asc");
				Collections.sort(records, new GenericComparator<StudyFinalDocumentDTO>(orderByList, directionsList));
			}
		}
	}
		
	
	
	/**
	 * Switches interface to English language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String englishLanguage() {
		setLocale("en");
		init = false;
		allInstitutions = null;
		return "searchDissertationsPAPage";
	}

	/**
	 * Switches interface to Serbian language
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String serbianLanguage() {
		setLocale("sr");
		init = false;
		allInstitutions = null;
		return "searchDissertationsPAPage";
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
				"languageSearchDissertationsPA=" + language + ";HttpOnly;expires=" + date.getTime());
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(language));
		this.language = language;
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

} 
