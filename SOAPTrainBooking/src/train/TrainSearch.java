package train;

import utils.HttpCall;

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
			String res = HttpCall.sendGET("http://localhost:8182/train/filter/" + idDeparture +
					"/" + idArrival + "/" + outboundDateTime + "/" + returnDateTime +
					"/" + nbTickets + "/" + travelClass);
            return res;
		} catch(Exception e) {
			e.printStackTrace();
			return "No list available";
		}
	}
}
