package it.polito.dp2.WF.sol4.client2;

import java.net.MalformedURLException;
import java.net.URL;

import it.polito.dp2.WF.sol4.gen.UnknownWorkflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowService;

public class WFControlClient {
	
	private static final String DEFAUL_WS_URL = "http://localhost:7070/wfcontrol";
	
	//return values legend:
	// 0 --> success
	// 1 --> error during the execution
	// 2 --> general error (service unreachable)
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Error! Usage: <program_name> <URL> <Workflow_Name>\n");
			System.exit(2);
		}

		// taking the parameters
		String webServiceURLString = args[0];
		String wfName = args[1];
		
		// checking the URL of the Web Service
		if(webServiceURLString == null || webServiceURLString.equals("")) {
			System.out.println("No URL was set. Will be used the default one: "+DEFAUL_WS_URL);
			webServiceURLString = DEFAUL_WS_URL;
		}
		
		try {	// creating the URL object
			URL webServiceURL = new URL(webServiceURLString);
			
			// taking the port (proxy) from the service
			WorkflowService service = new WorkflowService(webServiceURL);
			WorkflowControllerInterface proxyController = service.getWorkflowControllerPort();
			String psCode = proxyController.createNewProcess(wfName);
			if(psCode == null) {
				System.err.println("An error occurs during the creation of the Process!");
				System.exit(1);
			}
		}
		catch (MalformedURLException e) {
			System.err.println("Error! The given URL is not well formed!");
			System.exit(2);
		}
		catch (UnknownWorkflow e) {
			System.err.println("An error occurs during the creation of the Process! \n"
					+ e.getMessage());
			System.exit(1);
		}
		
		System.exit(0);	//everything all right!
	}

}
