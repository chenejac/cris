/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOEnergetskaEfikasnost extends MNO{

	private MNOEnergetskaEfikasnost() {
		super();
		this.nameMNO = "Energetska efikasnost";
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
		special2009.put("0354-6306", "M24"); 			//	0354-6306	Metalurgija
		special2009.put("1450-5029", "M51"); 			//	1450-5029	Časopis za procesnu tehniku i energetiku u poljoprivredi / PTEP
		special2009.put("0354-4605", "M51"); 			//	0354-4605	Facta Universitatis: Series Architecture and Civil Engineering
		special2009.put("0013-5755", "M51"); 			//	0013-5755	Elektroprivreda
		special2009.put("0354-8651", "M51"); 			//	0354-8651	Energija, ekonomija, ekologija
		special2009.put("1451-2092", "M51"); 			//	1451-2092	FME Transactions
		special2009.put("0354-8589", "M51"); 			//	0354-8589	Gas
		special2009.put("0350-1426", "M51"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		special2009.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2009.put("0554-5587", "M51"); 			//	0554-5587	Poljoprivredna tehnika
		special2009.put("0352-678X", "M51"); 			//	0352-678X	Procesna tehnika
		special2009.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2009.put("0040-2176", "M51"); 			//	0040-2176	Tehnika
		special2009.put("0013-5836", "M51"); 			//	0013-5836	Tehnika – Elektrotehnika
		special2009.put("0350-218X", "M51"); 			//	0350-218X	Termotehnika
		special2009.put("1450-5584", "M51"); 			//	1450-5584	Theoretical and Applied Mechanics
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0354-9836", "M23"); 			//	0354-9836	Thermal Science	
		special2010.put("1450-5584", "M24"); 			//	1450-5584	Theoretical and Applied Mechanics
		special2010.put("1450-5029", "M51"); 			//	1450-5029	Časopis za procesnu tehniku i energetiku u poljoprivredi / PTEP (novi naslov: Journal on Processing and Energy in Agriculture)
		special2010.put("1821-4487", "M51"); 			//	1821-4487	Journal on Processing and Energy in Agriculture
		special2010.put("0354-4605", "M51"); 			//	0354-4605	Facta Universitatis: Series Architecture and Civil Engineering
		special2010.put("0354-2025", "M51"); 			//	0354-2025	Facta Universitatis: Series Mechanical engineering
		special2010.put("0354-4656", "M51"); 			//	0354-4656	Facta Universitatis: Series Physics, Chemistry and Technology
		special2010.put("1451-2092", "M51"); 			//	1451-2092	FME Transactions
		special2010.put("0354-6306", "M51"); 			//	0354-6306	Metalurgija
		special2010.put("0554-5587", "M51"); 			//	0554-5587	Poljoprivredna tehnika
		special2010.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2010.put("0354-8589", "M52"); 			//	0354-8589	Gas
		special2010.put("0013-5755", "M52"); 			//	0013-5755	Elektroprivreda
		special2010.put("0350-1426", "M52"); 			//	0350-1426	Klimatizacija, grejanje, hlađenje
		special2010.put("0350-218X", "M52"); 			//	0350-218X	Termotehnika
		special2010.put("0013-5836", "M52"); 			//	0013-5836	Tehnika – Elektrotehnika
		this.specialJournalsAllYears.put(2010, special2010);
		
	}
	
	public static MNOEnergetskaEfikasnost MNOEnergetskaEfikasnost= null;
	
	public static MNOEnergetskaEfikasnost getMNOEnergetskaEfikasnost() {
		if(MNOEnergetskaEfikasnost==null)
			MNOEnergetskaEfikasnost = new MNOEnergetskaEfikasnost();
		return MNOEnergetskaEfikasnost;
	}
}

