package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOBiologija extends MNO{

	private MNOBiologija() {
		super();
		this.nameMNO = "Biologija";
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
		special2009.put("0567-8315", "M23"); 			//(BISIS)17094	0567-8315	Acta Veterinaria, Beograd
		special2009.put("0354-4664", "M23"); 			//(BISIS)8195	0354-4664	Archives of biological sciences
		special2009.put("0534-0012", "M51"); 			//(BISIS)8260	0534-0012	Genetika (Beograd)
		special2009.put("0514-6658", "M53"); 			//NEMA			0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2009.put("0354-4311", "M53"); 			//(BISIS)17112	0354-4311	Acta biologica iugoslavica - serija G: Acta herbologica
		special2009.put("0354-9410", "M53"); 			//(BISIS)17110	0354-9410	Acta Entomologica Serbica
		special2009.put("0354-7310", "M53"); 			//(BISIS)24418	0354-7310	Archive of oncology
		special2009.put("1452-8258", "M53"); 			//(BISIS)29850	1452-8258	Journal of Medical Biochemistry
		special2009.put("1450-9636", "M53"); 			//(BISIS)17260	1450-9636	Kragujevac Journal of Science
		special2009.put("0350-0519", "M53"); 			//(BISIS)24459	0350-0519	Vodoprivreda
		special2009.put("0372-7866", "M53"); 			//NEMA			0372-7866	Zaštita bilja
		special2009.put("0514-5899", "M53"); 			//NEMA			0514-5899	Zaštita prirode
		special2009.put("0352-4906", "M53"); 			//(BISIS)17099	0352-4906	Zbornik Matice srpske za prirodne nauke
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0567-8315", "M23"); 			//				0567-8315	Acta Veterinaria, Beograd
		special2010.put("0354-4664", "M23"); 			//				0354-4664	Archives of biological sciences
		special2010.put("0534-0012", "M24"); 			//				0534-0012	Genetika (Beograd)
		special2010.put("1452-8258", "M24"); 			//				1452-8258	Journal of Medical Biochemistry
		special2010.put("0352-4906", "M51"); 			//				0352-4906	Zbornik Matice srpske za prirodne nauke
		special2010.put("0354-9410", "M52"); 			//				0354-9410	Acta Entomologica Serbica
		special2010.put("0354-7310", "M52"); 			//				0354-7310	Archive of oncology
		special2010.put("1821-2158", "M53"); 			//(BISIS)17159	1821-2158	Botanica Serbica
		special2010.put("0514-6658", "M53"); 			//				0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2010.put("0354-4311", "M53"); 			//				0354-4311	Acta biologica iugoslavica - serija G: Acta herbologica
		special2010.put("1450-9636", "M53"); 			//				1450-9636	Kragujevac Journal of Science
		special2010.put("0350-0519", "M53"); 			//				0350-0519	Vodoprivreda
		special2010.put("0514-5899", "M53"); 			//				0514-5899	Zaštita prirode
		special2010.put("0372-7866", "M53"); 			//				0372-7866	Zaštita bilja
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0567-8315", "M23"); 			//				0567-8315	Acta Veterinaria, Beograd
		special2011.put("0354-4664", "M23"); 			//				0354-4664	Archives of biological sciences
		special2011.put("0534-0012", "M23"); 			//				0534-0012	Genetika (Beograd)
		special2011.put("1452-8258", "M24"); 			//				1452-8258	Journal of Medical Biochemistry
		special2011.put("1821-2158", "M51"); 			//				1821-2158	Botanica Serbica
		special2011.put("0352-4906", "M51"); 			//				0352-4906	Zbornik Matice srpske za prirodne nauke
		special2011.put("0354-9410", "M52"); 			//				0354-9410	Acta Entomologica Serbica
		special2011.put("0514-6658", "M53"); 			//				0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2011.put("0354-4311", "M53"); 			//				0354-4311	Acta biologica iugoslavica - serija G: Acta herbologica
		special2011.put("0354-7310", "M53"); 			//				0354-7310	Archive of oncology
		special2011.put("1450-9636", "M53"); 			//				1450-9636	Kragujevac Journal of Science
		special2011.put("0350-0519", "M53"); 			//				0350-0519	Vodoprivreda
		special2011.put("0372-7866", "M53"); 			//				0372-7866	Zaštita bilja
		special2011.put("0514-5899", "M53"); 			//				0514-5899	Zaštita prirode
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1821-2158", "M51"); 			//				1821-2158	Botanica Serbica
		special2012.put("0352-4906", "M51"); 			//				0352-4906	Zbornik Matice srpske za prirodne nauke
		special2012.put("0354-9410", "M51"); 			//				0354-9410	Acta Entomologica Serbica
		special2012.put("0354-7310", "M52"); 			//				0354-7310	Archive of oncology
		special2012.put("0372-7866", "M52"); 			//				0372-7866	Zaštita bilja
		special2012.put("0514-6658", "M53"); 			//				0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2012.put("0354-4311", "M53"); 			//				0354-4311	Acta biologica iugoslavica - serija G: Acta herbologica
		special2012.put("2217-5547", "M53"); 			//NOVI U KOB	2217-5547	Water Research and Management
		special2012.put("1450-9636", "M53"); 			//				1450-9636	Kragujevac Journal of Science
		special2012.put("0350-0519", "M53"); 			//				0350-0519	Vodoprivreda
		special2011.put("0514-5899", "M53"); 			//				0514-5899	Zaštita prirode
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiologija2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);
	}
	
	public static MNOBiologija MNOBiologija= null;
	
	public static MNOBiologija getMNOBiologija() {
		if(MNOBiologija==null)
			MNOBiologija = new MNOBiologija();
		return MNOBiologija;
	}
	
}
