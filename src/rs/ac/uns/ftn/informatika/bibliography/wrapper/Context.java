package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.util.Locale;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanQuery;
import org.z3950.zing.cql.CQLTermNode;
import org.z3950.zing.cql.ModifierSet;

public class Context {
	private ContextSetStrategy strategy=null;
	private ContextSetStrategy [] listStrategy = {new CQLContextSet(), new DcContexSet(), new CrisContextSet()};
	public Locale localeLan = null;
	
	public void setStrategy(String setName) {
		if (setName.equals("cql")){
			strategy=listStrategy[0];
		}else if (setName.equals("dc")){
			strategy=listStrategy[1];
		}
		else if (setName.equals("cris")){
			strategy=listStrategy[2];
		}
	}

  public Object mapIndexToLucene(String index, boolean spanqery){
	  return strategy.mapIndexToLucene(index, spanqery);
  }

  public Query parseRelation(String lucineIndex, String relation, CQLTermNode node, boolean spanqery){
	  return strategy.parseRelation(lucineIndex,relation, node, spanqery);
  }
  public SpanQuery parseProx(ModifierSet ms, SpanQuery left, SpanQuery right){
	  return strategy.parseProx(ms,left, right);
  }
  public String getErrorMessage(){
	  return strategy.getErrorMessage();
  }

  public void setLocaleLan(Locale localeLan) {
		this.localeLan = localeLan;
		this.listStrategy[0].setLocaleLan(localeLan);
		this.listStrategy[1].setLocaleLan(localeLan);
		this.listStrategy[2].setLocaleLan(localeLan);
  }
  
}
