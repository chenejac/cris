package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.richfaces.component.UITabPanel;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import java.util.*;


@SuppressWarnings("serial")
public class OrganizationProfileManagedBean extends CRUDManagedBean {

	protected List<OrganizationUnitDTO> subunits;
	protected InstitutionDTO selectedOrganizationUnit = null;
	protected InstitutionDTO selectedSuperOrgUnit = null;
	protected List<AuthorDTO> researchers;

	protected String OUCN = null;
	protected String INCN = null;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private RecordDAO personDAO = new RecordDAO(new PersonDB());

	protected String filterFirstname = "";
	protected String filterLastname = "";


	public OrganizationProfileManagedBean(){
		tableModalPanel = "researchersTableFormPanel";
		editModalPanel = "researchersViewDetailsModalPanel";
	}

	@Override
	public void populateList() {

	}

	@Override
	public String resetForm() {
		selectedOrganizationUnit = null;
		selectedSuperOrgUnit = null;
		subunits = new ArrayList<OrganizationUnitDTO>();
		researchers = new ArrayList<AuthorDTO>();
		tableModalPanel = "researchersTableFormPanel";
		editModalPanel = "researchersViewDetailsModalPanel";
		return super.resetForm();
	}
	
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "organizationPage";
		return retVal;
	}
	
	
	public String pmfPageEnter() {
		selectedOrganizationUnit = (InstitutionDTO) recordDAO.getDTO("(BISIS)5929");
		filterFirstname = "";
		filterLastname = "";
		setUpForms();
		changeTabAndLoad("researchers");
		return "organizationPage";
	}

	private void setUpForms() {
		if (selectedOrganizationUnit != null) {
			selectedOrganizationUnit.getAcro();
			if(selectedOrganizationUnit.getControlNumber().equals("(BISIS)5929")){
				selectedSuperOrgUnit = null;
			} else {
				if ((selectedOrganizationUnit instanceof OrganizationUnitDTO) && (((OrganizationUnitDTO) selectedOrganizationUnit).getSuperOrganizationUnit() != null))
					selectedSuperOrgUnit = ((OrganizationUnitDTO) selectedOrganizationUnit).getSuperOrganizationUnit();
				else if (selectedOrganizationUnit instanceof OrganizationUnitDTO)
					selectedSuperOrgUnit = ((OrganizationUnitDTO) selectedOrganizationUnit).getInstitution();
			}

		}
	}

	public void enterPage(PhaseEvent event){
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("organization") != null){
			organizationId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("organization");
			selectedOrganizationUnit = (InstitutionDTO) recordDAO.getDTO(organizationId);
			organizationId = null;
			filterFirstname = "";
			filterLastname = "";
			changeTabAndLoad("researchers");
		}
		setUpForms();
	}

	private String organizationId = null;

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

//	public String openOrganizationPage(){
//		if(organizationId == null)
//			organizationId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("organization");
//		if(organizationId != null) {
//			selectedOrganizationUnit = (InstitutionDTO)recordDAO.getDTO(organizationId);
//			organizationId = null;
//		}
//		setUpForms();
//		return "organizationPage";
//	}


	
	@Override
	public void switchToBrowseMode() {
		selectedOrganizationUnit = null;
		selectedSuperOrgUnit = null;
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
	public void populateSubunits() {
		subunits = getSubunits(selectedOrganizationUnit.getControlNumber(), selectedOrganizationUnit instanceof OrganizationUnitDTO);
		getTree();
	}

	public List<OrganizationUnitDTO> getSubunits(String controlNumber, boolean OUCN) {
		List<OrganizationUnitDTO> retVal = new ArrayList<OrganizationUnitDTO>();
		try {
			debug("getSubunits");
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("TYPE", Types.ORGANIZATION_UNIT)), BooleanClause.Occur.MUST);
			bq.add(new TermQuery(new Term(OUCN?"OUCN":"INCN", controlNumber)), BooleanClause.Occur.MUST);
			List<OrganizationUnitDTO> listTmp = getOrganizationUnits(
					bq, orderBy, direction, new AllDocCollector(((orderList==true)&&(orderBy==null))));
			if(listTmp != null)
				retVal = listTmp;
		} catch (ParseException e) {
			error("getSubunits", e);
		}
		return retVal;
	}

	private List<TreeNodeDTO<OrganizationUnitDTO>> rootNodes = null;

	private void addNode(TreeNodeDTO<OrganizationUnitDTO> parentNode, OrganizationUnitDTO parentOrganizationUnit) {
		for(OrganizationUnitDTO ins:getOrganizationUnits(parentOrganizationUnit)){
			TreeNodeDTO<OrganizationUnitDTO> node = new TreeNodeDTO<OrganizationUnitDTO>(ins);
			addNode(node, ins);
			parentNode.addChild(node);
		}
	}

	private List<OrganizationUnitDTO> getOrganizationUnits(OrganizationUnitDTO parentOrganizationUnit) {
		List<OrganizationUnitDTO> retVal = new ArrayList<OrganizationUnitDTO>();
		for (OrganizationUnitDTO ins : getSubunits(parentOrganizationUnit.getControlNumber(), parentOrganizationUnit instanceof OrganizationUnitDTO)) {
			if(ins.getSuperOrganizationUnit() != null)
				if(parentOrganizationUnit.getControlNumber().equals(ins.getSuperOrganizationUnit().getControlNumber())){
					retVal.add(ins);
				}
		}
		return retVal;
	}

	public List<TreeNodeDTO<OrganizationUnitDTO>> getRootNodes() {
		return rootNodes;
	}

	public void setRootNodes(List<TreeNodeDTO<OrganizationUnitDTO>> rootNodes) {
		this.rootNodes = rootNodes;
	}

	public void getTree() {
		debug("getTree");
		try {
			rootNodes = new ArrayList<TreeNodeDTO<OrganizationUnitDTO>>();
			for(OrganizationUnitDTO ins:subunits){
				TreeNodeDTO<OrganizationUnitDTO> node = new TreeNodeDTO<OrganizationUnitDTO>(ins);
				addNode(node, ins);
				node.setExpanded(true);
				rootNodes.add(node);
			}

		} catch (Exception e) {
			error("getTree", e);
		}
	}

	/**
	 * Retrieves a list of organizationUnits that correspond to the query.
	 *
	 * @param query
	 *            query for retrieving organizationUnits
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of organizationUnits that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<OrganizationUnitDTO> getOrganizationUnits(String query, String orderBy,
														   String direction, HitCollector hitCollector) throws ParseException{
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), QueryParser.Operator.AND);
		return getOrganizationUnits(q, orderBy, direction, hitCollector);
	}

	/**
	 * Retrieves a list of organizationUnits that correspond to the query.
	 *
	 * @param query
	 *            query for retrieving organizationUnits
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return list of organizationUnits that correspond to the query
	 */
	private List<OrganizationUnitDTO> getOrganizationUnits(Query query, String orderBy,
														   String direction, HitCollector hitCollector) throws ParseException {
		List<OrganizationUnitDTO> retVal = new ArrayList<OrganizationUnitDTO>();
		List<Record> list = recordDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				OrganizationUnitDTO dto = (OrganizationUnitDTO) record.getDto();
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
			orderByList.add("controlNumber");
			directionsList.add("asc");
			Collections.sort(retVal, new GenericComparator<OrganizationUnitDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	private SearchManagedBean searchManagedBean = null;

	/**
	 * @return the SearchManagedBean from current session
	 */
	protected SearchManagedBean getSearchManagedBean() {
		if (searchManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			SearchManagedBean mb = (SearchManagedBean) extCtx.getSessionMap().get(
					"searchManagedBean");
			if (mb == null) {
				mb = new SearchManagedBean();
				extCtx.getSessionMap().put("searchManagedBean", mb);
			}
			searchManagedBean = mb;
		}
		return searchManagedBean;
	}

	public List<OrganizationUnitDTO> getSubunits() {
		return subunits;
	}

	public void setSubunits(List<OrganizationUnitDTO> subunits) {
		this.subunits = subunits;
	}

	public InstitutionDTO getSelectedOrganizationUnit() {
		return selectedOrganizationUnit;
	}

	public void setSelectedOrganizationUnit(InstitutionDTO selectedOrganizationUnit) {
		this.selectedOrganizationUnit = selectedOrganizationUnit;
	}

	public InstitutionDTO getSelectedSuperOrgUnit() {
		return selectedSuperOrgUnit;
	}

	public void setSelectedSuperOrgUnit(InstitutionDTO selectedSuperOrgUnit) {
		this.selectedSuperOrgUnit = selectedSuperOrgUnit;
	}

	public List<AuthorDTO> getResearchers() {
		if(populateList){
			populateResearchers();
		}
		return researchers;
	}

	public void setResearchers(List<AuthorDTO> researchers) {
		this.researchers = researchers;
	}

	public void populateResearchers(){
		OUCN = null;
		INCN = null;
		BooleanQuery bq = new BooleanQuery();
		if(selectedOrganizationUnit instanceof OrganizationUnitDTO)
			OUCN = selectedOrganizationUnit.getControlNumber();
		else
			INCN = selectedOrganizationUnit.getControlNumber();
		if(OUCN != null){
			bq.add(personDAO.getInstitutionRecordsQuery(OUCN, "" + Calendar.getInstance().get(Calendar.YEAR) + "-01-01 00:00:00"), BooleanClause.Occur.MUST);
		}
		else if(INCN != null){
			bq.add(personDAO.getInstitutionRecordsQuery(INCN, "" + Calendar.getInstance().get(Calendar.YEAR) + "-01-01 00:00:00"), BooleanClause.Occur.MUST);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), BooleanClause.Occur.MUST);
		if(filterFirstname!=null && !filterFirstname.equals("")){
			BooleanQuery firstNameTerm = QueryUtils.makeBooleanQuery("FN", filterFirstname.toLowerCase()+"*", BooleanClause.Occur.MUST, (float)1.0, (float)0.0, false);
			bq.add(firstNameTerm, BooleanClause.Occur.MUST);
		}
		if(filterLastname!=null && !filterLastname.equals("")){
			BooleanQuery lastNameTerm = QueryUtils.makeBooleanQuery("LN", filterLastname.toLowerCase()+"*", BooleanClause.Occur.MUST, (float)1.0, (float)0.0, false);
			bq.add(lastNameTerm, BooleanClause.Occur.MUST);
		}
		researchers = new ArrayList<AuthorDTO>();
		List<Record> list = personDAO.getDTOs(bq, new AllDocCollector(false));
		for (Record record : list) {
			try {
				AuthorDTO dto = (AuthorDTO) record.getDto();
				if (dto != null)
					researchers.add(dto);
			} catch (Exception e) {
				log.error(e);
			}
		}
		Collections.sort(researchers, new GenericComparator<AuthorDTO>(
				"names", "asc"));
		populateList = false;
	}

	public String getFilterFirstname() {
		return filterFirstname;
	}

	public void setFilterFirstname(String filterFirstname) {
		this.filterFirstname = filterFirstname;
		populateList = true;
	}

	public String getFilterLastname() {
		return filterLastname;
	}

	public void setFilterLastname(String filterLastname) {
		this.filterLastname = filterLastname;
		populateList = true;
	}

	public void btnShowAll(){
		filterLastname="";
		filterFirstname="";
		populateList = true;
	}

	private String activeItem = "researchers";

	public void changeTab(javax.faces.event.FacesEvent event){
		changeTabAndLoad(((UITabPanel)event.getComponent()).getActiveItem().toString());
	}

	public void changeTabAndLoad(String newActiveItem){
		activeItem = newActiveItem;
		loadInfo();
	}

	private void loadInfo() {
		if (activeItem.equalsIgnoreCase("researchers")) {
			populateResearchers();
		} else if (activeItem.equalsIgnoreCase("hierarchy")) {
			populateSubunits();
		} else if (activeItem.equalsIgnoreCase("searchProduction")) {
			getSearchManagedBean().searchPageEnterDepartment(selectedOrganizationUnit.getControlNumber());
		}
	}

	public String getActiveItem() {
		return activeItem;
	}

	public void setActiveItem(String activeItem) {
		this.activeItem = activeItem;
	}
}
