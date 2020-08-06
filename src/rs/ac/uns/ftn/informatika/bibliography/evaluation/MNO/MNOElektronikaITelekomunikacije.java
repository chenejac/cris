/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOElektronikaITelekomunikacije extends MNO{

	private MNOElektronikaITelekomunikacije() {
		super();
		this.nameMNO = "Elektronika i telekomunikacije";
		this.yearsSpecial = new int [3];
		yearsSpecial[0] = 2009;
		yearsSpecial[1] = 2010;
		yearsSpecial[2] = 2011;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2009;
		HashMap <String, String> special2010;
		HashMap <String, String> special2011;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();
		special2009.put("0353-3670", "M51"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2009.put("1450-5835", "M52"); 			//	1450-5835	Microwave Review
		special2009.put("0013-5836", "M52"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2009.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2009.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2009.put("1450-989X", "M52"); 			//	1450-989X	Tehnika - Kvalitet, standardizacija i metrologija
		special2009.put("0040-2605", "M53"); 			//	0040-2605	Telekomunikacije
		special2009.put("1451-4869", "M53"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2009.put("0042-8469", "M53"); 			//	0042-8469	Vojnotehnički glasnik
		special2009.put("0353-5517", "M53"); 			//	0353-5517	Nauka, tehnika, bezbednost
		special2009.put("1451-4397", "M53"); 			//	1451-4397	Info M
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0353-3670", "M24"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2010.put("1450-5835", "M51"); 			//	1450-5835	Microwave Review
		special2010.put("1820-0206", "M52"); 			//	1820-0206	Scientific Technical Review
		special2010.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2010.put("1451-4869", "M52"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2010.put("0013-5836", "M52"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2010.put("1451-4397", "M53"); 			//	1451-4397	Info M
		special2010.put("1450-989X", "M53"); 			//	1450-989X	Tehnika - Kvalitet, standardizacija i metrologija
		special2010.put("0042-8469", "M53"); 			//	0042-8469	Vojnotehnički glasnik
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0353-3670", "M24"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2011.put("1820-0206", "M51"); 			//	1820-0206	Scientific Technical Review
		special2011.put("0350-0667", "M52"); 			//	0350-0667	Naučno-tehnički pregled
		special2011.put("1451-4869", "M52"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2011.put("0042-8469", "M52"); 			//	0042-8469	Vojnotehnički glasnik
		special2011.put("1450-5835", "M52"); 			//	1450-5835	Microwave Review
		special2011.put("0013-5836", "M52"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2011.put("1451-4397", "M53"); 			//	1451-4397	Info M
		special2011.put("1450-989X", "M53"); 			//	1450-989X	Tehnika - Kvalitet, standardizacija i metrologija	
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOElektronikaITelekomunikacije MNOElektronikaITelekomunikacije= null;
	
	public static MNOElektronikaITelekomunikacije getMNOElektronikaITelekomunikacije() {
		if(MNOElektronikaITelekomunikacije==null)
			MNOElektronikaITelekomunikacije = new MNOElektronikaITelekomunikacije();
		return MNOElektronikaITelekomunikacije;
	}
	
}
