package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.ac.uns.ftn.informatika.bibliography.reports.freemarker.TemplateRunner;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;

/**
 * DTO class which presents bibliographic mARC21Record with data about paper published
 * in journal.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class PaperJournalDTO extends PublicationDTO {

	private MultilingualContentDTO title;
	private List<MultilingualContentDTO> titleTranslations;
	private MultilingualContentDTO subtitle;
	private List<MultilingualContentDTO> subtitleTranslations;
	private String startPage;
	private String endPage;
	private String paperType = "";
	private JournalDTO journal;
	private AuthorDTO mainAuthor;
	private List<AuthorDTO> otherAuthors;
	private String volume;
	private String number;
	private MultilingualContentDTO note;
	private List<MultilingualContentDTO> noteTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO abstracT;
	private List<MultilingualContentDTO> abstractTranslations;
//	private String uri = "";
	private String doi;

	public PaperJournalDTO() {
		super();
		otherAuthors = new ArrayList<AuthorDTO>();
		mainAuthor = new AuthorDTO();
		journal = new JournalDTO();
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
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public PaperJournalDTO(String controlNumber) {
		super();
		this.controlNumber = controlNumber;
		otherAuthors = new ArrayList<AuthorDTO>();
		mainAuthor = new AuthorDTO();
		journal = new JournalDTO();
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
	}

	
	
	/**
	 * @param controlNumber
	 * @param title
	 * @param titleLanguage
	 * @param titleTranslations
	 * @param subtitle
	 * @param subtitleLanguage
	 * @param subtitleTranslations
	 * @param startPage
	 * @param endPage
	 * @param paperType
	 * @param journal
	 * @param mainAuthor
	 * @param otherAuthors
	 * @param volume
	 * @param number
	 * @param publicationYear
	 * @param note
	 * @param keywords
	 * @param abstracT
	 * @param uri
	 * @param doi
	 */
	public PaperJournalDTO(String controlNumber, MultilingualContentDTO title,  
			List<MultilingualContentDTO> titleTranslations, MultilingualContentDTO subtitle,
			List<MultilingualContentDTO> subtitleTranslations,
			String startPage, String endPage, String paperType, 
			JournalDTO journal, AuthorDTO mainAuthor,
			List<AuthorDTO> otherAuthors, String volume, String number,
			String publicationYear, MultilingualContentDTO note,  
			List<MultilingualContentDTO> noteTranslations, MultilingualContentDTO keywords,  
			List<MultilingualContentDTO> keywordsTranslations, MultilingualContentDTO abstracT,  
			List<MultilingualContentDTO> abstractTranslations, String uri, String doi) {
		super(controlNumber, publicationYear);
		this.title = title;
		this.titleTranslations = titleTranslations;
		this.subtitle = subtitle;
		this.subtitleTranslations = subtitleTranslations;
		this.startPage = startPage;
		this.endPage = endPage;
		this.paperType = paperType;
		this.journal = journal;
		this.mainAuthor = mainAuthor;
		this.otherAuthors = otherAuthors;
		this.volume = volume;
		this.number = number;
		this.note = note;
		this.noteTranslations = noteTranslations;
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
//		if(true){
////			record.loadFromDatabase();
//			IObjectProfileNode profile = ObjectProfiler.profile (record);
//        
//			System.out.println ("obj size = " + profile.size () + " bytes");
//			System.out.println (profile.dump ());
//			setNotLoaded(true);
//		}
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
	 * @return the startPage
	 */
	public String getStartPage() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return startPage;
	}

	/**
	 * @param startPage
	 *            the start page to set
	 */
	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	/**
	 * @return the endPage
	 */
	public String getEndPage() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return endPage;
	}

	/**
	 * @param endPage
	 *            the end page to set
	 */
	public void setEndPage(String endPage) {
		this.endPage = endPage;
	}
	
	/**
	 * @return the paperType
	 */
	public String getPaperType() {
		return paperType;
	}
	
	/**
	 * @return the localized paper type
	 */
	public String getLocalizedPaperType() {
		return new FacesMessages("messages.messages-records",  locale) 
		//FacesContext.getCurrentInstance().getViewRoot().getLocale())
						.getMessageFromResourceBundle(paperType);
	}

	/**
	 * @param paperType the paperType to set
	 */
	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	/**
	 * @return the journal
	 */
	public JournalDTO getJournal() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return journal;
	}

	/**
	 * @param journal
	 *            the journal to set
	 */
	public void setJournal(JournalDTO journal) {
		this.journal = journal;
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
	 *            the main author to set
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
	 *            the other authors to set
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
	 * @return the volume
	 */
	public String getVolume() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
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
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
			else
				retVal.append("mainAuthor, ");
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
				retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ subtitle.getLanguage())+ "), ");
			if ((journal != null) && (journal.getControlNumber() != null))
				retVal.append(journal.toString());
			else
				retVal.append("Journal");
			if (volume != null)
				retVal.append(", Vol " + volume);
			if (number != null)
				retVal.append(", No " + number);
			if (publicationYear != null)
				retVal.append(", " + publicationYear);
			if ((startPage != null) && (endPage != null))
				retVal.append(", pp. " + startPage + "-" + endPage + ".");
				
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
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.bibliography.paperJournal") + "</u><br/>");
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
			if ((journal != null) && (journal.getControlNumber() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.journal") + ": "  + journal.toString() + "<br/>");
			else
				retVal.append("Journal");
			if (paperType != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.paperType") + ": " + getLocalizedPaperType() + "<br/>");
			if (volume != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.vol") + ": " + volume + "<br/>");
			if (number != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.no") + ": " + number + "<br/>");
			if ((startPage != null) && (endPage != null))
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.pagination") + ": " + startPage + "-" + endPage + "<br/>");
			if (publicationYear != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.publicationYear") + ": " + publicationYear);
				
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
		retVal.append(indent + "<paperJournal>\n");
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
		if(startPage != null)
			retVal.append(indent + "\t<startPage>" + startPage + "</startPage>\n");
		if(endPage != null)
			retVal.append(indent + "\t<endPage>" + endPage + "</endPage>\n");
		if(volume != null)
			retVal.append(indent + "\t<volume>" + volume + "</volume>\n");
		if(number != null)
			retVal.append(indent + "\t<number>" + number + "</number>\n");
		if(doi != null)
			retVal.append(indent + "\t<doi>" + doi + "</doi>\n");
		if((paperType != null) && (paperType.trim().length() > 0))
			retVal.append(indent + "\t<paperType>" + getLocalizedPaperType() + "</paperType>\n");
		if(journal != null)
			retVal.append(journal.getKnrXML(indent + "\t", null));
		List<AuthorDTO> authors = getAllAuthors();
		if(authors.size() > 0){
			retVal.append(indent + "\t<authors>\n");
			for (AuthorDTO author : authors) {
				retVal.append(author.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</authors>\n");
		}
		retVal.append(indent + "</paperJournal>\n");
		return retVal.toString();
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		otherAuthors = new ArrayList<AuthorDTO>();
		mainAuthor = new AuthorDTO();
		journal = new JournalDTO();
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
			retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ subtitle.getLanguage())+ "), ");
		if ((journal != null) && (journal.getControlNumber() != null))
			retVal.append(journal.toString());
		else
			retVal.append("Journal");
		if (volume != null)
			retVal.append(", Vol " + volume);
		if (number != null)
			retVal.append(", No " + number);
		if (publicationYear != null)
			retVal.append(", " + publicationYear);
		if ((startPage != null) && (endPage != null))
			retVal.append(", pp. " + startPage + "-" + endPage + ".");
			
		return retVal.toString();
	}	
	
	
}
