package org.komparator.supplier.ws;


import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;


/** Main class that starts the Supplier Web Service. */
public class SupplierApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + SupplierApp.class.getName() + " wsURL");
			return;
		}
		String wsURL = args[0];
		String wsName = args[1];
		String uddiURL = args[2];
		
		// Create server implementation object
		UDDINaming uddiNaming = null;
		SupplierEndpointManager endpoint = new SupplierEndpointManager(wsURL);
		endpoint.setWsName(wsName);
		System.out.printf("Publishing '%s' to UDDI at %s%n", wsName, uddiURL);
		uddiNaming = new UDDINaming(uddiURL);
		uddiNaming.rebind(wsName, wsURL);
		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
			uddiNaming.unbind(wsName);
			uddiNaming.disconnect();
		}

	}

}
