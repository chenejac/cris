package rs.ac.uns.ftn.informatika.bibliography.reports.unsdesertacije;

public class UnsDisertacijeZbirnoBean {
	
	private String nazivFakulteta;
	private int brojDisertacija;
	
	
	public UnsDisertacijeZbirnoBean(){
		
	}
	

	/**
	 * @param nazivFakulteta
	 * @param brojDisertacija
	 */
	public UnsDisertacijeZbirnoBean(String nazivFakulteta, int brojDisertacija) {
		super();
		this.nazivFakulteta = nazivFakulteta;
		this.brojDisertacija = brojDisertacija;
	}

	/**
	 * @return the nazivFakulteta
	 */
	public String getNazivFakulteta() {
		return nazivFakulteta;
	}
	/**
	 * @param nazivFakulteta the nazivFakulteta to set
	 */
	public void setNazivFakulteta(String nazivFakulteta) {
		this.nazivFakulteta = nazivFakulteta;
	}
	/**
	 * @return the brojDisertacija
	 */
	public int getBrojDisertacija() {
		return brojDisertacija;
	}
	/**
	 * @param brojDisertacija the brojDisertacija to set
	 */
	public void setBrojDisertacija(int brojDisertacija) {
		this.brojDisertacija = brojDisertacija;
	}
	
	
	
}
