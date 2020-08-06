package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperMonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;
import rs.ac.uns.ftn.informatika.bibliography.utils.Sanitizer;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportThesesTask implements Task {

	public ExportThesesTask(PrintWriter out) {
		this.out = out;
	}

	@Override
	public boolean execute() {
		try {
			List<StudyFinalDocumentDTO> theses = collectTheses();
			printTheses(theses);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	public List<StudyFinalDocumentDTO> collectTheses() throws ParseException{
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		BooleanQuery allThesesQuery = new BooleanQuery();
		Query type1 = new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT));
		Query type2 = new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT));
		allThesesQuery.add(type1, Occur.SHOULD);
		allThesesQuery.add(type2, Occur.SHOULD);
		List<Record> listTheses = recordDAO.getDTOs(allThesesQuery, new AllDocCollector(false));
		for (Record recordDTO : listTheses) {
			retVal.add((StudyFinalDocumentDTO)recordDTO.getDto());
		}
		return retVal;
	}
	
	private void printTheses(List<StudyFinalDocumentDTO> theses) {
//		out.println("Email,ID Monografije,Jezik,Naslov,Podnaslov,ISBN,Broj str.,Napomena,URI,Izdavac,Godina,Autor,Broj autocitata,Spisak autocitata,Recezent,Prikaz kriticki(navesti referencu)");
//		out.println("Email,ID Monografije,Jezik,Naslov,Podnaslov,ISBN,Broj str.,Napomena,URI,Izdavac,Godina,Urednik,Recezent,Prikaz kriticki(navesti referencu)");
		int i = 1;
		for (StudyFinalDocumentDTO thesis : theses) {
			thesis.loadRegisterEntry(true);
//			if(thesis.getRegisterEntry().getId() == null){
				String dateString = null;
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				if(thesis.getRegisterEntry().getDefendedOn() != null){
					Date date = thesis.getRegisterEntry().getDefendedOn().getTime();
					if(date != null)
						dateString = formatter.format(date);
				}
				String promotionString = null;
				if(thesis.getRegisterEntry().getPromotionDate() != null){
					Date date = thesis.getRegisterEntry().getPromotionDate().getTime();
					if(date != null)
						promotionString = formatter.format(date);
				}
				out.println(i++ + "," + 
						Sanitizer.sanitizeCSV(thesis.getAuthor().getName().toString()) + "," + 
						Sanitizer.sanitizeCSV(thesis.getSomeTitle()) + "," +
						Sanitizer.sanitizeCSV(thesis.getRegisterEntry().getInstitution()) + "," +
						thesis.getRegisterEntry().getId() + "," + 
						thesis.getRegisterEntry().getAcademicYear() + "," +
						thesis.getRegisterEntry().getDiplomaNumber() + "," +
						dateString + "," + 
						promotionString + "," +
						Sanitizer.sanitizeCSV(thesis.getRegisterEntry().getAdvisors()) + "," +
						Sanitizer.sanitizeCSV(thesis.getRegisterEntry().getCommissionMembers()) + "," +
						Sanitizer.sanitizeCSV(thesis.getRegisterEntry().getNameOfAuthorDegree()) 
						);
//			}
			thesis.clear();
		}
		out.flush();
	}

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private PrintWriter out;
	
	private static Log log = LogFactory.getLog(ExportConferencesTask.class.getName());

}
