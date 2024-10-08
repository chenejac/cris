package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

import java.util.HashMap;

/**
 * @author Dragan Ivanovic, dragan.ivanovic@uns.ac.rs
 *
 */
public class MNOMehanika extends MNO{

	private MNOMehanika() {
		super();
		this.nameMNO = "Mehanika";
		this.yearsSpecial = new int [3];
		yearsSpecial[0] = 2021;
		yearsSpecial[1] = 2022;
		yearsSpecial[2] = 2023;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2021;
		HashMap <String, String> special2022;
		HashMap <String, String> special2023;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMehanika2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_za_mehaniku.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);

		special2023 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2023_za_mehaniku.xlsx", special2023);
		this.specialJournalsAllYears.put(2023, special2023);
	}

	public static MNOMehanika MNOMehanika= null;
	
	public static MNOMehanika getMNOMehanika() {
		if(MNOMehanika==null)
			MNOMehanika = new MNOMehanika();
		return MNOMehanika;
	}
}
