package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXMLFile {

	
   public static void main(String argv[]) {
//	    String[] fileNames = {"E:\\srWaC1.1.01", "E:\\srWaC1.1.02", "E:\\srWaC1.1.03", "E:\\srWaC1.1.04", "E:\\srWaC1.1.05", "E:\\srWaC1.1.06"};
	    String[] fileNames = {"E:\\srWaC1.1.01"};
//	    String[] fileNames = {"E:\\bsWaC1.1.01", "E:\\bsWaC1.1.02", "E:\\bsWaC1.1.03"};
//	    String[] fileNames = {"E:\\hrWaC2.1.01", "E:\\hrWaC2.1.02", "E:\\hrWaC2.1.03", "E:\\hrWaC2.1.04", "E:\\hrWaC2.1.05", "E:\\hrWaC2.1.06", "E:\\hrWaC2.1.07", "E:\\hrWaC2.1.08", "E:\\hrWaC2.1.09", "E:\\hrWaC2.1.10", "E:\\hrWaC2.1.11", "E:\\hrWaC2.1.12", "E:\\hrWaC2.1.13", "E:\\hrWaC2.1.14"};

    try {

	SAXParserFactory factory = SAXParserFactory.newInstance();

    long paragraphNumber = 0l;
    long queryNumber = 0l;
    for (String fileName : fileNames) {		
//    	SerbianDocumentsSaxHandler documentsHandler = new SerbianDocumentsSaxHandler(fileName, paragraphNumber);
//    	SAXParser saxParser = factory.newSAXParser();
//    	saxParser.parse(fileName + ".xml", documentsHandler);
//    	paragraphNumber = handler.paragraphNumber;
    	
//    	SerbianQueriesSaxHandler queriesHandler = new SerbianQueriesSaxHandler(fileName, paragraphNumber, queryNumber);
//    	SAXParser saxParser = factory.newSAXParser();
//    	saxParser.parse(fileName + ".xml", queriesHandler);
//    	SerbianMatrixSaxHandler matrixHandler = new SerbianMatrixSaxHandler(fileName, paragraphNumber, queryNumber, queriesHandler.lemmas);
//    	saxParser = factory.newSAXParser();
//    	saxParser.parse(fileName + ".xml", matrixHandler);
//    	queryNumber = queriesHandler.queriesNumber;
    	
    	SerbianLemmasSaxHandler lemmasHandler = new SerbianLemmasSaxHandler(fileName);
    	SAXParser saxParser = factory.newSAXParser();
    	saxParser.parse(fileName + ".xml", lemmasHandler);
	}

     } catch (Exception e) {
       e.printStackTrace();
     }

   }

}
