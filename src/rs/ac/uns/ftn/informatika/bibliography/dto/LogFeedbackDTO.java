/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Dragan Ivanovic
 *
 */
public class LogFeedbackDTO extends LogDTO {

	private long miliseconds = 0l;
	private String userId;
	private String searchingMode;
	private int position;
	private double relevanceScore;
	private double recommendationScore;
	private double mixedScore;
	private String feedbackType;
	private String evaluation;
	
	/**
	 * 
	 */
	public LogFeedbackDTO() {
		super();
	}

	/**
	 * @return the miliseconds
	 */
	public long getMiliseconds() {
		return miliseconds;
	}

	/**
	 * @param miliseconds the miliseconds to set
	 */
	public void setMiliseconds(long miliseconds) {
		this.miliseconds = miliseconds;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the searchingMode
	 */
	public String getSearchingMode() {
		return searchingMode;
	}

	/**
	 * @param searchingMode the searchingMode to set
	 */
	public void setSearchingMode(String searchingMode) {
		this.searchingMode = searchingMode;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the relevanceScore
	 */
	public double getRelevanceScore() {
		return relevanceScore;
	}

	/**
	 * @param relevanceScore the relevanceScore to set
	 */
	public void setRelevanceScore(double relevanceScore) {
		this.relevanceScore = relevanceScore;
	}

	/**
	 * @return the recommendationScore
	 */
	public double getRecommendationScore() {
		return recommendationScore;
	}

	/**
	 * @param recommendationScore the recommendationScore to set
	 */
	public void setRecommendationScore(double recommendationScore) {
		this.recommendationScore = recommendationScore;
	}

	/**
	 * @return the mixedScore
	 */
	public double getMixedScore() {
		return mixedScore;
	}

	/**
	 * @param mixedScore the mixedScore to set
	 */
	public void setMixedScore(double mixedScore) {
		this.mixedScore = mixedScore;
	}

	/**
	 * @return the feedbackType
	 */
	public String getFeedbackType() {
		return feedbackType;
	}

	/**
	 * @param feedbackType the feedbackType to set
	 */
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	/**
	 * @return the evaluation
	 */
	public String getEvaluation() {
		return evaluation;
	}

	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
//	@Override
//	public String toString() {
//		return "method: " + getmethod + 
//				" controlNumber: " + controlNumber + 
//				" day: " + day +
//				" month: " + month +
//				" year: " + year +
//				" hour: " + hour +
//				" minute: " + minute +
//				" seconds: " + seconds +
//				" dayOfWeek: " + dayOfWeek +
//				" fileId: " + fileId + 
//				" fileName: " + fileName + 
//				" source: " + source + 
//				" referrer: " + referrer +
//				" ip address: " + ipaddress + 
//				" proxy: " + proxy + 
//				" record: " + recordString; 
//				
//				
//	}
	
	
	
}
