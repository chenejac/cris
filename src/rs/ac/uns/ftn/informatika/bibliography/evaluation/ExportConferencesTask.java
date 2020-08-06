package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ConferenceDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ProceedingsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.dto.eval.ConferenceAndProceedings;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.CrisAnalyzer;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportConferencesTask implements Task {

	public ExportConferencesTask(PrintWriter out) {
		this.out = out;
	}

	@Override
	public boolean execute() {
		try {
			List<ConferenceAndProceedings> conferences = collectConferences();
			printConferences(conferences);
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		} catch (Throwable e) {
			log.error(e);
			return false;
		}
	}

	public List<ConferenceAndProceedings> collectConferences(){
		List<ConferenceAndProceedings> retVal = new ArrayList<ConferenceAndProceedings>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.CONFERENCE));
		List<Record> listConferences = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		for (Record recordDTO : listConferences) {
			try {
				System.out.println("CO: " + recordDTO.getControlNumber());
				String conferenceClassification = "";
				recordDTO.loadFromDatabase();
				ConferenceDTO conferenceDTO = (ConferenceDTO) recordDTO.getDto();
				Calendar publicationDate = new GregorianCalendar();
				Date publicationTime = new Date();
				publicationDate.setTime(publicationTime); 
				publicationDate.set(GregorianCalendar.YEAR, conferenceDTO.getYear());
				for (Classification classification : recordDTO.getRecordClasses()) {
					if(
							((!"0".equals(classification.getCommissionId().toString())))
					&& (classification.getCfClassSchemeId().equals("type")) 
					&&  (! (publicationDate.before(classification.getCfStartDate()))) 
					&& (!(publicationDate.after(classification.getCfEndDate())))){
						conferenceClassification = classification.getCfClassId();
					}
				}
				if (conferenceDTO != null) {
					BooleanQuery allConferenceProceedingsQuery = new BooleanQuery();
					allConferenceProceedingsQuery.add(new TermQuery(new Term("TYPE", Types.PROCEEDINGS)), Occur.MUST);
//					allConferenceProceedingsQuery.add(new TermQuery(new Term("YE", "2015")), Occur.MUST);
					allConferenceProceedingsQuery.add(new TermQuery(new Term("COCN", conferenceDTO.getControlNumber())), Occur.MUST);
					List<Record> listConferenceProceedings = recordDAO.getDTOs(allConferenceProceedingsQuery, new AllDocCollector(false));
					List<ProceedingsAndPapers> listProceedingsAndPapers = new ArrayList<ProceedingsAndPapers>();
					for (Record recDTO : listConferenceProceedings) {
//						System.out.println("PRO: " + recDTO.getControlNumber());
						ProceedingsDTO proceedingsDTO = (ProceedingsDTO) recDTO.getDto();
						if (proceedingsDTO != null) {
							BooleanQuery allProceedingsPapersQuery = new BooleanQuery();
							BooleanQuery type = new BooleanQuery();
							type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
							type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
							BooleanQuery institution = new BooleanQuery();
							QueryParser qparser = new QueryParser("INS", new CrisAnalyzer());
//							Query orgUnitQuery = qparser.parse("+(INS:\"tehnoloski fakultet novi sad\")");
							Query orgUnitQuery = qparser.parse("+(INS:\"prirodno matematicki fakultet\")");
							institution.add(orgUnitQuery, Occur.SHOULD);
							
							orgUnitQuery = qparser.parse("+(INS:\"katedra za opste inzenjerske discipline\" INS:\"katedra za primenjene i inzenjerske hemije\" INS:\"katedra za hemijsko inzenjerstvo\" INS:\"katedra za tehnologije ugljenohidratne hrane\" INS:\"katedra za tehnologije konzervisane hrane\" INS:\"katedra za biotehnologiju i farmaceutsko inzenjerstvo\" INS:\"katedra za naftno petrohemijsko inzenjerstvo\" INS:\"katedra za inzenjerstvo materijala\")");
							institution.add(orgUnitQuery, Occur.SHOULD);
//							orgUnitQuery = qparser.parse("+(INS:\"departman za matematiku i informatiku\" INS:\"katedra za opstu algebru i teorijsko racunarstvo\" INS:\"katedra za analizu verovatnocu i diferencijalne jednacine\" INS:\"katedra za numericku matematiku\" INS:\"katedra za primenjenu algebru\" INS:\"katedra za funkcionalnu analizu geometriju i topologiju\" INS:\"katedra za racunarske nauke\" INS:\"katedra za matematicku logiku i diskretnu matematiku\" INS:\"katedra za primenjenu analizu\" INS:\"katedra za informacione tehnologije i sisteme\" INS:\"katedra za teorijske osnove informatike\")");
//							institution.add(orgUnitQuery, Occur.SHOULD);
//							orgUnitQuery = qparser.parse("+(INS:\"departman za geografiju turizam i hotelijerstvo\" INS:\"katedra za fizicku geografiju\" INS:\"katedra za drustvenu geografiju\" INS:\"katedra za regionalnu geografiju\" INS:\"katedra za turizam\" INS:\"katedra za hotelijerstvo\" INS:\"katedra za lovni turizam\")");
//							institution.add(orgUnitQuery, Occur.SHOULD);
//							orgUnitQuery = qparser.parse("+(INS:\"departman za fiziku\" INS:\"katedra za eksperimentalnu fiziku kondenzovane materije\" INS:\"katedra za fizicku elektroniku\" INS:\"katedra za nuklearnu fiziku\" INS:\"katedra za teorijsku fiziku\" INS:\"katedra za opstu fiziku i metodiku nastave fizike\")");
//							institution.add(orgUnitQuery, Occur.SHOULD);
//							orgUnitQuery = qparser.parse("+(INS:\"departman za hemiju biohemiju i zastitu zivotne sredine\" INS:\"katedra za analiticku hemiju\" INS:\"katedra za biohemiju i hemiju prirodnih proizvoda\" INS:\"katedra za fizicku hemiju\" INS:\"katedra za hemijsko obrazovanje i metodiku nastave hemije\" INS:\"katedra za hemijsku tehnologiju i zastitu zivotne sredine\" INS:\"katedra za opstu i neorgansku hemiju\" INS:\"katedra za organsku hemiju\")");
//							institution.add(orgUnitQuery, Occur.SHOULD);
//							orgUnitQuery = qparser.parse("+(INS:\"departman za biologiju i ekologiju\" INS:\"katedra za botaniku\" INS:\"katedra za fiziologiju\" INS:\"katedra za zoologiju\" INS:\"katedra za mikrobiologiju\" INS:\"katedra za humanu biologiju i metodiku nastave biologije\" INS:\"katedra za ekologiju i zastitu zivotne sredine\")");
//							institution.add(orgUnitQuery, Occur.SHOULD);
//							
							allProceedingsPapersQuery.add(institution, Occur.MUST);
							allProceedingsPapersQuery.add(type, Occur.MUST);
							allProceedingsPapersQuery.add(new TermQuery(new Term("PRCN", proceedingsDTO.getControlNumber())), Occur.MUST);
							List<Record> listProceedingsPapers = recordDAO.getDTOs(allProceedingsPapersQuery, new AllDocCollector(false));
							List<PaperProceedingsDTO> listPapers = new ArrayList<PaperProceedingsDTO>();
							for (Record rDTO : listProceedingsPapers) {
//								System.out.println("PAP: " + rDTO.getControlNumber());
								PaperProceedingsDTO paperProceedingsDTO = (PaperProceedingsDTO) rDTO.getDto();
								if (paperProceedingsDTO != null) {
									listPapers.add(paperProceedingsDTO);
								}
//								paperProceedingsDTO.getOtherAuthors().clear();
								paperProceedingsDTO.setNotLoaded(true);
							}
							if(listPapers.size() > 0)
								listProceedingsAndPapers.add(new ProceedingsAndPapers(proceedingsDTO, listPapers));
						}
						proceedingsDTO.setNotLoaded(true);
					}	
					if(listProceedingsAndPapers.size() > 0)
						retVal.add(new ConferenceAndProceedings(conferenceDTO, listProceedingsAndPapers, conferenceClassification));
				}
				conferenceDTO.setNotLoaded(true);
				
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				
			}
		}
		List<String> orderBy = new ArrayList<String>();
		orderBy.add("conference.year");
		orderBy.add("conference.someName");
		List<String> directions = new ArrayList<String>();
		directions.add("asc");
		directions.add("asc");
		Collections.sort(retVal, new GenericComparator<ConferenceAndProceedings>(
				orderBy, directions));
		return retVal;
	}
	
	private void printConferences(List<ConferenceAndProceedings> conferences) {
		out.println("R.B.,ID Skupa,Naziv Skupa,Godina Skupa,Mesto Skupa, Vrednovanje, Dodatno vrednovanje, Radovi");
		int i = 1;
		for (ConferenceAndProceedings conferenceAndProceedings : conferences) {
			out.println(i++ + "," + conferenceAndProceedings.toString());
		}
		out.flush();
	}

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	private PrintWriter out;
	
	private String commissionId = "1";
	
	private static Log log = LogFactory.getLog(ExportConferencesTask.class.getName());

}
