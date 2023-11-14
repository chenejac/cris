package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

import java.util.HashMap;

/**
 * @author Dragan Ivanovic, dragan.ivanovic@uns.ac.rs
 *
 */
public class MNOUrbanizam extends MNO{

	private MNOUrbanizam() {
		super();
		this.nameMNO = "Urbanizam";
		this.yearsSpecial = new int [2];
		yearsSpecial[0] = 2021;
		yearsSpecial[1] = 2022;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2021;
		HashMap <String, String> special2022;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoUrbanizam2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);

		special2022 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mno_2022_za_urbanizam.xlsx", special2022);
		this.specialJournalsAllYears.put(2022, special2022);
	}

	public static MNOUrbanizam MNOUrbanizam= null;
	
	public static MNOUrbanizam getMNOUrbanizam() {
		if(MNOUrbanizam==null)
			MNOUrbanizam = new MNOUrbanizam();
		return MNOUrbanizam;
	}
}
