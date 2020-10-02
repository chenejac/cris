package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;



/**
 * Builds Lucene documents from PDF files.
 * 
 * @author mbranko@uns.ns.ac.yu
 */
public class PdfDocTextExtractor implements DocTextExtractor {

  /**
   * Extracts text from the given file.
   * 
   * @param file The file to be processed
   * @return The extracted text
   * @throws IOException if file is encrypted
   */
  public String extractText(File file) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file));
		return PdfDocTextExtractor.extractText(in);
	}
  
  public static String extractText(byte[] file) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(
				file));
		return PdfDocTextExtractor.extractText(in);
	}
  
  private static String extractText(BufferedInputStream in ) throws IOException {
//		System.out.println("Used Memory1: " +  (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		PDDocument pdfDoc = PDDocument.load(in);
//		System.out.println("Used Memory2: " +  (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		String retVal = null;
		try{
			if (pdfDoc.isEncrypted()) {
				log.warn("PDF file encrypted");
				throw new IOException();
			}
//				try {
//					pdfDoc.decrypt(""); // try empty password
//				} catch (InvalidPasswordException ex) {
//					log.warn(ex);
//				} catch (CryptographyException ex) {
//					log.warn(ex);
//				}
	
			StringWriter sw = new StringWriter();
			//if (stripper == null)
			PDFTextStripper	stripper = new PDFTextStripper();
			//else
				//stripper.resetEngine();
			stripper.writeText(pdfDoc, sw);
			retVal = sw.getBuffer().toString();
		} finally {
			pdfDoc.close();
		}
		return retVal;
	}
  
  private static Log log = LogFactory.getLog(PdfDocTextExtractor.class);
  
}
