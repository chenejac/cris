package rs.ac.uns.ftn.informatika.bibliography.evaluation.kobson;

import java.util.ArrayList;

import org.w3c.dom.Node;
/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
public class Funkcije {

	public static boolean checkInteger(String intValue){
		try {
				Integer.parseInt(intValue);
			}
		catch (NumberFormatException e) {
				return false;
		}
		return true;
	}
	
	public static boolean checkFloat(String floatValue){
		try {
				Float.parseFloat(floatValue);
			}
		catch (NumberFormatException e) {
				return false;
		}
		return true;
	}
	
	public static String [] parseVersionchange(String versionchange){
		String[] retVal = null;
		String[] temp = versionchange.split(";");
		ArrayList<String> tempLista = new ArrayList<String>();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("NEW") || temp[i].equals("NASLOV") || temp[i].equals("SKRACENINASLOV") 
					|| temp[i].equals("JEZIK") || temp[i].equals("URL") || temp[i].equals("IMF")) {
				tempLista.add(temp[i]);
			}
		}
		if (tempLista.isEmpty()) {
			return null;
		}
		retVal = new String[tempLista.size()];
		
		for (int i = 0; i < tempLista.size(); i++) {
			retVal[i] = tempLista.get(i);
		}
		
		return retVal;
	}
	
	public static String getLanguageFromKobson(String jezik)
	{
		if(jezik==null)
			return null;
		
		if(jezik.trim().equalsIgnoreCase(""))
			return null;
		
		String tokens [] = jezik.split(";");

		for(int i=0; i<tokens.length;i++)
		{
			String token = tokens[i].toLowerCase();			
			String detektovaniJezik = getLanguageFromString(token);
			
			if(token.contains("text")){
				return detektovaniJezik;
			}
		}
		
		return null;
	}
	
	public static String getLanguageFromString(String jezik)
	{
		//detektujemo prvi jezik po nasim prioritetima
		
		if(jezik.contains("engleski"))
			return "eng";
		if(jezik.contains("english"))
			return "eng";
		if(jezik.contains("croatian"))
			return "srp";
		else if(jezik.contains("serbo-croatian"))
			return "srp";
		if(jezik.contains("serbian"))
			return "srp";
		if(jezik.contains("srpski"))
			return "srp";
		if(jezik.contains("yugoslavian"))
			return "srp";
		if(jezik.contains("french"))
			return "fre";
		if(jezik.contains("german"))
			return "ger";
		if(jezik.contains("russian"))
			return "rus";	
		if(jezik.contains("italian"))
			return "ita";
		if(jezik.contains("spanish"))
			return "spa";
		if(jezik.contains("hungarian"))
			return "hun";
		if(jezik.contains("slovenian"))
			return "slo";

		return "eng";
	}
	
	public static Node getNextNode(Node node){
		Node siblingNode = node.getNextSibling();
		while (!siblingNode.getNodeName().equalsIgnoreCase("TD")) 
			siblingNode = siblingNode.getNextSibling();
		
		return siblingNode;
	}
    
	public static String getNextNodeContent(Node node){
		Node siblingNode = node.getNextSibling();
		while (!siblingNode.getNodeName().equalsIgnoreCase("TD")) 
			siblingNode = siblingNode.getNextSibling();
		
		return siblingNode.getTextContent().trim();
	}
}
