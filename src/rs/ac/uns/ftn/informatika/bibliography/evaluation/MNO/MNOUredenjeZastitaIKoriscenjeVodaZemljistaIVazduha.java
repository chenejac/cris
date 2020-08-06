package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha extends MNO{
	
	private MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha(){
		super();
		this.nameMNO = "Uređenje, zaštitu i korišćenje voda, zemljišta i vazduha";
		this.yearsSpecial = new int [8];
		yearsSpecial[0] = 2012;
		yearsSpecial[1] = 2013;
		yearsSpecial[2] = 2014;
		yearsSpecial[3] = 2015;
		yearsSpecial[4] = 2016;
		yearsSpecial[5] = 2017;
		yearsSpecial[6] = 2018;
		yearsSpecial[7] = 2019;
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
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2012 = new HashMap<String, String>();
		special2012.put("0365-4478", "M24"); //(BISIS)33575		0365-4478	Acta Medica Medianae
		special2012.put("0354-4605", "M24"); //(BISIS)31944		0354-4605	Facta Universitatis: Series Architecture and Civil Engineering
		special2012.put("1452-4864", "M24"); //(BISIS)37492		1452-4864	Serbian Journal of Management
		special2012.put("0354-3285", "M51"); //(BISIS)37492		0354-3285	Ecologica
		special2012.put("0354-4656", "M51"); //(BISIS)24424		0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2012.put("0354-804X", "M51"); //(BISIS)36147		0354-804X	Facta Universitatis: Series Working and Living Environmental Protection
		special2012.put("1450-8109", "M51"); //					1916-9752	Journal of Agricultural Science
		special2012.put("1450-7404", "M51"); //(BISIS)24376		1450-7404	Journal of Research in Physics
		special2012.put("1450-9636", "M51"); //(BISIS)17260		1450-9636	Kragujevac Journal of Science
		special2012.put("0025-8105", "M51"); //(BISIS)17268		0025-8105	Medicinski pregled
		special2012.put("0040-2176", "M51"); //NEMA U KOB		0040-2176	Tehnika
		special2012.put("1450-5584", "M51"); //(BISIS)35956		1450-5584	Theoretical and Applied Mechanics
		special2012.put("2217-5547", "M51"); //NOVI U KOB		2217-5547	Water Research and Management
		special2012.put("0350-5049", "M51"); //(BISIS)8178		0350-5049	Вoда и санитарна техника
		special2012.put("0350-0519", "M51"); //(BISIS)24459		0350-0519	Vodoprivreda
		special2012.put("0350-3593", "M51"); //(BISIS)8455		0350-3593	Glasnik Srpskog geografskog društva
		special2012.put("0353-4537", "M51"); //(BISIS)17222		0353-4537	Glasnik Šumarskog fakulteta = Bulletin Faculty of forestry
		special2012.put("0354-9542", "M52"); //(BISIS)17104		0354-9542	Acta Agriculturae Serbica
		special2012.put("1451-9070", "M52"); //NEMA U KOB		1451-9070	Energetske tehnologije
		special2012.put("0351-9465", "M52"); //(BISIS)38817		0351-9465	Zaštita materijala
		special2012.put("0514-6658", "M52"); //NEMA U KOB		0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2012.put("0013-5755", "M52"); //(BISIS)38678		0013-5755	Elektroprivreda
		special2012.put("0350-9648", "M52"); //NEMA U KOB		0350-9648	Erozija
		special2012.put("0350-5421", "M52"); //NEMA U KOB		0350-5421	Izgradnja
		special2012.put("0354-6306", "M52"); //(BISIS)34764		0354-6306	Metalurgija
		special2012.put("1820-7480", "M52"); //(BISIS)23622		1820-7480	Reciklaža i održivi razvoj
		special2012.put("1451-0162", "M52"); //(BISIS)37637		1451-0162	Rudarski radovi
		special2012.put("0350-1752", "M52"); //(BISIS)17319		0350-1752	Šumarstvo
		special2012.put("0354-4699", "M53"); //(BISIS)32090		0354-4699	Facta Universitatis: Series Economics and Organization
		special2012.put("0354-2025", "M53"); //(BISIS)35588		0354-2025	Facta Universitatis: Series Mechanical engineering
		special2012.put("0352-2733", "M53"); //NEMA U KOB		0352-2733	Građevinski kalendar
		special2012.put("2217-7124", "M53"); //NEMA U KOB		2217-7124	Safety Engineering
		special2012.put("0409-2953", "M53"); //(BISIS)35067		0409-2953	Bezbednost
		special2012.put("0354-6829", "M53"); //(BISIS)58400		0354-6829	IMK-14 - Istraživanje i razvoj
		special2012.put("0440-6826", "M53"); //(BISIS)8267		0440-6826	Hemijski pregled
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUredjenjeZastitaIKoriscenjeVodaZemljistaIVazduha2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);
	}
	
	public static MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha= null;
	
	public static MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha() {
		if(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha==null)
			MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha = new MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha();
		return MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha;
	}
}
