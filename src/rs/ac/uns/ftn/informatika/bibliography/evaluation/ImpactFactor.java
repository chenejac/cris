/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenejac@uns.ac.rs
 *
 */
public class ImpactFactor {

	private Integer year;
	private Double valueOfImpactFactor;
	private List<ResearchAreaRanking> researchAreas;
	
	
	/**
	 * 
	 */
	public ImpactFactor() {
		super();
		researchAreas = new ArrayList<ResearchAreaRanking>();
	}
	/**
	 * @param year
	 * @param valueOfImpactFactor
	 * @param researchAreas
	 */
	public ImpactFactor(Integer year, Double valueOfImpactFactor, List<ResearchAreaRanking> researchAreas) {
		super();
		this.year = year;
		this.valueOfImpactFactor = valueOfImpactFactor;
		this.researchAreas = researchAreas;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	
	/**
	 * @return the valueOfImpactFactor
	 */
	public Double getValueOfImpactFactor() {
		return valueOfImpactFactor;
	}
	/**
	 * @param valueOfImpactFactor the valueOfImpactFactor to set
	 */
	public void setValueOfImpactFactor(Double valueOfImpactFactor) {
		this.valueOfImpactFactor = valueOfImpactFactor;
	}
	/**
	 * @param researchAreas the researchAreas to set
	 */
	/**
	 * @return the researchAreas
	 */
	public List<ResearchAreaRanking> getResearchAreas() {
		return researchAreas;
	}
	/**
	 * @param researchAreas the researchAreas to set
	 */
	public void setResearchAreas(List<ResearchAreaRanking> researchAreas) {
		this.researchAreas = researchAreas;
	}
	
	public String getLeadingInternationalJournalResearchAreas() {
		StringBuffer retVal = new StringBuffer();
		for (ResearchAreaRanking researchArea : researchAreas) {
			if(researchArea.getDividend() <= 0.3){
				if(retVal.length() > 0)
					retVal.append("; ");
				retVal.append(researchArea.getResearchAreaDTO().toString());
			}
		}
		return retVal.toString();
	}
	public String getOutstandingInternationalJournalResearchAreas() {
		StringBuffer retVal = new StringBuffer();
		for (ResearchAreaRanking researchArea : researchAreas) {
			if((researchArea.getDividend() > 0.3) && (researchArea.getDividend() <= 0.5)){
				if(retVal.length() > 0)
					retVal.append("; ");
				retVal.append(researchArea.getResearchAreaDTO().toString());
			}
		}
		return retVal.toString();
	}
	public String getInternationalJournalResearchAreas() {
		StringBuffer retVal = new StringBuffer();
		for (ResearchAreaRanking researchArea : researchAreas) {
			if(researchArea.getDividend() > 0.5){
				if(retVal.length() > 0)
					retVal.append("; ");
				retVal.append(researchArea.getResearchAreaDTO().toString());
			}
		}
		return retVal.toString();
	}
	
	public ResearchAreaRanking getMaxPositionReseachArea(){
		ResearchAreaRanking retVal = null;
		for (ResearchAreaRanking researchArea : researchAreas) {
			if(retVal == null)
				retVal = researchArea;
			else if (retVal.getDividend() > researchArea.getDividend())
				retVal = researchArea;
		}
		return retVal;
	}
	
	public String getMaxCategory(){
		String retVal = null;
		if(getMaxPositionReseachArea().getDividend() <= 0.1)
			retVal = "M21a";
		else if(getMaxPositionReseachArea().getDividend() <= 0.3)
			retVal = "M21";
		else if(getMaxPositionReseachArea().getDividend() <= 0.6)
			retVal = "M22";
		else
			retVal = "M23";
		return retVal;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		ImpactFactor imf = (ImpactFactor) obj;
		return year.equals(imf.getYear());
	}
	
	
}
