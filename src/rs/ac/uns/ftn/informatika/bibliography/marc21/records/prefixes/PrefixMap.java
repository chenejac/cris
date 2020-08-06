package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

import java.util.List;
import java.util.Map;

/**
 * Represents a map from subfields to prefixes and vice versa.
 * 
 * @author mbranko@uns.ac.rs
 */
public interface PrefixMap {

	/**
	 * Returns a list of prefix names (strings) mapped to the given dataSource.
	 * 
	 * @param type
	 *            the type
	 * @param dataSource
	 *            the data source
	 * @return The list of corresponding prefixes (some of prefixes have preconditions).
	 */
	public List<String> getPrefixes(String type, String dataSource);

	/**
	 * Returns a list of data source (strings) mapped to the given prefix.
	 * 
	 * @param type
	 *            the type
	 * @param prefix
	 *            the prefix
	 * @return The list of corresponding data sources.
	 */
	public List<String> getDataSources(String type, String prefix);

	/**
	 * Returns a map of prefix names lists (strings) mapped to
	 * corresponding data sources.
	 * 
	 * @param type
	 *            the type
	 * @return The map of prefix names lists (strings) mapped to 
	 *         corresponding data sources.
	 */
	public Map<String, List<String>> getPrefixMap(String type);

	/**
	 * Returns a map of data sources (strings) mapped to 
	 * corresponding prefixes.
	 * 
	 * @param type
	 *            the type
	 * @return The map of data sources (strings) mapped to 
	 *         corresponding prefixes
	 */
	public Map<String, List<String>> getDataSourceMap(String type);

}
