/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import ORG.oclc.oai.util.OAIUtil;
import com.gint.util.xml.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to https://github.com/openaire/guidelines-cris-managers/blob/master/schemas/openaire-cerif-profile.xsd XMLSchema
 * 
 * 
 */
public class TeslaRISXMLSerializer extends OpenAIRECRISXMLSerializer {



	/**
	 */
	public TeslaRISXMLSerializer(){
		this.languages = Arrays.asList("en", "sr");
		this.source = "TeslaRIS";
	}
	

}