package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

import com.gint.util.xml.XMLUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.openarchives.org/OAI/oai_marc.xsd XMLSchema Full format
 * 
 * @author bojana
 * 
 */
public class OaiMarcXMLSerializer implements Serializer{

	static SAXParserFactory factory;

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
		return "";
	}

	
	/**
	 * Reads mARC21Record from the OAIMARC XML representation
	 * 
	 * @param xml
	 *            XML in OAIMARC format
	 * @return created MARC21Record
	 * 
	 */
	public Record toRecord(String stringRecord) {
		try {
			SAXParser parser = factory.newSAXParser();
			OaiMarcSAXHandler handler = new OaiMarcSAXHandler();
			parser.parse(XMLUtils.getInputSourceFromString(stringRecord), handler);
			Record retVal = new Record();
			retVal.setMARC21Record(handler.getRecord());
			return retVal;
		} catch (Exception ex) {
			log.error("fromOaiMarcXML", ex);
			return null;
		}
	}
	

	private static Log log = LogFactory.getLog(OaiMarcXMLSerializer.class
			.getName());

}
