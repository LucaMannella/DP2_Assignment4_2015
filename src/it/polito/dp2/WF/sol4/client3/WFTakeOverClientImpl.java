package it.polito.dp2.WF.sol4.client3;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import it.polito.dp2.WF.lab4.ServiceUnavailableException;
import it.polito.dp2.WF.lab4.WFTakeOverClient;
import it.polito.dp2.WF.sol4.gen.ActionAlreadyTaken_Exception;
import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowService;
import it.polito.dp2.WF.sol4.gen.WrongActor;

public class WFTakeOverClientImpl implements WFTakeOverClient {

	private static final String CONTROL_WS_URL = "http://localhost:7070/wfcontrol";
	private static final String INFO_WS_URL = "http://localhost:7071/wfinfo";
	
	public WFTakeOverClientImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean takeOver(String workflowName, String actionName,	String actorName) 
			throws ServiceUnavailableException {
		
		boolean toRet = false;
		
		try {	// creating the URL object
			URL webServiceInfoURL = new URL(INFO_WS_URL);
			URL webServiceControlURL = new URL(CONTROL_WS_URL);
			
			// taking the port (proxy) from the service
			WorkflowService service = new WorkflowService(webServiceInfoURL);
			WorkflowInfoInterface proxyReader = service.getWorkflowInfoPort();
			
			service = new WorkflowService(webServiceControlURL);
			WorkflowControllerInterface proxyController = service.getWorkflowControllerPort();
			
			List<String> wfNames = new LinkedList<String>();
			wfNames.add(workflowName);
			Holder<XMLGregorianCalendar> lastModTime = new Holder<XMLGregorianCalendar>();
			Holder<List<Workflow>> workflows = new Holder<List<Workflow>>();
			Holder<List<it.polito.dp2.WF.sol4.gen.Process>> processes = new Holder<List<it.polito.dp2.WF.sol4.gen.Process>>();
			proxyReader.getProcesses(wfNames, lastModTime, processes, workflows);
			
			Actor actor = new Actor();
			actor.setName(actorName);
//			actor.setRole(arg2);
			
			for( it.polito.dp2.WF.sol4.gen.Process p : processes.value ) {
				String psCode = p.getCode();
				toRet = proxyController.takeOverAction(psCode, actor);
				if(toRet == true)
					break;
			}
		}
		catch (MalformedURLException e) {
			System.err.println("Error! The given URL is not well formed!");
			throw new ServiceUnavailableException("The given URL is not well formed!");
		}
		catch (ActionAlreadyTaken_Exception e) {
			System.err.println("Impossible to take over the action!"
					+ e.getMessage());
			return false;
		}
		catch (UnknownCode e) {
			System.err.println("Impossible to take over the action!"
					+ e.getMessage());
			return false;
		}
		catch (WrongActor e) {
			System.err.println("Impossible to take over the action!"
					+ e.getMessage());
			return false;
		} 
		catch (UnknownNames_Exception e) {
			System.err.println("Impossible to take over the action!"
					+ e.getMessage());
			return false;
		}
		
		if(toRet == false) {
			System.err.println("Impossible to take over the action!");
		}
		return toRet;

	}

}
