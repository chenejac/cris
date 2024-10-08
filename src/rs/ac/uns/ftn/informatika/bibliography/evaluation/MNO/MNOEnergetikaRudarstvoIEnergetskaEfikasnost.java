/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOEnergetikaRudarstvoIEnergetskaEfikasnost extends MNO{

	private MNOEnergetikaRudarstvoIEnergetskaEfikasnost() {
		super();
		this.nameMNO = "Energetika, rudarstvo i energetska efikasnost";
		this.yearsSpecial = new int [13];
		yearsSpecial[0] = 2011;
		yearsSpecial[1] = 2012;
		yearsSpecial[2] = 2013;
		yearsSpecial[3] = 2014;
		yearsSpecial[4] = 2015;
		yearsSpecial[5] = 2016;
		yearsSpecial[6] = 2017;
		yearsSpecial[7] = 2018;
		yearsSpecial[8] = 2019;
		yearsSpecial[9] = 2020;
		yearsSpecial[10] = 2021;
		yearsSpecial[11] = 2022;
		yearsSpecial[12] = 2023;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
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
		HashMap <String, String> special2022;
		HashMap <String, String> special2023;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2011 = new HashMap<String, String>();
		special2011.put("0354-9836", "M23"); 			//	0354-9836	Thermal Science
		special2011.put("0354-4605", "M24"); 			//	0354-4605	Facta Universitatis: Series Architecture and Civil Engineering
		special2011.put("0353-3670", "M24"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2011.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2011.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2011.put("1451-2092", "M51"); 			//	1451-2092	FME Transactions
		special2011.put("1821-4487", "M51"); 			//	1821-4487	Journal on Processing and Energy in Agriculture
		special2011.put("1451-4869", "M51"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2011.put("0354-6306", "M51"); 			//	0354-6306	Metalurgija
		special2011.put("0554-5587", "M51"); 			//	0554-5587	Poljoprivredna tehnika
		special2011.put("1451-0162", "M51"); 			//	1451-0162	Rudarski radovi, Bor
		special2011.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2011.put("0350-218X", "M51"); 			//	0350-218X	Termotehnika
		special2011.put("0013-5836", "M51"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2011.put("0354-4656", "M52"); 			//	0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2011.put("1450-5959", "M52"); 			//	1450-5959	Journal of Mining and Metallurgy, Section A: Mining
		special2011.put("0354-8589", "M52"); 			//	0354-8589	Gas
		special2011.put("0013-5755", "M52"); 			//	0013-5755	Elektroprivreda
		special2011.put("0350-1426", "M52"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		special2011.put("0354-2904", "M52"); 			//	0354-2904	Podzemni radovi
		special2011.put("0350-2627", "M52"); 			//	0350-2627	Tehnika - Rudarstvo, geologija i metalurgija
		special2011.put("0351-0212", "M53"); 			//	0351-0212	Bakar
		special2011.put("0353-2631", "M53"); 			//	0353-2631	Inovacije i razvoj
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1451-4869", "M24"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2012.put("1451-0162", "M24"); 			//	1451-0162	Rudarski radovi, Bor
		special2012.put("0350-218X", "M51"); 			//	0350-218X	Termotehnika
		special2012.put("0013-5755", "M51"); 			//	0013-5755	Elektroprivreda
		special2012.put("0350-1426", "M51"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		special2012.put("0233-3619", "M51"); 			//	0233-3619	Energija
		special2012.put("0040-2176", "M52"); 			//	0040-2176	Tehnika
		special2012.put("1820-7480", "M53"); 			//	1820-7480	Reciklaža i održivi razvoj
		special2012.put("0350-428X", "M53"); 			//	0350-428X	Zbornik radova Fakulteta tehničkih nauka	
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoEnergetikaRudarstvoIEnergetskaEfikasnost2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_za_energetiku_rudarstvo_i_energetsku_efikasnost.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);

		special2023 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2023_za_energetiku_rudarstvo_i_energetsku_efikasnost.xlsx", special2023);
		this.specialJournalsAllYears.put(2023, special2023);
	}
	
	public static MNOEnergetikaRudarstvoIEnergetskaEfikasnost MNOEnergetikaRudarstvoIEnergetskaEfikasnost= null;
	
	public static MNOEnergetikaRudarstvoIEnergetskaEfikasnost getMNOEnergetikaRudarstvoIEnergetskaEfikasnost() {
		if(MNOEnergetikaRudarstvoIEnergetskaEfikasnost==null)
			MNOEnergetikaRudarstvoIEnergetskaEfikasnost = new MNOEnergetikaRudarstvoIEnergetskaEfikasnost();
		return MNOEnergetikaRudarstvoIEnergetskaEfikasnost;
	}
}

