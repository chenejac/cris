/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.marc21.records;

import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.MARC21slimXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers.OaiMarcXMLSerializer;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * MARC21 mARC21Record factory contain operations for serialization of MARC21 records
 * in different formats
 * 
 * Format types that are supported by now:
 * 
 * XML format according to
 * http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd XMLSchema XML
 * format according to http://www.openarchives.org/OAI/oai_marc.xsd XMLSchema
 * Full format Full format string according to
 * http://www.loc.gov/marc/marcdocz.html
 * 
 * 
 * @author bojana
 * 
 */
public class MARC21RecordFactory {

	/**
	 * Creates object MARC21Record from MARC21slimXML format
	 * 
	 * XML format according to
	 * http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd XMLSchema
	 * 
	 * @return MARC 21 mARC21Record
	 */
	public static MARC21Record fromMARC21slimXML(String str) {
		return new MARC21slimXMLSerializer().toRecord(str).getMARC21Record();
	}

	/**
	 * Creates MARC21slimXML from object MARC21Record
	 * 
	 * XML format according to
	 * http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd XMLSchema
	 * 
	 * @return MARC21slimXML
	 */
	public static String toMARC21slimXML(MARC21Record rec, String indent) {
		rec.sortFields(); 
		Record record = new Record();
		record.setMARC21Record(rec);
		return new MARC21slimXMLSerializer().fromRecord(record, indent);
	}

	/**
	 * Creates object MARC21Record from OaiMarcXML format
	 * 
	 * XML format according to http://www.openarchives.org/OAI/oai_marc.xsd
	 * XMLSchema Full format
	 * 
	 * @return MARC 21 mARC21Record
	 */
	public static MARC21Record fromOaiMarcXML(String str) {
		return new OaiMarcXMLSerializer().toRecord(str).getMARC21Record();
	}

	/**
	 * Creates OaiMarcXML from object MARC21Record
	 * 
	 * XML format according to http://www.openarchives.org/OAI/oai_marc.xsd
	 * XMLSchema Full format
	 * 
	 * @return OaiMarcXML
	 */
	public static String toOaiMarcXML(MARC21Record rec, String indent) {
		rec.sortFields(); 
		Record record = new Record();
		record.setMARC21Record(rec);
		return new OaiMarcXMLSerializer().fromRecord(record, indent);
	}

	/**
	 * Creates object MARC21Record from full format string of MARC21 mARC21Record
	 * 
	 * @return object MARC 21 mARC21Record
	 */
	public static MARC21Record fromFullFormatString(String str) {
		MARC21Record rec = new MARC21Record();
		rec.fromString(str);
		return rec;
	}

	/**
	 * Creates full format string of MARC21 mARC21Record from object MARC21Record
	 * 
	 * @return full format string of MARC21 mARC21Record
	 */
	public static String toFullFormatString(MARC21Record rec) {
		rec.sortFields();
		return rec.toString();
	}

}
