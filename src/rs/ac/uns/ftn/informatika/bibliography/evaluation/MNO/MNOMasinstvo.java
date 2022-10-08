package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;
/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOMasinstvo extends MNO{

	private MNOMasinstvo() {
		super();
		this.nameMNO = "Mašinstvo";
		this.yearsSpecial = new int [13];
		yearsSpecial[0] = 2009;
		yearsSpecial[1] = 2010;
		yearsSpecial[2] = 2011;
		yearsSpecial[3] = 2012;
		yearsSpecial[4] = 2013;
		yearsSpecial[5] = 2014;
		yearsSpecial[6] = 2015;
		yearsSpecial[7] = 2016;
		yearsSpecial[8] = 2017;
		yearsSpecial[9] = 2018;
		yearsSpecial[10] = 2019;
		yearsSpecial[11] = 2020;
		yearsSpecial[12] = 2021;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2009;
		HashMap <String, String> special2010;
		HashMap <String, String> special2011;
		HashMap <String, String> special2012;
		HashMap <String, String> special2013;
		HashMap <String, String> special2014;
		HashMap <String, String> special2015;
		HashMap <String, String> special2016;
		HashMap <String, String> special2017;
		HashMap <String, String> special2018;
		HashMap <String, String> special2019;
		HashMap <String, String> special2020;
		HashMap <String, String> special2021;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();
		special2009.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2009.put("0354-2009", "M51"); 			//	0354-2009	Facta Universitatis: Series Mechanics, Automatic Control and Robotics
		special2009.put("1451-2092", "M52"); 			//	1451-2092	FME Transactions
		special2009.put("0354-6829", "M52"); 			//	0354-6829	IMK-14 - Istraživanje i razvoj
		special2009.put("1451-4117", "M52"); 			//	1451-4117	Istraživanja i projektovanja za privredu
		special2009.put("1450-5304", "M52"); 			//	1450-5304	Mobility and Vehicle Mechanics
		special2009.put("1451-401X", "M52"); 			//	1451-401X	Prerada drveta
		special2009.put("0352-678X", "M52"); 			//	0352-678X	Procesna tehnika
		special2009.put("0350-2953", "M52"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2009.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2009.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2009.put("0354-9496", "M52"); 			//	0354-9496	Traktori i pogonske mašine
		special2009.put("0351-1642", "M52"); 			//	0351-1642	Tribology in Industry
		special2009.put("0354-2025", "M53"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2009.put("1450-5401", "M53"); 			//	1450-5401	Konstruisanje mašina
		special2009.put("1450-5096", "M53"); 			//	1450-5096	Production Engineering and Computers
		special2009.put("1451-1975", "M53"); 			//	1451-1975	Tehnička dijagnostika
		special2009.put("0461-2531", "M53"); 			//	0461-2531	Tehnika – Mašinstvo
		special2009.put("0042-8469", "M53"); 			//	0042-8469	Vojnotehnički glasnik
		special2009.put("0350-5138", "M53"); 			//	0350-5138	Železnice - naučno-stručni časopis Jugoslovenskih železnica
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2010.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2010.put("1451-2092", "M51"); 			//	1451-2092	FME Transactions
		special2010.put("0354-6829", "M51"); 			//	0354-6829	IMK-14 - Istraživanje i razvoj
		special2010.put("0351-1642", "M51"); 			//	0351-1642	Tribology in Industry
		special2010.put("0354-2009", "M52"); 			//	0354-2009	Facta Universitatis: Series Mechanics, Automatic Control and Robotics
		special2010.put("1451-4117", "M52"); 			//	1451-4117	Istraživanja i projektovanja za privredu
		special2010.put("0350-2953", "M52"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2010.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2010.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2010.put("0354-9496", "M52"); 			//	0354-9496	Traktori i pogonske mašine
		special2010.put("1450-5401", "M53"); 			//	1450-5401	Konstruisanje mašina
		special2010.put("1450-5304", "M53"); 			//	1450-5304	Mobility and Vehicle Mechanics
		special2010.put("1451-401X", "M53"); 			//	1451-401X	Prerada drveta
		special2010.put("0461-2531", "M53"); 			//	0461-2531	Tehnika – Mašinstvo
		special2010.put("1451-1975", "M53"); 			//	1451-1975	Tehnička dijagnostika
		special2010.put("0042-8469", "M53"); 			//	0042-8469	Vojnotehnički glasnik
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2011.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2011.put("1451-2092", "M51"); 			//	1451-2092	FME Transactions
		special2011.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2011.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2011.put("0351-1642", "M51"); 			//	0351-1642	Tribology in Industry
		special2011.put("0354-2009", "M52"); 			//	0354-2009	Facta Universitatis: Series Mechanics, Automatic Control and Robotics
		special2011.put("1450-5304", "M52"); 			//	1450-5304	Mobility and Vehicle Mechanics
		special2011.put("0042-8469", "M52"); 			//	0042-8469	Vojnotehnički glasnik
		special2011.put("0350-2953", "M52"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2011.put("0354-9496", "M52"); 			//	0354-9496	Traktori i pogonske mašine
		special2011.put("0354-6829", "M53"); 			//	0354-6829	IMK-14 - Istraživanje i razvoj
		special2011.put("1451-4117", "M53"); 			//	1451-4117	Istraživanja i projektovanja za privredu
		special2011.put("1451-401X", "M53"); 			//	1451-401X	Prerada drveta
		special2011.put("0461-2531", "M53"); 			//	0461-2531	Tehnika – Mašinstvo
		special2011.put("1451-1975", "M53"); 			//	1451-1975	Tehnička dijagnostika
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1451-2092", "M24"); 			//	1451-2092	FME Transactions
		special2012.put("0351-1642", "M24"); 			//	0351-1642	Tribology in Industry
		special2012.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2012.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2012.put("0354-3870", "M51"); 			//	0354-3870	Journal for Technology of Plasticity
		special2012.put("1451-4117", "M51"); 			//	1451-4117	Istraživanja i projektovanja za privredu
		special2012.put("1450-5304", "M52"); 			//	1450-5304	Mobility and Vehicle Mechanics
		special2012.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2012.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2012.put("0350-2953", "M52"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2012.put("0354-9496", "M52"); 			//	0354-9496	Traktori i pogonske mašine
		special2012.put("0040-2176", "M52"); 			//	0040-2176	Tehnika
		special2012.put("1451-1975", "M52"); 			//	1451-1975	Tehnička dijagnostika
		special2012.put("1820-6417", "M53"); 			//	1820-6417	Facta Universitatis: Series Automatic Control and Robotics
		special2012.put("0354-2009", "M53"); 			//	0354-2009	Facta Universitatis: Series Mechanics, Automatic Control and Robotics
		special2012.put("1820-6530", "M53"); 			//	1820-6530	Journal of Serbian Society for Computational Mechanics
		special2012.put("1821-4932", "M53"); 			//	1821-4932	Journal of Production Engineering
		special2012.put("1821-1259", "M53"); 			//	1821-1259	Machine Design
		special2012.put("0042-8469", "M53"); 			//	0042-8469	Vojnotehnički glasnik
		special2012.put("0354-6829", "M53"); 			//	0354-6829	IMK-14 - Istraživanje i razvoj
		special2012.put("1450-5401", "M53"); 			//	1450-5401	Konstruisanje mašina
		special2012.put("1451-401X", "M53"); 			//	1451-401X	Prerada drveta
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMasinstvo2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);
	}
	
	public static MNOMasinstvo MNOMasinstvo= null;
	
	public static MNOMasinstvo getMNOMasinstvo() {
		if(MNOMasinstvo==null)
			MNOMasinstvo = new MNOMasinstvo();
		return MNOMasinstvo;
	}
}

