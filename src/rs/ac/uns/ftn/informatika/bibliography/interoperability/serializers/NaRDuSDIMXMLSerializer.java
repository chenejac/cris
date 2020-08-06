/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.dspace.org/schema/dim.xsd XMLSchema
 * 
 * 
 */
public class NaRDuSDIMXMLSerializer extends AbstractDIMXMLSerializer {

	
	public NaRDuSDIMXMLSerializer(){
		super(Arrays.asList("sr"), "NaRDuS");
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.AbstractDIMXMLSerializer#getDescriptions(java.lang.String, java.lang.String, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected List<XMLTag> getDescriptions(String mdschema, String element,
			RecordDTO record) {
		//Jsoup.parse(html).text();
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO publication = (StudyFinalDocumentDTO)record;
			if(publication.getAbstracT().getContent() != null){
				String lang = publication.getAbstracT().getLanguage();
				String text = Jsoup.parse(publication.getAbstracT().getContent()).text();
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
			for (MultilingualContentDTO abstracT : publication.getAbstractTranslations()) {
				if(abstracT.getContent()!=null){
					XMLTag textTag = new XMLTag(tagName,Jsoup.parse(abstracT.getContent()).text());
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					if(abstracT.getLanguage() != null){
						textTag.getAttributes().add(new AttributeValue("lang", abstracT.getLanguage().substring(0,2)));
						if(abstracT.getLanguage().equals("srp")){
							XMLTag latinTag = new XMLTag(tagName,LatCyrUtils.toLatin(textTag.getBody()));
							latinTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
							latinTag.getAttributes().add(new AttributeValue("element", element));
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
		} 
		return retVal;
	}

	/* (non-Javadoc)
	 * @see rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.AbstractDIMXMLSerializer#getIdentifiers(java.lang.String, java.lang.String, rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO)
	 */
	@Override
	protected List<XMLTag> getIdentifiers(String mdschema, String element,
			RecordDTO record) {
		List<XMLTag> retVal = super.getIdentifiers(mdschema, element, record);
		String tagName = "dim:field";
		if(record instanceof StudyFinalDocumentDTO){
			
			if(((((StudyFinalDocumentDTO)record).getReportURL() != null)) && (!((StudyFinalDocumentDTO)record).getReportURL().equals(""))){
				for (String language : languages) {
					XMLTag textTag = new XMLTag(tagName, (Serializer.serverURL + ((StudyFinalDocumentDTO)record).getReportURL() + ((source != null)?("&source=" + source):("")) + "&language="+language).replace(" ", "%20"));
					textTag.getAttributes().add(new AttributeValue("mdschema", mdschema));
					textTag.getAttributes().add(new AttributeValue("element", element));
					textTag.getAttributes().add(new AttributeValue("qualifier", "uri"));
					textTag.getAttributes().add(new AttributeValue("lang", language));
					retVal.add(textTag);
				}
			}
		}
		return retVal;
	}
	
	
	
	
}
