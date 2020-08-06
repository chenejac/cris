package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResultEvaluator;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.AbstractCommissionEvaluation;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommissionFactory;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

@SuppressWarnings("serial")
public class EvaluationOldJournalManagedBean extends CRUDManagedBean implements IPickJournalManagedBean{

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
	
	private List<String []> evaluatedResylts = null;
	
	private List<ImpactFactor> impactFactors = null;
	private List<String []> evaluatedResyltsLegendCategories = null;
	private List<String []> evaluatedResyltsImpactFactorsWithCategories = null;
	private List<String []> evaluatedResyltsAllYears = null;
	private String detailExplanation = "";
	
	public EvaluationOldJournalManagedBean() {
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
	        //PMF KOMISIJE od 11-50
	        
	        for (CommissionDTO com : commissionList){
	        	if(com.getCommissionId()>=11 && com.getCommissionId()<=50)
	        		allCommissions.add(new SelectItem(com.getCommissionId(), facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.name.id"+com.getCommissionId())));
	        }
		} catch (SQLException e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		
		evaluatedResylts = new ArrayList<String[]>();
		evaluatedResyltsLegendCategories = new ArrayList<String[]>();
		evaluatedResyltsImpactFactorsWithCategories = new ArrayList<String[]>();
		evaluatedResyltsAllYears = new ArrayList<String[]>();
		detailExplanation = "";
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
		evaluatedResylts.clear();
		evaluatedResyltsLegendCategories.clear();
		evaluatedResyltsImpactFactorsWithCategories.clear();
		evaluatedResyltsAllYears.clear();
		detailExplanation = "";
		return super.resetForm();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "evaluationOldJournalPage";
		return retVal;
	}
	
	
	public String enterPage() {
		resetForm();
		init();
		pickJournal();
		returnPage = "indexPage";
		return "evaluationOldJournalPage";
	}
	
	/**
	 * Performs the account activation
	 */
	public void enterPage(PhaseEvent event){
		String commissionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commission");
		if((init == false) || (commissionId != null)){
			resetForm();
			init();
			pickJournal();
			if(commissionId != null){
				this.commissionID = Integer.parseInt(commissionId);
				for (int i=0;i<allCommissions.size();){
		        	SelectItem com = allCommissions.get(i);
					if((com.getValue() == null) || (!com.getValue().toString().equals(commissionId)))
		        		allCommissions.remove(com);
					else
						i++;
		        }
				setCommissionRuleBook();
			}
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
			pdfUrl = "/PMF_Rulebook_For_Departments.pdf";//facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.rulebook.id"+11);
		else
			pdfUrl = null;
		
		evaluatedResylts.clear();
		evaluatedResyltsLegendCategories.clear();
		evaluatedResyltsImpactFactorsWithCategories.clear();
		evaluatedResyltsAllYears.clear();
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
		evaluatedResylts.clear();
		evaluatedResyltsLegendCategories.clear();
		evaluatedResyltsImpactFactorsWithCategories.clear();
		evaluatedResyltsAllYears.clear();
		detailExplanation = "";
	}
	
	public void evaluateSelectedJournalForCommissionAndYear() {
		
//		System.out.println("metoda evaluate pokrenuta*************************************************************************");
		loadSerbianRuleBook();
		for (CommissionDTO com : commissionList){
        	if(com.getCommissionId().equals(commissionID)){
        		commission = com;
        	}
        }
		
		evaluatedResylts.clear();
		evaluatedResyltsLegendCategories.clear();
		evaluatedResyltsImpactFactorsWithCategories.clear();
		evaluatedResyltsAllYears.clear();
		detailExplanation = "";
		
//		System.out.println("1");
	
		
		if (selectedJournal != null && commissionID != null && commission != null && year != null){
//			System.out.println("2");
			Record record = selectedJournal.getRecord();
			List<ResultMeasureDTO> retVal = ResultEvaluator.getResultTypes(record, year, ruleBook, commission);
			for (int i = 0; i < retVal.size(); i++) {
				ResultMeasureDTO rM = retVal.get(i);
				String [] pom = {facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.results."+rM.getResultType().getClassId()) , 
						rM.getResultType().getClassId(),  rM.getQuantitativeMeasure().toString()};
				evaluatedResylts.add(pom);
				
//				System.out.println("Kategorija " + rM.getResultType().getClassId() + " opis " + rM.getResultType().getSomeTerm() + " ocena " + rM.getQuantitativeMeasure());
			}
			if(evaluatedResylts.isEmpty()){
                String [] pom = {facesMessages.getMessageFromResourceBundle("evaluation.evaluationData.notEvaluatedExplain"), facesMessages.getMessageFromResourceBundle("evaluation.evaluationData.notEvaluated"), "0"};
                evaluatedResylts.add(pom);
            }
			
			Connection conn = null;
			try {
//				System.out.println("3");
				conn = dataSource.getConnection();
				MetricsDB metricsDB = new MetricsDB();
				impactFactors = metricsDB.getJournalImpactFactors(conn, selectedJournal.getControlNumber(), "twoYearsIF");
				if(impactFactors!=null && !impactFactors.isEmpty())
					Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
							"year", "asc"));
				
//				System.out.println("4");
				
				calculateExplainTable();
				
//				System.out.println("5");
				
			} catch (SQLException e) {
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		
//		System.out.println("Broj rezultata je "+ evaluatedResylts.size());
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

	public List<String[]> getEvaluatedResylts() {
		return evaluatedResylts;
	}

	public void setEvaluatedResylts(List<String[]> evaluatedResylts) {
		this.evaluatedResylts = evaluatedResylts;
	}

	public List<String[]> getEvaluatedResyltsImpactFactorsWithCategories() {
		return evaluatedResyltsImpactFactorsWithCategories;
	}

	public void setEvaluatedResyltsImpactFactorsWithCategories(
			List<String[]> evaluatedResyltsImpactFactorsWithCategories) {
		this.evaluatedResyltsImpactFactorsWithCategories = evaluatedResyltsImpactFactorsWithCategories;
	}

	public List<String[]> getEvaluatedResyltsLegendCategories() {
		return evaluatedResyltsLegendCategories;
	}
	
	public String getDetailExplanation() {	
		
		return detailExplanation;
	}
	
	public List<String[]> getEvaluatedResyltsAllYears() {
		return evaluatedResyltsAllYears;
	}

	public boolean isRenderTabelaTip1(){
		boolean retVal = false;
		if (year < 1981)
			retVal = true;
		return retVal && !evaluatedResyltsImpactFactorsWithCategories.isEmpty() && evaluatedResyltsAllYears.isEmpty();
	}
	
	public boolean isRenderTabelaTip2(){
		boolean retVal = false;
		if (year >= 1989 && year<=1997)
			retVal = true;
		return retVal && !evaluatedResyltsImpactFactorsWithCategories.isEmpty() && evaluatedResyltsAllYears.isEmpty();
	}
	
	public boolean isRenderTabelaTip3(){
		boolean retVal = false;
		if (!isRenderTabelaTip1() && !isRenderTabelaTip2())
			retVal = true;
		return retVal && !evaluatedResyltsImpactFactorsWithCategories.isEmpty() && evaluatedResyltsAllYears.isEmpty();
	}
	
	private HashMap<String, ResearchAreaDTO> getRowsCategoriesRanking() {
		HashMap<String, ResearchAreaDTO> retVal = new HashMap<String, ResearchAreaDTO>();
		if (impactFactors!=null) {
			
			for (ImpactFactor imF:  impactFactors) {
				for (ResearchAreaRanking rar : imF.getResearchAreas()) {
					ResearchAreaDTO ra = rar.getResearchAreaDTO();
					if (!retVal.containsKey(ra.getClassId())) {
						retVal.put(ra.getClassId(), ra);
					}
				}
			}
		}
		return retVal;
	}
	
	public void calculateExplainTable() {
		
		evaluatedResyltsLegendCategories.clear();
		evaluatedResyltsImpactFactorsWithCategories.clear();
		evaluatedResyltsAllYears.clear();
		detailExplanation = "";
		
		if (selectedJournal == null || year==null || commissionID == null)
			return;
		
		
		AbstractCommissionEvaluation commisionPMF = null;
		CommissionFactory commissionFactory = CommissionFactory.getInstance();
		
		if (commissionID >=11 && commissionID <=49){
			commisionPMF = commissionFactory.getCommissionEvaluation(commissionID.intValue());
		}
		
		boolean hasIF = (impactFactors!=null && impactFactors.size()>0);
		boolean isSpecial = false;
		StringBuffer isSpecialMNO = new StringBuffer("");
		List<MNO> mnoList = new ArrayList<MNO>();
		if(commisionPMF.getMnoList() != null)
			mnoList.addAll(commisionPMF.getMnoList());
		List<CommissionDTO> conComs = commisionDAO.getConnectedCommissions(commisionPMF.getComissionID());
		for (CommissionDTO commissionDTO : conComs) {
			AbstractCommissionEvaluation tempCommision = commissionFactory.getCommissionEvaluation(commissionDTO.getCommissionId().intValue());
			if(tempCommision.getMnoList() != null)
				mnoList.addAll(tempCommision.getMnoList());
			List<CommissionDTO> conComs2 = commisionDAO.getConnectedCommissions(commissionDTO.getCommissionId());
			for (CommissionDTO commissionDTO2 : conComs2) {
				AbstractCommissionEvaluation tempCommision2 = commissionFactory.getCommissionEvaluation(commissionDTO2.getCommissionId().intValue());
				if(tempCommision2.getMnoList() != null)
					mnoList.addAll(tempCommision2.getMnoList());
			}
		}
		for (MNO mno : mnoList) {
			isSpecial = isSpecial || mno.isSpecial(selectedJournal.getIssn(), 0);
			if(mno.isSpecial(selectedJournal.getIssn(), 0)){
				if("AMS".equals(mno.getNameMNO()))
					isSpecialMNO.append("AMS (1980-2014:M24) ");
				else {
					isSpecialMNO.append(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.SpecialMNO") + ": " + mno.getNameMNO() + "("); 
					int [] godSpec = mno.getYearsSpecial();
					for (int i = godSpec[0]; i <= godSpec[godSpec.length-1]; i++) {
						if (mno.isSpecial(selectedJournal.getIssn(), i)) {
							isSpecialMNO.append(i + ":" + mno.getSpecial(selectedJournal.getIssn(), i) + ";" );
						}
					}
					isSpecialMNO.append(") ");
				}
			}
				
		}
		String ifUGodini = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.dontHaveIF");
		if(impactFactors!=null && impactFactors.size()>0)
			for (ImpactFactor imf: impactFactors) {
				if (year.intValue() == imf.getYear().intValue()) {
					ifUGodini = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.haveIF");
					break;
				}
			}
		
		if(hasIF && isSpecial){
			detailExplanation = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.IfInYearAndSpecial"), ifUGodini, isSpecialMNO.toString());
		}
		else if(hasIF){
			detailExplanation = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.IfInYear"), ifUGodini);
		}
		else if(isSpecial){
			detailExplanation = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.Special"), isSpecialMNO.toString());
		}
		else {
			detailExplanation = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.DoNotHaveIfAndIsNotSpecial");
			return;
		}
		
		detailExplanation += ", "+ facesMessages.getMessageFromResourceBundle("evaluation.evaluationData.notEvaluatedExplain");
		
		if(hasIF){
			List<Integer> years = new ArrayList<Integer>();
			int brojKolona = 0;
			int startEv  = 0;
			int endEv  = 0;
			
			if (year < 1981){
				brojKolona = 4;
				startEv = 1981;
				endEv = 1983;
			}
			else if(year >= 1989 && year<=1997){
				brojKolona = 16;
				startEv = 1985;
				endEv = 1999;
			}
			else {
				brojKolona = 5;
				startEv = year-2;
				endEv = year+1;
			}

			for(int i = startEv; i <= endEv; i++)
				years.add(i);
			
			String [] red = null;
			
			//popunjavam prvu vrstu godine
			red = new String [brojKolona];
			red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderYear");
			for (int i = 1; i < red.length; i++) {
				red[i] = years.get(i-1)+"";
			}
			evaluatedResyltsImpactFactorsWithCategories.add(red);
			
			//popunjavam drugu vrstu izvrednovane kategorije
			HashMap<Integer, String> evaluacijaSvihGodine = getAllTypes(selectedJournal.getRecord(), commission, startEv, endEv);
			red = new String [brojKolona];
			red[0] = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderEvaluation");
			for (int i = 1; i < red.length; i++) {
				int godinaEv = years.get(i-1);
				if (evaluacijaSvihGodine.containsKey(godinaEv)) {
					red[i] = evaluacijaSvihGodine.get(godinaEv);
				}
				
			}
			evaluatedResyltsImpactFactorsWithCategories.add(red);
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
			}
			evaluatedResyltsImpactFactorsWithCategories.add(red);
			
			//popunjavam naucne oblasti i kategorije
			String [] redHL = null;
			int legendCount = 1;
			HashMap<String, ResearchAreaDTO> kategorije = getRowsCategoriesRanking();
			for (String kljucKategorije : kategorije.keySet()){
				redHL = new String[2];
				redHL[0] = MessageFormat.format(facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.evaluationResultsPanel.detailExplanation.tableRowHeaderScienceField"), legendCount);
				redHL[1] = kategorije.get(kljucKategorije).getTerm().getContent();
				evaluatedResyltsLegendCategories.add(redHL);
				
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
				}
				legendCount++;
				evaluatedResyltsImpactFactorsWithCategories.add(red);
			}


		}
	}

	public void calculateExplainTableAllYears() {
		
		evaluatedResyltsAllYears.clear();

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
		evaluatedResyltsAllYears.add(red);
		
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
		evaluatedResyltsAllYears.add(red);
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
		evaluatedResyltsAllYears.add(red);
		
		//popunjavam naucne oblasti i kategorije
		int legendCount = 1;
		HashMap<String, ResearchAreaDTO> kategorije = getRowsCategoriesRanking();
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
			evaluatedResyltsAllYears.add(red);
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
//								break;
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
	
}
