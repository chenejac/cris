package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ConferenceAndProceedings;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ProceedingsAndPapers;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.utils.EvaluationScheduledThread;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * @author Dragan Ivanovic, chenejac@uns.ac.rs
 *
 */

public class EvaluationConferenceRecordsManagedBean extends CRUDManagedBean {

	private Integer startYear = 2021;

	private Integer endYear = 2021;

	protected List<SelectItem> yearRange = null;

	private static final long serialVersionUID = -3945361701930476206L;

	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());
	
	private static DataSource dataSource = null;
	
	static {
		dataSource = DataSourceFactory.getDataSource();
	}
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private EvaluationDB evaluationDB = new EvaluationDB();
	
	private CommissionDAO commissionDAO = new CommissionDAO();

	
	private List<ConferenceAndProceedings> conferences = new ArrayList<ConferenceAndProceedings>();
	private List<ConferenceAndProceedings> allConferences = new ArrayList<ConferenceAndProceedings>();
	
	private Integer commissionNumber = null;
	
	private Integer fromDate = null;
	private Integer toDate = null;
	
	private Integer firstYear = null;
	private Integer lastYear = null;
	
	private String classifications = "all";
	
	

	public EvaluationConferenceRecordsManagedBean() {
		super();
//		System.out.println("Konstruktor poziva init");
		init();
		if (yearRange==null)
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			yearRange = new ArrayList() ;
			/*for (int i = 1960,j=0; i<=currentYear;i++,j++)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}*/

			// idemo od tekuce godine do 2018
			for (int i = currentYear; i>=1980;i--)
			{
				yearRange.add(new SelectItem(String.valueOf(i)));
			}
		}
	}

	/**
	 * @return the dateRange
	 */
	public List<SelectItem> getYearRange() {
		return yearRange;
	}
	/**
	 * Initialized 
	 */
	public void init ()
	{

	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		return super.resetForm();
	}
	
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "evaluationConferenceRecordsPage";
		return retVal;
	}
	
	public String enterPage() {
		init();
		resetForm();	
		returnPage = "indexPage";
		return "evaluationConferenceRecordsPage";
	}
	
	
	/**
	 * Performs the account activation
	 */
	public void enterPage(PhaseEvent event){
		if(init == false){
			init();
			resetForm();
		}
	}
	
	@Override
	public void populateList() {
		// TODO Auto-generated method stub

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


	/**
	 * @param evaluationScheduledThread the evaluationScheduledThread to set
	 */
	public void setEvaluationScheduledThread(
			EvaluationScheduledThread evaluationScheduledThread) {
		EvaluationAllRecordsManagedBean.evaluationScheduledThread = evaluationScheduledThread;
	}
	
	public void collectTFConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 701;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
			Query orgUnitQuery = qparser.parse("+(INS:\"tehnoloski fakultet novi sad\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			
			orgUnitQuery = qparser.parse("+(INS:\"katedra za opste inzenjerske discipline\" INS:\"katedra za primenjene i inzenjerske hemije\" INS:\"katedra za hemijsko inzenjerstvo\" INS:\"katedra za tehnologije ugljenohidratne hrane\" INS:\"katedra za tehnologije konzervisane hrane\" INS:\"katedra za biotehnologiju i farmaceutsko inzenjerstvo\" INS:\"katedra za naftno petrohemijsko inzenjerstvo\" INS:\"katedra za inzenjerstvo materijala\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5933'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}
	
	public void collectPMFDMIConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 711;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
//			Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
//			institution.add(orgUnitQuery, Occur.SHOULD);
		
			Query orgUnitQuery = qparser.parse("+(INS:\"departman za matematiku i informatiku\" INS:\"katedra za opstu algebru i teorijsko racunarstvo\" INS:\"katedra za analizu verovatnocu i diferencijalne jednacine\" INS:\"katedra za numericku matematiku\" INS:\"katedra za primenjenu algebru\" INS:\"katedra za funkcionalnu analizu geometriju i topologiju\" INS:\"katedra za racunarske nauke\" INS:\"katedra za matematicku logiku i diskretnu matematiku\" INS:\"katedra za primenjenu analizu\" INS:\"katedra za informacione tehnologije i sisteme\" INS:\"katedra za teorijske osnove informatike\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}
	
	public void collectPMFDGTHConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 713;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
//			Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
//			institution.add(orgUnitQuery, Occur.SHOULD);
		
			Query orgUnitQuery = qparser.parse("+(INS:\"departman za geografiju turizam i hotelijerstvo\" INS:\"katedra za fizicku geografiju\" INS:\"katedra za drustvenu geografiju\" INS:\"katedra za regionalnu geografiju\" INS:\"katedra za turizam\" INS:\"katedra za hotelijerstvo\" INS:\"katedra za lovni turizam\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}
	
	public void collectPMFDFConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 714;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
//			Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
//			institution.add(orgUnitQuery, Occur.SHOULD);
		
			Query orgUnitQuery = qparser.parse("+(INS:\"departman za fiziku\" INS:\"katedra za eksperimentalnu fiziku kondenzovane materije\" INS:\"katedra za fizicku elektroniku\" INS:\"katedra za nuklearnu fiziku\" INS:\"katedra za teorijsku fiziku\" INS:\"katedra za opstu fiziku i metodiku nastave fizike\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}
	
	public void collectPMFDHZZSConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 712;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
//			Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
//			institution.add(orgUnitQuery, Occur.SHOULD);
		
			Query orgUnitQuery = qparser.parse("+(INS:\"departman za hemiju biohemiju i zastitu zivotne sredine\" INS:\"katedra za analiticku hemiju\" INS:\"katedra za biohemiju i hemiju prirodnih proizvoda\" INS:\"katedra za fizicku hemiju\" INS:\"katedra za hemijsko obrazovanje i metodiku nastave hemije\" INS:\"katedra za hemijsku tehnologiju i zastitu zivotne sredine\" INS:\"katedra za opstu i neorgansku hemiju\" INS:\"katedra za organsku hemiju\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}
	
	public void collectPMFDBEConferences(){
		BooleanQuery institution = new BooleanQuery();
		List<Record> listConferences = new ArrayList<Record>();
		commissionNumber = 715;
		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
		try{
//			Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
//			institution.add(orgUnitQuery, Occur.SHOULD);
		
			Query orgUnitQuery = qparser.parse("+(INS:\"departman za biologiju i ekologiju\" INS:\"katedra za botaniku\" INS:\"katedra za fiziologiju\" INS:\"katedra za zoologiju\" INS:\"katedra za mikrobiologiju\" INS:\"katedra za humanu biologiju i metodiku nastave biologije\" INS:\"katedra za ekologiju i zastitu zivotne sredine\")");
			institution.add(orgUnitQuery, Occur.SHOULD);
			String query = "(";
			for(int i=startYear;i<=endYear;i++) {
				if(!query.equals("(")){
					query += " OR ";
				}
				query += "(RECORDSTRING like '%d" + i + "%')";
			}
			query += ") AND ARCHIVED = 0 AND RECORDID in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is output from' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is published in' and RECORDID1 in " +
					"(SELECT DISTINCT(RECORDID2) FROM MARC21RECORD_MARC21RECORD m where CFCLASSID like 'is author of' and RECORDID1 in " +
					"(SELECT distinct(RECORDID1) FROM MARC21RECORD_MARC21RECORD m where CFCLASSSCHEMEID like 'authorInstitutionSelfevaluation' and RECORDID2 like '(BISIS)5929'))))";

			listConferences = recordDAO.getRecordsIdsFromDatabaseByWhereClause(query);
		} catch (Exception e) {
			log.error(e);
		}
		conferences = collectConferences(listConferences, institution);
	}

	public List<ConferenceAndProceedings> collectConferences(Query institution){
		List<ConferenceAndProceedings> retVal = new ArrayList<ConferenceAndProceedings>();
		BooleanQuery bq = new BooleanQuery();
		Query allConferencesQuery = new TermQuery(new Term("TYPE", Types.CONFERENCE));
//		Query yearQuery = new TermQuery(new Term("YEAR", "2019"));
//		bq.add(allConferencesQuery, Occur.MUST);
//		bq.add(yearQuery, Occur.MUST);
//		List<Record> listConferences = recordDAO.getDTOs(bq, new AllDocCollector(false));
		List<Record> listConferences = recordDAO.getDTOs(allConferencesQuery, new AllDocCollector(false));
		retVal = collectConferences(listConferences, institution);
		return retVal;
	}
		
	public List<ConferenceAndProceedings> collectConferences(List<Record> listConferences, Query institution){
		allConferences = new ArrayList<ConferenceAndProceedings>();
		for (Record recordDTO : listConferences) {
			try {
				String conferenceClassification = "a";
				recordDTO.loadFromDatabase();
				ConferenceDTO conferenceDTO = (ConferenceDTO) recordDTO.getDto();
				Calendar publicationDate = new GregorianCalendar();
				Date publicationTime = new Date();
				publicationDate.setTime(publicationTime); 
				publicationDate.set(GregorianCalendar.YEAR, conferenceDTO.getYear());
				for (Classification classification : recordDTO.getRecordClasses()) {
					if(
							((commissionNumber.equals(classification.getCommissionId().toString())))
					&& (classification.getCfClassSchemeId().equals("type")) 
					&&  (! (publicationDate.before(classification.getCfStartDate()))) 
					&& (!(publicationDate.after(classification.getCfEndDate())))){
						conferenceClassification = classification.getCfClassId();
						break;
					} else if(
							((!"0".equals(classification.getCommissionId().toString())))
							&& (classification.getCfClassSchemeId().equals("type")) 
							&&  (! (publicationDate.before(classification.getCfStartDate()))) 
							&& (!(publicationDate.after(classification.getCfEndDate())))){
								conferenceClassification = classification.getCfClassId();
							}
					
				}
				if (conferenceDTO != null) {
					BooleanQuery allConferenceProceedingsQuery = new BooleanQuery();
					allConferenceProceedingsQuery.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
//						allConferenceProceedingsQuery.add(new TermQuery(new Term("YE", "2015")), Occur.MUST);
					allConferenceProceedingsQuery.add(new TermQuery(new Term("COCN", conferenceDTO.getControlNumber())), Occur.MUST);
					List<Record> listConferenceProceedings = recordDAO.getDTOs(allConferenceProceedingsQuery, new AllDocCollector(false));
					List<ProceedingsAndPapers> listProceedingsAndPapers = new ArrayList<ProceedingsAndPapers>();
					List<RecordDTO> relatedRecords = new ArrayList<RecordDTO>();
					for (Record recDTO : listConferenceProceedings) {
						ProceedingsDTO proceedingsDTO = (ProceedingsDTO) recDTO.getDto();
						if (proceedingsDTO != null) {
							BooleanQuery allProceedingsPapersQuery = new BooleanQuery();
							BooleanQuery type = new BooleanQuery();
							type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
							
							allProceedingsPapersQuery.add(institution, Occur.MUST);
							allProceedingsPapersQuery.add(type, Occur.MUST);
							allProceedingsPapersQuery.add(new TermQuery(new Term("PRCN", proceedingsDTO.getControlNumber())), Occur.MUST);
							List<Record> listProceedingsPapers = recordDAO.getDTOs(allProceedingsPapersQuery, new AllDocCollector(false));
							List<PaperProceedingsDTO> listPapers = new ArrayList<PaperProceedingsDTO>();
							for (Record rDTO : listProceedingsPapers) {
								PaperProceedingsDTO paperProceedingsDTO = (PaperProceedingsDTO) rDTO.getDto();
								if (paperProceedingsDTO != null) {
									listPapers.add(paperProceedingsDTO);
								}
								paperProceedingsDTO.setNotLoaded(true);
							}
							if(listPapers.size() > 0){
								listProceedingsAndPapers.add(new ProceedingsAndPapers(proceedingsDTO, listPapers));
								relatedRecords.addAll(listPapers);
							}
						}
						proceedingsDTO.setNotLoaded(true);
					}	
					if(listProceedingsAndPapers.size() > 0){
						allConferences.add(new ConferenceAndProceedings(conferenceDTO, listProceedingsAndPapers, conferenceClassification));
						if((fromDate == null) || (fromDate > conferenceDTO.getYear())){
							fromDate = conferenceDTO.getYear();
						}
						if((toDate == null) || (toDate < conferenceDTO.getYear())){
							toDate = conferenceDTO.getYear();
						}
					}
					conferenceDTO.setNotLoaded(true);
					conferenceDTO.setRelatedRecords(relatedRecords);
				}				
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		if(fromDate == null)
			fromDate = new GregorianCalendar().get(Calendar.YEAR);
		if(toDate == null)
			toDate = new GregorianCalendar().get(Calendar.YEAR);
		firstYear = fromDate;
		lastYear = toDate;
		classifications = "all";
		List<String> orderBy = new ArrayList<String>();
		orderBy.add("classification");
		orderBy.add("conference.year");
		orderBy.add("conference.someName");
		List<String> directions = new ArrayList<String>();
		directions.add("asc");
		directions.add("desc");
		directions.add("asc");
		Collections.sort(allConferences, new GenericComparator<ConferenceAndProceedings>(
				orderBy, directions));
		classifications = "notEvaluatedConferences";
		return allConferences;
	}
	
	public void filterConferences(){
		conferences = new ArrayList<ConferenceAndProceedings>();
		for (ConferenceAndProceedings conference : allConferences) {
			String classification = conference.getClassification();
			Integer year = conference.getConference().getYear();
			if((fromDate <= year) && (year <= toDate)){
				if((classifications != null) && (classifications.equals("evaluatedConferences"))){
					if((classification != null) && ((classification.equals("internationalConference")) || (classification.equals("nationalConference")) || (classification.equals("notConference"))))
						conferences.add(conference);
				} else if((classifications != null) && (classifications.equals("notEvaluatedConferences"))){
					if((classification == null) || ((!classification.equals("nationalConference")) && (!classification.equals("internationalConference")) && (!classification.equals("notConference"))))
						conferences.add(conference);
				} else if((classifications != null) && (classifications.equals("all")))
					conferences.add(conference);
			}
				
		}
		List<String> orderBy = new ArrayList<String>();
		orderBy.add("classification");
		orderBy.add("conference.year");
		orderBy.add("conference.someName");
		List<String> directions = new ArrayList<String>();
		directions.add("asc");
		directions.add("desc");
		directions.add("asc");
		Collections.sort(conferences, new GenericComparator<ConferenceAndProceedings>(
				orderBy, directions));
	}
	
	public void saveEvaluations(){
		Connection conn = null;
		try{
			conn = dataSource.getConnection();
			Calendar startDate = new GregorianCalendar();
			startDate.set(Calendar.YEAR, 1908);
			startDate.set(Calendar.DAY_OF_YEAR, 1);
			Calendar endDate = new GregorianCalendar();
			endDate.set(Calendar.YEAR, 3008);
			endDate.set(Calendar.MONTH, Calendar.DECEMBER);
			endDate.set(Calendar.DAY_OF_MONTH, 31);
			int k = 0;
			for (ConferenceAndProceedings conference : conferences) {
				if(("internationalConference".equals(conference.getClassification())) || ("nationalConference".equals(conference.getClassification())) || ("notConference".equals(conference.getClassification()))){
					if(evaluationDB.removeSingleResultClassificationByCommission(conn, conference.getConference().getControlNumber(), commissionNumber))
						if(evaluationDB.addResultClassificationByCommission(conn, conference.getConference().getControlNumber(), "type", conference.getClassification(), startDate, endDate, commissionNumber)){
							k++;
							if(conference.changedClassification())
								commissionDAO.reevaluateByCommissionFromAuthorsAffiliation(conference.getConference().getRelatedRecords(), null);
						}
				} else if(conference.getClassification() == null)
					evaluationDB.removeSingleResultClassificationByCommission(conn, conference.getConference().getControlNumber(), commissionNumber);
			}
			conn.commit();
			facesMessages.addToControlFromResourceBundle(
					"evaluationConferenceTableForm:general", FacesMessage.SEVERITY_INFO, 
					"evaluation.conference.tablePanel.evaluate.success", FacesContext
							.getCurrentInstance(), new Integer(k));
		} catch(Throwable e){
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * @return the conferences
	 */
	public List<ConferenceAndProceedings> getConferences() {
		return conferences;
	}

	/**
	 * @param conferences the conferences to set
	 */
	public void setConferences(List<ConferenceAndProceedings> conferences) {
		this.conferences = conferences;
	}

	/**
	 * @return the commissionNumber
	 */
	public Integer getCommissionNumber() {
		return commissionNumber;
	}

	/**
	 * @param commissionNumber the commissionNumber to set
	 */
	public void setCommissionNumber(Integer commissionNumber) {
		this.commissionNumber = commissionNumber;
	}

	/**
	 * @return the allConferences
	 */
	public List<ConferenceAndProceedings> getAllConferences() {
		return allConferences;
	}

	/**
	 * @param allConferences the allConferences to set
	 */
	public void setAllConferences(List<ConferenceAndProceedings> allConferences) {
		this.allConferences = allConferences;
	}

	/**
	 * @return the fromDate
	 */
	public Integer getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Integer fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Integer getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Integer toDate) {
		this.toDate = toDate;
	}
	

	/**
	 * @return the firstYear
	 */
	public Integer getFirstYear() {
		return firstYear;
	}

	/**
	 * @param firstYear the firstYear to set
	 */
	public void setFirstYear(Integer firstYear) {
		this.firstYear = firstYear;
	}

	/**
	 * @return the lastYear
	 */
	public Integer getLastYear() {
		return lastYear;
	}

	/**
	 * @param lastYear the lastYear to set
	 */
	public void setLastYear(Integer lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return the classifications
	 */
	public String getClassifications() {
		return classifications;
	}

	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(String classifications) {
		this.classifications = classifications;
	}
	
	public List<SelectItem> getDateRange(){
		List<SelectItem> retVal = new ArrayList<SelectItem>();
		for (int i=lastYear.intValue(); i>=firstYear.intValue(); i--)
			retVal.add(new SelectItem(new Integer(i), ""+i));
		return retVal;
	}
	
	public boolean canSave(){
		boolean retVal = false;
		if(commissionNumber != null){
			UserDTO user = getUserManagedBean().getLoggedUser();
			String insOrgUnitControlNumber = ((user.getInstitution().getControlNumber() != null)?(user.getInstitution().getControlNumber()):((user.getOrganizationUnit().getControlNumber() != null)?user.getOrganizationUnit().getControlNumber():"no number"));
			if(user.getType().equals("administrator"))
				retVal = true;
			else if ((commissionNumber == 701) && (insOrgUnitControlNumber.equals("(BISIS)5933")))
					retVal = true;
			else if ((commissionNumber == 711) && (insOrgUnitControlNumber.equals("(BISIS)6782")))
				retVal = true;
			else if ((commissionNumber == 712) && (insOrgUnitControlNumber.equals("(BISIS)6781")))
				retVal = true;
			else if ((commissionNumber == 713) && (insOrgUnitControlNumber.equals("(BISIS)6780")))
				retVal = true;
			else if ((commissionNumber == 714) && (insOrgUnitControlNumber.equals("(BISIS)6779")))
				retVal = true;
			else if ((commissionNumber == 715) && (insOrgUnitControlNumber.equals("(BISIS)6778")))
				retVal = true;
		}
		return retVal;
	}

	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}


}