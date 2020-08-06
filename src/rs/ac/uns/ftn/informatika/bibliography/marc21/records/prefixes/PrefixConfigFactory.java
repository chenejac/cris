package rs.ac.uns.ftn.informatika.bibliography.marc21.records.prefixes;

/**
 * Produces PrefixMap objects according to the prefix.map system property.
 * 
 * @author mbranko@uns.ac.rs
 */
public class PrefixConfigFactory {

	/**
	 * @return instance of class DefaultPrefixConfig
	 */
	public static PrefixConfig getPrefixConfig() {
		return new DefaultPrefixConfig();
	}
}
