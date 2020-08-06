/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.io.InputStream;
import java.io.OutputStream;

import rs.ac.uns.ftn.informatika.bibliography.interoperability.ImportRecordsDTO;

/**
 * @author Dragan Ivanovic
 *
 */
public interface GroupSerializer {
	
	public OutputStream exportRecords (ImportRecordsDTO records);
	
	public ImportRecordsDTO importRecords (InputStream is);

}
