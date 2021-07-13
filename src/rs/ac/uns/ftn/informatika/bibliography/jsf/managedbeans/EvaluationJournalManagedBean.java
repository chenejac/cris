package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.AbstractCommissionEvaluation;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommissionFactory;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

public class EvaluationJournalManagedBean extends CRUDManagedBean implements IPickJournalManagedBean{

	int firstEvaluationYear = -1;
	int lastEvaluationYear = -1;
	
	private List<CommissionDTO> commissionList = null;
	private List<RuleBookDTO> ruleBookList = null;
	private List<SelectItem> allRuleBooks;
	private List<SelectItem> allCommissions;
	
	private JournalDTO selectedJournal = null;
	private String journalControlNUmber = "";
	private String journalISSN = "";
	private String journalSomeName = "";
	private Integer year = null;
	private RuleBookDTO ruleBook = null;
	private CommissionDTO commission = null;
	private Integer commissionID = null;
	
	private String pdfUrl = null;
	
	private CommissionDAO commisionDAO = new CommissionDAO();
	
	private EvaluationDB evaluationDB = new EvaluationDB();
	private DataSource dataSource = null;
	
	private List<String []> evaluatedResults = null;
	
	private boolean showAllImpactFactors = false;
	
	private JournalEvaluationResult evaluatedResult = null;
	
	private List<ImpactFactor> impactFactors = null;
	private List<ImpactFactor> allImpactFactors = null;
	private List<String []> evaluatedResultsLegendCategories = null;
	private List<Map<String, String>> evaluatedResultsImpactFactorsWithCategoriesTwoYears = null;
	private List<String> evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears = null;
	private List<Map<String, String>> evaluatedResultsImpactFactorsWithCategoriesFiveYears = null;
	private List<String> evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears = null;
	private List<String []> evaluatedResultsAllYearsTwoYears = null;
	private List<String []> evaluatedResultsAllYearsFiveYears = null;
	private String detailExplanation = "";
	
	public EvaluationJournalManagedBean() {
		super();
		init();	
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initialized 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void init ()
	{
		//ukoliko treba inicijalizacija
		init=true;
		
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		lastEvaluationYear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
		
		evaluatedResults = new ArrayList<String[]>();
		evaluatedResult = null;
		evaluatedResultsLegendCategories = new ArrayList<String[]>();
		evaluatedResultsImpactFactorsWithCategoriesTwoYears = new ArrayList<Map<String, String>>();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears = new ArrayList<String>();
		evaluatedResultsImpactFactorsWithCategoriesFiveYears = new ArrayList<Map<String, String>>();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears = new ArrayList<String>();
		evaluatedResultsAllYearsTwoYears = new ArrayList<String[]>();
		evaluatedResultsAllYearsFiveYears = new ArrayList<String[]>();
		detailExplanation = "";
		
		dataSource = DataSourceFactory.getDataSource();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			allRuleBooks = new ArrayList<SelectItem>();
	        ruleBookList = evaluationDB.getRuleBooks(conn);
	        allRuleBooks.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("records.evaluation.pleaseSelectRuleBook")));
	        for (RuleBookDTO rb : ruleBookList){
	        	allRuleBooks.add(new SelectItem(rb, rb.toString()));
	        }
	        
			allCommissions = new ArrayList<SelectItem>();
	        commissionList = commisionDAO.getCommissionsOrderdList();
	        allCommissions.add(new SelectItem(null, facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.pleaseSelectCommission")));
	        //Tehnoloski fakultet
	        
	        for (CommissionDTO com : commissionList){
	        	if((com.getCommissionId()>=711) && (com.getCommissionId()<=715)){
	        		allCommissions.add(new SelectItem(com.getCommissionId(), facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.name.id"+com.getCommissionId())));
//	        		commissionID = com.getCommissionId();
//	        		commission = com;
//	        		setCommissionRuleBook();
	        	}
	        }
	        
		} catch (SQLException e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		
		
	}
	
	public void loadSerbianRuleBook() {
		for (RuleBookDTO rb : ruleBookList) {
        	if((("("+rb.getSchemeId()+") " + rb.getClassId()).equals("(ruleBook) serbianResearchersEvaluation"))){
				ruleBook = rb;
				break;
			}
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedJournal = null;
		journalControlNUmber = "";
		journalISSN = "";
		journalSomeName = "";
		year=null;
		ruleBook = null;
		commission = null;
		commissionID = null;
		pdfUrl = null;
		init = false;
		evaluatedResults.clear();
		evaluatedResult = null;
		evaluatedResultsLegendCategories.clear();
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesFiveYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears.clear();
		evaluatedResultsAllYearsTwoYears.clear();
		evaluatedResultsAllYearsFiveYears.clear();
		detailExplanation = "";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "evaluationJournalPage";
		return retVal;
	}
	
	
	public String enterPage() {
		resetForm();
		init();
		pickJournal();
		returnPage = "indexPage";
		return "evaluationJournalPage";
	}
	
	/**
	 * Performs the account activation
	 */
	public void enterPage(PhaseEvent event){
		if(init == false){
			resetForm();
			init();
			pickJournal();
		}
	}

	@Override
	public void populateList() {
		// TODO Auto-generated method stub
		//nista ne radimo
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//nista ne radimo
		
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		//nista ne radimo
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		//nista ne radimo
	}

	@Override
	public String finishWizard() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private JournalManagedBean journalManagedBean = null;

	/**
	 * @return the PaperJournalManagedBean from current session
	 */
	protected JournalManagedBean getJournalManagedBean() {
		if (journalManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			JournalManagedBean mb = (JournalManagedBean) extCtx.getSessionMap().get(
					"journalManagedBean");
			if (mb == null) {
				mb = new JournalManagedBean();
				extCtx.getSessionMap().put("journalManagedBean", mb);
			}
			journalManagedBean = mb;
		}
		return journalManagedBean;
	}

	/**
	 * Prepares web form where user can choose Journal
	 */
	public void pickJournal() {
		getJournalManagedBean();
		journalManagedBean.setIPickJournalManagedBean(this);
		journalManagedBean.setSelectedJournal(null);
		journalManagedBean.setCustomPick(false);
		journalManagedBean.switchToPickMode();
		journalManagedBean.switchToTableNoneMode();
	}
	/**
	 * Sets ALL from selected journal
	 */
	public void setCommissionRuleBook() {

		if(commissionID!=null)
			pdfUrl = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.rulebook.id"+commissionID.intValue());
		else
			pdfUrl = null;
		
		evaluatedResults.clear();
		evaluatedResult = null;
		evaluatedResultsLegendCategories.clear();
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesFiveYears.clear();
		evaluatedResultsAllYearsTwoYears.clear();
		evaluatedResultsAllYearsFiveYears.clear();
		detailExplanation = "";
	}
	/**
	 * Sets ALL from selected journal
	 */
	public void setAllFromJournal() {

		if(journalSomeName.indexOf("(BISIS)") != -1){
			journalControlNUmber = journalSomeName;
		 }
		
		if(journalISSN.indexOf("(BISIS)") != -1){
			journalControlNUmber = journalISSN;
		 }
		
		journalSomeName ="";
		journalISSN="";
		
		getJournalManagedBean();
		
		journalManagedBean.setJournalControlNumber(journalControlNUmber);
		
		selectedJournal = journalManagedBean.getSelectedJournal();
		if(selectedJournal!=null)
		{
			journalISSN = selectedJournal.getIssn();
			journalControlNUmber = selectedJournal.getControlNumber();
			journalSomeName = selectedJournal.getSomeName();
		}
		evaluatedResults.clear();
		evaluatedResult = null;
		evaluatedResultsLegendCategories.clear();
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesFiveYears.clear();
		evaluatedResultsAllYearsTwoYears.clear();
		evaluatedResultsAllYearsFiveYears.clear();
		detailExplanation = "";
	}
	
	public String impactFactorsForAllYears(){
//		Connection conn = null;
//		try {
//			conn = dataSource.getConnection();
//			MetricsDB metricsDB = new MetricsDB();
//			List<ImpactFactor> allImpactFactors = metricsDB.getJournalImpactFactors(conn, selectedJournal.getControlNumber(), "twoYearsIF");
			List<ImpactFactor> allImpactFactorsExtended = new ArrayList<ImpactFactor>();
			if(allImpactFactors!=null && !allImpactFactors.isEmpty()){
				Collections.sort(allImpactFactors, new GenericComparator<ImpactFactor>(
						"year", "asc"));
				for(int startYear = allImpactFactors.get(0).getYear(); startYear < (allImpactFactors.get(allImpactFactors.size()-1).getYear()+1); startYear++){
					ImpactFactor tempIF = new ImpactFactor(startYear, null, new ArrayList<ResearchAreaRanking>(), null, new ArrayList<ResearchAreaRanking>());
					for (ImpactFactor impactFactor : allImpactFactors) {
						if(impactFactor.getYear().intValue() == startYear){
								tempIF = impactFactor;
								break;
						}
					}
					allImpactFactorsExtended.add(tempIF);
				}
			}
			calculateExplainTable(allImpactFactorsExtended, true, true);
			showAllImpactFactors = true;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//		} 
//		finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//			}
//		}
		return dispetcher(0);
	}
	
	public String evaluateSelectedJournalForCommissionAndYear() {
		
//		System.out.println("metoda evaluate pokrenuta*************************************************************************");
		loadSerbianRuleBook();
		for (CommissionDTO com : commissionList){
        	if(com.getCommissionId().equals(commissionID)){
        		commission = com;
        	}
        }
		if(impactFactors == null)
			impactFactors = new ArrayList<ImpactFactor>();
		impactFactors.clear();
		evaluatedResults.clear();
		evaluatedResultsLegendCategories.clear();
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesFiveYears.clear();
		evaluatedResultsAllYearsTwoYears.clear();
		evaluatedResultsAllYearsFiveYears.clear();
		detailExplanation = "";
		
//		System.out.println("1");
		
		if (selectedJournal != null && commissionID != null && commission != null && year != null){
//			System.out.println("2");
			evaluatedResult = new JournalEvaluationResult("M52", null, null, 5, true, true);
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				MetricsDB metricsDB = new MetricsDB();
				allImpactFactors = metricsDB.getJournalImpactFactors(conn, selectedJournal.getControlNumber(), Arrays.asList(new String[]{"twoYearsIF", "fiveYearsIF"}));
				JournalEval journalEval= new JournalEval(selectedJournal.getControlNumber(), selectedJournal.getSomeName(), selectedJournal.getIssn(), allImpactFactors, firstEvaluationYear);
				AbstractCommissionEvaluation absCommission = CommissionFactory.getInstance().getCommissionEvaluation(commissionID);
				HashMap<Integer, JournalEvaluationResult> results = absCommission.getJournalEvaluations(journalEval);
				
				if(results.get(year) != null)
					evaluatedResult = results.get(year);
				
				if(evaluatedResult.getEvaluation() < 5){
					if(evaluatedResult.getRuleNumber() == 3){
						for(int startYear = 1981; startYear < 1984; startYear++){
							ImpactFactor tempIF = new ImpactFactor(startYear, null, new ArrayList<ResearchAreaRanking>(), null, new ArrayList<ResearchAreaRanking>());
							for (ImpactFactor impactFactor : allImpactFactors) {
								if(impactFactor.getYear().intValue() == startYear){
										tempIF = impactFactor;
										break;
								}
							}
							impactFactors.add(tempIF);
						}
					} else if(evaluatedResult.getRuleNumber() == 2){
						for(int startYear = 1987; startYear < 1999; startYear++){
								ImpactFactor tempIF = new ImpactFactor(startYear, null, new ArrayList<ResearchAreaRanking>(), null, new ArrayList<ResearchAreaRanking>());
								for (ImpactFactor impactFactor : allImpactFactors) {
									if(impactFactor.getYear().intValue() == startYear){
											tempIF = impactFactor;
											break;
									}
								}
								impactFactors.add(tempIF);
						}
					} else if(evaluatedResult.getRuleNumber() == 1){
						for(int startYear = year-2; startYear < year+1; startYear++){
							ImpactFactor tempIF = new ImpactFactor(startYear, null, new ArrayList<ResearchAreaRanking>(), null, new ArrayList<ResearchAreaRanking>());
							for (ImpactFactor impactFactor : allImpactFactors) {
								if(impactFactor.getYear().intValue() == startYear){
										tempIF = impactFactor;
										break;
								}
							}
							impactFactors.add(tempIF);
						}
					}
				}

				
				if(impactFactors!=null && !impactFactors.isEmpty())
					Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
							"year", "asc"));
				
//				System.out.println("4");
				
				calculateExplainTable(impactFactors, true, true);
				showAllImpactFactors = false;
				
//				System.out.println("5");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			} 
			finally {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return dispetcher(0);
		
//		System.out.println("Broj rezultata je "+ evaluatedResults.size());
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#setJournal(rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO)
	 */
	public void setJournal(JournalDTO journal) {
		selectedJournal = journal;
	}
	
	public JournalDTO getSelectedJournal() {
		return selectedJournal;
	}
	
	public void setSelectedJournal(JournalDTO selectedJournal) {
//		System.out.println("metoda pokrenuta selectedJournal sa aprametrom");
		this.selectedJournal = selectedJournal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickJournalManagedBean#cancelPickingJournal()
	 */
	@Override
	public void cancelPickingJournal() {
	}

	public String getJournalControlNUmber() {
		return journalControlNUmber;
	}

	public void setJournalControlNUmber(String journalControlNUmber) {
//		System.out.println(journalControlNUmber);
		this.journalControlNUmber = journalControlNUmber;
	}

	public String getJournalISSN() {
		return journalISSN;
	}

	public void setJournalISSN(String journalISSN) {
		this.journalISSN = journalISSN;
	}

	public String getJournalSomeName() {
		return journalSomeName;
	}

	public void setJournalSomeName(String journalSomeName) {
		this.journalSomeName = journalSomeName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	public String setYearString(String year) {
		if(year!=null){
			try {
				this.year = Integer.parseInt(year);
				evaluateSelectedJournalForCommissionAndYear();
				return dispetcher(0);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	public CommissionDTO getCommission() {
		return commission;
	}

	public void setCommission(CommissionDTO commission) {
		this.commission = commission;
	}

	public List<CommissionDTO> getCommissionList() {
		return commissionList;
	}

	public void setCommissionList(List<CommissionDTO> commissionList) {
		this.commissionList = commissionList;
	}

	public List<SelectItem> getAllCommissions() {
		return allCommissions;
	}

	public void setAllCommissions(List<SelectItem> allCommissions) {
		this.allCommissions = allCommissions;
	}

	public String getMinEvaluationYear() {
		return firstEvaluationYear+"";
	}

	public String getMaxEvaluationYear() {
		return lastEvaluationYear+"";
	}

	public Integer getCommissionID() {
		return commissionID;
	}

	public void setCommissionID(Integer commissionID) {
		this.commissionID = commissionID;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public List<String[]> getEvaluatedResults() {
		return evaluatedResults;
	}

	public void setEvaluatedResults(List<String[]> evaluatedResults) {
		this.evaluatedResults = evaluatedResults;
	}
	
	/**
	 * @return the showAllImpactFactors
	 */
	public boolean isShowAllImpactFactors() {
		return showAllImpactFactors;
	}

	/**
	 * @param showAllImpactFactors the showAllImpactFactors to set
	 */
	public void setShowAllImpactFactors(boolean showAllImpactFactors) {
		this.showAllImpactFactors = showAllImpactFactors;
	}

	/**
	 * @return the evaluatedResult
	 */
	public JournalEvaluationResult getEvaluatedResult() {
		return evaluatedResult;
	}

	/**
	 * @param evaluatedResult the evaluatedResult to set
	 */
	public void setEvaluatedResult(JournalEvaluationResult evaluatedResult) {
		this.evaluatedResult = evaluatedResult;
	}
	
	public String getEvaluatedResultAsString() {
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
	
	public String getSelectedJournalHeader(){
		String retVal = null;
		retVal = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.selectedJournal1") + " " +selectedJournal.toString() + " " + facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.selectedJournal2") + " " + year;
		return retVal;
	}
	
	public String getEvaluatedResultHeader(){
		String retVal = null;
		retVal = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.com"  + evaluatedResult.getCommId() + ".rule" + evaluatedResult.getRuleNumber() ) ;
		return retVal;
	}
	
//	public String getAllYearsHeader(){
//		String retVal = null;
//		retVal = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.allYearsImpactFactor") ;
//		return retVal;
//	}
	
//	public String getBibliometricIndicatorsHeader(){
//		String retVal = null;
//		retVal = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.bibliometricIndicators") ;
//		return retVal;
//	}

	public List<Map<String, String>> getEvaluatedResultsImpactFactorsWithCategoriesTwoYears() {
		return evaluatedResultsImpactFactorsWithCategoriesTwoYears;
	}

	public void setEvaluatedResultsImpactFactorsWithCategoriesTwoYears(
			List<Map<String, String>> evaluatedResultsImpactFactorsWithCategoriesTwoYears) {
		this.evaluatedResultsImpactFactorsWithCategoriesTwoYears = evaluatedResultsImpactFactorsWithCategoriesTwoYears;
	}
	
	public List<String> getEvaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears() {
		return evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears;
	}

	public void setEvaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears(
			List<String> evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears) {
		this.evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears = evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears;
	}

	public List<Map<String, String>> getEvaluatedResultsImpactFactorsWithCategoriesFiveYears() {
		return evaluatedResultsImpactFactorsWithCategoriesFiveYears;
	}

	public void setEvaluatedResultsImpactFactorsWithCategoriesFiveYears(List<Map<String, String>> evaluatedResultsImpactFactorsWithCategoriesFiveYears) {
		this.evaluatedResultsImpactFactorsWithCategoriesFiveYears = evaluatedResultsImpactFactorsWithCategoriesFiveYears;
	}

	public List<String> getEvaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears() {
		return evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears;
	}

	public void setEvaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears(List<String> evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears) {
		this.evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears = evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears;
	}

	public List<String[]> getEvaluatedResultsLegendCategories() {
		return evaluatedResultsLegendCategories;
	}
	
	public String getDetailExplanation() {	
		
		return detailExplanation;
	}
	
	public List<String[]> getEvaluatedResultsAllYearsTwoYears() {
		return evaluatedResultsAllYearsTwoYears;
	}

	public List<String[]> getEvaluatedResultsAllYearsFiveYears() {
		return evaluatedResultsAllYearsFiveYears;
	}

	public boolean isRenderTabelaTip1(){
		boolean retVal = false;
		if (year < 1981)
			retVal = true;
		return retVal && !(evaluatedResultsImpactFactorsWithCategoriesTwoYears.isEmpty() || evaluatedResultsImpactFactorsWithCategoriesFiveYears.isEmpty()) && evaluatedResultsAllYearsTwoYears.isEmpty() && evaluatedResultsAllYearsFiveYears.isEmpty();
	}
	
	public boolean isRenderTabelaTip2(){
		boolean retVal = false;
		if (year >= 1989 && year<=1997)
			retVal = true;
		return retVal && !(evaluatedResultsImpactFactorsWithCategoriesTwoYears.isEmpty() || evaluatedResultsImpactFactorsWithCategoriesFiveYears.isEmpty()) && evaluatedResultsAllYearsTwoYears.isEmpty() && evaluatedResultsAllYearsFiveYears.isEmpty();
	}
	
	public boolean isRenderTabelaTip3(){
		boolean retVal = false;
		if (!isRenderTabelaTip1() && !isRenderTabelaTip2())
			retVal = true;
		return retVal && !(evaluatedResultsImpactFactorsWithCategoriesTwoYears.isEmpty() || evaluatedResultsImpactFactorsWithCategoriesFiveYears.isEmpty()) && evaluatedResultsAllYearsTwoYears.isEmpty() && evaluatedResultsAllYearsFiveYears.isEmpty();
	}
	
	private HashMap<String, ResearchAreaDTO> getRowsCategoriesRanking(boolean twoYears, boolean fiveYears) {
		HashMap<String, ResearchAreaDTO> retVal = new HashMap<String, ResearchAreaDTO>();
		if (impactFactors!=null) {
			for (ImpactFactor imF:  impactFactors) {
				if(twoYears) {
					for (ResearchAreaRanking rar : imF.getResearchAreas()) {
						ResearchAreaDTO ra = rar.getResearchAreaDTO();
						if (!retVal.containsKey(ra.getClassId())) {
							retVal.put(ra.getClassId(), ra);
						}
					}
				}
				if(fiveYears) {
					for (ResearchAreaRanking rar : imF.getResearchAreas()) {
						ResearchAreaDTO ra = rar.getResearchAreaDTO();
						if (!retVal.containsKey(ra.getClassId())) {
							retVal.put(ra.getClassId(), ra);
						}
					}
				}
			}
		}
		return retVal;
	}

	public void calculateExplainTable(List<ImpactFactor> impactFactors, boolean twoYears, boolean fiveYears) {
		if(twoYears)
			calculateExplainTableTwoYears(impactFactors);
		if(fiveYears)
			calculateExplainTableFiveYears(impactFactors);
	}

	public void calculateExplainTableTwoYears(List<ImpactFactor> impactFactors) {
		
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears.clear();
		
		if (selectedJournal == null || year==null || commissionID == null)
			return;
		
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears.add("header");
		for (ImpactFactor impactFactor : impactFactors) {
			evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesTwoYears.add(impactFactor.getYear().toString());
		}
		
		Set<ResearchAreaDTO> researchAreas = new HashSet<ResearchAreaDTO>();
		for (ImpactFactor impactFactor : impactFactors) {
			for (ResearchAreaRanking researchAreaRanking : impactFactor.getResearchAreas()) {
				if(! researchAreas.contains(researchAreaRanking.getResearchAreaDTO()))
					researchAreas.add(researchAreaRanking.getResearchAreaDTO());
			}
		}
		
		Map<String, String> tempMap= new HashMap<String, String>();
		tempMap.put("header", facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.year"));
		
		for (ImpactFactor impactFactor : impactFactors) {
			tempMap.put(impactFactor.getYear().toString(), impactFactor.getYear().toString());
		}
		
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.add(tempMap);
		
		tempMap= new HashMap<String, String>();
		tempMap.put("header", facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.impactFactor") + " (IF2)");
		
		for (ImpactFactor impactFactor : impactFactors) {
			tempMap.put(impactFactor.getYear().toString(), (impactFactor.getValueOfImpactFactor()!=null)?(impactFactor.getValueOfImpactFactor().toString()):"-");
		}
		
		evaluatedResultsImpactFactorsWithCategoriesTwoYears.add(tempMap);
		
		for (ResearchAreaDTO researchAreaDTO : researchAreas) {
			tempMap= new HashMap<String, String>();
			tempMap.put("header", researchAreaDTO.getSomeTerm());
			
			for (ImpactFactor impactFactor : impactFactors) {
				String position = "";
				for (ResearchAreaRanking ra : impactFactor.getResearchAreas()) {
					if(researchAreaDTO.equals(ra.getResearchAreaDTO())){
						long round  = Math.round(ra.getPosition()/ra.getDividend());
						position = ra.getPosition().intValue() + "/" + round;
					}
				}
				tempMap.put(impactFactor.getYear().toString(), position);
			}
			
			evaluatedResultsImpactFactorsWithCategoriesTwoYears.add(tempMap);
		}
		
	}

	public void calculateExplainTableFiveYears(List<ImpactFactor> impactFactors) {

		evaluatedResultsImpactFactorsWithCategoriesFiveYears.clear();
		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears.clear();

		if (selectedJournal == null || year==null || commissionID == null)
			return;

		evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears.add("header");
		for (ImpactFactor impactFactor : impactFactors) {
			evaluatedResultsImpactFactorsWithCategoriesItemPropertyNamesFiveYears.add(impactFactor.getYear().toString());
		}

		Set<ResearchAreaDTO> researchAreas = new HashSet<ResearchAreaDTO>();
		for (ImpactFactor impactFactor : impactFactors) {
			for (ResearchAreaRanking researchAreaRanking : impactFactor.getResearchAreasFiveYears()) {
				if(! researchAreas.contains(researchAreaRanking.getResearchAreaDTO()))
					researchAreas.add(researchAreaRanking.getResearchAreaDTO());
			}
		}

		Map<String, String> tempMap= new HashMap<String, String>();
		tempMap.put("header", facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.year"));

		for (ImpactFactor impactFactor : impactFactors) {
			tempMap.put(impactFactor.getYear().toString(), impactFactor.getYear().toString());
		}

		evaluatedResultsImpactFactorsWithCategoriesFiveYears.add(tempMap);

		tempMap= new HashMap<String, String>();
		tempMap.put("header", facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.evaluatedResult.impactFactor") + " (IF5)");

		for (ImpactFactor impactFactor : impactFactors) {
			tempMap.put(impactFactor.getYear().toString(), (impactFactor.getValueOfImpactFactorFiveYears()!=null)?(impactFactor.getValueOfImpactFactorFiveYears().toString()):"-");
		}

		evaluatedResultsImpactFactorsWithCategoriesFiveYears.add(tempMap);

		for (ResearchAreaDTO researchAreaDTO : researchAreas) {
			tempMap= new HashMap<String, String>();
			tempMap.put("header", researchAreaDTO.getSomeTerm());

			for (ImpactFactor impactFactor : impactFactors) {
				String position = "";
				for (ResearchAreaRanking ra : impactFactor.getResearchAreasFiveYears()) {
					if(researchAreaDTO.equals(ra.getResearchAreaDTO())){
						long round  = Math.round(ra.getPosition()/ra.getDividend());
						position = ra.getPosition().intValue() + "/" + round;
					}
				}
				tempMap.put(impactFactor.getYear().toString(), position);
			}

			evaluatedResultsImpactFactorsWithCategoriesFiveYears.add(tempMap);
		}

	}

	public void calculateExplainTableAllYears(boolean twoYears, boolean fiveYears) {
		if(twoYears)
			calculateExplainTableAllYearsTwoYearsIF();
		if(fiveYears)
			calculateExplainTableAllYearsFiveYearsIF();
	}

	public void calculateExplainTableAllYearsTwoYearsIF() {
		
		evaluatedResultsAllYearsTwoYears.clear();

		List<Integer> years = new ArrayList<Integer>();
		
		int startEv  = 1981;
		int endEv  = lastEvaluationYear; //
		
		int brojKolona = endEv-startEv+2; //33
		
		
		for(int i = startEv; i <= endEv; i++){
			years.add(i);
//			System.out.println(i +" unete godine "+i);
		}
		String [] red = null;
		
		//popunjavam prvu vrstu godine
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderYear");
		for (int i = 1; i < red.length; i++) {
			red[i] = years.get(i-1)+"";
//			System.out.println(i +" unete godine vrsta "+red[i]);
		}
		evaluatedResultsAllYearsTwoYears.add(red);
		
		//popunjavam drugu vrstu izvrednovane kategorije
		HashMap<Integer, String> evaluacijaSvihGodine = getAllTypes(selectedJournal.getRecord(), commission, startEv, endEv);
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderEvaluation");
		for (int i = 1; i < red.length; i++) {
			int godinaEv = years.get(i-1);
			if (evaluacijaSvihGodine.containsKey(godinaEv)) {
				red[i] = evaluacijaSvihGodine.get(godinaEv);
//				System.out.println(i +" unete eval vrsta "+red[i]);
			}
			
		}
		evaluatedResultsAllYearsTwoYears.add(red);
		//popunjavam trecu vrstu impakt faktori
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderIF");
		for (int i = 1; i < red.length; i++) {
			String vrednostIF = "";
			for (ImpactFactor imF:  impactFactors){
					if(years.get(i-1).intValue() == imF.getYear().intValue()){
						vrednostIF = imF.getValueOfImpactFactor()+"";
						break;
					}
			}
			red[i] = vrednostIF;
//			System.out.println(i +" unete if vrsta "+red[i]);
		}
		evaluatedResultsAllYearsTwoYears.add(red);
		
		//popunjavam naucne oblasti i kategorije
		int legendCount = 1;
		HashMap<String, ResearchAreaDTO> kategorije = getRowsCategoriesRanking(true, false);
		for (String kljucKategorije : kategorije.keySet()){
			red = new String [brojKolona];
			red[0] = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderScienceField"), legendCount);
			
			for (int i = 1; i < red.length; i++) {
				String vrednostKategorije = "";
				for (ImpactFactor imF:  impactFactors){
						if(years.get(i-1).intValue() == imF.getYear().intValue()){
							List<ResearchAreaRanking> rar = imF.getResearchAreas();
							for (ResearchAreaRanking ra : rar) {
								if (ra.getResearchAreaDTO().getClassId().equals(kljucKategorije)) {
									long round  = Math.round(ra.getPosition()/ra.getDividend());
									vrednostKategorije = ra.getPosition().intValue() + "/" + round;
									break;
								}
							}
						}
				}
				red[i] = vrednostKategorije;
//				System.out.println(i +" unete kat " + kljucKategorije +" vrsta "+red[i]);
			}
			legendCount++;
			evaluatedResultsAllYearsTwoYears.add(red);
		}
	}

	public void calculateExplainTableAllYearsFiveYearsIF() {

		evaluatedResultsAllYearsFiveYears.clear();

		List<Integer> years = new ArrayList<Integer>();

		int startEv  = 1981;
		int endEv  = lastEvaluationYear; //

		int brojKolona = endEv-startEv+2; //33


		for(int i = startEv; i <= endEv; i++){
			years.add(i);
//			System.out.println(i +" unete godine "+i);
		}
		String [] red = null;

		//popunjavam prvu vrstu godine
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderYear");
		for (int i = 1; i < red.length; i++) {
			red[i] = years.get(i-1)+"";
//			System.out.println(i +" unete godine vrsta "+red[i]);
		}
		evaluatedResultsAllYearsFiveYears.add(red);

		//popunjavam drugu vrstu izvrednovane kategorije
		HashMap<Integer, String> evaluacijaSvihGodine = getAllTypes(selectedJournal.getRecord(), commission, startEv, endEv);
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderEvaluation");
		for (int i = 1; i < red.length; i++) {
			int godinaEv = years.get(i-1);
			if (evaluacijaSvihGodine.containsKey(godinaEv)) {
				red[i] = evaluacijaSvihGodine.get(godinaEv);
//				System.out.println(i +" unete eval vrsta "+red[i]);
			}

		}
		evaluatedResultsAllYearsFiveYears.add(red);
		//popunjavam trecu vrstu impakt faktori
		red = new String [brojKolona];
		red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderIF");
		for (int i = 1; i < red.length; i++) {
			String vrednostIF = "";
			for (ImpactFactor imF:  impactFactors){
				if(years.get(i-1).intValue() == imF.getYear().intValue()){
					vrednostIF = imF.getValueOfImpactFactor()+"";
					break;
				}
			}
			red[i] = vrednostIF;
//			System.out.println(i +" unete if vrsta "+red[i]);
		}
		evaluatedResultsAllYearsFiveYears.add(red);

		//popunjavam naucne oblasti i kategorije
		int legendCount = 1;
		HashMap<String, ResearchAreaDTO> kategorije = getRowsCategoriesRanking(false, true);
		for (String kljucKategorije : kategorije.keySet()){
			red = new String [brojKolona];
			red[0] = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderScienceField"), legendCount);

			for (int i = 1; i < red.length; i++) {
				String vrednostKategorije = "";
				for (ImpactFactor imF:  impactFactors){
					if(years.get(i-1).intValue() == imF.getYear().intValue()){
						List<ResearchAreaRanking> rar = imF.getResearchAreasFiveYears();
						for (ResearchAreaRanking ra : rar) {
							if (ra.getResearchAreaDTO().getClassId().equals(kljucKategorije)) {
								long round  = Math.round(ra.getPosition()/ra.getDividend());
								vrednostKategorije = ra.getPosition().intValue() + "/" + round;
								break;
							}
						}
					}
				}
				red[i] = vrednostKategorije;
//				System.out.println(i +" unete kat " + kljucKategorije +" vrsta "+red[i]);
			}
			legendCount++;
			evaluatedResultsAllYearsFiveYears.add(red);
		}
	}
	
	public HashMap<Integer, String> getAllTypes(Record record, CommissionDTO commission, Integer startYear, Integer endYear) {
		HashMap<Integer, String> retVal = new HashMap<Integer, String>();
		if(commission == null || commission.getCommissionId()<=0 || startYear == null || endYear == null)
			return null;
		
		if(!(record.getDto() instanceof PublicationDTO))
			return null;
		
		if(!(record.getDto() instanceof JournalDTO))
			return null;
	
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
						
		record.loadFromDatabase();
		String classSuperPublicationType = null;
		
		for (int i = startYear; i <= endYear; i++) {
			classSuperPublicationType = null;
			Date publicationTime = new Date();
			publicationDate.setTime(publicationTime);
			publicationDate.set(GregorianCalendar.YEAR, i);
			
			for (Classification classification : record.getRecordClasses()) {
				if(
						(commission.getCommissionId().equals(classification.getCommissionId()))
				&& (classification.getCfClassSchemeId().equals("type")) 
				&&  (! (publicationDate.before(classification.getCfStartDate()))) 
				&& (((!(publicationDate.after(classification.getCfEndDate())))))){
					classSuperPublicationType =  classification.getCfClassId();
					break;
				}
			}
			
			if(classSuperPublicationType == null){
				List<CommissionDTO> conComs = commisionDAO.getConnectedCommissions(commission.getCommissionId());
				for (CommissionDTO commissionDTO : conComs) {
					for (Classification classification : record.getRecordClasses()) {
						if(
								(commissionDTO.getCommissionId().equals(classification.getCommissionId()))
						&& (classification.getCfClassSchemeId().equals("type")) 
						&&  (! (publicationDate.before(classification.getCfStartDate()))) 
						&& (((!(publicationDate.after(classification.getCfEndDate())))))){
							if(classSuperPublicationType == null)
								classSuperPublicationType =  classification.getCfClassId();
							else if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(classification.getCfClassId()).intValue()){
								classSuperPublicationType =  classification.getCfClassId();
							}
//							break;
						}
					}
				}
			
				List<CommissionDTO> conComs1 = commisionDAO.getConnectedCommissions(commission.getCommissionId());
				for (CommissionDTO commissionDTO1 : conComs1) {
					List<CommissionDTO> conComs2 = commisionDAO.getConnectedCommissions(commissionDTO1.getCommissionId());
					for (CommissionDTO commissionDTO : conComs2) {
						for (Classification classification : record.getRecordClasses()) {
							if(
									(commissionDTO.getCommissionId().equals(classification.getCommissionId()))
							&& (classification.getCfClassSchemeId().equals("type")) 
							&&  (! (publicationDate.before(classification.getCfStartDate()))) 
							&& (((!(publicationDate.after(classification.getCfEndDate())))))){
								if(classSuperPublicationType == null)
									classSuperPublicationType =  classification.getCfClassId();
								else if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(classification.getCfClassId()).intValue()){
									classSuperPublicationType =  classification.getCfClassId();
								}
							}
						}
					}
				}
			}
			
			if(journalCategories.containsKey(classSuperPublicationType)){
				String m = null;
				if(classSuperPublicationType.equalsIgnoreCase("topLeadingInternationalJournal"))
					m = "M21a";
				else if(classSuperPublicationType.equalsIgnoreCase("leadingInternationalJournal"))
					m = "M21";
				else if (classSuperPublicationType.equalsIgnoreCase("outstandingInternationalJournal"))
					m = "M22";
				else if (classSuperPublicationType.equalsIgnoreCase("internationalJournal"))
					m = "M23";
				else if (classSuperPublicationType.equalsIgnoreCase("speciallyVerifiedInternationalJournal"))
					m = "M24";
				else if (classSuperPublicationType.equalsIgnoreCase("leadingNationalJournal"))
					m = "M51";
				else if (classSuperPublicationType.equalsIgnoreCase("nationalJournal"))
					m = "M52";
				else if (classSuperPublicationType.equalsIgnoreCase("scienceJournal"))
					m = "M53";
				else {
					m = facesMessages.getMessageFromResourceBundle("evaluation.evaluationData.notEvaluated");
				}
				retVal.put(i, m);
			}
            else {
                retVal.put(i, facesMessages.getMessageFromResourceBundle("evaluation.evaluationData.notEvaluated"));
            } 
			
		}
		return retVal;
	}

	/**
	 * @return the impactFactors
	 */
	public List<ImpactFactor> getImpactFactors() {
		return impactFactors;
	}

	/**
	 * @param impactFactors the impactFactors to set
	 */
	public void setImpactFactors(List<ImpactFactor> impactFactors) {
		this.impactFactors = impactFactors;
	}

	/**
	 * @return the allImpactFactors
	 */
	public List<ImpactFactor> getAllImpactFactors() {
		return allImpactFactors;
	}

	/**
	 * @param allImpactFactors the allImpactFactors to set
	 */
	public void setAllImpactFactors(List<ImpactFactor> allImpactFactors) {
		this.allImpactFactors = allImpactFactors;
	}
	
	
	
	
}
