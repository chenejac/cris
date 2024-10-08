package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOMedicina extends MNO{

	private MNOMedicina() {
		super();
		this.nameMNO = "Medicina";
		this.yearsSpecial = new int [15];
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
		yearsSpecial[13] = 2022;
		yearsSpecial[14] = 2023;
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
		HashMap <String, String> special2022;
		HashMap <String, String> special2023;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2009.xlsx", special2009);
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2010.xlsx", special2010);
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2011.xlsx", special2011);
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2012.xlsx", special2012);
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMedicina2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_za_medicinske_nauke.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);

		special2023 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2023_za_medicinske_nauke.xlsx", special2023);
		this.specialJournalsAllYears.put(2023, special2023);
	}
	
	public static MNOMedicina MNOMedicina= null;
	
	public static MNOMedicina getMNOMedicina() {
		if(MNOMedicina==null)
			MNOMedicina = new MNOMedicina();
		return MNOMedicina;
	}
	
}
