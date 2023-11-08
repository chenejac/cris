/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.*;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author Dragan Ivanovic
 *
 */
@SuppressWarnings("serial")
public class ImportManagedBean extends CRUDManagedBean implements Serializable,
	IPickInstitutionManagedBean, IPickOrganizationUnitManagedBean, 
	IPickAuthorManagedBean, IPickJournalManagedBean, IPickProceedingsManagedBean, IPickConferenceManagedBean, IPickStudyFinalDocumentManagedBean, IPickPaperJournalManagedBean, IPickPaperProceedingsManagedBean, IPickMonographManagedBean{

	private ImportRecordsDTO records;
	
	private GroupSerializer groupSerializer;
	
	private RecordDTO selectedRecord;
	
	private RecordDTO nextPublication;
	
	private List<RecordDTO> nextPublicationList;
	
	private String selectedRecordIdentifier;
	
	private String serializedDataPath;

	private String scopusdatapath = "/opt/cris/import/scopus.xlsx";
	
	private Integer counter;
	
	private Integer saveStep = 20;
	
	private String scopusID = "Insert Scopus (Author) ID here";
	
	private Integer startYear = 2020;
	
	private Integer endYear = 2020;
	
	private Integer publicationYear = 2020;
	
	private String bibtex = "Copy Bibtex record here";
	
	protected List<SelectItem> dateRange = null;
	
	private List<RecordDTO> skipRecords = new ArrayList<RecordDTO>();
	
	private RecordDAO personDAO = new RecordDAO(new PersonDB());
	
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	
	/**
	 * 
	 */
	public ImportManagedBean() {
		super();
		tableModalPanel = "importBrowseModalPanel";
		editModalPanel = "importWizardModalPanel";
		
		if (dateRange==null)
		{	
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			dateRange = new ArrayList() ;
			/*for (int i = 1960,j=0; i<=currentYear;i++,j++)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}*/
			
			// idemo od tekuce godine do 2018
			for (int i = currentYear; i>=2018;i--)
			{
				dateRange.add(new SelectItem(String.valueOf(i)));
			}

			startYear = currentYear;
			endYear = currentYear;
			publicationYear = currentYear;
		}
	}
	
	/**
	 * @return the dateRange
	 */
	public List<SelectItem> getDateRange() {
		return dateRange;
	}
	
	@Override
	public void setStudyFinalDocument(StudyFinalDocumentDTO studyFinalDocument) {
		records.addImportedRecord(selectedRecordIdentifier, studyFinalDocument);
		studyFinalDocument.getAuthor().clear();
		studyFinalDocument.getAuthor().setNotLoaded(true);
		for (AuthorDTO advisor : studyFinalDocument.getAdvisors()) {
			advisor.clear();
			advisor.setNotLoaded(true);
		}
		for (AuthorDTO committeeMember : studyFinalDocument.getCommitteeMembers()) {
			committeeMember.clear();
			committeeMember.setNotLoaded(true);
		}
		studyFinalDocument.clear();
		studyFinalDocument.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingStudyFinalDocument() {
	}

	@Override
	public void setAuthor(AuthorDTO author) {
		records.addImportedRecord(selectedRecordIdentifier, author);
		author.clear();
		author.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingAuthor() {
		
	}

	@Override
	public void setOrganizationUnit(OrganizationUnitDTO organizationUnit) {
		records.addImportedRecord(selectedRecordIdentifier, organizationUnit);
		organizationUnit.clear();
		organizationUnit.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingOrganizationUnit() {
		
	}	

	@Override
	public void setInstitution(InstitutionDTO institution) {
		records.addImportedRecord(selectedRecordIdentifier, institution);
		institution.clear();
		institution.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingInstitution() {
		
	}
	
	@Override
	public void setPaperJournal(PaperJournalDTO paperJournal) {
		records.addImportedRecord(selectedRecordIdentifier, paperJournal);
		paperJournal.getJournal().clear();
		paperJournal.getJournal().setNotLoaded(true);
		for (AuthorDTO author : paperJournal.getAllAuthors()) {
			author.clear();
			author.setNotLoaded(true);
		}
		paperJournal.clear();
		paperJournal.setNotLoaded(true);
		nextEditTab();
	}
	
	@Override
	public void cancelPickingPaperJournal() {
	}
	
	@Override
	public void setPaperProceedings(PaperProceedingsDTO paperProceedings) {
		records.addImportedRecord(selectedRecordIdentifier, paperProceedings);
		paperProceedings.getProceedings().getConference().clear();
		paperProceedings.getProceedings().getConference().setNotLoaded(true);
		paperProceedings.getProceedings().clear();
		paperProceedings.getProceedings().setNotLoaded(true);
		for (AuthorDTO author : paperProceedings.getAllAuthors()) {
			author.clear();
			author.setNotLoaded(true);
		}
		paperProceedings.clear();
		paperProceedings.setNotLoaded(true);
		nextEditTab();
	}
	
	@Override
	public void cancelPickingPaperProceedings() {
	}
	
	@Override
	public void setJournal(JournalDTO journal) {
		records.addImportedRecord(selectedRecordIdentifier, journal);
		journal.clear();
		journal.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingJournal() {
	}
	
	@Override
	public void setProceedings(ProceedingsDTO proceedings) {
		records.addImportedRecord(selectedRecordIdentifier, proceedings);
		proceedings.getConference().clear();
		proceedings.getConference().setNotLoaded(true);
		proceedings.clear();
		proceedings.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingProceedings() {
	}
	
	@Override
	public void setConference(ConferenceDTO conference) {
		records.addImportedRecord(selectedRecordIdentifier, conference);
		conference.clear();
		conference.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingConference() {
	}
	
	@Override
	public void setMonograph(MonographDTO monograph) {
		records.addImportedRecord(selectedRecordIdentifier, monograph);
		monograph.clear();
		monograph.setNotLoaded(true);
		nextEditTab();
	}

	@Override
	public void cancelPickingMonograph() {
	}



	private UserManagedBean userManagedBean = null;

	/**
	 * @return the UserManagedBean from current session
	 */
	public UserManagedBean getUserManagedBean() {
		if (userManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			UserManagedBean mb = (UserManagedBean) extCtx.getSessionMap().get(
					"userManagedBean");
			if (mb == null) {
				mb = new UserManagedBean();
				extCtx.getSessionMap().put("userManagedBean", mb);
			}
			userManagedBean = mb;
		}
		return userManagedBean;
	}
	
	/**
	 * Resets form to the initial state
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String resetForm() {
		tableModalPanel = "importBrowseModalPanel";
		editModalPanel = "importWizardModalPanel";
		return "importPage";
	}
	
	/**
	 * Prepares CRUD page for using
	 * 
	 * @return the outcome string for JSF navigation
	 */
	public String enterCRUDPage() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		return retVal;
	}
	
	public String enterCRUDPageJournals() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		retVal = "paperJournalImportPage";
		if(getUserManagedBean().getLoggedUser() != null){
			if(getUserManagedBean().getLoggedUser().getAuthor() != null)
				this.scopusID = getUserManagedBean().getLoggedUser().getAuthor().getScopusID();
		}
		return retVal;
	}
	
	public String enterCRUDPageProceedings() {
		getUserManagedBean().resetAllForms();
		String retVal = resetForm();
		retVal = "paperProceedingsImportPage";
		if(getUserManagedBean().getLoggedUser() != null){
			if(getUserManagedBean().getLoggedUser().getAuthor() != null)
				this.scopusID = getUserManagedBean().getLoggedUser().getAuthor().getScopusID();
		}
		return retVal;
	}
	
	public void importDiglib(){
		groupSerializer = new DiglibSerializer();
		serializedDataPath = "D:/diglib";
		switchToImportMode();
	}
	
	public void importDMIResearchers(){
		groupSerializer = new AuthorSerializer();
//		((AuthorSerializer)groupSerializer).setXlsPath("D:/zaposleniDMI.xlsx");
		((AuthorSerializer)groupSerializer).setXlsPath("/opt/cris/import/zaposleniDMI.xlsx");
		serializedDataPath = null;
		switchToImportMode();
	}
	
	public void importDFResearchers(){
		groupSerializer = new AuthorSerializer();
//		((AuthorSerializer)groupSerializer).setXlsPath("D:/zaposleniDF.xlsx");
		((AuthorSerializer)groupSerializer).setXlsPath("/opt/cris/import/zaposleniDF.xlsx");
		serializedDataPath = null;
		switchToImportMode();
	}
	
	public void importDHResearchers(){
		groupSerializer = new AuthorSerializer();
//		((AuthorSerializer)groupSerializer).setXlsPath("D:/zaposleniDH.xlsx");
		((AuthorSerializer)groupSerializer).setXlsPath("/opt/cris/import/zaposleniDH.xlsx");
		serializedDataPath = null;
		switchToImportMode();
	}
	
	public void importDBEResearchers(){
		groupSerializer = new AuthorSerializer();
//		((AuthorSerializer)groupSerializer).setXlsPath("D:/zaposleniDBE.xlsx");
		((AuthorSerializer)groupSerializer).setXlsPath("/opt/cris/import/zaposleniDBE.xlsx");
		serializedDataPath = null;
		switchToImportMode();
	}
	
	public void importDGTResearchers(){
		groupSerializer = new AuthorSerializer();
//		((AuthorSerializer)groupSerializer).setXlsPath("D:/zaposleniDGT.xlsx");
		((AuthorSerializer)groupSerializer).setXlsPath("/opt/cris/import/zaposleniDGT.xlsx");
		serializedDataPath = null;
		switchToImportMode();
	}
	
	public void importAndrejevic(){
		groupSerializer = new AndrejevicSerializer();
//		serializedDataPath = "D:/andrejevic";
		serializedDataPath = "/opt/cris/import/andrejevic";
		switchToImportMode();
	}
	
	public void importPMFDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/pmfDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/pmfDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/pmfDisertacije";		
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/pmfDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	public void importFiloDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/filoDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/filoDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/filoDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/filoDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importMediDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/mediDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/mediDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/mediDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/mediDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importTFDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/tfDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/tfDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/tfDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/tfDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importPoljoDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/poljoDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/poljoDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/poljoDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/poljoDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importOstaloDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/ostaloDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/ostaloDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/ostaloDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/ostaloDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importPreskocenoDissertations(){
		groupSerializer = new UNSDissertationsSerializer();
//		serializedDataPath = "D:/ostaloDisertacije";
//		((UNSDissertationsSerializer)groupSerializer).setXlsPath("D:/ostaloDisertacije.xlsx");
		serializedDataPath = "/opt/cris/import/preskocenoDisertacije";
		((UNSDissertationsSerializer)groupSerializer).setXlsPath("/opt/cris/import/preskocenoDisertacije.xlsx");
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importY2013Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2013";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2013";
		((UNSArchiveSerializer)groupSerializer).setYear("2013"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2012Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2012";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2012";
		((UNSArchiveSerializer)groupSerializer).setYear("2012"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2011Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2011";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2011";
		((UNSArchiveSerializer)groupSerializer).setYear("2011"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2010Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2010";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2010";
		((UNSArchiveSerializer)groupSerializer).setYear("2010"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2009Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2009";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2009";
		((UNSArchiveSerializer)groupSerializer).setYear("2009"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2008Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2008";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2008";
		((UNSArchiveSerializer)groupSerializer).setYear("2008"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2007Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2007";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2007";
		((UNSArchiveSerializer)groupSerializer).setYear("2007"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2006Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2006";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2006";
		((UNSArchiveSerializer)groupSerializer).setYear("2006"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importY2005Dissertations(){
		groupSerializer = new UNSArchiveSerializer();
		//serializedDataPath = "D:/archiveDissertations/2005";
		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		switchToImportMode();
	}
	
	public void importScopusPaperJournal(){
		groupSerializer = new ScopusPaperJournalSerializer();
		((ScopusPaperJournalSerializer)groupSerializer).setScopusID(scopusID);
		((ScopusPaperJournalSerializer)groupSerializer).setStartYear(startYear);
		((ScopusPaperJournalSerializer)groupSerializer).setEndYear(endYear);
		//serializedDataPath = "D:/archiveDissertations/2005";
//		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
//		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		//TODO 
		switchToImportMode();
		automaticImport = true;
	}

	public void importScopusPaperJournalXLS(){
		groupSerializer = new ScopusPaperJournalXLSSerializer();
		//serializedDataPath = "D:/archiveDissertations/2006";
		((ScopusPaperJournalXLSSerializer)groupSerializer).setXlsPath(scopusdatapath);
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importScopusPaperProceedings(){
		groupSerializer = new ScopusPaperProceedingsSerializer();
		((ScopusPaperProceedingsSerializer)groupSerializer).setScopusID(scopusID);
		((ScopusPaperProceedingsSerializer)groupSerializer).setStartYear(startYear);
		((ScopusPaperProceedingsSerializer)groupSerializer).setEndYear(endYear);
		//serializedDataPath = "D:/archiveDissertations/2005";
//		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
//		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		//TODO 
		switchToImportMode();
		automaticImport = true;
	}

	public void importScopusPaperProceedingsXLS(){
		groupSerializer = new ScopusPaperProceedingsXLSSerializer();
		//serializedDataPath = "D:/archiveDissertations/2006";
		((ScopusPaperProceedingsXLSSerializer)groupSerializer).setXlsPath(scopusdatapath);
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importBibtexPaperJournal(){
		groupSerializer = new BibtexPaperJournalSerializer();
		((BibtexPaperJournalSerializer)groupSerializer).setBibtexText(bibtex);
		//serializedDataPath = "D:/archiveDissertations/2005";
//		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
//		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		//TODO 
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importBibtexPaperProceedings(){
		//TODO
		groupSerializer = new BibtexPaperProceedingsSerializer();
		((BibtexPaperProceedingsSerializer)groupSerializer).setBibtexText(bibtex);
		//serializedDataPath = "D:/archiveDissertations/2005";
//		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
//		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		//TODO 
		switchToImportMode();
		automaticImport = true;
	}
	
	public void importEndNotePaperJournal(){
		groupSerializer = new ScopusPaperJournalSerializer();
		//serializedDataPath = "D:/archiveDissertations/2005";
//		serializedDataPath = "/opt/cris/import/archiveDissertations/2005";
//		((UNSArchiveSerializer)groupSerializer).setYear("2005"); //TODO Set appropriated year
		//TODO 
		switchToImportMode();
		automaticImport = true;
	}

	/**
	 * Switches edit mode to import mode
	 */
	public void switchToImportMode() {
		counter = new Integer(0);
		populateList();
		super.switchToImportMode();
		nextEditTab();
	}

	@Override
	public void populateList() {
		File file = null;
		if(serializedDataPath != null){
			file = new File(serializedDataPath + "Counter.dat");
		}
		if((file != null) && (file.exists()))
		{
//			if(loadNeeded){
				try
		         {
		            FileInputStream fileIn =
		                          new FileInputStream(serializedDataPath + "Counter.dat");
		            ObjectInputStream in = new ObjectInputStream(fileIn);
		            counter = (Integer)in.readObject();
		            in.close();
		            fileIn.close();
		            fileIn = new FileInputStream(serializedDataPath + counter + ".dat");
		            in = new ObjectInputStream(fileIn);
		            records = (ImportRecordsDTO) in.readObject();
		            records.setAllRecords(records.getAllRecords());
	//	            InputStream inputStream = null;
	//				inputStream = new FileInputStream (((UNSDissertationsSerializer)groupSerializer).getXlsPath());
	//				XSSFWorkbook      workBook = new XSSFWorkbook (inputStream);
	//				XSSFSheet         sheet    = workBook.getSheetAt (0);
	//				Iterator<Row> rows     = sheet.rowIterator();
	//				String cellContent = "";
	//				RecordDAO recordDAO = new RecordDAO(new RecordDB());
	//				while (records.getAllRecordsIterator().hasNext()) {
	//					RecordDTO record = records.getAllRecordsIterator().next();
	//					if(record instanceof StudyFinalDocumentDTO){
	//						StudyFinalDocumentDTO temp = loadDissertations((XSSFRow) rows.next());
	//						StudyFinalDocumentDTO thesis = null;
	//						if((records.getImportedRecords(StudyFinalDocumentDTO.class.getSimpleName()).containsKey(temp.getControlNumber()))){
	//							thesis = (StudyFinalDocumentDTO) records.getImportedRecords(StudyFinalDocumentDTO.class.getSimpleName()).get(temp.getControlNumber());
	//						} else {
	//							thesis = (StudyFinalDocumentDTO) record;
	//						}
	//						
	//						thesis.getPublicationDate();
	//						
	//						thesis.setPromotionDate(temp.getPromotionDate());
	//						thesis.setPublicationDate(temp.getPublicationDate());
	//						thesis.setDefendedOn(temp.getDefendedOn());
	//						thesis.setAcceptedOn(temp.getAcceptedOn());
	//						if(thesis.getControlNumber().startsWith("(BISIS)")){
	//							System.out.println(thesis.isNotLoaded());
	//							String dateString = null;
	//							DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	//							if(temp.getPublicationDate() != null){
	//								Date date = temp.getPublicationDate().getTime();
	//								dateString = formatter.format(date);
	//								System.out.println("Publication date0: " + dateString);
	//							}
	//							if(thesis.getPublicationDate() != null){
	//								Date date = thesis.getPublicationDate().getTime();
	//								dateString = formatter.format(date);
	//								System.out.println("Publication date1: " + dateString);
	//							}
	//							thesis.getRecord().setDto(thesis);
	//							thesis.getRecord().loadMARC21FromDTO();
	//							
	//							if(recordDAO.update(new Record(thesis.getRecord().getCreator(), thesis.getRecord().getCreationDate(), thesis.getRecord().getModifier(), thesis.getRecord().getLastModificationDate(), thesis.getRecord().getArchived(), thesis.getRecord().getType(), thesis)))
	//								System.out.println("Updated: " + thesis.getRecord().toString());
	//							else {
	//								System.out.println("PROBLEM !!!!!!!!!!!!!!!");
	//							}
	//						}
	//						
	//					}
	//				}
	//				records.setAllRecords(records.getAllRecords());
		            
		            ImportRecordsDTO recordsTemp = groupSerializer.importRecords(null);
		            PrintWriter out2 = new PrintWriter(new File(serializedDataPath + "Greske.csv"), "UTF8");
		            RecordDAO recordDAO = new RecordDAO(new RecordDB());
		            RecordDAO recordDAO1 = new RecordDAO(new PersonDB());
		            while (recordsTemp.getAllRecordsIterator().hasNext()){
		            	RecordDTO record = recordsTemp.getAllRecordsIterator().next();
		            	RecordDTO recordTemp = null;
		            	String cn1 = record.getControlNumber();
		            	String cn2 = "";
		            	String cn3 = "";
		            	
		            	if(record instanceof StudyFinalDocumentDTO){
		            		StudyFinalDocumentDTO sfd = (StudyFinalDocumentDTO) record;
		            		if(sfd.getRegisterEntry().getPromotionDate() != null){
		            			cn2 = sfd.getRegisterEntry().getPromotionDate().get(Calendar.YEAR) + sfd.getTitle().getContent();
		            		}
		            		cn3 = null + sfd.getTitle().getContent();
		            			
		            	}
		            	
		            	try {
							if((records.getImportedRecords(record.getClass().getSimpleName()).containsKey(cn1)) || (records.getImportedRecords(record.getClass().getSimpleName()).containsKey(cn2)) || (records.getImportedRecords(record.getClass().getSimpleName()).containsKey(cn3))){
								recordTemp = records.getImportedRecords(record.getClass().getSimpleName()).get(cn1);
								if(recordTemp == null)
									recordTemp = records.getImportedRecords(record.getClass().getSimpleName()).get(cn2);
								if(recordTemp == null)
									recordTemp = records.getImportedRecords(record.getClass().getSimpleName()).get(cn3);
//								if((recordTemp instanceof AuthorDTO) && (record instanceof AuthorDTO)){
//			            			AuthorDTO aTemp = ((AuthorDTO)recordTemp);
//			            			AuthorDTO a = ((AuthorDTO)record);
//			            			aTemp.setNotLoaded(true);
//			            			aTemp.getAddress();
//			            			AuthorNameDTO nameWithoutAccents = new AuthorNameDTO(LatCyrUtils.removeAccents(a.getName().getFirstname()), LatCyrUtils.removeAccents(a.getName().getLastname()), "");
//			            			if ((!(aTemp.getAllNames().contains(a.getName()))) && (aTemp.getAllNames().contains(nameWithoutAccents)))
//			            				{
//			            				aTemp.getOtherFormatNames().add(a.getName());
//			            				if(recordDAO1.update(new Person(aTemp.getRecord().getCreator(), aTemp.getRecord().getCreationDate(), aTemp.getRecord().getModifier(), aTemp.getRecord().getLastModificationDate(), aTemp.getRecord().getArchived(), aTemp.getRecord().getType(), aTemp, aTemp.getJmbg(), aTemp.getDirectPhones(), aTemp.getLocalPhones(), aTemp.getApvnt())))
//											System.out.println("Updated: " + aTemp.getRecord().toString());
//										else {
//											System.out.println("PROBLEM !!!!!!!!!!!!!!!");
//										}
//			            			}
//			            		}
								if((recordTemp instanceof StudyFinalDocumentDTO) && (record instanceof StudyFinalDocumentDTO)){
									StudyFinalDocumentDTO sfdTemp = ((StudyFinalDocumentDTO)recordTemp);
									StudyFinalDocumentDTO sfd = ((StudyFinalDocumentDTO)record);
									sfdTemp.setNotLoaded(true);
									sfdTemp.getAbstracT();
									String sfdString = toStringUNS(sfd);
									String sfdTempString = toStringUNS(sfdTemp);
									if(sfdString.equals(sfdTempString))
										;
									else {
//			            				sfdTemp.getAuthor().setName(sfd.getAuthor().getName());
//			            				for (AuthorDTO advisor : sfdTemp.getAdvisors()) {
//											if(sfd.getAdvisors().size() == 1){
//												advisor.setTitle(sfd.getAdvisors().get(0).getTitle());
//												advisor.getCurrentPosition().getPosition().getTerm().setContent(sfd.getAdvisors().get(0).getCurrentPosition().getPosition().getTerm().getContent());
//												advisor.setName(sfd.getAdvisors().get(0).getName());
//											}
//										}
//			            				if(sfd.getCommitteeMembers().size() == sfdTemp.getCommitteeMembers().size()) {
//			            					int i = 0;
//				            				for (AuthorDTO committeeMember : sfdTemp.getCommitteeMembers()) {
//												if(sfd.getCommitteeMembers().size() > i){
//													committeeMember.setTitle(sfd.getCommitteeMembers().get(i).getTitle());
//													committeeMember.getCurrentPosition().getPosition().getTerm().setContent(sfd.getCommitteeMembers().get(i).getCurrentPosition().getPosition().getTerm().getContent());
//													committeeMember.setName(sfd.getCommitteeMembers().get(i).getName());
//												}
//												i++;
//											}
//			            				}
//			            				if(recordDAO.update(new Record(sfdTemp.getRecord().getCreator(), sfdTemp.getRecord().getCreationDate(), sfdTemp.getRecord().getModifier(), sfdTemp.getRecord().getLastModificationDate(), sfdTemp.getRecord().getArchived(), sfdTemp.getRecord().getType(), sfdTemp)))
//											System.out.println("Updated: " + sfdTemp.getRecord().toString());
//										else {
//											System.out.println("PROBLEM !!!!!!!!!!!!!!!");
//										}
										out2.println(sfdTempString);
										out2.println(sfdString);
										out2.println("\n\n");
									}
									recordTemp.clear();
								}
							}	
							else
								System.out.println("Nema: " + record);
						} catch (Exception e) {
							System.out.println("CN:" + record.getControlNumber());
							System.out.println("CN:" + recordTemp.getControlNumber());
//							e.printStackTrace();
						}
					}
		            out2.flush(); 

		           
		            for(int i=0; i<counter; i++){
		            	RecordDTO record = records.getAllRecordsIterator().next();
		            	if(i+1 < counter){
		            		record.clear();
		            		record.setNotLoaded(true);
		            	}	
		            }
		        }catch(IOException i)
		        {
		            i.printStackTrace();
		            return;
		        }catch(ClassNotFoundException c)
		        {
//		            System.out.println("ImportRecordsDTO class not found");
//		            c.printStackTrace();
		            return;
		        }
//			}
		} else {
			records = groupSerializer.importRecords(null);
			nextPublication = null;
			nextPublicationList = new ArrayList<RecordDTO>();
			skipRecords.clear();
		}
	}
	
	public String toStringUNS(StudyFinalDocumentDTO sfd) {
		StringBuffer retVal = new StringBuffer();
		String identificationNumber = sfd.getRegisterEntry().getId();
		String publicatYear = sfd.getPublicationYear();
		AuthorDTO author = sfd.getAuthor();
		String previouslyGraduated = sfd.getRegisterEntry().getPreviouslyGraduated();
		String metPrecondition = sfd.getRegisterEntry().getPreviouslyNameOfAuthorDegreeOld();
		String nameOfAuthorDegree = sfd.getRegisterEntry().getNameOfAuthorDegree();
		MultilingualContentDTO title = sfd.getTitle();
		Calendar publicationDate = sfd.getPublicationDate();
		Calendar promotionDate = sfd.getRegisterEntry().getPromotionDate();
		String diplomaNumber = sfd.getRegisterEntry().getDiplomaNumber();
		List<AuthorDTO> advisors = sfd.getAdvisors();
		List<AuthorDTO> committeeMembers = sfd.getCommitteeMembers();
		
		
		retVal.append("" + ((identificationNumber!=null)?(identificationNumber):("-")));
		retVal.append("," + ((publicatYear!=null)?(publicatYear):("-")));
		retVal.append("," + ((author.getName()!=null)?(Sanitizer.sanitizeCSV(author.getName().toString().replace('-', ' '))):("-")));
		retVal.append("," + ((previouslyGraduated!=null)?(Sanitizer.sanitizeCSV(previouslyGraduated.replace('\n', ' ').replaceAll("\\s+", " "))):("-")));
		retVal.append("," + ((metPrecondition!=null)?(Sanitizer.sanitizeCSV(metPrecondition.replaceAll("\\s+", " "))):("-")));
		retVal.append("," + ((nameOfAuthorDegree!=null)?(Sanitizer.sanitizeCSV(nameOfAuthorDegree.replaceAll("\\s+", " "))):("-")));
		retVal.append("," + ((title.getContent()!=null)?(Sanitizer.sanitizeCSV(title.getContent().replaceAll("\\s+", " "))):("-")));
		String dateString = null;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy.");
		if((publicationDate != null) && (publicationDate.getTime() != null)){
			Date date = publicationDate.getTime();
			if(date != null){
				dateString = formatter.format(date);
			}
		}
		retVal.append("," + ((dateString!=null)?(dateString):("-")));
		dateString = null;
		if((promotionDate != null) && (promotionDate.getTime() != null)){
			Date date = promotionDate.getTime();
			if(date != null){
				dateString = formatter.format(date);
			}
		}
		retVal.append("," + ((dateString!=null)?(dateString):("-")));
		retVal.append("," + ((diplomaNumber!=null)?(diplomaNumber):("-")));
		retVal.append(",");
		for (AuthorDTO advisor : advisors) {
			retVal.append(((advisor.getCurrentPosition().getPosition().getTerm().getContent()!=null)?(advisor.getCurrentPosition().getPosition().getTerm().getContent()):("-")) + ";");
			retVal.append(((advisor.getTitle()!=null)?(advisor.getTitle()):("-")) + ";");
			retVal.append(((advisor.getName()!=null)?(Sanitizer.sanitizeCSV(advisor.getName().toString().replace('-', ' '))):("-")) + ";");
		}
		for (AuthorDTO committeeMember : committeeMembers) {
			retVal.append(",");
			retVal.append(((committeeMember.getCurrentPosition().getPosition().getTerm().getContent()!=null)?(committeeMember.getCurrentPosition().getPosition().getTerm().getContent()):("-")) + ";");
			retVal.append(((committeeMember.getTitle()!=null)?(committeeMember.getTitle()):("-")) + ";");
			retVal.append(((committeeMember.getName()!=null)?(Sanitizer.sanitizeCSV(committeeMember.getName().toString().replace('-', ' '))):("-")) + ";");
		}
		
		return retVal.toString();
	}	
	
	/*private StudyFinalDocumentDTO loadDissertations(XSSFRow row) {
		StudyFinalDocumentDTO thesisDTO = new StudyFinalDocumentDTO();
		
		try {
			String cellContent = "";
			thesisDTO.setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
			
			XSSFCell cell = row.getCell(12);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if("".equals(cellContent))
				return null;
			thesisDTO.getTitle().setContent(cellContent);
			thesisDTO.getTitle().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
			
			cell = row.getCell(0);
			if(cell == null)
				cellContent = "";
			else {
				if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
					cellContent = "" + (int)cell.getNumericCellValue();
				} else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
			}
			thesisDTO.getRegisterEntry().setId(cellContent);
			
			cell = row.getCell(9);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			thesisDTO.getRegisterEntry().setPreviouslyGraduated(cellContent);
			
			cell = row.getCell(10);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			thesisDTO.getRegisterEntry().setPreviouslyNameOfAuthorDegreeOld(cellContent);
			
			cell = row.getCell(11);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			thesisDTO.getRegisterEntry().setNameOfAuthorDegree(cellContent);
			
			cell = row.getCell(14); 
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				DateFormat formatter ; 
				Date date ; 
				formatter = new SimpleDateFormat("dd.MM.yyyy.");
				date = (Date)formatter.parse(cellContent); 
				Calendar cal=GregorianCalendar.getInstance();
				cal.setTime(date);
				thesisDTO.getRegisterEntry().setPromotionDate(cal);
			}
			
			cell = row.getCell(13); 
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				DateFormat formatter ; 
				Date date ; 
				formatter = new SimpleDateFormat("dd.MM.yyyy.");
				date = (Date)formatter.parse(cellContent); 
				Calendar cal=GregorianCalendar.getInstance();
				cal.setTime(date);
				thesisDTO.setDefendedOn(cal);
				thesisDTO.setPublicationDate(cal);
			} else {
				cell = row.getCell(1); 
				if(cell == null)
					cellContent = "";
				else {
					if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
						cellContent = "" + (int)cell.getNumericCellValue();
					} else {
						XSSFRichTextString richTextString = cell.getRichStringCellValue();
						cellContent = richTextString.getString().trim();
					}
				}
				if(! ("".equals(cellContent))){
					Calendar cal=GregorianCalendar.getInstance();
					cal.set(Calendar.YEAR, Integer.parseInt(cellContent));
					cal.set(Calendar.DAY_OF_YEAR, 1);
					thesisDTO.setDefendedOn(cal);
					thesisDTO.setPublicationDate(cal);
				}
			}
			
			cell = row.getCell(23); 
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				DateFormat formatter ; 
				Date date ; 
				formatter = new SimpleDateFormat("dd.MM.yyyy.");
				date = (Date)formatter.parse(cellContent); 
				Calendar cal=GregorianCalendar.getInstance();
				cal.setTime(date);
				thesisDTO.setAcceptedOn(cal);
			}
		    
			cell = row.getCell(15);  
			if(cell == null)
				cellContent = "";
			else {
				if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
					cellContent = "" + (int)cell.getNumericCellValue();
				} else {
					XSSFRichTextString richTextString = cell.getRichStringCellValue();
					cellContent = richTextString.getString().trim();
				}
			}
			thesisDTO.getRegisterEntry().setDiplomaNumber(cellContent);
			
			cell = row.getCell(2);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			
			thesisDTO.getInstitution().getName().setContent(cellContent);
			thesisDTO.getInstitution().getName().setLanguage(MultilingualContentDTO.LANGUAGE_SERBIAN);
			thesisDTO.getInstitution().setControlNumber(cellContent);
			
			
			cell = row.getCell(3);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			
			thesisDTO.getAuthor().getName().setLastname(cellContent);
			
			cell = row.getCell(4);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			
			thesisDTO.getAuthor().getName().setFirstname(cellContent);
			thesisDTO.getAuthor().setControlNumber(thesisDTO.getTitle().getContent() + thesisDTO.getAuthor().getName().getLastname() + thesisDTO.getAuthor().getName().getFirstname());
			
			
			
			
			AuthorDTO authorDTO = new AuthorDTO();
			cell = row.getCell(17);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(18);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(19);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(20);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(21);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(22);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			
			authorDTO = new AuthorDTO();
			cell = row.getCell(16);
			if(cell == null)
				cellContent = "";
			else {
				XSSFRichTextString richTextString = cell.getRichStringCellValue();
				cellContent = richTextString.getString().trim();
			}
			if(! ("".equals(cellContent))){
				String[] data = cellContent.split(";");
				if(!("-".equals(data[0].trim())))
					authorDTO.getCurrentPosition().getPosition().getTerm().setContent(data[0].trim());
				if(!("-".equals(data[1].trim())))
					authorDTO.setTitle(data[1].trim());
				authorDTO.getName().setFirstname(data[2].trim());
				authorDTO.getName().setLastname(data[3].trim());
				authorDTO.setControlNumber(authorDTO.getName().getLastname()+authorDTO.getName().getFirstname());
				thesisDTO.getAdvisors().add(authorDTO);
				thesisDTO.getCommitteeMembers().add(authorDTO);
			}
			thesisDTO.setControlNumber(thesisDTO.getPublicationYear() + thesisDTO.getTitle().getContent());
			thesisDTO.setStudyType("records.studyFinalDocument.editPanel.studyType.phd");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			log.fatal("Cannot load theses");
		}
		return thesisDTO;
	}*/

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.CRUDManagedBean#nextEditTab()
	 */
	@Override
	public void nextEditTab() {
		boolean notImportRecord;
		do{
//			System.out.println(counter);
			if((counter % saveStep == 0) || (records.getAllRecordsIterator().hasNext() == false)){
//				System.out.println("Save");
				saveState();
			}
			counter++;
			notImportRecord = false;
			selectedRecord = (records.getAllRecordsIterator().hasNext())?records.getAllRecordsIterator().next():null;
			if(nextPublication == null){
				nextPublication = (records.getAllPublicationsIterator().hasNext())?records.getAllPublicationsIterator().next():null;
				nextPublicationList = new ArrayList<RecordDTO>();
				if(nextPublication != null)
					nextPublicationList.add(nextPublication);
			}
			if(selectedRecord!=null && nextPublication!=null && selectedRecord.equals(nextPublication)){
				nextPublicationList = new ArrayList<RecordDTO>();
				nextPublication = null;
			}
			selectedRecordIdentifier = (selectedRecord != null)?selectedRecord.getControlNumber():null;
			if((selectedRecord!=null) && (selectedRecord.getControlNumber()!=null) && (selectedRecord.getControlNumber().startsWith("(BISIS)"))){
				selectedRecord.getRecord().loadFromDatabase();
				records.addImportedRecord(selectedRecordIdentifier, selectedRecord);
				notImportRecord = true;
			} 
			if (selectedRecord instanceof AuthorDTO){
				AuthorDTO aut = (AuthorDTO)selectedRecord;
				if (aut.getRelatedRecords().size() == 0)
					notImportRecord = true;
			}
			if (selectedRecord instanceof InstitutionDTO){
				InstitutionDTO ins = (InstitutionDTO)selectedRecord;
				if(ins.getRelatedRecords().size() == 0)
					notImportRecord = true;
			}
			if (selectedRecord instanceof JournalDTO){
				JournalDTO jou = (JournalDTO)selectedRecord;
				if(jou.getRelatedRecords().size() == 0)
					notImportRecord = true;
			}
			
			if (selectedRecord instanceof ConferenceDTO){
				ConferenceDTO con = (ConferenceDTO)selectedRecord;
				if(con.getRelatedRecords().size() == 0)
					notImportRecord = true;
			}
			
			if (selectedRecord instanceof ProceedingsDTO){
				ProceedingsDTO pro = (ProceedingsDTO)selectedRecord;
				if(pro.getRelatedRecords().size() == 0)
					notImportRecord = true;
			}
			if (skipRecords.contains(selectedRecord)){
				notImportRecord = true;
			}
			if (checkExist()){
				notImportRecord = true;
				linkData();
			}
		} while (notImportRecord);
		if (selectedRecord != null) {
			linkData();		
		}
		super.nextEditTab();
	}
	
	private void linkData(){
		if (selectedRecord instanceof InstitutionDTO){
			InstitutionDTO ins = (InstitutionDTO)selectedRecord;
			if(records.getImportedRecords("InstitutionDTO").get(ins.getSuperInstitution().getControlNumber()) != null){
				ins.setSuperInstitution((InstitutionDTO)records.getImportedRecords("InstitutionDTO").get(ins.getSuperInstitution().getControlNumber()));
			}
		} else if (selectedRecord instanceof AuthorDTO){
			AuthorDTO aut = (AuthorDTO)selectedRecord;
			if(records.getImportedRecords("InstitutionDTO").get(aut.getInstitution().getControlNumber()) != null){
				aut.setInstitution((InstitutionDTO)records.getImportedRecords("InstitutionDTO").get(aut.getInstitution().getControlNumber()));
			}
			if(records.getImportedRecords("InstitutionDTO").get(aut.getTitleInstitution().getInstitution().getControlNumber()) != null){
				aut.getTitleInstitution().setInstitution((InstitutionDTO)records.getImportedRecords("InstitutionDTO").get(aut.getTitleInstitution().getInstitution().getControlNumber()));
			}
			if(records.getImportedRecords("OrganizationUnitDTO").get(aut.getOrganizationUnit().getControlNumber()) != null){
				aut.setOrganizationUnit((OrganizationUnitDTO)records.getImportedRecords("OrganizationUnitDTO").get(aut.getOrganizationUnit().getControlNumber()));
			}
		} else if (selectedRecord instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO sfd = (StudyFinalDocumentDTO)selectedRecord; 
			if ((records.getImportedRecords("InstitutionDTO").get(sfd.getInstitution().getControlNumber())) != null){
				sfd.setInstitution((InstitutionDTO)records.getImportedRecords("InstitutionDTO").get(sfd.getInstitution().getControlNumber()));
			}
			if(records.getImportedRecords("AuthorDTO").get(sfd.getAuthor().getControlNumber()) != null){
				AuthorNameDTO authorName = sfd.getAuthor().getName();
				sfd.setAuthor((AuthorDTO)records.getImportedRecords("AuthorDTO").get(sfd.getAuthor().getControlNumber()));
				sfd.getAuthor().setName(authorName);
			}
			for (AuthorDTO author : sfd.getAdvisors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					int index = sfd.getAdvisors().indexOf(author);
					AuthorDTO adv = (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber());
					adv.setName(sfd.getAdvisors().get(index).getName());
					sfd.getAdvisors().set(index, adv);
					sfd.getAdvisors().get(index).setTitle(author.getTitle());
					sfd.getAdvisors().get(index).getCurrentPosition().getPosition().getTerm().setContent(author.getCurrentPosition().getPosition().getTerm().getContent());
				}
			}
			for (AuthorDTO author : sfd.getCommitteeMembers()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					int index = sfd.getCommitteeMembers().indexOf(author);
					AuthorDTO cm = (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber());
					cm.setName(sfd.getCommitteeMembers().get(index).getName());
					sfd.getCommitteeMembers().set(index, cm);
					sfd.getCommitteeMembers().get(index).setTitle(author.getTitle());
					sfd.getCommitteeMembers().get(index).getCurrentPosition().getPosition().getTerm().setContent(author.getCurrentPosition().getPosition().getTerm().getContent());
				}
			}
		} else if (selectedRecord instanceof MonographDTO){
			MonographDTO mon = (MonographDTO)selectedRecord; 
			if(records.getImportedRecords("AuthorDTO").get(mon.getMainAuthor().getControlNumber()) != null){
				mon.setMainAuthor((AuthorDTO)records.getImportedRecords("AuthorDTO").get(mon.getMainAuthor().getControlNumber()));
			}
			for (AuthorDTO author : mon.getOtherAuthors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					mon.getOtherAuthors().set(mon.getOtherAuthors().indexOf(author), (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber()));
				}
			}
		} else if (selectedRecord instanceof JournalDTO){
			JournalDTO jou = (JournalDTO)selectedRecord; 
			for (AuthorDTO author : jou.getEditors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					jou.getEditors().set(jou.getEditors().indexOf(author), (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber()));
				}
			}
		} else if (selectedRecord instanceof PaperJournalDTO){
			PaperJournalDTO paper = (PaperJournalDTO)selectedRecord; 
			if(records.getImportedRecords("JournalDTO").get(paper.getJournal().getControlNumber()) != null){
				paper.setJournal((JournalDTO)records.getImportedRecords("JournalDTO").get(paper.getJournal().getControlNumber()));
			}
			if(records.getImportedRecords("AuthorDTO").get(paper.getMainAuthor().getControlNumber()) != null){
				AuthorNameDTO authorName = paper.getMainAuthor().getName();
				paper.setMainAuthor((AuthorDTO)records.getImportedRecords("AuthorDTO").get(paper.getMainAuthor().getControlNumber()));
				paper.getMainAuthor().setName(authorName);
			}
			for (AuthorDTO author : paper.getOtherAuthors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					int index = paper.getOtherAuthors().indexOf(author);
					AuthorDTO aut = (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber());
					aut.setName(paper.getOtherAuthors().get(index).getName());
					paper.getOtherAuthors().set(index, aut);
				}
			}
		} else if (selectedRecord instanceof ConferenceDTO){
			ConferenceDTO con = (ConferenceDTO)selectedRecord; 
		}  else if (selectedRecord instanceof ProceedingsDTO){
			ProceedingsDTO pro = (ProceedingsDTO)selectedRecord; 
			for (AuthorDTO author : pro.getEditors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					pro.getEditors().set(pro.getEditors().indexOf(author), (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber()));
				}
			}
			if(records.getImportedRecords("ConferenceDTO").get(pro.getConference().getControlNumber()) != null){
				pro.setConference((ConferenceDTO)records.getImportedRecords("ConferenceDTO").get(pro.getConference().getControlNumber()));
			}
		} else if (selectedRecord instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paper = (PaperProceedingsDTO)selectedRecord; 
			if(records.getImportedRecords("ProceedingsDTO").get(paper.getProceedings().getControlNumber()) != null){
				paper.setProceedings((ProceedingsDTO)records.getImportedRecords("ProceedingsDTO").get(paper.getProceedings().getControlNumber()));
				if(records.getImportedRecords("ConferenceDTO").get(paper.getProceedings().getConference().getControlNumber()) != null){
					paper.getProceedings().setConference((ConferenceDTO)records.getImportedRecords("ConferenceDTO").get(paper.getProceedings().getConference().getControlNumber()));
				}
			}
			if(records.getImportedRecords("AuthorDTO").get(paper.getMainAuthor().getControlNumber()) != null){
				AuthorNameDTO authorName = paper.getMainAuthor().getName();
				paper.setMainAuthor((AuthorDTO)records.getImportedRecords("AuthorDTO").get(paper.getMainAuthor().getControlNumber()));
				paper.getMainAuthor().setName(authorName);
			}
			for (AuthorDTO author : paper.getOtherAuthors()) {
				if(records.getImportedRecords("AuthorDTO").get(author.getControlNumber()) != null){
					int index = paper.getOtherAuthors().indexOf(author);
					AuthorDTO aut = (AuthorDTO)records.getImportedRecords("AuthorDTO").get(author.getControlNumber());
					aut.setName(paper.getOtherAuthors().get(index).getName());
					paper.getOtherAuthors().set(index, aut);
				}
			}
		}
	}
	
	public void skipRecord() {
		skipRecords.add(selectedRecord);
		for (RecordDTO record : selectedRecord.getRelatedRecords()) {
			skipRecords.add(record);
			for (RecordDTO record2 : record.getRelatedRecords()) {
				skipRecords.add(record2);
			}
		}
		for (RecordDTO record : records.getAllRecords()) {
			boolean allRelated = false;
			for (RecordDTO relatedRecord : record.getRelatedRecords()) {
				if(skipRecords.contains(relatedRecord))
					allRelated = true;
				else {
					allRelated = false;
					break;
				}
			}
			if(allRelated)
				skipRecords.add(record);
		}
		for (RecordDTO record : records.getAllRecords()) {
			boolean allRelated = false;
			for (RecordDTO relatedRecord : record.getRelatedRecords()) {
				if(skipRecords.contains(relatedRecord))
					allRelated = true;
				else {
					allRelated = false;
					break;
				}
			}
			if(allRelated)
				skipRecords.add(record);
		}
		nextEditTab();
	}
	
	public void saveState(){
		try
	      {
			 if(serializedDataPath != null) {
				 FileOutputStream fileOut =
		         new FileOutputStream(serializedDataPath+"Counter.dat");
		         ObjectOutputStream out =
		                            new ObjectOutputStream(fileOut);
		         out.writeObject(counter);
		         out.close();
		         fileOut.close();
		         File file = new File(serializedDataPath+counter + ".dat");
		         if(! file.exists()) {
			            
		        	 
		         	for (RecordDTO record : records.getImportedRecords(InstitutionDTO.class.getSimpleName()).values()) {
						record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(AuthorDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(StudyFinalDocumentDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(JournalDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(PaperJournalDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(ConferenceDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(ProceedingsDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		            for (RecordDTO record : records.getImportedRecords(PaperProceedingsDTO.class.getSimpleName()).values()) {
		            	record.clear();
						record.setNotLoaded(true);
					}
		        	 fileOut = new FileOutputStream(serializedDataPath+counter + ".dat");
		        	 out = new ObjectOutputStream(fileOut);
		        	 out.writeObject(records);
		        	 out.close();
		        	 fileOut.close();
		         }
			 }
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	/**
	 * @return the selectedRecord
	 */
	public RecordDTO getSelectedRecord() {
		return selectedRecord;
	}

	/**
	 * @param selectedRecord the selectedRecord to set
	 */
	public void setSelectedRecord(RecordDTO selectedRecord) {
		this.selectedRecord = selectedRecord;
	}

	/**
	 * @return the selectedRecordIdentifier
	 */
	public String getSelectedRecordIdentifier() {
		return selectedRecordIdentifier;
	}

	/**
	 * @param selectedRecordIdentifier the selectedRecordIdentifier to set
	 */
	public void setSelectedRecordIdentifier(String selectedRecordIdentifier) {
		this.selectedRecordIdentifier = selectedRecordIdentifier;
	}

	private boolean automaticImport;
	
	/**
	 * @return the automaticImport
	 */
	public boolean isAutomaticImport() {
		return automaticImport;
	}

	/**
	 * @param automaticImport the automaticImport to set
	 */
	public void setAutomaticImport(boolean automaticImport) {
		this.automaticImport = automaticImport;
	}

	private boolean checkExist(){
		boolean notImportRecord = false;
		if (selectedRecord != null) {
			TermQuery tq = new TermQuery(new Term("SCOPUSID", (selectedRecord.getScopusID()==null)?"noSCOPUSID":selectedRecord.getScopusID()));
			TopDocCollector collector = new TopDocCollector(1);
			if (selectedRecord instanceof AuthorDTO){
				List<Record> list = personDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				} 
				else {
					notImportRecord = true;
					for (RecordDTO recordDTO : selectedRecord.getRelatedRecords()) {
						TermQuery tqc = new TermQuery(new Term("SCOPUSID", (recordDTO.getScopusID()==null)?"noSCOPUSID":recordDTO.getScopusID()));
						List<Record> listc = recordDAO.getDTOs(tqc, collector);
						if((listc == null) || (listc.size() == 0)){
							notImportRecord = false;
							break;
						}
					}
				}
			} else if (selectedRecord instanceof JournalDTO){
				List<Record> list = recordDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				}
				else {
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.makeBooleanQuery("NA", ((JournalDTO)selectedRecord).getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.MUST);
					//bq.add(QueryUtils.makeBooleanQuery("ISSN", ((JournalDTO)selectedRecord).getIssn(), Occur.MUST, 0.99f, 0.99f), Occur.SHOULD);
					bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
					list = recordDAO.getDTOs(bq, new TopDocCollector(5));
					boolean found = false;
					for (Record record : list) {
						JournalDTO jou = (JournalDTO)record.getDto();
						JournalManagedBean imb = getJournalManagedBean();
						imb.setSelectedJournal((JournalDTO)selectedRecord);
						imb.switchToImportMode();
						imb.setIPickJournalManagedBean(this);
						if((jou.getIssn() != null) && (jou.getIssn().toLowerCase().contains(imb.getSelectedJournal().getIssn().toLowerCase()))){
							imb.examineData(jou);
							imb.update();
							if (imb.getTableMode() != ModesManagedBean.MODE_NONE)
								imb.setTableMode(ModesManagedBean.MODE_NONE);
							imb.setEditMode(ModesManagedBean.MODE_NONE);
							records.addImportedRecord(selectedRecordIdentifier, imb.getSelectedJournal());
							found = true;
							notImportRecord = true;
							break;
						}
					}
					if(found == false){
						bq = new BooleanQuery();
						//bq.add(QueryUtils.makeBooleanQuery("NA", ((JournalDTO)selectedRecord).getName().getContent(), Occur.SHOULD, 0.99f, 0.99f), Occur.SHOULD);
						bq.add(QueryUtils.makeBooleanQuery("ISSN", ((JournalDTO)selectedRecord).getIssn(), Occur.MUST, 0.99f, 0.99f, false), Occur.MUST);
						bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
						list = recordDAO.getDTOs(bq, new TopDocCollector(5));
						for (Record record : list) {
							JournalDTO jou = (JournalDTO)record.getDto();
							JournalManagedBean imb = getJournalManagedBean();
							imb.setSelectedJournal((JournalDTO)selectedRecord);
							imb.switchToImportMode();
							imb.setIPickJournalManagedBean(this);
							if(jou.getSomeName().equalsIgnoreCase(imb.getSelectedJournal().getName().getContent())){
								imb.examineData(jou);
								imb.update();
								if (imb.getTableMode() != ModesManagedBean.MODE_NONE)
									imb.setTableMode(ModesManagedBean.MODE_NONE);
								imb.setEditMode(ModesManagedBean.MODE_NONE);
								records.addImportedRecord(selectedRecordIdentifier, imb.getSelectedJournal());
								notImportRecord = true;
								break;
							}
						}
					}
				}
			} else if (selectedRecord instanceof PaperJournalDTO){
				List<Record> list = recordDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				}
			} else if (selectedRecord instanceof ConferenceDTO){
				List<Record> list = recordDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				}
				else {
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.makeBooleanQuery("NA", ((ConferenceDTO)selectedRecord).getName().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.MUST);
					//bq.add(QueryUtils.makeBooleanQuery("ISSN", ((JournalDTO)selectedRecord).getIssn(), Occur.MUST, 0.99f, 0.99f), Occur.SHOULD);
					bq.add(new TermQuery(new Term("TYPE", Types.CONFERENCE)), Occur.MUST);
					bq.add(new TermQuery(new Term("YE", ((ConferenceDTO)selectedRecord).getYear().toString())), Occur.MUST);
					list = recordDAO.getDTOs(bq, new TopDocCollector(5));
					boolean found = false;
					for (Record record : list) {
						ConferenceDTO con = (ConferenceDTO)record.getDto();
						ConferenceManagedBean imb = getConferenceManagedBean();
						imb.setSelectedConference((ConferenceDTO)selectedRecord);
						imb.switchToImportMode();
						imb.setIPickConferenceManagedBean(this);
//						if((con.getIsbn() != null) && (pro.getIsbn().toLowerCase().contains(imb.getSelectedProceedings().getIsbn().toLowerCase()))){
							imb.examineData(con);
							imb.update();
							if (imb.getTableMode() != ModesManagedBean.MODE_NONE)
								imb.setTableMode(ModesManagedBean.MODE_NONE);
							imb.setEditMode(ModesManagedBean.MODE_NONE);
							records.addImportedRecord(selectedRecordIdentifier, imb.getSelectedConference());
							found = true;
							notImportRecord = true;
							break;
//						}
					}
				}
			} else if (selectedRecord instanceof ProceedingsDTO){
				List<Record> list = recordDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				}
				else {
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.makeBooleanQuery("TI", ((ProceedingsDTO)selectedRecord).getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.MUST);
					//bq.add(QueryUtils.makeBooleanQuery("ISSN", ((JournalDTO)selectedRecord).getIssn(), Occur.MUST, 0.99f, 0.99f), Occur.SHOULD);
					bq.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
					bq.add(new TermQuery(new Term("COCN", ((ProceedingsDTO)selectedRecord).getConference().getControlNumber())), Occur.MUST);
//					bq.add(QueryUtils.makeBooleanQuery("NA", ((ProceedingsDTO)selectedRecord).getConference().getName().getContent(), Occur.SHOULD, 0.6f, 0.5f, false), Occur.MUST);
//					bq.add(new TermQuery(new Term("YE", ((ProceedingsDTO)selectedRecord).getPublicationYear())), Occur.MUST);
					list = recordDAO.getDTOs(bq, new TopDocCollector(1));
					boolean found = false;
					if((list != null) && (list.size() == 1)){
						Record record = list.get(0);
						ProceedingsDTO pro = (ProceedingsDTO)record.getDto();
						ProceedingsManagedBean imb = getProceedingsManagedBean();
						imb.setSelectedProceedings((ProceedingsDTO)selectedRecord);
						imb.switchToImportMode();
						imb.setIPickProceedingsManagedBean(this);
//						if((pro.getIsbn() != null) && (pro.getIsbn().toLowerCase().contains(imb.getSelectedProceedings().getIsbn().toLowerCase()))){
							imb.examineData(pro);
							imb.update();
							if (imb.getTableMode() != ModesManagedBean.MODE_NONE)
								imb.setTableMode(ModesManagedBean.MODE_NONE);
							imb.setEditMode(ModesManagedBean.MODE_NONE);
							records.addImportedRecord(selectedRecordIdentifier, imb.getSelectedProceedings());
							found = true;
							notImportRecord = true;
//						}
					} else {
						ProceedingsManagedBean imb = getProceedingsManagedBean();
						imb.setSelectedProceedings((ProceedingsDTO)selectedRecord);
						imb.switchToImportMode();
						imb.setIPickProceedingsManagedBean(this);
						imb.add();
						if (imb.getTableMode() != ModesManagedBean.MODE_NONE)
							imb.setTableMode(ModesManagedBean.MODE_NONE);
						imb.setEditMode(ModesManagedBean.MODE_NONE);
						records.addImportedRecord(selectedRecordIdentifier, imb.getSelectedProceedings());
						notImportRecord = true;
					}
				}
			} else if (selectedRecord instanceof PaperProceedingsDTO){
				List<Record> list = recordDAO.getDTOs(tq, collector);
				if((list != null) && (list.size() == 1)){
					records.addImportedRecord(selectedRecordIdentifier, list.get(0).getDto());
					notImportRecord = true;
				}
			} 
		}
		return notImportRecord;
	}
	

	private boolean automaticImportDone;
		
	public void switchAppropriateBeanToImportMode() {
		automaticImportDone = false;
		if (selectedRecord != null) {
			if (selectedRecord instanceof InstitutionDTO){
				InstitutionManagedBean imb = getInstitutionManagedBean();
				imb.setSelectedInstitution((InstitutionDTO)selectedRecord);
				imb.switchToImportMode();
				imb.setIPickInstitutionManagedBean(this);
				if(automaticImport){
					if(imb.getSimilarInstitutions().size() == 0){
						imb.add();
						imb.finishWizard();
						automaticImportDone = true;
					} else if (imb.getSimilarInstitutions().size() == 1){
						if(imb.getSelectedInstitution().getName().getContent().equals(imb.getSimilarInstitutions().get(0).getName().getContent())){
							imb.chooseSimilar(imb.getSimilarInstitutions().get(0));
							automaticImportDone = true;
						}
					} 
				}
			} else if (selectedRecord instanceof AuthorDTO){
				AuthorManagedBean amb = getAuthorManagedBean();
				amb.setSelectedAuthor((AuthorDTO)selectedRecord);
				amb.switchToImportMode();
				amb.setIPickAuthorManagedBean(this);
				if(automaticImport){
					if(amb.getSimilarAuthors().size() == 0){
						amb.add();
						amb.finishWizard();
						automaticImportDone = true;
					} else if(amb.getSimilarAuthors().size() == 1){
						if(amb.getSimilarAuthors().get(0).getAllNames().contains(amb.getSelectedAuthor().getName())){
							amb.examineData(amb.getSimilarAuthors().get(0));
							amb.update();
							amb.finishWizard();
							automaticImportDone = true;
						}
					} 
//					else if ((amb.getSelectedAuthor().getScopusID()!=null) && (!"".equals(amb.getSelectedAuthor().getScopusID()))){
//						for (AuthorDTO aut:amb.getSimilarAuthors())
//							if(aut.getAllNames().contains(amb.getSelectedAuthor().getName()))
//								if((aut.getScopusID() != null) && (aut.getScopusID().equals(amb.getSelectedAuthor().getScopusID()))){
//									amb.chooseSimilar(aut);
//									automaticImportDone = true;
//									break;
//								}
//										
//					} 
//					else if(amb.getSimilarAuthors().size() > 10){
//						amb.switchToImportMode();
//					}
					/*else if (amb.getSimilarAuthors().size() == 1){
						if(amb.getSimilarAuthors().get(0).getAllNames().contains(amb.getSelectedAuthor().getName())){
//							if(amb.getSelectedAuthor().getYearOfBirth() == null){
								amb.chooseSimilar(amb.getSimilarAuthors().get(0));
//							} 
//							else {
//								amb.examineData(amb.getSimilarAuthors().get(0));
//								amb.update();
//								amb.finishWizard();
//							}
							automaticImportDone = true;
						}
					} */
				}
			} else if (selectedRecord instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();	
				sfdmb.setSelectedStudyFinalDocument((StudyFinalDocumentDTO)selectedRecord);
				sfdmb.switchToImportMode();
				sfdmb.setIPickStudyFinalDocumentManagedBean(this);
				if(automaticImport){
					if(sfdmb.getSimilarStudyFinalDocuments().size() == 0){
						sfdmb.add();
						sfdmb.finishWizard();
						automaticImportDone = true;
					} 
				}
//					else if (sfdmb.getSimilarStudyFinalDocuments().size() == 1){
//					if(sfdmb.getSelectedStudyFinalDocument().getTitle().equals(sfdmb.getSimilarStudyFinalDocuments().get(0).getTitle())){
//						FacesContext.getCurrentInstance().getExternalContext()
//							.getRequestParameterMap().put("controlNumber", sfdmb.getSimilarStudyFinalDocuments().get(0).getControlNumber());
//						sfdmb.mergeData() chooseSimilar();
//						nextEditTab();
//					}
//				} 
			} else if (selectedRecord instanceof MonographDTO){
				MonographManagedBean mmb = getMonographManagedBean();	
				mmb.setSelectedMonograph((MonographDTO)selectedRecord);
				mmb.switchToImportMode();
				mmb.setIPickMonographManagedBean(this);
			} else if (selectedRecord instanceof JournalDTO){
				JournalManagedBean imb = getJournalManagedBean();
				imb.setSelectedJournal((JournalDTO)selectedRecord);
				imb.switchToImportMode();
				imb.setIPickJournalManagedBean(this);
				if(automaticImport){
					if(imb.getSimilarJournals().size() == 0){
						imb.add();
						imb.finishWizard();
						automaticImportDone = true;
					} 
//					else if ((imb.getSelectedJournal().getIssn()!=null) && (!"".equals(imb.getSelectedJournal().getIssn()))){
//						for (JournalDTO jou:imb.getSimilarJournals())
//							if(jou.getName().getContent().equalsIgnoreCase(imb.getSelectedJournal().getName().getContent()))
//								if((jou.getIssn() != null) && (jou.getIssn().toLowerCase().contains(imb.getSelectedJournal().getIssn().toLowerCase()))){
//									imb.examineData(jou);
//									imb.update();
//									imb.finishWizard();
//									automaticImportDone = true;
//									break;
//								}
//										
//					} 
//					else if ((imb.getSelectedJournal().getScopusID()!=null) && (!"".equals(imb.getSelectedJournal().getScopusID()))){
//						for (JournalDTO jou:imb.getSimilarJournals())
//							if(jou.getName().getContent().equalsIgnoreCase(imb.getSelectedJournal().getName().getContent()))
//								if((jou.getScopusID() != null) && (jou.getScopusID().equals(imb.getSelectedJournal().getScopusID()))){
//									imb.chooseSimilar(jou);
//									automaticImportDone = true;
//									break;
//								}
//										
//					} 
//					else if(imb.getSimilarJournals().size() > 10){
//						imb.switchToImportMode();
//					}
					/*else if (imb.getSimilarJournals().size() == 1){
						if(imb.getSelectedJournal().getName().getContent().equals(imb.getSimilarJournals().get(0).getName().getContent())){
							imb.chooseSimilar(imb.getSimilarJournals().get(0));
							automaticImportDone = true;
						}
					} */
				}
			} else if (selectedRecord instanceof PaperJournalDTO){
				PaperJournalManagedBean imb = getPaperJournalManagedBean();
				imb.setSelectedPaperJournal((PaperJournalDTO)selectedRecord);
				imb.switchToImportMode();
				imb.setIPickPaperJournalManagedBean(this);
				if(automaticImport){
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.makeBooleanQuery("TI", ((PaperJournalDTO)selectedRecord).getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.MUST);
					BooleanQuery type = new BooleanQuery();
					type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
					bq.add(type, Occur.MUST);
					List<Record> list = recordDAO.getDTOs(bq, new TopDocCollector(5));
					boolean same = false;
					for (Record record : list) {
						PaperJournalDTO jou = (PaperJournalDTO)record.getDto();
						if((jou.getAllAuthors().size() == imb.getSelectedPaperJournal().getAllAuthors().size())
								&& (jou.getPublicationYear()!=null && jou.getPublicationYear().equalsIgnoreCase(imb.getSelectedPaperJournal().getPublicationYear()))){
							imb.examineData(jou);
							imb.update();
							same = true;
	//						imb.finishWizard();
							break;
						}
					}
					if(same==false){
						if(imb.getSimilarPaperJournals().size() == 0){
							imb.add();
//							imb.finishWizard();
//							automaticImportDone = true;
						} 
	//					else {
	//						for (PaperJournalDTO jou:imb.getSimilarPaperJournals()){
	//							if((jou.getTitle().getContent().equalsIgnoreCase(imb.getSelectedPaperJournal().getTitle().getContent()))
	//									&& (jou.getAllAuthors().size() == imb.getSelectedPaperJournal().getAllAuthors().size())
	//									&& (jou.getPublicationYear().equalsIgnoreCase(imb.getSelectedPaperJournal().getPublicationYear()))){
	//								imb.examineData(jou);
	//								imb.update();
	//								imb.finishWizard();
	//								automaticImportDone = true;
	//								break;
	//							}
	//						}
	//					}	
	//					else if ((imb.getSelectedPaperJournal().getScopusID()!=null) && (!"".equals(imb.getSelectedPaperJournal().getScopusID()))){
	//						for (PaperJournalDTO jou:imb.getSimilarPaperJournals()){
	//							if(jou.getTitle().getContent().equalsIgnoreCase(imb.getSelectedPaperJournal().getTitle().getContent())){
	//								if((jou.getScopusID() != null) && (jou.getScopusID().equals(imb.getSelectedPaperJournal().getScopusID()))){
	//									imb.chooseSimilar(jou);
	//									automaticImportDone = true;
	//									break;
	//								}
	//							}
	//						}
	//					}	
						/*else if (imb.getSimilarPaperJournals().size() == 1){
							if(imb.getSelectedPaperJournal().getTitle().equals(imb.getSimilarPaperJournals().get(0).getTitle())){
								FacesContext.getCurrentInstance().getExternalContext()
									.getRequestParameterMap().put("controlNumber", imb.getSimilarPaperJournals().get(0).getControlNumber());
								imb.mergeData();
								imb.chooseSimilar();
								nextEditTab();
							}
						} */
					}
				}
			} else if (selectedRecord instanceof ConferenceDTO){
				ConferenceManagedBean imb = getConferenceManagedBean();
				imb.setSelectedConference((ConferenceDTO)selectedRecord);
				imb.switchToImportMode();
				imb.setIPickConferenceManagedBean(this);
				if(automaticImport){
					if(imb.getSimilarConferences().size() == 0){
						imb.add();
						imb.finishWizard();
						automaticImportDone = true;
					} 
				}
			} else if (selectedRecord instanceof ProceedingsDTO){
				ProceedingsManagedBean imb = getProceedingsManagedBean();
				imb.setSelectedProceedings((ProceedingsDTO)selectedRecord);
//				imb.setConference(((ProceedingsDTO)selectedRecord).getConference());
				imb.switchToImportMode();
				imb.setIPickProceedingsManagedBean(this);
				if(automaticImport){
					if(imb.getSimilarProceedings().size() == 0){
						imb.add();
						imb.finishWizard();
						automaticImportDone = true;
					} 
				}
			} else if (selectedRecord instanceof PaperProceedingsDTO){
				PaperProceedingsManagedBean imb = getPaperProceedingsManagedBean();
				imb.setSelectedPaperProceedings((PaperProceedingsDTO)selectedRecord);
				imb.switchToImportMode();
				imb.setIPickPaperProceedingsManagedBean(this);
				if(automaticImport){
					BooleanQuery bq = new BooleanQuery();
					bq.add(QueryUtils.makeBooleanQuery("TI", ((PaperProceedingsDTO)selectedRecord).getTitle().getContent(), Occur.SHOULD, 0.99f, 0.99f, false), Occur.MUST);
					BooleanQuery type = new BooleanQuery();
					type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
					bq.add(type, Occur.MUST);
					List<Record> list = recordDAO.getDTOs(bq, new TopDocCollector(5));
					boolean same = false;
					for (Record record : list) {
						PaperProceedingsDTO pro = (PaperProceedingsDTO)record.getDto();
						if((pro.getAllAuthors().size() == imb.getSelectedPaperProceedings().getAllAuthors().size())
								&& (pro.getPublicationYear()!=null && pro.getPublicationYear().equalsIgnoreCase(imb.getSelectedPaperProceedings().getPublicationYear()))){
							imb.examineData(pro);
							imb.update();
							same = true;
	//						imb.finishWizard();
							break;
						}
					}
					if(same==false){
						if(imb.getSimilarPaperProceedings().size() == 0){
							imb.add();
//							imb.finishWizard();
//							automaticImportDone = true;
						} 
	//					else {
	//						for (PaperJournalDTO jou:imb.getSimilarPaperJournals()){
	//							if((jou.getTitle().getContent().equalsIgnoreCase(imb.getSelectedPaperJournal().getTitle().getContent()))
	//									&& (jou.getAllAuthors().size() == imb.getSelectedPaperJournal().getAllAuthors().size())
	//									&& (jou.getPublicationYear().equalsIgnoreCase(imb.getSelectedPaperJournal().getPublicationYear()))){
	//								imb.examineData(jou);
	//								imb.update();
	//								imb.finishWizard();
	//								automaticImportDone = true;
	//								break;
	//							}
	//						}
	//					}	
	//					else if ((imb.getSelectedPaperJournal().getScopusID()!=null) && (!"".equals(imb.getSelectedPaperJournal().getScopusID()))){
	//						for (PaperJournalDTO jou:imb.getSimilarPaperJournals()){
	//							if(jou.getTitle().getContent().equalsIgnoreCase(imb.getSelectedPaperJournal().getTitle().getContent())){
	//								if((jou.getScopusID() != null) && (jou.getScopusID().equals(imb.getSelectedPaperJournal().getScopusID()))){
	//									imb.chooseSimilar(jou);
	//									automaticImportDone = true;
	//									break;
	//								}
	//							}
	//						}
	//					}	
						/*else if (imb.getSimilarPaperJournals().size() == 1){
							if(imb.getSelectedPaperJournal().getTitle().equals(imb.getSimilarPaperJournals().get(0).getTitle())){
								FacesContext.getCurrentInstance().getExternalContext()
									.getRequestParameterMap().put("controlNumber", imb.getSimilarPaperJournals().get(0).getControlNumber());
								imb.mergeData();
								imb.chooseSimilar();
								nextEditTab();
							}
						} */
					}
				}
			} 
		}
	}
	
	public String getFormForImport() {
		String retVal = null;
		if(automaticImportDone)
			return "importWizardModalPanel";
		if (selectedRecord != null) {
			if (selectedRecord instanceof InstitutionDTO){
				InstitutionManagedBean imb = getInstitutionManagedBean();
				retVal = imb.getEditModalPanel();
			}
			else if (selectedRecord instanceof AuthorDTO){
				AuthorManagedBean amb = getAuthorManagedBean();
				retVal = amb.getEditModalPanel();
			} else if (selectedRecord instanceof StudyFinalDocumentDTO){
				StudyFinalDocumentManagedBean sfdmb = getStudyFinalDocumentManagedBean();	
				retVal = sfdmb.getEditModalPanel();
			} else if (selectedRecord instanceof MonographDTO){
				MonographManagedBean mmb = getMonographManagedBean();	
				retVal = mmb.getEditModalPanel();
			} else if (selectedRecord instanceof ConferenceDTO){
				ConferenceManagedBean cmb = getConferenceManagedBean();	
				retVal = cmb.getEditModalPanel();
			} else if (selectedRecord instanceof ProceedingsDTO){
				ProceedingsManagedBean pmb = getProceedingsManagedBean();	
				retVal = pmb.getEditModalPanel();
			} else if (selectedRecord instanceof PaperProceedingsDTO){
				PaperProceedingsManagedBean ppmb = getPaperProceedingsManagedBean();	
				retVal = ppmb.getEditModalPanel();
			} else if (selectedRecord instanceof JournalDTO){
				JournalManagedBean jmb = getJournalManagedBean();	
				retVal = jmb.getEditModalPanel();
			} else if (selectedRecord instanceof PaperJournalDTO){
				PaperJournalManagedBean pjmb = getPaperJournalManagedBean();	
				retVal = pjmb.getEditModalPanel();
			} 
		}
		return retVal;
	}
	
	private InstitutionManagedBean institutionManagedBean = null;

	/**
	 * @return the InstitutionManagedBean from current session
	 */
	protected InstitutionManagedBean getInstitutionManagedBean() {
		if (institutionManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			InstitutionManagedBean mb = (InstitutionManagedBean) extCtx.getSessionMap().get(
					"institutionManagedBean");
			if (mb == null) {
				mb = new InstitutionManagedBean();
				extCtx.getSessionMap().put("institutionManagedBean", mb);
			}
			institutionManagedBean = mb;
		}
		return institutionManagedBean;
	}
	
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
	
	private JournalManagedBean journalManagedBean = null;

	/**
	 * @return the JournalManagedBean from current session
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
	
	private ConferenceManagedBean conferenceManagedBean = null;

	/**
	 * @return the ConferenceManagedBean from current session
	 */
	protected ConferenceManagedBean getConferenceManagedBean() {
		if (conferenceManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ConferenceManagedBean mb = (ConferenceManagedBean) extCtx.getSessionMap().get(
					"conferenceManagedBean");
			if (mb == null) {
				mb = new ConferenceManagedBean();
				extCtx.getSessionMap().put("conferenceManagedBean", mb);
			}
			conferenceManagedBean = mb;
		}
		return conferenceManagedBean;
	}
	
	private ProceedingsManagedBean proceedingsManagedBean = null;

	/**
	 * @return the ProceedingsManagedBean from current session
	 */
	protected ProceedingsManagedBean getProceedingsManagedBean() {
		if (proceedingsManagedBean == null) {
			FacesContext facesCtx = FacesContext.getCurrentInstance();
			ExternalContext extCtx = facesCtx.getExternalContext();
			ProceedingsManagedBean mb = (ProceedingsManagedBean) extCtx.getSessionMap().get(
					"proceedingsManagedBean");
			if (mb == null) {
				mb = new ProceedingsManagedBean();
				extCtx.getSessionMap().put("proceedingsManagedBean", mb);
			}
			proceedingsManagedBean = mb;
		}
		return proceedingsManagedBean;
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
		setEditMode(ModesManagedBean.MODE_NONE);
		return null;
	}

	@Override
	protected String dispetcher(int condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the scopusID
	 */
	public String getScopusID() {
		return scopusID;
	}

	/**
	 * @param scopusID the scopusID to set
	 */
	public void setScopusID(String scopusID) {
		this.scopusID = scopusID;
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
	 * @return the publicationYear
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
		startYear = publicationYear;
		endYear = publicationYear;
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

	public String getScopusdatapath() {
		return scopusdatapath;
	}

	public void setScopusdatapath(String scopusdatapath) {
		this.scopusdatapath = scopusdatapath;
	}

	/**
	 * @return the bibtex
	 */
	public String getBibtex() {
		return bibtex;
	}

	/**
	 * @param bibtex the bibtex to set
	 */
	public void setBibtex(String bibtex) {
		this.bibtex = bibtex;
	}
	
	public List<RecordDTO> getNextPublication(){
		return nextPublicationList;
	}
	
}
