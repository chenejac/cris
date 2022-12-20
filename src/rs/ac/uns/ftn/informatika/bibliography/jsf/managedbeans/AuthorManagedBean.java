package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.richfaces.component.UIDataTable;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorPosition;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Person;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import com.gint.util.string.StringUtils;

/**
 * Managed bean with CRUD operations for authors
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class AuthorManagedBean extends CRUDManagedBean implements IPickInstitutionManagedBean, IPickPositionManagedBean{

	private List<AuthorDTO> list;
	
	private List<AuthorDTO> duplicateAuthors;

	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());

	private AuthorDTO selectedAuthor = null;
	
	private AuthorDTO coauthor = null;

	private InstitutionDTO selectedOrganizationUnit = null;

	public AuthorManagedBean(){
		pickSimilarMessage = "records.author.pickSimilarMessage";
		tableModalPanel = "authorBrowseModalPanel";
		editModalPanel = "authorEditModalPanel";
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#resetForm()
	 */
	@Override
	public String resetForm() {
		selectedOrganizationUnit = null;
		firstnameOtherFormat = "";
		lastnameOtherFormat = "";
		selectedAuthor = null;
		coauthor = null;
		pickMessage = null;
		pickMessageFirstTab = null;
		pickMessageSecondTabSimilarNotExistFirstSentence = null;
		pickMessageSecondTabSimilarNotExistSecondSentence = null;
		pickMessageSecondTabSimilarExistFirstSentence = null;
		pickMessageSecondTabSimilarExistSecondSentence = null;
		pickMessageSecondTabSimilarExistThirdSentence = null;
		pickMessageSecondTabSimilarExistFourthSentence = null;
		iPickAuthorManagedBean = null;
		pleaseInstitution = false;
		pleaseInstitutionMessage = null;
		tableModalPanel = "authorBrowseModalPanel";
		editModalPanel = "authorEditModalPanel";
		return super.resetForm();
	}
	
	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
		if (editMode == ModesManagedBean.MODE_ADD_FORMAT_NAME){
			editModalPanel = "authorNameFormatsModalPanel";
		} else {
			editModalPanel = "authorEditModalPanel";
		}
		super.setEditMode(editMode);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#populateList()
	 */
	@Override
	public void populateList(){
		try {
			debug("populateList");
			if(coauthor == null){
				List<AuthorDTO> listTmp = getAuthors(createQuery(),
						orderBy, direction, new AllDocCollector((orderList == false)&&(orderBy==null)));
				if (init == true && listTmp.size() != 0 && selectedAuthor != null
						&& selectedAuthor.getControlNumber() != null) {
	
					int index = -1;
					for (int i = 0; i < listTmp.size(); i++) {
						if (listTmp.get(i).getControlNumber().equals(
								selectedAuthor.getControlNumber())) {
							index = i;
						}
					}
					if (index != -1) {
						UIDataTable table = (UIDataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("authorTable");
						if(table!=null){
							int page = index / table.getRows();
							table.setFirst(table.getRows()*page);
						}
					}
					init = false;
				}
				list = listTmp;
			} else {
				list = new ArrayList<AuthorDTO>();
				FacesContext facesCtx = FacesContext.getCurrentInstance();
				ExternalContext extCtx = facesCtx.getExternalContext();
				BibliographyManagedBean mb = (BibliographyManagedBean) extCtx.getSessionMap().get(
						"bibliographyManagedBean");
				if (mb == null) {
					mb = new BibliographyManagedBean();
					extCtx.getSessionMap().put("bibliographyManagedBean", mb);
				}
				mb.switchToBrowseMode();
				List<PublicationDTO> publications = mb.getPublications();
				for (int i = 0; i < publications.size(); ) {
					PublicationDTO publicationDTO = publications.get(i);
					List<AuthorDTO> authors = publicationDTO.getAllAuthors();
					for (AuthorDTO authorDTO : authors) {
						if((coauthor==null) || ((! coauthor.equals(authorDTO)) && (! list.contains(authorDTO))))
							list.add(authorDTO);
					}
					List<AuthorDTO> editors = publicationDTO.getEditors();
					for (AuthorDTO editorDTO : editors) {
						if((coauthor==null) || ((! coauthor.equals(editorDTO)) && (! list.contains(editorDTO))))
							list.add(editorDTO);
					}
					publications.remove(i);
				}
				orderList = true;
			}
		} catch (ParseException e) {
			error("populateList", e);
			list = new ArrayList<AuthorDTO>();
		}
	}
	
	/**
	 * Retrieves a list of authors that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving authors
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return the list of authors that correspond to the query
	 */
	@SuppressWarnings("unused")
	private List<AuthorDTO> getAuthors(String query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		Query q = QueryUtils.parseQuery(query,
				new CrisAnalyzer(), Operator.AND);
		return getAuthors(q, orderBy, direction, hitCollector);
	}
	
	/**
	 * Retrieves a list of authors that correspond to the query.
	 * 
	 * @param query
	 *            query for retrieving authors
	 * @param orderBy
	 *            field used for sorting
	 * @param direction
	 *            direction used for sorting
	 * @param hitCollector
	 *            hitCollector
	 * @return the list of authors that correspond to the query
	 */
	private List<AuthorDTO> getAuthors(Query query, String orderBy,
			String direction, HitCollector hitCollector) throws ParseException {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		
		List<Record> list = personDAO.getDTOs(query, hitCollector);
		for (Record record : list) {
			try {
				AuthorDTO dto = (AuthorDTO) record.getDto();
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
			Collections.sort(retVal, new GenericComparator<AuthorDTO>(
					orderByList, directionsList));
		}
		return retVal;
	}
	
	/**
	 * @return the list of authors (filtered and ordered by ...)
	 */
	public List<AuthorDTO> getAuthors() {
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
			Collections.sort(list, new GenericComparator<AuthorDTO>(
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
	private Query createQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		if((tableMode == ModesManagedBean.MODE_PICK) && (customPick)){
			if (selectedOrganizationUnit != null) {
				String OUCN = null;
				String INCN = null;
				if (selectedOrganizationUnit instanceof OrganizationUnitDTO)
					OUCN = selectedOrganizationUnit.getControlNumber();
				else
					INCN = selectedOrganizationUnit.getControlNumber();
				if (OUCN != null) {
					bq.add(personDAO.getInstitutionRecordsQuery(OUCN, "2022-01-01 00:00:00"), BooleanClause.Occur.MUST);
				} else if (INCN != null) {
					bq.add(personDAO.getInstitutionRecordsQuery(INCN, "2022-01-01 00:00:00"), BooleanClause.Occur.MUST);
				}
			}
			if ((firstname != null) && (!"".equals(firstname))){
				bq.add(QueryUtils.makeBooleanQuery("AU", firstname, Occur.SHOULD, 0.8f, 0.7f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("AU", firstname, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
			if ((lastname != null) && (!"".equals(lastname))){
				bq.add(QueryUtils.makeBooleanQuery("AU", lastname, Occur.SHOULD, 0.8f, 0.7f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("AU", lastname, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
			if ((otherName != null) && (!"".equals(otherName))){
				bq.add(QueryUtils.makeBooleanQuery("AU", otherName, Occur.SHOULD, 0.8f, 0.7f, false), Occur.MUST);
				bq.add(QueryUtils.makeBooleanQuery("AU", otherName, Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
			}
		} else if ((whereStr != null) && (!"".equals(whereStr))) 
			bq.add(QueryUtils.parseQuery(StringUtils.clearDelimiters(whereStr, Indexer.delims), new CrisAnalyzer(), Operator.AND), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		return bq;
	}
	
	List<AuthorDTO> similarAuthors = null;
	
	/**
	 * @return the list of SIMILAR authors with selected author
	 */
	public List<AuthorDTO> getSimilarAuthors() {
		return similarAuthors;
	}

	/**
	 * Creates query for finding SIMILAR authors with selected
	 * 
	 * @return the created query
	 * @throws ParseException
	 */
	private Query createSimilarAuthorsQuery() throws ParseException{
		BooleanQuery bq = new BooleanQuery();
		bq.add(QueryUtils.makeBooleanQuery("AU", selectedAuthor.getName().getFirstname(), Occur.SHOULD, 0.5f, 0.6f, false), Occur.MUST);
		bq.add(QueryUtils.makeBooleanQuery("AU", selectedAuthor.getName().getLastname(), Occur.SHOULD, 0.5f, 0.6f, false), Occur.MUST);
		bq.add(QueryUtils.makeBooleanQuery("AU", selectedAuthor.getName().getFirstname() + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		bq.add(QueryUtils.makeBooleanQuery("AU", selectedAuthor.getName().getLastname() + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		return bq;
	}
	
	public List<AuthorDTO> autocomplete(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        if(selectedAuthor!=null && selectedAuthor.getControlNumber() != null){
        	retVal.add(selectedAuthor);
        	return retVal;
        }
        String authorLastname = suggest;
    
        BooleanQuery bq = new BooleanQuery();
		if(selectedOrganizationUnit != null) {
			String OUCN = null;
			String INCN = null;
			if (selectedOrganizationUnit instanceof OrganizationUnitDTO)
				OUCN = selectedOrganizationUnit.getControlNumber();
			else
				INCN = selectedOrganizationUnit.getControlNumber();
			if (OUCN != null) {
				bq.add(personDAO.getInstitutionRecordsQuery(OUCN, "2022-01-01 00:00:00"), BooleanClause.Occur.MUST);
			} else if (INCN != null) {
				bq.add(personDAO.getInstitutionRecordsQuery(INCN, "2022-01-01 00:00:00"), BooleanClause.Occur.MUST);
			}
		}
		if(authorLastname != null)
			bq.add(QueryUtils.makeBooleanQuery("AU", authorLastname + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		if ((firstname != null) && (!"".equals(firstname)))
			bq.add(QueryUtils.makeBooleanQuery("AU", firstname, Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		

		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
				if (dto != null) {
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	

	public List<AuthorDTO> autocompleteForSearch(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        String authorLastname = suggest;
        
        
        StringTokenizer st = new StringTokenizer(suggest);
        StringBuilder temp = new StringBuilder();
        if (st.countTokens() == 3){
        	temp.append(st.nextToken());
        	String otherName = st.nextToken().replace(".", "");
        	if(otherName.length() == 1){
        		temp.append(" " + st.nextToken());
        		authorLastname = temp.toString();
        	} 
        }
        
        BooleanQuery bq = new BooleanQuery();
		if(authorLastname != null){
			bq.add(QueryUtils.makeBooleanQuery("AU", authorLastname + ".", Occur.SHOULD, 0.8f, 0.6f, false), Occur.MUST);
			bq.add(QueryUtils.makeBooleanQuery("AU", authorLastname + ".", Occur.SHOULD, 0.99f, 0.99f, false), Occur.SHOULD);
		}
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		

		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
				if (dto != null) {				
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	private String queryFieldName = null;
	
	/**
	 * @return the queryFieldName
	 */
	public String getQueryFieldName() {
		return queryFieldName;
	}

	/**
	 * @param queryFieldName the queryFieldName to set
	 */
	public void setQueryFieldName(String queryFieldName) {
		this.queryFieldName = queryFieldName;
	}

	public List<AuthorDTO> autocompleteForSearchDissertations(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
		if(queryFieldName!=null){
	        if(selectedAuthor!=null && selectedAuthor.getControlNumber() != null){
	        	retVal.add(selectedAuthor);
	        	return retVal;
	        }
		}
        
        BooleanQuery bq = new BooleanQuery();
		if(suggest != null)
			bq.add(QueryUtils.makeBooleanQuery("AU", suggest + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		if(queryFieldName != null){
			if("AU".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("AUTHORUNSDISSERTATIONS", "true")), Occur.MUST);
			} else if("AD".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("ADVISORUNSDISSERTATIONS", "true")), Occur.MUST);
			} else if("CC".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("COMMISSIONCHAIRUNSDISSERTATIONS", "true")), Occur.MUST);
			} else if("CM".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("COMMISSIONMEMBERUNSDISSERTATIONS", "true")), Occur.MUST);
			}
		} else {
			BooleanQuery unsDissertation = new BooleanQuery();
			unsDissertation.add(new TermQuery(new Term("AUTHORUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("ADVISORUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONCHAIRUNSDISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONMEMBERUNSDISSERTATIONS", "true")), Occur.SHOULD);
			bq.add(unsDissertation, Occur.MUST);
		}

		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
				if (dto != null) {				
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<AuthorDTO> autocompleteForSearchDissertationsPA(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
		if(queryFieldName!=null){
	        if(selectedAuthor!=null && selectedAuthor.getControlNumber() != null){
	        	retVal.add(selectedAuthor);
	        	return retVal;
	        }
		}
        
        BooleanQuery bq = new BooleanQuery();
		if(suggest != null)
			bq.add(QueryUtils.makeBooleanQuery("AU", suggest + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		if(queryFieldName != null){
			if("AU".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("AUTHORPADISSERTATIONS", "true")), Occur.MUST);
			} else if("AD".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("ADVISORPADISSERTATIONS", "true")), Occur.MUST);
			} else if("CC".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("COMMISSIONCHAIRPADISSERTATIONS", "true")), Occur.MUST);
			} else if("CM".equals(queryFieldName)){
				bq.add(new TermQuery(new Term("COMMISSIONMEMBERPADISSERTATIONS", "true")), Occur.MUST);
			}
		} else {
			BooleanQuery unsDissertation = new BooleanQuery();
			unsDissertation.add(new TermQuery(new Term("AUTHORPADISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("ADVISORPADISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONCHAIRPADISSERTATIONS", "true")), Occur.SHOULD);
			unsDissertation.add(new TermQuery(new Term("COMMISSIONMEMBERPADISSERTATIONS", "true")), Occur.SHOULD);
			bq.add(unsDissertation, Occur.MUST);
		}
		
		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
				if (dto != null) {				
					retVal.add(dto);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }
	
	public List<AuthorDTO> autocompleteForAndrejevicSearch(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        String authorLastname = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(authorLastname != null)
			bq.add(QueryUtils.makeBooleanQuery("AU", authorLastname + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		if ((firstname != null) && (!"".equals(firstname)))
			bq.add(QueryUtils.makeBooleanQuery("AU", firstname, Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		

		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
//				dto.getName().getFirstname();
//				dto.getName().getLastname();
				if (dto != null) {
					
					BooleanQuery bqMonograph = new BooleanQuery();
					
					BooleanQuery bqAuthorRole = new BooleanQuery();
					bqAuthorRole.add(new TermQuery(new Term("AUCN", dto.getControlNumber())), Occur.SHOULD);
					bqAuthorRole.add(new TermQuery(new Term("EDCN", dto.getControlNumber())), Occur.SHOULD);
					
					bqMonograph.add(bqAuthorRole, Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
					
					List<Record> listRecordMonograph = recordDAO.getDTOs(bqMonograph, new AllDocCollector(true));
					if(listRecordMonograph.size()>0){
						retVal.add(dto);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
    }

	public List<AuthorDTO> autocompleteForFirstAndrejevicSearch(String suggest) {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
        if(suggest.contains("(BISIS)")){
        	retVal.add((AuthorDTO)recordDAO.getDTO(suggest));
        	return retVal;
        }
        String authorLastname = suggest;
        
        BooleanQuery bq = new BooleanQuery();
		if(authorLastname != null)
			bq.add(QueryUtils.makeBooleanQuery("AU", authorLastname + ".", Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		if ((firstname != null) && (!"".equals(firstname)))
			bq.add(QueryUtils.makeBooleanQuery("AU", firstname, Occur.SHOULD, 0.8f, 0.5f, false), Occur.MUST);
		bq.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.MUST);
		

		List<Record> listRecord = personDAO.getDTOs(bq, new AllDocCollector(true));
		for (Record recordDTO : listRecord) {
			try {
				AuthorDTO dto = (AuthorDTO) recordDTO.getDto();
//				dto.getName().getFirstname();
//				dto.getName().getLastname();
				if (dto != null) {
					
					BooleanQuery bqMonograph = new BooleanQuery();		
					bqMonograph.add(new TermQuery(new Term("FACN", dto.getControlNumber())), Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("PU", "andrejevic")), Occur.MUST);
					bqMonograph.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.MUST);
					
					List<Record> listRecordMonograph = recordDAO.getDTOs(bqMonograph, new AllDocCollector(true));
					if(listRecordMonograph.size()>0){
						retVal.add(dto);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		return retVal;
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
	 * @return the coauthor
	 */
	public AuthorDTO getCoauthor() {
		return coauthor;
	}

	/**
	 * @param coauthor the coauthor to set
	 */
	public void setCoauthor(AuthorDTO coauthor) {
		this.coauthor = coauthor;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToUpdateMode()
	 */
	@Override
	public void switchToUpdateMode() {
		selectedAuthor = findAuthorByControlNumber(list);
		if (selectedAuthor != null) {
			super.switchToUpdateMode();
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
			setInstitutionManageBeanToPick();
			debug("switchToUpdateMode: \n" + selectedAuthor);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToAddMode()
	 */
	@Override
	public void switchToAddMode() {
		super.switchToAddMode();
		selectedAuthor = new AuthorDTO();
		selectedAuthor.getName().setFirstname(firstname);
		selectedAuthor.getName().setLastname(lastname);
		selectedAuthor.getName().setOtherName(otherName);
		setInstitutionManageBeanToPick();
		if(getUserManagedBean().editMode == ModesManagedBean.MODE_REGISTER){
			add();
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToViewDetailsMode()
	 */
	@Override
	public void switchToViewDetailsMode() {
		selectedAuthor = findAuthorByControlNumber(list);
		if (selectedAuthor != null) {
			super.switchToViewDetailsMode();
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
			debug("switchToViewDetailsMode: \n" + selectedAuthor);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToImportMode()
	 */
	@Override
	public void switchToImportMode() {
		try {
			debug("findSimilarAuthors");
			orderList = false;
			similarAuthors = getAuthors(createSimilarAuthorsQuery(),
					null, null,  new AllDocCollector(true));
			editTabNumber = 1;
			authorControlNumber = null;
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
			super.switchToImportMode();
		} catch (ParseException e) {
			error("findSimilarAuthors", e);
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToEditNoneMode()
	 */
	@Override
	public void switchToEditNoneMode() {
		if(editMode != ModesManagedBean.MODE_IMPORT)
			selectedAuthor.setNotLoaded(true);
		if((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE))
			finishWizard();
		else 
			super.switchToEditNoneMode();
	}

	@Override
	public void switchToBrowseMode() {
		selectedOrganizationUnit = null;
		super.switchToBrowseMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToPickMode()
	 */
	@Override
	public void switchToPickMode() {
		super.switchToPickMode();
		firstname = null;
		lastname = null;
		otherName = null;
		authorControlNumber = null;
		duplicateAuthors = new ArrayList<AuthorDTO>();
	}

	public void examineData(){
		mergeAuthor = selectedAuthor;
		
		selectedAuthor = findAuthorByControlNumber(similarAuthors);
		if(selectedAuthor == null){
			selectedAuthor = mergeAuthor;
			mergeAuthor = null;
		} else {
			if(! selectedAuthor.getAllNames().contains(mergeAuthor.getName()))
				selectedAuthor.getOtherFormatNames().add(mergeAuthor.getName());
			if(selectedAuthor.getScopusID() == null || "".equals(selectedAuthor.getScopusID())){
				selectedAuthor.setScopusID(mergeAuthor.getScopusID());
			}
			if(selectedAuthor.getORCID() == null || "".equals(selectedAuthor.getORCID())){
				selectedAuthor.setORCID(mergeAuthor.getORCID());
			}
			if(selectedAuthor.getInstitution().getControlNumber() == null)
				selectedAuthor.setInstitution(mergeAuthor.getInstitution());
			if(selectedAuthor.getOrganizationUnit().getControlNumber() == null)
				selectedAuthor.setOrganizationUnit(mergeAuthor.getOrganizationUnit());
			if(selectedAuthor.getAddress() == null)
				selectedAuthor.setAddress(mergeAuthor.getAddress());
			if(selectedAuthor.getCity() == null)
				selectedAuthor.setCity(mergeAuthor.getCity());
			if((selectedAuthor.getSex() != 'm') && (selectedAuthor.getSex() != 'f'))
				selectedAuthor.setSex(mergeAuthor.getSex());
			if(selectedAuthor.getDirectPhones() == null)
				selectedAuthor.setDirectPhones(mergeAuthor.getDirectPhones());
			if(selectedAuthor.getTitleInstitution().getInstitution().getControlNumber() == null)
				selectedAuthor.getTitleInstitution().setInstitution(mergeAuthor.getTitleInstitution().getInstitution());
			if(selectedAuthor.getTitleInstitution().getYear() == null)
				selectedAuthor.getTitleInstitution().setYear(mergeAuthor.getTitleInstitution().getYear());
			if(selectedAuthor.getTitle().equals(""))
				selectedAuthor.setTitle(mergeAuthor.getTitle());
			if(selectedAuthor.getEmail() == null)
				selectedAuthor.setEmail(mergeAuthor.getEmail());
			if(selectedAuthor.getPlaceOfBirth() == null)
				selectedAuthor.setPlaceOfBirth(mergeAuthor.getPlaceOfBirth());
			if(selectedAuthor.getState() == null)
				selectedAuthor.setState(mergeAuthor.getState());
			if(selectedAuthor.getDateOfBirth() == null)
				selectedAuthor.setDateOfBirth(mergeAuthor.getDateOfBirth());
			if("".equals(selectedAuthor.getName().getOtherName()));
				selectedAuthor.getName().setOtherName(mergeAuthor.getName().getOtherName());
		}
		editTabNumber = 0;
		//if(populateImportMessages() == false){
			update();
			finishWizard();
		//}
	}
	
	public void examineData(AuthorDTO author){
		mergeAuthor = selectedAuthor;
		
		selectedAuthor = author;
		if(selectedAuthor == null){
			selectedAuthor = mergeAuthor;
			mergeAuthor = null;
		} else {
			if(! selectedAuthor.getAllNames().contains(mergeAuthor.getName()))
				selectedAuthor.getOtherFormatNames().add(mergeAuthor.getName());
			if(selectedAuthor.getScopusID() == null || "".equals(selectedAuthor.getScopusID()))
				selectedAuthor.setScopusID(mergeAuthor.getScopusID());
			if(selectedAuthor.getORCID() == null || "".equals(selectedAuthor.getORCID()))
				selectedAuthor.setORCID(mergeAuthor.getORCID());
			if(selectedAuthor.getInstitution().getControlNumber() == null)
				selectedAuthor.setInstitution(mergeAuthor.getInstitution());
			if(selectedAuthor.getOrganizationUnit().getControlNumber() == null)
				selectedAuthor.setOrganizationUnit(mergeAuthor.getOrganizationUnit());
			if(selectedAuthor.getAddress() == null)
				selectedAuthor.setAddress(mergeAuthor.getAddress());
			if(selectedAuthor.getCity() == null)
				selectedAuthor.setCity(mergeAuthor.getCity());
			if((selectedAuthor.getSex() != 'm') && (selectedAuthor.getSex() != 'f'))
				selectedAuthor.setSex(mergeAuthor.getSex());
			if(selectedAuthor.getDirectPhones() == null)
				selectedAuthor.setDirectPhones(mergeAuthor.getDirectPhones());
			if(selectedAuthor.getTitleInstitution().getInstitution().getControlNumber() == null)
				selectedAuthor.getTitleInstitution().setInstitution(mergeAuthor.getTitleInstitution().getInstitution());
			if(selectedAuthor.getTitleInstitution().getYear() == null)
				selectedAuthor.getTitleInstitution().setYear(mergeAuthor.getTitleInstitution().getYear());
			if(selectedAuthor.getTitle().equals(""))
				selectedAuthor.setTitle(mergeAuthor.getTitle());
			if(selectedAuthor.getEmail() == null)
				selectedAuthor.setEmail(mergeAuthor.getEmail());
			if(selectedAuthor.getPlaceOfBirth() == null)
				selectedAuthor.setPlaceOfBirth(mergeAuthor.getPlaceOfBirth());
			if(selectedAuthor.getState() == null)
				selectedAuthor.setState(mergeAuthor.getState());
			if(selectedAuthor.getDateOfBirth() == null)
				selectedAuthor.setDateOfBirth(mergeAuthor.getDateOfBirth());
			if("".equals(selectedAuthor.getName().getOtherName()));
				selectedAuthor.getName().setOtherName(mergeAuthor.getName().getOtherName());
		}
		editTabNumber = 0;
		populateImportMessages();
	}
	
	public void mergeData(){
		mergeAuthor = findAuthorByControlNumber(similarAuthors);
		if(mergeAuthor != null){
			selectedAuthor.setControlNumber(mergeAuthor.getControlNumber());
			editTabNumber = 0;
			populateImportMessages();
		}
	}
	
	private AuthorDTO mergeAuthor;
	
	public boolean populateImportMessages(){
		boolean retVal = false;
		if(mergeAuthor != null){
			/*if((mergeAuthor.getName().getLastname() != null) && (mergeAuthor.getName().getLastname().trim().length()>0))
				facesMessages.addToControl(
					"authorEditForm:lastnameOtherFormat", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getName().getLastname(), FacesContext
							.getCurrentInstance());
			if((mergeAuthor.getName().getFirstname() != null) && (mergeAuthor.getName().getFirstname().trim().length()>0))
				facesMessages.addToControl(
					"authorEditForm:firstnameOtherFormat", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getName().getFirstname(), FacesContext
							.getCurrentInstance()); */
			if((mergeAuthor.getAddress() != null) && (mergeAuthor.getAddress().trim().length()>0))
				if((selectedAuthor.getAddress() != null) && (! selectedAuthor.getAddress().equals(mergeAuthor.getAddress()))){
					facesMessages.addToControl(
							"authorEditForm:address", FacesMessage.SEVERITY_INFO, 
							mergeAuthor.getAddress(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeAuthor.getCity() != null) && (mergeAuthor.getCity().trim().length()>0))
				if((selectedAuthor.getCity() != null) && (!selectedAuthor.getCity().equals(mergeAuthor.getCity()))){
					facesMessages.addToControl(
							"authorEditForm:city", FacesMessage.SEVERITY_INFO, 
							mergeAuthor.getCity(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if(mergeAuthor.getYearOfBirth() != null)
				if((selectedAuthor.getYearOfBirth() != null) && (! selectedAuthor.getYearOfBirth().equals(mergeAuthor.getYearOfBirth()))){
					facesMessages.addToControl(
							"authorEditForm:yearOfBirth", FacesMessage.SEVERITY_INFO, 
							mergeAuthor.getYearOfBirth().toString(), FacesContext
								.getCurrentInstance());
					retVal = true;
				}
			if((mergeAuthor.getInstitution().toString() != null) && (mergeAuthor.getInstitution().toString().trim().length()>0)){
					facesMessages.addToControl(
								"authorEditForm:institution", FacesMessage.SEVERITY_INFO, 
								mergeAuthor.getInstitution().toString(), FacesContext
								.getCurrentInstance());
					retVal = true;
			}
			if((mergeAuthor.getOrganizationUnit().toString() != null) && (mergeAuthor.getOrganizationUnit().toString().trim().length()>0)){
				facesMessages.addToControl(
						"authorEditForm:organizationUnit", FacesMessage.SEVERITY_INFO, 
						mergeAuthor.getOrganizationUnit().toString(), FacesContext
						.getCurrentInstance());
				retVal = true;
			}
			if((mergeAuthor.getTitle() != null) && (mergeAuthor.getTitle().trim().length()>0)){
				facesMessages.addToControl(
					"authorEditForm:title", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getTitle(), FacesContext
							.getCurrentInstance());
				retVal = true;
			}
			if((mergeAuthor.getTitleInstitution().getInstitution().toString() != null) && (mergeAuthor.getTitleInstitution().getInstitution().toString().trim().length()>0)){
				facesMessages.addToControl(
					"authorEditForm:titleInstitution", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getTitleInstitution().toString(), FacesContext
							.getCurrentInstance());
				retVal = true;
			}
			if(mergeAuthor.getTitleInstitution().getYear() != null) {
				facesMessages.addToControl(
					"authorEditForm:titleYear", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getTitleInstitution().getYear().toString(), FacesContext
							.getCurrentInstance());
				retVal = true;
			}
			if((mergeAuthor.getCurrentPosition().toString() != null) && (mergeAuthor.getCurrentPosition().toString().trim().length()>0)){
				String dateString = null;
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				if(mergeAuthor.getCurrentPosition().getStartDate() != null){
					Date date = mergeAuthor.getCurrentPosition().getStartDate().getTime();
					if(date != null)
						dateString = formatter.format(date);
				}
				facesMessages.addToControl(
					"authorEditForm:position", FacesMessage.SEVERITY_INFO, 
					mergeAuthor.getCurrentPosition().toString() + ((dateString!=null)?(", " + dateString):("")), FacesContext
							.getCurrentInstance());
				retVal = true;
			}
		}
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#update()
	 */
	@Override
	public void update() {
		if((selectedAuthor.getControlNumber() == null) || (! selectedAuthor.getControlNumber().contains("BISIS")))
			add();
		else {
			if (personDAO.update(new Person(null, null, getUserManagedBean()
		
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
				selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false) {
			facesMessages.addToControlFromResourceBundle(
					"authorEditForm:general", FacesMessage.SEVERITY_ERROR, "records.author.update.error",
					FacesContext.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"authorEditForm:general", FacesMessage.SEVERITY_INFO,  "records.author.update.success",
						FacesContext.getCurrentInstance());
				populateList = true;
				orderList = true;
				debug("updated: \n" + selectedAuthor);
				if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedAuthor, "update");					
			}
		}
		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#add()
	 */
	@Override
	public void add() {
		similarAuthors = null;
		if(editTabNumber == 0){
			try {
				debug("findSimilarAuthors");
				similarAuthors = getAuthors(createSimilarAuthorsQuery(),
						null, null, new TopDocCollector(5));
			} catch (ParseException e) {
				error("findSimilarAuthors", e);
			}
		}
		if((similarAuthors == null ) || (similarAuthors.size()==0) ){		
			if(getUserManagedBean().editMode != ModesManagedBean.MODE_REGISTER){
				selectedAuthor.setControlNumber(null);
				if(personDAO.add(new Person(getUserManagedBean().getLoggedUser()
						.getEmail(), new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.PERSON, 
						selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false) {
					facesMessages.addToControlFromResourceBundle(
							"authorEditForm:general", FacesMessage.SEVERITY_ERROR, "records.author.add.error",
							FacesContext.getCurrentInstance());
				} else {
					init = true;
					if(editTabNumber == 0)
						nextEditTab();
					nextEditTab();
					facesMessages.addToControlFromResourceBundle(
							"authorEditForm:general", FacesMessage.SEVERITY_INFO, "records.author.add.success",
							FacesContext.getCurrentInstance());
					populateList = true;
					orderList = true;
					debug("added: \n" + selectedAuthor);
					newRecordEmailNotification(selectedAuthor, facesMessages.getMessageFromResourceBundle("records.author.newAuthorNotification.subject"));
				}
			} else {
					selectedAuthor.setControlNumber("(TEMP)0");
					debug("added for registration: \n" + selectedAuthor);
					finishWizard();
			}
		} else {
			nextEditTab();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#delete()
	 */
	@Override
	public void delete() {
		selectedAuthor = findAuthorByControlNumber(list);
		if (selectedAuthor == null)
			return;
		if (personDAO.delete(new Record(null, null, getUserManagedBean()
				.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(100), CerifEntitiesNames.PERSON, 
				selectedAuthor)) == false) {
			facesMessages.addToControlFromResourceBundle(
					"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.author.delete.error", FacesContext
							.getCurrentInstance());
		} else {
			debug("deleted: \n" + selectedAuthor);
			selectedAuthor = null;
			populateList = true;
			orderList = true;
		}
	}
	
	/**
	 * Prepares web form where user can choose Institution
	 */
	public void pickInstitution() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx
				.getSessionMap().get("institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}

		mb.setIPickInstitutionManagedBean(this);
		mb.setSelectedInstitution(selectedAuthor.getInstitution());
		mb.setIncludeOrganizationUnits(false);
		mb.setPickMessage("records.author.pickInstitutionMessage");

		mb.setCustomPick(false);
		mb.switchToPickMode();
	}
	
	/**
	 * Prepares web form where user can choose Institution
	 */
	public void pickOrganizationUnit() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx
				.getSessionMap().get("institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}

		mb.setIPickInstitutionManagedBean(this);
		mb.setSelectedInstitution(selectedAuthor.getOrganizationUnit());
		mb.setIncludeOrganizationUnits(true);
		mb.setPickMessage("records.author.pickOrganizationUnitMessage");

		mb.setCustomPick(false);
		mb.switchToPickMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickInstitutionManagedBean#setInstitution(rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO)
	 */
	@Override
	public void setInstitution(InstitutionDTO institution) {
		if(institution instanceof OrganizationUnitDTO)
			selectedAuthor.setOrganizationUnit((OrganizationUnitDTO)institution);
		else
			selectedAuthor.setInstitution(institution);
		populateImportMessages();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickInstitutionManagedBean#cancelPickingInstitution()
	 */
	@Override
	public void cancelPickingInstitution() {
	}
	
	/**
	 * Prepares web form where user can choose Position
	 */
	public void pickPosition() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		PositionManagedBean mb = (PositionManagedBean) extCtx
				.getSessionMap().get("positionManagedBean");
		if (mb == null) {
			mb = new PositionManagedBean();
			extCtx.getSessionMap().put("positionManagedBean", mb);
		}

		mb.setIPickPositionManagedBean(this);
		mb.setSelectedPosition(null);
		mb.setPickMessage("records.author.pickPositionMessage");

		mb.setCustomPick(false);
		mb.switchToPickMode();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickPositionManagedBean#setPosition(rs.ac.uns.ftn.informatika.bibliography.dto.PositionDTO)
	 */
	@Override
	public void setPosition(PositionDTO position) {
		if(position!=null)
			selectedAuthor.getCurrentPosition().setPosition(position);
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.IPickPositionManagedBean#cancelPickingPosition()
	 */
	@Override
	public void cancelPickingPosition() {
	}

	
	public void chooseDuplicateAuthor() {

		try {
			AuthorDTO duplicateAuthor = findAuthorByControlNumber(list);
			if (duplicateAuthor != null) {
				if(duplicateAuthors.contains(duplicateAuthor))
					duplicateAuthors.remove(duplicateAuthor);
				else
					duplicateAuthors.add(duplicateAuthor);
			}
		} catch (Exception e) {
			error("chooseDuplicateAuthor", e);
		}
	}
	
	public void replaceDuplicateAuthors(){
		try {
			selectedAuthor = findAuthorByControlNumber(list);
			if ((selectedAuthor != null) && (!(duplicateAuthors.contains(selectedAuthor)))){
				
				for (AuthorDTO duplicateAuthor : duplicateAuthors) {
					BooleanQuery bq = new BooleanQuery();
					bq.add(new TermQuery(new Term("AUCN", duplicateAuthor.getControlNumber())), Occur.SHOULD);
					bq.add(new TermQuery(new Term("EDCN", duplicateAuthor.getControlNumber())), Occur.SHOULD);
					bq.add(new TermQuery(new Term("ADCN", duplicateAuthor.getControlNumber())), Occur.SHOULD);
					bq.add(new TermQuery(new Term("CMCN", duplicateAuthor.getControlNumber())), Occur.SHOULD);
					List<Record> list = recordDAO.getDTOs(bq, new AllDocCollector(false));
					boolean canDelete = true;
					for (Record record : list) {
						record.loadFromDatabase();
						RecordDTO dto = record.getDto();						
						if (dto instanceof PublicationDTO){
							PublicationDTO temp = (PublicationDTO)(dto);
							temp.getOtherAuthors().remove(selectedAuthor);
							for (AuthorDTO author : temp.getAllAuthors()) {
								if (author.equals(duplicateAuthor)){
									author.setControlNumber(selectedAuthor.getControlNumber());
									author.setInstitution(selectedAuthor.getInstitution());
									author.setOrganizationUnit(selectedAuthor.getOrganizationUnit());
								}
							}
							temp.getEditors().remove(selectedAuthor);
							for (AuthorDTO author : temp.getEditors()) {
								if (author.equals(duplicateAuthor)){
									author.setControlNumber(selectedAuthor.getControlNumber());
									author.setInstitution(selectedAuthor.getInstitution());
									author.setOrganizationUnit(selectedAuthor.getOrganizationUnit());
								}
							}
							if(dto instanceof StudyFinalDocumentDTO){
								((StudyFinalDocumentDTO)temp).getAdvisors().remove(selectedAuthor);
								for (AuthorDTO author : ((StudyFinalDocumentDTO)temp).getAdvisors()) {
									if (author.equals(duplicateAuthor)){
										author.setControlNumber(selectedAuthor.getControlNumber());
										author.setInstitution(selectedAuthor.getInstitution());
										author.setOrganizationUnit(selectedAuthor.getOrganizationUnit());
									}
								}
								((StudyFinalDocumentDTO)temp).getCommitteeMembers().remove(selectedAuthor);
								for (AuthorDTO author : ((StudyFinalDocumentDTO)temp).getCommitteeMembers()) {
									if (author.equals(duplicateAuthor)){
										author.setControlNumber(selectedAuthor.getControlNumber());
										author.setInstitution(selectedAuthor.getInstitution());
										author.setOrganizationUnit(selectedAuthor.getOrganizationUnit());
									}
								}
							}
							if (dto instanceof PatentDTO){
								if(recordDAO.update(new Record(null, null, getUserManagedBean()
										.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PATENT, 
										temp)) == false){
									facesMessages.addToControlFromResourceBundle(
											"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
											"records.author.replace.error", FacesContext
													.getCurrentInstance());
									return;
								} else {
									sendRecordMessage(temp, "update");
								}
							} else if (dto instanceof ProductDTO) {
								if(recordDAO.update(new Record(null, null, getUserManagedBean()
										.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PRODUCT, 
										temp)) == false){
									facesMessages.addToControlFromResourceBundle(
											"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
											"records.author.replace.error", FacesContext
													.getCurrentInstance());
									return;
								} else {
									sendRecordMessage(temp, "update");
								}
							} else {
								if(temp.getRecord().getArchived().intValue() != 100){
									if(recordDAO.update(new Record(null, null, getUserManagedBean()
										.getLoggedUser().getEmail(), new GregorianCalendar(), temp.getRecord().getArchived(), CerifEntitiesNames.RESULT_PUBLICATION, 
										temp)) == false){
										facesMessages.addToControlFromResourceBundle(
												"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
												"records.author.replace.error", FacesContext
														.getCurrentInstance());
										return;
									} else {
										sendRecordMessage(temp, "update");
									}
								} else {
									canDelete = false;
								}
							}
							
						} else {
							facesMessages.addToControlFromResourceBundle(
									"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
									"records.author.replace.error", FacesContext
											.getCurrentInstance());
							return;
						}
					}
					if(! canDelete){
						facesMessages.addToControlFromResourceBundle(
								"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
								"records.author.replace.error", FacesContext
										.getCurrentInstance());
						return;
					}
					selectedAuthor.getRecord().loadFromDatabase();
					for (AuthorNameDTO name : duplicateAuthor.getAllNames()) {
						if(!(selectedAuthor.getAllNames().contains(name))){
							selectedAuthor.getOtherFormatNames().add(name);
						}
					}
					debug("author: \n" + duplicateAuthor +  "\n\nreplaced with: \n" + selectedAuthor);
					personDAO.delete(duplicateAuthor.getRecord());
				}
				if(personDAO.update(new Person(null, null, getUserManagedBean()
						.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
						selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false){
					facesMessages.addToControlFromResourceBundle(
							"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
							"records.author.replace.error", FacesContext
									.getCurrentInstance());
				} else {
					facesMessages.addToControlFromResourceBundle(
							"authorPickForm:general", FacesMessage.SEVERITY_INFO, 
							"records.author.replace.success", FacesContext
									.getCurrentInstance());
//					sendRecordMessage(selectedAuthor);
					selectedAuthor = null;
					populateList = true;
					orderList = true;
					duplicateAuthors = new ArrayList<AuthorDTO>();
				}
			} else {
				facesMessages.addToControlFromResourceBundle(
						"authorPickForm:general", FacesMessage.SEVERITY_ERROR, 
						"records.author.replace.error", FacesContext
								.getCurrentInstance());
			}
		} catch (Exception e) {
			error("replaceDuplicateAuthors", e);
		}
	}

	/**
	 * @return the duplicateAuthors
	 */
	public String getDuplicateAuthorsAsString() {
		StringBuffer retVal = new StringBuffer();
		for (AuthorDTO duplicateAuthor : duplicateAuthors) {
			retVal.append(duplicateAuthor + "\n");
		}
		return retVal.toString();
	}

	/**
	 * Sets chosen author to the CRUDManagedBean which wanted to pick author
	 */
	public void chooseAuthor() {
		try {
			if(tableTabNumber != 0) 
				selectedAuthor = findAuthorByControlNumber(list);
			else if(selectedAuthor == null){
				Thread.sleep(500);
				if(selectedAuthor == null){
					String controlNumber = (authorControlNumber!=null)?authorControlNumber:"nema";
					selectedAuthor = (AuthorDTO)recordDAO.getDTO(controlNumber);
				}
			}
			tableTabNumber = 0;
			if ((selectedAuthor != null) && (((selectedAuthor.getInstitution()==null) || (selectedAuthor.getInstitution().getSomeName() == null)) && (pleaseInstitution))){
				setEditMode(ModesManagedBean.MODE_UPDATE);
				setTableMode(ModesManagedBean.MODE_NONE);
				editTabNumber = 0;
			} 
//			else if ((selectedAuthor != null) && (((selectedAuthor.getName().getOtherName()==null) || (selectedAuthor.getName().getOtherName().trim().equals(""))) && (otherName != null))){
//				setEditMode(ModesManagedBean.MODE_UPDATE);
//				setTableMode(ModesManagedBean.MODE_NONE);
//				editTabNumber = 0;
//				selectedAuthor.getName().setOtherName(otherName);
//			} 
			else if(selectedAuthor != null){
				setTableMode(ModesManagedBean.MODE_NONE);
				iPickAuthorManagedBean.setAuthor(selectedAuthor);
				pleaseInstitution = false;
			} 
			
		} catch (Exception e) {
			error("chooseAuthor", e);
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#chooseSimilar()
	 */
	@Override
	public void chooseSimilar() {
		try {
			selectedAuthor = findAuthorByControlNumber(similarAuthors);
			if (selectedAuthor != null)
				iPickAuthorManagedBean.setAuthor(selectedAuthor);
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilar", e);
		}
	}
	
	public void chooseSimilar(AuthorDTO author) {
		try {
			selectedAuthor = author;
			if (selectedAuthor != null)
				iPickAuthorManagedBean.setAuthor(selectedAuthor);
			tableTabNumber = 0;
			setTableMode(ModesManagedBean.MODE_NONE);
			editTabNumber = 0;
			setEditMode(ModesManagedBean.MODE_NONE);
		} catch (Exception e) {
			error("chooseSimilar", e);
		}
	}

	private IPickAuthorManagedBean iPickAuthorManagedBean = null;

	/**
	 * @param iPickAuthorManagedBean
	 *            the CRUDManagedBean which wants to pick author
	 */
	public void setIPickAuthorManagedBean(
			IPickAuthorManagedBean iPickAuthorManagedBean) {
		this.iPickAuthorManagedBean = iPickAuthorManagedBean;
	}

	private String pickMessage;

	/**
	 * @return the pick message 
	 */
	public String getPickMessage() {
		return facesMessages.getMessageFromResourceBundle(pickMessage);
	}

	/**
	 * @param pickMessage
	 *            the pick message to set
	 */
	public void setPickMessage(String pickMessage) {
		this.pickMessage = pickMessage;
	}

	
	private String pickMessageFirstTab;

	/**
	 * @return the pick message for first tab
	 */
	public String getPickMessageFirstTab() {
		return facesMessages.getMessageFromResourceBundle(pickMessageFirstTab);
	}

	/**
	 * @param pickMessageFirstTab
	 *            the pick message for first tab to set
	 */
	public void setPickMessageFirstTab(String pickMessageFirstTab) {
		this.pickMessageFirstTab = pickMessageFirstTab;
	}
	
	private String pickMessageSecondTabSimilarNotExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistFirstSentence
	 *            the pick message for first tab if SIMILAR authors do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistFirstSentence(String pickMessageSecondTabSimilarNotExistFirstSentence) {
		this.pickMessageSecondTabSimilarNotExistFirstSentence = pickMessageSecondTabSimilarNotExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarNotExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors do not exist
	 */
	public String getPickMessageSecondTabSimilarNotExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarNotExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarNotExistSecondSentence
	 *            the pick message for first tab if SIMILAR authors do not exist to set
	 */
	public void setPickMessageSecondTabSimilarNotExistSecondSentence(String pickMessageSecondTabSimilarNotExistSecondSentence) {
		this.pickMessageSecondTabSimilarNotExistSecondSentence = pickMessageSecondTabSimilarNotExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFirstSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors  exist
	 */
	public String getPickMessageSecondTabSimilarExistFirstSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFirstSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFirstSentence
	 *            the pick message for first tab if SIMILAR authors exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFirstSentence(String pickMessageSecondTabSimilarExistFirstSentence) {
		this.pickMessageSecondTabSimilarExistFirstSentence = pickMessageSecondTabSimilarExistFirstSentence;
	}
	
	private String pickMessageSecondTabSimilarExistSecondSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors exist
	 */
	public String getPickMessageSecondTabSimilarExistSecondSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistSecondSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistSecondSentence
	 *            the pick message for first tab if SIMILAR authors exist to set
	 */
	public void setPickMessageSecondTabSimilarExistSecondSentence(String pickMessageSecondTabSimilarExistSecondSentence) {
		this.pickMessageSecondTabSimilarExistSecondSentence = pickMessageSecondTabSimilarExistSecondSentence;
	}
	
	private String pickMessageSecondTabSimilarExistThirdSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors exist
	 */
	public String getPickMessageSecondTabSimilarExistThirdSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistThirdSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistThirdSentence
	 *            the pick message for first tab if SIMILAR authors exist to set
	 */
	public void setPickMessageSecondTabSimilarExistThirdSentence(String pickMessageSecondTabSimilarExistThirdSentence) {
		this.pickMessageSecondTabSimilarExistThirdSentence = pickMessageSecondTabSimilarExistThirdSentence;
	}
	
	private String pickMessageSecondTabSimilarExistFourthSentence;

	/**
	 * @return the pick message for second tab if SIMILAR authors exist
	 */
	public String getPickMessageSecondTabSimilarExistFourthSentence() {
		return facesMessages.getMessageFromResourceBundle(pickMessageSecondTabSimilarExistFourthSentence);
	}

	/**
	 * @param pickMessageSecondTabSimilarExistFourthSentence
	 *            the pick message for first tab if SIMILAR authors exist to set
	 */
	public void setPickMessageSecondTabSimilarExistFourthSentence(String pickMessageSecondTabSimilarExistFourthSentence) {
		this.pickMessageSecondTabSimilarExistFourthSentence = pickMessageSecondTabSimilarExistFourthSentence;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#dispetcher(int)
	 */
	@Override
	protected String dispetcher(int condition) {
		String retVal = null;
		retVal = "indexPage";
		return retVal;
	}

	private String firstname;

	private String lastname;
	
	private String otherName;
	
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * @return the otherName
	 */
	public String getOtherName() {
		return otherName;
	}

	/**
	 * @param otherName the otherName to set
	 */
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	private String firstnameOtherFormat;

	private String lastnameOtherFormat;

	/**
	 * @return the first name other format
	 */
	public String getFirstnameOtherFormat() {
		return firstnameOtherFormat;
	}

	/**
	 * @param firstnameOtherFormat
	 *            the first name other format to set
	 */
	public void setFirstnameOtherFormat(String firstnameOtherFormat) {
		this.firstnameOtherFormat = firstnameOtherFormat;
	}

	/**
	 * @return the last name other format
	 */
	public String getLastnameOtherFormat() {
		return lastnameOtherFormat;
	}

	/**
	 * @param lastnameOtherFormat
	 *            the last name other format to set
	 */
	public void setLastnameOtherFormat(String lastnameOtherFormat) {
		this.lastnameOtherFormat = lastnameOtherFormat;
	}

	/**
	 * Adds new format of personal name
	 */
	public void addName() {
		
		if ((firstnameOtherFormat != null) && (!("".equals(firstnameOtherFormat)))
				&& (lastnameOtherFormat != null) && (!("".equals(lastnameOtherFormat)))) {
			
			if(! (selectedAuthor.getAllNames().contains(new AuthorNameDTO(firstnameOtherFormat,
							lastnameOtherFormat, "")))){
				selectedAuthor.getOtherFormatNames().add(
						new AuthorNameDTO(firstnameOtherFormat,
								lastnameOtherFormat, ""));
				firstnameOtherFormat = "";
				lastnameOtherFormat = "";
				if (personDAO.update(new Person(null, null, getUserManagedBean()
						.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
						selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false) {
					facesMessages.addToControlFromResourceBundle(
							"authorNameFormatsForm:general", FacesMessage.SEVERITY_ERROR, "records.author.addName.error",
							FacesContext.getCurrentInstance());
				} else {
					init = true;
					nextEditTab();
					facesMessages.addToControlFromResourceBundle(
							"authorNameFormatsForm:general", FacesMessage.SEVERITY_INFO,  "records.author.addName.success",
							FacesContext.getCurrentInstance());
					populateList = true;
					orderList = true;
					if(editMode != ModesManagedBean.MODE_IMPORT)
						sendRecordMessage(selectedAuthor, "update");
				}
			}
		}
	}
	
	/**
	 * Adds the new format of personal name for author connected to selected
	 * user
	 */
	public void addAuthorName() {
		if ((firstnameOtherFormat != null) && (!("".equals(firstnameOtherFormat)))
				&& (lastnameOtherFormat != null) && (!("".equals(lastnameOtherFormat)))) {
			if (("".equals(selectedAuthor.getName().getFirstname())))
				selectedAuthor.setName(
						new AuthorNameDTO(firstnameOtherFormat,
								lastnameOtherFormat, ""));
			else
				selectedAuthor.getOtherFormatNames().add(
						new AuthorNameDTO(firstnameOtherFormat,
								lastnameOtherFormat, ""));
			firstnameOtherFormat = "";
			lastnameOtherFormat = "";
		} else {
			facesMessages.addToControlFromResourceBundle(
					"authorEditForm:general", FacesMessage.SEVERITY_ERROR, 
					"records.author.addName.error", FacesContext
							.getCurrentInstance());
		}
		populateImportMessages();

	}
	
	/**
	 * Removes the existing format of personal name for author connected to
	 * selected user
	 */
	public void removeAuthorName() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedAuthor.getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedAuthor.getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		if (index != -1)
			selectedAuthor.getOtherFormatNames().remove(index);
		
		if(editMode == ModesManagedBean.MODE_ADD_FORMAT_NAME) {
			if (personDAO.update(new Person(null, null, getUserManagedBean()
					.getLoggedUser().getEmail(), new GregorianCalendar(), new Integer(0), CerifEntitiesNames.PERSON, 
					selectedAuthor, selectedAuthor.getJmbg(), selectedAuthor.getDirectPhones(), selectedAuthor.getLocalPhones(), selectedAuthor.getApvnt())) == false) {
				facesMessages.addToControlFromResourceBundle(
						"authorNameFormatsForm:general", FacesMessage.SEVERITY_ERROR, "records.author.removeName.error",
						FacesContext.getCurrentInstance());
			} else {
				init = true;
				nextEditTab();
				facesMessages.addToControlFromResourceBundle(
						"authorNameFormatsForm:general", FacesMessage.SEVERITY_INFO,  "records.author.removeName.success",
						FacesContext.getCurrentInstance());
				populateList = true;
				orderList = true;
				if(editMode != ModesManagedBean.MODE_IMPORT)
					sendRecordMessage(selectedAuthor, "update");
			}
		}
	}

	/**
	 * Switches the selected format of personal name with previous
	 */
	public void moveAuthorNameUp() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedAuthor.getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedAuthor.getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		switch (index) {
			case -1:
				switchNames(index, selectedAuthor
						.getOtherFormatNames().size() - 1);
				break;
			default:
				switchNames(index, index - 1);
				break;
		}

	}

	/**
	 * Switches the selected format of personal name with next
	 */
	public void moveAuthorNameDown() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String firstname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("firstname");
		String lastname = facesCtx.getExternalContext()
				.getRequestParameterMap().get("lastname");

		int index = -1;
		AuthorNameDTO authorNameDTO = null;
		for (int i = 0; i < selectedAuthor.getOtherFormatNames()
				.size(); i++) {
			authorNameDTO = selectedAuthor.getOtherFormatNames().get(
					i);
			if ((authorNameDTO.getFirstname().equals(firstname))
					&& (authorNameDTO.getLastname().equals(lastname))) {
				index = i;
				break;
			}
		}
		switch (index) {
			default:
				switchNames(index, ((index + 1) == selectedAuthor
						.getOtherFormatNames().size()) ? (-1) : (index + 1));
				break;
		}

	}

	private void switchNames(int firstIndex, int secondIndex) {
		AuthorNameDTO first = (firstIndex == -1) ? selectedAuthor
				.getName() : selectedAuthor.getOtherFormatNames()
				.get(firstIndex);
		AuthorNameDTO second = (secondIndex == -1) ? selectedAuthor
				.getName() : selectedAuthor.getOtherFormatNames()
				.get(secondIndex);
		if ((firstIndex == -1) || (secondIndex == -1)) {
			if (firstIndex == -1)
				selectedAuthor.setName(second);
			else
				selectedAuthor.setName(first);
		} else {
			selectedAuthor.getOtherFormatNames().set(firstIndex,
					second);
			selectedAuthor.getOtherFormatNames().set(secondIndex,
					first);
		}
	}

	private String authorControlNumber;
	
	public void setAuthorControlNumber(String controlNumber){
		authorControlNumber=controlNumber;
		selectedAuthor = (AuthorDTO)recordDAO.getDTO(controlNumber);
	}
	
	public String getAuthorControlNumber(){
		return authorControlNumber;
	}
	
	private AuthorDTO findAuthorByControlNumber(List<AuthorDTO> authorsList) {
		AuthorDTO retVal = null;
		try {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			String controlNumber = (authorControlNumber!=null)?authorControlNumber:facesCtx.getExternalContext().getRequestParameterMap().get("controlNumber");
			for (AuthorDTO dto : authorsList) {
				if ((dto.getControlNumber() != null)
						&& (dto.getControlNumber()
								.equalsIgnoreCase(controlNumber))) {
					retVal = dto;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#switchToTableNoneMode()
	 */
	@Override
	public void switchToTableNoneMode() {
		if (tableMode == ModesManagedBean.MODE_PICK) {
			iPickAuthorManagedBean.cancelPickingAuthor();
			pleaseInstitution = false;
		}
		super.switchToTableNoneMode();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#finishWizard()
	 */
	@Override
	public String finishWizard() {
		if ((editMode == ModesManagedBean.MODE_IMPORT) || ((editMode == ModesManagedBean.MODE_ADD) && (customPick)) || ((editMode == ModesManagedBean.MODE_UPDATE) && (tableMode == ModesManagedBean.MODE_NONE)) || (editMode == ModesManagedBean.MODE_ADD_FORMAT_NAME)) {
			if(iPickAuthorManagedBean!=null){
				iPickAuthorManagedBean.setAuthor(selectedAuthor);
				setTableMode(ModesManagedBean.MODE_NONE);
			}
			pleaseInstitution = false;
			customPick = false;
			setEditMode(ModesManagedBean.MODE_NONE);
		} else {
			super.switchToEditNoneMode();
		}
		return null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#setCustomPick(boolean)
	 */
	@Override
	public void setCustomPick(boolean customPick) {
		super.setCustomPick(customPick);
		if (!customPick) {
			orderBy = "name.lastname";
			direction = "asc";
		}
	}
	

	protected boolean pleaseInstitution = false;

	/**
	 * @return the pleaseInstitution
	 */
	public boolean isPleaseInstitution() {
		return pleaseInstitution;
	}

	/**
	 * @param pleaseInstitution the pleaseInstitution to set
	 */
	public void setPleaseInstitution(boolean pleaseInstitution) {
		this.pleaseInstitution = pleaseInstitution;
	}
	
	private String pleaseInstitutionMessage;

	/**
	 * @return the pleaseInstitutionMessage
	 */
	public String getPleaseInstitutionMessage() {
		return facesMessages.getMessageFromResourceBundle(pleaseInstitutionMessage);
	}

	/**
	 * @param pleaseInstitutionMessage the pleaseInstitutionMessage to set
	 */
	public void setPleaseInstitutionMessage(String pleaseInstitutionMessage) {
		this.pleaseInstitutionMessage = pleaseInstitutionMessage;
	}
	
	private AuthorPositionManagedBean authorPositionManagedBean = null;
	
	/**
	 * @return the AuthorPositionManagedBean from current session
	 */
	private AuthorPositionManagedBean getAuthorPositionManagedBean() {
		if (authorPositionManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			AuthorPositionManagedBean mb = (AuthorPositionManagedBean) extCtx.getSessionMap().get(
					"authorPositionManagedBean");
			if (mb == null) {
				mb = new AuthorPositionManagedBean();
				extCtx.getSessionMap().put("authorPositionManagedBean", mb);
			}
			authorPositionManagedBean = mb;
		}
		return authorPositionManagedBean;
	}
	
	/**
	 * @param mode
	 * @param positions
	 */
	private void openAuthorPositionForm(int mode, List<AuthorPosition> positions){
		AuthorPositionManagedBean mb = getAuthorPositionManagedBean();
		mb.setPosition(null);
		mb.setStartDate(null);
		mb.setEndDate(null);
		mb.setPositions(positions);
		mb.setEditMode(mode);
	}
	
	public void authorPositions(){
		int mode = editMode;
		if(mode == ModesManagedBean.MODE_IMPORT)
			mode = ModesManagedBean.MODE_UPDATE;
		this.openAuthorPositionForm(mode, selectedAuthor.getFormerPositions());
	}
	
	/**
	 * Sets all necessary things for InstitutionManageBean in pick mode
	 */
	public void setInstitutionManageBeanToPick() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
				"institutionManagedBean");
		if (mb == null) {
			mb = new InstitutionManagedBean();
			extCtx.getSessionMap().put("institutionManagedBean", mb);
		}

		mb.setIPickInstitutionManagedBean(this);
		mb.setSelectedInstitution(null);
		mb.setCustomPick(true);
		mb.switchToPickMode();
	}

	public InstitutionDTO getSelectedOrganizationUnit() {
		return selectedOrganizationUnit;
	}

	public void setSelectedOrganizationUnit(InstitutionDTO selectedOrganizationUnit) {
		this.selectedOrganizationUnit = selectedOrganizationUnit;
	}

	public void setSelectedOrganizationUnit(String controlNumber) {
		this.selectedOrganizationUnit = (InstitutionDTO) recordDAO.getDTO(controlNumber);
	}

}
