package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.CommissionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultMeasureDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResultTypeDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.RuleBookDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ScienceAreaDTO;

/**
 * Class for persist and retrieve data about evaluation
 * from database.
 * 
 * @author chenejac@uns.ac.rs
 */
public class EvaluationDB extends CERIFSemanticLayerDB {
	
	private static Log log = LogFactory.getLog(EvaluationDB.class.getName());
	
	//--------------------------------- ResultMeasure By RuleBook and Science Area START: GET -------------------// 
	
	public ResultMeasureDTO getResultMeasure(Connection conn, RuleBookDTO ruleBook, CommissionDTO commission, String schemePrizeType, String classPrizeType, String schemeResearcherRoleType, String classResearcherRoleType, String schemeEventType, String classEventType, 
			  									String schemePublicationType, String classPublicationType, String schemeSuperPublicationType, String classSuperPublicationType, String schemeProductType, String classProductType, String schemePatentType, String classPatentType){
		ResultMeasureDTO rm = null;
		try {
			ResultTypeDTO rt = getResultType(conn, ruleBook, schemePrizeType, classPrizeType, schemeResearcherRoleType, classResearcherRoleType, schemeEventType, classEventType, 
						schemePublicationType, classPublicationType, schemeSuperPublicationType, classSuperPublicationType, schemeProductType, classProductType, schemePatentType, classPatentType);
			if(rt!=null){
				rm = getResultMeasure(conn, rt, commission);
			}
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rm;
	}
	
	private ResultMeasureDTO getResultMeasure(Connection conn, ResultTypeDTO resultType, CommissionDTO commission) {
		ResultMeasureDTO rm = null;
		try {
			String query = "select QUANTITATIVEMEASURE from RESULTTYPEMEASURE where CFCLASSSCHEMEIDSCIENCEGROUP like '"
				+ commission.getScienceArea().getSchemeId() + "' and CFCLASSIDSCIENCEGROUP like '"
				+ commission.getScienceArea().getClassId() + "' and CFCLASSSCHEMEIDRESULTTYPE like '"
				+ resultType.getSchemeId() + "' and CFCLASSIDRESULTTYPE like '"
				+ resultType.getClassId() + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) {
				Double quantiativeMeasure = rset.getDouble(1);
				rm = new ResultMeasureDTO(resultType, commission, commission.getScienceArea(), quantiativeMeasure);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rm;
	}

	//============================ ResultMeasure By RuleBook and Science Area END: GET ================================// 
	
	//--------------------------------- ResultType Classification (e.g.M) By RuleBook START: GET -------------------// 
	
	private ResultTypeDTO getResultType(Connection conn, RuleBookDTO ruleBook,
			String schemePrizeType, String classPrizeType, String schemeResearcherRoleType,
			String classResearcherRoleType, String schemeEventType,
			String classEventType, String schemePublicationType,
			String classPublicationType, String schemeSuperPublicationType,
			String classSuperPublicationType, String schemeProductType,
			String classProductType, String schemePatentType,
			String classPatentType) {
		ResultTypeDTO rt = null;
		String query = "select CFCLASSSCHEMEIDRULEBOOKRESULLTTYPE, CFCLASSIDRULEBOOKRESULTTYPE from RESULTMAPPING where CFCLASSSCHEMEIDPRIZETYPE "
			+ ((schemePrizeType==null)?("is null"):("like '"
					+ schemePrizeType + "'")) + " and CFCLASSIDPRIZETYPE "
					+ ((classPrizeType==null)?("is null"):("like '"
					+ classPrizeType + "'")) + " and CFCLASSSCHEMEIDRESEARCHERROLETYPE "
					+ ((schemeResearcherRoleType==null)?("is null"):("like '"
					+ schemeResearcherRoleType + "'")) + " and CFCLASSIDRESEARCHERROLETYPE "
					+ ((classResearcherRoleType==null)?("is null"):("like '"
					+ classResearcherRoleType + "'")) + " and CFCLASSSCHEMEIDEVENTTYPE " 
					+ ((schemeEventType==null)?("is null"):("like '"
					+ schemeEventType + "'")) + " and CFCLASSIDEVENTTYPE "
					+ ((classEventType==null)?("is null"):("like '"
					+ classEventType + "'")) + " and CFCLASSSCHEMEIDPUBLICATIONTYPE "
					+ ((schemePublicationType==null)?("is null"):("like '"
					+ schemePublicationType + "'")) + " and CFCLASSIDPUBLICATIONTYPE "
					+ ((classPublicationType==null)?("is null"):("like '"
					+ classPublicationType + "'")) + " and CFCLASSSCHEMEIDSUPERPUBLICATIONTYPE "
					+ ((schemeSuperPublicationType==null)?("is null"):("like '"
					+ schemeSuperPublicationType + "'")) + " and CFCLASSIDSUPERPUBLICATIONTYPE "
					+ ((classSuperPublicationType==null)?("is null"):("like '"
					+ classSuperPublicationType + "'")) + " and CFCLASSSCHEMEIDPRODUCTTYPE "
					+ ((schemeProductType==null)?("is null"):("like '"
					+ schemeProductType + "'")) + " and CFCLASSIDPRODUCTTYPE "
					+ ((classProductType==null)?("is null"):("like '"
					+ classProductType + "'")) + " and CFCLASSSCHEMEIDPATENTTYPE "
					+ ((schemePatentType==null)?("is null"):("like '"
					+ schemePatentType + "'")) + " and CFCLASSIDPATENTTYPE "
					+ ((classPatentType==null)?("is null"):("like '"
					+ classPatentType + "'"));
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt 		  
					.executeQuery(query);

			while (rset.next()) {
				String schemeResultType = rset.getString(1);
				String classResultType = rset.getString(2);
				
				ResultTypeDTO tmp = getResultType(conn, schemeResultType, classResultType);
				if(ruleBook.equals(tmp.getRuleBook())){
					rt = tmp;
					break;
				}
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rt;
	}
 
	private ResultTypeDTO getSuperResultType(Connection conn,
			String schemeResultType, String classResultType) {
		ResultTypeDTO rb = null;
		try {
			String query = "select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
				+ schemeResultType + "' and CFCLASSID1 like '"
				+ classResultType + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like 'resultType'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) {
				String classSuperResultType = rset.getString(1);
				rb = getResultType(conn, "resultType", classSuperResultType);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rb;
	}
	
	private RuleBookDTO getResultTypeRuleBook(Connection conn,
			String schemeResultType, String classResultType) {
		RuleBookDTO rb = null;
		try {
			String query = "select CFCLASSID2 from CFCLASS_CLASS where CFCLASSSCHEMEID1 like '"
				+ schemeResultType + "' and CFCLASSID1 like '"
				+ classResultType + "' and CFCLASSSCHEMEID like 'hierarchy' and CFCLASSID like 'belongsTo' and CFCLASSSCHEMEID2 like 'ruleBook'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) {
				String classRuleBook = rset.getString(1);
				rb = getRuleBook(conn, "ruleBook", classRuleBook);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rb;
	}
	
	public ResultTypeDTO getResultType(Connection conn, String schemeResultType, String classResultType){
		ResultTypeDTO rt = null;
		try {
			rt = new ResultTypeDTO(schemeResultType, classResultType, getClassTerm(conn, schemeResultType, classResultType), 
					getClassTermTranslations(conn, schemeResultType, classResultType), getClassDescription(conn, schemeResultType, classResultType), 
					getClassDescriptionTranslations(conn, schemeResultType, classResultType),getSuperResultType(conn, schemeResultType, classResultType), 
					getResultTypeRuleBook(conn, schemeResultType, classResultType));
		} catch (Exception ex) {
			
			log.fatal(ex);
		}
		return rt;
	}
	
	//============================ ResultType Classification (e.g.M) By RuleBook END: GET ============================// 
	
	//--------------------------------- ResultClassification By Commission START: ADD, GET, REMOVE -------------------// 
	
	//kreira klasifikaciju po komisiji koja vazi u odredjenom periodu, ako se postavi classSchemeId da je resultType onda se dobija klasifikacija naucnih rezultata
	public boolean addResultClassificationByCommission(Connection conn, String recordid, String cfclassschemeid, String cfclassid, Calendar cfstartdate, Calendar cfenddate, Integer commissionid){
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_CLASS (RECORDID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE, COMMISSIONID) values (?, ?, ?, ?, ?, ?)");

			stmt.setString(1, recordid);
			stmt.setString(2, cfclassschemeid);
			stmt.setString(3, cfclassid);
			stmt.setDate(4, new java.sql.Date(cfstartdate.getTime()
						.getTime()));
			stmt.setDate(5, new java.sql.Date(cfenddate.getTime()
					.getTime()));
			stmt.setInt(6, commissionid);
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot add relations between mARC21Record: " + recordid + " and classes: " + cfclassid + " according to commission: " + commissionid);
		}
		return false;
	}

	//vraca klasifikaciju po komisiji za odredjeno vreme , ako se postavi classSchemeId da je resultType onda se dobija klasifikacija naucnih rezultata
	public String getResultClassificationByCommission(Connection conn, String controlNumber, String classSchemeId, Calendar publicationDate, int commissionId) {
		String retVal = null;
		try {
			java.sql.Date pubDate = new java.sql.Date(publicationDate.getTime().getTime());
			Statement stmt = conn.createStatement();
			String query = "select CFCLASSID from MARC21RECORD_CLASS where RECORDID like '" + controlNumber + "' AND  CFCLASSSCHEMEID like '" + classSchemeId + "' AND COMMISSIONID = " + commissionId + " AND CFSTARTDATE < '" + pubDate + "' AND CFENDDATE > '" + pubDate+ "'";
			ResultSet rset = stmt
					.executeQuery(query);

			while (rset.next()) {
				retVal = rset.getString(1);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	//uklanja klasifikaciju po komisiji za odredjeno vreme, ako se postavi classSchemeId da je resultType onda se uklanja klasifikacija naucnih rezultata
	public boolean removeResultClassificationByCommission(Connection conn, String controlNumber, String classSchemeId, Calendar publicationDate, int commissionId) {
		boolean retVal = false;
		try {
			java.sql.Date pubDate = new java.sql.Date(publicationDate.getTime().getTime());
			Statement stmt = conn.createStatement();
			String query = "delete from MARC21RECORD_CLASS where RECORDID like '" + controlNumber + "' AND  CFCLASSSCHEMEID like '" + classSchemeId + "' AND COMMISSIONID = " + commissionId + " AND CFSTARTDATE < '" + pubDate + "' AND CFENDDATE > '" + pubDate+ "'";
			stmt.executeUpdate(query);
			stmt.close();
			retVal = true;
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	//uklanja sve klasifikacije odredjenog tipa zapisa po komisiji, cfclassid mora biti neki od tipova iz Types klase
	public boolean removeAllResultsClassificationByCommissionAndRecordType(Connection conn, int commissionId, String recordsType ) {
		boolean retVal = false;
		try {
//			System.out.println("A");
			if (commissionId == 0 || recordsType == null || recordsType.equals("")) {
				throw new Exception("metoda:removeAllClassificationByCommission, nije zadata komisija ili nije zadat tip koji se brise");
			}	
			Statement stmt = conn.createStatement();
//			System.out.println("B");
//			String query = "DELETE A FROM MARC21RECORD_CLASS A LEFT JOIN MARC21RECORD_CLASS B ON A.recordid=B.recordid WHERE A.commissionid = " + commissionId + " AND B.commissionid = 0 AND B.cfclassschemeid LIKE 'type' AND B.cfclassid LIKE '" + recordsType + "'";
			String query = "DELETE FROM MARC21RECORD_CLASS WHERE commissionid = " + commissionId + " AND recordid in (SELECT recordid FROM (SELECT DISTINCT(B.recordid) FROM MARC21RECORD_CLASS B WHERE B.commissionid = 0 AND B.cfclassschemeid LIKE 'type' AND B.cfclassid LIKE '" + recordsType + "') Privremena) ORDER BY recordid ASC LIMIT 1000";	
//			System.out.println(query);
			int brojObrisanih = -1;
			do {
				brojObrisanih = stmt.executeUpdate(query);
				conn.commit();
				log.info("OBRISAO U PROLAZU ZAPISA:"+brojObrisanih + " za komisiju:"+commissionId);
//				System.out.println("Broj obrisanih je:"+brojObrisanih + " za komisiju:"+commissionId);
			} while (brojObrisanih>0);
			
//			System.out.println("D");
			stmt.close();
			retVal = true;
		} catch (Exception ex) {
			log.fatal(ex);
			retVal = false;
		}
		
		try {
			if (retVal!=true){
				conn.rollback();
			}
		}
		catch (SQLException ex) {
			log.fatal(ex);
		}
		
		return retVal;
	}
	
	//uklanja sve klasifikacije klasifikacije odredjenog zapisa po komisiji, cfclassid mora biti neki od tipova iz Types klase
	public boolean removeSingleResultClassificationByCommission(Connection conn, String controlNumber, int commissionId) {
		boolean retVal = false;
		try {
//			System.out.println("A");
			if (commissionId == 0 || controlNumber == null || controlNumber.equals("")) {
				throw new Exception("metoda:removeSingleClassificationByCommission, nije zadata komisija ili nije zadat zapis koji se brise");
			}
			Statement stmt = conn.createStatement();
//			System.out.println("B");
			String query = "delete from MARC21RECORD_CLASS where commissionid = " +  commissionId + " AND recordid like '" + controlNumber + "'";
//			System.out.println(query);
			stmt.executeUpdate(query);
//			System.out.println("D");
			stmt.close();
			retVal = true;
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	//============================ ResultClassification By Commission END: ADD, GET, REMOVE ================================// 
	
	//--------------------------------- RuleBook START: GET-------------------// 
	
	public RuleBookDTO getRuleBook(Connection conn, String schemeRuleBook, String classRuleBook){
		RuleBookDTO rb = null;
		try {
			rb = new RuleBookDTO(schemeRuleBook, classRuleBook, getClassTerm(conn, schemeRuleBook, classRuleBook), getClassTermTranslations(conn, schemeRuleBook, classRuleBook), getClassDescription(conn, schemeRuleBook, classRuleBook), getClassDescriptionTranslations(conn, schemeRuleBook, classRuleBook));
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return rb;
	}
	
	public List<RuleBookDTO> getRuleBooks(Connection conn) {
		List<RuleBookDTO> retVal = new ArrayList<RuleBookDTO>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCLASSID from CFCLASS where CFCLASSSCHEMEID like 'ruleBook'");

			while (rset.next()) {
				String classRuleBook = rset.getString(1);
				RuleBookDTO rb = new RuleBookDTO("ruleBook", classRuleBook, getClassTerm(conn, "ruleBook", classRuleBook), getClassTermTranslations(conn, "ruleBook", classRuleBook), getClassDescription(conn, "ruleBook", classRuleBook), getClassDescriptionTranslations(conn, "ruleBook", classRuleBook));
				retVal.add(rb);
			}
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	//============================ RuleBook END: GET ================================// 
}
