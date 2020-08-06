package rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh;

import java.io.IOException;
import java.io.StringReader;
import java.util.GregorianCalendar;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import ORG.oclc.oai.harvester2.verb.ListRecords;

public class DOISerbiaClient extends TimerTask{
		
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
//	public static void main(String[] args){
//		DOISerbiaClient doiSerbiaClient = new DOISerbiaClient();
//		doiSerbiaClient.run();
//	}
	
	@Override
	public void run() {
		
		log.info("PhD DOI Serbia updater started");
		
		try {
			
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.interoperability.oaipmh.connection");
			String baseURL = rb.getString("baseURL");
	        String metadataPrefix = rb.getString("metadataPrefix");
	        String setSpec = rb.getString("setSpec");
	        System.setProperty("http.proxyHost", "proxy.uns.ac.rs");
	        System.setProperty("http.proxyPort", "8080");
	        String data = getData(baseURL, metadataPrefix, setSpec);
	        if(data != null)
	           	saveData(data);
	    } catch (Exception e) {
	    	e.printStackTrace();
		    log.error("PhD DOI Serbia updater error: " + e.getMessage());
		}
	    log.info("PhD DOI Serbia updater finished");
	}
	
	protected Log log = LogFactory.getLog(DOISerbiaClient.class.getName());

	public String getData(String baseURL, 
            String metadataPrefix, String setSpec)
		throws IOException, ParserConfigurationException, SAXException, TransformerException,
		NoSuchFieldException {
		StringBuffer retVal = new StringBuffer(""); 
		ListRecords listRecords = new ListRecords(baseURL, null, null, setSpec,
		                                   metadataPrefix);
		while (listRecords != null) {
			NodeList errors = listRecords.getErrors();
			if (errors != null && errors.getLength() > 0) {
				 log.error("Found errors");
				 int length = errors.getLength();
				 for (int i=0; i<length; ++i) {
				     Node item = errors.item(i);
				     log.error(item);
				 }
				 log.error("Error record: " + listRecords.toString());
				 break;
			}
			retVal.append(listRecords.toString());
			String resumptionToken = listRecords.getResumptionToken();
			if (resumptionToken == null || resumptionToken.length() == 0) {
				listRecords = null;
			} else {
				listRecords = new ListRecords(baseURL, resumptionToken);
			}
		}
		return retVal.toString();
	}
	
	private void saveData(String data) throws Exception {
    	
    	Document doc = loadXMLFromString(data);
    	NodeList nList = doc.getElementsByTagName("oai_dc:dc");
    	
    	for (int temp1 = 0; temp1 < nList.getLength(); temp1++) {
    		Node nNode = nList.item(temp1);
    		String controlNumber = null;
    		String DOI = null;
    		String vbs = null;
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    			Element eElement = (Element) nNode;
    			NodeList idenList = eElement.getElementsByTagName("dc:identifier");
    			for (int temp2 = 0; temp2 < idenList.getLength(); temp2++) {
    	    		Node nNode2 = idenList.item(temp2);
    	    		if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
    	    			Element eElement1 = (Element) nNode2;
    	    			String text = eElement1.getTextContent();
    	    			if(text != null){
    	    				if(text.toLowerCase().contains("cris.uns.ac.rs")){
    	    					if(text.contains("recordId")){
    	    						controlNumber = "(BISIS)" + text.split("recordId=")[1].split("&")[0];;
    	    					}
    	    				} else if(text.toLowerCase().contains("vbs.rs")){
    	    					vbs = text;
    	    				} else if(text.toLowerCase().startsWith("10")){
    	    					DOI = text;
    	    				}
    	    			}
    	    		}
    			}
    		}
    		if((controlNumber != null) && ((vbs != null) || (DOI != null))){
    			StudyFinalDocumentDTO thesis = (StudyFinalDocumentDTO)recordDAO.getDTO(controlNumber);
    			if(thesis != null){
	    			boolean dataChanged = false;
	    			if((thesis.getDoi() == null) && (DOI != null))
	    				dataChanged = true;
	    			else if((thesis.getUri() == null) && (vbs != null))
	    				dataChanged = true;
	    			else if((thesis.getDoi() != null) && (DOI != null) && (! DOI.equals(thesis.getDoi())))
	    					dataChanged = true;
	    			else if((thesis.getUri() != null) && (vbs != null) && (! vbs.equals(thesis.getUri())))
	    				dataChanged = true;
	    			if(dataChanged){
		    			thesis.setDoi(DOI);
		    			thesis.setUri(vbs);
		    			if(recordDAO.update(new Record(thesis.getRecord().getCreator(), thesis.getRecord().getCreationDate(), "PhD DOI updater", new GregorianCalendar(), thesis.getRecord().getArchived(), thesis.getRecord().getType(), thesis)) == false )
		    				log.error("PhD DOI Serbia updater error - controlNumber: " + controlNumber);
	    			}
    			}
    		}
    	}
	}
    
    public Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
    
}

