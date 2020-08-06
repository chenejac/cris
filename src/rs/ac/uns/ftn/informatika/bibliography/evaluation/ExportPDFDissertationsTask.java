package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;
import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.StudyFinalDocumentDTO;
import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ExportPDFDissertationsTask implements Task {

	public ExportPDFDissertationsTask(File exportFolder, int publicationYear) {
		this.exportFolder = exportFolder;
		this.publicationYear = publicationYear;
	}

	@Override
	public boolean execute() {
		try {
			List<StudyFinalDocumentDTO> dissertations = collectDefendedDissertations();
			copyDissertations(dissertations);
			dissertations = collectNonDefendedDissertations();
			copyDissertations(dissertations);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
	}
	
	public List<StudyFinalDocumentDTO> collectDefendedDissertations() {
		String whereClause = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' AND NOTE like 'Public period finished!' " +
						"AND DATEMODIFIED like '" + publicationYear + "-__-__') " +
						"AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
		return getThesesByWhereClause(whereClause);
	}
	
	public List<StudyFinalDocumentDTO> collectNonDefendedDissertations() {
		String whereClause = "ARCHIVED != 100 AND RECORDID in (select RECORDID from FILE_STORAGE where TYPE like 'report' and LICENSE like 'Temporary available - not defended' " +
						"AND DATEMODIFIED like '" + publicationYear + "-__-__') " +
							"AND (RECORDSTRING like '%710_2#__0(BISIS)8011%' OR RECORDSTRING like '%710_2#__0(BISIS)114901%' OR RECORDSTRING like '%710_2#__0(BISIS)593%' OR RECORDSTRING like '%710_2#__0(BISIS)5928%' OR RECORDSTRING like '%710_2#__0(BISIS)82773%' OR RECORDSTRING like '%710_2#__0(BISIS)5941%' OR RECORDSTRING like '%710_2#__0(BISIS)5940%' OR RECORDSTRING like '%710_2#__0(BISIS)5929%')";
		return getThesesByWhereClause(whereClause);
	}
	
	public List<StudyFinalDocumentDTO> getThesesByWhereClause(String whereClause){
		List<StudyFinalDocumentDTO> retVal = new ArrayList<StudyFinalDocumentDTO>();
		List<Record> records = recordDAO.getRecordsIdsFromDatabaseByWhereClause(whereClause);
		if(records != null) {
			for (Record record : records) {
				retVal.add((StudyFinalDocumentDTO) record.getDto());
			}
		}
		List<String> orderByList = new ArrayList<String>();
		List<String> directionsList = new ArrayList<String>();
		orderByList.add("dateModified");
		directionsList.add("desc");
		orderByList.add("controlNumber");
		directionsList.add("desc");
		Collections.sort(retVal, new GenericComparator<StudyFinalDocumentDTO>(
				orderByList, directionsList));
		return retVal;
	}

	
	
	private void copyDissertations(List<StudyFinalDocumentDTO> dissertations) {
		for (StudyFinalDocumentDTO dissertation : dissertations) {
			try {
				File directory = new File (exportFolder, dissertation.getAuthor().getName().toString());
				if (directory.exists()){
					directory = new File (exportFolder, dissertation.getAuthor().getName().toString() + " " + new GregorianCalendar().getTimeInMillis());
				}
				if(directory.mkdir()){
					if (dissertation.getFile() != null){
						File newFile = new File (directory, "disertacija.pdf");
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getFile())), newFile);
						} else {
							System.out.println("Dissertation Final pdf creation problem: " + dissertation.getRecord().toString());
						}
					} else if (dissertation.getPreliminaryTheses() != null){
						File newFile = new File (directory, "disertacija.pdf");
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getPreliminaryTheses())), newFile);
						} else {
							System.out.println("Dissertation Public Review pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getSupplement() != null){
						File newFile = new File (directory, "dodatak." + dissertation.getSupplement().getFileName().substring(dissertation.getSupplement().getFileName().lastIndexOf('.') + 1));
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getSupplement())), newFile);
						} else {
							System.out.println("Supplement Final pdf creation problem: " + dissertation.getRecord().toString());
						}
					} else if (dissertation.getPreliminarySupplement() != null){
						File newFile = new File (directory, "dodatak." + dissertation.getPreliminarySupplement().getFileName().substring(dissertation.getPreliminarySupplement().getFileName().lastIndexOf('.') + 1));
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getPreliminarySupplement())), newFile);
						} else {
							System.out.println("Supplement Public Review pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getReport() != null){
						File newFile = new File (directory, "izvestaj.pdf");
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getReport())), newFile);
						} else {
							System.out.println("Report pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getFileCopyright() != null){
						File newFile = new File (directory, "licenca.pdf");
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getFileCopyright())), newFile);
						} else {
							System.out.println("License pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
					if (dissertation.getMetadataCopyright() != null){
						File newFile = new File (directory, "izjavaIstovetnost.pdf");
						if(newFile.createNewFile()){
							copyFileUsingFileChannels(new File(FileStorage.getFullPath(dissertation.getMetadataCopyright())), newFile);
						} else {
							System.out.println("Metadata copyright pdf creation problem: " + dissertation.getRecord().toString());
						}
					}
				} else {
					System.out.println("Make dir problem: " + dissertation.getRecord().toString());
				}
			} catch (IOException e) {
				System.out.println("Some file pdf creation problem: " + dissertation.getRecord().toString());
				e.printStackTrace();
			}	finally {
				dissertation.clear();
			}
		}
	}
	
	private static void copyFileUsingFileChannels(File source, File dest)
	        throws IOException {
	    FileChannel inputChannel = null;
	    FileChannel outputChannel = null;
	    try {
	        inputChannel = new FileInputStream(source).getChannel();
	        outputChannel = new FileOutputStream(dest).getChannel();
	        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	    } finally {
	    	System.out.println(source.getAbsolutePath());
	        if(inputChannel == null)
	        	System.out.println("NEMA");
	        else {
	        	inputChannel.close();
	        
	        	outputChannel.close();
	        }
	    }
	}


	private RecordDAO recordDAO = new RecordDAO(new RecordDB());
	private File exportFolder;
	private int publicationYear;
	
	private static Log log = LogFactory.getLog(ExportPDFDissertationsTask.class.getName());

}
