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

public abstract class ReportGeneratorManagedBean extends CRUDManagedBean{
	
	protected List<SelectItem> reportTypes;
	protected String selectedOrganisation;
	protected String selectedReport = "";
	protected List<String> selectedReports;
	protected String message = "";

	protected String institutionID = "PMF" ; // UNS UPA
	
	protected List<ReportFileDescription> filesDesc = new ArrayList<ReportFileDescription>();
	protected boolean reportGenerationInProcess = false;
	
	public ReportGeneratorManagedBean(){
		populateList();
	}
	
	public abstract String enterPage();

	@Override
	public void populateList() {
		message = "";
		loadGeneratedReportsStatus();
	}
	
	public abstract void runReportGenerator();

	
	public abstract void loadGeneratedReportsStatus();
	
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

	public abstract void startReportGeneration();

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ReportFileDescription> getFilesDesc() {
		return filesDesc;
	}

	public void setFilesDesc(List<ReportFileDescription> filesDesc) {
		this.filesDesc = filesDesc;
	}

	public boolean isReportGenerationInProcess() {
		return reportGenerationInProcess;
	}

	public void setReportGenerationInProcess(boolean reportGenerationInProcess) {
		this.reportGenerationInProcess = reportGenerationInProcess;
	}

}

	