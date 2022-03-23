package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNODHNIstorijaArheologijaIEtnologija extends MNO{

	private MNODHNIstorijaArheologijaIEtnologija() {
		super();
		this.nameMNO = "Društveno-humanističke nauke: Ekonomija i organizacione nauke";
		this.yearsSpecial = new int [10];
		yearsSpecial[0] = 2012;
		yearsSpecial[1] = 2013;
		yearsSpecial[2] = 2014;
		yearsSpecial[3] = 2015;
		yearsSpecial[4] = 2016;
		yearsSpecial[5] = 2017;
		yearsSpecial[6] = 2018;
		yearsSpecial[7] = 2019;
		yearsSpecial[8] = 2020;
		yearsSpecial[9] = 2021;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
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
		special2012 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2012.xlsx", special2012);
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoDrustvenoHumanistickeNauke-IstorijaArheologijaIEtnologija2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);
	}
	
	public static MNODHNIstorijaArheologijaIEtnologija MNODHNIstorijaArheologijaIEtnologija= null;
	
	public static MNODHNIstorijaArheologijaIEtnologija getMNODHNIstorijaArheologijaIEtnologija() {
		if(MNODHNIstorijaArheologijaIEtnologija==null)
			MNODHNIstorijaArheologijaIEtnologija = new MNODHNIstorijaArheologijaIEtnologija();
		return MNODHNIstorijaArheologijaIEtnologija;
	}
	
}
