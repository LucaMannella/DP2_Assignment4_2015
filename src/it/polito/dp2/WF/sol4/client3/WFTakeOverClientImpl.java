package it.polito.dp2.WF.sol4.client3;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import it.polito.dp2.WF.lab4.ServiceUnavailableException;
import it.polito.dp2.WF.lab4.WFTakeOverClient;
import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowService;
import it.polito.dp2.WF.sol4.gen.WrongAction;
import it.polito.dp2.WF.sol4.gen.WrongActor;

public class WFTakeOverClientImpl implements WFTakeOverClient {

	private static final String CONTROL_WS_URL = "http://localhost:7070/wfcontrol";
	private static final String INFO_WS_URL = "http://localhost:7071/wfinfo";
	
	public WFTakeOverClientImpl() {	/*empty default constructor */ }

	// The actor does not have a role and we don't have a resource from which we can know them.
	// So I take the role directly from the action and I'll assign them to the Actor.
	@Override
	public boolean takeOver(String workflowName, String actionName,	String actorName) 
			throws ServiceUnavailableException {
		
		boolean toRet = false;
		System.out.println("The actor "+actorName+" wants to take the action "
				+actionName+" in a workflow "+workflowName);
		
		try {	// creating the URL object
			URL webServiceInfoURL = new URL(INFO_WS_URL);
			URL webServiceControlURL = new URL(CONTROL_WS_URL);
			
				// taking the port (proxy) from the service
			WorkflowInfoInterface proxyReader = new WorkflowService(webServiceInfoURL).getWorkflowInfoPort();
			WorkflowControllerInterface proxyController = new WorkflowService(webServiceControlURL).getWorkflowControllerPort();
			
				// preparing the workflow name
			List<String> wfNames = new LinkedList<String>();
			wfNames.add(workflowName);
				// preparing the return values
			Holder<XMLGregorianCalendar> lastModTime = new Holder<XMLGregorianCalendar>();
			Holder<List<Workflow>> workflowHolder = new Holder<List<Workflow>>();
			Holder<List<Process>> processHolder = new Holder<List<Process>>();
				// get the information about processes
			proxyReader.getProcesses(wfNames, lastModTime, processHolder, workflowHolder);
			
			System.out.println("There are "+processHolder.value.size()+" available processes...");
			
			for( Process p : processHolder.value) {
				for( ActionStatusType as : p.getActionStatus()) {
					Object o = as.getAction();
					if( o instanceof ActionType ) {
						ActionType action = (ActionType) o;
							// this is a possible action
						if( action.getName().equals(actionName) ){
							Actor actor = new Actor();
							actor.setName(actorName);
							actor.setRole(action.getRole());
							
							toRet = proxyController.takeOverAction(p.getCode(), actionName, actor);
							if(toRet == false)
								System.out.println("Impossible to take over the action in the process "+p.getCode());
							else
								return true;
						}
					}
					else {
						System.out.println("Error! The Object is not an ActionStatusType!");
					}
				}
			}
		}
		catch (MalformedURLException e) {
			System.out.println("Error! The given URL is not well formed!");
			throw new ServiceUnavailableException("The given URL is not well formed!");
		}
		catch (WrongAction e) {
			System.out.println("Impossible to take over the action!\n"
					+ e.getMessage());
			return false;
		}
		catch (UnknownCode e) {
			System.out.println("Impossible to take over the action!\n"
					+ e.getMessage());
			return false;
		}
		catch (WrongActor e) {
			System.out.println("Impossible to take over the action!\n"
					+ e.getMessage());
			return false;
		} 
		catch (UnknownNames_Exception e) {
			System.out.println("Impossible to take over the action!\n"
					+ e.getMessage());
			return false;
		}

		return toRet;
	}

}
