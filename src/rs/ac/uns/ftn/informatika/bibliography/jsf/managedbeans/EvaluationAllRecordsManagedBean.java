package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.EvaluationScheduledThread;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.ResourceBoundles;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class EvaluationAllRecordsManagedBean extends CRUDManagedBean {

	protected FacesMessages facesMessages = new FacesMessages(
			new ResourceBoundles());
	
	private DataSource dataSource = null;
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private EvaluationDB evaluationDB = new EvaluationDB();
	private CommissionDAO commissionDAO = new CommissionDAO();
	
	/**** ATRIBUTI POTREBNI ZA POZIVANJE NITI ********/
	public static ScheduledExecutorService executorService;
	public static EvaluationScheduledThread evaluationScheduledThread;
	public static ScheduledFuture<EvaluationScheduledThread> scheduleFuture;

	/**** OPCIJE POZIVANJA ZADATKA ********/
	private Date taskDate= null;
	public static final String ONLY_JOURNALS = "onlyJournals";
	public static final String ONLY_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS = "onlyResearchResultsForSelectedJournals";
	public static final String JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS = "journalsAndResearchResultsForSelectedJournals";
	public static final String ONLY_RESEARCH_CONFERENCE_RESULTS = "onlyResearchConferenceResults";
	
	private String evaluationTaskTypeRecordOptions = null;
	private String messageAboutTask = null;
	
	/**** OPCIJE ODABIRA CASOPISA ********/
	public static final String ALL_JOURNALS = "allJournals";
	public static final String JOURNALS_MODIFIED_WITHIN_TIME_RANGE = "journalsModifiedWithinTimeRange";
	public static final String SELECTED_JOURNALS = "selectedJournals";
	private String evaluationTaskJournalsOptions = null;
	
	private Date journalsFromDate= null;
	private Date journalsToDate= null;
	
	private String journalControlNumber = null;
	private List<RecordDTO> journalList = null;
	
	/**** OPCIJE ODABIRA NAUCNO ISTRAZIVACKIH REZULTATA ********/
	public static final String ALL_RESEARCH_RESULTS = "allResearchResults";
	public static final String RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE = "researchResultsModifiedWithinTimeRange";
	public static final String SELECTED_RESEARCH_RESULTS = "selectedResearchResults";
	private String evaluationTaskResearchResultsOptions = null;
	
	private Date researchResultsFromDate= null;
	private Date researchResultsToDate= null;
	
	private String researchResultControlNumber = null;
	private List<RecordDTO> researchResultList = null;
	
	public static final String EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS = "researchResultsBySelectedCommissions";
	public static final String EVALUATE_RESEARCH_RESULTS_BY_AUTHORS_COMMISSIONS = "researchResultsByAuthorsCommissions";
	private String evaluationTaskResearchResultsCommissionOptions = null;
	
	/**** OPCIJE ODABIRA KOMISIJA ********/
	private List<CommissionDTO> commissionList = null;
	private List<Integer> selectedCommissionIDList = null;
	
	/**** OPCIJE ODABIRA PRVILNIKA ********/
	private String ruleBookId = null;
	private RuleBookDTO ruleBook = null;
	private List<RuleBookDTO> ruleBookList = null;
	private List<SelectItem> allRuleBookSelectItems;

	public EvaluationAllRecordsManagedBean() {
		super();
//		System.out.println("Konstruktor poziva init");
		init();	
	}

	/**
	 * Initialized 
	 */
	public void init ()
	{
//		System.out.println("Poziv inti metode, init je "+init);
		
		if(init==true)
			return;
		
		Calendar calendar = Calendar.getInstance();
		
		/**** OPCIJE POZIVANJA ZADATKA ********/
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    taskDate = calendar.getTime();
		evaluationTaskTypeRecordOptions = EvaluationAllRecordsManagedBean.JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS;
		messageAboutTask = null;
		
		/**** OPCIJE ODABIRA CASOPISA ********/
		evaluationTaskJournalsOptions = EvaluationAllRecordsManagedBean.ALL_JOURNALS;
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		journalsFromDate = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    journalsToDate = calendar.getTime();
		journalControlNumber = "";
    	journalList = new ArrayList<RecordDTO>();
		
    	/**** OPCIJE ODABIRA NAUCNO ISTRAZIVACKIH REZULTATA ********/
    	evaluationTaskResearchResultsOptions = EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS;
    	evaluationTaskResearchResultsCommissionOptions = EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS;
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    researchResultsFromDate = calendar.getTime();
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    researchResultsToDate = calendar.getTime();
	    researchResultControlNumber = "";
	    researchResultList = new ArrayList<RecordDTO>();

    	
    	//ocitavanje pravilnika i komisija
		dataSource = DataSourceFactory.getDataSource();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			
			/**** OPCIJE ODABIRA PRVILNIKA ********/
			ruleBookList = evaluationDB.getRuleBooks(conn);
			allRuleBookSelectItems = new ArrayList<SelectItem>();
	        allRuleBookSelectItems.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allRuleBooks")));
	        for (RuleBookDTO rb : ruleBookList){
	        	allRuleBookSelectItems.add(new SelectItem(rb, rb.toString()));
	        }
	        ruleBookId = null;
	        ruleBook = null;
	        
	        /**** OPCIJE ODABIRA KOMISIJA ********/
	        commissionList = commissionDAO.getCommissionsOrderdList();
	        selectedCommissionIDList = new ArrayList<Integer>();
	        //ukoliko treba inicijalizacija
			init=true;
			
		} catch (SQLException e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
//		System.out.println("Poziv resetForm metode");
		
		/**** ATRIBUTI POTREBNI ZA POZIVANJE NITI ********/
		//ako postoji nit za vrednovanje
		if(evaluationScheduledThread !=null){
			Date currentDate = new Date();
			//ako je vec zadatak izvrsen oslobodi resurse za novi zadatak
			if(currentDate.after(evaluationScheduledThread.taskDate) && evaluationScheduledThread.isActive()==false){
				stopActiveThread();
			}
			//u slucaju da nije izvrsen zadatak ce biti vidljiv tako da moze da se zaustavi
		}
		resetFormForEvaluationTask();
		
		return super.resetForm();
	}
	
	/**
	 * reset form for evaluation task
	 */
	public void resetFormForEvaluationTask() {
		Calendar calendar = Calendar.getInstance();
		/**** OPCIJE POZIVANJA ZADATKA ********/
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    taskDate = calendar.getTime();
		evaluationTaskTypeRecordOptions = EvaluationAllRecordsManagedBean.JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS;
		messageAboutTask = null;
		
		/**** OPCIJE ODABIRA CASOPISA ********/
		evaluationTaskJournalsOptions = EvaluationAllRecordsManagedBean.ALL_JOURNALS;
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		journalsFromDate = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    journalsToDate = calendar.getTime();
		
		journalControlNumber = "";
    	journalList.clear();
		
    	/**** OPCIJE ODABIRA NAUCNO ISTRAZIVACKIH REZULTATA ********/
    	evaluationTaskResearchResultsOptions = EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS;
    	evaluationTaskResearchResultsCommissionOptions = EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS;
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    researchResultsFromDate = calendar.getTime();
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    researchResultsToDate = calendar.getTime();
	    researchResultControlNumber = "";
	    researchResultList.clear();
	    
	    
	    /**** OPCIJE ODABIRA PRVILNIKA ********/
		ruleBookId = null;
		ruleBook = null;
		
		/**** OPCIJE ODABIRA KOMISIJA ********/
		selectedCommissionIDList.clear();
	}
	
	/**
	 * clear journal and research results options
	 */
	public void clearJournalAndResearchResultsOptions() {
		/**** OPCIJE ODABIRA CASOPISA ********/
		evaluationTaskJournalsOptions = EvaluationAllRecordsManagedBean.ALL_JOURNALS;
		clearJournalOptions();
		/**** OPCIJE ODABIRA NAUCNO ISTRAZIVACKIH REZULTATA ********/
    	evaluationTaskResearchResultsOptions = EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS;
    	if((evaluationTaskTypeRecordOptions != null) && (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_RESEARCH_CONFERENCE_RESULTS))){
			evaluationTaskResearchResultsCommissionOptions = EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_AUTHORS_COMMISSIONS;
		} else {
			evaluationTaskResearchResultsCommissionOptions = EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS;
		}
		clearResearchResultsOptions();
	}
	
	/**
	 * clear journal options
	 */
	public void clearJournalOptions() {
		Calendar calendar = Calendar.getInstance();
		
		/**** OPCIJE ODABIRA CASOPISA ********/
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
		journalsFromDate = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    journalsToDate = calendar.getTime();
		
		journalControlNumber = "";
    	journalList.clear();
	}
	
	/**
	 * clear journal and research results options
	 */
	public void clearResearchResultsOptions() {
		Calendar calendar = Calendar.getInstance();
    	/**** OPCIJE ODABIRA NAUCNO ISTRAZIVACKIH REZULTATA ********/
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    researchResultsFromDate = calendar.getTime();
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    researchResultsToDate = calendar.getTime();
	    researchResultControlNumber = "";
	    researchResultList.clear();
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "evaluationAllRecordsPage";
		return retVal;
	}
	
	public String enterPage() {
//		System.out.println("Poziv enterPage metode");
		init();
		resetForm();	
		returnPage = "indexPage";
		return "evaluationAllRecordsPage";
	}
	
	/************************ MOJE FUNKCIJE START *************************************/
	/**
	 * add selected journal
	 */
	public void addSelectedJouranlToList() {
		if(!journalControlNumber.startsWith("(BISIS)")){
			journalControlNumber = "";
			return;
		}
		
		RecordDTO record = recordDAO.getDTO(journalControlNumber);
		journalList.add(record);
		journalControlNumber = "";
	}
	
	/**
	 * remove selected journal
	 */
	public void removeSelectedJournalFromList() {
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String ordNum = facesCtx.getExternalContext()
					.getRequestParameterMap().get("ordNum");
			journalList.remove(Integer.parseInt(ordNum));
		} catch (Exception e) {
		}
	}	
	
	/**
	 * add selected ResearchResul
	 */
	public void addSelectedResearchResultToList() {
		if(!researchResultControlNumber.startsWith("(BISIS)")){
			researchResultControlNumber = "";
			return;
		}
		
		RecordDTO record = recordDAO.getDTO(researchResultControlNumber);
		researchResultList.add(record);
		researchResultControlNumber = "";
		if (!journalList.isEmpty()) {
			evaluationTaskJournalsOptions = EvaluationAllRecordsManagedBean.ALL_JOURNALS;
			clearJournalOptions();
		}
	}
	
	/**
	 * remove selected ResearchResul
	 */
	public void removeSelectedResearchResultFromList() {
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String ordNum = facesCtx.getExternalContext()
					.getRequestParameterMap().get("ordNum");
			researchResultList.remove(Integer.parseInt(ordNum));
		} catch (Exception e) {
		}
	}
	
	/**
	 * remove all ResearchResul
	 */
	public void removeAllResearchResulsFromList() {
		researchResultList.clear();
	}
	
	/**
	 * remove active tread
	 */
	public void stopActiveThread() {
		scheduleFuture.cancel(true);
		executorService = null;
		evaluationScheduledThread = null;
		scheduleFuture = null;
	}
	
	public static ScheduledExecutorService getScheduledExecutorService(){
		if(executorService == null){
			executorService = Executors.newScheduledThreadPool(1);
		}
		return executorService;
	}
	
	/**
	 * set evaluation Task
	 */
	public void setEvaluationTask() {
		
		for (RuleBookDTO pravilnik : ruleBookList) {
			if (pravilnik.getClassId().equals(ruleBookId)) {
				ruleBook = pravilnik;
			}
		}
		
		if(ruleBook == null){
			ruleBook = ruleBookList.get(0);
		}
		
		ArrayList<CommissionDTO> selectedCommissionList = new ArrayList<CommissionDTO>();
		
		for (int i = 0; i < selectedCommissionIDList.size(); i++) {
			selectedCommissionList.add(commissionDAO.getCommission(selectedCommissionIDList.get(i)+""));
		}
		
		evaluationScheduledThread = new EvaluationScheduledThread(taskDate, evaluationTaskTypeRecordOptions,  
				evaluationTaskJournalsOptions, journalsFromDate, journalsToDate, journalList,
				evaluationTaskResearchResultsOptions, evaluationTaskResearchResultsCommissionOptions, researchResultsFromDate, researchResultsToDate, researchResultList,
				ruleBook, selectedCommissionList);
		
		ScheduledExecutorService tempExecutorService = getScheduledExecutorService();
		scheduleFuture = (ScheduledFuture<EvaluationScheduledThread>) tempExecutorService.schedule(evaluationScheduledThread, taskDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		messageAboutTask = "Задатак покренут";
	}

	/************************ MOJE FUNKCIJE END *************************************/
	
	/**
	 * Performs the account activation
	 */
	public void enterPage(PhaseEvent event){
//		System.out.println("Poziv enterPage metode phase");
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
	 * @return the onlyJournals
	 */
	public String getOnlyJournals() {
		return ONLY_JOURNALS;
	}

	/**
	 * @return the onlyResearchResultsForSelectedJournals
	 */
	public String getOnlyResearchResultsForSelectedJournals() {
		return ONLY_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS;
	}
	
	/**
	 * @return the onlyResearchResultsForSelectedJournals
	 */
	public String getOnlyResearchConferenceResults() {
		return ONLY_RESEARCH_CONFERENCE_RESULTS;
	}

	/**
	 * @return the journalsAndResearchResultsForSelectedJournals
	 */
	public String getJournalsAndResearchResultsForSelectedJournals() {
		return JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS;
	}

	/**
	 * @return the allJournals
	 */
	public String getAllJournals() {
		return ALL_JOURNALS;
	}

	/**
	 * @return the journalsModifiedWithinTimeRange
	 */
	public String getJournalsModifiedWithinTimeRange() {
		return JOURNALS_MODIFIED_WITHIN_TIME_RANGE;
	}

	/**
	 * @return the selectedJournals
	 */
	public String getSelectedJournals() {
		return SELECTED_JOURNALS;
	}

	/**
	 * @return the allResearchResults
	 */
	public String getAllResearchResults() {
		return ALL_RESEARCH_RESULTS;
	}

	/**
	 * @return the researchResultsModifiedWithinTimeRange
	 */
	public String getResearchResultsModifiedWithinTimeRange() {
		return RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE;
	}

	/**
	 * @return the selectedResearchResults
	 */
	public String getSelectedResearchResults() {
		return SELECTED_RESEARCH_RESULTS;
	}

	/**
	 * @return the evaluationScheduledThread
	 */
	public EvaluationScheduledThread getEvaluationScheduledThread() {
		return evaluationScheduledThread;
	}

	/**
	 * @param evaluationScheduledThread the evaluationScheduledThread to set
	 */
	public void setEvaluationScheduledThread(
			EvaluationScheduledThread evaluationScheduledThread) {
		EvaluationAllRecordsManagedBean.evaluationScheduledThread = evaluationScheduledThread;
	}

	/**
	 * @return the messageAboutTask
	 */
	public String getMessageAboutTask() {
		return messageAboutTask;
	}

	/**
	 * @param messageAboutTask the messageAboutTask to set
	 */
	public void setMessageAboutTask(String messageAboutTask) {
		this.messageAboutTask = messageAboutTask;
	}

	/**
	 * @return the taskDate
	 */
	public Date getTaskDate() {
		return taskDate;
	}

	/**
	 * @param taskDate the taskDate to set
	 */
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	/**
	 * @return the evaluationTaskTypeRecordOptions
	 */
	public String getEvaluationTaskTypeRecordOptions() {
		return evaluationTaskTypeRecordOptions;
	}

	/**
	 * @param evaluationTaskTypeRecordOptions the evaluationTaskTypeRecordOptions to set
	 */
	public void setEvaluationTaskTypeRecordOptions(
			String evaluationTaskTypeRecordOptions) {
		this.evaluationTaskTypeRecordOptions = evaluationTaskTypeRecordOptions;
	}

	/**
	 * @return the evaluationTaskJournalsOptions
	 */
	public String getEvaluationTaskJournalsOptions() {
		return evaluationTaskJournalsOptions;
	}

	/**
	 * @param evaluationTaskJournalsOptions the evaluationTaskJournalsOptions to set
	 */
	public void setEvaluationTaskJournalsOptions(
			String evaluationTaskJournalsOptions) {
		this.evaluationTaskJournalsOptions = evaluationTaskJournalsOptions;
	}

	/**
	 * @return the journalsFromDate
	 */
	public Date getJournalsFromDate() {
		return journalsFromDate;
	}

	/**
	 * @param journalsFromDate the journalsFromDate to set
	 */
	public void setJournalsFromDate(Date journalsFromDate) {
		this.journalsFromDate = journalsFromDate;
	}

	/**
	 * @return the journalsToDate
	 */
	public Date getJournalsToDate() {
		return journalsToDate;
	}

	/**
	 * @param journalsToDate the journalsToDate to set
	 */
	public void setJournalsToDate(Date journalsToDate) {
		this.journalsToDate = journalsToDate;
	}

	/**
	 * @return the journalControlNumber
	 */
	public String getJournalControlNumber() {
		return journalControlNumber;
	}

	/**
	 * @param journalControlNumber the journalControlNumber to set
	 */
	public void setJournalControlNumber(String journalControlNumber) {
		this.journalControlNumber = journalControlNumber;
	}

	/**
	 * @return the journalList
	 */
	public List<RecordDTO> getJournalList() {
		return journalList;
	}

	/**
	 * @param journalList the journalList to set
	 */
	public void setJournalList(List<RecordDTO> journalList) {
		this.journalList = journalList;
	}

	/**
	 * @return the evaluationTaskResearchResultsOptions
	 */
	public String getEvaluationTaskResearchResultsOptions() {
		return evaluationTaskResearchResultsOptions;
	}

	/**
	 * @param evaluationTaskResearchResultsOptions the evaluationTaskResearchResultsOptions to set
	 */
	public void setEvaluationTaskResearchResultsOptions(
			String evaluationTaskResearchResultsOptions) {
		this.evaluationTaskResearchResultsOptions = evaluationTaskResearchResultsOptions;
	}

	/**
	 * @return the researchResultsFromDate
	 */
	public Date getResearchResultsFromDate() {
		return researchResultsFromDate;
	}

	/**
	 * @param researchResultsFromDate the researchResultsFromDate to set
	 */
	public void setResearchResultsFromDate(Date researchResultsFromDate) {
		this.researchResultsFromDate = researchResultsFromDate;
	}

	/**
	 * @return the researchResultsToDate
	 */
	public Date getResearchResultsToDate() {
		return researchResultsToDate;
	}

	/**
	 * @param researchResultsToDate the researchResultsToDate to set
	 */
	public void setResearchResultsToDate(Date researchResultsToDate) {
		this.researchResultsToDate = researchResultsToDate;
	}

	/**
	 * @return the researchResultControlNumber
	 */
	public String getResearchResultControlNumber() {
		return researchResultControlNumber;
	}

	/**
	 * @param researchResultControlNumber the researchResultControlNumber to set
	 */
	public void setResearchResultControlNumber(String researchResultControlNumber) {
		this.researchResultControlNumber = researchResultControlNumber;
	}

	/**
	 * @return the researchResultList
	 */
	public List<RecordDTO> getResearchResultList() {
		return researchResultList;
	}

	/**
	 * @param researchResultList the researchResultList to set
	 */
	public void setResearchResultList(List<RecordDTO> researchResultList) {
		this.researchResultList = researchResultList;
	}

	/**
	 * @return the commissionList
	 */
	public List<CommissionDTO> getCommissionList() {
		return commissionList;
	}

	/**
	 * @param commissionList the commissionList to set
	 */
	public void setCommissionList(List<CommissionDTO> commissionList) {
		this.commissionList = commissionList;
	}

	/**
	 * @return the selectedCommissionIDList
	 */
	public List<Integer> getSelectedCommissionIDList() {
		return selectedCommissionIDList;
	}

	/**
	 * @param selectedCommissionIDList the selectedCommissionIDList to set
	 */
	public void setSelectedCommissionIDList(List<Integer> selectedCommissionIDList) {
		this.selectedCommissionIDList = selectedCommissionIDList;
	}

	/**
	 * @return the ruleBookId
	 */
	public String getRuleBookId() {
		return ruleBookId;
	}

	/**
	 * @param ruleBookId the ruleBookId to set
	 */
	public void setRuleBookId(String ruleBookId) {
		this.ruleBookId = ruleBookId;
	}

	/**
	 * @return the ruleBookList
	 */
	public List<RuleBookDTO> getRuleBookList() {
		return ruleBookList;
	}

	/**
	 * @param ruleBookList the ruleBookList to set
	 */
	public void setRuleBookList(List<RuleBookDTO> ruleBookList) {
		this.ruleBookList = ruleBookList;
	}

	/**
	 * @return the evaluationTaskResearchResultsCommissionOptions
	 */
	public String getEvaluationTaskResearchResultsCommissionOptions() {
		return evaluationTaskResearchResultsCommissionOptions;
	}

	/**
	 * @param evaluationTaskResearchResultsCommissionOptions the evaluationTaskResearchResultsCommissionOptions to set
	 */
	public void setEvaluationTaskResearchResultsCommissionOptions(
			String evaluationTaskResearchResultsCommissionOptions) {
		this.evaluationTaskResearchResultsCommissionOptions = evaluationTaskResearchResultsCommissionOptions;
	}

	/**
	 * @return the evaluateResearchResultsBySelectedCommissions
	 */
	public String getEvaluateResearchResultsBySelectedCommissions() {
		return EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS;
	}

	/**
	 * @return the evaluateResearchResultsByAuthorsCommissions
	 */
	public String getEvaluateResearchResultsByAuthorsCommissions() {
		return EVALUATE_RESEARCH_RESULTS_BY_AUTHORS_COMMISSIONS;
	}
}