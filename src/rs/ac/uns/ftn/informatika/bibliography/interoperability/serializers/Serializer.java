/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * @author Dragan Ivanovic
 *
 */
public interface Serializer {
	
	public static String serverURL = "https://www.cris.uns.ac.rs"; 
	
	public String fromRecord (Record record, String indent);
	
	public Record toRecord (String stringRecord);

}
