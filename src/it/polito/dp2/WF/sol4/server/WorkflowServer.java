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

	public static void main(String[] args) {
		try {
			WorkflowMonitor wfMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();
			
			WorkflowDataManager manager = new WorkflowDataManager(wfMonitor);

			// --- Part 2 --- //
			Endpoint wfControl = Endpoint.create(new WorkflowController(manager));
			wfControl.setExecutor(Executors.newFixedThreadPool(MAX_THREADS));
			wfControl.publish(wfControlURL);
			System.out.println("The service was published at: "+wfControlURL);
				
			Endpoint wfInfo = Endpoint.create(new WorkflowInformation(manager));
			wfInfo.setExecutor(Executors.newFixedThreadPool(MAX_THREADS));
			wfInfo.publish(wfInfoURL);
			System.out.println("The service was published at: "+wfInfoURL);
		}
		catch (FactoryConfigurationError e) {
			System.err.println("Configuration Error! Impossible to create the WorkflowMonitorFactory!");
			e.printStackTrace();
		}
		catch (WorkflowMonitorException e) {
			System.err.println("Error! Impossible to create the WorkflowMonitor!");
			e.printStackTrace();
		}
		catch(SecurityException | IllegalStateException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}