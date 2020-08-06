package rs.ac.uns.ftn.informatika.bibliography.evaluation.wosImport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class WosFilesProcessor {
	
	private static String filePath = "e:/CRIS/evaluacija/imports/sci-2016";

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		File[] listFilesSE = new File(filePath+"/JCR Science Edition").listFiles();
		File[] listFilesSSE = new File(filePath+"/JCR Social Science Edition").listFiles();
		List<ISIJournal> seJournals = getJournalsFromFiles(listFilesSE, "se","2013");
		for(ISIJournal journal:seJournals){
			System.out.println("abb title:"+journal.getAbbvTitle()+"issn="+journal.getIssn());
		}		
	}
	
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
	
	public static List<ISIJournal> getAllJournals(){
		File[] listFilesSE = new File(filePath+"/JCR Science Edition").listFiles();
		File[] listFilesSSE = new File(filePath+"/JCR Social Science Edition").listFiles();
		List<ISIJournal> seJournals = getJournalsFromFiles(listFilesSE, "se","2016");
		List<ISIJournal> sseJournals = getJournalsFromFiles(listFilesSSE, "sse","2016");
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
//			rf.readLine();	
			String journalLine;
			while((journalLine = rf.readLine())!=null){
				if(journalLine.equals("") || journalLine.startsWith("Copyright") || journalLine.contains("By exporting the selected data")) break;				
				String[] parts = journalLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 99);
				ISIJournal journal = new ISIJournal(categoryId,year, list, parts[2].trim(), parts[1].trim(), parts[3].trim(), 
						parts[4].trim(),null, null, null,null, null, null,null);
				if((!parts[5].equals("")) && (!parts[5].trim().equals("Not Available"))){
					journal.setImpactFactor(Double.parseDouble(parts[5].trim()));
				}
				else
					journal.setImpactFactor(0.0);
				if((!parts[6].equals("")) && (!parts[6].trim().equals("Not Available"))){
					journal.setImpactFactor5(Double.parseDouble(parts[6].trim()));
				}
				else
					journal.setImpactFactor5(0.0);				
				retVal.add(journal);				
			}		
		}
		return retVal;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
