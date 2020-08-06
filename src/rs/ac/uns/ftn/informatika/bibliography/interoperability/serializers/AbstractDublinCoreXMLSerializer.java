/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PatentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProductDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
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
public abstract class AbstractDublinCoreXMLSerializer implements Serializer {

	protected List<String> languages;
	protected String source;
	
	/**
	 * @param languages
	 * @param source
	 */
	public AbstractDublinCoreXMLSerializer(List<String> languages, String source){
		this.languages = languages;
		this.source = source;
	}
	
	protected static SAXParserFactory factory;
	

	static {
		try {
			factory = SAXParserFactory.newInstance();
		} catch (Exception e) {
		}
	}

	/**
	 * Creates MARC21slim XML from mARC21Record
	 * 
	 * @param rec
	 *            MARC 21 MARC21Record
	 * @param indent
	 *            tags indent
	 * @return created XML
	 * 
	 */
	public String fromRecord(Record rec, String indent) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n");
		RecordDTO record = rec.getDto();
		if(record.isNotLoaded())
			rec.loadFromDatabase();
		List<XMLTag> titles = getTitles("dc:title", record);
		List<XMLTag> creators = getCreators("dc:creator", record);
		List<XMLTag> subjects = getSubjects("dc:subject", record);
		List<XMLTag> descriptions = getDescriptions("dc:description", record);
		List<XMLTag> publishers = getPublishers("dc:publisher", record);
		List<XMLTag> contributors = getContributors("dc:contributor", record);
		List<XMLTag> dates = getDates("dc:date", record);
		List<XMLTag> types = getTypes("dc:type", record);
		List<XMLTag> formats = getFormats("dc:format", record);
		List<XMLTag> identifiers = getIdentifiers("dc:identifier", record);
		List<XMLTag> sources = getSources("dc:source", record);
		List<XMLTag> languages = getLanguages("dc:language", record);
		List<XMLTag> relations = getRelations("dc:relation", record);
		List<XMLTag> coverages = getCoverages("dc:coverage", record);
		List<XMLTag> rights = getRights("dc:rights", record);
//		List<XMLTag> audiences = getAudiences("dc:audience", record);
	
		for (XMLTag dcTitle : titles) {
		    buff.append(indent + "\t");
		    buff.append(dcTitle.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcCreator : creators) {
		    buff.append(indent + "\t");
		    buff.append(dcCreator.toString());
		    buff.append("\n");
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
		
		for (XMLTag dcContributor : contributors) {
		    buff.append(indent + "\t");
		    buff.append(dcContributor.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcDate : dates) {
		    buff.append(indent + "\t");
		    buff.append(dcDate.toString());
		    buff.append("\n");
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
		
		for (XMLTag dcSource : sources) {
		    buff.append(indent + "\t");
		    buff.append(dcSource.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcLanguage : languages) {
		    buff.append(indent + "\t");
		    buff.append(dcLanguage.toString());
		    buff.append("\n");
		}
		
		for (XMLTag dcRelation : relations) {
		    buff.append(indent + "\t");
		    buff.append(dcRelation.toString());
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
		
//		for (XMLTag dcAudience : audiences) {
//		    buff.append(indent + "\t");
//		    buff.append(dcAudience.toString());
//		    buff.append("\n");
//		}
		
		buff.append(indent + "</oai_dc:dc>");
		
		return buff.toString();
    }

	
	protected List<XMLTag> getTitles(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getName().getContent() != null)
				retVal.add(new XMLTag(tagName, journal.getName().getContent()));
			for (MultilingualContentDTO name : journal.getNameTranslations()) {
				if(name.getContent()!=null)
					retVal.add(new XMLTag(tagName,name.getContent()));
			}
			if(journal.getNameAbbreviation().getContent() != null)
				retVal.add(new XMLTag(tagName,journal.getNameAbbreviation().getContent()));
			for (MultilingualContentDTO nameAbbreviation : journal.getNameAbbreviationTranslations()) {
				if(nameAbbreviation.getContent()!=null)
					retVal.add(new XMLTag(tagName,nameAbbreviation.getContent()));
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperJournal.getTitle().getContent()));
			for (MultilingualContentDTO title : paperJournal.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
			if(paperJournal.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperJournal.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : paperJournal.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName,subtitle.getContent()));
			}
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,monograph.getTitle().getContent()));
			for (MultilingualContentDTO title : monograph.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
			if(monograph.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName,monograph.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : monograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName,subtitle.getContent()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperMonograph.getTitle().getContent()));
			for (MultilingualContentDTO title : paperMonograph.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
			if(paperMonograph.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperMonograph.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : paperMonograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName,subtitle.getContent()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,proceedings.getTitle().getContent()));
			for (MultilingualContentDTO title : proceedings.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
			if(proceedings.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName,proceedings.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : proceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName,subtitle.getContent()));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperProceedings.getTitle().getContent()));
			for (MultilingualContentDTO title : paperProceedings.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
			if(paperProceedings.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName,paperProceedings.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : paperProceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName,subtitle.getContent()));
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName,patent.getTitle().getContent()));
			for (MultilingualContentDTO title : patent.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName,title.getContent()));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getName().getContent() != null)
				retVal.add(new XMLTag(tagName,product.getName().getContent()));
			for (MultilingualContentDTO name : product.getNameTranslations()) {
				if(name.getContent()!=null)
					retVal.add(new XMLTag(tagName,name.getContent()));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getTitle().getContent() != null){
				String lang = studyFinalDocument.getTitle().getLanguage();
				String text = studyFinalDocument.getTitle().getContent();
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
			for (MultilingualContentDTO title : studyFinalDocument.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,title.getContent());
					if(title.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", title.getLanguage().substring(0,2)));
						if(title.getLanguage().equals("srp")){
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
			if(studyFinalDocument.getSubtitle().getContent() != null){
				String lang = studyFinalDocument.getSubtitle().getLanguage();
				String text = studyFinalDocument.getSubtitle().getContent();
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
			for (MultilingualContentDTO subtitle : studyFinalDocument.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,subtitle.getContent());
					if(subtitle.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", subtitle.getLanguage().substring(0,2)));
						if(subtitle.getLanguage().equals("srp")){
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
		if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			for (AuthorDTO author : publication.getAllAuthors()) {
				retVal.add(new XMLTag(tagName, LatCyrUtils.toLatin(author.getName().toString())));
			}
//			for (AuthorDTO editor : publication.getEditors()) {
//				retVal.add(new XMLTag(tagName, editor.getName().toString()));
//			}
		}  
		return retVal;
	}
	
	
	protected List<XMLTag> getSubjects(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO publication = (JournalDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName,publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO publication = (PaperJournalDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO publication = (PaperMonographDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO publication = (PaperProceedingsDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getKeywords().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getKeywords().getContent()));
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null)
					retVal.add(new XMLTag(tagName, keywords.getContent()));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getKeywords().getContent() != null){
				String lang = publication.getKeywords().getLanguage();
				String text = publication.getKeywords().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
				}
				if((publication.getLanguage() != null) && (publication.getLanguage().equals("srp")) &&
					((publication.getAlphabet() != null) && (publication.getAlphabet().equals("cyrillic script")))){ 
						retVal.add(textTag);
						if(! latinTag.equals(textTag))
							retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO keyword : publication.getKeywordsTranslations()) {
				if(keyword.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,keyword.getContent());
					if(keyword.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("xml:lang", keyword.getLanguage().substring(0,2)));
						if(keyword.getLanguage().equals("srp")){
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
	
	protected List<XMLTag> getDescriptions(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof JournalDTO){
			JournalDTO publication = (JournalDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO publication = (PaperJournalDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
			if(publication.getAbstracT().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getAbstracT().getContent()));
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null)
					retVal.add(new XMLTag(tagName, abstracT.getContent()));
			}
		} else if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
			if(publication.getAbstracT().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getAbstracT().getContent()));
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null)
					retVal.add(new XMLTag(tagName, abstracT.getContent()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO publication = (PaperMonographDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
			if(publication.getAbstracT().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getAbstracT().getContent()));
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null)
					retVal.add(new XMLTag(tagName, abstracT.getContent()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO publication = (PaperProceedingsDTO)record;
			if(publication.getNote().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null)
					retVal.add(new XMLTag(tagName, note.getContent()));
			}
			if(publication.getAbstracT().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getAbstracT().getContent()));
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null)
					retVal.add(new XMLTag(tagName, abstracT.getContent()));
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getAbstracT().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getAbstracT().getContent()));
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null)
					retVal.add(new XMLTag(tagName, abstracT.getContent()));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getDescription().getContent() != null)
				retVal.add(new XMLTag(tagName, publication.getDescription().getContent()));
			for (MultilingualContentDTO description : publication.getDescriptionTranslations()) {
				if(description.getContent()!=null)
					retVal.add(new XMLTag(tagName, description.getContent()));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
//			if(publication.getNote().getContent() != null)
//				retVal.add(new XMLTag(tagName, publication.getNote().getContent()));
//			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
//				if(note.getContent()!=null)
//					retVal.add(new XMLTag(tagName, note.getContent()));
//			}
			if(publication.getAbstracT().getContent() != null){
				String lang = publication.getAbstracT().getLanguage();
				String text = publication.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("xml:lang", lang.substring(0,2)));
				}
				if((publication.getLanguage() != null) && (publication.getLanguage().equals("srp")) &&
					((publication.getAlphabet() != null) && (publication.getAlphabet().equals("cyrillic script")))){ 
						retVal.add(textTag);
						if(! latinTag.equals(textTag))
							retVal.add(latinTag);
				} else {
					retVal.add(latinTag);
				}
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
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
		} 
		return retVal;
	}
	
	protected List<XMLTag> getPublishers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null)
				retVal.add(new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().toString()));
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null)
					retVal.add(new XMLTag(tagName, publisherName.toString()));
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null)
				retVal.add(new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().toString()));
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null)
					retVal.add(new XMLTag(tagName, publisherName.toString()));
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null)
				retVal.add(new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().toString()));
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null)
					retVal.add(new XMLTag(tagName, publisherName.toString()));
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null)
				retVal.add(new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().toString()));
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null)
					retVal.add(new XMLTag(tagName, publisherName.toString()));
			}
		} else if(record instanceof StudyFinalDocumentDTO){
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
				if(language != null)
					institutionTag.getAttributes().add(new AttributeValue("xml:lang", language.substring(0, 2)));
				retVal.add(institutionTag);
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
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("xml:lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			
//			if(publication.getPublisher().getOriginalPublisher().getName() != null){
//				retVal.add(new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().toString()));
//				if((publication.getLanguage() != null) && (publication.getLanguage().equals("srp")))
//					if((publication.getAlphabet() != null) && (publication.getAlphabet().equals("cyrillic script"))){ 
//						String latin = LatCyrUtils.toLatin(publication.getPublisher().getOriginalPublisher().getName());
//						if(! latin.equals(publication.getPublisher().getOriginalPublisher().getName()))
//							retVal.add(new XMLTag(tagName,latin));
//					}
//			}
//			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
//				if(publisherName.getName()!=null)
//					retVal.add(new XMLTag(tagName, publisherName.toString()));
//			}
		} 
		return retVal;
	}

	protected List<XMLTag> getContributors(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			for (AuthorDTO advisor : studyFinalDocument.getAdvisors()) {
				retVal.add(new XMLTag(tagName, LatCyrUtils.toLatin(advisor.getName().toString())));
			}
			for (AuthorDTO committeeMember : studyFinalDocument.getCommitteeMembers()) {
				if(! retVal.contains(new XMLTag(tagName, LatCyrUtils.toLatin(committeeMember.getName().toString()))))
					retVal.add(new XMLTag(tagName, LatCyrUtils.toLatin(committeeMember.getName().toString())));
			}
		} else if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			for (AuthorDTO editor : publication.getEditors()) {
				retVal.add(new XMLTag(tagName, LatCyrUtils.toLatin(editor.getName().toString())));
			}
		}  
		return retVal;
	}
	
	protected List<XMLTag> getDates(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getDefendedOn() != null){
				dateString = formatter.format(studyFinalDocument.getDefendedOn().getTime());
				if((!dateString.endsWith("-01-01")) && (!retVal.contains(new XMLTag(tagName, dateString))))
					retVal.add(new XMLTag(tagName, dateString));
			}
			else if(studyFinalDocument.getPublicationDate() != null){
				dateString = formatter.format(studyFinalDocument.getPublicationDate().getTime());
				if(!dateString.endsWith("-01-01"))
					retVal.add(new XMLTag(tagName, dateString));
			} 
			else if(studyFinalDocument.getAcceptedOn() != null){
				dateString = formatter.format(studyFinalDocument.getAcceptedOn().getTime());
				if((!dateString.endsWith("-01-01")) && (!retVal.contains(new XMLTag(tagName, dateString))))
					retVal.add(new XMLTag(tagName, dateString));
			} 
		} else if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			if(publication.getPublicationYear() != null)
				retVal.add(new XMLTag(tagName, publication.getPublicationYear()));
		}  
		return retVal;
	}
	
	protected List<XMLTag> getTypes(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Doktorska disertacija");
					typeTag.getAttributes().add(new AttributeValue("xml:lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "PhD thesis");
					typeTag.getAttributes().add(new AttributeValue("xml:lang", "en"));
					retVal.add(typeTag);
				}
			}
		} else if (record.getRecord().getMARC21Record().getLeader().getRecordType() == 'a')
			retVal.add(new XMLTag(tagName, "Text"));
		return retVal;
	}
	
	protected List<XMLTag> getFormats(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getContentFormat() != null)
				retVal.add(new XMLTag(tagName, studyFinalDocument.getContentFormat()));
		} 
		return retVal;
	}
	
	protected List<XMLTag> getIdentifiers(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PublicationDTO){
			if((((PublicationDTO)record).getFile() != null) && (((PublicationDTO)record).getFile().getLicense() != null) && (!((PublicationDTO)record).getFile().getLicense().equals("Usage forbidden"))){
				for (String language : languages) {
					XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + ((PublicationDTO)record).getFileURL() + ((source != null)?("&source=" + source):("")) + "&language="+language);
					languageTag.getAttributes().add(new AttributeValue("xml:lang", language));
					retVal.add(languageTag);
				}
			}
		}
		for (String language : languages) {
			XMLTag languageTag = new XMLTag(tagName, Serializer.serverURL + "/record.jsf?recordId="+record.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+language);
			languageTag.getAttributes().add(new AttributeValue("xml:lang", language));
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
	
	protected List<XMLTag> getSources(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			JournalDTO journal = paperJournal.getJournal();
			if(journal.getName().getContent() != null)
				retVal.add(new XMLTag(tagName, journal.getName().getContent()));
			for (MultilingualContentDTO name : journal.getNameTranslations()) {
				if(name.getContent()!=null)
					retVal.add(new XMLTag(tagName, name.getContent()));
			}
			if(journal.getNameAbbreviation().getContent() != null)
				retVal.add(new XMLTag(tagName, journal.getNameAbbreviation().getContent()));
			for (MultilingualContentDTO nameAbbreviation : journal.getNameAbbreviationTranslations()) {
				if(nameAbbreviation.getContent()!=null)
					retVal.add(new XMLTag(tagName, nameAbbreviation.getContent()));
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			MonographDTO monograph = paperMonograph.getMonograph();
			if(monograph.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName, monograph.getTitle().getContent()));
			for (MultilingualContentDTO title : monograph.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName, title.getContent()));
			}
			if(monograph.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName, monograph.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : monograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName, subtitle.getContent()));
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			ProceedingsDTO proceedings = paperProceedings.getProceedings();
			if(proceedings.getTitle().getContent() != null)
				retVal.add(new XMLTag(tagName, proceedings.getTitle().getContent()));
			for (MultilingualContentDTO title : proceedings.getTitleTranslations()) {
				if(title.getContent()!=null)
					retVal.add(new XMLTag(tagName, title.getContent()));
			}
			if(proceedings.getSubtitle().getContent() != null)
				retVal.add(new XMLTag(tagName, proceedings.getSubtitle().getContent()));
			for (MultilingualContentDTO subtitle : proceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null)
					retVal.add(new XMLTag(tagName, subtitle.getContent()));
			}
			ConferenceDTO conference = proceedings.getConference();
			if(conference.getName().getContent() != null)
				retVal.add(new XMLTag(tagName, conference.getName().getContent()));
			for (MultilingualContentDTO name : conference.getNameTranslations()) {
				if(name.getContent()!=null)
					retVal.add(new XMLTag(tagName, name.getContent()));
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
				String language = journal.getName().getLanguage().substring(0,2);
				String alphabet = journal.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
//						new FacesMessages("messages.messages-records",  locale) 
//					.getMessageFromResourceBundle("records.language." + journal.getName().getLanguage())));
			}
		} else if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getTitle().getLanguage() != null){
				String language = paperJournal.getTitle().getLanguage().substring(0,2);
				String alphabet = paperJournal.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//						new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperJournal.getTitle().getLanguage())));
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getTitle().getLanguage() != null){
				String language = monograph.getTitle().getLanguage().substring(0,2);
				String alphabet = monograph.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + monograph.getTitle().getLanguage())));
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getTitle().getLanguage() != null){
				String language = paperMonograph.getTitle().getLanguage().substring(0,2);
				String alphabet = paperMonograph.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperMonograph.getTitle().getLanguage())));
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getTitle().getLanguage() != null){
				String language = proceedings.getTitle().getLanguage().substring(0,2);
				String alphabet = proceedings.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + proceedings.getTitle().getLanguage())));
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getTitle().getLanguage() != null){
				String language = paperProceedings.getTitle().getLanguage().substring(0,2);
				String alphabet = paperProceedings.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + paperProceedings.getTitle().getLanguage())));
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getTitle().getLanguage() != null){
				String language = patent.getTitle().getLanguage().substring(0,2);
				String alphabet = patent.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + patent.getTitle().getLanguage())));
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getName().getLanguage() != null){
				String language = product.getName().getLanguage().substring(0,2);
				String alphabet = product.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//					.getMessageFromResourceBundle("records.language." + product.getName().getLanguage())));
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLanguage() != null){
				String language = studyFinalDocument.getLanguage().substring(0,2);
				String alphabet = studyFinalDocument.getAlphabet();
				if((language.equals("sr")) && (alphabet != null)
						&& ((alphabet.equals("cyrillic script")) || (alphabet.equals("latin script")))){
					language += " (" + alphabet + ")";
				}
				retVal.add(new XMLTag(tagName,  language));
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + studyFinalDocument.getLanguage())));
		} 
		return retVal;
	}
	
	protected List<XMLTag> getRelations(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		return retVal;
	}
	
	protected List<XMLTag> getCoverages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		return retVal;
	}
	
	protected List<XMLTag> getRights(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getRights().getContent() != null){
				retVal.add(new XMLTag(tagName, studyFinalDocument.getRights().getContent()));
				if((studyFinalDocument.getLanguage() != null) && (studyFinalDocument.getLanguage().equals("srp")))
					if((studyFinalDocument.getAlphabet() != null) && (studyFinalDocument.getAlphabet().equals("cyrillic script"))){ 
						String latin = LatCyrUtils.toLatin(studyFinalDocument.getRights().getContent());
						if(! latin.equals(studyFinalDocument.getRights().getContent()))
							retVal.add(new XMLTag(tagName,latin));
					}
			}
			for (MultilingualContentDTO rights : studyFinalDocument.getRightsTranslations()) {
				if(rights.getContent()!=null)
					retVal.add(new XMLTag(tagName, rights.getContent()));
			}
		}
		return retVal;
	}
	
//	protected List<XMLTag> getAudiences(String tagName, RecordDTO record) {
//		List<XMLTag> retVal = new ArrayList<XMLTag>();
//		return retVal;
//	}

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
