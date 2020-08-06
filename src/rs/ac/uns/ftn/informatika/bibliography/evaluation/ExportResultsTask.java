package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.db.CommissionDB;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.PersonDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.MentorDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.ResultForYearDTO;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportResultsTask implements Task {

	public ExportResultsTask(Connection conn, PrintWriter out) {
		this.out = out;
		this.conn = conn;
	}

	@Override
	public boolean execute() {
		try {
//			List<ResultForYearDTO> results = collectResults();
//			printResults(results);
			List<Record> publications = collectPublications();
			printPublications(publications);
//			List<Record> authors = collectAuthors();
//			printAuthors(authors);
//			List<MentorDTO> mentors = collectMentors();
//			printMentors(mentors);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}
	
	public List<ResultForYearDTO> collectResults(){
		List<ResultForYearDTO> retVal = new ArrayList<ResultForYearDTO>();
		PersonDB personDB = new PersonDB();
		List<Record> listAuthors = personDB.getInstitutionRecords(conn, "(BISIS)5929", "2014-01-01 00:00:00");
		List<String> ids = new ArrayList<String>();
		for (Record record : listAuthors) {
			try {
				ids.add(record.getControlNumber());
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		
//		List<String> years = new ArrayList<String>();
//		years.add("2011");
//		years.add("2010");
//		years.add("2009");
//		years.add("2008");
//		years.add("2007");
//		
//		
//		retVal = SamovrednovanjeUtils.getSCIResults(ids, years);
		
		List<String> years = new ArrayList<String>();
		years.add("2011");
		
		retVal = SamovrednovanjeUtils.getAllResults(ids, years);
		return retVal;
	}
	
	private void printResults(List<ResultForYearDTO> results) {
		int i = 1;
		for (ResultForYearDTO result : results) {
			out.println(i++ + "," + result.toString());
		}
		out.flush();
	}
	
	
	public List<Record> collectPublications(){
		BooleanQuery bq = new BooleanQuery();
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
		type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		
		List<Record> records = Retriever.select(bq, new AllDocCollector(false));
		
		return records;
	}
	
	private void printPublications(List<Record> publications) {
		int i = 1;
		CommissionDB commissionDB = new CommissionDB();
		EvaluationDB evaluationDB = new EvaluationDB();
				
		List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
		RuleBookDTO ruleBook = null;
		for (RuleBookDTO rb : ruleBookList){
			if("serbianResearchersEvaluation".equals(rb.getClassId()))
				ruleBook = rb;
		}
		List<CommissionDTO> allCommissionList = commissionDB.getCommissions(conn);
			
		for (Record record : publications) {
			StringBuffer row = new StringBuffer("");
			try{
				if(record.getDto() instanceof PublicationDTO){
					PublicationDTO publication = (PublicationDTO) record.getDto();
					if((publication.getAllAuthors() == null) || (publication.getAllAuthors().size() == 0))
						continue;
					ResultMeasureDTO resultMeasure = SamovrednovanjeUtils.getResultMeasure(record, ruleBook, allCommissionList, false);
					row.append(i++ + "," + Sanitizer.sanitizeCSV(publication.toString()));
					if((resultMeasure!= null) && (resultMeasure.getResultType()!=null))
						row.append(","+resultMeasure.getResultType().getClassId());
					else 
						row.append(",M99");
					for (AuthorDTO authorDTO : publication.getAllAuthors()) {
						row.append("," + authorDTO.getControlNumber());
					}
					publication.clear();
					out.println(row.toString());
				}
			} catch (Exception e){
				System.out.println(record.getControlNumber());
				e.printStackTrace();
			}
		}
		out.flush();
	}
	
	public List<Record> collectAuthors(){
		BooleanQuery bq = new BooleanQuery();
		BooleanQuery type = new BooleanQuery();
		type.add(new TermQuery(new Term("TYPE", Types.AUTHOR)), Occur.SHOULD);
		bq.add(type, Occur.MUST);
		
		List<Record> records = Retriever.select(bq, new AllDocCollector(false));
		
		return records;
	}
	
	private void printAuthors(List<Record> authors) {
		int i = 1;
		for (Record record : authors) {
			AuthorDTO author = (AuthorDTO) record.getDto();
			out.println(i++ + "," + author.getControlNumber() + "," + Sanitizer.sanitizeCSV(author.toString()));
		}
		out.flush();
	}
	
	public List<MentorDTO> collectMentors(){
		List<MentorDTO> retVal = new ArrayList<MentorDTO>();
		PersonDB personDB = new PersonDB();
		List<Record> listAuthors = personDB.getInstitutionRecords(conn, "(BISIS)5929", "2014-01-01 00:00:00");
		List<String> ids = new ArrayList<String>();
		for (Record record : listAuthors) {
			try {
				ids.add(record.getControlNumber());
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		
		
		retVal = SamovrednovanjeUtils.getMentors(ids, "2014", 10, 5);
		
		return retVal;
	}
	
	private void printMentors(List<MentorDTO> mentors) {
		int i = 1;
		for (MentorDTO mentor : mentors) {
			out.println(i++ + "," + mentor.toString());
		}
		out.flush();
	}

	private PrintWriter out;
	
	private Connection conn;
	
	private static Log log = LogFactory.getLog(ExportAuthorsTask.class.getName());

}
