package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOGeonaukeIAstronomija extends MNO{

	private MNOGeonaukeIAstronomija() {
		super();
		this.nameMNO = "Geonauke i astronomija";
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
		special2009.put("0350-0608", "M24"); //(BISIS)32816		0350-0608	Geološki anali Balkanskoga poluostrva
		special2009.put("0350-3593", "M24"); //(BISIS)8455		0350-3593	Glasnik Srpskog geografskog društva
		special2009.put("0739-1781", "M24"); //NEMA				0739-1781	The Journal of weather modification
		special2009.put("1450-698X", "M24"); //(BISIS)24454		1450-698X	Serbian Astronomical Journal
		special2009.put("0561-7332", "M51"); //(BISIS)17162		0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2009.put("1820-4244", "M51"); //(BISIS)8442		1820-4244	Demografija
		special2009.put("0354-8724", "M51"); //(BISIS)17216		0354-8724	Geographica Pannonica
		special2009.put("1450-7552", "M51"); //(BISIS)8484		1450-7552	Zbornik radova PMF - Geografski institut, Beograd , pomenio nayiv al na kobsonu nije, promenjeni naziv je Zbornik radova - Geografski fakultet Univerziteta u Beogradu
		special2009.put("0350-7599", "M51"); //(BISIS)8451		0350-7599	Zbornik radova Geografskog Instituta 'Jovan Cvijić'
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0350-0608", "M24"); //(BISIS)32816		0350-0608	Geološki anali Balkanskoga poluostrva
		special2010.put("1450-698X", "M24"); //(BISIS)24454		1450-698X	Serbian Astronomical Journal
		special2010.put("0561-7332", "M51"); //(BISIS)17162		0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2010.put("0354-8724", "M51"); //(BISIS)17216		0354-8724	Geographica Pannonica
		special2010.put("0350-3593", "M51"); //(BISIS)8455		0350-3593	Glasnik Srpskog geografskog društva
		special2010.put("0350-7599", "M51"); //(BISIS)8451		0350-7599	Zbornik radova Geografskog Instituta 'Jovan Cvijić'
		special2010.put("1450-7552", "M51"); //(BISIS)8484		1450-7552	Zbornik radova PMF - Geografski institut, Beograd , pomenio nayiv al na kobsonu nije, promenjeni naziv je Zbornik radova - Geografski fakultet Univerziteta u Beogradu
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("1450-698X", "M24"); //(BISIS)24454		1450-698X	Serbian Astronomical Journal
		special2011.put("0350-0608", "M24"); //(BISIS)32816		0350-0608	Geološki anali Balkanskoga poluostrva
		special2011.put("0561-7332", "M51"); //(BISIS)17162		0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2011.put("0354-8724", "M51"); //(BISIS)17216		0354-8724	Geographica Pannonica
		special2011.put("0350-3593", "M51"); //(BISIS)8455		0350-3593	Glasnik Srpskog geografskog društva
		special2011.put("0350-7599", "M51"); //(BISIS)8451		0350-7599	Zbornik radova Geografskog Instituta 'Jovan Cvijić'
		special2011.put("1450-7552", "M51"); //(BISIS)8484		1450-7552	Zbornik radova PMF - Geografski institut, Beograd , pomenio nayiv al na kobsonu nije, promenjeni naziv je Zbornik radova - Geografski fakultet Univerziteta u Beogradu
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1450-698X", "M24"); //(BISIS)24454		1450-698X	Serbian Astronomical Journal
		special2012.put("0350-0608", "M51"); //(BISIS)32816		0350-0608	Geološki anali Balkanskoga poluostrva
		special2012.put("0561-7332", "M51"); //(BISIS)17162		0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2012.put("0354-8724", "M51"); //(BISIS)17216		0354-8724	Geographica Pannonica
		special2012.put("0350-3593", "M51"); //(BISIS)8455		0350-3593	Glasnik Srpskog geografskog društva
		special2012.put("0350-7599", "M51"); //(BISIS)8451		0350-7599	Zbornik radova Geografskog Instituta 'Jovan Cvijić'
		special2012.put("1450-7552", "M51"); //(BISIS)8484		1450-7552	Zbornik radova PMF - Geografski institut, Beograd , pomenio nayiv al na kobsonu nije, promenjeni naziv je Zbornik radova - Geografski fakultet Univerziteta u Beogradu
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeonaukaIAstronomija2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);
	}

	public static MNOGeonaukeIAstronomija MNOGeonaukeIAstronomija= null;
	
	public static MNOGeonaukeIAstronomija getMNOGeonaukeIAstronomija() {
		if(MNOGeonaukeIAstronomija==null)
			MNOGeonaukeIAstronomija = new MNOGeonaukeIAstronomija();
		return MNOGeonaukeIAstronomija;
	}
}
