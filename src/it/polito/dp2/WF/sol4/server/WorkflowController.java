package it.polito.dp2.WF.sol4.server;

import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.UnknownActionName;
import it.polito.dp2.WF.sol4.gen.UnknownActorName;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNextActionName;
import it.polito.dp2.WF.sol4.gen.UnknownWorkflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;

@WebService(name = "WorkflowControllerInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowControllerPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface")
@XmlSeeAlso({ObjectFactory.class})
public class WorkflowController implements WorkflowControllerInterface {
	
	private WorkflowDataManager manager;
	private ObjectFactory objFactory;
	
	private static Logger log = Logger.getLogger(WorkflowController.class.getName());
	
	public WorkflowController(WorkflowDataManager wfManager) {		
		this.manager = wfManager;
		this.objFactory = new ObjectFactory();
	}

	@WebMethod
	@Override
	public String createNewProcess (String wfName) throws UnknownWorkflow{		//TODO: Implement me
		log.entering(log.getName(), "createNewProcess");
		
		String psCode = manager.createNewProcess(wfName);
		
		log.exiting(log.getName(), "createNewProcess");
		return psCode;
	}

	@WebMethod
	@Override
	public boolean takeOverAction(String psCode, Actor actor)
			throws UnknownActorName, UnknownCode {
		log.entering(log.getName(), "takeOverAction");
		
		// ---	This method is not required by the specifications.	--- //
		
		log.exiting(log.getName(), "takeOverAction");
		return false;
	}

	@WebMethod
	@Override
	public boolean completeAction(String actionStatusName, String nextActionName)
			throws UnknownActionName, UnknownNextActionName {
		log.entering(log.getName(), "completeAction");
		
		// ---	This method is not required by the specifications.	--- //
		boolean toRet = manager.completeAction(actionStatusName, nextActionName);
		
		log.exiting(log.getName(), "completeAction");
		return toRet;
	}

}