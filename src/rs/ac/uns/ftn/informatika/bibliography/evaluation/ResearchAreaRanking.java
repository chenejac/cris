package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import rs.ac.uns.ftn.informatika.bibliography.dto.ResearchAreaDTO;

public class ResearchAreaRanking {
	
	private Double position;
	private Double dividend;
	private ResearchAreaDTO researchAreaDTO;
	
	
	/**
	 * 
	 */
	public ResearchAreaRanking() {
		super();
	}
	
	/**
	 * @param dividend
	 * @param position
	 * @param researchAreaDTO
	 */
	public ResearchAreaRanking(Double position, Double dividend, 
			ResearchAreaDTO researchAreaDTO) {
		super();
		this.position = position;
		this.dividend = dividend;
		this.researchAreaDTO = researchAreaDTO;
	}



	/**
	 * @return the position
	 */
	public Double getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Double position) {
		this.position = position;
	}
	/**
	 * @return the dividend
	 */
	public Double getDividend() {
		return dividend;
	}
	/**
	 * @param dividend the dividend to set
	 */
	public void setDividend(Double dividend) {
		this.dividend = dividend;
	}
	/**
	 * @return the researchAreaDTO
	 */
	public ResearchAreaDTO getResearchAreaDTO() {
		return researchAreaDTO;
	}
	/**
	 * @param researchAreaDTO the researchAreaDTO to set
	 */
	public void setResearchAreaDTO(ResearchAreaDTO researchAreaDTO) {
		this.researchAreaDTO = researchAreaDTO;
	}


}
