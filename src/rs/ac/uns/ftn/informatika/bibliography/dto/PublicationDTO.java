package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.wordcloud.WordCloudFromPdfGenerator;


/**
 * Abstract class which must be extended by DTO classes which present some type
 * of bibliographic or normative MARC21Record.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public abstract class PublicationDTO extends RecordDTO {

	protected String alphabet;
	protected String publicationYear;
	protected FileDTO file;
	protected String fileName;
	protected int fileId;
	protected String fileURL="";
	protected String fileLicense="";
	protected FileDTO supplement;
	protected String supplementURL = "";
	protected FileDTO fileCopyright;
	protected FileDTO wordCloudImage;
	private String wordCloudImageURL = "";
	protected List<JobAdDTO> jobAds;
	protected String uri = "";

	protected String doi;

	/**
	 * @return the doi
	 */
	public String getDoi() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return doi;
	}

	/**
	 * @param doi the doi to set
	 */
	public void setDoi(String doi) {
		this.doi = doi;
	}


	public PublicationDTO() {
		super();
		jobAds = new ArrayList<JobAdDTO>();
	}

	/**
	 * @param controlNumber
	 */
	public PublicationDTO(String controlNumber) {
		super();
		this.controlNumber = controlNumber;
		jobAds = new ArrayList<JobAdDTO>();
	}
	
	/**
	 * @param controlNumber
	 * @param publicationYear
	 */
	public PublicationDTO(String controlNumber, String publicationYear) {
		super();
		this.controlNumber = controlNumber;
		this.publicationYear = publicationYear;
		jobAds = new ArrayList<JobAdDTO>();
	}
	
	public List<AuthorDTO> getAllAuthors(){
		return new ArrayList<AuthorDTO>();
	}
	
	public List<AuthorDTO> getOtherAuthors(){
		return new ArrayList<AuthorDTO>();
	}
	
	public List<AuthorDTO> getEditors(){
		return new ArrayList<AuthorDTO>();
	}
	
	/**
	 * @return the alphabet
	 */
	public String getAlphabet() {
		return alphabet;
	}

	/**
	 * @param alphabet the alphabet to set
	 */
	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	/**
	 * @return the publicationYear
	 */
	public String getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
	
	/**
	 * @return the file
	 */
	public FileDTO getFile() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileDTO file) {
		this.file = file;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	/**
	 * @return the fileURL
	 */
	public String getFileURL() {
		if(notLoaded){
			return fileURL;
		} 
		else if(record.getFiles()!=null){
				for (FileDTO file : record.getFiles()) {
					if((file.getId() != 0) && (file.getType() != null) && (file.getType().equals("document"))){
						String filePath = "";
						if(FacesContext.getCurrentInstance()!=null){
							try {
								filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
							} catch (Throwable e){
							}
						}
						if(filePath == null)
							filePath = "";
						return filePath + "/DownloadFileServlet/Disertacija" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId();
					}
				}
			}  
		return "";
	}

	/**
	 * @param fileURL the fileURL to set
	 */
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	
	/**
	 * @return the wordCloudImage
	 */
	public FileDTO getWordCloudImage() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return wordCloudImage;
	}

	/**
	 * @param wordCloudImage the wordCloudImage to set
	 */
	public void setWordCloudImage(FileDTO wordCloudImage) {
		this.wordCloudImage = wordCloudImage;
	}

	/**
	 * @return the wordCloudImageURL
	 */
	public String getWordCloudImageURL() {
		if(notLoaded){
			return wordCloudImageURL;
		} 
		if((wordCloudImage != null) && (wordCloudImage.getId() != 0)){
			String filePath = "";
//			if(FacesContext.getCurrentInstance()!=null)
//				filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			return filePath + "/DownloadFileServlet/wordCloud" + wordCloudImage.getFileName() + "?controlNumber=" + wordCloudImage.getControlNumber() + "&fileName=" + wordCloudImage.getFileName() + "&id=" + wordCloudImage.getId();
		}
		return "";
	}

	/**
	 * @param wordCloudImageURL the wordCloudImageURL to set
	 */
	public void setWordCloudImageURL(String wordCloudImageURL) {
		this.wordCloudImageURL = wordCloudImageURL;
	}
	
	/**
	 * @return the fileLicense
	 */
	public String getFileLicense() {
		if(notLoaded){
			return fileLicense;
		}  else if(record.getFiles()!=null){
			for (FileDTO file : record.getFiles()) {
				if((file.getId() != 0) && (file.getType() != null) && (file.getType().equals("document"))){
					return file.getLicense();
				}
			}
		}  
		return "";
	}

	/**
	 * @param fileLicense the fileLicense to set
	 */
	public void setFileLicense(String fileLicense) {
		this.fileLicense = fileLicense;
	}
	
	/**
	 * @return the supplement
	 */
	public FileDTO getSupplement() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return supplement;
	}

	/**
	 * @param supplement the supplement to set
	 */
	public void setSupplement(FileDTO supplement) {
		this.supplement = supplement;
	}

	/**
	 * @return the supplementURL
	 */
	public String getSupplementURL() {
		if(notLoaded){
			return supplementURL;
		} 
		if((supplement != null) && (supplement.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/DisertacijaDodatak" + supplement.getFileName() + "?controlNumber=" + supplement.getControlNumber() + "&fileName=" + supplement.getFileName() + "&id=" + supplement.getId();
		}
		return "";
	}

	/**
	 * @param supplementURL the supplementURL to set
	 */
	public void setSupplementURL(String supplementURL) {
		this.supplementURL = supplementURL;
	}
	
	/**
	 * @return the fileCopyright
	 */
	public FileDTO getFileCopyright() {
		return fileCopyright;
	}

	/**
	 * @param fileCopyright the fileCopyright to set
	 */
	public void setFileCopyright(FileDTO fileCopyright) {
		this.fileCopyright = fileCopyright;
	}
	
	/**
	 * @return the fileCopyrightURL
	 */
	public String getFileCopyrightURL() {
		if(notLoaded){
			record.loadFromDatabase();
		} 
		if((fileCopyright != null) && (fileCopyright.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/IzjavaOKoriscenju" + fileCopyright.getFileName() + "?controlNumber=" + fileCopyright.getControlNumber() + "&fileName=" + fileCopyright.getFileName() + "&id=" + fileCopyright.getId();
		}
		return "";
	}


	/**
	 * @return the jobAds
	 */
	public List<JobAdDTO> getJobAds() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return jobAds;
	}

	/**
	 * @param jobAds the jobAds to set
	 */
	public void setJobAds(List<JobAdDTO> jobAds) {
		this.jobAds = jobAds;
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#setFiles(java.util.List)
	 */
	@Override
	public void setFiles(List<FileDTO> files) {
		this.file = null;
		this.wordCloudImage = null;
		boolean newWordCloudImage = false;
		if(files!=null){
			for (FileDTO fileDTO : files) {
				if((fileDTO != null) && (fileDTO.getType() != null) && (fileDTO.getType().equals("wordCloudImage"))){
					this.wordCloudImage = fileDTO;
				} else if ((fileDTO != null) && (fileDTO.getType() != null) && (fileDTO.getType().equals("supplement"))){
					this.supplement = fileDTO;
				} else if ((fileDTO != null) && (fileDTO.getType() != null) && (fileDTO.getType().equals("copyright"))) {
					this.fileCopyright = fileDTO;
				} else if((fileDTO != null) && (fileDTO.getType() != null) && (fileDTO.getType().equals("document"))){
					this.file = fileDTO;
//					if(file.getId() == 0){
//						try {
//							newWordCloudImage = true;
//							this.wordCloudImage = new FileDTO();
//							ByteArrayOutputStream bos = new ByteArrayOutputStream();
//							break;
//						} catch (Exception e) {
//						}
//					}
				}
			}
			
		}
		super.setFiles(files);
	}
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getRelevanceScore()
	 */
	@Override
	public Float getRelevanceScore() {
		// TODO Auto-generated method stub
		return super.getRelevanceScore();
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getRecommendationScore()
	 */
	@Override
	public Float getRecommendationScore() {
		// TODO Auto-generated method stub
		return super.getRecommendationScore();
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getMixedScore()
	 */
	@Override
	public Float getMixedScore() {
		// TODO Auto-generated method stub
		return super.getMixedScore();
	}
	
	
	
	
}
