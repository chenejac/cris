package rs.ac.uns.ftn.informatika.bibliography.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.MetricsDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ImpactFactor;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ResearchAreaRanking;

/**
 * Class for persist and retrieve data about metrics.
 * 
 * @author chenejac@uns.ac.rs
 */
public class MetricsDB {
		
	public MultilingualContentDTO getMetricsName(Connection conn, String metricsId) {
		MultilingualContentDTO retVal = new MultilingualContentDTO(null, "eng", null);
		try {
			String query = "select CFNAME, CFLANGCODE from CFMETRICSNAME where CFMETRICSID like '"
			+ metricsId + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) 
				retVal = new MultilingualContentDTO(rset.getString(1), rset.getString(2), null);
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<MultilingualContentDTO> getMetricsNameTranslations(Connection conn, String metricsId) {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		try {
			String query = "select CFNAME, CFLANGCODE from CFMETRICSNAME where CFMETRICSID like '"
			+ metricsId + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			while (rset.next()) 
				retVal.add(new MultilingualContentDTO(rset.getString(1), rset.getString(2), null));
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public MultilingualContentDTO getMetricsDescription(Connection conn, String metricsId) {
		MultilingualContentDTO retVal = new MultilingualContentDTO(null, "srp", null);
		try {
			String query = "select CFDESCRIPTION, CFLANGCODE from CFMETRICSDESCRIPTION where CFMETRICSID like '"
				+ metricsId + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			if (rset.next()) 
				retVal = new MultilingualContentDTO(rset.getString(1), rset.getString(2), null);
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public List<MultilingualContentDTO> getMetricsDescriptionTranslations(Connection conn, String metricsId) {
		List<MultilingualContentDTO> retVal = new ArrayList<MultilingualContentDTO>();
		try {
			String query = "select CFDESCRIPTION, CFLANGCODE from CFMETRICSDESCRIPTION where CFMETRICSID like '"
				+ metricsId + "'";
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery(query);

			while (rset.next()) 
				retVal.add(new MultilingualContentDTO(rset.getString(1), rset.getString(2), null));
			rset.close();
			stmt.close();
		} catch (Exception ex) {
			log.fatal(ex);
		}
		return retVal;
	}
	
	public boolean addMetricsNames(Connection conn, String metricsId, List<MultilingualContentDTO> metricsNames) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFMETRICSNAME (CFMETRICSID, CFLANGCODE, CFNAME) values (?, ?, ?)");
			stmt.setString(1, metricsId);
			
			for (MultilingualContentDTO metricsName : metricsNames) {
				stmt.setString(2, metricsName.getLanguage());
				stmt.setString(3, metricsName.getContent());
			
				stmt.executeUpdate();
			}
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add metricsNames: " + metricsId );
		}
		return false;
	}
	
	public boolean addMetricsDescriptions(Connection conn, String metricsId, List<MultilingualContentDTO> metricsDescriptions) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFMETRICSDESCRIPTION (CFMETRICSID, CFLANGCODE, CFDESCRIPTION) values (?, ?, ?)");
			stmt.setString(1, metricsId);
			
			for (MultilingualContentDTO metricsDescription : metricsDescriptions) {
				stmt.setString(2, metricsDescription.getLanguage());
				stmt.setString(3, metricsDescription.getContent());
			
				stmt.executeUpdate();
			}
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add metricsDescriptions: " + metricsId );
		}
		return false;
	}
	
	public boolean updateMetricsNames(Connection conn, String metricsId, List<MultilingualContentDTO> metricsNames) {
		if(deleteMetricsNames(conn, metricsId))
			return addMetricsNames(conn, metricsId, metricsNames);
		else return false;
	}
	
	public boolean updateMetricsDescriptions(Connection conn, String metricsId, List<MultilingualContentDTO> metricsDescriptions) {
		if(deleteMetricsDescriptions(conn, metricsId))
			return addMetricsDescriptions(conn, metricsId, metricsDescriptions);
		else return false;
	}
	
	public boolean deleteMetricsNames(Connection conn, String metricsId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFMETRICSNAME where CFMETRICSID like '" + metricsId + "'");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete metricsNames: " + metricsId );
		}
		return false;
	}
	
	public boolean deleteMetricsDescriptions(Connection conn, String metricsId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFMETRICSDESCRIPTION where CFMETRICSID like '" + metricsId + "'");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete metricsDescriptions: " + metricsId );
		}
		return false;
	}
	
	public boolean addMetricsClass(Connection conn, String metricsId, String schemeId, String classId, Calendar startDate, Calendar endDate) {
		try {
			if((schemeId == null) || (classId == null))
				return true;
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFMETRICS_CLASS (CFMETRICSID, CFCLASSSCHEMEID, CFCLASSID, CFSTARTDATE, CFENDDATE) values (?, ?, ?, ?, ?)");
			stmt.setString(1, metricsId);
			stmt.setString(2, schemeId);
			stmt.setString(3, classId);
			stmt.setDate(4, new java.sql.Date(startDate.getTime()
					.getTime()));
			stmt.setDate(5, new java.sql.Date(endDate.getTime()
					.getTime()));
			
			stmt.executeUpdate();
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add metricsClass: " + metricsId + " and " + schemeId + " | " + classId);
		}
		return false;
	}
	
	public boolean updateMetricsClass(Connection conn, String metricsId, String schemeId, String classId, Calendar startDate, Calendar endDate) {
		if(deleteMetricsClass(conn, metricsId, schemeId, classId))
			return addMetricsClass(conn, metricsId, schemeId, classId, startDate, endDate);
		else 
			return false;
	}
	
	public boolean deleteMetricsClass(Connection conn, String metricsId, String schemeId, String classId) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFMETRICS_CLASS where CFMETRICSID like '" + metricsId + "' " +
															"and CFCLASSSCHEMEID like '" + schemeId + "' and CFCLASSID like '" + classId + "' ");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete metricsClass: " + metricsId + " | " + schemeId + " | " + classId);
		}
		return false;
	}

	public List<ImpactFactor> getJournalImpactFactors(Connection conn, String recordId, List<String> metricsIds) {
		List<ImpactFactor> retVal = new ArrayList<ImpactFactor>();
		for (String metricsId: metricsIds) {
			List<ImpactFactor> temp = getJournalImpactFactors(conn, recordId, metricsId);
			if(temp != null){
				for (ImpactFactor impFac:temp) {
					if(retVal.contains(impFac)){
						ImpactFactor mergeIF = retVal.get(retVal.indexOf(impFac));
						if(mergeIF.getValueOfImpactFactor() == null){
							mergeIF.setValueOfImpactFactor(impFac.getValueOfImpactFactor());
							mergeIF.getResearchAreas().addAll(impFac.getResearchAreas());
						}
						if(mergeIF.getValueOfImpactFactorFiveYears() == null){
							mergeIF.setValueOfImpactFactorFiveYears(impFac.getValueOfImpactFactorFiveYears());
							mergeIF.getResearchAreasFiveYears().addAll(impFac.getResearchAreasFiveYears());
						}
					} else {
						retVal.add(impFac);
					}
				}
			}
				retVal.addAll(temp);
		}
		return retVal;
	}
	
	private List<ImpactFactor> getJournalImpactFactors(Connection conn, String recordId, String metricsId) {
		List<ImpactFactor> retVal = new ArrayList<ImpactFactor>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select distinct CFYEAR from MARC21RECORD_METRICS where RECORDID like '" + recordId + "' and CFMETRICSID like '" + metricsId + "'");
			while (rset.next()) {
				Integer year = rset.getInt(1);
				
				ImpactFactor impactFactor = getJournalImpactFactor(conn, recordId, metricsId, year);
				retVal.add(impactFactor);
			}
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read impact factors ");
			log.fatal(ex);
			return null;
		}
	}
	
	public ImpactFactor getJournalImpactFactor(Connection conn,
			String recordId, String metricsId, Integer year) {
		ImpactFactor retVal = new ImpactFactor();
		try {
			retVal.setYear(year);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt
					.executeQuery("select CFCOUNT from MARC21RECORD_METRICS where RECORDID like '" + recordId + "' and CFMETRICSID like '" + metricsId + "'" 
												+ " and CFYEAR=" + year + " and CFCLASSSCHEMEID like 'value of metric' and CFCLASSID like 'value of IF'");
			if (rset.next()) {
				Double count = rset.getDouble(1);
				if(metricsId.equals("twoYearsIF"))
					retVal.setValueOfImpactFactor(count);
				else if(metricsId.equals("fiveYearsIF"))
					retVal.setValueOfImpactFactorFiveYears(count);
				rset.close();
				rset = stmt
				.executeQuery("select CFCOUNT, CFFRACTION, CFCLASSSCHEMEID, CFCLASSID from MARC21RECORD_METRICS where RECORDID like '" + recordId + "' and CFMETRICSID like '" + metricsId + "'" 
											+ " and CFYEAR=" + year + " and (CFCLASSSCHEMEID like 'researchArea' or CFCLASSID like 'wos%')");
				ResearchAreaDB radb = new ResearchAreaDB();
				List<ResearchAreaRanking> rarlist = new ArrayList<ResearchAreaRanking>();
				while (rset.next()){
					ResearchAreaRanking rar = new ResearchAreaRanking();
					rar.setPosition(rset.getDouble(1));
					rar.setDividend(rset.getDouble(2));
					rar.setResearchAreaDTO(radb.getResearchArea(conn, rset.getString(3), rset.getString(4), allResearchAreas));
					rarlist.add(rar);
				}
				if(metricsId.equals("twoYearsIF"))
					retVal.setResearchAreas(rarlist);
				else if(metricsId.equals("fiveYearsIF"))
					retVal.setResearchAreasFiveYears(rarlist);
			} else {
				rset.close();
				stmt.close();
				return null;
			}
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read impact factors ");
			log.fatal(ex);
			return null;
		}
	}

	public boolean addResultMetrics(Connection conn, String recordId, String metricsId, String schemeId, String classId, Integer year, Double count, Double fraction) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into MARC21RECORD_METRICS (RECORDID, CFMETRICSID, CFCLASSSCHEMEID, CFCLASSID, CFYEAR, CFCOUNT, CFFRACTION) values (?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, recordId);
			stmt.setString(2, metricsId);
			stmt.setString(3, schemeId);
			stmt.setString(4, classId);
			stmt.setInt(5, year);
			stmt.setDouble(6, count);
			if(fraction != null)
				stmt.setDouble(7, fraction);
			else
				stmt.setNull(7, Types.DOUBLE);
			
			
			stmt.executeUpdate();
			stmt.close();
			
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add resultMetrics: " + recordId + " and " + metricsId);
		}
		return false;
	}
	
	public boolean updateResultMetrics(Connection conn, String recordId, String metricsId, String schemeId, String classId, Integer year, Double count, Double fraction) {
		if(deleteResultMetrics(conn, recordId, metricsId, schemeId, classId, year))
			return addResultMetrics(conn, recordId, metricsId, schemeId, classId, year, count, fraction);
		else 
			return false;
	}
	
	public boolean deleteResultMetrics(Connection conn, String recordId, String metricsId, String schemeId, String classId, Integer year) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from MARC21RECORD_METRICS where RECORDID like '" + recordId + "' and CFMETRICSID like '" + metricsId + "' " +
															"and CFCLASSSCHEMEID like '" + schemeId + "' and CFCLASSID like '" + classId + "' and CFCLASSID = " + year);
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete resultMetrics: " + recordId + " | " + metricsId + " | " + schemeId + " | " + classId + " | " + year);
		}
		return false;
	}
	
	public boolean deleteResultMetricsAll(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from MARC21RECORD_METRICS ");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete deleteResultMetricsAll: ");
		}
		return false;
	}
	
	/**
	 * Adds a new metrics to the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param metricsDTO
	 *            metrics to add
	 * @return true if successful else false
	 */
	public boolean addMetrics(Connection conn, MetricsDTO metricsDTO) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("insert into CFMETRICS (CFMETRICSID, CFURI) values (?, ?)");
			stmt.setString(1, metricsDTO.getMetricsId());
			stmt.setString(2, metricsDTO.getUri());
			
			stmt.executeUpdate();
			stmt.close();
			
			if(addMetricsNames(conn, metricsDTO.getMetricsId(), metricsDTO.getAllNames()))
				if(addMetricsDescriptions(conn, metricsDTO.getMetricsId(), metricsDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot add metricsDTO: " + metricsDTO);
		}
		return false;
	}

	/**
	 * Updates the metrics in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param metricsDTO
	 *            metrics to update
	 * @return true if successful else false
	 */
	public boolean updateMetrics(Connection conn, MetricsDTO metricsDTO) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("update CFMETRICS set CFURI=? where CFMETRICSID=?");
			
			stmt.setString(1, metricsDTO.getUri());
			stmt.setString(2, metricsDTO.getMetricsId());
			
			stmt.executeUpdate();
			stmt.close();
			
			if(updateMetricsNames(conn, metricsDTO.getMetricsId(), metricsDTO.getAllNames()))
				if(updateMetricsDescriptions(conn, metricsDTO.getMetricsId(), metricsDTO.getAllDescriptions()))
					return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot update metrics: " + metricsDTO);
		}
		return false;
	}

	/**
	 * Deletes the metrics from the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param metricsDTO
	 *            metrics
	 * @return true if successful
	 */
	public boolean deleteMetrics(Connection conn, MetricsDTO metricsDTO) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from CFMETRICS where CFMETRICSID like '" + metricsDTO.getMetricsId() + "'");
			stmt.close();
			return true;
		} catch (SQLException e) {
			log.fatal(e);
			log.fatal("Cannot delete metrics: " + metricsDTO);
		}
		return false;
	}

	private static Log log = LogFactory.getLog(MetricsDB.class.getName());
	
	private static Map<String, ResearchAreaDTO> allResearchAreas = new HashMap<String, ResearchAreaDTO>();

}
