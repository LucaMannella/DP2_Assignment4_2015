package it.polito.dp2.WF.sol4.server;

import it.polito.dp2.WF.FactoryConfigurationError;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;

import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

public class WorkflowServer {
	
	public final static String wfControlURL = "http://localhost:7070/wfcontrol";
	public final static String wfInfoURL = "http://localhost:7071/wfinfo";
	
	public final static int MAX_THREADS = 7;

	public static void main(String[] args) {	// TODO Auto-generated method stub
		
		WorkflowMonitor wfMonitor = null;
		try {
			wfMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();
		}
		catch (FactoryConfigurationError e) {
			System.err.println("Configuration Error! Impossible to create the WorkflowMonitorFactory!");
			e.printStackTrace();
			System.exit(-1);
		}
		catch (WorkflowMonitorException e) {
			System.err.println("Error! Impossible to create the WorkflowMonitor!");
			e.printStackTrace();
			System.exit(-11);
		}
		
		Endpoint wfControl = Endpoint.create(new WorkflowController());
		wfControl.setExecutor(Executors.newFixedThreadPool(MAX_THREADS));
		wfControl.publish(wfControlURL);
		
		Endpoint wfInfo = Endpoint.create(new WorkflowInformation());
		wfInfo.setExecutor(Executors.newFixedThreadPool(MAX_THREADS));
		wfInfo.publish(wfInfoURL);
		
	}

}
