package main;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

public class RESTDistributor extends ServerResource {
	public static void main(String[] args) throws Exception
	{
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8182);
		component.getDefaultHost().attach(new RouterApplication());
		component.start();
	}
}
