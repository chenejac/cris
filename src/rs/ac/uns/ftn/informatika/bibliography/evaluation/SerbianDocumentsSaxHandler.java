package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;

public class SerbianDocumentsSaxHandler extends DefaultHandler{
	
	long sentenceNumber;
	StringBuilder sentence;
	public long paragraphNumber;
boolean corpus = false;
boolean par = false;
boolean sen = false;
boolean g = false;

Writer out ;	



	/**
 * 
 */
public SerbianDocumentsSaxHandler(String fileName,long paragraphNumber ) {
	super();
	try {
		this.paragraphNumber = paragraphNumber;
		out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName + "Documents.xml"), "UTF8"));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	public void startElement(String uri, String localName,String qName,
            Attributes attributes) throws SAXException {

	
	if (qName.equalsIgnoreCase("corpus")) {
		corpus = true;
		try {
			out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			out.append("<documents>");
			out.flush();
		} catch (IOException e) {
		}
	}

	if (qName.equalsIgnoreCase("p")) {
		par = true;
		sentenceNumber = 0l;
		paragraphNumber++;
		sentence = new StringBuilder();
		sentence.append("<document id=\"" + paragraphNumber + "\">\n");
	}

	if (qName.equalsIgnoreCase("s")) {
		sen = true;
		sentenceNumber++;
		
		sentence.append("\t<sentence id=\"" + paragraphNumber + "." + sentenceNumber + "\">\n");
	}
	
	
	if (qName.equalsIgnoreCase("g")) {
		g = true;
	}


}

public void endElement(String uri, String localName,
	String qName) throws SAXException {

	if(qName.equalsIgnoreCase("corpus")){
		System.out.println("End Element :" + paragraphNumber);
		corpus = false;
		try {
			out.append("</documents>");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	if(qName.equalsIgnoreCase("g")){
		g = false;
	}

	if(qName.equalsIgnoreCase("s")){
		sen = false;
		sentence.append("\t</sentence>\n");
	}

	if(qName.equalsIgnoreCase("p")){
		par = false;
		try {
			sentence.append("</document>\n");
			if(paragraphNumber % 20 == 0)
				out.append(sentence.toString() + "\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

public void characters(char ch[], int start, int length) throws SAXException {

	if (sen) {
		String temp = new String(ch, start, length);
		String[] tempArray = temp.split("\\n");
		for (int i = 0; i < tempArray.length; i++) {
			String[] wordsArray = tempArray[i].split("\\t");
			if((wordsArray != null) && (wordsArray.length > 0) && (wordsArray[0] != null) && (wordsArray[0].trim().length() > 0)){
				sentence.append(wordsArray[0].replace("&", "i") + " ");
//				List<String> output = wordsArray[0]QueryUtils.throwAnalyzer(null, wordsArray[0]);
//				for(String o:output)
//					sentence.append(o + " ");
			}
		}
	} 	
	
}

}
