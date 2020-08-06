package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDataDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MonographEvaluationDataDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;



public class EvaluationDataDAO {
	
	static{
		EvaluationDataDAO.log = LogFactory.getLog(EvaluationDataDAO.class.getName());
		EvaluationDataDAO.dataSource = DataSourceFactory.getDataSource();
		
	}
	
	public EvaluationDataDAO(EvaluationDataDB storage){
		this.storage = storage;
	}
	
	public MonographEvaluationDataDTO getEvaluationDataFromDatabase(MonographDTO monograph){
		Connection conn = null;
		MonographEvaluationDataDTO retVal = null;
		try {
			conn = EvaluationDataDAO.dataSource.getConnection();
			retVal = storage.getEvaluationDataForMonograph(conn, monograph);
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
	
	
	public boolean addMonographEvaluationData(MonographEvaluationDataDTO monEval){
		Connection conn = null;
		boolean retVal = false;
		try{
			conn = EvaluationDataDAO.dataSource.getConnection();
			if(storage.insertMonographEvaluationData(conn, monEval) == false){
				retVal = false;
				conn.rollback();
			}else{
				conn.commit();
			}				
		}catch(SQLException ex){
			log.fatal(ex);
			retVal = false;
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					log.fatal(e);
				}
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public boolean updateMonographEvaluationData(MonographEvaluationDataDTO monEval) {
		Connection conn = null;
		boolean retVal = true;
		try {
			conn = EvaluationDataDAO.dataSource.getConnection();
			if (storage.updateMonographEvaluationData(conn, monEval) == false) {
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
		
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
		return retVal;
	}
	
	public boolean deleteAttachedFile(FileDTO file){
		Connection conn = null;
		boolean retVal = true;
		try{
			conn = EvaluationDataDAO.dataSource.getConnection();
			if(storage.deleteAttachedFile(conn, file)== false){
				retVal = false;
				conn.rollback();
			}else{
				conn.commit();
			}			
		}catch(SQLException e){
			log.fatal(e);
			retVal = false;
			if(conn!=null){
				try{
					conn.rollback();
				}catch(SQLException ex){
					log.fatal(ex);
				}
			}
		}
		return retVal;
	}
	
	
	protected static Log log;
	
	protected static DataSource dataSource;
	
	protected EvaluationDataDB storage;


}
