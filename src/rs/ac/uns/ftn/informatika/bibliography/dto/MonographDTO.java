package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.reports.freemarker.TemplateRunner;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic mARC21Record with data about authors monograph.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class MonographDTO extends PublicationDTO {

	private MultilingualContentDTO title;
	private String someTitle = "";
	private List<MultilingualContentDTO> titleTranslations;
	private MultilingualContentDTO subtitle;
	private List<MultilingualContentDTO> subtitleTranslations;
	private String volumeCode;
	private String volumeTitle;
	private String isbn = "";
	private Integer numberOfPages;
	private String dimension;
	private Integer count;
	private String editionTitle;
	private Integer editionNumber;
	private String editionISSN;
	private String alphabet;
	private AuthorDTO mainAuthor;
	private List<AuthorDTO> otherAuthors;
	private List<AuthorDTO> editors;
	private PublisherDTO publisher;
	private Boolean translated;
	private List<String> languages;
	private List<String> originalLanguages;
	private MultilingualContentDTO note;
	private List<MultilingualContentDTO> noteTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO abstracT;
	private List<MultilingualContentDTO> abstractTranslations;
//	private String uri = "";
	private String doi;
	
	private ResearchAreaDTO researchArea;
	
	private MonographEvaluationDataDTO evaluationData;

	public MonographDTO() {
		super();
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		originalLanguages = new ArrayList<String>();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		abstractTranslations = new ArrayList<MultilingualContentDTO>();
		abstracT = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		researchArea = new ResearchAreaDTO();
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public MonographDTO(String controlNumber) {
		super(controlNumber);
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		originalLanguages = new ArrayList<String>();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		abstractTranslations = new ArrayList<MultilingualContentDTO>();
		abstracT = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		researchArea = new ResearchAreaDTO();
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
	 * @param editionISSN
	 * @param alphabet
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
	 * @param researchArea
	 */
	public MonographDTO(String controlNumber, MultilingualContentDTO title,  
			List<MultilingualContentDTO> titleTranslations, MultilingualContentDTO subtitle,
			List<MultilingualContentDTO> subtitleTranslations,
			String volumeCode, String volumeTitle, String isbn,
			Integer numberOfPages, String dimension, Integer count,
			String editionTitle, Integer editionNumber, String editionISSN, String alphabet, AuthorDTO mainAuthor,
			List<AuthorDTO> otherAuthors, List<AuthorDTO> editors,
			PublisherDTO publisher,String publicationYear,
			Boolean translated, List<String> languages,
			List<String> originalLanguages, MultilingualContentDTO note, 
			List<MultilingualContentDTO> noteTranslations, MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations, MultilingualContentDTO abstracT,  
			List<MultilingualContentDTO> abstractTranslations, String uri, String doi, ResearchAreaDTO researchArea) {
		super(controlNumber, publicationYear);
		this.title = title;
		this.titleTranslations = titleTranslations;
		this.subtitle = subtitle;
		this.subtitleTranslations = subtitleTranslations;
		this.volumeCode = volumeCode;
		this.volumeTitle = volumeTitle;
		this.isbn = isbn;
		this.numberOfPages = numberOfPages;
		this.dimension = dimension;
		this.count = count;
		this.editionTitle = editionTitle;
		this.editionNumber = editionNumber;
		this.editionISSN = editionISSN;
		this.alphabet = alphabet;
		this.mainAuthor = mainAuthor;
		this.otherAuthors = otherAuthors;
		this.editors = editors;
		this.publisher = publisher;
		this.translated = translated;
		this.languages = languages;
		this.originalLanguages = originalLanguages;
		this.note = note;
		this.noteTranslations = noteTranslations;
		this.keywords = keywords;
		this.keywordsTranslations = keywordsTranslations;
		this.abstracT = abstracT;
		this.abstractTranslations = abstractTranslations;
		this.uri = uri;
		this.doi = doi;
		this.researchArea = researchArea;
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
	 * @return original title or first translated title (if original title is not defined)
	 */
	public String getSomeTitle(){
		if(notLoaded){
			return someTitle;
		} else {
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
	 * @return the volumeCode
	 */
	public String getVolumeCode() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return volumeCode;
	}

	/**
	 * @param volumeCode
	 *            the volume code to set
	 */
	public void setVolumeCode(String volumeCode) {
		this.volumeCode = volumeCode;
	}

	/**
	 * @return the volumeTitle
	 */
	public String getVolumeTitle() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return volumeTitle;
	}

	/**
	 * @param volumeTitle
	 *            the volume title to set
	 */
	public void setVolumeTitle(String volumeTitle) {
		this.volumeTitle = volumeTitle;
	}

	/**
	 * @return the isbn
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
	 * @return the numberOfPages
	 */
	public Integer getNumberOfPages() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return numberOfPages;
	}

	/**
	 * @param numberOfPages
	 *            the numberOfPages to set
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
	 * @return the count
	 */
	public Integer getCount() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
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
	 *            the editionTitle to set
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
	 *            the editionNumber to set
	 */
	public void setEditionNumber(Integer editionNumber) {
		this.editionNumber = editionNumber;
	}
	
	/**
	 * @return the editionISSN
	 */
	public String getEditionISSN() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return editionISSN;
	}

	/**
	 * @param editionISSN the editionISSN to set
	 */
	public void setEditionISSN(String editionISSN) {
		this.editionISSN = editionISSN;
	}

	/**
	 * @return the alphabet
	 */
	public String getAlphabet() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return alphabet;
	}

	/**
	 * @param alphabet the alphabet to set
	 */
	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
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
	 * @return string representation of ALL editors
	 */
	public String getStringEditors() {
		StringBuffer retVal = new StringBuffer();
		for (AuthorDTO author : getEditors()) {
			retVal.append(author.getName() + ";");
		}
		return retVal.toString();
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
	 * @return is translated
	 */
	public Boolean isTranslated() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return translated;
	}

	/**
	 * @param translated
	 *            the translated to set
	 */
	public void setTranslated(Boolean translated) {
		this.translated = translated;
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
			String ll = new FacesMessages("messages.messages-records",  new Locale("sr")) 
						//FacesContext.getCurrentInstance().getViewRoot().getLocale())
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
	 * @return the originalLanguages
	 */
	public List<String> getOriginalLanguages() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return originalLanguages;
	}
	
	/**
	 * @return the list of the localized original languages 
	 */
	public List<String> getLocalizedOriginalLanguages() {
		List<String> retVal = new ArrayList<String>();
		List<String> allLanguages = getOriginalLanguages();
		for (String language : allLanguages) {
			String ll = new FacesMessages("messages.messages-records",  new Locale("sr")) 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale())
							.getMessageFromResourceBundle("records.language."
									+ language);
			retVal.add(ll);
		}
		return retVal;
	}

	/**
	 * @param originalLanguages
	 *            the original languages to set
	 */
	public void setOriginalLanguages(List<String> originalLanguages) {
		this.originalLanguages = originalLanguages;
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

//	/**
//	 * @return the uri
//	 */
//	public String getUri() {
//		return uri;
//	}
//
//	/**
//	 * @param uri the uri to set
//	 */
//	public void setUri(String uri) {
//		this.uri = uri;
//	}
	
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
	 * @return the researchArea
	 */
	public ResearchAreaDTO getResearchArea() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return researchArea;
	}

	/**
	 * @param researchArea the researchArea to set
	 */
	public void setResearchArea(ResearchAreaDTO researchArea) {
		this.researchArea = researchArea;
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
			if (subtitle.getContent() != null)
				retVal.append(subtitle.getContent() + ", ");
			else if (subtitleTranslations.size()>0)
				retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			if (publisher != null) 
				retVal.append(publisher + ", ");
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getHarvardRepresentation()
	 */
	@Override
	public String getHarvardRepresentation() {
		if(notLoaded)
			return harvardRepresentation;
		else {
			harvardRepresentation = TemplateRunner.getRepresentation(this,TemplateRunner.HARVARD);
			if(harvardRepresentation == null)
				harvardRepresentation = "";
			return harvardRepresentation;
		}
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
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.bibliography.monograph") + "</u><br/>");
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
			if (subtitle.getContent() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.subtitle") + ": " + subtitle.getContent() + "<br/>");
			else if (subtitleTranslations.size()>0){
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.subtitle") + ": " + fm.getMessageFromResourceBundle("records.bibliography.notDefined") + "<br/>");
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.translatedSubtitle") + ": " + subtitleTranslations.get(0).getContent() + "<br/>");
			}
			if (publisher != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.publisher") + ": " + publisher + "<br/>");
			if (publicationYear != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.publicationYear") + ": " + publicationYear);
			else
				retVal.append("Godina");
			
	//		if (volumeCode != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.volumeCode") + ": " + volumeCode + "<br/>");
	//		if (volumeTitle != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.volumeTitle") + ": " + volumeTitle + "<br/>");
	//		if (isbn != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.isbn") + ": " + isbn + "<br/>");
	//		if (numberOfPages != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.numberOfPages") + ": " + numberOfPages + "<br/>");
	//		if (dimension != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.dimension") + ": " + dimension + "<br/>");
	//		if (count != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.count") + ": " + count + "<br/>");
	//		if (editionTitle != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.editionTitle") + ": " + editionTitle + "<br/>");
	//		if (editionNumber != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.editionNumber") + ": " + editionNumber + "<br/>");
	//		if (languages != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.languages") + ": " + languages + "<br/>");
	//		if (originalLanguages != null)
	//			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.originalLanguages") + ": " + subtitle + "<br/>");
	//			
			
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
		retVal.append(indent + "<monograph>\n");
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
		if(numberOfPages != null)
			retVal.append(indent + "\t<numberOfPages>" + numberOfPages + "</numberOfPages>\n");
		if((isbn != null) && (isbn.trim().length() > 0))
			retVal.append(indent + "\t<isbn>" + isbn + "</isbn>\n");
		if((publisher != null) && (publisher.toString().trim().length() > 0))
			retVal.append(indent + "\t<publisher>" + publisher.toString() + "</publisher>\n");
		if(doi != null)
			retVal.append(indent + "\t<doi>" + doi + "</doi>\n");
		List<AuthorDTO> authors = getAllAuthors();
		if(authors.size() > 0){
			retVal.append(indent + "\t<authors>\n");
			for (AuthorDTO author : getAllAuthors()) {
				retVal.append(author.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</authors>\n");
		}
		List<AuthorDTO> editors = getEditors();
		if(editors.size() > 0){
			retVal.append(indent + "\t<editors>\n");
			for (AuthorDTO editor : editors) {
				retVal.append(editor.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</editors>\n");
		}
		retVal.append(indent + "</monograph>\n");
		return retVal.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		mainAuthor = new AuthorDTO();
		otherAuthors = new ArrayList<AuthorDTO>();
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		originalLanguages = new ArrayList<String>();
		publisher = new PublisherDTO();
		titleTranslations = new ArrayList<MultilingualContentDTO>();
		title = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		subtitleTranslations = new ArrayList<MultilingualContentDTO>();
		subtitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
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

	/**
	 * @return the evaluationData
	 */
	public MonographEvaluationDataDTO getEvaluationData() {
		return evaluationData;
	}

	/**
	 * @param evaluationData the evaluationData to set
	 */
	public void setEvaluationData(MonographEvaluationDataDTO evaluationData) {
		this.evaluationData = evaluationData;
	}	
	
	public void loadEvaluationData(){
		
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
		if (subtitle.getContent() != null)
			retVal.append(subtitle.getContent() + ", ");
		else if (subtitleTranslations.size()>0)
			retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
		if (publisher != null) 
			retVal.append(publisher + ", ");
		if (publicationYear != null) 
			retVal.append(publicationYear);
		else
			retVal.append("publicationYear");
		return retVal.toString();

	}
	
}
