package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordDocTextExtractor implements DocTextExtractor{

	@Override
	public String extractText(File file) throws IOException {
		String bodyText=null;
		try{
			WordExtractor we=new WordExtractor(new java.io.FileInputStream(file));
			bodyText = we.getText();
		}catch(Exception e){
			log.error(e);
		}
		return bodyText;
	}
	
	private static Log log = LogFactory.getLog(WordDocTextExtractor.class);
}
