package rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO;

import java.util.HashMap;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class MNOIndustrijskiSoftverIInformatika extends MNO{
	
	private MNOIndustrijskiSoftverIInformatika(){
		super();
		this.nameMNO = "Industrijski softver i informatika";
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
		special2009.put("1820-0214", "M23"); //(BISIS)2731	1820-0214	Computer Science and Information Systems / ComSIS
		special2009.put("0354-0243", "M24"); //(BISIS)2667	0354-0243	Yugoslav Journal of Operations Research
		special2009.put("1820-6417", "M51"); //(BISIS)32875	1820-6417	Facta Universitatis: Series Automatic Control and Robotics
		special2009.put("0353-3670", "M51"); //(BISIS)31127	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2009.put("0352-9665", "M52"); //(BISIS)2765	0352-9665	Facta Universitatis: Series Mathematics and Informatics
		special2009.put("0182-0109", "M52"); //(BISIS)31903	0182-0109	Pregled nacionalnog centra za digitalizaciju
		special2009.put("0350-0667", "M52"); //(BISIS)36755	0350-0667	Scientific-Technical Review	
		special2009.put("1451-4397", "M53"); //(BISIS)2807	1451-4397	Info M
		special2009.put("1820-6530", "M53"); //(BISIS)33116	1820-6530	Journal of Serbian Society for Computational Mechanics
		this.specialJournalsAllYears.put(2009, special2009);

		
		special2010 = new HashMap<String, String>();
		special2010.put("1820-0214", "M23"); //(BISIS)2731	1820-0214	Computer Science and Information Systems / ComSIS
		special2010.put("0354-0243", "M24"); //(BISIS)2667	0354-0243	Yugoslav Journal of Operations Research
		special2010.put("0352-9665", "M51"); //(BISIS)2765	0352-9665	Facta Universitatis: Series Mathematics and Informatics
		special2010.put("1820-6417", "M52"); //(BISIS)32875	1820-6417	Facta Universitatis: Series Automatic Control and Robotics
		special2010.put("0353-3670", "M52"); //(BISIS)31127	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2010.put("0350-0667", "M52"); //(BISIS)36755	0350-0667	Scientific-Technical Review	
		special2010.put("1451-4397", "M53"); //(BISIS)2807	1451-4397	Info M
		special2010.put("1450-9687", "M53"); //(BISIS)33898	1450-9687	Infoteka
		special2010.put("1820-6530", "M53"); //(BISIS)33116	1820-6530	Journal of Serbian Society for Computational Mechanics
		special2010.put("0182-0109", "M53"); //(BISIS)31903	0182-0109	Pregled nacionalnog centra za digitalizaciju
		special2010.put("0042-8469", "M53"); //(BISIS)8482	0042-8469	Vojnotehnički glasnik
		this.specialJournalsAllYears.put(2010, special2010);
		
		special2011 = new HashMap<String, String>();
		special2011.put("1820-0214", "M23"); //(BISIS)2731	1820-0214	Computer Science and Information Systems / ComSIS
		special2011.put("0353-3670", "M52"); //(BISIS)31127	0353-3670	Facta Universitatis: Series Electronics and Energetics
		special2011.put("1820-6530", "M52"); //(BISIS)33116	1820-6530	Journal of Serbian Society for Computational Mechanics
		special2011.put("0350-0667", "M52"); //(BISIS)36755	0350-0667	Scientific-Technical Review
		special2011.put("1451-4397", "M53"); //(BISIS)2807	1451-4397	Info M
		special2011.put("0354-0243", "M53"); //(BISIS)2667	0354-0243	Yugoslav Journal of Operations Research
		special2011.put("0042-8469", "M53"); //(BISIS)8482	0042-8469	Vojnotehnički glasnik
		special2011.put("1450-9687", "M53"); //(BISIS)33898	1450-9687	Infoteka
		special2011.put("0182-0109", "M53"); //(BISIS)31903	0182-0109	Pregled nacionalnog centra za digitalizaciju
		this.specialJournalsAllYears.put(2011, special2011);
	}
	
	public static MNOIndustrijskiSoftverIInformatika MNOInformatika= null;
	
	public static MNOIndustrijskiSoftverIInformatika getMNOIndustrijskiSoftverIInformatika() {	
		if(MNOInformatika==null)
			MNOInformatika = new MNOIndustrijskiSoftverIInformatika();
		return MNOInformatika;
	}
}
