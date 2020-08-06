/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

import ORG.oclc.oai.util.OAIUtil;

import com.gint.util.xml.XMLUtils;

/**
 * Class for conversions between object of class MARC21Record and XML format according
 * to http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd XMLSchema
 * 
 * @author bojana
 * 
 */
public class MARC21slimXMLSerializer implements Serializer {

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
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "<marc:record xmlns:marc=\"http://www.loc.gov/MARC21/slim\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"xsi:schemaLocation=\"http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd\" type=\"Bibliographic\">\n");
		buff
				.append(indent + "\t<marc:leader>" + OAIUtil.xmlEncode(rec.getMARC21Record().getLeader().getContent())
						+ "</marc:leader>\n");
		for (int i = 0; i < rec.getMARC21Record().getControlFieldsCount(); i++) {
			ControlField cf = (ControlField) rec.getMARC21Record().getControlField(i);
			buff.append(controlFieldToMARC21slimXML(cf, indent));
		}
		for (int i = 0; i < rec.getMARC21Record().getDataFieldsCount(); i++) {
			DataField df = (DataField) rec.getMARC21Record().getDataField(i);
			buff.append(dataFieldToMARC21slimXML(df, indent));
		}
		buff.append("</marc:record>");
		return buff.toString();
	}

	/**
	 * Creates part of MARC21slim XML from control field
	 * 
	 * @param cf
	 *            MARC 21 Control Field
     * @param indent
	 *            tags indent
	 * @return created part of XML
	 * 
	 */
	private String controlFieldToMARC21slimXML(ControlField cf, String indent) {
		return indent + "\t<marc:controlfield tag=\"" + OAIUtil.xmlEncode(cf.getName()) + "\">"
				+ OAIUtil.xmlEncode(cf.getContent()) + "</marc:controlfield>\n";
	}

	/**
	 * Creates part of MARC21slim XML from data field
	 * 
	 * @param df
	 *            MARC 21 Data Field
	 * @param indent
	 *            tags indent
	 * @return created part of XML
	 * 
	 */
	private String dataFieldToMARC21slimXML(DataField df, String indent) {
		StringBuffer buff = new StringBuffer(1024);
		buff.append(indent + "\t<marc:datafield tag=\"");
		buff.append(OAIUtil.xmlEncode(df.getName()));
		buff.append("\" ind1=\"");
		buff.append(df.getInd1());
		buff.append("\" ind2=\"");
		buff.append(df.getInd2());
		buff.append("\">\n");
		for (int i = 0; i < df.getSubfieldCount(); i++) {
			Subfield subfield = df.getSubfield(i);
			buff.append(indent + "\t\t<marc:subfield code=\"");
			buff.append(subfield.getName());
			buff.append("\">");
			buff.append(OAIUtil.xmlEncode(subfield.getContent()));
			buff.append("</marc:subfield>\n");
		}
		buff.append(indent + "\t</marc:datafield>\n");
		return buff.toString();
	}

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

	private static Log log = LogFactory.getLog(MARC21slimXMLSerializer.class
			.getName());

}
