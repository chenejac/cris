/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import rs.ac.uns.ftn.informatika.bibliography.dao.RecordDAO;
import rs.ac.uns.ftn.informatika.bibliography.db.EvaluationDB;
import rs.ac.uns.ftn.informatika.bibliography.db.MetricsDB;
import rs.ac.uns.ftn.informatika.bibliography.db.RecordDB;
import rs.ac.uns.ftn.informatika.bibliography.db.ResearchAreaDB;
import rs.ac.uns.ftn.informatika.bibliography.dto.JournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.PaperJournalDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.Types;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.MNO.MNO;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.AbstractCommissionEvaluation;
import rs.ac.uns.ftn.informatika.bibliography.evaluation.commisions.JournalEval;
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
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.AllDocCollector;
import rs.ac.uns.ftn.informatika.bibliography.textsrv.Retriever;
import rs.ac.uns.ftn.informatika.bibliography.utils.GenericComparator;


/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class EvaluationOfJournalsInDatabase {

	public static Connection connCris;
	public static Connection connKobson;
	
	public static RecordDB recordDB = new RecordDB();
	public static RecordDAO recordDAO = new RecordDAO(new RecordDB());
	public static ResearchAreaDB researchAreaDB = new ResearchAreaDB();
	public static MetricsDB metricsDB = new MetricsDB();
	public static EvaluationDB evaluationDB = new EvaluationDB();
	
	public static Log log = LogFactory.getLog(ExportJournalNameISSNDuplicates.class.getName());
	
	public static String importFolder;
	public static String exportFolder;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ResourceBundle rbEval = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.evaluation");
		int firstEvaluationYear = Integer.parseInt(rbEval.getString("firstEvaluationYear"));
		int lastEvaluationyear = Integer.parseInt(rbEval.getString("lastEvaluationYear"));
		
		
		Connection connCris = null;
		
		String luceneIndexPath = "";
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.evaluation.connection");
		String hostname = rb.getString("hostname");
		String port = rb.getString("port");
		String schema = rb.getString("schema");
		String connectionParameters = rb.getString("connectionParameters");
		String username = rb.getString("username");
		String password = rb.getString("password");
		
		importFolder = rb.getString("importFolder");
		exportFolder = rb.getString("exportFolder");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connCris = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + schema + connectionParameters, username, password);
			connCris.setAutoCommit(false);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connKobson = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port 
				+ "/" + "kobson" + connectionParameters, username, password);
			connKobson.setAutoCommit(false);
		} catch (Exception e) 
		{		
			e.printStackTrace();
		}
		
		luceneIndexPath = rb.getString("luceneIndex");
		Retriever.setIndexPath(luceneIndexPath);
		
		System.out.println("Poceo");
		
		/******                   POC KOMISIJA 				*******/
		HashMap<Integer, AbstractCommissionEvaluation> mapaKomisija = new HashMap<Integer, AbstractCommissionEvaluation>();
		
		/******                   SAMOVEREDNOVENJE DEPARTMANI 				*******/
		
//		CommisionOldOldPmfDepMathInf commisionPmfDepMathInf = new CommisionOldOldPmfDepMathInf();
//		mapaKomisija.put(commisionPmfDepMathInf.getComissionID(), commisionPmfDepMathInf);
//		
//		CommisionOldOldPmfDepHemBHemZZS commisionPmfDepHemBHemZZS = new CommisionOldOldPmfDepHemBHemZZS();
//		mapaKomisija.put(commisionPmfDepHemBHemZZS.getComissionID(), commisionPmfDepHemBHemZZS);
//		
//		CommisionOldOldPmfDepGeogTuriHotel commisionPmfDepGeogTuriHotel = new CommisionOldOldPmfDepGeogTuriHotel();
//		mapaKomisija.put(commisionPmfDepGeogTuriHotel.getComissionID(), commisionPmfDepGeogTuriHotel);
//		
//		CommisionOldOldPmfDepFiz commisionPmfDepFiz = new CommisionOldOldPmfDepFiz();
//		mapaKomisija.put(commisionPmfDepFiz.getComissionID(), commisionPmfDepFiz);
//		
//		CommisionOldOldPmfDepBioEko commisionPmfDepBioEko = new CommisionOldOldPmfDepBioEko();
//		mapaKomisija.put(commisionPmfDepBioEko.getComissionID(), commisionPmfDepBioEko);
		
		/******                   SAMOVEREDNOVENJE DEPARTMANI NEW				*******/
//		CommisionOldPmfDepMathInf commisionPmfDepMathInfNovo = new CommisionOldPmfDepMathInf();
//		mapaKomisija.put(commisionPmfDepMathInfNovo.getComissionID(), commisionPmfDepMathInfNovo);
//		
//		CommisionOldPmfDepHemBHemZZS commisionPmfDepHemBHemZZSNovo = new CommisionOldPmfDepHemBHemZZS();
//		mapaKomisija.put(commisionPmfDepHemBHemZZSNovo.getComissionID(), commisionPmfDepHemBHemZZSNovo);
//		
//		CommisionOldPmfDepGeogTuriHotelNaturalSciences commisionPmfDepGeogTuriHotelNovoNaturalSciences = new CommisionOldPmfDepGeogTuriHotelNaturalSciences();
//		mapaKomisija.put(commisionPmfDepGeogTuriHotelNovoNaturalSciences.getComissionID(), commisionPmfDepGeogTuriHotelNovoNaturalSciences);
//		
//		CommisionOldPmfDepGeogTuriHotelSocialSciences commisionPmfDepGeogTuriHotelNovoSocialSciences = new CommisionOldPmfDepGeogTuriHotelSocialSciences();
//		mapaKomisija.put(commisionPmfDepGeogTuriHotelNovoSocialSciences.getComissionID(), commisionPmfDepGeogTuriHotelNovoSocialSciences);
//		
//		CommisionOldPmfDepFiz commisionPmfDepFizNovo = new CommisionOldPmfDepFiz();
//		mapaKomisija.put(commisionPmfDepFizNovo.getComissionID(), commisionPmfDepFizNovo);
//		
//		CommisionOldPmfDepBioEko commisionPmfDepBioEkoNovo = new CommisionOldPmfDepBioEko();
//		mapaKomisija.put(commisionPmfDepBioEkoNovo.getComissionID(), commisionPmfDepBioEkoNovo);
		
		/******                   STRUCNA VECA PRIRODNO MATEMATICKIH NAUKA		*******/
		
//		CommisionStrucnoVecePMInformatika commisionEvaluationInformatika = new CommisionStrucnoVecePMInformatika();
//		mapaKomisija.put(commisionEvaluationInformatika.getComissionID(), commisionEvaluationInformatika);
//		
//		CommisionStrucnoVecePMMatematika commisionStrucnoVeceMatematika = new CommisionStrucnoVecePMMatematika();
//		mapaKomisija.put(commisionStrucnoVeceMatematika.getComissionID(), commisionStrucnoVeceMatematika);
//		
//		CommisionStrucnoVecePMHemija commisionStrucnoVeceHemija = new CommisionStrucnoVecePMHemija();
//		mapaKomisija.put(commisionStrucnoVeceHemija.getComissionID(), commisionStrucnoVeceHemija);
//		
//		CommisionStrucnoVecePMBiologija commisionStrucnoVeceBiologija = new CommisionStrucnoVecePMBiologija();
//		mapaKomisija.put(commisionStrucnoVeceBiologija.getComissionID(), commisionStrucnoVeceBiologija);
//		
//		CommisionStrucnoVecePMFizika commisionStrucnoVeceFizika = new CommisionStrucnoVecePMFizika();
//		mapaKomisija.put(commisionStrucnoVeceFizika.getComissionID(), commisionStrucnoVeceFizika);
//		
//		CommisionStrucnoVecePMGeografija commisionStrucnoVeceGeografija = new CommisionStrucnoVecePMGeografija();
//		mapaKomisija.put(commisionStrucnoVeceGeografija.getComissionID(), commisionStrucnoVeceGeografija);
		
		/******                   STRUCNA VECA TEHNICKO TEHNOLOSKIH NAUKA		*******/	 
//		CommisionStrucnoVeceTTArhitektura commisionStrucnoVeceTTArhitektura = new CommisionStrucnoVeceTTArhitektura();
//		mapaKomisija.put(commisionStrucnoVeceTTArhitektura.getComissionID(), commisionStrucnoVeceTTArhitektura);
//		
//		CommisionStrucnoVeceTTBiotehnickeNauke commisionStrucnoVeceTTBiotehnickeNauke = new CommisionStrucnoVeceTTBiotehnickeNauke();
//		mapaKomisija.put(commisionStrucnoVeceTTBiotehnickeNauke.getComissionID(), commisionStrucnoVeceTTBiotehnickeNauke);
//		
//		CommisionStrucnoVeceTTGradevinskoInzenjerstvo commisionStrucnoVeceTTGradevinskoInzenjerstvo = new CommisionStrucnoVeceTTGradevinskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTGradevinskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTGradevinskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTGeodetskoInzenjerstvo commisionStrucnoVeceTTGeodetskoInzenjerstvo = new CommisionStrucnoVeceTTGeodetskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTGeodetskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTGeodetskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo commisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo = new CommisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTElektrotehnickoIRacunarskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment commisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment = new CommisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment();
//		mapaKomisija.put(commisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment.getComissionID(), commisionStrucnoVeceTTIndustrijskoInzenjerstvoIInzenjerskiMenadzment);
//		
//		CommisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine commisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine = new CommisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine();
//		mapaKomisija.put(commisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine.getComissionID(), commisionStrucnoVeceTTInzenjerstvoZastiteZivotneSredine);
//		
//		CommisionStrucnoVeceTTMasinskoInzenjerstvo commisionStrucnoVeceTTMasinskoInzenjerstvo = new CommisionStrucnoVeceTTMasinskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTMasinskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTMasinskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTOrganizacioneNauke commisionStrucnoVeceTTOrganizacioneNauke = new CommisionStrucnoVeceTTOrganizacioneNauke();
//		mapaKomisija.put(commisionStrucnoVeceTTOrganizacioneNauke.getComissionID(), commisionStrucnoVeceTTOrganizacioneNauke);
//		
//		CommisionStrucnoVeceTTRudarskoInzenjerstvo commisionStrucnoVeceTTRudarskoInzenjerstvo = new CommisionStrucnoVeceTTRudarskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTRudarskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTRudarskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTSaobracajnoInzenjerstvo commisionSaobracajnoInzenjerstvo= new CommisionStrucnoVeceTTSaobracajnoInzenjerstvo();
//		mapaKomisija.put(commisionSaobracajnoInzenjerstvo.getComissionID(), commisionSaobracajnoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTTehnoloskoInzenjerstvo commisionStrucnoVeceTTTehnoloskoInzenjerstvo= new CommisionStrucnoVeceTTTehnoloskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTTehnoloskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTTehnoloskoInzenjerstvo);
//		
//		CommisionStrucnoVeceTTMetalurskoInzenjerstvo commisionStrucnoVeceTTMetalurskoInzenjerstvo= new CommisionStrucnoVeceTTMetalurskoInzenjerstvo();
//		mapaKomisija.put(commisionStrucnoVeceTTMetalurskoInzenjerstvo.getComissionID(), commisionStrucnoVeceTTMetalurskoInzenjerstvo);
		
		/******                   STRUCNA VECA TEHNICKO TEHNOLOSKIH NAUKA	FTN	*******/	
		
//		CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima.getComissionID(), commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemima);
//		
//		CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering.getComissionID(), commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaBiomedicinskiInzenjering);
//
//		CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika = new CommisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika.getComissionID(), commisionStrucnoVeceTT_FTN_AutomatikaIUpravljanjeSistemimaGeoinformatika);
//
//		CommisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu commisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu = new CommisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu.getComissionID(), commisionStrucnoVeceTT_FTN_TeorijeIInterpretacijeGeometrijskogProstoraUArhitekturiIUrbanizmu);
//
//		CommisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054 commisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054 = new CommisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054.getComissionID(), commisionStrucnoVeceTT_FTN_MehanikaDeformabilnogTelaKatedraZaMehaniku054);
//	
//		CommisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054 commisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054 = new CommisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054.getComissionID(), commisionStrucnoVeceTT_FTN_MehanikaKatedraZaMehaniku054);
//		
//		CommisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054 commisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054 = new CommisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054.getComissionID(), commisionStrucnoVeceTT_FTN_BiomehanikaKatedraZaMehaniku054);
//		
//		CommisionStrucnoVeceTT_FTN_RacunarskaGrafika commisionStrucnoVeceTT_FTN_RacunarskaGrafika = new CommisionStrucnoVeceTT_FTN_RacunarskaGrafika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_RacunarskaGrafika.getComissionID(), commisionStrucnoVeceTT_FTN_RacunarskaGrafika);
//
//		CommisionStrucnoVeceTT_FTN_ElektricnaMerenja commisionStrucnoVeceTT_FTN_ElektricnaMerenja = new CommisionStrucnoVeceTT_FTN_ElektricnaMerenja();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_ElektricnaMerenja.getComissionID(), commisionStrucnoVeceTT_FTN_ElektricnaMerenja);
//		
//		CommisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport commisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport = new CommisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport.getComissionID(), commisionStrucnoVeceTT_FTN_LogistikaIIntermodalniTransport);
//		
//		CommisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala commisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala = new CommisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala.getComissionID(), commisionStrucnoVeceTT_FTN_TelekomunikacijeIObradaSignala);
//
//		CommisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment commisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment = new CommisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment.getComissionID(), commisionStrucnoVeceTT_FTN_OrganizacijaTehnologijaGradjenjaIMenadzment);
//		
//		CommisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije commisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije = new CommisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije.getComissionID(), commisionStrucnoVeceTT_FTN_ZgradarstvoGradjevinskeIArhitektonskeKonstrukcije);
//
//		CommisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije commisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije = new CommisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije.getComissionID(), commisionStrucnoVeceTT_FTN_PostanskiSaobracajIKomunikacije);
//
//		CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija commisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija = new CommisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija.getComissionID(), commisionStrucnoVeceTT_FTN_GradjevinskiMaterijaliProcenaStanjaISanacijaKonstrukcija);
//		
//		CommisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn commisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn = new CommisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn.getComissionID(), commisionStrucnoVeceTT_FTN_UmetnostPrimenjenaNaArhitekturuTehnikuIDizajn);
//		
//		CommisionStrucnoVeceTT_FTN_Elektronika commisionStrucnoVeceTT_FTN_Elektronika = new CommisionStrucnoVeceTT_FTN_Elektronika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_Elektronika.getComissionID(), commisionStrucnoVeceTT_FTN_Elektronika);
//		
//		CommisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika commisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika = new CommisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika.getComissionID(), commisionStrucnoVeceTT_FTN_GeodezijaIGeoinformatika);
//
//		CommisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja commisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja = new CommisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja.getComissionID(), commisionStrucnoVeceTT_FTN_PlaniranjeRegulisanjeIBezbednostSaobracaja);
//
//		CommisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika commisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika = new CommisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika.getComissionID(), commisionStrucnoVeceTT_FTN_KvalitetEfektivnostILogistika);
//
//		CommisionStrucnoVeceTT_FTN_MehanikaFluida commisionStrucnoVeceTT_FTN_MehanikaFluida = new CommisionStrucnoVeceTT_FTN_MehanikaFluida();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_MehanikaFluida.getComissionID(), commisionStrucnoVeceTT_FTN_MehanikaFluida);
//		
//		CommisionStrucnoVeceTT_FTN_HidropneumatskaTehnika commisionStrucnoVeceTT_FTN_HidropneumatskaTehnika = new CommisionStrucnoVeceTT_FTN_HidropneumatskaTehnika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_HidropneumatskaTehnika.getComissionID(), commisionStrucnoVeceTT_FTN_HidropneumatskaTehnika);
//		
//		CommisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika commisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika = new CommisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika.getComissionID(), commisionStrucnoVeceTT_FTN_GasnaINaftnaTehnika);
//
//		CommisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika commisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika = new CommisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika.getComissionID(), commisionStrucnoVeceTT_FTN_PrimenjeneRacunarskeNaukeIInformatika);
//		
//		CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn commisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn = new CommisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn();
//		mapaKomisija.put(commisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn.getComissionID(), commisionStrucnoVeceTT_FTN_GrafickoInzenjerstvoIDizajn);
		
		/******                   KRAJ KOMISIJA 				*******/
		
		EvaluationOfJournalsInDatabase evaluationOfJournalsInDatabase = new EvaluationOfJournalsInDatabase();
		evaluationOfJournalsInDatabase.connCris = connCris;
		
//		if(!evaluationOfJournalsInDatabase.prepareDatabase(mapaKomisija)){
//			System.out.println("Zavrsio sa greskom brisanje baze i pripreme komisija");
//			System.exit(0);
//		}
		
		List<JournalEval> lista =  evaluationOfJournalsInDatabase.collectJournals(firstEvaluationYear, lastEvaluationyear);
		
//		System.out.println("Broj casopisa " + lista.size());
//		int brojacCasEl = 0;
//		for (JournalEval journalEval : lista) {
//			brojacCasEl++;
//			System.out.println(brojacCasEl + ". Casopis "+ journalEval.getSomeName() + " id "+ journalEval.getControlNumber() + " ISSN "+ journalEval.getISSN() +  " Pocetna godina je: "+ journalEval.getStartingYear());
//			for (int commisionID : mapaKomisija.keySet()) {
//				AbstractCommissionEvaluation commision = mapaKomisija.get(commisionID);
//				System.out.print("Komisija " + commision.getAppointmentBoard() +", ");
//				HashMap<Integer, String> evaluated = commision.getJournalEvaluations(journalEval);
//				
//				System.out.println("Komisija " + commision.getAppointmentBoard() + " Pocetna godina je: "+ journalEval.getStartingYear());
//				for (int i = journalEval.getStartingYear(); i<=lastEvaluationyear; i++) {
//					System.out.println("Za godinu " + i + " evaluacija je " + evaluated.get(i) );
//				}
//				
//				if(!evaluationOfJournalsInDatabase.storeEvaluation(journalEval, evaluated, commision, firstEvaluationYear, lastEvaluationyear))
//				{
//					System.out.println("Zavrsio sa greskom unos vrednosvanja");
//					System.exit(0);
//				}
//				
//			}
//			System.out.println(" ");
//			
//			try {
//				connCris.commit();
//			} catch (SQLException ex) {
//				log.fatal(ex);
//				log.fatal("Cannot add relations between mARC21Record: " + journalEval.getControlNumber() + " and classes");
//				System.out.println("Punkao commit za bazu");
//				System.exit(0);
//			}
//		}
		
		System.out.println("Zavrsio");
	}
	
	
	public List<JournalEval> collectJournals(int firstEvaluationYear, int lastEvaluationYear){
		List<JournalEval> retVal = new ArrayList<JournalEval>();
		Query allJournalsQuery = new TermQuery(new Term("TYPE", Types.JOURNAL));
		List<Record> listJournals = recordDAO.getDTOs(allJournalsQuery, new AllDocCollector(false));
		String journalConterolNumber = null;
		
		for (Record recordDTO : listJournals) {
			try {
				JournalDTO journalDTO = (JournalDTO) recordDTO.getDto();
				if (journalDTO != null /* && journalDTO.getControlNumber().equals("(BISIS)24457") */) {
					int startingYear = firstEvaluationYear;
					journalConterolNumber = journalDTO.getControlNumber();
					
					if(journalDTO.getIssn()==null || journalDTO.getIssn().equalsIgnoreCase("")){
						System.out.println("Casopis \""+ journalDTO.getSomeName() + "\" kontrolni broj je CN " + journalDTO.getControlNumber() + " nema ISSN");
					}
					
					if(journalDTO.getIssn()==null || journalDTO.getIssn().equalsIgnoreCase("")){
						System.out.println("Casopis \""+ journalDTO.getSomeName() + "\" kontrolni broj je CN " + journalDTO.getControlNumber() + " nema ISSN");
					}
					
//					if(!isNoviCasopis(journalConterolNumber)){
//						continue;
//					}
					
					
					
					BooleanQuery allJournalPapersQuery = new BooleanQuery();
					BooleanQuery type = new BooleanQuery();
					type.add(new TermQuery(new Term("TYPE", Types.PAPER_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.SCIENTIFIC_CRITICISM_JOURNAL)), Occur.SHOULD);
					type.add(new TermQuery(new Term("TYPE", Types.OTHER_JOURNAL)), Occur.SHOULD);
					allJournalPapersQuery.add(type, Occur.MUST);
					allJournalPapersQuery.add(new TermQuery(new Term("JOCN", journalDTO.getControlNumber())), Occur.MUST);
					List<Record> listJournalPapers = recordDAO.getDTOs(allJournalPapersQuery, new AllDocCollector(false));		
					
					if (listJournalPapers!=null)
						for (Record recDTO : listJournalPapers) {
							PaperJournalDTO paperJournalDTO = (PaperJournalDTO) recDTO.getDto();
							if (paperJournalDTO != null) {
								if(paperJournalDTO.getPublicationYear()!=null){
									
									String temp = paperJournalDTO.getPublicationYear();
									if(temp.contains("/")){
										temp = paperJournalDTO.getPublicationYear().substring(0, paperJournalDTO.getPublicationYear().indexOf("/"));
									}
									if (isInteger(temp) == false) {
										System.out.println("Rad \""+ paperJournalDTO.getSomeTitle() + "\" NEMA BROJ KAO GODINU IZADNJA "+ paperJournalDTO.getPublicationYear() +" kontrolni broj je CN " + paperJournalDTO.getControlNumber() + " od autora " + paperJournalDTO.getAllAuthors().get(0).getStringRepresentation());
										System.out.println(paperJournalDTO.getStringRepresentation());
//										System.exit(0);
										continue;
									}
									
									Integer publicationYear = null;
									if(paperJournalDTO.getPublicationYear().contains("/")){
										publicationYear = Integer.parseInt(paperJournalDTO.getPublicationYear().substring(0, paperJournalDTO.getPublicationYear().indexOf("/")));
									} else 
										publicationYear = Integer.parseInt(paperJournalDTO.getPublicationYear());
									
									if( (startingYear>publicationYear || startingYear==-1 ) && publicationYear >= firstEvaluationYear && publicationYear <=lastEvaluationYear)
										startingYear = publicationYear;
									
									if(publicationYear < firstEvaluationYear)
										System.out.println("Rad \""+ paperJournalDTO.getSomeTitle() + "\" nema validnu godinu izdanja ("+ publicationYear +"<" +firstEvaluationYear+ ") "+ paperJournalDTO.getPublicationYear() + " kontrolni broj je CN " + paperJournalDTO.getControlNumber() + " od autora " + paperJournalDTO.getAllAuthors().get(0).getStringRepresentation());
								
								}
								else{
									System.out.println(paperJournalDTO.getStringRepresentation());
								}
							}
						}
					
					
					MetricsDB metricsDB = new MetricsDB();
					List<ImpactFactor> impactFactors = metricsDB.getJournalImpactFactors(connCris, journalDTO.getControlNumber(), Arrays.asList(new String[]{"twoYearsIF", "fiveYearsIF"}));
					if(impactFactors!=null)
						Collections.sort(impactFactors, new GenericComparator<ImpactFactor>(
								"year", "asc"));
					
//					for (ImpactFactor impactFactor : impactFactors) {
//						System.out.println("godina=" + impactFactor.getYear() + " vrednost=" + impactFactor.getValueOfImpactFactor());
//						for(ResearchAreaRanking  researchAreaRanking : impactFactor.getResearchAreas())
//							System.out.println("naziv oblasti=" + researchAreaRanking.getResearchAreaDTO() + " pozicija=" + researchAreaRanking.getPosition());
//					}
					
					/*
					if(impactFactors!=null)
						if(impactFactors.size()>0)
							if((startingYear>impactFactors.get(0).getYear()) || startingYear==-1)
								startingYear = impactFactors.get(0).getYear();
					 */
					
					startingYear = firstEvaluationYear;
					
					if (startingYear != -1) {
						retVal.add(new JournalEval(journalDTO.getControlNumber(), journalDTO.getSomeName(), journalDTO.getIssn(), impactFactors, startingYear));
					}
				}
			} catch (Exception e) {
				log.error(e);
				System.out.println("PUKO ko zvecka collectJournals " + journalConterolNumber);
				return null;
			}
		}
		Collections.sort(retVal, new GenericComparator<JournalEval>(
				"startingYear", "asc"));
		return retVal;
	}
	
	public boolean prepareDatabase(HashMap<Integer, AbstractCommissionEvaluation> mapaKomisija){
		try {
			connCris.setAutoCommit(true);
//			Calendar startDate = new GregorianCalendar();
//			startDate.set(Calendar.DAY_OF_YEAR, 1);
//			Calendar endDate = new GregorianCalendar();
//			endDate.set(Calendar.MONTH, Calendar.DECEMBER);
//			endDate.set(Calendar.DAY_OF_MONTH, 31);
//			
//			startDate.set(Calendar.YEAR, 2012);
//			endDate.set(Calendar.YEAR, 2012);

			for (int commisionID : mapaKomisija.keySet()) {
				AbstractCommissionEvaluation commision = mapaKomisija.get(commisionID);
				//brisanje svih validacija casopisa za komisiju
				PreparedStatement prepStmt = connCris.prepareStatement("DELETE FROM MARC21RECORD_CLASS where commissionid = ? and ( cfclassid like 'topLeadingInternationalJournal' or cfclassid like 'leadingInternationalJournal' or cfclassid like 'outstandingInternationalJournal' or cfclassid like 'internationalJournal' or cfclassid like 'speciallyVerifiedInternationalJournal' or cfclassid like 'leadingNationalJournal' or cfclassid like 'nationalJournal' or cfclassid like 'scienceJournal' )");
				prepStmt.setInt(1, commision.getComissionID());
				prepStmt.executeUpdate();
				prepStmt.close();
				
				System.out.println("Brisao evaluacije za komisisju id=" + commision.getComissionID() + " naziv=" + commision.getAppointmentBoard());
				
				//provera da li komisija postoji
				Statement stmt = connCris.createStatement();
				String query = "SELECT * FROM COMMISSION C where COMMISSIONID = "+ commision.getComissionID();
				ResultSet rset = stmt.executeQuery(query);

				if (rset.next()) {
					rset.close();
					stmt.close();
					continue;
				} else {
					rset.close();
					stmt.close();
				}
				
				//kreiraj komisiju ako ne postoji
				prepStmt = connCris.prepareStatement("insert into COMMISSION (COMMISSIONID, APPOINTMENTBOARD, APPOINTMENTDATE, MEMBERS, CFCLASSSCHEMEIDSCIENCEAREA, CFCLASSIDSCIENCEAREA, SCIENTIFICFIELD) values (?, ?, ?, ?, ?, ?, )");
				
				prepStmt.setInt(1, commision.getComissionID());
				prepStmt.setString(2, commision.getAppointmentBoard());
				prepStmt.setDate(3, new java.sql.Date(commision.getAppointmentDate().getTime()
							.getTime()));
				prepStmt.setString(4, commision.getMembers());
				prepStmt.setString(5, commision.getCfClassShemeIdScienceArea());
				prepStmt.setString(6, commision.getCfClassIdScienceArea());
				prepStmt.setString(7, null);
				
				prepStmt.executeUpdate();
				stmt.close();
			}
			
			connCris.setAutoCommit(false);
			return true;
		} catch (SQLException ex) {
			log.fatal(ex);
			log.fatal("Cannot delete relations between mARC21Record and classes");
		}
		
		return false;
		
	}
	
	
	public boolean storeEvaluation(JournalEval journalEval, HashMap<Integer, String> results, AbstractCommissionEvaluation commision, int firstEvaluationYear, int lastEvaluationyear){
			
		if (results == null)
			return false;
		if (results.isEmpty())
			return true;

		Calendar startDate = null;
		Calendar endDate = null;
		
		String startKategorija = null;
		String endKategorija = null;
		
		for (int godina = journalEval.getStartingYear(); godina<=lastEvaluationyear; godina++) {		
			
			if(godina == journalEval.getStartingYear()){
				startKategorija = results.get(godina);
				startDate = new GregorianCalendar();
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				startDate.set(Calendar.YEAR, godina);
				continue;
			}
			
			endKategorija = results.get(godina);

			if(!startKategorija.equals(endKategorija)){
				endDate = new GregorianCalendar();
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.YEAR, godina-1);			
				String kategorija = determinateJournal(startKategorija);
//				System.out.println("DIFFERENT CAT " + "start godina "+ startDate.getTime() + " karaj godina "+ endDate.getTime() + " kategorija " + kategorija + " komisija "+ commision.getComissionID());
				if(!evaluationDB.addResultClassificationByCommission(connCris, journalEval.getControlNumber(), "type", kategorija, startDate, endDate, commision.getComissionID())){
					System.out.println("Greska pri ubacivanju vrednovanja: " + journalEval.getControlNumber());
					return false;
				}
				startDate = new GregorianCalendar();
				startDate.set(Calendar.DAY_OF_YEAR, 1);
				startDate.set(Calendar.YEAR, godina);
				startKategorija = endKategorija;
			}
			
			if (godina == lastEvaluationyear) {
				endDate = new GregorianCalendar();
				endDate.set(Calendar.DAY_OF_MONTH, 31);
				endDate.set(Calendar.MONTH, Calendar.DECEMBER);
				endDate.set(Calendar.YEAR, godina);
				String kategorija = determinateJournal(startKategorija);
//				System.out.println("LAST YEAR " + "start godina "+ startDate.getTime() + " karaj godina "+ endDate.getTime() + " kategorija " + kategorija + " komisija "+ commision.getComissionID());
				if(!evaluationDB.addResultClassificationByCommission(connCris, journalEval.getControlNumber(), "type", kategorija, startDate, endDate, commision.getComissionID())){
					System.out.println("Greska pri ubacivanju vrednovanja: " + journalEval.getControlNumber());
					return false;
				}	
			}
		}
		
		return true;
	}
	
	public String determinateJournal (String m){
		
		String retVal = null;
		if(m.equalsIgnoreCase("M21a"))
			retVal = "topLeadingInternationalJournal";
		else if(m.equalsIgnoreCase("M21"))
			retVal = "leadingInternationalJournal";
		else if (m.equalsIgnoreCase("M22"))
			retVal = "outstandingInternationalJournal";
		else if (m.equalsIgnoreCase("M23"))
			retVal = "internationalJournal";
		else if (m.equalsIgnoreCase("M24"))
			retVal = "speciallyVerifiedInternationalJournal";
		else if (m.equalsIgnoreCase("M51"))
			retVal = "leadingNationalJournal";
		else if (m.equalsIgnoreCase("M52"))
			retVal = "nationalJournal";
		else if (m.equalsIgnoreCase("M53"))
			retVal = "scienceJournal";
		else {
			System.out.println("Sinak tvoja kategorija ne postoji " + m);
		}
		
		return retVal;
	}
	
	public boolean isInteger(String s){
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean isNoviCasopis(String bysisID){
		
		String [] noviID = {""};
		
		for (int i = 0; i < noviID.length; i++) {
			if (bysisID.equals(noviID[i])) {
				return true;
			}
		}
		
		return false;
	}

}
