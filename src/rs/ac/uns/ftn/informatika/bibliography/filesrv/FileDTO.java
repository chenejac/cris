package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.context.FacesContext;

@SuppressWarnings("serial")
public class FileDTO implements Serializable{

    private String fileName;
    private String fileNameClient;
    private String mime;
    private long length;
    private byte[] data;
    private int id;
    private String controlNumber;
    private String uploader;
    private Calendar dateModified;
    private String license;
    private String type;
    private String note;
    
    
	public FileDTO() {
		super();
	}

	/**
	 * @param id
	 * @param controlNumber
	 * @param fileName
	 */
	public FileDTO(int id, String controlNumber, String fileName) {
		super();
		this.id = id;
		this.controlNumber = controlNumber;
		setFileName(fileName);
	}
	
	/**
	 * @param id
	 * @param controlNumber
	 * @param fileName
	 * @param fileNameClient
	 * @param mime
	 * @param uploader
	 * @param length
	 * @param dateModified
	 * @param license
	 * @param type
	 * @param note
	 */
	public FileDTO(int id, String controlNumber, String fileName, String fileNameClient, String mime, String uploader,
			long length, Calendar dateModified, String license, String type, String note) {
		super();
		this.id = id;
		this.controlNumber = controlNumber;
		this.fileName = fileName;
		this.fileNameClient = fileNameClient;
		this.mime = mime;
		this.uploader = uploader;
		this.length = length;
		this.dateModified = dateModified;
		this.license = license;
		this.type = type;
		this.note = note;
	}

	public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String name) {
        fileName = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("pdf".equals(extension)){
                mime="application/pdf";
            } 
            if("doc".equals(extension)){
                mime="application/msword";
            } 
            if("docx".equals(extension)){
                mime="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } 
            if("odt".equals(extension)){
                mime="application/vnd.oasis.opendocument.text";
            } 
            if("png".equals(extension)){
                mime="image/png";
            } 
        }
    }
    
    public String getFileNameClient() {
        return fileNameClient;
    }
    
    public void setFileNameClient(String name) {
        fileNameClient = name;
        if(fileName == null){
        	int extDot = name.lastIndexOf('.');
            if(extDot > 0){
                String extension = name.substring(extDot +1);
                setFileName(System.currentTimeMillis() + "" + (int)(Math.random() * 100) + "." + extension);
            }
        }
    }
    
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getMime(){
        return mime;
    }
    /**
     * @param mime the mime to set
     */
    public void setMime(String mime) {
    	this.mime = mime;
    }
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the controlNumber
	 */
	public String getControlNumber() {
		return controlNumber;
	}

	/**
	 * @param controlNumber the controlNumber to set
	 */
	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	/**
	 * @return the uploader
	 */
	public String getUploader() {
		return uploader;
	}
	/**
	 * @param uploader the uploader to set
	 */
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	
	/**
	 * @return the dateModified
	 */
	public Calendar getDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Calendar dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public String toString() {
	    return "FileDB[id=" + id + ",controlNumber=" + controlNumber + ",filename=" + fileName
	        + ",filenameclient=" + fileNameClient +  ",mime=" + mime + ",uploader=" + uploader + ",license=" + license + ",type=" + type + ",note=" + note +"]";
	}
	
	public String getFileURL(){
		
		if(id > 0){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null)
				filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			return filePath + "/DownloadFileServlet/File" + fileName + "?controlNumber=" + controlNumber + "&fileName=" + fileName + "&id=" + id;
		}
		return "";
	}	
    
}
