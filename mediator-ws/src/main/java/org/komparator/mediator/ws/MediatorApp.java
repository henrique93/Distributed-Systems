package org.komparator.mediator.ws;

import java.util.Timer;

import org.komparator.mediator.domain.LifeProof;


public class MediatorApp {
	

	public static void main(String[] args) throws Exception {

		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + MediatorApp.class.getName() + " wsURL OR uddiURL wsName wsURL");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;

		// Create server implementation object, according to options
		MediatorEndpointManager endpoint = null;
		if (args.length == 1) {
			wsURL = args[0];
			endpoint = new MediatorEndpointManager(wsURL);
		} else if (args.length >= 3) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			endpoint = new MediatorEndpointManager(uddiURL, wsName, wsURL);
			endpoint.setVerbose(true);
		}
		
		boolean start = true;
		if (wsURL.equals("http://localhost:8071/mediator-ws/endpoint")) {
			System.out.println("I am the Primary Server");
		}
		else {
			System.out.println("I am the Secondary Server");
			start = false;	
		}
		Timer timer = new Timer(true);
		LifeProof myLifeProof = new LifeProof(endpoint, timer);
        timer.schedule(myLifeProof, /*delay*/ 0 * 1000, /*period*/ 5 * 1000);
		try {
			endpoint.start(start);
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}
		

	}

}
