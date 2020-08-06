package rs.ac.uns.ftn.informatika.bibliography.wrapper;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.List;

import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanQuery;
import org.z3950.zing.cql.*;

import rs.ac.uns.ftn.informatika.bibliography.contextset.util.SRWValidation;
import rs.ac.uns.ftn.informatika.bibliography.mediator.MediatorService;

//import sruserver.validator.SRWValidation;

public class LuceneWrapper {
	private Context ctx = new Context();
	private HashMap<String, String> prefixes = new HashMap<String, String>();
	
	public String errorMessage = null;
	public ResourceBundle rbSearch = PropertyResourceBundle.getBundle("messages.messages-search", new Locale("en"));
	public Locale localeLan = new Locale("en");
	
	public Query makeRootQuery(CQLNode node) {
		errorMessage = null;
		ctx.setLocaleLan(localeLan);

		if(SRWValidation.getPath()==null || SRWValidation.getPath().equalsIgnoreCase("") || SRWValidation.getSets()==null)
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			
			SRWValidation.setPath(MediatorService.contextSetPath);
			StringTokenizer setsT = new StringTokenizer(rb.getString("sets"),",");
			String [] sets = new String[setsT.countTokens()];
			
			int granica = setsT.countTokens();
			
			for(int i=0; i<granica; i++)
			{
				sets[i]= rb.getString(setsT.nextToken().trim());
			}
			SRWValidation.setSets(sets);
			SRWValidation.getContextSets();
		}
		if(errorMessage!=null)
			return null;
		
		return makeQuery(node);
	}

	public Query makeQuery(CQLNode node) {
		if(errorMessage!=null)
			return null;
		return makeQuery(node, false);
	}

	public Query makeQuery(CQLNode node, boolean spanqery) {
		Query query = null;
		if (node instanceof CQLBooleanNode) {
			CQLBooleanNode cbn = (CQLBooleanNode) node;
			query = parseBooleanNode(cbn,spanqery);
			if(errorMessage!=null)
				return null;
		} else if (node instanceof CQLPrefixNode) {
			CQLPrefixNode cpn = (CQLPrefixNode) node;
			CQLPrefix prefix = cpn.prefix;
			String name = prefix.name;
			String identifier = prefix.identifier;
			if (SRWValidation.checkInIndexSetURL(identifier)) { 
				addPrefix(name, identifier);
				query = makeQuery(cpn.subtree,spanqery);
				removePrefix(name);
			} else { 
				String pattern = rbSearch.getString("search.LuceneWrapper.identifierURL");
				String messageToSend = MessageFormat.format(pattern, identifier);
				sendMessageLucine(messageToSend);
				return null;
			}
		} else if (node instanceof CQLTermNode) {
			CQLTermNode ctn = (CQLTermNode) node;
			query = parseTermNode(ctn, spanqery);
			if(errorMessage!=null)
				return null;
		}
		else if (node instanceof CQLSortNode) {
			sendMessageLucine(rbSearch.getString("search.LuceneWrapper.CQLSort"));
			return null;
		}
		
		if(errorMessage!=null)
			return null;
		
		return query;
	}


	@SuppressWarnings("unchecked")
	public Query parseTermNode(CQLTermNode ctn, boolean spanqery) {
		Query query = null;
		//provera da li je u okviru term noda definisan index i da li index pripada definisanom setu
		String index = "";
		String contextSetNameIndex = "";
		if(ctn.getIndex()==null)
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			contextSetNameIndex = rb.getString("defaultSet").trim();
			index = rb.getString("defaultIndex").trim();
		}
		else if (ctn.getIndex().equalsIgnoreCase(""))
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			contextSetNameIndex = rb.getString("defaultSet").trim();
			index = rb.getString("defaultIndex").trim();
		}
		else
		{
			index = ctn.getIndex();
			contextSetNameIndex = determinateContexSet(index);
			if(errorMessage!=null)
				return null;
		}
		
		if (index.indexOf(".") != -1) {
			index = index.substring(index.indexOf(".") + 1);
		}
		
		if(SRWValidation.checkIndexInContexSet(contextSetNameIndex, index)==false)
		{
			String pattern = rbSearch.getString("search.LuceneWrapper.indexContextSet");
			String messageToSend = MessageFormat.format(pattern, index, contextSetNameIndex);
			sendMessageLucine(messageToSend);
			return null;
		}
		
		//provera da li je u okviru term noda definisan relacija i da li relacija pripada definisanom setu
		String relation = "";
		String contextSetNameRelation = "";
		if(ctn.getRelation()==null)
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			relation = rb.getString("defaultRelation").trim();
			contextSetNameRelation = rb.getString("defaultSet").trim();
			String term = ctn.getTerm();
			ctn = new CQLTermNode(index, new CQLRelation(relation), term);
		}
		else if (ctn.getRelation().getBase()==null)
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			relation = rb.getString("defaultRelation").trim();
			contextSetNameRelation = rb.getString("defaultSet").trim();
			String term = ctn.getTerm();
			ctn = new CQLTermNode(index, new CQLRelation(relation), term);
		}
		else if (ctn.getRelation().getBase().equalsIgnoreCase(""))
		{
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			relation = rb.getString("defaultRelation").trim();
			contextSetNameRelation = rb.getString("defaultSet").trim();
			String term = ctn.getTerm();
			ctn = new CQLTermNode(index, new CQLRelation(relation), term);
		}
		else
		{
			relation = ctn.getRelation().getBase();
			contextSetNameRelation = determinateContexSet(ctn.getRelation().getBase());
			if(errorMessage!=null)
				return null;
		}
		
		if (relation.indexOf(".") != -1) {
			relation = relation.substring(index.indexOf(".") + 1);
		}
		
		if(SRWValidation.checkRelationInContexSet(contextSetNameRelation, relation)==false)
		{
			String pattern = rbSearch.getString("search.LuceneWrapper.relationContextSet");
			String messageToSend = MessageFormat.format(pattern, relation, contextSetNameRelation);
			sendMessageLucine(messageToSend);
			return null;
		}
		
		//provera da li je u okviru term noda definisan modifikatori relacija i da li modifikator relacija ima isti contex set kao relacija
		if(ctn.getRelation().getModifiers()!=null)
		{
			for(Modifier modifier : ctn.getRelation().getModifiers())
			{
				String type = modifier.getType();
				String comparison = (modifier.getComparison()!=null)?(!modifier.getComparison().equalsIgnoreCase("")?modifier.getComparison():null):null;
				String value = (modifier.getValue()!=null)?(!modifier.getValue().equalsIgnoreCase("")?modifier.getValue():null):null;
			
				if(SRWValidation.checkModifierInContexSet(contextSetNameRelation, type)==false)
				{	
					String pattern = rbSearch.getString("search.LuceneWrapper.relationModifierContextSet");
					String messageToSend = MessageFormat.format(pattern, type, contextSetNameRelation+" "+relation);
					sendMessageLucine(messageToSend);
					return null;
				}
				
				if(SRWValidation.checkRelationModifierUsageInContexSet(contextSetNameRelation, type, comparison, value)==false)
				{
					String pattern = rbSearch.getString("search.LuceneWrapper.relationModifierUsageContextSet");
					String messageToSend = MessageFormat.format(pattern, type, comparison+" "+ value, contextSetNameRelation );
					sendMessageLucine(messageToSend);
					return null;
				}
			}
		}
		
		ctx.setStrategy(contextSetNameIndex);
		Object obj = ctx.mapIndexToLucene(index,spanqery);
		
		if(ctx.getErrorMessage()!=null)
		{	errorMessage = ctx.getErrorMessage();
			return null;
		}
		
		if(obj==null)
		{
			String pattern = rbSearch.getString("search.LuceneWrapper.indexMappingContextSet");
			String messageToSend = MessageFormat.format(pattern, index, contextSetNameIndex);
			sendMessageLucine(messageToSend);
			return null;
		}
		
		if (obj instanceof List) {
			int countTokens = ((List<String>)obj).size();
			
			Query[] tempListQyery = null;
			if(spanqery)
				tempListQyery = new SpanQuery[countTokens];
			else
				tempListQyery = new Query[countTokens];
			
			countTokens = 0;
			for(String lucineIndex : ((List<String>)obj))
			{
				if (!lucineIndex.equalsIgnoreCase("")) { 
					
					ctx.setStrategy(contextSetNameRelation);
					tempListQyery[countTokens] = ctx.parseRelation(lucineIndex, relation, ctn, spanqery);
					if(ctx.getErrorMessage()!=null)
					{	errorMessage = ctx.getErrorMessage();
						return null;
					}
					countTokens += 1;
					
				} else {
					String pattern = rbSearch.getString("search.LuceneWrapper.indexMappingEmptyContextSet");
					String messageToSend = MessageFormat.format(pattern, index, contextSetNameIndex);
					sendMessageLucine(messageToSend);
					return null;
				}
			}
			
			if(countTokens==1)
			{
				query = tempListQyery[0];
			}
			else if (spanqery==true && countTokens>1)
			{
//				SpanQuery [] sq = new SpanQuery[countTokens];
//				for(int i=0;i<countTokens;i++)
//				{
//					sq[i]=(SpanQuery)tempListQyery[i];
//				}
//				query = new SpanOrQuery(sq);
				
				query = (SpanQuery)tempListQyery[0];
				//nije podrzano preslikavanje na vise lucene indeksa samo an jedan
				//ogranicenje samog lucene paketa za SpanORQuery koji ne sadrzi iste fildove
				
			}
			else if (spanqery==false && countTokens>1)
			{
//				if (!(tempListQyery[0] instanceof BooleanQuery))
//				{
//					System.out.println("Prvi nije boolean");
//					query = new BooleanQuery();
//					orQuery((BooleanQuery)query, tempListQyery[0]);
//				}
//				else{
//					
//					query = new BooleanQuery();
//					orQuery((BooleanQuery)query, tempListQyery[0]);
//					System.out.println("Prvi je boolean");
////					query = tempListQyery[0];
//				}
				query = new BooleanQuery();
				for(int i=0;i<countTokens;i++)
				{
					orQuery((BooleanQuery)query, tempListQyery[i]);
				//	System.out.println(tempListQyery[i].toString());
				}
				//System.out.println(query.toString());
			}
		} 
		else if (obj instanceof Query){
			return (Query) obj;
		}
		else
		{
			String pattern = rbSearch.getString("search.LuceneWrapper.indexMappingJavaContextSet");
			String messageToSend = MessageFormat.format(pattern, index, contextSetNameIndex);
			sendMessageLucine(messageToSend);
			return null;
		}
		return query;
	}

public Query parseBooleanNode(CQLBooleanNode cbn, boolean spanqery)
{
	Query query = null;
	
	if (cbn instanceof CQLProxNode)
	{
		boolean reorgTreeBool = false;
		reorgTreeBool = shoudReorganizeTree((CQLNode)cbn);
		if(reorgTreeBool==true)
		{
			cbn = (CQLBooleanNode) reorganizeTree((CQLNode)cbn);
		}
		
		if(errorMessage!=null)
			return null;
		
		if(reorgTreeBool==true)
		{	
			query = makeQuery(cbn);
			if(errorMessage!=null)
				return null;
		}
		else
		{
			// obradimo levi operand
			Query left = makeQuery(cbn.left, true);
			if(errorMessage!=null)
				return null;
			if(!(left instanceof SpanQuery))
			{
				sendMessageLucine(rbSearch.getString("search.LuceneWrapper.leftSpanQuery"));
				return null;
			}
			
			// obradimo desni operand
			Query right = makeQuery(cbn.right, true);
			if(errorMessage!=null)
				return null;
			if(!(right instanceof SpanQuery))
			{
				sendMessageLucine(rbSearch.getString("search.LuceneWrapper.rightSpanQuery"));
				return null;
			}
			
			if(cbn.ms==null)
			{
				cbn.ms = new ModifierSet("PROX");
			}
			
			if(cbn.ms.getModifiers().size()<1)
			{
				cbn.ms.addModifier("distance", "=", "1");
				cbn.ms.addModifier("unit", "=", "word");
				cbn.ms.addModifier("unordered");
			}
			
			String contexSetName = null;
			
			ModifierSet ms = cbn.ms;
			//chack for ALL approved modifiers
			for(Modifier modifier : ms.getModifiers())
			{
				boolean found = false;
				String type = modifier.getType();
				String comparison = (modifier.getComparison()!=null)?(!modifier.getComparison().equalsIgnoreCase("")?modifier.getComparison():null):null;
				String value = (modifier.getValue()!=null)?(!modifier.getValue().equalsIgnoreCase("")?modifier.getValue():null):null;
				if(contexSetName!=null)
				{
					if(SRWValidation.checkModifierInContexSet(contexSetName, type))
					{	
						found = SRWValidation.checkBooleanModifierUsageInContexSet(contexSetName, type, comparison, value);
					}
				}
				else
				{	
					for(String prefix : prefixes.keySet())
					{	
						if(SRWValidation.checkInIndexSetURL(prefixes.get(prefix)))
							contexSetName = SRWValidation.getIndexSetName(prefixes.get(prefix));
						if(SRWValidation.checkModifierInContexSet(contexSetName, type))
							if(SRWValidation.checkBooleanModifierUsageInContexSet(prefix, type, comparison, value)) {
								found = true;
								break;
							}
					}
					if(found==false)
					{	
						ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
						contexSetName = rb.getString("defaultSet").trim();
						if(SRWValidation.checkModifierInContexSet(contexSetName, type))
						{	
							found = SRWValidation.checkBooleanModifierUsageInContexSet(contexSetName, type, comparison, value);
						}
						else
						{
							String pattern = rbSearch.getString("search.LuceneWrapper.proxModifierNotRegistred");
							String messageToSend = MessageFormat.format(pattern, modifier.getType());
							sendMessageLucine(messageToSend);
							return null;
						}
					}
				}
				if(found==false)
				{
					String pattern = rbSearch.getString("search.LuceneWrapper.proxModifierNotCorrectlyUsed");
					String messageToSend = MessageFormat.format(pattern, contexSetName, modifier.getType(), modifier.getComparison(), modifier.getValue());
					sendMessageLucine(messageToSend);
					return null;
				}
			}
			ctx.setStrategy(contexSetName);
			query = ctx.parseProx(cbn.ms, (SpanQuery)left, (SpanQuery)right);
			if(ctx.getErrorMessage()!=null)
			{	errorMessage = ctx.getErrorMessage();
				return null;
			}
		}
		
	}
	else
	{
		// obradimo levi operand
		Query left = makeQuery(cbn.left, spanqery);
		if(errorMessage!=null)
			return null;
		
		// obradimo desni operand
		Query right = makeQuery(cbn.right, spanqery);
		if(errorMessage!=null)
			return null;
		
		// spojimo levi i desni operand sa operatorom
		if (cbn instanceof CQLAndNode) {
			query = new BooleanQuery();
			andQuery((BooleanQuery) query, left);
			andQuery((BooleanQuery) query, right);
		} else if (cbn instanceof CQLNotNode) {
			query = new BooleanQuery();
			orQuery((BooleanQuery) query, left);
			notQuery((BooleanQuery) query, right);	
		} else if (cbn instanceof CQLOrNode) {
			query = new BooleanQuery();
			orQuery((BooleanQuery) query, left);
			orQuery((BooleanQuery) query, right);
		} else {
			sendMessageLucine(rbSearch.getString("search.LuceneWrapper.UnknownBoolean"));
			return null;
		}
	}
	
	if(errorMessage!=null)
		return null;
	
	return query;
}

	public static void andQuery(BooleanQuery query, Query query2) {
		query.add(query2, BooleanClause.Occur.MUST);
	}

	public static void orQuery(BooleanQuery query, Query query2) {

		query.add(query2, BooleanClause.Occur.SHOULD);
	}

	public static void notQuery(BooleanQuery query, Query query2) {

		query.add(query2, BooleanClause.Occur.MUST_NOT);
	}
	
	public static void proxQuery(BooleanQuery query, Query query2) {
		query.add(query2, BooleanClause.Occur.MUST);
	}
	
	public String determinateContexSet (String IndexOrRelation)
	{
		String contextSetName = "";
		if (IndexOrRelation.indexOf(".") != -1) {
			String pref = IndexOrRelation.substring(0, IndexOrRelation.indexOf("."));
			
			if(SRWValidation.checkInIndexSetURL(prefixes.get(pref)))
				contextSetName = SRWValidation.getIndexSetName(prefixes.get(pref));
			else if (SRWValidation.checkInIndexSet(pref)) {
				contextSetName = pref;
			}else {
				String pattern = rbSearch.getString("search.LuceneWrapper.prefixNotSuportedContextSet");
				String messageToSend = MessageFormat.format(pattern, pref);
				sendMessageLucine(messageToSend);
				return null;
			}

		} else {
			ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.wrapper.contextSet");
			contextSetName =rb.getString("defaultSet").trim();
		}
		
		return contextSetName;
	}
	
	public boolean shoudReorganizeTree(CQLNode cn)
	{
		boolean reorgTreeBool = false;
		
		if(cn instanceof CQLTermNode)
		{
			reorgTreeBool = false;
			return reorgTreeBool;
		}
		else if(cn instanceof CQLPrefixNode)
		{
			CQLNode prefNodTree = ((CQLPrefixNode)cn).subtree;
			reorgTreeBool = shoudReorganizeTree(prefNodTree);
			return reorgTreeBool;
		}
		else if(cn instanceof CQLBooleanNode && !(cn instanceof CQLProxNode))
		{
			reorgTreeBool = true;
			return reorgTreeBool;
		}
		else if (cn instanceof CQLProxNode)
		{
			//u svim ostalim slucajevima moze biti samo prox node
			CQLProxNode cpn = (CQLProxNode) cn;
			//obrada levog podrveta ako je ono prox
			reorgTreeBool =  shoudReorganizeTree(cpn.left);	
			//obrada desnog podrveta ako je ono prox
			reorgTreeBool =  reorgTreeBool ||  shoudReorganizeTree(cpn.right);
		}
		return reorgTreeBool;
	}
	
	public CQLNode reorganizeTree(CQLNode cn)
	{
		if(cn instanceof CQLTermNode)
		{
			return cn;
		}
		else if(cn instanceof CQLPrefixNode)
		{
			CQLNode prefNodTree = ((CQLPrefixNode)cn).subtree;
			((CQLPrefixNode)cn).subtree = reorganizeTree(prefNodTree);
			return cn;
		}
		else if(cn instanceof CQLBooleanNode && !(cn instanceof CQLProxNode))
		{
			return cn;
		}
		
		//u svim ostalim slucajevima moze biti samo prox node
		CQLProxNode cpn = (CQLProxNode) cn;

		//obrada levog podrveta ako je ono prox
		if (((cpn.left instanceof CQLBooleanNode) && !(cpn.left instanceof CQLProxNode))==false)
		{	
			cpn.left =  reorganizeTree(cpn.left);
		}
		//obrada desnog podrveta ako je ono prox
		if (((cpn.right instanceof CQLBooleanNode) && !(cpn.right instanceof CQLProxNode))==false)
		{	
			cpn.right =  reorganizeTree(cpn.right);
		}

		// resavanje 4 moguca situacije
		// CQLBooleanNode prox CQLBooleanNode
		// CQLBooleanNode prox CQLNode
		// CQLNode prox CQLBooleanNode
		// CQLNode prox CQLNode
		boolean leftB = false;
		boolean rightB = false;
		String LL = null;
		String LR = null;
		String RL = null;
		String RR = null;
		String leftBoolean = null;
		String rightBoolean = null;
		
		if(cpn.left instanceof CQLBooleanNode && !(cpn.left instanceof CQLProxNode))
		{	
			leftB = true;
			LL = (String)((CQLBooleanNode) cpn.left).left.toCQL();
		    LR = (String)((CQLBooleanNode) cpn.left).right.toCQL();

	    	if (cpn.left instanceof CQLAndNode) {
	    	    leftBoolean = "AND";
	    	} else if (cpn.left instanceof CQLNotNode) {
	    		leftBoolean = "NOT";
	    	} else if (cpn.left instanceof CQLOrNode) {
	    		leftBoolean = "OR";
	    	}
	    	else {
	    		sendMessageLucine(rbSearch.getString("search.LuceneWrapper.UnknownBooleanReorganizeTree"));
	    	}
		}
		if(cpn.right instanceof CQLBooleanNode && !(cpn.right instanceof CQLProxNode))
		{	
			rightB = true;      	
	    	RL = (String)((CQLBooleanNode) cpn.right).left.toCQL();
	    	RR = (String)((CQLBooleanNode) cpn.right).right.toCQL();
	    	    	
	    	if (cpn.right instanceof CQLAndNode) {
	    	    rightBoolean = "AND";
	    	} else if (cpn.right instanceof CQLNotNode) {
	    		rightBoolean = "NOT";
	    	} else if (cpn.right instanceof CQLOrNode) {
	    		rightBoolean = "OR";
	    	}
	    	else{
	    		sendMessageLucine(rbSearch.getString("search.LuceneWrapper.UnknownBooleanReorganizeTree"));
	    	}
		}
		String modifierSet = "";
		if (cpn.ms!=null)
		{
			modifierSet = (String) cpn.ms.toCQL();
		}
		else
		{
			modifierSet = "PROX";
		}
		
		if (leftB && rightB)
		{
			String cqlRestructuredString = null;
			cqlRestructuredString = "(( "+LL+" "+modifierSet+" "+ RL+" ) "+ rightBoolean +" ( "+LL+" "+modifierSet+" "+ RR+" )) " + leftBoolean +
									"(( "+LR+" "+modifierSet+" "+ RL+" ) "+ rightBoolean +" ( "+LR+" "+modifierSet+" "+ RR+" ))";
			CQLParser parser = new CQLParser();
    	    try {
    	    	return parser.parse((String)cqlRestructuredString);
			} catch (CQLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (leftB)
		{
			String cqlRestructuredString = null;
			cqlRestructuredString = "( "+LL+" "+modifierSet+" "+ cpn.right.toCQL() +" ) "+ leftBoolean +
								   " ( "+LR+" "+modifierSet+" "+ cpn.right.toCQL() +" )";
			CQLParser parser = new CQLParser();
    	    try {
    	    	return parser.parse((String)cqlRestructuredString);
			} catch (CQLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (rightB)
		{	
			String cqlRestructuredString = null;
			cqlRestructuredString = "( "+ cpn.left.toCQL() + " "+ modifierSet+ " "+ RL+" ) "+ rightBoolean + 
								   " ( "+ cpn.left.toCQL() + " "+ modifierSet+ " "+ RL+" )";
			CQLParser parser = new CQLParser();
    	    try {
    	    	
    	    	return parser.parse((String)cqlRestructuredString);
			} catch (CQLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cn;
	}
	
	public void sendMessageLucine(String messageS)
	{
//		System.out.println(messageS);
		errorMessage = messageS;
	}
	
	public void addPrefix(String name, String identifier)
	{
		String value = prefixes.get(name);
		if (value == null) {
			prefixes.put(name, identifier);
		}
	}
	public void removePrefix(String name)
	{
		prefixes.remove(name);
	}

	/**
	 * @return the localeLan
	 */
	public Locale getLocaleLan() {
		return localeLan;
	}
	/**
	 * @param localeLan the localeLan to set
	 */
	public void setLocaleLan(Locale localeLan) {
		rbSearch = PropertyResourceBundle.getBundle(
				"messages.messages-search", localeLan);
		ctx.setLocaleLan(localeLan);
		this.localeLan = localeLan;
	}
}
