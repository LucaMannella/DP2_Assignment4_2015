package it.polito.dp2.WF.sol4.client1;

import java.net.MalformedURLException;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;

public class WorkflowMonitorFactory extends it.polito.dp2.WF.WorkflowMonitorFactory {

	/**
	 * This method creates an instance of my concrete class that implements the WorkflowMonitor interface.
	 */
	@Override
	public WorkflowMonitor newWorkflowMonitor() throws WorkflowMonitorException {
		WorkflowMonitor myMonitor = new ConcreteWorkflowMonitor();
//		try {
//			myMonitor = new ConcreteWorkflowMonitor();
//		} catch (MalformedURLException e) {
//			System.err.println("Error parsing the URL: "+e.getMessage());
//			e.printStackTrace();
//			throw new WorkflowMonitorException("Error parsing the given URL: "+e.getMessage());
//		} catch (WorkflowMonitorException e) {
//			System.err.println("Error: "+e.getMessage());
//			e.printStackTrace();
//			throw e;
//		}
		
		return myMonitor;
	}
	
	//toString() implemented for debugging purposes
	@Override
	public String toString(){
		return "This is a custom WorkflowMonitorFactory implementation for the assignment 4.";
	}
}
