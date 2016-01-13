package it.polito.dp2.WF.sol4.server;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.UnknownActionName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownActorName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNextActionName_Exception;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;

@WebService(name = "WorkflowControllerInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowControllerPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface")
@XmlSeeAlso({ObjectFactory.class})
public class WorkflowController implements WorkflowControllerInterface {
	
	public WorkflowController(WorkflowDataManager wfManager) {
		// TODO Auto-generated constructor stub
	}

	@WebMethod
    @WebResult(name = "result", targetNamespace = "")
    @RequestWrapper(localName = "createNewProcess", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.CreateNewProcess")
    @ResponseWrapper(localName = "createNewProcessResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.CreateNewProcessResponse")
	@Override
	public boolean createNewProcess(String wfName) throws UnknownNames_Exception {
		
		// TODO Implement me
		
		return false;
	}

	@WebMethod
    @WebResult(name = "result", targetNamespace = "")
    @RequestWrapper(localName = "takeOverAction", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.TakeOverAction")
    @ResponseWrapper(localName = "takeOverActionResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.TakeOverActionResponse")
	@Override
	public boolean takeOverAction(String psCode, Actor actor)
			throws UnknownActorName_Exception, UnknownCode_Exception {
		
		// ---	This method is not required by the specifications.	--- //
		return false;
	}

	@WebMethod
    @WebResult(name = "result", targetNamespace = "")
    @RequestWrapper(localName = "completeAction", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.CompleteAction")
    @ResponseWrapper(localName = "completeActionResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.CompleteActionResponse")
	@Override
	public boolean completeAction(String actionStatusName, String nextActionName)
			throws UnknownActionName_Exception, UnknownNextActionName_Exception {
		
		// ---	This method is not required by the specifications.	--- //
		return false;
	}

}
