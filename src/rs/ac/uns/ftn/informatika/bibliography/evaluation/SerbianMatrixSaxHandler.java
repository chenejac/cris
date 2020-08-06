package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SerbianMatrixSaxHandler extends DefaultHandler{
	
	long sentenceNumber;
//	StringBuilder sentence;
	List<String> lemmas = new ArrayList<String>();
	Map<String, List<Long>> map = new HashMap<String, List<Long>>();
	public long paragraphNumber;
	public long queriesNumber;
boolean corpus = false;
boolean par = false;
boolean sen = false;
boolean g = false;

Writer outMatrix ;	



	/**
 * 
 */
public SerbianMatrixSaxHandler(String fileName,long paragraphNumber, long queriesNumber, List<String> lemmas) {
	super();
	try {
		this.paragraphNumber = paragraphNumber;
		this.queriesNumber = queriesNumber;
		this.lemmas = lemmas;
		for (String lemma : lemmas) {
			map.put(lemma, new ArrayList<Long>());
		}
		System.out.println(map.size());
		outMatrix = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName + "Matrix.xml"), "UTF8"));
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
			outMatrix.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			outMatrix.flush();
		} catch (IOException e) {
		}
	}

	if (qName.equalsIgnoreCase("p")) {
		par = true;
		sentenceNumber = 0l;
		paragraphNumber++;
//		sentence = new StringBuilder();
//		sentence.append("<document id=\"" + paragraphNumber + "\">\n");
	}

	if (qName.equalsIgnoreCase("s")) {
		sen = true;
		sentenceNumber++;
		
//		sentence.append("<sentence id=\"" + paragraphNumber + "." + sentenceNumber + "\">\n");
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
			outMatrix.append("<matrix>");
			for (int i=0; i<lemmas.size(); i++) {
				outMatrix.append("<query id=\"" + (i+1) + "\">\n");
				List<Long> list = map.get(lemmas.get(i));
				for (Long document : list) {
					outMatrix.append("\t<document id=\"" + document + "\" />\n");
				}
				outMatrix.append("</query>\n");
			}
			outMatrix.append("</matrix>");
			outMatrix.flush();
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
//		sentence.append("</sentence>\n");
	}

	if(qName.equalsIgnoreCase("p")){
		par = false;
//		try {
////			sentence.append("</document>\n");
////			out.append(sentence.toString() + "\n");
////			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}


}

public void characters(char ch[], int start, int length) throws SAXException {

	if (sen && (paragraphNumber % 20 == 0)) {
		String temp = new String(ch, start, length);
		String[] tempArray = temp.split("\\n");
		for (int i = 0; i < tempArray.length; i++) {
			String[] wordsArray = tempArray[i].split("\\t");
			if((wordsArray != null) && (wordsArray.length > 2) && (wordsArray[0] != null) && (wordsArray[2] != null) && (wordsArray[0].trim().length() > 0)){
				if(map.containsKey(wordsArray[2])){
					if(! map.get(wordsArray[2]).contains(new Long(paragraphNumber)))
						map.get(wordsArray[2]).add(new Long(paragraphNumber));
				}
//				sentence.append(wordsArray[0] + " ");
//				List<String> output = wordsArray[0]QueryUtils.throwAnalyzer(null, wordsArray[0]);
//				for(String o:output)
//					sentence.append(o + " ");
			}
		}
	} 	
	
}

}
