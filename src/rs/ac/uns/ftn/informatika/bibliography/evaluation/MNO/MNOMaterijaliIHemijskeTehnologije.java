package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOMaterijaliIHemijskeTehnologije extends MNO{
	
	private MNOMaterijaliIHemijskeTehnologije(){
		super();
		this.nameMNO = "Materijali i hemijske tehnologije";
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
		special2009.put("1451-9372", "M23"); //		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2009.put("0367-598X", "M23"); //		0367-598X	Hemijska industrija
		special2009.put("0352-5139", "M23"); //		0352-5139	Journal of the Serbian Chemical Society
		special2009.put("1451-3994", "M23"); //		1451-3994	Nuclear Technology and Radiation Protection
		special2009.put("0350-820X", "M23"); //		0350-820X	Science of Sintering
		special2009.put("1450-5339", "M24"); //		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2009.put("1450-7188", "M51"); //		1450-7188	Acta periodica technologica
		special2009.put("0354-3285", "M52"); //		0354-3285	Ecologica
		special2009.put("0354-6306", "M52"); //		0354-6306	Metalurgija
		special2009.put("0354-8872", "M52"); //		0354-8872	Nauka, bezbednost i policija – Žurnal za kriminalistiku
		special2009.put("0350-0667", "M52"); //		0350-0667	Naučno-tehnički pregled
		special2009.put("1820-0206", "M52"); //		1820-0206	Scientific Technical Review
		special2009.put("0351-0212", "M53"); //		0351-0212	Bakar
		special2009.put("0354-3870", "M53"); //		0354-3870	Journal of Technology and Plasticity
		special2009.put("0353-5517", "M53"); //		0353-5517	Nauka, tehnika, bezbednost
		special2009.put("1450-6734", "M53"); //		1450-6734	Svet polimera
		special2009.put("0354-2300", "M53"); //		0354-2300	Tehnika - Novi materijali
		special2009.put("0040-2389", "M53"); //		0040-2389	Tekstilna industrija
		special2009.put("0351-1642", "M53"); //		0351-1642	Tribology in Industry
		special2009.put("0350-5049", "M53"); //		0350-5049	Voda i sanitarna tehnika
		special2009.put("0351-9465", "M53"); //		0351-9465	Zaštita materijala
		special2009.put("0352-6542", "M53"); //		0352-6542	Zbornik Tehnološkog fakulteta u Leskovcu
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("1450-5339", "M22"); //		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2010.put("1451-9372", "M23"); //		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2010.put("0367-598X", "M23"); //		0367-598X	Hemijska industrija
		special2010.put("0352-5139", "M23"); //		0352-5139	Journal of the Serbian Chemical Society
		special2010.put("1451-3994", "M23"); //		1451-3994	Nuclear Technology and Radiation Protection
		special2010.put("0350-820X", "M23"); //		0350-820X	Science of Sintering
		special2010.put("1450-7188", "M51"); //		1450-7188	Acta periodica technologica
		special2010.put("0354-3285", "M52"); //		0354-3285	Ecologica
		special2010.put("0354-8872", "M52"); //		0354-8872	Nauka, bezbednost i policija – Žurnal za kriminalistiku
		special2010.put("1820-6131", "M52"); //		1820-6131	Processing and Application of Ceramics
		special2010.put("0350-0667", "M52"); //		0350-0667	Naučno-tehnički pregled
		special2010.put("1820-0206", "M52"); //		1820-0206	Scientific Technical Review
		special2010.put("0351-1642", "M52"); //		0351-1642	Tribology in Industry
		special2010.put("0351-0212", "M53"); //		0351-0212	Bakar
		special2010.put("0354-3870", "M53"); //		0354-3870	Journal for Technology of Plasticity
		special2010.put("0354-6306", "M53"); //		0354-6306	Metalurgija
		special2010.put("1450-6734", "M53"); //		1450-6734	Svet polimera
		special2010.put("0040-2389", "M53"); //		0040-2389	Tekstilna industrija
		special2010.put("0354-2300", "M53"); //		0354-2300	Tehnika - Novi materijali
		special2010.put("0350-5049", "M53"); //		0350-5049	Voda i sanitarna tehnika
		special2010.put("0351-9465", "M53"); //		0351-9465	Zaštita materijala
		this.specialJournalsAllYears.put(2010, special2010);
		
		
		special2011 = new HashMap<String, String>();
		special2011.put("1450-5339", "M21"); //		1450-5339	Journal of mining and metallurgy, Section B: Metallurgy
		special2011.put("1451-9372", "M23"); //		1451-9372	Chemical Industry and Chemical Engineering Quarterly / CICEQ
		special2011.put("0352-5139", "M23"); //		0352-5139	Journal of the Serbian Chemical Society
		special2011.put("1451-3994", "M23"); //		1451-3994	Nuclear Technology and Radiation Protection
		special2011.put("0350-820X", "M23"); //		0350-820X	Science of Sintering
		special2011.put("0367-598X", "M23"); //		0367-598X	Hemijska industrija
		special2011.put("1450-7188", "M51"); //		1450-7188	Acta periodica technologica
		special2011.put("0350-0667", "M51"); //		0350-0667	Naučno-tehnički pregled
		special2011.put("1820-0206", "M51"); //		1820-0206	Scientific Technical Review
		special2011.put("0354-3285", "M52"); //		0354-3285	Ecologica
		special2011.put("0354-3870", "M52"); //		0354-3870	Journal for Technology of Plasticity
		special2011.put("1820-6131", "M52"); //		1820-6131	Processing and Application of Ceramics
		special2011.put("0351-1642", "M52"); //		0351-1642	Tribology in Industry
		special2011.put("0351-0212", "M52"); //		0351-0212	Bakar
		special2011.put("0350-5049", "M52"); //		0350-5049	Voda i sanitarna tehnika
		special2011.put("0042-8469", "M52"); //		0042-8469	Vojnotehnički glasnik
		special2011.put("0351-9465", "M52"); //		0351-9465	Zaštita materijala
		special2011.put("0352-6542", "M52"); //		0352-6542	Zbornik Tehnološkog fakulteta u Leskovcu
		special2011.put("0354-6306", "M52"); //		0354-6306	Metalurgija
		special2011.put("0354-8872", "M52"); //		0354-8872	Nauka, bezbednost i policija – Žurnal za kriminalistiku
		special2011.put("0353-5517", "M52"); //		0353-5517	Nauka, tehnika, bezbednost
		special2011.put("1450-6734", "M52"); //		1450-6734	Svet polimera
		special2011.put("0040-2389", "M52"); //		0040-2389	Tekstilna industrija
		special2011.put("0354-2300", "M52"); //		0354-2300	Tehnika - Novi materijali	
		this.specialJournalsAllYears.put(2011, special2011);
		
		special2012 = new HashMap<String, String>();
		special2012.put("1450-7188", "M24"); //		1450-7188	Acta periodica technologica
		special2012.put("0354-3285", "M51"); //		0354-3285	Ecologica
		special2012.put("0350-0667", "M51"); //		0350-0667	Naučno-tehnički pregled
		special2012.put("1820-0206", "M51"); //		1820-0206	Scientific Technical Review
		special2012.put("0351-9465", "M51"); //		0351-9465	Zaštita materijala
		special2012.put("0354-6306", "M51"); //		0354-6306	Metalurgija
		special2012.put("0354-8872", "M51"); //		0354-8872	Nauka, bezbednost i policija – Žurnal za kriminalistiku
		special2012.put("0354-3870", "M52"); //		0354-3870	Journal for Technology of Plasticity
		special2012.put("1820-6131", "M52"); //		1820-6131	Processing and Application of Ceramics
		special2012.put("0351-0212", "M52"); //		0351-0212	Bakar
		special2012.put("0350-5049", "M52"); //		0350-5049	Voda i sanitarna tehnika
		special2012.put("0352-6542", "M52"); //		0352-6542	Zbornik Tehnološkog fakulteta u Leskovcu
		special2012.put("1450-6734", "M52"); //		1450-6734	Svet polimera
		special2012.put("0040-2389", "M52"); //		0040-2389	Tekstilna industrija
		special2012.put("0040-2176", "M52"); //		0040-2176	Tehnika
		special2012.put("1820-7480", "M53"); //		1820-7480	Reciklaža i održivi razvoj
		this.specialJournalsAllYears.put(2012, special2012);
		
		special2013 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2013.xlsx", special2013);
		this.specialJournalsAllYears.put(2013, special2013);
		
		special2014 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2014.xlsx", special2014);
		this.specialJournalsAllYears.put(2014, special2014);
		
		special2015 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2015.xlsx", special2015);
		this.specialJournalsAllYears.put(2015, special2015);
		
		special2016 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2016.xlsx", special2016);
		this.specialJournalsAllYears.put(2016, special2016);
		
		special2017 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2017.xlsx", special2017);
		this.specialJournalsAllYears.put(2017, special2017);
		
		special2018 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2018.xlsx", special2018);
		this.specialJournalsAllYears.put(2018, special2018);
		
		special2019 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMaterijaliIHemijskaTehnologija2019.xlsx", special2019);
		this.specialJournalsAllYears.put(2019, special2019);
	}
	
	public static MNOMaterijaliIHemijskeTehnologije MNOMaterijaliIHemijskeTehnologije= null;
	
	public static MNOMaterijaliIHemijskeTehnologije getMNOMaterijaliIHemijskeTehnologije() {
		if(MNOMaterijaliIHemijskeTehnologije==null)
			MNOMaterijaliIHemijskeTehnologije = new MNOMaterijaliIHemijskeTehnologije();
		return MNOMaterijaliIHemijskeTehnologije;
	}
}
