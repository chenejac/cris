package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

import java.util.HashMap;

/**
 * @author Dragan Ivanovic, dragan.ivanovic@uns.ac.rs
 *
 */
public class MNOGeodezija extends MNO{

	private MNOGeodezija() {
		super();
		this.nameMNO = "Geodezija";
		this.yearsSpecial = new int [1];
		yearsSpecial[0] = 2021;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2021;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoGeodezija2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);
	}

	public static MNOGeodezija MNOGeodezija= null;
	
	public static MNOGeodezija getMNOGeodezija() {
		if(MNOGeodezija==null)
			MNOGeodezija = new MNOGeodezija();
		return MNOGeodezija;
	}
}
