package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic mARC21Record with data about product.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ProductDTO extends PublicationDTO {

	private MultilingualContentDTO name;
	private List<MultilingualContentDTO> nameTranslations;
	private String internalNumber;
	private PublisherDTO publisher;
	private AuthorDTO mainAuthor;
	private List<AuthorDTO> otherAuthors;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO description;
	private List<MultilingualContentDTO> descriptionTranslations;
	private String uri;
	private String doi;

	public ProductDTO() {
		super();
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public ProductDTO(String controlNumber) {
		super(controlNumber);
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 * @param title
	 * @param titleLanguage
	 * @param titleTranslations
	 * @param subtitle
	 * @param subtitleLanguage
	 * @param subtitleTranslations
	 * @param volumeCode
	 * @param volumeTitle
	 * @param isbn
	 * @param numberOfPages
	 * @param dimension
	 * @param count
	 * @param editionTitle
	 * @param editionNumber
	 * @param mainAuthor
	 * @param otherAuthors
	 * @param editors
	 * @param publisher
	 * @param publicationYear
	 * @param translated
	 * @param languages
	 * @param originalLanguages
	 * @param note
	 * @param keywords
	 * @param uri
	 * @param doi
	 */
	public ProductDTO(String controlNumber, MultilingualContentDTO name,  
			List<MultilingualContentDTO> nameTranslations, String internalNumber, PublisherDTO publisher,
			AuthorDTO mainAuthor, List<AuthorDTO> otherAuthors, String publicationYear,
			MultilingualContentDTO keywords, List<MultilingualContentDTO> keywordsTranslations, 
			MultilingualContentDTO description, List<MultilingualContentDTO> descriptionTranslations, String uri, String doi) {
		super(controlNumber, publicationYear);
		this.name = name;
		this.nameTranslations = nameTranslations;
		this.internalNumber = internalNumber;
		this.publisher = publisher;
		this.mainAuthor = mainAuthor;
		this.otherAuthors = otherAuthors;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.description = description;
		this.descriptionTranslations = descriptionTranslations;
		this.uri = uri;
		this.doi = doi;
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
	 * @return original name or first translated name (if original name is not defined)
	 */
	public String getSomeName(){
		if(notLoaded){
			record.loadFromDatabase();
		}
		String retVal = null;
		if (name.getContent() != null)
			retVal = name.getContent();
		else if (nameTranslations.size()>0)
				retVal = nameTranslations.get(0).getContent();
		return retVal;
	}

	/**
	 * @return the internalNumber
	 */
	public String getInternalNumber() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return internalNumber;
	}

	/**
	 * @param internalNumber the internalNumber to set
	 */
	public void setInternalNumber(String internalNumber) {
		this.internalNumber = internalNumber;
	}

	/**
	 * @return the publisher
	 */
	public PublisherDTO getPublisher() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(PublisherDTO publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the mainAuthor
	 */
	public AuthorDTO getMainAuthor() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return mainAuthor;
	}

	/**
	 * @param mainAuthor
	 *            the mainAuthor to set
	 */
	public void setMainAuthor(AuthorDTO mainAuthor) {
		this.mainAuthor = mainAuthor;
	}

	/**
	 * @return the otherAuthors
	 */
	public List<AuthorDTO> getOtherAuthors() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return otherAuthors;
	}

	/**
	 * @param otherAuthors
	 *            the otherAuthors to set
	 */
	public void setOtherAuthors(List<AuthorDTO> otherAuthors) {
		this.otherAuthors = otherAuthors;
	}
	
	/**
	 * @return the ALL authors
	 */
	@Override
	public List<AuthorDTO> getAllAuthors() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		if ((mainAuthor.getControlNumber() != null)
				&& (!("".equals(mainAuthor.getControlNumber()))))
			retVal.add(mainAuthor);
		retVal.addAll(otherAuthors);
		return retVal;
	}

	/**
	 * @return string representation of ALL authors
	 */
	public String getStringAuthors() {
		StringBuffer retVal = new StringBuffer();
		for (AuthorDTO author : getAllAuthors()) {
			retVal.append(author.getName() + ";");
		}
		return retVal.toString();
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
	 * @return the uri
	 */
	public String getUri() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
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

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(notLoaded)
			return stringRepresentation;
		else{
			StringBuffer retVal = new StringBuffer();
			if ((mainAuthor != null) && (mainAuthor.getControlNumber() != null))
				retVal.append(mainAuthor.getName() + ", ");
			for (AuthorDTO author : otherAuthors) {
				retVal.append(author.getName() + ", ");
			}
			if (name.getContent() != null)
				retVal.append(name.getContent() + ", ");
			else if (nameTranslations.size()>0)
					retVal.append("" + nameTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ "), ");
			else
				retVal.append("Name, ");
			if (publisher.getOriginalPublisher().getName() != null) 
				retVal.append(publisher.getOriginalPublisher() + ", ");
			else if (publisher.getPublisherTranslations().size() > 0)
				retVal.append(publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ "), ");
			if (publicationYear != null) 
				retVal.append(publicationYear);
			else
				retVal.append("publicationYear");
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO#getPublicationYear()
	 */
	@Override
	public String getPublicationYear() {
		return super.getPublicationYear();
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
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.bibliography.product") + "</u><br/>");
			if ((mainAuthor != null) && (mainAuthor.getControlNumber() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.authors") + ": " + mainAuthor.getName());
			else
				retVal.append("mainAuthor, ");
			for (AuthorDTO author : otherAuthors) {
				retVal.append(", " + author.getName());
			}
			retVal.append("<br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.nameLanguage") + ": " + fm.getMessageFromResourceBundle("records.language." + name.getLanguage()) + "<br/>"); 
			if (name.getContent() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.name") + ": <i>" + name.getContent() + "</i><br/>");
			else if (nameTranslations.size()>0){
					retVal.append(fm.getMessageFromResourceBundle("records.bibliography.name") + ": " + fm.getMessageFromResourceBundle("records.bibliography.notDefined") + "<br/>");
					retVal.append(fm.getMessageFromResourceBundle("records.bibliography.translatedName")+": <i>" + nameTranslations.get(0).getContent() + "</i><br/>");
				}
			else
				retVal.append("Name, ");
			if (publisher.getOriginalPublisher().getName() != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.product.publisher") + ": " + publisher.getOriginalPublisher() + "<br/>");
			else if (publisher.getPublisherTranslations().size() > 0)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.product.publisher")+": <i>" + publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ ")</i><br/>");
			if (publicationYear != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.publicationYear") + ": " + publicationYear);
			else
				retVal.append("Godina");		
			
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
		retVal.append(indent + "<product>\n");
		if(controlNumber != null)
			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
		if(resultMeasure != null){
			retVal.append(indent + "\t<evaluation>\n");
			if((resultMeasure != null) && (resultMeasure.getCommissionDTO() != null) && (resultMeasure.getCommissionDTO().getAppointmentBoard() != null)) 
				retVal.append(indent + "\t\t<commission>" + resultMeasure.getCommissionDTO().getAppointmentBoard() + "</commission>\n");
			if((resultMeasure != null) && (resultMeasure.getResultType() != null))
				retVal.append(indent + "\t\t<type>" + resultMeasure.getResultType().getClassId() + "</type>\n");
			if((resultMeasure != null) && (resultMeasure.getScienceArea() != null))
				retVal.append(indent + "\t\t<sciencesGroup>" + resultMeasure.getScienceArea().getSomeTerm() + "</sciencesGroup>\n");
			if((resultMeasure != null) && (resultMeasure.getCommissionDTO() != null) && (resultMeasure.getCommissionDTO().getScientificField() != null))
				retVal.append(indent + "\t\t<scientificField>" + resultMeasure.getCommissionDTO().getScientificField() + "</scientificField>\n");
			retVal.append(indent + "\t</evaluation>\n");
		}
		if(getSomeName() != null)
			retVal.append(indent + "\t<name>" + getSomeName() + "</name>\n");
		if(publicationYear != null)
			retVal.append(indent + "\t<year>" + publicationYear + "</year>\n");
		if(internalNumber != null)
			retVal.append(indent + "\t<number>" + internalNumber + "</number>\n");
		if((publisher != null) && (publisher.toString().trim().length() > 0))
			retVal.append(indent + "\t<publisher>" + publisher.toString() + "</publisher>\n");
		List<AuthorDTO> authors = getAllAuthors();
		if(authors.size() > 0){
			retVal.append(indent + "\t<authors>\n");
			for (AuthorDTO author : getAllAuthors()) {
				retVal.append(author.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</authors>\n");
		}
		retVal.append(indent + "</products>\n");
		return retVal.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		descriptionTranslations = new ArrayList<MultilingualContentDTO>();
		description = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		relatedRecords = null;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#toString(java.lang.String)
	 */
	@Override
	public String toString(String id) {
		if(notLoaded)
			record.loadFromDatabase();
		
		StringBuffer retVal = new StringBuffer();
		if ((mainAuthor != null) && (mainAuthor.getControlNumber() != null)){
			if((mainAuthor.getInstitution()!=null) && (mainAuthor.getInstitution().getControlNumber() != null) && (mainAuthor.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("<b>");
			}
			retVal.append(mainAuthor.getName()); 
			if((mainAuthor.getInstitution()!=null) && (mainAuthor.getInstitution().getControlNumber() != null) && (mainAuthor.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("</b>");
			}
			retVal.append(", ");
		}
		for (AuthorDTO author : otherAuthors) {
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber() != null) && (author.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("<b>");
			}
			retVal.append(author.getName());
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber() != null) && (author.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("</b>");
			}
			retVal.append(", ");
		}
		if (name.getContent() != null)
			retVal.append(name.getContent() + ", ");
		else if (nameTranslations.size()>0)
				retVal.append("" + nameTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ "), ");
		else
			retVal.append("Name, ");
		if (publisher.getOriginalPublisher().getName() != null) 
			retVal.append(publisher.getOriginalPublisher() + ", ");
		else if (publisher.getPublisherTranslations().size() > 0)
			retVal.append(publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ "), ");
		if (publicationYear != null) 
			retVal.append(publicationYear);
		else
			retVal.append("publicationYear");
		return retVal.toString();
	}	
	
	
}
