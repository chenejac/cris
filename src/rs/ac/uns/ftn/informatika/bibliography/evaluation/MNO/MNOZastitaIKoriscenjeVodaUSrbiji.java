package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOZastitaIKoriscenjeVodaUSrbiji extends MNO{

	private MNOZastitaIKoriscenjeVodaUSrbiji() {
		super();
		this.nameMNO = "Zaštita i korišćenje voda u Srbiji";
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
		special2009.put("0350-5049", "M51"); //NEMA				0350-5049	Voda i sanitarna tehnika
		special2009.put("0350-0519", "M51"); //(BISIS)24459		0350-0519	Vodoprivreda
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0350-5049", "M52"); //NEMA				0350-5049	Voda i sanitarna tehnika
		special2010.put("0350-0519", "M52"); //(BISIS)24459		0350-0519	Vodoprivreda
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0350-5049", "M52"); //NEMA				0350-5049	Voda i sanitarna tehnika
		special2011.put("0350-0519", "M52"); //(BISIS)24459		0350-0519	Vodoprivreda
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOZastitaIKoriscenjeVodaUSrbiji MNOZastitaIKoriscenjeVodaUSrbiji= null;
	
	public static MNOZastitaIKoriscenjeVodaUSrbiji getMNOZastitaIKoriscenjeVodaUSrbiji() {
		if(MNOZastitaIKoriscenjeVodaUSrbiji==null)
			MNOZastitaIKoriscenjeVodaUSrbiji = new MNOZastitaIKoriscenjeVodaUSrbiji();
		return MNOZastitaIKoriscenjeVodaUSrbiji;
	}
}
