package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

/**
 * Represents a configuration of prefix settings.
 * 
 * @author mbranko@uns.ns.ac.yu
 */
public interface PrefixConfig {

	/**
	 * @return prefix handler
	 */
	public PrefixHandler getPrefixHandler();

	/**
	 * @return prefix map
	 */
	public PrefixMap getPrefixMap();

}
