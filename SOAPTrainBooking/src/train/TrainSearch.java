package train;

import utils.HttpCall;

public class TrainSearch {
	/***
	 * R�cup�rer tous les id des trains disponibles selon les crit�res en param�tres
	 * @param idDeparture id de la station de d�part
	 * @param idArrival id de la station d'arriv�e
	 * @param outboundDateTime Date et heure de d�part
	 * @param returnDateTime Date et heure de retour
	 * @param nbTickets nombre de tickets demand�
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
