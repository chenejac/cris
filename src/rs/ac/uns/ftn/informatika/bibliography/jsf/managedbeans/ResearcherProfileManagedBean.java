package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import org.primefaces.component.api.UITabPanel;
import org.primefaces.event.TabChangeEvent;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.UserDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import java.util.*;


@SuppressWarnings("serial")
public class ResearcherProfileManagedBean extends CRUDManagedBean {

	/*ApvRegisteredResearchersManagedBean(){
		//iscitivanje korisnika i njihovih BISISS u posebnu listu
	}*/

	protected List<AuthorDTO> list;
	protected UserDAO userDAO = new UserDAO();
	protected AuthorDTO selectedAuthor = null;
	protected OrganizationUnitDTO selectedAuthorOrgUnit = null;

	protected String OUCN = null;
	protected String INCN = null;


	private String authorControlNumber;
	private RecordDAO personDAO = new RecordDAO(new PersonDB());


	public ResearcherProfileManagedBean(){
		//pickSimilarMessage = "records.author.pickSimilarMessage";
		tableModalPanel = "researchersTableFormPanel";
		editModalPanel = "researchersViewDetailsModalPanel";
	}
	@Override
	public String resetForm() {
		selectedAuthor = null;
		selectedAuthorOrgUnit = null;
		authorControlNumber = null;
		tableModalPanel = "researchersTableFormPanel";
		editModalPanel = "researchersViewDetailsModalPanel";
		return super.resetForm();
	}
	
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "researcherPage";
		return retVal;
	}
	
	
	public String researchersPageEnter() {
		selectedAuthor = (getUserManagedBean().isLoggedIn())?(AuthorDTO) getUserManagedBean().getLoggedUser().getAuthor():null;
		changeTabAndLoad("journal");
		setUpForms();
		return "researcherPage";
	}

	private void setUpForms() {
		if (selectedAuthor != null) {
			AuthorManagedBean authormb = getAuthorManagedBean();
			authormb.setSelectedAuthor(selectedAuthor);
			selectedAuthorOrgUnit = selectedAuthor.getOrganizationUnit();
			selectedAuthorOrgUnit.getRecord().loadFromDatabase();
		}
	}

	public String getAuthorControlNumber() {
		return authorControlNumber;
	}

	public void setAuthorControlNumber(String authorControlNumber) {
		this.authorControlNumber = authorControlNumber;
	}

	public void enterPage(PhaseEvent event){
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("researcher") != null){
			authorControlNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("researcher");
			selectedAuthor = (AuthorDTO) personDAO.getDTO(authorControlNumber);
			authorControlNumber = null;
			changeTabAndLoad("journal");
		}
		setUpForms();
	}

//	public String openResearcherPage(){
//		if(authorControlNumber == null)
//			authorControlNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("researcher");
//		if(authorControlNumber != null) {
//			selectedAuthor = (AuthorDTO) personDAO.getDTO(authorControlNumber);
//			authorControlNumber = null;
//		}
//		setUpForms();
//		return "researcherPage";
//	}

	private AuthorManagedBean authorManagedBean = null;

	/**
	 * @return the AuthorManagedBean from current session
	 */
	protected AuthorManagedBean getAuthorManagedBean() {
		if (authorManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorManagedBean mb = (AuthorManagedBean) extCtx.getSessionMap().get(
					"authorManagedBean");
			if (mb == null) {
				mb = new AuthorManagedBean();
				extCtx.getSessionMap().put("authorManagedBean", mb);
			}
			authorManagedBean = mb;
		}
		return authorManagedBean;
	}

	private BibliographyManagedBean bibliographyManagedBean = null;

	/**
	 * @return the BibliographyManagedBean from current session
	 */
	protected BibliographyManagedBean getBibliographyManagedBean() {
		if (bibliographyManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			BibliographyManagedBean mb = (BibliographyManagedBean) extCtx.getSessionMap().get(
					"bibliographyManagedBean");
			if (mb == null) {
				mb = new BibliographyManagedBean();
				extCtx.getSessionMap().put("bibliographyManagedBean", mb);
			}
			bibliographyManagedBean = mb;
		}
		return bibliographyManagedBean;
	}

	private ApvRegisteredResearchersManagedBean apvRegisteredResearchersManagedBean = null;

	/**
	 * @return the ApvRegisteredResearchersManagedBean from current session
	 */
	protected ApvRegisteredResearchersManagedBean getApvRegisteredResearchersManagedBean() {
		if (apvRegisteredResearchersManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ApvRegisteredResearchersManagedBean mb = (ApvRegisteredResearchersManagedBean) extCtx.getSessionMap().get(
					"apvRegisteredResearchersManagedBean");
			if (mb == null) {
				mb = new ApvRegisteredResearchersManagedBean();
				extCtx.getSessionMap().put("apvRegisteredResearchersManagedBean", mb);
			}
			apvRegisteredResearchersManagedBean = mb;
		}
		return apvRegisteredResearchersManagedBean;
	}


	
	@Override
	public void switchToBrowseMode() {
		selectedAuthor = null;
		selectedAuthorOrgUnit = null;
		super.switchToBrowseMode();
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
	 * @see CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList(){

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

	private String activeItem = "journal";

	public void changeTab(TabChangeEvent event){
		changeTabAndLoad(event.getTab().getId().toString());
	}

	public void changeTabAndLoad(String newActiveItem){
		activeItem = newActiveItem;
		loadInfo();
	}

	private void loadInfo() {
		if (activeItem.equalsIgnoreCase("journal")) {
			BibliographyManagedBean bibliograhymb = getBibliographyManagedBean();
			bibliograhymb.enterCRUDPageJournals();
			bibliograhymb.setSelectedResearcher(selectedAuthor);
		} else if (activeItem.equalsIgnoreCase("conference")) {
			BibliographyManagedBean bibliograhymb = getBibliographyManagedBean();
			bibliograhymb.enterCRUDPageConferences();
			bibliograhymb.setSelectedResearcher(selectedAuthor);
		} else if (activeItem.equalsIgnoreCase("other")) {
			BibliographyManagedBean bibliograhymb = getBibliographyManagedBean();
			bibliograhymb.enterCRUDPageOther();
			bibliograhymb.setSelectedResearcher(selectedAuthor);
		} else if (activeItem.equalsIgnoreCase("mresults")) {
			ApvRegisteredResearchersManagedBean apvmb = getApvRegisteredResearchersManagedBean();
			apvmb.selectResearcherProfile(selectedAuthor);
		}
	}

	public String getActiveItem() {
		return activeItem;
	}

	public void setActiveItem(String activeItem) {
		this.activeItem = activeItem;
	}
}
