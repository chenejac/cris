package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOSrpskiJezikIKnjizevnost extends MNO{

	private MNOSrpskiJezikIKnjizevnost() {
		super();
		this.nameMNO = "Srpski jezik i književnost";
		this.yearsSpecial = new int [5];
		yearsSpecial[0] = 2006;
		yearsSpecial[1] = 2008;
		yearsSpecial[2] = 2009;
		yearsSpecial[3] = 2010;
		yearsSpecial[4] = 2011;
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
		
		//spceijalni casopisi u godinama
		HashMap <String, String> special2006;
		HashMap <String, String> special2008;
		HashMap <String, String> special2009;
		HashMap <String, String> special2010;
		HashMap <String, String> special2011;
		
		//kreiranje liste casopisa u odredjenim godinama za maticne odbore 
		special2006 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSrpskiJezikIKnjizevnost2006.xlsx", special2006);
		this.specialJournalsAllYears.put(2006, special2006);
		
		special2008 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSrpskiJezikIKnjizevnost2008.xlsx", special2008);
		this.specialJournalsAllYears.put(2008, special2008);
		
		special2009 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSrpskiJezikIKnjizevnost2009.xlsx", special2009);
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSrpskiJezikIKnjizevnost2010.xlsx", special2010);
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		importFromXLS(FileStorage.storageRoot + "/import/mnoSrpskiJezikIKnjizevnost2011.xlsx", special2011);
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOSrpskiJezikIKnjizevnost MNOSrpskiJezikIKnjizevnost= null;
	
	public static MNOSrpskiJezikIKnjizevnost getMNOSrpskiJezikIKnjizevnost() {
		if(MNOSrpskiJezikIKnjizevnost==null)
			MNOSrpskiJezikIKnjizevnost = new MNOSrpskiJezikIKnjizevnost();
		return MNOSrpskiJezikIKnjizevnost;
	}
	
}
