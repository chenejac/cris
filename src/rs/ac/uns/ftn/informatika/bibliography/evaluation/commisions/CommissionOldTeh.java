package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOBiologija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOBiotehnologijaIAgroindustrija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOBiotehnologijaIPoljoprivreda;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOElektronikaITelekomunikacije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOElektronikaTelekomunikacijeIInformacioneTehnologije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetikaRudarstvoIEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskaEfikasnost;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOEnergetskeTehnologijeIRudarstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOFizika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOHemija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOIndustrijskiSoftverIInformatika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMasinstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMatematikaIMehanika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMatematikaRacunarskeNaukeIMehanika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMaterijaliIHemijskeTehnologije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOMedicina;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOSaobracajUrbanizamIGradevinarstvo;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNOZastitaIKoriscenjeVodaUSrbiji;

public class CommissionOldTeh extends CommisionStrucnoVece {

	public CommissionOldTeh(){
		super();
		comissionID = 500;
		appointmentBoard = "TEH";
		appointmentDate = new GregorianCalendar();
		appointmentDate.set(Calendar.DAY_OF_YEAR, 30);
		appointmentDate.set(Calendar.MONTH, Calendar.JUNE);
		appointmentDate.set(Calendar.YEAR, 2014);
		members = " ";
		cfClassShemeIdScienceArea = "sciencesGroup";
		cfClassIdScienceArea = "technicalSciences";
		
		//"Fizika", "Hemija", "Matematika i mehanika", "Matematika", "Racunarske nauke i mehanika", "Medicina", "Biologija"
		//"Elektronika i telekomunikacije", "Elektronika, telekomunikacije i informacione tehnologije"
		//"Industrijski softver i informatika", "Mašinstvo","Saobraćaj, urbanizam i građevinarstvo", "Energetske tehnologije i rudarstvo"
		//"Energetska efikasnost", "Energetske tehnologije i rudarstvo", "Materijali i hemijske tehnologije"
		//"Biotehnologija i agroindustrija","Biotehnologija i poljoprivreda", "Zaštita i korišćenje voda u Srbiji", "Uređenje, zaštitu i korišćenje voda, zemljišta i vazduha"
		mnoList = new ArrayList<MNO>();
		mnoList.add(MNOFizika.getMNOFizika());
		mnoList.add(MNOHemija.getMNOHemija());
		mnoList.add(MNOMatematikaIMehanika.getMNOMatematikaIMehanika());
		mnoList.add(MNOMatematikaRacunarskeNaukeIMehanika.getMNOMatematikaRacunarskeNaukeIMehanika());
		mnoList.add(MNOMedicina.getMNOMedicina());
		mnoList.add(MNOBiologija.getMNOBiologija());
		mnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
		mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
		mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
		mnoList.add(MNOMasinstvo.getMNOMasinstvo());
		mnoList.add(MNOSaobracajUrbanizamIGradevinarstvo.getMNOSaobracajUrbanizamIGradevinarstvo());
		mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
		mnoList.add(MNOEnergetskaEfikasnost.getMNOEnergetskaEfikasnost());
		mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
		mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
		mnoList.add(MNOBiotehnologijaIAgroindustrija.getMNOBiotehnologijaIAgroindustrija());
		mnoList.add(MNOBiotehnologijaIPoljoprivreda.getMNOBiotehnologijaIPoljoprivreda());
		mnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());
		mnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha());
		
		//setovanje naucnih oblasti
		researchAreas = new HashSet<ResearchAreaDTO>();
		
		ResearchAreaDTO wos = null;
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos13");				//Agricultural Engineering
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos11");				//Agricultural Experiment Station Reports (current - Agricultural…)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos42");				//Biochemical Research Methods
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos43");				//Biochemistry & Molecular Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos26");				//Biodiversity Conservation
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos44");				//Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos45");				//Biology, Miscellaneous (current - relevant discipline)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos34");				//Biomethods (current - Biochemical Research Methods)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos54");				//Biophysics
		researchAreas.add(wos);

		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos55");				//Biotechnology & Applied Microbiology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos24");				//Ceramics Chemistry
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos36");				//Chemistry (current - Chemistry, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos68");				//Chemistry, Analytical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos64");				//Chemistry, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos37");				//Chemistry, Clinical & Medicinal (current - Chemistry, Medicinal)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos69");				//Chemistry, Inorganic & Nuclear
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos65");				//Chemistry, Medicinal
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos22");				//Chemistry, Miscellaneous (current - Chemistry, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos66");				//Chemistry, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos70");				//Chemistry, Organic
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos71");				//Chemistry, Physical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos78");				//Computer Science, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos84");				//Crystallography
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos39");				//Dentistry & Odontology (current - Dentistry, Oral Surgery & Medicine)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos86");				//Dentistry, Oral Surgery & Medicine
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos5");					//Drugs & Addiction (current - Substance Abuse)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos90");				//Ecology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos98");				//Electrochemistry
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos102");				//Energy & Fuels
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos40");				//Engineering (current - Engineering, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos16");				//Engineering, Aerospace
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos104");				//Engineering, Biomedical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos106");				//Engineering, Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos105");				//Engineering, Environmental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos115");				//Engineering, Geological
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos103");				//Engineering, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos112");				//Engineering, Petroleum
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos117");				//Environmental Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos123");				//Food Science & Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos47");				//Materials Science (current - Materials Science, …)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos174");				//Materials Science, Biomaterials
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos162");				//Materials Science, Ceramics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos175");				//Materials Science, Characterization & Testing
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos176");				//Materials Science, Coatings & Films
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos177");				//Materials Science, Composites
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos163");				//Materials Science, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos161");				//Materials Science, Paper & Wood
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos178");				//Materials Science, Textiles
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos135");				//Mathematical & Computational Biology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos166");				//Mathematics
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos164");				//Mathematics, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos165");				//Mathematics, Interdisciplinary Applications
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos147");				//Nanoscience & Nanotechnology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos8");					//Medicine, Miscellaneous (current - relevant discipline)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos173");				//Medicine, Research & Experimental
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos179");				//Meteorology & Atmospheric Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos180");				//Microbiology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos181");				//Microscopy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos183");				//Mineralogy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos184");				//Multidisciplinary Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos185");				//Mycology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos147");				//Nanoscience & Nanotechnology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos189");				//Nuclear Science & Technology
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos204");				//Pharmacology & Pharmacy
		researchAreas.add(wos);
		
		wos.setSchemeId("researchArea");
		wos.setClassId("wos23");				//Physics (current - Physics, ...)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos206");				//Physics, Applied
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos209");				//Physics, Atomic, Molecular & Chemical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos211");				//Physics, Condensed Matter
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos208");				//Physics, Fluids & Plasmas
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos21");				//Physics, General (current - Physics, ...)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos216");				//Physics, Mathematical
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos50");				//Physics, Miscellaneous (current - Physics, ...)
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos210");				//Physics, Multidisciplinary
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos213");				//Physics, Nuclear
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos214");				//Physics, Particles & Fields
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos56");				//Plant Sciences
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos218");				//Polymer Science
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos238");				//Spectroscopy
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos240");				//Statistics & Probability
		researchAreas.add(wos);
		
		wos = new ResearchAreaDTO();
		wos.setSchemeId("researchArea");
		wos.setClassId("wos63");				//Thermodynamics
		researchAreas.add(wos);
		
	}
}
