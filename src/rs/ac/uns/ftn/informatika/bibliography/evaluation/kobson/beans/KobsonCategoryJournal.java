package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class KobsonCategoryJournal {
	
	//casopis
	protected int idJournal =0;
	
	//kategorija
	protected int idCategory =0;
	
	//rang casopisa
	protected int godina =0;

	protected String rang ="";
	protected String vrednost ="";
	protected String kategorijaCasopisa ="";
	protected String versionchange="";
	
	public KobsonCategoryJournal(int idJournal, int idCategory)
	{
		this.idJournal = idJournal;
		this.idCategory = idCategory;
	}
	
	public KobsonCategoryJournal(int idJournal, int idCategory, int godina, String rang, String vrednost, String kategorijaCasopisa)
	{
		this.idJournal = idJournal;
		this.idCategory = idCategory;
		this.godina = godina;
		this.rang = rang;
		
		if(vrednost==null || vrednost.equals("")){
			
			String niz[] = rang.split("/");
				
			if(niz.length==1 || rang.equals("0/0")){
				this.vrednost = "";
				this.kategorijaCasopisa = "";
				return;
			}
				
			float vred = new Float(niz[0]) / new Float(niz[1]) ;
			this.vrednost = ""+vred;
				
			if(vred<=0.3)
			{
				this.kategorijaCasopisa = "M21";
			} 
			else if (vred<=0.5)
			{
				this.kategorijaCasopisa = "M22";
			}
			else if (vred<=1)
			{
				this.kategorijaCasopisa = "M23";
			}
			else
			{
				this.kategorijaCasopisa = "GRESKA";
			}	
		}
		else {
			this.vrednost = vrednost;
			this.kategorijaCasopisa = kategorijaCasopisa;
		}
	}
	
	public KobsonCategoryJournal(int idJournal, int idCategory, int godina, String rang, String vrednost, String kategorijaCasopisa, String versionchange )
	{
		this.idJournal = idJournal;
		this.idCategory = idCategory;
		this.godina = godina;
		this.rang = rang;
		this.versionchange = versionchange;
		
		if(vrednost==null || vrednost.equals("")){
			
			String niz[] = rang.split("/");
				
			if(niz.length==1 || rang.equals("0/0")){
				this.vrednost = "";
				this.kategorijaCasopisa = "";
				return;
			}
				
			float vred = new Float(niz[0]) / new Float(niz[1]) ;
			this.vrednost = ""+vred;
				
			if(vred<=0.3)
			{
				this.kategorijaCasopisa = "M21";
			} 
			else if (vred<=0.5)
			{
				this.kategorijaCasopisa = "M22";
			}
			else if (vred<=1)
			{
				this.kategorijaCasopisa = "M23";
			}
			else
			{
				this.kategorijaCasopisa = "GRESKA";
			}	
		}
		else {
			this.vrednost = vrednost;
			this.kategorijaCasopisa = kategorijaCasopisa;
		}
	}
	
	public void clear(){

		idJournal =0;
		idCategory =0;
		godina =0;
		rang ="";
		vrednost ="";
		kategorijaCasopisa ="";
		versionchange="";
	}
	
	public void print(){
		
	}
	
	public String toXML(String ident){
		
		return "";
	}
	
	/**
	 * @return the idJournal
	 */
	public int getIdJournal() {
		return idJournal;
	}
	
	/**
	 * @param idJournal the idJournal to set
	 */
	public void setIdJournal(int idJournal) {
		this.idJournal = idJournal;
	}

	/**
	 * @return the idCategory
	 */
	public int getIdCategory() {
		return idCategory;
	}

	/**
	 * @param idCategory the idCategory to set
	 */
	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}

	/**
	 * @return the godina
	 */
	public int getGodina() {
		return godina;
	}

	/**
	 * @param godina the godina to set
	 */
	public void setGodina(int godina) {
		this.godina = godina;
	}

	/**
	 * @return the rang
	 */
	public String getRang() {
		return rang;
	}

	/**
	 * @param rang the rang to set
	 */
	public void setRang(String rang) {
		this.rang = rang;
	}

	/**
	 * @return the vrednost
	 */
	public String getVrednost() {
		return vrednost;
	}

	/**
	 * @param vrednost the vrednost to set
	 */
	public void setVrednost(String vrednost) {
		this.vrednost = vrednost;
	}

	/**
	 * @return the versionchange
	 */
	public String getVersionchange() {
		return versionchange;
	}

	/**
	 * @param versionchange the versionchange to set
	 */
	public void setVersionchange(String versionchange) {
		this.versionchange = versionchange;
	}

	/**
	 * @return the kategorijaCasopisa
	 */
	public String getKategorijaCasopisa() {
		return kategorijaCasopisa;
	}

	/**
	 * @param kategorijaCasopisa the kategorijaCasopisa to set
	 */
	public void setKategorijaCasopisa(String kategorijaCasopisa) {
		this.kategorijaCasopisa = kategorijaCasopisa;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
