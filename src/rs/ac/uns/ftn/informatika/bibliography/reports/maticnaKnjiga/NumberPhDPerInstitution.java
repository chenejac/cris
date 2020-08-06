package rs.ac.uns.ftn.informatika.bibliography.reports.maticnaKnjiga;

public class NumberPhDPerInstitution {
	
	private String institutionName;
	private int numPhDOld;
	private int numPhDNew;
	
	public NumberPhDPerInstitution(){
		super();		
	}

	public NumberPhDPerInstitution(String institutionName, int numPhDold,
			int numPhDNew) {
		super();
		this.institutionName = institutionName;
		this.numPhDOld = numPhDold;
		this.numPhDNew = numPhDNew;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public int getNumPhDOld() {
		return numPhDOld;
	}

	public void setNumPhDOld(int numPhDold) {
		this.numPhDOld = numPhDold;
	}

	public int getNumPhDNew() {
		return numPhDNew;
	}

	public void setNumPhDNew(int numPhDNew) {
		this.numPhDNew = numPhDNew;
	}
	
	
	
	

}
