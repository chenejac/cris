/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Subfield;

/**
 * Class for getting object of class MARC21Record from XML format according to
 * http://www.openarchives.org/OAI/oai_marc.xsd XMLSchema Full format
 * 
 * @author bojana
 * 
 */
public class OaiMarcSAXHandler extends DefaultHandler {

	/**
	 * 
	 */
	private MARC21Record currRecord;
	private Leader currLeader;
	private ControlField currControlField;
	private DataField currDataField;
	private Subfield currSubfield;

	public OaiMarcSAXHandler() {
	}

	/**
	 * @return current mARC21Record
	 */
	public MARC21Record getRecord() {
		return currRecord;
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String lName, String qName,
			Attributes attrs) throws SAXException {
		try {
			if (qName.equals("oai_marc")) {
				currRecord = new MARC21Record();
				char status = returnCharFromString(attrs.getValue("status"));
				char type = returnCharFromString(attrs.getValue("type"));
				char level = returnCharFromString(attrs.getValue("level"));
				char ctlType = returnCharFromString(attrs.getValue("ctlType"));
				char charEnc = returnCharFromString(attrs.getValue("charEnc"));
				char encLvl = returnCharFromString(attrs.getValue("encLvl"));
				char catForm = returnCharFromString(attrs.getValue("catForm"));
				char lrRqrd = returnCharFromString(attrs.getValue("lrRqrd"));
				currLeader = new Leader(status, type, level, ctlType, charEnc,
						encLvl, catForm, lrRqrd);
				currRecord.setLeader(currLeader);
				currLeader = null;
			} else if (qName.equals("fixfield")) {
				String name = attrs.getValue("id");
				currControlField = new ControlField(name);
				currRecord.addControlField(currControlField);
			} else if (qName.equals("varfield")) {
				String name = attrs.getValue("id");
				char ind1 = attrs.getValue("i1").charAt(0);
				char ind2 = attrs.getValue("i2").charAt(0);
				currDataField = new DataField(name, ind1, ind2);
				currRecord.addDataField(currDataField);
			} else if (qName.equals("subfield")) {
				char name = attrs.getValue("label").charAt(0);
				currSubfield = new Subfield(name);
				currDataField.addSubfield(currSubfield);
			}
		} catch (NullPointerException ex) {
			log.error("startElement", ex);
		}
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String lName, String qName)
			throws SAXException {
		try {
			if (qName.equals("oai_marc")) {

			} else if (qName.equals("fixfield")) {
				currControlField = null;
			} else if (qName.equals("varfield")) {
				currDataField = null;
			} else if (qName.equals("subfield")) {
				currSubfield = null;
			}

		} catch (NullPointerException ex) {
			log.error("endElement", ex);
		}
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] buf, int offset, int len) throws SAXException {
		try {
			String cnt = new String(buf, offset, len).trim().replace('\r', ' ')
					.replace('\n', ' ').replaceAll("&", "&amp;");
			if (cnt.equals(""))
				return;
			if (currLeader != null)
				currLeader.setContent(cnt);
			else if (currControlField != null)
				currControlField
						.setContent(currControlField.getContent() + cnt);
			else if (currSubfield != null)
				currSubfield.setContent(currSubfield.getContent() + cnt);
		} catch (NullPointerException ex) {
			log.error("characters", ex);
		}
	}

	/**
	 * @param str
	 *            string for getting first character
	 * @return space character if str is null, else the first character of
	 *         string
	 */
	private char returnCharFromString(String str) {
		if (str == null)
			return ' ';
		else
			return str.charAt(0);
	}

	private static Log log = LogFactory.getLog(OaiMarcSAXHandler.class
			.getName());

}
