/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.Arrays;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.openarchives.org/OAI/2.0/oai_dc.xsd XMLSchema
 * 
 * 
 */
public class ETDMSXMLSerializer extends AbstractETDMSXMLSerializer {

	/**
	 */
	public ETDMSXMLSerializer() {
		super(Arrays.asList("sr", "en"), null, "en");
	}

}
