package rs.ac.uns.ftn.informatika.bibliography.wordcloud;

import java.util.Arrays;
import java.util.List;

import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;

/**
 * @author Georgia Kapitsaki
 *
 * Apr 12, 2017
 */

public class StemOriginalTokenizer implements WordTokenizer {

	@Override
	public List<String> tokenize(String sentence) {
		List<String> tokens = Arrays.asList(sentence.split("\\["));
		if (tokens != null && tokens.size() > 0) {
			tokens.get(1).trim();
			tokens.set(1,tokens.get(1).substring(0, tokens.get(1).length()-1));
		}
		return tokens;
	}
	
}