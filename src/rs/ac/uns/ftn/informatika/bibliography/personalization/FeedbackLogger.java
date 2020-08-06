package rs.ac.uns.ftn.informatika.bibliography.personalization;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeedbackLogger {
	
	private static Log logRecommendationEvaluation = LogFactory.getLog("rs.recommendation.evaluation");
	
	public static void logRecommendationEvaluation(String userId, String controlNumber, String searchingMode, int position, float relevanceScore, float recommendationScore, float mixedScore, String type, String evaluation){
		Date date = new Date();
		
		logRecommendationEvaluation.info("Date and time: " + date + "| miliseconds: " + date.getTime() + "| userId: " + userId +
				"| controlNumber: " + controlNumber + "| searching mode: " + searchingMode + "| position: " + position + 
				"| relevance score: " + relevanceScore + "| recommendation score: " + recommendationScore + 
				"| mixed score: " + mixedScore + "| feedback type: " + type + "| evaluation: " + evaluation);
	}

}
