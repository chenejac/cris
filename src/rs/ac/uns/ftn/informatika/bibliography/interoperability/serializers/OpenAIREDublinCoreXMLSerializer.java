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
public class OpenAIREDublinCoreXMLSerializer extends AbstractDublinCoreXMLSerializer {

	/**
	 */
	public OpenAIREDublinCoreXMLSerializer() {
		super(Arrays.asList("en"), "OpenAIRE");
	}
	
	protected List<XMLTag> getRights(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		retVal.add(new XMLTag(tagName, "info:eu-repo/semantics/openAccess"));
		retVal.add(new XMLTag(tagName, "http://creativecommons.org/licenses/by-sa/2.0/uk/"));
		return retVal;
	}
	
	protected List<XMLTag> getTypes(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		retVal.add(new XMLTag(tagName, "info:eu-repo/semantics/doctoralThesis"));
		retVal.add(new XMLTag(tagName, "info:eu-repo/semantics/publishedVersion"));
		return retVal;
	}
	
	protected List<XMLTag> getSources(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		retVal.add(new XMLTag(tagName, "CRIS UNS"));
		return retVal;
	}
	
	protected List<XMLTag> getRelations(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			if((((StudyFinalDocumentDTO)record).getDoi() != null)){
				retVal.add(new XMLTag(tagName, "info:eu-repo/semantics/altIdentifier/doi/" + ((StudyFinalDocumentDTO)record).getDoi()));
			}
		}
		return retVal;
	}
	
//	protected List<XMLTag> getAudiences(String tagName, RecordDTO record) {
//		List<XMLTag> retVal = new ArrayList<XMLTag>();
//		retVal.add(new XMLTag(tagName, "Researchers"));
//		retVal.add(new XMLTag(tagName, "Students"));
//		return retVal;
//	}
	
//	protected List<XMLTag> getCoverages(String tagName, RecordDTO record) {
//		List<XMLTag> retVal = new ArrayList<XMLTag>();
//		retVal.add(new XMLTag(tagName, "No spatial location or temporal period"));
//		return retVal;
//	}
	
	protected List<XMLTag> getLanguages(String tagName, RecordDTO record) {
		List<XMLTag> retVal = new ArrayList<XMLTag>();
		if(record instanceof StudyFinalDocumentDTO){
			StudyFinalDocumentDTO studyFinalDocument = (StudyFinalDocumentDTO)record;
			if(studyFinalDocument.getLanguage() != null){
				String language = studyFinalDocument.getLanguage().substring(0,2);
				retVal.add(new XMLTag(tagName,  language));
			}
		} 
		return retVal;
	}

}
