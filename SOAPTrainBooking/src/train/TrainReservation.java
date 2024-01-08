package train;

import utils.HttpCall;

import org.json.JSONObject;

public class TrainReservation {
	public String reserveTrain(int[] idClient, int[] flex, String[] travelClass, int[] idVoyage, int nbTickets) {
		try {
			String param_post = "";
			
			for (int id : idClient) {
				param_post += "idClient=" + Integer.toString(id) + "&";
			}
			for (int f : flex) {
				param_post += "flex=" + Integer.toString(f) + "&";
			}
			for (String t : travelClass) {
				param_post += "travelClass=" + t + "&";
			}
			for (int id : idVoyage) {
				param_post += "idVoyage=" + Integer.toString(id) + "&";
			}
			param_post += "nbTickets=" + nbTickets;
			System.out.println("parampost :" + param_post);
			
			String res = HttpCall.sendPOST("http://localhost:8080/REST_TrainFiltering/train/billet/reserve", param_post);
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
