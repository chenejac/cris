/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.util.HashMap;

/**
 * Managed bean for getting string constants of privileges, forms, user groups. This class is also used for getting privileges of logged user. 
 * 
 * @author chenejac@uns.ac.rs
 */
public class PrivilegesManagedBean {

	static String FORM_USER = "userForm";
	static String FORM_AUTHOR = "authorForm";
	static String FORM_CONFERENCE = "conferenceForm";
	static String FORM_INSTITUTION = "institutionForm";
	static String FORM_ORGANIZATION_UNIT = "organizationUnitForm";
	static String FORM_RESEARCH_AREA = "researchAreaForm";
	static String FORM_POSITION = "positionForm";
	static String FORM_PROCEEDINGS = "proceedingsForm";
	static String FORM_PAPER_PROCEEDINGS = "paperProceedingsForm";
	static String FORM_JOURNAL = "journalForm";
	static String FORM_PAPER_JOURNAL = "paperJournalForm";
	static String FORM_MONOGRAPH = "monographForm";
	static String FORM_PAPER_MONOGRAPH = "paperMonographForm";
	static String FORM_STUDY_FINAL_DOCUMENT = "studyFinalDocumentForm";
	static String FORM_PATENT = "patentForm";
	static String FORM_PRODUCT = "productForm";
	static String FORM_PUBLICATION = "publicationForm";
	static String FORM_BIBLIOGRAPHY = "bibliographyForm";
	static String FORM_EVALUATION = "evaluationForm";
	static String FORM_EVALUATION_JOURNAL = "evaluationJournalForm";
	static String FORM_EVALUATION_CONFERENCE = "evaluationConferenceForm";
	static String FORM_IMPORT = "importForm";
	static String FORM_REGISTER_ENTRY = "registerEntryForm";
	static String FORM_PROMOTION = "promotionForm";
	static String FORM_REPORT_SE = "reportSEForm";
	static String FORM_REPORT_PHD = "reportPHDForm";
	static String FORM_JOB_AD = "jobAdForm";
	static String FORM_RULEBOOK = "ruleBookForm"; 
	static String FORM_RULEBOOK_IMPLEMENTATION = "ruleBookImplementationForm";
	static String FORM_RESULTS_TYPE_GROUP = "resultsTypeGroupForm";
	static String FORM_RESULTS_TYPE = "resultsTypeForm";
	static String FORM_RESULTS_MEASURE = "resultsMeasureForm";
	static String FORM_SCIENCES_GROUP = "sciencesGroupForm";
	static String FORM_SPECIALLY_VERIFIED_LIST = "speciallyVerifiedListForm";
	
	static String PRIVILEGE_BROWSE = "canBrowse";
	static String PRIVILEGE_UPDATE = "canUpdate";
	static String PRIVILEGE_ADD = "canAdd";
	static String PRIVILEGE_DELETE = "canDelete";
	static String PRIVILEGE_SHOW_IN_MAIN_MENU = "viewInMainMenu";
	static String PRIVILEGE_ADD_NAME_FORMAT = "canAddNameFormat";
	static String PRIVILEGE_DELETE_NAME_FORMAT = "canDeleteNameFormat";
	static String PRIVILEGE_EDIT_FULL_DATA = "canEditFullData";
	static String PRIVILEGE_ARCHIVE_1 = "canArchiveFirstLevel";
	static String PRIVILEGE_ARCHIVE_2 = "canArchiveSecondLevel";
	static String PRIVILEGE_EDIT_ARCHIVED_DATA = "canEditArchivedData";
	static String PRIVILEGE_DOWNLOAD = "canDownload";
	
	static String GROUP_VISITOR = "visitor";
	static String GROUP_AUTHOR = "author";
	static String GROUP_APPLICANT = "applicant";
	static String GROUP_ADMINISTRATOR = "administrator";
	static String GROUP_LIBRARIAN = "librarian";
	static String GROUP_INSTITUTION = "institution";
	static String GROUP_REGISTER_ENTRY_EDITOR = "registerEntryEditor";
	static String GROUP_THESES_ADMINISTRATOR = "thesesAdministrator";
	static String COMMISSION = "commission";
	static String GROUP_INSTITUTION_MANAGER = "institutionManager";
	
	
	private static HashMap<String, HashMap<String, HashMap<String, Boolean>>> privileges = new HashMap<String, HashMap<String, HashMap<String, Boolean>>>();

	static {

		
		//visitor
		HashMap<String, HashMap<String, Boolean>> group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		HashMap<String, Boolean> form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, false);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);
		
		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);

		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		
		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		
		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);
		
		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_VISITOR, group);

		//author
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, false);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);
		
		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		

		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);
		
		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_AUTHOR, group);
		privileges.put(PrivilegesManagedBean.GROUP_APPLICANT, group);

		//administrator
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, true);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);

		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_ADMINISTRATOR, group);
		
		//librarian
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, true);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);

		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
				
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_LIBRARIAN, group);
		
		//theses administrator
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, true);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);

		privileges.put(PrivilegesManagedBean.GROUP_THESES_ADMINISTRATOR, group);
		
		//institution
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, true);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);

		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);
		
		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_INSTITUTION, group);
		
		//registerEntryEditor
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, false);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);

		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);
		
		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);

		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_REGISTER_ENTRY_EDITOR, group);
		
		//commission
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, false);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);

		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);
		
		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.COMMISSION, group);
		
		//institution manager
		group = new HashMap<String, HashMap<String, Boolean>>();

		//userForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_USER, form);

		//authorForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT, false);
		group.put(PrivilegesManagedBean.FORM_AUTHOR, form);

		//conferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_CONFERENCE, form);

		//institutionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_INSTITUTION, form);
		
		//organizationUnitForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_ORGANIZATION_UNIT, form);
		
		//researchAreaForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESEARCH_AREA, form);
		
		//positionForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_POSITION, form);
		
		//proceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROCEEDINGS, form);

		//paperProceedingsForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS, form);
		
		//journalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOURNAL, form);

		//paperJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_JOURNAL, form);
		
		//monographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_MONOGRAPH, form);

		//paperMonographForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PAPER_MONOGRAPH, form);
		
		//studyFinalDocumentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DOWNLOAD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT, form);
		
		//registerEntry
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REGISTER_ENTRY, form);
		
		//promotion
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PROMOTION, form);
		
		//report SE
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, true);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, true);
		group.put(PrivilegesManagedBean.FORM_REPORT_SE, form);

		//report PHD
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_REPORT_PHD, form);
		
		//patentForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PATENT, form);
		
		//productForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PRODUCT, form);
		
		//publicationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_PUBLICATION, form);
		
		//bibliographyForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_BIBLIOGRAPHY, form);
		
		//evaluationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION, form);
		
		//evaluationJournalForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_JOURNAL, form);
		
		//evaluationConferenceForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE, form);
		
		//importForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_IMPORT, form);

		//jobAd
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_JOB_AD, form);
		
		//ruleBookForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK, form);
		
		//ruleBookImplementationForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION, form);
		
		//resultsTypeGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP, form);
		
		//resultsTypeForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_TYPE, form);

		//resultsMeasureForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_RESULTS_MEASURE, form);
		
		//sciencesGroupForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SCIENCES_GROUP, form);
		
		//speciallyVerifiedListForm
		form = new HashMap<String, Boolean>();
		form.put(PrivilegesManagedBean.PRIVILEGE_BROWSE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_UPDATE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ADD, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_DELETE, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA, false);
		form.put(PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU, false);
		group.put(PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST, form);
		
		privileges.put(PrivilegesManagedBean.GROUP_INSTITUTION_MANAGER, group);
	}

	public static HashMap<String, HashMap<String, HashMap<String, Boolean>>> getPrivileges() {
		return privileges;
	}
	
	public String getFormUser(){
		return PrivilegesManagedBean.FORM_USER;
	}
	
	public String getFormAuthor(){
		return PrivilegesManagedBean.FORM_AUTHOR;
	}
	
	public String getFormConference(){
		return PrivilegesManagedBean.FORM_CONFERENCE;
	}
	
	public String getFormInstitution(){
		return PrivilegesManagedBean.FORM_INSTITUTION;
	}
	
	public String getFormOrganizationUnit(){
		return PrivilegesManagedBean.FORM_ORGANIZATION_UNIT;
	}
	
	public String getFormResearchArea(){
		return PrivilegesManagedBean.FORM_RESEARCH_AREA;
	}
	
	public String getFormPosition(){
		return PrivilegesManagedBean.FORM_POSITION;
	}
	
	public String getFormProceedings(){
		return PrivilegesManagedBean.FORM_PROCEEDINGS;
	}
	
	public String getFormPaperProceedings(){
		return PrivilegesManagedBean.FORM_PAPER_PROCEEDINGS;
	}
	
	public String getFormJournal(){
		return PrivilegesManagedBean.FORM_JOURNAL;
	}
	
	public String getFormPaperJournal(){
		return PrivilegesManagedBean.FORM_PAPER_JOURNAL;
	}
	
	public String getFormMonograph(){
		return PrivilegesManagedBean.FORM_MONOGRAPH;
	}
	
	public String getFormPaperMonograph(){
		return PrivilegesManagedBean.FORM_PAPER_MONOGRAPH;
	}
	
	public String getFormStudyFinalDocument(){
		return PrivilegesManagedBean.FORM_STUDY_FINAL_DOCUMENT;
	}
	
	public String getFormRegisterEntry(){
		return PrivilegesManagedBean.FORM_REGISTER_ENTRY;
	}
	
	public String getFormPromotion(){
		return PrivilegesManagedBean.FORM_PROMOTION;
	}
	
	public String getFormReportSE(){
		return PrivilegesManagedBean.FORM_REPORT_SE;
	}

	public String getFormReportPHD(){
		return PrivilegesManagedBean.FORM_REPORT_PHD;
	}
	
	public String getFormPatent(){
		return PrivilegesManagedBean.FORM_PATENT;
	}
	
	public String getFormProduct(){
		return PrivilegesManagedBean.FORM_PRODUCT;
	}
	
	public String getFormPublication(){
		return PrivilegesManagedBean.FORM_PUBLICATION;
	}
	
	public String getFormBibliography(){
		return PrivilegesManagedBean.FORM_BIBLIOGRAPHY;
	}
	
	public String getFormEvaluation(){
		return PrivilegesManagedBean.FORM_EVALUATION;
	}
	
	public String getFormEvaluationJournal(){
		return PrivilegesManagedBean.FORM_EVALUATION_JOURNAL;
	}
	
	public String getFormEvaluationConference(){
		return PrivilegesManagedBean.FORM_EVALUATION_CONFERENCE;
	}
	
	public String getFormImport(){
		return PrivilegesManagedBean.FORM_IMPORT;
	}
	
	public String getFormJobAd(){
		return PrivilegesManagedBean.FORM_JOB_AD;
	}
	
	public String getFormRuleBook(){
		return PrivilegesManagedBean.FORM_RULEBOOK;
	}
	
	public String getFormRuleBookImplementation(){
		return PrivilegesManagedBean.FORM_RULEBOOK_IMPLEMENTATION;
	}
	
	public String getFormResultsTypeGroup(){
		return PrivilegesManagedBean.FORM_RESULTS_TYPE_GROUP;
	}
	
	public String getFormResultsType(){
		return PrivilegesManagedBean.FORM_RESULTS_TYPE;
	}
	
	public String getFormResultsMeasure(){
		return PrivilegesManagedBean.FORM_RESULTS_MEASURE;
	}
	
	public String getFormSciencesGroup(){
		return PrivilegesManagedBean.FORM_SCIENCES_GROUP;
	}
	
	public String getFormSpeciallyVerifiedList(){
		return PrivilegesManagedBean.FORM_SPECIALLY_VERIFIED_LIST;
	}
	
	public String getBrowse(){
		return PrivilegesManagedBean.PRIVILEGE_BROWSE;
	}
	
	public String getUpdate(){
		return PrivilegesManagedBean.PRIVILEGE_UPDATE;
	}
	
	public String getAdd(){
		return PrivilegesManagedBean.PRIVILEGE_ADD;
	}
	
	public String getDelete(){
		return PrivilegesManagedBean.PRIVILEGE_DELETE;
	}
	
	public String getEditFullData(){
		return PrivilegesManagedBean.PRIVILEGE_EDIT_FULL_DATA;
	}
	
	public String getDownload(){
		return PrivilegesManagedBean.PRIVILEGE_DOWNLOAD;
	}
	
	public String getEditArchivedData(){
		return PrivilegesManagedBean.PRIVILEGE_EDIT_ARCHIVED_DATA;
	}
	
	public String getArchiveFirstLevel(){
		return PrivilegesManagedBean.PRIVILEGE_ARCHIVE_1;
	}
	
	public String getArchiveSecondLevel(){
		return PrivilegesManagedBean.PRIVILEGE_ARCHIVE_2;
	}
	
	public String getAddNameFormat(){
		return PrivilegesManagedBean.PRIVILEGE_ADD_NAME_FORMAT;
	}
	
	public String getDeleteNameFormat(){
		return PrivilegesManagedBean.PRIVILEGE_DELETE_NAME_FORMAT;
	}
	
	public String getShowInMainMenu(){
		return PrivilegesManagedBean.PRIVILEGE_SHOW_IN_MAIN_MENU;
	}

}
