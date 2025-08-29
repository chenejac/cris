/**
 *
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh.TeslaRISJDBCRecordFactory;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;
import ORG.oclc.oai.util.OAIUtil;


/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to https://github.com/openaire/guidelines-cris-managers/blob/master/schemas/openaire-cerif-profile.xsd XMLSchema
 *
 *
 */
public class TeslaRISXMLSerializer extends OpenAIRECRISXMLSerializer {

	/**
	 */
	public TeslaRISXMLSerializer(){
		this.languages = Arrays.asList("en", "sr");
		this.source = "TeslaRIS";
	}

	/**
	 *
	 * @param rec
	 *            MARC 21 MARC21Record
	 * @param indent
	 *            tags indent
	 * @return created XML
	 *
	 */
	public String fromRecord(Record rec, String indent) {
		RecordDTO record = rec.getDto();
		if(record.isNotLoaded())
			rec.loadFromDatabase();
		if(record instanceof PatentDTO)
			return patentFromRecord(record, indent, true);
		else if(record instanceof ProductDTO)
			return productFromRecord(record, indent, true);
		else if(record instanceof PublicationDTO)
			return publicationFromRecord(record, indent, true);
		else if(record instanceof AuthorDTO)
			return personFromRecord(record, indent, true, false);
		else if(record instanceof InstitutionDTO)
			return orgUnitFromRecord(record, indent, true);
		else if(record instanceof ConferenceDTO)
			return eventFromRecord(record, indent, true);
		else
			return "";
	}

	protected List<XMLTag> getPersonNames(String indent, String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO authorDTO = (AuthorDTO)record;
			if(authorDTO.getName() != null){
				List<XMLTag> subtags = new ArrayList<XMLTag>();
				if (authorDTO.getName().getLastname() != null && !"-".equals(authorDTO.getName().getLastname().trim())) {
					XMLTag familyNames = new XMLTag(indent + "\t\t", "FamilyNames", authorDTO.getName().getLastname());
					subtags.add(familyNames);
				}
				if (authorDTO.getName().getFirstname() != null && !"-".equals(authorDTO.getName().getFirstname().trim())) {
					XMLTag firstNames = new XMLTag(indent + "\t\t", "FirstNames", authorDTO.getName().getFirstname());
					subtags.add(firstNames);
				}
				if (authorDTO.getName().getOtherName() != null && !"-".equals(authorDTO.getName().getOtherName().trim())) {
					XMLTag middleNames = new XMLTag(indent + "\t\t", "MiddleNames", authorDTO.getName().getOtherName());
					subtags.add(middleNames);
				}
				XMLTag personName = new XMLTag(tagName);
				personName.setIndent(indent + "\t");
				personName.setSubTags(subtags);
				retVal.add(personName);
			}
		}
		return retVal;
	}

	/**
	 *
	 * @param record
	 *            Record Java Bean
	 * @param indent
	 *            tags indent
	 * @param xmlns
	 *            xmlns should be added
	 * @return created XML
	 *
	 */
	public String publicationFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<Publication" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");

		List<XMLTag> types = getTypes("Type", record);
		List<XMLTag> languages = getLanguages("Language", record);
		List<XMLTag> titles = getTitles("Title", record, false);
		List<XMLTag> subtitles = getSubtitles("Subtitle", record, false);
		List<XMLTag> alternativeTitles = getAlternativeTitles("AlternativeTitle", record, false);
		List<XMLTag> acronyms = getAcronyms("Acronym", record, false);

		List<XMLTag> publicationDates = getPublicationDates("PublicationDate", record);
		List<XMLTag> acceptedOnDates = getAcceptedOnDates("AcceptedOnDate", record);
		List<XMLTag> defendedOnDates = getDefendedOnDates("DefendedOnDate", record);
		List<XMLTag> publicReviewStartDates = getPublicReviewStartDates("PublicReviewStartDate", record);

		List<XMLTag> numbers = getNumbers("Number", record);
		List<XMLTag> volumes = getVolumes("Volume", record);
		List<XMLTag> issues = getIssues("Issue", record);
		List<XMLTag> editions = getEditions("Title", record);
		List<XMLTag> editionsISSN = getEditionsISSN("ISSN", record);
		List<XMLTag> editionsNumber = getEditionsNumber("Volume", record);
		List<XMLTag> startPages = getStartPages("StartPage", record);
		List<XMLTag> endPages = getEndPages("EndPage", record);
		List<XMLTag> numberOfPages = getNumberOfPages("NumberOfPages", record);
		List<XMLTag> numberOfChapters = getNumberOfChapters("NumberOfChapters", record);
		List<XMLTag> numberOfReferences = getNumberOfReferences("NumberOfReferences", record);
		List<XMLTag> numberOfTables = getNumberOfTables("NumberOfTables", record);
		List<XMLTag> numberOfPictures = getNumberOfPictures("NumberOfPictures", record);
		List<XMLTag> numberOfGraphs = getNumberOfGraphs("NumberOfGraphs", record);
		List<XMLTag> numberOfAppendixes = getNumberOfAppendixes("NumberOfAppendixes", record);


		List<XMLTag> dois = getDOIs("DOI", record);
		List<XMLTag> scpNumbers = getScopusIds("SCP-Number", record);
		List<XMLTag> issns = getIssns("ISSN", record);
		List<XMLTag> isbns = getIsbns("ISBN", record);
		List<XMLTag> urls = getURLs("URL", record);

		List<XMLTag> publishers = getPublishers("Name", record);
		List<XMLTag> publishersPlaces = getPublishersPlaces("Place", record);
		List<XMLTag> publishersStates = getPublishersStates("State", record);


		//License
		//Subject

		List<XMLTag> keywords = getKeywords("Keyword", record, false);
		List<XMLTag> abstracts = getAbstracts("Abstract", record, false);
		List<XMLTag> extendedAbstracts = getExtendedAbstracts("ExtendedAbstract", record, false);
		List<XMLTag> notes = getNotes("Note", record, false);
		List<XMLTag> researchAreas = getResearchAreas("ResearchArea", record);
		List<XMLTag> holdingDatas = getHoldingDatas("HoldingData", record);
		List<XMLTag> udcs = getUDCs("UDC", record);
		List<XMLTag> levelOfEducations = getLevelOfEducations("LevelOfEducation", record);


		List<XMLTag> accesss = getAccess("Access", record);

		for (XMLTag type : types) {
			buff.append(indent + "\t");
			buff.append(type.toString());
			buff.append("\n");
		}

		for (XMLTag language : languages) {
			buff.append(indent + "\t");
			buff.append(language.toString());
			buff.append("\n");
		}

		for (XMLTag title : titles) {
			buff.append(indent + "\t");
			buff.append(title.toString());
			buff.append("\n");
		}

		for (XMLTag subtitle : subtitles) {
			buff.append(indent + "\t");
			buff.append(subtitle.toString());
			buff.append("\n");
		}

		for (XMLTag alternativeTitle : alternativeTitles) {
			buff.append(indent + "\t");
			buff.append(alternativeTitle.toString());
			buff.append("\n");
		}

		for (XMLTag acronym : acronyms) {
			buff.append(indent + "\t");
			buff.append(acronym.toString());
			buff.append("\n");
		}

		if(record instanceof PaperJournalDTO){
			buff.append(indent + "\t<PublishedIn>\n");
			buff.append(publicationFromRecord(((PaperJournalDTO)record).getJournal(), indent+"\t\t", false));
			buff.append("\n" + indent + "\t</PublishedIn>\n");
		} else if ((record instanceof PaperProceedingsDTO) || (record instanceof PaperMonographDTO)){
			buff.append(indent + "\t<PartOf>\n");
			buff.append(indent + "\t\t<DisplayName>" + OAIUtil.xmlEncode(((record instanceof PaperProceedingsDTO)?(((PaperProceedingsDTO)record).getProceedings().getSomeTitle()):(((PaperMonographDTO)record).getMonograph().getSomeTitle()))) + "</DisplayName>\n");
			buff.append(publicationFromRecord(((record instanceof PaperProceedingsDTO)?(((PaperProceedingsDTO)record).getProceedings()):(((PaperMonographDTO)record).getMonograph())), indent+"\t\t", false));
			buff.append("\n" + indent + "\t</PartOf>\n");
		}

		for (XMLTag publicationDate : publicationDates) {
			buff.append(indent + "\t");
			buff.append(publicationDate.toString());
			buff.append("\n");
		}

		for (XMLTag acceptedOnDate : acceptedOnDates) {
			buff.append(indent + "\t");
			buff.append(acceptedOnDate.toString());
			buff.append("\n");
		}

		for (XMLTag defendedOnDate : defendedOnDates) {
			buff.append(indent + "\t");
			buff.append(defendedOnDate.toString());
			buff.append("\n");
		}

		for (XMLTag publicReviewStartDate : publicReviewStartDates) {
			buff.append(indent + "\t");
			buff.append(publicReviewStartDate.toString());
			buff.append("\n");
		}

		for (XMLTag number : numbers) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag volume : volumes) {
			buff.append(indent + "\t");
			buff.append(volume.toString());
			buff.append("\n");
		}

		for (XMLTag issue : issues) {
			buff.append(indent + "\t");
			buff.append(issue.toString());
			buff.append("\n");
		}

		for (XMLTag edition : editions) {
			buff.append(indent + "\t");
			buff.append("<BookSeries>");
			buff.append("\n");
			buff.append(indent + "\t\t");
			buff.append(edition.toString());
			buff.append("\n");
			for (XMLTag issn : editionsISSN) {
				buff.append(indent + "\t\t");
				buff.append(issn.toString());
				buff.append("\n");
			}
			buff.append(indent + "\t");
			buff.append("</BookSeries>");
			buff.append("\n");
		}

		for (XMLTag volume : editionsNumber) {
			buff.append(indent + "\t");
			buff.append(volume.toString());
			buff.append("\n");
		}

		for (XMLTag startPage : startPages) {
			buff.append(indent + "\t");
			buff.append(startPage.toString());
			buff.append("\n");
		}

		for (XMLTag endPage : endPages) {
			buff.append(indent + "\t");
			buff.append(endPage.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfPages) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfChapters) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfReferences) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfTables) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfPictures) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfGraphs) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag number : numberOfAppendixes) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag doi : dois) {
			buff.append(indent + "\t");
			buff.append(doi.toString());
			buff.append("\n");
		}

		for (XMLTag scpNumber : scpNumbers) {
			buff.append(indent + "\t");
			buff.append(scpNumber.toString());
			buff.append("\n");
		}

		for (XMLTag issn : issns) {
			buff.append(indent + "\t");
			buff.append(issn.toString());
			buff.append("\n");
		}

		for (XMLTag isbn : isbns) {
			buff.append(indent + "\t");
			buff.append(isbn.toString());
			buff.append("\n");
		}

		for (XMLTag url : urls) {
			buff.append(indent + "\t");
			buff.append(url.toString());
			buff.append("\n");
		}

		for (XMLTag publisher : publishers) {
			buff.append(indent + "\t");
			buff.append("<Publisher>");
			buff.append("\n");
			buff.append(indent + "\t\t");
			buff.append(publisher.toString());
			buff.append("\n");
			for (XMLTag place : publishersPlaces) {
				buff.append(indent + "\t\t");
				buff.append(place.toString());
				buff.append("\n");
			}
			for (XMLTag state : publishersStates) {
				buff.append(indent + "\t\t");
				buff.append(state.toString());
				buff.append("\n");
			}
			buff.append(indent + "\t");
			buff.append("</Publisher>");
			buff.append("\n");
		}

		PublicationDTO publication = (PublicationDTO)record;
		if(publication.getAllAuthors().size()>0){
			buff.append(indent + "\t<Authors>\n");
			for(AuthorDTO author:publication.getAllAuthors()){
				buff.append(indent + "\t\t<Author>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(author.getName().getLastname() + ", " + author.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(author, indent+"\t\t\t", false, false));
				if (author.getInstitution() != null){
					buff.append("\n" + indent + "\t\t\t<Affiliation>\n");
					String institutionName = author.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((author.getInstitution() != null) && (author.getInstitution().getControlNumber() != null)
//						&& (! "".equals(author.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</Author>\n");
			}
			buff.append(indent + "\t</Authors>\n");
		}

		if(publication.getEditors().size()>0){
			buff.append("\n" + indent + "\t<Editors>\n");
			for(AuthorDTO editor:publication.getEditors()){
				buff.append(indent + "\t\t<Editor>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(editor.getName().getLastname() + ", " + editor.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(editor, indent+"\t\t\t", false, false));
				if (editor.getInstitution() != null){
					buff.append(indent + "\t\t\t<Affiliation>\n");
					String institutionName = editor.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(editor.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((editor.getInstitution() != null) && (editor.getInstitution().getControlNumber() != null)
//						&& (! "".equals(editor.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(editor.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</Editor>\n");
			}
			buff.append(indent + "\t</Editors>\n");
		}

		if(publication instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO thesis = (StudyFinalDocumentDTO)publication;
			buff.append(indent + "\t<Advisors>\n");
			for(AuthorDTO advisor:thesis.getAdvisors()){
				buff.append(indent + "\t\t<Advisor>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(advisor.getName().getLastname() + ", " + advisor.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(advisor, indent+"\t\t\t", false, true));
				if (advisor.getInstitution() != null){
					buff.append("\n" + indent + "\t\t\t<Affiliation>\n");
					String institutionName = advisor.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(advisor.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((editor.getInstitution() != null) && (editor.getInstitution().getControlNumber() != null)
//						&& (! "".equals(editor.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(editor.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</Advisor>\n");
			}
			buff.append(indent + "\t</Advisors>\n");
			buff.append(indent + "\t<BoardMembers>\n");
			for(AuthorDTO boardMember:thesis.getCommitteeMembers()){
				buff.append(indent + "\t\t<BoardMember>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(boardMember.getName().getLastname() + ", " + boardMember.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(boardMember, indent+"\t\t\t", false, true));
				if (boardMember.getInstitution() != null){
					buff.append("\n" + indent + "\t\t\t<Affiliation>\n");
					String institutionName = boardMember.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(boardMember.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((editor.getInstitution() != null) && (editor.getInstitution().getControlNumber() != null)
//						&& (! "".equals(editor.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(editor.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</BoardMember>\n");
			}
			buff.append(indent + "\t</BoardMembers>\n");
			if((thesis.getInstitution() != null) && (thesis.getInstitution().getControlNumber() != null)
					&& (! "".equals(thesis.getInstitution().getControlNumber()))) {
				buff.append(indent + "\t<Institutions>\n");
				buff.append(indent + "\t\t<Institution>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(thesis.getInstitution().getSomeName()) + "</DisplayName>\n");
				buff.append(orgUnitFromRecord(thesis.getInstitution(), indent+"\t\t\t", false));
				buff.append("\n" + indent + "\t\t</Institution>\n");
				buff.append(indent + "\t</Institutions>\n");
			}
		}

		//publishers

		//License
		//Subject

		for (XMLTag keyword : keywords) {
			buff.append(indent + "\t");
			buff.append(keyword.toString());
			buff.append("\n");
		}

		for (XMLTag abstracT : abstracts) {
			buff.append(indent + "\t");
			buff.append(abstracT.toString());
			buff.append("\n");
		}

		for (XMLTag extendedAbstracT : extendedAbstracts) {
			buff.append(indent + "\t");
			buff.append(extendedAbstracT.toString());
			buff.append("\n");
		}

		for (XMLTag note : notes) {
			buff.append(indent + "\t");
			buff.append(note.toString());
			buff.append("\n");
		}

		for (XMLTag researchArea : researchAreas) {
			buff.append(indent + "\t");
			buff.append(researchArea.toString());
			buff.append("\n");
		}

		for (XMLTag holdingData : holdingDatas) {
			buff.append(indent + "\t");
			buff.append(holdingData.toString());
			buff.append("\n");
		}

		for (XMLTag udc : udcs) {
			buff.append(indent + "\t");
			buff.append(udc.toString());
			buff.append("\n");
		}

		for (XMLTag levelOfEducation : levelOfEducations) {
			buff.append(indent + "\t");
			buff.append(levelOfEducation.toString());
			buff.append("\n");
		}

		//status

		if (record instanceof ProceedingsDTO){
			buff.append(indent + "\t<OutputFrom>\n");
			buff.append(eventFromRecord(((ProceedingsDTO)record).getConference(), indent+"\t\t", false));
			buff.append("\n" + indent + "\t</OutputFrom>\n");
		}
		//List<XMLTag> presentedAt = getEditions("Edition", record);

		for (XMLTag access : accesss) {
			buff.append(indent + "\t");
			buff.append(access.toString());
			buff.append("\n");
		}


		buff.append(indent + "</Publication>");
		return buff.toString();
	}

	/**
	 *
	 * @param record
	 *            Record Java Bean
	 * @param indent
	 *            tags indent
	 * @param xmlns
	 *            xmlns should be added
	 * @return created XML
	 *
	 */
	public String patentFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<Patent" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		List<XMLTag> types = getTypes("Type", record);
		List<XMLTag> titles = getTitles("Title", record, false);

		List<XMLTag> approvalDates = getPublicationDates("ApprovalDate", record);

		//publisher

		List<XMLTag> patentNumbers = getNumbers("PatentNumber", record);


		List<XMLTag> dois = getDOIs("DOI", record);
		List<XMLTag> urls = getURLs("URL", record);

		List<XMLTag> publishers = getPublishers("Name", record);
		List<XMLTag> publishersPlaces = getPublishersPlaces("Place", record);
		List<XMLTag> publishersStates = getPublishersStates("State", record);


		//inventors - authors

		List<XMLTag> abstracts = getAbstracts("Abstract", record, false);
		//Subject
		List<XMLTag> keywords = getKeywords("Keyword", record, false);

		List<XMLTag> accesss = getAccess("Access", record);


		for (XMLTag type : types) {
			buff.append(indent + "\t");
			buff.append(type.toString());
			buff.append("\n");
		}


		for (XMLTag title : titles) {
			buff.append(indent + "\t");
			buff.append(title.toString());
			buff.append("\n");
		}

		for (XMLTag approvalDate : approvalDates) {
			buff.append(indent + "\t");
			buff.append(approvalDate.toString());
			buff.append("\n");
		}

		for (XMLTag doi : dois) {
			buff.append(indent + "\t");
			buff.append(doi.toString());
			buff.append("\n");
		}

		for (XMLTag url : urls) {
			buff.append(indent + "\t");
			buff.append(url.toString());
			buff.append("\n");
		}

		//publishers


		for (XMLTag number : patentNumbers) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag publisher : publishers) {
			buff.append(indent + "\t");
			buff.append("<Publisher>");
			buff.append("\n");
			buff.append(indent + "\t\t");
			buff.append(publisher.toString());
			buff.append("\n");
			for (XMLTag place : publishersPlaces) {
				buff.append(indent + "\t\t");
				buff.append(place.toString());
				buff.append("\n");
			}
			for (XMLTag state : publishersStates) {
				buff.append(indent + "\t\t");
				buff.append(state.toString());
				buff.append("\n");
			}
			buff.append(indent + "\t");
			buff.append("</Publisher>");
			buff.append("\n");
		}

		//inventors - authors
		PublicationDTO publication = (PublicationDTO)record;
		if(publication.getAllAuthors().size()>0){
			buff.append(indent + "\t<Inventors>\n");
			for(AuthorDTO author:publication.getAllAuthors()){
				buff.append(indent + "\t\t<Inventor>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(author.getName().getLastname() + ", " + author.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(author, indent+"\t\t\t", false, false));
				if (author.getInstitution() != null){
					buff.append(indent + "\t\t\t<Affiliation>\n");
					String institutionName = author.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((author.getInstitution() != null) && (author.getInstitution().getControlNumber() != null)
//						&& (! "".equals(author.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</Inventor>\n");
			}
			buff.append(indent + "\t</Inventors>\n");
		}


		for (XMLTag abstracT : abstracts) {
			buff.append(indent + "\t");
			buff.append(abstracT.toString());
			buff.append("\n");
		}

		//Subject

		for (XMLTag keyword : keywords) {
			buff.append(indent + "\t");
			buff.append(keyword.toString());
			buff.append("\n");
		}

		for (XMLTag access : accesss) {
			buff.append(indent + "\t");
			buff.append(access.toString());
			buff.append("\n");
		}

		buff.append(indent + "</Patent>");
		return buff.toString();
	}

	/**
	 *
	 * @param record
	 *            Record Java Bean
	 * @param indent
	 *            tags indent
	 * @param xmlns
	 *            xmlns should be added
	 * @return created XML
	 *
	 */
	public String productFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<Product" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		List<XMLTag> types = getTypes("Type", record);
		List<XMLTag> languages = getLanguages("Language", record);
		List<XMLTag> names = getNames("Name", record, false);

		List<XMLTag> numbers = getNumbers("InternalNumber", record);


		List<XMLTag> publicationDates = getPublicationDates("PublicationDate", record);

		//versionInfo

		List<XMLTag> dois = getDOIs("DOI", record);
		List<XMLTag> urls = getURLs("URL", record);

		List<XMLTag> publishers = getPublishers("Name", record);
		List<XMLTag> publishersPlaces = getPublishersPlaces("Place", record);
		List<XMLTag> publishersStates = getPublishersStates("State", record);


		//creators - authors
		//publishers
		//license


		List<XMLTag> descriptions = getDescriptions("Description", record, false);
		//Subject
		List<XMLTag> keywords = getKeywords("Keyword", record, false);

		List<XMLTag> accesss = getAccess("Access", record);


		for (XMLTag type : types) {
			buff.append(indent + "\t");
			buff.append(type.toString());
			buff.append("\n");
		}

		for (XMLTag language : languages) {
			buff.append(indent + "\t");
			buff.append(language.toString());
			buff.append("\n");
		}

		for (XMLTag name : names) {
			buff.append(indent + "\t");
			buff.append(name.toString());
			buff.append("\n");
		}

		for (XMLTag number : numbers) {
			buff.append(indent + "\t");
			buff.append(number.toString());
			buff.append("\n");
		}

		for (XMLTag publicationDate : publicationDates) {
			buff.append(indent + "\t");
			buff.append(publicationDate.toString());
			buff.append("\n");
		}

		//versionInfo

		for (XMLTag doi : dois) {
			buff.append(indent + "\t");
			buff.append(doi.toString());
			buff.append("\n");
		}

		for (XMLTag url : urls) {
			buff.append(indent + "\t");
			buff.append(url.toString());
			buff.append("\n");
		}

		for (XMLTag publisher : publishers) {
			buff.append(indent + "\t");
			buff.append("<Publisher>");
			buff.append("\n");
			buff.append(indent + "\t\t");
			buff.append(publisher.toString());
			buff.append("\n");
			for (XMLTag place : publishersPlaces) {
				buff.append(indent + "\t\t");
				buff.append(place.toString());
				buff.append("\n");
			}
			for (XMLTag state : publishersStates) {
				buff.append(indent + "\t\t");
				buff.append(state.toString());
				buff.append("\n");
			}
			buff.append(indent + "\t");
			buff.append("</Publisher>");
			buff.append("\n");
		}

		//creators - authors
		PublicationDTO publication = (PublicationDTO)record;
		if(publication.getAllAuthors().size()>0){
			buff.append(indent + "\t<Creators>\n");
			for(AuthorDTO author:publication.getAllAuthors()){
				buff.append(indent + "\t\t<Creator>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(author.getName().getLastname() + ", " + author.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(author, indent+"\t\t\t", false, false));
				if (author.getInstitution() != null){
					buff.append(indent + "\t\t\t<Affiliation>\n");
					String institutionName = author.getInstitution().getSomeName();
					if (institutionName != null && institutionName.trim().length() != 0){
						buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
					}
					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t", false));
					buff.append(indent + "\t\t\t</Affiliation>\n");
				}
//				if((author.getInstitution() != null) && (author.getInstitution().getControlNumber() != null)
//						&& (! "".equals(author.getInstitution().getControlNumber()))){
//					buff.append(indent + "\t\t\t<Affiliation>\n");
//					buff.append(orgUnitFromRecord(author.getInstitution(), indent+"\t\t\t\t", false));
//					buff.append(indent + "\t\t\t</Affiliation>\n");
//				}
				buff.append("\n" + indent + "\t\t</Creator>\n");
			}
			buff.append(indent + "\t</Creators>\n");
		}
		//publishers
		//license

		for (XMLTag description : descriptions) {
			buff.append(indent + "\t");
			buff.append(description.toString());
			buff.append("\n");
		}

		//subject

		for (XMLTag keyword : keywords) {
			buff.append(indent + "\t");
			buff.append(keyword.toString());
			buff.append("\n");
		}

		for (XMLTag access : accesss) {
			buff.append(indent + "\t");
			buff.append(access.toString());
			buff.append("\n");
		}

		buff.append(indent + "</Product>");
		return buff.toString();
	}

	/**
	 * Creates MARC21slim XML from mARC21Record
	 *
	 * @param record
	 *            Record Java Bean
	 * @param indent
	 *            tags indent
	 * @param complete
	 *            whether the complete record is needed
	 * @return created XML
	 *
	 */
	public String personFromRecord(RecordDTO record, String indent, boolean complete, boolean boardMember) {
		StringBuffer buff = new StringBuffer(1024);
		if (!complete){
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			if (record instanceof AuthorDTO){
				if ((((AuthorDTO) record).getInstitution() == null
						|| ((AuthorDTO) record).getInstitution().getControlNumber() == null
						|| (!TeslaRISJDBCRecordFactory.customersInstitutions.contains(((AuthorDTO) record).getInstitution().getControlNumber()))) &&
						(! recordDAO.linksRecordsOfCertainTypes(TeslaRISJDBCRecordFactory.typesPHD, record.getControlNumber())))
				{
					return buff.toString();
				}
			}
		}
		buff.append(indent + "<Person" + (complete?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		if(complete) {
			List<XMLTag> personNames = getPersonNames(indent, "PersonName", record);
			List<XMLTag> genders = getGenders("Gender", record);
			List<XMLTag> birthDates = getDateOfBirth("DateOfBirth", record);
			List<XMLTag> birthPlaces = getPlaceOfBirth("PlaceOfBirth", record);
			List<XMLTag> birthCountries = getCountryOfBirth("CountryOfBirth", record);
			List<XMLTag> birthYears = getYearOfBirth("YearOfBirth", record);

			for (XMLTag personName : personNames) {
				buff.append(personName.toString());
				buff.append("\n");
			}


			for (XMLTag gender : genders) {
				buff.append(indent + "\t");
				buff.append(gender.toString());
				buff.append("\n");
			}

			for (XMLTag birthDate : birthDates) {
				buff.append(indent + "\t");
				buff.append(birthDate.toString());
				buff.append("\n");
			}

			for (XMLTag birthPlace : birthPlaces) {
				buff.append(indent + "\t");
				buff.append(birthPlace.toString());
				buff.append("\n");
			}

			for (XMLTag birthCountry : birthCountries) {
				buff.append(indent + "\t");
				buff.append(birthCountry.toString());
				buff.append("\n");
			}

			for (XMLTag birthYear : birthYears) {
				buff.append(indent + "\t");
				buff.append(birthYear.toString());
				buff.append("\n");
			}
		}

		if(complete || boardMember){
			List<XMLTag> titles = getAuthorTitles("Title", record);

			for (XMLTag title : titles) {
				buff.append(indent + "\t");
				buff.append(title.toString());
				buff.append("\n");
			}
		}

		List<XMLTag> ORCIDs = getORCIDs("ORCID", record);
		for (XMLTag ORCID : ORCIDs) {
			buff.append(indent + "\t");
			buff.append(ORCID.toString());
			buff.append("\n");
		}

		if (boardMember) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			AuthorPosition authorPosition = ((AuthorDTO)record).getCurrentPosition();
			if (authorPosition != null) {
				buff.append(indent + "\t<Positions>\n");
				buff.append(indent + "\t\t<Position>\n");
				if (authorPosition.getPosition() != null)
					buff.append(indent + "\t\t\t<Name>" + OAIUtil.xmlEncode(authorPosition.getPosition().getSomeTerm()) + "</Name>\n");
				if (authorPosition.getStartDate() != null) {
					String dateString = formatter.format(authorPosition.getStartDate().getTime());
					buff.append(indent + "\t\t\t<StartDate>" + OAIUtil.xmlEncode(dateString) + "</StartDate>\n");
				}
				if (authorPosition.getEndDate() != null) {
					String dateString = formatter.format(authorPosition.getEndDate().getTime());
					buff.append(indent + "\t\t\t<EndDate>" + OAIUtil.xmlEncode(dateString) + "</EndDate>\n");
				}
				if ((authorPosition.getResearchArea() != null) && (!"".equals(authorPosition.getResearchArea())))
					buff.append(indent + "\t\t\t<ResearchArea>" + OAIUtil.xmlEncode(authorPosition.getResearchArea()) + "</ResearchArea>\n");
				buff.append(indent + "\t\t</Position>\n");
				buff.append(indent + "\t</Positions>\n");
			}
		}

		if(complete){
			List<XMLTag> scopusIds = getScopusIds("ScopusAuthorID", record);
			List<XMLTag> apvnts = getAPVNTs("APVNT", record);
			List<XMLTag> jmbgs = getJMBG("JMBG", record);
			List<XMLTag> addressLines = getAddresses("AddressLine", record);
			List<XMLTag> places = getPlaces("Place", record);
			List<XMLTag> electronicAddresses = getElectronicAddresses("ElectronicAddress", record);


			for (XMLTag scopusId : scopusIds) {
				buff.append(indent + "\t");
				buff.append(scopusId.toString());
				buff.append("\n");
			}

			for (XMLTag apvnt : apvnts) {
				buff.append(indent + "\t");
				buff.append(apvnt.toString());
				buff.append("\n");
			}

			for (XMLTag jmbg : jmbgs) {
				buff.append(indent + "\t");
				buff.append(jmbg.toString());
				buff.append("\n");
			}

			for (XMLTag addressLine : addressLines) {
				buff.append(indent + "\t");
				buff.append(addressLine.toString());
				buff.append("\n");
			}

			for (XMLTag place : places) {
				buff.append(indent + "\t");
				buff.append(place.toString());
				buff.append("\n");
			}

			for (XMLTag electronicAddress : electronicAddresses) {
				buff.append(indent + "\t");
				buff.append(electronicAddress.toString());
				buff.append("\n");
			}

			if (((AuthorDTO)record).getAllPositions() != null && ((AuthorDTO)record).getAllPositions().size() > 0) {
				buff.append(indent + "\t<Positions>\n");
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				for (AuthorPosition authorPosition : ((AuthorDTO) record).getAllPositions()) {
					buff.append(indent + "\t\t<Position>\n");
					if (authorPosition.getPosition() != null)
						buff.append(indent + "\t\t\t<Name>" + OAIUtil.xmlEncode(authorPosition.getPosition().getSomeTerm()) + "</Name>\n");
					if (authorPosition.getStartDate() != null) {
						String dateString = formatter.format(authorPosition.getStartDate().getTime());
						buff.append(indent + "\t\t\t<StartDate>" + OAIUtil.xmlEncode(dateString) + "</StartDate>\n");
					}
					if (authorPosition.getEndDate() != null) {
						String dateString = formatter.format(authorPosition.getStartDate().getTime());
						buff.append(indent + "\t\t\t<EndDate>" + OAIUtil.xmlEncode(dateString) + "</EndDate>\n");
					}
					if ((authorPosition.getResearchArea() != null) && (!"".equals(authorPosition.getResearchArea())))
						buff.append(indent + "\t\t\t<ResearchArea>" + OAIUtil.xmlEncode(authorPosition.getResearchArea()) + "</ResearchArea>\n");
					buff.append(indent + "\t\t</Position>\n");
				}
				buff.append(indent + "\t</Positions>\n");
			}

			OrganizationUnitDTO orgUnit = ((AuthorDTO)record).getOrganizationUnit();
			InstitutionDTO institution = ((AuthorDTO)record).getInstitution();
			if((orgUnit != null) && (orgUnit.getControlNumber()!=null)
				&& (!"".equals(orgUnit.getControlNumber()))){
				buff.append(indent + "\t\t<Affiliation>\n");
				String institutionName = orgUnit.getSomeName();
				if (institutionName != null && institutionName.trim().length() != 0){
					buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
				}
				buff.append(orgUnitFromRecord(orgUnit, indent+"\t\t\t", false));
				buff.append("\n" + indent + "\t\t</Affiliation>\n");
			} else if ((institution != null) && (institution.getControlNumber()!=null)
						&& (!"".equals(institution.getControlNumber()))){
				buff.append(indent + "\t\t<Affiliation>\n");
				String institutionName = institution.getSomeName();
				if (institutionName != null && institutionName.trim().length() != 0){
					buff.append(indent + "\t\t\t\t<DisplayName>" + OAIUtil.xmlEncode(institutionName) + "</DisplayName>\n");
				}
				buff.append(orgUnitFromRecord(institution, indent+"\t\t\t", false));
				buff.append("\n" + indent + "\t\t</Affiliation>\n");
			}

			List<XMLTag> biographies = getBiographies("CV", record, false);
			List<XMLTag> keywords = getKeywords("Keyword", record, false);
			List<XMLTag> researchAreas = getResearchAreas("ResearchArea", record);

			for (XMLTag biography : biographies) {
				buff.append(indent + "\t");
				buff.append(biography.toString());
				buff.append("\n");
			}

			for (XMLTag keyword : keywords) {
				buff.append(indent + "\t");
				buff.append(keyword.toString());
				buff.append("\n");
			}

			for (XMLTag researchArea : researchAreas) {
				buff.append(indent + "\t");
				buff.append(researchArea.toString());
				buff.append("\n");
			}
		}

		buff.append(indent + "</Person>");
		return buff.toString();
	}

	/**
	 * Creates MARC21slim XML from mARC21Record
	 *
	 * @param record
	 *            Record Java Bean
	 * @param indent
	 *            tags indent
	 * @param complete
	 *            whether the complete record is needed
	 * @return created XML
	 *
	 */
	public String orgUnitFromRecord(RecordDTO record, String indent, boolean complete) {
		StringBuffer buff = new StringBuffer(1024);
		if (!complete){
			RecordDAO recordDAO = new RecordDAO(new RecordDB());
			if (record instanceof InstitutionDTO) {
				if ( record.getControlNumber() == null
						|| ((!TeslaRISJDBCRecordFactory.customersInstitutions.contains(record.getControlNumber()))) &&
						(!recordDAO.isLinkedByRecordsOfCertainTypes(TeslaRISJDBCRecordFactory.typesPHD, record.getControlNumber())) ) {
					return buff.toString();
				}
			}
		}
		buff.append(indent + "<OrgUnit" + (complete?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		if(complete) {
			List<XMLTag> names = getNames("Name", record, false);
			List<XMLTag> acronyms = getAcronyms("Acronym", record);
			List<XMLTag> places = getPlaces("Place", record);
			List<XMLTag> identifiers = getIdentifiers("Identifier", record);

			for (XMLTag acronym : acronyms) {
				buff.append(indent + "\t");
				buff.append(acronym.toString());
				buff.append("\n");
			}

			for (XMLTag name : names) {
				buff.append(indent + "\t");
				buff.append(name.toString());
				buff.append("\n");
			}

			for (XMLTag place : places) {
				buff.append(indent + "\t");
				buff.append(place.toString());
				buff.append("\n");
			}

			for (XMLTag identifier : identifiers) {
				buff.append(indent + "\t");
				buff.append(identifier.toString());
				buff.append("\n");
			}

			if (record instanceof OrganizationUnitDTO) {
				OrganizationUnitDTO orgUnit = (OrganizationUnitDTO) record;
				if ((orgUnit.getSuperOrganizationUnit() != null) && (orgUnit.getSuperOrganizationUnit().getControlNumber() != null)
						&& (!"".equals(orgUnit.getSuperOrganizationUnit().getControlNumber()))) {
					buff.append(indent + "\t<PartOf>");
					buff.append(orgUnitFromRecord(orgUnit.getSuperOrganizationUnit(), indent + "\t\t", false));
					buff.append("\n" + indent + "\t</PartOf>");
				} else if ((orgUnit.getInstitution() != null) && (orgUnit.getInstitution().getControlNumber() != null)
						&& (!"".equals(orgUnit.getInstitution().getControlNumber()))) {
					buff.append(indent + "\t<PartOf>");
					buff.append(orgUnitFromRecord(orgUnit.getInstitution(), indent + "\t\t", false));
					buff.append("\n" + indent + "\t</PartOf>");
				}
			} else if (record instanceof InstitutionDTO) {
				InstitutionDTO institution = (InstitutionDTO) record;
				if ((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber() != null)
						&& (!"".equals(institution.getSuperInstitution().getControlNumber()))) {
					buff.append(indent + "\t<PartOf>");
					buff.append(orgUnitFromRecord(institution.getSuperInstitution(), indent + "\t\t", false));
					buff.append("\n" + indent + "\t</PartOf>");
				}
			}

			List<XMLTag> keywords = getKeywords("Keyword", record, false);
			List<XMLTag> researchAreas = getResearchAreas("ResearchArea", record);

			for (XMLTag keyword : keywords) {
				buff.append(indent + "\t");
				buff.append(keyword.toString());
				buff.append("\n");
			}

			for (XMLTag researchArea : researchAreas) {
				buff.append(indent + "\t");
				buff.append(researchArea.toString());
				buff.append("\n");
			}
		}

		buff.append(indent + "</OrgUnit>");

		return buff.toString();
	}



	protected List<XMLTag> getAlternativeTitles(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getAlternativeTitle().getContent() != null){
				String lang = studyFinalDocument.getAlternativeTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getAlternativeTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				if((studyFinalDocument.getLanguage() != null) && (studyFinalDocument.getLanguage().equals("srp")) &&
						((studyFinalDocument.getAlphabet() != null) && (studyFinalDocument.getAlphabet().equals("cyrillic script")))){
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag)))
						retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO alternativeTitle : studyFinalDocument.getAlternativeTitleTranslations()) {
				if(alternativeTitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,alternativeTitle.getContent());
					String lang = alternativeTitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					if(lang != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						if(lang.equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							if(! retVal.contains(textTag)){
								retVal.add(textTag);
							}
							if(transliteration && (! latinTag.equals(textTag))){
								if(! retVal.contains(latinTag)){
									retVal.add(latinTag);
								}
							}
						}
					}
					if(! retVal.contains(textTag)){
						retVal.add(textTag);
					}
				}
			}
		}
		return retVal;
	}


	protected List<XMLTag> getAcronyms(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getNameAbbreviation().getContent() != null){
				String lang = journal.getNameAbbreviation().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = journal.getNameAbbreviation().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO nameAbbreviation : journal.getNameAbbreviationTranslations()) {
				if(nameAbbreviation.getContent()!=null){
					String lang = nameAbbreviation.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = nameAbbreviation.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getNameAbbreviation().getContent() != null){
				String lang = proceedings.getNameAbbreviation().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getNameAbbreviation().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO nameAbbreviation : proceedings.getNameAbbreviationTranslations()) {
				if(nameAbbreviation.getContent()!=null){
					String lang = nameAbbreviation.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = nameAbbreviation.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		}
		return retVal;
	}

	protected List<XMLTag> getPublicationDates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getPublicationDate() != null){
				dateString = formatter.format(studyFinalDocument.getPublicationDate().getTime());
				retVal.add(new XMLTag(tagName, dateString));
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if((patent.getPublicationYear() != null) && (! "".equals(patent.getPublicationYear()))){
				String year = ((patent.getPublicationYear().contains("/"))?patent.getPublicationYear().split("/")[1]:patent.getPublicationYear());
				if(year.trim().matches("\\d{4}"))
					retVal.add(new XMLTag(tagName, year.trim() + "-01-01"));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if((product.getPublicationYear() != null) && (! "".equals(product.getPublicationYear()))){
				String year = ((product.getPublicationYear().contains("/"))?product.getPublicationYear().split("/")[1]:product.getPublicationYear());
				if(year.trim().matches("\\d{4}"))
					retVal.add(new XMLTag(tagName, year.trim() + "-01-01"));
			}
		} else if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getYear() != null)
				retVal.add(new XMLTag(tagName, conference.getYear() + "-01-01"));
		} else if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			if((publication.getPublicationYear() != null) && (! "".equals(publication.getPublicationYear()))){
				String year = ((publication.getPublicationYear().contains("/"))?publication.getPublicationYear().split("/")[1]:publication.getPublicationYear());
				if(year.trim().matches("\\d{4}"))
					retVal.add(new XMLTag(tagName, year.trim()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getAcceptedOnDates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getAcceptedOn() != null){
				dateString = formatter.format(studyFinalDocument.getAcceptedOn().getTime());
				if(! retVal.contains(new XMLTag(tagName, dateString)))
					retVal.add(new XMLTag(tagName, dateString));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getDefendedOnDates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getDefendedOn() != null){
				dateString = formatter.format(studyFinalDocument.getDefendedOn().getTime());
				if(! retVal.contains(new XMLTag(tagName, dateString)))
					retVal.add(new XMLTag(tagName, dateString));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getPublicReviewStartDates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getPublicationDate() != null){
				dateString = formatter.format(studyFinalDocument.getPublicationDate().getTime());
				if(! retVal.contains(new XMLTag(tagName, dateString)))
					retVal.add(new XMLTag(tagName, dateString));
			}
		}
		return retVal;
	}



	protected List<XMLTag> getExtendedAbstracts(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getExtendedAbstract().getContent() != null){
				String lang = studyFinalDocument.getExtendedAbstract().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getExtendedAbstract().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				if((studyFinalDocument.getLanguage() != null) && (studyFinalDocument.getLanguage().equals("srp")) &&
						((studyFinalDocument.getAlphabet() != null) && (studyFinalDocument.getAlphabet().equals("cyrillic script")))){
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag)))
						retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO extendedAbstracT : studyFinalDocument.getExtendedAbstractTranslations()) {
				if(extendedAbstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,extendedAbstracT.getContent());
					String lang = extendedAbstracT.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					if(lang != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						if(lang.equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							if(! retVal.contains(textTag)){
								retVal.add(textTag);
							}
							if(transliteration && (! latinTag.equals(textTag))){
								if(! retVal.contains(latinTag)){
									retVal.add(latinTag);
								}
							}
						}
					}
					if(! retVal.contains(textTag)){
						retVal.add(textTag);
					}
				}
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNotes(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getNote().getContent() != null){
				String lang = journal.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = journal.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : journal.getNoteTranslations()) {
				if(note.getContent()!=null){
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getNote().getContent() != null){
				String lang = paperJournal.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperJournal.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : paperJournal.getNoteTranslations()) {
				if(note.getContent()!=null){
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getNote().getContent() != null){
				String lang = monograph.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = monograph.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : monograph.getNoteTranslations()) {
				if(note.getContent()!=null){
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getNote().getContent() != null){
				String lang = paperMonograph.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperMonograph.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : paperMonograph.getNoteTranslations()) {
				if(note.getContent()!=null){
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getNote().getContent() != null){
				String lang = proceedings.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : proceedings.getNoteTranslations()) {
				if(note.getContent()!=null){
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getNote().getContent() != null){
				String lang = paperProceedings.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperProceedings.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))){
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : paperProceedings.getNoteTranslations()) {
				if(note.getContent()!=null)
				{
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = note.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if(lang!=null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))){
						retVal.add(latinTag);
					}
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getNote().getContent() != null){
				String lang = studyFinalDocument.getNote().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
				}
				if((studyFinalDocument.getLanguage() != null) && (studyFinalDocument.getLanguage().equals("srp")) &&
						((studyFinalDocument.getAlphabet() != null) && (studyFinalDocument.getAlphabet().equals("cyrillic script")))){
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag)))
						retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO note : studyFinalDocument.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,note.getContent());
					String lang = note.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					if(lang != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						if(lang.equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							if(! retVal.contains(textTag)){
								retVal.add(textTag);
							}
							if(transliteration && (! latinTag.equals(textTag))){
								if(! retVal.contains(latinTag)){
									retVal.add(latinTag);
								}
							}
						}
					}
					if(! retVal.contains(textTag)){
						retVal.add(textTag);
					}
				}
			}
		}
		return retVal;
	}


	protected List<XMLTag> getKeywords(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getKeywords().getContent() != null){
				String lang = journal.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = journal.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : journal.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getKeywords().getContent() != null){
				String lang = paperJournal.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperJournal.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : paperJournal.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getKeywords().getContent() != null){
				String lang = monograph.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = monograph.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : monograph.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getKeywords().getContent() != null){
				String lang = paperMonograph.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperMonograph.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : paperMonograph.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getKeywords().getContent() != null){
				String lang = proceedings.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : proceedings.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getKeywords().getContent() != null){
				String lang = paperProceedings.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperProceedings.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : paperProceedings.getKeywordsTranslations()) {
				if(keyword.getContent()!=null)
				{
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getKeywords().getContent() != null)
			{
				String lang = patent.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = patent.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : patent.getKeywordsTranslations()) {
				if(keyword.getContent()!=null)
				{
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getKeywords().getContent() != null)
			{
				String lang = product.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = product.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : product.getKeywordsTranslations()) {
				if(keyword.getContent()!=null)
				{
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getKeywords().getContent() != null){
				String lang = studyFinalDocument.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : studyFinalDocument.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName,split);
							String lang = keyword.getLanguage();
							if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
								lang = defaultLanguage;
							if(lang != null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								if(lang.equals("srp")){
									XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
									latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
									if(! retVal.contains(textTag)){
										retVal.add(textTag);
									}
									if(transliteration && (! latinTag.equals(textTag))){
										if(! retVal.contains(latinTag)){
											retVal.add(latinTag);
										}
									}
								}
							}
							if(! retVal.contains(textTag)){
								retVal.add(textTag);
							}
						}
					}
				}
			}
		} else if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getKeywords().getContent() != null){
				String lang = conference.getKeywords().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = conference.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for(String split: splited){
					if(split.trim().length() > 0){
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if(lang!=null){
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))){
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : conference.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					String lang = keyword.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for(String split: splited){
						if(split.trim().length() > 0){
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if(lang!=null){
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))){
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof AuthorDTO) {
			AuthorDTO author = (AuthorDTO) record;
			if (author.getKeywords().getContent() != null) {
				String lang = author.getKeywords().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = author.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for (String split : splited) {
					if (split.trim().length() > 0) {
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if (lang != null) {
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))) {
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : author.getKeywordsTranslations()) {
				if (keyword.getContent() != null) {
					String lang = keyword.getLanguage();
					if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for (String split : splited) {
						if (split.trim().length() > 0) {
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if (lang != null) {
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))) {
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		} else if(record instanceof InstitutionDTO) {
			InstitutionDTO institution = (InstitutionDTO) record;
			if (institution.getKeywords().getContent() != null) {
				String lang = institution.getKeywords().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = institution.getKeywords().getContent();
				String[] splited = text.trim().split(",|;");
				for (String split : splited) {
					if (split.trim().length() > 0) {
						split = split.trim();
						XMLTag textTag = new XMLTag(tagName, split);
						String latin = LatCyrUtils.toLatin(split);
						XMLTag latinTag = new XMLTag(tagName, latin);
						if (lang != null) {
							textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
						}
						retVal.add(textTag);
						if(transliteration && (! latinTag.equals(textTag))) {
							retVal.add(latinTag);
						}
					}
				}
			}
			for (MultilingualContentDTO keyword : institution.getKeywordsTranslations()) {
				if (keyword.getContent() != null) {
					String lang = keyword.getLanguage();
					if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
						lang = defaultLanguage;
					String text = keyword.getContent();
					String[] splited = text.trim().split(",|;");
					for (String split : splited) {
						if (split.trim().length() > 0) {
							split = split.trim();
							XMLTag textTag = new XMLTag(tagName, split);
							String latin = LatCyrUtils.toLatin(split);
							XMLTag latinTag = new XMLTag(tagName, latin);
							if (lang != null) {
								textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
								latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
							}
							retVal.add(textTag);
							if(transliteration && (! latinTag.equals(textTag))) {
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		}
		return retVal;
	}

	protected List<XMLTag> getBiographies(String tagName, RecordDTO record, boolean transliteration) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO) {
			AuthorDTO author = (AuthorDTO) record;
			if (author.getBiography().getContent() != null) {
				String lang = author.getBiography().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = author.getBiography().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
				if(transliteration && (! latinTag.equals(textTag))) {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO biography : author.getBiographyTranslations()) {
				if (biography.getContent() != null) {
					String lang = biography.getLanguage();
					if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
						lang = defaultLanguage;
					String text = biography.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
					XMLTag latinTag = new XMLTag(tagName, latin);
					if (lang != null) {
						textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
						latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
					}
					retVal.add(textTag);
					if(transliteration && (! latinTag.equals(textTag))) {
						retVal.add(latinTag);
					}
				}
			}
		}
		return retVal;
	}


	protected List<XMLTag> getResearchAreas(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO) {
			AuthorDTO author = (AuthorDTO) record;
			for(ResearchAreaDTO researchArea:author.getResearchAreas()){
				if (researchArea.getFullHierarchy() != null) {
					String text = researchArea.getFullHierarchy();
					XMLTag textTag = new XMLTag(tagName, text);
					retVal.add(textTag);
				}
			}
		} else if(record instanceof InstitutionDTO) {
			InstitutionDTO institution = (InstitutionDTO) record;
			for(ResearchAreaDTO researchArea:institution.getResearchAreas()){
				if (researchArea.getFullHierarchy() != null) {
					String text = researchArea.getFullHierarchy();
					XMLTag textTag = new XMLTag(tagName, text);
					retVal.add(textTag);
				}
			}
		} else if(record instanceof MonographDTO) {
			MonographDTO monograph = (MonographDTO) record;
			ResearchAreaDTO researchArea = monograph.getResearchArea();
			if (researchArea.getFullHierarchy() != null) {
				String text = researchArea.getFullHierarchy();
				XMLTag textTag = new XMLTag(tagName, text);
				retVal.add(textTag);
			}
		} else if(record instanceof StudyFinalDocumentDTO) {
			StudyFinalDocumentDTO studyFinalDocumentDTO = (StudyFinalDocumentDTO) record;
			ResearchAreaDTO researchArea = studyFinalDocumentDTO.getResearchArea();
			if (researchArea.getFullHierarchy() != null) {
				String text = researchArea.getFullHierarchy();
				XMLTag textTag = new XMLTag(tagName, text);
				retVal.add(textTag);
			}
		}
		return retVal;
	}

	protected List<XMLTag> getHoldingDatas(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getHoldingData() != null){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getHoldingData()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getUDCs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getUdc() != null){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getUdc()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getLevelOfEducations(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLevelOfEducation() != null){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getLevelOfEducation()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getEditionsISSN(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getEditionISSN() != null){
				retVal.add(new XMLTag(tagName, monograph.getEditionISSN()));
			}
		}
		return retVal;
	}
	protected List<XMLTag> getEditionsNumber(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getEditionNumber() != null){
				retVal.add(new XMLTag(tagName, "" + monograph.getEditionNumber()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getEditionNumber() != null){
				retVal.add(new XMLTag(tagName, "" + proceedings.getEditionNumber()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getPublishers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher().getName() != null)
			){
				String lang = monograph.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = monograph.getPublisher().getOriginalPublisher().getName();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if((proceedings.getPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher().getName() != null)
			){
				String lang = proceedings.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getPublisher().getOriginalPublisher().getName();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher().getName() != null)
			){
				String lang = studyFinalDocument.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getPublisher().getOriginalPublisher().getName();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if((patent.getPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher().getName() != null)
			){
				String lang = patent.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = patent.getPublisher().getOriginalPublisher().getName();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if((product.getPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher().getName() != null)
			){
				String lang = product.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = product.getPublisher().getOriginalPublisher().getName();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		}
		return retVal;
	}
	protected List<XMLTag> getPublishersPlaces(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher().getPlace() != null)
			){
				String lang = monograph.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = monograph.getPublisher().getOriginalPublisher().getPlace();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if((proceedings.getPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher().getPlace() != null)
			){
				String lang = proceedings.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getPublisher().getOriginalPublisher().getPlace();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher().getPlace() != null)
			){
				String lang = studyFinalDocument.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getPublisher().getOriginalPublisher().getPlace();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if((patent.getPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher().getPlace() != null)
			){
				String lang = patent.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = patent.getPublisher().getOriginalPublisher().getPlace();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if((product.getPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher().getPlace() != null)
			){
				String lang = product.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = product.getPublisher().getOriginalPublisher().getPlace();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		}
		return retVal;
	}

	protected List<XMLTag> getPublishersStates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher() != null)
					&& (monograph.getPublisher().getOriginalPublisher().getState() != null)
			){
				String lang = monograph.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = monograph.getPublisher().getOriginalPublisher().getState();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if((proceedings.getPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher() != null)
					&& (proceedings.getPublisher().getOriginalPublisher().getState() != null)
			){
				String lang = proceedings.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getPublisher().getOriginalPublisher().getState();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher() != null)
					&& (studyFinalDocument.getPublisher().getOriginalPublisher().getState() != null)
			){
				String lang = studyFinalDocument.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getPublisher().getOriginalPublisher().getState();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if((patent.getPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher() != null)
					&& (patent.getPublisher().getOriginalPublisher().getState() != null)
			){
				String lang = patent.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = patent.getPublisher().getOriginalPublisher().getState();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if((product.getPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher() != null)
					&& (product.getPublisher().getOriginalPublisher().getState() != null)
			){
				String lang = product.getPublisher().getOriginalPublisher().getLanguage();
				if ((lang == null) || ("||".equals(lang.substring(0, 2).trim())))
					lang = defaultLanguage;
				String text = product.getPublisher().getOriginalPublisher().getState();
				XMLTag textTag = new XMLTag(tagName, text);
				if (lang != null) {
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0, 2).trim()));
				}
				retVal.add(textTag);
			}
		}
		return retVal;
	}


	protected List<XMLTag> getNumberOfPages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getNumberOfPages() != null){
				retVal.add(new XMLTag(tagName, monograph.getNumberOfPages().toString()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getNumberOfPages() != null){
				retVal.add(new XMLTag(tagName, proceedings.getNumberOfPages().toString()));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfPages() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfPages().toString()));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paper = (PaperProceedingsDTO)record;
			if(paper.getNumberOfPages() != null){
				retVal.add(new XMLTag(tagName, "" + paper.getNumberOfPages()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfChapters(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfChapters() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfChapters().toString()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfReferences(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfReferences() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfReferences().toString()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfTables(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfTables() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfTables().toString()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfPictures(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfPictures() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfPictures().toString()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfGraphs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfGraphs() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfGraphs().toString()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getNumberOfAppendixes(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfAppendixes() != null)){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getPhysicalDescription().getNumberOfAppendixes().toString()));
			}
		}
		return retVal;
	}


	protected List<XMLTag> getPlaces(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if((conference.getPlace() != null) && (! "".equals(conference.getPlace()))){
				retVal.add(new XMLTag(tagName, conference.getPlace()));
			}
		} else if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getCity() != null) && (! "".equals(author.getCity()))){
				retVal.add(new XMLTag(tagName, author.getCity()));
			}
		} else if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if((institution.getPlace() != null) && (! "".equals(institution.getPlace()))){
				retVal.add(new XMLTag(tagName, institution.getPlace()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getCountries(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if((conference.getState() != null) && (! "".equals(conference.getState()))){
				retVal.add(new XMLTag(tagName, conference.getState()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getAddresses(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getAddress() != null) && (! "".equals(author.getAddress()))){
				retVal.add(new XMLTag(tagName, author.getAddress()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getIdentifiers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if((institution.getUri() != null) && (! "".equals(institution.getUri()))){
				XMLTag typeTag = new XMLTag(tagName, institution.getUri());
				typeTag.getAttributes().add(new AttributeValue("type", ""));
				retVal.add(typeTag);
			}
		} else if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getUri() != null) && (! "".equals(author.getUri()))){
				XMLTag typeTag = new XMLTag(tagName, author.getUri());
				typeTag.getAttributes().add(new AttributeValue("type", ""));
				retVal.add(typeTag);
			}
		}
		return retVal;
	}

	protected List<XMLTag> getAPVNTs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getApvnt() != null) && (! "".equals(author.getApvnt()))){
				retVal.add(new XMLTag(tagName, author.getApvnt()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getAuthorTitles(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getTitle() != null) && (! "".equals(author.getTitle()))){
				retVal.add(new XMLTag(tagName, author.getTitle()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getDateOfBirth(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(author.getDateOfBirth() != null) {
				String dateString = formatter.format(author.getDateOfBirth().getTime());
				retVal.add(new XMLTag(tagName, dateString));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getPlaceOfBirth(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getPlaceOfBirth() != null) && (! "-".equals(author.getPlaceOfBirth().trim()))){
				retVal.add(new XMLTag(tagName, author.getPlaceOfBirth()));
			}
		}
		return retVal;
	}

	protected List<XMLTag> getCountryOfBirth(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO) {
			AuthorDTO author = (AuthorDTO) record;
			if ((author.getState() != null) && (!"-".equals(author.getState()))) {
				retVal.add(new XMLTag(tagName, author.getState()));
			}
		}
		return retVal;
	}


	protected List<XMLTag> getYearOfBirth(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getYearOfBirth() != null){
				retVal.add(new XMLTag(tagName, author.getYearOfBirth().toString()));
			}
		}
		return retVal;
	}


	protected List<XMLTag> getJMBG(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getJmbg() != null) && (! "".equals(author.getJmbg()))){
				retVal.add(new XMLTag(tagName, author.getJmbg()));
			}
		}
		return retVal;
	}

	private static Log log = LogFactory.getLog(TeslaRISXMLSerializer.class
			.getName());

}