package rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rs.ac.uns.ftn.informatika.bibliography.dto.RegisterEntryDTO;



public class MaticnaKnjigaItemBean {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	private boolean bologna;
	
	private String redniBroj = "";
	private String redniBrojUGodini = "";
	private String prezimeAutora = "";
	private String imeAutora = "";
	private String datumRodjenja = null;
	private String mestoRodjenja = "";
	private String opstinaRodjenja = "";
	private String drzavaRodjenja = "";
	private String ocevoIme = "";
	private String majcinoIme = "";
	private String ocevoImeIPrezime = "";
	private String majcinoImeIPrezime = "";
	private String imeIPrezimeStaratelja = "";
	
	private String roditeljIliStaratelj = "";
	
	

	private String nazivZavrseneVisokoskolskeUstanove = "";
	private String sedisteZavrseneVisokoskolskeUstanove = "";
	
	private String strucniAkademskiNazivDiplomskeStaro = ""; 
	private String strucniAkademskiSkraceniNazivStaro = "";
	private String datumSticanjaZvanjaStaro = null;
	
	private String strucniAkademskiNazivDiplomskeBolonja = ""; 
	private String strucniAkademskiSkraceniNazivBolonja = "";
	private String godinaZavrsetkaBolonja = "";
	
	private String organizacionaJedinicaOdbrane = "";
	private String mestoOrganizacioneJediniceOdbrane = "";
	
	private String naslovDisertacije = "";
	private String umetnickiProjekat = "";
	
	private String komisija = "";
	private String mentori;
	private String ocenaDisertacije = "";
	private String datumOdbrane = null;
	private String skolskaGodina = "";
	private String naucniNaziv = "";
	
	private String brojDiplome = "";
	private String datumIzdavanjaDiplome = null;
	
	private String brojDodatka = "";
	private String datumIzdavanjaDodatka = null;
	
	private Date datumPromocije = null;
	
	private RegisterEntryDTO entry = null;
	
//adresa za slanje poziva za promociju
	private String ulicaIBroj = "";
	private String mesto = "";
	private String postanskiBroj = "";
	private String opstina = "";
	private String drzava = "";
	private String email = "";
	private String telefon = "";
	
	private String adresa = "";
	
	
	public MaticnaKnjigaItemBean(){
		
	}
	
	public MaticnaKnjigaItemBean(RegisterEntryDTO entry){
		try{
			this.entry = entry;
		redniBroj = entry.getId()==null ? "" : entry.getId();
		redniBrojUGodini = entry.getAcademicYearNumber()==0 ? "" : ""+entry.getAcademicYearNumber();
		prezimeAutora = entry.getAuthorName()==null ? "" : entry.getAuthorName().getFirstname();
		imeAutora = entry.getAuthorName()==null ? "" : entry.getAuthorName().getLastname();
		datumRodjenja = entry.getBirthDate()==null ? "" : dateFormat.format(entry.getBirthDate().getTime());
		mestoRodjenja = entry.getBirthPlace()==null ? "" : entry.getBirthPlace();
		opstinaRodjenja = entry.getBirthCity()==null ? "" : entry.getBirthCity();
		drzavaRodjenja = entry.getBirthCountry()==null ? "" : entry.getBirthCountry();
		if(entry.getFatherName()!=null){
			if(entry.getFatherName().getLastname()==null || entry.getFatherName().getLastname().equals("")){
				ocevoIme = entry.getFatherName().getFirstname();
			}else{			
					ocevoImeIPrezime = entry.getFatherName().getLastname()+" "+entry.getFatherName().getFirstname();
			}
		}
		if(entry.getMotherName()!=null){
		 if(entry.getMotherName().getLastname()==null || entry.getMotherName().getLastname().equals("")){
		 	majcinoIme = entry.getMotherName().getFirstname();		 	
		 }else{
		 	majcinoImeIPrezime = entry.getMotherName().getLastname()+" "+entry.getMotherName().getFirstname();
		 }
		}
		imeIPrezimeStaratelja = entry.getGuardiansName()==null ? "" : entry.getGuardiansName();			
	
		if((ocevoIme!=null && !ocevoIme.equals("")) || (ocevoImeIPrezime!=null && !ocevoImeIPrezime.equals("")) 
				|| (majcinoIme!=null && !majcinoIme.equals("")) 
				|| (majcinoImeIPrezime!=null && !majcinoImeIPrezime.equals(""))
				|| (imeIPrezimeStaratelja!=null && !imeIPrezimeStaratelja.equals(""))){
			if(imeIPrezimeStaratelja!=null && !imeIPrezimeStaratelja.equals("")){
				roditeljIliStaratelj = "СТАРАТЕЉ:<br>"+imeIPrezimeStaratelja;
			}if(ocevoImeIPrezime!=null && !ocevoImeIPrezime.equals(""))
				roditeljIliStaratelj = "ОТАЦ:<br>"+ocevoImeIPrezime+"<br>";
			else if(ocevoIme!=null && !ocevoIme.equals(""))
				roditeljIliStaratelj = "ОТАЦ:<br>"+ocevoIme+"<br>";
			if(majcinoImeIPrezime!=null && !majcinoImeIPrezime.equals(""))
				roditeljIliStaratelj = roditeljIliStaratelj+"МАЈКА:<br>"+majcinoImeIPrezime;
			else if(majcinoIme!=null && !majcinoIme.equals(""))
				roditeljIliStaratelj = roditeljIliStaratelj+"МАЈКА:<br>"+majcinoIme;			
		}		
		
		nazivZavrseneVisokoskolskeUstanove = entry.getPreviouslyGraduated()==null ? "" : entry.getPreviouslyGraduated();
		sedisteZavrseneVisokoskolskeUstanove = entry.getPreviouslyGraduatedPlace()==null ? "":entry.getPreviouslyGraduatedPlace();		
	 if(entry.getPreviouslyNameOfAuthorDegreeOld()==null)
	 	bologna = true;
	 else
	 	bologna = false;
	 if(!bologna){
	 	strucniAkademskiNazivDiplomskeStaro = entry.getPreviouslyNameOfAuthorDegreeOld()==null ? "" :entry.getPreviouslyNameOfAuthorDegreeOld() ;	
	 	strucniAkademskiSkraceniNazivStaro  = entry.getPreviouslyNameOfAuthorDegreeAbbreviationOld()==null ? "" : entry.getPreviouslyNameOfAuthorDegreeAbbreviationOld() ;
	 	datumSticanjaZvanjaStaro = entry.getPreviouslyNameOfAuthorDegreeDateOld()==null ? "" : dateFormat.format(entry.getPreviouslyNameOfAuthorDegreeDateOld().getTime());	 	
	 }else{
	 	strucniAkademskiNazivDiplomskeBolonja = entry.getPreviouslyNameOfAuthorDegreeBologna()==null ? "" : entry.getPreviouslyNameOfAuthorDegreeBologna();	
	 	strucniAkademskiSkraceniNazivBolonja  = entry.getPreviouslyNameOfAuthorDegreeAbbreviationBologna()==null ? "" : entry.getPreviouslyNameOfAuthorDegreeAbbreviationBologna();
	 	godinaZavrsetkaBolonja = entry.getPreviouslyNameOfAuthorDegreeYearBologna()==null ? "" : entry.getPreviouslyNameOfAuthorDegreeYearBologna();
	 }
		organizacionaJedinicaOdbrane = entry.getInstitution();
		mestoOrganizacioneJediniceOdbrane = entry.getInstitutionPlace();
		naslovDisertacije = entry.getTitle();
		
		//umetnicki projekat
		
		komisija = entry.getCommissionMembers()==null ? "" : entry.getCommissionMembers().replace("\n", "<br>");
		mentori = entry.getAdvisors()==null ? "" : entry.getAdvisors().replace("\n", "<br>"); 
		ocenaDisertacije = entry.getMark()==null ? "" : entry.getMark();
		
		if(!ocenaDisertacije.equals("")){
			if(ocenaDisertacije.equals("10")) ocenaDisertacije+="(десет)";
			if(ocenaDisertacije.equals("9")) ocenaDisertacije+="(девет)";
			if(ocenaDisertacije.equals("8")) ocenaDisertacije+="(осам)";
			if(ocenaDisertacije.equals("7")) ocenaDisertacije+="(седам)";
			if(ocenaDisertacije.equals("6")) ocenaDisertacije+="(шест)";
		}
		
		datumOdbrane = entry.getDefendedOn()==null ? "" : dateFormat.format(entry.getDefendedOn().getTime());		
		skolskaGodina = entry.getPromotionDate()==null ? "" : entry.getAcademicYear();	
		
		
		naucniNaziv = entry.getNameOfAuthorDegree()==null ? "": entry.getNameOfAuthorDegree();
		
		brojDiplome = entry.getDiplomaNumber()==null ? "" : entry.getDiplomaNumber();
		datumIzdavanjaDiplome = entry.getDiplomaPublicationDate()==null ? "" : dateFormat.format(entry.getDiplomaPublicationDate().getTime());
		
		brojDodatka = entry.getSupplementNumber()== null ? "" : entry.getSupplementNumber();
		datumIzdavanjaDodatka = entry.getSupplementPublicationDate()== null ? "" : dateFormat.format(entry.getSupplementPublicationDate().getTime());
		
		datumPromocije = entry.getPromotionDate()==null ? null : entry.getPromotionDate().getTime();
		
		if((skolskaGodina==null || skolskaGodina.equals("")) && entry.getPromotionDate()!=null)
			skolskaGodina = MaticnaKnjigaUtils.calculateSkolskaGodina(entry.getPromotionDate());
		
		ulicaIBroj = entry.getStreetAndNumber() == null ? "" : entry.getStreetAndNumber();
		mesto = entry.getPlace()==null ? "" : entry.getPlace();
		postanskiBroj = entry.getPostalCode()==null ? "" : entry.getPostalCode();
	 opstina = entry.getCity()==null ? "" : entry.getCity();
		drzava = entry.getCountry()==null ? "" : entry.getCountry();
		email = entry.getEmail()==null ? "" : entry.getEmail();
		telefon = entry.getPhone()==null ? "" : entry.getPhone();
		adresa = ulicaIBroj+"<br>"+mesto+"<br>"+postanskiBroj
										+"<br>"+opstina+"<br>"+drzava+"<br>"
										+email+"<br>tel:"+telefon;
		
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	

	/**
	 * @return the bologna
	 */
	public boolean isBologna() {
		return bologna;
	}

	/**
	 * @param bologna the bologna to set
	 */
	public void setBologna(boolean bologna) {
		this.bologna = bologna;
	}

	/**
	 * @return the redniBroj
	 */
	public String getRedniBroj() {
		return redniBroj;
	}


	/**
	 * @param redniBroj the redniBroj to set
	 */
	public void setRedniBroj(String redniBroj) {
		this.redniBroj = redniBroj;
	}


	/**
	 * @return the prezimeAutora
	 */
	public String getPrezimeAutora() {
		return prezimeAutora;
	}


	/**
	 * @param prezimeAutora the prezimeAutora to set
	 */
	public void setPrezimeAutora(String prezimeAutora) {
		this.prezimeAutora = prezimeAutora;
	}


	/**
	 * @return the imeAutora
	 */
	public String getImeAutora() {
		return imeAutora;
	}


	/**
	 * @param imeAutora the imeAutora to set
	 */
	public void setImeAutora(String imeAutora) {
		this.imeAutora = imeAutora;
	}


	/**
	 * @return the datumRodjenja
	 */
	public String getDatumRodjenja() {
		return datumRodjenja;
	}


	/**
	 * @param datumRodjenja the datumRodjenja to set
	 */
	public void setDatumRodjenja(String datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}


	/**
	 * @return the mestoRodjenja
	 */
	public String getMestoRodjenja() {
		return mestoRodjenja;
	}


	/**
	 * @param mestoRodjenja the mestoRodjenja to set
	 */
	public void setMestoRodjenja(String mestoRodjenja) {
		this.mestoRodjenja = mestoRodjenja;
	}


	/**
	 * @return the opstinaRodjenja
	 */
	public String getOpstinaRodjenja() {
		return opstinaRodjenja;
	}


	/**
	 * @param opstinaRodjenja the opstinaRodjenja to set
	 */
	public void setOpstinaRodjenja(String opstinaRodjenja) {
		this.opstinaRodjenja = opstinaRodjenja;
	}


	/**
	 * @return the drzavaRodjenja
	 */
	public String getDrzavaRodjenja() {
		return drzavaRodjenja;
	}


	/**
	 * @param drzavaRodjenja the drzavaRodjenja to set
	 */
	public void setDrzavaRodjenja(String drzavaRodjenja) {
		this.drzavaRodjenja = drzavaRodjenja;
	}


	/**
	 * @return the ocevoIme
	 */
	public String getOcevoIme() {
		return ocevoIme;
	}


	/**
	 * @param ocevoIme the ocevoIme to set
	 */
	public void setOcevoIme(String ocevoIme) {
		this.ocevoIme = ocevoIme;
	}


	/**
	 * @return the majcinoIme
	 */
	public String getMajcinoIme() {
		return majcinoIme;
	}


	/**
	 * @param majcinoIme the majcinoIme to set
	 */
	public void setMajcinoIme(String majcinoIme) {
		this.majcinoIme = majcinoIme;
	}


	/**
	 * @return the ocevoImeIPrezime
	 */
	public String getOcevoImeIPrezime() {
		return ocevoImeIPrezime;
	}


	/**
	 * @param ocevoImeIPrezime the ocevoImeIPrezime to set
	 */
	public void setOcevoImeIPrezime(String ocevoImeIPrezime) {
		this.ocevoImeIPrezime = ocevoImeIPrezime;
	}


	/**
	 * @return the majcinoImeIPrezime
	 */
	public String getMajcinoImeIPrezime() {
		return majcinoImeIPrezime;
	}


	/**
	 * @param majcinoImeIPrezime the majcinoImeIPrezime to set
	 */
	public void setMajcinoImeIPrezime(String majcinoImeIPrezime) {
		this.majcinoImeIPrezime = majcinoImeIPrezime;
	}


	/**
	 * @return the imeIPrezimeStaratelja
	 */
	public String getImeIPrezimeStaratelja() {
		return imeIPrezimeStaratelja;
	}


	/**
	 * @param imeIPrezimeStaratelja the imeIPrezimeStaratelja to set
	 */
	public void setImeIPrezimeStaratelja(String imeIPrezimeStaratelja) {
		this.imeIPrezimeStaratelja = imeIPrezimeStaratelja;
	}


	/**
	 * @return the nazivZavrseneVisokoskolskeUstanove
	 */
	public String getNazivZavrseneVisokoskolskeUstanove() {
		return nazivZavrseneVisokoskolskeUstanove;
	}


	/**
	 * @param nazivZavrseneVisokoskolskeUstanove the nazivZavrseneVisokoskolskeUstanove to set
	 */
	public void setNazivZavrseneVisokoskolskeUstanove(
			String nazivZavrseneVisokoskolskeUstanove) {
		this.nazivZavrseneVisokoskolskeUstanove = nazivZavrseneVisokoskolskeUstanove;
	}


	/**
	 * @return the sedisteZavrseneVisokoskolskeUstanove
	 */
	public String getSedisteZavrseneVisokoskolskeUstanove() {
		return sedisteZavrseneVisokoskolskeUstanove;
	}


	/**
	 * @param sedisteZavrseneVisokoskolskeUstanove the sedisteZavrseneVisokoskolskeUstanove to set
	 */
	public void setSedisteZavrseneVisokoskolskeUstanove(
			String sedisteZavrseneVisokoskolskeUstanove) {
		this.sedisteZavrseneVisokoskolskeUstanove = sedisteZavrseneVisokoskolskeUstanove;
	}


	/**
	 * @return the strucniAkademskiNazivDiplomskeStaro
	 */
	public String getStrucniAkademskiNazivDiplomskeStaro() {
		return strucniAkademskiNazivDiplomskeStaro;
	}


	/**
	 * @param strucniAkademskiNazivDiplomskeStaro the strucniAkademskiNazivDiplomskeStaro to set
	 */
	public void setStrucniAkademskiNazivDiplomskeStaro(
			String strucniAkademskiNazivDiplomskeStaro) {
		this.strucniAkademskiNazivDiplomskeStaro = strucniAkademskiNazivDiplomskeStaro;
	}


	/**
	 * @return the strucniAkademskiSkraceniNazivStaro
	 */
	public String getStrucniAkademskiSkraceniNazivStaro() {
		return strucniAkademskiSkraceniNazivStaro;
	}


	/**
	 * @param strucniAkademskiSkraceniNazivStaro the strucniAkademskiSkraceniNazivStaro to set
	 */
	public void setStrucniAkademskiSkraceniNazivStaro(
			String strucniAkademskiSkraceniNazivStaro) {
		this.strucniAkademskiSkraceniNazivStaro = strucniAkademskiSkraceniNazivStaro;
	}


	/**
	 * @return the datumSticanjaZvanjaStaro
	 */
	public String getDatumSticanjaZvanjaStaro() {
		return datumSticanjaZvanjaStaro;
	}


	/**
	 * @param datumSticanjaZvanjaStaro the datumSticanjaZvanjaStaro to set
	 */
	public void setDatumSticanjaZvanjaStaro(String datumSticanjaZvanjaStaro) {
		this.datumSticanjaZvanjaStaro = datumSticanjaZvanjaStaro;
	}


	/**
	 * @return the strucniAkademskiNazivDiplomskeBolonja
	 */
	public String getStrucniAkademskiNazivDiplomskeBolonja() {
		return strucniAkademskiNazivDiplomskeBolonja;
	}


	/**
	 * @param strucniAkademskiNazivDiplomskeBolonja the strucniAkademskiNazivDiplomskeBolonja to set
	 */
	public void setStrucniAkademskiNazivDiplomskeBolonja(
			String strucniAkademskiNazivDiplomskeBolonja) {
		this.strucniAkademskiNazivDiplomskeBolonja = strucniAkademskiNazivDiplomskeBolonja;
	}


	/**
	 * @return the strucniAkademskiSkraceniNazivBolonja
	 */
	public String getStrucniAkademskiSkraceniNazivBolonja() {
		return strucniAkademskiSkraceniNazivBolonja;
	}


	/**
	 * @param strucniAkademskiSkraceniNazivBolonja the strucniAkademskiSkraceniNazivBolonja to set
	 */
	public void setStrucniAkademskiSkraceniNazivBolonja(
			String strucniAkademskiSkraceniNazivBolonja) {
		this.strucniAkademskiSkraceniNazivBolonja = strucniAkademskiSkraceniNazivBolonja;
	}


	/**
	 * @return the godinaZavrsetkaBolonja
	 */
	public String getGodinaZavrsetkaBolonja() {
		return godinaZavrsetkaBolonja;
	}


	/**
	 * @param godinaZavrsetkaBolonja the godinaZavrsetkaBolonja to set
	 */
	public void setGodinaZavrsetkaBolonja(String godinaZavrsetkaBolonja) {
		this.godinaZavrsetkaBolonja = godinaZavrsetkaBolonja;
	}


	/**
	 * @return the organizacionaJedinicaOdbrane
	 */
	public String getOrganizacionaJedinicaOdbrane() {
		return organizacionaJedinicaOdbrane;
	}


	/**
	 * @param organizacionaJedinicaOdbrane the organizacionaJedinicaOdbrane to set
	 */
	public void setOrganizacionaJedinicaOdbrane(String organizacionaJedinicaOdbrane) {
		this.organizacionaJedinicaOdbrane = organizacionaJedinicaOdbrane;
	}


	/**
	 * @return the mestoOrganizacioneJediniceOdbrane
	 */
	public String getMestoOrganizacioneJediniceOdbrane() {
		return mestoOrganizacioneJediniceOdbrane;
	}


	/**
	 * @param mestoOrganizacioneJediniceOdbrane the mestoOrganizacioneJediniceOdbrane to set
	 */
	public void setMestoOrganizacioneJediniceOdbrane(
			String mestoOrganizacioneJediniceOdbrane) {
		this.mestoOrganizacioneJediniceOdbrane = mestoOrganizacioneJediniceOdbrane;
	}


	/**
	 * @return the naslovDisertacije
	 */
	public String getNaslovDisertacije() {
		return naslovDisertacije;
	}


	/**
	 * @param naslovDisertacije the naslovDisertacije to set
	 */
	public void setNaslovDisertacije(String naslovDisertacije) {
		this.naslovDisertacije = naslovDisertacije;
	}


	/**
	 * @return the komisija
	 */
	public String getKomisija() {
		return komisija;
	}


	/**
	 * @param komisija the komisija to set
	 */
	public void setKomisija(String komisija) {
		this.komisija = komisija;
	}


	/**
	 * @return the ocenaDisertacije
	 */
	public String getOcenaDisertacije() {
		return ocenaDisertacije;
	}


	/**
	 * @param ocenaDisertacije the ocenaDisertacije to set
	 */
	public void setOcenaDisertacije(String ocenaDisertacije) {
		this.ocenaDisertacije = ocenaDisertacije;
	}


	/**
	 * @return the datumOdbrane
	 */
	public String getDatumOdbrane() {
		return datumOdbrane;
	}


	/**
	 * @param datumOdbrane the datumOdbrane to set
	 */
	public void setDatumOdbrane(String datumOdbrane) {
		this.datumOdbrane = datumOdbrane;
	}


	/**
	 * @return the naucniNaziv
	 */
	public String getNaucniNaziv() {
		return naucniNaziv;
	}


	/**
	 * @param naucniNaziv the naucniNaziv to set
	 */
	public void setNaucniNaziv(String naucniNaziv) {
		this.naucniNaziv = naucniNaziv;
	}


	/**
	 * @return the brojDiplome
	 */
	public String getBrojDiplome() {
		return brojDiplome;
	}


	/**
	 * @param brojDiplome the brojDiplome to set
	 */
	public void setBrojDiplome(String brojDiplome) {
		this.brojDiplome = brojDiplome;
	}


	/**
	 * @return the datumIzdavanjaDiplome
	 */
	public String getDatumIzdavanjaDiplome() {
		return datumIzdavanjaDiplome;
	}


	/**
	 * @param datumIzdavanjaDiplome the datumIzdavanjaDiplome to set
	 */
	public void setDatumIzdavanjaDiplome(String datumIzdavanjaDiplome) {
		this.datumIzdavanjaDiplome = datumIzdavanjaDiplome;
	}


	/**
	 * @return the brojDodatka
	 */
	public String getBrojDodatka() {
		return brojDodatka;
	}


	/**
	 * @param brojDodatka the brojDodatka to set
	 */
	public void setBrojDodatka(String brojDodatka) {
		this.brojDodatka = brojDodatka;
	}


	/**
	 * @return the datumIzdavanjaDodatka
	 */
	public String getDatumIzdavanjaDodatka() {
		return datumIzdavanjaDodatka;
	}


	/**
	 * @param datumIzdavanjaDodatka the datumIzdavanjaDodatka to set
	 */
	public void setDatumIzdavanjaDodatka(String datumIzdavanjaDodatka) {
		this.datumIzdavanjaDodatka = datumIzdavanjaDodatka;
	}


	/**
	 * @return the datumPromocije
	 */
	public Date getDatumPromocije() {
		return datumPromocije;
	}


	/**
	 * @param datumPromocije the datumPromocije to set
	 */
	public void setDatumPromocije(Date datumPromocije) {
		this.datumPromocije = datumPromocije;
	}

	/**
	 * @return the roditeljIliStaratelj
	 */
	public String getRoditeljIliStaratelj() {
		return roditeljIliStaratelj;
	}

	/**
	 * @param roditeljIliStaratelj the roditeljIliStaratelj to set
	 */
	public void setRoditeljIliStaratelj(String roditeljIliStaratelj) {
		this.roditeljIliStaratelj = roditeljIliStaratelj;
	}

	/**
	 * @return the mentori
	 */
	public String getMentori() {
		return mentori;
	}

	/**
	 * @param mentori the mentori to set
	 */
	public void setMentori(String mentori) {
		this.mentori = mentori;
	}

	public void setSkolskaGodina(String skolskaGodina) {
		this.skolskaGodina = skolskaGodina;
	}

	public String getSkolskaGodina() {
		return skolskaGodina;
	}

	/**
	 * @return the umetnickiProjekat
	 */
	public String getUmetnickiProjekat() {
		return umetnickiProjekat;
	}

	/**
	 * @param umetnickiProjekat the umetnickiProjekat to set
	 */
	public void setUmetnickiProjekat(String umetnickiProjekat) {
		this.umetnickiProjekat = umetnickiProjekat;
	}

	/**
	 * @return the entry
	 */
	public RegisterEntryDTO getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(RegisterEntryDTO entry) {
		this.entry = entry;
	}

	/**
	 * @return the redniBrojUGodini
	 */
	public String getRedniBrojUGodini() {
		return redniBrojUGodini;
	}

	/**
	 * @param redniBrojUGodini the redniBrojUGodini to set
	 */
	public void setRedniBrojUGodini(String redniBrojUGodini) {
		this.redniBrojUGodini = redniBrojUGodini;
	}

	/**
	 * @return the ulicaIBroj
	 */
	public String getUlicaIBroj() {
		return ulicaIBroj;
	}

	/**
	 * @param ulicaIBroj the ulicaIBroj to set
	 */
	public void setUlicaIBroj(String ulicaIBroj) {
		this.ulicaIBroj = ulicaIBroj;
	}

	/**
	 * @return the mesto
	 */
	public String getMesto() {
		return mesto;
	}

	/**
	 * @param mesto the mesto to set
	 */
	public void setMesto(String mesto) {
		this.mesto = mesto;
	}

	/**
	 * @return the postanskiBroj
	 */
	public String getPostanskiBroj() {
		return postanskiBroj;
	}

	/**
	 * @param postanskiBroj the postanskiBroj to set
	 */
	public void setPostanskiBroj(String postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	/**
	 * @return the opstina
	 */
	public String getOpstina() {
		return opstina;
	}

	/**
	 * @param opstina the opstina to set
	 */
	public void setOpstina(String opstina) {
		this.opstina = opstina;
	}

	/**
	 * @return the drzava
	 */
	public String getDrzava() {
		return drzava;
	}

	/**
	 * @param drzava the drzava to set
	 */
	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	
	
	public String getAdresa(){
		return adresa;
	}
	
	public void setAdresa(String adresa){
		this.adresa = adresa;
	}
	
	
	

}
