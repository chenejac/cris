package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.util.Comparator;

import org.apache.lucene.search.ScoreDoc;

/**
 * Class for comparing two lucene document object by given sort field
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
public class RelevanceScoreDocComparator implements Comparator<ScoreDoc> {
	
	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare (ScoreDoc i, ScoreDoc j) {
		if (i.score > j.score) return -1;
		if (i.score < j.score) return 1;
		return 0;
	}

}