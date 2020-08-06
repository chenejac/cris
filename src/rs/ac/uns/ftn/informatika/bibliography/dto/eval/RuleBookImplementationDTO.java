package rs.ac.uns.ftn.informatika.bibliography.dto.eval;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class RuleBookImplementationDTO extends RuleBookDTO {

	private RuleBookDTO superRuleBook;
	private RuleBookImplementationDTO superRuleBookImplementation;
	
	/**
	 * 
	 */
	public RuleBookImplementationDTO() {
		super();
	}

	/**
	 * @param controlNumber
	 */
	public RuleBookImplementationDTO(String controlNumber) {
		super(controlNumber);
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
	 * @param superRuleBook
	 * @param superRuleBookImplementation
	 */
	public RuleBookImplementationDTO(String ruleBookUserCode,
			MultilingualContentDTO name, String someName,
			List<MultilingualContentDTO> nameTranslations,
			MultilingualContentDTO description,
			List<MultilingualContentDTO> descriptionTranslations,
			Calendar startDate, Calendar endDate,
			MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations,
			ResultsTypeGroupDTO resultsTypeGroup,
			List<RuleBookResultsTypeDTO> ruleBookResultsTypes,
			List<RuleBookEntityTypeDTO> ruleBookEntityTypes,
			List<EntityResultsTypeDTO> entityResultsTypes,
			RuleBookDTO superRuleBook,
			RuleBookImplementationDTO superRuleBookImplementation, List<RuleBookResultsTypeMappingDTO> ruleBookResultsTypeMappings) {
		super(ruleBookUserCode, name, someName, nameTranslations, description,
				descriptionTranslations, startDate, endDate, keywords,
				keywordsTranslations, resultsTypeGroup, ruleBookResultsTypes,
				ruleBookEntityTypes, entityResultsTypes, ruleBookResultsTypeMappings);
		this.superRuleBook = superRuleBook;
		this.superRuleBookImplementation = superRuleBookImplementation;
	}

	/**
	 * @return the superRuleBook
	 */
	public RuleBookDTO getSuperRuleBook() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return superRuleBook;
	}

	/**
	 * @param superRuleBook the superRuleBook to set
	 */
	public void setSuperRuleBook(RuleBookDTO superRuleBook) {
		this.superRuleBook = superRuleBook;
	}

	/**
	 * @return the superRuleBookImplementation
	 */
	public RuleBookImplementationDTO getSuperRuleBookImplementation() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return superRuleBookImplementation;
	}

	/**
	 * @param superRuleBookImplementation the superRuleBookImplementation to set
	 */
	public void setSuperRuleBookImplementation(
			RuleBookImplementationDTO superRuleBookImplementation) {
		this.superRuleBookImplementation = superRuleBookImplementation;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		if(superRuleBookImplementation != null)
			superRuleBookImplementation.setLocale(locale);
		if(superRuleBook != null)
			superRuleBook.setLocale(locale);
		super.setLocale(locale);
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
			
//			if((superRuleBook != null) && (superRuleBook.getControlNumber()!=null)) 
//				retVal.append(", " + superRuleBook.toString());
//			
//			if((superRuleBookImplementation != null) && (superRuleBookImplementation.getControlNumber()!=null)) 
//				retVal.append(", " + superRuleBookImplementation.toString());
			
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
			String zaglavlje = fm.getMessageFromResourceBundle("records.ruleBookImplementation.header");
			retVal.append("<u>" + zaglavlje + "</u><br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.ruleBook.editPanel.name")+": ");
			if (getSomeName() != null)
				retVal.append(": "+ getSomeName());	
			
			if(startDate!=null || endDate!=null || (ruleBookUserCode!=null && !ruleBookUserCode.equals(""))){
				retVal.append("(");
				retVal.append((ruleBookUserCode!=null && !ruleBookUserCode.equals(""))?(ruleBookUserCode+";"):(""));
				retVal.append(startDate!=null?(startDate.get(Calendar.YEAR)):("NN"));
				retVal.append(endDate!=null?("-" + endDate.get(Calendar.YEAR)):("-NN"));
				retVal.append(")");
			}
			retVal.append("<br/>");
			
			retVal.append(fm.getMessageFromResourceBundle("records.ruleBook.editPanel.description")+": ");
			if (description.getContent() != null && locale.getLanguage().equals(description.getLanguage().substring(0,2)))
				retVal.append(description.getContent());
			else
				for (MultilingualContentDTO descriptionTranslation : descriptionTranslations) {
					if(locale.getLanguage().equals(descriptionTranslation.getLanguage().substring(0,2)) && descriptionTranslation.getContent()!=null){
						retVal.append(descriptionTranslation.getContent());
						break;
					}	
				}
			
			if (file != null) {
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null)
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.download") + ": <a target=\"_blank\" href=\"" + filePath + "/DownloadFileServlet/RuleBook" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId() + "\"><img src=\"" + filePath +  "/javax.faces.resource/download.png.jsf?ln=img\" height=\"24\" width=\"24\" alt=\"link\" target=\"_blank\"/></a>");
			}
			
			retVal.append("<br/>");
			
//			if((superRuleBook != null) && (superRuleBook.getControlNumber()!=null)) 
//				retVal.append("Је имплементациона верзија: " /*fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.institution")*/ + ": "+ superRuleBook.toString() + "<br/>");
//			if((superRuleBookImplementation != null) && (superRuleBookImplementation.getControlNumber()!=null)) 
//				retVal.append("Је имплементациона верзија: "/*fm.getMessageFromResourceBundle("records.organizationUnit.editPanel.superOrganizationUnit")*/ + ": "+ superRuleBookImplementation.toString() + "<br/>");
			
			return retVal.toString();
		}
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = true;
		RecordDTO dto = (RecordDTO) obj;
		try {
			if((controlNumber == null) && (dto.getControlNumber() == null))
				retVal = true;
			else 
				retVal = super.equals(obj);
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}	
	
	/**
	 * @return the fileURL
	 */
	@Override
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
				return filePath + "/DownloadFileServlet/RuleBookImplementation" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId();
			} else 
				return "";
		}
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		superRuleBook = null;
		superRuleBookImplementation = null;
	}
}
