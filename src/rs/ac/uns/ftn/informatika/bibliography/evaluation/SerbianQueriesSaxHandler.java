package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SerbianQueriesSaxHandler extends DefaultHandler{
	
	long sentenceNumber;
	List<String> queries = new ArrayList<String>();
	public List<String> lemmas = new ArrayList<String>();
	public long paragraphNumber;
	public long queriesNumber = 0;
boolean corpus = false;
boolean par = false;
boolean sen = false;
boolean g = false;

Writer outQueries ;	


	/**
 * 
 */
public SerbianQueriesSaxHandler(String fileName,long paragraphNumber, long queriesNumber ) {
	super();
	try {
		this.paragraphNumber = paragraphNumber;
		this.queriesNumber = queriesNumber;
		outQueries = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName + "Queries.xml"), "UTF8"));
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
			outQueries.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			outQueries.flush();
		} catch (IOException e) {
		}
	}

	if (qName.equalsIgnoreCase("p")) {
		par = true;
		paragraphNumber++;
	}

	if (qName.equalsIgnoreCase("s")) {
		sen = true;
		sentenceNumber++;
	}
	
	
	if (qName.equalsIgnoreCase("g")) {
		g = true;
	}


}

public void endElement(String uri, String localName,
	String qName) throws SAXException {

	if(qName.equalsIgnoreCase("corpus")){
		System.out.println("End Element :" + queriesNumber);
		corpus = false;
		try {		
			outQueries.append("<queries>\n");
			for (int i=0; i<queries.size(); i++) {
				outQueries.append("<query id=\"" + (i+1) + "\">" + queries.get(i) + "</query>\n");
			}
			outQueries.append("</queries>");
			outQueries.flush();
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
	}

	if(qName.equalsIgnoreCase("p")){
		par = false;
	}


}

public void characters(char ch[], int start, int length) throws SAXException {

	if (sen) {
		if(paragraphNumber%20000 == 0){
			String temp = new String(ch, start, length);
			String[] tempArray = temp.split("\\n");
			for (int i = 0; i < tempArray.length; i++) {
				String[] wordsArray = tempArray[i].split("\\t");
				if((wordsArray != null) && (wordsArray.length > 2) && (wordsArray[0] != null) && (wordsArray[2] != null) && (wordsArray[0].trim().length() > 4)){
					if(!lemmas.contains(wordsArray[2])){
						queriesNumber++;
						queries.add(wordsArray[0]);
						lemmas.add(wordsArray[2]);
					}
				}
			}
		}
	} 	
	
}

}
