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
 * http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd XMLSchema
 * 
 * @author bojana
 * 
 */
public class MARC21slimSAXHandler extends DefaultHandler {

	private MARC21Record currRecord;
	private Leader currLeader;
	private ControlField currControlField;
	private DataField currDataField;
	private Subfield currSubfield;

	public MARC21slimSAXHandler() {
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
			if (qName.equals("mARC21Record")) {
				currRecord = new MARC21Record();
			} else if (qName.equals("leader")) {
				currLeader = new Leader();
				currRecord.setLeader(currLeader);
			} else if (qName.equals("controlfield")) {
				String name = attrs.getValue("tag");
				currControlField = new ControlField(name);
				currRecord.addControlField(currControlField);
			} else if (qName.equals("datafield")) {
				String name = attrs.getValue("tag");
				char ind1 = attrs.getValue("ind1").charAt(0);
				char ind2 = attrs.getValue("ind2").charAt(0);
				currDataField = new DataField(name, ind1, ind2);
				currRecord.addDataField(currDataField);
			} else if (qName.equals("subfield")) {
				char name = attrs.getValue("code").charAt(0);
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

			if (qName.equals("leader")) {
				currLeader = null;
			} else if (qName.equals("controlfield")) {
				currControlField = null;
			} else if (qName.equals("datafield")) {
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
					.replace('\n', ' ');
			System.out.println(cnt + "\n");
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

	private static Log log = LogFactory.getLog(MARC21slimSAXHandler.class
			.getName());

}
