package rs.ac.uns.ftn.informatika.bibliography.reports.obrazci;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResultEvaluator;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

public class ObrazciDataUtils {
	
	
	static{
		dataSource = DataSourceFactory.getDataSource();
		
	}
	
	public static List<EvaluatedRecord> getAllResults(String researcherId, Integer commissionId){		
		List<EvaluatedRecord> retVal = new ArrayList<EvaluatedRecord>();
		TermQuery termQuery = null;
		BooleanQuery query = new BooleanQuery();
		if(researcherId != null && !researcherId.equals("")){
			BooleanQuery idsQuery = new BooleanQuery();		
			termQuery = new TermQuery(new Term("AUCN", researcherId));
			idsQuery.add(termQuery, Occur.SHOULD);		
			query.add(idsQuery, Occur.MUST);
		}		
		List<Record> records = Retriever.select(query, new AllDocCollector(false));		
		for (Record record : records) {			
			ResultMeasureDTO resultType = getResultType(record, commissionId);
			if(resultType != null){
				EvaluatedRecord evalr = new EvaluatedRecord(record.getDto(), resultType);
				retVal.add(evalr);
			}
		}	
		return retVal;		
		
	}
	
	
	
	
	private static ResultMeasureDTO getResultType(Record record, Integer commissionId) {
		ResultMeasureDTO retVal = null;
	 	Connection conn = null;
		EvaluationDB evaluationDB = new EvaluationDB();
		try{
			conn = dataSource.getConnection();			
			List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
	        RuleBookDTO ruleBook = null;
			for (RuleBookDTO rb : ruleBookList){
	        	if("serbianResearchersEvaluation".equals(rb.getClassId()))
	        		ruleBook = rb;
	        }
		
			List<CommissionDTO> commissionList = new ArrayList<CommissionDTO>();
			List<CommissionDTO> allCommissionList = null;//evaluationDB.getCommissions(conn);
//			PublicationDTO publicationDTO = (PublicationDTO)(record.getDto());
//			List<AuthorDTO> authorsAndEditors = publicationDTO.getAllAuthors();
//			authorsAndEditors.addAll(publicationDTO.getEditors());
			if(commissionId!=null){
					for (CommissionDTO commissionDTO : allCommissionList) {
						if(commissionDTO.getCommissionId().equals(commissionId)){
							if(! commissionList.contains(commissionDTO))
								commissionList.add(commissionDTO);
						}
				}
			}
			
			retVal = null;//ResultEvaluator.getResultType(record, ruleBook, commissionList);
		}catch (Throwable e) {
			e.printStackTrace();		
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		if(retVal == null)
			return null;
		else 
			return retVal;
	}
	
	
	
	
	
	
	private static DataSource dataSource;
	

}
