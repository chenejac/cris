package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOFizika extends MNO{

	private MNOFizika() {
		super();
		this.nameMNO = "Fizika";
		this.yearsSpecial = new int [12];
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
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();	
		special2009.put("0354-4656", "M52"); 			//(BISIS)24424	0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2009.put("1450-7404", "M52"); 			//(BISIS)24376	1450-7404	Journal of Research in Physics
		special2009.put("1450-9636", "M52"); 			//(BISIS)17260	1450-9636	Kragujevac Journal of Science
		special2009.put("1451-3994", "M52"); 			//(BISIS)29921	1451-3994	Nuclear technology and radiation protection
		this.specialJournalsAllYears.put(2009, special2009);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2010 = new HashMap<String, String>();
		special2010.put("1451-3994", "M24"); 			//				1451-3994	Nuclear technology and radiation protection
		special2010.put("0354-4656", "M51"); 			//				0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2010.put("1450-9636", "M51"); 			//				1450-9636	Kragujevac Journal of Science
		special2010.put("1450-7404", "M52"); 			//				1450-7404	Journal of Research in Physics
		this.specialJournalsAllYears.put(2010, special2010);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2011 = new HashMap<String, String>();
		special2011.put("1451-3994", "M23"); 			//				1451-3994	Nuclear technology and radiation protection
		special2011.put("1450-698X", "M24"); 			//(BISIS)24454	1450-698X	Serbian Astronomical Journal
		special2011.put("0354-4656", "M51"); 			//				0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2011.put("1450-9636", "M51"); 			//				1450-9636	Kragujevac Journal of Science
		special2011.put("1450-7404", "M52"); 			//				1450-7404	Journal of Research in Physics
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1450-9636", "M51"); 			//				1450-9636	Kragujevac Journal of Science
		special2012.put("0354-4656", "M52"); 			//				0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2012.put("1450-5835", "M52"); 			//(BISIS)35776	1450-5835	Microwave Review = Mikrotalasna revija
		special2012.put("0350-218X", "M52"); 			//(BISIS)36922	0350-218X	Termotehnika
		special2012.put("1450-7404", "M53"); 			//				1450-7404	Journal of Research in Physics
		special2012.put("0354-9291", "M53"); 			//(BISIS)24438	0354-9291	Sveske fizičkih nauka (SFIN), Lectures in Physical Sciences
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoFizika2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);
	}
	
	public static MNOFizika MNOFizika= null;
	
	public static MNOFizika getMNOFizika() {
		if(MNOFizika==null)
			MNOFizika = new MNOFizika();
		return MNOFizika;
	}
}
