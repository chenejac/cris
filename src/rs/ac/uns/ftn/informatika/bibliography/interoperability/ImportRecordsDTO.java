/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;

/**
 * @author Dragan Ivanovic
 *
 */
@SuppressWarnings("serial")
public class ImportRecordsDTO implements Serializable{
	
	private List<RecordDTO> allRecords;
	
	private transient Iterator<RecordDTO> allRecordsIterator;
	
	private Map<String, Map<String, RecordDTO>> importedRecords;
	
	private List<RecordDTO> publications;
	
	private transient Iterator<RecordDTO> allPublicationsIterator;
	
	public ImportRecordsDTO(){
		allRecords = new ArrayList<RecordDTO>();
		publications = new ArrayList<RecordDTO>();
		allRecordsIterator = allRecords.iterator();
		allPublicationsIterator = publications.iterator();
		importedRecords = new HashMap<String, Map<String, RecordDTO>>();
	}

	/**
	 * @return the allRecords
	 */
	public List<RecordDTO> getAllRecords() {
		return allRecords;
	}

	/**
	 * @param allRecords the allRecords to set
	 */
	public void setAllRecords(List<RecordDTO> allRecords) {
		this.allRecords = allRecords;
		this.allRecordsIterator = allRecords.iterator();
		this.publications = new ArrayList<RecordDTO>();
		for(RecordDTO record:allRecords){
			if((record instanceof PublicationDTO) && (record.getRelatedRecords().size() == 0))
				this.publications.add(record);
		}
		this.allPublicationsIterator = this.publications.iterator();
	}

	/**
	 * @return the importedRecords
	 */
	public Map<String, RecordDTO> getImportedRecords(String className) {
		if(importedRecords.get(className) == null){
			Map<String, RecordDTO> map = new HashMap<String, RecordDTO>();
			importedRecords.put(className, map);
		}
		return importedRecords.get(className);
	}

	/**
	 * @param importedRecords the importedRecords to set
	 */
	public void setImportedRecords(Map<String, Map<String, RecordDTO>> importedRecords) {
		this.importedRecords = importedRecords;
	}
	
	public void addImportedRecord(String recordIdentifier, RecordDTO record){
		getImportedRecords(record.getClass().getSimpleName()).put(recordIdentifier, record);
	}

	/**
	 * @return the allRecordsIterator
	 */
	public Iterator<RecordDTO> getAllRecordsIterator() {
		return allRecordsIterator;
	}

	/**
	 * @param allRecordsIterator the allRecordsIterator to set
	 */
	public void setAllRecordsIterator(Iterator<RecordDTO> allRecordsIterator) {
		this.allRecordsIterator = allRecordsIterator;
	}

	/**
	 * @return the allPublicationsIterator
	 */
	public Iterator<RecordDTO> getAllPublicationsIterator() {
		return allPublicationsIterator;
	}

	/**
	 * @param allPublicationsIterator the allPublicationsIterator to set
	 */
	public void setAllPublicationsIterator(
			Iterator<RecordDTO> allPublicationsIterator) {
		this.allPublicationsIterator = allPublicationsIterator;
	}

	
}
