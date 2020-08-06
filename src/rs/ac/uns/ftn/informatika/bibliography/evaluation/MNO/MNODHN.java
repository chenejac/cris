package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

import rs.ac.uns.ftn.informatika.bibliography.filesrv.FileStorage;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNODHN extends MNO{

	private MNODHN() {
		super();
		this.nameMNO = "Drustveno humanisticke nauke";
		this.yearsSpecial = new int [0];
		this.specialJournalsAllYears = new HashMap<Integer,HashMap<String,String>>();
	}
	
	public static MNODHN MNODHN= null;
	
	public static MNODHN getMNODHN() {
		if(MNODHN==null)
			MNODHN = new MNODHN();
		return MNODHN;
	}
	
}
