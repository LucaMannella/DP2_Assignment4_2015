package it.polito.dp2.WF.sol4.server;

import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import it.polito.dp2.WF.sol4.gen.ActionAlreadyFinished_Exception;
import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.UnknownActionName;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNextActionName;
import it.polito.dp2.WF.sol4.gen.UnknownWorkflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;
import it.polito.dp2.WF.sol4.gen.WrongAction;
import it.polito.dp2.WF.sol4.gen.WrongActor;

@WebService(name = "WorkflowControllerInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowControllerPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface")
@XmlSeeAlso({ObjectFactory.class})
public class WorkflowController implements WorkflowControllerInterface {
	
	private WorkflowDataManager manager;
	
	private static Logger log = Logger.getLogger(WorkflowController.class.getName());
	
	public WorkflowController(WorkflowDataManager wfManager) {		
		this.manager = wfManager;
	}

	@WebMethod
	@Override
	public String createNewProcess (String wfName) throws UnknownWorkflow {
		log.entering(log.getName(), "createNewProcess");
		
		String psCode = manager.createNewProcess(wfName);
		
		log.exiting(log.getName(), "createNewProcess");
		return psCode;
	}

	@WebMethod
	@Override
	public boolean takeOverAction(String processCode, String actionName, Actor actor)
			throws UnknownCode, WrongAction, WrongActor {
		log.entering(log.getName(), "takeOverAction");
		
		// ---	This method is not required by the Assignment 4	--- //
		// ---	It is required in the exam example				--- //
		boolean toRet = manager.takeOverAction(processCode, actionName, actor);
		
		log.exiting(log.getName(), "takeOverAction");
		return toRet;
	}
			
	@WebMethod
	@Override
	public boolean completeAction(String processCode, String actionStatusName, String nextActionName)
			throws ActionAlreadyFinished_Exception, UnknownActionName, UnknownNextActionName {
		log.entering(log.getName(), "completeAction");
		
		// ---	This method is not required by the specifications.	--- //
		boolean toRet = manager.completeAction(actionStatusName, nextActionName);
		
		log.exiting(log.getName(), "completeAction");
		return toRet;
	}

}