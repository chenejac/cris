/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOEnergetskeTehnologijeIRudarstvo extends MNO{

	private MNOEnergetskeTehnologijeIRudarstvo() {
		super();
		this.nameMNO = "Energetske tehnologije i rudarstvo";
		this.yearsSpecial = new int [2];
		yearsSpecial[0] = 2009;
		yearsSpecial[1] = 2010;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2009;
		HashMap <String, String> special2010;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2009 = new HashMap<String, String>();
		special2009.put("0354-9836", "M23"); 			//	0354-9836	Thermal Science
		special2009.put("0353-3670", "M24"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2009.put("1450-5959", "M24"); 			//	1450-5959	Journal of Mining and Metallurgy, Section A: Mining
		special2009.put("0013-5755", "M51"); 			//	0013-5755	Elektroprivreda
		special2009.put("0354-8651", "M51"); 			//	0354-8651	Energija, ekonomija, ekologija
		special2009.put("1451-4869", "M51"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2009.put("0013-5836", "M51"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2009.put("0350-218X", "M51"); 			//	0350-218X	Termotehnika
		special2009.put("0354-2904", "M51"); 			//	0354-2904	Podzemni radovi
		special2009.put("1451-0162", "M51"); 			//	1451-0162	Rudarski radovi, Bor
		special2009.put("1451-107X", "M51"); 			//	1451-107X	Transport i logistika
		special2009.put("1820-7480", "M52"); 			//	1820-7480	Reciklaža i održivi razvoj
		special2009.put("0350-2627", "M52"); 			//	0350-2627	Tehnika - Rudarstvo, geologija i metalurgija
		special2009.put("0351-0212", "M53"); 			//	0351-0212	Bakar
		special2009.put("0353-2631", "M53"); 			//	0353-2631	Inovacije i razvoj
		special2009.put("0352-678X", "M53"); 			//	0352-678X	Procesna tehnika	
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0354-9836", "M23"); 			//	0354-9836	Thermal Science
		special2010.put("0353-3670", "M24"); 			//	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2010.put("1450-5959", "M51"); 			//	1450-5959	Journal of Mining and Metallurgy, Section A: Mining
		special2010.put("1451-0162", "M51"); 			//	1451-0162	Rudarski radovi, Bor
		special2010.put("1451-4869", "M51"); 			//	1451-4869	Serbian Journal of Electrical Engineering
		special2010.put("0013-5836", "M51"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2010.put("0013-5755", "M52"); 			//	0013-5755	Elektroprivreda
		special2010.put("0354-2904", "M52"); 			//	0354-2904	Podzemni radovi
		special2010.put("0350-218X", "M52"); 			//	0350-218X	Termotehnika
		special2010.put("0350-2627", "M52"); 			//	0350-2627	Tehnika - Rudarstvo, geologija i metalurgija
		special2010.put("1451-107X", "M52"); 			//	1451-107X	Transport i logistika
		special2010.put("0351-0212", "M53"); 			//	0351-0212	Bakar
		special2010.put("0353-2631", "M53"); 			//	0353-2631	Inovacije i razvoj	
		this.specialJournalsAllYears.put(2010, special2010);
		
	}
	
	public static MNOEnergetskeTehnologijeIRudarstvo MNOEnergetskeTehnologijeIRudarstvo= null;
	
	public static MNOEnergetskeTehnologijeIRudarstvo getMNOEnergetskeTehnologijeIRudarstvo() {
		if(MNOEnergetskeTehnologijeIRudarstvo==null)
			MNOEnergetskeTehnologijeIRudarstvo = new MNOEnergetskeTehnologijeIRudarstvo();
		return MNOEnergetskeTehnologijeIRudarstvo;
	}
}
