package org.komparator.supplier.ws.cli;

/** Main class that starts the Supplier Web Service client. */
public class SupplierClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + SupplierClientApp.class.getName() + " wsURL");
			return;
		}

		String wsURL = args[0];
		String wsName = args[1];
		String uddiURL = args[2];	

		// Create client
		System.out.printf("Creating client for server at %s%n", wsURL);
		SupplierClient client = new SupplierClient(wsURL);
		if (args.length >= 2) {
			client.setWsName(wsName);
			client.setUddiURL(uddiURL);
		}

		// the following remote invocations are just basic examples
		// the actual tests are made using JUnit

		System.out.println("Invoke ping()...");
		String result = client.ping("client");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
