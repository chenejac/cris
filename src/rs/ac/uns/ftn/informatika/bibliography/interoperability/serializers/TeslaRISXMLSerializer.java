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
		List<XMLTag> titles = getTitles("Title", record);
		List<XMLTag> subtitles = getSubtitles("Subtitle", record);


		List<XMLTag> publicationDates = getPublicationDates("PublicationDate", record);
		List<XMLTag> numbers = getNumbers("Number", record);
		List<XMLTag> volumes = getVolumes("Volume", record);
		List<XMLTag> issues = getIssues("Issue", record);
		List<XMLTag> editions = getEditions("Edition", record);
		List<XMLTag> startPages = getStartPages("StartPage", record);
		List<XMLTag> endPages = getEndPages("EndPage", record);

		List<XMLTag> dois = getDOIs("DOI", record);
		List<XMLTag> scpNumbers = getScopusIds("SCP-Number", record);
		List<XMLTag> issns = getIssns("ISSN", record);
		List<XMLTag> isbns = getIsbns("ISBN", record);
		List<XMLTag> urls = getURLs("URL", record);

		//License
		//Subject

		List<XMLTag> keywords = getKeywords("Keyword", record);
		List<XMLTag> abstracts = getAbstracts("Abstract", record);

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
			buff.append(edition.toString());
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
				buff.append(indent + "\t<Publishers>\n");
				buff.append(indent + "\t\t<Publisher>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(thesis.getInstitution().getSomeName()) + "</DisplayName>\n");
				buff.append(orgUnitFromRecord(thesis.getInstitution(), indent+"\t\t\t", false));
				buff.append("\n" + indent + "\t\t</Publisher>\n");
				buff.append(indent + "\t</Publishers>\n");
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
		List<XMLTag> titles = getTitles("Title", record);

		List<XMLTag> approvalDates = getPublicationDates("ApprovalDate", record);

		//publisher

		List<XMLTag> patentNumbers = getNumbers("PatentNumber", record);

		//inventors - authors

		List<XMLTag> abstracts = getAbstracts("Abstract", record);
		//Subject
		List<XMLTag> keywords = getKeywords("Keyword", record);

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

		//publishers


		for (XMLTag number : patentNumbers) {
			buff.append(indent + "\t");
			buff.append(number.toString());
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
		List<XMLTag> names = getNames("Name", record);

		//versionInfo

		List<XMLTag> dois = getDOIs("DOI", record);
		List<XMLTag> urls = getURLs("URL", record);

		//creators - authors
		//publishers
		//license


		List<XMLTag> descriptions = getDescriptions("Description", record);
		//Subject
		List<XMLTag> keywords = getKeywords("Keyword", record);

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

			List<XMLTag> biographies = getBiographies("CV", record);
			List<XMLTag> keywords = getKeywords("Keyword", record);
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
			List<XMLTag> names = getNames("Name", record);
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

			List<XMLTag> keywords = getKeywords("Keyword", record);
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

	protected List<XMLTag> getKeywords(String tagName, RecordDTO record) {
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
									if(! latinTag.equals(textTag)){
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
						if(! latinTag.equals(textTag)){
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
							if(! latinTag.equals(textTag)){
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
						if (!latinTag.equals(textTag)) {
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
							if (!latinTag.equals(textTag)) {
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
						if (!latinTag.equals(textTag)) {
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
							if (!latinTag.equals(textTag)) {
								retVal.add(latinTag);
							}
						}
					}
				}
			}
		}
		return retVal;
	}

	protected List<XMLTag> getBiographies(String tagName, RecordDTO record) {
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
				if (!latinTag.equals(textTag)) {
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
					if (!latinTag.equals(textTag)) {
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