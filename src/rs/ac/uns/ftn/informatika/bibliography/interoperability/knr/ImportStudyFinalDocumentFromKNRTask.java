/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.interoperability.knr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.InstitutionDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ImportStudyFinalDocumentFromKNRTask implements Task {

	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	
	public ImportStudyFinalDocumentFromKNRTask(Connection source) {
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
			query
					.append("select t.RESULTID, t.TITLE, t.INSTITUTIONNAME, t.INSTITUTIONPLACE, t.PUBLICATIONYEAR, a.CONTROLNUMBER, t.R, t.CONTROLNUMBER from THESIS t, AUTHOR a where a.AUID = t.AUID and t.CONTROLNUMBER not like '(BISIS)%'");
			Statement stmt = source.createStatement();
			PreparedStatement pstmt = source
				.prepareStatement("update THESIS set CONTROLNUMBER=? where RESULTID=?");
			ResultSet rset = stmt.executeQuery(query.toString());
			while (rset.next()) {
				try {
					String title = rset.getString(2);
					String institutionName = rset.getString(3);
					String institutionPlace = rset.getString(4);
					Integer publicationYear = rset.getInt(5);
					Calendar publicationDate = new GregorianCalendar();
					publicationDate.set(Calendar.YEAR, publicationYear);
					publicationDate.set(Calendar.DAY_OF_YEAR, 1);
					String authorControlNumber = rset.getString(6);
					String r = rset.getString(7);
					String controlNumber = rset.getString(8);
					StudyFinalDocumentDTO studyFinalDocumentDTO = null;
					if((controlNumber.startsWith("(BISIS)"))){
						studyFinalDocumentDTO = (StudyFinalDocumentDTO)recordDAO.getDTO(controlNumber);
					} else {
						studyFinalDocumentDTO = new StudyFinalDocumentDTO();
					}
					AuthorDTO author = (AuthorDTO)recordDAO.getDTO(authorControlNumber);
					if(author==null)
						throw new Exception("Nema autora!!!");
					else
						studyFinalDocumentDTO.setAuthor(author);
					if ((title == null) || (title.trim().equals(""))){
						throw new Exception("Nema naslova!!!");
					}
					studyFinalDocumentDTO.getTitle().setContent(title.trim());
					studyFinalDocumentDTO.setPublicationDate(publicationDate);
					if("81".equals(r))
						studyFinalDocumentDTO.setStudyType("records.studyFinalDocument.editPanel.studyType.phd");
					else if("82".equals(r))
						studyFinalDocumentDTO.setStudyType("records.studyFinalDocument.editPanel.studyType.oldMaster");
					
					
					studyFinalDocumentDTO.setInstitution(loadInstitution(institutionName, institutionPlace));
					
					if((controlNumber.startsWith("(BISIS)"))){
						if (recordDAO.update(new Record(null, null, "knr", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
								studyFinalDocumentDTO)) == false) {
							retVal = false;
							System.out.println("Neuspesno!!!");
							break;
						} else {
							//System.out.println("NRID: " + rset.getInt(1) + "; ControlNumber" + author.getControlNumber());
//							pstmt.setInt(2, rset.getInt(1));
//							pstmt.setString(1, author.getControlNumber());
//							pstmt.executeUpdate();
							System.out.println("Uspesno!!!");
							//System.out.println("Number of changed rows: " + pstmt.executeUpdate());
						}
					} else {if ((recordDAO.add(new Record("knr", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
							studyFinalDocumentDTO)) == false)){
								retVal = false;
								break;
							} else {
								System.out.println("thesisAdded: " + studyFinalDocumentDTO.getStringRepresentation());
								pstmt.setInt(2, rset.getInt(1));
								pstmt.setString(1, studyFinalDocumentDTO.getControlNumber());
								pstmt.executeUpdate();
							}
					}
				} catch (Exception e){
					pstmt.setInt(2, rset.getInt(1));
					pstmt.setString(1, e.toString());
					pstmt.executeUpdate();
					System.out.println(e.toString());
					log.info(e);
				}
			}
			recordDAO.optimizeIndexer();
			rset.close();
			stmt.close();
			return retVal;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal("Cannot read multiple thesis");
			log.fatal(ex);
			return false;
		}
	}
	
	private InstitutionDTO loadInstitution(String institutionName, String institutionPlace) throws Exception{
		InstitutionDTO retVal = null;
		try {
			PreparedStatement pstmt = source
			.prepareStatement("select CONTROLNUMBER from INSTITUTION where NAME like ? and PLACE like ?");
			pstmt.setString(1, institutionName);
			pstmt.setString(2, institutionPlace);
			ResultSet rset = pstmt.executeQuery();
			rset.next();
			retVal = (InstitutionDTO) recordDAO.getDTO(rset.getString(1));
		} catch (Exception e) {
			throw e;
		}
		if(retVal == null)
			throw new Exception("Nije pronadjena institucija: " + institutionName);
		else
			return retVal;
	}

	
	private Connection source;
	
	private static Log log = LogFactory.getLog(ImportJournalPapersFromKNRTask.class.getName());

}
