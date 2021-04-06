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

	private String valueOfImpactFactorForMaxResearchArea;
	private Integer year;
	private Double valueOfImpactFactor;
	private List<ResearchAreaRanking> researchAreas;
	private Double valueOfImpactFactorFiveYears;
	private List<ResearchAreaRanking> researchAreasFiveYears;
	
	/**
	 * 
	 */
	public ImpactFactor() {
		super();
		researchAreas = new ArrayList<ResearchAreaRanking>();
		researchAreasFiveYears = new ArrayList<ResearchAreaRanking>();
	}
	/**
	 * @param year
	 * @param valueOfImpactFactor
	 * @param researchAreas
	 */
	public ImpactFactor(Integer year, Double valueOfImpactFactor, List<ResearchAreaRanking> researchAreas, Double valueOfImpactFactorFiveYears, List<ResearchAreaRanking> researchAreasFiveYears) {
		super();
		this.year = year;
		this.valueOfImpactFactor = valueOfImpactFactor;
		this.researchAreas = researchAreas;
		this.valueOfImpactFactorFiveYears = valueOfImpactFactorFiveYears;
		this.researchAreasFiveYears = researchAreasFiveYears;
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

	public Double getValueOfImpactFactorFiveYears() {
		return valueOfImpactFactorFiveYears;
	}

	public void setValueOfImpactFactorFiveYears(Double valueOfImpactFactorFiveYears) {
		this.valueOfImpactFactorFiveYears = valueOfImpactFactorFiveYears;
	}

	public List<ResearchAreaRanking> getResearchAreasFiveYears() {
		return researchAreasFiveYears;
	}

	public void setResearchAreasFiveYears(List<ResearchAreaRanking> researchAreasFiveYears) {
		this.researchAreasFiveYears = researchAreasFiveYears;
	}

	public String getLeadingInternationalJournalResearchAreas(boolean twoYears, boolean fiveYears) {
		StringBuffer retVal = new StringBuffer();
		if(twoYears){
			for (ResearchAreaRanking researchArea : researchAreas) {
				if(researchArea.getDividend() <= 0.3){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		if(fiveYears){
			for (ResearchAreaRanking researchArea : researchAreasFiveYears) {
				if(researchArea.getDividend() <= 0.3){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		return retVal.toString();
	}
	public String getOutstandingInternationalJournalResearchAreas(boolean twoYears, boolean fiveYears) {
		StringBuffer retVal = new StringBuffer();
		if(twoYears){
			for (ResearchAreaRanking researchArea : researchAreas) {
				if((researchArea.getDividend() > 0.3) && (researchArea.getDividend() <= 0.5)){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		if(fiveYears){
			for (ResearchAreaRanking researchArea : researchAreasFiveYears) {
				if((researchArea.getDividend() > 0.3) && (researchArea.getDividend() <= 0.5)){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		return retVal.toString();
	}
	public String getInternationalJournalResearchAreas(boolean twoYears, boolean fiveYears) {
		StringBuffer retVal = new StringBuffer();
		if(twoYears){
			for (ResearchAreaRanking researchArea : researchAreas) {
				if(researchArea.getDividend() > 0.5){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		if(fiveYears){
			for (ResearchAreaRanking researchArea : researchAreasFiveYears) {
				if(researchArea.getDividend() > 0.5){
					if(retVal.length() > 0)
						retVal.append("; ");
					retVal.append(researchArea.getResearchAreaDTO().toString());
				}
			}
		}
		return retVal.toString();
	}
	
	public ResearchAreaRanking getMaxPositionReseachArea(boolean twoYears, boolean fiveYears){
		ResearchAreaRanking retVal = null;
		if(twoYears){
			for (ResearchAreaRanking researchArea : researchAreas) {
				if(retVal == null)
					retVal = researchArea;
				else if (retVal.getDividend() > researchArea.getDividend())
					retVal = researchArea;
			}
		}
		if(fiveYears){
			for (ResearchAreaRanking researchArea : researchAreasFiveYears) {
				if(retVal == null)
					retVal = researchArea;
				else if (retVal.getDividend() > researchArea.getDividend())
					retVal = researchArea;
			}
		}
		return retVal;
	}

	public String getValueOfImpactFactorForMaxResearchArea(boolean twoYears, boolean fiveYears) {
		ResearchAreaRanking ra = null;
		if(twoYears){
			for (ResearchAreaRanking researchArea : researchAreas) {
				if(valueOfImpactFactorForMaxResearchArea == null) {
					ra = researchArea;
					valueOfImpactFactorForMaxResearchArea = "" + valueOfImpactFactor + " (IF2)";
				}
				else if (ra.getDividend() > researchArea.getDividend()) {
					ra = researchArea;
					valueOfImpactFactorForMaxResearchArea = "" + valueOfImpactFactor + " (IF2)";
				}
			}
		}
		if(fiveYears){
			for (ResearchAreaRanking researchArea : researchAreasFiveYears) {
				if(valueOfImpactFactorForMaxResearchArea == null) {
					ra = researchArea;
					valueOfImpactFactorForMaxResearchArea = "" + valueOfImpactFactorFiveYears + " (IF5)";
				}
				else if (ra.getDividend() > researchArea.getDividend()) {
					ra = researchArea;
					valueOfImpactFactorForMaxResearchArea = "" + valueOfImpactFactorFiveYears + " (IF5)";
				}
			}
		}
		return valueOfImpactFactorForMaxResearchArea;
	}
	
	public String getMaxCategory(boolean twoYears, boolean fiveYears){
		String retVal = null;
		if(getMaxPositionReseachArea(twoYears, fiveYears).getDividend() <= 0.1)
			retVal = "M21a";
		else if(getMaxPositionReseachArea(twoYears, fiveYears).getDividend() <= 0.3)
			retVal = "M21";
		else if(getMaxPositionReseachArea(twoYears, fiveYears).getDividend() <= 0.6)
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

	@Override
	public String toString() {
		return "ImpactFactor{" +
				"year=" + year +
				", valueOfImpactFactor=" + valueOfImpactFactor +
				", researchAreas=" + researchAreas +
				", valueOfImpactFactorFiveYears=" + valueOfImpactFactorFiveYears +
				", researchAreasFiveYears=" + researchAreasFiveYears +
				'}';
	}
}
