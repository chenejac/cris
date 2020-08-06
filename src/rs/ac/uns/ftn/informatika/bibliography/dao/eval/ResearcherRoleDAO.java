package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.CERIFSemanticLayerDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class ResearcherRoleDAO {

	static {
		ResearcherRoleDAO.log = LogFactory.getLog(ResearcherRoleDAO.class.getName());
		ResearcherRoleDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public ClassDTO getResearcherRole(String schemeResearcherRole, String classResearcherRole) {
		Connection conn = null;
		ClassDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getClassDTO(conn, schemeResearcherRole, classResearcherRole);
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
	
	public List<ClassDTO> getAllResearcherRole() {
		Connection conn = null;
		List<ClassDTO> retVal = new ArrayList<ClassDTO>();
		try {
			conn = dataSource.getConnection();
			for (String schemeResearcherRole : listSchemaId) {
				retVal.addAll(storage.getAllClassDTO(conn, schemeResearcherRole));
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
		return retVal;	
	}
	
	public static String [] listSchemaId = {"authorshipType", "authorInstitutionRelation", "authorOrganisationRelation", "authorEventRelation"};
	protected static DataSource dataSource;
	protected CERIFSemanticLayerDB storage = new CERIFSemanticLayerDB();
	protected static Log log;
}
