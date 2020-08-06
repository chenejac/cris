package rs.ac.uns.ftn.informatika.bibliography.textsrv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import rs.ac.uns.ftn.informatika.bibliography.dto.QueryDTO;
import rs.ac.uns.ftn.informatika.bibliography.utils.LatCyrUtils;

import com.gint.util.string.StringUtils;


/**
 * Contains various utilities for querying.
 * 
 * @author "chenejac@uns.ac.rs"
 */
public class QueryUtils {

	public static void main(String[] args) {
		try{
		File fileDir = new File(args[0]);
		File fileDirOut = new File(args[1]);
//		File fileDictionaryStem = new File(args[2]);
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(fileDir), "UTF8"));
		
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(fileDirOut), "UTF8"));	
		
//		CroSerUtils.loadStemDictionary(fileDictionaryStem);
		        
		String str;
		      
		while ((str = in.readLine()) != null) {
			List<String> output = QueryUtils.throwAnalyzer(null, str);
			for(String o:output)
				out.append(o + " ");
			out.append("\r\n");
		}
		
		out.flush();
		out.close();
		        
        in.close();
	    } 
	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    }
	  }
	
	/**
	 * Parses a string query.
	 * 
	 * @param query
	 *            The Lucene string query
	 * @param analyzer
	 *            analyzer for parsing
	 * @param defaultOperator
	 *            default operator for parsing
	 * @return the created query
	 * @throws ParseException
	 */
	public static Query parseQuery(String query, Analyzer analyzer, Operator defaultOperator) throws ParseException {
		try {
			query = query.replace("..", ".").replace('.', '*');
//			query = StringUtils.clearDelimiters(query, delims);
			BooleanQuery.setMaxClauseCount(20000);// zbog heap-a
//			AnalyzingQueryParser p = new AnalyzingQueryParser("ALL", analyzer); 
			QueryParser p = new QueryParser("ALL", analyzer);
			p.setDefaultOperator(defaultOperator); 
			// default operator
			// je AND a ne OR
			// kao sto je inace
			// inicijalno
			Query q = p.parse(query);
			return q;
		} catch (ParseException e) {
			log.error(e);
			throw e;
		} 
	}

	/**
	 * Parses a string query.
	 * 
	 * @param query
	 *            The Lucene string query
	 * @param analyzer
	 *            analyzer for parsing
	 * @param defaultOperator
	 *            default operator for parsing
	 * @param defaultField
	 *            default Lucene field for parsing
	 * @return the created query
	 * @throws ParseException
	 */
	public static Query parseQuery(String query, Analyzer analyzer, Operator defaultOperator, String defaultField) throws ParseException {
		try {
			query = query.replace("..", ".").replace('.', '*');
//			query = StringUtils.clearDelimiters(query, delims);
			BooleanQuery.setMaxClauseCount(20000);// zbog heap-a
//			AnalyzingQueryParser p = new AnalyzingQueryParser("ALL", analyzer); 
			QueryParser p = new QueryParser(defaultField, analyzer);
			p.setDefaultOperator(defaultOperator); 
			// default operator
			// je AND a ne OR
			// kao sto je inace
			// inicijalno
			Query q = p.parse(query);
			return q;
		} catch (ParseException e) {
			log.error(e);
			throw e;
		} 
	}
	
	/**
	 * @param fieldName
	 * 			name of the field
	 * @param text
	 * 			text
	 * @param occur
	 * 			SHULD, MUST or MUST_NOT
	 * @param minimumShouldMatch
	 * 			if occur is SHOULD minimum percentage of queries which have to match
	 * @param minimumSimilarity
	 * 			minimum similarity of fuzzy query
	 * @return the created query
	 */
	public static BooleanQuery makeBooleanQuery(String fieldName, String text, Occur occur, float minimumShouldMatch, float minimumSimilarity, boolean leaveDelims){
		if(text == null)
			return new BooleanQuery();
		text = text.replace("..", ".").replace(".", "* ");
		if(leaveDelims == false)
			text = StringUtils.clearDelimiters(text, delims);
		BooleanQuery bq = new BooleanQuery();
		String[] values = text.split("\\s");
		List<String> realValues = new ArrayList<String>();
		for (String string : values) {
			if((!("".equals(string.trim()))) && (!("*".equals(string.trim())))){
				realValues.add(string);
			}
		}
		if(occur.equals(Occur.SHOULD))
			bq.setMinimumNumberShouldMatch((int)(minimumShouldMatch*realValues.size() + 1 ));
		if(realValues.size() == 0)
			realValues.add("*");
		for (String string : realValues) {
			if(leaveDelims == false)
				string = LatCyrUtils.toLatinUnaccented(string.toLowerCase());				
			if(string.contains("*") || string.contains("?")){
				bq.add(new WildcardQuery(new Term(fieldName, string)), occur);
			} else {
				bq.add(new FuzzyQuery(new Term(fieldName, string), minimumSimilarity), occur);
			}
		}
		return bq;
	}
	
	/**
	 * @param query
	 * 			query
	 * @return the created query
	 */
	public static Query makeQuery(QueryDTO query){
		Query retVal = null;
		String text = query.getValue();
		if(text == null)
			return new BooleanQuery();
		if(query.getType().equals(QueryDTO.MORE_EXACT_PHRASES)){
			String[] values = text.split(";");
			List<String> analyzedStrings = new ArrayList<String>();
			for (String string : values) {
				string = StringUtils.clearDelimiters(string, delims);
				QueryDTO newQueryDTO = new QueryDTO(0, QueryDTO.OR, query.getFieldName(), QueryDTO.EXACT_PHRASE);
				newQueryDTO.setValue(string);
				Query newQuery = QueryUtils.makeQuery(newQueryDTO);
				newQuery.setBoost(query.getBoost());
				String analyzedString = LatCyrUtils.toLatinUnaccented(string).toLowerCase().trim();
				if(retVal == null){
					analyzedStrings.add(analyzedString);
					retVal = newQuery;
				} else {
					if(! (analyzedStrings.contains(analyzedString))){
						analyzedStrings.add(analyzedString);
						retVal = QueryUtils.makeBooleanQuery(retVal, newQuery, QueryDTO.OR);
					}
				}
			}
		} else {
			if(! query.getType().equals(QueryDTO.EXACT_PHRASE))
				text = text.replace("..", ".").replace('.', '*');
			text = StringUtils.clearDelimiters(text, delims);
			String[] values = throwAnalyzer(query.getFieldName(), query.getValue()).toArray(new String[0]); //text.split("\\s");
			List<String> words = new ArrayList<String>();
			for (String string : values) {
				if((!("".equals(string.trim()))) && (!("*".equals(string.trim())))){
					words.add(string);
				}
			}
			if(query.getType().equals(QueryDTO.EXACT_PHRASE)){
				retVal = new PhraseQuery();
				retVal.setBoost(query.getBoost());
				for (String word : words) {
					((PhraseQuery)retVal).add(new Term(query.getFieldName(), word));
				}
			} else {
				retVal = new BooleanQuery();
				Occur occur = null;
				if(words.size() == 0)
					words.add("*");
				if(query.getType().equals(QueryDTO.SIMILAR)){
					occur = Occur.SHOULD;
					((BooleanQuery)retVal).setMinimumNumberShouldMatch((int)(0.75*words.size() + 1 ));
					for (String word : words) {
						if(word.contains("*") || word.contains("?")){
							Query tempQuery = new WildcardQuery(new Term(query.getFieldName(), word));
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						} else {
							Query tempQuery = new FuzzyQuery(new Term(query.getFieldName(), word), 0.7f);
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						}
					}
				}else if (query.getType().equals(QueryDTO.AT_LEAST_ONE)){
					occur = Occur.SHOULD;
					((BooleanQuery)retVal).setMinimumNumberShouldMatch(1);
					for (String word : words) {
						if(word.contains("*") || word.contains("?")){
							Query tempQuery = new WildcardQuery(new Term(query.getFieldName(), word));
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						} else {
							Query tempQuery = new TermQuery(new Term(query.getFieldName(), word));
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						}
					}
				} else {
					if(query.getType().equals(QueryDTO.ALL)){
						occur = Occur.MUST;
					} else if(query.getType().equals(QueryDTO.NONE)){
						occur = Occur.MUST_NOT;
					}
					for (String word : words) {
						word = LatCyrUtils.toLatinUnaccented(word.toLowerCase());
						if(word.contains("*") || word.contains("?")){
							Query tempQuery = new WildcardQuery(new Term(query.getFieldName(), word));
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						} else {
							Query tempQuery = new TermQuery(new Term(query.getFieldName(), word));
							tempQuery.setBoost(query.getBoost());
							((BooleanQuery)retVal).add(tempQuery, occur);
						}
					}
				}
			}
		}
		return retVal;
	}
	
	/**
	 * @param oldQuery
	 * 			existing boolean query
	 * @param newQuery
	 * 			newQuery
	 * @param operator
	 * 			AND, OR or AND_NOT
	 * @return the created query
	 */
	public static BooleanQuery makeBooleanQuery(Query oldQuery, Query newQuery, String operator){
		BooleanQuery retVal = null;
		BooleanQuery booleanQuery = null;
		if(oldQuery instanceof BooleanQuery){
			booleanQuery = (BooleanQuery) oldQuery;
		} else {
			booleanQuery = new BooleanQuery();
			if(operator.equals(QueryDTO.OR))
				booleanQuery.add(oldQuery, Occur.SHOULD);
			else 
				booleanQuery.add(oldQuery, Occur.MUST);
		}
		if(operator.equals(QueryDTO.AND)){
			boolean notNewBooleanQuery = true;
			for (BooleanClause booleanClause : booleanQuery.getClauses()) {
				if(booleanClause.getOccur().equals(Occur.SHOULD))
					notNewBooleanQuery = false;
			}
			if(notNewBooleanQuery)
				retVal = booleanQuery;
			else {
				retVal = new BooleanQuery();
				retVal.add(booleanQuery, Occur.MUST);
			}
			if(newQuery instanceof BooleanQuery){
				BooleanQuery newBooleanQuery = (BooleanQuery) newQuery;
				notNewBooleanQuery = true;
				for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
					if(booleanClause.getOccur().equals(Occur.SHOULD))
						notNewBooleanQuery = false;
				}
				if(notNewBooleanQuery){
					for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
						retVal.add(booleanClause);
					}
				}else {
					retVal.add(newBooleanQuery, Occur.MUST);
				}
			} else {
				retVal.add(newQuery, Occur.MUST);
			}
		} else if(operator.equals(QueryDTO.OR)){
			boolean notNewBooleanQuery = true;
			if(booleanQuery.getMinimumNumberShouldMatch() > 1){
				notNewBooleanQuery = false;
			} else {
				for (BooleanClause booleanClause : booleanQuery.getClauses()) {
					if(! (booleanClause.getOccur().equals(Occur.SHOULD)))
						notNewBooleanQuery = false;
				}
			}
			if(notNewBooleanQuery)
				retVal = booleanQuery;
			else {
				retVal = new BooleanQuery();
				retVal.add(booleanQuery, Occur.SHOULD);
			}
			
			if(newQuery instanceof BooleanQuery){
				BooleanQuery newBooleanQuery = (BooleanQuery) newQuery;
				notNewBooleanQuery = true;
				if(newBooleanQuery.getMinimumNumberShouldMatch() > 1){
					notNewBooleanQuery = false;
				} else {
					for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
						if(! (booleanClause.getOccur().equals(Occur.SHOULD)))
							notNewBooleanQuery = false;
					}
				}
				if(notNewBooleanQuery){
					for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
						retVal.add(booleanClause);
					}
				}else {
					retVal.add(newBooleanQuery, Occur.SHOULD);
				}
			} else {
				retVal.add(newQuery, Occur.SHOULD);
			}
		} else if(operator.equals(QueryDTO.AND_NOT)){
			boolean notNewBooleanQuery = true;
			for (BooleanClause booleanClause : booleanQuery.getClauses()) {
				if(booleanClause.getOccur().equals(Occur.SHOULD))
					notNewBooleanQuery = false;
			}
			if(notNewBooleanQuery)
				retVal = booleanQuery;
			else {
				retVal = new BooleanQuery();
				retVal.add(booleanQuery, Occur.MUST);
			}
			
			if(newQuery instanceof BooleanQuery){
				BooleanQuery newBooleanQuery = (BooleanQuery) newQuery;
				notNewBooleanQuery = true;
				for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
					if(booleanClause.getOccur().equals(Occur.SHOULD))
						notNewBooleanQuery = false;
				}
				if(notNewBooleanQuery){
					for (BooleanClause booleanClause : newBooleanQuery.getClauses()) {
						if(booleanClause.getOccur().equals(Occur.MUST))
							retVal.add(booleanClause.getQuery(), Occur.MUST_NOT);
						else 
							retVal.add(booleanClause.getQuery(), Occur.MUST);
					}
				}else {
					retVal.add(newBooleanQuery, Occur.MUST_NOT);
				}
			} else {
				retVal.add(newQuery, Occur.MUST_NOT);
			}
		}
		return retVal;
	}
	
	@SuppressWarnings("deprecation")
	public static List<String> throwAnalyzer(String fieldName, String value){
		if(fieldName==null)
			fieldName = "FT";
		value = StringUtils.clearDelimiters(value, delims);
//		final TokenStream tokenStream = new SerbianAnalyzer().tokenStream( fieldName , new StringReader( value ) );
//		final TokenStream tokenStream = new YuAnalyzer().tokenStream( fieldName , new StringReader( value ) );
		final TokenStream tokenStream = new CrisAnalyzer().tokenStream( fieldName , new StringReader( value ) );
		final List<String> result = new ArrayList< String >();
		Token tok;
		try {
			tok = tokenStream.next();
			while ( tok != null ) {
				result.add(tok.term());
				tok = tokenStream.next();
			}
		} catch (IOException e) {
			log.error(e);
		}
		return result;
	}
	
	public static String delims = ",;:\"()[]{}+/.!-";

	private static Log log = LogFactory.getLog(QueryUtils.class.getName());
}
