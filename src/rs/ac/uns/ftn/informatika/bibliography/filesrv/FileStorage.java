package rs.ac.uns.ftn.informatika.bibliography.filesrv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileStorage {

	public static boolean add(FileDTO fileDTO) {
	    boolean retVal = false;
		String fullPath = getFullPath(fileDTO);
	    File file = new File(fullPath);
	    if (file.isFile()) {
	      log.warn("FileDTO " + fullPath + " exists: will be overwritten!");
	    }
	    File dir = file.getParentFile();
	    if (!dir.exists()) {
	      dir.mkdirs();
	      log.info("Creating directory " + dir.getAbsolutePath());
	    }
	    try {
	      InputStream input = new ByteArrayInputStream(fileDTO.getData());
	      BufferedOutputStream out = new BufferedOutputStream(
	          new FileOutputStream(file)); 
	      IOUtils.copy(input, out);
	      out.close();
	      retVal = true;
	    } catch (IOException ex) {
	      log.fatal(ex);
	    }
	    return retVal;
	}

	public static InputStream get(FileDTO fileDTO) {
	    String fullPath = getFullPath(fileDTO);
	    File file = new File(fullPath);
	    if (!file.isFile()) {
	      log.warn("FileDTO " + fullPath + " does not exist!");
	      return null;
	    }
	    try {
	      BufferedInputStream in = new BufferedInputStream(
	          new FileInputStream(file));
	      return in;
	    } catch (Exception ex) {
	      log.fatal(ex);
	      return null;
	    }
	}
	
	public static byte[] getByteArray(FileDTO fileDTO) {
	    InputStream is = FileStorage.get(fileDTO);
	    try {
	    if(is != null)
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
		}
	    	return null;
	}
  
	public static boolean delete(FileDTO fileDTO){
		String fullPath = getFullPath(fileDTO);
		File file = new File(fullPath);
		if (!file.isFile()) {
			log.warn("FileDTO " + fullPath + " does not exist!");
			return false;
		}else{
			file.delete();   	
			return true;
		}
	}
   
	public static boolean deleteFolderForFiles(FileDTO fileDTO){
		String dirPath = storageRoot + "/" + "set" + getFileset(fileDTO) + "/" +fileDTO.getControlNumber();
		File file = new File(dirPath);
		return file.delete();
	}
  
	public static String getFullPath(FileDTO fileDTO) {
		return storageRoot + "/" + "set" + getFileset(fileDTO) + "/" + 
   			fileDTO.getControlNumber() + "/" + fileDTO.getFileName();
	}
  
	public static int getFileset(FileDTO fileDTO) {
		return (fileDTO.getId() - 1) / documentsPerSet + 1;
	}

	//"D:/cris/files"
	public static String storageRoot = "E:/cris/files";
	public static int documentsPerSet = 1000;
	private static Log log = LogFactory.getLog(FileStorage.class.getName());
    
	public static void createStorageRootDir() {
		try {
			File d = new File(storageRoot);
			if (!d.exists()) {
				d.mkdirs();
			} else {
				if (d.isFile())
					log.fatal(storageRoot + " is a file, not a directory!");
			}
		} catch (Exception e) {
			log.fatal(storageRoot + " cannot be created!");
		}
  }

}
