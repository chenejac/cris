package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOElektronikaTelekomunikacijeIInformacioneTehnologije extends MNO{
	
	private MNOElektronikaTelekomunikacijeIInformacioneTehnologije(){
		super();
		this.nameMNO = "Elektronika, telekomunikacije i informacione tehnologije";
		this.yearsSpecial = new int [9];
		yearsSpecial[0] = 2012;
		yearsSpecial[1] = 2013;
		yearsSpecial[2] = 2014;
		yearsSpecial[3] = 2015;
		yearsSpecial[4] = 2016;
		yearsSpecial[5] = 2017;
		yearsSpecial[6] = 2018;
		yearsSpecial[7] = 2019;
		yearsSpecial[8] = 2020;
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
		
		special2012 = new HashMap<String, String>();
		special2012.put("0353-3670", "M24"); //(BISIS)31127	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2012.put("1451-4869", "M51"); //(BISIS)34205	1451-4869	Serbian Journal of Electrical Engineering
		special2012.put("1450-9903", "M52"); //(BISIS)31111	1450-9903	Journal of Automatic control
		special2012.put("1945-8738", "M52"); //NOVI U KOB	1945-8738	Journal of Mathematics
		special2012.put("0350-0667", "M52"); //(BISIS)36755	0350-0667	Scientific-Technical Review
		special2012.put("0040-2176", "M52"); //NEMA U KOB	0040-2176	Tehnika
		special2012.put("2217-2661", "M53"); //NEMA U KOB	2217-2661	International Journal of Industrial Engineering and Management
		special2012.put("1820-4511", "M53"); //NEMA U KOB	1820-4511	IPSI BgD Transactions on Advanced Research	
		special2012.put("1820-4503", "M53"); //NEMA U KOB	1820-4503	IPSI BgD Transactions on Internet Research
		special2012.put("1820-6530", "M53"); //(BISIS)33116	1820-6530	Journal of Serbian Society for Computational Mechanics			
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoElektronikaTelekomunikacijeIInformacioneTehnologije2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);
	}
	
	public static MNOElektronikaTelekomunikacijeIInformacioneTehnologije MNOElektronikaTelekomunikacijeIInformacioneTehnologije= null;
	
	public static MNOElektronikaTelekomunikacijeIInformacioneTehnologije getMNOElektronikaTelekomunikacijeIInformacioneTehnologije() {	
		if(MNOElektronikaTelekomunikacijeIInformacioneTehnologije==null)
			MNOElektronikaTelekomunikacijeIInformacioneTehnologije = new MNOElektronikaTelekomunikacijeIInformacioneTehnologije();
		return MNOElektronikaTelekomunikacijeIInformacioneTehnologije;
	}
}
