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
public class MNOBiotehnologijaIPoljoprivreda extends MNO{
	
	private MNOBiotehnologijaIPoljoprivreda(){
		super();
		this.nameMNO = "Biotehnologija i poljoprivreda";
		this.yearsSpecial = new int [11];
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
		yearsSpecial[10] = 2022;
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
		HashMap <String, String> special2022;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2012 = new HashMap<String, String>();
		special2012.put("1450-7188", "M24"); 			//	1450-7188	Acta periodica technologica
		special2012.put("1018-1806", "M24"); 			//	1018-1806	Helia
		special2012.put("1450-9156", "M24"); 			//	1450-9156	Biotechnology in Animal Husbandry
		special2012.put("0350-2457", "M24"); 			//	0350-2457	Veterinarski glasnik
		special2012.put("1820-9955", "M51"); 			//	1820-9955	Arhiv veterinarske medicine
		special2012.put("1820-5054", "M51"); 			//	1820-5054	Voćarstvo
		special2012.put("0353-4537", "M51"); 			//	0353-4537	Glasnik Šumarskog fakulteta
		special2012.put("0372-7866", "M51"); 			//	0372-7866	Zaštita bilja
		special2012.put("0352-4906", "M51"); 			//	0352-4906	Zbornik Matice srpske za prirodne nauke
		special2012.put("1820-3949", "M51"); 			//	1820-3949	Pesticidi i fitomedicina
		special2012.put("1821-3944", "M51"); 			//	1821-3944	Ratarstvo i povrtarstvo
		special2012.put("0350-1205", "M51"); 			//	0350-1205	Savremena poljoprivreda
		special2012.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2012.put("0494-9846", "M51"); 			//	0494-9846	Tehnologija mesa
		special2012.put("0514-6658", "M52"); 			//	0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2012.put("0354-4311", "M52"); 			//	0354-4311	Acta herbologica
		special2012.put("2217-5369", "M52"); 			//	2217-5369	Food and Feed research
		special2012.put("1450-8109", "M52"); 			//	1450-8109	Journal of Agricultural Science
		special2012.put("1821-4487", "M52"); 			//	1821-4487	Journal on Processing and Energy in Agriculture
		special2012.put("1821-1046", "M52"); 			//	1821-1046	Sustainable Forestry: Collection
		special2012.put("0354-9496", "M52"); 			//	0354-9496	Traktori i pogonske mašine
		special2012.put("0354-6160", "M52"); 			//	0354-6160	Biljni lekar
		special2012.put("0350-0519", "M52"); 			//	0350-0519	Vodoprivreda
		special2012.put("0514-5899", "M52"); 			//	0514-5899	Zaštita prirode
		special2012.put("0546-8264", "M52"); 			//	0546-8264	Letopis radova Poljoprivrednog fakulteta
		special2012.put("0554-5587", "M52"); 			//	0554-5587	Poljoprivredna tehnika
		special2012.put("1451-401X", "M52"); 			//	1451-401X	Prerada drveta
		special2012.put("0353-6564", "M52"); 			//	0353-6564	Prehrambena industrija – Mleko i mlečni proizvodi
		special2012.put("0354-5881", "M52"); 			//	0354-5881	Selekcija i semenarstvo
		special2012.put("0563-9034", "M52"); 			//	0563-9034	Topola
		special2012.put("0018-6872", "M52"); 			//	0018-6872	Hrana i ishrana
		special2012.put("0350-1752", "M52"); 			//	0350-1752	Šumarstvo
		special2012.put("0354-9542", "M53"); 			//	0354-9542	Acta Agriculturae Serbica
		special2012.put("0350-5928", "M53"); 			//	0350-5928	Agroekonomika
		special2012.put("0351-9430", "M53"); 			//	0351-9430	Bilten za hmelj, sirak i lekovito bilje
		special2012.put("2217-7205", "M53"); 			//	2217-7205	Bilten za alternativne biljne vrste
		special2012.put("0354-5695", "M53"); 			//	0354-5695	Journal of Scientific Agricultural Research
		special2012.put("0455-6224", "M53"); 			//	0455-6224	Lekovite sirovine
		special2012.put("0354-1320", "M53"); 			//	0354-1320	Zbornik naučnih radova Instituta PKB Agroekonomik
		special2012.put("0354-6438", "M53"); 			//	0354-6438	Poljoprivredne aktuelnosti
		special2012.put("0351-9503", "M53"); 			//	0351-9503	Uljarstvo
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);

		special2020 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2020.xlsx", special2020);
		this.specialJournalsAllYears.put(2020, special2020);

		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoBiotehnologijaIPoljoprivreda2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_za_biotehnologiju_i_poljoprivredu.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);
	}
	
	public static MNOBiotehnologijaIPoljoprivreda MNOBiotehnologijaIPoljoprivreda= null;
	
	public static MNOBiotehnologijaIPoljoprivreda getMNOBiotehnologijaIPoljoprivreda() {
		if(MNOBiotehnologijaIPoljoprivreda==null)
			MNOBiotehnologijaIPoljoprivreda = new MNOBiotehnologijaIPoljoprivreda();
		return MNOBiotehnologijaIPoljoprivreda;
	}
}

