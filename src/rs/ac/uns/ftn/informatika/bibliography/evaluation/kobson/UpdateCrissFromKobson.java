/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.MultilingualContentDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonCategory;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonCategoryJournal;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonImpactFactorJournal;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonJournal;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.ExportJournalNameISSNDuplicates;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.CerifEntitiesNames;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.RecordConsumerListener;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class UpdateCrissFromKobson {

	public static Connection connCris;
	public static Connection connKobson;
	
	public static RecordDB recordDB = new RecordDB();
	public static RecordDAO recordDAO = new RecordDAO(new RecordDB());
	public static ResearchAreaDB researchAreaDB = new ResearchAreaDB();
	public static MetricsDB metricsDB = new MetricsDB();
	
	public static Log log = LogFactory.getLog(UpdateCrissFromKobson.class.getName());
	
	public static HashMap<String, ResearchAreaDTO> researchAreas = new HashMap<String, ResearchAreaDTO>();
	
	//list of Cris loaded key ISSN and BYSIS ID
	public static Map<String, ArrayList<String>> crisJournalsIssnID  = new HashMap<String, ArrayList<String>>();
	
	//list of Kobson loaded key ID for changed journals
	public static ArrayList<Integer> kobsonIDs  = new ArrayList<Integer>();
	
	
//	public static boolean clearCategoriesCrissDatabase(Connection connCris)
//	{
//		boolean retVal = false;
//		try {
//			connCris.setAutoCommit(true);
//		} catch (SQLException e) {
//			retVal=false;
//			e.printStackTrace();
//		}
//		
//		if (researchAreas==null || researchAreas.isEmpty()){ 
//			loadCategoriesCrissDatabase();
//		}
//		
//		for(String keyWos : researchAreas.keySet())
//		{
//			if (keyWos.equalsIgnoreCase("wos") || !keyWos.startsWith("wos")) {
//				continue;
//			}
//			
//			System.out.println(keyWos);
//			if(researchAreaDB.deleteClass(connCris, researchAreas.get(keyWos))==false){
//					System.out.println("Greska u brisanju podataka kategorija");
//					System.exit(0);
//			}
//			System.out.println("Brisem obrisao klasu " + keyWos + " i semu " + researchAreas.get(keyWos).getSchemeId());
//		}
//		metricsDB.deleteResultMetricsAll(connCris);
//		retVal=true;
//		
//		try {
//			connCris.setAutoCommit(false);
//		} catch (SQLException e) {
//			retVal=false;
//			e.printStackTrace();
//		}
//		
//		return retVal;
//	}
	
	public static boolean loadCategoriesCrissDatabase()
	{
		boolean retVal = false;
		
		if (researchAreas!=null && !researchAreas.isEmpty()){ 
			return true;
		}
		
		try {
			connCris.setAutoCommit(true);
		} catch (SQLException e) {
			retVal = false;
			e.printStackTrace();
		}
		
		HashMap<String, ResearchAreaDTO> temp = (HashMap) researchAreaDB.getAll(connCris);
		for(String keyCat : temp.keySet()){
			if (keyCat.startsWith("wos")) {
				researchAreas.put(keyCat, temp.get(keyCat));
			}
		}
		retVal = true;
		
		try {
			connCris.setAutoCommit(false);
		} catch (SQLException e) {
			retVal = false;
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static boolean addCategoriesCrssDatabase()
	{
		boolean retVal = false;
		try {
			connCris.setAutoCommit(true);
		} catch (SQLException e) {
			retVal = false;
			e.printStackTrace();
		}
		
		KobsonDatabaseOperations.loadCategoriesFromDatabase();
		List<KobsonCategory> listKobsonKategories = KobsonDatabaseOperations.getCategoryList();
		
		if(!loadCategoriesCrissDatabase()){
			System.out.println("Neuspelo citanje Criss kategorija");
			System.exit(0);
		}
		
		ResearchAreaDTO superResearchAreaDTO = researchAreaDB.getResearchArea(connCris, ResearchAreaDTO.RESEARCH_AREA_SCHEMA, "wos", new HashMap<String, ResearchAreaDTO>());
		
		for(KobsonCategory cat : listKobsonKategories){
			if(!researchAreas.containsKey("wos"+cat.getId())){
				ResearchAreaDTO researchAreaDTO = new ResearchAreaDTO();
				researchAreaDTO.setClassId("wos"+cat.getId());
				researchAreaDTO.setTerm(new MultilingualContentDTO(cat.getName(), "eng", MultilingualContentDTO.TRANS_ORIGINAL));
				researchAreaDTO.setSuperResearchArea(superResearchAreaDTO);
				
				if(researchAreaDB.addClass(connCris, researchAreaDTO)==false){
					System.out.println("Greska u dodavanju kobsonovih naucnih oblasti");
					System.exit(0);
				}
				System.out.println("Ubacio u Criss kategoriju klasu " + researchAreaDTO.getClassId());
			}
		}
		
		retVal = true;
		
		try {
			connCris.commit();
			connCris.setAutoCommit(false);
		} catch (SQLException e) {
			retVal = false;
			e.printStackTrace();
			
		}
		
		return retVal;
	}
	
	
	static public boolean collectCrisJournals(){
		
		
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));

//		System.out.println("Ukupno preuzeto 1 " + listJournals.size());
		
		int brojac = 0;
		boolean retVal = true;
		while (listJournals.size()>0) {
			try {
				
				Record jRecord = listJournals.get(0);
				JournalDTO jDTO = (JournalDTO) jRecord.getDto();
				
				String jISSN = jDTO.getIssn();
				if(jISSN.startsWith("NN") || jISSN.startsWith("ISBN")){
					listJournals.remove(jRecord);
					continue;
				}
				
//				if(jDTO.getControlNumber().equals("(BISIS)67406") || jDTO.getControlNumber().equals("(BISIS)68308")
//						|| jDTO.getControlNumber().equals("(BISIS)68698") || jDTO.getControlNumber().equals("(BISIS)77302")){
//					listJournals.remove(jRecord);
//					continue;
//				}
				
				if(jISSN.equalsIgnoreCase("")){
					System.out.println("NEMA ISSN casopisa:"+ jDTO.getName().getContent() + "| CRISS id:"+jDTO.getControlNumber());
					retVal = false;
					listJournals.remove(jRecord);
					continue;
//					System.exit(0);
				}
				
				brojac++;
//				System.out.println("casopis:"+ jDTO.getName() + " id:"+jDTO.getControlNumber() +" ISSN:"+jDTO.getIssn());	
				String [] ISSNs = MNO.processISSN(jISSN);
				
				for (int i = 0; i < ISSNs.length; i++) {
					String ISSN = ISSNs[i];
					
					if(!crisJournalsIssnID.containsKey(ISSN))
						crisJournalsIssnID.put(ISSN, new ArrayList<String>());
					
					ArrayList<String> duplicatesISSN = crisJournalsIssnID.get(ISSN);
					duplicatesISSN.add(jDTO.getControlNumber());	
				}
				listJournals.remove(jRecord);
				
			} catch (Exception e) {
				log.error(e);
				System.out.println("Greska pri izcitavanju casopisa iz CRIS baze");
				e.printStackTrace();
				System.exit(0);
			}
		}
//		System.out.println("Ukupno preuzeto 2 " + brojac);
		return retVal;
	}
	
	static public void updateCrisFromCobson(){

		RecordConsumerListener recordConsumer = new RecordConsumerListener();
		
		if(collectCrisJournals()==false){
			System.out.println("Doslo je do greske");
			System.exit(0);
		}
		
//		ArrayList<String> allBrojac = new ArrayList<String>();
//		for (String key : crisJournalsIssnID.keySet()) {
//			ArrayList<String> duplicatesISSN = crisJournalsIssnID.get(key);
//			for (int i = 0; i < duplicatesISSN.size(); i++) {
//				if(!allBrojac.contains(duplicatesISSN.get(i)))
//					allBrojac.add(duplicatesISSN.get(i));
//			}
//		}
//		System.out.println("Ukupno preuzeto 3 " + allBrojac.size());
		
		KobsonDatabaseOperations kobsonDatabase = new KobsonDatabaseOperations();
		kobsonIDs = kobsonDatabase.getUpdatedJournalIDFromDatabase();
		
		System.out.println("Kobson lista "+kobsonIDs.size());
		
		if (kobsonIDs ==null) {
			System.out.println("nema kobson IDs");
			System.exit(0);
		}
		
		try {
			connCris.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int brojac = 0;
		for (int i = 0; i < kobsonIDs.size(); i++) {
//			System.out.print(i+(i%35==0?"\n":" "));
			
			Integer id = kobsonIDs.get(i);
			kobsonDatabase.loadJournalAdvInfFromDatabase(id.intValue());
			KobsonJournal kobsonLoadedJournal = kobsonDatabase.getLoadedJournal();
			String [] changes = Funkcije.parseVersionchange(kobsonLoadedJournal.getVERSIONCHANGE());
			
			if (changes == null){
				continue;
			}
			brojac++;
			System.out.print(brojac+(brojac%35==0?"\n":" "));
			
			if(!crisJournalsIssnID.containsKey(kobsonLoadedJournal.getISSN()) && !changes[0].equals("NEW")){
				System.out.println("\n Za issn:"+kobsonLoadedJournal.getISSN()+" nije pronadjen kljuc i nije NEW");
				changes = new String [1];
				changes[0]="NEW";
				changeToNewJoural(kobsonLoadedJournal);
			}
			
			if(!crisJournalsIssnID.containsKey(kobsonLoadedJournal.getISSN()) && changes[0].equals("NEW")){
				System.out.println("ADD--------------------------------");
				addJournalToCris(kobsonLoadedJournal);
			}
//			//izuzetci Duplikati iy ISI baze
//			else if (proveriDaLiPripadaListiDuplikataISIBAZE(kobsonLoadedJournal) && changes[0].equals("NEW")){
//				System.out.println("ADD--------------------------------");
//				addJournalToCris(kobsonLoadedJournal);
//			}
			else {
				ArrayList<String> journalsDuplicatesISSN = crisJournalsIssnID.get(kobsonLoadedJournal.getISSN());
				for (int j = 0; j < journalsDuplicatesISSN.size(); j++) {
					String updatedJournalID = journalsDuplicatesISSN.get(j);
					updateJournalToCris(kobsonLoadedJournal, updatedJournalID, changes, recordConsumer);
				}
			}
			
//			try {
//				connCris.commit();
//			} catch (SQLException e) {
//				e.printStackTrace();	
//			}
		}
		
		try {
			connCris.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();	
		}
	}

	static public void changeToNewJoural(KobsonJournal kobsonJournal){
		kobsonJournal.setVERSIONCHANGE("NEW;");
		List<KobsonImpactFactorJournal> impactFactorJournalLista = kobsonJournal.getImpaktFatorCasopisa();
		for (KobsonImpactFactorJournal imf : impactFactorJournalLista) {
			if(!imf.getVersionchange().equals("NEW;")){
				imf.setVersionchange("NEW;");
				ArrayList<KobsonCategoryJournal> categories = imf.getCategory();
				for(KobsonCategoryJournal category : categories){
					if (!category.getVersionchange().equals("NEW;")) {
						category.setVersionchange("NEW;");
					}
				}
			}
		}
		
		List<KobsonImpactFactorJournal> impactFactorJournalListapetGodina = kobsonJournal.getImpaktFatorCasopisaPetGodina();
		for (KobsonImpactFactorJournal imfPet : impactFactorJournalListapetGodina) {
			if(!imfPet.getVersionchange().equals("NEW;")){
				imfPet.setVersionchange("NEW;");
				ArrayList<KobsonCategoryJournal> categoriesPet = imfPet.getCategory();
				for(KobsonCategoryJournal category : categoriesPet){
					if (!category.getVersionchange().equals("NEW;")) {
						category.setVersionchange("NEW;");
					}
				}
			}
		}
				
	}
	
	
	static public void updateJournalToCris(KobsonJournal kobsonJournal, String crissJournalID, String [] changes, RecordConsumerListener recordConsumer){
		
		//promena osnovih podataka
		if(!changes[0].equals("IMF") || changes[0].equals("NEW")){
			//load JournalDTO from database
			JournalDTO crissJournal = (JournalDTO) recordDAO.getDTO(crissJournalID);
			@SuppressWarnings("unused")
			String imeC = crissJournal.getName().getContent();
		
			for (int i = 0; i < changes.length; i++) {
				if (changes[i].equals("NASLOV")) {
					crissJournal.getName().setContent(kobsonJournal.getNaslov());
				}
				else if (changes[i].equals("SKRACENINASLOV")) {
					crissJournal.getNameAbbreviation().setContent(kobsonJournal.getSkraceniNaslov());
				}
				else if (changes[i].equals("JEZIK")) {
					String jezik = Funkcije.getLanguageFromKobson(kobsonJournal.getJezik());
					if(jezik!=null)
						crissJournal.getName().setLanguage(jezik);
				}
				else if (changes[i].equals("URL")) {
					crissJournal.setUri(kobsonJournal.getURL());
				}
				else if (changes[i].equals("NEW")) {
					crissJournal.getName().setContent(kobsonJournal.getNaslov());
					crissJournal.getNameAbbreviation().setContent(kobsonJournal.getSkraceniNaslov());
					String jezik = Funkcije.getLanguageFromKobson(kobsonJournal.getJezik());
					if(jezik!=null)
						crissJournal.getName().setLanguage(jezik);
					crissJournal.setUri(kobsonJournal.getURL());
				}
			}
//			SNIMANJE JOURNALDTO IZMENJENOG U BAZU
			if ((recordDAO.update(new Record(null, null, "sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
					crissJournal)) == false) || (recordConsumer.update(crissJournal) == false)) {
				System.out.println("Neuspeli update casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
				System.exit(0);
			}
			else{
			System.out.println("USPEO update casopisa casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
			}
			crissJournal.getRecord().clear();
			crissJournal.clear();
			

		}
		
		//promena podataka o IMF i Kategoriji
		int velicinaNiza = changes.length-1;
		if(changes[velicinaNiza].equals("IMF") || changes[velicinaNiza].equals("NEW")){
			addIMFtoCRIS(kobsonJournal, crissJournalID);
		}
	}
	
	static public void addJournalToCris(KobsonJournal kobsonJournal){
		//kreiranje casopisa u bazi
		JournalDTO journalDTO = new JournalDTO();	
		
		journalDTO.getName().setContent(kobsonJournal.getNaslov());
		
		String kobsonJezik = kobsonJournal.getJezik();
		kobsonJezik = Funkcije.getLanguageFromKobson(kobsonJezik);
		if(kobsonJezik==null || kobsonJezik.trim().equalsIgnoreCase(""))
			kobsonJezik = "srp";
		
		journalDTO.getName().setLanguage(kobsonJezik);
		
		if(kobsonJournal.getSkraceniNaslov()!=null)
			if(!kobsonJournal.getSkraceniNaslov().trim().equalsIgnoreCase("")){
				journalDTO.getNameAbbreviation().setContent(kobsonJournal.getSkraceniNaslov());
				journalDTO.getNameAbbreviation().setLanguage(kobsonJezik);
			}
			
		if(kobsonJournal.getISSN()!=null)
			if(!kobsonJournal.getISSN().trim().equalsIgnoreCase(""))
				journalDTO.setIssn(kobsonJournal.getISSN());
			
		if(kobsonJournal.getURL()!=null)
			if(!kobsonJournal.getURL().trim().equalsIgnoreCase(""))
				journalDTO.setUri(kobsonJournal.getURL());
			
			//SNIMANJE JOURNALDTO DODATOG U BAZU
		if (recordDAO.add(new Record("sinisa_nikolic@uns.ac.rs", new GregorianCalendar(), null, null, new Integer(0), CerifEntitiesNames.RESULT_PUBLICATION, 
				journalDTO)) == false) {
			System.out.println("Neuspeli ADD kobson casopisa:"+ kobsonJournal.getNaslov() + " id:"+kobsonJournal.getISSN());
			System.exit(0);
		}
		else{
			System.out.println("USPEO ADD kobson casopisa:"+ kobsonJournal.getNaslov() + " id:"+kobsonJournal.getISSN());
		}
		
		String crissJournalID = journalDTO.getControlNumber();
		journalDTO.getRecord().clear();
		journalDTO.clear();
		
		addIMFtoCRIS(kobsonJournal, crissJournalID);
	}

	static protected void addIMFtoCRIS(KobsonJournal kobsonJournal, String crissJournalID){
		//ubaci metriku za dvogodisnji if
		List<KobsonImpactFactorJournal> impactFactorJournalLista = kobsonJournal.getImpaktFatorCasopisa();
		for (KobsonImpactFactorJournal imf : impactFactorJournalLista) {
			if(imf.getVersionchange().equals("NEW;")){
				//ubaci metriku za if
				int godinaIF = imf.getGodina();
				double vrednostIF = Double.parseDouble(imf.getVrednost());
				
				ArrayList<KobsonCategoryJournal> categories = imf.getCategory();
				boolean stvarnoIIma = false;
				for(KobsonCategoryJournal category : categories){
					if (category.getVersionchange().equals("NEW;")) {
						String categoryID = "wos"+category.getIdCategory();
						if(category.getRang()==null)
							continue;
						else if(category.getRang().trim().equalsIgnoreCase(""))
							continue;
						else if(category.getRang().equalsIgnoreCase("0/0"))
							continue;
						
						double count = Double.parseDouble(category.getRang().split("/")[0]);
						double fraction = count / Double.parseDouble(category.getRang().split("/")[1]);
	
						if(!metricsDB.addResultMetrics(connCris, crissJournalID, "twoYearsIF", "researchArea", categoryID, godinaIF, count, fraction)){
							System.out.println("Nije uneo kategoriju: "+  categoryID + " za godinu:" + godinaIF +" casopisa sa criss id:"+crissJournalID);
							System.exit(0);
						}
						stvarnoIIma = true;
					}
				}
				
				if(stvarnoIIma){
					if(!metricsDB.addResultMetrics(connCris, crissJournalID, "twoYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null)){
						System.out.println("Nije uneo IF: "+  vrednostIF + " za godinu:" + godinaIF +" casopisa sa criss id:"+crissJournalID);
						System.exit(0);
					}
				}
			}
		}
		
		//ubaci metriku za pretogodisnji if
		List<KobsonImpactFactorJournal> impactFactorPetGodinaJournalLista = kobsonJournal.getImpaktFatorCasopisaPetGodina();
		for (KobsonImpactFactorJournal imfPet : impactFactorPetGodinaJournalLista) {
			if(imfPet.getVersionchange().equals("NEW;")){
				//ubaci metriku za if
				int godinaIF = imfPet.getGodina();
				double vrednostIF = Double.parseDouble(imfPet.getVrednost());
				
				ArrayList<KobsonCategoryJournal> categories = imfPet.getCategory();
				boolean stvarnoIIma = false;
				for(KobsonCategoryJournal category : categories){
					if (category.getVersionchange().equals("NEW;")) {
						String categoryID = "wos"+category.getIdCategory();
						if(category.getRang()==null)
							continue;
						else if(category.getRang().trim().equalsIgnoreCase(""))
							continue;
						else if(category.getRang().equalsIgnoreCase("0/0"))
							continue;
						
						double count = Double.parseDouble(category.getRang().split("/")[0]);
						double fraction = count / Double.parseDouble(category.getRang().split("/")[1]);
	
						if(!metricsDB.addResultMetrics(connCris, crissJournalID, "fiveYearsIF", "researchArea", categoryID, godinaIF, count, fraction)){
							System.out.println("Nije uneo pet kategoriju: "+  categoryID + " za godinu:" + godinaIF +" casopisa sa criss id:"+crissJournalID);
							System.exit(0);
						}
						stvarnoIIma = true;
					}
				}
				
				if(stvarnoIIma){
					if(!metricsDB.addResultMetrics(connCris, crissJournalID, "fiveYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null)){
						System.out.println("Nije uneo pet IF: "+  vrednostIF + " za godinu:" + godinaIF +" casopisa sa criss id:"+crissJournalID);
						System.exit(0);
					}
				}
			}
		}
	}
	
//	static protected void addIMFtoCRIS(KobsonJournal kobsonJournal, JournalDTO crissJournal){
//		//ubaci metriku za dvogodisnji if
//		List<KobsonImpactFactorJournal> impactFactorJournalLista = kobsonJournal.getImpaktFatorCasopisa();
//		for (KobsonImpactFactorJournal imf : impactFactorJournalLista) {
//			if(imf.getVersionchange().equals("NEW;")){
//				//ubaci metriku za if
//				int godinaIF = imf.getGodina();
//				double vrednostIF = Double.parseDouble(imf.getVrednost());
//				
//				ArrayList<KobsonCategoryJournal> categories = imf.getCategory();
//				boolean stvarnoIIma = false;
//				for(KobsonCategoryJournal category : categories){
//					if (category.getVersionchange().equals("NEW;")) {
//						String categoryID = "wos"+category.getIdCategory();
//						if(category.getRang()==null)
//							continue;
//						else if(category.getRang().trim().equalsIgnoreCase(""))
//							continue;
//						else if(category.getRang().equalsIgnoreCase("0/0"))
//							continue;
//						
//						double count = Double.parseDouble(category.getRang().split("/")[0]);
//						double fraction = count / Double.parseDouble(category.getRang().split("/")[1]);
//	
////						if(!metricsDB.addResultMetrics(connCris, crissJournal.getControlNumber(), "twoYearsIF", "researchArea", categoryID, godinaIF, count, fraction)){
////							System.out.println("Nije uneo kategoriju: "+  categoryID + " za godinu:" + godinaIF +" casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
////							System.exit(0);
////						}
//						stvarnoIIma = true;
//					}
//				}
//				
////				if(stvarnoIIma){
////					if(!metricsDB.addResultMetrics(connCris, crissJournal.getControlNumber(), "twoYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null)){
////						System.out.println("Nije uneo IF: "+  vrednostIF + " za godinu:" + godinaIF +" casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
////						System.exit(0);
////					}
////				}
//			}
//		}
//		
//		//ubaci metriku za pretogodisnji if
//		List<KobsonImpactFactorJournal> impactFactorPetGodinaJournalLista = kobsonJournal.getImpaktFatorCasopisaPetGodina();
//		for (KobsonImpactFactorJournal imfPet : impactFactorPetGodinaJournalLista) {
//			if(imfPet.getVersionchange().equals("NEW;")){
//				//ubaci metriku za if
//				int godinaIF = imfPet.getGodina();
//				double vrednostIF = Double.parseDouble(imfPet.getVrednost());
//				
//				ArrayList<KobsonCategoryJournal> categories = imfPet.getCategory();
//				boolean stvarnoIIma = false;
//				for(KobsonCategoryJournal category : categories){
//					if (category.getVersionchange().equals("NEW;")) {
//						String categoryID = "wos"+category.getIdCategory();
//						if(category.getRang()==null)
//							continue;
//						else if(category.getRang().trim().equalsIgnoreCase(""))
//							continue;
//						else if(category.getRang().equalsIgnoreCase("0/0"))
//							continue;
//						
//						double count = Double.parseDouble(category.getRang().split("/")[0]);
//						double fraction = count / Double.parseDouble(category.getRang().split("/")[1]);
//	
////						if(!metricsDB.addResultMetrics(connCris, crissJournal.getControlNumber(), "fiveYearsIF", "researchArea", categoryID, godinaIF, count, fraction)){
////							System.out.println("Nije uneo pet kategoriju: "+  categoryID + " za godinu:" + godinaIF +" casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
////							System.exit(0);
////						}
//						stvarnoIIma = true;
//					}
//				}
//				
////				if(stvarnoIIma){
////					if(!metricsDB.addResultMetrics(connCris, crissJournal.getControlNumber(), "fiveYearsIF", "value of metric", "value of IF", godinaIF, vrednostIF, null)){
////						System.out.println("Nije uneo pet IF: "+  vrednostIF + " za godinu:" + godinaIF +" casopisa:"+ crissJournal.getName() + " id:"+crissJournal.getControlNumber());
////						System.exit(0);
////					}
////				}
//			}
//		}
//	}
	
	public static boolean proveriDaLiPripadaListiDuplikataISIBAZE(KobsonJournal journalKobson){
		String ISSN = journalKobson.getISSN();
		String skraceniNaslov = journalKobson.getSkraceniNaslov();
		
		if (("0013-5585".equals(ISSN) && !"BIOMED TECH SKRACENI".equals(skraceniNaslov)) ||
				("0990-0632".equals(ISSN) && !"PROD ANIM".equals(skraceniNaslov)) ||
				("0908-4282".equals(ISSN) && !"APPL NEUROPSYCHOL".equals(skraceniNaslov)) ||
				("0035-6050".equals(ISSN) && !"RIV BIOL-BIOL FORUM".equals(skraceniNaslov)) ||
				("0379-5136".equals(ISSN) && !"INDIAN J MAR SCI".equals(skraceniNaslov)) ||
				("1124-318X".equals(ISSN) && !"EUR T TELECOMMUN".equals(skraceniNaslov)))
				return true;
	
		return false;
	}
	
	
	static protected void oppenConnection() {
		String luceneIndexPath = "";
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connCris = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			connCris.setAutoCommit(false);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connKobson = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + "kobson" + connectionParameters, username, password);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		
		luceneIndexPath = rb.getString("luceneIndex");
		Retriever.setIndexPath(luceneIndexPath);
	}
	
	static protected void closeConnection() {
		try {
			if (connCris != null) {
				connCris.close();
			}
		} catch (Exception e) {

		}
		
		try {
			if (connKobson != null) {
				connKobson.close();
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Poceo");
		UpdateCrissFromKobson.oppenConnection();
		KobsonDatabaseOperations.setConnection(connKobson);
		
//		UpdateCrissFromKobson.addCategoriesCrssDatabase();
		UpdateCrissFromKobson.updateCrisFromCobson();
		
		
		UpdateCrissFromKobson.closeConnection();
		System.out.println("ZAVRSIO");
	}

}
