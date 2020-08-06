package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Word2007DocTextExtractor implements DocTextExtractor{

	@Override
	public String extractText(File file) throws IOException {
		String bodyText=null;
		try{
			XWPFDocument wordDoc=new XWPFDocument(new java.io.FileInputStream(file));
			
			XWPFWordExtractor we=new XWPFWordExtractor(wordDoc);
			
			bodyText=we.getText();
			
	        
		}catch(Exception e){
			log.error(e);
		}
		
		return bodyText;
	}
	
	private static Log log = LogFactory.getLog(Word2007DocTextExtractor.class);
	
}
