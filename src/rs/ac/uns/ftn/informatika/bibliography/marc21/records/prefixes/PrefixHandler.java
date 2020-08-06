package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.ControlField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.DataField;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.Leader;

/**
 * Represents a prefix handler responsible for handling details about conversion
 * of records to prefixes.
 * 
 * @author mbranko@uns.ns.ac.yu
 */
public interface PrefixHandler {

	/**
	 * @param prefix
	 *            prefix
	 * @param leader
	 *            leader
	 * @return leader subfield value
	 */
	public String getLeaderValue(String prefix, Leader leader);

	/**
	 * @param prefix
	 *            prefix
	 * @param controlField
	 *            control field
	 * @return control field subfield value
	 */
	public String getControlFieldValue(String prefix,
			ControlField controlField);

	/**
	 * @param prefix
	 *            prefix
	 * @param dataField
	 *            data field
	 * @return data field subfields values
	 */
	public List<String> getDataSubfieldsValues(String prefix,
			DataField dataField);
	
	
	public String getRecordClassValue(String prefix,
			Classification classification);
	
	public String getRecordKeywordsValue(String prefix,
			MultilingualContentDTO recordKeywords);
	
	public String getRecordRecordValue(String prefix,
			RecordRecord recordRecord);
}
