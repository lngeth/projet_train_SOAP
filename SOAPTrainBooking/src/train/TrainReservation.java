package train;

import utils.HttpCall;

import java.util.Iterator;

import org.json.JSONObject;

public class TrainReservation {
	public String reserveTrain(int[] idClient, int[] flex, String[] travelClass, int[] idVoyage, int nbTickets) {
		try {
			String res = HttpCall.sendGET("http://localhost:8080/REST_TrainFiltering/train/billet/reserve/" +
		idClient + '/' + flex + '/' + travelClass + '/' + idVoyage);
			JSONObject json = new JSONObject(res);
			res = "";
			
			if (json.getString("status").equals("500")) {
				res = "Bad SQL Request"; 
			} else {
				if (json.getBoolean("success"))
					res = "Successful reservation";
				else
					res = "Reservation error on the train";
			}
			
			return res;
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
