package it.polito.dp2.WF.sol4.wsdlGeneration;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import it.polito.dp2.WF.ProcessReader;

@WebService(targetNamespace="http://lucamannella.altervista.org/WorkflowManager")
public interface ProcessService {
	
	List<ProcessReader> getProcesses(
			@WebParam(name="workflowNames") List<String> workflowNames	)
		throws UnknownWorkflow;
	
	
}