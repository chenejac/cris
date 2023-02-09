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
import rs.ac.uns.ftn.informatika.bibliography.dto.PublisherNameDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.xml.XMLUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.dspace.org/schema/dim.xsd XMLSchema
 * 
 * 
 */
public abstract class AbstractDIMCRISXMLSerializer implements Serializer {

	protected List<String> languages;
	protected String source;
	
	/**
	 * @param languages
	 * @param source
	 */
	public AbstractDIMCRISXMLSerializer(List<String> languages, String source){
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
		buff.append(indent + "<dim:dim xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:dim=\"http://www.dspace.org/xmlns/dspace/dim\" " +
				"xsi:schemaLocation=\"http://www.dspace.org/xmlns/dspace/dim http://www.dspace.org/schema/dim.xsd\">\n");
		RecordDTO record = rec.getDto();
		if(record.isNotLoaded())
			rec.loadFromDatabase();
		List<XMLTag> titles = getTitles("dc","title", record);
//		List<XMLTag> creators = getCreators("dc","creator", record);
		List<XMLTag> contributorsAuthors = getAuthors("dc","contributor", "author", record);
		List<XMLTag> subjects = getSubjects("dc", "subject", record);
		List<XMLTag> descriptions = getDescriptions("dc","description", record);
		List<XMLTag> publishers = getPublishers("dc","publisher", record);
		List<XMLTag> contributors = getContributors("dc","contributor", record);
		List<XMLTag> dates = getDates("dc", "date", record);
		List<XMLTag> types = getTypes("dc", "type", record);
		List<XMLTag> formats = getFormats("dc", "format", record);
		List<XMLTag> identifiers = getIdentifiers("dc", "identifier", record);
		List<XMLTag> sources = getSources("dc", "source", record);
		List<XMLTag> languages = getLanguages("dc", "language", record);
		List<XMLTag> relations = getRelations("dc", "relation", record);
		List<XMLTag> coverages = getCoverages("dc", "coverage", record);
		List<XMLTag> rights = getRights("dc", "rights", record);
//		List<XMLTag> audiences = getAudiences("dc:audience", record);
	
		for (XMLTag dcTitle : titles) {
		    buff.append(indent + "\t");
		    buff.append(dcTitle.toString());
		    buff.append("\n");
		}
		
//		for (XMLTag dcCreator : creators) {
//		    buff.append(indent + "\t");
//		    buff.append(dcCreator.toString());
//		    buff.append("\n");
//		}
		
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
		
		for (XMLTag dcContributor : contributorsAuthors) {
		    buff.append(indent + "\t");
		    buff.append(dcContributor.toString());
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
		
		buff.append(indent + "</dim:dim>");
		
		return buff.toString();
    }

	
	protected List<XMLTag> getTitles(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof JournalDTO){
			JournalDTO journal = (JournalDTO)record;
			if(journal.getName().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, journal.getName().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(journal.getName().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", journal.getName().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO name : journal.getNameTranslations()) {
				if(name.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, name.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(name.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", name.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(journal.getNameAbbreviation().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, journal.getNameAbbreviation().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(journal.getNameAbbreviation().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", journal.getNameAbbreviation().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO nameAbbreviation : journal.getNameAbbreviationTranslations()) {
				if(nameAbbreviation.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, nameAbbreviation.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(nameAbbreviation.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", nameAbbreviation.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			if(paperJournal.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperJournal.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(paperJournal.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperJournal.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : paperJournal.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(paperJournal.getSubtitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperJournal.getSubtitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(paperJournal.getSubtitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperJournal.getSubtitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO subtitle : paperJournal.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, subtitle.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof MonographDTO){
			MonographDTO monograph = (MonographDTO)record;
			if(monograph.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, monograph.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(monograph.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", monograph.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : monograph.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(monograph.getSubtitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, monograph.getSubtitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(monograph.getSubtitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", monograph.getSubtitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO subtitle : monograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, subtitle.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			if(paperMonograph.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperMonograph.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(paperMonograph.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperMonograph.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : paperMonograph.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(paperMonograph.getSubtitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperMonograph.getSubtitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(paperMonograph.getSubtitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperMonograph.getSubtitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO subtitle : paperMonograph.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, subtitle.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			if(proceedings.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, proceedings.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(proceedings.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", proceedings.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : proceedings.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(proceedings.getSubtitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, proceedings.getSubtitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(proceedings.getSubtitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", proceedings.getSubtitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO subtitle : proceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, subtitle.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			if(paperProceedings.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperProceedings.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(paperProceedings.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperProceedings.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : paperProceedings.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(paperProceedings.getSubtitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, paperProceedings.getSubtitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(paperProceedings.getSubtitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", paperProceedings.getSubtitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO subtitle : paperProceedings.getSubtitleTranslations()) {
				if(subtitle.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, subtitle.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PatentDTO){
			PatentDTO patent = (PatentDTO)record;
			if(patent.getTitle().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, patent.getTitle().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(patent.getTitle().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", patent.getTitle().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO title : patent.getTitleTranslations()) {
				if(title.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, title.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProductDTO){
			ProductDTO product = (ProductDTO)record;
			if(product.getName().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, product.getName().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(product.getName().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", product.getName().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO name : product.getNameTranslations()) {
				if(name.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, name.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(name.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", name.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getTitle().getContent() != null){
				String lang = studyFinalDocument.getTitle().getLanguage();
				String text = studyFinalDocument.getTitle().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				latinTag.getAttributes().add(new AttributeValue("element", element));
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
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
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(title.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
						if(title.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							latinTag.getAttributes().add(new AttributeValue("element", element));
							latinTag.getAttributes().add(new AttributeValue("lang", title.getLanguage().substring(0,2)));
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
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				latinTag.getAttributes().add(new AttributeValue("element", element));
				latinTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
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
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
					if(subtitle.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
						if(subtitle.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							latinTag.getAttributes().add(new AttributeValue("element", element));
							latinTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
							latinTag.getAttributes().add(new AttributeValue("lang", subtitle.getLanguage().substring(0,2)));
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
		} else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			XMLTag textTag = new XMLTag(tagName,author.getName().getFirstname());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "firstname"));
			retVal.add(textTag);
			textTag = new XMLTag(tagName,author.getName().getLastname());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "lastname"));
			retVal.add(textTag);
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			XMLTag textTag = new XMLTag(tagName,conference.getSomeName());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "name"));
			textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
		} else if (record instanceof OrganizationUnitDTO){
			OrganizationUnitDTO orgUnit = (OrganizationUnitDTO)record;
			XMLTag textTag = new XMLTag(tagName,orgUnit.getSomeName());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "name"));
			textTag.getAttributes().add(new AttributeValue("lang", orgUnit.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
			textTag = new XMLTag(tagName,orgUnit.getAcro());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
			retVal.add(textTag);
		} else if (record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			XMLTag textTag = new XMLTag(tagName,institution.getSomeName());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "name"));
			textTag.getAttributes().add(new AttributeValue("lang", institution.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
			textTag = new XMLTag(tagName,institution.getAcro());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "alternative"));
			retVal.add(textTag);

		}
		return retVal;
	}
	
//	protected List<XMLTag> getCreators(String mdschema, String element, RecordDTO record) {
//		List<XMLTag> retVal = new ArrayList<XMLTag>();
//		String tagName = "dim:field";
//		if(record instanceof PublicationDTO){
//			PublicationDTO publication = (PublicationDTO)record;
//			for (AuthorDTO author : publication.getAllAuthors()) {
//				XMLTag textTag = new XMLTag(tagName, author.getName().toString());
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				retVal.add(textTag);
//			}
////			for (AuthorDTO editor : publication.getEditors()) {
////				retVal.add(new XMLTag(tagName, editor.getName().toString()));
////			}
//		}  
//		return retVal;
//	}
	
	protected List<XMLTag> getAuthors(String mdschema, String element, String qualifier, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			for (AuthorDTO author : publication.getAllAuthors()) {
				XMLTag textTag = new XMLTag(tagName, author.getName().toString());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", qualifier));
				if(author.getORCID()!=null && (!"".equals(author.getORCID().trim())))
					textTag.getAttributes().add(new AttributeValue("authority", author.getORCID())); 
				retVal.add(textTag);
//				textTag = new XMLTag(tagName, author.getORCID());
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
//				retVal.add(textTag);
			}
//			for (AuthorDTO editor : publication.getEditors()) {
//				retVal.add(new XMLTag(tagName, editor.getName().toString()));
//			}
		}  
		return retVal;
	}
	
	
	protected List<XMLTag> getSubjects(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof JournalDTO){
			JournalDTO publication = (JournalDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO publication = (PaperJournalDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO publication = (PaperMonographDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO publication = (PaperProceedingsDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : publication.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getKeywords().getContent() != null){
				String lang = publication.getKeywords().getLanguage();
				String text = publication.getKeywords().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				latinTag.getAttributes().add(new AttributeValue("element", element));
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
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
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keyword.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("lang", keyword.getLanguage().substring(0,2)));
						if(keyword.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							latinTag.getAttributes().add(new AttributeValue("element", element));
							latinTag.getAttributes().add(new AttributeValue("lang", keyword.getLanguage().substring(0,2)));
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
		} else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,author.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(author.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", author.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : author.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,conference.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));	
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(conference.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", conference.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : conference.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if (record instanceof OrganizationUnitDTO){
			OrganizationUnitDTO orgUnit = (OrganizationUnitDTO)record;
			if(orgUnit.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,orgUnit.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(orgUnit.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", orgUnit.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : orgUnit.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}		
		} else if (record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if(institution.getKeywords().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,institution.getKeywords().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(institution.getKeywords().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", institution.getKeywords().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO keywords : institution.getKeywordsTranslations()) {
				if(keywords.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, keywords.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(keywords.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", keywords.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getDescriptions(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof JournalDTO){
			JournalDTO publication = (JournalDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else 	if(record instanceof PaperJournalDTO){
			PaperJournalDTO publication = (PaperJournalDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(publication.getAbstracT().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getAbstracT().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(publication.getAbstracT().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getAbstracT().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, abstracT.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(publication.getAbstracT().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getAbstracT().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(publication.getAbstracT().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getAbstracT().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, abstracT.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO publication = (PaperMonographDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(publication.getAbstracT().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getAbstracT().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(publication.getAbstracT().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getAbstracT().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, abstracT.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO publication = (PaperProceedingsDTO)record;
			if(publication.getNote().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getNote().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getNote().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getNote().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO note : publication.getNoteTranslations()) {
				if(note.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, note.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(note.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", note.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
			if(publication.getAbstracT().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getAbstracT().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(publication.getAbstracT().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getAbstracT().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, abstracT.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getAbstracT().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getAbstracT().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(publication.getAbstracT().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getAbstracT().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, abstracT.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getDescription().getContent() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getDescription().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getDescription().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getDescription().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO description : publication.getDescriptionTranslations()) {
				if(description.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, description.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(description.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", description.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getAbstracT().getContent() != null){
				String lang = publication.getAbstracT().getLanguage();
				String text = publication.getAbstracT().getContent();
				XMLTag textTag = new XMLTag(tagName, text);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				String latin = LatCyrUtils.toLatin(text);
				XMLTag latinTag = new XMLTag(tagName, latin);
				latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				latinTag.getAttributes().add(new AttributeValue("element", element));
				latinTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(lang!=null){
					textTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
					latinTag.getAttributes().add(new AttributeValue("lang", lang.substring(0,2)));
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
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(abstracT.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
						if(abstracT.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							latinTag.getAttributes().add(new AttributeValue("element", element));
							latinTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
							latinTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
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
		} else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getBiography().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,author.getBiography().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
				if(author.getBiography().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", author.getBiography().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO biography : author.getBiographyTranslations()) {
				if(biography.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, biography.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "abstract"));
					if(biography.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", biography.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getDescription().getContent() != null){
				XMLTag textTag = new XMLTag(tagName,conference.getDescription().getContent());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(conference.getDescription().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", conference.getDescription().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (MultilingualContentDTO description : conference.getDescriptionTranslations()) {
				if(description.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName, description.getContent());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(description.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", description.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getPublishers(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof MonographDTO){
			MonographDTO publication = (MonographDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().getName());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getPublisher().getOriginalPublisher().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getPublisher().getOriginalPublisher().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null){
					XMLTag textTag = new XMLTag(tagName, publisherName.getName());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(publisherName.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", publisherName.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO publication = (ProceedingsDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().getName());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getPublisher().getOriginalPublisher().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getPublisher().getOriginalPublisher().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null){
					XMLTag textTag = new XMLTag(tagName, publisherName.getName());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(publisherName.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", publisherName.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof PatentDTO){
			PatentDTO publication = (PatentDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().getName());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getPublisher().getOriginalPublisher().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getPublisher().getOriginalPublisher().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null){
					XMLTag textTag = new XMLTag(tagName, publisherName.getName());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(publisherName.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", publisherName.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof ProductDTO){
			ProductDTO publication = (ProductDTO)record;
			if(publication.getPublisher().getOriginalPublisher().getName() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getPublisher().getOriginalPublisher().getName());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				if(publication.getPublisher().getOriginalPublisher().getLanguage() != null)
					textTag.getAttributes().add(new AttributeValue("lang", publication.getPublisher().getOriginalPublisher().getLanguage().substring(0,2)));
				retVal.add(textTag);
			}
			for (PublisherNameDTO publisherName : publication.getPublisher().getPublisherTranslations()) {
				if(publisherName.getName()!=null){
					XMLTag textTag = new XMLTag(tagName, publisherName.getName());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(publisherName.getLanguage() != null)
						textTag.getAttributes().add(new AttributeValue("lang", publisherName.getLanguage().substring(0,2)));
					retVal.add(textTag);
				}
			}
		} else if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if((publication.getInstitution()!=null) && (publication.getInstitution().getControlNumber()!=null)){
				String name = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getContent():"";
				String language = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getLanguage():null;
				
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
				institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				institutionTag.getAttributes().add(new AttributeValue("element", element));
				if(language != null)
					institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
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
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
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

	protected List<XMLTag> getContributors(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			for (AuthorDTO advisor : studyFinalDocument.getAdvisors()) {
				XMLTag textTag = new XMLTag(tagName, LatCyrUtils.toLatin(advisor.getName().toString()));
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "advisor"));
				if(advisor.getORCID()!=null && (!"".equals(advisor.getORCID().trim())))
					textTag.getAttributes().add(new AttributeValue("authority", advisor.getORCID())); 
				retVal.add(textTag);
//				textTag = new XMLTag(tagName, advisor.getORCID());
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
//				retVal.add(textTag);
			}
			for (AuthorDTO committeeMember : studyFinalDocument.getCommitteeMembers()) {
				XMLTag textTag = new XMLTag(tagName, LatCyrUtils.toLatin(committeeMember.getName().toString()));
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "other"));
				if(committeeMember.getORCID()!=null && (!"".equals(committeeMember.getORCID().trim())))
					textTag.getAttributes().add(new AttributeValue("authority", committeeMember.getORCID())); 
				retVal.add(textTag);
//				textTag = new XMLTag(tagName, committeeMember.getORCID());
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
//				retVal.add(textTag);
			}
		} else if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			for (AuthorDTO editor : publication.getEditors()) {
				XMLTag textTag = new XMLTag(tagName, LatCyrUtils.toLatin(editor.getName().toString()));
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "editor"));
				retVal.add(textTag);
//				textTag = new XMLTag(tagName, editor.getORCID());
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
//				retVal.add(textTag);
			}
		}  
		return retVal;
	}
	
	protected List<XMLTag> getDates(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			String dateString;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(studyFinalDocument.getDefendedOn() != null){
				dateString = formatter.format(studyFinalDocument.getDefendedOn().getTime());
				XMLTag textTag = new XMLTag(tagName, dateString);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issued"));
				if (!dateString.endsWith("-01-01"))
					retVal.add(textTag);
			}
			else if(studyFinalDocument.getPublicationDate() != null){
				dateString = formatter.format(studyFinalDocument.getPublicationDate().getTime());
				XMLTag textTag = new XMLTag(tagName, dateString);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issued"));
				if (!dateString.endsWith("-01-01"))
					retVal.add(textTag);
			} 
			else if(studyFinalDocument.getAcceptedOn() != null){
				dateString = formatter.format(studyFinalDocument.getAcceptedOn().getTime());
				XMLTag textTag = new XMLTag(tagName, dateString);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issued"));
				if (!dateString.endsWith("-01-01"))
					retVal.add(textTag);
			} 
		} else if(record instanceof PublicationDTO){
			PublicationDTO publication = (PublicationDTO)record;
			if(publication.getPublicationYear() != null){
				XMLTag textTag = new XMLTag(tagName, publication.getPublicationYear());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issued"));
				retVal.add(textTag);
			}
		}  else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getYearOfBirth() != null){
				XMLTag textTag = new XMLTag(tagName,author.getYearOfBirth().toString());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "yearOfBirth"));
				retVal.add(textTag);
			}
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getYear() != null){
				XMLTag textTag = new XMLTag(tagName,conference.getYear().toString());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "yearOfConference"));
				retVal.add(textTag);
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getTypes(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Thesis");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Doktorska disertacija");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof JournalDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Journal");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Casopis");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof PaperJournalDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Journal/Magazine Article");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Rad u casopisu");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				}  
			}
		} else if (record instanceof ProceedingsDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Conference Proceeding");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Zbornik sa konferencije");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				}  
			}
		} else if (record instanceof PaperProceedingsDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Rad u zborniku");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Conference Paper");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				}
			}
		} else if (record instanceof MonographDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Book");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Monografija");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof PaperMonographDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Book Chapter");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Poglavlje u monografiji");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof PatentDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Patent");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Patent");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof ProductDTO){
			for (String language : languages) {
				if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Software");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				} else if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Proizvod");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} 
			}
		} else if (record instanceof AuthorDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Osoba");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Person");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				}
			}
		} else if (record instanceof ConferenceDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Konferencija");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Conference");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				}
			}
		} else if (record instanceof OrganizationUnitDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Organizaciona jedinica");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Organisation unit");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				}
			}
		} else if (record instanceof InstitutionDTO){
			for (String language : languages) {
				if(language.equals("sr")){
					XMLTag typeTag = new XMLTag(tagName, "Instituticija");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "sr"));
					retVal.add(typeTag);
				} else if(language.equals("en")){
					XMLTag typeTag = new XMLTag(tagName, "Institution");
					typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					typeTag.getAttributes().add(new AttributeValue("element", element));
					typeTag.getAttributes().add(new AttributeValue("lang", "en"));
					retVal.add(typeTag);
				}
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getFormats(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getContentFormat() != null){
				XMLTag typeTag = new XMLTag(tagName, studyFinalDocument.getContentFormat());
				typeTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				typeTag.getAttributes().add(new AttributeValue("element", element));
				typeTag.getAttributes().add(new AttributeValue("qualifier", "mimetype"));
				retVal.add(typeTag);
			}
		} 
		return retVal;
	}
	
	protected List<XMLTag> getIdentifiers(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PublicationDTO){
			if((((PublicationDTO)record).getFile() != null) && (((PublicationDTO)record).getFile().getLicense() != null) && (!((PublicationDTO)record).getFile().getLicense().equals("Usage forbidden"))){
				for (String language : languages) {
					XMLTag textTag = new XMLTag(tagName, (Serializer.serverURL + ((PublicationDTO)record).getFileURL() + ((source != null)?("&source=" + source):("")) + "&language="+language).replace(" ", "%20"));
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "url"));
					textTag.getAttributes().add(new AttributeValue("lang", language));
					retVal.add(textTag);
				}
			}
			
			if(record instanceof MonographDTO){
				MonographDTO monograph = (MonographDTO)record;
				if((monograph.getEditionISSN() != null) && (! "".equals(monograph.getEditionISSN())) && (! "NN".equals(monograph.getEditionISSN()))){
					String[] splited = monograph.getEditionISSN().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
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
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
				if((monograph.getDoi() != null) && (! "".equals(monograph.getDoi()))){
					String correctDoi = null;
					String[] splited = monograph.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
			} else if(record instanceof JournalDTO){
				JournalDTO journal = (JournalDTO)record;
				if((journal.getIssn() != null) && (! "".equals(journal.getIssn())) && (! "NN".equals(journal.getIssn()))){
					String[] splited = journal.getIssn().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
				if((journal.getDoi() != null) && (! "".equals(journal.getDoi()))){
					String correctDoi = null;
					String[] splited = journal.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			}  else if(record instanceof ProceedingsDTO){
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
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
				}
				if((proceedings.getDoi() != null) && (! "".equals(proceedings.getDoi()))){
					String correctDoi = null;
					String[] splited = proceedings.getDoi().trim().split(":|\\s");
					for(String split: splited)
						if(split.matches("10\\.\\d{4,}(\\.\\d+)*/[^\\s]+")){
							correctDoi = split;
							break;
						}
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
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
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
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
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				ProceedingsDTO proceedings = paper.getProceedings();
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
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
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
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				JournalDTO journal = paper.getJournal();
				if((journal.getIssn() != null) && (! "".equals(journal.getIssn())) && (! "NN".equals(journal.getIssn()))){
					String[] splited = journal.getIssn().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
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
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
				MonographDTO monograph = paper.getMonograph();
				if((monograph.getEditionISSN() != null) && (! "".equals(monograph.getEditionISSN())) && (! "NN".equals(monograph.getEditionISSN()))){
					String[] splited = monograph.getEditionISSN().replace('x', 'X').replace(";", "").trim().split("\\(|\\)");
					for(String split: splited)
						if(split.matches("\\d{4}-?\\d{3}[\\dX]")){
							XMLTag textTag = new XMLTag(tagName, split);
							textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							textTag.getAttributes().add(new AttributeValue("element", element));
							textTag.getAttributes().add(new AttributeValue("qualifier", "issn"));
							retVal.add(textTag);
						}
				}
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
						XMLTag textTag = new XMLTag(tagName, split);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "isbn"));
						retVal.add(textTag);
					}
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
					if(correctDoi != null){
						XMLTag textTag = new XMLTag(tagName, correctDoi);
						textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
						textTag.getAttributes().add(new AttributeValue("element", element));
						textTag.getAttributes().add(new AttributeValue("qualifier", "doi"));
						retVal.add(textTag);
					}
				}
			}   		
		} else if (record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if(author.getEmail() != null){
				XMLTag textTag = new XMLTag(tagName,author.getEmail());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "email"));
				retVal.add(textTag);
			}
		} else if (record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getNumber() != null){
				XMLTag textTag = new XMLTag(tagName,conference.getNumber());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "number"));
				retVal.add(textTag);
			}
		} 
		for (String language : languages) {
			XMLTag textTag = new XMLTag(tagName, (Serializer.serverURL + "/record.jsf?recordId="+record.getControlNumber().substring(7) + ((source != null)?("&source=" + source):("")) + "&language="+language).replace(" ", "%20"));
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "url"));
			textTag.getAttributes().add(new AttributeValue("lang", language));
			retVal.add(textTag);
		}
		XMLTag textTag = new XMLTag(tagName, record.getControlNumber());
		textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
		textTag.getAttributes().add(new AttributeValue("element", element));
		textTag.getAttributes().add(new AttributeValue("qualifier", "externalcrisreference"));
		retVal.add(textTag);
		
		if(record.getScopusID() != null){
			textTag = new XMLTag(tagName, record.getScopusID());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "scopus"));
			retVal.add(textTag);
		}
		if(record.getORCID() != null){
			textTag = new XMLTag(tagName, record.getORCID());
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "orcid"));
			retVal.add(textTag);
		}
//		if(record instanceof StudyFinalDocumentDTO){
//			
//			if(((((StudyFinalDocumentDTO)record).getReportURL() != null)) && (!((StudyFinalDocumentDTO)record).getReportURL().equals(""))){
//				XMLTag textTag = new XMLTag(tagName, ((StudyFinalDocumentDTO)record).getReportURL().replace(" ", "%20"));
//				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//				textTag.getAttributes().add(new AttributeValue("element", element));
//				textTag.getAttributes().add(new AttributeValue("qualifier", "url"));
//				retVal.add(textTag);
//			}
//		}
		return retVal;
	}
	
	protected List<XMLTag> getSources(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if((publication.getInstitution()!=null) && (publication.getInstitution().getControlNumber()!=null)){
				String name = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getContent():"";
				String language = (publication.getInstitution().getName()!= null)?publication.getInstitution().getName().getLanguage():null;
				String id = publication.getInstitution().getControlNumber();
				
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		} else if(record instanceof AuthorDTO){
			AuthorDTO author = (AuthorDTO)record;
			if((author.getInstitution()!=null) && (author.getInstitution().getControlNumber()!=null)){
				String name = (author.getInstitution().getName()!= null)?author.getInstitution().getName().getContent():"";
				String language = (author.getInstitution().getName()!= null)?author.getInstitution().getName().getLanguage():null;
				String id = author.getInstitution().getControlNumber();
				
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if((author.getOrganizationUnit()!=null) && (author.getOrganizationUnit().getControlNumber()!=null)){
				String name = (author.getOrganizationUnit().getName()!= null)?author.getOrganizationUnit().getName().getContent():"";
				String language = (author.getOrganizationUnit().getName()!= null)?author.getOrganizationUnit().getName().getLanguage():null;
				String id = author.getOrganizationUnit().getControlNumber();
				
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "organizationUnit"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if(author.getState() != null){
				XMLTag state = new XMLTag(tagName, author.getState());
				state.getAttributes().add(new AttributeValue("mdschema", mdschema));
				state.getAttributes().add(new AttributeValue("element", element));
				state.getAttributes().add(new AttributeValue("qualifier", "country"));
				retVal.add(state);
			}
		} else if(record instanceof ConferenceDTO){
			ConferenceDTO conference = (ConferenceDTO)record;
			if(conference.getState() != null){
				XMLTag state = new XMLTag(tagName, conference.getState());
				state.getAttributes().add(new AttributeValue("mdschema", mdschema));
				state.getAttributes().add(new AttributeValue("element", element));
				state.getAttributes().add(new AttributeValue("qualifier", "country"));
				retVal.add(state);
			}
			if(conference.getPlace() != null){
				XMLTag place = new XMLTag(tagName, conference.getPlace());
				place.getAttributes().add(new AttributeValue("mdschema", mdschema));
				place.getAttributes().add(new AttributeValue("element", element));
				place.getAttributes().add(new AttributeValue("qualifier", "place"));
				retVal.add(place);
			}
		} else if(record instanceof OrganizationUnitDTO){
			OrganizationUnitDTO orgUnit = (OrganizationUnitDTO)record;
			if((orgUnit.getInstitution()!=null) && (orgUnit.getInstitution().getControlNumber()!=null)){
				String name = (orgUnit.getInstitution().getName()!= null)?orgUnit.getInstitution().getName().getContent():"";
				String language = (orgUnit.getInstitution().getName()!= null)?orgUnit.getInstitution().getName().getLanguage():null;
				String id = orgUnit.getInstitution().getControlNumber();
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
			if((orgUnit.getSuperOrganizationUnit()!=null) && (orgUnit.getSuperOrganizationUnit().getControlNumber()!=null)){
				String name = (orgUnit.getSuperOrganizationUnit().getName()!= null)?orgUnit.getSuperOrganizationUnit().getName().getContent():"";
				String language = (orgUnit.getSuperOrganizationUnit().getName()!= null)?orgUnit.getSuperOrganizationUnit().getName().getLanguage():null;
				String id = orgUnit.getSuperOrganizationUnit().getControlNumber();
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "organizationUnit"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		} else if(record instanceof InstitutionDTO){
			InstitutionDTO institution = (InstitutionDTO)record;
			if((institution.getSuperInstitution()!=null) && (institution.getSuperInstitution().getControlNumber()!=null)){
				String name = (institution.getSuperInstitution().getName() != null)?institution.getSuperInstitution().getName().getContent():"";
				String language = (institution.getSuperInstitution().getName() != null)?institution.getSuperInstitution().getName().getLanguage():null;
				String id = institution.getSuperInstitution().getControlNumber();
				
				if(name != null){
					XMLTag institutionTag = new XMLTag(tagName, name);
					institutionTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					institutionTag.getAttributes().add(new AttributeValue("element", element));
					institutionTag.getAttributes().add(new AttributeValue("qualifier", "institution"));
//					institutionTag.getAttributes().add(new AttributeValue("authority", id));
					if(language != null)
						institutionTag.getAttributes().add(new AttributeValue("lang", language.substring(0, 2)));
					retVal.add(institutionTag);
				}
			}
		}
		XMLTag textTag = new XMLTag(tagName, "CRIS UNS");
		textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
		textTag.getAttributes().add(new AttributeValue("element", element));
		retVal.add(textTag);
		textTag = new XMLTag(tagName, "http://cris.uns.ac.rs");
		textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
		textTag.getAttributes().add(new AttributeValue("element", element));
		textTag.getAttributes().add(new AttributeValue("qualifier", "uri"));
		retVal.add(textTag);
		return retVal;
	}
	
	protected List<XMLTag> getLanguages(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
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
				XMLTag textTag = new XMLTag(tagName, language);
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "iso"));
				retVal.add(textTag);
			}
//				retVal.add(new XMLTag(tagName, new FacesMessages("messages.messages-records",  locale) 
//				.getMessageFromResourceBundle("records.language." + studyFinalDocument.getLanguage())));
		} 
		return retVal;
	}
	
	protected List<XMLTag> getRelations(String mdschema, String element, RecordDTO record) {
		
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof PaperJournalDTO){
			PaperJournalDTO paperJournal = (PaperJournalDTO)record;
			JournalDTO journal = paperJournal.getJournal();
			XMLTag textTag = new XMLTag(tagName, (journal.getSomeName() != null)?journal.getSomeName():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof"));
//			textTag.getAttributes().add(new AttributeValue("authority", journal.getControlNumber()));
			if(journal.getName().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", journal.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
			if(((PaperJournalDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "lastpage"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "firstpage"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getVolume() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getVolume());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "volume"));
				retVal.add(textTag);
			}
			if(((PaperJournalDTO)record).getNumber() != null){
				textTag = new XMLTag(tagName, ((PaperJournalDTO)record).getNumber());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "issue"));
				retVal.add(textTag);
			}
		} else if(record instanceof PaperMonographDTO){
			PaperMonographDTO paperMonograph = (PaperMonographDTO)record;
			MonographDTO monograph = paperMonograph.getMonograph();
			XMLTag textTag = new XMLTag(tagName, (monograph.getSomeTitle() != null)?monograph.getSomeTitle():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof"));
//			textTag.getAttributes().add(new AttributeValue("authority", monograph.getControlNumber()));
			if(monograph.getTitle().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", monograph.getTitle().getLanguage().substring(0,2)));
			retVal.add(textTag);
			if(((PaperMonographDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperMonographDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "lastpage"));
				retVal.add(textTag);
			}
			if(((PaperMonographDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperMonographDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "firstpage"));
				retVal.add(textTag);
			}
		} else if(record instanceof PaperProceedingsDTO){
			PaperProceedingsDTO paperProceedings = (PaperProceedingsDTO)record;
			ProceedingsDTO proceedings = paperProceedings.getProceedings();
			XMLTag textTag = new XMLTag(tagName, ((proceedings.getSomeTitle() != null)?proceedings.getSomeTitle()+", ":"") + ((proceedings.getConference()!=null)?proceedings.getConference().toString():""));
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "ispartof")); 
//			textTag.getAttributes().add(new AttributeValue("authority", proceedings.getControlNumber()));
			if(proceedings.getTitle().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", proceedings.getTitle().getLanguage().substring(0,2)));
			retVal.add(textTag);
			if(((PaperProceedingsDTO)record).getEndPage() != null){
				textTag = new XMLTag(tagName, ((PaperProceedingsDTO)record).getEndPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "lastpage"));
				retVal.add(textTag);
			}
			if(((PaperProceedingsDTO)record).getStartPage() != null){
				textTag = new XMLTag(tagName, ((PaperProceedingsDTO)record).getStartPage());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				textTag.getAttributes().add(new AttributeValue("qualifier", "firstpage"));
				retVal.add(textTag);
			}
			
//			ConferenceDTO conference = proceedings.getConference();
//			textTag = new XMLTag(tagName, (conference.getName().getContent() != null)?conference.getName().getContent():"");
//			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
//			textTag.getAttributes().add(new AttributeValue("element", element));
//			textTag.getAttributes().add(new AttributeValue("qualifier", "conference"));
//			textTag.getAttributes().add(new AttributeValue("authority", conference.getControlNumber()));
//			if(conference.getName().getLanguage() != null)
//				textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
//			retVal.add(textTag);
			
		} else if(record instanceof ProceedingsDTO){
			ProceedingsDTO proceedings = (ProceedingsDTO)record;
			
			ConferenceDTO conference = proceedings.getConference();
			XMLTag textTag = new XMLTag(tagName, (conference.getSomeName() != null)?conference.getSomeName():"");
			textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
			textTag.getAttributes().add(new AttributeValue("element", element));
			textTag.getAttributes().add(new AttributeValue("qualifier", "conference"));
//			textTag.getAttributes().add(new AttributeValue("authority", conference.getControlNumber()));
			if(conference.getName().getLanguage() != null)
				textTag.getAttributes().add(new AttributeValue("lang", conference.getName().getLanguage().substring(0,2)));
			retVal.add(textTag);
			
		} 
		return retVal;
	}
	
	protected List<XMLTag> getCoverages(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		return retVal;
	}
	
	protected List<XMLTag> getRights(String mdschema, String element, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			if(((StudyFinalDocumentDTO)record).getFileLicense() != null){
				XMLTag textTag = new XMLTag(tagName, ((StudyFinalDocumentDTO)record).getFileLicense());
				textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
				textTag.getAttributes().add(new AttributeValue("element", element));
				retVal.add(textTag);
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

	private static Log log = LogFactory.getLog(AbstractDIMXMLSerializer.class
			.getName());

}
