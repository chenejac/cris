/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.CommissionDAO;
import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PublicationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Classification;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.RecordRecord;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * @author Dragan Ivanovic
 *
 */
public class ResultEvaluator {
	
	private static CommissionDAO commissionDAO = new CommissionDAO();
	
	static {
		ResultEvaluator.dataSource = DataSourceFactory.getDataSource();
	}
	
	public static Map<String, ResultMeasureDTO> calculateResultMeasures (Connection conn, Record record, RuleBookDTO ruleBook, List<CommissionDTO> commissionList, List<AuthorDTO> authorsAndEditors) throws Exception{
		
		if(record.getDto().isNotLoaded()){
			record.loadFromDatabase();
		}
		Set<Integer> noEvaluations = new HashSet<Integer>();
		
//		System.out.println("ZAPIS:" + record.getControlNumber());
//		System.out.println("PRAVILNIK:" + ruleBook.toString());
//		System.out.println("KOMISIJA:" + commissionList.get(0).getCommissionId());
//		System.out.println("AUTOR:" + authorsAndEditors.get(0).toString());
		
		Map<String, ResultMeasureDTO> retVal = new HashMap<String, ResultMeasureDTO>();
		RecordDB recordDB = new RecordDB();
		EvaluationDB evaluationDB = new EvaluationDB();
		
		PublicationDTO publicationDTO = (PublicationDTO)(record.getDto());
		Calendar publicationDate = new GregorianCalendar();
		Map<String, Integer> journalCategories = new HashMap<String, Integer>();
		journalCategories.put("scienceJournal", new Integer(1));
		journalCategories.put("nationalJournal", new Integer(2));
		journalCategories.put("leadingNationalJournal", new Integer(3));
		journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
		journalCategories.put("internationalJournal", new Integer(5));
		journalCategories.put("outstandingInternationalJournal", new Integer(6));
		journalCategories.put("leadingInternationalJournal", new Integer(7));
		journalCategories.put("topLeadingInternationalJournal", new Integer(8));
						
		Date publicationTime = new Date();
//		publicationTime.setYear(publicationDTO.getPublicationYear()-1900);
		publicationDate.setTime(publicationTime); 
		
		Integer publicationYear = null;
		if(publicationDTO.getPublicationYear()!=null){
			if(publicationDTO.getPublicationYear().contains("/")){
				publicationYear = Integer.parseInt(publicationDTO.getPublicationYear().substring(0, publicationDTO.getPublicationYear().indexOf("/")));
			} else 
				publicationYear = Integer.parseInt(publicationDTO.getPublicationYear());
		} else 
			return null;
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
		
//		System.out.println("DATUMI:"+publicationDateMinus2.get(GregorianCalendar.YEAR)+";"+publicationDateMinus1.get(GregorianCalendar.YEAR)+";"+publicationDate.get(GregorianCalendar.YEAR)+";"+publicationDatePlus1.get(GregorianCalendar.YEAR)+";");
		
		if (authorsAndEditors.isEmpty()) {
			authorsAndEditors = publicationDTO.getAllAuthors();
			authorsAndEditors.addAll(publicationDTO.getEditors());
		}
		
		for (AuthorDTO author : authorsAndEditors) { //nekako proveriti i vrstu autorstva, a ne samo da li je bilo po toj komisiji.
//			System.out.println("AUTORNIZ:" + author.toString());
			for (CommissionDTO commission : commissionList){
//				System.out.println("KOMISIJANIZ:" + commission.getCommissionId());
				if((retVal.get(commission.getCommissionId().toString())!=null) || (noEvaluations.contains(commission.getCommissionId())))
					continue;
				String schemePrizeType = null;
				String classPrizeType = null;
				String schemeEventType = null;
				String classEventType = null;
				String schemeSuperPublicationType = null;
				String classSuperPublicationType = null;
				String schemeProductType = null;
				String classProductType = null; 
				String schemePatentType = null; 
				String classPatentType = null;
				String schemeResearcherRoleType = null;
				String classResearcherRoleType = null;
				String schemePublicationType = "resultType";
				String classPublicationType = getCommissionType(record, "resultType", publicationDate, commission);	//trazi se direkno oznaka M-ova zadata za zapis po komisiji (M11,..M21..)
//				System.out.println("1 classPublicationType getCommissionType za semu resultType je "+classPublicationType);
				if(classPublicationType==null){
					schemePublicationType = "type";
					classPublicationType = getCommissionType(record, "type", publicationDate, commission);	//trazi se oznaka za zapis po pravilniku (internationalJournal, ..., internationalCornference,..., internationalMonograph)
					
//					System.out.println("2 classPublicationType getCommissionType za semu type je "+classPublicationType);
					
					if(classPublicationType==null)
						classPublicationType = getResearcherType(record.getRecordClasses(), Types.TYPE_SCHEMA, publicationDate); //trazi se klasifikacija samog zapisa da li je on (journal,paperJournal, product, patent, conference, proceedings, fullPP,..., paperMonograph...)
					
//					System.out.println("3 classPublicationType getResearcherType za semu type je "+classPublicationType);
					
					schemeResearcherRoleType = "authorshipType";
					classResearcherRoleType = getRelationClass(record.getRelationsOtherRecordsThisRecord(), schemeResearcherRoleType, author.getControlNumber(), publicationDate); //trazi se klasifikacija same veze sa autorom (is author of, is edithor of...)
					
//					System.out.println("4 classResearcherRoleType getRelationClass za semu authorshipType je "+classResearcherRoleType);
					
					String eventId = getRelatedRecord(record.getRelationsThisRecordOtherRecords(), "publicationEventRelation", "is output from", publicationDate); 
					if(eventId!=null){
//						System.out.println("5.1 provera za event getRelatedRecord");
						Record tmp = recordDB.getRecord(conn, eventId);
						if(tmp!=null){
							classEventType = getCommissionType(tmp, "type", publicationDate, commission);
//							System.out.println("5.1.1 classEventType getCommissionType za semu type je "+classEventType);
							if(classEventType!=null)
								schemeEventType = "type";
						}		
					}
					String superPublicationId = getRelatedRecord(record.getRelationsThisRecordOtherRecords(), "publicationsRelation", "is published in", publicationDate); //trazi se id publikacije u kojoj je objavljen zapis (superpublikacija)
					if(superPublicationId!=null){
//						System.out.println("5.2 provera za super publikaciju getRelatedRecord " + superPublicationId);
						Record tmp = recordDB.getRecord(conn, superPublicationId);
						if(tmp!=null){
							classSuperPublicationType = getCommissionType(tmp, "type", publicationDate, commission); //trazi se oznaka za superpublikacija po pravilniku (internationalJournal, ..., internationalCornference,..., internationalMonograph)
//							System.out.println("5.2.1 classSuperPublicationType getCommissionType za semu type i komisiju 101 je " +classSuperPublicationType);
							if(classSuperPublicationType!=null){
								schemeSuperPublicationType = "type";
//								System.out.println("5.2.1.1 provera da li je super publikacije casopis je vrednovana sa komisijiom i pripda nekoj od definisanih kategorija");
								if(commission.getCommissionId().intValue() < 700){
									if(journalCategories.containsKey(classSuperPublicationType)){
										String tempClassSuperPublicationType = getCommissionType(tmp, "type", publicationDateMinus1, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
										tempClassSuperPublicationType = getCommissionType(tmp, "type", publicationDateMinus2, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
										tempClassSuperPublicationType = getCommissionType(tmp, "type", publicationDatePlus1, commission);
										if(tempClassSuperPublicationType != null)
											if(journalCategories.get(classSuperPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
												classSuperPublicationType = tempClassSuperPublicationType;
											}
	//									System.out.println("5.2.1.1.1 provera za najbolju od tri godine je" + classSuperPublicationType);
									}
								}
							}
							if((classSuperPublicationType == null) && (classEventType == null)){
//								System.out.println("5.2.1.2 provera da li je super publikacije zbornik radova je vrednovana sa komisijiom i pripda nekoj od definisanih kategorija");
								eventId = getRelatedRecord(tmp.getRelationsThisRecordOtherRecords(), "publicationEventRelation", "is output from", publicationDate); 
								schemeEventType = null;
								classEventType = null;
								if(eventId!=null){
									Record tmp2 = recordDB.getRecord(conn, eventId);
//									System.out.println("5.2.1.2.1 provera da li je postoji event "+ tmp2.getControlNumber() +"za zbornik radova vrednovan sa komisijiom i pripada nekoj od definisanih kategorija");
									if(tmp2!=null){
										classEventType = getCommissionType(tmp2, "type", publicationDate, commission);
//										System.out.println("5.2.1.2.1.1 classEventType getCommissionType za semu type je "+classEventType);
										if(classEventType!=null)
											schemeEventType = "type";
									}		
								}
							}
						}		
					}
				}
//				System.out.println("Sve sto ulazi u getResultMeasure\n");
//				System.out.println("schemePrizeType je " + schemePrizeType + "; classPrizeType je " + classPrizeType);
//				System.out.println("schemeResearcherRoleType je " + schemeResearcherRoleType + "; classResearcherRoleType je " + classResearcherRoleType);
//				System.out.println("schemeEventType je " + schemeEventType + "; classEventType je " + classEventType);
//				System.out.println("schemePublicationType je " + schemePublicationType + "; classPublicationType je " + classPublicationType);
//				System.out.println("schemeSuperPublicationType je " + schemeSuperPublicationType + "; classSuperPublicationType je " + classSuperPublicationType);
//				System.out.println("schemeProductType je " + schemeProductType + "; classProductType je " + classProductType);
//				System.out.println("schemePatentType je " + schemePatentType + "; classPatentType je " + classPatentType);
				
				ResultMeasureDTO resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, 
						schemeResearcherRoleType, classResearcherRoleType, 
						schemeEventType, classEventType, 
						schemePublicationType, classPublicationType, 
						schemeSuperPublicationType, classSuperPublicationType, 
						schemeProductType, classProductType, 
						schemePatentType, classPatentType);
				
//				if(resultMeasure!=null)
//					System.out.println("Ocenjena kao "+ resultMeasure.getResultType().getClassId() + " sa bodom " +resultMeasure.getQuantitativeMeasure());
//				else
//					System.out.println("NIJE OCENJENAAAAAAAAAAAAAAAAAAAAAA");
				
				
				if(resultMeasure!=null){
					resultMeasure.setCommissionDTO(commission);
					retVal.put(commission.getCommissionId().toString(), resultMeasure);
				} else {
					noEvaluations.add(commission.getCommissionId());
					if((record.getDto() instanceof PaperJournalDTO) || (record.getDto() instanceof JournalDTO)){
						List<CommissionDTO> connectedCommissions = commissionDAO.getConnectedCommissions(commission.getCommissionId());
						Map<String, ResultMeasureDTO> measures = calculateResultMeasures(conn, record, ruleBook, connectedCommissions, authorsAndEditors);
						ResultMeasureDTO maxValue = null;
						for (ResultMeasureDTO measureDTO : measures.values()) {
							if(maxValue == null){
								maxValue = measureDTO;
							} else if (Integer.parseInt(maxValue.getResultType().getSomeTerm().substring(1)) > Integer.parseInt(measureDTO.getResultType().getSomeTerm().substring(1))){
								maxValue = measureDTO;		
							}
						}
						if(maxValue != null){
							maxValue.setCommissionDTO(commission);
							retVal.put(commission.getCommissionId().toString(), maxValue);
						}
					}
				}
			}				
		}
		return retVal;
	}
	
	
	public static ResultMeasureDTO getResultType(Record record, RuleBookDTO ruleBook, List<CommissionDTO> commissionList, boolean calculate) {
		Connection conn = null;
		ResultMeasureDTO retVal = null;
		try {
			conn = ResultEvaluator.dataSource.getConnection();
			Map<String, ResultMeasureDTO> resultMeasures = (calculate)?calculateResultMeasures(conn, record, ruleBook, commissionList, new ArrayList<AuthorDTO>()):(getCalculatedResultType(conn, record, ruleBook, commissionList));
			
			for (ResultMeasureDTO resultMeasure : resultMeasures.values()) {
				if (retVal == null){
					retVal = resultMeasure;
				} else {
					if(retVal.getResultType().getClassId().compareTo(resultMeasure.getResultType().getClassId())>0)
						retVal = resultMeasure;
				}
			}
		}catch (Throwable e) {
			e.printStackTrace();
			System.out.println(record);
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		return retVal;
	}
	
	private static Map<String, ResultMeasureDTO> getCalculatedResultType(
			Connection conn, Record record, RuleBookDTO ruleBook,
			List<CommissionDTO> commissionList) {
		Map<String, ResultMeasureDTO> retVal = new HashMap<String, ResultMeasureDTO>();
		try{
			PublicationDTO publicationDTO = (PublicationDTO) record.getDto();
			Calendar publicationDate = new GregorianCalendar();
							
			Date publicationTime = new Date();
			publicationDate.setTime(publicationTime); 
			
			Integer publicationYear = null;
			if(publicationDTO.getPublicationYear()!=null){
				if(publicationDTO.getPublicationYear().contains("/")){
					publicationYear = Integer.parseInt(publicationDTO.getPublicationYear().substring(0, publicationDTO.getPublicationYear().indexOf("/")));
				} else 
					publicationYear = Integer.parseInt(publicationDTO.getPublicationYear());
			} else {
				System.out.println(publicationDTO.getHTMLRepresentation());
				return null;
			}
			publicationDate.set(GregorianCalendar.YEAR, publicationYear);
						
			EvaluationDB evaluationDB = new EvaluationDB();
			String schemePrizeType = null;
			String classPrizeType = null;
			String schemeEventType = null;
			String classEventType = null;
			String schemeSuperPublicationType = null;
			String classSuperPublicationType = null;
			String schemeProductType = null;
			String classProductType = null; 
			String schemePatentType = null; 
			String classPatentType = null;
			String schemeResearcherRoleType = null;
			String classResearcherRoleType = null;
			String schemePublicationType = "resultType";
			for (CommissionDTO commission : commissionList) {
				String classPublicationType = evaluationDB.getResultClassificationByCommission(conn, publicationDTO.getControlNumber(), "resultType", publicationDate, commission.getCommissionId());
				
				
				if(classPublicationType!=null){
					ResultMeasureDTO resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
					if(resultMeasure != null){
						resultMeasure.setCommissionDTO(commission);
						retVal.put(commission.getCommissionId().toString(), resultMeasure);
					} 
				} 
//				else {
//					ResultMeasureDTO resultMeasure = new ResultMeasureDTO();
//					resultMeasure.setQuantitativeMeasure(new Double(0.0));
//					ResultTypeDTO rt = new ResultTypeDTO();
//					rt.setSchemeId("resultType");
//					rt.setClassId("M99");
//					rt.setTerm(new MultilingualContentDTO("Publikacije koje jos nisu vrednovane", MultilingualContentDTO.LANGUAGE_SERBIAN, MultilingualContentDTO.TRANS_ORIGINAL));
//					resultMeasure.setResultType(rt);
//					resultMeasure.setCommissionDTO(commission);
//					retVal.put(commission.getCommissionId().toString(), resultMeasure);
//				}
			}
			}catch (Throwable e) {
				e.printStackTrace();
				System.out.println(record);
			}
			return retVal;
	}


	public static void reevaluateAll(){
		EvaluationDB evaluationDB = new EvaluationDB();
		CommissionDAO commissionDAO = new CommissionDAO();
		Connection conn = null;
		try {
			conn = ResultEvaluator.dataSource.getConnection();
			List<RuleBookDTO> ruleBookList = evaluationDB.getRuleBooks(conn);
	        RuleBookDTO ruleBook = null;
			for (RuleBookDTO rb : ruleBookList){
	        	if("serbianResearchersEvaluation".equals(rb.getClassId()))
	        		ruleBook = rb;
	        }
			
			BooleanQuery bq = new BooleanQuery();
			BooleanQuery type = new BooleanQuery();
//			type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_FULL_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.INVITED_TALK_ABSTRACT_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.DISCUSSION_PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PAPER_PROCEEDINGS)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PAPER_MONOGRAPH)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.MONOGRAPH)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
			type.add(new TermQuery(new Term("TYPE", Types.PHD_ART_PROJECT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OLD_MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.MASTER_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.OLD_BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.BACHELOR_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.SPECIALISTIC_STUDY_FINAL_DOCUMENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PATENT)), Occur.SHOULD);
//			type.add(new TermQuery(new Term("TYPE", Types.PRODUCT)), Occur.SHOULD);
			bq.add(type, Occur.MUST);
			
			bq.add(new TermQuery(new Term("PY", "2019")), Occur.MUST);
			
			List<Record> records = Retriever.select(bq, new AllDocCollector(false));
			
			List<CommissionDTO> allCommissionList = commissionDAO.getCommissions();
			System.out.println(records.size());
			int i = 0;
			int j = 0;
			for (Record record : records) {
				System.out.println(records.indexOf(record));
//				if(records.indexOf(record) < 3000){
//					record.clear();
//					continue;
//				}
//				if(records.indexOf(record) > 3000)
//					break;
				if(
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 711) != null) || 
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 712) != null) || 
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 713) != null) || 
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 714) != null) ||
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 715) != null) 
						||
						(evaluationDB.getResultClassificationByCommission(conn, record.getControlNumber(), "resultType", new GregorianCalendar(), 701) != null)
						){
					record.clear();
					i++;
					continue;
				}
				try {
					List<CommissionDTO> commissionList = new ArrayList<CommissionDTO>();
					PublicationDTO publicationDTO = (PublicationDTO)(record.getDto());
					List<AuthorDTO> authorsAndEditors = publicationDTO.getAllAuthors();
					authorsAndEditors.addAll(publicationDTO.getEditors());
					for (AuthorDTO author : authorsAndEditors) {
						Integer commissionId = null;
						OrganizationUnitDTO rootOrganizationUnit = author.getOrganizationUnit();
						while (rootOrganizationUnit.getSuperOrganizationUnit() != null){
							rootOrganizationUnit = rootOrganizationUnit.getSuperOrganizationUnit();
						}
						String orgUnitControlNumber = rootOrganizationUnit.getControlNumber();
						if(orgUnitControlNumber != null){
							if(orgUnitControlNumber.equals("(BISIS)6782"))
								commissionId = 711;
							if(orgUnitControlNumber.equals("(BISIS)6781"))
								commissionId = 712;
							if(orgUnitControlNumber.equals("(BISIS)6780"))
								commissionId = 713;
							if(orgUnitControlNumber.equals("(BISIS)6779"))
								commissionId = 714;
							if(orgUnitControlNumber.equals("(BISIS)6778"))
								commissionId = 715;
						}
						/*if(commissionId == null && author.getInstitution()!=null && author.getInstitution().getControlNumber()!=null) {					
							if(author.getInstitution().getControlNumber().equals("(BISIS)5933")){
								commissionId = 701;
							}
						}*/
						
						if(commissionId!=null){
							for (CommissionDTO commissionDTO : allCommissionList) {
								if(commissionDTO.getCommissionId().equals(commissionId))
//								if(((! (publicationDTO instanceof PaperJournalDTO))) || (commissionDTO.getCommissionId().equals(commissionId)))
									if(! commissionList.contains(commissionDTO))
										commissionList.add(commissionDTO);
							}
						}
					}
					fillResultType(conn, record, ruleBook, commissionList);
					j++;
				} catch (Throwable e) {
					System.out.println("Problem: " + record.getControlNumber());
					e.printStackTrace();
				} finally {
					record.clear();
				}
			}
			System.out.println(i);
			System.out.println(j);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static void fillResultType(Connection conn, Record record, RuleBookDTO ruleBook, List<CommissionDTO> commissionList) {
		
		EvaluationDB evaluationDB = new EvaluationDB();
		PublicationDTO publicationDTO = (PublicationDTO)(record.getDto());
		
		try {
			Map<String, ResultMeasureDTO> resultMeasures = calculateResultMeasures(conn, record, ruleBook, commissionList, new ArrayList<AuthorDTO>());
			for (String commissionID : resultMeasures.keySet()) {
				ResultMeasureDTO resultMeasure = resultMeasures.get(commissionID);
				Calendar startDate = new GregorianCalendar();
				startDate.set(Calendar.YEAR, 1908);
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				Calendar endDate = new GregorianCalendar();
				endDate.set(Calendar.YEAR, 3008);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				evaluationDB.addResultClassificationByCommission(conn, publicationDTO.getControlNumber(), "resultType", resultMeasure.getResultType().getClassId(), startDate, endDate, Integer.parseInt(commissionID));
			}
		}catch (Throwable e) {
			e.printStackTrace();
			System.out.println(record);
		}
	}
	

	private static String getResearcherType(List<Classification> recordClasses,
			String scheme, Calendar publicationDate) {
		for (Classification classification : recordClasses) {
			if(
					(new Integer(0).equals(classification.getCommissionId()))
					&& (classification.getCfClassSchemeId().equals(scheme)) && (! classification.getCfClassId().equals("monograph")))
				return classification.getCfClassId();
		}
		return null;
	}

	private static String getCommissionType(Record record,
			String scheme, Calendar publicationDate, CommissionDTO commission) {
		List<Classification> recordClasses = record.getRecordClasses();
		for (Classification classification : recordClasses) {
			if(
					(commission.getCommissionId().equals(classification.getCommissionId()))
			&& (classification.getCfClassSchemeId().equals(scheme)) 
			&&  (! (publicationDate.before(classification.getCfStartDate()))) 
			&& (((!(publicationDate.after(classification.getCfEndDate())))))){
				return classification.getCfClassId();
			}
		}
		
		if((record.getDto() instanceof PaperJournalDTO) || (record.getDto() instanceof JournalDTO)){
			Map<String, Integer> journalCategories = new HashMap<String, Integer>();
			journalCategories.put("scienceJournal", new Integer(1));
			journalCategories.put("nationalJournal", new Integer(2));
			journalCategories.put("leadingNationalJournal", new Integer(3));
			journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
			journalCategories.put("internationalJournal", new Integer(5));
			journalCategories.put("outstandingInternationalJournal", new Integer(6));
			journalCategories.put("leadingInternationalJournal", new Integer(7));
			journalCategories.put("topLeadingInternationalJournal", new Integer(8));
			
			List<CommissionDTO> connectedCommissions = commissionDAO.getConnectedCommissions(commission.getCommissionId());
			String maxValue = null;
			for (CommissionDTO commissionDTO : connectedCommissions) {
				String tempValue = getCommissionType(record, scheme, publicationDate, commissionDTO);
				if(tempValue != null){
					if(maxValue == null){
						maxValue = tempValue;
					} else {
						if ((journalCategories.containsKey(tempValue)) && (journalCategories.containsKey(maxValue))){
							if(journalCategories.get(maxValue).intValue() < journalCategories.get(tempValue).intValue()){
								maxValue = tempValue;
							}
						} else {
							if (Integer.parseInt(maxValue.substring(1)) > Integer.parseInt(tempValue.substring(1))){
								maxValue = tempValue;		
							}
						}		
					}
				} 
			}
			if(maxValue != null){
				return maxValue;
			}
		}
		return null;
	}

	private static String getRelatedRecord(
			List<RecordRecord> relationsThisRecordOtherRecords, String scheme, String classId, Calendar publicationDate) {
		for (RecordRecord recordRecord : relationsThisRecordOtherRecords) {
			if((recordRecord.getCfClassSchemeId().equals(scheme)) && (recordRecord.getCfClassId().equals(classId)))
				return recordRecord.getRecordId();
		}
		return null;
	}

	private static String getRelationClass(
			List<RecordRecord> relationsOtherRecordsThisRecord, String scheme, String recordId, Calendar publicationDate) {
		for (RecordRecord recordRecord : relationsOtherRecordsThisRecord) {
			if((recordRecord.getCfClassSchemeId().equals(scheme)) && (recordRecord.getRecordId().equals(recordId)) )
				return recordRecord.getCfClassId();
		}
		return null;
	}

	public static List<ResultMeasureDTO> getResultTypes(Record record, Integer publicationYear, RuleBookDTO ruleBook, CommissionDTO commission) {
		List<ResultMeasureDTO> retVal = null;
		Connection conn = null;
		EvaluationDB evaluationDB = new EvaluationDB();
		try{
			
			if(commission == null || commission.getCommissionId()<=0)
				return null;
			
			if(!(record.getDto() instanceof PublicationDTO))
				return null;
			
			if(!(record.getDto() instanceof JournalDTO))
				return null;
			
			conn = ResultEvaluator.dataSource.getConnection();
		
			Calendar publicationDate = new GregorianCalendar();
			Map<String, Integer> journalCategories = new HashMap<String, Integer>();
			journalCategories.put("scienceJournal", new Integer(1));
			journalCategories.put("nationalJournal", new Integer(2));
			journalCategories.put("leadingNationalJournal", new Integer(3));
			journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
			journalCategories.put("internationalJournal", new Integer(5));
			journalCategories.put("outstandingInternationalJournal", new Integer(6));
			journalCategories.put("leadingInternationalJournal", new Integer(7));
			journalCategories.put("topLeadingInternationalJournal", new Integer(8));
							
			Date publicationTime = new Date();
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
			
			if(record.getDto().isNotLoaded())
				record.loadFromDatabase();
			
			List<ResultMeasureDTO> resultMeasures = new ArrayList<ResultMeasureDTO>();
			
			String schemePrizeType = null;
			String classPrizeType = null;
			String schemeEventType = null;
			String classEventType = null;
			String schemeSuperPublicationType = null;
			String classSuperPublicationType = null;
			String schemeProductType = null;
			String classProductType = null; 
			String schemePatentType = null; 
			String classPatentType = null;
			String schemeResearcherRoleType = null;
			String classResearcherRoleType = null;
			String schemePublicationType = null;
			String classPublicationType = null;

			String superPublicationType = getCommissionType(record, "type", publicationDate, commission); //vratice null ako ne postoji evaluacija
			if(journalCategories.containsKey(superPublicationType)){
				String tempClassSuperPublicationType = getCommissionType(record, "type", publicationDateMinus1, commission);
				if(tempClassSuperPublicationType != null)
					if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
						superPublicationType = tempClassSuperPublicationType;
					}
				tempClassSuperPublicationType = getCommissionType(record, "type", publicationDateMinus2, commission);
				if(tempClassSuperPublicationType != null)
					if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
						superPublicationType = tempClassSuperPublicationType;
					}
				tempClassSuperPublicationType = getCommissionType(record, "type", publicationDatePlus1, commission);
				if(tempClassSuperPublicationType != null)
					if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
						superPublicationType = tempClassSuperPublicationType;
					}
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is author of";
			schemePublicationType = "type";
			classPublicationType = "paperJournal";
			schemeSuperPublicationType = "type";
			classSuperPublicationType = superPublicationType;
			
			ResultMeasureDTO resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is author of";
			schemePublicationType = "type";
			classPublicationType = "scientificCriticismJournal";
			schemeSuperPublicationType = "type";
			classSuperPublicationType = superPublicationType;
			
			resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is editor of";
			schemePublicationType = "type";
			classPublicationType = superPublicationType;
			schemeSuperPublicationType = null;
			classSuperPublicationType = null;
			
			resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			retVal = resultMeasures;
			
		}catch (Throwable e) {
			e.printStackTrace();
			System.out.println(record);
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		return retVal;
	}
	
	public static List<ResultMeasureDTO> getResultTypesVece(Record record, Integer publicationYear, RuleBookDTO ruleBook, CommissionDTO commission) {
		List<ResultMeasureDTO> retVal = null;
		Connection conn = null;
		EvaluationDB evaluationDB = new EvaluationDB();
		MetricsDB metricsDB = new MetricsDB();
		
		try{
			
			if(commission == null || commission.getCommissionId()<=0)
				return null;
			
			if(!(record.getDto() instanceof PublicationDTO))
				return null;
			
			if(!(record.getDto() instanceof JournalDTO))
				return null;
			
			conn = ResultEvaluator.dataSource.getConnection();
		
			Calendar publicationDate = new GregorianCalendar();
			Map<String, Integer> journalCategories = new HashMap<String, Integer>();
			journalCategories.put("scienceJournal", new Integer(1));
			journalCategories.put("nationalJournal", new Integer(2));
			journalCategories.put("leadingNationalJournal", new Integer(3));
			journalCategories.put("speciallyVerifiedInternationalJournal", new Integer(4));
			journalCategories.put("internationalJournal", new Integer(5));
			journalCategories.put("outstandingInternationalJournal", new Integer(6));
			journalCategories.put("leadingInternationalJournal", new Integer(7));
			journalCategories.put("topLeadingInternationalJournal", new Integer(8));
			
			Date publicationTime = new Date();
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
			
			record.loadFromDatabase();
			
			List<ResultMeasureDTO> resultMeasures = new ArrayList<ResultMeasureDTO>();
			
			String schemePrizeType = null;
			String classPrizeType = null;
			String schemeEventType = null;
			String classEventType = null;
			String schemeSuperPublicationType = null;
			String classSuperPublicationType = null;
			String schemeProductType = null;
			String classProductType = null; 
			String schemePatentType = null; 
			String classPatentType = null;
			String schemeResearcherRoleType = null;
			String classResearcherRoleType = null;
			String schemePublicationType = null;
			String classPublicationType = null;

			String superPublicationType = getCommissionType(record, "type", publicationDate, commission);
			
			List<ImpactFactor> impactFactors = metricsDB.getJournalImpactFactors(conn, record.getControlNumber(), "twoYearsIF");
			
			boolean foundmin2 	= false;
			boolean foundmin1 	= false;
			boolean found0 		= false;
			boolean foundplu1 	= false;
			
			for (ImpactFactor imf : impactFactors) {			
				if(imf.getYear().intValue() == (publicationYear-2))
					foundmin2 = true;
				if(imf.getYear().intValue() == (publicationYear-1))
					foundmin1 = true;
				if(imf.getYear().intValue() == (publicationYear))
					found0 = true;
				if(imf.getYear().intValue() == (publicationYear+1))
					foundplu1 = true;
			}
			if(foundmin2 && foundmin1 && found0 && foundplu1){
				if(journalCategories.containsKey(superPublicationType)){
					String tempClassSuperPublicationType = getCommissionType(record, "type", publicationDateMinus1, commission);
					if(tempClassSuperPublicationType != null){
						if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
							superPublicationType = tempClassSuperPublicationType;
						}
					}
					tempClassSuperPublicationType = getCommissionType(record, "type", publicationDateMinus2, commission);
					if(tempClassSuperPublicationType != null){
						if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
							superPublicationType = tempClassSuperPublicationType;
						}
					}
					tempClassSuperPublicationType = getCommissionType(record, "type", publicationDatePlus1, commission);
					if(tempClassSuperPublicationType != null){
						if(journalCategories.get(superPublicationType).intValue() < journalCategories.get(tempClassSuperPublicationType).intValue()){
							superPublicationType = tempClassSuperPublicationType;
						}
					}
				}
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is author of";
			schemePublicationType = "type";
			classPublicationType = "paperJournal";
			schemeSuperPublicationType = "type";
			classSuperPublicationType = superPublicationType;
			
			ResultMeasureDTO resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is author of";
			schemePublicationType = "type";
			classPublicationType = "scientificCriticismJournal";
			schemeSuperPublicationType = "type";
			classSuperPublicationType = superPublicationType;
			
			resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			schemeResearcherRoleType = "authorshipType";
			classResearcherRoleType = "is editor of";
			schemePublicationType = "type";
			classPublicationType = superPublicationType;
			schemeSuperPublicationType = null;
			classSuperPublicationType = null;
			
			resultMeasure = evaluationDB.getResultMeasure(conn, ruleBook, commission, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(resultMeasure!=null){
				resultMeasures.add(resultMeasure);
			}
			
			retVal = resultMeasures; 
			
		}catch (Throwable e) {
			e.printStackTrace();
			System.out.println(record);
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		return retVal;
	}
	
	private static DataSource dataSource;
	


}
