/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.KnrDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.knr.ResultsGroupDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class TestAuthorEvaluatedRecordManagedBean extends CRUDManagedBean{

	private AuthorDTO selectedAuthor = null;
	private CommissionDTO selectedCommision = null;
	private String selectedCommisionName = null;
	protected OrganizationUnitDTO selectedAuthorOrgUnit = null;
	
	protected KnrDTO selectedAuthorEvaluatedResults;
	protected Map<String,Boolean> showList = new HashMap<String, Boolean>();

	private String pdfUrl = null;
	
	public TestAuthorEvaluatedRecordManagedBean() {
		super();
		//pickSimilarMessage = "records.author.pickSimilarMessage";
		tableModalPanel = "testAuthorEvaluatedRecordTableFormPanel";
		editModalPanel = "testAuthorEvaluatedRecordViewDetailsModalPanel";
		init = true;
	}

	@Override
	public String resetForm() {
		selectedAuthor = null;
		selectedCommision = null;
		selectedCommisionName = null;
		selectedAuthorOrgUnit = null;
		pdfUrl = null;
		tableModalPanel = "testAuthorEvaluatedRecordTableFormPanel";
		editModalPanel = "testAuthorEvaluatedRecordViewDetailsModalPanel";
		return super.resetForm();
	}
	
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "testAuthorEvaluatedRecordPage";
		return retVal;
	}
	
	public String testAuthorEvaluatedRecordPagePageEnter() {
		super.enterCRUDPage();
		returnPage = "indexPage";
		return "testAuthorEvaluatedRecordPage";
	}
	
	/**
	 * Performs the account activation
	 */
	public void enterPage(PhaseEvent event){
		
		if(init == false){
			enterCRUDPage();
			init = true;
		}
		switchToTableNoneMode();
		switchToViewDetailsMode();
		
		String authorId = null;
		String commissionId = null;
		
		authorId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("authorId");
		commissionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commissionId");
		
//		System.out.println("autor id je ="+authorId);
//		System.out.println("selected autor je ="+selectedAuthor);
//		System.out.println("commission id je ="+commissionId);
//		System.out.println("selected commission je ="+selectedCommision);
		
		if(selectedAuthor!= null && authorId!= null && !selectedAuthor.getControlNumber().equalsIgnoreCase("(BISIS)"+authorId))
			selectedAuthor = null;
		
		if(selectedCommision!= null && commissionId!= null && !selectedCommision.getCommissionId().toString().equalsIgnoreCase(commissionId))
			selectedCommision = null;

		if (selectedAuthor!=null && selectedCommision!=null) {
			return;
		}
		
		if (authorId == null)
			authorId = "446";
		if (commissionId == null)
			commissionId = "105";
		
		pdfUrl = null;
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		RecordDTO retrieved = null;
		retrieved = recordDAO.getDTO("(BISIS)"+authorId);
		if (retrieved != null && retrieved instanceof AuthorDTO) {
			selectedAuthor = (AuthorDTO) retrieved;
			selectedAuthorOrgUnit = selectedAuthor.getOrganizationUnit();
		}
		
		CommissionDAO commissionDAO = new CommissionDAO();
		selectedCommision = commissionDAO.getCommission(commissionId);
		
		pdfUrl = null;
		
		if (selectedCommision!=null) {
			selectedCommisionName = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.name.id"+selectedCommision.getCommissionId());
			
			if(selectedCommision.getCommissionId()!=null){
		    	Integer broj = selectedCommision.getCommissionId();
		    	if (broj >51 && broj < 100) {
					broj = 51;
				}
		    	else if (broj >101 && broj < 150) {
					broj = 101;
				}
		    	pdfUrl = facesMessages.getMessageFromResourceBundle("evaluation.mainPanel.yearCommissionSelection.commission.rulebook.id"+broj);
		    }
		}
		else
			selectedCommisionName = null;

		if (selectedAuthor!=null && selectedCommision!=null) {
			retrieveSelectedAuthorEvaluationStrucnoVeceTT();
		}
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
	
	@Override
	public void populateList() {
		// TODO Auto-generated method stub
		
	}
	
	private void retrieveSelectedAuthorEvaluationStrucnoVeceTT(){
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
			emb.setCommission(selectedCommision);
			emb.populateMapOnSpotEvaluation();	
		}
		
		HashMap<ResultMeasureDTO, List<PublicationDTO>> allResults = emb.getEvaluatedResults();		
		
//		System.out.println("TIPOVI PUBLIKACIJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA     " + allResults.size());
		

		List<ResultsGroupDTO> selectedAuthorResultsGroups = new ArrayList<ResultsGroupDTO>();
		for (ResultMeasureDTO resultMeasureDTO : allResults.keySet()) {
			if(resultMeasureDTO.getResultType().getClassId().equals("M99"))
				continue;		
			List<PublicationDTO> publications = allResults.get(resultMeasureDTO);
			List<ResultDTO> results = new ArrayList<ResultDTO>();
			for (PublicationDTO publicationDTO : publications) {
				results.add(new ResultDTO(resultMeasureDTO.getResultType(), publicationDTO));
			}
			ResultsGroupDTO rtsDTO = new ResultsGroupDTO(resultMeasureDTO, results);
			selectedAuthorResultsGroups.add(rtsDTO);
		}
		Collections.sort(selectedAuthorResultsGroups, new GenericComparator<ResultsGroupDTO>(
				"resultType.classId", "asc"));
		selectedAuthorEvaluatedResults = new KnrDTO();
		selectedAuthorEvaluatedResults.setResultsGroups(selectedAuthorResultsGroups);
		selectedAuthorEvaluatedResults.setResearcher(selectedAuthor);
		// orgUtnit
		selectedAuthorOrgUnit = selectedAuthor.getOrganizationUnit();
		selectedAuthorOrgUnit.getRecord().loadFromDatabase();
		  while (selectedAuthorOrgUnit.getSuperOrganizationUnit() != null){
		  	selectedAuthorOrgUnit = selectedAuthorOrgUnit.getSuperOrganizationUnit();
		  	selectedAuthorOrgUnit.getRecord().loadFromDatabase();
		  }
	    showList = new HashMap<String, Boolean>();
	    for(ResultsGroupDTO res : selectedAuthorEvaluatedResults.getResultsGroups()){			
			showList.put(res.getResultType().getClassId(), false);
		}
		debug("switchToViewDetailsMode: \n" + selectedAuthor);
//		System.out.println("USPESNO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	/**
	 * @return the selectedAuthor
	 */
	public AuthorDTO getSelectedAuthor() {
		return selectedAuthor;
	}

	/**
	 * @param selectedAuthor the selectedAuthor to set
	 */
	public void setSelectedAuthor(AuthorDTO selectedAuthor) {
		this.selectedAuthor = selectedAuthor;
	}

	/**
	 * @return the selectedCommision
	 */
	public CommissionDTO getSelectedCommision() {
		return selectedCommision;
	}

	/**
	 * @param selectedCommision the selectedCommision to set
	 */
	public void setSelectedCommision(CommissionDTO selectedCommision) {
		this.selectedCommision = selectedCommision;
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
	 * @return the selectedCommisionName
	 */
	public String getSelectedCommisionName() {
		return selectedCommisionName;
	}

	/**
	 * @param selectedCommisionName the selectedCommisionName to set
	 */
	public void setSelectedCommisionName(String selectedCommisionName) {
		this.selectedCommisionName = selectedCommisionName;
	}

	/**
	 * @return the selectedAuthorEvaluatedResults
	 */
	public KnrDTO getSelectedAuthorEvaluatedResults() {
		return selectedAuthorEvaluatedResults;
	}

	/**
	 * @param selectedAuthorEvaluatedResults the selectedAuthorEvaluatedResults to set
	 */
	public void setSelectedAuthorEvaluatedResults(
			KnrDTO selectedAuthorEvaluatedResults) {
		this.selectedAuthorEvaluatedResults = selectedAuthorEvaluatedResults;
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

	public String exit(){
		selectedAuthor = null;
		selectedCommision = null;	
		return super.exit();
	}

	

}
