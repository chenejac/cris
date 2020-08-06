package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson.beans;

import java.util.ArrayList;


/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */

public class KobsonImpactFactorJournal {

	//casopis
	protected int idJournal =0;
	
	//impakt faktor
	protected int godina =0;
	protected String vrednost ="";
	protected String versionchange="";
	
	protected ArrayList<KobsonCategoryJournal> category = new ArrayList<KobsonCategoryJournal>();
	
	public KobsonImpactFactorJournal(int idJournal, int godina)
	{
		this.idJournal = idJournal;
		this.godina = godina;
	}
	
	public KobsonImpactFactorJournal(int idJournal, int godina, String vrednost, String versionchange)
	{
		this.idJournal = idJournal;
		this.godina = godina;
		this.vrednost = vrednost;
		this.versionchange = versionchange;
	}
	
	public KobsonImpactFactorJournal(int idJournal, int godina,  String vrednost)
	{
		this.idJournal = idJournal;
		this.godina = godina;
		this.vrednost = vrednost;
	}
	
	public KobsonImpactFactorJournal(int idJournal, int godina,
			String vrednost, ArrayList<KobsonCategoryJournal> category) {
		super();
		this.idJournal = idJournal;
		this.godina = godina;
		this.vrednost = vrednost;
		this.category = category;
	}

	public void clear(){

		idJournal =0;
		godina =0;
		vrednost ="";
		vrednost="";
		for (KobsonCategoryJournal cat : category) {
			cat.clear();
		}
		category.clear();
	}
	
	public void print()
	{
		System.out.println("*********casopis********");
		System.out.println("idJournal="+idJournal+ ";");
		
		System.out.println("*********impakt faktor********");
		System.out.println("godina="+godina+ "; vrednost: "+ vrednost);
	}
	
	public String toXML(String ident)
	{
		StringBuffer sb = new StringBuffer("");
		sb.append(ident+"<impaktFaktor godina=\""+godina+"\">" + vrednost + "</impaktFaktor>\n");
		return sb.toString();
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
	 * @return the category
	 */
	public ArrayList<KobsonCategoryJournal> getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ArrayList<KobsonCategoryJournal> category) {
		this.category = category;
	}
	
	public void addCategory(KobsonCategoryJournal cat) {
		this.category.add(cat);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
