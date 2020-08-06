package rs.ac.uns.ftn.informatika.bibliography.interoperability.export;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

public class ExportUtils {
	
	public static List<PublicationDTO> getAllResultsForAuthor(String authorId){
		try{
			List<PublicationDTO> retVal = new ArrayList<PublicationDTO>();
			TermQuery termQuery = null;
			BooleanQuery query = new BooleanQuery();
			if(authorId != null){
				BooleanQuery idsQuery = new BooleanQuery();				
				termQuery = new TermQuery(new Term("AUCN", authorId));
				idsQuery.add(termQuery, Occur.SHOULD);				
				query.add(idsQuery, Occur.MUST);		    
			}		
			List<Record> records = Retriever.select(query, new AllDocCollector(false));		
			
			for (Record record : records) {				
				retVal.add((PublicationDTO)record.getDto());
			}			
			return retVal;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}		
	}
	
	public static List<PublicationDTO> getAllResultsForAuthors(List<String> ids){
		try{
			List<PublicationDTO> retVal = new ArrayList<PublicationDTO>();
			TermQuery termQuery = null;
			BooleanQuery query = new BooleanQuery();
			if(ids != null){
				BooleanQuery idsQuery = new BooleanQuery();
				for (String id : ids) {		
					termQuery = new TermQuery(new Term("AUCN", id));
					idsQuery.add(termQuery, Occur.SHOULD);
					termQuery = new TermQuery(new Term("EDCN", id));
					idsQuery.add(termQuery, Occur.SHOULD);
				}
				query.add(idsQuery, Occur.MUST);
			}
			List<Record> records = Retriever.select(query, new AllDocCollector(false));		
			
			for (Record record : records) {				
				retVal.add((PublicationDTO)record.getDto());
			}			
			return retVal;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}		
	}
	
	public static List<AuthorDTO> getAllAuthorsAndCoauthors(List<String> authorsIds){
		try{
			
			List<AuthorDTO> retVal = new ArrayList<AuthorDTO>();
			TermQuery termQuery = null;
			BooleanQuery query = new BooleanQuery();
			if(authorsIds != null){
				BooleanQuery idsQuery = new BooleanQuery();
				for (String id : authorsIds) {		
					termQuery = new TermQuery(new Term("AUCN", id));
					idsQuery.add(termQuery, Occur.SHOULD);					
				}
				query.add(idsQuery, Occur.MUST);
			}		
			List<Record> records = Retriever.select(query, new AllDocCollector(false));		
			
			for (Record record : records) {				
				PublicationDTO pub = (PublicationDTO)record.getDto();
				List<AuthorDTO> authors = pub.getAllAuthors();
				for(AuthorDTO aut:authors){
					if(!authorInList(aut, retVal))
						retVal.add(aut);
				}
			}			
			return retVal;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}		
	}
	
	public static String getResultType(Record rec){
		String resultType = SamovrednovanjeUtils.getResultType(rec);
		return resultType;
	}
	
	public static boolean authorInList(AuthorDTO author, List<AuthorDTO> list){
		for(AuthorDTO aut:list)
			if(aut.getControlNumber().equals(author.getControlNumber())) return true;
		return false;
	}
	
	

}
