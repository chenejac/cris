package rs.ac.uns.ftn.informatika.bibliography.reports.samovrednovanje;

import java.util.ArrayList;
import java.util.List;



public class ResultForYearDTO {
	
	
	// ove brojeve bojana postavlja ne mora dragan
	
	private int numM21a = 0;
	private int numM21 = 0;
	private int numM22 = 0;
	private int numM23 = 0;
	
	
	// ovo dragan da popuni
	String year;
	List<ResultForTypeDTO> resultsForType = new ArrayList<ResultForTypeDTO>();
	
	
	/**
	 * @return the resultsForType
	 */
	public List<ResultForTypeDTO> getResultsForType() {
		return resultsForType;
	}
	/**
	 * @param resultsForType the resultsForType to set
	 */
	public void setResultsForType(List<ResultForTypeDTO> resultsForType) {
		this.resultsForType = resultsForType;
	}
	
	
	/**
	 * @return the numM21a
	 */
	public int getNumM21a() {
		return numM21a;
	}
	/**
	 * @param numM21a the numM21a to set
	 */
	public void setNumM21a(int numM21a) {
		this.numM21a = numM21a;
	}
	/**
	 * @return the numM21
	 */
	public int getNumM21() {
		return numM21;
	}
	/**
	 * @param numM21 the numM21 to set
	 */
	public void setNumM21(int numM21) {
		this.numM21 = numM21;
	}
	/**
	 * @return the numM22
	 */
	public int getNumM22() {
		return numM22;
	}
	/**
	 * @param numM22 the numM22 to set
	 */
	public void setNumM22(int numM22) {
		this.numM22 = numM22;
	}
	/**
	 * @return the numM23
	 */
	public int getNumM23() {
		return numM23;
	}
	/**
	 * @param numM23 the numM23 to set
	 */
	public void setNumM23(int numM23) {
		this.numM23 = numM23;
	}
	
	
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	public void setNumbers(){
		numM21a = 0;
		numM21 = 0;
		numM22 = 0;
		numM23 = 0;
		for(ResultForTypeDTO res:resultsForType){
			if(res.getResultType().equals("M21a"))		
				numM21++;
			if(res.getResultType().equals("M21"))		
				numM21++;		
			if(res.getResultType().equals("M22"))		
				numM22++;				
			if(res.getResultType().equals("M23"))		
				numM23++;			
		}		
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer("\n\n" + year + "\n");
		for (ResultForTypeDTO resultForType : resultsForType) {
			String resultTemp = resultForType.toString();
			if(resultTemp != null)
				retVal.append(resultTemp + "\n");
		}
		return retVal.toString();
	}
	
	
	
}
