/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.openarchives.org/OAI/2.0/oai_dc.xsd XMLSchema
 * 
 * 
 */
public class NDLTDDublinCoreXMLSerializer extends AbstractDublinCoreXMLSerializer {

	/**
	 */
	public NDLTDDublinCoreXMLSerializer() {
		super(Arrays.asList("en"), "NDLTD");
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

}
