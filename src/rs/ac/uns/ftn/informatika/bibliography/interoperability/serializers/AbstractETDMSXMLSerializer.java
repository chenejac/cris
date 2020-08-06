/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.xml.XMLUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.openarchives.org/OAI/2.0/oai_dc.xsd XMLSchema
 * 
 * 
 */
public abstract class AbstractETDMSXMLSerializer extends AbstractDublinCoreXMLSerializer {

	protected String basicLanguage;
	
	/**
	 * @param languages
	 * @param source
	 */
	public AbstractETDMSXMLSerializer(List<String> languages, String source, String basicLanguage) {
		super(languages, source);
		this.basicLanguage = basicLanguage;
	}
	
	/**
	 * Creates MARC21slim XML from mARC21Record
	 * 
	 * @param rec
	 *            MARC 21 MARC21Record
	 * @return created XML
	 * 
	 */
	public String fromRecord(Record rec, String indent) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<thesis xmlns=\"http://www.ndltd.org/standards/metadata/etdms/1.1/\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " + 
        "xmlns:dcterms=\"http://purl.org/dc/terms/\" " + 
        "xsi:schemaLocation=\"http://www.ndltd.org/standards/metadata/etdms/1.1/ http://cris.uns.ac.rs/etdms/1.1/etdms11.xsd " + 
        						"http://purl.org/dc/elements/1.1/ http://cris.uns.ac.rs/etdms/1.1/etdmsdc.xsd http://purl.org/dc/terms/ " + 
        						"http://cris.uns.ac.rs/etdms/1.1/etdmsdcterms.xsd\">\n");
		RecordDTO record = rec.getDto();
		if(record.isNotLoaded())
			rec.loadFromDatabase();
		List<XMLTag> titles = getTitles("dc:title", record);
		List<XMLTag> alternativeTitles = getAlternativeTitles("dcterms:alternative", record);
		List<XMLTag> creators = getCreators("dc:creator", record);
		List<XMLTag> subjects = getSubjects("dc:subject", record);
		List<XMLTag> descriptions = getDescriptions("dc:description", record);
		List<XMLTag> publishers = getPublishers("dc:publisher", record);
		List<XMLTag> contributors = getContributors("dc:contributor", record);
		List<XMLTag> dates = getDates("dc:date", record);
		List<XMLTag> types = getTypes("dc:type", record);
		List<XMLTag> formats = getFormats("dc:format", record);
		List<XMLTag> identifiers = getIdentifiers("dc:identifier", record);
		List<XMLTag> languages = getLanguages("dc:language", record);
		List<XMLTag> coverages = getCoverages("dc:coverage", record);
		List<XMLTag> rights = getRights("dc:rights", record);
		List<String> degreeNames = getDegreeNames(record);
		List<String> degreeLevels = getDegreeLevels(record);
		List<String> degreeDisciplines = getDegreeDisciplines(record);
		List<XMLTag> degreeGrantors = getDegreeGrantors("grantor", record);
		
		for (XMLTag dcTitle : titles) {
		    buff.append(indent + "\t");
		    buff.append(dcTitle.toString());
		    buff.append("\n");
		}
		
		for (XMLTag etdmsAlternativeTitle : alternativeTitles) {
		    buff.append(indent + "\t");
		    buff.append(etdmsAlternativeTitle.toString());
		    buff.append("\n");
		}
		
		for (XMLTag etdmsCreator : creators) {
		    buff.append(indent + "\t");
		    buff.append(etdmsCreator.toString());
		    buff.append("\n");
		}
		
		//po nasem modelu subject nije obavezan, a po etdms jeste
		if(subjects.size() == 0){
			subjects.add(new XMLTag("subject", ""));
		}
		
		for (XMLTag dcSubject : subjects) {
		    buff.append(indent + "\t");
		    buff.append(dcSubject.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcDescription : descriptions) {
		    buff.append(indent + "\t");
		    buff.append(dcDescription.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcPublisher : publishers) {
		    buff.append(indent + "\t");
		    buff.append(dcPublisher.toString());
		    buff.append("\n");
		}
		
		for (XMLTag etdmsContributor : contributors) {
		    buff.append(indent + "\t");
		    buff.append(etdmsContributor.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcDate : dates) {
		    buff.append(indent + "\t");
		    buff.append(dcDate.toString());
		    buff.append("\n");
		    break;
		}
		
		for (XMLTag dcType : types) {
		    buff.append(indent + "\t");
		    buff.append(dcType.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcFormat : formats) {
		    buff.append(indent + "\t");
		    buff.append(dcFormat.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcIdentifier : identifiers) {
		    buff.append(indent + "\t");
		    buff.append(dcIdentifier.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcLanguage : languages) {
		    buff.append(indent + "\t");
		    buff.append(dcLanguage.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcCoverage : coverages) {
		    buff.append(indent + "\t");
		    buff.append(dcCoverage.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcRights : rights) {
		    buff.append(indent + "\t");
		    buff.append(dcRights.toString());
		    buff.append("\n");
		}
		
		for (int i=0; i < 1; i++) {
		    buff.append(indent + "\t<degree>\n");
		    if(degreeNames.size() > i){
		    	buff.append(indent + "\t\t<name>");
		    	buff.append(degreeNames.get(i));
		    	buff.append("</name>\n");
		    }
		    if(degreeLevels.size() > i){
		    	buff.append(indent + "\t\t<level>");
		    	buff.append(degreeLevels.get(i));
		    	buff.append("</level>\n");
		    }
		    if(degreeDisciplines.size() > i){
		    	buff.append(indent + "\t\t<discipline>");
		    	buff.append(degreeDisciplines.get(i));
		    	buff.append("</discipline>\n");
		    }
		    for (XMLTag degreeGrantor : degreeGrantors) {
		    	buff.append(indent + "\t\t");
		    	buff.append(degreeGrantor.toString());
		    	buff.append("\n");
		    }
		    buff.append(indent + "\t</degree>\n");
		}
		buff.append(indent + "</thesis>");
		
		return buff.toString();
    }

	private List<XMLTag> getAlternativeTitles(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getAlternativeTitle().getContent() != null){
				String lang = studyFinalDocument.getAlternativeTitle().getLanguage();
				String text = studyFinalDocument.getAlternativeTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
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
			for (MultilingualContentDTO alternativeTitle : studyFinalDocument.getAlternativeTitleTranslations()) {
				if(alternativeTitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,alternativeTitle.getContent());
					if(alternativeTitle.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", alternativeTitle.getLanguage().substring(0,2)));
						if(alternativeTitle.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", "sr"));
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
	
	protected List<XMLTag> getCreators(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocumentDTO = (StudyFinalDocumentDTO)record;
			for (AuthorDTO author : studyFinalDocumentDTO.getAllAuthors()) {
				XMLTag xmlTag = new XMLTag(tagName, author.getName().toString());
				xmlTag.getAttributes().add(new AttributeValue("resource", Serializer.serverURL + "/record.jsf?recordId="+author.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+basicLanguage));
				retVal.add(xmlTag);
			}
		}  
		return retVal;
	}
	
	protected List<XMLTag> getPublishers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getInstitution().getName().getContent() != null){
				String name = publication.getInstitution().getName().getContent();
				String language = publication.getInstitution().getName().getLanguage();
				if((language != null) && (publication.getInstitution().getSuperInstitution() != null) && (publication.getInstitution().getSuperInstitution().getControlNumber() != null)){
					if(publication.getInstitution().getSuperInstitution().getName().getLanguage().equals(language)){
						name = publication.getInstitution().getSuperInstitution().getName().getContent() + ", " + name;
					} else for (MultilingualContentDTO nameTran : publication.getInstitution().getSuperInstitution().getNameTranslations()) {
							if(nameTran.getLanguage().equals(language)){
								name = nameTran.getContent() + ", " + name;
							}
						}			
				}
				XMLTag institutionTag = new XMLTag(tagName, name);
				if((language != null) && (languages.contains(language.substring(0, 2)))){
					retVal.add(institutionTag);
				}
			}
			for (MultilingualContentDTO institutionName : publication.getInstitution().getNameTranslations()) {
				if(institutionName.getContent()!=null){
					String name = institutionName.getContent();
					String language = institutionName.getLanguage();
					if((language != null) && (publication.getInstitution().getSuperInstitution() != null) && (publication.getInstitution().getSuperInstitution().getControlNumber() != null)){
						if(publication.getInstitution().getSuperInstitution().getName().getLanguage().equals(language)){
							name = publication.getInstitution().getSuperInstitution().getName().getContent() + ", " + name;
						} else for (MultilingualContentDTO nameTran : publication.getInstitution().getSuperInstitution().getNameTranslations()) {
								if(nameTran.getLanguage().equals(language)){
									name = nameTran.getContent() + ", " + name;
								}
							}			
					}
					XMLTag institutionTag = new XMLTag(tagName, name);
					if((language != null) && (languages.contains(language.substring(0, 2)))){
						retVal.add(institutionTag);
					}
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getContributors(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			for (AuthorDTO advisor : studyFinalDocument.getAdvisors()) {
				XMLTag xmlTag = new XMLTag(tagName, advisor.getName().toString());
				if(basicLanguage.equals("sr"))
					xmlTag.getAttributes().add(new AttributeValue("role", "mentor"));
				else 
					xmlTag.getAttributes().add(new AttributeValue("role", "advisor"));
				retVal.add(xmlTag);
			}
			boolean boardChair = true;
			for (AuthorDTO committeeMember : studyFinalDocument.getCommitteeMembers()) {
				XMLTag xmlTag = new XMLTag(tagName, committeeMember.getName().toString());
				if(boardChair){
					if(basicLanguage.equals("sr"))
						xmlTag.getAttributes().add(new AttributeValue("role", "predsednik komisije"));
					else 
						xmlTag.getAttributes().add(new AttributeValue("role", "board chair"));
					boardChair = false;
				} else {
					if(basicLanguage.equals("sr"))
						xmlTag.getAttributes().add(new AttributeValue("role", "član komisije"));
					else 
						xmlTag.getAttributes().add(new AttributeValue("role", "board member"));
				}
				retVal.add(xmlTag);
			}
		} 
		return retVal;
	}
	
	
	protected List<XMLTag> getDescriptions(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getAbstracT().getContent() != null){
				String lang = studyFinalDocument.getAbstracT().getLanguage();
				String text = studyFinalDocument.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
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
					if(abstracT.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", abstracT.getLanguage().substring(0,2)));
						if(abstracT.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", "sr"));
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
			if(studyFinalDocument.getNote().getContent() != null){
				String lang = studyFinalDocument.getNote().getLanguage();
				String text = studyFinalDocument.getNote().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				textTag.getAttributes().add(new AttributeValue("role", "note"));
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				latinTag.getAttributes().add(new AttributeValue("role", "note"));
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
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
			for (MultilingualContentDTO note : studyFinalDocument.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,note.getContent());
					textTag.getAttributes().add(new AttributeValue("role", "note"));
					if(note.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", note.getLanguage().substring(0,2)));
						if(note.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("role", "note"));
							latinTag.getAttributes().add(new AttributeValue("xml:lang", "sr"));
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
	
	protected List<XMLTag> getIdentifiers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PublicationDTO){
			if((((PublicationDTO)record).getFile() != null) && (((PublicationDTO)record).getFile().getLicense() != null) && (!((PublicationDTO)record).getFile().getLicense().equals("Usage forbidden"))){
				for (String language : languages) {
					XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + ((PublicationDTO)record).getFileURL() + ((source != null)?("&source=" + source):("")) + "&language="+language);
					retVal.add(languageTag);
				}
			}
		}
		for (String language : languages) {
			XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + "/record.jsf?recordId="+record.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+language);
			retVal.add(languageTag);
		}
		if(record instanceof StudyFinalDocumentDTO){
			if((((StudyFinalDocumentDTO)record).getDoi() != null)){
				retVal.add(new XMLTag(tagName, "http://dx.doi.org/" + ((StudyFinalDocumentDTO)record).getDoi()));
			}
			
			if((((StudyFinalDocumentDTO)record).getUri() != null)){
				retVal.add(new XMLTag(tagName, ((StudyFinalDocumentDTO)record).getUri()));
			}
		}
		return retVal;
	}
	
	
	private List<String> getDegreeNames(RecordDTO record) {
		List<String> retVal = new ArrayList<String>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getRegisterEntry().getNameOfAuthorDegree() != null)
				retVal.add(studyFinalDocument.getRegisterEntry().getNameOfAuthorDegree());
		} 
		return retVal;
	}
	
	private List<String> getDegreeLevels(RecordDTO record) {
		List<String> retVal = new ArrayList<String>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLevelOfEducation() != null)
				retVal.add("2");//studyFinalDocument.getLevelOfEducation());
		} 
		return retVal;
	}
	
	private List<String> getDegreeDisciplines(RecordDTO record) {
		List<String> retVal = new ArrayList<String>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getResearchArea().getClassId() != null)
				retVal.add(studyFinalDocument.getResearchArea().getSomeTerm());
		} 
		return retVal;
	}
	
	private List<XMLTag> getDegreeGrantors(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getInstitution().getName().getContent() != null){
				String name = publication.getInstitution().getName().getContent();
				String language = publication.getInstitution().getName().getLanguage();
				if((language != null) && (publication.getInstitution().getSuperInstitution() != null) && (publication.getInstitution().getSuperInstitution().getControlNumber() != null)){
					if(publication.getInstitution().getSuperInstitution().getName().getLanguage().equals(language)){
						name = publication.getInstitution().getSuperInstitution().getName().getContent() + ", " + name;
					} else for (MultilingualContentDTO nameTran : publication.getInstitution().getSuperInstitution().getNameTranslations()) {
							if(nameTran.getLanguage().equals(language)){
								name = nameTran.getContent() + ", " + name;
							}
						}			
				}
				XMLTag institutionTag = new XMLTag(tagName, name);
				if(publication.getInstitution().getControlNumber() != null)
					institutionTag.getAttributes().add(new AttributeValue("resource", Serializer.serverURL + "/record.jsf?recordId="+publication.getInstitution().getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+basicLanguage));
				if((language != null) && (languages.contains(language.substring(0, 2)))){
					retVal.add(institutionTag);
				}
			}
			for (MultilingualContentDTO institutionName : publication.getInstitution().getNameTranslations()) {
				if(institutionName.getContent()!=null){
					String name = institutionName.getContent();
					String language = institutionName.getLanguage();
					if((language != null) && (publication.getInstitution().getSuperInstitution() != null) && (publication.getInstitution().getSuperInstitution().getControlNumber() != null)){
						if(publication.getInstitution().getSuperInstitution().getName().getLanguage().equals(language)){
							name = publication.getInstitution().getSuperInstitution().getName().getContent() + ", " + name;
						} else for (MultilingualContentDTO nameTran : publication.getInstitution().getSuperInstitution().getNameTranslations()) {
								if(nameTran.getLanguage().equals(language)){
									name = nameTran.getContent() + ", " + name;
								}
							}			
					}
					XMLTag institutionTag = new XMLTag(tagName, name);
					if(publication.getInstitution().getControlNumber() != null)
						institutionTag.getAttributes().add(new AttributeValue("resource", Serializer.serverURL + "/record.jsf?recordId="+publication.getInstitution().getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+basicLanguage));
					if((language != null) && (languages.contains(language.substring(0, 2)))){
						retVal.add(institutionTag);
					}
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getLanguages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLanguage() != null){
				String language = studyFinalDocument.getLanguage().substring(0,2);
//				String alphabet = studyFinalDocument.getAlphabet();
//				if((language.equals("sr")) && (alphabet != null)
//						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
//					language += " (" + alphabet + ")";
//				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + studyFinalDocument.getLanguage())));
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

	private static Log log = LogFactory.getLog(AbstractETDMSXMLSerializer.class
			.getName());

}
