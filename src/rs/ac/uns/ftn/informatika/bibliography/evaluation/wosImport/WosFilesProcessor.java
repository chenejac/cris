package rs.ac.uns.ftn.informatika.bibliography.evaluation.wosImport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class WosFilesProcessor {
	
	public static List<ISIJournal> getAllJournals(String filePath, String year){
		File[] listFilesSE = new File(filePath+"/JCR Science Edition").listFiles();
		File[] listFilesSSE = new File(filePath+"/JCR Social Science Edition").listFiles();
		List<ISIJournal> seJournals = getJournalsFromFiles(listFilesSE, "se",year);
		List<ISIJournal> sseJournals = getJournalsFromFiles(listFilesSSE, "sse",year);
		List<ISIJournal> allJournals = new ArrayList<ISIJournal>();
		allJournals.addAll(sseJournals);
		allJournals.addAll(seJournals);
		return allJournals;
	}
	
	private static List<ISIJournal> getJournalsFromFiles(File[] files, String list, String year){
		try{
		List<ISIJournal> retVal = new ArrayList<ISIJournal>();		
		
		for(File f:files){
			RandomAccessFile rf = new RandomAccessFile(f, "r");	
			String categoryId = f.getName().substring(f.getName().indexOf("wos"),f.getName().indexOf(".csv"));
//			String categoryId = f.getName().substring(f.getName().indexOf("wos"),f.getName().indexOf(".txt"));
			//System.out.println(categoryId);
			rf.readLine();
			rf.readLine();			
			rf.readLine();
			String journalLine;
			while((journalLine = rf.readLine())!=null){
				if(journalLine.equals("") || journalLine.startsWith("Copyright") || journalLine.contains("By exporting the selected data")) {
					break;
				}
				String[] parts = journalLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 99);
				if(parts[0].trim().equals("")) {
					break;
				}
				if(parts[10].replaceAll("^\"+|\"+$", "").trim().toLowerCase().equals("n/a")) {
					continue;
				}
				ISIJournal journal = new ISIJournal(
						categoryId,
						year,
						list,
						parts[1].replaceAll("^\"+|\"+$", "").trim(),
						parts[0].replaceAll("^\"+|\"+$", "").trim(),
						parts[3].replaceAll("^\"+|\"+$", "").trim(),
						parts[4].replaceAll("^\"+|\"+$", "").trim(),
						parts[7].replaceAll("^\"+|\"+$", "").trim(),
						null,
						null,
						parts[6].replaceAll("^\"+|\"+$", "").trim(),
						null,
						null,
						null,
						null,
						null);
				if (journal.getEdition() != null && (journal.getEdition().equalsIgnoreCase("SSCI") || journal.getEdition().equalsIgnoreCase("SCIE"))) {
					if ((!parts[8].replaceAll("^\"+|\"+$", "").equals("")) && (!parts[8].replaceAll("^\"+|\"+$", "").toLowerCase().trim().equals("n/a"))) {
						journal.setImpactFactor(Double.parseDouble(parts[8].replace("<0.1", "0.09").replaceAll("^\"+|\"+$", "").trim()));
					} else
						journal.setImpactFactor(0.0);
					if ((!parts[9].replaceAll("^\"+|\"+$", "").equals("")) && (!parts[9].replaceAll("^\"+|\"+$", "").toLowerCase().trim().equals("n/a"))) {
						journal.setImpactFactor5(Double.parseDouble(parts[9].replace("<0.1", "0.09").replaceAll("^\"+|\"+$", "").trim()));
					} else
						journal.setImpactFactor5(0.0);
					retVal.add(journal);
				}
			}		
		}
		return retVal;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
