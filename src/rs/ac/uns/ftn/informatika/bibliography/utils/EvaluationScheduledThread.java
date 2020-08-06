package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.EvaluationAllRecordsManagedBean;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;

public class EvaluationScheduledThread implements Runnable{

	private static Log log = LogFactory.getLog(EvaluationScheduledThread.class.getName());
	
	private String XTMLRepresentation = null;
	public RecordDAO recordDAO = new RecordDAO(new RecordDB());
	public CommissionDAO commissionDAO = new CommissionDAO();
	
	public Date taskDate = null;
	// pogeldaj EvaluationAllRecordsManagedBean
	private String evaluationTaskTypeRecordOptions = null;
	private String evaluationTaskJournalsOptions = null;
	private String evaluationTaskResearchResultsOptions = null;
	private String evaluationTaskResearchResultsCommissionOptions = null;
	
	private Date journalsFromDate= null;
	private Date journalsToDate= null;
	private ArrayList<String> journalIDList = null;
	private ArrayList<Record> journalLoadedList = null;
	
	private Date researchResultsFromDate= null;
	private Date researchResultsToDate= null;
	private ArrayList<String> researchResultsIDList = null;
	private ArrayList<Record> researchResultsLoadedList = null;
	
	private RuleBookDTO ruleBook = null;
	private ArrayList<CommissionDTO> commissionList = null;
	
	private boolean active = false;

	public EvaluationScheduledThread(Date taskDate, String evaluationTaskTypeRecordOptions,
			String evaluationTaskJournalsOptions, Date journalsFromDate, Date journalsToDate, List<RecordDTO> journalList,
			String evaluationTaskResearchResultsOptions, String evaluationTaskResearchResultsCommissionOptions, Date researchResultsFromDate, Date researchResultsToDate, List<RecordDTO> researchResultsList,
			RuleBookDTO ruleBook, List<CommissionDTO> selectedCommissionList) {
		super();
		this.taskDate = taskDate;
		this.evaluationTaskTypeRecordOptions = evaluationTaskTypeRecordOptions;
		this.evaluationTaskJournalsOptions = evaluationTaskJournalsOptions;
		this.journalsFromDate = journalsFromDate;
		this.journalsToDate = journalsToDate;
		journalIDList = new ArrayList<String>();
		for (RecordDTO journalTemp : journalList) {
			journalIDList.add(journalTemp.getControlNumber());
		}
		this.evaluationTaskResearchResultsOptions = evaluationTaskResearchResultsOptions;
		this.evaluationTaskResearchResultsCommissionOptions = evaluationTaskResearchResultsCommissionOptions;
		this.researchResultsFromDate = researchResultsFromDate;
		this.researchResultsToDate = researchResultsToDate;
		researchResultsIDList = new ArrayList<String>();
		for (RecordDTO researchResultTemp : researchResultsList) {
			researchResultsIDList.add(researchResultTemp.getControlNumber());
		}	
		this.ruleBook = ruleBook;
		this.commissionList = (ArrayList<CommissionDTO>) selectedCommissionList;
		
		formHTMLRepresentationFixed(taskDate, evaluationTaskTypeRecordOptions, 
				evaluationTaskJournalsOptions, journalsFromDate, journalsToDate, journalList, 
				evaluationTaskResearchResultsOptions, researchResultsFromDate, researchResultsToDate, researchResultsList, 
				ruleBook, commissionList);
	}
	
	@Override
	public void run() {	
		
		active = true;
		log.info("NIT ZA EVALUACIJU POKRENUTA--------------------------------------------------------------");
		
		boolean retVal = true;
		
		if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_JOURNALS))
			retVal =evaluateOnlyJournals();
		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS))
			retVal =evaluateOnlyResearchResultsForSelectedJournals();
		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_RESEARCH_CONFERENCE_RESULTS))
			retVal =evaluateOnlyResearchConferenceResults();
//		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS))
//			retVal=evaluateJournalsAndResearchResultsForSelectedJournals();
		
		if (retVal == false) {
			log.error("NIT ZA EVALUACIJU ZAVRSILA SA GRESKOM------------------------------------------------");
		}
		else{
			log.info("NIT ZA EVALUACIJU USPESNO SE ZAVRSILA-------------------------------------------------");
		}
		active = false;
	}

	private boolean evaluateOnlyJournals(){
		boolean retVal = true;
		retVal = clearEvaluationForJournals();
		if (retVal==false)
			return false;
		retVal = createEvaluationForJournals();
		return retVal;
	}
	
	private boolean clearEvaluationForJournals(){

		boolean retVal = true;
		String[] typesJournal = {Types.JOURNAL};
		String opcijaCasopisa = "";
		
		if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.ALL_JOURNALS)) {
			//obrisi evaluacije za casopise
			log.info("PREUZIMANJE LISTE JOURNAL Type");
			retVal = commissionDAO.removeEvaluationsByCommisionForRecords(typesJournal, null, commissionList);
			Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
			journalLoadedList = (ArrayList<Record>) recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
			log.info("LISTA JOURNAL Type KOJI SE BRISE JE:"+journalLoadedList.size());
			//retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, journalLoadedList, commissionList);
			opcijaCasopisa = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allJournals");
			
		}
		else if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.JOURNALS_MODIFIED_WITHIN_TIME_RANGE)) {
			//ocitaj casopise
			log.info("PREUZIMANJE LISTE JOURNAL U INTERVALU");
			journalLoadedList = (ArrayList<Record>) recordDAO.getRecordsFromDatabaseOfCertainTypeWithinDateRange(typesJournal, journalsFromDate, journalsToDate);
			//obrisi evaluacije za casopise
			log.info("LISTA JOURNAL U INTERVALU KOJI SE BRISE JE:"+journalLoadedList.size());
			retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, journalLoadedList, commissionList);
			opcijaCasopisa = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.journalsModifiedWithinTimeRange");
		}
		else if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_JOURNALS)) {
			//ocitaj casopise
			log.info("PREUZIMANJE LISTE JOURNAL IDs");
			journalLoadedList = (ArrayList<Record>) recordDAO.getRecords(journalIDList);
			//obrisi evaluacije za casopise
			log.info("LISTA JOURNAL SELEKTOVANI KOJI SE BRISE JE:"+journalLoadedList.size());
			retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, journalLoadedList, commissionList);
			opcijaCasopisa = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedJournals");
		}
		if( retVal == false)
			log.fatal("NEUSPELO brisanje evaluacija za opciju " + opcijaCasopisa + " ------------------------------------------------------------");
		else
			log.info("USPELO brisanje evaluacija za opciju " +opcijaCasopisa + " ------------------------------------------------------------");
		
		return retVal;
	}
	
	private boolean createEvaluationForJournals(){
		boolean retVal = true;
		if (journalLoadedList== null || journalLoadedList.isEmpty()){
			log.error("LISTA JOURNAL KOJI SE EVALUIRA JE PRAZNA");
			return false;
		}
		retVal = commissionDAO.createEvaluationsByCommisionForRecords(journalLoadedList, commissionList, ruleBook);
		if(retVal == false)
			log.fatal("NEUSPELO kreiranje evaluacija za JOURNAL");
		else 
			log.info("USPELO kreiranje evaluacija za JOURNAL");
		
		return retVal;
	}
	
	private boolean evaluateOnlyResearchResultsForSelectedJournals(){
		boolean retVal = true;
		retVal = clearEvaluationForResearchResults();
		if (retVal==false)
			return false;
		
		retVal = createEvaluationResearchResults();
		return retVal;
	}
	
	private boolean evaluateOnlyResearchConferenceResults(){
		boolean retVal = true;
		retVal = clearEvaluationForResearchConferenceResults();
		if (retVal==false)
			return false;
		
		retVal = createEvaluationResearchConferenceResults();
		return retVal;
	}
	
	private boolean clearEvaluationForResearchResults(){
		String[] typesPaperJournal = {Types.PAPER_JOURNAL, Types.SCIENTIFIC_CRITICISM_JOURNAL, Types.OTHER_JOURNAL};
		boolean retVal = true;
		String opcijaRezultata = "";
		
		if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.ALL_JOURNALS)) {
			if(evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS)){
				BooleanQuery type = new BooleanQuery();
				type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
				BooleanQuery allResearchResultQuery = new BooleanQuery();
				allResearchResultQuery.add(type, Occur.MUST);
				researchResultsLoadedList = (ArrayList<Record>) recordDAO.getDTOs(allResearchResultQuery, new AllDocCollector(false));
				//obrisi evaluacije za radove u casopisima
				if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
					retVal = commissionDAO.removeEvaluationsByCommisionForRecords(typesPaperJournal, null, commissionList);
				else{
					retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
				}
				opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allJournals") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allResearchResults");
			}
			else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE)) {
				//ocitaj evaluacije za radove u casopisima
				researchResultsLoadedList = (ArrayList<Record>) recordDAO.getRecordsFromDatabaseOfCertainTypeWithinDateRange(typesPaperJournal, researchResultsFromDate, researchResultsToDate);
				//obrisi evaluacije za radove u casopisima
				if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
					retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, researchResultsLoadedList, commissionList);
				else{
					retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
				}
				opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allJournals") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsModifiedWithinTimeRange");
			}
			else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_RESEARCH_RESULTS)) {
				//ocitaj evaluacije za radove u casopisima
				researchResultsLoadedList = (ArrayList<Record>) recordDAO.getRecords(researchResultsIDList);
				//obrisi evaluacije za radove u casopisima
				if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
					retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, researchResultsLoadedList, commissionList);
				else{
					retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
				}
				opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allJournals") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedResearchResults");	
			}
		}
		else if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_JOURNALS)){
			if(journalIDList.isEmpty()){
				return true;
			}
			
			if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS)) {
				for (int i = 0; i < journalIDList.size(); i++) {
					String jouranlID = journalIDList.get(i);
					
					BooleanQuery allResearchResultQuery = new BooleanQuery();
					BooleanQuery type = new BooleanQuery();
					type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
					
					allResearchResultQuery.add(type, Occur.MUST);
					allResearchResultQuery.add(new TermQuery(new Term("JOCN", jouranlID)), Occur.MUST);
					
					if(researchResultsLoadedList == null)
						researchResultsLoadedList = (ArrayList<Record>) recordDAO.getDTOs(allResearchResultQuery, new AllDocCollector(false));
					else
						researchResultsLoadedList.addAll(recordDAO.getDTOs(allResearchResultQuery, new AllDocCollector(false)));
				}
				
				//obrisi evaluacije za radove u casopisima
				if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
					retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, researchResultsLoadedList, commissionList);
				else{
					retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
				}
				opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedJournals") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allResearchResults");
			}
			else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE)) {
				//ocitaj evaluacije za radove u casopisima
				researchResultsLoadedList = (ArrayList<Record>) recordDAO.getRecordsFromDatabaseOfCertainTypeWithinDateRangeWhoAreReletedToRecords(typesPaperJournal, researchResultsFromDate, researchResultsToDate, journalIDList);
				//obrisi evaluacije za radove u casopisima
				if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
					retVal = commissionDAO.removeEvaluationsByCommisionForRecords(null, researchResultsLoadedList, commissionList);
				else{
					retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
				}
				opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedJournals") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsModifiedWithinTimeRange");
			}
		}
		
		if( retVal == false)
			log.fatal("NEUSPELO brisanje evaluacija za opciju " + opcijaRezultata + " ---------------------------------------------------------");
		else 
			log.info("USPELO brisanje evaluacija za opciju " + opcijaRezultata + " ---------------------------------------------------------");
		
		return retVal;
	}
	
	private boolean clearEvaluationForResearchConferenceResults(){
		String[] typesPaperProceeding = {Types.ABSTRACT_PAPER_PROCEEDINGS, Types.DISCUSSION_PAPER_PROCEEDINGS, Types.FULL_PAPER_PROCEEDINGS, Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS, Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS, Types.PAPER_PROCEEDINGS};
		boolean retVal = true;
		String opcijaRezultata = "";
		
		if(evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS)){
			BooleanQuery type = new BooleanQuery();
			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
			BooleanQuery allResearchResultQuery = new BooleanQuery();
			allResearchResultQuery.add(type, Occur.MUST);
			researchResultsLoadedList = (ArrayList<Record>) recordDAO.getDTOs(allResearchResultQuery, new AllDocCollector(false));
			//obrisi evaluacije za radove u zbornicima
			retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
			opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allConferenceProceedings") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allResearchResults");
		} else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE)) {
			//ocitaj evaluacije za radove u zbornicima
			researchResultsLoadedList = (ArrayList<Record>) recordDAO.getRecordsFromDatabaseOfCertainTypeWithinDateRange(typesPaperProceeding, researchResultsFromDate, researchResultsToDate);
			//obrisi evaluacije za radove u zbornicima
			retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
			opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allConferenceProceedings") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsModifiedWithinTimeRange");
		} else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_RESEARCH_RESULTS)) {
			//ocitaj evaluacije za radove u zbornicima
			researchResultsLoadedList = (ArrayList<Record>) recordDAO.getRecords(researchResultsIDList);
			//obrisi evaluacije za radove u zbornicima
			retVal = commissionDAO.removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(researchResultsLoadedList);
			opcijaRezultata = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allConferenceProceedings") + " ; "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedResearchResults");	
		}
		
		if( retVal == false)
			log.fatal("NEUSPELO brisanje evaluacija za opciju " + opcijaRezultata + " ---------------------------------------------------------");
		else 
			log.info("USPELO brisanje evaluacija za opciju " + opcijaRezultata + " ---------------------------------------------------------");
		
		return retVal;
	}
	
	private boolean createEvaluationResearchResults(){
		boolean retVal = true;
		if (researchResultsLoadedList== null || researchResultsLoadedList.isEmpty()){
			log.error("LISTA RESEARCH RESULTS KOJI SE EVALUIRA JE PRAZNA");
			return false;
		}
		
		if(evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
			retVal = commissionDAO.createEvaluationsByCommisionForRecords(researchResultsLoadedList, commissionList, ruleBook);
		else
			retVal = commissionDAO.createEvaluationsByCommisionFromAuthorsAfiliationForRecords(researchResultsLoadedList, ruleBook);
			
		if(retVal == false)
			log.fatal("NEUSPELO kreiranje evaluacija za RESEARCH RESULTS");
		else 
			log.info("USPELO kreiranje evaluacija za RESEARCH RESULTS");
		
		return retVal;
	}
	
	private boolean createEvaluationResearchConferenceResults(){
		boolean retVal = true;
		if (researchResultsLoadedList== null || researchResultsLoadedList.isEmpty()){
			log.error("LISTA RESEARCH RESULTS KOJI SE EVALUIRA JE PRAZNA");
			return false;
		}
		
		retVal = commissionDAO.createEvaluationsByCommisionFromAuthorsAfiliationForRecords(researchResultsLoadedList, ruleBook);
			
		if(retVal == false)
			log.fatal("NEUSPELO kreiranje evaluacija za RESEARCH RESULTS");
		else 
			log.info("USPELO kreiranje evaluacija za RESEARCH RESULTS");
		
		return retVal;
	}
	
	
	private boolean evaluateJournalsAndResearchResultsForSelectedJournals(){
		boolean retVal = true;
		retVal = evaluateOnlyJournals();
		if (retVal==false)
			return false;
		retVal = evaluateOnlyResearchResultsForSelectedJournals();
		if (retVal==false)
			return false;
		return retVal;
	}
	
	private void formHTMLRepresentationFixed(Date taskDate, String evaluationTaskTypeRecordOptions,
			String evaluationTaskJournalsOptions, Date journalsFromDate, Date journalsToDate, List<RecordDTO> journalList,
			String evaluationTaskResearchResultsOptions, Date researchResultsFromDate, Date researchResultsToDate, List<RecordDTO> researchResultsList,
			RuleBookDTO ruleBook, List<CommissionDTO> commissionList){
		
		String typeRecordOptions = "";
		if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_JOURNALS))
			typeRecordOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.onlyJournals");
		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS))
			typeRecordOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.onlyResearchResultsForSelectedJournals");
		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.ONLY_RESEARCH_CONFERENCE_RESULTS))
			typeRecordOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.onlyResearchConferenceResults");
		else if (evaluationTaskTypeRecordOptions.equals(EvaluationAllRecordsManagedBean.JOURNALS_AND_RESEARCH_RESULTS_FOR_SELECTED_JOURNALS))
			typeRecordOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.journalsAndResearchResultsForSelectedJournals");
		else {
			typeRecordOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.errorMessage");
		}
		
		String journalOptions = "";
		if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.ALL_JOURNALS))
			journalOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allJournals");
		else if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.JOURNALS_MODIFIED_WITHIN_TIME_RANGE)){
			journalOptions = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.journalsModifiedWithinTimeRange") + " "
					+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.fromDate") + " {0} "
					+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.toDate") + " {1} ", journalsFromDate.toString(), journalsToDate.toString());
		}
		else if (evaluationTaskJournalsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_JOURNALS)){
			journalOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedJournals");
			
			for (int i = 0; i<journalList.size();i++) {
				if(journalList.get(i) instanceof JournalDTO){
					JournalDTO dto = (JournalDTO) journalList.get(i);
					journalOptions += ("\""+ dto.getSomeName()+"\"");
					if(i<journalList.size()-1)
						journalOptions += ", ";
				}
			}
		}
		else {
			journalOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.errorMessage");
		}
		
		String researchResultsOptions = "";
		if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.ALL_RESEARCH_RESULTS))
			researchResultsOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.allResearchResults");
		else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.RESEARCH_RESULTS_MODIFIED_WITHIN_TIME_RANGE)){
			researchResultsOptions = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsModifiedWithinTimeRange") + " "
					+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.fromDate") + " {0} "
					+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.toDate") + " {1} ", researchResultsFromDate.toString(), researchResultsToDate.toString());
		}
		else if (evaluationTaskResearchResultsOptions.equals(EvaluationAllRecordsManagedBean.SELECTED_RESEARCH_RESULTS)){
			researchResultsOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.selectedResearchResults");
			
			for (int i = 0; i<researchResultsList.size();i++) {
				if(researchResultsList.get(i) instanceof PaperJournalDTO){
					PaperJournalDTO dto = (PaperJournalDTO) researchResultsList.get(i);
					researchResultsOptions+="\""+ dto+"\"";
					if(i<researchResultsList.size()-1)
						researchResultsOptions+=", ";
				}
			}
		}
		else {
			researchResultsOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.errorMessage");
		}
		String researchResultsCommisionOptions = "";
		
		if (evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_SELECTED_COMMISSIONS))
			researchResultsCommisionOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsBySelectedCommissions");
		else if (evaluationTaskResearchResultsCommissionOptions.equals(EvaluationAllRecordsManagedBean.EVALUATE_RESEARCH_RESULTS_BY_AUTHORS_COMMISSIONS))
			researchResultsCommisionOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.researchResultsByAuthorsCommissions");
		else {
			researchResultsCommisionOptions = facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.errorMessage");
		}
		
		String commisionNameList = "";
		for (int i = 0; i<commissionList.size();i++) {
			commisionNameList+=commissionList.get(i);
			if(i<commissionList.size()-1)
				commisionNameList+=", ";
		}
		
		XTMLRepresentation = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.evaluationAllRecords.threadHTMLRepresentation"), taskDate.toString(), 
				typeRecordOptions, journalOptions, researchResultsOptions, researchResultsCommisionOptions, ruleBook.getClassId(), commisionNameList);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		return XTMLRepresentation;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	FacesMessages facesMessages = new FacesMessages("messages.messages-evaluation", new Locale("en"));
}
