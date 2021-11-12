package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
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
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportMonographsTask implements Task {

	public ExportMonographsTask(PrintWriter out) {
		this.out = out;
	}

	@Override
	public boolean execute() {
		try {
			List<MonographAndPapers> monographs = collectMonographs();
			printMonographs(monographs);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	public List<MonographAndPapers> collectMonographs() throws ParseException{
		List<MonographAndPapers> retVal = new ArrayList<MonographAndPapers>();
		BooleanQuery allMonographsQuery = new BooleanQuery();
		Query type = new TermQuery(new Term("TYPE", Types.MONOGRAPH));
		allMonographsQuery.add(new TermQuery(new Term("PY", "2020")), Occur.MUST);
		allMonographsQuery.add(type, Occur.MUST);
//		QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
//		Query orgUnitQuery = qparser.parse("+(INS:\"departman za matematiku i informatiku\" INS:\"katedra za opstu algebru i teorijsko racunarstvo\" INS:\"katedra za analizu verovatnocu i diferencijalne jednacine\" INS:\"katedra za numericku matematiku\" INS:\"katedra za primenjenu algebru\" INS:\"katedra za funkcionalnu analizu geometriju i topologiju\" INS:\"katedra za racunarske nauke\" INS:\"katedra za matematicku logiku i diskretnu matematiku\" INS:\"katedra za primenjenu analizu\" INS:\"katedra za informacione sisteme\")");
//		Query orgUnitQuery = qparser.parse("+(INS:\"departman za hemiju biohemiju i zastitu zivotne sredine\" INS:\"katedra za analiticku hemiju\" INS:\"katedra za biohemiju i hemiju prirodnih proizvoda\" INS:\"katedra za fizicku hemiju\" INS:\"katedra za hemijsko obrazovanje i metodiku nastave hemije\" INS:\"katedra za hemijsku tehnologiju i zastitu zivotne sredine\" INS:\"katedra za opstu i neorgansku hemiju\" INS:\"katedra za organsku hemiju\")");
//		Query orgUnitQuery = qparser.parse("+(INS:\"tehnoloski fakultet novi sad\")");
//		allMonographsQuery.add(orgUnitQuery, Occur.MUST);
//		PhraseQuery phraseQuery = new PhraseQuery();
//		phraseQuery.add(new Term("INS", "Departman za matematiku i informatiku"));
//		allMonographsQuery.add(phraseQuery, Occur.MUST);
		List<Record> listMonographs = recordDAO.getDTOs(allMonographsQuery, new AllDocCollector(false));
		for (Record recordDTO : listMonographs) {
			try {
				String monographClassification = "";
				System.out.println(recordDTO.getControlNumber());
				recordDTO.loadFromDatabase();
				MonographDTO monographDTO = (MonographDTO) recordDTO.getDto();
				Calendar publicationDate = new GregorianCalendar();
				Date publicationTime = new Date();
				publicationDate.setTime(publicationTime); 
				publicationDate.set(GregorianCalendar.YEAR, Integer.parseInt(monographDTO.getPublicationYear()));
				for (Classification classification : recordDTO.getRecordClasses()) {
					if(
							(!("0".equals(classification.getCommissionId().toString())))
					&& (classification.getCfClassSchemeId().equals("type")) 
					&&  (! (publicationDate.before(classification.getCfStartDate()))) 
					&& (!(publicationDate.after(classification.getCfEndDate())))){
						monographClassification = classification.getCfClassId();
					}
				}
				if (monographDTO != null) {
					BooleanQuery allMonographPapersQuery = new BooleanQuery();
					type = new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH));
//					allMonographPapersQuery.add(new TermQuery(new Term("PY", "2014")), Occur.MUST_NOT);
					allMonographPapersQuery.add(type, Occur.MUST);
//					qparser = new QueryParser("INS", new CrisAnalyzer());
//					Query orgUnitQuery = qparser.parse("+(INS:\"departman za matematiku i informatiku\" INS:\"katedra za opstu algebru i teorijsko racunarstvo\" INS:\"katedra za analizu verovatnocu i diferencijalne jednacine\" INS:\"katedra za numericku matematiku\" INS:\"katedra za primenjenu algebru\" INS:\"katedra za funkcionalnu analizu geometriju i topologiju\" INS:\"katedra za racunarske nauke\" INS:\"katedra za matematicku logiku i diskretnu matematiku\" INS:\"katedra za primenjenu analizu\" INS:\"katedra za informacione sisteme\")");
//					orgUnitQuery = qparser.parse("+(INS:\"departman za hemiju biohemiju i zastitu zivotne sredine\" INS:\"katedra za analiticku hemiju\" INS:\"katedra za biohemiju i hemiju prirodnih proizvoda\" INS:\"katedra za fizicku hemiju\" INS:\"katedra za hemijsko obrazovanje i metodiku nastave hemije\" INS:\"katedra za hemijsku tehnologiju i zastitu zivotne sredine\" INS:\"katedra za opstu i neorgansku hemiju\" INS:\"katedra za organsku hemiju\")");
//					orgUnitQuery = qparser.parse("+(INS:\"tehnoloski fakultet novi sad\")");
//					allMonographPapersQuery.add(orgUnitQuery, Occur.MUST);
					allMonographPapersQuery.add(new TermQuery(new Term("MOCN", monographDTO.getControlNumber())), Occur.MUST);
					List<Record> listMonographPapers = recordDAO.getDTOs(allMonographPapersQuery, new AllDocCollector(false));
					List<PaperMonographDTO> listPapers = new ArrayList<PaperMonographDTO>();
					for (Record rDTO : listMonographPapers) {
						PaperMonographDTO paperMonographDTO = (PaperMonographDTO) rDTO.getDto();
						if (paperMonographDTO != null) {
							listPapers.add(paperMonographDTO);
						}
					}
					if(listPapers.size() == 0)
//					if(listPapers.size() != 0)
//					if(! monographClassification.equals(""))
						retVal.add(new MonographAndPapers(monographDTO, listPapers, monographClassification));
				}
				
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
//				return null;
			}
		}
		List<String> orderBy = new ArrayList<String>();
		orderBy.add("papersSize");
//		orderBy.add("monograph.year");
		List<String> directions = new ArrayList<String>();
		directions.add("desc");
//		directions.add("asc");
		Collections.sort(retVal, new GenericComparator<MonographAndPapers>(
				orderBy, directions));
		return retVal;
	}
	
	private void printMonographs(List<MonographAndPapers> monographs) {
//		out.println("Email,ID Monografije,Jezik,Naslov,Podnaslov,ISBN,Broj str.,Napomena,URI,Izdavac,Godina,Autor,Broj autocitata,Spisak autocitata,Recezent,Prikaz kriticki(navesti referencu)");
//		out.println("Email,ID Monografije,Jezik,Naslov,Podnaslov,ISBN,Broj str.,Napomena,URI,Izdavac,Godina,Urednik,Recezent,Prikaz kriticki(navesti referencu)");
		for (MonographAndPapers monographAndPapers : monographs) {
			out.println(monographAndPapers.toString());
		}
		out.flush();
	}

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private PrintWriter out;
	
	private static Log log = LogFactory.getLog(ExportConferencesTask.class.getName());

}
