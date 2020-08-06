package rs.ac.uns.ftn.informatika.bibliography.dao.eval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.dao.DataSourceFactory;
import rs.ac.uns.ftn.informatika.bibliography.db.FileDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * Class for persisting and retrieving data about bibliographic and authority
 * records from database and lucene index.
 * 
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class FileDAO {


	static {
		FileDAO.log = LogFactory.getLog(FileDAO.class.getName());
		FileDAO.dataSource = DataSourceFactory.getDataSource();
	}
	
	public List<FileDTO> getAllFilesByRecordFromDatabase(RecordDTO recordDTO){
		Connection conn = null;
		List<FileDTO> retVal = new ArrayList<FileDTO>();
		try {
			conn = dataSource.getConnection();
			retVal.addAll(storage.getFilesByRecordControlNumber(conn, recordDTO.getControlNumber()));
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
	
	@SuppressWarnings("static-access")
	public FileDTO getFileFromDatabase(int fileID){
		Connection conn = null;
		FileDTO retVal = null;
		try {
			conn = dataSource.getConnection();
			retVal = storage.getFileByID(conn, fileID);
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
	 * Adds a new Files to the database.
	 * 
	 * @param List of FileDTO
	 *            Files to add
	 * @return true if successful else false
	 */
	@SuppressWarnings("static-access")
	public boolean add(List<FileDTO> files) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (FileDTO file : files) {
				if(storage.getFileByID(conn, file.getId())==null){
					if( file.getData() == null || file.getData().length == 0)
						retVal = false;
					if(retVal==false)
						break;
					retVal &= storage.add(conn, file);
					if(retVal==false)
						break;
					retVal &= FileStorage.add(file);
					if(retVal==false)
						break;
				}
			}
			if (retVal == false) {
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
	
	/**
	 * Adds a new File to the database.
	 * 
	 * @param FileDTO
	 *            File to add
	 * @return true if successful else false
	 */
	public boolean add(FileDTO file) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			
			if( file.getData() == null || file.getData().length == 0)
				retVal = false;	
			if(retVal==true)
				retVal &= storage.add(conn, file);
			if(retVal==true)
				retVal &= FileStorage.add(file);
			
			if (retVal == false) {
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
	
	/**
	 * Updates the Files in the database.
	 * 
	 * @param conn
	 *            Database connection
	 * @param List of FileDTO
	 *            List of FileDTO  to update
	 * @return true if successful else false
	 */
	
	public boolean update(RecordDTO recordDTO, List<FileDTO> files) {
		boolean retVal = true;
		Connection conn = null;
		try {
			
//			System.out.println("FileDAO-update-1");
			
			conn = dataSource.getConnection();
			List<FileDTO> allFilesToAdd = new ArrayList<FileDTO>();
			List<FileDTO> allFilesToDelete = new ArrayList<FileDTO>();
			allFilesToDelete.addAll(storage.getFilesByRecordControlNumber(conn, recordDTO.getControlNumber()));
			
//			System.out.println("FileDAO-update-2");
//			System.out.println("broj iz baze je:"+allFilesToDelete.size());
			
			
			//filtriranje liste na nove i za brisanje
			for (FileDTO file : files) {
				boolean found = false;
				for (int i = 0; i < allFilesToDelete.size(); i++) {
					if(file.getId()==allFilesToDelete.get(i).getId()){
						allFilesToDelete.remove(i);
						found = true;
						break;
					}
				}
				if(found == false){
					allFilesToAdd.add(file);
				}
			}
			
//			System.out.println("FileDAO-update-3");
//			System.out.println("Broj za brisanje u bazi je:"+allFilesToDelete.size());
//			System.out.println("Broj za dodavanje u bazi je:"+allFilesToAdd.size());
			
			for (FileDTO file : allFilesToDelete) {
				retVal &= storage.delete(conn, file);
				if(retVal==false)
					break;
				retVal &= FileStorage.delete(file);
				if(retVal==false)
					break;
			}
			
//			System.out.println("FileDAO-update-4 "+retVal);
			
			if(retVal==true){
				for (FileDTO file : allFilesToAdd) {
					if( file.getData() == null || file.getData().length == 0)
						retVal = false;
//					System.out.println("FileDAO-update-4.1 "+retVal);
					if(retVal==false)
						break;
					retVal &= storage.add(conn, file);
//					System.out.println("FileDAO-update-4.2 "+retVal);
					if(retVal==false)
						break;
					retVal &= FileStorage.add(file);
//					System.out.println("FileDAO-update-4.3 "+retVal);
					if(retVal==false)
						break;
				}
			}
			
//			System.out.println("FileDAO-update-5 "+retVal);
			
			if (retVal == false) {
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
	
	/**
	 * Deletes all Files by RecordID from the database.
	 * 
	 * @param RecordDTO
	 *            RecordDTO which files should be deleted
	 * @return true if successful else false
	 */
	public boolean deleteAllFilesByRecordID(RecordDTO recordDTO) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<FileDTO> allFiles = new ArrayList<FileDTO>();
			allFiles.addAll(storage.getFilesByRecordControlNumber(conn, recordDTO.getControlNumber()));
			retVal &= storage.delete(conn, recordDTO.getControlNumber());
			if(retVal==true){
				for (FileDTO file : allFiles) {
					retVal &= FileStorage.delete(file);
					if(retVal==false)
						break;
				}
			}
			if (retVal == false) {
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
	
	/**
	 * Deletes a File from the database.
	 * 
	 * @param FileDTO
	 *            FileDTO which should be deleted
	 * @return true if successful else false
	 */
	public boolean delete(FileDTO file) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			retVal &= storage.delete(conn, file);
			if(retVal==true)
				retVal &= FileStorage.delete(file);
			
			if (retVal == false) {
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
	
	
	/**
	 * Deletes a File from the database.
	 * 
	 * @param fileID
	 *            fileID which FileDTO should be deleted
	 * @return true if successful else false
	 */
	@SuppressWarnings("static-access")
	public boolean delete(int fileID) {
		boolean retVal = true;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			FileDTO file = null;
			file = storage.getFileByID(conn, fileID);
			
			retVal &= storage.delete(conn, fileID);
			if(retVal==true)
				retVal &= FileStorage.delete(file);
			
			if (retVal == false) {
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

	
	protected static DataSource dataSource;
	protected FileDB storage = new FileDB();
	protected static Log log;
}
