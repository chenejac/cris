/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOBiotehnologijaIAgroindustrija extends MNO{

	private MNOBiotehnologijaIAgroindustrija() {
		super();
		this.nameMNO = "Biotehnologija i agroindustrija";
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
		special2009.put("0567-8315", "M23"); 			//	0567-8315	Аcta veterinaria
		special2009.put("1018-1806", "M24"); 			//	1018-1806	Helia
		special2009.put("1450-8109", "M51"); 			//	1450-8109	Journal of Agricultural Science
		special2009.put("0354-7698", "M51"); 			//	0354-7698	Zbornik radova Instituta za ratarstvo i povrtarstvo
		special2009.put("0534-0012", "M51"); 			//	0534-0012	Genetika
		special2009.put("1820-5054", "M51"); 			//	1820-5054	Voćarstvo
		special2009.put("1820-3949", "M51"); 			//	1820-3949	Pesticidi i fitomedicina
		special2009.put("0354-4311", "M51"); 			//	0354-4311	Acta biologica Iugoslavica - serija G: Acta herbologica
		special2009.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2009.put("1450-9156", "M51"); 			//	1450-9156	Biotechnology in Animal Husbandry
		special2009.put("0353-4537", "M51"); 			//	0353-4537	Glasnik Šumarskog fakulteta
		special2009.put("0494-9846", "M51"); 			//	0494-9846	Tehnologija mesa
		special2009.put("1450-7188", "M51"); 			//	1450-7188	Acta periodica technologica
		special2009.put("0350-1205", "M51"); 			//	0350-1205	Savremena poljoprivreda
		special2009.put("0514-6658", "M52"); 			//	0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2009.put("0354-9542", "M52"); 			//	0354-9542	Acta Agriculturae Serbica
		special2009.put("0354-5695", "M52"); 			//	0354-5695	Journal of Scientific Agricultural Research
		special2009.put("0354-5881", "M52"); 			//	0354-5881	Selekcija i semenarstvo
		special2009.put("1450-5029", "M52"); 			//	1450-5029	Časopis za procesnu tehniku i energetiku u poljoprivredi / PTEP
		special2009.put("0554-5587", "M52"); 			//	0554-5587	Poljoprivredna tehnika
		special2009.put("0350-1752", "M52"); 			//	0350-1752	Šumarstvo
		special2009.put("0514-5899", "M52"); 			//	0514-5899	Zaštita prirode
		special2009.put("1451-401X", "M52"); 			//	1451-401X	Prerada drveta
		special2009.put("0350-2457", "M52"); 			//	0350-2457	Veterinarski glasnik
		special2009.put("0353-6564", "M52"); 			//	0353-6564	Prehrambena industrija – Mleko i mlečni proizvodi
		special2009.put("0018-6872", "M53"); 			//	0018-6872	Hrana i ishrana
		special2009.put("0351-9430", "M53"); 			//	0351-9430	Bilten za hmelj, sirak i lekovito bilje
		special2009.put("0354-6160", "M53"); 			//	0354-6160	Biljni lekar
		special2009.put("0372-7866", "M53"); 			//	0372-7866	Zaštita bilja
		special2009.put("0354-8457", "M53"); 			//	0354-8457	Agricultural Engineering
		special2009.put("0002-1458", "M53"); 			//	0002-1458	Agricultural Engineering
		special2009.put("1451-1541", "M53"); 			//	1451-1541	Mlekarsto
		special2009.put("0563-9034", "M53"); 			//	0563-9034	Topola
		special2009.put("0354-1894", "M53"); 			//	0354-1894	Zbornik Instituta za šumarstvo
		special2009.put("0563-9034", "M53"); 			//	0563-9034	Radovi Instituta za nizijsko šumarstvo
		special2009.put("0350-0519", "M53"); 			//	0350-0519	Vodoprivreda
		special2009.put("0351-9503", "M53"); 			//	0351-9503	Uljarstvo
		special2009.put("0351-0999", "M53"); 			//	0351-0999	Žito hleb
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0567-8315", "M23"); 			//	0567-8315	Аcta veterinaria
		special2010.put("0534-0012", "M24"); 			//	0534-0012	Genetika
		special2010.put("1018-1806", "M24"); 			//	1018-1806	Helia
		special2010.put("0354-4311", "M51"); 			//	0354-4311	Acta biologica Iugoslavica - serija G: Acta herbologica
		special2010.put("1450-7188", "M51"); 			//	1450-7188	Acta periodica technologica
		special2010.put("1450-9156", "M51"); 			//	1450-9156	Biotechnology in Animal Husbandry
		special2010.put("0353-4537", "M51"); 			//	0353-4537	Glasnik Šumarskog fakulteta
		special2010.put("1450-8109", "M51"); 			//	1450-8109	Journal of Agricultural Science
		special2010.put("1820-3949", "M51"); 			//	1820-3949	Pesticidi i fitomedicina
		special2010.put("0350-1205", "M51"); 			//	0350-1205	Savremena poljoprivreda
		special2010.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2010.put("0350-2457", "M51"); 			//	0350-2457	Veterinarski glasnik
		special2010.put("1820-5054", "M51"); 			//	1820-5054	Voćarstvo
		special2010.put("0352-4906", "M51"); 			//	0352-4906	Zbornik Matice srpske za prirodne nauke
		special2010.put("0354-7698", "M51"); 			//	0354-7698	Zbornik radova Instituta za ratarstvo i povrtarstvo (novi naslov: Ratarstvo i povrtarstvo)
		special2010.put("1821-3944", "M51"); 			//	1821-3944	Ratarstvo i povrtarstvo
		special2010.put("0354-9542", "M52"); 			//	0354-9542	Acta Agriculturae Serbica
		special2010.put("0514-6658", "M52"); 			//	0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2010.put("0354-6160", "M52"); 			//	0354-6160	Biljni lekar
		special2010.put("1450-5029", "M52"); 			//	1450-5029	Časopis za procesnu tehniku i energetiku u poljoprivredi / PTEP (novi naslov: Journal on Processing and Energy in Agriculture)
		special2010.put("1821-4487", "M52"); 			//	1821-4487	Journal on Processing and Energy in Agriculture
		special2010.put("0354-5695", "M52"); 			//	0354-5695	Journal of Scientific Agricultural Research
		special2010.put("0554-5587", "M52"); 			//	0554-5587	Poljoprivredna tehnika
		special2010.put("0354-6438", "M52"); 			//	0354-6438	Poljoprivredne aktuelnosti
		special2010.put("1451-401X", "M52"); 			//	1451-401X	Prerada drveta
		special2010.put("0353-6564", "M52"); 			//	0353-6564	Prehrambena industrija – Mleko i mlečni proizvodi
		special2010.put("0494-9846", "M52"); 			//	0494-9846	Tehnologija mesa
		special2010.put("0563-9034", "M52"); 			//	0563-9034	Topola
		special2010.put("0350-1752", "M52"); 			//	0350-1752	Šumarstvo
		special2010.put("0372-7866", "M52"); 			//	0372-7866	Zaštita bilja
		special2010.put("0514-5899", "M52"); 			//	0514-5899	Zaštita prirode
		special2010.put("0351-9430", "M53"); 			//	0351-9430	Bilten za hmelj, sirak i lekovito bilje
		special2010.put("0018-6872", "M53"); 			//	0018-6872	Hrana i ishrana
		special2010.put("0351-9503", "M53"); 			//	0351-9503	Uljarstvo
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0567-8315", "M23"); 			//	0567-8315	Аcta veterinaria
		special2011.put("0534-0012", "M23"); 			//	0534-0012	Genetika
		special2011.put("1018-1806", "M24"); 			//	1018-1806	Helia
		special2011.put("0350-2457", "M24"); 			//	0350-2457	Veterinarski glasnik
		special2011.put("1820-3949", "M24"); 			//	1820-3949	Pesticidi i fitomedicina
		special2011.put("0354-4311", "M51"); 			//	0354-4311	Acta herbologica
		special2011.put("1450-7188", "M51"); 			//	1450-7188	Acta periodica technologica
		special2011.put("1450-9156", "M51"); 			//	1450-9156	Biotechnology in Animal Husbandry
		special2011.put("1820-5054", "M51"); 			//	1820-5054	Voćarstvo
		special2011.put("0353-4537", "M51"); 			//	0353-4537	Glasnik Šumarskog fakulteta
		special2011.put("1821-3944", "M51"); 			//	1821-3944	Ratarstvo i povrtarstvo
		special2011.put("0350-2953", "M51"); 			//	0350-2953	Savremena poljoprivredna tehnika
		special2011.put("0354-9542", "M52"); 			//	0354-9542	Acta Agriculturae Serbica
		special2011.put("0514-6658", "M52"); 			//	0514-6658	Acta biologica Iugoslavica - serija A: Zemljište i biljka
		special2011.put("2217-5369", "M52"); 			//	2217-5369	Food and Feed research
		special2011.put("1450-8109", "M52"); 			//	1450-8109	Journal of Agricultural Science
		special2011.put("0354-5695", "M52"); 			//	0354-5695	Journal of Scientific Agricultural Research
		special2011.put("1821-4487", "M52"); 			//	1821-4487	Journal on Processing and Energy in Agriculture
		special2011.put("0354-6160", "M52"); 			//	0354-6160	Biljni lekar
		special2011.put("0350-0519", "M52"); 			//	0350-0519	Vodoprivreda
		special2011.put("0372-7866", "M52"); 			//	0372-7866	Zaštita bilja
		special2011.put("0514-5899", "M52"); 			//	0514-5899	Zaštita prirode
		special2011.put("0352-4906", "M52"); 			//	0352-4906	Zbornik Matice srpske za prirodne nauke
		special2011.put("0546-8264", "M52"); 			//	0546-8264	Letopis radova Poljoprivrednog fakulteta
		special2011.put("0554-5587", "M52"); 			//	0554-5587	Poljoprivredna tehnika
		special2011.put("0354-6438", "M52"); 			//	0354-6438	Poljoprivredne aktuelnosti
		special2011.put("1451-401X", "M52"); 			//	1451-401X	Prerada drveta
		special2011.put("0353-6564", "M52"); 			//	0353-6564	Prehrambena industrija – Mleko i mlečni proizvodi
		special2011.put("0350-1205", "M52"); 			//	0350-1205	Savremena poljoprivreda
		special2011.put("0494-9846", "M52"); 			//	0494-9846	Tehnologija mesa
		special2011.put("0563-9034", "M52"); 			//	0563-9034	Topola
		special2011.put("0018-6872", "M52"); 			//	0018-6872	Hrana i ishrana
		special2011.put("0350-1752", "M52"); 			//	0350-1752	Šumarstvo
		special2011.put("1821-1046", "M53"); 			//	1821-1046	Sustainable Forestry: Collection
		special2011.put("1820-9955", "M53"); 			//	1820-9955	Arhiv veterinarske medicine
		special2011.put("0354-1320", "M53"); 			//	0354-1320	Zbornik naučnih radova Instituta PKB Agroekonomik
		special2011.put("0354-5881", "M53"); 			//	0354-5881	Selekcija i semenarstvo
		special2011.put("0351-9503", "M53"); 			//	0351-9503	Uljarstvo
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOBiotehnologijaIAgroindustrija MNOBiotehnologijaIAgroindustrija= null;
	
	public static MNOBiotehnologijaIAgroindustrija getMNOBiotehnologijaIAgroindustrija() {
		if(MNOBiotehnologijaIAgroindustrija==null)
			MNOBiotehnologijaIAgroindustrija = new MNOBiotehnologijaIAgroindustrija();
		return MNOBiotehnologijaIAgroindustrija;
	}
}
