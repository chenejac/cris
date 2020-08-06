package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dao.eval.EntityResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookEntityTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.eval.RuleBookResultsTypeMappingDAO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;
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
public class RuleBookDTO extends RecordDTO {

	protected FileDTO file;
	protected String fileName;
	protected int fileId;
	protected FileDTO deletedFile;
	protected String fileURL="";
	
	protected String ruleBookUserCode;
	protected MultilingualContentDTO name;
	protected String someName = "";
	protected List<MultilingualContentDTO> nameTranslations;
	protected MultilingualContentDTO description;
	protected List<MultilingualContentDTO> descriptionTranslations;
	protected Calendar startDate;
	protected Calendar endDate;
	protected MultilingualContentDTO keywords;
	protected List<MultilingualContentDTO> keywordsTranslations;

	protected ResultsTypeGroupDTO resultsTypeGroup;
	
	protected boolean notLoadedRuleBookResultsTypes = true;
	protected List<RuleBookResultsTypeDTO> ruleBookResultsTypes;
	
	protected boolean notLoadedRuleBookEntityTypes = true;
	protected List<RuleBookEntityTypeDTO> ruleBookEntityTypes;

	protected boolean notLoadedEntityResultsTypes = true;
	protected List<EntityResultsTypeDTO> entityResultsTypes;
	
	protected boolean notLoadedRuleBookResultsTypeMappings = true;
	protected List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings;
	
	public RuleBookDTO() {
		super();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		ruleBookUserCode = "";
		resultsTypeGroup = new ResultsTypeGroupDTO();
		
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		notLoadedRuleBookResultsTypes = true;
		ruleBookResultsTypes = new ArrayList<RuleBookResultsTypeDTO>();
		notLoadedRuleBookEntityTypes = true;
		ruleBookEntityTypes = new ArrayList<RuleBookEntityTypeDTO>();
		notLoadedEntityResultsTypes = true;
		entityResultsTypes = new ArrayList<EntityResultsTypeDTO>();
		notLoadedRuleBookResultsTypeMappings = true;
		ruleBookResultsTypeMappings = new ArrayList<RuleBookResultsTypeMappingDTO>();
	}
	
	/**
	 * @param controlNumber
	 *            Control number of the rule book
	 */
	public RuleBookDTO(String controlNumber) {
		super();
		this.controlNumber = controlNumber;
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		ruleBookUserCode = "";
		resultsTypeGroup = new ResultsTypeGroupDTO();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		startDate = new GregorianCalendar();
		startDate.set(Calendar.YEAR, 1900);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		endDate = new GregorianCalendar();
		endDate.set(Calendar.YEAR, 2099);
		endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		endDate.set(Calendar.DAY_OF_MONTH, 31);
		
		notLoadedRuleBookResultsTypes = true;
		ruleBookResultsTypes = new ArrayList<RuleBookResultsTypeDTO>();
		notLoadedRuleBookEntityTypes = true;
		ruleBookEntityTypes = new ArrayList<RuleBookEntityTypeDTO>();
		notLoadedEntityResultsTypes = true;
		entityResultsTypes = new ArrayList<EntityResultsTypeDTO>();
		notLoadedRuleBookResultsTypeMappings = true;
		ruleBookResultsTypeMappings = new ArrayList<RuleBookResultsTypeMappingDTO>();
	}

	/**
	 * @param ruleBookUserCode
	 * @param name
	 * @param someName
	 * @param nameTranslations
	 * @param description
	 * @param descriptionTranslations
	 * @param startDate
	 * @param endDate
	 * @param keywords
	 * @param keywordsTranslations
	 * @param resultsTypeGroup
	 * @param ruleBookResultsTypes
	 * @param ruleBookEntityTypes
	 * @param entityResultsTypes
	 * @param ruleBookResultsTypeMappings
	 */
	public RuleBookDTO(String ruleBookUserCode, MultilingualContentDTO name,
			String someName, List<MultilingualContentDTO> nameTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			Calendar startDate, Calendar endDate,
			MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations,
			ResultsTypeGroupDTO resultsTypeGroup,
			List<RuleBookResultsTypeDTO> ruleBookResultsTypes, List<RuleBookEntityTypeDTO> ruleBookEntityTypes, List<EntityResultsTypeDTO> entityResultsTypes,
			List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings) {
		super();
		this.ruleBookUserCode = ruleBookUserCode;
		this.name = name;
		this.someName = someName;
		this.nameTranslations = nameTranslations;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
		this.startDate = startDate;
		this.endDate = endDate;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.resultsTypeGroup = resultsTypeGroup;
		this.ruleBookResultsTypes = ruleBookResultsTypes;
		this.ruleBookEntityTypes = ruleBookEntityTypes;
		this.entityResultsTypes = entityResultsTypes;
		this.ruleBookResultsTypeMappings = ruleBookResultsTypeMappings;
		
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
		
		notLoadedRuleBookResultsTypes = false;
		notLoadedRuleBookEntityTypes = false;
		notLoadedEntityResultsTypes = false;
		notLoadedRuleBookResultsTypeMappings = false;
	}

	/**
	 * @return the ruleBookUserCode
	 */
	public String getRuleBookUserCode() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return ruleBookUserCode;
	}

	/**
	 * @param ruleBookUserCode the ruleBookUserCode to set
	 */
	public void setRuleBookUserCode(String ruleBookUserCode) {
		this.ruleBookUserCode = ruleBookUserCode;
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
			if(description!= null && description.getLanguage()==null)
				description.setLanguage(name.getLanguage());
			
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
	 * @return the description
	 */
	public MultilingualContentDTO getDescription() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(MultilingualContentDTO description) {
		this.description = description;
	}

	/**
	 * @return the descriptionTranslations
	 */
	public List<MultilingualContentDTO> getDescriptionTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return descriptionTranslations;
	}

	/**
	 * @param descriptionTranslations the descriptionTranslations to set
	 */
	public void setDescriptionTranslations(
			List<MultilingualContentDTO> descriptionTranslations) {
		this.descriptionTranslations = descriptionTranslations;
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
	 * @return the resultsTypeGroup
	 */
	public ResultsTypeGroupDTO getResultsTypeGroup() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return resultsTypeGroup;
	}

	/**
	 * @param resultsTypeGroup the resultsTypeGroup to set
	 */
	public void setResultsTypeGroup(ResultsTypeGroupDTO resultsTypeGroup) {
//		if(this.resultsTypeGroup!=null && !"".equals(this.resultsTypeGroup.getClassId()) && !this.resultsTypeGroup.equals(resultsTypeGroup)){
//			notLoadedBookResultsTypes = true;
//			notLoadedRuleBookEntityTypes = true;
//		}
			
		this.resultsTypeGroup = resultsTypeGroup;
	}

	/**
	 * @return the ruleBookResultsTypes
	 */
	public List<RuleBookResultsTypeDTO> getRuleBookResultsTypes() {
		if(notLoadedRuleBookResultsTypes){
			loadAllRuleBookResultsTypesFromDatabase();
		}
		return ruleBookResultsTypes;
	}

	/**
	 * @param ruleBookResultsTypes the ruleBookResultsTypes to set
	 */
	public void setRuleBookResultsTypes(
			List<RuleBookResultsTypeDTO> ruleBookResultsTypes) {
		this.ruleBookResultsTypes = ruleBookResultsTypes;
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
	 * @return the ruleBookEntityTypes
	 */
	public List<RuleBookEntityTypeDTO> getRuleBookEntityTypes() {
		if(notLoadedRuleBookEntityTypes){
			loadAllRuleBookEntityTypesFromDatabase();
		}
		return ruleBookEntityTypes;
	}

	/**
	 * @param ruleBookEntityTypes the ruleBookEntityTypes to set
	 */
	public void setRuleBookEntityTypes(
			List<RuleBookEntityTypeDTO> ruleBookEntityTypes) {
		this.ruleBookEntityTypes = ruleBookEntityTypes;
	}
	
	/**
	 * @return the entityResultsTypes
	 */
	public List<EntityResultsTypeDTO> getEntityResultsTypes() {
		if(notLoadedEntityResultsTypes){
			loadAllEntityResultsTypesFromDatabase();
		}
		return entityResultsTypes;
	}

	/**
	 * @param entityResultsTypes the entityResultsTypes to set
	 */
	public void setEntityResultsTypes(List<EntityResultsTypeDTO> entityResultsTypes) {
		this.entityResultsTypes = entityResultsTypes;
	}
	
	/**
	 * @return the ruleBookResultsTypeMappings
	 */
	public List<RuleBookResultsTypeMappingDTO> getRuleBookResultsTypeMappings() {
		if(notLoadedRuleBookResultsTypeMappings)
			loadAllRuleBookResultsTypeMappingsFromDatabase();
		return ruleBookResultsTypeMappings;
	}

	/**
	 * @param ruleBookResultsTypeMappings the ruleBookResultsTypeMappings to set
	 */
	public void setRuleBookResultsTypeMappings(
			List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings) {
		this.ruleBookResultsTypeMappings = ruleBookResultsTypeMappings;
	}
	
	/**
	 * @param notLoadedRuleBookResultsTypes the notLoadedRuleBookResultsTypes to set
	 */
	public void setNotLoadedRuleBookResultsTypes(
			boolean notLoadedRuleBookResultsTypes) {
		this.notLoadedRuleBookResultsTypes = notLoadedRuleBookResultsTypes;
	}

	/**
	 * @param notLoadedRuleBookEntityTypes the notLoadedRuleBookEntityTypes to set
	 */
	public void setNotLoadedRuleBookEntityTypes(boolean notLoadedRuleBookEntityTypes) {
		this.notLoadedRuleBookEntityTypes = notLoadedRuleBookEntityTypes;
	}

	/**
	 * @param notLoadedEntityResultsTypes the notLoadedEntityResultsTypes to set
	 */
	public void setNotLoadedEntityResultsTypes(boolean notLoadedEntityResultsTypes) {
		this.notLoadedEntityResultsTypes = notLoadedEntityResultsTypes;
	}

	/**
	 * @param notLoadedRuleBookResultsTypeMappings the notLoadedRuleBookResultsTypeMappings to set
	 */
	public void setNotLoadedRuleBookResultsTypeMappings(
			boolean notLoadedRuleBookResultsTypeMappings) {
		this.notLoadedRuleBookResultsTypeMappings = notLoadedRuleBookResultsTypeMappings;
	}

	public void setNotLoadedRuleBookResultsTypesDependentTables() {
		notLoadedRuleBookResultsTypeMappings = true;
	}
	
	public void setNotLoadedRuleBookEntityTypesDependentTables() {
		notLoadedEntityResultsTypes = true;
		notLoadedRuleBookResultsTypeMappings = true;
	}
	
	public void setNotLoadedAllDependentTables() {
		notLoadedRuleBookResultsTypes = true;
		notLoadedRuleBookEntityTypes = true;
		notLoadedEntityResultsTypes = true;
		notLoadedRuleBookResultsTypeMappings = true;
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
			
			if(startDate!=null || endDate!=null || (ruleBookUserCode!=null && !ruleBookUserCode.equals(""))){
				retVal.append("(");
				retVal.append((ruleBookUserCode!=null && !ruleBookUserCode.equals(""))?(ruleBookUserCode+";"):(""));
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

			StringBuffer retVal = new StringBuffer();
			String zaglavlje = fm.getMessageFromResourceBundle("records.ruleBook.header");
			retVal.append("<u>" + zaglavlje.substring(0, zaglavlje.length()-2) + "</u><br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.ruleBook.editPanel.name")+": ");
			if (getSomeName() != null)
				retVal.append(getSomeName());	
			if(startDate!=null || endDate!=null || (ruleBookUserCode!=null && !ruleBookUserCode.equals(""))){
				retVal.append("(");
				retVal.append((ruleBookUserCode!=null && !ruleBookUserCode.equals(""))?(ruleBookUserCode+";"):(""));
				retVal.append(startDate!=null?(startDate.get(Calendar.YEAR)):("NN"));
				retVal.append(endDate!=null?("-" + endDate.get(Calendar.YEAR)):("-NN"));
				retVal.append(")");
			}
			retVal.append("<br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.ruleBook.editPanel.description")+": ");
			if (description.getContent() != null && locale.getLanguage().equals(description.getLanguage().substring(0,2))){
				retVal.append(description.getContent());
			}
			else
				for (MultilingualContentDTO descriptionTranslation : descriptionTranslations) {
					
					if(locale.getLanguage().equals(descriptionTranslation.getLanguage().substring(0,2)) && descriptionTranslation.getContent()!=null){
						retVal.append(descriptionTranslation.getContent());
						break;
					}	
				}
			if (file != null) {
				retVal.append("<br/>");		
				retVal.append(fm.getMessageFromResourceBundle("records.ruleBook.editPanel.file"));
				
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null){
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				}
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.download") + ": <a target=\"_blank\" href=\"" + filePath + "/DownloadFileServlet/RuleBook" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId() + "\"><img src=\"" + filePath +  "/javax.faces.resource/download.png.jsf?ln=img\" height=\"24\" width=\"24\" alt=\"link\" target=\"_blank\"/></a>");
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
	 * @return the deletedFile
	 */
	public FileDTO getDeletedFile() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return deletedFile;
	}

	/**
	 * @param deletedFile the deletedFile to set
	 */
	public void setDeletedFile(FileDTO deletedFile) {
		this.deletedFile = deletedFile;
	}

	/**
	 * @return the fileURL
	 */
	public String getFileURL() {
		if(notLoaded){
			return fileURL;
		} 
		else {
			if(record.getFiles()!= null && !record.getFiles().isEmpty()){
				FileDTO file = record.getFiles().get(0);
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null)
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				return filePath + "/DownloadFileServlet/RuleBook" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId();
			} else 
				return "";
		}
	}

	/**
	 * @param fileURL the fileURL to set
	 */
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		
		notLoaded = true;
		
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		ruleBookUserCode = null;
		resultsTypeGroup = new ResultsTypeGroupDTO();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		relatedRecords = null;

		this.startDate = new GregorianCalendar();
		this.startDate.set(Calendar.YEAR, 1900);
		this.startDate.set(Calendar.MONTH, Calendar.JANUARY);
		this.startDate.set(Calendar.DAY_OF_MONTH, 1);

		this.endDate = new GregorianCalendar();
		this.endDate.set(Calendar.YEAR, 2099);
		this.endDate.set(Calendar.MONTH, Calendar.DECEMBER);
		this.endDate.set(Calendar.DAY_OF_MONTH, 31);

		notLoadedRuleBookResultsTypes = true;
		ruleBookResultsTypes = new ArrayList<RuleBookResultsTypeDTO>();
		notLoadedRuleBookEntityTypes = true;
		ruleBookEntityTypes = new ArrayList<RuleBookEntityTypeDTO>();
		notLoadedEntityResultsTypes = true;
		entityResultsTypes = new ArrayList<EntityResultsTypeDTO>();
		notLoadedRuleBookResultsTypeMappings = true;
		ruleBookResultsTypeMappings = new ArrayList<RuleBookResultsTypeMappingDTO>();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}	
	
	public RuleBookResultsTypeDTO getRuleBookResultsTypeByResultsType(ResultsTypeDTO resultTypeClass){
		RuleBookResultsTypeDTO retVal = null;
		
		for (RuleBookResultsTypeDTO el : getRuleBookResultsTypes()) {
			if(el.getResultsType().equals(resultTypeClass)){
				retVal = el;
				break;
			}	
		}
		return retVal;
	}
	
	public RuleBookEntityTypeDTO getRuleBookEntityTypeByEntityType(ClassDTO entityTypeClass){
		RuleBookEntityTypeDTO retVal = null;
		
		for (RuleBookEntityTypeDTO el : getRuleBookEntityTypes()) {
			if(el.getEntityType().equals(entityTypeClass)){
				retVal = el;
				break;
			}	
		}
		return retVal;
	}
	
	public EntityResultsTypeDTO getEntityResultsTypeByResultsTypeAndEntityType(ResultsTypeDTO resultTypeClass, ClassDTO entityTypeClass){
		EntityResultsTypeDTO retVal = null;
		
		for (EntityResultsTypeDTO ert : getEntityResultsTypes()) {
			if(ert.getResultsType().equals(resultTypeClass) && ert.getEntityType().equals(entityTypeClass)){
				retVal = ert;
				break;
			}	
		}
		return retVal;
	}
	
	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMappingByMappingId(Integer mappingId){
		RuleBookResultsTypeMappingDTO retVal = null;
		
		for (RuleBookResultsTypeMappingDTO rrtm : getRuleBookResultsTypeMappings()) {
			if(rrtm.getMappingId().intValue()==mappingId.intValue()){
				retVal = rrtm;
				break;
			}	
		}
		return retVal;
	}
	
	public RuleBookResultsTypeMappingDTO getRuleBookResultsTypeMappingByAllParms(ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeForResearcherRole, ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource){
		RuleBookResultsTypeMappingDTO retVal = null;
		
		for (RuleBookResultsTypeMappingDTO rrtm : getRuleBookResultsTypeMappings()) {
			if( ((rrtm.getResearcherRole() != null) && (rrtm.getResearcherRole().equals(researcherRole))) && 
					((rrtm.getEntityType() != null) && (rrtm.getEntityType().equals(entityType))) &&
					((rrtm.getEntitySourceType() == entitySourceType ) || ((rrtm.getEntitySourceType() != null) && (rrtm.getEntitySourceType().equals(entitySourceType)))) &&
					((rrtm.getResultsTypeForResearcherRole() != null) && (rrtm.getResultsTypeForResearcherRole().equals(resultsTypeForResearcherRole))) &&
					((rrtm.getResultsTypeOfObsEntity() == resultsTypeOfObsEntity) || ((rrtm.getResultsTypeOfObsEntity() != null) && (rrtm.getResultsTypeOfObsEntity().equals(resultsTypeOfObsEntity)))) &&
					((rrtm.getResultsTypeOfObsEntitySource() == resultsTypeOfObsEntitySource) || ((rrtm.getResultsTypeOfObsEntitySource() != null) && (rrtm.getResultsTypeOfObsEntitySource().equals(resultsTypeOfObsEntitySource))))
					){
				retVal = rrtm;
				break;
			}	
		}
		return retVal;
	}
	
	public ResultsTypeDTO getResultsTypeForResearcherRole(ClassDTO researcherRole, ClassDTO entityType, ClassDTO entitySourceType, 
			ResultsTypeDTO resultsTypeOfObsEntity, ResultsTypeDTO resultsTypeOfObsEntitySource){
		ResultsTypeDTO retVal = null;
		
		for (RuleBookResultsTypeMappingDTO rrtm : getRuleBookResultsTypeMappings()) {
			if( ((rrtm.getResearcherRole() != null) && (rrtm.getResearcherRole().equals(researcherRole))) && 
					((rrtm.getEntityType() != null) && (rrtm.getEntityType().equals(entityType))) &&
					((rrtm.getEntitySourceType() == entitySourceType ) || ((rrtm.getEntitySourceType() != null) && (rrtm.getEntitySourceType().equals(entitySourceType)))) &&
					((rrtm.getResultsTypeOfObsEntity() == resultsTypeOfObsEntity) || ((rrtm.getResultsTypeOfObsEntity() != null) && (rrtm.getResultsTypeOfObsEntity().equals(resultsTypeOfObsEntity)))) &&
					((rrtm.getResultsTypeOfObsEntitySource() == resultsTypeOfObsEntitySource) || ((rrtm.getResultsTypeOfObsEntitySource() != null) && (rrtm.getResultsTypeOfObsEntitySource().equals(resultsTypeOfObsEntitySource))))
					){
				retVal = rrtm.getResultsTypeForResearcherRole();
				break;
			}	
		}
		return retVal;
	}
	
	public void loadAllRuleBookResultsTypesFromDatabase(){
		RuleBookResultsTypeDAO dao = new RuleBookResultsTypeDAO();
		ruleBookResultsTypes.clear();
		ruleBookResultsTypes.addAll(dao.getAllRuleBookResultsTypes(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("resultsType");
		directionsList.add("asc");
		Collections.sort(ruleBookResultsTypes, new GenericComparator<RuleBookResultsTypeDTO>(
				orderByList, directionsList));
		
		notLoadedRuleBookResultsTypes = false;
	}
	
	public void loadAllRuleBookEntityTypesFromDatabase(){
		RuleBookEntityTypeDAO dao = new RuleBookEntityTypeDAO();
		ruleBookEntityTypes.clear();
		ruleBookEntityTypes.addAll(dao.getAllRuleBookEntityTypes(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("entityType");
		directionsList.add("asc");
		Collections.sort(ruleBookEntityTypes, new GenericComparator<RuleBookEntityTypeDTO>(
				orderByList, directionsList));
		
		notLoadedRuleBookEntityTypes = false;
	}

	public void loadAllEntityResultsTypesFromDatabase(){
		EntityResultsTypeDAO dao = new EntityResultsTypeDAO();
		entityResultsTypes.clear();
		entityResultsTypes.addAll(dao.getAllEntityResultsType(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("entityType");
		orderByList.add("resultsType");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(entityResultsTypes, new GenericComparator<EntityResultsTypeDTO>(
				orderByList, directionsList));
		
		notLoadedEntityResultsTypes = false;
	}
	
	public void loadAllRuleBookResultsTypeMappingsFromDatabase(){
		RuleBookResultsTypeMappingDAO dao = new RuleBookResultsTypeMappingDAO();
		ruleBookResultsTypeMappings.clear();
		ruleBookResultsTypeMappings.addAll(dao.getAllResultsTypeMappingsByRuleBook(this));
		
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		
		orderByList.add("researcherRole");
		orderByList.add("entityType");
		orderByList.add("entitySourceType");
		directionsList.add("asc");
		directionsList.add("asc");
		directionsList.add("asc");
		Collections.sort(ruleBookResultsTypeMappings, new GenericComparator<RuleBookResultsTypeMappingDTO>(
				orderByList, directionsList));
		
		notLoadedRuleBookResultsTypeMappings = false;
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
