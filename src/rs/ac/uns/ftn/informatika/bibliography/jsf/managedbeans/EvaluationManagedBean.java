package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResultEvaluator;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * Managed bean with CRUD operations for publications
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class EvaluationManagedBean implements Serializable{

	private HashMap<ResultMeasureDTO, List<PublicationDTO>> map;
	
	private HashMap<String, HashMap<ResultMeasureDTO, List<PublicationDTO>>> mapSamovrednovanje;
	
	private List<ResultMeasureDTO> resultTypes;
	
	private List<PublicationDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private RuleBookDTO ruleBook = null;
	
	private CommissionDTO commission = null;
	
	private RecordDB recordDB = new RecordDB();
	
	private EvaluationDB evaluationDB = new EvaluationDB();
	
	private List<SelectItem> allRuleBooks;
	
	private List<SelectItem> allCommissions;
	
	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());
	
	private DataSource dataSource = null;
	
	
	private AuthorDTO currentAuthor = null;
	
	private CommissionDAO commissionDAO = new CommissionDAO(); 

	/**
	 * 
	 */
	public EvaluationManagedBean() {
		super();
		dataSource = DataSourceFactory.getDataSource();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			allRuleBooks = new ArrayList<SelectItem>();
	        List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
	        allRuleBooks.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.evaluation.pleaseSelectRuleBook")));
	        for (RuleBookDTO rb : ruleBookList){
	        	allRuleBooks.add(new SelectItem(rb, rb.toString()));
	        }
			allCommissions = new ArrayList<SelectItem>();
	        List<CommissionDTO> commissionList = commissionDAO.getCommissionsOrderdList();
	        allCommissions.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.evaluation.pleaseSelectCommission")));
	        for (CommissionDTO com : commissionList){
	        	allCommissions.add(new SelectItem(com, com.toString()));
	        }
		} catch (SQLException e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		
	}

	public String enterPage(){
		currentAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		loadRuleBook();
		loadCommission();
//		populateMap();
		return "evaluationPage";
	}
	
	
	/*
	public void loadCommission() {
		for (SelectItem selectItem : getAllCommissions()) {
			if((selectItem.getValue()!=null) && (((CommissionDTO)selectItem.getValue()).getCommissionId().toString().equals("1"))){
				commission = (CommissionDTO)selectItem.getValue();
				break;
			}	
		}
	}
	
	*/
	
	public void loadCommission() {		
		Integer commissionId = SamovrednovanjeUtils.getCommissionId(currentAuthor);
		
//		System.out.println("1Komisija je:"+commissionId);
		
		for (SelectItem selectItem : getAllCommissions()) {
			if((selectItem.getValue()!=null) && (((CommissionDTO)selectItem.getValue()).getCommissionId().toString().equals(commissionId.toString()))){
				commission = (CommissionDTO)selectItem.getValue();
//				System.out.println("2Komisija je:"+commission.getCommissionId());
				break;
			}	
		}
	}
	

	public void loadRuleBook() {
		for (SelectItem selectItem : getAllRuleBooks()) {
			if((selectItem.getValue()!=null) && (("("+((RuleBookDTO)selectItem.getValue()).getSchemeId()+") " + ((RuleBookDTO)selectItem.getValue()).getClassId()).equals("(ruleBook) serbianResearchersEvaluation"))){
				ruleBook = (RuleBookDTO)selectItem.getValue();
				break;
			}	
		}
		
	}

	public void populateMap() {
		map = new HashMap<ResultMeasureDTO, List<PublicationDTO>>();
		resultTypes = new ArrayList<ResultMeasureDTO>();
		for (PublicationDTO publicationDTO : getPublications()) {
			// za svaku publikaciju odredi kategoriju
//			System.out.println(publicationDTO);
			ResultMeasureDTO rsm = calculateResultTypeMeasure(publicationDTO);
			
			if(rsm == null){
				rsm = new ResultMeasureDTO();
				rsm.setQuantitativeMeasure(new Double(0.0));
				ResultTypeDTO rt = new ResultTypeDTO();
				rt.setSchemeId("resultType");
				rt.setClassId("M99");
				rt.setTerm(new MultilingualContentDTO("Publikacije koje jos nisu vrednovane", MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL));
				rsm.setResultType(rt);
			}
			if(rsm != null){
				List<PublicationDTO> tmp = map.get(rsm);
				if(tmp == null){
					tmp = new ArrayList<PublicationDTO>();
					map.put(rsm, tmp);
				}
				tmp.add(publicationDTO);
				Collections.sort(tmp, new GenericComparator<PublicationDTO>(
						"publicationYear", "asc"));				
			}
		}
		for (ResultMeasureDTO resultMeasureDTO : map.keySet()) {
			resultTypes.add(resultMeasureDTO);
		}
		Collections.sort(resultTypes, new GenericComparator<ResultMeasureDTO>(
				"resultType.classId", "asc"));
		
	}
	
		public void populateMapSamovrednovanje() {
			mapSamovrednovanje = new HashMap<String, HashMap<ResultMeasureDTO, List<PublicationDTO>>>();
			resultTypes = new ArrayList<ResultMeasureDTO>();
			for (PublicationDTO publicationDTO : getPublications()) {
				ResultMeasureDTO rsm = calculateResultTypeMeasure(publicationDTO);
				if(rsm == null){
					rsm = new ResultMeasureDTO();
					rsm.setQuantitativeMeasure(new Double(0.0));
					ResultTypeDTO rt = new ResultTypeDTO();
					rt.setSchemeId("resultType");
					rt.setClassId("M99");
					rt.setTerm(new MultilingualContentDTO("Publikacije koje jos nisu vrednovane", MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL));
					rsm.setResultType(rt);
				}
				if(rsm != null){
					HashMap<ResultMeasureDTO, List<PublicationDTO>> tmp 
							= mapSamovrednovanje.get(publicationDTO.getPublicationYear().toString());
					if(tmp == null){
						tmp = new HashMap<ResultMeasureDTO, List<PublicationDTO>>();
						mapSamovrednovanje.put(publicationDTO.getPublicationYear().toString(), tmp);
					}	
						List<PublicationDTO> pubs = tmp.get(rsm);
						if(pubs== null){
							pubs = new ArrayList<PublicationDTO>();
							tmp.put(rsm, pubs);						
						}
						pubs.add(publicationDTO);		
						tmp.put(rsm,pubs);
					}
				
					//Collections.sort(tmp, new GenericComparator<PublicationDTO>(
					//		"publicationYear", "asc"));
					
				}
		
		
		//Collections.sort(resultTypes, new GenericComparator<ResultMeasureDTO>(
			//	"resultType.classId", "asc"));
		
	}

	public void populateMapOnSpotEvaluation() {
		map = new HashMap<ResultMeasureDTO, List<PublicationDTO>>();
		resultTypes = new ArrayList<ResultMeasureDTO>();
		
		List<PublicationDTO> svePublikacije = getPublications();
		
//		System.out.println("broj publikacija " + svePublikacije.size());
		int i = 0;
		
		
		
		for (PublicationDTO publicationDTO : svePublikacije) {
			
			i++;
//			System.out.println(i+ " publikacija je -----------------------------------------------------------------------------------");
			// za svaku publikaciju odredi kategoriju
			
//			if(! (publicationDTO instanceof PaperJournalDTO))
//				continue;
			
			
			ResultMeasureDTO rsm = calculateResultTypeMeasureOnSpotEvaluation(publicationDTO);
			if(rsm == null){
				rsm = new ResultMeasureDTO();
				rsm.setQuantitativeMeasure(new Double(0.0));
				ResultTypeDTO rt = new ResultTypeDTO();
				rt.setSchemeId("resultType");
				rt.setClassId("M99");
				rt.setTerm(new MultilingualContentDTO("Publikacije koje jos nisu vrednovane", MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL));
				rsm.setResultType(rt);
			}
			
//			System.out.println("Ocenjena kao "+ rsm.getResultType().getClassId() + " sa bodom " +rsm.getQuantitativeMeasure());
			
			
			if(rsm != null){
				List<PublicationDTO> tmp = map.get(rsm);
				if(tmp == null){
					tmp = new ArrayList<PublicationDTO>();
					map.put(rsm, tmp);
				}
				tmp.add(publicationDTO);
				Collections.sort(tmp, new GenericComparator<PublicationDTO>(
						"publicationYear", "asc"));				
			}
//			publicationDTO.clear();
		}
		for (ResultMeasureDTO resultMeasureDTO : map.keySet()) {
			resultTypes.add(resultMeasureDTO);
		}
		Collections.sort(resultTypes, new GenericComparator<ResultMeasureDTO>(
				"resultType.classId", "asc"));
	}
	/**
		 * @return the mapSamovrednovanje
		 */
		public HashMap<String, HashMap<ResultMeasureDTO, List<PublicationDTO>>> getMapSamovrednovanje() {
			return mapSamovrednovanje;
		}

		/**
		 * @param mapSamovrednovanje the mapSamovrednovanje to set
		 */
		public void setMapSamovrednovanje(
				HashMap<String, HashMap<ResultMeasureDTO, List<PublicationDTO>>> mapSamovrednovanje) {
			this.mapSamovrednovanje = mapSamovrednovanje;
		}

	private ResultMeasureDTO calculateResultTypeMeasure(
			PublicationDTO publicationDTO) {
		ResultMeasureDTO retVal = null;
		Connection conn = null;
		try{
		Calendar publicationDate = new GregorianCalendar();
		Map<String, Integer> journalCategories = new HashMap<String, Integer>();
		journalCategories.put("scienceJournal", new Integer(1));
		journalCategories.put("nationalJournal", new Integer(2));
		journalCategories.put("leadingNationalJournal", new Integer(3));
		journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
		journalCategories.put("internationalJournal", new Integer(5));
		journalCategories.put("outstandingInternationalJournal", new Integer(6));
		journalCategories.put("leadingInternationalJournal", new Integer(7));
		journalCategories.put("topLeadingInternationalJournal", new Integer(8));
		
		Date publicationTime = new Date();
//		publicationTime.setYear(publicationDTO.getPublicationYear()-1900);
		publicationDate.setTime(publicationTime); 
		
		Integer publicationYear = null;
		
		try{
			if(publicationDTO.getPublicationYear()!=null){
				if(publicationDTO.getPublicationYear().contains("/")){
					publicationYear = Integer.parseInt(publicationDTO.getPublicationYear().substring(0, publicationDTO.getPublicationYear().indexOf("/")));
				} else 
					publicationYear = Integer.parseInt(publicationDTO.getPublicationYear());
			} else {
//				System.out.println(publicationDTO.getHTMLRepresentation());
				return null;
			}
		}catch (Exception e) {
//			System.out.println("Greska pri izracunavanju godine za zapis: " + publicationDTO.getControlNumber() );
			return null;
		}
		
		CommissionDTO temp = commission;
		
		/*if( !((publicationDTO instanceof PaperJournalDTO) || (publicationDTO instanceof JournalDTO)) ){
			 if(commission.getCommissionId().intValue() == 11){
				 commission = commissionDAO.getCommission("1");
			 }
			 else if(commission.getCommissionId().intValue() == 12){
				 commission = commissionDAO.getCommission("2");
			 }
			 else if(commission.getCommissionId().intValue() == 13){
				 commission = commissionDAO.getCommission("3");
			 }
			 else if(commission.getCommissionId().intValue() == 14){
				 commission = commissionDAO.getCommission("4");
			 }
			 else if(commission.getCommissionId().intValue() == 15){
				 commission = commissionDAO.getCommission("5");
			 }
			 else{
//				 System.out.println("Debeli promasaj " + commission.getCommissionId().intValue() + " za "+ publicationDTO.getControlNumber() + " " + publicationDTO.getStringRepresentation());
			 }
		}*/
		
		publicationDate.set(GregorianCalendar.YEAR, publicationYear);
		Calendar publicationDateMinus1 = new GregorianCalendar();
		publicationDateMinus1.setTime(publicationTime); 
		publicationDateMinus1.set(GregorianCalendar.YEAR, publicationYear-1);
		Calendar publicationDateMinus2 = new GregorianCalendar();
		publicationDateMinus2.setTime(publicationTime); 
		publicationDateMinus2.set(GregorianCalendar.YEAR, publicationYear-2);
		Calendar publicationDatePlus1 = new GregorianCalendar();
		publicationDatePlus1.setTime(publicationTime); 
		publicationDatePlus1.set(GregorianCalendar.YEAR, publicationYear+1);

		conn = dataSource.getConnection();
		
//		Record rec = recordDB.getRecord(conn, publicationDTO.getControlNumber()); 
//		Record rec = recordDAO.getRecord(publicationDTO.getControlNumber());
//		rec.loadMARC21FromDTO();
		String schemePrizeType = null;
		String classPrizeType = null;
		String schemeEventType = null;
		String classEventType = null;
		String schemeSuperPublicationType = null;
		String classSuperPublicationType = null;
		String schemeProductType = null;
		String classProductType = null; 
		String schemePatentType = null; 
		String classPatentType = null;
		String schemeResearcherRoleType = null;
		String classResearcherRoleType = null;
		String schemePublicationType = "resultType";
		String classPublicationType = evaluationDB.getResultClassificationByCommission(conn, publicationDTO.getControlNumber(), "resultType", publicationDate, commission.getCommissionId());
		
		
		if(classPublicationType!=null){
			retVal = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
		}
		
		if(commission.getCommissionId().intValue() != temp.getCommissionId().intValue())
			commission = temp;
		
		}catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		
		return retVal;
	}

	private ResultMeasureDTO calculateResultTypeMeasureOnSpotEvaluation(PublicationDTO publicationDTO) {
		
		Connection conn = null;
		ResultMeasureDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			List<CommissionDTO> commissionList = new ArrayList<CommissionDTO>();
			if (commission != null) {
				commissionList.add(commission);
			}
			List<AuthorDTO> authorsAndEditors = new ArrayList<AuthorDTO>();
			if (currentAuthor !=null) {
				authorsAndEditors.add(currentAuthor);
			}

			Record record = publicationDTO.getRecord();
			Map<String, ResultMeasureDTO> resultMeasures = ResultEvaluator.calculateResultMeasures(conn, record, ruleBook, commissionList, authorsAndEditors);
			ResultMeasureDTO resultMeasure = resultMeasures.get(commission.getCommissionId().toString());
			if (resultMeasure != null){
				retVal = resultMeasure;
			}
			record.clear();
		}catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		return retVal;
	}
	
	public List<ResultMeasureDTO> getResultTypes(){
		populateMap();
		return resultTypes;
	}
	
	public HashMap<ResultMeasureDTO, List<PublicationDTO>> getEvaluatedResults(){
		return map;
	}

	public void populateList() {
		try {
			List<PublicationDTO> listTmp = getPublications(
					createQuery(), "publicationYear", "asc", new AllDocCollector(false));
			
			list = listTmp;
		} catch (ParseException e) {
			list = new ArrayList<PublicationDTO>();
		}
	}
	
	/**
	 * Retrieves a list of publications that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving publications
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of publications that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<PublicationDTO> getPublications(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getPublications(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of publications that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving papers
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of publications that correspond to the query
	 */
	private List<PublicationDTO> getPublications(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<PublicationDTO> retVal = new ArrayList<PublicationDTO>();

		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				PublicationDTO dto = (PublicationDTO) record.getDto();
				if (dto != null && !dto.getControlNumber().equalsIgnoreCase("(BISIS)77039")) {
					retVal.add(dto);
				}
			} catch (Exception e) {
			}
		}
		Collections.sort(retVal, new GenericComparator<PublicationDTO>(
			orderBy, direction));
		return retVal;
	}

	/**
	 * @return the list of publications (filtered and ordered
	 *         by ...)
	 */
	public List<PublicationDTO> getPublications() {
		
		populateList();
		
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
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		
		
		//AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if (currentAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", currentAuthor.getControlNumber())), Occur.SHOULD);
			authorsPapers.add(new TermQuery(new Term("EDCN", currentAuthor.getControlNumber())), Occur.SHOULD);
			
			bq.add(authorsPapers, Occur.MUST);
		}
		return bq;
	}
	
	/**
	 * bojana
	 * 
	 * 
	 * @param year
	 * @param authorIds
	 * @return
	 * @throws ParseException
	 */
	
	protected Query createQuery(List<String> years, List<String> authorIds) throws ParseException {
		BooleanQuery bq = new BooleanQuery();
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		
		
		//AuthorDTO loggedAuthor = getUserManagedBean().getLoggedUser().getAuthor();
		if (currentAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", currentAuthor.getControlNumber())), Occur.SHOULD);
			authorsPapers.add(new TermQuery(new Term("EDCN", currentAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		return bq;
	}
	
	
	private UserManagedBean userManagedBean = null;

	/**
	 * @return the UserManagedBean from current session
	 */
	protected UserManagedBean getUserManagedBean() {
		if (userManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			UserManagedBean mb = (UserManagedBean) extCtx.getSessionMap().get(
					"userManagedBean");
			if (mb == null) {
				mb = new UserManagedBean();
				extCtx.getSessionMap().put("userManagedBean", mb);
			}
			userManagedBean = mb;
		}
		return userManagedBean;
	}

	/**
	 * @return the ruleBook
	 */
	public RuleBookDTO getRuleBook() {
		return ruleBook;
	}

	/**
	 * @param ruleBook the ruleBook to set
	 */
	public void setRuleBook(RuleBookDTO ruleBook) {
		this.ruleBook = ruleBook;
		this.commission = null;
	}

	/**
	 * @return the commission
	 */
	public CommissionDTO getCommission() {
		return commission;
	}

	/**
	 * @param commission the commission to set
	 */
	public void setCommission(CommissionDTO commission) {
		this.commission = commission;
	}

	/**
	 * @return the allRuleBooks
	 */
	public List<SelectItem> getAllRuleBooks() {
		return allRuleBooks;
	}

	/**
	 * @return the allCommissions
	 */
	public List<SelectItem> getAllCommissions() {
		return allCommissions;
	}
	
	/**
	 * @return the currentAuthor
	 */
	public AuthorDTO getCurrentAuthor() {
		return currentAuthor;
	}

	/**
	 * @param currentAuthor the currentAuthor to set
	 */
	public void setCurrentAuthor(AuthorDTO currentAuthor) {
		this.currentAuthor = currentAuthor;
	}


}
