package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic mARC21Record with data about patent.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PatentDTO extends PublicationDTO {

	private MultilingualContentDTO title;
	private List<MultilingualContentDTO> titleTranslations;
	private String number;
	private PublisherDTO publisher;
//	private InstitutionDTO institution;
//	private String place;
//	private String state;
//	private Calendar registrationDate;
//	private Calendar approvmentDate;
	private AuthorDTO mainAuthor;
	private List<AuthorDTO> otherAuthors;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO abstracT;
	private List<MultilingualContentDTO> abstractTranslations;
	private String uri;

	public PatentDTO() {
		super();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
//		institution = new InstitutionDTO();
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		abstractTranslations = new ArrayList<MultilingualContentDTO>();
		abstracT = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public PatentDTO(String controlNumber) {
		super(controlNumber);
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
//		institution = new InstitutionDTO();
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		abstractTranslations = new ArrayList<MultilingualContentDTO>();
		abstracT = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 * @param title
	 * @param titleTranslations
	 * @param number
	 * @param publisher
	 * @param mainAuthor
	 * @param otherAuthors
	 * @param publicationYear
	 * @param keywords
	 * @param keywordsTranslations
	 * @param abstracT
	 * @param abstractTranslations
	 * @param uri
	 * @param doi
	 */
	public PatentDTO(String controlNumber, MultilingualContentDTO title,  
			List<MultilingualContentDTO> titleTranslations, String number, 
			PublisherDTO publisher, AuthorDTO mainAuthor, List<AuthorDTO> otherAuthors, String publicationYear,
			MultilingualContentDTO keywords, List<MultilingualContentDTO> keywordsTranslations, 
			MultilingualContentDTO abstracT, List<MultilingualContentDTO> abstractTranslations, String uri, String doi) {
		super(controlNumber, publicationYear);
		this.title = title;
		this.titleTranslations = titleTranslations;
		this.number = number;
		this.publisher = publisher;
//		this.place = place;
//		this.state = state;
		this.mainAuthor = mainAuthor;
		this.otherAuthors = otherAuthors;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.abstracT = abstracT;
		this.abstractTranslations = abstractTranslations;
		this.uri = uri;
		this.doi = doi;
	}

	/**
	 * @return the title
	 */
	public MultilingualContentDTO getTitle() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(MultilingualContentDTO title) {
		this.title = title;
	}

	/**
	 * @return the titleTranslations
	 */
	public List<MultilingualContentDTO> getTitleTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return titleTranslations;
	}

	/**
	 * @param titleTranslations the titleTranslations to set
	 */
	public void setTitleTranslations(List<MultilingualContentDTO> titleTranslations) {
		this.titleTranslations = titleTranslations;
	}
	
	/**
	 * @return original title or first translated title (if original title is not defined)
	 */
	public String getSomeTitle(){
		if(notLoaded){
			record.loadFromDatabase();
		}
		String retVal = null;
		if (title.getContent() != null)
			retVal = title.getContent();
		else if (titleTranslations.size()>0)
				retVal = titleTranslations.get(0).getContent();
		return retVal;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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

//	/**
//	 * @return the institution
//	 */
//	public InstitutionDTO getInstitution() {
//		return institution;
//	}
//
//	/**
//	 * @param institution the institution to set
//	 */
//	public void setInstitution(InstitutionDTO institution) {
//		this.institution = institution;
//	}


//	/**
//	 * @return the place
//	 */
//	public String getPlace() {
//		return place;
//	}
//
//	/**
//	 * @param place the place to set
//	 */
//	public void setPlace(String place) {
//		this.place = place;
//	}
//
//	/**
//	 * @return the state
//	 */
//	public String getState() {
//		return state;
//	}
//
//	/**
//	 * @param state the state to set
//	 */
//	public void setState(String state) {
//		this.state = state;
//	}

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
	 * @return the abstracT
	 */
	public MultilingualContentDTO getAbstracT() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return abstracT;
	}

	/**
	 * @param abstracT the abstracT to set
	 */
	public void setAbstracT(MultilingualContentDTO abstracT) {
		this.abstracT = abstracT;
	}

	/**
	 * @return the abstractTranslations
	 */
	public List<MultilingualContentDTO> getAbstractTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return abstractTranslations;
	}

	/**
	 * @param abstractTranslations the abstractTranslations to set
	 */
	public void setAbstractTranslations(
			List<MultilingualContentDTO> abstractTranslations) {
		this.abstractTranslations = abstractTranslations;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(notLoaded)
			return stringRepresentation;
		else {
			StringBuffer retVal = new StringBuffer();
			if ((mainAuthor != null) && (mainAuthor.getControlNumber() != null))
				retVal.append(mainAuthor.getName() + ", ");
			for (AuthorDTO author : otherAuthors) {
				retVal.append(author.getName() + ", ");
			}
			if (title.getContent() != null)
				retVal.append(title.getContent() + ", ");
			else if (titleTranslations.size()>0)
					retVal.append("" + titleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			else
				retVal.append("Title, ");
			if (publisher.getOriginalPublisher().getName() != null) 
				retVal.append(publisher.getOriginalPublisher() + ", ");
			else if (publisher.getPublisherTranslations().size() > 0)
				retVal.append(publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
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
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.bibliography.patent") + "</u><br/>");
			if ((mainAuthor != null) && (mainAuthor.getControlNumber() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.authors") + ": " + mainAuthor.getName());
			else
				retVal.append("mainAuthor, ");
			for (AuthorDTO author : otherAuthors) {
				retVal.append(", " + author.getName());
			}
			retVal.append("<br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.titleLanguage") + ": " + fm.getMessageFromResourceBundle("records.language." + title.getLanguage()) + "<br/>"); 
			if (title.getContent() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.title") + ": <i>" + title.getContent() + "</i><br/>");
			else if (titleTranslations.size()>0){
					retVal.append(fm.getMessageFromResourceBundle("records.bibliography.title") + ": " + fm.getMessageFromResourceBundle("records.bibliography.notDefined") + "<br/>");
					retVal.append(fm.getMessageFromResourceBundle("records.bibliography.translatedTitle")+": <i>" + titleTranslations.get(0).getContent() + "</i><br/>");
				}
			else
				retVal.append("Title, ");
			if (number != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.number") + ": " + number + "<br/>");
			if (publisher.getOriginalPublisher().getName() != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.patent.publisher") + ": " + publisher.getOriginalPublisher() + "<br/>");
			else if (publisher.getPublisherTranslations().size() > 0)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.patent.publisher")+": <i>" + publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ ")</i><br/>");
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
		retVal.append(indent + "<patent>\n");
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
		if(getSomeTitle() != null)
			retVal.append(indent + "\t<title>" + getSomeTitle() + "</title>\n");
		if(publicationYear != null)
			retVal.append(indent + "\t<year>" + publicationYear + "</year>\n");
		if(number != null)
			retVal.append(indent + "\t<number>" + number + "</number>\n");
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
		retVal.append(indent + "</patent>\n");
		return retVal.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		publisher = new PublisherDTO();
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		abstractTranslations = new ArrayList<MultilingualContentDTO>();
		abstracT = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
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
		if (title.getContent() != null)
			retVal.append(title.getContent() + ", ");
		else if (titleTranslations.size()>0)
				retVal.append("" + titleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
		else
			retVal.append("Title, ");
		if (publisher.getOriginalPublisher().getName() != null) 
			retVal.append(publisher.getOriginalPublisher() + ", ");
		else if (publisher.getPublisherTranslations().size() > 0)
			retVal.append(publisher.getPublisherTranslations().get(0) + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
		if (publicationYear != null) 
			retVal.append(publicationYear);
		else
			retVal.append("publicationYear");
		return retVal.toString();
	}	
	
	
	
}
