package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost extends MNO{

	private MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost() {
		super();
		this.nameMNO = "Društveno-humanističke nauke: Inostrani izdavaci - Srpski jezik i književnost";
		this.yearsSpecial = new int [10];
		yearsSpecial[0] = 2014;
		yearsSpecial[1] = 2015;
		yearsSpecial[2] = 2016;
		yearsSpecial[3] = 2017;
		yearsSpecial[4] = 2018;
		yearsSpecial[5] = 2019;
		yearsSpecial[6] = 2020;
		yearsSpecial[7] = 2021;
		yearsSpecial[8] = 2022;
		yearsSpecial[9] = 2023;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
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
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-InostraniIzdavaciSrpskiJezikIKnjizevnost2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_inostrani_izdavaci_u_oblasti_srpskog_jezika_i_knjzevnosti.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);

		special2023 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2023_inostrani_izdavaci_u_oblasti_srpskog_jezika_i_knjzevnosti.xlsx", special2023);
		this.specialJournalsAllYears.put(2023, special2023);
	}
	
	public static MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost= null;
	
	public static MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost getMNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost() {
		if(MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost==null)
			MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost = new MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost();
		return MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost;
	}
	
}
