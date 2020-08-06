package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.text.MessageFormat;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanQuery;
import org.z3950.zing.cql.CQLTermNode;
import org.z3950.zing.cql.ModifierSet;

import rs.ac.uns.ftn.informatika.bibliography.contextset.ContextSet;
public class CrisContextSet extends ContextSetStrategy{


	@Override
	public Object mapIndexToLucene(String indexS, boolean spanqery) {
		//obradi svaki ideks u skladu sa spanqery zahtevom

		ContextSet cs= ContextSetFactory.getContextSet("cris");
					   
		ContextSet.Indexes indexes = cs.getIndexes();
		for( ContextSet.Indexes.Index ixdex : indexes.getIndex())
		{
			if(ixdex.getName().equalsIgnoreCase(indexS))
			{
				return ixdex.getMappingElement();
			}
		}
		return null;		
	}

	@Override
	public Query parseRelation(String lucineIndex, String relation, CQLTermNode ctn, boolean spanqery) {
		String pattern = rbSearch.getString("search.CrissContextSet.relationCrissContextSet");
		String messageToSend = MessageFormat.format(pattern, relation);
		sendMessageLucine(messageToSend);
		return null;
	}
	@Override
	public SpanQuery parseProx(ModifierSet ms, SpanQuery left, SpanQuery right)
	{
		String pattern = rbSearch.getString("search.CrissContextSet.proxCrissContextSet");
		String messageToSend = MessageFormat.format(pattern, ms.toCQL());
		sendMessageLucine(messageToSend);
		return null;
	}
}
