/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.db;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dto.AuthorDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.AutocitationDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographEvaluationDataDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author bdimic@uns.ac.rs
 *
 */
public class EvaluationDataDB {
		
	public FileDB fileDB = new FileDB();
	
	
	public MonographEvaluationDataDTO getEvaluationDataForMonograph(Connection conn, MonographDTO monograph){
		MonographEvaluationDataDTO retVal = null;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select HASREVIEWININTERNATIONALJOURNAL, NUMBEROFREVIEWERS, MONOGRAPHRECORDID from " +
					"MONOGRAPHEVALUATIONDATA where MONOGRAPHRECORDID like '"+
					monograph.getControlNumber()+"'");
			if(rs.next()){				
				String hasReviewInInternationalJournal = rs.getString(1);
				int numberOfReviewers = rs.getInt(2);
				retVal = new MonographEvaluationDataDTO(monograph);
				retVal.setHasReviewInInternationalJournal(hasReviewInInternationalJournal);
				retVal.setNumberOfReviewers(numberOfReviewers);
				retVal.setAutocitations(getAutocitationsForMonograph(conn, monograph));
				List<FileDTO> files = FileDB.getFilesByRecordControlNumber(conn, monograph.getControlNumber());
				retVal.setAttachedFiles(files);
				
				
			}
		}catch(Exception ex){
			log.fatal("Cannot read evaluation data for monograph controlNumber="+monograph.getControlNumber());
			log.fatal(ex);
		}
		
		return retVal;
	}
	
	
	public boolean insertMonographEvaluationData(Connection conn, MonographEvaluationDataDTO monEval){
		boolean retVal = false;
		try{
			PreparedStatement stmt = conn.prepareStatement("insert into MONOGRAPHEVALUATIONDATA(MONOGRAPHRECORDID," +
					" HASREVIEWININTERNATIONALJOURNAL, NUMBEROFREVIEWERS) values(?,?,?)" );
			if(monEval.getMonograph().getControlNumber()==null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, monEval.getMonograph().getControlNumber());			
			if(monEval.getHasReviewInInternationalJournal()==null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2, monEval.getHasReviewInInternationalJournal());
			stmt.setInt(3, monEval.getNumberOfReviewers());			
			if(stmt.executeUpdate()>0){
				retVal = true;
				for(AutocitationDTO autocitation:monEval.getAutocitations()){
					if(!insertAutocitationForMonograph(conn, autocitation)){
						retVal = false;
						break;
					}					
				}				
			}
			
			if(retVal){
				if((monEval.getAttachedFiles()!=null) && (monEval.getAttachedFiles().size()>0)){				
				for(FileDTO file:monEval.getAttachedFiles()){
					if(FileDB.getFileByID(conn, file.getId())==null){
					file.setControlNumber(monEval.getMonograph().getControlNumber());
					if(fileDB.add(conn, file))
						if(FileStorage.add(file))
							retVal = true;
					}					
				}
				}
			}			
			
			// TODO add files
			stmt.close();			
		}catch(SQLException ex){
			log.fatal("Cannot add monograph evaluation data for monograph controlNimber="+monEval.getMonograph().getControlNumber());
			log.fatal(ex);			
			ex.printStackTrace();
		}		
		return retVal; 
	}
	
	
	public boolean updateMonographEvaluationData(Connection conn, MonographEvaluationDataDTO monEval){	
		try{
			// delete + insert
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from AUTOCITATION where EVALUATEDRECORDID like '"+monEval.getMonograph().getControlNumber()+"'");
			stmt.executeUpdate("delete from MONOGRAPHEVALUATIONDATA where MONOGRAPHRECORDID like '"+monEval.getMonograph().getControlNumber()+"'");		
			stmt.close();		
			return insertMonographEvaluationData(conn, monEval);
			
		}catch(Exception e){
			log.fatal("Cannot update monograph evaluation data for monograph controlNumber="+monEval.getMonograph().getControlNumber());
			log.fatal(e);
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean insertAutocitationForMonograph(Connection conn, AutocitationDTO autocitation){
		boolean retVal = false;
		try{
			PreparedStatement stmt = conn.prepareStatement("insert into AUTOCITATION (EVALUATEDRECORDID, RESEARCHERRECORDID, AUTOCITATIONM20NUM, " +
					"AUTOCITATIONM50NUM) values (?,?,?,?)");
			if(autocitation.getInPublication().getControlNumber()==null)
				stmt.setNull(1, Types.VARCHAR);
			else
				stmt.setString(1, autocitation.getInPublication().getControlNumber());
			if(autocitation.getForResearcher().getControlNumber()==null)
				stmt.setNull(2, Types.VARCHAR);
			else
				stmt.setString(2,autocitation.getForResearcher().getControlNumber());
			stmt.setInt(3, autocitation.getNumberOfAutocitationM20());
			stmt.setInt(4, autocitation.getNumberOfAutocitationM50());
			if(stmt.executeUpdate()>0)
				retVal = true;
			stmt.close();			
		}catch(SQLException ex){
			log.fatal("Cannot add autocitation evaluation data for publication controlNimber="+autocitation.getInPublication().getControlNumber());
			log.fatal(ex);
		}
		
		return retVal;
	}
	
	private List<AutocitationDTO> getAutocitationsForMonograph(Connection conn, MonographDTO monograph){
		List<AutocitationDTO> retVal = null;
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select RESEARCHERRECORDID, AUTOCITATIONM20NUM, AUTOCITATIONM50NUM from " +
					"AUTOCITATION where EVALUATEDRECORDID like '"+monograph.getControlNumber()+"'");
			while(rs.next()){
				if(retVal ==null)
					retVal = new ArrayList<AutocitationDTO>();
				String researchRecordId = rs.getString(1);
				int autocitationM20 = rs.getInt(2);
				int autocitationM50 = rs.getInt(3);
				AuthorDTO forAuthor = (AuthorDTO) new RecordDB().getRecord(conn, researchRecordId).getDto();
				AutocitationDTO autocitation = new AutocitationDTO(monograph,forAuthor);
				autocitation.setNumberOfAutocitationM20(autocitationM20);
				autocitation.setNumberOfAutocitationM50(autocitationM50);
				retVal.add(autocitation);				
			}
		}catch(Exception e){
			log.fatal("Cannot read autocitation for monograph controlNumber="+monograph.getControlNumber());
			log.fatal(e);			
		}	
		return retVal;		
	}
	
	
//	private boolean updateFiles(Connection conn, MonographEvaluationDataDTO monEval){
//		FileDB fileDB = new FileDB();
//		List<FileDTO> oldFiles = FileDB.getFilesByRecordControlNumber(conn, monEval.getMonograph().getControlNumber());	
//		for(FileDTO file:oldFiles){
//			if(!monEval.getAttachedFiles().contains(file)){
//				if(fileDB.delete(conn, file))
//					FileStorage.delete(file);
//			}				
//		}
//		
		
		
//		return false;
//	}
	
	
	public boolean deleteAttachedFile(Connection conn, FileDTO file){
		FileDB fileDB = new FileDB();
		if (fileDB.delete(conn, file))
			return FileStorage.delete(file);		
		return false;
	}
	
	
	private static Log log = LogFactory.getLog(EvaluationDataDB.class.getName());
	
}
