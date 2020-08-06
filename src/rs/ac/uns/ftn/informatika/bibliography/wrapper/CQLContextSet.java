package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreRangeQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.tartarus.snowball.ext.PorterStemmer;
import org.z3950.zing.cql.CQLTermNode;
import org.z3950.zing.cql.Modifier;
import org.z3950.zing.cql.ModifierSet;

import rs.ac.uns.ftn.informatika.bibliography.contextset.ContextSet;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Indexer;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.string.StringUtils;

public class CQLContextSet extends ContextSetStrategy{

public String [] modifiersFunctional = {
	    	"stem", "relevant", "phonetic", "fuzzy", 
	    	"partial", "ignoreCase", "respectCase",
	    	"ignoreAccents", "respectAccents", "locale"};
	    
public String [] modifiersFormat = {
	    	"word", "string", "isoDate", "number", 
	    	"uri", "oid", "masked", "unmasked", 
	    	"substring", "regexp"};	

private int wordSlopNumber = 1;
private int sentenceSlopNumber = 50;
private int paragraphSlopNumber = 200;

@Override
public Object mapIndexToLucene(String indexS, boolean spanqery) {
	//obradi svaki ideks u skladu sa spanqery zahtevom
	if (indexS.equalsIgnoreCase("allRecords") && (spanqery==false))
	{
		return new MatchAllDocsQuery();
	}else if (indexS.equalsIgnoreCase("resultSetId")){
			return null;
	}else{
		ContextSet cs= ContextSetFactory.getContextSet("cql");
		ContextSet.Indexes indexes = cs.getIndexes();
		for(ContextSet.Indexes.Index ixdex : indexes.getIndex())
		{
			if(ixdex.getName().equalsIgnoreCase(indexS))
			{
				return ixdex.getMappingElement();
			}
		}			
	}
	return null;
}

@Override
public SpanQuery parseProx(ModifierSet ms, SpanQuery left, SpanQuery right)
{

	ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
	wordSlopNumber = Integer.parseInt(rb.getString("word"));
	sentenceSlopNumber = Integer.parseInt(rb.getString("sentence"));
	paragraphSlopNumber = Integer.parseInt(rb.getString("paragraph"));
	
	//za sad hard kodiranje jer je nemoguje napraviti preslikavanje 1 na 1, vise modifikatora odredjuje preslikavanje na jedan element lusina
	Modifier modifierDistance = null;
	Modifier modifierUnit = null;
	boolean unorderedOrOrdered = false;
	
	for(Modifier modifier : ms.getModifiers()) 
	{
		if(modifier.getType().equalsIgnoreCase("distance"))
			modifierDistance = modifier;
		else if(modifier.getType().equalsIgnoreCase("unit"))
			modifierUnit = modifier;
		else if(modifier.getType().equalsIgnoreCase("unordered"))
			unorderedOrOrdered = false;
		else if(modifier.getType().equalsIgnoreCase("ordered"))
			unorderedOrOrdered = true;
		else
		{
			String pattern = rbSearch.getString("search.CQLContextSet.proxModifierNotRegistred");
			String messageToSend = MessageFormat.format(pattern, modifier.getType());
			sendMessageLucine(messageToSend);
			return null;
		}
	}
	if(modifierDistance==null)
	{
		modifierDistance = new Modifier("distance", "=", "1");
	}
	if(modifierUnit==null)
	{
		modifierUnit = new Modifier("unit", "=", "word");
	}
	
	int fistSlopK = modifierUnit.getValue().equalsIgnoreCase("word")? wordSlopNumber : (modifierUnit.getValue().equalsIgnoreCase("sentence")? sentenceSlopNumber: (modifierUnit.getValue().equalsIgnoreCase("paragraph")? paragraphSlopNumber:wordSlopNumber));
	int secondSlopK = Integer.parseInt(modifierDistance.getValue());
	
	if(!left.getField().trim().equalsIgnoreCase(right.getField().trim()))
	{
		String pattern = rbSearch.getString("search.CQLContextSet.proxSameFields");
		String messageToSend = MessageFormat.format(pattern, left.getField(), right.getField());
		sendMessageLucine(messageToSend);
		return null;
	}
	
	SpanQuery[] sp = new SpanQuery[2];
	sp[0]=(SpanQuery)left;
	sp[1]=(SpanQuery)right;
	return new SpanNearQuery(sp, fistSlopK*secondSlopK, unorderedOrOrdered);
}

@Override
public Query parseRelation(String fieldName, String relation, CQLTermNode ctn, boolean spanqery) {

	Query query=null;
	errorMessage = null;
	String value = ctn.getTerm();
	
	if(!(fieldName.equalsIgnoreCase("TYPE") ||  value.startsWith("(BISIS)")))	
	{
		int length = value.length();
		if(value.endsWith(".."))
			value = value.substring(0, length-2)+'*';
		else if (value.endsWith("."))
			value = value.substring(0, length-1)+'*';
		
		value = value.toLowerCase();
		value = LatCyrUtils.toLatinUnaccented(value);
		value = StringUtils.clearDelimiters(value, Indexer.delims);
	}
	
	Vector<Modifier> modifiers = null;
    if(ctn.getRelation()!=null)
    	if(ctn.getRelation().getModifiers().size()>0)
    		modifiers = ctn.getRelation().getModifiers();
    if(spanqery==true)
    {
    	if(relation.equals("=") || relation.equals("==") || relation.equalsIgnoreCase("adj")) {
			query = createSpanTermQuery(fieldName,value, relation, modifiers);
			if(errorMessage!=null)
				return null;
	    } else if (relation.equals("<")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelation0"));
			return null;
		} else if (relation.equals(">")) {
			sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelation1"));
			return null;
	    } else if (relation.equals("<=")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelation2"));
	    	return null;
	    } else if (relation.equals(">=")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelation3"));
	    	return null;
	    } else if (relation.equals("<>")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelation4"));
	    	return null;
	    } else if (relation.equalsIgnoreCase("any")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelationAny"));
	    	return null;
	    } else if (relation.equalsIgnoreCase("ALL")) {  
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelationAll"));
	    	return null;
	    } else if (relation.equalsIgnoreCase("within")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelationWithin"));
	    	return null;
	    }else if (relation.equalsIgnoreCase("encloses")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxRelationEncloses"));
	    	return null;
	    }
    }else
    {
		if(relation.equals("=") || relation.equals("==") || relation.equalsIgnoreCase("adj")) {
			query = createTermQuery(fieldName,value, relation, modifiers);
			if(errorMessage!=null)
				return null;
			query = analizeQuery(query,fieldName, value, Operator.AND, modifiers);
	    } else if (relation.equals("<")) {
			if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relation0Int"))==false)
				return null;
			if(modifiers!=null)
			{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relation0Mod"));
				return null;
			}
			query = new ConstantScoreRangeQuery(fieldName,null,value,true,false);
		} else if (relation.equals(">")) {
			if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relation1Int"))==false)
				return null;
			if(modifiers!=null)
			{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relation1Mod"));
				return null;
			}
			query = new ConstantScoreRangeQuery(fieldName,value,null,false,true);
	    } else if (relation.equals("<=")) {
	    	if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relation2Int"))==false)
	    		return null;
	    	if(modifiers!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relation2Mod"));
	    	query = new ConstantScoreRangeQuery(fieldName,null,value,true,true);
	    } else if (relation.equals(">=")) {
	    	if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relation3Int"))==false)
	    		return null;
	    	if(modifiers!=null)
	    	{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relation3Mod"));
				return null;
	    	}
	    	query = new ConstantScoreRangeQuery(fieldName,value,null,true,true);
	    } else if (relation.equals("<>")) {
	    	if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relation4Int"))==false)
	    		return null;
	    	if(modifiers!=null)
	    	{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relation4Mod"));
				return null;
	    	}
	    	Query temp1 = new ConstantScoreRangeQuery(fieldName,null,value,true,false);
	    	Query temp2 = new ConstantScoreRangeQuery(fieldName,value,null,false,true);
	        query = new BooleanQuery();
	        andQuery((BooleanQuery) query, temp1);
	        andQuery((BooleanQuery) query, temp2);             
	    } else if (relation.equalsIgnoreCase("any")) {  
	   	 	Query termQuery = new BooleanQuery();
	        StringTokenizer tokenizer = new StringTokenizer(value, " ");
	        while (tokenizer.hasMoreTokens()) {
	            String curValue = tokenizer.nextToken();
	            Query subSubQuery = createTermQuery(fieldName, curValue, relation, modifiers);
	            if(errorMessage!=null)
					return null;
	            orQuery((BooleanQuery) termQuery, subSubQuery);
	        }
	        query=termQuery;
			query = analizeQuery(query,fieldName, value, Operator.OR, modifiers);
	    } else if (relation.equalsIgnoreCase("ALL")) {            
	   	 	Query termQuery = new BooleanQuery();
	        StringTokenizer tokenizer = new StringTokenizer(value, " ");
	        while (tokenizer.hasMoreTokens()) {
	            String curValue = tokenizer.nextToken();
	            Query subSubQuery = createTermQuery(fieldName, curValue, relation, modifiers);
	            if(errorMessage!=null)
					return null;
	            andQuery((BooleanQuery) termQuery, subSubQuery);
	        }
	        query=termQuery;
			query = analizeQuery(query,fieldName, value, Operator.AND, modifiers);
	    } else if (relation.equalsIgnoreCase("within")) {
	    	StringTokenizer tokenizer = new StringTokenizer(value, " ");
	    	if(tokenizer.countTokens()!=2){
	    		sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationWithinBoundaries"));
	    		return null;
	    	}
	    	String firstValue = tokenizer.nextToken();
	    	String secondValue = tokenizer.nextToken();
	    	if(checkInteger(firstValue, rbSearch.getString("search.CQLContextSet.relationWithinInt"))==false)
	    		return null;
	    	if(checkInteger(secondValue, rbSearch.getString("search.CQLContextSet.relationWithinInt"))==false)
	    		return null;
	    	if(modifiers!=null)
	    	{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationWithinMod"));
				return null;
	    	}
	    	query = new ConstantScoreRangeQuery(fieldName,firstValue,secondValue,true,true);
	    }else if (relation.equalsIgnoreCase("encloses")) {
	    	sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationEncloses"));
	    	return null;
	    }
    }
    if(errorMessage!=null)
		return null;
    
	return query;
}
public Query createTermQuery(String fieldName, String value, String relation, Vector<Modifier> modifiers) {
	// =, ==, adj, any, ALL   ----- kreiranje terma za pomenute relacije
	//svaki put mora da se promeni pri promeni parsera
	
	Query termQuery = new BooleanQuery();    
	if(relation == null || relation.equals("="))
	{
		/*int length = value.length();
		if(value.endsWith(".."))
			value = value.substring(0, length-2)+'*';
		else if (value.endsWith("."))
			value = value.substring(0, length-1)+'*';*/
		
		if(modifiers!=null)
		{
			while(getModifier("unmasked",modifiers)==null && value.contains(" *"))
			{  // WORD * WORD --> word* word
				value = value.replace(" *", "*");
			}
			while(getModifier("unmasked",modifiers)==null && value.contains(" ?"))
			{  // WORD * WORD --> word* word
				value = value.replace(" ?", "?");
			}
			
			if (getModifier("word",modifiers)!=null)
			{
				StringTokenizer tokenizer = new StringTokenizer(value, " ");
				
				if(tokenizer.countTokens()>1)
				{
			        while (tokenizer.hasMoreTokens()) {
			            String curValue = tokenizer.nextToken();
			            Query curValueQuery = createTermQuery(fieldName, curValue, relation, modifiers);
			            andQuery((BooleanQuery) termQuery,curValueQuery);
			            }
			        return termQuery;
				}
			}
			else if (getModifier("number",modifiers)!=null)
			{
				if(checkInteger(value, rbSearch.getString("search.CQLContextSet.relationNumberModifier"))==false)
		    		return null;
			}
			else if (getModifier("string",modifiers)!=null || getModifier("isoDate",modifiers)!=null || 
					getModifier("uri",modifiers)!=null || getModifier("oid",modifiers)!=null)
			{
				
			}
			/*
			else
			{	//defaultni je word
				StringTokenizer tokenizer = new StringTokenizer(value, " ");
				if(tokenizer.countTokens()>1)
			        while (tokenizer.hasMoreTokens()) {
			            String curValue = tokenizer.nextToken();
			            Query curValueQuery = createTermQuery(fieldName, curValue, relation, modifiers);
			            andQuery((BooleanQuery) termQuery,curValueQuery);
			            }
			}*/
			
			value = ignoreOrRespectCase(value, modifiers);
			if(errorMessage!=null)
				return null;
			value = stemValue(value, modifiers);
			if(errorMessage!=null)
				return null;

			if (getModifier("fuzzy",modifiers)!=null)
			{
				StringTokenizer tokenizer = new StringTokenizer(value, " ");
				
				if(tokenizer.countTokens()>1)
				{
			        while (tokenizer.hasMoreTokens()) {
			            String curValue = tokenizer.nextToken();
			            Query curValueQuery = createTermQuery(fieldName, curValue, relation, modifiers);
			            andQuery((BooleanQuery) termQuery,curValueQuery);
			            }
			        return termQuery;
				}
				
				termQuery = new FuzzyQuery(new Term(fieldName, value), 0.7f);
				return termQuery;
			}
			
			if (getModifier("unmasked",modifiers)!=null)
			{
//				value = value.replace("\\*", "*");
//				value = value.replace("\\?", "?");
//				
//				value = value.replace("*", "\\*");
//				value = value.replace("?", "\\?");
				
				PhraseQuery phraseQuery = new PhraseQuery();
		        StringTokenizer tokenizer = new StringTokenizer(value, " ");
		        while (tokenizer.hasMoreTokens()) {
		                 String curValue = tokenizer.nextToken();
		                 phraseQuery.add(new Term(fieldName, curValue));
		             }
		        termQuery = phraseQuery;

				return termQuery;
			}
			
			// za sve ostale modifikatore vratiti da ih ne podrzavamo
			if (getModifier("relevant",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationRelevantModifier"));
			if (getModifier("phonetic",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationPhoneticModifier"));
			if (getModifier("partial",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationPartialModifier"));
			if (getModifier("ignoreAccents",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationIgnoreAccentsModifier"));
			if (getModifier("respectAccents",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationRespectAccentsModifier"));
			if (getModifier("substring",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationSubstringModifier"));
			if (getModifier("regexp",modifiers)!=null)
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationRegexpModifier"));
			
			Term term = new Term(fieldName, value);
			if(value.startsWith("?") || value.startsWith("*"))
			{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationForbidenValue"));
			}
			else if (value.indexOf("?") != -1 || value.indexOf("*")!=-1){
				termQuery = new WildcardQuery(term);
			} else {
				termQuery = new TermQuery(term);
			}
			
			if(errorMessage!=null)
				return null;
		}
		else
		{
			while(value.contains(" *"))
			{  // WORD * WORD --> word* word
				value = value.replace(" *", "*");
			}
			while(getModifier("unmasked",modifiers)==null && value.contains(" ?"))
			{  // WORD * WORD --> word* word
				value = value.replace(" ?", "?");
			}
			
			Term term = new Term(fieldName, value);
			if(value.startsWith("?") || value.startsWith("*"))
			{
				sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationForbidenValue"));
			}
			else if (value.indexOf("?") != -1 || value.indexOf("*")!=-1){
				termQuery = new WildcardQuery(term);
			} else {
				termQuery = new TermQuery(term);
			}
			
			/*
			StringTokenizer tokenizer = new StringTokenizer(value, " ");
			if(tokenizer.countTokens()>1)
			{
		        while (tokenizer.hasMoreTokens()) {
		            String curValue = tokenizer.nextToken();
		            Query curValueQuery = createTermQuery(fieldName, curValue, relation, modifiers);
		            andQuery((BooleanQuery) termQuery,curValueQuery);
		            }
		        return termQuery;
			}*/
		}
	}
	else if(relation.equals("==")||  relation.equalsIgnoreCase("adj"))
	{
		PhraseQuery phraseQuery = new PhraseQuery();
        StringTokenizer tokenizer = new StringTokenizer(value, " ");
        while (tokenizer.hasMoreTokens()) {
                 String curValue = tokenizer.nextToken();
                 phraseQuery.add(new Term(fieldName, curValue));
             }
             termQuery = phraseQuery;
		
	}
	else if(relation.equals("any") ||  relation.equals("ALL"))
	{
		/*int length = value.length();
		if(value.endsWith(".."))
			value = value.substring(0, length-2)+'*';
		else if (value.endsWith("."))
			value = value.substring(0, length-1)+'*';*/
		
		if(modifiers!=null)
		{
			value = ignoreOrRespectCase(value, modifiers);
			if(errorMessage!=null)
				return null;
			value = stemValue(value, modifiers);
			if(errorMessage!=null)
				return null;
			
			if (getModifier("unmasked",modifiers)!=null)
			{
//				value = value.replace("\\*", "*");
//				value = value.replace("\\?", "?");
//				
//				value = value.replace("*", "\\*");
//				value = value.replace("?", "\\?");
				
				PhraseQuery phraseQuery = new PhraseQuery();
		        StringTokenizer tokenizer = new StringTokenizer(value, " ");
		        while (tokenizer.hasMoreTokens()) {
		                 String curValue = tokenizer.nextToken();
		                 phraseQuery.add(new Term(fieldName, curValue));
		             }
		        termQuery = phraseQuery;
				
//				Term term = new Term(fieldName, value);
//				termQuery = new TermQuery(term);
				return termQuery;
			}
			if (getModifier("fuzzy",modifiers)!=null)
			{
				termQuery = new FuzzyQuery(new Term(fieldName, value), 0.7f);
				return termQuery;
			}
		}
		Term term = new Term(fieldName, value);
		if (value.indexOf("?") != -1 || value.indexOf("*")!=-1 ){
           termQuery = new WildcardQuery(term);
		} else {
           termQuery = new TermQuery(term);
		}
	}
	if(errorMessage!=null)
		return null;

	return termQuery;
}

public SpanQuery createSpanTermQuery(String fieldName, String value, String relation, Vector<Modifier> modifiers) {
	StringTokenizer tokenizer = new StringTokenizer(value, " ");
	if (tokenizer.countTokens()>1)
	{
		sendMessageLucine(rbSearch.getString("search.CQLContextSet.proxMultipleValue"));
		return null;
	}
	SpanQuery spanQuery = null;
	
	Term term = new Term(fieldName, value);
	spanQuery = new SpanTermQuery(term);
	
	return spanQuery;
}

	  public static void andQuery(BooleanQuery query, Query query2) {
	        /**
	         * required = true (must match sub query)
	         * prohibited = false (does not need to NOT match sub query)
	         */
	        query.add(query2, BooleanClause.Occur.MUST);
	    }

	    public static void orQuery(BooleanQuery query, Query query2) {
	        /**
	         * required = false (does not need to match sub query)
	         * prohibited = false (does not need to NOT match sub query)
	         */
	        query.add(query2, BooleanClause.Occur.SHOULD);
	    }

	    public static void notQuery(BooleanQuery query, Query query2) {
	        /**
	         * required = false (does not need to match sub query)
	         * prohibited = true (must not match sub query)
	         */
	        query.add(query2, BooleanClause.Occur.MUST_NOT);
	    }
	    


public String ignoreOrRespectCase(String value, Vector<Modifier> modifiers)
{
	if (getModifier("ignoreCase", modifiers)!=null)
	{
		return value.toLowerCase();
	}
	else if (getModifier("respectCase", modifiers)!=null)
	{
		return value;
	}
	return value;
}
public String stemValue(String value, Vector<Modifier> modifiers)
{
	if(getModifier("stem",modifiers)!=null)
	{
		String locale = "en";
		if(getModifier("locale",modifiers)!=null)
			locale = getModifier("locale",modifiers).getValue();
		
		if(locale.equalsIgnoreCase("en"))
		{
			PorterStemmer ps = new PorterStemmer();
			ps.setCurrent(value.toLowerCase());
			if(ps.stem()){
				String stream = ps.getCurrent();
				if(stream.equalsIgnoreCase("Invalid term") || stream.equalsIgnoreCase("No term entered"))
					sendMessageLucine(rbSearch.getString("search.CQLContextSet.relationStemModifier"));
				value = value.substring(0, stream.length()-1);
			}
		}
	}
	return value;
}
}
