package train;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TrainSearch {
	/***
	 * Récupérer tous les id des trains disponibles selon les critères en paramètres
	 * @param idDeparture id de la station de départ
	 * @param idArrival id de la station d'arrivée
	 * @param outboundDateTime Date et heure de départ
	 * @param returnDateTime Date et heure de retour
	 * @param nbTickets nombre de tickets demandé
	 * @param travelClass type de place voulu(First, Business or Standard)
	 * @return
	 */
	public String searchByAllCriterias(int idDeparture, int idArrival, String outboundDateTime, String returnDateTime, int nbTickets, String travelClass) {
		try {
			String res = sendGET("http://localhost:8182/train/filter/" + idDeparture +
					"/" + idArrival + "/" + outboundDateTime + "/" + returnDateTime +
					"/" + nbTickets + "/" + travelClass);
            return res;
		} catch(Exception e) {
			e.printStackTrace();
			return "No list available";
		}
	}
	
	private String sendGET(String get_URL) throws IOException {
		URL obj = new URL(get_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		} else {
			return "GET request did not work.";
		}
	}
	
//	private String sendPOST(String post_URL) throws IOException {
//		
//		// URL tuto : https://www.digitalocean.com/community/tutorials/java-httpurlconnection-example-java-http-request-get-post
//		
//		URL obj = new URL(post_URL);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", "Mozilla/5.0");
//
//		// For POST only - START
//		con.setDoOutput(true);
//		OutputStream os = con.getOutputStream();
//		os.write(POST_PARAMS.getBytes());
//		os.flush();
//		os.close();
//		// For POST only - END
//
//		int responseCode = con.getResponseCode();
//		System.out.println("POST Response Code :: " + responseCode);
//
//		if (responseCode == HttpURLConnection.HTTP_OK) { //success
//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//
//			// print result
//			System.out.println(response.toString());
//		} else {
//			System.out.println("POST request did not work.");
//		}
//	}
}
