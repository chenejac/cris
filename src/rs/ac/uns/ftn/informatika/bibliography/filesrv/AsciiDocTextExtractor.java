package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Builds Lucene documents from ASCII text files. Uses system default character 
 * encoding.
 */
public class AsciiDocTextExtractor implements DocTextExtractor {

  /**
   * Extracts text from the given file.
   * 
   * @param file The file to be processed
   * @return extracted text
   * @throws IOException
   */
  public String extractText(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuffer retVal = new StringBuffer();
    String nextLine = reader.readLine();
    while(nextLine!=null){
    	retVal.append(nextLine + " ");
    }
    return retVal.toString();
  }
}
