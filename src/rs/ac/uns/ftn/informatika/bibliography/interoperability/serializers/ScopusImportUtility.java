package rs.ac.uns.ftn.informatika.bibliography.interoperability.serializers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ScopusImportUtility {

    public static Map<String, String> headers = new HashMap<String, String>();

    private static boolean authenticated = false;


    public static boolean authenticate() throws JSONException {
        if (authenticated) {
            return true;
        }
        String url = "https://api.elsevier.com/authenticate?platform=SCOPUS";
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url)
                    .headers(headers)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (jsonResponse.getStatus() == 300) {
            JSONObject jo;
            String idString = "";
            try {
                jo = new JSONObject(jsonResponse.getBody().toString());
                idString = jo.getJSONObject("authenticate-response").getJSONObject("pathChoices").getJSONArray("choice").getJSONObject(1).get("@id").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int choice = Integer.parseInt(idString);
            boolean success = getAuthtoken(choice, headers);
            if (success) {
                authenticated = true;
            }
        } else if (jsonResponse.getStatus() == 200) {
            authenticated = true;
        } else {
            System.out.println(jsonResponse.getStatus());
        }
        return authenticated;
    }



    public static boolean getAuthtoken(int choice, Map headers) throws JSONException {
        String url = "https://api.elsevier.com/authenticate?platform=SCOPUS&choice=" + choice;
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url)
                    .headers(headers)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (jsonResponse.getStatus() == 200) {
            JSONObject jo = new JSONObject(jsonResponse.getBody().toString());
            String authtoken = jo.getJSONObject("authenticate-response").getString("authtoken");
            headers.put("X-ELS-Authtoken", authtoken);
            return true;
        } else {
            System.out.println(jsonResponse.getStatus());
        }
        return false;
    }


    public static List<JSONObject> getDocumentsByAuthor(String authorID, Integer startYear, Integer endYear) {
        List<JSONObject> retVal = new ArrayList<JSONObject>();
    	if(authenticate()){
    		for(int i = startYear; i<=endYear; i++){
	    		String url = "https://api.elsevier.com/content/search/scopus?query=AU-ID("+ authorID +")&count=25&date="+i+"&view=COMPLETE";
		        JSONObject obj = getDocumentsByQuery(url);
		        if(obj != null){
		        	retVal.add(obj);
		        	int numberOfDocumentsInYear = Integer.parseInt(obj.getJSONObject("search-results").get("opensearch:totalResults").toString());

	                for (int j=25; j<numberOfDocumentsInYear; j+=25) {
	                    String urlYear = "https://api.elsevier.com/content/search/scopus?query=AU-ID("+ authorID +")&start="+ j +"&count=25&date=" + i+"&view=COMPLETE";
	                    JSONObject objYear = getDocumentsByQuery(urlYear);
	                    if(objYear != null)
	                    	retVal.add(objYear);
	                }
		        }
		        //System.out.println(retVal.size());
    		}
    	} 
    	return retVal;
    }
    
    public static JSONObject getAbstractData(String scopusId) {
        JSONObject retVal = null;
    	if(authenticate()){
    		String url = "https://api.elsevier.com/content/abstract/scopus_id/"+ scopusId +"?view=FULL";
	        JSONObject obj = getDocumentsByQuery(url);
	        if(obj != null){
	        	retVal = obj;
	        }
    	} 
    	return retVal;
    }
    
    private static JSONObject getDocumentsByQuery(String query) {
        JSONObject retVal = null;
    	HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(query)
                    .headers(headers)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response.getStatus() == 200) {
        	retVal = new JSONObject(response.getBody().toString());
        } else {
        	log.error(response.getStatus());
        }
    	return retVal;
    }

    private static Log log = LogFactory.getLog(ScopusImportUtility.class.getName());

}
