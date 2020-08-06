package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic and normative mARC21Record with data about
 * journal. 
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class JournalDTO extends PublicationDTO {

	private MultilingualContentDTO name;
	private String someName = "";
	private List<MultilingualContentDTO> nameTranslations;
	private String issn = "";
	private List<AuthorDTO> editors;
	private List<String> languages;
	private MultilingualContentDTO nameAbbreviation;
	private List<MultilingualContentDTO> nameAbbreviationTranslations;
	private MultilingualContentDTO note;
	private List<MultilingualContentDTO> noteTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private String uri;
	private String doi;

	public JournalDTO() {
		super();
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		nameAbbreviationTranslations = new ArrayList<MultilingualContentDTO>();
		nameAbbreviation = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}

	/**
	 * @param controlNumber
	 *            Control number of the journal
	 */
	public JournalDTO(String controlNumber) {
		super(controlNumber);
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
		nameAbbreviationTranslations = new ArrayList<MultilingualContentDTO>();
		nameAbbreviation = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		noteTranslations = new ArrayList<MultilingualContentDTO>();
		note = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		keywordsTranslations = new ArrayList<MultilingualContentDTO>();
		keywords = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
	}
	
	/**
	 * @param controlNumber
	 * @param name
	 * @param nameTranslations
	 * @param issn
	 * @param editors
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
	public JournalDTO(String controlNumber,
			MultilingualContentDTO name, List<MultilingualContentDTO> nameTranslations, String issn, List<AuthorDTO> editors,
			List<String> languages, MultilingualContentDTO nameAbbreviation, List<MultilingualContentDTO> nameAbbreviationTranslations, 
			MultilingualContentDTO note, List<MultilingualContentDTO> noteTranslations, MultilingualContentDTO keywords,
			List<MultilingualContentDTO> keywordsTranslations, String uri, String doi) {
		super(controlNumber);
		this.name = name;
		this.nameTranslations = nameTranslations;
		this.issn = issn;
		this.editors = editors;
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
		if(notLoaded)
			return someName;
		else {
			String retVal = null;
			if (name.getContent() != null)
				retVal = name.getContent();
			else if (nameTranslations.size()>0)
					retVal = nameTranslations.get(0).getContent();
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
	 * @return the issn
	 */
	public String getIssn() {
		return issn;
	}

	/**
	 * @param issn the issn to set
	 */
	public void setIssn(String issn) {
		this.issn = issn;
	}

	/**
	 * @return the editors
	 */
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
					//FacesContext.getCurrentInstance().getViewRoot().getLocale());
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
		else {
			StringBuffer retVal = new StringBuffer();
			if (name.getContent() == null){
				if(nameTranslations.size()>0)
					retVal.append("" + nameTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ name.getLanguage())+ ")");
			} else
				retVal.append(name.getContent());
			retVal.append((((issn!=null) && (!("".equals(issn))))?(" (ISSN: " + issn + ")"):("")));
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
		if(notLoaded)
			return HTMLRepresentation;
		else { 
			FacesMessages fm= new FacesMessages("messages.messages-records",  new Locale("sr")); 
			//FacesContext.getCurrentInstance().getViewRoot().getLocale());
			StringBuffer retVal = new StringBuffer();
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.journal.header") + "</u><br/>");
			if (getSomeName() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.journal.editPanel.name") + ": "+ getSomeName() + "<br/>");
			else
				retVal.append("journal name <br/>");
			
			if ((issn != null) && (!"".equals(issn.trim())))
				retVal.append(fm.getMessageFromResourceBundle("records.journal.editPanel.issn") + ": "+ issn + "<br/>");
			
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
		retVal.append(indent + "<journal>\n");
		if(controlNumber != null)
			retVal.append(indent + "\t<identifier>" + controlNumber + "</identifier>\n");
		if(getSomeName() != null)
			retVal.append(indent + "\t<title>" + getSomeName() + "</title>\n");
		if(issn != null)
			retVal.append(indent + "\t<issn>" + issn + "</issn>\n");
		retVal.append(indent + "</journal>\n");
		return retVal.toString();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		nameTranslations = new ArrayList<MultilingualContentDTO>();
		name = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		editors = new ArrayList<AuthorDTO>();
		languages = new ArrayList<String>();
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
		return toString();
	}

}
