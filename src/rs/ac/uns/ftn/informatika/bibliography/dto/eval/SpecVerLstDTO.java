package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.FileDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.SpecVerLstResultsTypeOfResPublDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class SpecVerLstDTO extends RecordDTO{

	protected String specVerLstUserCode;
	protected MultilingualContentDTO name;
	protected String someName = "";
	protected List<MultilingualContentDTO> nameTranslations;
	protected Calendar startDate;
	protected Calendar endDate;
	protected MultilingualContentDTO keywords;
	protected List<MultilingualContentDTO> keywordsTranslations;
	
	
	protected boolean notLoadedAttachedFiles = true;
	protected List<FileDTO> attachedFiles;
	
	protected boolean notLoadedSpecVerLstResultsTypeOfResPubls = true;
	protected List<SpecVerLstResultsTypeOfResPublDTO> specVerLstResultsTypeOfResPubls;
	
	public SpecVerLstDTO() {
		super();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2008);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2015);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		notLoadedAttachedFiles=true;
		attachedFiles = new ArrayList<FileDTO>(); 
		
		notLoadedSpecVerLstResultsTypeOfResPubls = true;
		specVerLstResultsTypeOfResPubls = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
	}
	
	/**
	 * @param controlNumber
	 *            Control number of the specialy verified list
	 */
	public SpecVerLstDTO(String controlNumber) {
		super();
		this.controlNumber = controlNumber;
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 2000);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		notLoadedAttachedFiles=true;
		attachedFiles = new ArrayList<FileDTO>(); 
		
		notLoadedSpecVerLstResultsTypeOfResPubls = true;
		specVerLstResultsTypeOfResPubls = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
	}

	/**
	 * @param specVerLstUserCode
	 * @param name
	 * @param someName
	 * @param nameTranslations
	 * @param startDate
	 * @param endDate
	 * @param keywords
	 * @param keywordsTranslations
	 * @param attachedFiles
	 * @param resultsTypesOfResultPublications
	 */
	public SpecVerLstDTO(String specVerLstUserCode,
			MultilingualContentDTO name, String someName,
			List<MultilingualContentDTO> nameTranslations, Calendar startDate,
			Calendar endDate, MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations,
			List<FileDTO> attachedFiles, List<SpecVerLstResultsTypeOfResPublDTO> resultsTypesOfResultPublications) {
		super();
		this.specVerLstUserCode = specVerLstUserCode;
		this.name = name;
		this.someName = someName;
		this.nameTranslations = nameTranslations;
		this.startDate = startDate;
		this.endDate = endDate;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		
		notLoadedAttachedFiles=false;
		this.attachedFiles = attachedFiles;
		
		notLoadedSpecVerLstResultsTypeOfResPubls = false;
		this.specVerLstResultsTypeOfResPubls = resultsTypesOfResultPublications;
		
		if(this.startDate==null){
			this.startDate = new GregorianCalendar();
			this.startDate.set(Calendar.YEAR, 1900);
			this.startDate.set(Calendar.MONTH, Calendar.JANUARY);
			this.startDate.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		if(this.endDate==null){
			this.endDate = new GregorianCalendar();
			this.endDate.set(Calendar.YEAR, 2099);
			this.endDate.set(Calendar.MONTH, Calendar.DECEMBER);
			this.endDate.set(Calendar.DAY_OF_MONTH, 31);
		}
	}

	/**
	 * @return the specVerLstUserCode
	 */
	public String getSpecVerLstUserCode() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return specVerLstUserCode;
	}

	/**
	 * @param specVerLstUserCode the specVerLstUserCode to set
	 */
	public void setSpecVerLstUserCode(String specVerLstUserCode) {
		this.specVerLstUserCode = specVerLstUserCode;
	}

	/**
	 * @return the name
	 */
	public MultilingualContentDTO getName() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(MultilingualContentDTO name) {
		this.name = name;
	}
	
	/**
	 * @return original name or first translated name (if original name is not defined)
	 */
	public String getSomeName(){
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return someName;
		}else {
			if(notLoaded)
				record.loadFromDatabase();
			String retVal = null;
			if (name.getContent() != null && locale.getLanguage().equals(name.getLanguage().substring(0,2)))
				retVal = name.getContent();
			for (MultilingualContentDTO nameTranslation : nameTranslations) {
				if((retVal == null) && (locale.getLanguage().equals(nameTranslation.getLanguage().substring(0,2))) && nameTranslation.getContent()!=null)
					retVal = nameTranslation.getContent();	
			}
			
			//ako nije na trazenom jeziku postavi bilo koji naziv
			if (retVal == null){
				if (name.getContent() != null)
					retVal = name.getContent();
				else
					for (MultilingualContentDTO nameTranslation : nameTranslations) {
						if(nameTranslation.getContent()!=null){
							retVal = nameTranslation.getContent();	
							break;
						}
					}
			}
			
			return retVal;
		}
	}

	/**
	 * @param someName the someName to set
	 */
	public void setSomeName(String someName) {
		this.someName = someName;
	}
	
	/**
	 * @return the nameTranslations
	 */
	public List<MultilingualContentDTO> getNameTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return nameTranslations;
	}

	/**
	 * @param nameTranslations the nameTranslations to set
	 */
	public void setNameTranslations(List<MultilingualContentDTO> nameTranslations) {
		this.nameTranslations = nameTranslations;
	}
	
	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the keywords
	 */
	public MultilingualContentDTO getKeywords() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(MultilingualContentDTO keywords) {
		this.keywords = keywords;
	}
	
	/**
	 * @return the keywordsTranslations
	 */
	public List<MultilingualContentDTO> getKeywordsTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return keywordsTranslations;
	}

	/**
	 * @param keywordsTranslations the keywordsTranslations to set
	 */
	public void setKeywordsTranslations(
			List<MultilingualContentDTO> keywordsTranslations) {
		this.keywordsTranslations = keywordsTranslations;
	}
	
	/**
	 * @return the notLoadedAttachedFiles
	 */
	public boolean isNotLoadedAttachedFiles() {
		return notLoadedAttachedFiles;
	}

	/**
	 * @param notLoadedAttachedFiles the notLoadedAttachedFiles to set
	 */
	public void setNotLoadedAttachedFiles(boolean notLoadedAttachedFiles) {
		this.notLoadedAttachedFiles = notLoadedAttachedFiles;
	}

	/**
	 * @return the attachedFiles
	 */
	public List<FileDTO> getAttachedFiles() {
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		return attachedFiles;
	}

	/**
	 * @param attachedFiles the attachedFiles to set
	 */
	public void setAttachedFiles(List<FileDTO> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	/**
	 * @return the notLoadedSpecVerLstResultsTypeOfResPubls
	 */
	public boolean isNotLoadedSpecVerLstResultsTypeOfResPubls() {
		return notLoadedSpecVerLstResultsTypeOfResPubls;
	}

	/**
	 * @param notLoadedSpecVerLstResultsTypeOfResPubls the notLoadedSpecVerLstResultsTypeOfResPubls to set
	 */
	public void setNotLoadedSpecVerLstResultsTypeOfResPubls(
			boolean notLoadedSpecVerLstResultsTypeOfResPubls) {
		this.notLoadedSpecVerLstResultsTypeOfResPubls = notLoadedSpecVerLstResultsTypeOfResPubls;
	}

	/**
	 * @return the specVerLstResultsTypeOfResPubls
	 */
	public List<SpecVerLstResultsTypeOfResPublDTO> getSpecVerLstResultsTypeOfResPubls() {
		if(notLoadedSpecVerLstResultsTypeOfResPubls){
			loadResultsTypesOfResultPublicationsFromDatabase();
		}
		return specVerLstResultsTypeOfResPubls;
	}

	/**
	 * @param specVerLstResultsTypeOfResPubls the specVerLstResultsTypeOfResPubls to set
	 */
	public void setSpecVerLstResultsTypeOfResPubls(
			List<SpecVerLstResultsTypeOfResPublDTO> specVerLstResultsTypeOfResPubls) {
		this.specVerLstResultsTypeOfResPubls = specVerLstResultsTypeOfResPubls;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if((notLoaded) && (locale.getLanguage().equals("sr")))
			return stringRepresentation;
		else {
			if(notLoaded)
				record.loadFromDatabase();
			
			StringBuffer retVal = new StringBuffer();
			String someName = getSomeName();
			if(someName != null)
				retVal.append(getSomeName());
			
			if(startDate!=null || endDate!=null || (specVerLstUserCode!=null && !specVerLstUserCode.equals(""))){
				retVal.append("(");
				retVal.append((specVerLstUserCode!=null && !specVerLstUserCode.equals(""))?(specVerLstUserCode+";"):(""));
				retVal.append(startDate!=null?(startDate.get(Calendar.YEAR)):("NN"));
				retVal.append(endDate!=null?("-" + endDate.get(Calendar.YEAR)):("-NN"));
				retVal.append(")");
			}
			return retVal.toString();
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getHTMLRepresentation()
	 */
	@Override
	public String getHTMLRepresentation() {
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return HTMLRepresentation;
		} else { 
			if(notLoaded)
				record.loadFromDatabase();
			FacesMessages fm= new FacesMessages("messages.messages-records",  locale); 

			//FacesContext.getCurrentInstance().getViewRoot().getLocale()); ovo je bilo zakomentarisano
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + /*fm.getMessageFromResourceBundle("records.journal.header")*/ "Специјано верификована листа" + "</u><br/>");
			
			retVal.append("Назив специјано верификоване листе" /*fm.getMessageFromResourceBundle("records.journal.editPanel.name")*/);
			if (getSomeName() != null)
				retVal.append(": "+ getSomeName());	
			
			if(startDate!=null || endDate!=null || (specVerLstUserCode!=null && !specVerLstUserCode.equals(""))){
				retVal.append("(");
				retVal.append((specVerLstUserCode!=null && !specVerLstUserCode.equals(""))?(specVerLstUserCode+";"):(""));
				retVal.append(startDate!=null?(startDate.get(Calendar.YEAR)):("NN"));
				retVal.append(endDate!=null?("-" + endDate.get(Calendar.YEAR)):("-NN"));
				retVal.append(")");
			}

			if (attachedFiles != null && !attachedFiles.isEmpty()) {
				retVal.append("<br/>");		
				retVal.append("Документи: " /*fm.getMessageFromResourceBundle("records.journal.editPanel.name")*/);
				
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null)
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				
				for (FileDTO iterable_element : attachedFiles) {
					retVal.append("<br/>");
					retVal.append(fm.getMessageFromResourceBundle("records.bibliography.download") +"("+iterable_element.getFileName() +")"+ ": <a target=\"_blank\" href=\"" + filePath + "/DownloadFileServlet/SpecVerLst" + iterable_element.getFileName() + "?controlNumber=" + iterable_element.getControlNumber() + "&fileName=" + iterable_element.getFileName() + "&id=" + iterable_element.getId() + "\"><img src=\"" + filePath +  "/javax.faces.resource/download.png.jsf?ln=img\" height=\"24\" width=\"24\" alt=\"link\" target=\"_blank\"/></a>");
				}
			}
			
			return retVal.toString();
		}
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getKnrXML(java.lang.String, rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO)
	 */
	@Override
	public String getKnrXML(String indent, ResultMeasureDTO resultMeasure) {
		if(notLoaded)
			record.loadFromDatabase();
		StringBuffer retVal = new StringBuffer();
//		retVal.append(indent + "<journal>\n");
//		if(controlNumber != null)
//			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
//		if(getSomeName() != null)
//			retVal.append(indent + "\t<title>" + getSomeName() + "</title>\n");
//		if(issn != null)
//			retVal.append(indent + "\t<issn>" + issn + "</issn>\n");
//		retVal.append(indent + "</journal>\n");
		return retVal.toString();
	}
	
	/**
	 * @return the fileURL
	 */
	public String getFileURL(String fileName) {
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		for (FileDTO file : attachedFiles) {
			if(file.getFileName().equalsIgnoreCase(fileName)){
				return getFileURL(file);
			}
		}
		return "";
	}
	
	/**
	 * @return the fileURL
	 */
	public String getFileURL(int fileID) {
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		for (FileDTO file : attachedFiles) {
			if(file.getId()==fileID){
				return getFileURL(file);
			}
		}
		return "";
	}
	
	/**
	 * @return the fileURL
	 */
	public String getFileURL(FileDTO file){
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		if(file!=null && file.getId() > 0){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null)
				filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			return filePath + "/DownloadFileServlet/SpecVerLst" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId();
		}
		return "";
	}
	
	/**
	 * @return the FileDTO
	 */
	public FileDTO getFileByFileName(String fileName){
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		if(fileName!=null && !"".equals(fileName)){
			for (FileDTO iterable_element : attachedFiles) {
				if(iterable_element.getFileName().equalsIgnoreCase(fileName)){
					return iterable_element;
				}
			}
		}
		return null;
	}
	
	/**
	 * @return the boolean
	 */
	public boolean isExistedFile(FileDTO file){
		boolean retVal = false;
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		if(file!=null && file.getFileName()!=null && !"".equals(file.getFileName())){
			for (FileDTO iterable_element : attachedFiles) {
				if(iterable_element.getFileName().equalsIgnoreCase(file.getFileName())){
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	/**
	 * @return the boolean
	 */
	public boolean addFile(FileDTO file){
		boolean retVal = false;
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		if(!isExistedFile(file)){
        	attachedFiles.add(file);
        	retVal = true;
        }
		return retVal;
	}
	
	/**
	 * @return the boolean
	 */
	public boolean deleteFile(FileDTO file){
		boolean retVal = false;
		if(notLoadedAttachedFiles){
			loadAttachedFilesFromDatabase();
		}
		if(file!=null && file.getFileName()!=null && !"".equals(file.getFileName())){
			int upperBoundary = attachedFiles.size()-1;
			for (int i = upperBoundary; i >= 0; i--) {
				if(attachedFiles.get(i).getFileName().equalsIgnoreCase(file.getFileName())){
					attachedFiles.remove(i);
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	/**
	 * @return the boolean
	 */
	public boolean isExistedSpecVerLstResultsTypeOfResPubl(SpecVerLstResultsTypeOfResPublDTO temp){
		boolean retVal = false;
		if(notLoadedSpecVerLstResultsTypeOfResPubls){
			loadResultsTypesOfResultPublicationsFromDatabase();
		}
		
		if(temp!=null && temp.getPublHumanReadId()!=null && !"".equals(temp.getPublHumanReadId()) && temp.getSpecVerLst()!=null && !"".equals(temp.getSpecVerLst().getControlNumber())){
			for (SpecVerLstResultsTypeOfResPublDTO itEL : specVerLstResultsTypeOfResPubls) {
				if(itEL.equals(temp)==true){
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}
	
	public SpecVerLstResultsTypeOfResPublDTO getSpecVerLstResultsTypeOfResPublByPublHumanReadIdAndYear(String PublHumanReadId, int year){
		SpecVerLstResultsTypeOfResPublDTO retVal = null;
		if(notLoadedSpecVerLstResultsTypeOfResPubls){
			loadResultsTypesOfResultPublicationsFromDatabase();
		}
		
		for (SpecVerLstResultsTypeOfResPublDTO temp : specVerLstResultsTypeOfResPubls) {
			if( ((temp.getPublHumanReadId() != null) && (temp.getPublHumanReadId().equals(PublHumanReadId))) && 
					(temp.getYear() == year)){
				retVal = temp;
				break;
			}	
		}
		return retVal;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		
		notLoaded = true;
		
		specVerLstUserCode=null;
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		relatedRecords = null;

		this.startDate = new GregorianCalendar();
		this.startDate.set(Calendar.YEAR, 2000);
		this.startDate.set(Calendar.MONTH, Calendar.JANUARY);
		this.startDate.set(Calendar.DAY_OF_MONTH, 1);

		this.endDate = new GregorianCalendar();
		this.endDate.set(Calendar.YEAR, 2099);
		this.endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		this.endDate.set(Calendar.DAY_OF_MONTH, 31);

		notLoadedAttachedFiles = true;
		attachedFiles = new ArrayList<FileDTO>();
		
		notLoadedSpecVerLstResultsTypeOfResPubls = true;
		specVerLstResultsTypeOfResPubls = new ArrayList<SpecVerLstResultsTypeOfResPublDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}
	
	public void loadAttachedFilesFromDatabase(){
		attachedFiles.clear();
		FileDAO dao = new FileDAO();
		attachedFiles.addAll(dao.getAllFilesByRecordFromDatabase(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("fileName");
		directionsList.add("asc");
		
		Collections.sort(attachedFiles, new GenericComparator<FileDTO>(
				orderByList, directionsList));
		
		notLoadedAttachedFiles = false;
	}
	
	public void loadResultsTypesOfResultPublicationsFromDatabase(){
		
		specVerLstResultsTypeOfResPubls.clear();
		
		SpecVerLstResultsTypeOfResPublDAO dao = new SpecVerLstResultsTypeOfResPublDAO();
		specVerLstResultsTypeOfResPubls.addAll(dao.getAllSpecVerLstResultsTypeOfResPubls(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("year");
		orderByList.add("publHumanReadId");
		orderByList.add("resultsType");
		directionsList.add("asc");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(specVerLstResultsTypeOfResPubls, new GenericComparator<SpecVerLstResultsTypeOfResPublDTO>(
				orderByList, directionsList));

		notLoadedSpecVerLstResultsTypeOfResPubls = false;
	}
	
	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#toString(java.lang.String)
	 */
	@Override
	public String toString(String id) {
		// TODO Auto-generated method stub
		return this.toString();
	}
}
