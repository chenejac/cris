package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic mARC21Record with data about proceedings.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ProceedingsDTO extends PublicationDTO {

	private ConferenceDTO conference;
	private String conferenceName = "";
	private MultilingualContentDTO title;
	private String someTitle = "";
	private List<MultilingualContentDTO> titleTranslations;
	private MultilingualContentDTO subtitle;
	private List<MultilingualContentDTO> subtitleTranslations;
	private String isbn = "";
	private Integer numberOfPages;
	private String dimension;
	private String editionTitle;
	private Integer editionNumber;
	private List<AuthorDTO> editors;
	private PublisherDTO publisher;
	private List<String> languages;
	private MultilingualContentDTO nameAbbreviation;
	private List<MultilingualContentDTO> nameAbbreviationTranslations;
	private MultilingualContentDTO note;
	private List<MultilingualContentDTO> noteTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private String uri;
	private String doi;

	public ProceedingsDTO() {
		super();
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		conference = new ConferenceDTO();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		nameAbbreviationTranslations = new ArrayList<MultilingualContentDTO>();
		nameAbbreviation = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public ProceedingsDTO(String controlNumber) {
		super(controlNumber);
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		conference = new ConferenceDTO();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		nameAbbreviationTranslations = new ArrayList<MultilingualContentDTO>();
		nameAbbreviation = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	
	/**
	 * @param controlNumber
	 * @param conference
	 * @param title
	 * @param titleTranslations
	 * @param subtitle
	 * @param subtitleTranslations
	 * @param isbn
	 * @param numberOfPages
	 * @param dimension
	 * @param editionTitle
	 * @param editionNumber
	 * @param editors
	 * @param publisher
	 * @param publicationYear
	 * @param languages
	 * @param nameAbbreviation
	 * @param nameAbbreviationTranslations
	 * @param note
	 * @param noteTranslations
	 * @param keywords
	 * @param keywordsTranslations
	 * @param uri
	 * @param doi
	 */
	public ProceedingsDTO(String controlNumber, ConferenceDTO conference,
			MultilingualContentDTO title, List<MultilingualContentDTO> titleTranslations,
			MultilingualContentDTO subtitle, List<MultilingualContentDTO> subtitleTranslations,
			String isbn, Integer numberOfPages, String dimension,
			String editionTitle, Integer editionNumber,
			List<AuthorDTO> editors, PublisherDTO publisher,
			String publicationYear, List<String> languages,
			MultilingualContentDTO nameAbbreviation, List<MultilingualContentDTO> nameAbbreviationTranslations, 
			MultilingualContentDTO note, List<MultilingualContentDTO> noteTranslations, MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations,  String uri, String doi) {
		super(controlNumber, publicationYear);
		this.conference = conference;
		this.title = title;
		this.titleTranslations = titleTranslations;
		this.subtitle = subtitle;
		this.subtitleTranslations = subtitleTranslations;
		this.isbn = isbn;
		this.numberOfPages = numberOfPages;
		this.dimension = dimension;
		this.editionTitle = editionTitle;
		this.editionNumber = editionNumber;
		this.editors = editors;
		this.publisher = publisher;
		this.languages = languages;
		this.nameAbbreviation = nameAbbreviation;
		this.nameAbbreviationTranslations = nameAbbreviationTranslations;
		this.note = note;
		this.noteTranslations = noteTranslations;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.uri = uri;
		this.doi = doi;
	}

	/**
	 * @return the ALL authors
	 */
	@Override
	public List<AuthorDTO> getAllAuthors() {
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		return retVal;
	}
	
	/**
	 * @return the conference
	 */
	public ConferenceDTO getConference() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return conference;
	}

	/**
	 * @param conference
	 *            the conference to set
	 */
	public void setConference(ConferenceDTO conference) {
		this.conference = conference;
		if((publicationYear == null) && (conference != null)){
			publicationYear = conference.getYear().toString();
		}
	}

	/**
	 * @return the conferenceName
	 */
	public String getConferenceName() {
		if(notLoaded) 
			return conferenceName;
		else 
			return conference.getStringRepresentation();
	}

	/**
	 * @param conferenceName the conferenceName to set
	 */
	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	/**
	 * @return the title
	 */
	public MultilingualContentDTO getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(MultilingualContentDTO title) {
		this.title = title;
	}
	
	/**
	 * @return original title or first translated title (if original title is not defined)
	 */
	public String getSomeTitle(){
		if(notLoaded) 
			return someTitle;
		else {
			String retVal = null;
			if (title.getContent() != null)
				retVal = title.getContent();
			else if (titleTranslations.size()>0)
					retVal = titleTranslations.get(0).getContent();
			return retVal;
		}
	}

	/**
	 * @param someTitle the someTitle to set
	 */
	public void setSomeTitle(String someTitle) {
		this.someTitle = someTitle;
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
	 * @return the subtitle
	 */
	public MultilingualContentDTO getSubtitle() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return subtitle;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(MultilingualContentDTO subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the subtitleTranslations
	 */
	public List<MultilingualContentDTO> getSubtitleTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return subtitleTranslations;
	}

	/**
	 * @param subtitleTranslations the subtitleTranslations to set
	 */
	public void setSubtitleTranslations(
			List<MultilingualContentDTO> subtitleTranslations) {
		this.subtitleTranslations = subtitleTranslations;
	}

	/**
	 * @return the ISBN
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn
	 *            the ISBN to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the number of pages
	 */
	public Integer getNumberOfPages() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return numberOfPages;
	}

	/**
	 * @param numberOfPages
	 *            the number of pages to set
	 */
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * @return the dimension
	 */
	public String getDimension() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return dimension;
	}

	/**
	 * @param dimension
	 *            the dimension to set
	 */
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return the editionTitle
	 */
	public String getEditionTitle() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return editionTitle;
	}

	/**
	 * @param editionTitle
	 *            the edition title to set
	 */
	public void setEditionTitle(String editionTitle) {
		this.editionTitle = editionTitle;
	}

	/**
	 * @return the editionNumber
	 */
	public Integer getEditionNumber() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return editionNumber;
	}

	/**
	 * @param editionNumber
	 *            the edition number to set
	 */
	public void setEditionNumber(Integer editionNumber) {
		this.editionNumber = editionNumber;
	}

	/**
	 * @return the editors
	 */
	@Override
	public List<AuthorDTO> getEditors() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return editors;
	}

	/**
	 * @param editors
	 *            the editors to set
	 */
	public void setEditors(List<AuthorDTO> editors) {
		this.editors = editors;
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
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(PublisherDTO publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the languages
	 */
	public List<String> getLanguages() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return languages;
	}

	/**
	 * @return the list of the localized languages 
	 */
	public List<String> getLocalizedLanguages() {
		List<String> retVal = new ArrayList<String>();
		List<String> allLanguages = getLanguages();
		for (String language : allLanguages) {
			String ll = new FacesMessages("messages.messages-records", FacesContext
							.getCurrentInstance().getViewRoot().getLocale())
							.getMessageFromResourceBundle("records.language."
									+ language);
			retVal.add(ll);
		}
		return retVal;
	}

	/**
	 * @param languages
	 *            the languages to set
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	/**
	 * @return the nameAbbreviation
	 */
	public MultilingualContentDTO getNameAbbreviation() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return nameAbbreviation;
	}

	/**
	 * @param nameAbbreviation the nameAbbreviation to set
	 */
	public void setNameAbbreviation(MultilingualContentDTO nameAbbreviation) {
		this.nameAbbreviation = nameAbbreviation;
	}

	/**
	 * @return the nameAbbreviationTranslations
	 */
	public List<MultilingualContentDTO> getNameAbbreviationTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return nameAbbreviationTranslations;
	}

	/**
	 * @param nameAbbreviationTranslations the nameAbbreviationTranslations to set
	 */
	public void setNameAbbreviationTranslations(
			List<MultilingualContentDTO> nameAbbreviationTranslations) {
		this.nameAbbreviationTranslations = nameAbbreviationTranslations;
	}

	/**
	 * @return the note
	 */
	public MultilingualContentDTO getNote() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(MultilingualContentDTO note) {
		this.note = note;
	}

	/**
	 * @return the noteTranslations
	 */
	public List<MultilingualContentDTO> getNoteTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return noteTranslations;
	}

	/**
	 * @param noteTranslations the noteTranslations to set
	 */
	public void setNoteTranslations(List<MultilingualContentDTO> noteTranslations) {
		this.noteTranslations = noteTranslations;
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
			if (title.getContent() != null)
				retVal.append(title.getContent() + ", ");
			else if (titleTranslations.size()>0)
					retVal.append("" + titleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			else
				retVal.append("Title, ");
			if (subtitle.getContent() != null)
				retVal.append(subtitle.getContent() + ", ");
			else if (subtitleTranslations.size()>0)
				retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			if (publicationYear != null) 
				retVal.append(publicationYear);
			else
				retVal.append("publishYear");
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
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.proceedings.header") + "</u><br/>");
			if (getSomeTitle() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.proceedings.editPanel.title") + ": "+ getSomeTitle() + "<br/>");
			else
				retVal.append("Proceedings title <br/>");
			
			if ((subtitle.getContent() != null) && (!"".equals(subtitle.getContent().trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.proceedings.editPanel.subtitle") + ": "+ subtitle.getContent() + "<br/>");
			
			if ((isbn != null) && (!"".equals(isbn.trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.proceedings.editPanel.isbn") + ": "+ isbn + "<br/>");
			if (getPublicationYear() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.proceedings.editPanel.publicationYear") + ": " + getPublicationYear());
			
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
		retVal.append(indent + "<proceedings>\n");
		if(controlNumber != null)
			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
		if(getSomeTitle() != null)
			retVal.append(indent + "\t<title>" + getSomeTitle() + "</title>\n");
		if(publicationYear != null)
			retVal.append(indent + "\t<year>" + publicationYear + "</year>\n");
		if((isbn != null) && (isbn.trim().length() > 0))
			retVal.append(indent + "\t<isbn>" + isbn + "</isbn>\n");
		if((publisher != null) && (publisher.toString().trim().length() > 0))
			retVal.append(indent + "\t<publisher>" + publisher.toString() + "</publisher>\n");
		if(doi != null)
			retVal.append(indent + "\t<doi>" + doi + "</doi>\n");
//		if(conference != null)
//			retVal.append(conference.getKnrXML(indent + "\t", null));
		List<AuthorDTO> editors = getEditors();
		if(editors.size() > 0){
			retVal.append(indent + "\t<editors>\n");
			for (AuthorDTO editor : editors) {
				retVal.append(editor.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</editors>\n");
		}
		retVal.append(indent + "</proceedings>\n");
		return retVal.toString();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		conference = new ConferenceDTO();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		nameAbbreviationTranslations = new ArrayList<MultilingualContentDTO>();
		nameAbbreviation = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
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
		// TODO Auto-generated method stub
		return this.toString();
	}	
	
	

}
