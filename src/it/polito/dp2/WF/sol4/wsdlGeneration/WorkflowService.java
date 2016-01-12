package it.polito.dp2.WF.sol4.wsdlGeneration;

import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

@WebService//(targetNamespace="http://lucamannella.altervista.org/WorkflowManager")
public interface WorkflowService {
	
	@WebMethod
	List<String> getWorkflowNames();
	
	@WebMethod
	Map<String, WorkflowReader> getWorkflows(
			@WebParam(name="workflowNames") List<String> workflowNames	)
		throws UnknownWorkflow;
	
	@WebMethod
	List<ProcessReader> getProcesses(
			@WebParam(name="workflowNames") List<String> workflowNames	)
		throws UnknownWorkflow;
	
}