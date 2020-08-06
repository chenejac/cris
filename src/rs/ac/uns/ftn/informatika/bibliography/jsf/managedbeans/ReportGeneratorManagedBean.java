package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ReportFileDescription;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.Samovrednovanje;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

public class ReportGeneratorManagedBean extends CRUDManagedBean{
	
	private List<SelectItem> reportTypes;
	private List<SelectItem> organisations;
	private String year;
	private String selectedOrganisation;
	private String selectedReport = "";
	private List<String> selectedReports;
	private String poruka = "";
	private int numberOfYearsForMenthors = 5;
	
	private String institutionID = "PMF" ; // UNS UPA
	
	private List<ReportFileDescription> filesDesc = new ArrayList<ReportFileDescription>();
	private boolean reportGenerationInProcess = false;
	
	public ReportGeneratorManagedBean(){
		populateList();
	}
	
	public String enterPage() {
		UserDTO userDTO = getUserManagedBean().getLoggedUser();
		if(userDTO != null)
			if((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)5929")))
				institutionID = "PMF";
			else 
				institutionID = null;
		populateList();	
		returnPage = "indexPage";
		
		return "reportsGeneratorPage";
	}

	@Override
	public void populateList() {
		reportTypes = new ArrayList<SelectItem>();
		reportTypes.add(new SelectItem("Tabela63", "Tabela63"));
		reportTypes.add(new SelectItem("Tabela64", "Tabela64"));
		reportTypes.add(new SelectItem("Tabela67", "Tabela67"));
		reportTypes.add(new SelectItem("Tabela67saZvanjima", "Tabela67 sa zvanjima"));
		reportTypes.add(new SelectItem("UNS", "UNS"));
		reportTypes.add(new SelectItem("UNSIns", "UNS sa institucijama"));
		reportTypes.add(new SelectItem("UNSCol", "UNS sa bojama"));
		reportTypes.add(new SelectItem("SamoBol", "Samovrednovanje sa boldovanjem"));
		reportTypes.add(new SelectItem("Svi", "Svi"));
		organisations = new ArrayList<SelectItem>();	
		if(institutionID == null) {
			organisations.add(new SelectItem("pmf", "Prirodno-matematicki fakultet"));
			organisations.add(new SelectItem("pmf-dg", "Prirodno-matematicki fakultet bez Departmana za geografiju"));
			organisations.add(new SelectItem("tf", "Tehnoloski fakultet"));
			organisations.add(new SelectItem("dmi", "Departman za matematiku"));
			organisations.add(new SelectItem("dh", "Departman za hemiju"));
			organisations.add(new SelectItem("dg", "Departman za geografiju"));
			organisations.add(new SelectItem("df", "Departman za fiziku"));
			organisations.add(new SelectItem("db", "Departman za biologiju"));
		} else {
			organisations.add(new SelectItem("pmf", "Prirodno-matematicki fakultet"));
			organisations.add(new SelectItem("pmf-dg", "Prirodno-matematicki fakultet bez Departmana za geografiju"));
			organisations.add(new SelectItem("dmi", "Departman za matematiku"));
			organisations.add(new SelectItem("dh", "Departman za hemiju"));
			organisations.add(new SelectItem("dg", "Departman za geografiju"));
			organisations.add(new SelectItem("df", "Departman za fiziku"));
			organisations.add(new SelectItem("db", "Departman za biologiju"));
		}
		poruka = "";
		year = new SimpleDateFormat("yyyy").format(new Date());
		loadGeneratedReportsStatus();
		
	}
	
	public void runReportGenerator(){	
		
		
		Samovrednovanje.organisation = selectedOrganisation;
		selectedReports = new ArrayList<String>();
		
		if(selectedReport.equals("Svi")){
			selectedReports.add("Tabela63");
			selectedReports.add("Tabela64");
			selectedReports.add("Tabela67");
			selectedReports.add("Tabela67saZvanjima");
			selectedReports.add("UNS");
			selectedReports.add("UNSIns");
			selectedReports.add("UNSCol");
			selectedReports.add("SamoBol");
		}else{
			selectedReports.add(selectedReport);
		}
		Samovrednovanje.reportTypesToGenerate = selectedReports;		
		Samovrednovanje.numberOfYearsForMentors = numberOfYearsForMenthors;
		Samovrednovanje.lastYear = year;
		
		
		poruka = "Generisanje izvestaja je uspesno pokrenuto.";
		
		boolean initOk = Samovrednovanje.init();
		if(initOk){
			reportGenerationInProcess = true;
			Samovrednovanje.runReportGeneration();
		}	
		facesMessages.addToControlFromResourceBundle(
				"generateReportsForm:startGenerating", FacesMessage.SEVERITY_INFO,  "Generisanje izvestaja je zavrseno.", 	
				FacesContext.getCurrentInstance());
		
		/*
	   FacesContext context = FacesContext.getCurrentInstance();  
	   try {  
	      System.out.println("runReportGenerator()");           
	      HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
	      HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
	      request.setAttribute("organisation", selectedOrganisation);
	      request.setAttribute("reportTypes", reportTypes);
	      request.setAttribute("numberOfYearsForMentors", "5");
	      RequestDispatcher dispatcher = request.getRequestDispatcher("/ReportsGeneratorServlet");
	      dispatcher.forward(request, response);
	             
             
        }catch (Exception e) {  
           e.printStackTrace();  
        }  
        finally{  
           context.responseComplete();  
        } */		
	}
	
	
	public void loadGeneratedReportsStatus(){
		try{
		filesDesc = new ArrayList<ReportFileDescription>();
		File dir = new File(Samovrednovanje.getGeneratedReportsDir());
		File[] files = dir.listFiles();
		for(File f:files){
			if(institutionID == null){
				if(!f.isHidden()){
					BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					ReportFileDescription rf = new ReportFileDescription(f.getName(), sdf.format(attr.lastModifiedTime().toMillis()));
					rf.setUrl(f.toURI().toURL().toString());
					rf.setFile(f);				
					filesDesc.add(rf);
				}
			} else {
				if(!f.isHidden()){
					BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					ReportFileDescription rf = new ReportFileDescription(f.getName(), sdf.format(attr.lastModifiedTime().toMillis()));
					rf.setUrl(f.toURI().toURL().toString());
					rf.setFile(f);				
					if(!f.getName().toLowerCase().contains("tf.docx"))
						filesDesc.add(rf);
				}
			}
		}
		Collections.sort(filesDesc, new GenericComparator<ReportFileDescription>("creationDate", "desc"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void downloadFile(File file) {   
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	    response.setHeader("Content-Disposition", "attachment;filename="+file.getName());
	    response.setContentLength((int) file.length());
	    FileInputStream input= null;
	    try {
	        int i= 0;
	        input = new FileInputStream(file);  
	        byte[] buffer = new byte[1024];
	        while ((i = input.read(buffer)) != -1) {  
	            response.getOutputStream().write(buffer);  
	            response.getOutputStream().flush();  
	        }               
	        facesContext.responseComplete();
	        facesContext.renderResponse();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if(input != null) {
	                input.close();
	            }
	        } catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public boolean showNumberOfYearsForMenthors(){
		return selectedReport.equals("Tabela67saZvanjima") || selectedReport.equals("Tabela67") || selectedReport.equals("Svi");
	}
	
	
	public void startReportGeneration(){
		
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
	protected String dispetcher(int condition) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SelectItem> getReportTypes() {
		return reportTypes;
	}

	public void setReportTypes(List<SelectItem> reportTypes) {
		this.reportTypes = reportTypes;
	}

	public List<SelectItem> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<SelectItem> organisations) {
		this.organisations = organisations;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSelectedOrganisation() {
		return selectedOrganisation;
	}

	public void setSelectedOrganisation(String selectedOrganisation) {
		this.selectedOrganisation = selectedOrganisation;
	}

	public String getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(String selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<String> getSelectedReports() {
		return selectedReports;
	}

	public void setSelectedReports(List<String> selectedReports) {
		this.selectedReports = selectedReports;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public List<ReportFileDescription> getFilesDesc() {
		return filesDesc;
	}

	public void setFilesDesc(List<ReportFileDescription> filesDesc) {
		this.filesDesc = filesDesc;
	}

	public int getNumberOfYearsForMenthors() {
		return numberOfYearsForMenthors;
	}

	public void setNumberOfYearsForMenthors(int numberOfYearsForMenthors) {
		this.numberOfYearsForMenthors = numberOfYearsForMenthors;
	}

	public boolean isReportGenerationInProcess() {
		return reportGenerationInProcess;
	}

	public void setReportGenerationInProcess(boolean reportGenerationInProcess) {
		this.reportGenerationInProcess = reportGenerationInProcess;
	}

	
	
	
	
	
}

	