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
		this.yearsSpecial = new int [1];
		yearsSpecial[0] = 2021;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2021;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2021 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoMehanika2021.xlsx", special2021);
		this.specialJournalsAllYears.put(2021, special2021);
	}

	public static MNOMehanika MNOMehanika= null;
	
	public static MNOMehanika getMNOMehanika() {
		if(MNOMehanika==null)
			MNOMehanika = new MNOMehanika();
		return MNOMehanika;
	}
}
