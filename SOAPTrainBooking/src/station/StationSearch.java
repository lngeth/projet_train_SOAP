package station;

import utils.HttpCall;

public class StationSearch {
	public String getAllStations() {
		try {
			String res = HttpCall.sendGET("http://localhost:8182/station/all");
			return res;
		} catch(Exception e) {
			e.printStackTrace();
			return "No stations available";
		}
	}
}
