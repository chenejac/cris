package rs.ac.uns.ftn.informatika.bibliography.utils;

/**
 * Utility class to sanitize/desanitize strings that are going to be serialized and be part of full format string.
 * 
 *  @author chenejac@uns.ac.rs
 */
public class Sanitizer {
	
	/**
	 * Sanitize input string.
	 * 
	 * @param text	Input text to sanitize
	 * @return		Sanitized input text
	 */
	 public static String sanitize(String text)
	{
		String result = text;
		
		// If text contains backslash characters, make them literal 
		// (by making pairs of backslashes):
		result = result.replace("\\", "\\\\");

		// If text contains double quotes, make them literal chars by escaping them with backslash: 
		result = result.replace("\"", "\\\"");
		
		// If text contains commas, double quotes or backslashes, it must be enclosed into double 
		// quotes:
		if (result.contains(",") || result.contains("-") || result.contains("(") || result.contains(")") ||result.contains("\"") || result.contains("\\"))
			result = '"' + result + '"';
		
		return result;
	}
	 
	 /**
		 * Sanitize input string for CSV.
		 * 
		 * @param text	Input text to sanitize
		 * @return		Sanitized input text
		 */
		 public static String sanitizeCSV(String text)
		{
			String result = text;
			
			if(result == null)
				return null;
			
			
			// If text contains commas, double quotes or backslashes, it must be enclosed into double 
			// quotes:
			if (result.contains(",")){
				// If text contains double quotes, make them literal chars by escaping them with backslash: 
				result = result.replace("\"", "\"\"");
				result = '"' + result + '"';
			}
			
			if (result.contains("\n")){
				result = result.replace("\n", " ");
			}
			
			if (result.contains("\r")){
				result = result.replace("\r", " ");
			}
			
			return result;
		}
	
	/**
	 * Desanitize input string - convert it from sanitized form into normal text.
	 * 
	 * @param text	Input text to desanitize
	 * @return		Desanitized input text
	 */
	public static String desanitize(String text)
	{
		// First, trim all unnecessary whitespaces:
		String result = text.trim();
		
		// If text is enclosed into double quotes, remove those:
		if (result.startsWith("\"") && result.endsWith("\""))
		{
			// Turn back escape sequences for double quote character:
			result = result.replace("\\\"", "\"");

			// Turn back escape sequnces for backslash character:
			result = result.replace("\\\\", "\\");
			
			result = result.substring(1, result.length() - 1);
		}
		
		return result;
	}
	
	/**
	 * Extracts first parameter from string with all parameters separated with separator
	 * 
	 * @param parameters 
	 * 			String with all parameters separated with separator
	 * @return first parameter
	 */
	public static String nextPar(String parameters, char separator){
		parameters = parameters.trim();
		if(parameters.startsWith("\"")){
			parameters = parameters.substring(1);
			String temp = parameters;
			int index = 0;
			do{
				index += temp.indexOf("\"");
				if((temp.indexOf("\"") == 0) || (temp.charAt(temp.indexOf("\"")-1) != '\\')){
					break;
				}
				else {	
					temp = temp.substring(parameters.indexOf("\"")+1);
				}
			}while(true);
			return Sanitizer.desanitize('"'+parameters.substring(0, index).trim() + '"');
		} else if (parameters.indexOf(separator) != -1){
			return parameters.substring(0, parameters.indexOf(separator)).trim();
		} else {
			return parameters;
		}
	}
	
	/**
	 * Removes first parameter from string with all parameters separated with separator
	 * 
	 * @param parameters 
	 * 			String with all parameters separated with separator
	 * @return parameters without first parameter
	 */
	public static String remPar(String parameters, char separator){
		parameters = parameters.trim();
		if(parameters.startsWith("\"")){
			parameters = parameters.substring(1);
			String temp = parameters;
			int index = 0;
			do{
				index += temp.indexOf("\"");
				if((temp.indexOf("\"") == 0) || (temp.charAt(temp.indexOf("\"")-1) != '\\')){
					break;
				}
				else {	
					temp = temp.substring(parameters.indexOf("\"")+1);
				}
			}while(true);
			if((index < (parameters.length()-1)) && (parameters.charAt(index + 1) == separator))
				index++;
			return parameters.substring(index+1).trim();
		} else if (parameters.indexOf(separator) != -1){
			return parameters.substring(parameters.indexOf(separator)+1).trim();
		} else 
			return "";
	}
}
