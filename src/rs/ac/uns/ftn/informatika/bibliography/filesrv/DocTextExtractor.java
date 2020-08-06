package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.File;
import java.io.IOException;

/**
 * Interface for creating Lucene documents based on various file formats.
 */
public interface DocTextExtractor {

  /**
   * Extracts text from the given file.
   * 
   * @param file The file to be processed
   * @return The extracted text
   * @throws IOException
   */
  public String extractText(File file) throws IOException;
}
