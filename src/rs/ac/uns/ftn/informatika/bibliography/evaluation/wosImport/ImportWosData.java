package rs.ac.uns.ftn.informatika.bibliography.evaluation.wosImport;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.WildcardQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;

public class ImportWosData {
	
private static Connection conn = null;
	
	private static RecordDB recordDB = new RecordDB();
	
	private static EvaluationDB evaluationDB = new EvaluationDB();
	
	public static MetricsDB metricsDB = new MetricsDB();
	
	
	public static RandomAccessFile newJournalsList;
	
	
	
	/*
	
	static {
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	private static List<String> getRecordIdsFromISSN(String issn){
		WildcardQuery query1 = new WildcardQuery(new Term("ISSN", issn.toLowerCase()+"*"));
		WildcardQuery query2 = new WildcardQuery(new Term("ISSN", issn+"*"));
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(query1, Occur.SHOULD);
		booleanQuery.add(query2, Occur.SHOULD);
		
		List<Record> records = Retriever.select(booleanQuery, new AllDocCollector(false));
		List<String> controlNumbers = null ;
		if(records!=null && records.size()>0){
			controlNumbers = new ArrayList<String>();
			for(Record rec:records){
				 controlNumbers.add(rec.getControlNumber());		
			}	
		}
		return controlNumbers;		
	}
	
	private static void printNewJournals(){
	List<ISIJournal> allJournals = WosFilesProcessor.getAllJournals();		
		System.out.println("Ukupno casopisa:"+allJournals.size());
		int brNovih = 0;
		for(ISIJournal journal:allJournals){
			List<String> recordIds = getRecordIdsFromISSN(journal.getIssn());
			if(recordIds==null){
				System.out.println("abb. title:"+journal.getAbbvTitle()+", issn:"+journal.getIssn());
				brNovih++;
			}			
	//		else
	//			System.out.println("issn:"+journal.getIssn()+"controlNumber:"+recordId);
				
		}
		System.out.println("Broj novih "+brNovih);
	}
	
	private static void printDoubleISSNs(){
		List<ISIJournal> allJournals = WosFilesProcessor.getAllJournals();
		System.out.println("Dupli issn-ovi");
		for(ISIJournal journal1:allJournals){
			for(ISIJournal journal2:allJournals){
				if(journal1.getIssn().equals(journal2.getIssn()) && journal1.getCategory().equals(journal2.getCategory()) 
						&& !journal1.getImpactFactor().equals(journal2.getImpactFactor())){
					System.out.println(journal1.getIssn()+", first im:"+journal1.getImpactFactor()+", second im: "+journal2.getImpactFactor());
				}
			}
		}		
	}
	
	private static void printChangedTitle(){
		List<ISIJournal> allJournals = WosFilesProcessor.getAllJournals();
		System.out.println("Promenjeni naslovi");
		for(ISIJournal j:allJournals){
			List<String> recordIds = getRecordIdsFromISSN(j.getIssn());
			Record rec1 = recordDB.getRecord(conn, recordIds.get(0));
			if(rec1!=null){
				JournalDTO rec = (JournalDTO) rec1.getDto();
				if(rec!=null && rec.getNameAbbreviation()!=null && rec.getNameAbbreviation().getContent()!=null &&
						!rec.getNameAbbreviation().getContent().equals(j.getAbbvTitle()))
					System.out.println(j.getIssn()+", old:"+rec.getNameAbbreviation().getContent()+", new:"+j.getAbbvTitle());
				}
		}
	}
	
	private static Map<String, List<ISIJournal>> organizeJournalsByCategoryTwoYearsImpactFactor(List<ISIJournal> allJournals){
	 Map<String, List<ISIJournal>> retVal = new HashMap<String, List<ISIJournal>>();
		for(ISIJournal journal:allJournals){
			if(journal.getImpactFactor()!=null){     //&& !journal.getImpactFactor().equals(0.0)){
				if(retVal.get(journal.getCategory())==null){
					List<ISIJournal> list = new ArrayList<ISIJournal>();
					list.add(journal);
					retVal.put(journal.getCategory(), list);
				}else{
					List<ISIJournal> list = retVal.get(journal.getCategory());
					list.add(journal);
				}
				}
				
			}
		return retVal;
		}
	
	private static Map<String, List<ISIJournal>> organizeJournalsByCategoryFiveYearsImpactFactor(List<ISIJournal> allJournals){
	 Map<String, List<ISIJournal>> retVal = new HashMap<String, List<ISIJournal>>();
		for(ISIJournal journal:allJournals){
			if(journal.getImpactFactor5()!=null){ // && !journal.getImpactFactor5().equals(0.0)){
				if(retVal.get(journal.getCategory())==null){
					List<ISIJournal> list = new ArrayList<ISIJournal>();
					list.add(journal);
					retVal.put(journal.getCategory(), list);
				}else{
					List<ISIJournal> list = retVal.get(journal.getCategory());
					list.add(journal);
				}
				}
				
			}
		return retVal;
		}
	
	private static List<ISIJournal> removeDuplicates(List<ISIJournal> allJournals){
		List<ISIJournal> temp = new ArrayList<ISIJournal>();
		for(ISIJournal j:allJournals){
			if(!sadrziCasopis(temp, j))
				temp.add(j);
		}
		return temp;
	}
	
	private static boolean sadrziCasopis(List<ISIJournal> list, ISIJournal j){
		for(ISIJournal j1:list)
			if (j1.getIssn().equals(j.getIssn())) return true;
		return false;
	}
		
	private static List<ISIJournal> sortAndFindRang(List<ISIJournal> allJournals){
		List<ISIJournal> retVal = new ArrayList<ISIJournal>();
		Map<String, List<ISIJournal>> retValIF2 = organizeJournalsByCategoryTwoYearsImpactFactor(allJournals);
		Map<String, List<ISIJournal>> retValIF5 = organizeJournalsByCategoryFiveYearsImpactFactor(allJournals);
		List<String> sortAttributesForGen = new ArrayList<String>();
		sortAttributesForGen.add("impactFactor");			
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("desc");			
		for(String str:retValIF2.keySet()){
		 List<ISIJournal> list = removeDuplicates(retValIF2.get(str));			
			Collections.sort(list, new GenericComparator<ISIJournal>(
					sortAttributesForGen, sortDirections));
			retValIF2.put(str, list);			
			}
	
		sortAttributesForGen = new ArrayList<String>();
		sortAttributesForGen.add("impactFactor5");			
		sortDirections = new ArrayList<String>();
		sortDirections.add("desc");			
		for(String str:retValIF5.keySet()){
			List<ISIJournal> list = removeDuplicates(retValIF5.get(str));		
			Collections.sort(list, new GenericComparator<ISIJournal>(
					sortAttributesForGen, sortDirections));		
			retValIF5.put(str, list);
			
		}
		
		for(String str:retValIF2.keySet()){
			List<ISIJournal> list = retValIF2.get(str);		
			for(int i=0; i<list.size();i++){
				ISIJournal j = list.get(i);
				j.setUkupnoUKategorijiIF2(list.size());
				
				if(i>0 && j.getImpactFactor().equals(list.get(i-1).getImpactFactor())){					
					j.setRedniBrojIF2(list.get(i-1).getRedniBrojIF2());
				}else{
					j.setRedniBrojIF2(i+1);				
				}				
			}
		}
			for(String str:retValIF5.keySet()){
				List<ISIJournal> list = retValIF5.get(str);
				for(int i=0; i<list.size();i++){
					ISIJournal j = list.get(i);
					j.setUkupnoUKategorijiIF5(list.size());
					if(i>0 && j.getImpactFactor5().equals(list.get(i-1).getImpactFactor5()))
						j.setRedniBrojIF5(list.get(i-1).getRedniBrojIF5());
					else
						j.setRedniBrojIF5(i+1);
			}
		}
			
			for(String str:retValIF2.keySet()){
				List<ISIJournal> list = retValIF2.get(str);
				retVal.addAll(list);
			}		
		
		return retVal;
		
	}
	
	// parametar je putanja do foldera koji sadrzi foldere JCR Science Edition i JCR Social Science Edition 
	private static void wosImport(String filePath, String year) throws Exception{
		try{
		newJournalsList = new RandomAccessFile(new File("spisakNovih.txt"), "rw");
		RecordDAO recordDAO = new RecordDAO(recordDB);		
		List<ISIJournal> retVal =  sortAndFindRang(WosFilesProcessor.getAllJournals(filePath, year));
		List<String> addedIF2 = new ArrayList<String>();
		List<String> addedIF5 = new ArrayList<String>();
		
		for(ISIJournal isiJournal:retVal){
			List<String> controlNumbers = getRecordIdsFromISSN(isiJournal.getIssn());		
			//1. ako ne postoji u cris-u prvo ga treba ubaciti			
			if(controlNumbers==null){			
				newJournalsList.writeBytes("ISSN: "+isiJournal.getIssn()+", abb. title: "+isiJournal.getAbbvTitle()+"\n");
				JournalDTO crisJournal = new JournalDTO();
				crisJournal.setIssn(isiJournal.getIssn());
				crisJournal.setNameAbbreviation(new MultilingualContentDTO(isiJournal.getAbbvTitle(), MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
			 crisJournal.setName(new MultilingualContentDTO(isiJournal.getTitle(), MultilingualContentDTO.LANGUAGE_ENGLISH, MultilingualContentDTO.TRANS_HUMAN));
			 if (recordDAO.add(new Record("importWos"+year, new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
						crisJournal))){
					System.out.println("Uspelo dodavanje casopisa "+isiJournal.getAbbvTitle()+", id="+crisJournal.getControlNumber());
					controlNumbers = new ArrayList<String>();
					controlNumbers.add(crisJournal.getControlNumber());
			}
			}
			for(String controlNumber:controlNumbers){	
		
			if(metricsDB.addResultMetrics(conn, controlNumber, "twoYearsIF", "researchArea", isiJournal.getCategory(), Integer.parseInt(isiJournal.getYear()), 
					Double.valueOf(isiJournal.getRedniBrojIF2()), Double.valueOf(isiJournal.getRedniBrojIF2())/Double.valueOf(isiJournal.getUkupnoUKategorijiIF2()))){	
				System.out.println("Uspesno dodavanje ranga za casopis "+isiJournal.getIssn()+" (dvogodisnji impakt faktor)");
				if(!addedIF2.contains(controlNumber)){	
				if(metricsDB.addResultMetrics(conn, controlNumber, "twoYearsIF", "value of metric", "value of IF", Integer.parseInt(isiJournal.getYear()),
						isiJournal.getImpactFactor(), null)){
				 System.out.println("Uspesno dodavanje impakt faktora za casopis "+isiJournal.getIssn()+" (dvogodisnji impakt faktor)");					
					addedIF2.add(controlNumber);				
				}else{
					System.out.println("Greska, nije dodat impakt faktor za casopis "+isiJournal.getIssn()+" (dvogodisnji impakt faktor)");
				}
				}
			}else{
				System.out.println("Greska, nije dodat rang za casopis "+isiJournal.getIssn()+" (dvogodisnji impakt faktor)");
			}
			
			if(metricsDB.addResultMetrics(conn, controlNumber, "fiveYearsIF", "researchArea", isiJournal.getCategory(), Integer.parseInt(isiJournal.getYear()), 
					Double.valueOf(isiJournal.getRedniBrojIF5()), Double.valueOf(isiJournal.getRedniBrojIF5())/Double.valueOf(isiJournal.getUkupnoUKategorijiIF5()))){	
				System.out.println("Uspesno dodavanje ranga za casopis "+isiJournal.getIssn()+" (petogodisnji impakt faktor)");				
				if(!addedIF5.contains(controlNumber)){
				if(metricsDB.addResultMetrics(conn, controlNumber, "fiveYearsIF", "value of metric", "value of IF", Integer.parseInt(isiJournal.getYear()),
						isiJournal.getImpactFactor5(), null)){
					//System.out.println("Uspesno dodavanje impakt faktora za casopis "+isiJournal.getIssn()+" (petogodisnji impakt faktor)");
					addedIF5.add(controlNumber);
				}else{
					System.out.println("Greska, nije dodat impakt faktor za casopis "+isiJournal.getIssn()+" (petogodisnji impakt faktor)");
				}
				}
			}else{
				System.out.println("Greska, nije dodat rang za casopis "+isiJournal.getIssn()+" (petogodisnji impakt faktor)");
			}		
		}
		}
		newJournalsList.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public static void ispisiRangRadiProvere(String filePath, String year){
		List<ISIJournal> retVal =  sortAndFindRang(WosFilesProcessor.getAllJournals(filePath, year ));
		for(ISIJournal isiJournal:retVal){
			System.out.println(isiJournal.getIssn()+", oblast: "+isiJournal.getCategory()
					+" rang:"+isiJournal.getRedniBrojIF2()+"/"+isiJournal.getUkupnoUKategorijiIF2());
		}
	}
	
	
	public static void printListByCategory(String filePath){
		List<ISIJournal> allJournals = WosFilesProcessor.getAllJournals(filePath, "2018");
		//obrisatiSuvisne(allJournals);		
		Map<String,List<ISIJournal>> byCategory = organizeJournalsByCategoryTwoYearsImpactFactor(allJournals);
		List<String> sortAttributesForGen = new ArrayList<String>();
		sortAttributesForGen.add("impactFactor");			
		List<String> sortDirections = new ArrayList<String>();
		sortDirections.add("desc");			
		for(String str:byCategory.keySet()){
		 List<ISIJournal> list = removeDuplicates(byCategory.get(str));			
			Collections.sort(list, new GenericComparator<ISIJournal>(
					sortAttributesForGen, sortDirections));
			byCategory.put(str, list);			
			}		
		try{
		RandomAccessFile outFile = new RandomAccessFile(new File("spisakPoKategoriji.txt"), "rw");
		ResearchAreaDB researchAreaDB = new ResearchAreaDB();
		Map<String, ResearchAreaDTO> allAreas = researchAreaDB.getAll(conn);
		for(String kategorija: byCategory.keySet()){			
			outFile.writeBytes("\n\nOblast: ");
			
			for(MultilingualContentDTO content : allAreas.get(kategorija).getAllTerms()){
				outFile.writeBytes(content.getContent()+" ");
			}
			outFile.writeBytes("\nBroj casopisa: "+byCategory.get(kategorija).size()+"\n");
			outFile.writeBytes("Spisak casopisa, po impakt faktoru:\n");
			int i = 0;
			for(ISIJournal j:byCategory.get(kategorija)){
				//outFile.writeBytes(++i+". "+j.getAbbvTitle()+",  "+j.getIssn()+", "+j.getImpactFactor()+"\n");
				outFile.writeBytes(j.getIssn()+", "+j.getImpactFactor()+"\n");
			}
		}
		outFile.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	// samo za 2013
	private static void obrisatiSuvisne(List<ISIJournal> allJournals){
		Iterator<ISIJournal> iterator = allJournals.iterator();
		while(iterator.hasNext()){
			ISIJournal j = iterator.next();
			if(j.getCategory().equals("wos123") && j.getIssn().equals("0101-2061")){
				iterator.remove();
			}
			if(j.getCategory().equals("wos220") && j.getIssn().equals("0908-4282")){
				iterator.remove();
			}
			if(j.getCategory().equals("wos186") && j.getIssn().equals("0908-4282")){
				iterator.remove();
			}
			if(j.getCategory().equals("wos181") && j.getIssn().equals("0022-0744")){
				iterator.remove();
			}
			if(j.getCategory().equals("wos32") && j.getIssn().equals("1940-3151")){
				iterator.remove();
			}
			
		}
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length!=8){
			System.out.println("samovrednovanje <yearOfIF> <luceneIndex> <hostname> <port> <schema> <username> <password> " +
					"<folderPath>" );
			return;
		}
	
		
		String year = args[0];
		String luceneIndex = args[1];
		String hostname = args[2];
		String port = args[3];
		String schema = args[4];
		String connectionParameters = "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		String username = args[5];
		String password = args[6];
		String folderPath = args[7];
		
		
		
		Retriever.setIndexPath(luceneIndex);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ispisiRangRadiProvere("e:/CRIS/evaluacija/imports/sci-2015/", "2015");
		try{
			wosImport(folderPath, year);
			printListByCategory("e:/CRIS/evaluacija/imports/sci-2019/");
//			printNewJournals();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
}
}
