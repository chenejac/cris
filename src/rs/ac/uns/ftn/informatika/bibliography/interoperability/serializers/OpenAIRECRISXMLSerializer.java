/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;
import ORG.oclc.oai.util.OAIUtil;

import com.gint.util.xml.XMLUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to https://github.com/openaire/guidelines-cris-managers/blob/master/schemas/openaire-cerif-profile.xsd XMLSchema
 * 
 * 
 */
public class OpenAIRECRISXMLSerializer implements Serializer {

	protected List<String> languages;
	protected String source;
	protected String defaultLanguage = "  ";//MultilingualContentDTO.LANGUAGE_ENGLISH;
	
	/**
	 * @param languages
	 * @param source
	 */
	public OpenAIRECRISXMLSerializer(List<String> languages, String source){
		this.languages = languages;
		this.source = source;
	}
	
	/**
	 */
	public OpenAIRECRISXMLSerializer(){
		this.languages = Arrays.asList("en");
		this.source = "OpenAIRE";
	}
	
	protected static SAXParserFactory factory;
	

	static {
		try {
			factory = SAXParserFactory.newInstance();
		} catch (Exception e) {
		}
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
			return personFromRecord(record, indent, true);
		else if(record instanceof InstitutionDTO)
			return orgUnitFromRecord(record, indent, true);
		else if(record instanceof ConferenceDTO)
			return eventFromRecord(record, indent, true);
		else 
			return "";
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
				buff.append(personFromRecord(author, indent+"\t\t\t", false));
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
			buff.append(indent + "\t<Editors>\n");
			for(AuthorDTO editor:publication.getEditors()){
				buff.append(indent + "\t\t<Editor>\n");
				buff.append(indent + "\t\t\t<DisplayName>" + OAIUtil.xmlEncode(editor.getName().getLastname() + ", " + editor.getName().getFirstname()) + "</DisplayName>\n");
				buff.append(personFromRecord(editor, indent+"\t\t\t", false));
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
				buff.append(personFromRecord(author, indent+"\t\t\t", false));
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
				buff.append(personFromRecord(author, indent+"\t\t\t", false));
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
		 * @param xmlns
	 *            xmlns should be added
	 * @return created XML
	 * 
	 */
	public String personFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<Person" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		if(xmlns){
			List<XMLTag> personNames = getPersonNames(indent, "PersonName", record);		
			List<XMLTag> genders = getGenders("Gender", record);
	
			for (XMLTag personName : personNames) {
				buff.append(personName.toString());
				buff.append("\n");
			}
			
			
			for (XMLTag gender : genders) {
				buff.append(indent + "\t");
				buff.append(gender.toString());
				buff.append("\n");
			}
		}	
		
		List<XMLTag> ORCIDs = getORCIDs("ORCID", record);
		for (XMLTag ORCID : ORCIDs) {
			buff.append(indent + "\t");
			buff.append(ORCID.toString());
			buff.append("\n");
		}
	
		if(xmlns){
			List<XMLTag> scopusIds = getScopusIds("ScopusAuthorID", record);
			List<XMLTag> electronicAddresses = getElectronicAddresses("ElectronicAddress", record);
			
			for (XMLTag scopusId : scopusIds) {
			    buff.append(indent + "\t");
			    buff.append(scopusId.toString());
			    buff.append("\n");
			}
		
			
			for (XMLTag electronicAddress : electronicAddresses) {
			    buff.append(indent + "\t");
			    buff.append(electronicAddress.toString());
			    buff.append("\n");
			}
		
			OrganizationUnitDTO orgUnit = ((AuthorDTO)record).getOrganizationUnit();
			InstitutionDTO institution = ((AuthorDTO)record).getInstitution();
			if ((institution.getControlNumber()!=null)
					&& (!"".equals(institution.getControlNumber())) && ("(BISIS)5929".equals(institution.getControlNumber()))){
				if((orgUnit != null) && (orgUnit.getControlNumber()!=null)
						&& (!"".equals(orgUnit.getControlNumber()))){
					buff.append(indent + "\t\t<Affiliation>\n");	
					buff.append(orgUnitFromRecord(orgUnit, indent+"\t\t\t", false));
					buff.append("\n" + indent + "\t\t</Affiliation>\n");
				} else if ((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber()!=null)
						&& (!"".equals(institution.getSuperInstitution().getControlNumber()))){
					buff.append(indent + "\t\t<Affiliation>\n");
					buff.append(orgUnitFromRecord(institution, indent+"\t\t\t", false));
					buff.append("\n" + indent + "\t\t</Affiliation>\n");
				}
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
	 * @param xmlns
	 *            xmlns should be added
	 * @return created XML
	 * 
	 */
	public String orgUnitFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<OrgUnit" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
//		if(xmlns){
			List<XMLTag> names = getNames("Name", record);
			List<XMLTag> acronyms = getAcronyms("Acronym", record);
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
			
			for (XMLTag identifier : identifiers) {
			    buff.append(indent + "\t");
			    buff.append(identifier.toString());
			    buff.append("\n");
			}
//		}	else {
//			XMLTag name = new XMLTag("Name", ((InstitutionDTO)record).getSomeName());
//			name.getAttributes().add(new AttributeValue("xml:lang", "sr"));
//			buff.append(indent + "\t");
//			buff.append(name.toString());
//			buff.append("\n");
//		}
		
		if(record instanceof OrganizationUnitDTO){
			OrganizationUnitDTO orgUnit = (OrganizationUnitDTO)record;
			if((orgUnit.getSuperOrganizationUnit() != null) && (orgUnit.getSuperOrganizationUnit().getControlNumber()!=null)
					&& (!"".equals(orgUnit.getSuperOrganizationUnit().getControlNumber()))){
				buff.append(indent + "\t<PartOf>");
				buff.append(orgUnitFromRecord(orgUnit.getSuperOrganizationUnit(), indent+"\t\t", false));
				buff.append("\n" + indent + "\t</PartOf>");
			} else if ((orgUnit.getInstitution() != null) && (orgUnit.getInstitution().getControlNumber()!=null)
					&& (!"".equals(orgUnit.getInstitution().getControlNumber()))){
				buff.append(indent + "\t<PartOf>");
				buff.append(orgUnitFromRecord(orgUnit.getInstitution(), indent+"\t\t", false));
				buff.append("\n" + indent + "\t</PartOf>");
			}
		} else if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
		   if ((institution.getSuperInstitution() != null) && (institution.getSuperInstitution().getControlNumber()!=null)
					&& (!"".equals(institution.getSuperInstitution().getControlNumber()))){
				buff.append(indent + "\t<PartOf>");
				buff.append(orgUnitFromRecord(institution.getSuperInstitution(), indent+"\t\t", false));
				buff.append("\n" + indent + "\t</PartOf>");
			}
		} 
		
		buff.append(indent + "</OrgUnit>");
		return buff.toString();
    }
	
	/**
	 * Creates MARC21slim XML from mARC21Record
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
	public String eventFromRecord(RecordDTO record, String indent, boolean xmlns) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<Event" + (xmlns?" xmlns=\"https://www.openaire.eu/cerif-profile/1.1/\"":"") + " id=\"" + record.getControlNumber() + "\">\n");
		
//		if(xmlns){
			List<XMLTag> types = getTypes("Type", record);
			List<XMLTag> names = getNames("Name", record);
			
			
			List<XMLTag> places = getPlaces("Place", record);
			List<XMLTag> countries = getCountries("Country", record);		
			
			List<XMLTag> startYears = getPublicationDates("StartDate", record);
			List<XMLTag> endYears = getPublicationDates("EndDate", record);
			
			List<XMLTag> descriptions = getDescriptions("Description", record);
			//subject
			List<XMLTag> keywords = getKeywords("Keyword", record);
			
		
			for (XMLTag type : types) {
			    buff.append(indent + "\t");
			    buff.append(type.toString());
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
			
			for (XMLTag country : countries) {
			    buff.append(indent + "\t");
			    buff.append(country.toString());
			    buff.append("\n");
			}
			
			for (XMLTag startYear : startYears) {
			    buff.append(indent + "\t");
			    buff.append(startYear.toString());
			    buff.append("\n");
			}
			
			for (XMLTag endYear : endYears) {
			    buff.append(indent + "\t");
			    buff.append(endYear.toString());
			    buff.append("\n");
			}
			
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
//		}
		
		buff.append(indent + "</Event>");
		return buff.toString();
    }

	
	protected List<XMLTag> getTitles(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getName().getContent() != null){
				String lang = journal.getName().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = journal.getName().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO name : journal.getNameTranslations()) {
				if(name.getContent()!=null){
					String lang = name.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = name.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getTitle().getContent() != null){
				String lang = paperJournal.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperJournal.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : paperJournal.getTitleTranslations()) {
				if(title.getContent()!=null){
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getTitle().getContent() != null){
				String lang = monograph.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = monograph.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : monograph.getTitleTranslations()) {
				if(title.getContent()!=null){
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getTitle().getContent() != null){
				String lang = paperMonograph.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperMonograph.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : paperMonograph.getTitleTranslations()) {
				if(title.getContent()!=null){
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getTitle().getContent() != null){
				String lang = proceedings.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : proceedings.getTitleTranslations()) {
				if(title.getContent()!=null){
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getTitle().getContent() != null){
				String lang = paperProceedings.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperProceedings.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : paperProceedings.getTitleTranslations()) {
				if(title.getContent()!=null)
				{
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getTitle().getContent() != null)
			{
				String lang = patent.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = patent.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO title : patent.getTitleTranslations()) {
				if(title.getContent()!=null)
				{
					String lang = title.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = title.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getTitle().getContent() != null){
				String lang = studyFinalDocument.getTitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getTitle().getContent();
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
						if(! latinTag.equals(textTag))
							retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO title : studyFinalDocument.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,title.getContent());
					String lang = title.getLanguage();
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
		return retVal;
	}
	
	protected List<XMLTag> getSubtitles(String tagName, RecordDTO record) {
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
				if(! latinTag.equals(textTag)){
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
					if(! latinTag.equals(textTag)){
						retVal.add(latinTag);
					} 
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getSubtitle().getContent() != null){
				String lang = paperJournal.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperJournal.getSubtitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO subtitle : paperJournal.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					String lang = subtitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = subtitle.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getSubtitle().getContent() != null){
				String lang = monograph.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = monograph.getSubtitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO subtitle : monograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					String lang = subtitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = subtitle.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getSubtitle().getContent() != null){
				String lang = paperMonograph.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperMonograph.getSubtitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO subtitle : paperMonograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					String lang = subtitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = subtitle.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getSubtitle().getContent() != null){
				String lang = proceedings.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = proceedings.getSubtitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO subtitle : proceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					String lang = subtitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = subtitle.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getSubtitle().getContent() != null){
				String lang = paperProceedings.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperProceedings.getSubtitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO subtitle : paperProceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					String lang = subtitle.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = subtitle.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getSubtitle().getContent() != null){
				String lang = studyFinalDocument.getSubtitle().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getSubtitle().getContent();
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
						if(! latinTag.equals(textTag))
							retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO subtitle : studyFinalDocument.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,subtitle.getContent());
					String lang = subtitle.getLanguage();
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
		return retVal;
	}
	
	protected List<XMLTag> getPublicationDates(String tagName, RecordDTO record) {
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
			else if(studyFinalDocument.getPublicationDate() != null){
				dateString = formatter.format(studyFinalDocument.getPublicationDate().getTime());
				retVal.add(new XMLTag(tagName, dateString));
			} 
			else if(studyFinalDocument.getAcceptedOn() != null){
				dateString = formatter.format(studyFinalDocument.getAcceptedOn().getTime());
				if(! retVal.contains(new XMLTag(tagName, dateString)))
					retVal.add(new XMLTag(tagName, dateString));
			} 
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if((patent.getPublicationYear() != null) && (! "".equals(patent.getPublicationYear()))){
				String year = ((patent.getPublicationYear().contains("/"))?patent.getPublicationYear().split("/")[1]:patent.getPublicationYear());
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
	
	protected List<XMLTag> getNumbers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getNumber() != null)
				retVal.add(new XMLTag(tagName, patent.getNumber()));
		}  
		return retVal;
	}
	
	protected List<XMLTag> getVolumes(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getVolumeTitle() != null){
				retVal.add(new XMLTag(tagName, monograph.getVolumeTitle()));
			}
		} else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getVolume() != null){
				retVal.add(new XMLTag(tagName, paperJournal.getVolume()));
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getIssues(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getVolumeCode() != null){
				retVal.add(new XMLTag(tagName, monograph.getVolumeCode()));
			}
		} else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getNumber() != null){
				retVal.add(new XMLTag(tagName, paperJournal.getNumber()));
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getEditions(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getEditionTitle() != null){
				retVal.add(new XMLTag(tagName, monograph.getEditionTitle()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getEditionTitle() != null){
				retVal.add(new XMLTag(tagName, proceedings.getEditionTitle()));
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getStartPages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getNumberOfPages() != null){
				retVal.add(new XMLTag(tagName, "1"));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getNumberOfPages() != null){
				retVal.add(new XMLTag(tagName, "1"));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getPhysicalDescription() != null) && (studyFinalDocument.getPhysicalDescription().getNumberOfPages() != null)){
				retVal.add(new XMLTag(tagName, "1"));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paper = (PaperProceedingsDTO)record;
			if(paper.getStartPage() != null){
				retVal.add(new XMLTag(tagName, paper.getStartPage()));
			}
		}  else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paper = (PaperJournalDTO)record;
			if(paper.getStartPage() != null){
				retVal.add(new XMLTag(tagName, paper.getStartPage()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paper = (PaperMonographDTO)record;
			if(paper.getStartPage() != null){
				retVal.add(new XMLTag(tagName, paper.getStartPage()));
			}
		} 
		
		return retVal;
	}
	
	protected List<XMLTag> getEndPages(String tagName, RecordDTO record) {
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
			if(paper.getEndPage() != null){
				retVal.add(new XMLTag(tagName, paper.getEndPage()));
			}
		}  else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paper = (PaperJournalDTO)record;
			if(paper.getEndPage() != null){
				retVal.add(new XMLTag(tagName, paper.getEndPage()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paper = (PaperMonographDTO)record;
			if(paper.getEndPage() != null){
				retVal.add(new XMLTag(tagName, paper.getEndPage()));
			}
		} 
		
		return retVal;
	}
	
	protected List<XMLTag> getDOIs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getDoi() != null) && (! "".equals(monograph.getDoi()))){
				String correctDoi = null;
				String[] splited = monograph.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if((proceedings.getDoi() != null) && (! "".equals(proceedings.getDoi()))){
				String correctDoi = null;
				String[] splited = proceedings.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getDoi() != null) && (! "".equals(studyFinalDocument.getDoi()))){
				String correctDoi = null;
				String[] splited = studyFinalDocument.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paper = (PaperProceedingsDTO)record;
			if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
				String correctDoi = null;
				String[] splited = paper.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		}  else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paper = (PaperJournalDTO)record;
			if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
				String correctDoi = null;
				String[] splited = paper.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paper = (PaperMonographDTO)record;
			if((paper.getDoi() != null) && (! "".equals(paper.getDoi()))){
				String correctDoi = null;
				String[] splited = paper.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if((product.getDoi() != null) && (! "".equals(product.getDoi()))){
				String correctDoi = null;
				String[] splited = product.getDoi().trim().split(":|\\s");
				for(String split: splited)
					if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
						correctDoi = split;
						break;
					}
				if(correctDoi != null)
					retVal.add(new XMLTag(tagName, correctDoi));
			}
		} 
		
		return retVal;
	}
	
	protected List<XMLTag> getIssns(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getEditionISSN() != null) && (! "".equals(monograph.getEditionISSN())) && (! "NN".equals(monograph.getEditionISSN()))){
				String[] splited = monograph.getEditionISSN().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
				for(String split: splited)
					if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
						retVal.add(new XMLTag(tagName, split));
					}
			}
		} else if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if((journal.getIssn() != null) && (! "".equals(journal.getIssn())) && (! "NN".equals(journal.getIssn()))){
				String[] splited = journal.getIssn().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
				for(String split: splited)
					if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
						retVal.add(new XMLTag(tagName, split));
					}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getIsbns(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if((monograph.getIsbn() != null) && (! "".equals(monograph.getIsbn()))){ 
				String split = monograph.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
				if((((split.matches("978-\\d+-\\d+-\\d+-\\d")) 
					|| (split.matches("978 \\d+ \\d+ \\d+ \\d")) 
					|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
					|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
					|| (((split.matches("978\\d{10}"))
					|| (split.matches("979[1-9]\\d{9}"))
					|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
					|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
					|| (split.matches("\\d{9}[\\dX]"))){
					retVal.add(new XMLTag(tagName, split));
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if((proceedings.getIsbn() != null) && (! "".equals(proceedings.getIsbn()))){
				String split = proceedings.getIsbn().toUpperCase().replace("ISBN:", "").replace("ISBN", "").replace(";", "").trim();
				if((((split.matches("978-\\d+-\\d+-\\d+-\\d")) 
						|| (split.matches("978 \\d+ \\d+ \\d+ \\d")) 
						|| (split.matches("979-[1-9]\\d*-\\d+-\\d+-\\d"))
						|| (split.matches("979 [1-9]\\d* \\d+ \\d+ \\d"))) && (split.length() == 17))
						|| (((split.matches("978\\d{10}"))
						|| (split.matches("979[1-9]\\d{9}"))
						|| (split.matches("\\d+-\\d+-\\d+-[\\dX]"))
						|| (split.matches("\\d+ \\d+ \\d+ [\\dX]"))) && (split.length() == 13))
						|| (split.matches("\\d{9}[\\dX]"))){
					retVal.add(new XMLTag(tagName, split));
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getURLs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PublicationDTO){
			if((((PublicationDTO)record).getUri() != null) && (! "".equals(((PublicationDTO)record).getUri()))){
				retVal.add(new XMLTag(tagName, ((PublicationDTO)record).getUri()));
			}
			else if((((PublicationDTO)record).getFile() != null) && (((PublicationDTO)record).getFile().getLicense() != null) && (!((PublicationDTO)record).getFile().getLicense().equals("Usage forbidden"))){
				for (String language : languages) {
					XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + ((PublicationDTO)record).getFileURL() + ((source != null)?("&source=" + source):("")) + "&language="+language);
					languageTag.getAttributes().add(new AttributeValue("xml:lang", language));
					retVal.add(languageTag);
				}
			} else if(record.getControlNumber()!=null){
				for (String language : languages) {
					XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + "/record.jsf?recordId="+record.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+language);
					languageTag.getAttributes().add(new AttributeValue("xml:lang", language));
					retVal.add(languageTag);
				}
			}
		}
		return retVal;
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
		} 
		return retVal;
	}
	
	protected List<XMLTag> getAbstracts(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getAbstracT().getContent() != null){
				String lang = paperJournal.getAbstracT().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperJournal.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO abstracT : paperJournal.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					String lang = abstracT.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = abstracT.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getAbstracT().getContent() != null){
				String lang = monograph.getAbstracT().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = monograph.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO abstracT : monograph.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					String lang = abstracT.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = abstracT.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getAbstracT().getContent() != null){
				String lang = paperMonograph.getAbstracT().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperMonograph.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO abstracT : paperMonograph.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					String lang = abstracT.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = abstracT.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getAbstracT().getContent() != null){
				String lang = paperProceedings.getAbstracT().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = paperProceedings.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO abstracT : paperProceedings.getSubtitleTranslations()) {
				if(abstracT.getContent()!=null){
					String lang = abstracT.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = abstracT.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getAbstracT().getContent() != null){
				String lang = studyFinalDocument.getAbstracT().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = studyFinalDocument.getAbstracT().getContent();
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
						if(! latinTag.equals(textTag))
							retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO abstracT : studyFinalDocument.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,abstracT.getContent());
					String lang = abstracT.getLanguage();
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
		return retVal;
	}
	
	protected List<XMLTag> getSubjects(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		
		return retVal;
	}
	
	protected List<XMLTag> getDescriptions(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getDescription().getContent() != null){
				String lang = product.getDescription().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = product.getDescription().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO description : product.getDescriptionTranslations()) {
				if(description.getContent()!=null){
					String lang = description.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = description.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getDescription().getContent() != null){
				String lang = conference.getDescription().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = conference.getDescription().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO description : conference.getDescriptionTranslations()) {
				if(description.getContent()!=null){
					String lang = description.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = description.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		return retVal;
	}
	
	protected List<XMLTag> getPlaces(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if((conference.getPlace() != null) && (! "".equals(conference.getPlace()))){
				retVal.add(new XMLTag(tagName, conference.getPlace()));
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
	
	protected List<XMLTag> getStartYear(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if((conference.getPlace() != null) && (! "".equals(conference.getPlace()))){
				retVal.add(new XMLTag(tagName, conference.getPlace()));
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getEndYear(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if((conference.getState() != null) && (! "".equals(conference.getState()))){
				retVal.add(new XMLTag(tagName, conference.getState()));
			}
		} 
		return retVal;
	}
	
	
	protected List<XMLTag> getTypes(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			XMLTag typeTag = new XMLTag(tagName, "https://w3id.org/cerif/vocab/EventTypes#Conference");
			typeTag.getAttributes().add(new AttributeValue("scheme", "https://w3id.org/cerif/vocab/EventTypes"));
			retVal.add(typeTag);
		} else if(record instanceof OrganizationUnitDTO){
			XMLTag typeTag = new XMLTag(tagName, "https://w3id.org/cerif/vocab/OrganisationTypes#HigherEducation");
			typeTag.getAttributes().add(new AttributeValue("scheme", "https://w3id.org/cerif/vocab/OrganisationTypes"));
//			retVal.add(typeTag);
		} else if(record instanceof InstitutionDTO){
			XMLTag typeTag = new XMLTag(tagName, "https://w3id.org/cerif/vocab/OrganisationTypes#HigherEducation");
			typeTag.getAttributes().add(new AttributeValue("scheme", "https://w3id.org/cerif/vocab/OrganisationTypes"));
			retVal.add(typeTag);
		} else if(record instanceof JournalDTO){
			XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_0640");
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
			retVal.add(typeTag);
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO) record;
			if(paperJournal.getPaperType() != null && paperJournal.getPaperType().equals("records.paperJournal.editPanel.paperType.paper")){
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_2df8fbb1");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
			} else {
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_3e5a");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
			}
		} else if(record instanceof MonographDTO){
			XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_2f33");
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
			retVal.add(typeTag);
		} else if(record instanceof PaperMonographDTO){
			XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_3248");
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
			retVal.add(typeTag);
		} else if(record instanceof ProceedingsDTO){
			XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_f744");
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
			retVal.add(typeTag);
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO) record;
			if(paperProceedings.getPaperType() != null && (paperProceedings.getPaperType().equals("records.paperProceedings.editPanel.paperType.full") || paperProceedings.getPaperType().equals("records.paperProceedings.editPanel.paperType.invitedTalkFull"))){
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_5794");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
			} else {
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_c94f");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
			}
		} else if(record instanceof PatentDTO){
			XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_15cd");
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Patent_Types"));
			retVal.add(typeTag);
		} else if(record instanceof ProductDTO){
			XMLTag typeTag = new XMLTag(tagName);
			String type = "http://purl.org/coar/resource_type/c_5ce6";
			for (Classification classification : record.getRecord().getRecordClasses()) {
				if(classification.getCfClassId().equals("M86"))
					type = "http://purl.org/coar/resource_type/c_ddb1";
			}
			typeTag.setBody(type);
			typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Product_Types"));
			retVal.add(typeTag);
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO thesis = (StudyFinalDocumentDTO) record;
			if(thesis.getStudyType() != null && (thesis.getStudyType().equals("records.studyFinalDocument.editPanel.studyType.phd") || thesis.getStudyType().equals("records.studyFinalDocument.editPanel.studyType.phdArt"))){		
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_db06");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
			} else {
				XMLTag typeTag = new XMLTag(tagName, "http://purl.org/coar/resource_type/c_46ec");
				typeTag.getAttributes().add(new AttributeValue("xmlns", "https://www.openaire.eu/cerif-profile/vocab/COAR_Publication_Types"));
				retVal.add(typeTag);
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
		} 
		return retVal;
	}
	
	protected List<XMLTag> getLanguages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
//		Locale locale = new Locale("en");
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getName().getLanguage() != null){
				String language = journal.getName().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = journal.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
//						new FacesMessages("messages.messages-records",  locale) 
//					.getMessageFromResourceBundle("records.language." + journal.getName().getLanguage())));
			}
		} else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getTitle().getLanguage() != null){
				String language = paperJournal.getTitle().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = paperJournal.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//						new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperJournal.getTitle().getLanguage())));
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getTitle().getLanguage() != null){
				String language = monograph.getTitle().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = monograph.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + monograph.getTitle().getLanguage())));
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getTitle().getLanguage() != null){
				String language = paperMonograph.getTitle().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = paperMonograph.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperMonograph.getTitle().getLanguage())));
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getTitle().getLanguage() != null){
				String language = proceedings.getTitle().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = proceedings.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + proceedings.getTitle().getLanguage())));
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getTitle().getLanguage() != null){
				String language = paperProceedings.getTitle().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = paperProceedings.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperProceedings.getTitle().getLanguage())));
		} 
		/*else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getTitle().getLanguage() != null){
				String language = patent.getTitle().getLanguage().substring(0,2).trim();
				String alphabet = patent.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + patent.getTitle().getLanguage())));
		}*/ 
		else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getName().getLanguage() != null){
				String language = product.getName().getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = product.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//					.getMessageFromResourceBundle("records.language." + product.getName().getLanguage())));
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLanguage() != null){
				String language = studyFinalDocument.getLanguage();
				if((language == null) || ("||".equals(language.substring(0,2).trim())))
					language = defaultLanguage;
				language = language.substring(0,2).trim();
				String alphabet = studyFinalDocument.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += ((alphabet.equals("cyrillic script"))?("-Cyrl"):("-Latn"));
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + studyFinalDocument.getLanguage())));
		} 
		return retVal;
	}	
	
	protected List<XMLTag> getAccess(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String text = "http://purl.org/coar/access_right/c_14cb";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if((studyFinalDocument.getFileLicense() != null) && (! "Usage forbidden".equals(studyFinalDocument.getFileLicense()))){
				text = "http://purl.org/coar/access_right/c_abf2";
				XMLTag textTag = new XMLTag(tagName, text);
				textTag.getAttributes().add(new AttributeValue("xmlns", "http://purl.org/coar/access_right"));
				retVal.add(textTag);
			}
		}
//		XMLTag textTag = new XMLTag(tagName, text);
//		textTag.getAttributes().add(new AttributeValue("xmlns", "http://purl.org/coar/access_right"));
//		retVal.add(textTag);
		return retVal;
	}
	
	
	protected List<XMLTag> getNames(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getName().getContent() != null){
				String lang = conference.getName().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = conference.getName().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO name : conference.getNameTranslations()) {
				if(name.getContent()!=null){
					String lang = name.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = name.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
			
		} else if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if(institution.getName().getContent() != null){
				String lang = institution.getName().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = institution.getName().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO name : institution.getNameTranslations()) {
				if(name.getContent()!=null){
					String lang = name.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = name.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getName().getContent() != null)
			{
				String lang = product.getName().getLanguage();
				if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
					lang = defaultLanguage;
				String text = product.getName().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
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
			for (MultilingualContentDTO name : product.getNameTranslations()) {
				if(name.getContent()!=null)
				{
					String lang = name.getLanguage();
					if((lang == null) || ("||".equals(lang.substring(0,2).trim())))
						lang = defaultLanguage;
					String text = name.getContent();
					XMLTag textTag = new XMLTag(tagName, text);
					String latin = LatCyrUtils.toLatin(text);
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
		return retVal;
	}
	
	protected List<XMLTag> getAcronyms(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if((institution.getAcro() != null) && (! "".equals(institution.getAcro()))){
				retVal.add(new XMLTag(tagName, institution.getAcro()));
			}			
		}	
		return retVal;
	}
	
	protected List<XMLTag> getGenders(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getSex() == 'f') || (author.getSex() == 'm')){
				retVal.add(new XMLTag(tagName, "" + author.getSex()));
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getElectronicAddresses(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getEmail() != null) && (! "".equals(author.getEmail()))){
				retVal.add(new XMLTag(tagName, "mailto:" + author.getEmail()));
			}	
			if((author.getDirectPhones() != null) && (! "".equals(author.getDirectPhones()))){
				retVal.add(new XMLTag(tagName, "tel:" + author.getDirectPhones()));
			}
			if((author.getLocalPhones() != null) && (! "".equals(author.getLocalPhones()))){
				retVal.add(new XMLTag(tagName, "tel:" + author.getLocalPhones()));
			}
		}	
		return retVal;
	}
	protected List<XMLTag> getORCIDs(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if((record.getORCID() != null) && (! "".equals(record.getORCID()))){
			if(record.getORCID().trim().matches("https://orcid\\.org/0000-000(1-[5-9]|2-[0-9]|3-[0-4])[0-9]{3}-[0-9]{3}[0-9X]"))
				retVal.add(new XMLTag(tagName, record.getORCID().trim()));
			else if(record.getORCID().trim().matches("0000-000(1-[5-9]|2-[0-9]|3-[0-4])[0-9]{3}-[0-9]{3}[0-9X]"))
				retVal.add(new XMLTag(tagName, "https://orcid.org/" + record.getORCID().trim()));
		}		
		return retVal;
	}
	
	protected List<XMLTag> getScopusIds(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if((record.getScopusID() != null) && (! "".equals(record.getScopusID()))){
			if(record.getScopusID().trim().matches("[0-9]{10,11}"))
				retVal.add(new XMLTag(tagName, record.getScopusID().trim()));
		}		
		return retVal;
	}
	
	protected List<XMLTag> getPersonNames(String indent, String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof AuthorDTO){
			AuthorDTO authorDTO = (AuthorDTO)record;
			if(authorDTO.getName() != null){
				List<XMLTag> subtags = new ArrayList<XMLTag>();
				XMLTag familyNames = new XMLTag(indent + "\t\t", "FamilyNames", authorDTO.getName().getLastname());
				XMLTag firstNames = new XMLTag(indent + "\t\t", "FirstNames", authorDTO.getName().getFirstname());
				subtags.add(familyNames);
				subtags.add(firstNames);
				XMLTag personName = new XMLTag(tagName);
				personName.setIndent(indent + "\t");
				personName.setSubTags(subtags);
				retVal.add(personName);
			}
		}
		return retVal;
	}
	

	/**
	 * Reads mARC21Record from the MARC21slim XML representation
	 * 
	 * @param xml
	 *            XML in MARC 21 slim format
	 * @return created MARC21Record
	 * 
	 */
	public Record toRecord(String stringRecord) {
		try {
			SAXParser parser = factory.newSAXParser();
			MARC21slimSAXHandler handler = new MARC21slimSAXHandler();
			parser.parse(XMLUtils.getInputSourceFromString(stringRecord), handler);
			Record retVal = new Record();
			retVal.setMARC21Record(handler.getRecord());
			return retVal;
		} catch (Exception ex) {
			log.error("fromMARC21slimXML", ex);
			return null;
		}
	}

	private static Log log = LogFactory.getLog(AbstractDublinCoreXMLSerializer.class
			.getName());

}