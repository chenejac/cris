package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.io.File;

public class ReportFileDescription {
	
	private String fileName;
	private String creationDate;
	private String url;
	private File file;
	
	
	public ReportFileDescription(String fileName, String creationDate) {
		super();
		this.fileName = fileName;
		this.creationDate = creationDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	
	
	
	
	

}
