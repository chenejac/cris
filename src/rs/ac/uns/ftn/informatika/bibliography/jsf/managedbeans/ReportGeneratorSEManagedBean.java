package rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans;

import rs.ac.uns.ftn.informatika.bibliography.dto.UserDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ReportFileDescription;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.Samovrednovanje;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
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

public class ReportGeneratorSEManagedBean extends ReportGeneratorManagedBean{

	private List<SelectItem> organisations;
	private String year;

	private int numberOfYearsForMenthors = 5;

	public String enterPage() {
		UserDTO userDTO = getUserManagedBean().getLoggedUser();
		if(userDTO != null)
			if((userDTO.getInstitution() != null) && (userDTO.getInstitution().getControlNumber() != null) && (userDTO.getInstitution().getControlNumber().equals("(BISIS)5929")))
				institutionID = "PMF";
			else 
				institutionID = "PMF";
		populateList();	
		returnPage = "indexPage";
		
		return "reportsGeneratorSEPage";
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
		reportTypes.add(new SelectItem("PMFInterni", "PMF Interni izvestaj - zbirna produkcija"));
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
		message = "";
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
			selectedReports.add("PMFInterni");
		}else{
			selectedReports.add(selectedReport);
		}
		Samovrednovanje.reportTypesToGenerate = selectedReports;		
		Samovrednovanje.numberOfYearsForMentors = numberOfYearsForMenthors;
		Samovrednovanje.lastYear = year;
		
		startReportGeneration();

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
	
	public boolean showNumberOfYearsForMenthors(){
		return selectedReport.equals("Tabela67saZvanjima") || selectedReport.equals("Tabela67") || selectedReport.equals("Svi");
	}
	
	
	public void startReportGeneration(){
		message = "Generisanje izvestaja je uspesno pokrenuto.";

		boolean initOk = Samovrednovanje.init();
		if(initOk){
			reportGenerationInProcess = true;
			Samovrednovanje.runReportGeneration();
		}
		facesMessages.addToControlFromResourceBundle(
				"generateReportsForm:startGenerating", FacesMessage.SEVERITY_INFO,  "Generisanje izvestaja je zavrseno.",
				FacesContext.getCurrentInstance());
		
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getNumberOfYearsForMenthors() {
		return numberOfYearsForMenthors;
	}

	public void setNumberOfYearsForMenthors(int numberOfYearsForMenthors) {
		this.numberOfYearsForMenthors = numberOfYearsForMenthors;
	}
	
}

	