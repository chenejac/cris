package rs.ac.uns.ftn.informatika.bibliography.wordcloud;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;

/**
 * @author Georgia Kapitsaki
 *
 * Apr 12, 2017
 */

public class FrequencyAnalyzerWithOriginal extends FrequencyAnalyzer {

	private Map<String, Set<WordFrequency>> stemsToOriginalWords = new HashMap<String, Set<WordFrequency>>(); 
	private boolean ignoreNumbers; // indicates if numbers in text are ignored 
	
	public Set<WordFrequency> getOriginalsFromStem(String stem) {
		//Set<WordFrequency> originals = new TreeSet<WordFrequency>();
		return stemsToOriginalWords.get(stem);
	}
	
	/**
	 * 
	 * @param the stemmed version of the word
	 * @return the original word with the highest frequency in the text
	 */
	public String getFirstOriginalFromStem(String stem) {
		Set<WordFrequency> originals = getOriginalsFromStem(stem);
		if (originals!= null && originals.size() > 0)
			return stemsToOriginalWords.get(stem).iterator().next().getWord();
		return null;
	}
	
	public void addNewOriginalForStem(String stem, String original) {
        if (!stemsToOriginalWords.containsKey(stem)) {
        	Set<WordFrequency> originalWords = new TreeSet<WordFrequency>();
        	originalWords.add(new WordFrequency(original,1));
        	stemsToOriginalWords.put(stem, originalWords);
        } 
        else {
        	Set<WordFrequency> originals = getOriginalsFromStem(stem);
        	boolean found = false;
        	for (Iterator<WordFrequency> it = originals.iterator(); it.hasNext();) {
        		WordFrequency wordfreq = it.next();
        		if (wordfreq.getWord().equals(original)) {
        			wordfreq.setFrequency(wordfreq.getFrequency() + 1);
        			found = true;
        			break;
        		}
        	}
        	if (!found) 
        		originals.add(new WordFrequency(original,1));
        }
	}
	
	@Override
    protected Map<String, Integer> buildWordFrequencies(final List<String> texts, final WordTokenizer tokenizer) {
        final Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
        StemOriginalTokenizer stemTokenizer = new StemOriginalTokenizer();
        
        for (final String text : texts) {
            final List<String> words = filter(tokenizer.tokenize(text));

            for (final String word : words) {
            	// get the stem (position 0) and the original word (position 1)
            	List<String> tokens = stemTokenizer.tokenize(word);
                final String normalized = normalize(tokens.get(0));
                // ignore numbers and small words
                if (normalized.length() > getMinWordLength() && (ignoreNumbers ? !normalized.matches("[-+]?\\d*\\.?\\d+") : true)) {
                    if (!wordFrequencies.containsKey(normalized)) {
                        wordFrequencies.put(normalized, 1);
                    }
                    wordFrequencies.put(normalized, wordFrequencies.get(normalized) + 1);
                    // add to set with original words
                    addNewOriginalForStem(tokens.get(0),tokens.get(1));
                }
            }
        }
        return wordFrequencies;
    }
	
	public boolean getIgnoreNumbers() {
		return ignoreNumbers;
	}
	
	public void setIgnoreNumbers(boolean ignoreNumbers) {
		this.ignoreNumbers = ignoreNumbers;
	}
	
}