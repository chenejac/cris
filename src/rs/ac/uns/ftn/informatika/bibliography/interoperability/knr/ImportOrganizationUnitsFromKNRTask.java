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
import rs.ac.uns.ftn.informatika.bibliography.dto.OrganizationUnitDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportOrganizationUnitsFromKNRTask implements Task {

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	public ImportOrganizationUnitsFromKNRTask(Connection source) {
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
			query.append("select ID, SUPERID, INSTITUTIONID, NAME, CONTROLNUMBER from ORGANIZATIONUNIT where INSTITUTIONID is  null");
			Statement stmt = source.createStatement();
			PreparedStatement pstmt = source
				.prepareStatement("update ORGANIZATIONUNIT set CONTROLNUMBER=? where ID=?");
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				OrganizationUnitDTO organizationUnit = null;
				String controlNumber = rset.getString(5);
				if(controlNumber == null)
					controlNumber = "";
				if((controlNumber.startsWith("(BISIS)"))){
					Record temp = new Record();
					temp.getMARC21Record().setControlNumber(controlNumber);
					recordDAO.delete(temp);
					controlNumber = "";
					organizationUnit = (OrganizationUnitDTO)recordDAO.getDTO(controlNumber);
				} 
				else {
					organizationUnit = new OrganizationUnitDTO();
				}				
				organizationUnit.getName().setContent(rset.getString(4));
				organizationUnit.setSuperOrganizationUnit(loadSuperOrganizationUnit(rset.getInt(2)));
//				organizationUnit.setInstitution(loadInstitution(rset.getInt(3)));
				if((controlNumber.startsWith("(BISIS)"))){
					if (recordDAO.update(new Record(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
							organizationUnit)) == false) {
						retVal = false;
						break;
					} else {
						System.out.println("ID: " + rset.getInt(1) + "; ControlNumber" + organizationUnit.getControlNumber());
						System.out.println("OrganizationUnit: " + organizationUnit.getRecord());
					}
				}
				else {
					if (recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.ORGANIZATION_UNIT, 
										organizationUnit)) == false) {
					retVal = false;
					break;
					} else {
						System.out.println("ID: " + rset.getInt(1) + "; ControlNumber" + organizationUnit.getControlNumber());
						System.out.println("OrganizationUnit: " + organizationUnit.getRecord());
						pstmt.setInt(2, rset.getInt(1));
						pstmt.setString(1, organizationUnit.getControlNumber());
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
			log.fatal("Cannot read multiple organizationUnits");
			ex.printStackTrace();
			log.fatal(ex);
			return false;
		}
	}
	
	private OrganizationUnitDTO loadSuperOrganizationUnit(int superID) {
		OrganizationUnitDTO retVal = null;
		try {
			PreparedStatement pstmt = source
			.prepareStatement("select CONTROLNUMBER from ORGANIZATIONUNIT where ID = ?");
			pstmt.setInt(1, superID);
			ResultSet rset = pstmt.executeQuery();
			rset.next();
			retVal = (OrganizationUnitDTO) recordDAO.getDTO(rset.getString(1));
		} catch (Exception e) {
		}
		return retVal;
	}
	
	@SuppressWarnings("unused")
	private InstitutionDTO loadInstitution(int institutionID) {
		InstitutionDTO retVal = null;
		try {
			PreparedStatement pstmt = source
			.prepareStatement("select CONTROLNUMBER from INSTITUTION where ID = ?");
			pstmt.setInt(1, institutionID);
			ResultSet rset = pstmt.executeQuery();
			rset.next();
			retVal = (InstitutionDTO) recordDAO.getDTO(rset.getString(1));
		} catch (Exception e) {
		}
		return retVal;
	}
	
	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportOrganizationUnitsFromKNRTask.class.getName());

}
