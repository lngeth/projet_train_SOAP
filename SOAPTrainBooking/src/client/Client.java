package client;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.HttpCall;

public class Client {
	public String getClientIdByName(String name) {
		try {
			String res = HttpCall.sendGET("http://localhost:8080/REST_TrainFiltering/client/get/" + name);
			
			String resultToReturn = "";
			JSONObject json = new JSONObject(res);
			if (json.getString("status").equals("500")) {
				resultToReturn = "Erreur requête";
			} else {
				resultToReturn = json.getString("idClient");
			}
			
            return resultToReturn;
			
		} catch(Exception e) {
			e.printStackTrace();
			return "No client available";
		}
	}
	
	public String createClient(String name) {
		try {
			String param_post = "name=" + name;
			
			String res = HttpCall.sendPOST("http://localhost:8080/REST_TrainFiltering/client/create", param_post);
			
			String resultToReturn = "";
			JSONObject json = new JSONObject(res);
			if (json.getString("status").equals("500")) {
				resultToReturn = "Erreur requête";
			} else {
				resultToReturn = String.valueOf(json.getBoolean("success"));
			}
			
            return resultToReturn;
			
		} catch(Exception e) {
			e.printStackTrace();
			return "No client created";
		}
	}
}
