package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Produces DocTextExtractor objects based on the given file extension.
 */
public class DocTextExtractorFactory {

  /**
   * Returns a single instance of a DocTextExtractor suitable for the given MIME type.
   * 
   * @param extension The file extension.
   * @return A suitable DocTextExtractor object
   */
  public static DocTextExtractor getDocTextExtractor(String mimeType) {
	 DocTextExtractor docTextExtractor = null;
	 if(mimeType!=null)
		docTextExtractor = (DocTextExtractor)map.get(mimeType);
    if (docTextExtractor == null)
      log.warn("No suitable DocTextExtractor for MIME type: " + mimeType);
    return docTextExtractor;
  }
  
  private static Properties map = new Properties();
  private static Log log = LogFactory.getLog(DocTextExtractorFactory.class);
  
  static {
    map.put("text/plain", new AsciiDocTextExtractor());
    map.put("application/pdf", new PdfDocTextExtractor());
    map.put("application/msword", new WordDocTextExtractor());
    map.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new Word2007DocTextExtractor());
//    map.put("application/vnd.ms-powerpoint", new PowerPointDocBuilder());
//    map.put("application/vns.ms-excel", new ExcelDocBuilder());
//    map.put("text/html", new HtmlDocBuilder());
//    map.put("application/zip", new ZipDocBuilder());
//    map.put("application/x-rar", new RarDocBuilder());
  }
}