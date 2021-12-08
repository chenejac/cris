package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.richfaces.component.UIDataTable;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for publications
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class BibliographyManagedBean extends CRUDManagedBean {

	private List<PublicationDTO> list;

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private PublicationDTO selectedPublication = null;

	private AuthorDTO selectedResearcher = null;
	
	private boolean justJournals = false;
	
	private boolean justConferences = false;

	private boolean justOther = false;
	
	private Integer startYear = null;
	
	private Integer endYear = null;
	
	private List<InstitutionDTO> institutions;

	/**
	 * 
	 */
//	public BibliographyManagedBean() {
//		super();
//		tableModalPanel = "bibliographyBrowseModalPanel";
//		editModalPanel = "bibliographyEditModalPanel";
//	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedPublication = null;
		selectedResearcher = null;
		justJournals = false;
		justConferences = false;
		justOther = false;
		startYear = null;
		endYear = null;
		getPaperJournalManagedBean().resetForm();
		getPaperProceedingsManagedBean().resetForm();
		getMonographManagedBean().resetForm();
		getPaperMonographManagedBean().resetForm();
		getStudyFinalDocumentManagedBean().resetForm();
		getProductManagedBean().resetForm();
		getPatentManagedBean().resetForm();
		return super.resetForm();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList() {
		try {
			debug("populateList");
			List<PublicationDTO> listTmp = getPublications(
					createQuery(), orderBy, direction, new AllDocCollector(((orderList==true)&& (orderBy==null))));
			if (init == true && listTmp.size() != 0
					&& selectedPublication != null
					&& selectedPublication.getControlNumber() != null) {

				int index = -1;
				for (int i = 0; i < listTmp.size(); i++) {
					if (listTmp.get(i).getControlNumber().equals(
							selectedPublication.getControlNumber())) {
						index = i;
					}
				}
				if (index != -1) {
					UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("bibliographyTable");
					if(table!=null){
						int page = index / table.getRows();
						table.setFirst(table.getRows()*page);
					}
				}
				init = false;
			}

			list = listTmp;
		} catch (ParseException e) {
			error("populateList", e);
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
				if (dto != null) {
					retVal.add(dto);
				}
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
			Collections.sort(retVal, new GenericComparator<RecordDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}

	/**
	 * @return the list of publications (filtered and ordered
	 *         by ...)
	 */
	public List<PublicationDTO> getPublications() {
		if(populateList){
			populateList();
			populateList = false;
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
			Collections.sort(list, new GenericComparator<RecordDTO>(
					orderByList, directionsList));
			
		}
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
		if ((whereStr != null) && (!"".equals(whereStr)))
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		BooleanQuery type = new BooleanQuery();
		if(justJournals) {
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		} else if (justConferences) {
			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		} else if (justOther) {
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
				if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() == null)
//						||
//						(! ("(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())))
				){
					type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
				}
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PATENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PRODUCT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
			}
		} else {
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_JOURNAL).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_MONOGRAPH).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
				type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
				if((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() == null) 
//						|| 
//						(! ("(BISIS)5920".equals(getUserManagedBean().getLoggedUser().getInstitution().getControlNumber())))
						){
					type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
				}
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PATENT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
			}
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PRODUCT).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
			}
		}
		bq.add(type, Occur.MUST);
		
		
		AuthorDTO loggedAuthor = (selectedResearcher!=null)?selectedResearcher:getUserManagedBean().getLoggedUser().getAuthor();
		if (loggedAuthor.getControlNumber() != null){
			BooleanQuery authorsPapers = new BooleanQuery();
			authorsPapers.add(new TermQuery(new Term("AUCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			authorsPapers.add(new TermQuery(new Term("EDCN", loggedAuthor.getControlNumber())), Occur.SHOULD);
			bq.add(authorsPapers, Occur.MUST);
		}
		
		if ((startYear != null) && (endYear != null)){
			RangeQuery years = new RangeQuery(new Term("PY", startYear.toString()), new Term("PY", endYear.toString()), true);
			bq.add(years, Occur.MUST);
		}
		
		if(institutions == null){
			fillInstitutins();
		}
		
		
		BooleanQuery institutionQuery = new BooleanQuery();
		QueryParser qp = new QueryParser("INS", new CrisAnalyzer());
		for (InstitutionDTO institution : institutions) {
			if(getUserManagedBean().getPrivileges().get(PrivilegesManagedBean.FORM_PAPER_JOURNAL).get(PrivilegesManagedBean.PRIVILEGE_BROWSE)){
				institutionQuery.add(qp.parse("\""+StringUtils.clearDelimiters(institution.getSomeName(), QueryUtils.delims)+"\""), Occur.SHOULD);
			} else {
				institutionQuery.add(new TermQuery(new Term("INCN", institution.getControlNumber())), Occur.SHOULD);
			}
		}
		if(institutions.size() != 0)
			bq.add(institutionQuery, Occur.MUST);
		
		return bq;
	}
	
	private void fillInstitutins() {
		institutions = new ArrayList<InstitutionDTO>();
		if(selectedResearcher==null) {
			if ((getUserManagedBean().getLoggedUser().getInstitution().getControlNumber() != null)) {
				InstitutionDTO userInstitution = getUserManagedBean().getLoggedUser().getInstitution();
				institutions.add(userInstitution);
				for (RecordDTO record : userInstitution.getRelatedRecords()) {
					//				if((!(record instanceof OrganizationUnitDTO)) && (record instanceof InstitutionDTO)){
					if (((record instanceof OrganizationUnitDTO)) || (record instanceof InstitutionDTO)) {
						institutions.add((InstitutionDTO) record);
						for (RecordDTO record2 : record.getRelatedRecords()) {
							//						if((!(record instanceof OrganizationUnitDTO)) && (record instanceof InstitutionDTO)){
							if (((record2 instanceof OrganizationUnitDTO)) || (record2 instanceof InstitutionDTO)) {
								institutions.add((InstitutionDTO) record2);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @return the selected publication
	 */
	public PublicationDTO getSelectedPublication() {
		return selectedPublication;
	}

	/**
	 * @param publicationDTO
	 *            the publication to set as selected publication
	 */
	public void setSelectedPublication(
			PublicationDTO publicationDTO) {
		selectedPublication = publicationDTO;
	}

	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToBrowseMode()
	 */
	@Override
	public void switchToBrowseMode() {
		orderBy = "publicationYear";
		direction = "desc";
		super.switchToBrowseMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
			if (selectedPublication instanceof PaperJournalDTO){
				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
				pjmb.switchToSimpleUpdateMode();
				pjmb.setSelectedPaperJournal((PaperJournalDTO)selectedPublication);
			}
			else if (selectedPublication instanceof PaperProceedingsDTO){
				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
				ppmb.switchToSimpleUpdateMode();
				ppmb.setSelectedPaperProceedings((PaperProceedingsDTO)selectedPublication);
			} else if (selectedPublication instanceof MonographDTO){
				MonographManagedBean mmb = getMonographManagedBean();
				mmb.switchToSimpleUpdateMode();
				mmb.setSelectedMonograph((MonographDTO)selectedPublication);
			} else if (selectedPublication instanceof PaperMonographDTO){
				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
				pmmb.switchToSimpleUpdateMode();
				pmmb.setSelectedPaperMonograph((PaperMonographDTO)selectedPublication);
			} else if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();	
				sfdmb.setSelectedStudyFinalDocument((StudyFinalDocumentDTO)selectedPublication);
				sfdmb.switchToUpdateMode();
			} else if (selectedPublication instanceof PatentDTO){
				PatentManagedBean patmb = getPatentManagedBean();	
				patmb.switchToUpdateMode();
				patmb.setSelectedPatent((PatentDTO)selectedPublication);
			} else if (selectedPublication instanceof ProductDTO){
				ProductManagedBean promb = getProductManagedBean();	
				promb.switchToUpdateMode();
				promb.setSelectedProduct((ProductDTO)selectedPublication);
			}
			debug("switchToUpdateMode: \n" + selectedPublication);
		}
	}
	
	
	public void switchToAddEvaluationDataMode(){
		selectedPublication = findPublicationByControlNumber();
		if(selectedPublication instanceof MonographDTO){
			MonographManagedBean mmb = getMonographManagedBean();			
			mmb.setSelectedMonograph((MonographDTO)selectedPublication);
			mmb.swichToAddEvaluationData();
		}
	}
	
	
	private PaperJournalManagedBean paperJournalManagedBean = null;

	/**
	 * @return the PaperJournalManagedBean from current session
	 */
	protected PaperJournalManagedBean getPaperJournalManagedBean() {
		if (paperJournalManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			PaperJournalManagedBean mb = (PaperJournalManagedBean) extCtx.getSessionMap().get(
					"paperJournalManagedBean");
			if (mb == null) {
				mb = new PaperJournalManagedBean();
				extCtx.getSessionMap().put("paperJournalManagedBean", mb);
			}
			paperJournalManagedBean = mb;
		}
		return paperJournalManagedBean;
	}
	
	private PaperProceedingsManagedBean paperProceedingsManagedBean = null;

	/**
	 * @return the PaperProceedingsManagedBean from current session
	 */
	protected PaperProceedingsManagedBean getPaperProceedingsManagedBean() {
		if (paperProceedingsManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			PaperProceedingsManagedBean mb = (PaperProceedingsManagedBean) extCtx.getSessionMap().get(
					"paperProceedingsManagedBean");
			if (mb == null) {
				mb = new PaperProceedingsManagedBean();
				extCtx.getSessionMap().put("paperProceedingsManagedBean", mb);
			}
			paperProceedingsManagedBean = mb;
		}
		return paperProceedingsManagedBean;
	}
	
	private MonographManagedBean monographManagedBean = null;

	/**
	 * @return the MonographManagedBean from current session
	 */
	protected MonographManagedBean getMonographManagedBean() {
		if (monographManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			MonographManagedBean mb = (MonographManagedBean) extCtx.getSessionMap().get(
					"monographManagedBean");
			if (mb == null) {
				mb = new MonographManagedBean();
				extCtx.getSessionMap().put("monographManagedBean", mb);
			}
			monographManagedBean = mb;
		}
		return monographManagedBean;
	}
	
	private PaperMonographManagedBean paperMonographManagedBean = null;

	/**
	 * @return the PaperMonographManagedBean from current session
	 */
	protected PaperMonographManagedBean getPaperMonographManagedBean() {
		if (paperMonographManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			PaperMonographManagedBean mb = (PaperMonographManagedBean) extCtx.getSessionMap().get(
					"paperMonographManagedBean");
			if (mb == null) {
				mb = new PaperMonographManagedBean();
				extCtx.getSessionMap().put("paperMonographManagedBean", mb);
			}
			paperMonographManagedBean = mb;
		}
		return paperMonographManagedBean;
	}
	
	private StudyFinalDocumentManagedBean studyFinalDocumentManagedBean = null;

	/**
	 * @return the StudyFinalDocumentManagedBean from current session
	 */
	protected StudyFinalDocumentManagedBean getStudyFinalDocumentManagedBean() {
		if (studyFinalDocumentManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			StudyFinalDocumentManagedBean mb = (StudyFinalDocumentManagedBean) extCtx.getSessionMap().get(
					"studyFinalDocumentManagedBean");
			if (mb == null) {
				mb = new StudyFinalDocumentManagedBean();
				extCtx.getSessionMap().put("studyFinalDocumentManagedBean", mb);
			}
			studyFinalDocumentManagedBean = mb;
		}
		return studyFinalDocumentManagedBean;
	}
	
	private PatentManagedBean patentManagedBean = null;

	/**
	 * @return the PatentManagedBean from current session
	 */
	protected PatentManagedBean getPatentManagedBean() {
		if (patentManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			PatentManagedBean mb = (PatentManagedBean) extCtx.getSessionMap().get(
					"patentManagedBean");
			if (mb == null) {
				mb = new PatentManagedBean();
				extCtx.getSessionMap().put("patentManagedBean", mb);
			}
			patentManagedBean = mb;
		}
		return patentManagedBean;
	}
	
	private ProductManagedBean productManagedBean = null;

	/**
	 * @return the ProductManagedBean from current session
	 */
	protected ProductManagedBean getProductManagedBean() {
		if (productManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ProductManagedBean mb = (ProductManagedBean) extCtx.getSessionMap().get(
					"productManagedBean");
			if (mb == null) {
				mb = new ProductManagedBean();
				extCtx.getSessionMap().put("productManagedBean", mb);
			}
			productManagedBean = mb;
		}
		return productManagedBean;
	}


	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
			if (selectedPublication instanceof PaperJournalDTO){
				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
				pjmb.switchToViewDetailsMode();
				pjmb.setSelectedPaperJournal((PaperJournalDTO)selectedPublication);
			}
			else if (selectedPublication instanceof PaperProceedingsDTO){
				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
				ppmb.switchToViewDetailsMode();
				ppmb.setSelectedPaperProceedings((PaperProceedingsDTO)selectedPublication);
			} else if (selectedPublication instanceof MonographDTO){
				MonographManagedBean mmb = getMonographManagedBean();
				mmb.switchToViewDetailsMode();
				mmb.setSelectedMonograph((MonographDTO)selectedPublication);
			} else if (selectedPublication instanceof PaperMonographDTO){
				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
				pmmb.switchToViewDetailsMode();
				pmmb.setSelectedPaperMonograph((PaperMonographDTO)selectedPublication);
			} else if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();	
				sfdmb.setSelectedStudyFinalDocument((StudyFinalDocumentDTO)selectedPublication);
				sfdmb.switchToViewDetailsMode();
			} else if (selectedPublication instanceof PatentDTO){
				PatentManagedBean patmb = getPatentManagedBean();	
				patmb.switchToViewDetailsMode();
				patmb.setSelectedPatent((PatentDTO)selectedPublication);
			} else if (selectedPublication instanceof ProductDTO){
				ProductManagedBean promb = getProductManagedBean();	
				promb.switchToViewDetailsMode();
				promb.setSelectedProduct((ProductDTO)selectedPublication);
			}
		}
	}	

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#archive()
	 */
	@Override
	public void archive() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.list = new ArrayList<StudyFinalDocumentDTO>();
				sfdmb.list.add((StudyFinalDocumentDTO) selectedPublication);
				sfdmb.archive();
				
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	public void switchToUpdateRegisterEntryMode() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.switchToUpdateRegisterEntryMode((StudyFinalDocumentDTO)selectedPublication);
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	public void extractArchive() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.list = new ArrayList<StudyFinalDocumentDTO>();
				sfdmb.list.add((StudyFinalDocumentDTO) selectedPublication);
				sfdmb.extractArchive();
				
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	public void relatedPublications() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.setStudyFinalDocumentPublications((StudyFinalDocumentDTO)selectedPublication);
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	public void setUnavailableToThePublic() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.list = new ArrayList<StudyFinalDocumentDTO>();
				sfdmb.list.add((StudyFinalDocumentDTO) selectedPublication);
				sfdmb.setUnavailableToThePublic();
				
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	public void setAvailableToThePublic() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
//			if (selectedPublication instanceof PaperJournalDTO){
//				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
//				
//			}
//			else if (selectedPublication instanceof PaperProceedingsDTO){
//				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
//				
//			} else if (selectedPublication instanceof MonographDTO){
//				MonographManagedBean mmb = getMonographManagedBean();
//				
//			} else if (selectedPublication instanceof PaperMonographDTO){
//				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
//				
//			} else 
			if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();
				sfdmb.list = new ArrayList<StudyFinalDocumentDTO>();
				sfdmb.list.add((StudyFinalDocumentDTO) selectedPublication);
				sfdmb.setAvailableToThePublic();
				
			} 
//			else if (selectedPublication instanceof PatentDTO){
//				PatentManagedBean patmb = getPatentManagedBean();	
//				
//			} else if (selectedPublication instanceof ProductDTO){
//				ProductManagedBean promb = getProductManagedBean();	
//				
//			}
		}
	}
	
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "bibliographyPage";
		return retVal;
	}


	private PublicationDTO findPublicationByControlNumber() {
		PublicationDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = facesCtx.getExternalContext()
					.getRequestParameterMap().get("controlNumber");
			for (PublicationDTO dto : list) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
								.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedPublication = findPublicationByControlNumber();
		if (selectedPublication != null) {
			if (selectedPublication instanceof PaperJournalDTO){
				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();
				pjmb.switchToBrowseMode();
				pjmb.setJournal(((PaperJournalDTO)selectedPublication).getJournal());
				pjmb.populateList();
				pjmb.delete();
			}
			else if (selectedPublication instanceof PaperProceedingsDTO){
				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();
				ppmb.switchToBrowseMode();
				ppmb.setConference(((PaperProceedingsDTO)selectedPublication).getProceedings().getConference());
				ppmb.setProceedings(((PaperProceedingsDTO)selectedPublication).getProceedings());
				ppmb.populateList();
				ppmb.delete();
			} else if (selectedPublication instanceof MonographDTO){
				MonographManagedBean mmb = getMonographManagedBean();
				mmb.switchToBrowseMode();
				mmb.populateList();
				mmb.delete();
			} else if (selectedPublication instanceof PaperMonographDTO){
				PaperMonographManagedBean pmmb = getPaperMonographManagedBean();
				pmmb.switchToBrowseMode();
				pmmb.setMonograph(((PaperMonographDTO)selectedPublication).getMonograph());
				pmmb.populateList();
				pmmb.delete();
			} else if (selectedPublication instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();	
//				sfdmb.switchToBrowseMode();
//				sfdmb.populateList();
				sfdmb.list = new ArrayList<StudyFinalDocumentDTO>();
				sfdmb.list.add((StudyFinalDocumentDTO) selectedPublication);
				sfdmb.delete();
			} else if (selectedPublication instanceof PatentDTO){
				PatentManagedBean patmb = getPatentManagedBean();	
				patmb.switchToBrowseMode();
				patmb.populateList();
				patmb.delete();
			} else if (selectedPublication instanceof ProductDTO){
				ProductManagedBean promb = getProductManagedBean();	
				promb.switchToBrowseMode();
				promb.populateList();
				promb.delete();
			}
			this.populateList = true;
			this.orderList = true;
			debug("switchToUpdateMode: \n" + selectedPublication);
		}
	}
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPageJournals() {
		String temp = this.whereStr;
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		this.whereStr = temp;
		tableModalPanel = "";
		justJournals = true;
		justConferences = false;
		justOther = false;
		switchToBrowseMode();
		return retVal;
	}
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPageConferences() {
		String temp = this.whereStr;
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		this.whereStr = temp;
		tableModalPanel = "";
		justJournals = false;
		justConferences = true;
		justOther = false;
		switchToBrowseMode();
		return retVal;
	}

	/**
	 * Prepares CRUD page for using
	 *
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPageOther() {
		String temp = this.whereStr;
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		this.whereStr = temp;
		tableModalPanel = "";
		justJournals = false;
		justConferences = false;
		justOther = true;
		switchToBrowseMode();
		return retVal;
	}
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPageJournals20022011() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		tableModalPanel = "";
		justJournals = true;
		justConferences = false;
		justOther = false;
		startYear = 2004;
		endYear = 2013;
		switchToBrowseMode();
		return retVal;
	}
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPage2011() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		tableModalPanel = "";
		startYear = 2013;
		endYear = 2013;
		switchToBrowseMode();
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
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


	public AuthorDTO getSelectedResearcher() {
		return selectedResearcher;
	}

	public void setSelectedResearcher(AuthorDTO selectedResearcher) {
		this.selectedResearcher = selectedResearcher;
	}
}
