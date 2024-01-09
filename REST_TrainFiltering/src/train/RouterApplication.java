package train;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import user.CreateClient;
import user.GetClient;

public class RouterApplication extends Application {
	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		// Create a router Restlet that routes each call to a new respective instance of resource.
		Router router = new Router(getContext());
		// Defines only two routes
		router.attach("/train/filter/{idDeparture}/{idArrival}/{outboundDateTime}/{returnDateTime}/{nbTickets}/{travelClass}", TrainFiltering.class);
		router.attach("/station/all", Stations.class);
		router.attach("/train/billet/reserve", TrainReservation.class);
		router.attach("/client/get/{name}", GetClient.class);
		router.attach("/client/create", CreateClient.class);
		
		return router;
	}
}
