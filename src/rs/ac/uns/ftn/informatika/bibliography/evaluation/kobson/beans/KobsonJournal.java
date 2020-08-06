package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class KobsonJournal {

	//database identifikator
	protected int id =0;
	
	//Kobson identifikator
	protected String jid="";
	
	//Podaci o casopisu
	protected String ISSN ="";
	protected String naslov ="";
	protected String status ="";
	protected String tipDokumenta ="";
	protected String ucestalost ="";
	protected String jezik ="";
	protected String prviBroj ="";
	protected String abstrakt ="";
	protected String alternativniNaslovi ="";
	
	protected String skraceniNaslov ="";
	
	//Impakt faktor, kategorije
	protected List<String> kategorije = new ArrayList<String>();
	protected String UCurrentContents="";
	protected String scienceCitation="";
	
	//impakt faktor na 2 godine
	protected List<KobsonImpactFactorJournal> impaktFatorCasopisa = new ArrayList<KobsonImpactFactorJournal>();
	
	//impakt faktor na 5 godina
	protected List<KobsonImpactFactorJournal> impaktFatorCasopisaPetGodina = new ArrayList<KobsonImpactFactorJournal>();
	
	//Izdavac
	protected String sponzor="";
	protected String izdavac="";
	protected String adresa="";
	protected String telefon="";
	protected String fax="";
	protected String eMail="";
	protected String URL="";
	protected String zemlja="";

	//PODACI ZA prvlacenje sa kobsona
	protected String CRIS_ID="";
	protected String VERSIONCHANGE="";
	protected String DEPENDENCY="";
	
	public void clear()
	{
		id=0;
		
		//Kobson identifikator
		jid="";
		
		//Podaci o casopisu
		ISSN ="";
		naslov ="";
		status ="";
		tipDokumenta ="";
		ucestalost ="";
		jezik ="";
		prviBroj ="";
		abstrakt ="";
		alternativniNaslovi ="";
		
		skraceniNaslov ="";
		
		//Impakt faktor, kategorije
		kategorije = new ArrayList<String>();
		UCurrentContents="";
		scienceCitation="";
		
		for (KobsonImpactFactorJournal imf : impaktFatorCasopisa) {
			imf.clear();
		}
		impaktFatorCasopisa.clear();
		
		for (KobsonImpactFactorJournal imf : impaktFatorCasopisaPetGodina) {
			imf.clear();
		}
		impaktFatorCasopisaPetGodina.clear();
		
		//Izdavac
		sponzor="";
		izdavac="";
		adresa="";
		telefon="";
		fax="";
		eMail="";
		URL="";
		zemlja="";
		
		//PODACI ZA prvlacenje sa kobsona
		CRIS_ID="";
		VERSIONCHANGE="";
		DEPENDENCY="";
	}
	
	public void print()
	{
		System.out.println("*********Kobson identifikator********");
		System.out.println("jid="+jid);
		
		System.out.println("*********Podaci o casopisu********");
		System.out.println("ISSN: "+ ISSN + "; naslov: "+ naslov + "; status: "+ status + "; tipDokumenta: "+ tipDokumenta 
				+ "; ucestalost: "+ ucestalost + "; jezik: "+ jezik + "; prviBroj: "+ prviBroj
				+ "; abstrakt: "+ abstrakt + "; alternativniNaslovi: "+ alternativniNaslovi + "; skraceniNaslov: "+ skraceniNaslov);
		
		System.out.println("*********Impakt faktor, kategorije********");
		System.out.println("UCurrentContents: "+ UCurrentContents + "; scienceCitation: "+ scienceCitation);
		for(String kategorija : kategorije)
		{
			System.out.println("kategorija: "+ kategorija);
		}
		System.out.println("*********Impakt faktor, kategorije za 2 godine********");
		for(int i = 0; i< impaktFatorCasopisa.size(); i++)
		{
			impaktFatorCasopisa.get(i).print();
		}
		System.out.println("*********Impakt faktor, kategorije za 5 godina********");
		for(int i = 0; i< impaktFatorCasopisaPetGodina.size(); i++)
		{
			impaktFatorCasopisaPetGodina.get(i).print();
		}
		
		System.out.println("*********Izdavac********");
		System.out.println("sponzor: "+ sponzor + "; izdavac: "+ izdavac + "; adresa: "+ adresa + "; telefon: "+ telefon 
				+ "; fax: "+ fax + "; eMail: "+ eMail + "; URL: "+ URL + "; zemlja: "+ zemlja);
	}
	
	public String toXML()
	{
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<journal jid=\""+jid+ "\" ISSN=\""+ ISSN + "\">\n");
			sb.append("\t<podaciocasopisu>\n");
				sb.append("\t\t<naslov>" + naslov + "</naslov>\n");
				sb.append("\t\t<status>" + status + "</status>\n");
				sb.append("\t\t<tipDokumenta>" + tipDokumenta + "</tipDokumenta>\n");
				sb.append("\t\t<ucestalost>" + ucestalost + "</ucestalost>\n");
				sb.append("\t\t<jezik>" + jezik + "</jezik>\n");
				sb.append("\t\t<prviBroj>" + prviBroj + "</prviBroj>\n");
				sb.append("\t\t<abstrakt>" + abstrakt + "</abstrakt>\n");
				sb.append("\t\t<alternativniNaslovi>" + alternativniNaslovi + "</alternativniNaslovi>\n");
				sb.append("\t\t<skraceniNaslov>" + skraceniNaslov + "</skraceniNaslov>\n");
			sb.append("\t</podaciocasopisu>\n");
			sb.append("\t<impaktfaktorkategorije>\n");
				sb.append("\t\t<UCurrentContents>" + UCurrentContents + "</UCurrentContents>\n");
				sb.append("\t\t<scienceCitation>" + scienceCitation + "</scienceCitation>\n");
				for(int i = 0; i< kategorije.size(); i++)
				{
					sb.append("\t\t<aktivnaKategorija>" + kategorije.get(i) + "</aktivnaKategorija>\n");
				}
				sb.append("\t\t<impaktfaktorkategorijePlanDvogodisnji>\n");
				for(int i = 0; i< impaktFatorCasopisa.size(); i++)
				{
					sb.append(impaktFatorCasopisa.get(i).toXML("\t\t\t"));
				}
				sb.append("\t\t</impaktfaktorkategorijePlanDvogodisnji>\n");
				
				sb.append("\t\t<impaktfaktorkategorijePlanPetogodisnji>\n");
				for(int i = 0; i< impaktFatorCasopisaPetGodina.size(); i++)
				{
					sb.append(impaktFatorCasopisaPetGodina.get(i).toXML("\t\t\t"));
				}
				sb.append("\t\t</impaktfaktorkategorijePlanPetogodisnji>\n");				

			sb.append("\t</impaktfaktorkategorije>\n");

			sb.append("\t<izdavacCasopisa>\n");
				sb.append("\t\t<sponzor>" + sponzor + "</sponzor>\n");
				sb.append("\t\t<izdavac>" + izdavac + "</izdavac>\n");
				sb.append("\t\t<adresa>" + adresa + "</adresa>\n");
				sb.append("\t\t<telefon>" + telefon + "</telefon>\n");
				sb.append("\t\t<fax>" + fax + "</fax>\n");
				sb.append("\t\t<eMail>" + eMail + "</eMail>\n");
				sb.append("\t\t<URL>" + URL + "</URL>\n");
				sb.append("\t\t<zemlja>" + zemlja + "</zemlja>\n");
			sb.append("\t</izdavacCasopisa>\n");
			
		sb.append("</journal>\n");
		
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the jid
	 */
	public String getJid() {
		return jid;
	}

	/**
	 * @param jid the jid to set
	 */
	public void setJid(String jid) {
		this.jid = jid;
	}

	/**
	 * @return the iSSN
	 */
	public String getISSN() {
		return ISSN;
	}

	/**
	 * @param iSSN the iSSN to set
	 */
	public void setISSN(String iSSN) {
		ISSN = iSSN;
	}

	/**
	 * @return the naslov
	 */
	public String getNaslov() {
		return naslov;
	}

	/**
	 * @param naslov the naslov to set
	 */
	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the tipDokumenta
	 */
	public String getTipDokumenta() {
		return tipDokumenta;
	}

	/**
	 * @param tipDokumenta the tipDokumenta to set
	 */
	public void setTipDokumenta(String tipDokumenta) {
		this.tipDokumenta = tipDokumenta;
	}

	/**
	 * @return the ucestalost
	 */
	public String getUcestalost() {
		return ucestalost;
	}

	/**
	 * @param ucestalost the ucestalost to set
	 */
	public void setUcestalost(String ucestalost) {
		this.ucestalost = ucestalost;
	}

	/**
	 * @return the jezik
	 */
	public String getJezik() {
		return jezik;
	}

	/**
	 * @param jezik the jezik to set
	 */
	public void setJezik(String jezik) {
		this.jezik = jezik;
	}

	/**
	 * @return the prviBroj
	 */
	public String getPrviBroj() {
		return prviBroj;
	}

	/**
	 * @param prviBroj the prviBroj to set
	 */
	public void setPrviBroj(String prviBroj) {
		this.prviBroj = prviBroj;
	}

	/**
	 * @return the abstrakt
	 */
	public String getAbstrakt() {
		return abstrakt;
	}

	/**
	 * @param abstrakt the abstrakt to set
	 */
	public void setAbstrakt(String abstrakt) {
		this.abstrakt = abstrakt;
	}

	/**
	 * @return the alternativniNaslovi
	 */
	public String getAlternativniNaslovi() {
		return alternativniNaslovi;
	}

	/**
	 * @param alternativniNaslovi the alternativniNaslovi to set
	 */
	public void setAlternativniNaslovi(String alternativniNaslovi) {
		this.alternativniNaslovi = alternativniNaslovi;
	}

	/**
	 * @return the skraceniNaslov
	 */
	public String getSkraceniNaslov() {
		return skraceniNaslov;
	}

	/**
	 * @param skraceniNaslov the skraceniNaslov to set
	 */
	public void setSkraceniNaslov(String skraceniNaslov) {
		this.skraceniNaslov = skraceniNaslov;
	}

	/**
	 * @return the kategorije
	 */
	public List<String> getKategorije() {
		return kategorije;
	}

	/**
	 * @param kategorije the kategorije to set
	 */
	public void setKategorije(List<String> kategorije) {
		this.kategorije = kategorije;
	}

	/**
	 * @return the uCurrentContents
	 */
	public String getUCurrentContents() {
		return UCurrentContents;
	}

	/**
	 * @param uCurrentContents the uCurrentContents to set
	 */
	public void setUCurrentContents(String uCurrentContents) {
		UCurrentContents = uCurrentContents;
	}

	/**
	 * @return the scienceCitation
	 */
	public String getScienceCitation() {
		return scienceCitation;
	}

	/**
	 * @param scienceCitation the scienceCitation to set
	 */
	public void setScienceCitation(String scienceCitation) {
		this.scienceCitation = scienceCitation;
	}

	/**
	 * @return the impaktFatorCasopisa
	 */
	public List<KobsonImpactFactorJournal> getImpaktFatorCasopisa() {
		return impaktFatorCasopisa;
	}

	/**
	 * @param impaktFatorCasopisa the impaktFatorCasopisa to set
	 */
	public void setImpaktFatorCasopisa(List<KobsonImpactFactorJournal> impaktFatorCasopisa) {
		this.impaktFatorCasopisa = impaktFatorCasopisa;
	}
	
	/**
	 * @return the impaktFatorCasopisaPetGodina
	 */
	public List<KobsonImpactFactorJournal> getImpaktFatorCasopisaPetGodina() {
		return impaktFatorCasopisaPetGodina;
	}

	/**
	 * @param impaktFatorCasopisaPetGodina the impaktFatorCasopisaPetGodina to set
	 */
	public void setImpaktFatorCasopisaPetGodina(
			List<KobsonImpactFactorJournal> impaktFatorCasopisaPetGodina) {
		this.impaktFatorCasopisaPetGodina = impaktFatorCasopisaPetGodina;
	}

	/**
	 * @return the sponzor
	 */
	public String getSponzor() {
		return sponzor;
	}

	/**
	 * @param sponzor the sponzor to set
	 */
	public void setSponzor(String sponzor) {
		this.sponzor = sponzor;
	}

	/**
	 * @return the izdavac
	 */
	public String getIzdavac() {
		return izdavac;
	}

	/**
	 * @param izdavac the izdavac to set
	 */
	public void setIzdavac(String izdavac) {
		this.izdavac = izdavac;
	}

	/**
	 * @return the adresa
	 */
	public String getAdresa() {
		return adresa;
	}

	/**
	 * @param adresa the adresa to set
	 */
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	/**
	 * @return the telefon
	 */
	public String getTelefon() {
		return telefon;
	}

	/**
	 * @param telefon the telefon to set
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the eMail
	 */
	public String geteMail() {
		return eMail;
	}

	/**
	 * @param eMail the eMail to set
	 */
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the zemlja
	 */
	public String getZemlja() {
		return zemlja;
	}

	/**
	 * @param zemlja the zemlja to set
	 */
	public void setZemlja(String zemlja) {
		this.zemlja = zemlja;
	}

	public String getAktivneKategorije(){
		String retVal = "";
		for (String iterable_element : kategorije) {
			retVal+=iterable_element+";";
		}
		return retVal;
	}

	/**
	 * @return the cRIS_ID
	 */
	public String getCRIS_ID() {
		return CRIS_ID;
	}

	/**
	 * @param cRIS_ID the cRIS_ID to set
	 */
	public void setCRIS_ID(String cRIS_ID) {
		CRIS_ID = cRIS_ID;
	}
	
	/**
	 * @param vERSIONCHANGE the vERSIONCHANGE to set
	 */
	public void setVERSIONCHANGE(String vERSIONCHANGE) {
		VERSIONCHANGE = vERSIONCHANGE;
	}
	
	/**
	 * @return the vERSIONCHANGE
	 */
	public String getVERSIONCHANGE() {
		return VERSIONCHANGE;
	}
	
	/**
	 * @return the dEPENDENCY
	 */
	public String getDEPENDENCY() {
		return DEPENDENCY;
	}

	/**
	 * @param dEPENDENCY the dEPENDENCY to set
	 */
	public void setDEPENDENCY(String dEPENDENCY) {
		DEPENDENCY = dEPENDENCY;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
