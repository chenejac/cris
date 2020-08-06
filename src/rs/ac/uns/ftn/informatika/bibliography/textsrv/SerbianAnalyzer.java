package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;


/**
 * Analyser for Lucene, it is latin - cyrilic insensitive
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
public class SerbianAnalyzer extends Analyzer {

	private Set<String> serbianStopSet;
	private static final String[] SERBIAN_STOP_WORDS = {
		"i", "ili", "a", "ali", "pa",
		"biti", "ne", 
		"jesam", "sam", "jesi", "si", "je", "jesmo", "smo", "jeste", "ste", "jesu", "su",
		"nijesam", "nisam", "nijesi", "nisi", "nije", "nijesmo", "nismo", "nijeste", "niste", "nijesu", "nisu",
		"budem", "budeš", "bude", "budemo", "budete", "budu",
		"budes",
		"bih",  "bi", "bismo", "biste", "biše", 
		"bise",
		"bio", "bili", "budimo", "budite", "bila", "bilo", "bile", 
		"ću", "ćeš", "će", "ćemo", "ćete", 
		"neću", "nećeš", "neće", "nećemo", "nećete", 
		"cu", "ces", "ce", "cemo", "cete",
		"necu", "neces", "nece", "necemo", "necete",
		"mogu", "možeš", "može", "možemo", "možete",
		"mozes", "moze", "mozemo", "mozete"};
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SerbianAnalyzer() {
		super();
		serbianStopSet = StopFilter.makeStopSet(SERBIAN_STOP_WORDS);
	}



	/**
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
	 *      java.io.Reader)
	 */
	public TokenStream tokenStream(String fieldname, Reader reader) {
			return 
			new RemoveAccentsFilter(
//						new CustomSnowballFilter(
						new SnowballFilter(
							new StopFilter(
//								new LatCyrFilter(
									new LowerCaseFilter(
											new WhitespaceTokenizer(reader)), serbianStopSet), new SerbianStemmer()), true);
		
	}
}
