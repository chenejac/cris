package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanQuery;
import org.z3950.zing.cql.CQLTermNode;
import org.z3950.zing.cql.Modifier;
import org.z3950.zing.cql.ModifierSet;

import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.QueryUtils;

public abstract class ContextSetStrategy {
	
	public String errorMessage = null;
	public ResourceBundle rbSearch = PropertyResourceBundle.getBundle("messages.messages-search", new Locale("en"));
	public Locale localeLan = new Locale("en");
	
	public abstract Object mapIndexToLucene(String index, boolean spanqery);
	public abstract Query parseRelation(String lucineIndex, String relation, CQLTermNode node, boolean spanqery);
	public abstract SpanQuery parseProx(ModifierSet ms, SpanQuery left, SpanQuery right);
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void  sendMessageLucine(String messageS)
	{
		errorMessage = messageS;
	}
	
	public boolean checkInteger(String intValue, String errMessage)
	{
		try {
				Integer.parseInt(intValue);
			}
		catch (NumberFormatException e) {
			sendMessageLucine(errMessage);
				return false;
		}
		return true;
	}
	
	public void setLocaleLan(Locale localeLan) {
		rbSearch = PropertyResourceBundle.getBundle(
				"messages.messages-search", localeLan);
		this.localeLan = localeLan;
	}
	
	public Modifier getModifier(String nameModifier, Vector<Modifier> modifiers) 
	{
		if (modifiers==null)
		{
			return null;
		}
		
		for(Modifier modifier : modifiers) 
		{
			if(modifier.getType().equalsIgnoreCase(nameModifier))
				return modifier;
		}
		return null;
	}
	
	public Query analizeQuery(Query termQuery, String fieldName, String value, Operator defaultOperator, Vector<Modifier> modifiers)
	{
	if(!(fieldName.equalsIgnoreCase("TYPE") ||  value.startsWith("(BISIS)") || getModifier("fuzzy", modifiers)!=null))	
		try {
			if(termQuery!=null)
				termQuery = QueryUtils.parseQuery(termQuery.toString(),new CrisAnalyzer(), defaultOperator, fieldName);
		} catch (ParseException e) {
			termQuery = null;
			sendMessageLucine(rbSearch.getString("search.CQLContextSet.analizeQuery"));
		}
	return termQuery;
	}
}
