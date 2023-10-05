 package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringEscapeUtils;

import rs.ac.uns.ftn.informatika.bibliography.dao.RegisterEntryDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RegisterEntryDB;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.ETDMSXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.Serializer;
import rs.ac.uns.ftn.informatika.bibliography.reports.freemarker.TemplateRunner;
import rs.ac.uns.ftn.informatika.bibliography.utils.FacesMessages;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * DTO class which presents bibliographic mARC21Record with data about study final document.
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class StudyFinalDocumentDTO extends PublicationDTO {

	private MultilingualContentDTO title;
	private List<MultilingualContentDTO> titleTranslations;
	private String someTitle = "";
	private MultilingualContentDTO subtitle;
	private List<MultilingualContentDTO> subtitleTranslations;
	private AuthorDTO author;
	private String someAuthorName = "";
	private InstitutionDTO institution;
	private String someInstitutionName = "";
	private String studyType = "";
	private MultilingualContentDTO note;
	private List<MultilingualContentDTO> noteTranslations;
	private MultilingualContentDTO keywords;
	private List<MultilingualContentDTO> keywordsTranslations;
	private MultilingualContentDTO abstracT;
	private List<MultilingualContentDTO> abstractTranslations;
//	private String uri = "";

	
	private ResearchAreaDTO researchArea;
	private String someResearchArea = "";
	
	private MultilingualContentDTO alternativeTitle;
	private List<MultilingualContentDTO> alternativeTitleTranslations;
	private MultilingualContentDTO extendedAbstract;
	private List<MultilingualContentDTO> extendedAbstractTranslations;
	private String language;
	private String isbn;
	private PhysicalDescriptionDTO physicalDescription;
	private String udc;
	private PublisherDTO publisher;
	private Calendar publicationDate;
	private char recordType = 'a';
	private String contentFormat;
	private MultilingualContentDTO rights;
	private List<MultilingualContentDTO> rightsTranslations;
	private String levelOfEducation;
	private Calendar acceptedOn;
	private Calendar defendedOn;
	private String defendedStatus = defendedNameString[2];
	private String holdingData;
	
	private List<AuthorDTO> advisors;
	private List<AuthorDTO> committeeMembers;
	
	private String harvardRepresentationEn="";
	private String ETDMS = "";
	private boolean unsDissertation;
	private boolean paDissertation;
	
	private RegisterEntryDTO registerEntry;
	
	
	private FileDTO metadataCopyright;
	private FileDTO preliminaryTheses;
	private String preliminaryThesesURL = "";
	private FileDTO preliminarySupplement;
	private String preliminarySupplementURL = "";
	
	
	private FileDTO report;
	private String reportURL = "";
	
	private String publicPeriod = "";
	
	private long dateModified = 0;
	
	private List<PublicationDTO> relatedPublications = new ArrayList<PublicationDTO>();
	
	private String relatedPublicationsHTMLRepresentation = "";
	
	public static String [] defendedNameString = new String [] {"ALL", "DEFENDED", "NONDEFENDED"};
	
	public StudyFinalDocumentDTO() {
		super();
		author = new AuthorDTO();
		institution = new InstitutionDTO();
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
		
		
		alternativeTitleTranslations = new ArrayList<MultilingualContentDTO>();
		alternativeTitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		extendedAbstractTranslations = new ArrayList<MultilingualContentDTO>();
		extendedAbstract = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		physicalDescription = new PhysicalDescriptionDTO();
		publisher = new PublisherDTO();
		rightsTranslations = new ArrayList<MultilingualContentDTO>();
		rights = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		
		advisors = new ArrayList<AuthorDTO>();
		committeeMembers = new ArrayList<AuthorDTO>();
		registerEntry = new RegisterEntryDTO(this, this.author);
	}

	/**
	 * @param controlNumber
	 *            Control number
	 */
	public StudyFinalDocumentDTO(String controlNumber) {
		super(controlNumber);
		author = new AuthorDTO();
		institution = new InstitutionDTO();
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
		
		alternativeTitleTranslations = new ArrayList<MultilingualContentDTO>();
		alternativeTitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		extendedAbstractTranslations = new ArrayList<MultilingualContentDTO>();
		extendedAbstract = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		physicalDescription = new PhysicalDescriptionDTO();
		publisher = new PublisherDTO();
		rightsTranslations = new ArrayList<MultilingualContentDTO>();
		rights = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		
		advisors = new ArrayList<AuthorDTO>();
		committeeMembers = new ArrayList<AuthorDTO>();
		
		registerEntry = new RegisterEntryDTO(this, this.author);
	
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
//		if(true){
//			record.loadFromDatabase();
//			IObjectProfileNode profile = ObjectProfiler.profile (record);
//        
//			System.out.println ("obj size = " + profile.size () + " bytes");
//			System.out.println (profile.dump ());
//			setNotLoaded(true);
//		}
		
		if(notLoaded) {
			return someTitle;
		} else {
			if(notLoaded){
				record.loadFromDatabase();
			}
			String retVal = null;
			if (title.getContent() != null)
				retVal = title.getContent();
			for (MultilingualContentDTO titleTranslation : titleTranslations) {
				if((retVal == null) || (locale.getLanguage().equals(titleTranslation.getLanguage().substring(0,2))))
					retVal = titleTranslation.getContent();	
			}	
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
	 * @return the author
	 */
	public AuthorDTO getAuthor() {
		if(notLoaded){
			record.loadFromDatabase();
		} 
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(AuthorDTO author) {
		this.author = author;
	}
	
	/**
	 * @return the someAuthorName
	 */
	public String getSomeAuthorName() {
		if(notLoaded)
			return someAuthorName;
		else
			return author.getName().toString();
	}

	/**
	 * @param someAuthorName the someAuthorName to set
	 */
	public void setSomeAuthorName(String someAuthorName) {
		this.someAuthorName = someAuthorName;
	}

	/**
	 * @return the all authors
	 */
	@Override
	public List<AuthorDTO> getAllAuthors() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
		if ((author.getControlNumber() != null)
				&& (!("".equals(author.getControlNumber()))))
			retVal.add(author);
		return retVal;
	}
	
	/**
	 * @return the institution
	 */
	public InstitutionDTO getInstitution() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return institution;
	}

	/**
	 * @param institution
	 *            the institution to set
	 */
	public void setInstitution(InstitutionDTO institution) {
		this.institution = institution;
	}

	/**
	 * @return the someInstitutionName
	 */
	public String getSomeInstitutionName() {
		if(notLoaded)
			return someInstitutionName;
		else 
			return institution.getSomeName();
	}

	/**
	 * @param someInstitutionName the someInstitutionName to set
	 */
	public void setSomeInstitutionName(String someInstitutionName) {
		this.someInstitutionName = someInstitutionName;
	}

	/**
	 * @return the studyType
	 */
	public String getStudyType() {
		return studyType;
	}
	
	/**
	 * @return the localized study type
	 */
	public String getLocalizedStudyType() {
		return new FacesMessages("messages.messages-records", locale)
						.getMessageFromResourceBundle(studyType);
	}

	/**
	 * @param studyType the studyType to set
	 */
	public void setStudyType(String studyType) {
		this.studyType = studyType;
		if("records.studyFinalDocument.editPanel.studyType.phd".equals(studyType)){
			levelOfEducation = "PhD (dr)";
		} else if("records.studyFinalDocument.editPanel.studyType.phdArt".equals(studyType)){
			levelOfEducation = "D.A. (Doctor of Arts)";
		}
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
	 * @return the someResearchArea
	 */
	public String getSomeResearchArea() {
		if(notLoaded)
			return someResearchArea;
		else 
			return researchArea.getSomeTerm();
	}

	/**
	 * @param someResearchArea the someResearchArea to set
	 */
	public void setSomeResearchArea(String someResearchArea) {
		this.someResearchArea = someResearchArea;
	}

	/**
	 * @return the alternativeTitle
	 */
	public MultilingualContentDTO getAlternativeTitle() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return alternativeTitle;
	}

	/**
	 * @param alternativeTitle the alternativeTitle to set
	 */
	public void setAlternativeTitle(MultilingualContentDTO alternativeTitle) {
		this.alternativeTitle = alternativeTitle;
	}
	
	/**
	 * @return the alternativeTitleTranslations
	 */
	public List<MultilingualContentDTO> getAlternativeTitleTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return alternativeTitleTranslations;
	}

	/**
	 * @param alternativeTitleTranslations the alternativeTitleTranslations to set
	 */
	public void setAlternativeTitleTranslations(
			List<MultilingualContentDTO> alternativeTitleTranslations) {
		this.alternativeTitleTranslations = alternativeTitleTranslations;
	}

	

	/**
	 * @return the extendedAbstract
	 */
	public MultilingualContentDTO getExtendedAbstract() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return extendedAbstract;
	}

	/**
	 * @param extendedAbstract the extendedAbstract to set
	 */
	public void setExtendedAbstract(MultilingualContentDTO extendedAbstract) {
		this.extendedAbstract = extendedAbstract;
	}

	/**
	 * @return the extendedAbstractTranslations
	 */
	public List<MultilingualContentDTO> getExtendedAbstractTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return extendedAbstractTranslations;
	}

	/**
	 * @param extendedAbstractTranslations the extendedAbstractTranslations to set
	 */
	public void setExtendedAbstractTranslations(
			List<MultilingualContentDTO> extendedAbstractTranslations) {
		this.extendedAbstractTranslations = extendedAbstractTranslations;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
		if((language == null) || (!language.equals("srp")))
			alphabet = null;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the physicalDescription
	 */
	public PhysicalDescriptionDTO getPhysicalDescription() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return physicalDescription;
	}

	/**
	 * @param physicalDescription the physicalDescription to set
	 */
	public void setPhysicalDescription(PhysicalDescriptionDTO physicalDescription) {
		this.physicalDescription = physicalDescription;
	}

	/**
	 * @return the udc
	 */
	public String getUdc() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return udc;
	}

	/**
	 * @param udc the udc to set
	 */
	public void setUdc(String udc) {
		this.udc = udc;
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
	 * @return the publicationDate
	 */
	public Calendar getPublicationDate() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return publicationDate;
	}
	
	public String getPublicPeriod(){
		String retVal = "";
		if(notLoaded){
			retVal = publicPeriod;
		} else {
			String dateString1 = null;
			String dateString2 = null;
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			Date date = publicationDate.getTime();
			if(date != null){
				dateString1 = formatter.format(date);
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(date.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 30);
				dateString2 = formatter.format(cal.getTime());
				retVal += dateString1 + " - " + dateString2;
			}
		}
		return retVal;
	}
	
	public String getPublicPeriodNotDefended(){
		String retVal = "";
		if(notLoaded){
			if(publicPeriod!=null){
				retVal = publicPeriod.substring(publicPeriod.indexOf('-') + 1) + " - ";
			}
		} else {
			String dateString2 = null;
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			Date date = publicationDate.getTime();
			if(date != null){
				date.setTime(date.getTime() + (30l*24*60*60*1000));
				dateString2 = formatter.format(date);
				retVal += dateString2 + " - ";
			}
		}
		return retVal;
	}
	
	/**
	 * @param publicPeriod the publicPeriod to set
	 */
	public void setPublicPeriod(String publicPeriod) {
		this.publicPeriod = publicPeriod;
	}

	/**
	 * @param publicationDate the publicationDate to set
	 */
	public void setPublicationDate(Calendar publicationDate) {
		this.publicationDate = publicationDate;
		if((publicationDate != null) && (this.defendedOn == null))
			this.publicationYear = ""+publicationDate.get(Calendar.YEAR);
		else if(this.defendedOn == null)
				this.publicationYear = null;
	}

	/**
	 * @return the recordType
	 */
	public char getRecordType() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return recordType;
	}

	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the contentFormat
	 */
	public String getContentFormat() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return contentFormat;
	}

	/**
	 * @param contentFormat the contentFormat to set
	 */
	public void setContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
	}

	/**
	 * @return the rights
	 */
	public MultilingualContentDTO getRights() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(MultilingualContentDTO rights) {
		this.rights = rights;
	}

	/**
	 * @return the rightsTranslations
	 */
	public List<MultilingualContentDTO> getRightsTranslations() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return rightsTranslations;
	}

	/**
	 * @param rightsTranslations the rightsTranslations to set
	 */
	public void setRightsTranslations(
			List<MultilingualContentDTO> rightsTranslations) {
		this.rightsTranslations = rightsTranslations;
	}

//	/**
//	 * @return the nameOfAuthorDegree
//	 */
//	public String getNameOfAuthorDegree() {
//		if(notLoaded){
//			record.loadFromDatabase();
//		}
//		return nameOfAuthorDegree;
//	}
//
//	/**
//	 * @param nameOfAuthorDegree the nameOfAuthorDegree to set
//	 */
//	public void setNameOfAuthorDegree(String nameOfAuthorDegree) {
//		this.nameOfAuthorDegree = nameOfAuthorDegree;
//	}

	/**
	 * @return the levelOfEducation
	 */
	public String getLevelOfEducation() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return levelOfEducation;
	}

	/**
	 * @param levelOfEducation the levelOfEducation to set
	 */
	public void setLevelOfEducation(String levelOfEducation) {
		this.levelOfEducation = levelOfEducation;
	}

	/**
	 * @return the acceptedOn
	 */
	public Calendar getAcceptedOn() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return acceptedOn;
	}

	/**
	 * @param acceptedOn the acceptedOn to set
	 */
	public void setAcceptedOn(Calendar acceptedOn) {
		this.acceptedOn = acceptedOn;
	}

	/**
	 * @return the defendedOn
	 */
	public Calendar getDefendedOn() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return defendedOn;
	}

	/**
	 * @param defendedOn the defendedOn to set
	 */
	public void setDefendedOn(Calendar defendedOn) {
		this.defendedOn = defendedOn;
		if(defendedOn != null) {
			this.publicationYear = "" + defendedOn.get(Calendar.YEAR);
			this.defendedStatus = defendedNameString[1];
		}
	}

	public String getDefendedStatus() {
		return defendedStatus;
	}

	public void setDefendedStatus(String defendedStatus) {
		this.defendedStatus = defendedStatus;
	}

	public boolean isDefended(){
		return defendedNameString[1].equals(defendedStatus);
	}

	/**
	 * @return the holdingData
	 */
	public String getHoldingData() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return holdingData;
	}

	/**
	 * @param holdingData the holdingData to set
	 */
	public void setHoldingData(String holdingData) {
		this.holdingData = holdingData;
	}

	/**
	 * @return the advisors
	 */
	public List<AuthorDTO> getAdvisors() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return advisors;
	}

	/**
	 * @param advisors the advisors to set
	 */
	public void setAdvisors(List<AuthorDTO> advisors) {
		this.advisors = advisors;
	}

	/**
	 * @return the committeeMembers
	 */
	public List<AuthorDTO> getCommitteeMembers() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return committeeMembers;
	}

	/**
	 * @param committeeMembers the committeeMembers to set
	 */
	public void setCommitteeMembers(List<AuthorDTO> committeeMembers) {
		this.committeeMembers = committeeMembers;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if((notLoaded) && (locale.getLanguage().equals("sr"))){
			return stringRepresentation;
		} else {
			if(notLoaded){
				record.loadFromDatabase();
			}
			StringBuffer retVal = new StringBuffer();
			if ((author != null) && (author.getControlNumber() != null))
				retVal.append(author.getName() + ", ");
			else
				retVal.append("mainAuthor, ");
			if(locale.getLanguage().equals("sr")){
				if ((title.getContent() != null) && (title.getContent().trim().length() > 0))
					retVal.append(title.getContent() + ", ");
				else if (titleTranslations.size()>0)
						retVal.append("" + titleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
				else
					retVal.append("Title, ");
			} else {
				retVal.append(getSomeTitle());
			}
			if (subtitle.getContent() != null)
				retVal.append(subtitle.getContent() + ", ");
			else if (subtitleTranslations.size()>0)
				retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			if(institution != null) {
				retVal.append(institution + ", "); 
			}
			if (publicationYear != null) 
				retVal.append(publicationYear);
			else
				retVal.append("publicationYear");
			if(locale.getLanguage().equals("sr")){
//				if((this.getLanguage()!= null) && (this.getLanguage().equals("srp")) && (this.getAlphabet()!= null) && (this.getAlphabet().equals("cyrillic script"))){
//					return LatCyrUtils.toCyrillic(retVal.toString());
//				} else 
				stringRepresentation = retVal.toString();
				return retVal.toString();
			}
			else {
				stringRepresentation = LatCyrUtils.toLatin(retVal.toString());
				return LatCyrUtils.toLatin(retVal.toString());
			}
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
		String retVal = "";
		if(notLoaded) {
			retVal = harvardRepresentation;
		} else {
			harvardRepresentation = TemplateRunner.getRepresentation(this,TemplateRunner.HARVARD);
			if(harvardRepresentation == null)
				harvardRepresentation = "";
			retVal = harvardRepresentation;
			/*if((this.getLanguage()!= null) && (this.getLanguage().equals("srp")) && (this.getAlphabet()!= null) && (this.getAlphabet().equals("cyrillic script"))){
				retVal = LatCyrUtils.toCyrillic(retVal);
				retVal = retVal.replace("&нбсп;", "&nbsp;");
				retVal = retVal.replace("и>", "i>");
			}*/
		}
		return retVal;
	}

	public String getSomeHarvardRepresentation() {
		if(locale.getLanguage().equals("en")){
			return getHarvardRepresentationEn();
		} else {
			return getHarvardRepresentation();
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
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO#setFile(rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO)
	 */
	@Override
	public void setFile(FileDTO file) {
		super.setFile(file);
		if(file != null)
			contentFormat = file.getMime();
		else
			contentFormat = null;
	}
	
	
	
	/**
	 * @return the metadataCopyright
	 */
	public FileDTO getMetadataCopyright() {
		return metadataCopyright;
	}

	/**
	 * @param metadataCopyright the metadataCopyright to set
	 */
	public void setMetadataCopyright(FileDTO metadataCopyright) {
		this.metadataCopyright = metadataCopyright;
	}
	

	/**
	 * @return the preliminaryTheses
	 */
	public FileDTO getPreliminaryTheses() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return preliminaryTheses;
	}

	/**
	 * @param preliminaryTheses the preliminaryTheses to set
	 */
	public void setPreliminaryTheses(FileDTO preliminaryTheses) {
		this.preliminaryTheses = preliminaryTheses;
	}
	
	/**
	 * @return the preliminaryThesesURL
	 */
	public String getPreliminaryThesesURL() {
		if(notLoaded){
			return preliminaryThesesURL;
		} 
		if((preliminaryTheses != null) && (preliminaryTheses.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/javniUvid" + preliminaryTheses.getFileName() + "?controlNumber=" + preliminaryTheses.getControlNumber() + "&fileName=" + preliminaryTheses.getFileName() + "&id=" + preliminaryTheses.getId();
		}
		return "";
	}
	
	/**
	 * @param preliminaryThesesURL the preliminaryThesesURL to set
	 */
	public void setPreliminaryThesesURL(String preliminaryThesesURL) {
		this.preliminaryThesesURL = preliminaryThesesURL;
	}

	/**
	 * @return the preliminarySupplement
	 */
	public FileDTO getPreliminarySupplement() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return preliminarySupplement;
	}

	/**
	 * @param preliminarySupplement the preliminarySupplement to set
	 */
	public void setPreliminarySupplement(FileDTO preliminarySupplement) {
		this.preliminarySupplement = preliminarySupplement;
	}

	/**
	 * @return the preliminarySupplementURL
	 */
	public String getPreliminarySupplementURL() {
		if(notLoaded){
			return preliminarySupplementURL;
		} 
		if((preliminarySupplement != null) && (preliminarySupplement.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/javniUvidDodatak" + preliminarySupplement.getFileName() + "?controlNumber=" + preliminarySupplement.getControlNumber() + "&fileName=" + preliminarySupplement.getFileName() + "&id=" + preliminarySupplement.getId();
		}
		return "";
	}

	/**
	 * @param preliminarySupplementURL the preliminarySupplementURL to set
	 */
	public void setPreliminarySupplementURL(String preliminarySupplementURL) {
		this.preliminarySupplementURL = preliminarySupplementURL;
	}

	
	/**
	 * @return the report
	 */
	public FileDTO getReport() {
		if(notLoaded){
			record.loadFromDatabase();
		}
		return report;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(FileDTO report) {
		this.report = report;
	}
	
	/**
	 * @return the reportURL
	 */
	public String getReportURL() {
		if(notLoaded){
			return reportURL;
		} 
		else if((report != null) && (report.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/IzvestajKomisije" + report.getFileName() + "?controlNumber=" + report.getControlNumber() + "&fileName=" + report.getFileName() + "&id=" + report.getId();
		}
		return "";
	}
	
	
//	/**
//	 * @return the reportURL
//	 */
//	public String getReportURL() {
//		if(notLoaded){
//			record.loadFromDatabase();
//		} 
//		if((report != null) && (report.getId() != 0)){
//			String filePath = "";
//			if(FacesContext.getCurrentInstance()!=null)
//				filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
//			return filePath + "/DownloadFileServlet/IzvestajKomisije" + report.getFileName() + "?controlNumber=" + report.getControlNumber() + "&fileName=" + report.getFileName() + "&id=" + report.getId();
//		}
//		return "";
//	}

	/**
	 * @param reportURL the reportURL to set
	 */
	public void setReportURL(String reportURL) {
		this.reportURL = reportURL;
	}

	/**
	 * @return the dateModified
	 */
	public long getDateModified() {
		if(notLoaded)
			return dateModified;
		else if(record.getFiles()!=null){
			for (FileDTO file : record.getFiles()) {
				if((file.getId() != 0) && (file.getType() != null) && (file.getType().equals("report"))){
					if(file.getDateModified() != null)
						return file.getDateModified().getTimeInMillis();
				}
			}
		}  
		return 0;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#setFiles(java.util.List)
	 */
	@Override
	public void setFiles(List<FileDTO> files) {
		if(files!=null)
			for (FileDTO fileDTO : files) {
				if((fileDTO != null) && (fileDTO.getType() != null)) {
					if (fileDTO.getType().equals("metadataCopyright"))
						this.metadataCopyright = fileDTO;
					else if (fileDTO.getType().equals("report"))
						this.report = fileDTO;
					else if (fileDTO.getType().equals("preliminaryTheses"))
						this.preliminaryTheses = fileDTO;
					else if (fileDTO.getType().equals("preliminarySupplement"))
						this.preliminarySupplement = fileDTO;
				}
			}
		super.setFiles(files);
	}

	
	
	
	/**
	 * @return the metadataCopyrightURL
	 */
	public String getMetadataCopyrightURL() {
		if(notLoaded){
			record.loadFromDatabase();
		} 
		if((metadataCopyright != null) && (metadataCopyright.getId() != 0)){
			String filePath = "";
			if(FacesContext.getCurrentInstance()!=null){
				try {
					filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				} catch (Throwable e){
				}
			}
			return filePath + "/DownloadFileServlet/IzjavaOIstovetnosti" + metadataCopyright.getFileName() + "?controlNumber=" + metadataCopyright.getControlNumber() + "&fileName=" + metadataCopyright.getFileName() + "&id=" + metadataCopyright.getId();
		}
		return "";
	}

	
	/**
	 * @return the relatedPublications
	 */
	public List<PublicationDTO> getRelatedPublications() {
		if(notLoaded){
			record.loadFromDatabase();
		} 
		return relatedPublications;
	}

	/**
	 * @param relatedPublications the relatedPublications to set
	 */
	public void setRelatedPublications(List<PublicationDTO> relatedPublications) {
		this.relatedPublications = relatedPublications;
	}
	
	

	/**
	 * @return the relatedPublicationsHTMLRepresentation
	 */
	public String getRelatedPublicationsHTMLRepresentation() {
		if(notLoaded == false){
			relatedPublicationsHTMLRepresentation = "";
			for (PublicationDTO publication : relatedPublications) {
				relatedPublicationsHTMLRepresentation += publication.getHarvardRepresentation() + "<br/>";
			}
		}
		return relatedPublicationsHTMLRepresentation;
	}

	/**
	 * @param relatedPublicationsHTMLRepresentation the relatedPublicationsHTMLRepresentation to set
	 */
	public void setRelatedPublicationsHTMLRepresentation(
			String relatedPublicationsHTMLRepresentation) {
		this.relatedPublicationsHTMLRepresentation = relatedPublicationsHTMLRepresentation;
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
			retVal.append("<u>" + fm.getMessageFromResourceBundle("records.bibliography.studyFinalDocument") + "</u><br/>");
			if ((author != null) && (author.getControlNumber() != null))
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.author") + ": "+ author.getName() + "<br/>");
			else
				retVal.append("mainAuthor <br/>");
			retVal.append(fm.getMessageFromResourceBundle("records.bibliography.titleLanguage") + ": " + fm.getMessageFromResourceBundle("records.language." + title.getLanguage()) + "<br/>"); 
			if (title.getContent() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.title") + ": <i>" + getSomeTitle() + "</i><br/>");
			else
				retVal.append("Title, ");
			if (subtitle.getContent() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.subtitle") + ": " + subtitle.getContent() + "<br/>");
			else if (subtitleTranslations.size()>0){
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.subtitle") + ": " + fm.getMessageFromResourceBundle("records.bibliography.notDefined") + "<br/>");
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.translatedSubtitle") + ": " + subtitleTranslations.get(0).getContent() + "<br/>");
			}
			if (studyType != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.studyType") + ": " + getLocalizedStudyType() + "<br/>");
			if (institution != null) 
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.institution") + ": " + institution + "<br/>");
			if (getPublicationYear() != null)
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.publicationYear") + ": " + getPublicationYear() + "<br/>");
			if ((file != null)){
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null){
					try {
						filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
					} catch (Throwable e){
					}
				}
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.download") + ": <a target=\"_blank\" href=\"" + filePath + "/DownloadFileServlet/Disertacija" + file.getFileName() + "?controlNumber=" + file.getControlNumber() + "&fileName=" + file.getFileName() + "&id=" + file.getId() + "\"><img src=\"" + filePath +  "/javax.faces.resource/download.png.jsf?ln=img\" height=\"24\" width=\"24\" alt=\"link\" target=\"_blank\"/></a><br/>");
			} 
			if ((supplement != null)){
				String filePath = "";
				if(FacesContext.getCurrentInstance()!=null){
					try {
						filePath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
					} catch (Throwable e){
					}
				}
				retVal.append(fm.getMessageFromResourceBundle("records.bibliography.download") + ": <a target=\"_blank\" href=\"" + filePath + "/DownloadFileServlet/DisertacijaDodatak" + supplement.getFileName() + "?controlNumber=" + supplement.getControlNumber() + "&fileName=" + supplement.getFileName() + "&id=" + supplement.getId() + "\"><img src=\"" + filePath +  "/javax.faces.resource/download.png.jsf?ln=img\" height=\"24\" width=\"24\" alt=\"link\" target=\"_blank\"/></a>");
			}
			if(locale.getLanguage().equals("sr"))
				return retVal.toString();
			else 
				return LatCyrUtils.toLatin(retVal.toString());
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
		retVal.append(indent + "<theses>\n");
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
		if((institution != null) && (institution.getSomeName() != null))
			retVal.append(indent + "\t<institution>" + institution.getSomeName()+ "</institution>\n");
		if(publicationYear != null)
			retVal.append(indent + "\t<year>" + publicationYear + "</year>\n");
		if(isbn != null)
			retVal.append(indent + "\t<isbn>" + isbn + "</isbn>\n");
		if((publisher != null) && (publisher.toString().trim().length() > 0))
			retVal.append(indent + "\t<publisher>" + publisher.toString() + "</publisher>\n");
		if(doi != null)
			retVal.append(indent + "\t<doi>" + doi + "</doi>\n");
		if((studyType != null) && (studyType.trim().length() > 0))
			retVal.append(indent + "\t<thesesType>" + getLocalizedStudyType() + "</thesesType>\n");
		List<AuthorDTO> authors = getAllAuthors();
		if(authors.size() > 0){
			retVal.append(indent + "\t<author>\n");
			for (AuthorDTO author : getAllAuthors()) {
				retVal.append(author.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</author>\n");
		}
		List<AuthorDTO> advisors = getAdvisors();
		if(advisors.size() > 0){
			retVal.append(indent + "\t<advisors>\n");
			for (AuthorDTO advisor : advisors) {
				retVal.append(advisor.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</advisors>\n");
		}
		List<AuthorDTO> boardMembers = getCommitteeMembers();
		if(boardMembers.size() > 0){
			retVal.append(indent + "\t<boardMembers>\n");
			for (AuthorDTO boardMember : boardMembers) {
				retVal.append(boardMember.getKnrXML(indent + "\t\t", null));
			}
			retVal.append(indent + "\t</boardMembers>\n");
		}
		retVal.append(indent + "</theses>\n");
		return retVal.toString();
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#clear()
	 */
	@Override
	public void clear() {
		author = new AuthorDTO();
		institution = new InstitutionDTO();
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
		
		
		alternativeTitleTranslations = new ArrayList<MultilingualContentDTO>();
		alternativeTitle = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		extendedAbstractTranslations = new ArrayList<MultilingualContentDTO>();
		extendedAbstract = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		physicalDescription = new PhysicalDescriptionDTO();
		publisher = new PublisherDTO();
		rightsTranslations = new ArrayList<MultilingualContentDTO>();
		rights = new MultilingualContentDTO(null, null, MultilingualContentDTO.TRANS_ORIGINAL);
		
		advisors = new ArrayList<AuthorDTO>();
		committeeMembers = new ArrayList<AuthorDTO>();
		relatedRecords = null;
		
		file = null;
		wordCloudImage = null;
		files = null;
		preliminaryTheses = null;
		preliminarySupplement = null;
		supplement = null;
		report = null;
		fileCopyright = null;		
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#getControlNumber()
	 */
	@Override
	public String getControlNumber() {
		return super.getControlNumber();
	}
	
	public String getHarvardRepresentationEn(){
		if(notLoaded){
			return harvardRepresentationEn;
		} 
		else {
			Locale oldLocale = locale;
			Locale enLocale = new Locale("en");
			this.setLocale(enLocale);
			Locale oldLocaleAuthor = author.getLocale();
			author.setLocale(enLocale);
			Locale oldLocaleInstitution = institution.getLocale();
			institution.setLocale(locale);
			harvardRepresentationEn = TemplateRunner.getRepresentation(this,TemplateRunner.HARVARD);
			if(harvardRepresentationEn == null)
				harvardRepresentationEn = "";
			this.setLocale(oldLocale);
			author.setLocale(oldLocaleAuthor);
			institution.setLocale(oldLocaleInstitution);
			return harvardRepresentationEn;
		}
	}

	public void setHarvardRepresentationEn(String harvardRepresentationEn) {
		this.harvardRepresentationEn = harvardRepresentationEn;
	}
	
	public String getETDMS(){
		if(notLoaded){
			record.loadFromDatabase();
		} 
//		if(notLoaded){
//			return ETDMS;
//		} 
//		else {
			Serializer serializer = new ETDMSXMLSerializer();
			String retVal = serializer.fromRecord(record, "");
			retVal = retVal.replace("\n", "tempEnterCRIS").replace("\t", "tempTabCRIS");
			retVal = StringEscapeUtils.escapeHtml(retVal);
			retVal = retVal.replace("tempEnterCRIS", "<br/>").replace("tempTabCRIS", "&nbsp;&nbsp;&nbsp;");
			return retVal;
//		}
	}

	/**
	 * @param eTDMS the eTDMS to set
	 */
	public void setETDMS(String eTDMS) {
//		ETDMS = eTDMS;
	}

	/**
	 * @return the unsDissertation
	 */
	public boolean isUnsDissertation() {
		if(notLoaded){
			return unsDissertation;
		} else {
			boolean retVal = false;
			try {
				if((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber()!=null) && (institution.getSuperInstitution().getControlNumber().equalsIgnoreCase("(BISIS)5920"))){
					if((!(institution.getControlNumber().equals("(BISIS)5963"))) && (!(institution.getControlNumber().equals("(BISIS)69119"))) && (!(institution.getControlNumber().equals("(BISIS)69121")))){
						retVal = true;
					}
				}
			} catch (NullPointerException e) {
			}
			return retVal;
		}
	}

	/**
	 * @param unsDissertation the unsDissertation to set
	 */
	public void setUnsDissertation(boolean unsDissertation) {
		this.unsDissertation = unsDissertation;
	}
	
	/**
	 * @return the paDissertation
	 */
	public boolean isPaDissertation() {
		if(notLoaded){
			return paDissertation;
		} else {
			boolean retVal = false;
			try {
				if((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber()!=null) && (institution.getSuperInstitution().getControlNumber().equalsIgnoreCase("(BISIS)94894"))){
						retVal = true;
				}
			} catch (NullPointerException e) {
			}
			return retVal;
		}
	}

	/**
	 * @param paDissertation the paDissertation to set
	 */
	public void setPaDissertation(boolean paDissertation) {
		this.paDissertation = paDissertation;
	}

	/**
	 * @return the registerEntry
	 */
	public RegisterEntryDTO getRegisterEntry() {
		return registerEntry;
	}

	/**
	 * @param registerEntry the registerEntry to set
	 */
	public void setRegisterEntry(RegisterEntryDTO registerEntry) {
		this.registerEntry = registerEntry;
	}
	
	public void loadRegisterEntry(boolean translateToCyrillic){
		RegisterEntryDAO registerEntryDAO = new RegisterEntryDAO(new RegisterEntryDB());
		this.setRegisterEntry(registerEntryDAO.getRegisterEntryFromDatabase(this, translateToCyrillic));
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

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO#toString(java.lang.String)
	 */
	@Override
	public String toString(String id) {
		if(notLoaded){
			record.loadFromDatabase();
		}	
		StringBuffer retVal = new StringBuffer();
		if ((author != null) && (author.getControlNumber() != null)){
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber() != null) && (author.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("<b>");
			}
			retVal.append(author.getName()); 
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber() != null) && (author.getInstitution().getControlNumber().equalsIgnoreCase(id))){
				retVal.append("</b>");
			}
			retVal.append(", ");
		} else
			retVal.append("mainAuthor, ");
		if(locale.getLanguage().equals("sr")){
			if ((title.getContent() != null) && (title.getContent().trim().length() > 0))
				retVal.append(title.getContent() + ", ");
			else if (titleTranslations.size()>0)
					retVal.append("" + titleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
			else
				retVal.append("Title, ");
		} else {
			retVal.append(getSomeTitle());
		}
		if (subtitle.getContent() != null)
			retVal.append(subtitle.getContent() + ", ");
		else if (subtitleTranslations.size()>0)
			retVal.append("" + subtitleTranslations.get(0).getContent() + "(in " + new FacesMessages("messages.messages-records", new Locale("eng")).getMessageFromResourceBundle("records.language."+ title.getLanguage())+ "), ");
		if(institution != null) {
			retVal.append(institution + ", "); 
		}
		if (publicationYear != null) 
			retVal.append(publicationYear);
		else
			retVal.append("publicationYear");
		
		return retVal.toString();
	}
	

}
