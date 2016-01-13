package it.polito.dp2.WF.sol4.server;

import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "WorkflowInfoInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowInfoPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface")
@XmlSeeAlso({ObjectFactory.class})
public class WorkflowInformation implements WorkflowInfoInterface {
	
	public WorkflowInformation(WorkflowDataManager wfManager) {
		// TODO Auto-generated constructor stub
	}

	@WebMethod
    @RequestWrapper(localName = "getWorkflowNames", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetWorkflowNames")
    @ResponseWrapper(localName = "getWorkflowNamesResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetWorkflowNamesResponse")
	@Override
	public void getWorkflowNames(Holder<XMLGregorianCalendar> lastModTime, Holder<List<String>> name) {
		
		// TODO Implement me

	}

	@WebMethod
    @RequestWrapper(localName = "getWorkflows", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetWorkflows")
    @ResponseWrapper(localName = "getWorkflowsResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetWorkflowsResponse")
	@Override
	public void getWorkflows(List<String> wfName, Holder<XMLGregorianCalendar> lastModTime, Holder<List<Workflow>> workflow) 
			throws UnknownNames_Exception {
		
		// TODO Implement me

	}

	@WebMethod
    @RequestWrapper(localName = "getProcesses", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetProcesses")
    @ResponseWrapper(localName = "getProcessesResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetProcessesResponse")
	@Override
	public void getProcesses(List<String> wfName, Holder<XMLGregorianCalendar> lastModTime,	Holder<List<Process>> process) 
			throws UnknownNames_Exception {
		
		// TODO Implement me

	}

	@WebMethod
    @WebResult(name = "returnValues", targetNamespace = "")
    @RequestWrapper(localName = "getActions", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetActions")
    @ResponseWrapper(localName = "getActionsResponse", targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/", className = "it.polito.dp2.WF.sol4.gen.GetActionsResponse")
	@Override
	public List<ActionStatusType> getActions(String psCode) throws UnknownCode_Exception {
		// ---	This method is not required by the specifications.	--- //
		return null;
	}

}
