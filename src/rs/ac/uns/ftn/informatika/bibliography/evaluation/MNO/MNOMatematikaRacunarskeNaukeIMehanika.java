package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOMatematikaRacunarskeNaukeIMehanika extends MNO{
	
	private MNOMatematikaRacunarskeNaukeIMehanika(){
		super();
		this.nameMNO = "Matematika, računarske nauke i mehanika";
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
		special2012.put("0350-1302", "M24"); //(BISIS)2985	0350-1302	Publications de l'Institut Mathematique, Beograd
		special2012.put("0561-7332", "M51"); //(BISIS)17162	0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2012.put("0352-9665", "M51"); //(BISIS)2765	0352-9665	Facta Universitatis: Series Mathematics and Informatics
		special2012.put("1450-9628", "M51"); //(BISIS)2901	1450-9628	Kragujevac Journal of Mathematics
		special2012.put("1450-5444", "M51"); //(BISIS)2853	1450-5444	Novi Sad Journal of Mathematics
		special2012.put("0025-5165", "M51"); //(BISIS)2913	0025-5165	Matematički vesnik
		special2012.put("1450-5584", "M51"); //(BISIS)35956	1450-5584	Theoretical and Applied Mechanics
		special2012.put("0354-0243", "M51"); //(BISIS)3039	0354-0243	Yugoslav journal of operations research
		special2012.put("1821-410x", "M52"); //(BISIS)31575	1821-410x	Functional Analysis, Approximation and Computation
		special2012.put("1450-5932", "M52"); //(BISIS)38860	1450-5932	Mathematica Moravica
		special2012.put("2217-5539", "M52"); //(BISIS)37964	2217-5539	Scientific Publications of the State University of Novi Pazar. Series A, Applied Mathematics, Informatics and Mechanics
		special2012.put("0042-8469", "M52"); //(BISIS)8482	0042-8469	Vojnotehnički glasnik
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMatematikaRacunarskeNaukeMehanika2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);
	}
	
	public static MNOMatematikaRacunarskeNaukeIMehanika MNOMatematikaRacunarskeNaukeIMehanika= null;
	
	public static MNOMatematikaRacunarskeNaukeIMehanika getMNOMatematikaRacunarskeNaukeIMehanika() {
		if(MNOMatematikaRacunarskeNaukeIMehanika==null)
			MNOMatematikaRacunarskeNaukeIMehanika = new MNOMatematikaRacunarskeNaukeIMehanika();
		return MNOMatematikaRacunarskeNaukeIMehanika;
	}
}