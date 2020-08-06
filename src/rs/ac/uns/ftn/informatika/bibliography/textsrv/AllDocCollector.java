package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.ScoreDoc;

/**
 * Class which presents container for ALL results of searching Lucene index
 * files
 * 
 * @author chenejac@uns.ac.rs
 * 
 */
public class AllDocCollector extends HitCollector {
	
	@SuppressWarnings("rawtypes")
	private List docs = new ArrayList<ScoreDoc>();

	private boolean sortByRelevance;
	
	/**
	 * @param sortByRelevance
	 */
	public AllDocCollector(boolean sortByRelevance) {
		super();
		this.sortByRelevance = sortByRelevance;
	}

	/**
	 * @see org.apache.lucene.search.HitCollector#collect(int, float)
	 */
	@SuppressWarnings("unchecked")
	public void collect(int doc, float score) {
		if (score > 0.0f) {
			docs.add(new ScoreDoc(doc, score));
		}
	}

	/**
	 * Clears list of hits
	 */
	public void reset() {
		docs.clear();
	}

	/**
	 * @return list of hits
	 */
	@SuppressWarnings("unchecked")
	public List<ScoreDoc> getHits() {
		if(sortByRelevance)
			Collections.sort(docs, new RelevanceScoreDocComparator());
		return docs;
	}
}
