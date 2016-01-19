package it.polito.dp2.WF.sol4.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ErrorMessage;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;

@WebService(name = "WorkflowInfoInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowInfoPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface")
@XmlSeeAlso({ObjectFactory.class})
//@HandlerChain(file = "META-INF/handler-chain.xml")
public class WorkflowInformation implements WorkflowInfoInterface {
	
	private WorkflowDataManager manager;
	private static Logger log = Logger.getLogger(WorkflowInformation.class.getName());

	public WorkflowInformation(WorkflowDataManager wfManager) {	// TODO Auto-generated constructor stub
		
		this.manager = wfManager;
	}

	@WebMethod
	@Override
	public void getWorkflowNames(Holder<XMLGregorianCalendar> lastModTime, Holder<List<String>> names) {	//TODO: Check me
		log.entering(log.getName(), "getWorkflowNames");
		
		lastModTime.value = manager.getLastWorkflowsUpdate();
		names.value = manager.getWorkflowNames();
		
		log.exiting(log.getName(), "getWorkflowNames", lastModTime.value.toString());
	}

	
	@WebMethod
	@Override
	public void getWorkflows(List<String> wfNames, Holder<XMLGregorianCalendar> lastModTime, Holder<List<Workflow>> workflows) 
			throws UnknownNames_Exception {		//TODO: Check me
		log.entering(log.getName(), "getWorkflows", wfNames.toString());
		
		lastModTime.value = manager.getLastWorkflowsUpdate();
		workflows.value = manager.getWorkflows(wfNames);
		
		log.exiting(log.getName(), "getWorkflows", lastModTime.value.toString());
	}

	@WebMethod
	@Override
	public void getProcesses(List<String> wfNames,						//input parameters
			Holder<XMLGregorianCalendar> lastModTime, Holder<List<Process>> processes, Holder<List<Workflow>> workflows)	//output parameters
			throws UnknownNames_Exception {								//fault message
		
		log.entering(log.getName(), "getProcesses", wfNames.toString());
		
		lastModTime.value = manager.getLastProcessesUpdate();
		
		workflows.value = manager.getWorkflows(wfNames);
		processes.value = manager.getProcesses(wfNames);
		
		log.exiting(log.getName(), "getProcesses", lastModTime.value.toString());
	}

	@WebMethod
	@Override
	public List<ActionStatusType> getActions(String psCode) throws UnknownCode {
		// ---	This method is not required by the specifications.	--- //
		log.entering(log.getName(), "getActions", psCode);
		
		Process p = manager.getProcess(psCode);
		if(p==null) {
			String message = "The code \""+psCode+"\" is wrong written or does not exists in the structure.";
						
			ErrorMessage fault = new ErrorMessage();
			fault.setMessage(message);
			throw new UnknownCode(message, fault);
		}
		
		log.exiting(log.getName(), "getActions", p.getActionStatus().toString());
		return p.getActionStatus();
	}

}
