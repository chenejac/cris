package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.obrazci.DocxUtils;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ReportFileDescription;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.Samovrednovanje;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportGeneratorPHDManagedBean extends ReportGeneratorManagedBean{

	public String enterPage() {
		UserDTO userDTO = getUserManagedBean().getLoggedUser();
		if(userDTO != null)
			if((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)5920")))
				institutionID = "UNS";
			else if((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)94894")))
				institutionID = "UPA";
			else
				institutionID = "UPA";
		populateList();	
		returnPage = "indexPage";
		
		return "reportsGeneratorPHDPage";
	}

	@Override
	public void populateList() {
		reportTypes = new ArrayList<SelectItem>();
		reportTypes.add(new SelectItem("none", "-"));
		reportTypes.add(new SelectItem("aggregateReport", "Zbirni podaci po fakultetima"));
		reportTypes.add(new SelectItem("all", "Svi izvestaji"));


		loadGeneratedReportsStatus();
		
	}
	
	public void runReportGenerator(){	

		message = "Generisanje izvestaja je uspesno pokrenuto.";
		
		reportGenerationInProcess = true;

		selectedReports = new ArrayList<String>();

		if(selectedReport.equals("all")){
			selectedReports.add("aggregateReport");
		}else{
			selectedReports.add(selectedReport);
		}

		startReportGeneration();

	}
	
	
	public void loadGeneratedReportsStatus(){
		try{
		filesDesc = new ArrayList<ReportFileDescription>();
		File dir = new File(generatedReportsDir());
		File[] files = dir.listFiles();
		for(File f:files){
			if(!f.isHidden()){
				BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				ReportFileDescription rf = new ReportFileDescription(f.getName(), sdf.format(attr.lastModifiedTime().toMillis()));
				rf.setUrl(f.toURI().toURL().toString());
				rf.setFile(f);
				filesDesc.add(rf);
			}
		}
		Collections.sort(filesDesc, new GenericComparator<ReportFileDescription>("creationDate", "desc"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String generatedReportsDir() {
		ResourceBundle rbR = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.reports.reports");
		return rbR.getString(("UNS".equals(institutionID))?"generatedReportsUNSDir":"generatedReportsUPADir");
	}
	
	public void startReportGeneration(){
		for (String report:selectedReports){
			if (report.equals("aggregateReport"))
				generateAggregateReportDissertations();
		}
	}

	protected String fromDate = "";
	protected String toDate = "";
	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	private List<StudyFinalDocumentDTO> allPhD(){
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), BooleanClause.Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), BooleanClause.Occur.SHOULD);
		Query query = new BooleanQuery();
		query = QueryUtils.makeBooleanQuery(query, type, QueryDTO.AND);
		query = QueryUtils.makeBooleanQuery(query, new TermQuery(new Term(("UNS".equals(institutionID))?"UNSDISSERTATION":"PADISSERTATION", "true")), QueryDTO.AND);
		RecordDAO recordDAO = new RecordDAO(new RecordDB());
		List<Record> recordsTemp = recordDAO.getDTOs(query, new AllDocCollector(true));
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		for (Record record : recordsTemp) {
			retVal.add((StudyFinalDocumentDTO)record.getDto());
		}
		return retVal;
	}

	private List<InstitutionDTO> getInsitutionsUNS() {
		List<InstitutionDTO> retVal = new ArrayList<>();
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		SearchDissertationsManagedBean mb = (SearchDissertationsManagedBean) extCtx.getSessionMap().get(
				"searchDissertationsManagedBean");
		if (mb == null) {
			mb = new SearchDissertationsManagedBean();
			extCtx.getSessionMap().put("searchDissertationsManagedBean", mb);
		}
		for (ListIterator<TreeNodeDTO<Object>> it = mb.getRoot().listIterator(); it.hasNext(); ) {
			InstitutionDTO ins = (InstitutionDTO)it.next().getElement();
			retVal.add(ins);
		}
		return retVal;
	}

	private List<InstitutionDTO> getInsitutionsUPA() {
		List<InstitutionDTO> retVal = new ArrayList<>();
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		ExternalContext extCtx = facesCtx.getExternalContext();
		SearchDissertationsPAManagedBean mb = (SearchDissertationsPAManagedBean) extCtx.getSessionMap().get(
				"searchDissertationsPAManagedBean");
		if (mb == null) {
			mb = new SearchDissertationsPAManagedBean();
			extCtx.getSessionMap().put("searchDissertationsPAManagedBean", mb);
		}
		for (ListIterator<TreeNodeDTO<Object>> it = mb.getRoot().listIterator(); it.hasNext(); ) {
			InstitutionDTO ins = (InstitutionDTO)it.next().getElement();
			retVal.add(ins);
		}
		return retVal;
	}

	private List<InstitutionDTO> getInsitutions(){
		return ("UNS".equals(institutionID))?getInsitutionsUNS():getInsitutionsUPA();
	}

	private boolean inRange(Calendar date, Integer startYear, Integer endYear){
		if ((date!=null) && (date.get(Calendar.YEAR) >= startYear) && (date.get(Calendar.YEAR) <= endYear))
			return true;
		else
			return false;
	}

	public void generateAggregateReportDissertations(){
		Integer startYear = (fromDate!=null)?Integer.parseInt(fromDate):1950;
		Integer endYear = (toDate!=null)?Integer.parseInt(toDate):Calendar.getInstance().get(Calendar.YEAR);

		Map<String, Map<String, List<StudyFinalDocumentDTO>>> resultsByFacultiesAggregated = new TreeMap<String, Map<String, List<StudyFinalDocumentDTO>>>();
		for (InstitutionDTO institution:getInsitutions()) {
			resultsByFacultiesAggregated.putIfAbsent(institution.getSomeName(), new TreeMap<String, List<StudyFinalDocumentDTO>>());
			resultsByFacultiesAggregated.get(institution.getSomeName()).putIfAbsent("odbranjene", new ArrayList<StudyFinalDocumentDTO>());
			resultsByFacultiesAggregated.get(institution.getSomeName()).putIfAbsent("javne", new ArrayList<StudyFinalDocumentDTO>());
			resultsByFacultiesAggregated.get(institution.getSomeName()).putIfAbsent("usvojene", new ArrayList<StudyFinalDocumentDTO>());
			resultsByFacultiesAggregated.get(institution.getSomeName()).putIfAbsent("otvorene", new ArrayList<StudyFinalDocumentDTO>());
		}

		Map<String, List<StudyFinalDocumentDTO>> total = new TreeMap<String, List<StudyFinalDocumentDTO>>();
		total.putIfAbsent("odbranjene", new ArrayList<StudyFinalDocumentDTO>());
		total.putIfAbsent("javne", new ArrayList<StudyFinalDocumentDTO>());
		total.putIfAbsent("usvojene", new ArrayList<StudyFinalDocumentDTO>());
		total.putIfAbsent("otvorene", new ArrayList<StudyFinalDocumentDTO>());

		List<StudyFinalDocumentDTO> dissertations = allPhD();
		for (StudyFinalDocumentDTO dto:dissertations
		) {
			if (inRange(dto.getPublicationDate(), startYear, endYear)) {
				resultsByFacultiesAggregated.get(dto.getSomeInstitutionName()).get("javne").add(dto);
				total.get("javne").add(dto);
			}

			if (inRange(dto.getDefendedOn(), startYear, endYear)) {
				resultsByFacultiesAggregated.get(dto.getSomeInstitutionName()).get("odbranjene").add(dto);
				total.get("odbranjene").add(dto);
				if ((StringUtils.isNotEmpty(dto.getFileLicense())) && (!"Usage forbidden".equals(dto.getFileLicense()))){
					resultsByFacultiesAggregated.get(dto.getSomeInstitutionName()).get("otvorene").add(dto);
					total.get("otvorene").add(dto);
				}
			}

			if (inRange(dto.getAcceptedOn(), startYear, endYear)) {
				resultsByFacultiesAggregated.get(dto.getSomeInstitutionName()).get("usvojene").add(dto);
				total.get("usvojene").add(dto);
			}

			dto.clear();
		}


		log.info("Creating word document");
		try{

			File f = new File(generatedReportsDir()+"TabelaDisertacije-Zbirno-" + startYear + "-" + endYear + ".docx");
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			ObjectFactory factory = Context.getWmlObjectFactory();
			P naslov1 = DocxUtils.createParagraphWithText("1.	ZBIRNI PREGLED DISERTACIJA ZA PERIOD " + ((fromDate!=null)?fromDate:"1950") + "-" + ((toDate!=null)?toDate:""+Calendar.getInstance().get(Calendar.YEAR)));
			wordMLPackage.getMainDocumentPart().addObject(naslov1);

			Tbl tabelaZbirni = factory.createTbl();
			Tr tableHeaderZbirni = factory.createTr();
			DocxUtils.addTableCell(tableHeaderZbirni, "Redni broj", wordMLPackage);
			DocxUtils.addTableCell(tableHeaderZbirni, "Fakultet", wordMLPackage);
			DocxUtils.addTableCell(tableHeaderZbirni, "Odbranjene", wordMLPackage);
			DocxUtils.addTableCell(tableHeaderZbirni, "Stavljene na uvid", wordMLPackage);
			DocxUtils.addTableCell(tableHeaderZbirni, "Prihvaćene", wordMLPackage);
			DocxUtils.addTableCell(tableHeaderZbirni, "Javne", wordMLPackage);
			tabelaZbirni.getContent().add(tableHeaderZbirni);
			int i = 0;
			for(String fakultet:resultsByFacultiesAggregated.keySet()){
				Tr tableRow = factory.createTr();
				i++;
				DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
				DocxUtils.addTableCell(tableRow, fakultet, wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByFacultiesAggregated.get(fakultet).get("odbranjene").size(), wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByFacultiesAggregated.get(fakultet).get("javne").size(), wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByFacultiesAggregated.get(fakultet).get("usvojene").size(), wordMLPackage);
				DocxUtils.addTableCell(tableRow, ""+resultsByFacultiesAggregated.get(fakultet).get("otvorene").size(), wordMLPackage);
				tabelaZbirni.getContent().add(tableRow);
			}

			Tr tableRow = factory.createTr();
			i++;
			DocxUtils.addTableCell(tableRow, ""+i, wordMLPackage);
			DocxUtils.addTableCell(tableRow, "Ukupno", wordMLPackage);
			DocxUtils.addTableCell(tableRow, "" + total.get("odbranjene").size(), wordMLPackage);
			DocxUtils.addTableCell(tableRow, "" + total.get("javne").size(), wordMLPackage);
			DocxUtils.addTableCell(tableRow, "" + total.get("usvojene").size(), wordMLPackage);
			DocxUtils.addTableCell(tableRow, "" + total.get("otvorene").size(), wordMLPackage);
			tabelaZbirni.getContent().add(tableRow);

			DocxUtils.addBorders(tabelaZbirni);
			wordMLPackage.getMainDocumentPart().addObject(tabelaZbirni);
			wordMLPackage.save(f);

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public boolean showDateRange(){
		return selectedReport.equals("aggregateReport") || selectedReport.equals("all");
	}
}

	