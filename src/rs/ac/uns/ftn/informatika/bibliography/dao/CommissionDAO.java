package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.CommissionDB;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.*;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.JournalEvaluationResult;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResultEvaluator;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.AbstractCommissionEvaluation;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.CommissionFactory;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje.SamovrednovanjeUtils;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class CommissionDAO {

	int firstEvaluationYear = -1;
	int lastEvaluationYear = -1;
	
	public CommissionDAO() {
		dataSource = DataSourceFactory.getDataSource();
		if (commissions == null)
			fillCommissions();
		
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		lastEvaluationYear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
		
		
	}

	/**
	 * @return the commission
	 */
	public CommissionDTO getCommission(String commissionId) {
		return commissions.get(commissionId);
	}
	
	/**
	 * @return the commissions
	 */
	public List<CommissionDTO> getCommissions() {
		return new ArrayList<CommissionDTO>(commissions.values());
	}
	
	public List<CommissionDTO> getConnectedCommissions(Integer commissionId) {
		List<CommissionDTO> retVal = new ArrayList<CommissionDTO>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			retVal = commissionDB.getConnectedCommissions(conn, commissionId);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	/**
	 * @return the commissions
	 */
	public List<CommissionDTO> getCommissionsOrderdList() {
		ArrayList<CommissionDTO> orderCommissions = new ArrayList<CommissionDTO>();
		for (CommissionDTO commissionDTO : commissions.values()) {
			if(orderCommissions.isEmpty())
				orderCommissions.add(commissionDTO);
			else {
				boolean inserted = false;
				for (int i = 0; i < orderCommissions.size(); i++) {
					if (orderCommissions.get(i).getCommissionId().intValue()>commissionDTO.getCommissionId().intValue()) {
						orderCommissions.add(i, commissionDTO);
						inserted = true;
						break;
					}
				}
				if (inserted == false) {
					orderCommissions.add(commissionDTO);
				}
				
			}
		}
		
		return orderCommissions;
	}
		
	/**
	 * @return the commissions
	 */
	public Map<String, CommissionDTO> getCommissionsAsMap() {
		return new HashMap<String, CommissionDTO>(commissions);
	}
	
	
	private void fillCommissions() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<CommissionDTO> temp = commissionDB.getCommissions(conn);
			commissions = new HashMap<String, CommissionDTO>();
			for (CommissionDTO commissionDTO : temp) {
				commissions.put(commissionDTO.getCommissionId()+"", commissionDTO);
			}
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

	/**
	 * Adds a new commission to the database.
	 * 
	 * @param commissionDTO
	 *            Commission to add
	 * @return true if successful else false
	 */
	public boolean add(CommissionDTO commissionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (commissionDB.addCommission(conn, commissionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				commissions.put(commissionDTO.getCommissionId()+"", commissionDTO);
				conn.commit();
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

	/**
	 * Updates the commission to the database.
	 *
	 * @param commissionDTO
	 *            Commission to add
	 * @return true if successful else false
	 */
	public boolean update(CommissionDTO commissionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (commissionDB.updateCommission(conn, commissionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				commissions.put(commissionDTO.getCommissionId()+"", commissionDTO);
				conn.commit();
				
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

	/**
	 * Deletes a commission to the database.
	 * 
	 * @param commissionDTO
	 *            Commission to add
	 * @return true if successful else false
	 */
	public boolean delete(CommissionDTO commissionDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (commissionDB.deleteCommission(conn, commissionDTO) == false) {
				retVal = false;
				conn.rollback();
			} else {
				commissions.remove(commissionDTO.getCommissionId());
				conn.commit();
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

	//--------------------------------- ResultClassification By Commission START: ADD, GET, REMOVE -------------------// 
	public boolean addResultClassificationByCommission(String recordid, String cfclassschemeid, String cfclassid, Calendar cfstartdate, Calendar cfenddate, Integer commissionid){
		
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (evaluationDB.addResultClassificationByCommission(conn, recordid, cfclassschemeid, cfclassid, cfstartdate, cfenddate, commissionid) == false) {
				retVal = false;
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	public String getResultClassificationByCommission(String controlNumber, String classSchemeId, Calendar publicationDate, int commissionId) {
		String retVal = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			retVal = evaluationDB.getResultClassificationByCommission(conn, controlNumber, classSchemeId, publicationDate, commissionId);
		} catch (SQLException ex) {
			log.fatal(ex);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public boolean removeResultClassificationByCommission(String controlNumber, String classSchemeId, Calendar publicationDate, int commissionId) {
		
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (evaluationDB.removeResultClassificationByCommission(conn, controlNumber, classSchemeId, publicationDate, commissionId) == false) {
				retVal = false;
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}

	//============================ ResultClassification By Commission END: ADD, GET, REMOVE ================================// 
	
	//--------------------------------- JouranlEvaluation By Commission START: ADD, GET, REMOVE -------------------//
	public boolean createEvaluationsByCommisionForRecords(ArrayList<Record> recordsList, ArrayList<CommissionDTO> selectedCommissionList, RuleBookDTO ruleBook) {
		boolean retVal = true;	
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if(recordsList != null){
				int velicinaListe = recordsList.size();
				log.info("UKUPAN BROJ ZAPISA ZA EVALUACIJU JE:" + velicinaListe);
				String opisEvaluacije = "";
				for (int i = 0; i<velicinaListe; i++) {
					Record rec = recordsList.get(i);			
					if (rec != null && rec.getDto()!=null){

						//za casopis
						if(rec.getDto() instanceof JournalDTO){
							retVal = createEvaluationsByCommisionForRecordJournal(conn, (JournalDTO)rec.getDto(), selectedCommissionList);
							opisEvaluacije = ((JournalDTO)rec.getDto()).getControlNumber() + " za casopis "+((JournalDTO)rec.getDto()).getSomeName();
						}
						//za rad u casopisu
						else if(rec.getDto() instanceof PaperJournalDTO){
							retVal = createEvaluationsByCommisionForRecordPaperJournalDTO(conn, (PaperJournalDTO)rec.getDto(), selectedCommissionList, ruleBook);
							opisEvaluacije = ((PaperJournalDTO)rec.getDto()).getControlNumber() + " za rad u casopisu "+((PaperJournalDTO)rec.getDto()).getSomeTitle();
						}
						//fali za sve ostale tipove zapisa evaluacije
						
						if (retVal == false){
							conn.rollback();
							log.fatal("Neuspelo unos evaluacija: " + opisEvaluacije);
							break;
						} else if (i%100==0) {
							conn.commit();
							log.info("EVALUACIJA STIGLA DO:"+i + " ZAPISA");
						}
						else if (i==(velicinaListe-1)) {
							conn.commit();
							log.info("EVALUACIJA ZAVRSILA ZADNJI:"+i + " ZAPISA");
						}
					}
					rec.clear();
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	public boolean createEvaluationsByCommisionFromAuthorsAfiliationForRecords(List<Record> recordsList, RuleBookDTO ruleBook) {
		boolean retVal = true;	
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if(recordsList != null){
				int velicinaListe = recordsList.size();
				List<CommissionDTO> authorCommissionList = new ArrayList<CommissionDTO>();
				log.info("UKUPAN BROJ ZAPISA ZA EVALUACIJU JE:" + velicinaListe);
				String opisEvaluacije = "";
				for (int i = 0; i<velicinaListe; i++) {
					authorCommissionList.clear();
					Record rec  = recordsList.get(i);

					if (rec != null && rec.getDto()!=null){
						if (rec.getDto() instanceof PublicationDTO)
							authorCommissionList.addAll(getCommisionsByAuthorsAffiliationFromResearchResult(rec));
						
						if (!authorCommissionList.isEmpty()) {
							//za casopis
							if(rec.getDto() instanceof JournalDTO){
								retVal = createEvaluationsByCommisionForRecordJournal(conn, (JournalDTO)rec.getDto(), authorCommissionList);
								opisEvaluacije = ((JournalDTO)rec.getDto()).getControlNumber() + " za casopis "+((JournalDTO)rec.getDto()).getSomeName();
							}
							//za rad u casopisu
							else if(rec.getDto() instanceof PaperJournalDTO){
								retVal = createEvaluationsByCommisionForRecordPaperJournalDTO(conn, (PaperJournalDTO)rec.getDto(), authorCommissionList, ruleBook);;
								opisEvaluacije = ((PaperJournalDTO)rec.getDto()).getControlNumber() + " za rad u casopisu "+((PaperJournalDTO)rec.getDto()).getSomeTitle();
							}
							
							else if(rec.getDto() instanceof PaperProceedingsDTO){
								retVal = createEvaluationsByCommisionForRecordPaperProceedingsDTO(conn, (PaperProceedingsDTO)rec.getDto(), authorCommissionList, ruleBook);;
								opisEvaluacije = ((PaperProceedingsDTO)rec.getDto()).getControlNumber() + " za rad u zborniku konferencije "+((PaperProceedingsDTO)rec.getDto()).getSomeTitle();
							}
							//fali za sve ostale tipove zapisa evaluacije
						}
					}
					
					if (retVal == false){
						conn.rollback();
						log.fatal("Greska Neuspelo unos evaluacija: " + opisEvaluacije);
						break;
					} else if (i%100==0) {
						conn.commit();
						log.info("EVALUACIJA STIGLA DO:"+i + " ZAPISA");
					}
					else if (i==(velicinaListe-1)) {
						conn.commit();
						log.info("EVALUACIJA ZAVRSILA ZADNJI:"+i + " ZAPISA");
					}
					rec.clear();
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	
	
	
	private boolean createEvaluationsByCommisionForRecordJournal(Connection conn, JournalDTO journal, List<CommissionDTO> selectedCommissionList){
		List<ImpactFactor> impactFactors = metricsDB.getJournalImpactFactors(conn, journal.getControlNumber(), Arrays.asList(new String[]{"twoYearsIF", "fiveYearsIF"}));
		if(impactFactors!=null)
			Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
					"year", "asc"));
		//godina od koje se vrednuje je firstEvaluationYear
		JournalEval journalEval= new JournalEval(journal.getControlNumber(), journal.getSomeName(), journal.getIssn(), impactFactors, firstEvaluationYear);
		
		CommissionFactory commissionFactory = CommissionFactory.getInstance();
		for (CommissionDTO commissionDTO : selectedCommissionList) {
			AbstractCommissionEvaluation abstractCommissionEvaluation = commissionFactory.getCommissionEvaluation(commissionDTO.getCommissionId());
			if (abstractCommissionEvaluation == null) {
				log.fatal("Komisija sa trazenim ID ne postoji id:" + commissionDTO.getCommissionId());
//				System.out.println("Komisija sa trazenim ID ne postoji");
				return false;
			}
			HashMap<Integer, JournalEvaluationResult> evaluated = abstractCommissionEvaluation.getJournalEvaluations(journalEval);
			if(evaluated.isEmpty())
				continue;
			Calendar startDate = null;
			Calendar endDate = null;
			
			JournalEvaluationResult startKategorija = null;
			JournalEvaluationResult endKategorija = null;
			for (int godina = journalEval.getStartingYear(); godina<=lastEvaluationYear; godina++) {
				if(godina == journalEval.getStartingYear()){
					startKategorija = evaluated.get(godina);
					startDate = new GregorianCalendar();
					startDate.set(Calendar.DAY_OF_YEAR, 1);
					startDate.set(Calendar.YEAR, godina);
					continue;
				}
				
				endKategorija = evaluated.get(godina);
				if(endKategorija != null){
					if(!startKategorija.getCategory().equals(endKategorija.getCategory())){
						endDate = new GregorianCalendar();
						endDate.set(Calendar.DAY_OF_MONTH, 31);
						endDate.set(Calendar.MONTH, Calendar.DECEMBER);
						endDate.set(Calendar.YEAR, godina-1);			
						String kategorija = determinateJournalDTOEvaluatuion(startKategorija.getCategory());
						
	//					System.out.println("DIFFERENT CAT " + "start godina "+ startDate.getTime() + " karaj godina "+ endDate.getTime() + " kategorija " + kategorija + " komisija "+ commision.getComissionID());
						if(!evaluationDB.addResultClassificationByCommission(conn, journal.getControlNumber(), "type", kategorija, startDate, endDate, commissionDTO.getCommissionId())){
							log.fatal("Greska pri ubacivanju vrednovanja: " + journal.getControlNumber() + " za casopis "+journal.getSomeName());
//							System.out.println("Greska pri ubacivanju vrednovanja: " + journal.getControlNumber() + " za casopis "+journal.getSomeName());
							return false;
						}
						startDate = new GregorianCalendar();
						startDate.set(Calendar.DAY_OF_YEAR, 1);
						startDate.set(Calendar.YEAR, godina);
						startKategorija = endKategorija;
					}
				}
				
				if (godina == lastEvaluationYear) {
					endDate = new GregorianCalendar();
					endDate.set(Calendar.DAY_OF_MONTH, 31);
					endDate.set(Calendar.MONTH, Calendar.DECEMBER);
					endDate.set(Calendar.YEAR, godina);
					String kategorija = determinateJournalDTOEvaluatuion(startKategorija.getCategory());
					if(kategorija==null)
                        continue; 
					
//					System.out.println("LAST YEAR " + "start godina "+ startDate.getTime() + " karaj godina "+ endDate.getTime() + " kategorija " + kategorija + " komisija "+ commision.getComissionID());
					if(!evaluationDB.addResultClassificationByCommission(conn, journal.getControlNumber(), "type", kategorija, startDate, endDate, commissionDTO.getCommissionId())){
						log.fatal("Greska pri ubacivanju vrednovanja: " + journal.getControlNumber() + " za casopis "+journal.getSomeName());
//						System.out.println("Greska pri ubacivanju vrednovanja1: " + journal.getControlNumber() + " za casopis "+journal.getSomeName());
						return false;
					}	
					
				}
			}
		}
			return true;
	}
	
	public String determinateJournalDTOEvaluatuion (String m){
		
		String retVal = null;
		if(m == null)
			retVal = null;
		else if(m.equalsIgnoreCase("M21a"))
			retVal = "topLeadingInternationalJournal";
		else if(m.equalsIgnoreCase("M21"))
			retVal = "leadingInternationalJournal";
		else if (m.equalsIgnoreCase("M22"))
			retVal = "outstandingInternationalJournal";
		else if (m.equalsIgnoreCase("M23"))
			retVal = "internationalJournal";
		else if (m.equalsIgnoreCase("M24"))
			retVal = "speciallyVerifiedInternationalJournal";
		else if (m.equalsIgnoreCase("M51"))
			retVal = "leadingNationalJournal";
		else if (m.equalsIgnoreCase("M52"))
			retVal = "nationalJournal";
		else if (m.equalsIgnoreCase("M53"))
			retVal = "scienceJournal";
		else if (m.equalsIgnoreCase("M54"))
			retVal = "newScienceJournal";
		else if (m.equalsIgnoreCase("M100")) //nije evaluirano
            retVal = null; 
		else {
			log.fatal("kategorija preuzeta za evaluaciju ne postoji : " + m);
//			System.out.println("Sinak tvoja kategorija ne postoji " + m);
		}
		
		return retVal;
	}

	private boolean createEvaluationsByCommisionForRecordPaperJournalDTO(Connection conn, PaperJournalDTO paperJournalDTO, List<CommissionDTO> selectedCommissionList, RuleBookDTO ruleBook){
		
//		System.out.println("DO 1");
		if(paperJournalDTO.getPublicationYear()==null)
			return true;
		else if(paperJournalDTO.getPublicationYear().trim()=="")
			return true;
		else if(paperJournalDTO.getPublicationYear().trim().contains("/")){
			
			try {
				Integer.parseInt(paperJournalDTO.getPublicationYear().substring(0, paperJournalDTO.getPublicationYear().indexOf("/")));
			}
			catch (Exception ex ){
				return true;
			}
		}
		else {
			
			try {
				Integer.parseInt(paperJournalDTO.getPublicationYear());
			}
			catch (Exception ex ){
				return true;
			}
		}
		
		try {
		
			Map<String, ResultMeasureDTO> resultMeasures = ResultEvaluator.calculateResultMeasures(conn, paperJournalDTO.getRecord(), ruleBook, selectedCommissionList, new ArrayList<AuthorDTO>());
//			System.out.println("DO 2");
			for (String commissionID : resultMeasures.keySet()) {
				ResultMeasureDTO resultMeasure = resultMeasures.get(commissionID);
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1908);
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 3008);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
//				System.out.println("DO 3");
				evaluationDB.addResultClassificationByCommission(conn, paperJournalDTO.getControlNumber(), "resultType", resultMeasure.getResultType().getClassId(), startDate, endDate, Integer.parseInt(commissionID));
//				System.out.println("DO 4");
			}
		}catch (Throwable e) {
			e.printStackTrace();
//			System.out.println(paperJournalDTO);
		}
		return true;
	}
	
	private boolean createEvaluationsByCommisionForRecordPaperProceedingsDTO(Connection conn, PaperProceedingsDTO paperProceedingsDTO, List<CommissionDTO> selectedCommissionList, RuleBookDTO ruleBook){
		
//		System.out.println("DO 1");
		
		if(paperProceedingsDTO.getPublicationYear()==null)
			return true;
		else if(paperProceedingsDTO.getPublicationYear().trim()=="")
			return true;
		else if(paperProceedingsDTO.getPublicationYear().trim().contains("/")){
			
			try {
				Integer.parseInt(paperProceedingsDTO.getPublicationYear().substring(0, paperProceedingsDTO.getPublicationYear().indexOf("/")));
			}
			catch (Exception ex ){
				return true;
			}
		}
		else {
			
			try {
				Integer.parseInt(paperProceedingsDTO.getPublicationYear());
			}
			catch (Exception ex ){
				return true;
			}
		}
		
		try {
			Map<String, ResultMeasureDTO> resultMeasures = ResultEvaluator.calculateResultMeasures(conn, paperProceedingsDTO.getRecord(), ruleBook, selectedCommissionList, new ArrayList<AuthorDTO>());
//			System.out.println("DO 2");
			for (String commissionID : resultMeasures.keySet()) {
				ResultMeasureDTO resultMeasure = resultMeasures.get(commissionID);
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1908);
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 3008);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
//				System.out.println("DO 3");
				evaluationDB.addResultClassificationByCommission(conn, paperProceedingsDTO.getControlNumber(), "resultType", resultMeasure.getResultType().getClassId(), startDate, endDate, Integer.parseInt(commissionID));
//				System.out.println("DO 4");
			}
		}catch (Throwable e) {
			e.printStackTrace();
//			System.out.println(paperJournalDTO);
		}
		return true;
	}

	private boolean createEvaluationsByCommisionForRecordPaperJournalDTOX(Connection conn, PaperJournalDTO paperJournalDTO, List<CommissionDTO> selectedCommissionList, RuleBookDTO ruleBook){
		
		
//		System.out.println("DO 1");
		
		Map<String, ResultMeasureDTO> resultMeasures = new HashMap<String, ResultMeasureDTO>();
		String commissionId = null;
		String authorID = null;
		
		Map<String, Integer> journalCategories = new HashMap<String, Integer>();
		journalCategories.put("scienceJournal", new Integer(1));
		journalCategories.put("nationalJournal", new Integer(2));
		journalCategories.put("leadingNationalJournal", new Integer(3));
		journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
		journalCategories.put("internationalJournal", new Integer(5));
		journalCategories.put("outstandingInternationalJournal", new Integer(6));
		journalCategories.put("leadingInternationalJournal", new Integer(7));
		journalCategories.put("topLeadingInternationalJournal", new Integer(8));
			
		try {
			Integer publicationYear = null;
			if(paperJournalDTO.getPublicationYear()!=null){
				if(paperJournalDTO.getPublicationYear().contains("/")){
					publicationYear = Integer.parseInt(paperJournalDTO.getPublicationYear().substring(0, paperJournalDTO.getPublicationYear().indexOf("/")));
				} else 
					publicationYear = Integer.parseInt(paperJournalDTO.getPublicationYear());
			} else 
				return false;
	
			Date publicationTime = new Date();
			
			Calendar publicationDate = new GregorianCalendar();
			publicationDate.setTime(publicationTime);
			publicationDate.set(GregorianCalendar.YEAR, publicationYear);
			
			Calendar publicationDateMinus1 = new GregorianCalendar();
			publicationDateMinus1.setTime(publicationTime); 
			publicationDateMinus1.set(GregorianCalendar.YEAR, publicationYear-1);
			
			Calendar publicationDateMinus2 = new GregorianCalendar();
			publicationDateMinus2.setTime(publicationTime); 
			publicationDateMinus2.set(GregorianCalendar.YEAR, publicationYear-2);
			
			Calendar publicationDatePlus1 = new GregorianCalendar();
			publicationDatePlus1.setTime(publicationTime); 
			publicationDatePlus1.set(GregorianCalendar.YEAR, publicationYear+1);
			
			List<AuthorDTO> authorsAndEditors = paperJournalDTO.getAllAuthors();
			authorsAndEditors.addAll(paperJournalDTO.getEditors());
			
			Record record = paperJournalDTO.getRecord();
			if(record.getDto().isNotLoaded())
				record.loadFromDatabase();
//			System.out.println("DO 2");
			
			for (AuthorDTO author : authorsAndEditors) { //nekako proveriti i vrstu autorstva, a ne samo da li je bilo po toj komisiji.
				for (CommissionDTO commission : selectedCommissionList){
					authorID = author.getControlNumber();
					commissionId = commission.getCommissionId()+"";
					if(resultMeasures.get(commission.getCommissionId().toString())!=null)
						continue;
//					System.out.println("DO 4");
					String schemeSuperPublicationType = null;
					String classSuperPublicationType = null;
					String schemeResearcherRoleType = null;
					String classResearcherRoleType = null;
					String schemePublicationType = "resultType";
					String classPublicationType = getCommissionType(record.getRecordClasses(), schemePublicationType, publicationDate, commission); //trazi se direkno oznaka M-ova zadata za zapis po komisiji (M11,..M21..)
					if(classPublicationType==null){
//						System.out.println("DO 5");
						schemePublicationType = "type";
						classPublicationType = getCommissionType(record.getRecordClasses(), schemePublicationType, publicationDate, commission);	//trazi se oznaka za zapis po pravilniku (internationalJournal, ..., internationalCornference,..., internationalMonograph)
						
						if(classPublicationType==null) 
							classPublicationType = getRecordBasicType(record.getRecordClasses(), schemePublicationType, publicationDate); //trazi se klasifikacija samog zapisa da li je on (journal,paperJournal, product, patent, conference, proceedings, fullPP,..., paperMonograph...)
	
						schemeResearcherRoleType = "authorshipType";
						classResearcherRoleType = getRelationClass(record.getRelationsOtherRecordsThisRecord(), schemeResearcherRoleType, author.getControlNumber(), publicationDate); //trazi se klasifikacija same veze sa autorom (is author of, is edithor of...)
						
						String superPublicationId = getRelatedRecord(record.getRelationsThisRecordOtherRecords(), "publicationsRelation", publicationDate); //trazi se id publikacije u kojoj je objavljen zapis (superpublikacija)
						if(superPublicationId!=null){ //ako postoji superpublikacija
//							System.out.println("DO 6");
							Record tmp = recordDB.getRecord(conn, superPublicationId);
							if(tmp!=null){
								classSuperPublicationType = getCommissionType(tmp.getRecordClasses(), "type", publicationDate, commission); //trazi se oznaka za superpublikacija po pravilniku (internationalJournal, ..., internationalCornference,..., internationalMonograph)
								if(classSuperPublicationType!=null){
									schemeSuperPublicationType = "type";
									if(journalCategories.containsKey(classSuperPublicationType)){
										String tempClassSuperPublicationType = getCommissionType(tmp.getRecordClasses(), "type", publicationDateMinus1, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
										tempClassSuperPublicationType = getCommissionType(tmp.getRecordClasses(), "type", publicationDateMinus2, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
										tempClassSuperPublicationType = getCommissionType(tmp.getRecordClasses(), "type", publicationDatePlus1, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
									}
								}
							}		
						}
					}
					
//					System.out.println("DO 7");
	
					ResultMeasureDTO resultMeasure = evaluationDB.getResultMeasure(
							conn, ruleBook, commission, null, null, 
							schemeResearcherRoleType, classResearcherRoleType, 
							null, null, 
							schemePublicationType, classPublicationType, 
							schemeSuperPublicationType, classSuperPublicationType, 
							null, null, 
							null, null);
	
//					if(resultMeasure==null){
//						System.out.println("******************************************** sve sto ulazi u getResultMeasure");
//						System.out.println("schemePrizeType je " + null + "; classPrizeType je " + null);
//						System.out.println("schemeResearcherRoleType je " + schemeResearcherRoleType + "; classResearcherRoleType je " + classResearcherRoleType);
//						System.out.println("schemeEventType je " + null + "; classEventType je " + null);
//						System.out.println("schemePublicationType je " + schemePublicationType + "; classPublicationType je " + classPublicationType);
//						System.out.println("schemeSuperPublicationType je " + schemeSuperPublicationType + "; classSuperPublicationType je " + classSuperPublicationType);
//						System.out.println("schemeProductType je " + null + "; classProductType je " + null);
//						System.out.println("schemePatentType je " + null + "; classPatentType je " + null);
//					}
					
					if(resultMeasure!=null){
						resultMeasures.put(commission.getCommissionId().toString(), resultMeasure);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Greska pri izracunavanju vrednovanja: " + paperJournalDTO.getControlNumber() + " za rad u casopisu "+paperJournalDTO.getSomeTitle() + " i komisiju '" + commissionId+"'" + " i autora '" + authorID+"'");
//			return false;
		}
		
		String commissionIdString = "";
		try {
			for (String commissionID : resultMeasures.keySet()) {
				ResultMeasureDTO resultMeasure = resultMeasures.get(commissionID);
				commissionIdString = commissionID;
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1908);
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 3008);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				evaluationDB.addResultClassificationByCommission(conn, paperJournalDTO.getControlNumber(), "resultType", resultMeasure.getResultType().getClassId(), startDate, endDate, Integer.parseInt(commissionID));
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.fatal("Greska pri ubacivanju vrednovanja: " + paperJournalDTO.getControlNumber() + " za rad u casopisu "+paperJournalDTO.getSomeTitle() + " i komisiju '" + commissionIdString+"'");
			return false;
		}
		
		return true;
	}
	
		
	public boolean removeEvaluationsByCommisionForRecords(String[] allRecordsTypes, List<Record> recordsList, 
			List<CommissionDTO> selectedCommissionList) {
		boolean retVal = true;	
		//prepare database
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (CommissionDTO commissionDTO : selectedCommissionList) {
				if(allRecordsTypes != null){
					for (int i = 0; i < allRecordsTypes.length; i++) {
						retVal = evaluationDB.removeAllResultsClassificationByCommissionAndRecordType(conn, commissionDTO.getCommissionId().intValue(), allRecordsTypes[i]);
						if(retVal == false)
							break;
					}
				}
				if(retVal == false)
					break;
				if(recordsList != null){
					int velicinaListe  = recordsList.size();
					for (int i = 0; i < velicinaListe; i++) {
						Record rec  = recordsList.get(i);
						if (evaluationDB.removeSingleResultClassificationByCommission(conn, rec.getControlNumber(), commissionDTO.getCommissionId().intValue()) == false){
							retVal = false;
							conn.rollback();
						} else if (i%100==0) {
							conn.commit();
							log.info("OBRISAO ZAPIS PO REDU:"+i + " za komisiju:"+commissionDTO.getCommissionId().intValue());
						}
						else if (i==(velicinaListe-1)) {
							conn.commit();
							log.info("OBRISAO ZADNJI ZAPIS za komisiju:"+commissionDTO.getCommissionId().intValue());
						}
						if(retVal == false)
							break;
						rec.getDto().setNotLoaded(true);
					}
				}
				if(retVal == false)
					break;
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	public boolean removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(List<Record> recordsList) {
		boolean retVal = true;	
		//prepare database
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<CommissionDTO> authorCommissionList = new ArrayList<CommissionDTO>();
			
			if(recordsList != null){
				int velicinaListe  = recordsList.size();
				for (int i = 0; i < velicinaListe; i++) {
					authorCommissionList.clear();
					Record rec  = recordsList.get(i);
					if((rec!=null) && (rec.getDto() != null)){
						if (rec.getDto() instanceof PublicationDTO){
							authorCommissionList.addAll(getCommisionsByAuthorsAffiliationFromResearchResult(rec));
						}
					
						for (CommissionDTO commissionDTO : authorCommissionList) {
							if (evaluationDB.removeSingleResultClassificationByCommission(conn, rec.getControlNumber(), commissionDTO.getCommissionId().intValue()) == false){
								retVal = false;
								conn.rollback();
							}
							if(retVal == false)
								break;
						}
					
						if (retVal == true && i%100==0) {
							conn.commit();
						}
						else if (retVal == true && i==(velicinaListe-1)) {
							conn.commit();
						}
						
						if(retVal == false)
							break;
						
						rec.getDto().setNotLoaded(true);
					}
				}
			}
		} catch (SQLException ex) {
			log.fatal(ex);
			ex.printStackTrace();
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} catch (Exception e) {
			log.fatal(e);
			e.printStackTrace();
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					log.fatal(ex);
				}
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		}
		return retVal;
	}
	
	//--------------------------------- Commissions By Authors affiliation START:  GET -------------------//
	
	public List<CommissionDTO> getCommisionsByAuthorsAffiliationFromResearchResult(Record rec){
		List<CommissionDTO> retVal = new ArrayList<CommissionDTO>();
		//za publikcaije
		try{
			if(rec.getDto() instanceof PublicationDTO){
				PublicationDTO publicationDTO = (PublicationDTO)(rec.getDto());
				List<AuthorDTO> authorsAndEditors = publicationDTO.getAllAuthors();
				authorsAndEditors.addAll(publicationDTO.getEditors());
				if(publicationDTO instanceof StudyFinalDocumentDTO) {
					StudyFinalDocumentDTO theses = (StudyFinalDocumentDTO)publicationDTO;
					if((theses.getInstitution().getControlNumber() != null) && (theses.getInstitution().getControlNumber().equals("(BISIS)5929"))){
						authorsAndEditors.addAll(theses.getAdvisors());
					}
				}
				for (AuthorDTO author : authorsAndEditors) {
					Integer commissionId = SamovrednovanjeUtils.getCommissionId(author);
					if(commissionId!=null){
						CommissionDTO commissionDTO = commissions.get(commissionId+"");
						if(!retVal.contains(commissionDTO)){
							retVal.add(commissionDTO);
						}
//						if(commissionId == 713){
//							commissionId = 723;
//							commissionDTO = commissions.get(commissionId+"");
//							if(!retVal.contains(commissionDTO)){
//								retVal.add(commissionDTO);
//							}
//						}
					}
				}
				//fali za patente, projekte i ostale
			}
			//za publikcaije
			else if(rec.getDto() instanceof AuthorDTO){
				AuthorDTO author = (AuthorDTO)(rec.getDto());
				Integer commissionId = SamovrednovanjeUtils.getCommissionId(author);
				if(commissionId!=null){
					CommissionDTO commissionDTO = commissions.get(commissionId+"");
					if(!retVal.contains(commissionDTO)){
						retVal.add(commissionDTO);
					}
//					if(commissionId == 713){
//						commissionId = 723;
//						commissionDTO = commissions.get(commissionId+"");
//						if(!retVal.contains(commissionDTO)){
//							retVal.add(commissionDTO);
//						}
//					}
				}
			}
		} catch (Throwable e){
			log.error("Zapis ne moze da se ucita: " + rec.getMARC21Record().toString());
		}
		
		
		return retVal;
	}
	//============================ Commissions By Authors affiliation START:  GET  ================================//
	
	private String getCommissionType(List<Classification> recordClasses, String scheme, Calendar publicationDate, CommissionDTO commission) {
		for (Classification classification : recordClasses) {
			if(
					(commission.getCommissionId().equals(classification.getCommissionId())) && (classification.getCfClassSchemeId().equals(scheme)) 
						&&  (! (publicationDate.before(classification.getCfStartDate()))) && (((!(publicationDate.after(classification.getCfEndDate())))))){
					return classification.getCfClassId();
			}
		}
		return null;
	}
	
	private String getRecordBasicType(List<Classification> recordClasses, String scheme, Calendar publicationDate) {
		for (Classification classification : recordClasses) {
			if((new Integer(0).equals(classification.getCommissionId())) && (classification.getCfClassSchemeId().equals(scheme)) && (! classification.getCfClassId().equals("monograph")))
			return classification.getCfClassId();
		}
		return null;
	}

	private String getRelationClass(List<RecordRecord> relationsOtherRecordsThisRecord, String scheme, String recordId, Calendar publicationDate) {
		for (RecordRecord recordRecord : relationsOtherRecordsThisRecord) {
			if((recordRecord.getCfClassSchemeId().equals(scheme)) && (recordRecord.getRecordId().equals(recordId)) )
				return recordRecord.getCfClassId();
		}
		return null;
	}
	
	private String getRelatedRecord(List<RecordRecord> relationsThisRecordOtherRecords, String scheme, Calendar publicationDate) {
		for (RecordRecord recordRecord : relationsThisRecordOtherRecords) {
			if((recordRecord.getCfClassSchemeId().equals(scheme)))
				return recordRecord.getRecordId();
		}
		return null;
	}
	
	public boolean reevaluateByCommissionFromAuthorsAffiliation(List<RecordDTO> records, RuleBookDTO ruleBookDTO){
		boolean retVal = false;
		List<Record> listRecords = new ArrayList<Record>();
		for (RecordDTO record : records) {
			listRecords.add(record.getRecord());
		}
		retVal = reevaluateRecordsByCommissionFromAuthorsAffiliation(listRecords, ruleBookDTO);
		return retVal;
	}
	
	public boolean reevaluateRecordsByCommissionFromAuthorsAffiliation(List<Record> records, RuleBookDTO ruleBookDTO){
		boolean retVal = false;
		if(ruleBookDTO == null)
			ruleBookDTO = new RuleBookDTO("ruleBook", "serbianResearchersEvaluation", null, null, null, null);
		retVal = removeEvaluationsByCommisionsFromAuthorsAfiliationForRecords(records);
		if(retVal == true)
			retVal = createEvaluationsByCommisionFromAuthorsAfiliationForRecords(records, ruleBookDTO);
		return retVal;
	}
	
	private DataSource dataSource;
	private CommissionDB commissionDB = new CommissionDB();
	private EvaluationDB evaluationDB = new EvaluationDB();
	private MetricsDB metricsDB = new MetricsDB();
	private RecordDB recordDB = new RecordDB();
	
	private static Map<String, CommissionDTO> commissions; 
	
	private static Log log = LogFactory.getLog(ResearchAreaDAO.class.getName());
	
	

}
