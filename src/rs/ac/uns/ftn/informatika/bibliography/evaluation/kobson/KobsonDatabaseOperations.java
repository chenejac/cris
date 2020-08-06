package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonCategory;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonCategoryJournal;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonImpactFactorJournal;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans.KobsonJournal;


/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class KobsonDatabaseOperations {

    static protected Connection connection = null;
    static protected Statement statement = null;
    static protected PreparedStatement preparedStatement = null;
    static protected ResultSet resultSet = null;
    
    //journal
    protected KobsonJournal journal = new KobsonJournal();
    protected KobsonJournal loadedJournal = new KobsonJournal();
    
    protected boolean hasLoadedJournal = false;
    protected ArrayList<String> needUpdateJournal = new ArrayList<String>();
    
    //categories
    private static ArrayList<KobsonCategory> categoryList = new ArrayList<KobsonCategory>();
    
    //naziv fajla i sam fajl sa kobsona
	String nameServis  = null;
	File dataFileServis = null;
    
    int printlnCounter = 0;
    
    protected static final File inputDirServis130 = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/novo/servis130");
    protected static final File inputDirServis131 = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/novo/servis131/");
    protected static final File outputDir = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/");
    protected static final File fileKategorijeXML = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/Kategorije.XML");
    protected static final File outputDir130 = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/servis130/");
    protected static final File outputDir130Novi = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/servis130Novi/");
    protected static final File outputDir130NoviIF = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/servis130NoviIF/");
    protected static final File outputDir131 = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/Obradjeno/servis131/");
    protected static final File errorDir = new File("/media/storageNTFS/RECEIVE_BOX/KOBSON_sep2012/GRESKE/");
    
    
	//ocitavanje ID sacopisa iz baze podataka
	protected ArrayList<Integer> getUpdatedJournalIDFromDatabase()
	{
		ArrayList<Integer> retVal = null;
		try {
			statement = connection.createStatement();
			statement.execute ("select ID from journal WHERE VERSIONCHANGE NOT LIKE 'NOCHANGE;'");
			
			resultSet = statement.getResultSet();
			retVal = new ArrayList<Integer>();
			
			while (resultSet.next ()){
				retVal.add(resultSet.getInt("ID"));
			}
			
		} catch (Exception e) {
			System.out.println("Greska za getUpdatedJournalIDFromDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return retVal;
	}
	
    //uzima prvi slobodan id iz baze podataka za unos novog casopisa iz kosona 
	protected int getFreeJournalIDFromDatabase()
	{
		int freeJournalID = 1;	
		try {
			statement = connection.createStatement();
			statement.execute("select max(ID) AS FREEID from journal");
			resultSet = statement.getResultSet ();

			while (resultSet.next())
			{
				freeJournalID = resultSet.getInt ("FREEID") + 1;
			}
		} catch (Exception e) {
			System.out.println("Greska za pronalazenje maksimalnog elementa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return freeJournalID;
	}
	
	//ocitavanje ID sacopisa iz baze podataka
	protected int getJournalIDFromDatabase(String jid, String issn)
	{
		int id  = -1;
		
		try {

			statement = connection.createStatement();

			if((jid!=null) && (issn!=null))
			{
				statement.execute ("select ID from journal WHERE JID = \'" + jid + "\' OR ISSN = \'" + issn+"\'");
			}
			else if ((jid==null) && (issn==null))
			{
				System.out.println("getJournalIDFromDatabase jid i issn su null");
    			System.exit(0);
			}
			else if(jid!=null)
			{
				statement.execute ("select ID from journal WHERE JID = \'"+ jid+"\'");
			}
			else
			{
				statement.execute ("select ID from journal WHERE ISSN = \'"+ issn+"\'");
			}
			resultSet = statement.getResultSet ();		
			while (resultSet.next ())
			{
				id = resultSet.getInt ("ID");
			}
		} catch (Exception e) {
			System.out.println("Greska za getJournalIDFromDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return id;
	}
	
	//proverava da li postoji casopisa u bayi podataka kobsona
	protected boolean hasJournalInDatabase(String jid, String issn)
	{
		boolean found  = false;
		
		try {

			statement = connection.createStatement();
			if(jid!=null){
				statement.execute ("select ID, ISSN, NASLOV from journal WHERE JID = \'"+ jid+"\'");
				resultSet = statement.getResultSet ();
				int brojPogodaka = 0;
				while (resultSet.next ()){
					brojPogodaka=brojPogodaka+1;
					found = true;
					if(issn!=null){
						if (!issn.equalsIgnoreCase(resultSet.getString("ISSN"))) {
							System.out.println("Greska za hasJournalInDatabase same JID=" +jid +" and different ISSN, FILE(ISSN="+issn+
									";NASLOV=\""+journal.getNaslov()+"\") DATABASE(ISSN=" +resultSet.getString("ISSN") + 
									";NASLOV=\""+resultSet.getString("NASLOV")+"\") for "+nameServis);
							prebaciFajloveServisERROR("noSameISSNAsJID="+jid);
							System.exit(0);
						}
					}
				}
				
				if (brojPogodaka>1){
					System.out.println("hasJournalInDatabase jid su dali vise pogodaka dolazi do greske, duplira se jid kroz vise jourala for "+nameServis);
					prebaciFajloveServisERROR("hasMoreRecordsJID");
	    			System.exit(0);
				}
			}
			
			if (found == true) {
				return found;
			}
			
			if(issn!=null){
				statement.execute ("select ID, JID, NASLOV from journal WHERE ISSN = \'"+ issn+"\'");
				resultSet = statement.getResultSet ();
				int brojPogodaka = 0;
				while (resultSet.next ()){
					brojPogodaka=brojPogodaka+1;
					found = true;
					if(jid!=null){
						if (!jid.equalsIgnoreCase(resultSet.getString("JID"))) {
							System.out.println("Greska za hasJournalInDatabase same ISSN=" +issn +" and different JID, FILE(JID="+jid+
									";NASLOV=\""+journal.getNaslov()+"\") DATABASE(JID=" +resultSet.getString("JID") + 
									";NASLOV=\""+resultSet.getString("NASLOV")+"\") for "+nameServis);
							prebaciFajloveServisERROR("noSameJIDAsISSN="+issn);
							System.exit(0);
						}
					}
				}
				
				if (brojPogodaka>1){
					System.out.println("hasJournalInDatabase issn su dali vise pogodaka dolazi do greske, duplira se issn kroz vise jourala for "+nameServis);
					prebaciFajloveServisERROR("hasMoreRecordsISSN");
	    			System.exit(0);
				}
			}

			if ((jid==null) && (issn==null)){
				System.out.println("hasJournalInDatabase jid i issn su null for "+nameServis);
				prebaciFajloveServisERROR("noJidAndISSN");
    			System.exit(0);
			}
			
		} catch (Exception e) {
			System.out.println("Greska za hasJournalInDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return found;
	}
	//proverava da li za novi casopis postoji casopis u bazi podataka kobsona sa istim imenom
	protected boolean hasJournalInDatabase(String naslov)
	{
		boolean found  = false;
		try {
			preparedStatement = connection.prepareStatement("select ID, ISSN, JID from journal WHERE NASLOV like ?");
			if(naslov!=null){
				preparedStatement.setString(1, naslov);
				preparedStatement.execute();
				resultSet = preparedStatement.getResultSet ();
				int brojPogodaka = 0;
				while (resultSet.next ()){
					brojPogodaka=brojPogodaka+1;
					found = true;
					System.out.println("Casopis sa JID="+journal.getJid()+ " ISSN=" + journal.getISSN() + 
							" ima isti naslov \""+ naslov+ "\" kao u Bazi podataka JID="+resultSet.getString("JID") +" ISSN=" + resultSet.getString("ISSN") );
				}			
				if (brojPogodaka>1){
					System.out.println("hasJournalInDatabase naslov vise pogodaka dolazi do greske, duplira se naslov kroz vise jourala for "+nameServis);
//					System.exit(0);
				}
			}
		} catch (Exception e) {
			System.out.println("Greska za hasJournalInDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return found;
	}
	
	//proverava da li za novi casopis postoji casopis u bazi podataka kobsona sa istim imenom i da ima impakt faktor
	protected boolean hasJournalInDatabaseIMF(String naslov)
	{
		boolean found  = false;
		int [] idOcitanog = null;
		String [] issnOcitanog = null;
		
		try {
			if(naslov!=null){
				preparedStatement = connection.prepareStatement("select ID, ISSN, JID from journal WHERE NASLOV like ?");
				preparedStatement.setString(1, naslov);
				preparedStatement.execute();
				resultSet = preparedStatement.getResultSet ();
				
				int rowcount = 0;
				if (resultSet.last()) {
				  rowcount = resultSet.getRow();
				  resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
				}
				
				if (rowcount>0) {
					idOcitanog = new int [rowcount];
					issnOcitanog = new String [rowcount];
				}
				
				int brojPogodaka = 0;
				if (resultSet.next()) {
					idOcitanog[brojPogodaka] = resultSet.getInt("ID");
					issnOcitanog[brojPogodaka] = resultSet.getString("ISSN");
					brojPogodaka=brojPogodaka+1;
					found  = true;
				}
			}
		} catch (Exception e) {
			System.out.println("Greska za hasJournalInDatabaseIMF casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		if (found) {
			found = false;
			try {
				for (int i = 0; i < idOcitanog.length; i++) {
					preparedStatement = connection.prepareStatement("select COUNT(*) AS BROJ from impactfactorjournal WHERE JOURNAL_ID = ?");
					preparedStatement.setInt(1, idOcitanog[i]);
					preparedStatement.execute();
					resultSet = preparedStatement.getResultSet ();
						
					if (resultSet.next()) {
							if(resultSet.getInt("BROJ")>0) {
								found  = true;
								System.out.println("Casopis sa JID="+journal.getJid()+ " ISSN=" + journal.getISSN() + 
										" ima isti naslov \""+ naslov+ "\" kao u Bazi podataka ID="+idOcitanog[i] + " ISSN="+issnOcitanog[i]+" koji ima impakt faktor");
							}
					}
				}
			} catch (Exception e) {
				System.out.println("Greska za hasJournalInDatabaseIMF casopisa u bazi ");
				e.printStackTrace();
				System.exit(0);
			} finally {
				closeStatement();
			}
		}
		return found;
	}
	
	protected void loadJournalBasicInfFromDatabase(int id)
	{
		loadedJournal.clear();
		hasLoadedJournal = false;
		try {
			statement = connection.createStatement();	
			String sql = "select ID, JID, ISSN, NASLOV, SKRACENINASLOV, STATUS, TIPDOKUMENTA, UCESTALOST, JEZIK, PRVIBROJ, ABSTRAKT, ALTERNATIVNINASLOVI, " +
					"AKTIVNEKATEGORIJE, UCURRENTCONTENTS, SCIENCECITATION, " +
					"SPONZOR, IZDAVAC, ADRESA, TELEFON, FAX, EMAIL, URL, ZEMLJA, " +
					"CRIS_ID, VERSIONCHANGE, DEPENDENCY from journal WHERE ID = " + id;
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();		
			while (resultSet.next())
			{
				loadedJournal.setId(resultSet.getInt ("ID"));
				loadedJournal.setJid(resultSet.getString("JID"));
				loadedJournal.setISSN(resultSet.getString("ISSN"));
				loadedJournal.setNaslov(resultSet.getString("NASLOV"));
				loadedJournal.setSkraceniNaslov(resultSet.getString("SKRACENINASLOV"));
				loadedJournal.setStatus(resultSet.getString("STATUS"));
				loadedJournal.setTipDokumenta(resultSet.getString("TIPDOKUMENTA"));
				loadedJournal.setUcestalost(resultSet.getString("UCESTALOST"));
				loadedJournal.setJezik(resultSet.getString("JEZIK"));
				loadedJournal.setPrviBroj(resultSet.getString("PRVIBROJ"));
				loadedJournal.setAbstrakt(resultSet.getString("ABSTRAKT"));
				loadedJournal.setAlternativniNaslovi(resultSet.getString("ALTERNATIVNINASLOVI"));
				
				
				String[] aktivneKategorije = resultSet.getString("AKTIVNEKATEGORIJE").split(";");
				for (int i = 0; i < aktivneKategorije.length; i++) {
					loadedJournal.getKategorije().add(aktivneKategorije[i]);
				}
				loadedJournal.setUCurrentContents(resultSet.getString("UCURRENTCONTENTS"));
				loadedJournal.setScienceCitation(resultSet.getString("SCIENCECITATION"));
				
				
				loadedJournal.setSponzor(resultSet.getString("SPONZOR"));
				loadedJournal.setIzdavac(resultSet.getString("IZDAVAC"));
				loadedJournal.setAdresa(resultSet.getString("ADRESA"));
				loadedJournal.setTelefon(resultSet.getString("TELEFON"));
				loadedJournal.setFax(resultSet.getString("FAX"));
				loadedJournal.seteMail(resultSet.getString("EMAIL"));
				loadedJournal.setURL(resultSet.getString("URL"));
				loadedJournal.setZemlja(resultSet.getString("ZEMLJA"));
				
				loadedJournal.setCRIS_ID(resultSet.getString("CRIS_ID"));
				loadedJournal.setVERSIONCHANGE(resultSet.getString("VERSIONCHANGE"));
				loadedJournal.setDEPENDENCY(resultSet.getString("DEPENDENCY"));
				
				hasLoadedJournal = true;
			}
		} catch (Exception e) {
			hasLoadedJournal = false;
			System.out.println("Greska za loadJournalBasicInfFromDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
	}
	
	protected ArrayList<String> compareJournalFromFileAndDatabase()
	{
		needUpdateJournal.clear();
		
		//prvi deo
//		int id="";	
		if(journal.getId() == 0){
			journal.setId(loadedJournal.getId());
		}
		//Kobson identifikator
//		String jid="";	
		if(!journal.getJid().equalsIgnoreCase("") && !loadedJournal.getJid().equalsIgnoreCase("") && !journal.getJid().equalsIgnoreCase(loadedJournal.getJid())){
			System.out.println("Greska za compareJournalFromFaleAndDatabase journal JID= "+journal.getJid()+
					" se ne poklapa sa bazom JID= " +loadedJournal.getJid()+ " for "+nameServis);
			prebaciFajloveServisERROR("noSameJIDAsID="+loadedJournal.getId());
			System.exit(0);
		}
		else if(journal.getJid().equalsIgnoreCase("") && !loadedJournal.getJid().equalsIgnoreCase("")){
			journal.setJid(loadedJournal.getJid());
		}
		else if(!journal.getJid().equalsIgnoreCase("") && loadedJournal.getJid().equalsIgnoreCase("")){
			needUpdateJournal.add("JID");
		}
		
		//Podaci o casopisu
//		String ISSN ="";
		if(!journal.getISSN().equalsIgnoreCase("") && !loadedJournal.getISSN().equalsIgnoreCase("") && !journal.getISSN().equalsIgnoreCase(loadedJournal.getISSN())){
			System.out.println("Greska za compareJournalFromFaleAndDatabase journal ISSN= "+journal.getISSN()+
					" se ne poklapa sa bazom ISSN= " +loadedJournal.getISSN()+ " for "+nameServis);
			prebaciFajloveServisERROR("noSameISSNAsID="+loadedJournal.getId());
			System.exit(0);
		}
		else if(journal.getISSN().equalsIgnoreCase("") && !loadedJournal.getISSN().equalsIgnoreCase("")){
			journal.setISSN(loadedJournal.getISSN());
		}
		else if(!journal.getISSN().equalsIgnoreCase("") && loadedJournal.getISSN().equalsIgnoreCase("")){
			needUpdateJournal.add("ISSN");
		}
		
//		String naslov ="";
		if(!journal.getNaslov().equalsIgnoreCase("") && !loadedJournal.getNaslov().equalsIgnoreCase("") && !journal.getNaslov().equalsIgnoreCase(loadedJournal.getNaslov())){
			if(!journal.getISSN().equalsIgnoreCase("0395-2649") && !journal.getISSN().equalsIgnoreCase("0307-661x") 
					&& !journal.getISSN().equalsIgnoreCase("0003-9632") && !journal.getISSN().equalsIgnoreCase("0016-7606")
					&& !journal.getISSN().equalsIgnoreCase("0533-7712")) 
				needUpdateJournal.add("NASLOV");
		}
		else if(journal.getNaslov().equalsIgnoreCase("") && !loadedJournal.getNaslov().equalsIgnoreCase("")){
			journal.setNaslov(loadedJournal.getNaslov());
		}
		else if(!journal.getNaslov().equalsIgnoreCase("") && loadedJournal.getNaslov().equalsIgnoreCase("")){
			needUpdateJournal.add("NASLOV");
		}
		
//		String skraceniNaslov ="";
		if(!journal.getSkraceniNaslov().equalsIgnoreCase("") && !loadedJournal.getSkraceniNaslov().equalsIgnoreCase("") && !journal.getSkraceniNaslov().equalsIgnoreCase(loadedJournal.getSkraceniNaslov())){
			needUpdateJournal.add("SKRACENINASLOV");
		}
		else if(journal.getSkraceniNaslov().equalsIgnoreCase("") && !loadedJournal.getSkraceniNaslov().equalsIgnoreCase("")){
			journal.setSkraceniNaslov(loadedJournal.getSkraceniNaslov());
		}
		else if(!journal.getSkraceniNaslov().equalsIgnoreCase("") && loadedJournal.getSkraceniNaslov().equalsIgnoreCase("")){
			needUpdateJournal.add("SKRACENINASLOV");
		}
		
//		String status ="";
		if(!journal.getStatus().equalsIgnoreCase("") && !loadedJournal.getStatus().equalsIgnoreCase("") && !journal.getStatus().equalsIgnoreCase(loadedJournal.getStatus())){
			needUpdateJournal.add("STATUS");
		}
		else if(journal.getStatus().equalsIgnoreCase("")  && !loadedJournal.getStatus().equalsIgnoreCase("")){
			journal.setStatus(loadedJournal.getStatus());
		}
		else if(!journal.getStatus().equalsIgnoreCase("") && loadedJournal.getStatus().equalsIgnoreCase("")){
			needUpdateJournal.add("STATUS");
		}
		
//		String tipDokumenta ="";
		if(!journal.getTipDokumenta().equalsIgnoreCase("") && !loadedJournal.getTipDokumenta().equalsIgnoreCase("") && !journal.getTipDokumenta().equalsIgnoreCase(loadedJournal.getTipDokumenta())){
			needUpdateJournal.add("TIPDOKUMENTA");
		}
		else if(journal.getTipDokumenta().equalsIgnoreCase("")  && !loadedJournal.getTipDokumenta().equalsIgnoreCase("")){
			journal.setTipDokumenta(loadedJournal.getTipDokumenta());
		}
		else if(!journal.getTipDokumenta().equalsIgnoreCase("") && loadedJournal.getTipDokumenta().equalsIgnoreCase("")){
			needUpdateJournal.add("TIPDOKUMENTA");
		}
		
//		String ucestalost ="";
		if(!journal.getUcestalost().equalsIgnoreCase("") && !loadedJournal.getUcestalost().equalsIgnoreCase("") && !journal.getUcestalost().equalsIgnoreCase(loadedJournal.getUcestalost())){
			needUpdateJournal.add("UCESTALOST");
		}
		else if(journal.getUcestalost().equalsIgnoreCase("") && !loadedJournal.getUcestalost().equalsIgnoreCase(""))
		{
			journal.setUcestalost(loadedJournal.getUcestalost());
		}
		else if(!journal.getUcestalost().equalsIgnoreCase("") && loadedJournal.getUcestalost().equalsIgnoreCase("")){
			needUpdateJournal.add("UCESTALOST");
		}
		
//		String jezik ="";
		if(!journal.getJezik().equalsIgnoreCase("") && !loadedJournal.getJezik().equalsIgnoreCase("") && !journal.getJezik().equalsIgnoreCase(loadedJournal.getJezik())){
			needUpdateJournal.add("JEZIK");
		}
		else if(journal.getJezik().equalsIgnoreCase("") && !loadedJournal.getJezik().equalsIgnoreCase("")){
			journal.setJezik(loadedJournal.getJezik());
		}
		else if(!journal.getJezik().equalsIgnoreCase("") && loadedJournal.getJezik().equalsIgnoreCase("")){
			needUpdateJournal.add("JEZIK");
		}
		
//		String prviBroj ="";
		if(!journal.getPrviBroj().equalsIgnoreCase("") && !loadedJournal.getPrviBroj().equalsIgnoreCase("") && !journal.getPrviBroj().equalsIgnoreCase(loadedJournal.getPrviBroj())){
			needUpdateJournal.add("PRVIBROJ");
		}
		else if(journal.getPrviBroj().equalsIgnoreCase("") && !loadedJournal.getPrviBroj().equalsIgnoreCase("")){
			journal.setPrviBroj(loadedJournal.getPrviBroj());
		}
		else if(!journal.getPrviBroj().equalsIgnoreCase("") && loadedJournal.getPrviBroj().equalsIgnoreCase("")){
			needUpdateJournal.add("PRVIBROJ");
		}
		
//		String abstrakt ="";
		if(!journal.getAbstrakt().equalsIgnoreCase("") && !loadedJournal.getAbstrakt().equalsIgnoreCase("") && !journal.getAbstrakt().equalsIgnoreCase(loadedJournal.getAbstrakt())){
			needUpdateJournal.add("ABSTRAKT");
		}
		else if(journal.getAbstrakt().equalsIgnoreCase("") && !loadedJournal.getAbstrakt().equalsIgnoreCase("")){
			journal.setAbstrakt(loadedJournal.getAbstrakt());
		}
		else if(!journal.getAbstrakt().equalsIgnoreCase("") && loadedJournal.getAbstrakt().equalsIgnoreCase("")){
			needUpdateJournal.add("ABSTRAKT");
		}
		
//		String alternativniNaslovi ="";
		if(!journal.getAlternativniNaslovi().equalsIgnoreCase("") && !loadedJournal.getAlternativniNaslovi().equalsIgnoreCase("") && !journal.getAlternativniNaslovi().equalsIgnoreCase(loadedJournal.getAlternativniNaslovi())){
			needUpdateJournal.add("ALTERNATIVNINASLOVI");
		}
		else if(journal.getAlternativniNaslovi().equalsIgnoreCase("") && !loadedJournal.getAlternativniNaslovi().equalsIgnoreCase("")){
			journal.setAlternativniNaslovi(loadedJournal.getAlternativniNaslovi());
		}
		else if(!journal.getAlternativniNaslovi().equalsIgnoreCase("") && loadedJournal.getAlternativniNaslovi().equalsIgnoreCase("")){
			needUpdateJournal.add("ALTERNATIVNINASLOVI");
		}
		
		//drugi deo
//		List<String> kategorije = new ArrayList<String>();
		if(journal.getKategorije().size() != loadedJournal.getKategorije().size()){
			needUpdateJournal.add("AKTIVNEKATEGORIJE");
		}
		else {
			for (int i = 0; i < journal.getKategorije().size(); i++) {
				if (!journal.getKategorije().get(i).equalsIgnoreCase(loadedJournal.getKategorije().get(i))) {
					needUpdateJournal.add("AKTIVNEKATEGORIJE");
					break;
				}
			}
		}
		
//		String UCurrentContents="";
		if(!journal.getUCurrentContents().equalsIgnoreCase("") && !loadedJournal.getUCurrentContents().equalsIgnoreCase("") && !journal.getUCurrentContents().equalsIgnoreCase(loadedJournal.getUCurrentContents())){
			needUpdateJournal.add("UCURRENTCONTENTS");
		}
		else if(journal.getUCurrentContents().equalsIgnoreCase("") && !loadedJournal.getUCurrentContents().equalsIgnoreCase("")){
			needUpdateJournal.add("UCURRENTCONTENTS");
		}
		else if(!journal.getUCurrentContents().equalsIgnoreCase("") && loadedJournal.getUCurrentContents().equalsIgnoreCase("")){
			needUpdateJournal.add("UCURRENTCONTENTS");
		}
		
//		String scienceCitation="";
		if(!journal.getScienceCitation().equalsIgnoreCase("") && !loadedJournal.getScienceCitation().equalsIgnoreCase("") && !journal.getScienceCitation().equalsIgnoreCase(loadedJournal.getScienceCitation())){
			needUpdateJournal.add("SCIENCECITATION");
		}
		else if(journal.getScienceCitation().equalsIgnoreCase("") && !loadedJournal.getScienceCitation().equalsIgnoreCase("")){
			needUpdateJournal.add("SCIENCECITATION");
		}
		else if(!journal.getScienceCitation().equalsIgnoreCase("") && loadedJournal.getScienceCitation().equalsIgnoreCase("")){
			needUpdateJournal.add("SCIENCECITATION");
		}
		
		//treci deo
//		String sponzor="";
		if(!journal.getSponzor().equalsIgnoreCase("") && !loadedJournal.getSponzor().equalsIgnoreCase("") && !journal.getSponzor().equalsIgnoreCase(loadedJournal.getSponzor())){
			needUpdateJournal.add("SPONZOR");
		}
		else if(journal.getSponzor().equalsIgnoreCase("") && !loadedJournal.getSponzor().equalsIgnoreCase("")){
			journal.setSponzor(loadedJournal.getSponzor());
		}
		else if(!journal.getSponzor().equalsIgnoreCase("") && loadedJournal.getSponzor().equalsIgnoreCase("")){
			needUpdateJournal.add("SPONZOR");
		}
		
//		String izdavac="";
		if(!journal.getIzdavac().equalsIgnoreCase("") && !loadedJournal.getIzdavac().equalsIgnoreCase("") && !journal.getIzdavac().equalsIgnoreCase(loadedJournal.getIzdavac())){
			needUpdateJournal.add("IZDAVAC");
		}
		else if(journal.getIzdavac().equalsIgnoreCase("") && !loadedJournal.getIzdavac().equalsIgnoreCase("")){
			journal.setIzdavac(loadedJournal.getIzdavac());
		}
		else if(!journal.getIzdavac().equalsIgnoreCase("") && loadedJournal.getIzdavac().equalsIgnoreCase("")){
			needUpdateJournal.add("IZDAVAC");
		}
		
//		String adresa="";
		if(!journal.getAdresa().equalsIgnoreCase("") && !loadedJournal.getAdresa().equalsIgnoreCase("") && !journal.getAdresa().equalsIgnoreCase(loadedJournal.getAdresa())){
			needUpdateJournal.add("ADRESA");
		}
		else if(journal.getAdresa().equalsIgnoreCase("") && !loadedJournal.getAdresa().equalsIgnoreCase("")){
			journal.setAdresa(loadedJournal.getAdresa());
		}
		else if(!journal.getAdresa().equalsIgnoreCase("") && loadedJournal.getAdresa().equalsIgnoreCase("")){
			needUpdateJournal.add("ADRESA");
		}
		
//		String telefon="";
		if(!journal.getTelefon().equalsIgnoreCase("") && !loadedJournal.getTelefon().equalsIgnoreCase("") && !journal.getTelefon().equalsIgnoreCase(loadedJournal.getTelefon())){
			needUpdateJournal.add("TELEFON");
		}
		else if(journal.getTelefon().equalsIgnoreCase("") && !loadedJournal.getTelefon().equalsIgnoreCase("")){
			journal.setTelefon(loadedJournal.getTelefon());
		}
		else if(!journal.getTelefon().equalsIgnoreCase("") && loadedJournal.getTelefon().equalsIgnoreCase("")){
			needUpdateJournal.add("TELEFON");
		}
		
//		String fax="";
		if(!journal.getFax().equalsIgnoreCase("") && !loadedJournal.getFax().equalsIgnoreCase("") && !journal.getFax().equalsIgnoreCase(loadedJournal.getFax())){
			needUpdateJournal.add("FAX");
		}
		else if(journal.getFax().equalsIgnoreCase("") && !loadedJournal.getFax().equalsIgnoreCase("")){
			journal.setFax(loadedJournal.getFax());
		}
		else if(!journal.getFax().equalsIgnoreCase("") && loadedJournal.getFax().equalsIgnoreCase("")){
			needUpdateJournal.add("FAX");
		}
		
//		String eMail="";
		if(!journal.geteMail().equalsIgnoreCase("") && !loadedJournal.geteMail().equalsIgnoreCase("") && !journal.geteMail().equalsIgnoreCase(loadedJournal.geteMail())){
			needUpdateJournal.add("EMAIL");
		}
		else if(journal.geteMail().equalsIgnoreCase("") && !loadedJournal.geteMail().equalsIgnoreCase("")){
			journal.seteMail(loadedJournal.geteMail());
		}
		else if(!journal.geteMail().equalsIgnoreCase("") && loadedJournal.geteMail().equalsIgnoreCase("")){
			needUpdateJournal.add("EMAIL");
		}

//		String URL="";
		if(!journal.getURL().equalsIgnoreCase("") && !loadedJournal.getURL().equalsIgnoreCase("") && !journal.getURL().equalsIgnoreCase(loadedJournal.getURL())){
			needUpdateJournal.add("URL");
		}
		else if(journal.getURL().equalsIgnoreCase("") && !loadedJournal.getURL().equalsIgnoreCase("")){
			journal.setURL(loadedJournal.getURL());
		}
		else if(!journal.getURL().equalsIgnoreCase("") && loadedJournal.getURL().equalsIgnoreCase("")){
			needUpdateJournal.add("URL");
		}

//		String zemlja="";
		if(!journal.getZemlja().equalsIgnoreCase("") && !loadedJournal.getZemlja().equalsIgnoreCase("") && !journal.getZemlja().equalsIgnoreCase(loadedJournal.getZemlja())){
			needUpdateJournal.add("ZEMLJA");
		}
		else if(journal.getZemlja().equalsIgnoreCase("") && !loadedJournal.getZemlja().equalsIgnoreCase("")){
			journal.setZemlja(loadedJournal.getZemlja());
		}
		else if(!journal.getZemlja().equalsIgnoreCase("") && loadedJournal.getZemlja().equalsIgnoreCase("")){
			needUpdateJournal.add("ZEMLJA");
		}
		
		return needUpdateJournal;
	}
	
	
	
	protected KobsonJournal loadJournalToXMLFromDatabase(int id)
	{
		KobsonJournal tempJournal = new KobsonJournal();
		
		String jid = "";
		String ISSN = "";
		String naslov = "";
		String skraceniNaslov = "";
		
		tempJournal.clear();
		tempJournal.setId(id);
		
		try {
			statement = connection.createStatement();	
			String sql = "select JID, ISSN, NASLOV, SKRACENINASLOV, STATUS, TIPDOKUMENTA, UCESTALOST, JEZIK, PRVIBROJ  from journal WHERE ID = " + id;
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();		
			while (resultSet.next ())
			{
				jid = resultSet.getString("JID");
				ISSN = resultSet.getString("ISSN");
				naslov = resultSet.getString("NASLOV");
				skraceniNaslov = resultSet.getString("SKRACENINASLOV");
				
				tempJournal.setJid(jid);
				tempJournal.setISSN(ISSN);
				tempJournal.setNaslov(naslov);
				tempJournal.setSkraceniNaslov(skraceniNaslov);
			}
		} catch (Exception e) {
			System.out.println("Greska za loadJournal131FromDatabase casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		//load journal IF godina data
		try {
			statement = connection.createStatement();	
			String sql = "select GODINA, VREDNOST  from impactfactorjournal WHERE JOURNAL_ID = " + id + " ORDER BY GODINA ASC";
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();	
			List<KobsonImpactFactorJournal> impactFactorJournalLista = tempJournal.getImpaktFatorCasopisa();
			
			while (resultSet.next ())
			{
				int godina = resultSet.getInt("GODINA");
				String vrednost = resultSet.getString("VREDNOST");
				impactFactorJournalLista.add(new KobsonImpactFactorJournal(id, godina, vrednost));
				
			}
		} catch (Exception e) {
			System.out.println("Greska za loadJournalToXMLFromDatabase IF casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		return tempJournal;
		
	}
	
	protected void loadJournalAdvInfFromDatabase(int id)
	{
		loadedJournal.clear();
		hasLoadedJournal = false;
		//load journal basic data
		loadJournalBasicInfFromDatabase(id);
		if(hasLoadedJournal==false)
			return;
		
		hasLoadedJournal = false;
		
		//load journal IF godina data
		List<KobsonImpactFactorJournal> impactFactorJournalLista = loadedJournal.getImpaktFatorCasopisa();
		try {
			statement = connection.createStatement();	
			String sql = "select GODINA, VREDNOST, VERSIONCHANGE from impactfactorjournal WHERE JOURNAL_ID = " + id;
			statement.execute (sql);
			resultSet = statement.getResultSet ();		
			while (resultSet.next ()){
				int godina = resultSet.getInt("GODINA");
				String vrednost = resultSet.getString("VREDNOST");
				String versionchange = resultSet.getString("VERSIONCHANGE");
				impactFactorJournalLista.add(new KobsonImpactFactorJournal(id, godina, vrednost, versionchange));
			}	
		} catch (Exception e) {
			hasLoadedJournal = false;
			System.out.println("Greska za loadJournalAdvInfFromDatabase IF casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		//load journal IF PET data
		List<KobsonImpactFactorJournal> impactFactorJournalListaPetGodina = loadedJournal.getImpaktFatorCasopisaPetGodina();
		try {

			statement = connection.createStatement();	
			String sql = "select GODINA, VREDNOST, VERSIONCHANGE  from impactfactorjournalfiveyear WHERE JOURNAL_ID = " + id;
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();
			while (resultSet.next ()){
				int godina = resultSet.getInt("GODINA");
				String vrednost = resultSet.getString("VREDNOST");
				String versionchange = resultSet.getString("VERSIONCHANGE");
				impactFactorJournalListaPetGodina.add(new KobsonImpactFactorJournal(id, godina, vrednost, versionchange));
			}
		} catch (Exception e) {
			hasLoadedJournal = false;
			System.out.println("Greska za loadJournalAdvInfFromDatabase IF PET casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		//load journal JOURNALCATEGORY data
		try {

			statement = connection.createStatement();	
			String sql = "select CATEGORY_ID, GODINA, RANG, VERSIONCHANGE  from journalcategory WHERE JOURNAL_ID = " + id;
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();
			while (resultSet.next ()){
				int categoryID = resultSet.getInt("CATEGORY_ID");
				int godina = resultSet.getInt("GODINA");
				String rang = resultSet.getString("RANG");
				String versionchange = resultSet.getString("VERSIONCHANGE");
				
				for (KobsonImpactFactorJournal imf : impactFactorJournalLista) {
					if(godina != imf.getGodina()){
						continue;
					}
					imf.addCategory(new KobsonCategoryJournal(id, categoryID, godina, 
							rang, null, null, versionchange));				
					break;
				}
			}
		} catch (Exception e) {
			hasLoadedJournal = false;
			System.out.println("Greska za loadJournalAdvInfFromDatabase IFCategory casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		//load journal IF PET data
		try {

			statement = connection.createStatement();	
			String sql = "select CATEGORY_ID, GODINA, RANG, VERSIONCHANGE  from journalcategoryfiveyear WHERE JOURNAL_ID = " + id;
			statement.execute (sql);
			
			resultSet = statement.getResultSet ();
			
			while (resultSet.next ()){
				int categoryID = resultSet.getInt("CATEGORY_ID");
				int godina = resultSet.getInt("GODINA");
				String rang = resultSet.getString("RANG");
				String versionchange = resultSet.getString("VERSIONCHANGE");

				for (KobsonImpactFactorJournal imfF : impactFactorJournalListaPetGodina) {
					if(godina != imfF.getGodina()){
						continue;
					}
					imfF.addCategory(new KobsonCategoryJournal(id, categoryID, godina, 
							rang, null, null,versionchange));				
					break;
				}
			}
		} catch (Exception e) {
			hasLoadedJournal = false;
			System.out.println("Greska za loadJournalAdvInfFromDatabase IFCategory PET casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		
		hasLoadedJournal = true;
	}
	
	protected ArrayList<String> compareJournalIfANDCategoryFromFileAndDatabase()
	{
		needUpdateJournal.clear();
		
//		String skraceniNaslov ="";
		if(!journal.getSkraceniNaslov().equalsIgnoreCase("") && !loadedJournal.getSkraceniNaslov().equalsIgnoreCase("") && !journal.getSkraceniNaslov().equalsIgnoreCase(loadedJournal.getSkraceniNaslov())){
			needUpdateJournal.add("SKRACENINASLOV");
		}
		else if(journal.getSkraceniNaslov().equalsIgnoreCase("") && !loadedJournal.getSkraceniNaslov().equalsIgnoreCase("")){
			journal.setSkraceniNaslov(loadedJournal.getSkraceniNaslov());
		}
		else if(!journal.getSkraceniNaslov().equalsIgnoreCase("") && loadedJournal.getSkraceniNaslov().equalsIgnoreCase("")){
			needUpdateJournal.add("SKRACENINASLOV");
		}
		
		List<KobsonImpactFactorJournal> impactFactorJournalLista = loadedJournal.getImpaktFatorCasopisa();
		for (KobsonImpactFactorJournal imf : impactFactorJournalLista) {
			List<KobsonImpactFactorJournal> impactFactorJournalListaFile = journal.getImpaktFatorCasopisa();
			for (KobsonImpactFactorJournal imfF : impactFactorJournalListaFile) {
				if(imf.getGodina() != imfF.getGodina()){
					continue;
				}
				if ((imf.getGodina() == imfF.getGodina()) && (!imf.getVrednost().equalsIgnoreCase(imfF.getVrednost())) && (!imfF.getVrednost().equalsIgnoreCase("0.000"))) {
					System.out.println("Greska za compareJournalIfANDCategoryFromFileAndDatabase journal (\"" + journal.getNaslov() + "\", ISSN="+journal.getISSN()+ ") ImpactFactor za" +
							" godinu= "+imf.getGodina() + " sa vrednoscu="+imf.getVrednost()+ 
							" se ne poklapa sa bazom vrednoscu= " +imfF.getVrednost()+ " for "+nameServis);
					prebaciFajloveServisERROR("noSameIMFAsID="+loadedJournal.getId());
					System.exit(0);
				}
				
				ArrayList<KobsonCategoryJournal> category = imf.getCategory();
				ArrayList<KobsonCategoryJournal> categoryF = imfF.getCategory();
				for (KobsonCategoryJournal cat : category) {
					for (KobsonCategoryJournal catF : categoryF) {
						if (cat.getIdCategory()!= catF.getIdCategory()) {
							continue;
						}
						else{
							if (!cat.getRang().equalsIgnoreCase(catF.getRang()) && !catF.getRang().equalsIgnoreCase("0/0")){
								System.out.println("Greska za compareJournalIfANDCategoryFromFileAndDatabase journal (\"" + journal.getNaslov() + "\", ISSN="+journal.getISSN()+ ") Category za" +
										" godinu= "+imf.getGodina() + " sa rangom="+cat.getRang()+ 
										" se ne poklapa sa bazom rangom= " +catF.getRang()+ " for "+nameServis);
								prebaciFajloveServisERROR("noSameCategoryAsID="+loadedJournal.getId());
								System.exit(0);
							}
							break;
						}	
					}
				}
				break;
			}
		}
		
		List<KobsonImpactFactorJournal> impactFactorJournalListaPetGodina = loadedJournal.getImpaktFatorCasopisaPetGodina();
		for (KobsonImpactFactorJournal imf : impactFactorJournalListaPetGodina) {
			List<KobsonImpactFactorJournal> impactFactorJournalListaFilePetGodina = journal.getImpaktFatorCasopisaPetGodina();
			for (KobsonImpactFactorJournal imfF : impactFactorJournalListaFilePetGodina) {
				if(imf.getGodina() != imfF.getGodina()){
					continue;
				}
				if ((imf.getGodina() == imfF.getGodina()) &&  (!imf.getVrednost().equalsIgnoreCase(imfF.getVrednost())) && (!imfF.getVrednost().equalsIgnoreCase("0.000"))) {
					System.out.println("Greska za compareJournalIfANDCategoryFromFileAndDatabase journal (\"" + journal.getNaslov() + "\", ISSN="+journal.getISSN()+ ") ImpactFactor PET za" +
							" godinu= "+imf.getGodina() + " sa vrednoscu="+imf.getVrednost()+ 
							" se ne poklapa sa bazom vrednoscu= " +imfF.getVrednost()+ " for "+nameServis);
					prebaciFajloveServisERROR("noSameIMFPETAsID="+loadedJournal.getId());
					System.exit(0);
				}

				ArrayList<KobsonCategoryJournal> category = imf.getCategory();
				ArrayList<KobsonCategoryJournal> categoryF = imfF.getCategory();
				
				for (KobsonCategoryJournal cat : category) {
					for (KobsonCategoryJournal catF : categoryF) {
						if (cat.getIdCategory()!= catF.getIdCategory()) {
							continue;
						}
						else{
							if (!cat.getRang().equalsIgnoreCase(catF.getRang()) && !catF.getRang().equalsIgnoreCase("0/0")){
								System.out.println("Greska za compareJournalIfANDCategoryFromFileAndDatabase journal (\"" + journal.getNaslov() + "\", ISSN="+journal.getISSN()+ ") Category PET za" +
										" godinu= "+imf.getGodina() + " sa rangom="+cat.getRang()+ 
										" se ne poklapa sa bazom rangom= " +catF.getRang()+ " for "+nameServis);
								prebaciFajloveServisERROR("noSameCategoryPETAsID="+loadedJournal.getId());
								System.exit(0);
							}
							break;
						}	
					}

				}
				break;
			}
		}
		return needUpdateJournal;
	}
	
	protected void insertJournalToDatabase()
	{
		printlnCounter=printlnCounter+1;
		try {

			preparedStatement = connection.prepareStatement("insert into  journal values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
							"?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, journal.getId());
			preparedStatement.setString(2, journal.getJid());
			preparedStatement.setString(3, journal.getISSN());
			preparedStatement.setString(4, journal.getNaslov());
			preparedStatement.setString(5, journal.getSkraceniNaslov());
			preparedStatement.setString(6, journal.getStatus());
			preparedStatement.setString(7, journal.getTipDokumenta());
			preparedStatement.setString(8, journal.getUcestalost());
			preparedStatement.setString(9, journal.getJezik());
			preparedStatement.setString(10, journal.getPrviBroj());
			preparedStatement.setString(11, journal.getAbstrakt());
			preparedStatement.setString(12, journal.getAlternativniNaslovi());
			StringBuffer sb = new StringBuffer("");
			for(String journalCategory : journal.getKategorije()){
				sb.append(journalCategory+";");
			}
			preparedStatement.setString(13, sb.toString());
			preparedStatement.setString(14, journal.getUCurrentContents());
			preparedStatement.setString(15, journal.getScienceCitation());
			preparedStatement.setString(16, journal.getSponzor());
			preparedStatement.setString(17, journal.getIzdavac());
			preparedStatement.setString(18, journal.getAdresa());
			preparedStatement.setString(19, journal.getTelefon());
			preparedStatement.setString(20, journal.getFax());
			preparedStatement.setString(21, journal.geteMail());
			preparedStatement.setString(22, journal.getURL());
			preparedStatement.setString(23, journal.getZemlja());
			preparedStatement.setString(24, "");
			preparedStatement.setString(25, "NEW;");
			preparedStatement.setString(26, "");
			
//			System.out.println("Prosao");
			
			preparedStatement.executeUpdate();
			if((printlnCounter%200)==0)
			{
				printlnCounter =0;
				System.out.println("");
			}

			System.out.print("+");
			
        	
		} catch (Exception e) {
			System.out.println("Greska za insert journal jid="+ journal.getJid()+" or issn="+ journal.getISSN());
			System.exit(0);
		} finally {
			closeStatement();
		}
	}
	
	protected void updateJournalToDatabase()
	{
//		System.out.println("--------------------------------------------------------------------");
//		System.out.println("Casopis jid:"+journal.getJid() + " issn:"+journal.getISSN() + " naziv:"+journal.getNaslov());
		
		if(needUpdateJournal.size()>0){
			printlnCounter=printlnCounter+1;
			
//			boolean podaci = false;
			
			try {
				StringBuffer bufferUpdateCql = new StringBuffer("UPDATE journal SET ");
				StringBuffer bufferJournalUpdateState = new StringBuffer("");
				
				for (int i = 0; i < needUpdateJournal.size(); i++) {
					bufferUpdateCql.append(needUpdateJournal.get(i)+" = ? , ");
//					if(!needUpdateJournal.get(i).equals("NASLOV") && !needUpdateJournal.get(i).equals("AKTIVNEKATEGORIJE") 
//							&& !needUpdateJournal.get(i).equals("UCURRENTCONTENTS") && !needUpdateJournal.get(i).equals("SCIENCECITATION"))
//						podaci = true;
					bufferJournalUpdateState.append(needUpdateJournal.get(i)+";");
				}
				String VERSIONCHANGE = "";
				if(loadedJournal.getVERSIONCHANGE()==null)
					VERSIONCHANGE = bufferJournalUpdateState.toString();
				else if(loadedJournal.getVERSIONCHANGE().equalsIgnoreCase(""))
					VERSIONCHANGE = bufferJournalUpdateState.toString();
				else if (loadedJournal.getVERSIONCHANGE().equalsIgnoreCase("NOCHANGE;"))
					VERSIONCHANGE = bufferJournalUpdateState.toString();
				else if (loadedJournal.getVERSIONCHANGE().equalsIgnoreCase("NEW;"))
					VERSIONCHANGE = "NEW;";
				else {
					VERSIONCHANGE = loadedJournal.getVERSIONCHANGE() + bufferJournalUpdateState.toString();
				}
				
				
				String sql = bufferUpdateCql.toString() + "VERSIONCHANGE = \'" + VERSIONCHANGE + "\' WHERE ID = "+ journal.getId();
				preparedStatement = connection.prepareStatement(sql);

				for (int i = 0; i < needUpdateJournal.size(); i++) {
					if (needUpdateJournal.get(i).equals("JID")) {
						preparedStatement.setString(i+1, journal.getJid());
					}
					else if (needUpdateJournal.get(i).equals("ISSN")) {
						preparedStatement.setString(i+1, journal.getISSN());
					}
					else if (needUpdateJournal.get(i).equals("NASLOV")) {
						preparedStatement.setString(i+1, journal.getNaslov());
					}
					else if (needUpdateJournal.get(i).equals("SKRACENINASLOV")) {
						preparedStatement.setString(i+1, journal.getSkraceniNaslov());
					}
					else if (needUpdateJournal.get(i).equals("STATUS")) {
						preparedStatement.setString(i+1, journal.getStatus());
					}
					else if (needUpdateJournal.get(i).equals("TIPDOKUMENTA")) {
						preparedStatement.setString(i+1, journal.getTipDokumenta());
					}
					else if (needUpdateJournal.get(i).equals("UCESTALOST")) {
						preparedStatement.setString(i+1, journal.getUcestalost());
					}
					else if (needUpdateJournal.get(i).equals("JEZIK")) {
						preparedStatement.setString(i+1, journal.getJezik());
					}
					else if (needUpdateJournal.get(i).equals("PRVIBROJ")) {
						preparedStatement.setString(i+1, journal.getPrviBroj());
					}
					else if (needUpdateJournal.get(i).equals("ABSTRAKT")) {
						preparedStatement.setString(i+1, journal.getAbstrakt());
					}
					else if (needUpdateJournal.get(i).equals("ALTERNATIVNINASLOVI")) {
						preparedStatement.setString(i+1, journal.getAlternativniNaslovi());
					}
					else if (needUpdateJournal.get(i).equals("AKTIVNEKATEGORIJE")) {
						preparedStatement.setString(i+1, journal.getAktivneKategorije());
					}
					else if (needUpdateJournal.get(i).equals("UCURRENTCONTENTS")) {
						preparedStatement.setString(i+1, journal.getUCurrentContents());
					}
					else if (needUpdateJournal.get(i).equals("SCIENCECITATION")) {
						preparedStatement.setString(i+1, journal.getScienceCitation());
					}
					else if (needUpdateJournal.get(i).equals("SPONZOR")) {
						preparedStatement.setString(i+1, journal.getSponzor());
					}
					else if (needUpdateJournal.get(i).equals("IZDAVAC")) {
						preparedStatement.setString(i+1, journal.getIzdavac());
					}
					else if (needUpdateJournal.get(i).equals("ADRESA")) {
						preparedStatement.setString(i+1, journal.getAdresa());
					}
					else if (needUpdateJournal.get(i).equals("TELEFON")) {
						preparedStatement.setString(i+1, journal.getTelefon());
					}
					else if (needUpdateJournal.get(i).equals("FAX")) {
						preparedStatement.setString(i+1, journal.getFax());
					}
					else if (needUpdateJournal.get(i).equals("EMAIL")) {
						preparedStatement.setString(i+1, journal.geteMail());
					}
					else if (needUpdateJournal.get(i).equals("URL")) {
						preparedStatement.setString(i+1, journal.getURL());
					}
					else if (needUpdateJournal.get(i).equals("ZEMLJA")) {
						preparedStatement.setString(i+1, journal.getZemlja());
					}
				}
				preparedStatement.executeUpdate();
//				if (podaci) {
//					System.out.println("");
//					System.out.println(preparedStatement.toString());
//				}
			} catch (Exception e) {
				System.out.println("Greska za update journal has change "+ journal.getISSN());
				System.exit(0);
			} finally {
				closeStatement();
			}
		}
		else {
			try {
				String VERSIONCHANGE = "";
				if(loadedJournal.getVERSIONCHANGE()==null)
					VERSIONCHANGE = "NOCHANGE;";
				else if(loadedJournal.getVERSIONCHANGE().equalsIgnoreCase(""))
					VERSIONCHANGE = "NOCHANGE;";
				else 
					return;
				
				
				String sql = "UPDATE journal SET VERSIONCHANGE= ? WHERE ID = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, VERSIONCHANGE);
				preparedStatement.setInt(2, journal.getId());
				preparedStatement.executeUpdate();
//				System.out.println(preparedStatement.toString());
			} catch (Exception e) {
				System.out.println("Greska za update journal no change "+ journal.getISSN());
				System.exit(0);
			} finally {
				closeStatement();
			}
		}
		
	}
	
	protected void insertImpactFactorToDatabase(String TableForPlan)
	{
		printlnCounter=printlnCounter+1;
		try {
			List<KobsonImpactFactorJournal> impactFactorJournalLista = null;
			if(TableForPlan.equalsIgnoreCase("IMPACTFACTORJOURNAL"))
				impactFactorJournalLista = journal.getImpaktFatorCasopisa();
			else
				impactFactorJournalLista = journal.getImpaktFatorCasopisaPetGodina();
			
			boolean inserted = false; 
			for(KobsonImpactFactorJournal ImpactFactorJournalEl : impactFactorJournalLista){
				
				if(chackImpactFactorIDFromDatabase(journal.getId(), ImpactFactorJournalEl.getGodina(), TableForPlan))
					continue;
				
//				if(ImpactFactorJournalEl.getVrednost().equalsIgnoreCase("0.000"))
//					continue;
				
				String sql = "insert into  "+TableForPlan.toLowerCase()+" values (?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);
						
				preparedStatement.setInt(1, ImpactFactorJournalEl.getIdJournal());
				preparedStatement.setInt(2, ImpactFactorJournalEl.getGodina());
				preparedStatement.setString(3, ImpactFactorJournalEl.getVrednost());
				preparedStatement.setString(4, "NEW;");
				preparedStatement.executeUpdate();

				preparedStatement.close();
				
				String VERSIONCHANGE = "";
				if(loadedJournal.getVERSIONCHANGE()==null)
					VERSIONCHANGE = "IMF;";
				else if(loadedJournal.getVERSIONCHANGE().equalsIgnoreCase(""))
					VERSIONCHANGE = "IMF;";
				else if (loadedJournal.getVERSIONCHANGE().equalsIgnoreCase("NOCHANGE;"))
					VERSIONCHANGE = "IMF;";
				else if (loadedJournal.getVERSIONCHANGE().equalsIgnoreCase("NEW;"))
					VERSIONCHANGE = "NEW;";
				else if (loadedJournal.getVERSIONCHANGE().endsWith("IMF;"))
					VERSIONCHANGE = loadedJournal.getVERSIONCHANGE();
				else {
					VERSIONCHANGE = loadedJournal.getVERSIONCHANGE() + "IMF;";
				}
				
				sql = "UPDATE journal SET VERSIONCHANGE = \'" + VERSIONCHANGE + "\' WHERE ID = "+ journal.getId();
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
				
				inserted = true;
				
			}
			if((printlnCounter%200)==0)
			{
				printlnCounter =0;
//				System.out.println("");
			}

//			if(!inserted && TableForPlan.equalsIgnoreCase("IMPACTFACTORJOURNAL"))
//        		System.out.print("+");
//        	if(inserted && TableForPlan.equalsIgnoreCase("IMPACTFACTORJOURNAL"))
//        	{
//        		System.out.print("!");
//        	}
        	
		} catch (Exception e) {
			System.out.println("Greska za insertImpactFactorToDatabase "+TableForPlan+" impact factor journal  "+ journal.getJid());
			System.exit(0);
		} finally {
			closeStatement();
		}

	}
	protected boolean chackImpactFactorIDFromDatabase(int JournalID, int godina, String TableForPlan)
	{
		boolean found  = false;
		try {
			statement = connection.createStatement();
			statement.execute ("select * from "+TableForPlan.toLowerCase()+" WHERE JOURNAL_ID = "+ JournalID + " AND GODINA = " + godina);
			resultSet = statement.getResultSet ();
			
			while (resultSet.next ()){
				found = true;
			}
		} catch (Exception e) {
			System.out.println("Greska za chackImpactFactorIDFromDatabase "+TableForPlan+" impakt faktora casopsia u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return found;
	}
	
	protected void insertCategoryJournalToDatabase(String TableForPlan)
	{
		try {
			
			List<KobsonImpactFactorJournal> impactFactorJournalLista = null;
			if(TableForPlan.equalsIgnoreCase("JOURNALCATEGORY"))
				impactFactorJournalLista = journal.getImpaktFatorCasopisa();
			else
				impactFactorJournalLista = journal.getImpaktFatorCasopisaPetGodina();
			
			for(KobsonImpactFactorJournal ImpactFactorJournalEl : impactFactorJournalLista){
				List<KobsonCategoryJournal> categoryJournalLista = ImpactFactorJournalEl.getCategory();
					
				for (KobsonCategoryJournal categoryJournalEl : categoryJournalLista) {
					if (chackCategoryJournalIDFromDatabase(journal.getId(), categoryJournalEl.getIdCategory(), ImpactFactorJournalEl.getGodina(), TableForPlan))
						continue;
					
//					if(categoryJournalEl.getRang().equalsIgnoreCase("0/0"))
//						categoryJournalEl.setRang("");
					
					String sql = "";
					
					sql = "insert into  "+TableForPlan.toLowerCase()+" values (?, ?, ?, ?, ?, ?, ?)";
							// PreparedStatements can use variables and are more efficient
					preparedStatement = connection.prepareStatement(sql);
							
					preparedStatement.setInt(1, categoryJournalEl.getIdJournal());
					preparedStatement.setInt(2, categoryJournalEl.getIdCategory());
					preparedStatement.setInt(3, categoryJournalEl.getGodina());
					preparedStatement.setString(4, categoryJournalEl.getRang());
					preparedStatement.setString(5, categoryJournalEl.getVrednost());
					preparedStatement.setString(6, categoryJournalEl.getKategorijaCasopisa());
					preparedStatement.setString(7, "NEW;");
					preparedStatement.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println("Greska za insertCategoryJournalToDatabase "+TableForPlan+" category journal "+ journal.getJid());
			System.exit(0);
		} finally {
			closeStatement();
		}
	}
	
	protected boolean chackCategoryJournalIDFromDatabase(int JOURNAL_ID, int CATEGORY_ID, int godina, String TableForPlan)
	{
		boolean found  = false;
		
		try {
			statement = connection.createStatement();
			statement.executeQuery ("select * from "+TableForPlan.toLowerCase()+" WHERE JOURNAL_ID = "+ JOURNAL_ID + " AND CATEGORY_ID = " + CATEGORY_ID + " AND GODINA = " + godina);
			resultSet = statement.getResultSet ();
			
			while (resultSet.next ())
			{
				found = true;
			}
		} catch (Exception e) {
			System.out.println("Greska za chackCategoryJournalIDFromDatabase "+TableForPlan+" kategorije casopisa u bazi ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
		return found;
	}
	
    protected void prebaciFajloveServisERROR(String errorName)
    {
		if (nameServis!=null)
        {  		
        	File renamed = new File(errorDir +"/"+ nameServis +"." +errorName);
        	boolean success = dataFileServis.renameTo(renamed);
        	if (!success) {
        		System.out.println("Puko prebaciFajloveServisERROR : "+ nameServis);
        		System.exit(0);
        	    // File was not successfully renamed
        	}
        }
    }

    /**
	 * @return the categoryList
	 */
	public static ArrayList<KobsonCategory> getCategoryList() {
		if (categoryList.size()==0) {
			loadCategoriesFromDatabase();
		}
		return categoryList;
	}

	public static String getCategoryName(int idCategory){
		if (categoryList.size()==0) {
			loadCategoriesFromDatabase();
		}
		
		for (KobsonCategory cat : categoryList) {
			if (cat.getId() == idCategory) {
				return cat.getName();
			}
			
		}
		return "";
	}
	
	public static int getCategoryID(String nameCategory){
		if (categoryList.size()==0) {
			loadCategoriesFromDatabase();
		}
		
		for(KobsonCategory cat : categoryList)
		{
			if(cat.getName().equalsIgnoreCase(nameCategory))
				return cat.getId();
		}
		return -1;
	}
	
	public static void loadCategoriesFromDatabase()
	{
		try {
			statement = connection.createStatement();	
			String sql = "select ID, NAME from category";
			statement.execute(sql);
			resultSet = statement.getResultSet();
			
			while (resultSet.next ()){
				categoryList.add(new KobsonCategory(resultSet.getInt ("ID"), resultSet.getString("NAME")));
			}
			
		} catch (Exception e) {
			System.out.println("Greska za ocitavanje kategorija iz baze ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}
	}
    
	protected void insertCategoryToDatabase()
	{
		try {
			preparedStatement = connection.prepareStatement("insert into  cetegory values ( ?, ?, ?)");

			for(KobsonCategory cat : categoryList){
				preparedStatement.setInt(1, cat.getId());
				preparedStatement.setString(2, cat.getName());
				preparedStatement.setString(3, "");
				preparedStatement.executeUpdate();
			}
			
		} catch (Exception e) {
			System.out.println("Greska za kategoriju ");
			e.printStackTrace();
			System.exit(0);
		} finally {
			closeStatement();
		}

	}
	
	/**
	 * @param connection the connection to set
	 */
	public static void setConnection(Connection connection) {
		KobsonDatabaseOperations.connection = connection;
	}

	static protected void openConnection() {
		try
		{
		// This will load the MySQL driver, each DB has its own driver
    	Class.forName("com.mysql.jdbc.Driver");
		String url =
			 "jdbc:mysql://localhost:3306/kobson?useUnicode=true&characterEncoding=UTF8";
		connection = DriverManager.getConnection(url, "root", "021sandra021Mysql");
		connection.setAutoCommit(false);
		
	    } catch (Exception e) {
			System.out.println("Greska pri otvaranju konekcije ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// You need to close the resultSet
	static protected void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {

		}
	}
	
	// You need to close the resultSet
	protected static void closeStatement() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * @return the journal
	 */
	public KobsonJournal getJournal() {
		return journal;
	}

	/**
	 * @return the loadedJournal
	 */
	public KobsonJournal getLoadedJournal() {
		return loadedJournal;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
