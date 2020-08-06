package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

/**
 * Implements a default prefix configuration.
 * 
 * @author mbranko@uns.ac.rs
 */
public class DefaultPrefixConfig implements PrefixConfig {

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixConfig#getPrefixHandler()
	 */
	@Override
	public PrefixHandler getPrefixHandler() {
		return prefixHandler;
	}

	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes.PrefixConfig#getPrefixMap()
	 */
	@Override
	public PrefixMap getPrefixMap() {
		return prefixMap;
	}

	public DefaultPrefixConfig() {
		prefixMap = new DefaultPrefixMap();
		prefixHandler = new DefaultPrefixHandler();
	}

	private PrefixMap prefixMap;
	private PrefixHandler prefixHandler;
}
