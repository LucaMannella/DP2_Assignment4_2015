package it.polito.dp2.WF.sol4.server;

import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.UnknownActionName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownActorName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNextActionName_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;
import it.polito.dp2.WF.sol4.gen.Process;

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
	
	public WorkflowController(WorkflowDataManager wfManager) {		//TODO Auto-generated constructor stub
		
		this.manager = wfManager;
		this.objFactory = new ObjectFactory();
	}

	@WebMethod
	@Override
	public boolean createNewProcess(String wfName) throws UnknownNames_Exception {
		log.entering(log.getName(), "createNewProcess");
		
		//TODO: Implement me.
		Workflow wf = manager.getWorkflow(wfName);
		Process ps = objFactory.createProcess();
		
		
		log.exiting(log.getName(), "createNewProcess");
		return false;
	}

	@WebMethod
	@Override
	public boolean takeOverAction(String psCode, Actor actor)
			throws UnknownActorName_Exception, UnknownCode_Exception {
		log.entering(log.getName(), "takeOverAction");
		
		// ---	This method is not required by the specifications.	--- //
		
		log.exiting(log.getName(), "takeOverAction");
		return false;
	}

	@WebMethod
	@Override
	public boolean completeAction(String actionStatusName, String nextActionName)
			throws UnknownActionName_Exception, UnknownNextActionName_Exception {
		log.entering(log.getName(), "completeAction");
		
		// ---	This method is not required by the specifications.	--- //
		
		log.exiting(log.getName(), "completeAction");
		return false;
	}

}