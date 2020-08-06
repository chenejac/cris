package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOMatematikaIMehanika extends MNO{
	
	private MNOMatematikaIMehanika(){
		super();
		this.nameMNO = "Matematika i mehanika";
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
		special2009.put("0350-1302", "M24"); //(BISIS)2985	0350-1302	Publications de l'Institut Mathematique, Beograd
		special2009.put("1452-8630", "M51"); //(BISIS)2667	1452-8630	Applicable Analysis and Discrete Mathematics
		special2009.put("0561-7332", "M51"); //(BISIS)17162	0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2009.put("0352-9665", "M51"); //(BISIS)2765	0352-9665	Facta Universitatis: Series Mathematics and Informatics
		special2009.put("0354-5180", "M51"); //(BISIS)2771	0354-5180	Filomat
		special2009.put("1450-9628", "M51"); //(BISIS)2901	1450-9628	Kragujevac Journal of Mathematics
		special2009.put("0025-5165", "M51"); //(BISIS)2913	0025-5165	Matematički vesnik
		special2009.put("1450-5444", "M51"); //(BISIS)2853	1450-5444	Novi Sad Journal of Mathematics
		special2009.put("1450-5584", "M51"); //(BISIS)35956	1450-5584	Theoretical and Applied Mechanics
		special2009.put("0354-0243", "M51"); //(BISIS)3039	0354-0243	Yugoslav journal of operations research
		special2009.put("1450-5932", "M52"); //(BISIS)38860	1450-5932	Mathematica Moravica
		this.specialJournalsAllYears.put(2009, special2009);
		
		special2010 = new HashMap<String, String>();
		special2010.put("0340-6253", "M21"); //(BISIS)30479		0340-6253	Match: Communications in Mathematical and in Computer Chemistry
		special2010.put("0354-5180", "M24"); //(BISIS)2771	0354-5180	Filomat
		special2010.put("0350-1302", "M24"); //(BISIS)2985	0350-1302	Publications de l'Institut Mathematique, Beograd
		special2010.put("1452-8630", "M51"); //(BISIS)2667	1452-8630	Applicable Analysis and Discrete Mathematics
		special2010.put("0352-9665", "M51"); //(BISIS)2765	0352-9665	Facta Universitatis: Series Mathematics and Informatics
		special2010.put("1450-9628", "M51"); //(BISIS)2901	1450-9628	Kragujevac Journal of Mathematics
		special2010.put("0025-5165", "M51"); //(BISIS)2913	0025-5165	Matematički vesnik
		special2010.put("1450-5584", "M51"); //(BISIS)35956	1450-5584	Theoretical and Applied Mechanics
		special2010.put("0354-0243", "M51"); //(BISIS)3039	0354-0243	Yugoslav journal of operations research
		special2010.put("0561-7332", "M52"); //(BISIS)17162	0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2010.put("1450-5932", "M52"); //(BISIS)38860	1450-5932	Mathematica Moravica
		special2010.put("1450-5444", "M52"); //(BISIS)2853	1450-5444	Novi Sad Journal of Mathematics
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("0340-6253", "M21"); //(BISIS)30479		0340-6253	Match: Communications in Mathematical and in Computer Chemistry
		special2011.put("1452-8630", "M22"); //(BISIS)2667	1452-8630	Applicable Analysis and Discrete Mathematics
		special2011.put("0354-5180", "M23"); //(BISIS)2771	0354-5180	Filomat
		special2011.put("0350-1302", "M24"); //(BISIS)2985	0350-1302	Publications de l'Institut Mathematique, Beograd
		special2011.put("1450-9628", "M51"); //(BISIS)2901	1450-9628	Kragujevac Journal of Mathematics
		special2011.put("1450-5584", "M51"); //(BISIS)35956	1450-5584	Theoretical and Applied Mechanics
		special2011.put("0354-0243", "M51"); //(BISIS)3039	0354-0243	Yugoslav journal of operations research
		special2011.put("0025-5165", "M51"); //(BISIS)2913	0025-5165	Matematički vesnik
		special2011.put("0561-7332", "M52"); //(BISIS)17162	0561-7332	Bulletin, Classes des Sciences Mathematiques et Naturelles, Sciences
		special2011.put("1450-5932", "M52"); //(BISIS)38860	1450-5932	Mathematica Moravica
		special2011.put("1450-5444", "M52"); //(BISIS)2853	1450-5444	Novi Sad Journal of Mathematics
		special2011.put("0042-8469", "M52"); //(BISIS)8482	0042-8469	Vojnotehnički glasnik
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOMatematikaIMehanika MNOMatematikaIMehanika= null;
	
	public static MNOMatematikaIMehanika getMNOMatematikaIMehanika() {
		if(MNOMatematikaIMehanika==null)
			MNOMatematikaIMehanika = new MNOMatematikaIMehanika();
		return MNOMatematikaIMehanika;
	}
}
