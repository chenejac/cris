package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportJournalsTask implements Task {

	public ExportJournalsTask(Connection conn, PrintWriter out) {
		this.conn = conn;
		this.out = out;
	}

	@Override
	public boolean execute() {
		try {
			List<JournalAndPapers> journals = collectJournals2();
			printJournals2FirstPublication(journals);
//			Map<String, List<JournalAndPapers>> journals = collectJournals3();
//			printJournals3(journals);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
	}

	public List<JournalAndPapers> collectJournals(){
		List<JournalAndPapers> retVal = new ArrayList<JournalAndPapers>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		for (Record recordDTO : listJournals) {
			try {
				JournalDTO journalDTO = (JournalDTO) recordDTO.getDto();
//				if (journalDTO != null) {
//					BooleanQuery allJournalPapersQuery = new BooleanQuery();
//					BooleanQuery type = new BooleanQuery();
//					type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
//					type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
//					allJournalPapersQuery.add(type, Occur.MUST);
//					allJournalPapersQuery.add(new TermQuery(new Term("JOCN", journalDTO.getControlNumber())), Occur.MUST);
//					QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
//					Query orgUnitQuery = qparser.parse("+(INS:\"departman za matematiku i informatiku\" INS:\"katedra za opstu algebru i teorijsko racunarstvo\" INS:\"katedra za analizu verovatnocu i diferencijalne jednacine\" INS:\"katedra za numericku matematiku\" INS:\"katedra za primenjenu algebru\" INS:\"katedra za funkcionalnu analizu geometriju i topologiju\" INS:\"katedra za racunarske nauke\" INS:\"katedra za matematicku logiku i diskretnu matematiku\" INS:\"katedra za primenjenu analizu\" INS:\"katedra za informacione sisteme\")");
//					allJournalPapersQuery.add(orgUnitQuery, Occur.MUST);
//					List<Record> listJournalPapers = recordDAO.getDTOs(allJournalPapersQuery, new AllDocCollector(false));
					List<PaperJournalDTO> listPapers = new ArrayList<PaperJournalDTO>();
//					for (Record recDTO : listJournalPapers) {
//						PaperJournalDTO paperJournalDTO = (PaperJournalDTO) recDTO.getDto();
//						if (paperJournalDTO != null) {
//							listPapers.add(paperJournalDTO);
//						}
//					}	
//					if(listPapers.size() > 0){
//						Collections.sort(listPapers, new GenericComparator<PaperJournalDTO>(
//							"publicationYear", "asc"));
						MetricsDB metricsDB = new MetricsDB();
						List<ImpactFactor> impactFactors = metricsDB.getJournalImpactFactors(conn, journalDTO.getControlNumber(), "twoYearsIF");
//						Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
//								"year", "asc"));
//						if(impactFactors.size() != 0)
							retVal.add(new JournalAndPapers(journalDTO, listPapers, impactFactors));
//					}
//				}
				
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		Collections.sort(retVal, new GenericComparator<JournalAndPapers>(
				"journal.someName", "asc"));
		return retVal;
	}
	
	private void printJournals(List<JournalAndPapers> journals) {
		out.println("ID,Naziv,Skraceni naziv,ISSN,URL");
//		int i = 1;
		for (JournalAndPapers journalAndPapers : journals) {
//			out.println(i++ + "," + journalAndPapers.toString());
			out.println(journalAndPapers.toString2());
		}
		out.flush();
	}
	
	public List<JournalAndPapers> collectJournals2(){
		List<JournalAndPapers> retVal = new ArrayList<JournalAndPapers>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		for (Record recordDTO : listJournals) {
			try {
				JournalDTO journalDTO = (JournalDTO) recordDTO.getDto();
				if (journalDTO != null) {
					BooleanQuery allJournalPapersQuery = new BooleanQuery();
					BooleanQuery type = new BooleanQuery();
					type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
					allJournalPapersQuery.add(type, Occur.MUST);
					allJournalPapersQuery.add(new TermQuery(new Term("JOCN", journalDTO.getControlNumber())), Occur.MUST);
					List<Record> listJournalPapers = recordDAO.getDTOs(allJournalPapersQuery, new AllDocCollector(false));
					List<PaperJournalDTO> listPapers = new ArrayList<PaperJournalDTO>();
					for (Record recDTO : listJournalPapers) {
						PaperJournalDTO paperJournalDTO = (PaperJournalDTO) recDTO.getDto();
						if (paperJournalDTO != null) {
							listPapers.add(paperJournalDTO);
						}
					}	
					Collections.sort(listPapers, new GenericComparator<PaperJournalDTO>(
							"publicationYear", "asc"));
					MetricsDB metricsDB = new MetricsDB();
					List<ImpactFactor> impactFactors = metricsDB.getJournalImpactFactors(conn, journalDTO.getControlNumber(), "twoYearsIF");
					Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
							"year", "asc"));
					if(impactFactors.size() != 0)
						retVal.add(new JournalAndPapers(journalDTO, listPapers, impactFactors));
				}
				
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		Collections.sort(retVal, new GenericComparator<JournalAndPapers>(
				"firstImpactFactorYear", "asc"));
		return retVal;
	}
	
	@SuppressWarnings("unused")
	private void printJournals2(List<JournalAndPapers> journals) {
		out.print("Naziv,prva godina");
		List<Integer> years = new ArrayList<Integer>();
		for (int i=journals.get(0).getImpactFactors().get(0).getYear();i<2011; i++) {
			out.print(","+i);
			years.add(i);
		}
		out.println();
		for (JournalAndPapers journalAndPapers : journals) {
//			out.println(i++ + "," + journalAndPapers.toString());
			out.println(journalAndPapers.toString2(years));
		}
		out.flush();
	}
	
	@SuppressWarnings("unused")
	private void printJournals2FirstPublication(List<JournalAndPapers> journals) {
		out.print("Naziv,prva godina");
		List<Integer> years = new ArrayList<Integer>();
		for (int i=journals.get(0).getImpactFactors().get(0).getYear();i<2011; i++) {
			out.print(","+i);
			years.add(i);
		}
		out.println();
		for (JournalAndPapers journalAndPapers : journals) {
//			out.println(i++ + "," + journalAndPapers.toString());
			int startingIfYear = -1;
			if(journalAndPapers.getImpactFactors()!=null)
				if(journalAndPapers.getImpactFactors().get(0)!=null)
					startingIfYear = journalAndPapers.getImpactFactors().get(0).getYear();
				
			out.println(journalAndPapers.toString2(years, startingIfYear));
		}
		out.flush();
	}
	
	public Map<String, List<JournalAndPapers>> collectJournals3(){
		Map<String, List<JournalAndPapers>> retVal = new HashMap<String, List<JournalAndPapers>>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		List<PaperJournalDTO> listPapers = new ArrayList<PaperJournalDTO>();
		List<ImpactFactor> impactFactors = new ArrayList<ImpactFactor>();
		for (int i=0;i<listJournals.size();) {
			try {
				JournalDTO jDTO = (JournalDTO) listJournals.get(i).getDto();
				BooleanQuery bq = new BooleanQuery();
				bq.add(new TermQuery(new Term("ISSN", jDTO.getIssn())), Occur.MUST);
				bq.add(new TermQuery(new Term("TYPE", Types.JOURNAL)), Occur.MUST);
				List<Record> listJournalsSameISSN = recordDAO.getDTOs(bq, new AllDocCollector(false));
				for (Record record : listJournalsSameISSN) {
					JournalDTO journalDTO = (JournalDTO) record.getDto();
					if(retVal.containsKey(jDTO.getControlNumber()))
						retVal.get(jDTO.getControlNumber()).add(new JournalAndPapers(journalDTO, listPapers, impactFactors));
					else{
						retVal.put(jDTO.getControlNumber(), new ArrayList<JournalAndPapers>());
						retVal.get(jDTO.getControlNumber()).add(new JournalAndPapers(journalDTO, listPapers, impactFactors));
					}
					listJournals.remove(journalDTO.getRecord());
				}
				if(listJournalsSameISSN.size() == 0){
					retVal.put(jDTO.getControlNumber(), new ArrayList<JournalAndPapers>());
					retVal.get(jDTO.getControlNumber()).add(new JournalAndPapers(jDTO, listPapers, impactFactors));
					listJournals.remove(jDTO.getRecord());
				}
			} catch (Exception e) {
				log.error(e);
				return null;
			}
		}
		return retVal;
	}
	
	private void printJournals3(Map<String, List<JournalAndPapers>> journals) {
		out.println("UsvojeniID,ID,Naziv,Skraceni naziv,ISSN,URL");
//		int i = 1;
		for (List<JournalAndPapers> journalsList : journals.values()){
			out.println(journalsList.get(0).getJournal().getControlNumber()+",");
			
			for (JournalAndPapers journalAndPapers : journalsList) {
			//			out.println(i++ + "," + journalAndPapers.toString());
				out.println(","+journalAndPapers.toString2());
			}
		}
		out.flush();
	}

	private Connection conn;
	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private PrintWriter out;
	
	private static Log log = LogFactory.getLog(ExportJournalsTask.class.getName());

}
