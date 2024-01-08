package station;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.HttpCall;

public class StationSearch {
	public String getAllStations() {
		try {
			String res = HttpCall.sendGET("http://localhost:8080/REST_TrainFiltering/station/all");
			
			String resultToReturn = "";
			JSONObject json = new JSONObject(res);
			if (json.getString("status").equals("500")) {
				resultToReturn = "No train station";
			} else {
				JSONArray listTrain = json.getJSONArray("listStation");
				if (listTrain.length() == 0) {
					resultToReturn = "No train station";
				} else {
					resultToReturn = listTrain.toString();
				}
			}
			
            return resultToReturn;
			
		} catch(Exception e) {
			e.printStackTrace();
			return "No stations available";
		}
	}
}
