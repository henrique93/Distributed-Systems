package org.komparator.mediator.domain;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.komparator.mediator.ws.MediatorEndpointManager;
import org.komparator.mediator.ws.MediatorPortImpl;
import org.komparator.mediator.ws.cli.MediatorClient;

public class LifeProof extends TimerTask {

    private MediatorEndpointManager endpointManager;
    
    private Timer timer;

    public LifeProof(MediatorEndpointManager endpointManager, Timer timer) {
        this.endpointManager = endpointManager;
        this.timer = timer;
    }

    @Override
    public void run() {
        System.out.println(this.getClass() + " running...");
       
        if (endpointManager.getWsURL().equals("http://localhost:8071/mediator-ws/endpoint")) {
        	try {
				MediatorClient mc = new MediatorClient("http://localhost:8072/mediator-ws/endpoint");
				mc.imAlive();
				
			} catch (Exception e) {
				System.out.println("Secondary Mediator is not available");
			}
    	}
        else {
        	MediatorPortImpl mpi = endpointManager.getPort();
        	Date oldDate = mpi.getDate();
        	Date currentDate = new Date();
        	long seconds = (currentDate.getTime()-oldDate.getTime())/1000;
        	if(seconds > 5) {
				try {
					timer.cancel();
					timer.purge();
					endpointManager.publishToUDDI();
					endpointManager.awaitConnections();
				} catch (Exception e) {
					System.out.println("Could not publish");
				}
			}
        }
        

    } 
}


