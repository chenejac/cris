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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;

public class SerbianLemmasSaxHandler extends DefaultHandler{
	

boolean corpus = false;
boolean par = false;
boolean sen = false;
boolean g = false;

HashMap<String, String> map = new HashMap<String, String>();

Writer out ;
Writer outStems ;



	/**
 * 
 */
public SerbianLemmasSaxHandler(String fileName ) {
	super();
	try {
		out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName + "Words.txt"), "UTF8"));
		outStems = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName + "Stems.txt"), "UTF8"));
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
//		try {
//			out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//			out.append("<words>");
//			out.flush();
//		} catch (IOException e) {
//		}
	}

	if (qName.equalsIgnoreCase("p")) {
		par = true;
	}

	if (qName.equalsIgnoreCase("s")) {
		sen = true;
	}
	
	
	if (qName.equalsIgnoreCase("g")) {
		g = true;
	}


}

public void endElement(String uri, String localName,
	String qName) throws SAXException {

	if(qName.equalsIgnoreCase("corpus")){
		corpus = false;
		try {
//			out.append("</words>");
			List<String> list = new ArrayList<String>(map.keySet());
			java.util.Collections.sort(list);
			for (String word : list) {
				out.append(word + "\n");
				outStems.append(map.get(word) + "\n");
			}
			out.flush();
			outStems.flush();
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
		String temp = new String(ch, start, length);
		String[] tempArray = temp.split("\\n");
		for (int i = 0; i < tempArray.length; i++) {
			String[] wordsArray = tempArray[i].split("\\t");
			if((wordsArray != null) && (wordsArray.length > 1) && (wordsArray[1] != null) && (wordsArray[1].trim().length() > 0) && ((wordsArray[1].equals(wordsArray[0]))) && (!Character.isDigit(wordsArray[1].charAt(0)))){
				if(!map.containsKey(wordsArray[1])){
					List<String> stems = QueryUtils.throwAnalyzer("", wordsArray[1]);
					if((stems.size() == 1) && (stems.get(0)!=null) && (!"".equals(stems.get(0).trim()))){
						map.put(wordsArray[1], stems.get(0));
					}
				}
//				sentence.append(wordsArray[0].replace("&", "i") + " ");
//				List<String> output = wordsArray[0]QueryUtils.throwAnalyzer(null, wordsArray[0]);
//				for(String o:output)
//					sentence.append(o + " ");
			}
		}
	} 	
	
}

}
