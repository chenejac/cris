package rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.*;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_ElektricnaMerenja;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_Elektronika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_HidropneumatskaTehnika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_MehanikaFluida;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_RacunarskaGrafika;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.TT.FTN.CommisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije;

public class CommissionFactory {

	
	
	private static CommissionFactory commissionFactory = null;
	
	private CommissionFactory(){};
	
	public static CommissionFactory getInstance(){
		if(commissionFactory == null){
			commissionFactory = new CommissionFactory();
		}
		return commissionFactory;
	}
	
	private Map<String, AbstractCommissionEvaluation> commissions = new HashMap<String, AbstractCommissionEvaluation>();
	
	public AbstractCommissionEvaluation getCommissionEvaluation(int commissionID){
//		System.out.println("komisija id "+commissionID);
//		System.out.println("Postojece komisije");
//		for (String key : commissions.keySet()) {
//			System.out.println("IDs " + commissions.get(key).comissionID);
//		}
		
		AbstractCommissionEvaluation commissionEvaluation = null;
		commissionEvaluation = commissions.get(commissionID+"");
		
		if (commissionEvaluation != null) {
//			System.out.println("Komisija vec postoji ");
			return commissionEvaluation;
		}
		
//		System.out.println("Komisija jos nije trebovana ");
		
		switch(commissionID){
	//PMF DEPARTMENTS OLD OLD
			case CommissionTypes.PmfDep_Mathematics_Informatics_OLD_OLD:
				commissionEvaluation = new CommisionOldOldPmfDepMathInf();
				break;
			case CommissionTypes.PmfDep_Chemistry_Biochemistry_EnvironmentalProtection_OLD_OLD:
				commissionEvaluation = new CommisionOldOldPmfDepHemBHemZZS();
				break;
			case CommissionTypes.PmfDep_Geography_Tourism_HotelManagement_OLD_OLD:
				commissionEvaluation = new CommisionOldOldPmfDepGeogTuriHotel();
				break;
			case CommissionTypes.PmfDep_Physics_OLD_OLD:
				commissionEvaluation = new CommisionOldOldPmfDepFiz();
				break;
			case CommissionTypes.PmfDep_Biology_Ecology_OLD_OLD:
				commissionEvaluation = new CommisionOldOldPmfDepBioEko();
				break;
	//PMF DEPARTMENTS OLD
			case CommissionTypes.PmfDep_Mathematics_Informatics_OLD:
				commissionEvaluation = new CommisionOldPmfDepMathInf();
				break;
			case CommissionTypes.PmfDep_Chemistry_Biochemistry_EnvironmentalProtection_OLD:
				commissionEvaluation = new CommisionOldPmfDepHemBHemZZS();
				break;
			case CommissionTypes.PmfDep_Geography_Tourism_HotelManagement_Natural_Sciencies_OLD:
				commissionEvaluation = new CommisionOldPmfDepGeogTuriHotelNaturalSciences();
				break;
			case CommissionTypes.PmfDep_Geography_Tourism_HotelManagement_Social_Sciencies_OLD:
				commissionEvaluation = new CommisionOldPmfDepGeogTuriHotelSocialSciences();
				break;
			case CommissionTypes.PmfDep_Physics_OLD:
				commissionEvaluation = new CommisionOldPmfDepFiz();
				break;
			case CommissionTypes.PmfDep_Biology_Ecology_OLD:
				commissionEvaluation = new CommisionOldPmfDepBioEko();
				break;
	//PMF DEPARTMENTS
			case CommissionTypes.Pmf:
				commissionEvaluation = new CommissionPmf();
				break;
			case CommissionTypes.PmfDep_Mathematics_Informatics:
				commissionEvaluation = new CommissionPmfDepMathInf();
				break;
			case CommissionTypes.PmfDep_Chemistry_Biochemistry_EnvironmentalProtection:
				commissionEvaluation = new CommissionPmfDepHemZZS();
				break;
			case CommissionTypes.PmfDep_Chemistry_Biochemistry_EnvironmentalProtection_Social_Sciences:
				commissionEvaluation = new CommissionPmfDepHemZZSSocialSciences();
				break;
			case CommissionTypes.PmfDep_Geography_Tourism_HotelManagement_Natural_Sciencies:
				commissionEvaluation = new CommissionPmfDepGeogTuriHotelNaturalSciences();
				break;
			case CommissionTypes.PmfDep_Geography_Tourism_HotelManagement_Social_Sciencies:
				commissionEvaluation = new CommissionPmfDepGeogTuriHotelSocialSciences();
				break;
			case CommissionTypes.PmfDep_Physics:
				commissionEvaluation = new CommissionPmfDepFiz();
				break;
			case CommissionTypes.PmfDep_Physics_Social_Sciences:
				commissionEvaluation = new CommissionPmfDepFizSocialSciences();
				break;
			case CommissionTypes.PmfDep_Biology_Ecology:
				commissionEvaluation = new CommissionPmfDepBioEko();
				break;
			case CommissionTypes.PmfDep_Biology_Ecology_Social_Sciences:
				commissionEvaluation = new CommissionPmfDepBioEkoSocialSciences();
				break;
	//Expert Council for Natural Science in Areas
			case CommissionTypes.EC_NatSci_Informatics:
				commissionEvaluation = new CommisionStrucnoVecePMInformatika();
				break;
			case CommissionTypes.EC_NatSci_Mathematics:
				commissionEvaluation = new CommisionStrucnoVecePMMatematika();
				break;
			case CommissionTypes.EC_NatSci_Chemistry:
				commissionEvaluation = new CommisionStrucnoVecePMHemija();
				break;
			case CommissionTypes.EC_NatSci_Biology:
				commissionEvaluation = new CommisionStrucnoVecePMBiologija();
				break;
			case CommissionTypes.EC_NatSci_Physics:
				commissionEvaluation = new CommisionStrucnoVecePMFizika();
				break;
			case CommissionTypes.EC_NatSci_Geography:
				commissionEvaluation = new CommisionStrucnoVecePMGeografija();
				break;
	//Expert Council for Technical Science in Areas
			case CommissionTypes.EC_TehSci_Architecture:
				commissionEvaluation = new CommisionStrucnoVeceTTArhitektura();
				break;				
			case CommissionTypes.EC_TehSci_BiotechnicalSciences:
				commissionEvaluation = new CommisionStrucnoVeceTTBiotehnickeNauke();
				break;
			case CommissionTypes.EC_TehSci_CivilEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTGradevinskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_GeodeticEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTGeodetskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_ElectricalAndComputerEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTTehnoloskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_IndustrialEngineeringAndEngineeringManagement:
				commissionEvaluation = new CommisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment();
				break;
			case CommissionTypes.EC_TehSci_EnvironmentalEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine();
				break;
			case CommissionTypes.EC_TehSci_MechanicalEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTMasinskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_OrganisationalScience:
				commissionEvaluation = new CommisionStrucnoVeceTTOrganizacioneNauke();
				break;
			case CommissionTypes.EC_TehSci_MiningEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTRudarskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_TrafficAndTransportEngineerin:
				commissionEvaluation = new CommisionStrucnoVeceTTSaobracajnoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_TehnologyEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTTehnoloskoInzenjerstvo();
				break;
			case CommissionTypes.EC_TehSci_MetallurgicalEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTTMetalurskoInzenjerstvo();
				break;
	//FTN Expert Council for Technical Science in Areas
			case CommissionTypes.EC_TehSci_FTN_AutomationAndControlSystems:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima();
				break;
			case CommissionTypes.EC_TehSci_FTN_AutomationAndControlSystems_BiomedicalEngineering:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering();
				break;			
			case CommissionTypes.EC_TehSci_FTN_AutomationAndControlSystems_Geoinformatics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika();
				break;	
			case CommissionTypes.EC_TehSci_FTN_TheoryAndInterpretationOfGeometricSpaceInArchitectureAndUrbanism:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu();
				break;
			case CommissionTypes.EC_TehSci_FTN_MechanicsOfDeformableBodies:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054();
				break;
			case CommissionTypes.EC_TehSci_FTN_Mechanics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054();
				break;
			case CommissionTypes.EC_TehSci_FTN_Biomechanics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054();
				break;
			case CommissionTypes.EC_TehSci_FTN_ComputerGraphics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_RacunarskaGrafika();
				break;
			case CommissionTypes.EC_TehSci_FTN_ElectricalMeasurements:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_ElektricnaMerenja();
				break;
			case CommissionTypes.EC_TehSci_FTN_LogisticsAndIntermodalTransport:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport();
				break;
			case CommissionTypes.EC_TehSci_FTN_TelecommunicationsAndSignalProcessing:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala();
				break;	
			case CommissionTypes.EC_TehSci_FTN_OrganizationsConstructionTechnologyAndManagement:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment();
				break;		
			case CommissionTypes.EC_TehSci_FTN_BuildingsAndArchitecturalConstructions:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije();
				break;	
			case CommissionTypes.EC_TehSci_FTN_PostalTransportationAndCommunications:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije();
				break;	
			case CommissionTypes.EC_TehSci_FTN_BuildingMaterialsAssessmentAndReparationOfStructures:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija();
				break;
			case CommissionTypes.EC_TehSci_FTN_ArtAppliedToArchitectureTechnologyAndDesign:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn();
				break;
			case CommissionTypes.EC_TehSci_FTN_Electronics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_Elektronika();
				break;
			case CommissionTypes.EC_TehSci_FTN_GeodesyAndGeoinformatics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika();
				break;
			case CommissionTypes.EC_TehSci_FTN_PlanningRegulationAndSafetyOfTrafficAndTransportation:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja();
				break;
			case CommissionTypes.EC_TehSci_FTN_QualityEfficiencyAndLogistic:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika();
				break;
			case CommissionTypes.EC_TehSci_FTN_FluidMechanics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_MehanikaFluida();
				break;
			case CommissionTypes.EC_TehSci_FTN_HydropneumaticTechnology:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_HidropneumatskaTehnika();
				break;
			case CommissionTypes.EC_TehSci_FTN_GasAndPetroleumTechnology:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika();
				break;
			case CommissionTypes.EC_TehSci_FTN_AppliedComputerScienceAndInformatics:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika();
				break;
			case CommissionTypes.EC_TehSci_FTN_GraphicTechnologyAndDesign:
				commissionEvaluation = new CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn();
				break;
	//Tehnoloski fakultet
			case CommissionTypes.OldTeh:
				commissionEvaluation = new CommissionOldTeh();
				break;
			case CommissionTypes.Teh:
				commissionEvaluation = new CommissionTeh();
				break;
			//WoS
			case CommissionTypes.OldWoSSimple:
				String appointmentBoard = "OldWoSSimple";
				Calendar appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				String members = "WoS journals' list selectors";
				String cfClassShemeIdScienceArea = "sciencesGroup";
				String cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				Set<ResearchAreaDTO> researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				List<MNO> mnoList = new ArrayList<MNO>();
				
				commissionEvaluation = new CommisionOldWoSSimple(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			case CommissionTypes.WoSSimple:
				appointmentBoard = "OldWoSSimple";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "WoS journals' list selectors";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				
				commissionEvaluation = new CommissionWoSSimple(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList, true, true);
				break;
			//MNOBiologija
			case CommissionTypes.MNOBiologija:
				appointmentBoard = "MNOBiologija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Биологија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOBiologija.getMNOBiologija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			//MNOBiotehnologijaIAgroindustrija
			case CommissionTypes.MNOBiotehnologijaIAgroindustrija:
				appointmentBoard = "MNOBiotehnologijaIAgroindustrija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Биологија и агроиндустрија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOBiotehnologijaIAgroindustrija.getMNOBiotehnologijaIAgroindustrija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			//MNOBiotehnologijaIPoljoprivreda
			case CommissionTypes.MNOBiotehnologijaIPoljoprivreda:
				appointmentBoard = "MNOBiotehnologijaIPoljoprivreda";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Биотехнологија и пољопривреда";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOBiotehnologijaIPoljoprivreda.getMNOBiotehnologijaIPoljoprivreda());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNODrustveneNauke
			case CommissionTypes.MNODrustveneNauke:
				appointmentBoard = "MNODrustveneNauke";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвене науке";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOElektronikaITelekomunikacije
			case CommissionTypes.MNOElektronikaITelekomunikacije:
				appointmentBoard = "MNOElektronikaITelekomunikacije";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Електроника и телекомуникације";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOElektronikaTelekomunikacijeIInformacioneTehnologije
			case CommissionTypes.MNOElektronikaTelekomunikacijeIInformacioneTehnologije:
				appointmentBoard = "MNOElektronikaTelekomunikacijeIInformacioneTehnologije";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Електроника, телекомуникације и информационе технологије";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOEnergetikaRudarstvoIEnergetskaEfikasnost
			case CommissionTypes.MNOEnergetikaRudarstvoIEnergetskaEfikasnost:
				appointmentBoard = "MNOEnergetikaRudarstvoIEnergetskaEfikasnost";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Енергетика, рударство и енергетска ефикасност";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOEnergetskaEfikasnost
			case CommissionTypes.MNOEnergetskaEfikasnost:
				appointmentBoard = "MNOEnergetskaEfikasnost";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Енергетска ефикасност";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOEnergetskaEfikasnost.getMNOEnergetskaEfikasnost());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOEnergetskeTehnologijeIRudarstvo
			case CommissionTypes.MNOEnergetskeTehnologijeIRudarstvo:
				appointmentBoard = "MNOEnergetskeTehnologijeIRudarstvo";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Енергетске технологије и рударство";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOFizika
			case CommissionTypes.MNOFizika:
				appointmentBoard = "MNOFizika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Физика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOFizika.getMNOFizika());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;	
			
			//MNOGeonaukeIAstronomija
			case CommissionTypes.MNOGeonaukeIAstronomija:
				appointmentBoard = "MNOGeonaukeIAstronomija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Геонауке и астрономија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOGeonaukeIAstronomija.getMNOGeonaukeIAstronomija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;	
			
			//MNOHemija
			case CommissionTypes.MNOHemija:
				appointmentBoard = "MNOHemija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Хемија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOHemija.getMNOHemija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;	
			
			//MNOIndustrijskiSoftverIInformatika
			case CommissionTypes.MNOIndustrijskiSoftverIInformatika:
				appointmentBoard = "MNOIndustrijskiSoftverIInformatika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Индустријски софтвер и информатика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOMasinstvo
			case CommissionTypes.MNOMasinstvo:
				appointmentBoard = "MNOMasinstvo";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Машинство";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMasinstvo.getMNOMasinstvo());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOMatematikaIMehanika
			case CommissionTypes.MNOMatematikaIMehanika:
				appointmentBoard = "MNOMatematikaIMehanika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Математика и механика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMatematikaIMehanika.getMNOMatematikaIMehanika());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOMatematikaRacunarskeNaukeIMehanika
			case CommissionTypes.MNOMatematikaRacunarskeNaukeIMehanika:
				appointmentBoard = "MNOMatematikaRacunarskeNaukeIMehanika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Математика, рачунарске науке и механика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMatematikaRacunarskeNaukeIMehanika.getMNOMatematikaRacunarskeNaukeIMehanika());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOMaterijaliIHemijskeTehnologije
			case CommissionTypes.MNOMaterijaliIHemijskeTehnologije:
				appointmentBoard = "MNOMaterijaliIHemijskeTehnologije";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Материјали и хемијске технологије";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
			
			//MNOMedicina
			case CommissionTypes.MNOMedicina:
				appointmentBoard = "MNOMedicina";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Медицина";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMedicina.getMNOMedicina());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOSaobracajUrbanizamIGradevinarstvo
			case CommissionTypes.MNOSaobracajUrbanizamIGradevinarstvo:
				appointmentBoard = "MNOSaobracajUrbanizamIGradevinarstvo";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Саобраћај, урбанизам и грађевинарство";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOSaobracajUrbanizamIGradevinarstvo.getMNOSaobracajUrbanizamIGradevinarstvo());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha
			case CommissionTypes.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha:
				appointmentBoard = "MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Уређење, заштита и коришћење вода, земљишта и ваздуха";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.getMNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha
			case CommissionTypes.MNOZastitaIKoriscenjeVodaUSrbiji:
				appointmentBoard = "MNOZastitaIKoriscenjeVodaUSrbiji";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - заштита и коришћење вода";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			//MNOSrpskiJezikIKnjizevnost
			case CommissionTypes.MNOSrpskiJezikIKnjizevnost:
				appointmentBoard = "MNOSrpskiJezikIKnjizevnost";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Српски језик и књижевност";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOSrpskiJezikIKnjizevnost.getMNOSrpskiJezikIKnjizevnost());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHN
			case CommissionTypes.MNODHN:
				appointmentBoard = "MNODHN";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHN.getMNODHN());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNEkonomijaIOrganizacioneNauke
			case CommissionTypes.MNODHNEkonomijaIOrganizacioneNauke:
				appointmentBoard = "MNODHNEkonomijaIOrganizacioneNauke";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Економија и организационе науке";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNEkonomijaIOrganizacioneNauke.getMNODHNEkonomijaIOrganizacioneNauke());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNFilozofijaITeologija
			case CommissionTypes.MNODHNFilozofijaITeologija:
				appointmentBoard = "MNODHNFilozofijaITeologija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Филозофија и теологија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNFilozofijaITeologija.getMNODHNFilozofijaITeologija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNInterdisciplinarneNauke
			case CommissionTypes.MNODHNInterdisciplinarneNauke:
				appointmentBoard = "MNODHNInterdisciplinarneNauke";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Интердисциплинарне науке";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNInterdisciplinarneNauke.getMNODHNInterdisciplinarneNauke());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNIstorijaArheologijaIEtnologija
			case CommissionTypes.MNODHNIstorijaArheologijaIEtnologija:
				appointmentBoard = "MNODHNIstorijaArheologijaIEtnologija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Историја, археологија и етнологија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNIstorijaArheologijaIEtnologija.getMNODHNIstorijaArheologijaIEtnologija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNNaukaOSportu
			case CommissionTypes.MNODHNNaukaOSportu:
				appointmentBoard = "MNODHNNaukaOSportu";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Наука о спорту";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNNaukaOSportu.getMNODHNNaukaOSportu());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNPravoIPolitikologija
			case CommissionTypes.MNODHNPravoIPolitikologija:
				appointmentBoard = "MNODHNPravoIPolitikologija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Право и политикологија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNPravoIPolitikologija.getMNODHNPravoIPolitikologija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje
			case CommissionTypes.MNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje:
				appointmentBoard = "MNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Психологија, педагогија, андрагогија и специјално васпитање";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje.getMNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNSociologijaIDemografija
			case CommissionTypes.MNODHNSociologijaIDemografija:
				appointmentBoard = "MNODHNSociologijaIDemografija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Социологија и демографија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNSociologijaIDemografija.getMNODHNSociologijaIDemografija());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNSrpskiJezikIKnjizevnost
			case CommissionTypes.MNODHNSrpskiJezikIKnjizevnost:
				appointmentBoard = "MNODHNSrpskiJezikIKnjizevnost";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Српски језик и књижевност";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNSrpskiJezikIKnjizevnost.getMNODHNSrpskiJezikIKnjizevnost());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost
			case CommissionTypes.MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost:
				appointmentBoard = "MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Друштвено хуманистичке науке - Инострани издавачи Српски језик и књижевност";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost.getMNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOGeodezija
			case CommissionTypes.MNOGeodezija:
				appointmentBoard = "MNOGeodezija";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Геодезија";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOGeodezija.getMNOGeodezija());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOGradjevinarstvo
			case CommissionTypes.MNOGradjevinarstvo:
				appointmentBoard = "MNOGradjevinarstvo";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Грађевинарство";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOGradjevinarstvo.getMNOGradjevinarstvo());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOMatematika
			case CommissionTypes.MNOMatematika:
				appointmentBoard = "MNOMatematika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Математика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMatematika.getMNOMatematika());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOMehanika
			case CommissionTypes.MNOMehanika:
				appointmentBoard = "MNOMehanika";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Механика";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOMehanika.getMNOMehanika());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOProstornoPlaniranje
			case CommissionTypes.MNOProstornoPlaniranje:
				appointmentBoard = "MNOProstornoPlaniranje";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Просторно Планирање";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOProstornoPlaniranje.getMNOProstornoPlaniranje());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNORacunarskeNauke
			case CommissionTypes.MNORacunarskeNauke:
				appointmentBoard = "MNORacunarskeNauke";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Рачунарске науке";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNORacunarskeNauke.getMNORacunarskeNauke());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOSaobracaj
			case CommissionTypes.MNOSaobracaj:
				appointmentBoard = "MNOSaobracaj";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Саобраћај";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOSaobracaj.getMNOSaobracaj());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;

			//MNOUrbanizam
			case CommissionTypes.MNOUrbanizam:
				appointmentBoard = "MNOUrbanizam";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови матичног научног одбора - Урбанизам";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";

				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();

				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(MNOUrbanizam.getMNOUrbanizam());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//AMS
			case CommissionTypes.AMS:
				appointmentBoard = "AMS";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "AMS journals' list selectors";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
				mnoList = new ArrayList<MNO>();
				mnoList.add(AMS.getAMS());
				
				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
				//MNOAll
			case CommissionTypes.MNOAll:
				appointmentBoard = "MNOAll";
				appointmentDate = new GregorianCalendar();
				appointmentDate.set(Calendar.DAY_OF_MONTH, 21);
				appointmentDate.set(Calendar.MONTH, Calendar.MARCH);
				appointmentDate.set(Calendar.YEAR, 2011);
				members = "Чланови свих матичног научних одбора";
				cfClassShemeIdScienceArea = "sciencesGroup";
				cfClassIdScienceArea = "allSciences";
				
				//setovanje naucnih oblasti
				researchAreas = new HashSet<ResearchAreaDTO>();
				
				//lista odbora
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
				
				mnoList.add(MNOBiologija.getMNOBiologija());
				mnoList.add(MNOBiotehnologijaIAgroindustrija.getMNOBiotehnologijaIAgroindustrija());
				mnoList.add(MNOBiotehnologijaIPoljoprivreda.getMNOBiotehnologijaIPoljoprivreda());
				mnoList.add(MNODrustveneNauke.getMNODrustveneNauke());
				mnoList.add(MNOElektronikaITelekomunikacije.getMNOElektronikaITelekomunikacije());
				mnoList.add(MNOElektronikaTelekomunikacijeIInformacioneTehnologije.getMNOElektronikaTelekomunikacijeIInformacioneTehnologije());
				mnoList.add(MNOEnergetikaRudarstvoIEnergetskaEfikasnost.getMNOEnergetikaRudarstvoIEnergetskaEfikasnost());
				mnoList.add(MNOEnergetskaEfikasnost.getMNOEnergetskaEfikasnost());
				mnoList.add(MNOEnergetskeTehnologijeIRudarstvo.getMNOEnergetskeTehnologijeIRudarstvo());
				mnoList.add(MNOFizika.getMNOFizika());
				mnoList.add(MNOGeonaukeIAstronomija.getMNOGeonaukeIAstronomija());
				mnoList.add(MNOHemija.getMNOHemija());
				mnoList.add(MNOIndustrijskiSoftverIInformatika.getMNOIndustrijskiSoftverIInformatika());
				mnoList.add(MNOMasinstvo.getMNOMasinstvo());
				mnoList.add(MNOMatematikaIMehanika.getMNOMatematikaIMehanika());
				mnoList.add(MNOMatematikaRacunarskeNaukeIMehanika.getMNOMatematikaRacunarskeNaukeIMehanika());
				mnoList.add(MNOMaterijaliIHemijskeTehnologije.getMNOMaterijaliIHemijskeTehnologije());
				mnoList.add(MNOMedicina.getMNOMedicina());
				mnoList.add(MNOSaobracajUrbanizamIGradevinarstvo.getMNOSaobracajUrbanizamIGradevinarstvo());
				mnoList.add(MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha.MNOUredenjeZastitaIKoriscenjeVodaZemljistaIVazduha);
				mnoList.add(MNOZastitaIKoriscenjeVodaUSrbiji.getMNOZastitaIKoriscenjeVodaUSrbiji());	
				mnoList.add(MNOSrpskiJezikIKnjizevnost.getMNOSrpskiJezikIKnjizevnost());
//				mnoList.add(MNODHN.getMNODHN());
				mnoList.add(MNODHNEkonomijaIOrganizacioneNauke.getMNODHNEkonomijaIOrganizacioneNauke());
				mnoList.add(MNODHNFilozofijaITeologija.getMNODHNFilozofijaITeologija());
				mnoList.add(MNODHNInterdisciplinarneNauke.getMNODHNInterdisciplinarneNauke());
				mnoList.add(MNODHNIstorijaArheologijaIEtnologija.getMNODHNIstorijaArheologijaIEtnologija());
				mnoList.add(MNODHNNaukaOSportu.getMNODHNNaukaOSportu());
				mnoList.add(MNODHNPravoIPolitikologija.getMNODHNPravoIPolitikologija());
				mnoList.add(MNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje.getMNODHNPsihologijaPedagogijaAndragogijaISpecijalnoVaspitanje());
				mnoList.add(MNODHNSociologijaIDemografija.getMNODHNSociologijaIDemografija());
				mnoList.add(MNODHNSrpskiJezikIKnjizevnost.getMNODHNSrpskiJezikIKnjizevnost());
				mnoList.add(MNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost.getMNODHNInostraniIzdavaciSrpskiJezikIKnjizevnost());
				mnoList.add(AMS.getAMS());

				mnoList.add(MNOGeodezija.getMNOGeodezija());
				mnoList.add(MNOGradjevinarstvo.getMNOGradjevinarstvo());
				mnoList.add(MNOMatematika.getMNOMatematika());
				mnoList.add(MNOMehanika.getMNOMehanika());
				mnoList.add(MNOProstornoPlaniranje.getMNOProstornoPlaniranje());
				mnoList.add(MNORacunarskeNauke.getMNORacunarskeNauke());
				mnoList.add(MNOSaobracaj.getMNOSaobracaj());
				mnoList.add(MNOUrbanizam.getMNOUrbanizam());

				commissionEvaluation = new CommisionMNO(commissionID, appointmentBoard, appointmentDate, members, cfClassShemeIdScienceArea, cfClassIdScienceArea, null, researchAreas, mnoList);
				break;
				
			default:
				System.out.println("Nepostoji komisija pod tim rednim brojem " +commissionID);
				break;
		}
		
		commissions.put(commissionID+"", commissionEvaluation);
		
		return commissionEvaluation;
	}
	
	public AbstractCommissionEvaluation getCommissionEvaluation(String appointmentBoard){
		AbstractCommissionEvaluation commissionEvaluation = null;
		//TO BE IMPLEMENTED
		
		return commissionEvaluation;
	}
}
