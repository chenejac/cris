package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOHemija extends MNO{

	private MNOHemija() {
		super();
		this.nameMNO = "Hemija";
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
		special2009.put("0340-6253", "M22"); //(BISIS)30479		0340-6253	Match: Communications in Mathematical and in Computer Chemistry
		special2009.put("0352-5139", "M23"); //(BISIS)24385		0352-5139	Journal of the Serbian Chemical Society
		special2009.put("0350-820X", "M23"); //(BISIS)24542		0350-820X	Science of Sintering
		special2009.put("1451-9372", "M51"); //(BISIS)24488		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2009.put("1450-5959", "M51"); //(BISIS)40659		1450-5959	Journal of Mining and Metallurgy, Section A: Mining
		special2009.put("1450-5339", "M51"); //(BISIS)30942		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2009.put("1450-7188", "M52"); //(BISIS)8155		1450-7188	Acta periodica technologica
		special2009.put("0354-4656", "M52"); //(BISIS)24424		0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2009.put("0354-804X", "M52"); //(BISIS)36147		0354-804X	Facta Universitatis: Series Working and Living Environmental Protection		
		special2009.put("0367-598X", "M52"); //(BISIS)24504		0367-598X	Hemijska industrija
		special2009.put("1452-8258", "M52"); //(BISIS)29850		1452-8258	Journal of Medical Biochemistry
		special2009.put("1450-9636", "M52"); //(BISIS)17260		1450-9636	Kragujevac Journal of Science
		special2009.put("0354-2300", "M52"); //NEMA				0354-2300	Tehnika - Novi materijali
		special2009.put("0351-9465", "M52"); //(BISIS)38817		0351-9465	Zaštita materijala
		special2009.put("0440-6826", "M53"); //(BISIS)8267		0440-6826	Hemijski pregled
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0340-6253", "M21"); //(BISIS)30479		0340-6253	Match: Communications in Mathematical and in Computer Chemistry
		special2010.put("1452-3981", "M22"); //(BISIS)27530		1452-3981	International Journal of Electrochemical Science
		special2010.put("1450-5339", "M22"); //(BISIS)30942		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2010.put("0367-598X", "M23"); //(BISIS)24504		0367-598X	Hemijska industrija
		special2010.put("0352-5139", "M23"); //(BISIS)24385		0352-5139	Journal of the Serbian Chemical Society
		special2010.put("0350-820X", "M23"); //(BISIS)24542		0350-820X	Science of Sintering
		special2010.put("1451-9372", "M24"); //(BISIS)24488		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2010.put("1452-8258", "M24"); //(BISIS)29850		1452-8258	Journal of Medical Biochemistry
		special2010.put("0354-4656", "M51"); //(BISIS)24424		0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2010.put("1450-7188", "M52"); //(BISIS)8155		1450-7188	Acta periodica technologica
		special2010.put("0354-804X", "M52"); //(BISIS)36147		0354-804X	Facta Universitatis: Series Working and Living Environmental Protection		
		special2010.put("1450-9636", "M52"); //(BISIS)17260		1450-9636	Kragujevac Journal of Science
		special2010.put("0354-2300", "M52"); //NEMA				0354-2300	Tehnika - Novi materijali
		special2010.put("0440-6826", "M53"); //(BISIS)8267		0440-6826	Hemijski pregled
		special2010.put("0351-9465", "M53"); //(BISIS)38817		0351-9465	Zaštita materijala
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0340-6253", "M21"); //(BISIS)30479		0340-6253	Match: Communications in Mathematical and in Computer Chemistry
		special2011.put("1450-5339", "M21"); //(BISIS)30942		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2011.put("1452-3981", "M22"); //(BISIS)27530		1452-3981	International Journal of Electrochemical Science
		special2011.put("1451-9372", "M23"); //(BISIS)24488		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2011.put("1452-8258", "M23"); //(BISIS)29850		1452-8258	Journal of Medical Biochemistry
		special2011.put("0352-5139", "M23"); //(BISIS)24385		0352-5139	Journal of the Serbian Chemical Society
		special2011.put("0350-820X", "M23"); //(BISIS)24542		0350-820X	Science of Sintering
		special2011.put("0367-598X", "M23"); //(BISIS)24504		0367-598X	Hemijska industrija
		special2011.put("1450-7188", "M52"); //(BISIS)8155		1450-7188	Acta periodica technologica
		special2011.put("0354-4656", "M52"); //(BISIS)24424		0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2011.put("0351-9465", "M52"); //(BISIS)38817		0351-9465	Zaštita materijala
		special2011.put("1450-9636", "M53"); //(BISIS)17260		1450-9636	Kragujevac Journal of Science
		special2011.put("0354-2300", "M53"); //NEMA				0354-2300	Tehnika - Novi materijali
		special2011.put("0440-6826", "M53"); //(BISIS)8267		0440-6826	Hemijski pregled
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1450-7188", "M51"); //(BISIS)8155		1450-7188	Acta periodica technologica
		special2012.put("1450-9636", "M51"); //(BISIS)17260		1450-9636	Kragujevac Journal of Science
		special2012.put("1820-6131", "M51"); //(BISIS)8379		1820-6131	Processing and application of ceramics
		special2012.put("0354-4656", "M52"); //(BISIS)24424		0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2012.put("0351-9465", "M52"); //(BISIS)38817		0351-9465	Zaštita materijala		
		special2012.put("0040-2176", "M53"); //NEMA U KOB		0040-2176	Tehnika	
		special2012.put("0440-6826", "M53"); //(BISIS)8267		0440-6826	Hemijski pregled
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoHemija2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);
	}
	
	public static MNOHemija MNOHemija= null;
	
	public static MNOHemija getMNOHemija() {
		if(MNOHemija==null)
			MNOHemija = new MNOHemija();
		return MNOHemija;
	}

}
