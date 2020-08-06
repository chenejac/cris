package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;


/**
 * 
 * @author chenejac@uns.ac.rs
 */
@SuppressWarnings("serial")
public class ResultMeasureDTO implements Serializable {

	protected Double quantitativeMeasure;
	
	protected ResultTypeDTO resultType;
	
	protected ScienceAreaDTO scienceArea;
	
	protected CommissionDTO commissionDTO;
	
	
	public ResultMeasureDTO() {
		super();
	}


	/**
	 * @param resultType
	 * @param scienceArea
	 * @param quantitativeMeasure
	 */
	public ResultMeasureDTO(ResultTypeDTO resultType,
			CommissionDTO commissionDTO, ScienceAreaDTO scienceArea, Double quantitativeMeasure) {
		super();
		this.resultType = resultType;
		this.scienceArea = scienceArea;
		this.quantitativeMeasure = quantitativeMeasure;
		this.commissionDTO = commissionDTO;
	}


	/**
	 * @return the quantitativeMeasure
	 */
	public Double getQuantitativeMeasure() {
		return quantitativeMeasure;
	}


	/**
	 * @param quantitativeMeasure the quantitativeMeasure to set
	 */
	public void setQuantitativeMeasure(Double quantitativeMeasure) {
		this.quantitativeMeasure = quantitativeMeasure;
	}


	/**
	 * @return the resultType
	 */
	public ResultTypeDTO getResultType() {
		return resultType;
	}


	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(ResultTypeDTO resultType) {
		this.resultType = resultType;
	}


	/**
	 * @return the scienceArea
	 */
	public ScienceAreaDTO getScienceArea() {
		return scienceArea;
	}


	/**
	 * @param scienceArea the scienceArea to set
	 */
	public void setScienceArea(ScienceAreaDTO scienceArea) {
		this.scienceArea = scienceArea;
	}


	/**
	 * @return the commissionDTO
	 */
	public CommissionDTO getCommissionDTO() {
		return commissionDTO;
	}


	/**
	 * @param commissionDTO the commissionDTO to set
	 */
	public void setCommissionDTO(CommissionDTO commissionDTO) {
		this.commissionDTO = commissionDTO;
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		boolean retVal = false;
		try{
			ResultMeasureDTO rm = (ResultMeasureDTO) arg0;
			retVal = this.getResultType().equals(rm.getResultType());
		} catch (Exception ex){
			
		}
		return retVal;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.resultType.classId.hashCode();
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return resultType.toString() + " ( " + quantitativeMeasure + "p. )";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toStringWithScienceArea() {
		return resultType.toString() + " ( " + quantitativeMeasure + "p. ) in "+scienceArea;
	}
	
}
