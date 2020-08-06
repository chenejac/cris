package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOSaobracajUrbanizamIGradevinarstvo extends MNO{

	private MNOSaobracajUrbanizamIGradevinarstvo() {
		super();
		this.nameMNO = "Saobraćaj, urbanizam i građevinarstvo";
		this.yearsSpecial = new int [11];
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
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();	
		special2009.put("0354-6055", "M51"); 			//	0354-6055	Arhitektura i urbanizam
		special2009.put("0350-5421", "M51"); 			//	0350-5421	Izgradnja
		special2009.put("0543-0798", "M51"); 			//	0543-0798	Materijali i konstrukcije
		special2009.put("0558-6208", "M52"); 			//	0558-6208	Tehnika – Saobraćaj
		this.specialJournalsAllYears.put(2009, special2009);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2010 = new HashMap<String, String>();
		special2010.put("0354-4605", "M24"); 			//	0354-4605	Facta universitatis - Architecture and Civil Engineering
		special2010.put("1450-569X", "M24"); 			//	1450-569X	Spatium
		special2010.put("0354-6055", "M51"); 			//	0354-6055	Arhitektura i urbanizam
		special2010.put("0543-0798", "M51"); 			//	0543-0798	Materijali i konstrukcije
		special2010.put("0350-5421", "M52"); 			//	0350-5421	Izgradnja
		special2010.put("1451-8341", "M52"); 			//	1451-8341	Nauka + Praksa
		special2010.put("0558-6208", "M52"); 			//	0558-6208	Tehnika – Saobraćaj
		special2010.put("0350-0519", "M52"); 			//	0350-0519	Vodoprivreda
		special2010.put("0354-7965", "M52"); 			//	0354-7965	Zavarivanje i zavarene konstrukcije
		special2010.put("0350-8587", "M52"); 			//	0350-8587	Zbornik radova Građevinsko-arhitektonskog fakulteta, Niš
		special2010.put("0352-2733", "M53"); 			//	0352-2733	Građevinski kalendar
		special2010.put("0350-6355", "M53"); 			//	0350-6355	Institut za puteve
		special2010.put("1451-3749", "M53"); 			//	1451-3749	Integritet i vek konstrukcija
		special2010.put("0350-1426", "M53"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		special2010.put("0478-9733", "M53"); 			//	0478-9733	Put i saobraćaj
		special2010.put("0350-2619", "M53"); 			//	0350-2619	Tehnika-građevinarstvo
		special2010.put("1451-107X", "M53"); 			//	1451-107X	Transport i logistika
		special2010.put("0350-5049", "M53"); 			//	0350-5049	Voda i sanitarna tehnika
		special2010.put("0352-6852", "M53"); 			//	0352-6852	Zbornik Građevinskog fakulteta Subotica	
		this.specialJournalsAllYears.put(2010, special2010);
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2011 = new HashMap<String, String>();
		special2011.put("0354-4605", "M24"); 			//	0354-4605	Facta universitatis - Architecture and Civil Engineering
		special2011.put("1450-569X", "M24"); 			//	1450-569X	Spatium
		special2011.put("0354-7965", "M24"); 			//	0354-7965	Zavarivanje i zavarene konstrukcije
		special2011.put("0354-6055", "M51"); 			//	0354-6055	Arhitektura i urbanizam
		special2011.put("1451-3749", "M51"); 			//	1451-3749	Integritet i vek konstrukcija
		special2011.put("0543-0798", "M51"); 			//	0543-0798	Materijali i konstrukcije
		special2011.put("0350-8587", "M52"); 			//	0350-8587	Zbornik radova Građevinsko-arhitektonskog fakulteta, Niš
		special2011.put("0350-5421", "M52"); 			//	0350-5421	Izgradnja
		special2011.put("1451-4117", "M52"); 			//	1451-4117	Istraživanja i projektovanja za privredu
		special2011.put("1451-8341", "M52"); 			//	1451-8341	Nauka + Praksa
		special2011.put("0478-9733", "M52"); 			//	0478-9733	Put i saobraćaj
		special2011.put("0350-2619", "M52"); 			//	0350-2619	Tehnika-građevinarstvo
		special2011.put("0558-6208", "M52"); 			//	0558-6208	Tehnika – Saobraćaj
		special2011.put("1821-3952", "M53"); 			//	1821-3952	Serbian Architectural Journal
		special2011.put("0350-5049", "M53"); 			//	0350-5049	Voda i sanitarna tehnika
		special2011.put("0350-0519", "M53"); 			//	0350-0519	Vodoprivreda
		special2011.put("0352-2733", "M53"); 			//	0352-2733	Građevinski kalendar
		special2011.put("0352-6852", "M53"); 			//	0352-6852	Zbornik Građevinskog fakulteta Subotica
		special2011.put("0350-6355", "M53"); 			//	0350-6355	Institut za puteve
		special2011.put("0350-1426", "M53"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1450-569X", "M24"); 			//	1450-569X	Spatium
		special2012.put("1451-3749", "M24"); 			//	1451-3749	Integritet i vek konstrukcija
		special2012.put("0354-4605", "M51"); 			//	0354-4605	Facta universitatis - Architecture and Civil Engineering
		special2012.put("0354-6055", "M51"); 			//	0354-6055	Arhitektura i urbanizam
		special2012.put("0354-7965", "M51"); 			//	0354-7965	Zavarivanje i zavarene konstrukcije
		special2012.put("0543-0798", "M51"); 			//	0543-0798	Građevinski materijali i konstrukcije
		special2012.put("0350-5421", "M51"); 			//	0350-5421	Izgradnja
		special2012.put("0040-2176", "M51"); 			//	0040-2176	Tehnika
		special2012.put("0350-0519", "M52"); 			//	0350-0519	Vodoprivreda
		special2012.put("0350-8587", "M52"); 			//	0350-8587	Zbornik radova Građevinsko-arhitektonskog fakulteta, Niš
		special2012.put("0478-9733", "M52"); 			//	0478-9733	Put i saobraćaj
		special2012.put("1821-3952", "M52"); 			//	1821-3952	Serbian Architectural Journal
		special2012.put("0350-5049", "M53"); 			//	0350-5049	Voda i sanitarna tehnika
		special2012.put("0352-2733", "M53"); 			//	0352-2733	Građevinski kalendar
		special2012.put("0352-6852", "M53"); 			//	0352-6852	Zbornik Građevinskog fakulteta Subotica
		special2012.put("1451-8341", "M53"); 			//	1451-8341	Nauka + Praksa
		special2012.put("2217-544X", "M53"); 			//	2217-544X	IJTTE International Journal for Trafific and Transport Engineering
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSaobracajUrbanizamIGradjervinarstvo2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);
	}
	
	public static MNOSaobracajUrbanizamIGradevinarstvo MNOSaobracajUrbanizamIGradevinarstvo= null;
	
	public static MNOSaobracajUrbanizamIGradevinarstvo getMNOSaobracajUrbanizamIGradevinarstvo() {
		if(MNOSaobracajUrbanizamIGradevinarstvo==null)
			MNOSaobracajUrbanizamIGradevinarstvo = new MNOSaobracajUrbanizamIGradevinarstvo();
		return MNOSaobracajUrbanizamIGradevinarstvo;
	}
}
