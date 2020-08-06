/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportInstitutionsFromKNRTask implements Task {

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	public ImportInstitutionsFromKNRTask(Connection source) {
		this.source = source;
	}
	
	/**
	 * @see rs.ac.uns.ftn.informatika.bibliography.backup.Task#execute()
	 */
	@Override
	public boolean execute() {
		boolean retVal = true;
		try {
			StringBuffer query = new StringBuffer();
			query.append("select ID, SUPERID, NAME, PLACE, CONTROLNUMBER from INSTITUTION");
			Statement stmt = source.createStatement();
			PreparedStatement pstmt = source
				.prepareStatement("update INSTITUTION set CONTROLNUMBER=? where ID=?");
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				InstitutionDTO institution = null;
				String controlNumber = rset.getString(5);
				if((controlNumber.startsWith("(BISIS)"))){
					institution = (InstitutionDTO)recordDAO.getDTO(controlNumber);
				} else {
					institution = new InstitutionDTO();
				}				
				institution.getName().setContent(rset.getString(3));
				institution.setPlace(rset.getString(4));
				institution.setSuperInstitution(loadSuperInstitution(rset.getInt(2)));
				if((controlNumber.startsWith("(BISIS)"))){
					if (recordDAO.update(new Record(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
							institution)) == false) {
						retVal = false;
						break;
					} else {
						System.out.println("ID: " + rset.getInt(1) + "; ControlNumber" + institution.getControlNumber());
						System.out.println("Institution: " + institution.getRecord());
					}
				}
				else {
					if (recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
										institution)) == false) {
					retVal = false;
					break;
					} else {
						System.out.println("ID: " + rset.getInt(1) + "; ControlNumber" + institution.getControlNumber());
						System.out.println("Institution: " + institution.getRecord());
						pstmt.setInt(2, rset.getInt(1));
						pstmt.setString(1, institution.getControlNumber());
						pstmt.executeUpdate();
						//System.out.println("Number of changed rows: " + pstmt.executeUpdate());
					}
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			log.fatal("Cannot read multiple institutions");
			ex.printStackTrace();
			log.fatal(ex);
			return false;
		}
	}
	
	private InstitutionDTO loadSuperInstitution(int superID) {
		InstitutionDTO retVal = null;
		try {
			PreparedStatement pstmt = source
			.prepareStatement("select CONTROLNUMBER from INSTITUTION where ID = ?");
			pstmt.setInt(1, superID);
			ResultSet rset = pstmt.executeQuery();
			rset.next();
			retVal = (InstitutionDTO) recordDAO.getDTO(rset.getString(1));
		} catch (Exception e) {
		}
		return retVal;
	}
	
	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportAuthorsFromKNRTask.class.getName());

}
