/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.Arrays;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.dspace.org/schema/dim.xsd XMLSchema
 * 
 * 
 */
public class BEOPENDIMXMLSerializer extends AbstractDIMCRISXMLSerializer {

	
	public BEOPENDIMXMLSerializer(){
		super(Arrays.asList("sr","en"), "BEOPEN");
	}
	
}
